package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.Report;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.apache.commons.lang3.StringUtils;
import me.chang.gpms.pojo.Comment;
import me.chang.gpms.pojo.Page;
import me.chang.gpms.pojo.User;
import me.chang.gpms.pojo.ro.FileNameRo;
import me.chang.gpms.pojo.ro.OldNewPasswordRo;
import me.chang.gpms.pojo.ro.UsernameRo;
import me.chang.gpms.service.*;
import me.chang.gpms.util.GPMSUtil;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.BbEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "UAC", description = "UserApiController")
public class UserApiController {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CommentService commentService;

    // 网站域名
    @Value("${community.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;

    /**
     * 账号设置信息
     *
     * @param resp
     * @return
     */
    @GetMapping("setting")
    @Operation(summary = "账号设置信息")
    public R getSettingPage(
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();

        // 生成上传文件的名称
        String fileName = GPMSUtil.generateUUID();
        data.put("fileName", fileName);

        // 设置响应信息(qiniu 的规定写法)
        StringMap policy = new StringMap();
        policy.put("returnBody", GPMSUtil.getJSONString(0));

        // 生成上传到 qiniu 的凭证(qiniu 的规定写法)
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);
        data.put("uploadToken", uploadToken);

//        return "/site/setting";
//        return R.ok(resp, "账号设置信息", data);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "account settings hints",
                data
        );
    }

    /**
     * 更新图像路径（将本地的图像路径更新为云服务器上的图像路径）
     *
     * @param fnr
     * @param resp
     * @return
     */
    @PutMapping("header/url")
    @Operation(summary = "还没好-更新图像路径（将本地的图像路径更新为云服务器上的图像路径）")
    public R updateHeaderUrl(
            @RequestBody
            FileNameRo fnr,
            HttpServletResponse resp
    ) {
        var fileName = fnr.getFileName();
        if (StringUtils.isBlank(fileName)) {
//            return GPMSUtil.getJSONString(1, "文件名不能为空");
//            return R.error(resp, "文件名不能为空");
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(),
                    "文件名不能为空"
            );
        }

        // 文件在云服务器上的的访问路径
        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeader(hostHolder.getUser().getId(), url);

//        return GPMSUtil.getJSONString(0);
//        return R.ok(resp, "更新完成");
        return R.ok(
                GPMSResponseCode.OK.value(),
                "更新完成"
        );
    }

    /**
     * 修改用户密码
     *
     * @param onpr
     * @param resp
     * @return
     */
    @PutMapping("password")
    @Operation(summary = "修改用户密码")
    public R updatePassword(
            @RequestBody
            @Parameter(required = true)
            OldNewPasswordRo onpr,
            HttpServletResponse resp
    ) {
        var oldPassword = onpr.getOldPwd();
        var newPassword = onpr.getNewPwd();
        var data = new HashMap<String, Object>();

        // 验证原密码是否正确
        User user = hostHolder.getUser();
        String md5OldPassword = GPMSUtil.md5(oldPassword + user.getSalt());

        // 注释此段暂时取消判断原密码、验证密码
        if (!user.getPassword().equals(md5OldPassword)) {
            data.put("oldPasswordError", "原密码错误");
//            return R.error(resp, "非法密码", data);
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(),
                    "非法密码",
                    data
            );
        }

        // 判断新密码是否合法
        String md5NewPassword = GPMSUtil.md5(newPassword + user.getSalt());
        if (user.getPassword().equals(md5NewPassword)) {
            data.put("newPasswordError", "新密码和原密码相同");
//            return R.error(resp, "非法密码", data);
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(),
                    "非法密码",
                    data
            );

        }

        // 修改用户密码
        userService.updatePassword(user.getId(), newPassword);

//        return R.ok(resp, "修改成功");
        return R.ok(
                GPMSResponseCode.OK.value(),
                "修改成功"
        );
    }

    /**
     * 修改用户名
     *
     * @param ur
     * @param resp
     * @return
     */
    @PutMapping("username")
    @Operation(summary = "修改用户名")
    public R updateUsername(
            @RequestBody
            @Parameter(required = true)
            UsernameRo ur,
            HttpServletResponse resp
    ) {
        User user = hostHolder.getUser();
        var oldName = user.getUsername();
        var newName = ur.getUsername();
        var data = new HashMap<String, Object>();

        // 验证新旧用户名
        if (StringUtils.equals(oldName, newName)) {
            data.put("newUsernameError", "新旧用户名相同");
//            return R.error(resp, "非法用户名", data);
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(),
                    "新旧用户名相同"
            );
        }

        // 验证新用户名是否存在
        var checkUser = userService.findUserByName(newName);
        if (ObjectUtil.isNotEmpty(checkUser)) {
//            data.put("newUsernameError", "用户名已存在，请重新设置新用户名");
//            return R.error(resp, "非法用户名", data);
            return R.error(
                    GPMSResponseCode.CLIENT_ERROR.value(),
                    "非法用户名：用户名已存在，请重新设置新用户名"
            );
        }

        // 修改
        userService.updateUsername(user.getId(), newName);

