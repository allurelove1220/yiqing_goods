package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.ConsumerMapper;
import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.ConsumerVO;
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
 * @createTime 2023/08/08 16:46:51
 * @className ConsumerServiceImpl
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Autowired
    private ConsumerMapper consumerMapper;
    /**
     * 分页列表查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, String name, String address, String contact) {
        PageHelper.startPage(pageNum,pageSize);
        Example example = new Example(Supplier.class);
        Example.Criteria criteria = example.createCriteria();
        if (!name.isEmpty()) {
            name = "%" + name + "%";
            criteria.andLike("name", name);
        }
        if (address != null) {
            address = "%" + address + "%";
            criteria.andLike("address", address);
        }
        if (contact != null) {
            contact = "%" + contact + "%";
            criteria.andLike("contact", contact);
        }
        List<Consumer> consumers = consumerMapper.selectByExample(example);
        List<ConsumerVO> consumerVOS = new ArrayList<>();
        consumers.forEach(consumer -> {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer,consumerVO);
            consumerVOS.add(consumerVO);
        });
        PageInfo<ConsumerVO> pageInfo = new PageInfo<>(consumerVOS);
        return new PageVO<>(pageInfo.getTotal(),consumerVOS);
    }

    /**
     * 添加物资去向
     * @param consumerVO
     */
    @Override
    public void addConsumer(ConsumerVO consumerVO) {
        Consumer consumer =new Consumer();
        BeanUtils.copyProperties(consumerVO,consumer);
        consumer.setCreateTime(new Date());
        consumer.setModifiedTime(new Date());
        consumerMapper.insert(consumer);
    }

    /**
     * 回显数据
     * @param id
     * @return
     */
    @Override
    public Consumer getById(Long id) {
        Consumer consumer = consumerMapper.selectByPrimaryKey(id);
        return consumer;
    }

    /**
     * 修改数据
     * @param id
     * @param consumer
     */
    @Override
    public void update(Long id, Consumer consumer) {
        consumer.setId(id);
        consumerMapper.updateByPrimaryKey(consumer);
    }

    /**
     * 删除数据
     * @param id
     */
    @Override
    public void delete(Long id) {
        consumerMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询地区
     * @return
     */
    @Override
    public List<ConsumerVO> findAll() {
        List<Consumer> consumers = consumerMapper.selectAll();
        List<ConsumerVO> consumerVOS = new ArrayList<>();
        consumers.forEach(consumer -> {
            ConsumerVO consumerVO =new ConsumerVO();
            BeanUtils.copyProperties(consumer,consumerVO);
            consumerVOS.add(consumerVO);
        });
        return consumerVOS;
    }
}
