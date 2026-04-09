package com.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warehouse.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MaterialMapper extends BaseMapper<Material> {

    /**
     * 原子更新库存（增加或减少）
     * @param id 物料ID
     * @param delta 变化量（正数为增加，负数为减少）
     * @return 影响行数
     */
    @Update("UPDATE wms_material SET current_stock = current_stock + #{delta} WHERE id = #{id}")
    int updateStockAtomic(@Param("id") Long id, @Param("delta") int delta);

    /**
     * 原子扣减库存（带库存不足校验）
     * @param id 物料ID
     * @param quantity 扣减数量
     * @return 影响行数（0表示库存不足）
     */
    @Update("UPDATE wms_material SET current_stock = current_stock - #{quantity} WHERE id = #{id} AND current_stock >= #{quantity}")
    int decreaseStockAtomic(@Param("id") Long id, @Param("quantity") int quantity);

    /**
     * 查询总库存量
     * @return 总库存
     */
    @Select("SELECT COALESCE(SUM(current_stock), 0) FROM wms_material")
    int getTotalStock();

    /**
     * 查询库存预警数量
     * @return 预警物料数
     */
    @Select("SELECT COUNT(*) FROM wms_material WHERE COALESCE(current_stock, 0) <= COALESCE(warning_stock, 0)")
    int getWarningCount();
}
