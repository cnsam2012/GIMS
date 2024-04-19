package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chang.gpms.pojo.ro.*;
import me.chang.gpms.service.*;
import me.chang.gpms.util.constant.GPMSResponseCode;
import me.chang.gpms.util.constant.GPMSUserAuth;
import org.apache.http.HttpStatus;
import me.chang.gpms.event.EventProducer;
import me.chang.gpms.pojo.*;
import me.chang.gpms.util.GPMSUtil;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.RedisKeyUtil;
import me.chang.gpms.util.constant.BbEntityType;
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
    private ReportService reportService;

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

    @Autowired
    private MessageService messageService;

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
    @Operation(description = "添加报告 (auth)")
    @RequestMapping(method = RequestMethod.POST, path = "add")
    public R addReport(
            @Parameter(required = true)
            @RequestBody
            ReportTitleContentTypeRo dpReceive
    ) {
        var data = new HashMap<String, Object>();
        var title = dpReceive.getTitle();
        var content = dpReceive.getContent();
        var type = dpReceive.getType();
        User user = hostHolder.getUser();
        if (user == null) {
            var status = GPMSResponseCode.OK.value();
            return R.error(status, "您尚未登录");
        }
        Report report = new Report();
        report.setUserId(user.getId());
        report.setTitle(title);
        report.setContent(content);
        report.setType(type);
        report.setCreateTime(new Date());
        report.setIsDraft(dpReceive.getIsDraft());
        report.setLastedEditUserId(user.getId());
        var count = reportService.addReport(report);
        data.put("count", count);
        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
//        Event event = new Event()
//                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
//                .setUserId(user.getId())
//                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
//                .setEntityId(discussPost.getId());
//        eventProducer.fireEvent(event);
//        // 计算帖子分数
//        String redisKey = RedisKeyUtil.getPostScoreKey();
//        redisTemplate.opsForSet().add(redisKey, report.getId());
        var status = GPMSResponseCode.OK.value();
        return R.ok(status, "报告上传成功", data);
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
        User currentUser = hostHolder.getUser();
        if (ObjectUtil.isNull(currentUser) || ObjectUtil.isEmpty(currentUser)) {
            return R.ok(GPMSResponseCode.CLIENT_ERROR.value(), "用户未登录");
        }
        int userType = currentUser.getType();
        List<Report> list = null;


        Map<String, Object> data = new HashMap<>();
        var orderMode = page.getOrderMode();
        // 获取总页数
        // 分页查询

        // 根据已登录的用户类型返回特定的report
        switch (userType) {
            case 9 -> { // 管理员
                log.info("got admin list");
                list = reportService.findReports(0, page.getCurrent(), page.getLimit(), orderMode); // 返回所有
                page.setRows(reportService.findReportRows(0));
            }
            case 1 -> { // 学生
                log.info("student list");
                list = reportService.findReports(currentUser.getId(), page.getCurrent(), page.getLimit(), orderMode); // 返回自己的报告
                page.setRows(reportService.findReportRows(currentUser.getId()));
            }
            case 2 -> {  // 老师
                log.info("got tutor's student's report list");
                var tutorSId = currentUser.getId();
                // get all this tutor's student
                list = reportService.findTutorSStudentSReport(tutorSId, page.getOffset(), page.getLimit(), orderMode);
                page.setRows(reportService.findTutorSStudentSReportRows(tutorSId));

            }
            case 3 -> { // 企业
                log.info("got com's dep list");
                var comUserSId = currentUser.getId();
                // get all this com's student
                list = reportService.findDepartmentSStudentSReport(comUserSId, page.getOffset(), page.getLimit(), orderMode);
                page.setRows(reportService.findDepartmentSStudentSReportRow(comUserSId));
            }
            default -> { // 默认
                log.error("no usertype got!");
            }
        }

        // 管理员 所有report
        // 教师 所管理学生的report 部门下的report
        // 学生 自己的report


//        List<Report> list = reportService.findReports(0, page.getCurrent(), page.getLimit(), orderMode);


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

                User lEuser = new User();
                try {
                    lEuser = userService.findUserById(userId);
                } catch (Exception e) {
                }
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
     * 特定报告详细内容
     *
     * @param discussPostId
     * @return
     */
    @GetMapping("detail/{discussPostId}")
    @Operation(description = "特定报告详情")
    public R getReport(
            @PathVariable("discussPostId")
            @Parameter(name = "discussPostId", description = "单条讨论的ID", example = "110008")
            int discussPostId
//            ,
//            @Parameter(required = false)
//            Page page
    ) {
        Map<String, Object> data = new HashMap<>();

        // 帖子
        Report report = reportService.findReportById(discussPostId);
        if (ObjectUtil.isEmpty(report) || ObjectUtil.isNull(report)) {
            data.put("reportMsg", "查无报告");
            return R.error(GPMSResponseCode.OK.value(), "查无报告", data);
        }
        String content = HtmlUtils.htmlUnescape(report.getContent()); // 内容反转义，不然 markDown 格式无法显示
        report.setContent(content);
        data.put("report", report);

        // 作者
        User user = userService.findUserById(report.getUserId());
        data.put("user", user);


        return R.ok(GPMSResponseCode.OK.value(), "特定报告", data);
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

        var res = reportService.updateType(id, type);
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

        reportService.updateStatus(id, 1);


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
        var res = reportService.updatePass(id, dpId.getIsPassed());
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
        var res = reportService.updateRead(id, rIr.getIsRead());
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


    @PutMapping("mark")
    @Operation(description = "设置评分并更改已读状态")
    public R setScore(@Parameter(required = true)
                      @RequestBody
                      ReportIdMarkRo rIr) {
        if (rIr == null) {
            var status = GPMSResponseCode.CLIENT_ERROR.value();
            return R.error(status, "参数不齐");
        }

        User currentUser = hostHolder.getUser();
        if (ObjectUtil.isEmpty(currentUser) || ObjectUtil.isNull(currentUser)) {
            var status = GPMSResponseCode.CLIENT_ERROR.value();
            return R.error(status, "未登录");
        }

        var id = rIr.getId();
        var report = reportService.findReportById(id);
        if (ObjectUtil.isNull(report) || ObjectUtil.isEmpty(report)) {
            var status = GPMSResponseCode.CLIENT_ERROR.value();
            return R.error(status, "查无report");
        }
        var score = rIr.getScore();
        if (score == -1) {
            // 若score为-1 更新分数 同时置为未读
            // 给报告所有者发送信息
            reportService.updateRead(id, 0);
            reportService.updateScore(id, -1);
            var msgContent = "您好！您的报告《" + report.getTitle() + "》正被重新评价，请耐心等待新得分。";
            this.sendMessage(currentUser.getId(), report.getUserId(), msgContent);
        } else {
            // else 更新分数
            // 给报告所有者发送信息
            reportService.updateRead(id, 1);
            reportService.updateScore(id, Double.parseDouble(String.valueOf(score)));
            var msgContent = "您好！您的报告《" + report.getTitle() + "》已被评分！得分为：" + score + "。";
            this.sendMessage(currentUser.getId(), report.getUserId(), msgContent);
        }

        return R.ok(GPMSResponseCode.OK.value(), "评分成功");
    }


    /**
     * 删除
     *
     * @param discussPostId
     * @return
     */
    @DeleteMapping("delete")
    @Operation(description = "删除讨论 (auth admin-1 only)")
    public R setDelete(@Parameter(required = true)
                       @RequestBody
                       ReportIdRo discussPostId
    ) {
        if (discussPostId == null) {
            var status = GPMSResponseCode.CLIENT_ERROR.value();
//            return BbUtil.getJSONString(status, "参数不齐");
            return R.error(status, "参数不齐");
        }
        var id = discussPostId.getId();
//        discussPostService.updateStatus(id, 2);
        int i = reportService.deleteReport(id);
        if (i <= 0) {
            var status = GPMSResponseCode.CLIENT_ERROR.value();
            return R.ok(status, "找不到帖子，没有帖子被删除");
        }
        // 触发删帖事件，通过消息队列更新 Elasticsearch 服务器
//        Event event = new Event()
//                .setTopic(BbKafkaTopic.TOPIC_DELETE.value())
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
//                .setEntityId(id);
//        eventProducer.fireEvent(event);
        var status = GPMSResponseCode.OK.value();
//        return BbUtil.getJSONString(status, "删除成功！");
        return R.ok(status, "删除成功");
    }

    private void sendMessage(int fromId, int toId, String messageContent) {
        // 发送部门信息通知
        Message message = new Message();
        String toRoleName = userService.findUserById(toId).getRoleName();
        String fromRoleName = userService.findUserById(fromId).getRoleName();

        message.setFromId(fromId);
        message.set_fromId(fromRoleName);

        message.setToId(toId);
        message.set_toId(toRoleName);

        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setStatus(0); // 默认就是 0 未读，可不写
        message.setCreateTime(new Date());
        message.setContent(messageContent);
        messageService.addMessage(message);
    }


}
