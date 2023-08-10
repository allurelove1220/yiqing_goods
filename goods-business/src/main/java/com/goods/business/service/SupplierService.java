package com.goods.business.service;

import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * @author lx
 * @createTime 2023/08/07 18:06:54
 * @className Supplierservice
 */
public interface SupplierService {
    /**
     * 分页列表查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    PageVO<SupplierVO> findSupplierList(Integer pageNum, Integer pageSize, String name,String address,String contact );

    /**
     * 添加物资来源
     * @param supplierVo
     */
    void addSupplier(SupplierVO supplierVo);

    /**
     * 回显数据
     * @param id
     * @return
     */
    Supplier getById(Long id);

    /**
     * 修改数据
     * @param id
     * @param supplier
     */
    void update(Long id, Supplier supplier);

    /**
     * 删除物资来源
     * @param id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 查询地区
     * @return
     */
    List<SupplierVO> findAll();
}
