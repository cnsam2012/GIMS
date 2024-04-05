package me.chang.gpms.ctrler.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chang.gpms.pojo.User;
import me.chang.gpms.service.MessageService;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@Tag(name = "NAC", description = "NoticeApiController")
@RestController
public class NoticeApiController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

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
}
