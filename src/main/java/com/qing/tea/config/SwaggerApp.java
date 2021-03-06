package com.qing.tea.config;

import springfox.documentation.service.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @ClassName SwaggerApp
 * @Description
 * @Author lq
 * @Date 2020/6/8 15:24
 * @Version V1.0
 **/
@Configuration
@EnableSwagger2
public class SwaggerApp {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.qing.tea.controller"))
                .paths(PathSelectors.any())
                .build();
//        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
    }
    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("喝好茶溯源系统后端接口文档")
                //创建人
                .contact(new Contact("LiaoQing", "https://blog.csdn.net/liaoqing1999", "1609614437@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("欢迎访问本接口文档")
                .build();
    }
}