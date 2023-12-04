package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "实体请求对象", name = "EntityTypeIdRo")
public class EntityTypeIdRo {
    @Schema(description = "实体类型: 1-帖子(entity:post), 2-评论(entity:comment), 3-用户(entity:user)", example = "3", name = "entityType")
    int entityType;

    @Schema(description = "实体ID", example = "2", name = "entityId")
    int entityId;
}
