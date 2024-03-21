package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chang.gpms.pojo.ro.*;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.apache.http.HttpStatus;
import me.chang.gpms.event.EventProducer;
import me.chang.gpms.pojo.*;
import me.chang.gpms.service.CommentService;
import me.chang.gpms.service.ReportService;
import me.chang.gpms.service.LikeService;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.GPMSUtil;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.RedisKeyUtil;
import me.chang.gpms.util.constant.BbEntityType;
import me.chang.gpms.util.constant.BbKafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * 报告
 */
@RestController
@Tag(description = "Report Api Controller", name = "RAC")
@RequestMapping("api/report")
@Slf4j
public class ReportApiController {

    @Autowired
    private ReportService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    // 网站域名
    @Value("${community.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    // editorMd 图片上传地址
    @Value("${community.path.editormdUploadPath}")
    private String editormdUploadPath;

//    /**
//     * 进入帖子发布页
//     *
//     * @return
//     */
//    @GetMapping("/publish")
//    @ApiOperation("进入讨论发布页")
//    public String getPublishPage() {
//        return "/site/discuss-publish";
//    }

    /**
     * markdown 图片上传
     * TODO: 未完善！
     *
     * @param file
     * @return
     */
    @PostMapping("uploadMdPic")
    @Operation(description = "未完善！图片上传 (auth)")
    public String uploadMdPic(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file) {

        String url = null; // 图片访问地址
        try {
            // 获取上传文件的名称
            String trueFileName = file.getOriginalFilename();
            String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
            String fileName = GPMSUtil.generateUUID() + suffix;

            // 图片存储路径
            File dest = new File(editormdUploadPath + "/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            // 保存图片到存储路径
            file.transferTo(dest);

            // 图片访问地址
            url = domain + contextPath + "/editor-md-upload/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return GPMSUtil.getEditorMdJSONString(500, "上传失败", url);
        }

        return GPMSUtil.getEditorMdJSONString(201, "上传成功", url);
    }

    /**
     * 添加帖子
     *
     * @param dpReceive
     * @return
     */
    @Operation(description = "添加讨论（发布通知） (auth)")
    @RequestMapping(method = RequestMethod.POST, path = "add")
    public R addReport(
            @Parameter(required = true)
            @RequestBody
            ReportTitleContentRo dpReceive,
            HttpServletResponse resp
    ) {
        var title = dpReceive.getTitle();
        var content = dpReceive.getContent();


        User user = hostHolder.getUser();
        if (user == null) {
            var status = HttpStatus.SC_UNAUTHORIZED;
            resp.setStatus(status);
//            return BbUtil.getJSONString(status, "您还未登录");
            return R.error(status, "您尚未登录");
        }

        Report discussPost = new Report();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostService.addReport(discussPost);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
                .setUserId(user.getId())
                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                .setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, discussPost.getId());

        var status = HttpStatus.SC_CREATED;
        resp.setStatus(status);
//        return BbUtil.getJSONString(status, "discussPost_added");
        return R.ok(status, "讨论添加成功");

    }

    /**
     * 获取所有报告
     *
     * @param page
     * @return
     */
    @PostMapping("detail/all")
    @Operation(description = "获取所有讨论")
    // TODO
    public R getAllReport(
//            @RequestParam(name = "orderMode", defaultValue = "0")
//            @Parameter(name = "orderMode", description = "默认是 0--按时间排序，可选 1--按分值排序", example = "0")
//            int orderMode,
//            @Parameter(required = false)
//            Page page,
            @RequestBody
            @Parameter(required = true)
            PageAndOrderModeRo page
    ) {

        Map<String, Object> data = new HashMap<>();
        var orderMode = page.getOrderMode();
        // 获取总页数
        page.setRows(discussPostService.findReportRows(0));
        page.setPath("/index?orderMode=" + orderMode);
        // 分页查询
        List<Report> list = discussPostService.findReports(0, page.getOffset(), page.getLimit(), orderMode);
        // 封装帖子和该帖子对应的用户信息
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (Report post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("reports", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
//                long likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_POST.value(), post.getId());
//                map.put("likeCount", likeCount);
                var userId = post.getLastedEditUserId();
                User lEuser = userService.findUserById(userId);
                map.put("latestEditUserName", lEuser.getUsername());
                map.put("latestEditRoleName", lEuser.getRoleName());
                discussPosts.add(map);
            }
        }
        data.put("reports", discussPosts);
        data.put("orderMode", orderMode);
        data.put("page", page);
        var status = HttpStatus.SC_OK;
//        return BbUtil.getJSONString(status, "success", data);
        val ok = GPMSResponseCode.OK.value();
        return R.ok(ok, "所有报告", data);
    }

    /**
     * 进入特定讨论详情页
     *
     * @param discussPostId 单条讨论的ID
     * @param page          分页信息
     * @return
     */
    @GetMapping("detail/{discussPostId}")
    @Operation(description = "进入特定讨论详情页")
    public R getReport(
            @PathVariable("discussPostId")
            @Parameter(name = "discussPostId", description = "单条讨论的ID", example = "3")
            int discussPostId,
            @Parameter(required = false)
            Page page
    ) {
        Map<String, Object> data = new HashMap<>();

        // 帖子
        Report discussPost = discussPostService.findReportById(discussPostId);
        String content = HtmlUtils.htmlUnescape(discussPost.getContent()); // 内容反转义，不然 markDown 格式无法显示
        discussPost.setContent(content);
        data.put("post", discussPost);

        // 作者
        User user = userService.findUserById(discussPost.getUserId());
        data.put("user", user);

        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_POST.value(), discussPostId);
        data.put("likeCount", likeCount);

        // 当前登录用户的点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_POST.value(), discussPostId);
        data.put("likeStatus", likeStatus);

        // 评论分页信息
        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        // 帖子的评论列表
        List<Comment> commentList = commentService.findCommentByEntity(
                BbEntityType.ENTITY_TYPE_POST.value(), discussPost.getId(), page.getOffset(), page.getLimit());

        // 封装评论及其相关信息
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 存储对帖子的评论
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment); // 评论
                commentVo.put("user", userService.findUserById(comment.getUserId())); // 发布评论的作者
                likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId()); // 该评论点赞数量
                commentVo.put("likeCount", likeCount);
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                        hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId()); // 当前登录用户对该评论的点赞状态
                commentVo.put("likeStatus", likeStatus);


                // 存储每个评论对应的回复（不做分页）
                List<Comment> replyList = commentService.findCommentByEntity(
                        BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>(); // 封装对评论的评论和评论的作者信息
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply); // 回复
                        replyVo.put("user", userService.findUserById(reply.getUserId())); // 发布该回复的作者
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target); // 该回复的目标用户
                        likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_COMMENT.value(), reply.getId());
                        replyVo.put("likeCount", likeCount); // 该回复的点赞数量
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                                hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_COMMENT.value(), reply.getId());
                        replyVo.put("likeStatus", likeStatus); // 当前登录用户的点赞状态

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 每个评论对应的回复数量
                int replyCount = commentService.findCommentCount(BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        data.put("comments", commentVoList);
        data.put("page", page);

        var status = HttpStatus.SC_OK;
//        return BbUtil.getJSONString(status, "success", data);
        return R.ok(status, "特定讨论", data);
    }


    /**
     * 修改报告类型
     *
     * @param idAndType
     * @param resp
     * @return
     */
    @PutMapping("type")
    @Schema(description = "修改报告类型：1-周记; 2-月记; 3-总结", name = "updateTop")
    public R updateType(
            @Parameter(required = true)
            @RequestBody
            ReportIdTypeRo idAndType,
            HttpServletResponse resp
    ) {
        if (
                ObjectUtil.isEmpty(idAndType) ||
                        idAndType.getId() == 0
        ) {
            var status = HttpStatus.SC_EXPECTATION_FAILED;
            resp.setStatus(status);
//            return BbUtil.getJSONString(status, "参数不齐");
            return R.error(status, "参数不齐");
        }
        var id = idAndType.getId();
        var type = idAndType.getType();

        var res = discussPostService.updateType(id, type);
        if (res == -1) {
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(), "非法报告类型：1-周记; 2-月记; 3-总结"
            );
        }

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
//        Event event = new Event()
//                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
//                .setEntityId(id);
//        eventProducer.fireEvent(event);

        var status = HttpStatus.SC_OK;
        resp.setStatus(status);
