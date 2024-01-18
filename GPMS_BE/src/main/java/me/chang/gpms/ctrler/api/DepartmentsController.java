package me.chang.gpms.ctrler.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import me.chang.gpms.dao.DepartmentsMapper;
import me.chang.gpms.pojo.Departments;
import me.chang.gpms.pojo.Page;
import me.chang.gpms.pojo.User;
import me.chang.gpms.pojo.ro.DepartmentRo;
import me.chang.gpms.service.DepartmentsService;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Tag(name = "DC", description = "DepartmentsController")
@CrossOrigin
public class DepartmentsController {

    DepartmentsService departmentsService;

    HostHolder hostHolder;

    @Autowired
    public DepartmentsController(DepartmentsService departmentsService, HostHolder hostHolder) {
        this.departmentsService = departmentsService;
        this.hostHolder = hostHolder;
    }

    @RequestMapping(value = "api/departments", method = {RequestMethod.GET})
    @Operation(summary = "Get all departments information.")
    public R departments(
            HttpServletResponse resp,
            @Parameter(required = false)
            Page page
    ) {
        var data = new HashMap<String, Object>();
        var offset = page.getOffset();
        var limit = page.getLimit();
        List<Departments> departmentsList = departmentsService.getAllDepartments(offset, limit);
        page.setRows(departmentsList.size());
        data.put("page", page);
        data.put("departments", departmentsList);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "success",
                data
        );
    }

    @RequestMapping(value = "api/addDepartment", method = {RequestMethod.POST})
    @Operation(summary = "Get all departments information.")
    public R addDepartment(
            @Parameter(required = true)
            @RequestBody
            DepartmentRo departmentRo,
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }

        Departments departments = new Departments();
        departments.setName(departmentRo.getName());
        departments.setType(departmentRo.getType());
        departments.setContent(departmentRo.getContent());
        // 当前登录用户获取
        departments.setBelongTo(user.getId());

        departmentsService.insertDepartment(departments);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "success",
                data
        );
    }
}
