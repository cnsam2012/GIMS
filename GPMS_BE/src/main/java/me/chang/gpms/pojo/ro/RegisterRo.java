package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户注册请求对象", name = "RegisterRo")
public class RegisterRo {
    @Schema(description = "用户名", name = "username")
    private String username;

    @Schema(description = "密码", name = "password")
    private String password;

    @Schema(description = "邮箱", name = "email")
    private String email;

    @Schema(description = "部门", name = "departmentId")
    private Integer departmentId;
}
