package me.chang.gpms.pojo;


import lombok.Data;

@Data
public class Message {
    private int id;
    private int fromId;
    private String _fromId;
    private int toId;
    private String _toId;
    private String conversationId;
    private String content;
    private int status;
    private java.util.Date createTime;

}
