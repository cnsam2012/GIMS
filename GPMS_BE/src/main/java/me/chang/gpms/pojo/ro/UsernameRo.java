package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户名请求对象", name = "UsernameRo")
@Data
public class UsernameRo {
    @Schema(description = "用户名", example = "这里是新用户名", name = "username")
    private String username;
}
