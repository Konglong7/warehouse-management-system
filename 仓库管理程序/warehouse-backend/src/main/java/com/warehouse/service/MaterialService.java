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

    public boolean save(Material material) {
        return materialMapper.insert(material) > 0;
    }

    public boolean update(Material material) {
        return materialMapper.updateById(material) > 0;
    }

    public boolean delete(Long id) {
        return materialMapper.deleteById(id) > 0;
    }

    public List<Material> getWarningList() {
        QueryWrapper<Material> wrapper = new QueryWrapper<>();
        wrapper.apply("current_stock <= warning_stock");
        return materialMapper.selectList(wrapper);
    }
}
