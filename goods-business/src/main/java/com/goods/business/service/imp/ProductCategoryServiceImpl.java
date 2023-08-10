package com.goods.business.service.imp;

import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.ProductCategoryMapper;
import com.goods.business.mapper.ProductMapper;
import com.goods.business.service.ProductCategoryService;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.utils.CategoryTreeBuilder;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author lx
 * @createTime 2023/08/07 11:25:24
 * @className ProductCategoryServiceImpl
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询分页列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageVO<ProductCategoryTreeNodeVO> categoryTree(Integer pageNum, Integer pageSize) {
        List<ProductCategory> productCategories = productCategoryMapper.selectAll();
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = new ArrayList<>();
        productCategories.forEach(productCategory -> {
            ProductCategoryTreeNodeVO productCategoryTreeNodeVO1 = new ProductCategoryTreeNodeVO();
            productCategoryTreeNodeVO1.setId(productCategory.getId());
            productCategoryTreeNodeVO1.setName(productCategory.getName());
            productCategoryTreeNodeVO1.setRemark(productCategory.getRemark());
            productCategoryTreeNodeVO1.setSort(productCategory.getSort());
            productCategoryTreeNodeVO1.setCreateTime(new Date());
            productCategoryTreeNodeVO1.setModifiedTime(productCategory.getModifiedTime());
            productCategoryTreeNodeVO1.setPid(productCategory.getPid());
            ProductCategoryTreeNodeVO.order();
            productCategoryTreeNodeVOList.add(productCategoryTreeNodeVO1);
        });
        List<ProductCategoryTreeNodeVO> build = CategoryTreeBuilder.build(productCategoryTreeNodeVOList);
        PageInfo<ProductCategoryTreeNodeVO> pageInfo = new PageInfo<>(build);
        if(pageNum != null){
            pageInfo.setPageNum(pageNum);
        }
        if(pageSize != null){
            pageInfo.setSize(pageSize);
        }
        // PageHelper.startPage(pageNum,pageSize);
        return new PageVO<>(pageInfo.getTotal(),build);
    }

    /**
     * 获取分类
     * @return
     */
    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {
        List<ProductCategory> productCategories = productCategoryMapper.selectAll();
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = new ArrayList<>();
        productCategories.forEach(productCategory -> {
            ProductCategoryTreeNodeVO productCategoryTreeNodeVO1 = new ProductCategoryTreeNodeVO();
            productCategoryTreeNodeVO1.setId(productCategory.getId());
            productCategoryTreeNodeVO1.setName(productCategory.getName());
            productCategoryTreeNodeVO1.setRemark(productCategory.getRemark());
            productCategoryTreeNodeVO1.setSort(productCategory.getSort());
            productCategoryTreeNodeVO1.setCreateTime(new Date());
            productCategoryTreeNodeVO1.setModifiedTime(productCategory.getModifiedTime());
            productCategoryTreeNodeVO1.setPid(productCategory.getPid());
            ProductCategoryTreeNodeVO.order();
            productCategoryTreeNodeVOList.add(productCategoryTreeNodeVO1);
        });
        CategoryTreeBuilder.build(productCategoryTreeNodeVOList);
        return productCategoryTreeNodeVOList;
    }

    /**
     * 添加分类
     */
    @Override
    public void addProductCategory(ProductCategoryVO productCategoryVO) {
        ProductCategory productCategory =new ProductCategory();
        BeanUtils.copyProperties(productCategoryVO,productCategory);
        productCategory.setCreateTime(new Date());
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.insert(productCategory);
    }

    /**
     * 回显数据
     * @param id
     * @return
     */
    @Override
    public ProductCategory getById(Long id) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        return  productCategoryMapper.selectOne(productCategory);
    }

    /**
     * 修改数据
     * @param productCategory
     * @param id
     * @return
     */
    @Override
    public void update(Long id,ProductCategory productCategory) {
        productCategoryMapper.updateByPrimaryKey(productCategory);
    }

    /**
     * 删除数据
     * @param id
     */
    @Override
    public boolean delete(Long id) {
        Example example = new Example(Product.class);

        example.createCriteria().orEqualTo("oneCategoryId",id)
                .orEqualTo("twoCategoryId",id)
                .orEqualTo("threeCategoryId",id);
        List<Product> products = productMapper.selectByExample(example);
        System.out.println(products.isEmpty());
        Example example1=new Example(ProductCategory.class);
        example1.createCriteria().andEqualTo("pid",id);
        List<ProductCategory> productCategories = productCategoryMapper.selectByExample(example1);
        System.out.println(productCategories.isEmpty());

        if (products.isEmpty()==true && productCategories.isEmpty()==true){
            int i = productCategoryMapper.deleteByPrimaryKey(id);
            if (i>0){
                return true;
            }
        }
        return false;
    }
}
