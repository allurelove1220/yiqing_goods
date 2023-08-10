package com.goods.business.service;

import com.goods.common.model.business.InStock;
import com.goods.common.vo.business.*;
import com.goods.common.vo.system.PageVO;

/**
 * @author lx
 * @createTime 2023/08/08 12:58:42
 * @className InStockService
 */
public interface InStockService {
    /**
     * 分页查新列表
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize, Integer status,InStockVO inStockVO);

    /**
     * 物资明细
     * @param id
     * @param pageNum
     * @return
     */
    InStockDetailVO findDetailList(Long id, Integer pageNum);

    /**
     * 添加入库单(物资入库)
     * @param inStockVO
     * @return
     */
    Boolean addIntoStock(InStockVO inStockVO,String username);

    /**
     * 删除数据
     * @param id
     */
    void delete(Long id);

    /**
     * 同过审核
     * @param id
     */
    void publish(Long id);
}
