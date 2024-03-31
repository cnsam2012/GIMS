package me.chang.gpms.pojo.ro;

import lombok.Data;

@Data
public class AlterUserRo {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private String phone;
    private Integer type;
    private Integer status;
    private String activationCode;
    private String headerUrl;
    private java.util.Date createTime;
    private String wechatOpenId;
    private Integer departmentId;
    private String roleName;
}
