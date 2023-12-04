package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "点赞实体请求对象", name = "EntityTypeIdLikeRo")
public class EntityTypeIdLikeRo {
    @Schema(description = "实体类型: 1-帖子(entity:post), 2-评论(entity:comment)", example = "1", name = "entityType")
    int entityType;

    @Schema(description = "实体ID", example = "102")
    int entityId;

    @Schema(description = "被赞的实体（帖子/评论）的作者ID", example = "102")
    int entityUserId;

    @Schema(description = "帖子的 id (点赞了哪个帖子，点赞的评论属于哪个帖子，点赞的回复属于哪个帖子)", example = "102")
    int postId;
}
