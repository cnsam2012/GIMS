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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("plan")
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @TableId(type = IdType.AUTO, value = "ID")
    private int id;

    private int type;
    private String name;
    private int major_orie_id;
    private int grade;
    private Date start_d;
    private Date end_d;
    private int class_hour;
    private float credit;
    private int percent_in;
    private int percent_ex;
    private boolean is_deleted;
    private String content;
    private String objective;
    private String demand;
    private int score_cal_type;
    private int percent_in_daily_report;
    private int percent_in_weekly_report;
    private int percent_in_monthly_report;
    private int percent_in_summary;
    private Date deadline;
    private int creator;

    @TableField(exist = false)
    private String _creator;

    public static Plan getPlanByPlanRo(PlanRo planRo) {
        var p = new Plan();
        p.id = planRo.getId();
        p.type = planRo.getType();
        p.name = planRo.getName();
        p.major_orie_id = planRo.getMajor_orie_id();
        p.grade = planRo.getGrade();
        p.start_d = planRo.getStart_d();
        p.end_d = planRo.getEnd_d();
        p.class_hour = planRo.getClass_hour();
        p.credit = planRo.getCredit();
        p.percent_in = planRo.getPercent_in();
        p.percent_ex = planRo.getPercent_ex();
        p.is_deleted = planRo.is_deleted();
        p.content = planRo.getContent();
        p.objective = planRo.getObjective();
        p.demand = planRo.getDemand();
        p.score_cal_type = planRo.getScore_cal_type();
        p.percent_in_daily_report = planRo.getPercent_in_daily_report();
        p.percent_in_weekly_report = planRo.getPercent_in_weekly_report();
        p.percent_in_monthly_report = planRo.getPercent_in_monthly_report();
        p.percent_in_summary = planRo.getPercent_in_summary();
        p.deadline = planRo.getDeadline();
        p.creator = planRo.getCreator();
        return p;
    }

    public static Plan getPlanByPlanAddRo(PlanAddRo planRo) {
        var p = new Plan();
        p.type = planRo.getType();
        p.name = planRo.getName();
        p.major_orie_id = planRo.getMajor_orie_id();
        p.grade = planRo.getGrade();
        p.start_d = planRo.getStart_d();
        p.end_d = planRo.getEnd_d();
        p.class_hour = planRo.getClass_hour();
        p.credit = planRo.getCredit();
        p.percent_in = planRo.getPercent_in();
        p.percent_ex = planRo.getPercent_ex();
        p.is_deleted = planRo.is_deleted();
        p.content = planRo.getContent();
        p.objective = planRo.getObjective();
        p.demand = planRo.getDemand();
        p.score_cal_type = planRo.getScore_cal_type();
        p.percent_in_daily_report = planRo.getPercent_in_daily_report();
        p.percent_in_weekly_report = planRo.getPercent_in_weekly_report();
        p.percent_in_monthly_report = planRo.getPercent_in_monthly_report();
        p.percent_in_summary = planRo.getPercent_in_summary();
        p.deadline = planRo.getDeadline();
        return p;
    }

}
