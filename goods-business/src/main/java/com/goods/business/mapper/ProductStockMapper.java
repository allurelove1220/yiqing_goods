package com.goods.business.mapper;

import com.goods.common.model.business.ProductStock;
import com.goods.common.vo.business.ProductStockVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/09 13:51:08
 * @className ProductStockMapper
 */
public interface ProductStockMapper extends Mapper<ProductStock> {
    /**
     * 根据三级分类id，以及name进行查询
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @param name
     * @return
     */
    List<ProductStockVO> findProductStocks(@Param("category1Id") String category1Id, @Param("category2Id")String category2Id, @Param("category3Id")String category3Id, @Param("name")String name);
}
