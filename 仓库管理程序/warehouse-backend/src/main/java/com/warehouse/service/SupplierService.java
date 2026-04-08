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

    public boolean save(Supplier supplier) {
        return supplierMapper.insert(supplier) > 0;
    }

    public boolean update(Supplier supplier) {
        return supplierMapper.updateById(supplier) > 0;
    }

    public boolean delete(Long id) {
        return supplierMapper.deleteById(id) > 0;
    }
}
