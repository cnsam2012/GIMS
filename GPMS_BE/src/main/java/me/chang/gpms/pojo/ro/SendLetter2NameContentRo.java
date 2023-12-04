package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "发送私信：目标与内容请求对象", name = "SendLetter2NameContentRo")
public class SendLetter2NameContentRo {
    @Schema(description = "私信目标", example = "admin", name = "toName")
    private String toName;

    @Schema(description = "私信内容", example = "这是一条发给用户admin的测试信息", name = "content")
    private String content;
}
