package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.ProductCategoryMapper;
import com.goods.business.mapper.ProductMapper;
import com.goods.business.mapper.ProductStockMapper;
import com.goods.business.mapper.SupplierMapper;
import com.goods.business.service.ProductService;
import com.goods.common.error.SystemCodeEnum;
import com.goods.common.error.SystemException;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductStock;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import jdk.net.SocketFlow;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author lx
 * @createTime 2023/08/07 21:21:34
 * @className ProductServiceImpl
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, String name, Long categoryId, Supplier supplier, Integer status, String categorys) {
        // 分页
        PageHelper.startPage(pageNum,pageSize);
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (name != null) {
            name = "%" + name + "%";
            criteria.andLike("name", name);
        }
        if (null != status) {
            criteria.andEqualTo("status", status);
        }
        if (null != categorys) {
            String[] split = categorys.split(",");
            if (split.length == 1) {
                criteria.andEqualTo("oneCategoryId", split[0]);
            }
            if (split.length == 2) {
                criteria.andEqualTo("oneCategoryId", split[0]);
                criteria.andEqualTo("twoCategoryId", split[1]);
            }
            if (split.length == 3) {
                criteria.andEqualTo("oneCategoryId", split[0]);
                criteria.andEqualTo("twoCategoryId", split[1]);
                criteria.andEqualTo("threeCategoryId", split[2]);
            }
        }
        List<Product> products = productMapper.selectByExample(example);
        List<ProductVO> productVOS = new ArrayList<>();
        products.forEach(product -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product, productVO);
            productVOS.add(productVO);
        });
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        return new PageVO<>(pageInfo.getTotal(), productVOS);
    }

    /**
     * 添加物资
     *
     * @param productVO
     * @return
     */
    @Override
    public Boolean addProduct(ProductVO productVO) {
        Product product=new Product();
        BeanUtils.copyProperties(productVO,product);
        product.setOneCategoryId(productVO.getCategoryKeys()[0]);
        product.setTwoCategoryId(productVO.getCategoryKeys()[1]);
        product.setThreeCategoryId(productVO.getCategoryKeys()[2]);
        String PNum = UUID.randomUUID().toString();
        product.setPNum(PNum);
        product.setCreateTime(new Date());
        product.setModifiedTime(new Date());
        product.setStatus(2);
        productMapper.insert(product);
        return true;
    }

    /**
     * 数据回显
     *
     * @param id
     * @return
     */
    @Override
    public Product getById(Long id) {
        Product product = new Product();
        product.setId(id);
        return productMapper.selectOne(product);
    }

    /**
     * 修改数据
     *
     * @param id
     * @param product
     */
    @Override
    public void update(Long id, Product product) {
        product.setId(id);
        productMapper.updateByPrimaryKey(product);
    }

    /**
     * 物资回收站
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product.getStatus() == 0){
            product.setStatus(1);
        }
        productMapper.updateByPrimaryKeySelective(product);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Product product = new Product();
        product.setId(id);
        productMapper.delete(product);
    }

    /**
     * 恢复
     *
     * @param id
     */
    @Override
    public void back(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product.getStatus() == 1)
        product.setStatus(0);
        productMapper.updateByPrimaryKeySelective(product);
    }

    /**
     * 审核
     * @return
     */
    @Override
    public Boolean publish(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product.getStatus() == 2){
            product.setStatus(0);
        }
        productMapper.updateByPrimaryKeySelective(product);
        return true;
    }

    /**
     * 柱状图信息
     * @param map
     * @return
     */
    @Override
    public List<ProductStockVO> getAllProductVO(Map map) {
        String categorys = (String) map.get("categorys");
        String[] categoryIds = new String[3];

        if (!StringUtils.isEmpty(categorys)) {
            String[] split = categorys.split(",");
            for (int i = 0; i < split.length; i++) {
                categoryIds[i]=split[i];
            }
        }

        Integer category1Id = StringUtils.isEmpty(categoryIds[0]) ? null : Integer.valueOf(categoryIds[0]);
        Integer category2Id = StringUtils.isEmpty(categoryIds[1]) ? null : Integer.valueOf(categoryIds[1]);
        Integer category3Id = StringUtils.isEmpty(categoryIds[2]) ? null : Integer.valueOf(categoryIds[2]);

        String name = (String) map.get("name");
        return productMapper.getAllProductVO(category1Id, category2Id, category3Id, name);
    }
}
