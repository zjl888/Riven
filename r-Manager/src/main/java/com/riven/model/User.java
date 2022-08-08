package com.riven.model;
import com.riven.annotation.Excel;

import javax.persistence.*;
import java.util.List;

/**
 * 用户信息表
 */
@Entity
@Table(name = "Z_USER")
public class User {
    /**
     * id
     */
    @Id
    @Excel(name = "id",cellType = Excel.ColumnType.NUMERIC)
    private Long id;
    /**
     * 登录名
     */
    @Excel(name = "名字",cellType = Excel.ColumnType.STRING)
    @Column(name = "username")
    private String username;
    /**
     * 密码
     */
    @Excel(name = "密码",cellType = Excel.ColumnType.STRING)
    @Column(name = "password")
    private String password;

    @Transient
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
