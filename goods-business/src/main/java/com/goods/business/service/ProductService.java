package com.goods.business.service;

import com.goods.common.model.business.Product;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author lx
 * @createTime 2023/08/07 21:10:15
 * @className ProductService
 */
public interface ProductService {
    /**
     * 分页查询列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param categoryId
     * @param supplier
     * @param status
     * @return
     */
    PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, String name, Long categoryId, Supplier supplier, Integer status,String categorys);

    /**
     * 添加物资
     * @param productVO
     * @return
     */
    Boolean addProduct(ProductVO productVO);

    /**
     * 数据回显
     * @param id
     * @return
     */
    Product getById(Long id);

    /**
     * 修改数据
     * @param id
     * @param product
     */
    void update(Long id, Product product);

    /**
     * 物资回收站
     * @param id
     */
    void remove(Long id);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 恢复
     * @param id
     */
    void back(Long id);

    /**
     * 审核
     * @return
     */
    Boolean publish(Long id);

    /**
     * 柱状图信息
     * @param map
     * @return
     */
    List<ProductStockVO> getAllProductVO(Map map);
}
