package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.*;
import com.goods.business.service.InStockService;
import com.goods.business.service.SupplierService;
import com.goods.common.model.business.*;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.BrowserUtil;
import com.goods.common.vo.business.*;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lx
 * @createTime 2023/08/08 12:59:04
 * @className InStockServiceImpl
 */
@Service
public class InStockServiceImpl implements InStockService {
    @Autowired
    private InStockMapper inStockMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private InStockInfoMapper inStockInfoMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductStockMapper productStockMapper;

    /**
     * 分页查询列表
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize, Integer status, InStockVO inStockVO) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(InStock.class);
        if (null != inStockVO.getType()) {
            example.createCriteria().andEqualTo("type", inStockVO.getType());
        }
        if (null != inStockVO.getInNum()) {
            example.createCriteria().andEqualTo("inNum", inStockVO.getInNum());
        }
        if (null != status) {
            example.createCriteria().andEqualTo("status", status);
        }
        if (inStockVO.getStartTime() != null && inStockVO.getEndTime() != null) {
            example.createCriteria().andBetween("createTime", inStockVO.getStartTime(), inStockVO.getEndTime());
        }
        List<InStock> inStocks = inStockMapper.selectByExample(example);
        List<InStockVO> inStockVOS = new ArrayList<>();
        inStocks.forEach(inStock -> {
            InStockVO inStockVO1 = new InStockVO();
            BeanUtils.copyProperties(inStock,inStockVO1);

            Long supplierId = inStockVO1.getSupplierId();
            Supplier supplier = supplierMapper.selectByPrimaryKey(supplierId);
            inStockVO1.setSupplierName(supplier.getName());
            inStockVO1.setPhone(supplier.getPhone());
            inStockVOS.add(inStockVO1);
        });
        PageInfo<InStockVO> pageInfo = new PageInfo<>(inStockVOS);
        return new PageVO<>(pageInfo.getTotal(), inStockVOS);
    }

    /**
     * 物资明细
     *
     * @param id
     * @param pageNum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("all")
    public InStockDetailVO findDetailList(Long id, Integer pageNum) {
        InStock inStock =new InStock();
        inStock.setId(id);
        InStock inStock1 = inStockMapper.selectByPrimaryKey(id);

        String inNum=inStock1.getInNum();
        Long supplierId = inStock1.getSupplierId();
        Supplier supplier = supplierMapper.selectByPrimaryKey(supplierId);
        SupplierVO supplierVO = new SupplierVO();
        BeanUtils.copyProperties(supplier,supplierVO);

        InStockInfo inStockInfo1 = new InStockInfo();
        inStockInfo1.setInNum(inNum);

        List<InStockInfo> select = inStockInfoMapper.select(inStockInfo1);
        List<InStockItemVO> inStockItemVOList = new ArrayList<>();
        select.forEach(s->{
            Product product = new Product();
            product.setPNum(s.getPNum());
            Product product1 = productMapper.selectOne(product);
            InStockItemVO inStockItemVO = new InStockItemVO();
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product1,inStockItemVO);
            inStockItemVO.setCount(s.getProductNumber());

            inStockItemVOList.add(inStockItemVO);
        });


        InStockDetailVO inStockDetailVO = new InStockDetailVO();
        inStockDetailVO.setSupplierVO(supplierVO);
        inStockDetailVO.setItemVOS(inStockItemVOList);
        BeanUtils.copyProperties(inStock1,inStockDetailVO);

        PageInfo<InStockItemVO> productVOPageInfo = new PageInfo<InStockItemVO>(inStockItemVOList);
        productVOPageInfo.setPageNum(pageNum);
        productVOPageInfo.setPageSize(20);
        List<InStockDetailVO> inStockDetailVOS = new ArrayList<>();
        inStockDetailVOS.add(inStockDetailVO);
        PageVO<InStockDetailVO> InStockDetailVOspage = new PageVO<InStockDetailVO>(productVOPageInfo.getTotal(),inStockDetailVOS);
        return inStockDetailVO;
    }

    /**
     * 添加入库单(物资入库)
     * @param inStockVO
     * @return
     */
    @Override
    public Boolean addIntoStock(InStockVO inStockVO,String username) {
        List<Object> products = inStockVO.getProducts();

        InStock inStock = new InStock();
        inStock.setInNum(UUID.randomUUID().toString().replaceAll("-", ""));
        BeanUtils.copyProperties(inStockVO, inStock);

        //判断suppId是否存在
        Long supplierId = inStockVO.getSupplierId();
        Supplier supplier = new Supplier();
        if (null == supplierId) {
            supplier.setCreateTime(new Date());
            supplier.setModifiedTime(new Date());
            BeanUtils.copyProperties(inStockVO, supplier);
            supplierMapper.insert(supplier);
            inStock.setSupplierId(supplier.getId());
        }

        //存储入库明细
        AtomicInteger atomicInteger = new AtomicInteger();
        products.stream().forEach(product -> {
            InStockInfo inStockInfo = new InStockInfo();
            inStockInfo.setInNum(inStock.getInNum());
            Map map = (Map) product;
            inStockInfo.setPNum(productMapper.selectByPrimaryKey(map.get("productId")).getPNum());
            inStockInfo.setProductNumber((Integer) map.get("productNumber"));
            inStockInfo.setCreateTime(new Date());
            inStockInfo.setModifiedTime(new Date());
            atomicInteger.addAndGet((Integer) map.get("productNumber"));
            inStockInfoMapper.insert(inStockInfo);
        });

        //如果suppId不存在 进行入库操作
        inStock.setCreateTime(new Date());
        inStock.setStatus(0);
        inStock.setOperator(username);
        inStock.setModified(new Date());
        inStock.setProductNumber(atomicInteger.get());
        inStockMapper.insert(inStock);
        return true;
    }



    /**
     * 删除数据
     * @param id
     */
    @Override
    public void delete(Long id) {
        inStockMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过审核
     * @param id
     */
    @Override
    public void publish(Long id) {
        InStock inStock = inStockMapper.selectByPrimaryKey(id); // inStock == null
        if (null!=inStock.getStatus() && inStock.getStatus() == 2){
            inStock.setStatus(0);
        }
        inStockMapper.updateByPrimaryKeySelective(inStock);
    }
}
