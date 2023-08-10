package com.goods.controller.business;

import com.goods.business.service.ProductService;
import com.goods.business.service.ProductStockService;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductStock;
import com.goods.common.model.business.Supplier;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lx
 * @createTime 2023/08/07 21:08:52
 * @className ProductController
 */
@RestController
@RequestMapping("/business/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductStockService productStockService;

    /**
     * 查询分页列表
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @param categoryId
     * @param supplier
     * @param status
     * @return
     */
    @GetMapping("/findProductList")
    public ResponseBean findProductList(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "categoryId", required = false) Long categoryId,
                                        Supplier supplier,
                                        @RequestParam(value = "status", required = false) Integer status,
                                        @RequestParam(value = "categorys", required = false) String categorys) {
        PageVO<ProductVO> productVOPageVO = productService.findProductList(pageNum, pageSize, name, categoryId, supplier, status, categorys);
        return ResponseBean.success(productVOPageVO);
    }

    /**
     * 添加物资
     */
    @PostMapping("/add")
    public ResponseBean addProduct(@RequestBody @Validated ProductVO productVO) {
        Boolean flag = productService.addProduct(productVO);
        if (flag) {
            return ResponseBean.success();
        } else {
            return ResponseBean.error(false);
        }
    }

    /**
     * 数据回显
     *
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseBean.success(product);
    }

    /**
     * 修改数据
     *
     * @param id
     * @param product
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id, @RequestBody Product product) {
        productService.update(id, product);
        return ResponseBean.success();
    }

    /**
     * 物资回收站、恢复与删除
     *
     * @param id
     * @return
     */
    @PutMapping("/remove/{id}")
    public ResponseBean remove(@PathVariable Long id) {
        productService.remove(id);
        return ResponseBean.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseBean.success();
    }

    /**
     * 恢复
     *
     * @param id
     * @return
     */
    @PutMapping("/back/{id}")
    public ResponseBean back(@PathVariable Long id) {
        productService.back(id);
        return ResponseBean.success();
    }

    /**
     * 审核
     *
     * @param id
     * @return
     */
    @PutMapping("/publish/{id}")
    public ResponseBean publish(@PathVariable Long id) {
        Boolean flag = productService.publish(id);
        if (flag) {
            return ResponseBean.success();
        } else {
            return ResponseBean.error(false);
        }
    }

    @GetMapping("/findProducts")
    public ResponseBean<PageVO<ProductVO>> findProducts(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        @RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                        Supplier supplier,
                                                        @RequestParam(value = "status", required = false) Integer status,
                                                        @RequestParam(value = "categorys", required = false) String categorys) {
        PageVO<ProductVO> productList = productService.findProductList(pageNum, pageSize, name, categoryId, supplier, status, categorys);
        return ResponseBean.success(productList);
    }

    /**
     * 物资库存数据,分页表格
     *
     * @param pageSize
     * @param pageNum
     * @param categorys
     * @param name
     * @return
     */
    @GetMapping("/findProductStocks")
    public ResponseBean findProductStocks(@RequestParam Integer pageSize,
                                          @RequestParam Integer pageNum,
                                          @RequestParam(required = false) String categorys,
                                          @RequestParam(required = false) String name) {
        List<ProductStockVO> productStockVOS = productStockService.findProductStocks(categorys, name);
        List<ProductStockVO> page = ListPageUtils.page(productStockVOS, pageSize, pageNum);
        PageVO pageVO = new PageVO(productStockVOS.size(), page);
        return ResponseBean.success(pageVO);
    }


    /**
     * 构建饼状图
     * @param categorys
     * @param name
     * @return
     */
    @GetMapping("/findAllStocks")
    public ResponseBean findAllStocks(@RequestParam(required = false) String categorys,
                                      @RequestParam(required = false) String  name) {

        List<ProductStockVO> productStockVOList = productStockService.findProductStocks(categorys, name);
        return ResponseBean.success(productStockVOList);
    }
}