//        return BbUtil.getJSONString(status, "状态设置成功");
        return R.ok(status, "类型设置成功");
    }


    /**
     * 加精讨论
     *
     * @param dpId
     * @return
     */
    @PutMapping("wonderful")
    @Operation(description = "加精讨论 (auth master-2 only)")
    public R setWonderful(@Parameter(required = true)
                          @RequestBody
                          ReportIdRo dpId,
                          HttpServletResponse resp) {
        if (dpId == null) {
            var status = HttpStatus.SC_EXPECTATION_FAILED;
            resp.setStatus(status);
//            return BbUtil.getJSONString(status, "参数不齐");
            return R.error(status, "参数不齐");
        }
        var id = dpId.getId();

        discussPostService.updateStatus(id, 1);


        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
//        Event event = new Event()
//                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
//                .setEntityId(id);
//        eventProducer.fireEvent(event);


        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        var status = HttpStatus.SC_OK;
        resp.setStatus(status);
//        return BbUtil.getJSONString(status, "加精成功");
        return R.ok(status, "加精成功");
    }

    @PutMapping("pass")
    @Operation(description = "设置通过")
    public R setPass(@Parameter(required = true)
                     @RequestBody
                     ReportIdPassRo dpId) {
        if (dpId == null) {
            var status = GPMSResponseCode.CLIENT_ERROR.value();
            return R.error(status, "参数不齐");

        }
        var id = dpId.getId();
        var res = discussPostService.updatePass(id, dpId.getIsPassed());
        if (res == -1) {
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(), "非法通过状态：状态仅为-1、0、1"
            );
        }
        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
