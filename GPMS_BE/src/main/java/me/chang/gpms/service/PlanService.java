package me.chang.gpms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chang.gpms.dao.PlanMapper;
import me.chang.gpms.pojo.Plan;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    private final PlanMapper planMapper;

    public PlanService(PlanMapper planMapper) {
        this.planMapper = planMapper;
    }
}
