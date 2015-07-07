package pl.java.scalatech.metrics;

import com.codahale.metrics.health.HealthCheck;

public class BasicHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy("for now everything is ok");
    }
}