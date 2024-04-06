package me.chang.gpms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Planchoose {
    @TableId(type = IdType.AUTO, value = "id")
    private int id;
    private int userId;
    private int planId;
    private int status;
    private Date createTime;

    @TableField(exist = false)
    private String _userId;
    @TableField(exist = false)
    private String _planId;
}
