package org.sample.mqtt.component.model;

public class Connected {

    private String clientid;
    private String username;
    private String ipaddress;
    private Integer connack;
    private Long ts;
    private Integer proto_ver;
    private String proto_name;
    private Boolean clean_start;
    private Integer keepalive;

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Integer getConnack() {
        return connack;
    }

    public void setConnack(Integer connack) {
        this.connack = connack;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Integer getProto_ver() {
        return proto_ver;
    }

    public void setProto_ver(Integer proto_ver) {
        this.proto_ver = proto_ver;
    }

    public String getProto_name() {
        return proto_name;
    }

    public void setProto_name(String proto_name) {
        this.proto_name = proto_name;
    }

    public Boolean getClean_start() {
        return clean_start;
    }

    public void setClean_start(Boolean clean_start) {
        this.clean_start = clean_start;
    }

    public Integer getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Integer keepalive) {
        this.keepalive = keepalive;
    }
}
