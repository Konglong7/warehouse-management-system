package com.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warehouse.entity.Inbound;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InboundMapper extends BaseMapper<Inbound> {

    /**
     * 查询今日入库记录数
     * @return 今日入库数量
     */
    @Select("SELECT COUNT(*) FROM wms_inbound WHERE DATE(inbound_time) = CURDATE()")
    int getTodayCount();
}
