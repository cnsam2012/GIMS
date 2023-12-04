package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 接口返回数据示例
 *     {
 *         "openid":"xxxxxx",
*           "session_key":"xxxxx",
 *          "unionid":"xxxxx",
 *          "errcode":0,
 *          "errmsg":"xxxxx"
 *     }
 */
@Data
@Schema(description = "微信接口返回封装对象", name = "WeLoginRo")
public class WeLoginRo {
    /**
     *普通用户标识，对该公众帐号唯一
     */
    private String openid;

    /**
     *用户在开放平台的唯一标识符
     */
    private String unionid;

    private String session_key;

    private String errcode;

    private String errmsg;
}
