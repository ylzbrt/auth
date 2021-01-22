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
 * 对象
 * </p>
 *
 * @author ${author}
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RepoObject对象", description="对象")
public class RepoObject implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "对象ID")
    @TableId(value = "object_id", type = IdType.AUTO)
    private Integer objectId;

    @ApiModelProperty(value = "对象组编码")
    @TableField("object_group_code")
    private String objectGroupCode;

    @ApiModelProperty(value = "输出描述")
    @TableField("illegal_info")
    private String illegalInfo;

    @ApiModelProperty(value = "编码标准")
    @TableField("code_standard")
    private String codeStandard;

    @ApiModelProperty(value = "对象编码")
    @TableField("object_code")
    private String objectCode;

    @ApiModelProperty(value = "对象名称")
    @TableField("object_name")
    private String objectName;

    @ApiModelProperty(value = "换算值")
    @TableField("equivalent_value")
    private BigDecimal equivalentValue;

    @ApiModelProperty(value = "中药饮片拒付标准")
    @TableField("tcmp_limit")
    private String tcmpLimit;

    @ApiModelProperty(value = "阈值")
    @TableField("threshold")
    private BigDecimal threshold;

    @ApiModelProperty(value = "依据明细id")
    @TableField("basis_detail_id")
    private Integer basisDetailId;

    @ApiModelProperty(value = "依据信息")
    @TableField("basis_info")
    private String basisInfo;

    @ApiModelProperty(value = "起始时间")
    @TableField("start_date")
    private Integer startDate;

    @ApiModelProperty(value = "截止时间")
    @TableField("end_date")
    private Integer endDate;

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

    @ApiModelProperty(value = "项目类型")
    @TableField("contents_type")
    private String contentsType;


}
