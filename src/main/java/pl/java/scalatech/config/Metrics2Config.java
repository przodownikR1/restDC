package pl.java.scalatech.config;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.java.scalatech.metrics.BasicHealthCheck;
import pl.java.scalatech.metrics.DatabaseHealthCheck;
import pl.java.scalatech.metrics.DiskCapacityHealthCheck;
import pl.java.scalatech.metrics.RestResourcesHealthCheck;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics
@Slf4j
public class Metrics2Config extends MetricsConfigurerAdapter {

    private final MetricRegistry metricRegistry = new MetricRegistry();
    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Value("${server.port}")
    private String port;

    @Autowired
    private DataSource dataSource;

    @Bean
    @Override
    public MetricRegistry getMetricRegistry() {
        metricRegistry.registerAll(new GarbageCollectorMetricSet());
        metricRegistry.registerAll(new MemoryUsageGaugeSet());
        //metricRegistry.registerAll(new ThreadStatesGaugeSet());
        //metricRegistry.register("jvm.files", new FileDescriptorRatioGauge());
        // metricRegistry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        metricRegistry.register(MetricRegistry.name("przodownik", "gauge", "size"), new Gauge<Integer>() {
            Random random = new Random();

            @Override
            public Integer getValue() {
                return +random.nextInt(1000);
            }
        });
        configureReporters(metricRegistry);
        return metricRegistry;
    }

    @Bean
    @Override
    public HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

    @Bean
    public Counter counter() {
        return metricRegistry.counter("simpleCounter");
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        log.info("+++                                        configureReporters");
        Slf4jReporter.forRegistry(metricRegistry).outputTo(log).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build()
                .start(1, TimeUnit.MINUTES);
        JmxReporter.forRegistry(metricRegistry).build().start();
    }

    @PostConstruct
    private void registerHealthChecks() {
        healthCheckRegistry.register("Metrics HealthCheck mechanism", new BasicHealthCheck());
        healthCheckRegistry.register("Database", new DatabaseHealthCheck(dataSource, "select 1 from dual"));
        healthCheckRegistry.register("DeadLocks", new ThreadDeadlockHealthCheck());
        healthCheckRegistry.register("REST resources", new RestResourcesHealthCheck("http://localhost:" + port + "/api/appContext"));
        healthCheckRegistry.register("Disk space check", new DiskCapacityHealthCheck());

    }

    @Bean
    @Autowired
    public ServletRegistrationBean servletRegistrationBean(MetricRegistry metricRegistry) {
        MetricsServlet ms = new MetricsServlet(metricRegistry);
        ServletRegistrationBean srb = new ServletRegistrationBean(ms, "/stats/*");
        srb.setLoadOnStartup(1);
        return srb;
    }

    @Bean
    @Autowired
    public ServletRegistrationBean servletHealthRegistryBean(HealthCheckRegistry healthCheckRegistry) {
        HealthCheckServlet hc = new HealthCheckServlet(healthCheckRegistry);
        ServletRegistrationBean srb = new ServletRegistrationBean(hc, "/health/*");
        srb.setLoadOnStartup(2);
        return srb;
    }
}