package pl.java.scalatech.metrics;

import static com.codahale.metrics.MetricRegistry.name;
import static com.google.common.collect.Maps.newHashMap;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.servlet.AbstractInstrumentedFilter;

@Slf4j
public class MetricsFilter implements Filter {

    private final String otherMetricName;
    private final Map<Integer, String> meterNamesByStatusCode;

    private ConcurrentMap<Integer, Meter> metersByStatusCode;
    private Meter otherMeter;
    private Counter activeRequests;
    private Timer requestTimer;

    public MetricsFilter(MetricRegistry metricsRegistry) {
        this.otherMetricName = "other";
        this.meterNamesByStatusCode = createMeterNamesByStatusCode();

        this.metersByStatusCode = new ConcurrentHashMap<>(meterNamesByStatusCode.size());
        for (Entry<Integer, String> entry : meterNamesByStatusCode.entrySet()) {
            metersByStatusCode.put(entry.getKey(), metricsRegistry.meter(name(AbstractInstrumentedFilter.class, entry.getValue())));
        }
        this.otherMeter = metricsRegistry.meter(name(AbstractInstrumentedFilter.class, otherMetricName));
        this.activeRequests = metricsRegistry.counter(name(AbstractInstrumentedFilter.class, "activeRequests"));
        this.requestTimer = metricsRegistry.timer(name(AbstractInstrumentedFilter.class, "requests"));
        log.info("+++ Metrics filter initialized.");
    }

    private static Map<Integer, String> createMeterNamesByStatusCode() {
        final Map<Integer, String> meterNamesByStatusCode = newHashMap();
        meterNamesByStatusCode.put(HttpStatus.OK.value(), HttpStatus.OK.name());
        meterNamesByStatusCode.put(HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        meterNamesByStatusCode.put(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.name());
        meterNamesByStatusCode.put(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name());
        meterNamesByStatusCode.put(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name());
        meterNamesByStatusCode.put(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name());
        return meterNamesByStatusCode;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final StatusExposingServletResponse wrappedResponse = new StatusExposingServletResponse((HttpServletResponse) response);
        activeRequests.inc();
        final Timer.Context context = requestTimer.time();
        try {
            chain.doFilter(request, wrappedResponse);
        } finally {
            context.stop();
            activeRequests.dec();
            markMeterForStatusCode(wrappedResponse.getStatus());
        }
    }

    private void markMeterForStatusCode(int status) {
        final Meter metric = metersByStatusCode.get(status);
        if (metric != null) {
            metric.mark();
        } else {
            otherMeter.mark();
        }
    }

    private static class StatusExposingServletResponse extends HttpServletResponseWrapper {
        // The Servlet spec says: calling setStatus is optional, if no status is set, the default is 200.
        private int httpStatus = 200;

        public StatusExposingServletResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int sc) throws IOException {
            httpStatus = sc;
            super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            httpStatus = sc;
            super.sendError(sc, msg);
        }

        @Override
        public void setStatus(int sc) {
            httpStatus = sc;
            super.setStatus(sc);
        }

        @Override
        public int getStatus() {
            return httpStatus;
        }
    }
}