package com.goods.controller.business;


import com.goods.business.service.InStockInfoService;
import com.goods.business.service.InStockService;
import com.goods.common.model.business.InStock;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.JWTUtils;
import com.goods.common.vo.business.*;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author lx
 * @createTime 2023/08/08 12:54:53
 * @className InStockController
 */
@RestController
@RequestMapping("/business/inStock")
public class InStockController {
    @Autowired
    private InStockService inStockService;
    @Autowired
    private InStockInfoService inStockInfoService;

    /**
     * 分页查询列表
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/findInStockList")
    public ResponseBean findInStockList(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam Integer status,
                                        InStockVO inStockVO){
        PageVO<InStockVO> inStockVOPageVO = inStockService.findInStockList(pageNum,pageSize,status,inStockVO);
        return ResponseBean.success(inStockVOPageVO);
    }

    /**
     * 明细列表
     * @param id
     * @param pageNum
     * @return
     */
    @GetMapping("/detail/{id}")
    public ResponseBean detail(@PathVariable Long id, @RequestParam Integer pageNum){
        InStockDetailVO inStockDetailVO = inStockService.findDetailList(id,pageNum);
        return ResponseBean.success(inStockDetailVO);
    }

    @PostMapping("/addIntoStock")
    public ResponseBean addIntoStock(@RequestBody @Validated InStockVO inStockVO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtils.getUsername(token);
        Boolean flag = inStockService.addIntoStock(inStockVO,username);
        if (flag){
            return ResponseBean.success();
        }else {
            return ResponseBean.error(false);
        }
    }
    /**
     * 通过审核
     * @param id
     * @return
     */
    @PutMapping("/publish/{id}")
    public ResponseBean publish(@PathVariable Long id){
        inStockService.publish(id);
        return ResponseBean.success();
    }

    /**
     * 删除数据
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id){
        inStockService.delete(id);
        return ResponseBean.success();
    }
}
