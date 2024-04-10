package me.chang.gpms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chang.gpms.dao.PlanMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.chang.gpms.pojo.Plan;
import me.chang.gpms.pojo.ro.PlanRo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {
    private final PlanMapper planMapper;
    private final PlanchooseService planchooseService;

    @Autowired
    public PlanService(PlanMapper planMapper, PlanchooseService planchooseService) {
        this.planMapper = planMapper;
        this.planchooseService = planchooseService;
    }


    public List<Plan> findAllPlanByPage(int pageNum, int pageSize) {
        Page<Plan> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Plan> planQueryWrapper = new QueryWrapper<>();
        planQueryWrapper.orderByDesc("ID");
        return planMapper.selectPage(page, planQueryWrapper).getRecords();
    }

    public int getAllPlanRows() {
        QueryWrapper<Plan> planQueryWrapper = new QueryWrapper<>();
        return Math.toIntExact(planMapper.selectCount(planQueryWrapper));
    }

    public int insertOnePlan(Plan plan) {
        return planMapper.insert(plan);
    }

    public int insertOnePlan(PlanRo plan) {
        Plan planInsert = Plan.getPlanByPlanRo(plan);
        return planMapper.insert(planInsert);
    }

    public Plan getPlanById(int planId) {
        return planMapper.selectById(planId);
    }

    public int deletePlanById(int planId) {
        var plan = this.getPlanById(planId);
        planchooseService.deletePlancByPlanId(planId);
        return planMapper.deleteById(plan);
    }

    public int updatePlan(Plan plan) {
        return planMapper.updateById(plan);
    }

    public List<Plan> getAllMyPlan(int userId) {
        // TODO 获取所有userId创建的plan
        return null;
    }

    public List<Plan> getPlanByCreator(Integer id) {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("CREATOR", id);
        return planMapper.selectList(queryWrapper);
    }
}
