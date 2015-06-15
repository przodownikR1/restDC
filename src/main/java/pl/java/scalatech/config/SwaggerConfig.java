package pl.java.scalatech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

/**
 * @author Sławomir Borowiec
 *         Module name : Cep
 *         Creating time : 9 wrz 2014 14:05:38
 */
@Configuration
@EnableSwagger
// /@Profile("dev")
public class SwaggerConfig {
    private SpringSwaggerConfig springSwaggerConfig;

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;

    }

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".*api/*.*");
    }

    private static ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("Content repo", "PoC content repo", "API terms of service", "przodownikR1@gmail.com", "MIT API Licence Type",
                "przodownikR1 API License");
        return apiInfo;
    }
}