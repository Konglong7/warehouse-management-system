package com.warehouse.controller;

import com.warehouse.common.Result;
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

    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        Map<String, Object> data = new HashMap<>();

        // 总库存
        int totalStock = materialService.list().stream()
                .mapToInt(m -> m.getCurrentStock())
                .sum();
        data.put("totalStock", totalStock);

        // 今日入库
        data.put("todayInbound", inboundService.list().size());

        // 今日出库
        data.put("todayOutbound", outboundService.list().size());

        // 预警物料数量
        data.put("warningCount", materialService.getWarningList().size());

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
