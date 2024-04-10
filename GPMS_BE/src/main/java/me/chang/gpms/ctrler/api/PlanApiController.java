package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.dao.PlanMapper;
import me.chang.gpms.pojo.*;
import me.chang.gpms.pojo.ro.DepartmentUpdateRo;
import me.chang.gpms.pojo.ro.PlanAddRo;
import me.chang.gpms.pojo.ro.PlanRo;
import me.chang.gpms.service.PlanService;
import me.chang.gpms.service.UserService;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@Tag(name = "PAC", description = "PlanApiController")
@RestController
@Slf4j
public class PlanApiController {

    @Autowired
    PlanService planService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "api/plan/all", method = RequestMethod.POST)
    @Operation(summary = "list all plan")
    public R plans(
            @Parameter(required = false)
            @RequestBody
            Page page
    ) {
        var data = new HashMap<String, Object>();
        var pageNum = page.getCurrent();
        var pageSize = page.getLimit();
        var pageTotal = planService.getAllPlanRows();
        page.setRows(pageTotal);
        var allPlan = planService.findAllPlanByPage(pageNum, pageSize);
        var planReturn = new ArrayList<Plan>();
        for (Plan p : allPlan) {
            try {
                var creatorId = p.getCreator();
                var userGot = userService.findUserById(creatorId);
                p.set_creator(userGot.getRoleName());
                planReturn.add(p);
            } catch (Exception e) {

            }
        }
        data.put("plan", planReturn);
        data.put("page", page);
        return R.ok(GPMSResponseCode.OK.value(), "success", data);
    }

    @RequestMapping(value = "api/plan/detail", method = RequestMethod.POST)
    @Operation(summary = "get Plan Detail")
    public R planDetail(
            @Parameter(required = false)
            @RequestBody
            PlanIdRo planIdRo
    ) {
        try {
            var data = new HashMap<String, Object>();
            Plan planById = planService.getPlanById(planIdRo.getPlanId());
            var creatorId = planById.getCreator();
            var userGot = userService.findUserById(creatorId);
            planById.set_creator(userGot.getRoleName());
            data.put("plan", planById);
            return R.ok(GPMSResponseCode.OK.value(), "success", data);
        } catch (Exception e) {
            return R.ok(GPMSResponseCode.SERVER_INTERNAL_ERROR.value(), "fail");
        }

    }

    @RequestMapping(value = "api/plan/add", method = {RequestMethod.POST})
    @Operation(summary = "add a plan")
    public R addPlan(
            @Parameter(required = true)
            @RequestBody
            PlanAddRo planAddRo
    ) {
        var data = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        var planRo = Plan.getPlanByPlanAddRo(planAddRo);
        planRo.setCreator(user.getId());
        var cnt = planService.insertOnePlan(planRo);
        data.put("operationCount", cnt);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "success",
                data
        );
    }

    @RequestMapping(value = "api/plan/delete", method = {RequestMethod.DELETE})
    @Operation(description = "delete a plan")
    public R setDelete(@Parameter(required = true)
                       @RequestBody
                       int planId) {
        var data = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        if (ObjectUtil.isEmpty(planService.getPlanById(planId))) {
            data.put("planMsg", "找不到该实习，该id对应的实习不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到该实习，该id对应的实习信息不存在", data);
        }
        if (planService.deletePlanById(planId) != 0) {
            return R.ok(
                    GPMSResponseCode.OK.value(),
                    "success",
                    data
            );
        }
        data.put("planMsg", "未知错误");
        return R.ok(
                GPMSResponseCode.CLIENT_ERROR.value(),
                "delete_fail",
                data
        );
    }


    @RequestMapping(value = "api/plan/update", method = {RequestMethod.PUT})
    @Operation(description = "修改实习")
    public R setUpdate(
            @Parameter(required = true)
            @RequestBody
            PlanRo planRo) {
        var data = new HashMap<String, Object>();
        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        if (ObjectUtil.isEmpty(planService.getPlanById(planRo.getId()))) {
            data.put("planMsg", "找不到该实习，该id对应的实习不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到该实习，该id对应的实习信息不存在", data);
        }

        Plan newPlan = Plan.getPlanByPlanRo(planRo);

        if (planService.updatePlan(newPlan) == 1) {
            return R.ok(
                    GPMSResponseCode.OK.value(),
                    "success",
                    data
            );
        }
        data.put("planMsg", "未知错误");
        return R.ok(
                GPMSResponseCode.CLIENT_ERROR.value(),
                "delete_fail",
                data
        );
    }
}
