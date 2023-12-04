package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "新旧密码请求对象", name = "OldNewPasswordRo")
@Data
public class OldNewPasswordRo {
    @Schema(description = "旧密码", name = "oldPwd")
    private String oldPwd;
    @Schema(description = "新密码", name = "newPwd")
    private String newPwd;
}
