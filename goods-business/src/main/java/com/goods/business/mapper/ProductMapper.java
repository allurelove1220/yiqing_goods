package com.goods.business.mapper;

import com.goods.common.model.business.Product;
import com.goods.common.vo.business.ProductStockVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/07 16:59:27
 * @className ProductMapper
 */

public interface ProductMapper extends Mapper<Product> {

    List<ProductStockVO> getAllProductVO(@Param("category1Id") Integer category1Id, @Param("category2Id") Integer category2Id, @Param("category3Id") Integer category3Id, String name);
}
