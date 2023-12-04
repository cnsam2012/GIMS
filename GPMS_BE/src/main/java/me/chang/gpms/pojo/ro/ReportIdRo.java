package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "讨论ID请求对象", name = "DiscussPostIdRo")
@Data
public class ReportIdRo {
    @Schema(description = "帖子的id", example = "20", name = "id")
    private int id;
}
