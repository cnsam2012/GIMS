package me.chang.gpms.pojo.ro;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "PlanChooseMarkingRo", name = "PlanChooseMarkingRo")
public class PlanChooseMarkingRo {
    @Schema(description = "实习C id", example = "32", name = "id")
    private Integer id;

    @Schema(description = "实习评分", example = "80", name = "score")
    private Integer score;
}
