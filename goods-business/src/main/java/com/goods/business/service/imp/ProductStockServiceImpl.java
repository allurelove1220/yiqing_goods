package com.goods.business.service.imp;

import com.goods.business.mapper.ProductStockMapper;
import com.goods.business.service.ProductStockService;
import com.goods.common.vo.business.ProductStockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/09 22:16:04
 * @className ProductStockServiceImpl
 */
@Service
public class ProductStockServiceImpl implements ProductStockService {
    @Autowired
    private ProductStockMapper productStockMapper;
    @Override
    public List<ProductStockVO> findProductStocks(String categorys, String name) {
        String category1Id = null;
        String category2Id = null;
        String category3Id = null;
        if (!StringUtils.isEmpty(categorys)){
            String[] split = categorys.split(",");
            if (null!=split && split.length==1){
                category1Id = split[0];
            }else if (null!=split && split.length==2){
                category1Id = split[0];
                category2Id = split[1];
            }else if (null!=split && split.length==3){
                category1Id = split[0];
                category2Id = split[1];
                category3Id = split[2];
            }
        }
        List<ProductStockVO> productStockVOs = productStockMapper.findProductStocks(category1Id,category2Id,category3Id,name);
        return productStockVOs;
    }
}
