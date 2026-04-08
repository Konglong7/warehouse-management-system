package com.warehouse.service;

import com.warehouse.entity.Inbound;
import com.warehouse.entity.Material;
import com.warehouse.mapper.InboundMapper;
import com.warehouse.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InboundService {
    @Autowired
    private InboundMapper inboundMapper;

    @Autowired
    private MaterialMapper materialMapper;

    public List<Inbound> list() {
        return inboundMapper.selectList(null);
    }

    public Inbound getById(Long id) {
        return inboundMapper.selectById(id);
    }

    @Transactional
    public boolean save(Inbound inbound) {
        // 生成入库单号
        inbound.setInboundNo("IN" + System.currentTimeMillis());
        inbound.setInboundTime(LocalDateTime.now());

        // 保存入库记录
        int result = inboundMapper.insert(inbound);

        // 更新物料库存
        Material material = materialMapper.selectById(inbound.getMaterialId());
        if (material != null) {
            material.setCurrentStock(material.getCurrentStock() + inbound.getQuantity());
            materialMapper.updateById(material);
        }

        return result > 0;
    }
}
