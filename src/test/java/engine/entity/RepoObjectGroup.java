package engine.entity;

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
 * 对象组
 * </p>
 *
 * @author ${author}
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RepoObjectGroup对象", description="对象组")
public class RepoObjectGroup implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "组编码")
    @TableId("object_group_code")
    private String objectGroupCode;

    @ApiModelProperty(value = "组名称")
    @TableField("object_group_name")
    private String objectGroupName;

    @ApiModelProperty(value = "对象类型")
    @TableField("object_type")
    private String objectType;

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
