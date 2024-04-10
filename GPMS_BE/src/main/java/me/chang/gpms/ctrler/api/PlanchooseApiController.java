package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.*;
import me.chang.gpms.service.*;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Tag(name = "PCAC", description = "PlanchooseApiController")
@RestController
@Slf4j
public class PlanchooseApiController {

    @Autowired
    PlanchooseService planchooseService;

    @Autowired
    PlanService planService;

    @Autowired
    UserService userService;

    @Autowired
    DepartmentsService departmentsService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @RequestMapping(value = "api/planc/all", method = RequestMethod.POST)
    @Operation(summary = "list all plan choose")
    public R plancs(
            @Parameter(required = false)
            @RequestBody
            Page page
    ) {
        var data = new HashMap<String, Object>();
        var pageNum = page.getCurrent();
        var pageSize = page.getLimit();
        var pageTotal = planchooseService.getAllPlancRows();
        page.setRows(pageTotal);
        List<Planchoose> allPlancByPage = planchooseService.findAllPlancByPage(pageNum, pageSize);
        for (Planchoose pc : allPlancByPage) {
            var planId = pc.getPlanId();
            var userId = pc.getUserId();
            var _planId = "";
            var _userId = "";
            try {
                _planId = planService.getPlanById(planId).getName();
            } catch (Exception ignored) {
            }
            try {
                _userId = userService.findUserById(userId).getRoleName();
            } catch (Exception ignored) {

            }
            pc.set_planId(_planId);
            pc.set_userId(_userId);
        }
        data.put("planc", allPlancByPage);
        data.put("page", page);
        return R.ok(GPMSResponseCode.OK.value(), "success", data);
    }

    @RequestMapping(value = "api/planc/user", method = RequestMethod.POST)
    @Operation(summary = "list a special plan choose")
    public R plancsSpec(
            @Parameter(required = false)
            @RequestBody
            int requestUserId
    ) {
        var data = new HashMap<String, Object>();
        var pc = planchooseService.getPlancByUserId(requestUserId);
        if (ObjectUtil.isNotNull(pc) && ObjectUtil.isNotEmpty(pc)) {
            int planId = pc.getPlanId();
            int userId = pc.getUserId();
            String _planId = "";
            String _userId = "";
            try {
                _planId = planService.getPlanById(planId).getName();
            } catch (Exception ignored) {
            }
            try {
                _userId = userService.findUserById(userId).getRoleName();
            } catch (Exception ignored) {

            }
            pc.set_planId(_planId);
            pc.set_userId(_userId);
        }
        data.put("planc", pc);
        return R.ok(GPMSResponseCode.OK.value(), "success", data);
    }


    @RequestMapping(value = "api/planc/add", method = {RequestMethod.POST})
    @Operation(summary = "add a plan choose")
    public R addPlanc(
            @Parameter(required = true)
            @RequestBody
            Planchoose pc
    ) {
        var data = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        pc.setCreateTime(new Date());
        var cnt = planchooseService.insertOnePlanc(pc);


        try {
            // 更新用户的部门信息,
            Plan planById = planService.getPlanById(pc.getPlanId());
            Departments departmentByCreator = departmentsService.getDepartmentByCreator(planById.getCreator());
            // 若计划创建者有创建了的部门，则加入
            if (ObjectUtil.isNotNull(departmentByCreator)) {
                int departmentByCreatorId = departmentByCreator.getId();
                userService.updateDepartment(pc.getUserId(), departmentByCreatorId);
                this.sendMessage(pc.getUserId(), planById.getName(), departmentByCreator.getName(), true);
            } else {
                this.sendMessage(pc.getUserId(), planById.getName(), null, false);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }

        data.put("operationCount", cnt);

        return R.ok(
                GPMSResponseCode.OK.value(),
                "add plan choose success",
                data
        );
    }

    @RequestMapping(value = "api/planc/delete", method = {RequestMethod.DELETE})
    @Operation(description = "delete a plan choose")
    public R setDelete(@Parameter(required = true)
                       @RequestBody
                       int plancId) {
        var data = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        Planchoose plancById = planchooseService.getPlancById(plancId);
        if (ObjectUtil.isEmpty(plancById)) {
            data.put("planMsg", "找不到记录，该id对应的记录不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到记录，该id对应的记录不存在", data);
        }
        if (planchooseService.deletePlancById(plancId) != 0) {

            try {
                // 更新用户的部门信息,
                Plan planById = planService.getPlanById(plancId);
                Departments departmentByCreator = departmentsService.getDepartmentByCreator(planById.getCreator());
                // 若计划创建者有创建了的部门，则加入
                if (ObjectUtil.isNotNull(departmentByCreator)) {
                    int departmentByCreatorId = departmentByCreator.getId();
                    userService.updateDepartment(plancById.getUserId(), departmentByCreatorId);
                    this.sendExitMessage(plancById.getUserId(), planById.getName(), departmentByCreator.getName(), true);
                } else {
                    this.sendExitMessage(plancById.getUserId(), planById.getName(), null, false);
                }
            } catch (Exception e) {
                log.error(e.toString());
            }

            return R.ok(
                    GPMSResponseCode.OK.value(),
                    "success",
                    data
            );
        }
        data.put("plancMsg", "未知错误");
        return R.ok(
                GPMSResponseCode.CLIENT_ERROR.value(),
                "delete_fail",
                data
        );
    }

    private void sendMessage(int toId, String planName, String departmentName, boolean hasDepartment) {
        // 发送部门信息通知
        Message message = new Message();
        String roleName = userService.findUserById(toId).getRoleName();
        message.setFromId(910007);
        message.set_fromId("管理员");
        message.setToId(toId);
        message.set_toId(roleName);
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setStatus(0); // 默认就是 0 未读，可不写
        message.setCreateTime(new Date());
        if (hasDepartment) {
            message.setContent(roleName + ": 您好，您已经选择实习计划: " + planName + "。您已加入部门: " + departmentName + "。");
        } else {
            message.setContent(roleName + ": 您好，您已经选择实习计划: " + planName + "。");
        }
        messageService.addMessage(message);
    }

    private void sendExitMessage(int toId, String planName, String departmentName, boolean hasDepartment) {
        // 发送部门信息通知
        Message message = new Message();
        String roleName = userService.findUserById(toId).getRoleName();
        message.setFromId(910007);
        message.set_fromId("管理员");
        message.setToId(toId);
        message.set_toId(roleName);
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setStatus(0); // 默认就是 0 未读，可不写
        message.setCreateTime(new Date());
        if (hasDepartment) {
            message.setContent(roleName + ": 您好，您已经退出实习计划: " + planName + "。您已退出部门: " + departmentName + "。");
        } else {
            message.setContent(roleName + ": 您好，您已经退出实习计划: " + planName + "。");
        }
        messageService.addMessage(message);
    }


}
