package com.warehouse.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.warehouse.entity.Material;
import com.warehouse.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private MaterialMapper materialMapper;

    public List<Material> list() {
        return materialMapper.selectList(null);
    }

    public Material getById(Long id) {
        return materialMapper.selectById(id);
    }

    /**
     * 保存物料
     * @param material 物料信息
     * @return 是否成功
     */
    public boolean save(Material material) {
        // 基本验证
        if (material.getMaterialCode() == null || material.getMaterialCode().trim().isEmpty()) {
            throw new RuntimeException("物料编码不能为空");
        }
        if (material.getMaterialName() == null || material.getMaterialName().trim().isEmpty()) {
            throw new RuntimeException("物料名称不能为空");
        }
        if (material.getUnit() == null || material.getUnit().trim().isEmpty()) {
            throw new RuntimeException("单位不能为空");
        }
        // 检查物料编码是否重复
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.eq("material_code", material.getMaterialCode());
        if (materialMapper.selectOne(wrapper) != null) {
            throw new RuntimeException("物料编码已存在");
        }
        // 初始化库存
        if (material.getCurrentStock() == null) {
            material.setCurrentStock(0);
        }
        if (material.getWarningStock() == null) {
            material.setWarningStock(10);
        }
        return materialMapper.insert(material) > 0;
    }

    public boolean update(Material material) {
        // 基本验证
        if (material.getMaterialName() == null || material.getMaterialName().trim().isEmpty()) {
            throw new RuntimeException("物料名称不能为空");
        }
        if (material.getUnit() == null || material.getUnit().trim().isEmpty()) {
            throw new RuntimeException("单位不能为空");
        }
        return materialMapper.updateById(material) > 0;
    }

    public boolean delete(Long id) {
        return materialMapper.deleteById(id) > 0;
    }

    /**
     * 获取库存预警列表
     * @return 库存不足的物料列表
     */
    public List<Material> getWarningList() {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.apply("COALESCE(current_stock, 0) <= COALESCE(warning_stock, 0)");
        return materialMapper.selectList(wrapper);
    }

    /**
     * 获取总库存量（数据库聚合查询）
     * @return 总库存
     */
    public int getTotalStock() {
        return materialMapper.getTotalStock();
    }

    /**
     * 获取预警物料数量（数据库聚合查询）
     * @return 预警数量
     */
    public int getWarningCount() {
        return materialMapper.getWarningCount();
    }
}
