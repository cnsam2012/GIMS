package me.chang.gpms.ctrler.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.chang.gpms.event.EventProducer;
import me.chang.gpms.pojo.Comment;
import me.chang.gpms.pojo.Report;
import me.chang.gpms.service.CommentService;
import me.chang.gpms.service.ReportService;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.BbEntityType;
import me.chang.gpms.util.constant.BbKafkaTopic;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import me.chang.gpms.pojo.Event;

import java.util.Date;

/**
 * 评论/回复
 */
@RestController
@RequestMapping("api/comment")
@Tag(name = "CAC", description = "CommentApiController")
public class CommentApiController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EventProducer eventProducer;


    /**
     * 添加评论
     *
     * @param discussPostId
     * @param comment
     * @return
     */
    @PostMapping("add/{discussPostId}")
    @Operation(summary = "add comment")
    public R addComment(
            @PathVariable("discussPostId")
            int discussPostId,
            @Parameter(required = true)
            @RequestBody
            Comment comment
    ) {

        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件（系统通知）
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_COMMNET.value())
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);
        if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_POST.value()) {
            Report target = reportService.findReportById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_COMMENT.value()) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_POST.value()) {
            // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
            event = new Event()
                    .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
                    .setUserId(comment.getUserId())
                    .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }

        return R.ok(GPMSResponseCode.OK.value(), "评论成功");
    }

}
