package com.goods.business.converter;

import com.goods.common.model.business.ProductCategory;
import com.goods.common.utils.CategoryTreeBuilder;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CategoruConverter {
    /**
     * 获取分类树信息
     * @param productCategories
     * @return
     */
    public static List<ProductCategoryTreeNodeVO> converterToALLCategoryNodeVO(List<ProductCategory> productCategories){
        //先过滤出用户的菜单

        if(!CollectionUtils.isEmpty(productCategories)){
            List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = productCategories.stream().map(productCategory -> {
                ProductCategoryTreeNodeVO productCategoryTreeNodeVO = new ProductCategoryTreeNodeVO();
                BeanUtils.copyProperties(productCategory, productCategoryTreeNodeVO);
                return productCategoryTreeNodeVO;
            }).collect(Collectors.toList());
            List<ProductCategoryTreeNodeVO> build = CategoryTreeBuilder.build(productCategoryTreeNodeVOList);
            return build;
        }
        return null;
    }
}
