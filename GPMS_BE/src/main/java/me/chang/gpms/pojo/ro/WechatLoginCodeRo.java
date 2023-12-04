package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "微信小程序登录请求对象", name = "WechatLoginCodeRo")
@Data
public class WechatLoginCodeRo {
    @Schema(description = "临时登录凭证code", name = "code")
    public String code;
}
