package com.hzh.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Hzh
 * @since 2022-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@ApiModel(value="HzhUser对象", description="用户表")
public class HzhUser implements Serializable {

    private static final long serialVersionUID=1L;

    //@ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //@ApiModelProperty(value = "用户名")
    private String userName;

    //@ApiModelProperty(value = "密码")
    private String password;

    //@ApiModelProperty(value = "用户描述")
    private String userDescription;

    //@ApiModelProperty(value = "账号状态（0正常 1停用）")
    private String status;

    //@ApiModelProperty(value = "邮箱")
    private String email;

    //@ApiModelProperty(value = "手机号")
    private String phonenumber;

    //@ApiModelProperty(value = "用户性别（0男，1女，2未知）")
    private String sex;

    private String salt;
    //@ApiModelProperty(value = "头像")
    private String avatar;

    //@ApiModelProperty(value = "用户类型（eventman 体育类型管理用户，sportstypeman 体育类型管理用户，teamsman 球队管理用户，admin  用户管理， visitor 访客 ， member 会员）")
    private String userType;

    //@ApiModelProperty(value = "创建时间")
    private String createTime;

    //@ApiModelProperty(value = "更新人")
    private Long updateBy;

    //@ApiModelProperty(value = "更新时间")
    private String updateTime;

    //@ApiModelProperty(value = "删除标志（0代表未删除，1代表已删除）")
    private String delFlag;


}
