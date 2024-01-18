package me.chang.gpms.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Departments {

    private int id;

    private int type;

    private String name;

    private String content;

    @TableField("belong_to")
    private int belongTo;

}
