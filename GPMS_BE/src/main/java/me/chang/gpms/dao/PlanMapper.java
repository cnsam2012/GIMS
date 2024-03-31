package me.chang.gpms.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.chang.gpms.pojo.Plan;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PlanMapper extends BaseMapper<Plan> {

    void deletePlanInternshipById(int id);

    Plan selectById(int id);

    void insertMyPlan(Plan plan);

    void updatePlanInternship(Plan plan);

}


