package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "讨论ID、状态请求对象", name = "DiscussPostIdTypeRo")
public class ReportIdTypeRo {
    @Schema(description = "帖子的id", example = "20", name = "id")
    private int id;
    @Schema(description = "状态：0-普通，1-置顶", example = "0", name = "type")
    private int type;
}
