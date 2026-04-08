package com.warehouse.controller;

import com.warehouse.common.Result;
import com.warehouse.entity.Location;
import com.warehouse.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(locationService.list());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(locationService.getById(id));
    }

    @PostMapping
    public Result<?> save(@RequestBody Location location) {
        try {
            locationService.save(location);
            return Result.success();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Location location) {
        location.setId(id);
        try {
            locationService.update(location);
            return Result.success();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        locationService.delete(id);
        return Result.success();
    }
}
