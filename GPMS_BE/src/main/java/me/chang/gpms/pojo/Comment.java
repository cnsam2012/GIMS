package me.chang.gpms.pojo;


import lombok.Data;

@Data
public class Comment {

  private int id;
  private int userId;
  private int entityType;
  private int entityId;
  private int targetId;
  private String content;
  private long status;
  private java.util.Date createTime;


}
