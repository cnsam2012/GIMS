package me.chang.gpms.quartz;

import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.Report;
import me.chang.gpms.service.ReportService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 连接保活
 */
@Slf4j
@Component
public class MysqlConnectionRefreshJob implements Job {

    @Autowired
    private ReportService discussPostService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("[任务开始] 正在保活连接");
            this.refresh();
        log.info("[任务结束] 连接保活完毕");
    }

    /**
     * 刷新任务
     */
    private void refresh() {
        log.info("连接保活中");
        List<Report> list = discussPostService.findReports(0, 1, 10, 0);
    }
}
