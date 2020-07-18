package org.sample.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.sample.mqtt.component.support.BytesMessageConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mapping.BytesMessageMapper;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackages = "org.sample.mqtt")
public class MqttConfig {

    @Value("${mqtt.server-uris}")
    private String[] serverURIs;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private char[] password;

    @Value("${mqtt.keep-alive-interval}")
    private int keepAliveInterval;

    @Value("${mqtt.sub-topics}")
    private String[] subTopics;

    @Value("${mqtt.message-mapper}")
    private Class<? extends BytesMessageMapper> mapperClass;

    @Value("${mqtt.client-id-prefix}")
    private String clientIdPrefix;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(serverURIs);
        options.setUserName(username);
        options.setPassword(password);
        options.setKeepAliveInterval(keepAliveInterval);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MqttMessageConverter bytesMessageConverter(@Value("${mqtt.model-packages}") String modelPackages) throws NoSuchMethodException {
        BytesMessageMapper bytesMessageMapper = BeanUtils.instantiateClass(mapperClass.getConstructor(String.class), modelPackages);
        return new BytesMessageConverter(bytesMessageMapper);
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttMessageConverter mqttMessageConverter) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientIdPrefix + "_outbound", mqttClientFactory());
        messageHandler.setConverter(mqttMessageConverter);
        messageHandler.setCompletionTimeout(5000);
        messageHandler.setAsync(true);
        return messageHandler;
    }

    @Bean
    public MessageProducer mqttInbound(MqttMessageConverter mqttMessageConverter) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientIdPrefix + "_inbound", mqttClientFactory(), subTopics);
        adapter.setConverter(mqttMessageConverter);
        adapter.setOutputChannel(mqttInboundChannel());
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        return adapter;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }
}