package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.mapper.InboundMapper;
import com.warehouse.mapper.OutboundMapper;
import com.warehouse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    @Autowired
    private MaterialService materialService;

    @Autowired
    private InboundService inboundService;

    @Autowired
    private OutboundService outboundService;

    @Autowired
    private InboundMapper inboundMapper;

    @Autowired
    private OutboundMapper outboundMapper;

    /**
     * Dashboard统计 - 使用数据库聚合查询替代全表扫描
     */
    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        Map<String, Object> data = new HashMap<>();
        // 总库存（数据库SUM聚合）
        data.put("totalStock", materialService.getTotalStock());
        // 今日入库数（数据库COUNT）
        data.put("todayInbound", inboundMapper.getTodayCount());
        // 今日出库数（数据库COUNT）
        data.put("todayOutbound", outboundMapper.getTodayCount());
        // 预警物料数（数据库COUNT）
        data.put("warningCount", materialService.getWarningCount());
        return Result.success(data);
    }

    @GetMapping("/inventory")
    public Result<?> inventory() {
        return Result.success(materialService.list());
    }

    @GetMapping("/trend")
    public Result<?> trend() {
        Map<String, Object> data = new HashMap<>();
        data.put("inboundList", inboundService.list());
        data.put("outboundList", outboundService.list());
        return Result.success(data);
    }
}
