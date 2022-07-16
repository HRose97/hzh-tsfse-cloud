package com.hzh.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@ApiModel(value="HzhRole对象", description="角色表")
public class HzhRole implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    //@ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    //@ApiModelProperty(value = "角色状态（0停用 1正常）")
    private String status;

    //@ApiModelProperty(value = "del_flag")
    private Integer delFlag;


}
