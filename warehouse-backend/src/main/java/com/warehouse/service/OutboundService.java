package com.warehouse.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warehouse.entity.Material;
import com.warehouse.entity.Outbound;
import com.warehouse.mapper.MaterialMapper;
import com.warehouse.mapper.OutboundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OutboundService {
    @Autowired
    private OutboundMapper outboundMapper;

    @Autowired
    private MaterialMapper materialMapper;

    public List<Outbound> list() {
        return outboundMapper.selectList(null);
    }

    /**
     * 获取今日出库记录
     * @return 今日出库记录列表
     */
    public List<Outbound> getTodayList() {
        QueryWrapper<Outbound> wrapper = new QueryWrapper<>();
        LocalDate today = LocalDate.now();
        wrapper.ge("outbound_time", today.atStartOfDay());
        wrapper.lt("outbound_time", today.plusDays(1).atStartOfDay());
        return outboundMapper.selectList(wrapper);
    }

    public Outbound getById(Long id) {
        return outboundMapper.selectById(id);
    }

    /**
     * 出库操作 - 事务性保存
     * 使用原子操作校验并扣减库存，防止并发超卖和竞态条件
     * @param outbound 出库信息
     * @return 是否成功
     */
    @Transactional
    public boolean save(Outbound outbound) {
        // 验证物料ID
        if (outbound.getMaterialId() == null) {
            throw new RuntimeException("物料ID不能为空");
        }
        // 验证数量
        if (outbound.getQuantity() == null || outbound.getQuantity() <= 0) {
            throw new RuntimeException("出库数量必须大于0");
        }

        // 验证物料是否存在
        Material material = materialMapper.selectById(outbound.getMaterialId());
        if (material == null) {
            throw new RuntimeException("物料不存在");
        }

        // 生成出库单号（时间戳+随机数，防止高并发重复）
        outbound.setOutboundNo("OUT" + System.currentTimeMillis() + String.format("%04d", ThreadLocalRandom.current().nextInt(10000)));
        outbound.setOutboundTime(LocalDateTime.now());

        // 原子扣减库存（SQL层面校验库存充足性，返回0表示库存不足）
        int affectedRows = materialMapper.decreaseStockAtomic(outbound.getMaterialId(), outbound.getQuantity());
        if (affectedRows == 0) {
            throw new RuntimeException("库存不足，当前库存：" + material.getCurrentStock());
        }

        // 保存出库记录
        int result = outboundMapper.insert(outbound);

        return result > 0;
    }
}
