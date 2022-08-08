package com.riven.model;
import com.riven.annotation.Excel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author :zhujl
 * @date : 2022/8/5
 */
@Entity
@Table(name = "Z_Test")
public class Test {
    @Excel(name = "id",cellType = Excel.ColumnType.NUMERIC)
    @Id
    private Integer id;
    @Excel(name = "名字",cellType = Excel.ColumnType.STRING)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
