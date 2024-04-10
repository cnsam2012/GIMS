package me.chang.gpms.pojo;


import lombok.Data;

@Data
public class Message {
    private Integer id;
    private Integer fromId;
    private String _fromId;
    private Integer toId;
    private String _toId;
    private String conversationId;
    private String content;
    private Integer status;
    private java.util.Date createTime;

}
