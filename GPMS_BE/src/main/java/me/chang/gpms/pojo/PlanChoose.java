package me.chang.gpms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("planchoose")
public class PlanChoose {
    @TableId(type = IdType.AUTO, value = "id")
    private Integer id;
    private Integer userId;
    private Integer planId;
    private Integer status;
    private Date createTime;

    @TableField(exist = false)
    private String _userId;
    @TableField(exist = false)
    private String _planId;
}
