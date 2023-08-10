package com.goods.business.mapper;

import com.goods.common.model.business.Health;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/09 19:07:19
 * @className HealthMapper
 */
public interface HealthMapper extends Mapper<Health> {
    /**
     * 提示打卡
     * @param id
     * @return
     */
    Health isReport(Long id);

    List<Health> history(Long id);
}
