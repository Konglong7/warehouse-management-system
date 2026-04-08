package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Inbound;
import com.warehouse.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inbound")
public class InboundController {
    @Autowired
    private InboundService inboundService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(inboundService.list());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(inboundService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Inbound inbound) {
        try {
            inboundService.save(inbound);
            return Result.success();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }
}
