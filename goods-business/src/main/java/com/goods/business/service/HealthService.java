package com.goods.business.service;

import com.goods.common.model.business.Health;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/09 11:22:17
 * @className HealthService
 */
public interface HealthService {
    /**
     * 提示打卡
     * @param id
     * @return
     */
    Health isReport(Long id);

    /**
     * 打卡
     * @param health
     * @return
     */
    Boolean report(Health health);

    /**
     * 历史记录
     * @param id
     * @return
     */
    List<Health> history(Long id);
}
