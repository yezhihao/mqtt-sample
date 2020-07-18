#mqtt-sample

#### topic设计原则：
#### 1.设备端订阅的topic以“iot_client/”开头，依次为设备ID“deivceId/”，结尾为动作“action”
例如：iot_client/test123/settings

#### 2.服务端订阅的topic以“iot_server/”开头，依次为动作“action/”，结尾为设备ID“deivceId”
例如：iot_server/reply/test123

注意到设备端和服务端的action和deviceId互换了位置
由于topc支持以“#”为占位符的模糊订阅，所以服务端仅需订阅“iot_server/#”即可接收所有设备发送的消息
设备端仅需订阅“iot_client/test123/#”即可接收所有动作的消息
该原则并非规范，仅仅为实际开发中的一些经验之谈。


使用@Topic注解定义主题, {deviceId}会被替换为具体的设备ID，
在报文解析过程中也会根据“iot_server/location”来选择对应的模型
如：
@Topic("iot_server/location/{deviceId}")
public class Location {
...
}


swagger文档中定义的两个接口用于测试同步消息和异步消息
http://127.0.0.1:8080/swagger-ui.html

技术交流群：906230542