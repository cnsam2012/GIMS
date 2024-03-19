package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "报告ID、已读请求对象", name = "ReportIdReadRo")
public class ReportIdReadRo {
    @Schema(description = "报告id", example = "20", name = "id")
    private int id;
    @Schema(description = "报告阅读状态 1-read; 0-unread", example = "0", name = "isRead")
    private int isRead;
}
