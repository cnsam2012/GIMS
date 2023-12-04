package me.chang.gpms.util.constant;

public enum GPMSUserAuth {

    /**
     * 1-students-学生; 2-instructors-指导老师; 3-companies-实习单位; 9-admin-院系管理员; 99-superuser-超级用户
     */

    /**
     * 权限：学生
     */
    AUTHORITY_STUDENT("student"),
    /**
     * 权限：指导老师
     */
    AUTHORITY_INSTRUCTORS("instructors"),
    /**
     * 权限：实习单位
     */
    AUTHORITY_COMPANIES("companies"),
    /**
     * 权限：院系管理员
     */
    AUTHORITY_ADMIN("admin"),
    /**
     * 权限：超级用户
     */
    AUTHORITY_SU("su");

    private final String value;

    GPMSUserAuth(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
