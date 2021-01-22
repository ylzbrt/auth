package engine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 知识库内容
 * </p>
 *
 * @author ${author}
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RepoRestrictDetail对象", description="知识库内容")
public class RepoRestrictDetail implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "知识库内容id")
    @TableId(value = "restrict_detail_id", type = IdType.AUTO)
    private Integer restrictDetailId;

    @ApiModelProperty(value = "知识库模板编码")
    @TableField("restrict_code")
    private String restrictCode;

    @ApiModelProperty(value = "主对象组编码")
    @TableField("a_object_group_code")
    private String aObjectGroupCode;

    @ApiModelProperty(value = "副对象组编码")
    @TableField("b_object_group_code")
    private String bObjectGroupCode;

    @ApiModelProperty(value = "计算方式")
    @TableField("calculate_method")
    private String calculateMethod;

    @ApiModelProperty(value = "阈值")
    @TableField("threshold")
    private BigDecimal threshold;

    @ApiModelProperty(value = "回溯周期")
    @TableField("trace_period")
    private Integer tracePeriod;

    @ApiModelProperty(value = "回溯周期单位")
    @TableField("trace_period_unit")
    private String tracePeriodUnit;

    @ApiModelProperty(value = "输出描述")
    @TableField("illegal_info")
    private String illegalInfo;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "有效标志")
    @TableField("valid_flag")
    private String validFlag;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "操作人")
    @TableField("operator")
    private String operator;

    @ApiModelProperty(value = "统筹区")
    @TableField("poolarea_no")
    private String poolareaNo;


}
