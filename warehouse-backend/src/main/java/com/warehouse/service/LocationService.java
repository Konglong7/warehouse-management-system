package com.warehouse.service;

import com.warehouse.entity.Location;
import com.warehouse.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationMapper locationMapper;

    public List<Location> list() {
        return locationMapper.selectList(null);
    }

    public Location getById(Long id) {
        return locationMapper.selectById(id);
    }

    /**
     * 保存仓位
     * @param location 仓位信息
     * @return 是否成功
     */
    public boolean save(Location location) {
        // 验证必填字段
        if (location.getLocationCode() == null || location.getLocationCode().trim().isEmpty()) {
            throw new RuntimeException("仓位编码不能为空");
        }
        if (location.getLocationName() == null || location.getLocationName().trim().isEmpty()) {
            throw new RuntimeException("仓位名称不能为空");
        }
        if (location.getWarehouse() == null || location.getWarehouse().trim().isEmpty()) {
            throw new RuntimeException("所属仓库不能为空");
        }
        if (location.getCapacity() == null || location.getCapacity() <= 0) {
            throw new RuntimeException("容量必须大于0");
        }
        // 默认状态
        if (location.getStatus() == null || location.getStatus().trim().isEmpty()) {
            location.setStatus("available");
        }
        return locationMapper.insert(location) > 0;
    }

    public boolean update(Location location) {
        // 验证必填字段
        if (location.getLocationName() == null || location.getLocationName().trim().isEmpty()) {
            throw new RuntimeException("仓位名称不能为空");
        }
        if (location.getWarehouse() == null || location.getWarehouse().trim().isEmpty()) {
            throw new RuntimeException("所属仓库不能为空");
        }
        if (location.getCapacity() == null || location.getCapacity() <= 0) {
            throw new RuntimeException("容量必须大于0");
        }
        return locationMapper.updateById(location) > 0;
    }

    public boolean delete(Long id) {
        return locationMapper.deleteById(id) > 0;
    }
}
