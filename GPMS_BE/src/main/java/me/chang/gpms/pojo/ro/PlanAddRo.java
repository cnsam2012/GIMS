package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Schema(name = "PlanAddRo")
public class PlanAddRo {
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

    @Schema(hidden = true)
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
}
