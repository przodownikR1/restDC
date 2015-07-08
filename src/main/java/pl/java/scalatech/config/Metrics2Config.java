package pl.java.scalatech.config;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import pl.java.scalatech.metrics.BasicHealthCheck;
import pl.java.scalatech.metrics.DatabaseHealthCheck;
import pl.java.scalatech.metrics.DiskCapacityHealthCheck;
import pl.java.scalatech.metrics.MetricsFilter;
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
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics
@Slf4j
public class Metrics2Config extends MetricsConfigurerAdapter implements EnvironmentAware {
    private static final String ENABLE_METRICS = "metrics";
    private static final String METRIC_JMX_ENABLED = "jmx.enabled";
    private static final String METRIC_JVM_MEMORY = "jvm.memory";
    private static final String METRIC_JVM_GARBAGE = "jvm.garbage";
    private static final String METRIC_JVM_THREADS = "jvm.threads";
    private static final String METRIC_JVM_FILES = "jvm.files";
    private static final String METRIC_JVM_BUFFERS = "jvm.buffers";
    private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
    private RelaxedPropertyResolver propertyResolver;
    private final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    @Value("${server.port}")
    private String port;

    @Autowired
    private DataSource dataSource;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENABLE_METRICS);
    }

    @Bean
    @Override
    public MetricRegistry getMetricRegistry() {
        METRIC_REGISTRY.registerAll(new GarbageCollectorMetricSet());
        METRIC_REGISTRY.registerAll(new MemoryUsageGaugeSet());
        METRIC_REGISTRY.registerAll(new ThreadStatesGaugeSet());
        METRIC_REGISTRY.register(METRIC_JVM_MEMORY, new MemoryUsageGaugeSet());
        /*
         * METRIC_REGISTRY.register(METRIC_JVM_FILES, new FileDescriptorRatioGauge());
         * METRIC_REGISTRY.register(METRIC_JVM_BUFFERS, new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
         */
        if (propertyResolver.getProperty(METRIC_JMX_ENABLED, Boolean.class, false)) {
            log.info("Initializing Metrics JMX reporting");
            final JmxReporter jmxReporter = JmxReporter.forRegistry(METRIC_REGISTRY).build();
            jmxReporter.start();
        }

        /*
         * metricRegistry.register("jvm.files", new FileDescriptorRatioGauge());
         * metricRegistry.register("jvm.buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
         */

        METRIC_REGISTRY.register(MetricRegistry.name("przodownik", "gauge", "size"), new Gauge<Integer>() {
            Random random = new Random();

            @Override
            public Integer getValue() {
                return +random.nextInt(1000);
            }
        });
        configureReporters(METRIC_REGISTRY);
        return METRIC_REGISTRY;
    }

    @Bean
    @Override
    public HealthCheckRegistry getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

    @Bean
    public Counter counter() {
        return METRIC_REGISTRY.counter("simpleCounter");

    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        log.info("+++                                        configureReporters");
        /* ConsoleReporter.forRegistry(metricRegistry).build().start(10, TimeUnit.SECONDS); */

        Slf4jReporter.forRegistry(metricRegistry).outputTo(log).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build()
                .start(1, TimeUnit.MINUTES);

        /*
         * CsvReporter reporter = CsvReporter.forRegistry(metricRegistry).formatFor(Locale.US).convertRatesTo(TimeUnit.SECONDS)
         * .convertDurationsTo(TimeUnit.MILLISECONDS).build(new File("slawek.csv"));
         * reporter.start(1, TimeUnit.SECONDS);
         */

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

    @Bean
    public FilterRegistrationBean metricsFilterRegistration(MetricRegistry metricRegistry) {
        log.info("+++  register metrics filter.");
        return new FilterRegistrationBean(new MetricsFilter(METRIC_REGISTRY));
    }
}