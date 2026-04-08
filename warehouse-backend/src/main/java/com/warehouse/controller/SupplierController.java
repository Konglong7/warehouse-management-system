package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Supplier;
import com.warehouse.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(supplierService.list());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(supplierService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Supplier supplier) {
        try {
            supplierService.save(supplier);
            return Result.success();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Supplier supplier) {
        supplier.setId(id);
        try {
            supplierService.update(supplier);
            return Result.success();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return Result.success();
    }
}
