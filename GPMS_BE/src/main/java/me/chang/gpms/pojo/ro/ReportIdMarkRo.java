package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "报告ID、得分请求对象", name = "ReportIdMarkRo")
public class ReportIdMarkRo {
    @Schema(description = "报告id", example = "110008", name = "id")
    private int id;
    @Schema(description = "报告得分 1~100; -1将同时置为未读", example = "60", name = "score")
    private int score;
}
