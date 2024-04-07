package me.chang.gpms.ctrler.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.Message;
import me.chang.gpms.pojo.User;
import me.chang.gpms.pojo.ro.SendNoticeRo;
import me.chang.gpms.service.MessageService;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Tag(name = "NAC", description = "NoticeApiController")
@RestController
@Slf4j
public class NoticeApiController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping("api/notice/unread")
    @Operation(summary = "获取未读私信/系统通知的数量")
    public R getAllUnreadNotice() {
        User user = hostHolder.getUser();
        var data = new HashMap<String, Object>();
        if (user != null) {
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
            int noticeUnreadCount = messageService.findNoticeUnReadCount(user.getId(), null);
            data.put("allUnreadCount", letterUnreadCount + noticeUnreadCount);
            return R.ok(GPMSResponseCode.OK.value(), "未读信息", data);
        }
        return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "no_logging_user");
    }

    /**
     * 发送公告
     *
     * @param slnc
     * @return
     */
    @PostMapping("api/notice/send")
    @ResponseBody
    @Operation(description = "发送公告")
    public R sendNotice(
            @Parameter(required = true)
            @RequestBody
            SendNoticeRo slnc
    ) {
        var data = new HashMap<String, Object>();
        var userIdList = new ArrayList<Integer>();
        var content = slnc.getContent();
        AtomicInteger mesCount = new AtomicInteger();
        List<User> allUsers = userService.getAllUsers();
        allUsers.forEach(user -> {
            userIdList.add(user.getId());
        });
        // 向所有用户发送公告
        userIdList.forEach(id -> {
            Message message = new Message();
            message.setFromId(910000);
            message.setToId(id);
            if (message.getFromId() < message.getToId()) {
                message.setConversationId(message.getFromId() + "_" + message.getToId());
            } else {
                message.setConversationId(message.getToId() + "_" + message.getFromId());
            }
            message.setContent(content);
            message.setStatus(0); // 默认就是 0 未读，可不写
            message.setCreateTime(new Date());
            messageService.addMessage(message);
            mesCount.set(mesCount.get() + 1);
        });
        data.put("messageInserted", mesCount.get());
        return R.ok(GPMSResponseCode.OK.value(), "公告发布成功", data);
    }

    /**
     * 清空公告
     * @return
     */
    @PostMapping("api/notice/empty")
    @ResponseBody
    @Operation(description = "发送公告")
    public R emptyNotice(
    ) {
        var data = new HashMap<String, Object>();

        // 清空已发送的所有系统通知
        var mesCount = messageService.emptyMessageByFromUserId(910000);

        data.put("messageInserted", mesCount);
        return R.ok(GPMSResponseCode.OK.value(), "公告清空成功", data);
    }
}
