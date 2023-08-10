package com.goods.controller.business;

import com.goods.business.service.ProductCategoryService;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/07 20:49:20
 * @className ProductCategoryController
 */
@Api(tags = "物资分类模块")
@RestController
@RequestMapping("/business/productCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 分页查询列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/categoryTree")
    public ResponseBean categoryTree(@RequestParam(value = "pageNum",required = false) Integer pageNum,@RequestParam(value = "pageSize",required = false) Integer pageSize){
        PageVO<ProductCategoryTreeNodeVO> categoryTree = productCategoryService.categoryTree(pageNum, pageSize);
        return ResponseBean.success(categoryTree);
    }

    /**
     * 获取分类数据
     * @return
     */
    @GetMapping("/getParentCategoryTree")
    public ResponseBean getParentCategoryTree(){
        List<ProductCategoryTreeNodeVO> parentCategoryTree = productCategoryService.getParentCategoryTree();
        return ResponseBean.success(parentCategoryTree);
    }

    /**
     * 添加分类
     * @param productCategoryVO
     * @return
     */
    @PostMapping("/add")
    public ResponseBean add(@RequestBody ProductCategoryVO productCategoryVO){
        productCategoryService.addProductCategory(productCategoryVO);
        return ResponseBean.success();
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id){
        ProductCategory productCategory = productCategoryService.getById(id);
        return ResponseBean.success(productCategory);
    }

    /**
     * 修改数据
     * @param id
     * @param productCategory
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id,@RequestBody ProductCategory productCategory){
        productCategoryService.update(id,productCategory);
        return ResponseBean.success();
    }

    /**
     * 删除数据
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id){
        boolean flag = productCategoryService.delete(id);
        if (flag){
            return ResponseBean.success();
        }else {
            return ResponseBean.error(false);
        }
    }
}