//        return R.ok(resp, "修改成功");
        return R.ok(
                GPMSResponseCode.OK.value(),
                "修改成功"
        );
    }

    /**
     * 个人主页（个人数据）
     *
     * @param userId
     * @param resp
     * @return
     */
    @GetMapping("profile/{userId}")
    @Operation(summary = "个人主页（个人数据）")
    public R getProfilePage(
            @PathVariable("userId") int userId,
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }

        // 用户
        data.put("user", user);
        // 获赞数量
        int userLikeCount = likeService.findUserLikeCount(userId);
        data.put("userLikeCount", userLikeCount);
        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, BbEntityType.ENTITY_TYPE_USER.value());
        data.put("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(BbEntityType.ENTITY_TYPE_USER.value(), userId);
        data.put("followerCount", followerCount);
        // 当前登录用户是否已关注该用户
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_USER.value(), userId);
        }
        data.put("hasFollowed", hasFollowed);
        data.put("tab", "profile"); // 该字段用于指示标签栏高亮

//        return R.ok(resp, "个人资料/个人数据", data);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "个人资料/个人数据",
                data
        );
    }

    /**
     * 进入我的帖子（查询某个用户的帖子列表）
     *
     * @param userId
     * @param page
     * @param resp
     * @return
     */
    @GetMapping("discuss/{userId}")
    @Operation(summary = "进入我的帖子（查询某个用户的帖子列表）")
    public R getMyDiscussPosts(
            @PathVariable("userId") int userId,
            Page page,
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        data.put("user", user);

        // 该用户的帖子总数
//        int rows = reportService.findDiscussPostRows(userId);
        int rows = reportService.findReportRows(userId);
        data.put("rows", rows);

        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }
        page.setPath("/user/discuss/" + userId);
        page.setRows(rows);

        // 分页查询(按照最新查询)
        List<Report> list = reportService.findReports(userId, page.getOffset(), page.getLimit(), 0);
        // 封装帖子和该帖子对应的用户信息
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (Report post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                long likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_POST.value(), post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        data.put("discussPosts", discussPosts);
        data.put("tab", "mypost"); // 该字段用于指示标签栏高亮

//        return R.ok(resp, "个人帖子列表", data);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "个人帖子列表",
                data
        );
    }

    /**
     * 进入我的评论/回复（查询某个用户的评论/回复列表）
     *
     * @param userId
     * @param page
     * @param resp
     * @return
     */
    @GetMapping("comment/{userId}")
    @Operation(summary = "个人评论/回复列表")
    public R getMyComments(
            @PathVariable("userId") int userId,
            Page page,
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        data.put("user", user);

        // 该用户的评论/回复总数
        int commentCounts = commentService.findCommentCountByUserId(userId);
        data.put("commentCounts", commentCounts);

        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }
        page.setPath("/user/comment/" + userId);
        page.setRows(commentCounts);

        // 分页查询
        List<Comment> list = commentService.findCommentByUserId(userId, page.getOffset(), page.getLimit());
        // 封装评论和该评论对应的帖子信息
        List<Map<String, Object>> comments = new ArrayList<>();
        if (list != null) {
            for (Comment comment : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                // 显示评论/回复对应的文章信息
                if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_POST.value()) {
                    // 如果是对帖子的评论，则直接查询 target_id 即可
                    Report post = reportService.findReportById(comment.getEntityId());
                    map.put("post", post);
                } else if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_COMMENT.value()) {
                    // 如过是对评论的回复，则先根据该回复的 target_id 查询评论的 id, 再根据该评论的 target_id 查询帖子的 id
                    Comment targetComment = commentService.findCommentById(comment.getEntityId());
                    Report post = reportService.findReportById(targetComment.getEntityId());
                    map.put("post", post);
                }

                comments.add(map);
            }
        }
        data.put("comments", comments);
        data.put("tab", "myreply"); // 该字段用于指示标签栏高亮

//        return R.ok(resp, "个人评论/回复列表", data);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "个人评论/回复列表",
                data
        );
    }

}
