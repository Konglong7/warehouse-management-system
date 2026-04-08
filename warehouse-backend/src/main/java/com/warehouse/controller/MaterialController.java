package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Material;
import com.warehouse.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(materialService.list());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(materialService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Material material) {
        try {
            materialService.save(material);
            return Result.success();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Material material) {
        material.setId(id);
        try {
            materialService.update(material);
            return Result.success();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        materialService.delete(id);
        return Result.success();
    }

    @GetMapping("/warning")
    public Result<?> getWarningList() {
        return Result.success(materialService.getWarningList());
    }
}
