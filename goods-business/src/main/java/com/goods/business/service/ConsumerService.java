package com.goods.business.service;

import com.goods.common.model.business.Consumer;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/08 16:46:35
 * @className ConsumerService
 */
public interface ConsumerService {
    /**
     * 分页列表查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, String name, String address, String contact);

    /**
     * 添加物资去向
     * @param consumerVO
     */
    void addConsumer(ConsumerVO consumerVO);

    /**
     * 回显数据
     * @param id
     * @return
     */
    Consumer getById(Long id);

    /**
     * 修改数据
     * @param id
     * @param consumer
     */
    void update(Long id, Consumer consumer);

    /**
     * 删除数据
     * @param id
     */
    void delete(Long id);

    /**
     * 查询地区
     * @return
     */
    List<ConsumerVO> findAll();
}
