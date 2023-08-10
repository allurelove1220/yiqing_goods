package com.goods.business.service;

import com.goods.common.vo.business.ProductStockVO;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/09 22:15:37
 * @className ProductStockService
 */
public interface ProductStockService {
    List<ProductStockVO> findProductStocks(String categorys, String name);
}