//        Event event = new Event()
//                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
//                .setEntityId(id);
//        eventProducer.fireEvent(event);
        // 计算帖子分数
//        String redisKey = RedisKeyUtil.getPostScoreKey();
//        redisTemplate.opsForSet().add(redisKey, id);
        var status = GPMSResponseCode.OK.value();
//        return BbUtil.getJSONString(status, "加精成功");
        return R.ok(status, "设置通过状态成功");
    }

    @PutMapping("read")
    @Operation(description = "设置已读状态")
    public R setRead(@Parameter(required = true)
                     @RequestBody
                     ReportIdReadRo rIr) {
        if (rIr == null) {
            var status = GPMSResponseCode.CLIENT_ERROR.value();
            return R.error(status, "参数不齐");

        }
        var id = rIr.getId();
        var res = discussPostService.updateRead(id, rIr.getIsRead());
        if (res == -1) {
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(), "非法已读状态：状态仅为 1-read; 0-unread"
            );
        }
        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
//        Event event = new Event()
//                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
//                .setEntityId(id);
//        eventProducer.fireEvent(event);
        // 计算帖子分数
//        String redisKey = RedisKeyUtil.getPostScoreKey();
//        redisTemplate.opsForSet().add(redisKey, id);
        var status = GPMSResponseCode.OK.value();
//        return BbUtil.getJSONString(status, "加精成功");
        return R.ok(status, "设置通过状态成功");
    }


    /**
     * 删除讨论
     *
     * @param discussPostId
     * @param resp
     * @return
     */
    @DeleteMapping("delete")
    @Operation(description = "删除讨论 (auth admin-1 only)")
    public R setDelete(@Parameter(required = true)
                       @RequestBody
                       ReportIdRo discussPostId, HttpServletResponse resp) {
        if (discussPostId == null) {
            var status = HttpStatus.SC_EXPECTATION_FAILED;
            resp.setStatus(status);
//            return BbUtil.getJSONString(status, "参数不齐");
            return R.error(status, "参数不齐");
        }

        var id = discussPostId.getId();

        discussPostService.updateStatus(id, 2);
        discussPostService.deleteReport(id);

        // 触发删帖事件，通过消息队列更新 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_DELETE.value())
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                .setEntityId(id);
        eventProducer.fireEvent(event);

        var status = HttpStatus.SC_OK;
        resp.setStatus(status);
//        return BbUtil.getJSONString(status, "删除成功！");
        return R.ok(status, "删除成功");
    }


}
