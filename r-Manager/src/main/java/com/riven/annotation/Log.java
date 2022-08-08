package com.riven.annotation;

import com.riven.enums.BusinessType;
import com.riven.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 */
//Target注解决定Log注解能加在哪些成分之上
@Target({ ElementType.PARAMETER, ElementType.METHOD })
/**
 * Retention注解决定Log注解的声明周期
 * "RetentionPolicy.RUNTIME"意思是让Log这个注解的生命周期一直程序运行时都存在
 */
@Retention(RetentionPolicy.RUNTIME)
//生成文档信息的时候保留注解，对类作辅助说明
@Documented
public @interface Log
{
    /**
     * 模块 
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}
