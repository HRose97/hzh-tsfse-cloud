package com.hzh.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
//@ApiModel(value="ChinaCity对象", description="")
public class ChinaCity implements Serializable {

    public static final long serialVersionUID=1L;


    @TableId(type = IdType.AUTO)
    public Long id;

    public Integer pid;

    public String cityname;

    public Integer type;

    public String state;


}
