package org.sample.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.sample.mqtt.component.support.BytesMessageConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

@EnableIntegration
@Configuration
@ConfigurationProperties(prefix = "mqtt")
@IntegrationComponentScan(basePackages = "org.sample.mqtt")
public class MqttConfig {

    private String[] serverUris;
    private String username;
    private char[] password;
    private int keepAliveInterval;
    private String[] subTopics;
    private Class<? extends BytesMessageMapper> messageMapper;
    private String clientIdPrefix;
    private String modelPackages;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(serverUris);
        options.setUserName(username);
        options.setPassword(password);
        options.setKeepAliveInterval(keepAliveInterval);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MqttMessageConverter bytesMessageConverter() throws NoSuchMethodException {
        BytesMessageMapper bytesMessageMapper = BeanUtils.instantiateClass(messageMapper.getConstructor(String.class), modelPackages);
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

    public String[] getServerUris() {
        return serverUris;
    }

    public void setServerUris(String[] serverUris) {
        this.serverUris = serverUris;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public String[] getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(String[] subTopics) {
        this.subTopics = subTopics;
    }

    public Class<? extends BytesMessageMapper> getMessageMapper() {
        return messageMapper;
    }

    public void setMessageMapper(Class<? extends BytesMessageMapper> messageMapper) {
        this.messageMapper = messageMapper;
    }

    public String getClientIdPrefix() {
        return clientIdPrefix;
    }

    public void setClientIdPrefix(String clientIdPrefix) {
        this.clientIdPrefix = clientIdPrefix;
    }

    public String getModelPackages() {
        return modelPackages;
    }

    public void setModelPackages(String modelPackages) {
        this.modelPackages = modelPackages;
    }
}