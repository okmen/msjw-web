package cn.web.front.common;

public class Device {

    private String type;
    private String os;
    private String cpu;
    private String uuid;
    private String model;
    private String network;
    private String dpi;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }
    
    @Override
    public String toString() {

        return String.format("type:%s, os:%s, cpu:%s, uuid:%s, model:%s, network:%s, dpi:%s", this.type, this.os, this.cpu, this.uuid, this.model,
                this.network, this.dpi);
    }
}
