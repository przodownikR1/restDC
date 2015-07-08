package pl.java.scalatech.config;

import java.util.concurrent.Callable;

import javax.servlet.MultipartConfigElement;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pl.java.scalatech.web.interceptor.SwaggerInterceptor;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@Configuration
@EnableWebMvc
@Slf4j
@Profile(value = "dev")
@ComponentScan(basePackages = { "pl.java.scalatech.web" }, useDefaultFilters = false, includeFilters = { @Filter(Controller.class) })
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private Environment env;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new HystrixMetricsStreamServlet(), "/hystrix.stream");
    }

    @Bean
    public HystrixCommandAspect hystrixCommandAspect() {
        return new HystrixCommandAspect();
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("1500KB");
        factory.setMaxRequestSize("1500KB");
        return factory.createMultipartConfig();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SwaggerInterceptor());
    }

    @Value("${session-timeout}")
    private Long sessionTimeOut;

    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(sessionTimeOut * 1000L);
        log.info("configureAsyncSupport ");
        configurer.registerCallableInterceptors(timeoutInterceptor());
    }

    @Bean
    public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }

    static class TimeoutCallableProcessingInterceptor extends CallableProcessingInterceptorAdapter {

        @Override
        public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
            throw new IllegalStateException("[" + task.getClass().getName() + "] timed out");
        }
    }
}
/*
 * @Override
 * public void addResourceHandlers(ResourceHandlerRegistry registry) {
 * registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
 * registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
 * registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(31556926);
 * registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
 * }
 */

// @Bean
/*
 * public TemplateResolver templateResolver() {
 * ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
 * templateResolver.setPrefix("/templates/");
 * templateResolver.setSuffix(".html");
 * templateResolver.setTemplateMode("HTML5");
 * List<String> profiles = Arrays.asList(env.getActiveProfiles());
 * if (profiles.contains("dev")) {
 * templateResolver.setCacheable(false);
 * log.info("++++ templateResolver cache off ... -> dev");
 * }
 * // TODO
 * templateResolver.setCacheable(false);
 * templateResolver.setCharacterEncoding("UTF-8");
 * templateResolver.setOrder(2);
 * return templateResolver;
 * }
 */

/*
 * @Override
 * public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
 * configurer.favorPathExtension(true).favorParameter(true).parameterName("mediaType").ignoreAcceptHeader(false)
 * .defaultContentType(MediaType.APPLICATION_JSON).mediaType("xml", MediaType.APPLICATION_XML).mediaType("json", MediaType.APPLICATION_JSON);
 * }
 */
/*
 * @Bean
 * public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
 * ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
 * resolver.setContentNegotiationManager(manager);
 * return resolver;
 * }
 */
