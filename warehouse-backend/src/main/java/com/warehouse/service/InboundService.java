package com.warehouse.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warehouse.entity.Inbound;
import com.warehouse.entity.Material;
import com.warehouse.mapper.InboundMapper;
import com.warehouse.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InboundService {
    @Autowired
    private InboundMapper inboundMapper;

    @Autowired
    private MaterialMapper materialMapper;

    public List<Inbound> list() {
        return inboundMapper.selectList(null);
    }

    /**
     * 获取今日入库记录
     * @return 今日入库记录列表
     */
    public List<Inbound> getTodayList() {
        QueryWrapper<Inbound> wrapper = new QueryWrapper<>();
        LocalDate today = LocalDate.now();
        wrapper.ge("inbound_time", today.atStartOfDay());
        wrapper.lt("inbound_time", today.plusDays(1).atStartOfDay());
        return inboundMapper.selectList(wrapper);
    }

    public Inbound getById(Long id) {
        return inboundMapper.selectById(id);
    }

    /**
     * 入库操作 - 事务性保存
     * 自动生成入库单号并使用原子操作更新库存，防止并发竞态条件
     * @param inbound 入库信息
     * @return 是否成功
     */
    @Transactional
    public boolean save(Inbound inbound) {
        // 验证物料ID
        if (inbound.getMaterialId() == null) {
            throw new RuntimeException("物料ID不能为空");
        }
        // 验证数量
        if (inbound.getQuantity() == null || inbound.getQuantity() <= 0) {
            throw new RuntimeException("入库数量必须大于0");
        }

        // 验证物料是否存在
        Material material = materialMapper.selectById(inbound.getMaterialId());
        if (material == null) {
            throw new RuntimeException("物料不存在");
        }

        // 生成入库单号（时间戳+随机数，防止高并发重复）
        inbound.setInboundNo("IN" + System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
        inbound.setInboundTime(LocalDateTime.now());

        // 保存入库记录
        int result = inboundMapper.insert(inbound);

        // 原子更新库存（防止并发竞态条件）
        materialMapper.updateStockAtomic(inbound.getMaterialId(), inbound.getQuantity());

        return result > 0;
    }
}
