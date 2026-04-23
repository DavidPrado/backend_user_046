package com.baser_users.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import reactor.core.publisher.Mono;

public class EmailServiceHealthIndicator implements ReactiveHealthIndicator {

    @Override
    public Mono<Health> health() {
        return checkEmailServiceStatus()
                .map(isUp -> isUp ? Health.up().build() : Health.down().withDetail("erro", "Servidor SMTP fora do ar").build());
    }

    private Mono<Boolean> checkEmailServiceStatus() {
        return Mono.just(true);
    }
}
