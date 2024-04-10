package me.chang.gpms.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Departments {

    @TableId(type = IdType.AUTO)
    private int id;

    private int type;

    private String name;

    private String content;

    @TableField("belong_to")
    private int belongTo;

}
