package me.chang.gpms.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 讨论贴
 * 对应数据库表 `discuss_post`
 */
@Data
@Document(indexName = "report")
//@Document(indexName = "discusspost")
@Schema(description = "讨论贴", name = "Report")
public class Report {

    /**
     * 唯一标识
     */
    @Id
    @Schema(description = "唯一标识", example = "3", name = "")
    private Integer id;

    /**
     * 发布讨论的作
     */
    @Field(type = FieldType.Integer)
    @Schema(description = "发布讨论的作者", example = "102", name = "")
    private Integer userId;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @Schema(description = "标题", example = "这个是不为空的标题", name = "")
    private String title;

    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @Schema(description = "内容", example = "#### Welcome to", name = "")
    private String content;

    /**
     * 报告类型：1-周记; 2-月记; 3-总结
     */
    @Field(type = FieldType.Integer)
    @Schema(description = "报告类型：1-周记; 2-月记; 3-总结", example = "0", name = "")
    private Integer type;

    /**
     * 状态：0-正常，1-精华 2-拉黑
     */
    @Field(type = FieldType.Integer)
    @Schema(description = "状态：0-正常，1-精华 2-拉黑", example = "0", name = "")
    private Integer status;

    /**
     * 发布时间
     */
    @Field(type = FieldType.Date)
    @Schema(description = "发布时间", example = "2023-09-24 17:12:17", name = "")
    private Date createTime;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Integer)
    @Schema(description = "评论数量", example = "0", name = "")
    private Integer commentCount;

    /**
     * 热度/分数
     */
    @Field(type = FieldType.Double)
    @Schema(description = "热度/分数", example = "3553", name = "")
    private double score;

    /**
     * 逻辑删除
     * optional: MP automatically convert "isDeleted" into "is_deleted"
     * 可选：Mybatis-Plus 自动转换驼峰命名
     * `@TableField(value = "is_deleted")`
     */
    @TableLogic
    @Schema(description = "逻辑删除", example = "0", name = "")
    private Integer isDeleted;

    @Field(type = FieldType.Integer)
    @Schema(description = "批阅情况", example = "0", name = "")
    private Integer isRead;

    @Field(type = FieldType.Integer)
    @Schema(description = "通过", example = "0", name = "")
    private Integer isPassed;

    @Field(type = FieldType.Integer)
    @Schema(description = "最后编辑", example = "0", name = "")
    private Integer lastedEditUserId;

}
