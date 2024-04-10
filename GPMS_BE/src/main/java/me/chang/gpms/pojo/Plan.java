package me.chang.gpms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chang.gpms.pojo.ro.PlanAddRo;
import me.chang.gpms.pojo.ro.PlanRo;

import java.util.Date;

@Data
@TableName("plan")
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @TableId(type = IdType.AUTO, value = "ID")
    private Integer id;

    private String type;
    private String name;

    @TableField(value = "MAJOR_ORIE_ID")
    private String majorOrieId;

    private Integer grade;

    @TableField(value = "START_D")
    private Date startD;

    @TableField(value = "END_D")
    private Date endD;

    private int classHour;
    private float credit;
    private Integer percentIn;
    private Integer percentEx;
    private boolean isDeleted;
    private String content;
    private String objective;
    private String demand;
    private Integer scoreCalType;
    private Integer percentInDailyReport;
    private Integer percentInWeeklyReport;
    private Integer percentInMonthlyReport;
    private Integer percentInSummary;
    private Date deadline;
    private Integer creator;

    @TableField(exist = false)
    private String _creator;

    public static Plan getPlanByPlanRo(PlanRo planRo) {
        var p = new Plan();
        p.id = planRo.getId();
        p.type = planRo.getType();
        p.name = planRo.getName();
        p.majorOrieId = planRo.getMajor_orie_id();
        p.grade = planRo.getGrade();
        p.startD = planRo.getStart_d();
        p.endD = planRo.getEnd_d();
        p.classHour = planRo.getClass_hour();
        p.credit = planRo.getCredit();
        p.percentIn = planRo.getPercent_in();
        p.percentEx = planRo.getPercent_ex();
        p.isDeleted = planRo.is_deleted();
        p.content = planRo.getContent();
        p.objective = planRo.getObjective();
        p.demand = planRo.getDemand();
        p.scoreCalType = planRo.getScore_cal_type();
        p.percentInDailyReport = planRo.getPercent_in_daily_report();
        p.percentInWeeklyReport = planRo.getPercent_in_weekly_report();
        p.percentInMonthlyReport = planRo.getPercent_in_monthly_report();
        p.percentInSummary = planRo.getPercent_in_summary();
        p.deadline = planRo.getDeadline();
        p.creator = planRo.getCreator();
        return p;
    }

    public static Plan getPlanByPlanAddRo(PlanAddRo planRo) {
        var p = new Plan();
        p.type = planRo.getType();
        p.name = planRo.getName();
        p.majorOrieId = planRo.getMajor_orie_id();
        p.grade = planRo.getGrade();
        p.startD = planRo.getStart_d();
        p.endD = planRo.getEnd_d();
        p.classHour = planRo.getClass_hour();
        p.credit = planRo.getCredit();
        p.percentIn = planRo.getPercent_in();
        p.percentEx = planRo.getPercent_ex();
        p.isDeleted = planRo.is_deleted();
        p.content = planRo.getContent();
        p.objective = planRo.getObjective();
        p.demand = planRo.getDemand();
        p.scoreCalType = planRo.getScore_cal_type();
        p.percentInDailyReport = planRo.getPercent_in_daily_report();
        p.percentInWeeklyReport = planRo.getPercent_in_weekly_report();
        p.percentInMonthlyReport = planRo.getPercent_in_monthly_report();
        p.percentInSummary = planRo.getPercent_in_summary();
        p.deadline = planRo.getDeadline();
        return p;
    }

}
