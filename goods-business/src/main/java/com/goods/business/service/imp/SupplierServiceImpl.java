package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.SupplierMapper;
import com.goods.business.service.SupplierService;
import com.goods.common.model.business.Supplier;
import com.goods.common.model.system.User;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author lx
 * @createTime 2023/08/07 18:07:38
 * @className SupplierServiceImpl
 */
@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 分页列表查询
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public PageVO<SupplierVO> findSupplierList(Integer pageNum, Integer pageSize, String name, String address, String contact) {
        // PageHelper.startPage(pageNum,pageSize);
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

        List<Supplier> suppliers = supplierMapper.selectByExample(example);
        List<SupplierVO> supplierVOList = new ArrayList<>();
        suppliers.forEach(s -> {
            SupplierVO supplierVO = new SupplierVO();
            BeanUtils.copyProperties(s, supplierVO);
            supplierVOList.add(supplierVO);
        });
        PageInfo<SupplierVO> pageInfo = new PageInfo<>(supplierVOList);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPages(pageSize);
        return new PageVO<>(pageInfo.getTotal(), supplierVOList);
    }

    /**
     * 添加物资来源
     *
     * @param supplierVO
     */
    @Override
    public void addSupplier(SupplierVO supplierVO) {
        Supplier supplier =new Supplier();
        BeanUtils.copyProperties(supplierVO,supplier);
        supplier.setCreateTime(new Date());
        supplier.setModifiedTime(new Date());
        supplierMapper.insert(supplier);
    }

    /**
     * 回显数据
     *
     * @param id
     * @return
     */
    @Override
    public Supplier getById(Long id) {
        Supplier supplier = new Supplier();
        supplier.setId(id);
        return supplierMapper.selectOne(supplier);
    }

    /**
     * 修改数据
     *
     * @param id
     * @param supplier
     */
    @Override
    public void update(Long id, Supplier supplier) {
        supplierMapper.updateByPrimaryKey(supplier);
    }

    /**
     * 删除物资来源
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delete(Long id) {
        Supplier supplier = new Supplier();
        supplier.setId(id);
        int i = supplierMapper.delete(supplier);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 地区列表
     * @return
     */
    @Override
    public List<SupplierVO> findAll() {
        List<Supplier> supplierList = supplierMapper.selectAll();
        List<SupplierVO> supplierVOS = new ArrayList<>();
        supplierList.forEach(supplier -> {
            SupplierVO supplierVO =new SupplierVO();
            BeanUtils.copyProperties(supplier,supplierVO);
            supplierVOS.add(supplierVO);
        });
        return supplierVOS;
    }
}
