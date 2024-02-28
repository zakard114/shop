package com.heeju.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // Activate JPA's Auditing function.
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){  // Register AuditorAware, which handles registrants and modifiers, as a bean.
        return new AuditorAwareImpl();
    }
}
