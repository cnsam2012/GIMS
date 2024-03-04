package me.chang.gpms.config;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Spring Quartz 配置类，用于将数据存入数据库，以后直接从数据库中调用数据
 */
@Configuration
public class QuartzConfig {

    /**
     * 连接保活任务
     * @return
     */
    @Bean
    public JobDetailFactoryBean mysqlConnectionRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();

        factoryBean.setJobClass(me.chang.gpms.quartz.MysqlConnectionRefreshJob.class);
        factoryBean.setName("mysqlConnectionRefreshJob");
        factoryBean.setGroup("gpmsJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);

        return factoryBean;
    }

    /**
     * 连接保活触发器
     * @return
     */
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail mysqlConnectionRefreshJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();

        factoryBean.setJobDetail(mysqlConnectionRefreshJobDetail);
        factoryBean.setName("mysqlConnectionRefreshTrigger");
        factoryBean.setGroup("gpmsTriggerGroup");
//        factoryBean.setRepeatInterval(1000 * 60 * 5); // 5分钟刷新一次
        factoryBean.setRepeatInterval(1000 * 60 * 1); // 1 min
        factoryBean.setJobDataMap(new JobDataMap());

        return factoryBean;
    }
}
