package com.goods.business.service;

import com.goods.common.error.BusinessException;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;

import java.util.Map;

/**
 * @author lx
 * @createTime 2023/08/09 14:32:45
 * @className OutStockService
 */
public interface OutStockService {
    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param status
     * @param outStockVO
     * @return
     */
    PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, Integer status, OutStockVO outStockVO);

    /**
     * 明细
     * @param id
     * @param pageNum
     * @return
     */
    Map<String, Object> detail(Long id, Integer pageNum);

    /**
     * 物资去处
     * @param outStockVO
     * @param username
     */
    void addOutStock(OutStockVO outStockVO, String username);

    /**
     * 审核
     * @param id
     */
    void publish(Long id) throws BusinessException;

    /**
     * 删除数据
     * @param id
     */
    void delete(Long id);
}
