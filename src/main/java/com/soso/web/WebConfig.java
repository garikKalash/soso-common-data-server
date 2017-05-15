package com.soso.web;

import com.soso.models.ServiceInfo;
import com.soso.service.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpEncodingProperties;
import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

@Configuration
@ComponentScan
@EnableWebMvc
@PropertySource(value = { "classpath:application.properties" })
public class WebConfig  extends WebMvcConfigurerAdapter {

    @Bean
    public DataSource dataSource() {
        CommonDataService commonDataService = new CommonDataService(4);
        ServiceInfo myInfo = commonDataService.getDestinationService();

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(myInfo.getDbConnectionMetaData().getDriverClassName());
        ds.setUrl(myInfo.getDbConnectionMetaData().getUrl());
        ds.setUsername(myInfo.getDbConnectionMetaData().getUsername());
        ds.setPassword(myInfo.getDbConnectionMetaData().getPassword());
        return ds;
    }

    @Autowired
    private HttpEncodingProperties httpEncodingProperties;

    @Bean
    public OrderedCharacterEncodingFilter characterEncodingFilter() {
        OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(this.httpEncodingProperties.getCharset().name());
        filter.setForceEncoding(true);
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filter;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("128KB");
        factory.setMaxRequestSize("128KB");
        return factory.createMultipartConfig();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        return resolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
    }



}
