package me.chang.gpms.pojo.ro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@Schema(description = "分页与对话ID接受对象", name = "PageWithConversationIdRo")
public class PageWithConversationIdRo {

    @Schema(description = "会话ID", example = "910007_910007", name = "conversationId")
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Schema(description = "当前页码", example = "1", name = "current")
    private int current = 1;

    @Schema(description = "单页显示的帖子数量上限", example = "10", name = "limit")
    private int limit = 10;

    @Schema(description = "帖子总数（用于计算总页数）", example = "3", hidden = true, name = "rows")
    private int rows;

    @Schema(description = "查询路径（复用分页链接, 其他界面也可以有分页）", example = "/discuss/detail/3", hidden = true, name = "path")
    private String path;

    @Schema(description = "当前页的起始索引 offset", example = "0", hidden = true, name = "offset")
    private int offset;

    @Schema(description = "总页数", example = "0", hidden = true, name = "total")
    private int total;

    @Schema(description = "获取分页栏起始页码，分页栏显示当前页码及其前后两页", example = "1", hidden = true, name = "from")
    private int from;

    @Schema(description = "获取分页栏结束页码", example = "0", hidden = true, name = "to")
    private int to;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (current >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始索引 offset
     *
     * @return
     */
    public int getOffset() {
        return current * limit - limit;
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取分页栏起始页码
     * 分页栏显示当前页码及其前后两页
     *
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取分页栏结束页码
     *
     * @return
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Page{" +
                "current=" + current +
                ", limit=" + limit +
                ", rows=" + rows +
                ", path='" + path + '\'' +
                ", offset=" + offset +
                ", total=" + total +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

    @Test
    void pagePojoTest() {
        var p = new PageAndOrderModeAndUserIdRo();
        System.out.println(p.getOffset());
        System.out.println(p.getTotal());
        System.out.println(p.getFrom());
        System.out.println(p.getTo());
    }
}
