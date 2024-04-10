package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.Message;
import me.chang.gpms.pojo.Page;
import me.chang.gpms.pojo.User;
import me.chang.gpms.pojo.ro.PageWithConversationIdRo;
import me.chang.gpms.pojo.ro.SendLetter2NameContentRo;
import me.chang.gpms.service.MessageService;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.BbKafkaTopic;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * 私信/系统通知
 */
@RestController
@Tag(name = "MAC", description = "MessageApiController")
@Slf4j
public class MessageApiController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * 私信列表
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "api/letter/list", method = {RequestMethod.POST})
    @Operation(description = "私信列表")
    public R getLetterList(
            @Parameter(required = false)
            @RequestBody
            Page page
    ) {
        var data = new HashMap<String, Object>();
        // Integer.valueOf("abc"); // 测试统一异常处理（普通请求）

        // 获取当前登录用户信息
        User user = hostHolder.getUser();

        // 分页信息
        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }

        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCout(user.getId()));
        // 私信列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());
        page.setRows(conversationList.size());

        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                var fromUserRoleName = userService.findUserById(message.getFromId()).getRoleName();
                var toUserRoleName = userService.findUserById(message.getToId()).getRoleName();
                message.set_fromId(fromUserRoleName);
                message.set_toId(toUserRoleName);
                map.put("conversation", message); // 私信
                map.put("letterCount", messageService.findLetterCount(
                        message.getConversationId())); // 私信数量
                map.put("unreadCount", messageService.findLetterUnreadCount(
                        user.getId(), message.getConversationId())); // 未读私信数量
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                var userPut = userService.findUserById(targetId);
                userPut.setSalt("");
                userPut.setPassword("");
                map.put("target", userPut); // 私信对方
                conversations.add(map);
            }
        }

        data.put("conversations", conversations);

        // 查询当前用户的所有未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        data.put("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnReadCount(user.getId(), null);
        data.put("noticeUnreadCount", noticeUnreadCount);
        data.put("page", page);

