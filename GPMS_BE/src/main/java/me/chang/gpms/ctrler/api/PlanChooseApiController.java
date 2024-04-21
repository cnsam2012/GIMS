package me.chang.gpms.ctrler.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.*;
import me.chang.gpms.pojo.ro.PlanChooseMarkingRo;
import me.chang.gpms.service.*;
import me.chang.gpms.util.HostHolder;
import me.chang.gpms.util.R;
import me.chang.gpms.util.constant.GPMSResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Tag(name = "PCAC", description = "PlanchooseApiController")
@RestController
@Slf4j
public class PlanChooseApiController {

    @Autowired
    PlanchooseService planchooseService;

    @Autowired
    PlanService planService;

    @Autowired
    UserService userService;

    @Autowired
    ReportService reportService;

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
        List<PlanChoose> allPlancByPage = planchooseService.findAllPlancByPage(pageNum, pageSize);
        for (PlanChoose pc : allPlancByPage) {
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
            PlanChoose pc
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
        PlanChoose plancById = planchooseService.getPlancById(plancId);
        if (ObjectUtil.isEmpty(plancById)) {
            data.put("planMsg", "找不到记录，该id对应的记录不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到记录，该id对应的记录不存在", data);
        }

        // 用户为学生，即自删自退实习时候
        if (user.getType() == 1) {
            try {
                // 校验是否为自主实习（自主实习的creator即为删除者）
                Plan planById = planService.getPlanById(
                        planchooseService.getPlancById(plancId).getPlanId()
                );
                // 若为自主实习，删除对应的实习信息
                if (planById.getCreator().equals(user.getId())) {
                    log.info("STUDENT SELF_PLAN DETECTED");
                    data.put("planMsg", "delete a self-plan");
                    var cnt = planService.deletePlanById(planById.getId());
                    data.put("opCount", cnt);
                    return R.ok(
                            GPMSResponseCode.OK.value(),
                            "success",
                            data
                    );
                }
            } catch (Exception e) {
                data.put("errorMsg", "plancId找不到对应的planchoose，或planchoose中记录的planId找不到对应的plan，请练习管理员");
                return R.ok(
                        GPMSResponseCode.CLIENT_ERROR.value(),
                        "error",
                        data
                );
            }
        }

        if (planchooseService.deletePlancById(plancId) != 0) {
            try {
                // 更新用户的部门信息,
                Plan planById = planService.getPlanById(plancById.getPlanId());
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

    @RequestMapping(value = "api/planc/score", method = {RequestMethod.PUT})
    @Operation(description = "delete a plan choose")
    public R setScore(@Parameter(required = true)
                      @RequestBody
                      PlanChooseMarkingRo planChooseMarkingRo) {
        // TODO 评分待完成
        var data = new HashMap<String, Object>();
        var plancId = planChooseMarkingRo.getId();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }
        PlanChoose plancById = planchooseService.getPlancById(plancId);
        if (ObjectUtil.isEmpty(plancById)) {
            data.put("planMsg", "找不到记录，该id对应的记录不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到记录，该id对应的记录不存在", data);
        }

        Integer score = planChooseMarkingRo.getScore();
        if (ObjectUtil.isNotNull(score)) {
            if (planchooseService.setScore(plancById, score) > 0) {
                return R.ok(
                        GPMSResponseCode.OK.value(),
                        "planc_marked"
                );
            }
        }

        return R.ok(
                GPMSResponseCode.SERVER_INTERNAL_ERROR.value(),
                "planc_marking_faild"
        );
    }


    @RequestMapping(value = "api/planc/detail", method = {RequestMethod.POST})
    @Operation(description = "get planChoose detail")
    public R getDetail(
            @RequestBody
            @Parameter(required = true, example = "29") int plancId
    ) {
        var data = new HashMap<String, Object>();

        User user = hostHolder.getUser();
        if (user == null) {
            return R.error(GPMSResponseCode.CLIENT_NO_AUTHORITY.value(), "您尚未登录");
        }

        PlanChoose plancById = planchooseService.getPlancById(plancId);
        if (ObjectUtil.isEmpty(plancById)) {
            data.put("planMsg", "找不到记录，该id对应的记录不存在");
            return R.error(GPMSResponseCode.CLIENT_ERROR.value(), "找不到记录，该id对应的记录不存在", data);
        }

        // 封装学生信息
        Integer studentId = plancById.getUserId();
        User student = userService.findUserById(studentId);
        if (ObjectUtil.isNull(student)) {
            return R.error(GPMSResponseCode.SERVER_INTERNAL_ERROR.value(), "student_not_found");
        }
        data.put("student", student);

        // 封装学生未读报告数量
        int reportUnreadRows = reportService.findReportUnreadRows(student.getId());
        data.put("reportUnreadRows", reportUnreadRows);

        // 封装学生总结报告数量
        int reportSummaryRows = reportService.findReportSummaryRows(student.getId());
        data.put("reportSummaryRows", reportSummaryRows);

        //封装学生今日提交报告数量
        int todayCreatedRows = reportService.findReportTodayRows(student.getId());
        data.put("todayCreatedRows", todayCreatedRows);

        //与学生的对话总数
        String id1 = String.valueOf(user.getId());
        String id2 = String.valueOf(studentId);
        String conversationId = "";
        if (Integer.parseInt(id1) < Integer.parseInt(id2)) {
            conversationId = id1 + "_" + id2;
        } else {
            conversationId = id2 + "_" + id1;
        }
        int messageCount = messageService.findLettersRows(conversationId);
        data.put("messageCount", messageCount);

        //周、月、总占比
        Plan plan = planService.getPlanById(plancById.getPlanId());
        // plan信息
        data.put("plan", plan);

        // 查询正在参加的实习的阶段
        int planStage;
        // 查询实习阶段
        planStage = 1; // 初始假设为 planc 中没有记录
        String planLineOne = "尚未选择实习";
        String planLineTwo = "无实习信息记录";
        String planLineThree = "请提醒学生在学院规定的时间内选择已发布的实习、或添加自主实习信息";
        if (ObjectUtil.isNotEmpty(plancById)) {
            try {
                // 检查 plancByUserId 是否已得分
                if (plancById.getScore() > 0) {
                    planStage = 3;
                    Plan planById = planService.getPlanById(plancById.getPlanId());
                    planLineOne = "已经完成的实习";
                    planLineTwo = plancById.getScore().toString();
                    var planName = planById.getName();
                    var departmentName = departmentsService.getDepartmentByCreator(
                            planById.getCreator()
                    ).getName();
                    planLineThree = planName + " - " + departmentName + " (" + planById.getType() + ") ";
                } else {
                    planStage = 2;
                    Plan planById = planService.getPlanById(plancById.getPlanId());
                    planLineOne = "正在参加的实习";
                    planLineTwo = planById.getName();
                    var departmentName = departmentsService.getDepartmentByCreator(
                            planById.getCreator()
                    ).getName();
                    var creatorName = userService.findUserById(planById.getCreator()).getRoleName();
                    planLineThree = departmentName + " - " + creatorName + " (" + planById.getType() + ") ";
                }
            } catch (Exception e) {
                e.printStackTrace();
                planLineOne = "实习信息加载出错";
                planLineTwo = "暂无记录";
                planLineThree = "请联系管理员";
            }
        }
        data.put("planStage", planStage); // 添加实习阶段到data
        data.put("planLineOne", planLineOne); // 添加实习阶段到data
        data.put("planLineTwo", planLineTwo); // 添加实习阶段到data
        data.put("planLineThree", planLineThree); // 添加实习阶段到data

        boolean isMarkable = false;
        String markableReason = "";
        // 直接检查是否已有得分，如果有，则可以重新提交
        if (plancById.getScore() >= 0) {
            markableReason = "已有得分，可重新提交";
            isMarkable = true;
        } else if (reportUnreadRows == 0 && reportSummaryRows >= 1) {
            // 如果没有未读报告且已提交实习总结，设置为可标记
            markableReason = "无未读报告，且已提交实习总结";
            isMarkable = true;
        } else {
            // 其他情况，不可标记，需根据具体情况设置原因
            if (reportUnreadRows > 0) {
                markableReason = reportSummaryRows < 1 ? "存在未读报告、学生尚未提交实习总结" : "存在未读报告";
            } else {
                markableReason = reportSummaryRows < 1 ? "学生尚未提交实习总结" : "";
            }
        }
        // 将结果放入data中
        data.put("isMarkable", isMarkable);
        data.put("markableReason", markableReason);

        // TODO ...ECHARTS 折线图学生提交报告折线图数据返回
        List<Map<String, Object>> reportDataList = reportService.findReportDataByStudentId(student.getId());
        List<String> dates = new ArrayList<>(); // 存储日期
        List<Integer> reportCounts = new ArrayList<>(); // 存储每天的报告数量
        for (Map<String, Object> reportData : reportDataList) {
            dates.add(reportData.get("date").toString()); // 假设map中有日期
            reportCounts.add(Integer.parseInt(String.valueOf(reportData.get("count")))); // 假设map中有当天报告的数量
        }
        Map<String, Object> reportChartData = new HashMap<>();
        reportChartData.put("dates", dates);
        reportChartData.put("reportCounts", reportCounts);
        data.put("reportChartData", reportChartData);

        // 使用改进的方法来计算得分
        double week = checkAndCorrectNaN(reportService.calculateReportScore(studentId, 1)); // 周记
        double month = checkAndCorrectNaN(reportService.calculateReportScore(studentId, 2)); // 月记
        double summary = checkAndCorrectNaN(reportService.calculateReportScore(studentId, 3)); // 总结

        double score = (week * (((double) plan.getPercentInMonthlyReport()) / 100)) +
                (month * (((double) plan.getPercentInMonthlyReport()) / 100)) +
                (summary * (((double) plan.getPercentInSummary()) / 100));

        data.put("score", score);
        data.put("week", week);
        data.put("month", month);
        data.put("summary", summary);

        return R.ok(
                GPMSResponseCode.OK.value(),
                "get_detail",
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

    // 定义一个函数来检查并修正NaN值
    private double checkAndCorrectNaN(double value) {
        return Double.isNaN(value) ? 0 : value;
    }


}
