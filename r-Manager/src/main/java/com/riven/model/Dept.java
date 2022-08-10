package com.riven.model;

import com.riven.model.base.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :zhujl
 * @date : 2022/8/9
 */
@Entity
@Table(name = "Z_DEPT")
//默认为true，不会为字段值不变的字段生成sql，注意：修改时，当字段值为null时，会将表中的字段值同样置为null
//组合使用，提高访问数据库的效率
@DynamicInsert
@DynamicUpdate
public class Dept extends BaseEntity {
    @Id
    private Long deptId;
    private Long parentId;
    private Long orgId;
    private String ancestors;
    private String deptName;
    private Integer orderNum;
    /**
     * 状态
     * 0:正常
     * 1:停用
     */
    private String status;
    /**
     * 删除
     * 0:正常
     * 1:删除
     */
    private String delFlag;
    @Transient
    private String parentName;
    @Transient
    private List<Dept> children=new ArrayList<Dept>();

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<Dept> getChildren() {
        return children;
    }

    public void setChildren(List<Dept> children) {
        this.children = children;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "deptId=" + deptId +
                ", parentId=" + parentId +
                ", orgId=" + orgId +
                ", ancestors='" + ancestors + '\'' +
                ", deptName='" + deptName + '\'' +
                ", orderNum=" + orderNum +
                ", parentName='" + parentName + '\'' +
                ", children=" + children +
                '}';
    }
}
