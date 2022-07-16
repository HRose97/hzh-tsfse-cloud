package com.hzh.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 体育赛事举办信息表
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@ApiModel(value="EventInfo对象", description="体育赛事举办信息表")
public class EventInfo implements Serializable {

    private static final long serialVersionUID=1L;

    //@ApiModelProperty(value = "体育类型ID")
    @TableId(value = "physical_id", type = IdType.AUTO)
    private Integer physicalId;

    //@ApiModelProperty(value = "体育类型名称")
    private String physicalName;

    //@ApiModelProperty(value = "举办国")
    private String heldCountry;

    //@ApiModelProperty(value = "举办地")
    private String heldLocation;

    //@ApiModelProperty(value = "举办主场方")
    private String heldHome;

    //@ApiModelProperty(value = "场地最大容纳人数")
    private String maximumCapacity;

    //@ApiModelProperty(value = "客场方")
    private String awayGround;

    //@ApiModelProperty(value = "举办主场方logo URL")
    private String physicalHeldLogo;

    //@ApiModelProperty(value = "体育描述")
    private String physicalDesc;

    //@ApiModelProperty(value = "体育状态,0禁用,1启用")
    private Integer physicalStatus;

    //@ApiModelProperty(value = "开赛时间")
    private String matchTime;

    //@ApiModelProperty(value = "举办场馆")
    private String heldVenues;



}
