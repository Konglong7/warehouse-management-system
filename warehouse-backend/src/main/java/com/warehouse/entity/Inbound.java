package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("wms_inbound")
public class Inbound {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String inboundNo;
    private Long materialId;
    private Long locationId;
    private Integer quantity;
    private String operator;
    private LocalDateTime inboundTime;
    private String remark;
    private LocalDateTime createTime;
}
