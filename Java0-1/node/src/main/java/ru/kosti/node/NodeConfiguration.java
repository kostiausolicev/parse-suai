package ru.kosti.node;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableFeignClients(basePackages = "ru.kosti.node.proxy")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "ru.kosti.repository.dao")
@EntityScan(basePackages = "ru.kosti.repository.entity")
public class NodeConfiguration {
}
