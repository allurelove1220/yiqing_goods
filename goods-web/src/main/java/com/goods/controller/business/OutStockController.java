package com.goods.controller.business;

import com.goods.business.service.OutStockService;
import com.goods.common.error.BusinessException;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.JWTUtils;
import com.goods.common.vo.business.OutStockDetailVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * @author lx
 * @createTime 2023/08/09 14:29:17
 * @className OutStockController
 */
@RestController
@RequestMapping("/business/outStock")
public class OutStockController {
    @Autowired
    private OutStockService outStockService;

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @param outStockVO
     * @return
     */
    @GetMapping("/findOutStockList")
    public ResponseBean findOutStockList(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         @RequestParam Integer status,
                                         OutStockVO outStockVO) {
        PageVO<OutStockVO> outStockVOPageVO = outStockService.findOutStockList(pageNum, pageSize, status, outStockVO);
        return ResponseBean.success(outStockVOPageVO);
    }

    /**
     * 出库明细列表
     *
     * @param id
     * @param pageNum
     * @return
     */
    @GetMapping("/detail/{id}")
    public ResponseBean detail(@PathVariable Long id,
                               @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        Map<String, Object> map = outStockService.detail(id, pageNum);
        return ResponseBean.success(map);
    }

    /**
     * 出库
     *
     * @param outStockVO
     * @return
     */
    @PostMapping("/addOutStock")
    public ResponseBean addOutStock(@RequestBody OutStockVO outStockVO , HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = JWTUtils.getUsername(token);
        outStockService.addOutStock(outStockVO,username);
        return ResponseBean.success();
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @PutMapping("/publish/{id}")
    public ResponseBean publish (@PathVariable Long id) throws BusinessException {
        outStockService.publish(id);
        return ResponseBean.success();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id){
        outStockService.delete(id);
        return ResponseBean.success();
    }
}
