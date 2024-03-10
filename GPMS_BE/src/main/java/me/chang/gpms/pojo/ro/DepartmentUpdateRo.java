package me.chang.gpms.pojo.ro;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Schema(name = "DepartmentUpdateRo", description = "部门更新请求对象")
public class DepartmentUpdateRo {
    @Schema(description = "部门类型 1-Academic_Department-院系部门; 2-companies-实习单位", example = "1", name = "type")
    private int type;

    @Schema(description = "部门名称", example = "教务处", name = "name")
    private String name;

    @Schema(description = "部门描述", example = "教务通知、毕业实习通知下发", name = "content")
    private String content;

    @Schema(description = "部门ID", example = "-0", name = "departmentId")
    private int departmentId;
}
