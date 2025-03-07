package org.bank.pay.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationProperties
@EnableScheduling
public class SchedulerConfig {
}
