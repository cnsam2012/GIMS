package me.chang.gpms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.chang.gpms.dao.PlanchooseMapper;
import me.chang.gpms.pojo.Plan;
import me.chang.gpms.pojo.Planchoose;
import me.chang.gpms.pojo.ro.PlanRo;
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

    public List<Planchoose> findAllPlancByPage(int pageNum, int pageSize) {
        Page<Planchoose> page = new Page<>(pageNum, pageSize);
        return planchooseMapper.selectPage(page, null).getRecords();
    }

    public int getAllPlancRows() {
        return Math.toIntExact(planchooseMapper.selectCount(null));
    }

    public int insertOnePlanc(Planchoose plan) {
        return planchooseMapper.insert(plan);
    }

    public Planchoose getPlancById(int plancId) {
        return planchooseMapper.selectById(plancId);
    }

    public List<Planchoose> getPlancByPlanId(int planId) {
        QueryWrapper<Planchoose> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", planId);
        return planchooseMapper.selectList(queryWrapper);
    }

    public Planchoose getPlancByUserId(int userId) {
        QueryWrapper<Planchoose> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return planchooseMapper.selectOne(qw);
    }

    public int deletePlancById(int plancId) {
        return planchooseMapper.deleteById(this.getPlancById(plancId));
    }

    public int updatePlanc(Planchoose plan) {
        return planchooseMapper.updateById(plan);
    }

    public List<Planchoose> getAllMyPlan(int userId) {
        // TODO 获取所有userId创建的plan
        return null;
    }

    public void deletePlancByPlanId(int planId) {
        QueryWrapper<Planchoose> qw = new QueryWrapper<>();
        qw.eq("plan_id", planId);
        planchooseMapper.delete(qw);
    }
}
