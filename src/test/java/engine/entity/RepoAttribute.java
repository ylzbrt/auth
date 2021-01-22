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
import java.util.Date;

/**
 * <p>
 * 属性
 * </p>
 *
 * @author ${author}
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RepoAttribute对象", description="属性")
public class RepoAttribute implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "属性ID")
    @TableId(value = "attribute_id", type = IdType.AUTO)
    private Integer attributeId;

    @ApiModelProperty(value = "属性编码")
    @TableField("attribute_code")
    private String attributeCode;

    @ApiModelProperty(value = "属性名称")
    @TableField("attribute_name")
    private String attributeName;

    @ApiModelProperty(value = "属性分类编码")
    @TableField("attribute_category_code")
    private String attributeCategoryCode;

    @ApiModelProperty(value = "属性类型")
    @TableField("attribute_type")
    private String attributeType;

    @ApiModelProperty(value = "属性单位")
    @TableField("unit")
    private String unit;

    @ApiModelProperty(value = "是否统计属性")
    @TableField("statistics_type")
    private String statisticsType;

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

    @ApiModelProperty(value = "增量属性xml")
    @TableField("xml")
    private String xml;

}
