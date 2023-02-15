package ml.ilei.moneybox.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class MvcConfiguration implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public RestTemplate getRestTempale(){
        return new RestTemplate();
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/temp/**")
                .addResourceLocations("file://" + uploadPath);
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

}