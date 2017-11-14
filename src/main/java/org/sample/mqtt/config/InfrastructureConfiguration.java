package org.sample.mqtt.config;

import org.springframework.context.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class InfrastructureConfiguration {

    @Bean
    public MessageChannel upstreamChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel noticeChannel() {
        return new QueueChannel(50);
    }

    @Bean
    public MessageChannel responseChannel() {
        return new QueueChannel(50);
    }

}