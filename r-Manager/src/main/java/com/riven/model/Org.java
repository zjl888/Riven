package com.riven.model;

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
@Table(name = "Z_ORG")
@DynamicInsert
@DynamicUpdate
public class Org {
    @Id
    private Long orgId;
    private Long parentId;
    private String ancestors;
    private String orgName;
    private Integer orderNum;
    @Transient
    private String parentName;
    @Transient
    private List<Org> children=new ArrayList<Org>();

    @Override
    public String toString() {
        return "Org{" +
                "orgId=" + orgId +
                ", parentId=" + parentId +
                ", ancestors='" + ancestors + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orderNum=" + orderNum +
                ", parentName='" + parentName + '\'' +
                ", children=" + children +
                '}';
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public List<Org> getChildren() {
        return children;
    }

    public void setChildren(List<Org> children) {
        this.children = children;
    }
}
