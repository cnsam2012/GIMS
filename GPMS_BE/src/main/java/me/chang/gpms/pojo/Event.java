package me.chang.gpms.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装事件（用于系统通知）
 */


@Data
@Accessors(chain = true)
@Schema(description = "封装事件（用于系统通知）", name = "Event")
public class Event {

    /**
     * 事件类型
     */
    @Schema(description = "事件类型", example = "comment", name = "topic")
    private String topic;

    /**
     * 事件由谁触发
     */
    @Schema(description = "事件由谁触发", example = "1", name = "userId")
    private int userId;

    /**
     * 实体类型
     * 帖子 = 1
     * 评论 = 2
     * 人 = 3
     */
    @Schema(description = "实体类型", example = "1", name = "entityType")
    private int entityType;

    /**
     * 实体 id
     */
    @Schema(description = "实体 id", name = "entityId")
    private int entityId;

    /**
     * 实体的作者（该通知发送给他）
     */
    @Schema(description = "实体的作者（该通知发送给他/该通知的目标/event's destination）", name = "entityUserId")
    private int entityUserId;

    /**
     * 存储未来可能需要用到的数据
     */
    @Schema(description = "存储未来可能需要用到的数据", name = "data")
    private Map<String, Object> data = new HashMap<>();

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
