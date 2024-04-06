package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.*;
import me.chang.gpms.service.PlanService;
import me.chang.gpms.service.PlanchooseService;
import me.chang.gpms.service.UserService;
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
    PlanchooseService plancService;

    @Autowired
    PlanService planService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

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
        var pageTotal = plancService.getAllPlancRows();
        page.setRows(pageTotal);
        List<Planchoose> allPlancByPage = plancService.findAllPlancByPage(pageNum, pageSize);
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
        var pc = plancService.getPlancByUserId(requestUserId);
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
        var cnt = plancService.insertOnePlanc(pc);
        data.put("operationCount", cnt);
        return R.ok(
                GPMSResponseCode.OK.value(),
                "success",
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
        if (ObjectUtil.isEmpty(plancService.getPlancById(plancId))) {
            data.put("planMsg", "找不到记录，该id对应的记录不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到记录，该id对应的记录不存在", data);
        }
        if (plancService.deletePlancById(plancId) != 0) {
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


}
