package com.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("wms_material")
public class Material {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String materialCode;
    private String materialName;
    private String category;
    private String unit;
    private Long supplierId;
    private Integer warningStock;
    private Integer currentStock;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
