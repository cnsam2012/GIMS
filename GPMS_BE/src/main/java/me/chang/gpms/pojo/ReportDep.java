package me.chang.gpms.pojo;


import lombok.Data;

@Data
@Deprecated
public class ReportDep {

  private long id;
  private long userId;
  private String title;
  private String content;
  private long type;
  private long status;
  private java.util.Date createTime;
  private String comment;
  private double score;
  private long isDeleted;

}
