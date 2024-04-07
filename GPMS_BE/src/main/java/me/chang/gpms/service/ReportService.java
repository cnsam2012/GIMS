package me.chang.gpms.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.dao.ReportMapper;
import me.chang.gpms.pojo.Report;
import me.chang.gpms.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 报告相关
 */
@Service
@Slf4j
public class ReportService {


    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.reports.max-size}")
    private int maxSize;

    @Value("${caffeine.reports.expire-seconds}")
    private int expireSeconds;

    // 热帖列表的本地缓存
    // key - offset(每页的起始索引):limit(每页显示多少条数据)
    private LoadingCache<String, List<Report>> reportListCache;

    // 报告总数的本地缓存
    // key - userId(其实就是0,表示查询的是所有用户. 对特定用户的查询不启用缓存）
    private LoadingCache<Integer, Integer> reportRowsCache;

    /**
     * 初始化本地缓存 Caffeine
     */
    @PostConstruct
    public void init() {
        // 初始化热帖列表缓存
        reportListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<Report>>() {
                    // 如果缓存Caffeine中没有数据，告诉缓存如何去数据库中查数据，再装到缓存中
                    @Nullable
                    @Override
                    public List<Report> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误");
                        }

                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误");
                        }

                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);

                        // 此处可以再访问二级缓存 Redis
                        log.debug("load report list from DB");
                        return reportMapper.selectReports(0, offset, limit, 1);
                    }
                });

        // 初始化报告总数缓存
        reportRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer key) throws Exception {
                        log.debug("load report rows from DB");
                        return reportMapper.selectReportRows(key);
                    }
                });
    }


    /**
     * 分页查询报告信息
     *
     * @param userId    当传入的 userId = 0 时查找所有用户的报告
     *                  当传入的 userId != 0 时，查找该指定用户的报告
     * @param offset    每页的起始索引
     * @param limit     每页显示多少条数据
     * @param orderMode 排行模式(若传入 1, 则按照热度来排序)
     * @return
     */
    public List<Report> findReports(int userId, int offset, int limit, int orderMode) {
        // 查询本地缓存(当查询的是所有用户的报告并且按照热度排序时)
//        if (userId == 0 && orderMode == 1) {
//            return reportListCache.get(offset + ":" + limit);
//        }

        Page<Report> page = new Page<>(offset, limit);
        QueryWrapper<Report> qw = new QueryWrapper<>();

        if (userId == 0 && orderMode == 0) {
            qw.orderByDesc("create_time");
        } else if (orderMode == 1) {
            qw = null;
        } else {
            qw.orderByDesc("create_time");
            qw.eq(userId != 0, "user_id", userId);
        }

        return reportMapper.selectPage(page, qw).getRecords();
//        return reportMapper.selectReports(userId, offset, limit, orderMode);
    }

    /**
     * 查询报告的个数
     *
     * @param userId 当传入的 userId = 0 时计算所有用户的报告总数
     *               当传入的 userId ！= 0 时计算该指定用户的报告总数
     * @return
     */
    public int findReportRows(int userId) {
        // 查询本地缓存(当查询的是所有用户的报告总数时)
        if (userId == 0) {
            return reportRowsCache.get(userId);
        }

        // 查询数据库
        log.debug("load report rows from DB");
        return reportMapper.selectReportRows(userId);
    }

    /**
     * 添加报告
     *
     * @param reportAdded
     * @return
     */
    public int addReport(Report reportAdded) {
        if (reportAdded == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 转义 HTML 标记，防止在 HTML 标签中注入攻击语句
        reportAdded.setTitle(HtmlUtils.htmlEscape(reportAdded.getTitle()));
        reportAdded.setContent(HtmlUtils.htmlEscape(reportAdded.getContent()));

        // 过滤敏感词
        reportAdded.setTitle(sensitiveFilter.filter(reportAdded.getTitle()));
        reportAdded.setContent(sensitiveFilter.filter(reportAdded.getContent()));

        return reportMapper.insert(reportAdded);
//        return reportMapper.insertReport(reportAdded);
    }

    /**
     * 根据 id 查询报告
     *
     * @param id
     * @return
     */
    public Report findReportById(int id) {
        return reportMapper.selectReportById(id);
    }

    /**
     * 修改报告的评论数量
     *
     * @param id           报告 id
     * @param commentCount
     * @return
     */
    public int updateCommentCount(int id, int commentCount) {
//        return reportMapper.updateCommentCount(id, commentCount);
        return -1;
    }

    /**
     * 修改报告类型：1-周记; 2-月记; 3-总结
     *
     * @param id
     * @param type
     * @return
     */
    public int updateType(int id, int type) {
        if (type < 1 || type > 3) {
            int i = -1;
            return i;
        }
        return reportMapper.updateType(id, type);
    }

    /**
     * 修改报告状态：0-正常; 1-精华; 2-拉黑;
     *
     * @param id
     * @param status
     * @return
     */
    public int updateStatus(int id, int status) {
        if (status < 0 || status > 2) {
            int i = -1;
            return i;
        }
        return reportMapper.updateStatus(id, status);
    }

    /**
     * 修改报告阅读状态 1-read; 0-unread
     *
     * @param id
     * @param isRead
     * @return
     */
    public int updateRead(int id, int isRead) {
        if (isRead < 0 || isRead > 1) {
            int i = -1;
            return i;
        }
        return reportMapper.updateRead(id, isRead);
    }

    /**
     * 修改报告通过状态 1-pass; 0-none; -1-fail
     *
     * @param id
     * @param isPass
     * @return
     */
    public int updatePass(int id, int isPass) {
        if (isPass < -1 || isPass > 1) {
            int i = -1;
            return i;
        }
        return reportMapper.updatePass(id, isPass);
    }

    /**
     * 修改报告分数
     *
     * @param id
     * @param score
     * @return
     */
    public int updateScore(int id, double score) {
        return reportMapper.updateScore(id, score);
    }

    /**
     * 删除报告
     *
     * @param id
     * @return
     */
    public int deleteReport(int id) {
        return reportMapper.deleteById(id);
    }

    public List<Report> searchReports(String keywords, int current, int limit) {
        String username = null;
        try {
            var userFound = userService.findAUserByRoleName(keywords);
            username = String.valueOf(userFound.getId());
            log.info("==========got user {}", username);
        } catch (Exception e) {
            log.warn(e.toString());
        }
        Page<Report> page = new Page<>(current, limit); // 创建分页对象
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>(); // 创建查询条件包装器
        if (ObjectUtil.isNotNull(username) || ObjectUtil.isNotEmpty(username)) {
            queryWrapper.like("title", "%" + keywords + "%")
                    .or()
                    .like("content", "%" + keywords + "%")
                    .or()
                    .eq("user_id", username);
        } else {
            queryWrapper.like("title", "%" + keywords + "%")
                    .or()
                    .like("content", "%" + keywords + "%");
        }
        reportMapper.selectPage(page, queryWrapper); // 执行分页查询
        return page.getRecords(); // 返回查询结果
    }
}
