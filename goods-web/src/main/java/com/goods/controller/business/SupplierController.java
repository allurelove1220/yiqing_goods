package com.goods.controller.business;

import com.goods.business.service.SupplierService;
import com.goods.common.model.business.Supplier;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * @author lx
 * @createTime 2023/08/07 18:02:05
 * @className SupplierController
 */
@Api(tags = "物资模块")
@RestController
@RequestMapping("/business/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 分页列表查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/findSupplierList")
    public ResponseBean findSupplierList(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         @RequestParam String name,
                                         @RequestParam(value = "address",required = false) String address,
                                         @RequestParam(value = "contact",required = false) String contact){
        PageVO<SupplierVO> supplierList = supplierService.findSupplierList(pageNum,pageSize,name,address,contact);
        return ResponseBean.success(supplierList);
    }

    /**
     * 添加物资来源
     * @param supplierVO
     * @return
     */
    @PostMapping("/add")
    public ResponseBean addSupplier(@RequestBody @Validated SupplierVO supplierVO){
        supplierService.addSupplier(supplierVO);
        return ResponseBean.success();
    }

    /**
     * 回显数据
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public ResponseBean edit(@PathVariable Long id){
        Supplier supplier = supplierService.getById(id);
        return ResponseBean.success(supplier);
    }

    /**
     * 修改数据
     * @param id
     * @param supplier
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id,@RequestBody Supplier supplier){
        supplierService.update(id,supplier);
        return ResponseBean.success();
    }

    /**
     * 删除数据
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResponseBean delete(@PathVariable Long id){
        Boolean flag = supplierService.delete(id);
        if (flag){
            return ResponseBean.success();
        }else {
            return ResponseBean.error(false);
        }
    }
    @GetMapping("/findAll")
    public ResponseBean findAll(){
        List<SupplierVO> supplierList = supplierService.findAll();
        return ResponseBean.success(supplierList);
    }
}
