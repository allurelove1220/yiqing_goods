package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.*;
import com.goods.business.service.OutStockService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.*;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.*;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lx
 * @createTime 2023/08/09 14:33:00
 * @className OutStockVOServiceImpl
 */
@Service
public class OutStockServiceImpl implements OutStockService {

    @Autowired
    private OutStockMapper outStockMapper;
    @Autowired
    private ConsumerMapper consumerMapper;
    @Autowired
    private OutStockInfoMapper outStockInfoMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param status
     * @param outStockVO
     * @return
     */
    @Override
    public PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, Integer status, OutStockVO outStockVO) {
        Example example = new Example(OutStock.class);
        if (null != outStockVO.getOutNum()){
            example.createCriteria().andEqualTo("outNum",outStockVO.getOutNum());
        }
        if (null != outStockVO.getType()){
            example.createCriteria().andEqualTo("type",outStockVO.getType());
        }
        if (null != status){
            example.createCriteria().andEqualTo("status",status);
        }
        List<OutStock> outStocks = outStockMapper.selectByExample(example);
        List<OutStockVO> outStockVOS =new ArrayList<>();
        for (OutStock outStock : outStocks) {
            OutStockVO outStockVO1 = new OutStockVO();
            BeanUtils.copyProperties(outStock, outStockVO1);
            Long consumerId = outStockVO1.getConsumerId();
            System.out.println("consumerId = " + consumerId);
            Consumer consumer = consumerMapper.selectByPrimaryKey(outStockVO1.getConsumerId());
            String name = consumer.getName();
            System.out.println("name = " + name);
            outStockVO1.setName(consumer.getName());
            outStockVO1.setPhone(consumer.getPhone());
            outStockVOS.add(outStockVO1);
        }
        PageInfo<OutStockVO> pageInfo =new PageInfo<>(outStockVOS);
        PageHelper.startPage(pageNum,pageSize);
        PageVO<OutStockVO> outStockVOPageVO = new PageVO<>(pageInfo.getTotal(), outStockVOS);
        return outStockVOPageVO;
    }

    /**
     * 明细
     * @param id
     * @param pageNum
     * @return
     */
    @Override
    public Map<String, Object> detail(Long id, Integer pageNum) {
        Map<String,Object> map =new HashMap<>();
        List<OutStockItemVO> outStockItemVOS = outStockMapper.findOUtStockDetail(id,pageNum);
        ConsumerVO consumerVO = outStockMapper.getConsumer(id);
        map.put("itemVOS", outStockItemVOS);
        map.put("total", outStockItemVOS.size());
        map.put("consumerVO", consumerVO);
        map.put("status", outStockMapper.selectByPrimaryKey(id).getStatus());
        return map;
    }

    /**
     * 物资去处
     * @param outStockVO
     * @param username
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("all")
    public void addOutStock(OutStockVO outStockVO, String username) {
        //获取detail
        List<Object> products = outStockVO.getProducts();
        //准备outStock
        OutStock outStock =new OutStock();
        BeanUtils.copyProperties(outStockVO,outStock);
        // 生成编号
        String outNum = UUID.randomUUID().toString().replaceAll("-", "");
        //判断是否存在ConsumerId
        Long consumerId = outStockVO.getConsumerId();
        if (consumerId == null ){
            //保存consumer
            Consumer consumer =new Consumer();
            consumer.setCreateTime(new Date());
            consumer.setModifiedTime(new Date());
            BeanUtils.copyProperties(outStockVO,consumer);
            consumerMapper.insert(consumer);
            outStock.setConsumerId(consumer.getId());
        }
        //保存 明细
        AtomicInteger atomicInteger = new AtomicInteger();
        products.forEach(product -> {
            Map map = (Map) product;
            OutStockInfo outStockInfo = new OutStockInfo();
            outStockInfo.setCreateTime(new Date());
            outStockInfo.setModifiedTime(new Date());
            outStockInfo.setOutNum(outNum);
            outStockInfo.setPNum(productMapper.selectByPrimaryKey(map.get("productId")).getPNum());
            outStockInfo.setProductNumber((Integer) map.get("productNumber"));
            atomicInteger.addAndGet((Integer) map.get("productNumber"));
            outStockInfoMapper.insert(outStockInfo);
        });
        //保存 outStock
        outStock.setOutNum(outNum);
        outStock.setCreateTime(new Date());
        outStock.setProductNumber(atomicInteger.get());
        outStock.setStatus(2);
        outStock.setOperator(username);
        outStockMapper.insert(outStock);
    }

    @Override
    public void publish(Long id) throws BusinessException {
        try {
            extracted(id, 0);
            //减库存
            OutStock outStock = outStockMapper.selectByPrimaryKey(id);
            Example E1 = new Example(OutStock.class);
            Example.Criteria C1 = E1.createCriteria();
            C1.andEqualTo("outNum", outStock.getOutNum());
            List<OutStockInfo> outStockInfos = outStockInfoMapper.selectByExample(E1);
            outStockInfos.forEach(info -> {
                Example E2 = new Example(ProductStock.class);
                Example.Criteria C2 = E2.createCriteria();
                C2.andEqualTo("pNum", info.getPNum());
                List<ProductStock> productStocks = productStockMapper.selectByExample(E2);
                ProductStock productStock = productStocks.get(0);
                productStock.setStock(productStock.getStock() - info.getProductNumber());
                productStockMapper.updateByPrimaryKey(productStock);
            });
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("出库失败！"));
        }
    }

    /**
     * 删除数据
     * @param id
     */
    @Override
    public void delete(Long id) {
        outStockMapper.deleteByPrimaryKey(id);
    }

    private void extracted(Long id, Integer status) {
        OutStock outStock = outStockMapper.selectByPrimaryKey(id);
        outStock.setStatus(status);
        outStock.setId(id);
        outStockMapper.updateByPrimaryKey(outStock);
    }
}