//        return "/site/letter";
        return R.ok(GPMSResponseCode.OK.value(), "私信列表", data);
    }

    /**
     * 私信详情
     *
     * @param pageWithConversationId
     * @return
     */
    @PostMapping("api/letter/detail")
    @Operation(description = "私信详情")
    public R getLetterDetail(
            @RequestBody
            PageWithConversationIdRo pageWithConversationId
    ) {
        var data = new HashMap<String, Object>();
        var conversationId = pageWithConversationId.getConversationId();

        // 分页信息
        if (pageWithConversationId.getLimit() == 0 || ObjectUtil.isEmpty(pageWithConversationId.getLimit())) {
            pageWithConversationId.setLimit(5);
        }
        pageWithConversationId.setPath("/letter/detail/" + conversationId);
        pageWithConversationId.setRows(messageService.findLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, pageWithConversationId.getOffset(), pageWithConversationId.getLimit());

        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                var fromUserRoleName = userService.findUserById(message.getFromId()).getRoleName();
                var toUserRoleName = userService.findUserById(message.getToId()).getRoleName();
                message.set_fromId(fromUserRoleName);
                message.set_toId(toUserRoleName);
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                var userPut = userService.findUserById(message.getFromId());
                userPut.setSalt("");
                userPut.setPassword("");
                map.put("fromUser", userPut);
                letters.add(map);
            }
        }
        data.put("letters", letters);
        data.put("page", pageWithConversationId);

        // 私信目标
        data.put("target", getLetterTarget(conversationId));


        List<Message> letterListToRead = messageService.findLetters(conversationId);

        // 将私信列表中的未读消息改为已读
        List<Integer> ids = getUnreadLetterIds(letterListToRead);
        log.info("------------------ids = {}", ids.toString());

        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return R.ok(GPMSResponseCode.OK.value(), "私信详情", data);
    }

    /**
     * 获取私信对方对象
     *
     * @param conversationId
     */
    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    /**
     * 获取当前登录用户未读私信的 id
     *
     * @param letterList
     * @return
     */
    private List<Integer> getUnreadLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                // 当前用户是私信的接收者且该私信处于未读状态
                log.info("======================");
                log.info("id  {}", message.getId());
                log.info("host  {}", hostHolder.getUser().getId());
                log.info("messageToId  {}", message.getToId());
                log.info("messageStatus {}", message.getStatus());

                if ((hostHolder.getUser().getId().intValue() == message.getToId().intValue()) && (message.getStatus() == 0)) {
                    ids.add(message.getId());
                }

                log.info("======================");
            }
        }

        return ids;
    }

    /**
     * 发送私信
     *
     * @param slnc
     * @return
     */
    @PostMapping("api/letter/send")
    @ResponseBody
    @Operation(description = "发送私信")
    public R sendLetter(
            @Parameter(required = true)
            @RequestBody
            SendLetter2NameContentRo slnc
    ) {
        var toName = slnc.getToName();
        var content = slnc.getContent();

        // Integer.valueOf("abc"); // 测试统一异常处理（异步请求）
        User target = userService.findAUserByRoleName(toName);
        if (target == null) {
            try {
                target = userService.findUserById(Integer.parseInt(toName));
                if (target == null) {
                    return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "目标用户不存在");
                }
            } catch (Exception e) {
                log.warn(e.toString());
            }
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "目标用户不存在");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0); // 默认就是 0 未读，可不写
        message.setCreateTime(new Date());

        messageService.addMessage(message);

        return R.ok(GPMSResponseCode.OK.value(), "私信发送成功");
    }

    /**
     * 通知列表（只显示最新一条消息
     *
     * @return
     */
    @GetMapping("api/notice/list")
    @Operation(description = "通知列表（只显示最新一条消息）")
    @Deprecated
    public R getNoticeList() {
        var rdata = new HashMap<String, Object>();
        User user = hostHolder.getUser();
        // 查询评论类通知
        Message message = messageService.findLatestNotice(user.getId(), BbKafkaTopic.TOPIC_COMMNET.value());
        // 封装通知需要的各种数据
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            var userPut = userService.findUserById((Integer) data.get("userId"));
            userPut.setSalt("");
            userPut.setPassword("");
            messageVO.put("user", userPut);
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), BbKafkaTopic.TOPIC_COMMNET.value());
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(user.getId(), BbKafkaTopic.TOPIC_COMMNET.value());
            messageVO.put("unread", unread);

            rdata.put("commentNotice", messageVO);
        }

        // 查询点赞类通知
        message = messageService.findLatestNotice(user.getId(), BbKafkaTopic.TOPIC_LIKE.value());
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            var userPut = userService.findUserById((Integer) data.get("userId"));
            userPut.setSalt("");
            userPut.setPassword("");
            messageVO.put("user", userPut);
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), BbKafkaTopic.TOPIC_LIKE.value());
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(user.getId(), BbKafkaTopic.TOPIC_LIKE.value());
            messageVO.put("unread", unread);

            rdata.put("likeNotice", messageVO);
        }

        // 查询关注类通知
        message = messageService.findLatestNotice(user.getId(), BbKafkaTopic.TOPIC_FOLLOW.value());
        if (message != null) {
            Map<String, Object> messageVO = new HashMap<>();

            messageVO.put("message", message);

            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            var userPut = userService.findUserById((Integer) data.get("userId"));
            userPut.setSalt("");
            userPut.setPassword("");
            messageVO.put("user", userPut);
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));

            int count = messageService.findNoticeCount(user.getId(), BbKafkaTopic.TOPIC_FOLLOW.value());
            messageVO.put("count", count);

            int unread = messageService.findNoticeUnReadCount(user.getId(), BbKafkaTopic.TOPIC_FOLLOW.value());
            messageVO.put("unread", unread);

            rdata.put("followNotice", messageVO);
        }

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        rdata.put("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnReadCount(user.getId(), null);
        rdata.put("noticeUnreadCount", noticeUnreadCount);

//        return "/site/notice";
        return R.ok(GPMSResponseCode.OK.value(), "通知列表", rdata);
    }

    /**
     * 查询某个主题（关注follow/赞like/评论comment）所包含的通知列表
     *
     * @param topic
     * @param page
     * @return
     */
    @GetMapping("api/notice/detail/{topic}")
    @Operation(description = "查询某个主题（关注follow/赞like/评论comment）所包含的通知列表")
    @Deprecated
    public R getNoticeDetail(@PathVariable("topic") String topic, Page page) {
        var rdata = new HashMap<String, Object>();

        User user = hostHolder.getUser();

        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));

        List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                var userPut = userService.findUserById((Integer) data.get("userId"));
                userPut.setSalt("");
                userPut.setPassword("");
                map.put("user", userPut);
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 发送系统通知的作者
                var fromUserPut = userService.findUserById(notice.getFromId());
                userPut.setSalt("");
                userPut.setPassword("");
                map.put("fromUser", fromUserPut);

                noticeVoList.add(map);
            }
        }

        rdata.put("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = getUnreadLetterIds(noticeList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

//        return "/site/notice-detail";
        return R.ok(GPMSResponseCode.OK.value(), topic + "通知列表", rdata);
    }
}
