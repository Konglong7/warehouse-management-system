package com.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warehouse.entity.Outbound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OutboundMapper extends BaseMapper<Outbound> {

    /**
     * 查询今日出库记录数
     * @return 今日出库数量
     */
    @Select("SELECT COUNT(*) FROM wms_outbound WHERE DATE(outbound_time) = CURDATE()")
    int getTodayCount();
}
