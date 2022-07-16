package com.hzh.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
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
//@ApiModel(value="CoachInfo对象", description="")
public class CoachInfo implements Serializable {

    private static final long serialVersionUID=1L;

    //@ApiModelProperty(value = "教练团队编号")
    @TableId(value = "coach_team_id", type = IdType.AUTO)
    private Integer coachTeamId;

    //@ApiModelProperty(value = "教练编号")
    private Integer coachId;

    //@ApiModelProperty(value = "教练名称")
    private String coachName;

    //@ApiModelProperty(value = "入队时间")
    private String coachTime;

    //@ApiModelProperty(value = "教练身高")
    private Integer coachHeight;

    //@ApiModelProperty(value = "执教胜场")
    private Integer coachWonNum;

    //@ApiModelProperty(value = "执教总场次")
    private Integer coachTotal;

    //@ApiModelProperty(value = "教练体重")
    private Integer coachWeight;

    //@ApiModelProperty(value = "教练国籍")
    private Integer coachCountry;

    //@ApiModelProperty(value = "教练年龄")
    private Integer coachAge;

    //@ApiModelProperty(value = "队服号码")
    private Integer coachUniformNumber;

    //@ApiModelProperty(value = "教练位置  1-主教练  2-助理教练  3-其他教练")
    private Integer coachLocation;

    //@ApiModelProperty(value = "从业时长")
    private Integer coachPractitionersAge;

    //@ApiModelProperty(value = "教练类型-篮球-足球等")
    private String coachType;


}
