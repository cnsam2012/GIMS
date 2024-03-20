package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ID、报告类型请求对象", name = "DiscussPostIdTypeRo")
public class ReportIdTypeRo {
    @Schema(description = "报告的id", example = "20", name = "id")
    private int id;
    @Schema(description = "修改报告类型：1-周记; 2-月记; 3-总结", example = "0", name = "type")
    private int type;
}
