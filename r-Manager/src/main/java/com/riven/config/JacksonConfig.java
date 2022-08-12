package com.riven.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
//把一个类作为一个IOC容器，注册了@Bean的方法，会作为这个Spring容器中的Bean
@Configuration
public class JacksonConfig {

    @Bean
    //同一个接口可能有几种不同的实现类，注入bean时，在众多实现类中，优先使用@Primary注解的bean注入，
    @Primary
    //当bean被注册时，如果注册相同类型的bean就会出现异常，一般来说，对于自定义的配置类，加上该注解以避免多个配置同时注入的风险
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 全局配置序列化返回 JSON 处理
        SimpleModule simpleModule = new SimpleModule();
        //Long类型的id传到前端会发生失真现象，需要先转换为String类型
        //JSON Long ==> String
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }


}