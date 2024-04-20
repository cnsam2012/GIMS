package me.chang.gpms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.chang.gpms.dao.PlanchooseMapper;
import me.chang.gpms.pojo.PlanChoose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PlanchooseService {

    private final PlanchooseMapper planchooseMapper;

    @Autowired
    public PlanchooseService(PlanchooseMapper planchooseMapper) {
        this.planchooseMapper = planchooseMapper;
    }

    public List<PlanChoose> findAllPlancByPage(int pageNum, int pageSize) {
        Page<PlanChoose> page = new Page<>(pageNum, pageSize);
        return planchooseMapper.selectPage(page, null).getRecords();
    }

    public int getAllPlancRows() {
        return Math.toIntExact(planchooseMapper.selectCount(null));
    }

    public int insertOnePlanc(PlanChoose plan) {
        return planchooseMapper.insert(plan);
    }

    public PlanChoose getPlancById(int plancId) {
        return planchooseMapper.selectById(plancId);
    }

    public List<PlanChoose> getPlancByPlanId(int planId) {
        QueryWrapper<PlanChoose> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", planId);
        return planchooseMapper.selectList(queryWrapper);
    }

    public PlanChoose getPlancByUserId(int userId) {
        QueryWrapper<PlanChoose> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return planchooseMapper.selectOne(qw);
    }

    public int deletePlancById(int plancId) {
        return planchooseMapper.deleteById(this.getPlancById(plancId));
    }

    public int updatePlanc(PlanChoose plan) {
        return planchooseMapper.updateById(plan);
    }

    public List<PlanChoose> getAllMyPlan(int userId) {
        // TODO 获取所有userId创建的plan
        return null;
    }

    public void deletePlancByPlanId(int planId) {
        QueryWrapper<PlanChoose> qw = new QueryWrapper<>();
        qw.eq("plan_id", planId);
        planchooseMapper.delete(qw);
    }

    public int setScore(PlanChoose planChoose, Integer score) {
        // 设置对象的score为传入的score
        planChoose.setScore(score);
        // 使用MyBatis Plus的updateById方法更新记录
        return planchooseMapper.updateById(planChoose);
    }
}
