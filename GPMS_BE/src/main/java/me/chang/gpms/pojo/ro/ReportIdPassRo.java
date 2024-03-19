package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "报告ID、通过请求对象", name = "ReportIdPassRo")
public class ReportIdPassRo {
    @Schema(description = "报告id", example = "20", name = "id")
    private int id;
    @Schema(description = "报告通过状态 1-pass; 0-none; -1-fail", example = "0", name = "isPassed")
    private int isPassed;
}
