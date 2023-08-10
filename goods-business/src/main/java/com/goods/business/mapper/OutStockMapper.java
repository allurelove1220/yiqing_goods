package com.goods.business.mapper;

import com.goods.common.model.business.OutStock;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.OutStockItemVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/09 14:39:15
 * @className OutStockMapper
 */
public interface OutStockMapper extends Mapper<OutStock> {
    ConsumerVO getConsumer(Long id);

    List<OutStockItemVO> findOUtStockDetail(Long id, Integer pageNum);
}
