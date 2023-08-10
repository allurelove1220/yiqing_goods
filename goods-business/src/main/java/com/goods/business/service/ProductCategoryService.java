package com.goods.business.service;

import com.github.pagehelper.Page;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/07 11:25:00
 * @className ProductCategoryService
 */
public interface ProductCategoryService {

    /**
     * 查询分页列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum, Integer pageSize);

    /**
     * 获取分类
     * @return
     */
    List<ProductCategoryTreeNodeVO> getParentCategoryTree();

    /**
     * 添加分类
     */
    void addProductCategory(ProductCategoryVO productCategoryVO);

    /**
     * 根据Id回显数据
     * @param id
     * @return
     */
    ProductCategory getById(Long id);

    /**
     * 修改数据
     * @param productCategory
     * @param id
     * @return
     */
    void update(Long id,ProductCategory productCategory);

    /**
     * 删除数据
     * @param id
     */
    boolean delete(Long id);
}
