package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import me.chang.gpms.pojo.Departments;
import me.chang.gpms.pojo.Page;
import me.chang.gpms.pojo.User;
import me.chang.gpms.pojo.ro.DepartmentRo;
import me.chang.gpms.pojo.ro.DepartmentUpdateRo;
import me.chang.gpms.service.DepartmentsService;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Tag(name = "DC", description = "DepartmentsController")
@CrossOrigin
public class DepartmentsApiController {

    DepartmentsService departmentsService;

    HostHolder hostHolder;

    @Autowired
    public DepartmentsApiController(DepartmentsService departmentsService, HostHolder hostHolder) {
        this.departmentsService = departmentsService;
        this.hostHolder = hostHolder;
    }

    /**
     * 查看所有部门，包括分页信息
     *
     * @param resp
     * @param page
     * @return
     */
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

    /**
     * 添加部门
     *
     * @param departmentRo
     * @param resp
     * @return
     */
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

    /**
     * 删除部门
     *
     * @param departmentId
     * @return
     */
    @RequestMapping(value = "api/deleteDepartment", method = {RequestMethod.DELETE})
    @Operation(description = "删除部门")
    public R setDelete(@Parameter(required = true)
                       @RequestBody
                       int departmentId) {
        var data = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        if (ObjectUtil.isEmpty(departmentsService.getDepartmentById(departmentId))) {
            data.put("departmentMsg", "找不到该部门，该id对应的部门不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到该部门，该id对应的部门不存在", data);
        }
        if (departmentsService.deleteDepartmentsById(departmentId) != 0) {
            return R.ok(
                    GPMSResponseCode.OK.value(),
                    "success",
                    data
            );
        }
        data.put("departmentMsg", "未知错误");
        return R.ok(
                GPMSResponseCode.CLIENT_ERROR.value(),
                "delete_fail",
                data
        );
    }

    @RequestMapping(value = "api/updateDepartment", method = {RequestMethod.PUT})
    @Operation(description = "修改部门")
    public R updateDepartments(
            @Parameter(required = true)
            @RequestBody
            DepartmentUpdateRo departmentRo) {
        var data = new HashMap<String, Object>();
        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        if (ObjectUtil.isEmpty(departmentsService.getDepartmentById(departmentRo.getDepartmentId()))) {
            data.put("departmentMsg", "找不到该部门，该id对应的部门不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到该部门，该id对应的部门不存在", data);
        }
        Departments departments = new Departments();
        departments.setName(departmentRo.getName());
        departments.setType(departmentRo.getType());
        departments.setContent(departmentRo.getContent());
        if (departmentsService.updateDepartment(departmentRo.getDepartmentId(), departments)) {
            return R.ok(
                    GPMSResponseCode.OK.value(),
                    "success",
                    data
            );
        }
        data.put("departmentMsg", "未知错误");
        return R.ok(
                GPMSResponseCode.CLIENT_ERROR.value(),
                "delete_fail",
                data
        );
    }
}
