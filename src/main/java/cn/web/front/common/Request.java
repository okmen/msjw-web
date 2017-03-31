package cn.web.front.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class Request implements IRequest {

    private String sign;
    private String bundle;
    private String version;
    private Device device;
    private long timestamp;
    private long sequence;
    private String accessToken;
    private IRequest request;
    private Map<String, Object> attributes = new HashMap<>();

    public Request() {

    }

    public Request(final HttpServletRequest httpRequest) {

        this.sign = StringUtils.defaultString(httpRequest.getParameter("sign"));
        this.bundle = StringUtils.defaultString(httpRequest.getParameter("bundle"));
        this.version = StringUtils.defaultString(httpRequest.getParameter("version"));
        this.timestamp = Long.valueOf(StringUtils.defaultString(httpRequest.getParameter("timestamp"), "0"));
        this.sequence = Long.valueOf(StringUtils.defaultString(httpRequest.getParameter("sequence"), "0"));
        this.accessToken = httpRequest.getParameter("access-token");

        final Device device = new Device();

        device.setType(StringUtils.defaultString(httpRequest.getParameter("device-type")));
        device.setDpi(StringUtils.defaultString(httpRequest.getParameter("device-dpi")));
        device.setModel(StringUtils.defaultString(httpRequest.getParameter("device-model")));
        device.setOs(StringUtils.defaultString(httpRequest.getParameter("device-os")));
        device.setCpu(StringUtils.defaultString(httpRequest.getParameter("device-cpu")));
        device.setUuid(StringUtils.defaultString(httpRequest.getParameter("device-uuid")));
        device.setNetwork(StringUtils.defaultString(httpRequest.getParameter("device-network")));

        this.device = device;

    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public void setAttribute(String name, Object obj) {
        this.attributes.put(name, obj);
    }

    public IRequest getRequest() {
        return request;
    }

    public void setRequest(IRequest request) {
        this.request = request;
    }

    @Override
    public String toString() {

        return String.format("sign:%s, bundle:%s, version:%s, device:[%s], timestamp:%s, sequence:%s, accessToken:%s, request:[%s]", this.sign, this.bundle,
                this.version, this.device, this.timestamp, this.sequence, this.accessToken, this.request);
    }
}
