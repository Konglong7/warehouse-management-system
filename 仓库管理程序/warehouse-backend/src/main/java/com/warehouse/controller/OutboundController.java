package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Outbound;
import com.warehouse.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/outbound")
public class OutboundController {
    @Autowired
    private OutboundService outboundService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(outboundService.list());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(outboundService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Outbound outbound) {
        try {
            outboundService.save(outbound);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
