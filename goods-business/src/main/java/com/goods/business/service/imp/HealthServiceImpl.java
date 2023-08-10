package com.goods.business.service.imp;

import com.goods.business.mapper.HealthMapper;
import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/09 11:22:36
 * @className HealthServiceImpl
 */
@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private HealthMapper healthMapper;

    /**
     * 提示打卡
     *
     * @param id
     * @return
     */
    @Override
    public Health isReport(Long id) {
        return healthMapper.isReport(id);
    }

    /**
     * 打卡
     *
     * @param health
     * @return
     */
    @Override
    public Boolean report(Health health) {
        int i = healthMapper.insert(health);
        if (i > 0) {
            return true;
        }
        return false;
    }

    /**
     * 历史记录
     *
     * @param id
     * @return
     */
    @Override
    public List<Health> history(Long id) {
        Health health = new Health();
        health.setUserId(id);
        List<Health> list = healthMapper.select(health);
        return list;
    }
}
