package engine.entity;

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
 * 知识库模板
 * </p>
 *
 * @author ${author}
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RepoRestrict对象", description="知识库模板")
public class RepoRestrict implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "知识库模板编码")
    @TableId("restrict_code")
    private String restrictCode;

    @ApiModelProperty(value = "知识库模板名称")
    @TableField("restrict_name")
    private String restrictName;

    @ApiModelProperty(value = "上级模板编码")
    @TableField("parent_code")
    private String parentCode;

    @ApiModelProperty(value = "三目类型(可见范围)")
    @TableField("contents_type")
    private String contentsType;

    @ApiModelProperty(value = "适用范围")
    @TableField("apply_scope")
    private String applyScope;

    @ApiModelProperty(value = "是否配置方案")
    @TableField("solution_flag")
    private String solutionFlag;

    @ApiModelProperty(value = "实现方案")
    @TableField("impl_solution")
    private String implSolution;

    @ApiModelProperty(value = "规则描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "输出描述")
    @TableField("illegal_info")
    private String illegalInfo;

    @ApiModelProperty(value = "限用属性")
    @TableField("restrict_attribute")
    private String restrictAttribute;

    @ApiModelProperty(value = "限用属性单位")
    @TableField("restrict_attribut_unit")
    private String restrictAttributUnit;

    @ApiModelProperty(value = "比较符（条件）")
    @TableField("compare_symbol")
    private String compareSymbol;

    @ApiModelProperty(value = "限用条件比较值")
    @TableField("compare_value")
    private String compareValue;

    @ApiModelProperty(value = "比较属性")
    @TableField("compare_attribute")
    private String compareAttribute;

    @ApiModelProperty(value = "统计周期")
    @TableField("period")
    private Integer period;

    @ApiModelProperty(value = "统计周期单位")
    @TableField("period_unit")
    private String periodUnit;

    @ApiModelProperty(value = "比较对象类型")
    @TableField("compare_object_type")
    private String compareObjectType;

    @ApiModelProperty(value = "阈值")
    @TableField("threshold")
    private BigDecimal threshold;

    @ApiModelProperty(value = "比较对象属性编码")
    @TableField("compare_object_attibute")
    private String compareObjectAttibute;

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

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "修正天数")
    @TableField("offset_value")
    private Integer offsetValue;
}
