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

    public boolean save(Location location) {
        return locationMapper.insert(location) > 0;
    }

    public boolean update(Location location) {
        return locationMapper.updateById(location) > 0;
    }

    public boolean delete(Long id) {
        return locationMapper.deleteById(id) > 0;
    }
}
