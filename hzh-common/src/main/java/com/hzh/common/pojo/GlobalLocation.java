package com.hzh.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@ApiModel(value="GlobalLocation对象", description="")
public class GlobalLocation implements Serializable {

    private static final long serialVersionUID=1L;

    //@ApiModelProperty(value = "主键")
    @TableId(value = "GL_ID", type = IdType.AUTO)
    private Integer glId;

    //@ApiModelProperty(value = "父ID")
    @TableField("GLP_ID")
    private Integer glpId;

    //@ApiModelProperty(value = "层级")
    @TableField("LEVEL")
    private Integer level;

    //@ApiModelProperty(value = "路径")
    @TableField("PATH")
    private String path;

    //@ApiModelProperty(value = "代码")
    @TableField("CODE")
    private String code;

    //@ApiModelProperty(value = "国家缩写")
    @TableField("ABBREVIATION")
    private String abbreviation;

    // @ApiModelProperty(value = "中文名称")
    @TableField("CHINESE_NAME")
    private String chineseName;

    //@ApiModelProperty(value = "英文名称")
    @TableField("ENGLISH_NAME")
    private String englishName;

    //@ApiModelProperty(value = "首字母")
    @TableField("INITIALS")
    private String initials;

    //@ApiModelProperty(value = "中文拼音")
    @TableField("PINYIN")
    private String pinyin;


}
