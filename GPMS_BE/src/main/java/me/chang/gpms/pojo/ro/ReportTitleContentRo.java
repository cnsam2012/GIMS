package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 讨论贴
 *
 */
@Data
@Schema(description = "讨论标题、内容请求对象", name = "DiscussPostTitleContentRo")
public class ReportTitleContentRo {
    @Schema(description = "标题，非空", example = "not_null_title", name = "title")
    private String title;
    @Schema(description = "内容", example = "content", name = "content")
    private String content;
}
