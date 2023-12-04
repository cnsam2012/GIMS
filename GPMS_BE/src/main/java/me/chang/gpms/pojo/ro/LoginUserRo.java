package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户登录请求对象", name = "LoginUserRo")
public class LoginUserRo {

    @Schema(description = "用户名", name = "username")
    private String username;

    @Schema(description = "密码", name = "password")
    private String password;

    @Schema(description = "验证码", name = "code")
    private String code;

    @Schema(description = "记住我", name = "rememberMe")
    private boolean rememberMe;
}
