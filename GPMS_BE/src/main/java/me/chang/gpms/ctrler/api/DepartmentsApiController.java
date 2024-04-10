package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.*;
import me.chang.gpms.pojo.ro.DepartmentRo;
import me.chang.gpms.pojo.ro.DepartmentUpdateRo;
import me.chang.gpms.pojo.ro.PageWithFuzzyRo;
import me.chang.gpms.service.*;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RestController
@Tag(name = "DAC", description = "DepartmentsApiController")
@CrossOrigin
@Slf4j
public class DepartmentsApiController {

    DepartmentsService departmentsService;

    PlanService planService;

    PlanchooseService planchooseService;

    UserService userService;

    MessageService messageService;

    HostHolder hostHolder;

    @Autowired
    public DepartmentsApiController(DepartmentsService departmentsService, PlanService planService, PlanchooseService planchooseService, UserService userService, MessageService messageService, HostHolder hostHolder) {
        this.departmentsService = departmentsService;
        this.planService = planService;
        this.planchooseService = planchooseService;
        this.userService = userService;
        this.messageService = messageService;
        this.hostHolder = hostHolder;
    }

    /**
     * 查看所有部门，包括分页信息
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "api/departments", method = {RequestMethod.POST})
    @Operation(summary = "Get all departments information.")
    public R departments(
            @Parameter(required = false)
            @RequestBody
            Page page
    ) {
        var data = new HashMap<String, Object>();
        var offset = page.getOffset();
        var limit = page.getLimit();

        List<Departments> departmentsList = departmentsService.getAllDepartments(offset, limit);
        page.setRows(departmentsService.selectDepartmentsRows(0));
        System.out.println(page);
        data.put("page", page);
        data.put("departments", departmentsList);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "success",
                data
        );
    }

    @RequestMapping(value = "api/fuzzySearchDepartments", method = {RequestMethod.POST})
    @Operation(summary = "Get all departments by keywords.")
    public R fuzzySearchDepartments(
            @Parameter(name = "page", required = true)
            @RequestBody(required = true)
            PageWithFuzzyRo pageWithFuzzyRo
    ) {
        var data = new HashMap<String, Object>();
        var offset = pageWithFuzzyRo.getOffset();
        var limit = pageWithFuzzyRo.getLimit();
        var keywords = pageWithFuzzyRo.getKeywords();

        List<Departments> departmentsList = departmentsService.getAllDepartmentsByFuzzyKeywords(keywords, offset, limit);
        pageWithFuzzyRo.setRows(departmentsService.selectAllDepartmentsByFuzzyKeywordsRows(keywords));
        System.out.println(pageWithFuzzyRo);
        data.put("page", pageWithFuzzyRo);
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
        int insertDepartmentSId = departmentsService.insertDepartment(departments);

        // 查询该用户有无创建Plan
        var creatorSPlan = planService.getPlanByCreator(user.getId());
        // 无，pass
        if (creatorSPlan.size() <= 0) {
            log.info("this user has not create any departments -- {}", user.toString());
        } else {
            // 有，在planchoose中将所有选中该plan的用户的departmentId修改为新建的department
            for (Plan plan : creatorSPlan) {
                var planId = plan.getId();
                List<Planchoose> plancByPlanId = planchooseService.getPlancByPlanId(planId);
                for (Planchoose next : plancByPlanId) {
                    int userId = next.getUserId();
                    var userRoleName = userService.findUserById(userId).getRoleName();

                    userService.updateDepartment(userId, insertDepartmentSId);

                    // 向被操作用户发送通知
                    Message message = new Message();
                    message.setFromId(user.getId());
                    message.set_fromId(user.getRoleName());
                    message.setToId(userId);
                    message.set_toId(userRoleName);
                    if (message.getFromId() < message.getToId()) {
                        message.setConversationId(message.getFromId() + "_" + message.getToId());
                    } else {
                        message.setConversationId(message.getToId() + "_" + message.getFromId());
                    }
                    message.setStatus(0); // 默认就是 0 未读，可不写
                    message.setCreateTime(new Date());
                    message.setContent(userRoleName + ": 您好。您已加入部门: " + departments.getName() + "。");
                    messageService.addMessage(message);
                }
            }
        }


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
        var departmentName = departmentsService.getDepartmentById(departmentId).getName();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        if (ObjectUtil.isEmpty(departmentsService.getDepartmentById(departmentId))) {
            data.put("departmentMsg", "找不到该部门，该id对应的部门不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到该部门，该id对应的部门不存在", data);
        }
        if (departmentsService.deleteDepartmentsById(departmentId) != 0) {

            // 查询该用户有无创建Plan
            var creatorSPlan = planService.getPlanByCreator(user.getId());
            // 无，pass
            if (creatorSPlan.size() <= 0) {
                log.info("this user has not create any departments -- {}", user.toString());
            } else {
                // 有，在planchoose中将所有选中该plan的用户的departmentId置为-1，表示无归属部门
                for (Plan plan : creatorSPlan) {
                    var planId = plan.getId();
                    List<Planchoose> plancByPlanId = planchooseService.getPlancByPlanId(planId);
                    for (Planchoose next : plancByPlanId) {
                        int userId = next.getUserId();
                        userService.updateDepartment(userId, -1);
                        var userRoleName = userService.findUserById(userId).getRoleName();

                        // 向被操作用户发送通知
                        Message message = new Message();
                        message.setFromId(user.getId());
                        message.set_fromId(user.getRoleName());
                        message.setToId(userId);
                        message.set_toId(userRoleName);
                        if (message.getFromId() < message.getToId()) {
                            message.setConversationId(message.getFromId() + "_" + message.getToId());
                        } else {
                            message.setConversationId(message.getToId() + "_" + message.getFromId());
                        }
                        message.setStatus(0); // 默认就是 0 未读，可不写
                        message.setCreateTime(new Date());
                        message.setContent(userRoleName + ": 您好。原部门已经删除，您已被移出部门: " + departmentName + "。");
                        messageService.addMessage(message);
                    }
                }
            }

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

//    @RequestMapping(value = "api/fuzzySearchDepartments", method = {RequestMethod.POST})
//    @Operation(description = "搜索部门")
//    public R fuzzySearchDepartments(
//            @Parameter(required = true)
//            @RequestBody
//            String keywords) {
//        var data = new HashMap<String, Object>();
//
//        departmentsService.getDepartmentByFuzzyKeywords(keywords);
//
//        return R.ok(
//                GPMSResponseCode.CLIENT_ERROR.value(),
//                "delete_fail",
//                data
//        );
//    }
}
