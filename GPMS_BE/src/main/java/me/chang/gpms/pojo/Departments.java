package me.chang.gpms.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Departments {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer type;

    private String name;

    private String content;

    @TableField("belong_to")
    private Integer belongTo;

}
