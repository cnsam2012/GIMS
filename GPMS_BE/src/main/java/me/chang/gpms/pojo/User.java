package me.chang.gpms.pojo;


import lombok.Data;
import me.chang.gpms.pojo.ro.RegisterRo;

@Data
public class User {

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

    public static User getUserByRr(RegisterRo rr) {
        var user = new User();
        user.setDepartmentId(rr.getDepartmentId());
        user.setPassword(rr.getPassword());
        user.setUsername(rr.getUsername());
        user.setEmail(rr.getEmail());
        return user;
    }

}
