package com.warehouse.service;

import com.warehouse.entity.Supplier;
import com.warehouse.mapper.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierMapper supplierMapper;

    public List<Supplier> list() {
        return supplierMapper.selectList(null);
    }

    public Supplier getById(Long id) {
        return supplierMapper.selectById(id);
    }

    /**
     * 保存供应商
     * @param supplier 供应商信息
     * @return 是否成功
     */
    public boolean save(Supplier supplier) {
        // 验证必填字段
        if (supplier.getSupplierCode() == null || supplier.getSupplierCode().trim().isEmpty()) {
            throw new RuntimeException("供应商编码不能为空");
        }
        if (supplier.getSupplierName() == null || supplier.getSupplierName().trim().isEmpty()) {
            throw new RuntimeException("供应商名称不能为空");
        }
        if (supplier.getContact() == null || supplier.getContact().trim().isEmpty()) {
            throw new RuntimeException("联系人不能为空");
        }
        if (supplier.getPhone() == null || supplier.getPhone().trim().isEmpty()) {
            throw new RuntimeException("联系电话不能为空");
        }
        return supplierMapper.insert(supplier) > 0;
    }

    public boolean update(Supplier supplier) {
        // 验证必填字段
        if (supplier.getSupplierName() == null || supplier.getSupplierName().trim().isEmpty()) {
            throw new RuntimeException("供应商名称不能为空");
        }
        if (supplier.getContact() == null || supplier.getContact().trim().isEmpty()) {
            throw new RuntimeException("联系人不能为空");
        }
        if (supplier.getPhone() == null || supplier.getPhone().trim().isEmpty()) {
            throw new RuntimeException("联系电话不能为空");
        }
        return supplierMapper.updateById(supplier) > 0;
    }

    public boolean delete(Long id) {
        return supplierMapper.deleteById(id) > 0;
    }
}
