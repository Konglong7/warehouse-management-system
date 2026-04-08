package com.warehouse.service;

import com.warehouse.entity.Material;
import com.warehouse.entity.Outbound;
import com.warehouse.mapper.MaterialMapper;
import com.warehouse.mapper.OutboundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
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

    public Outbound getById(Long id) {
        return outboundMapper.selectById(id);
    }

    @Transactional
    public boolean save(Outbound outbound) {
        // 生成出库单号
        outbound.setOutboundNo("OUT" + System.currentTimeMillis());
        outbound.setOutboundTime(LocalDateTime.now());

        // 检查库存是否充足
        Material material = materialMapper.selectById(outbound.getMaterialId());
        if (material == null || material.getCurrentStock() < outbound.getQuantity()) {
            throw new RuntimeException("库存不足");
        }

        // 保存出库记录
        int result = outboundMapper.insert(outbound);

        // 更新物料库存
        material.setCurrentStock(material.getCurrentStock() - outbound.getQuantity());
        materialMapper.updateById(material);

        return result > 0;
    }
}
