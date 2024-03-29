package me.chang.gpms.dao;


import com.baomidou.mybatisplus.extension.service.IService;
import me.chang.gpms.pojo.Plan;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;


@Mapper
public interface PlanMapper {

    void deletePlanInternshipById(int id);

    Plan selectById(int id);

    void insertPlanInternship(Plan plan);

    void updatePlanInternship(Plan plan);


}


