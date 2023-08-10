package com.goods.controller.business;

import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/08 16:41:28
 * @className ConsumerController
 */
@RestController
@RequestMapping("/business/consumer")
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;
    @GetMapping("/findConsumerList")
    public ResponseBean findConsumerList(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         @RequestParam String name,
                                         @RequestParam(value = "address",required = false) String address,
                                         @RequestParam(value = "contact",required = false) String contact){
        PageVO<ConsumerVO> consumerVOPageVO = consumerService.findConsumerList(pageNum,pageSize,name, address,contact);
        return ResponseBean.success(consumerVOPageVO);
    }
    @PostMapping("/add")
    public ResponseBean add(@RequestBody @Validated ConsumerVO consumerVO){
        consumerService.addConsumer(consumerVO);
        return ResponseBean.success();
    }

    /**
     * 回显数据
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public ResponseBean getById(@PathVariable Long id){
        Consumer consumer = consumerService.getById(id);
        return ResponseBean.success(consumer);
    }

    /**
     * 修改数据
     * @param id
     * @param consumer
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseBean update(@PathVariable Long id,@RequestBody Consumer consumer){
        consumerService.update(id,consumer);
        return ResponseBean.success();
    }

    /**
     * 删除数据
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id){
        consumerService.delete(id);
        return ResponseBean.success();
    }

    @GetMapping("/findAll")
    public ResponseBean findAll(){
        List<ConsumerVO> consumerVOS = consumerService.findAll();
        return ResponseBean.success(consumerVOS);
    }
}

