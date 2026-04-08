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
     * 自动生成出库单号并更新库存（带库存校验）
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

        // 生成出库单号
        outbound.setOutboundNo("OUT" + System.currentTimeMillis());
        outbound.setOutboundTime(LocalDateTime.now());

        // 检查库存是否充足（使用悲观锁）
        Material material = materialMapper.selectById(outbound.getMaterialId());
        if (material == null) {
            throw new RuntimeException("物料不存在");
        }
        if (material.getCurrentStock() < outbound.getQuantity()) {
            throw new RuntimeException("库存不足，当前库存：" + material.getCurrentStock());
        }

        // 保存出库记录
        int result = outboundMapper.insert(outbound);

        // 更新物料库存
        material.setCurrentStock(material.getCurrentStock() - outbound.getQuantity());
        materialMapper.updateById(material);

        return result > 0;
    }
}
