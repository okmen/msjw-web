package cn.web.front.support;

import com.alibaba.fastjson.JSON;

/**
 * HTTP服务器返回的标准消息实体
 *
 * @author wubinhong
 */
public class Result<T> {

    /** 版本号，与请求报文version字段相同*/
    private String version;
    /**
     * 返回代码
     * 0 - 成功；
     * < 0 - 失败，见《API错误代码表》{@link //wiki.choumei.me/pages/viewpage.action?pageId=655496}；
     * > 0 - 保留
     */
    private Integer code;
    /**
     * 调试信息 仅DEV / TEST环境使用
     */
    private String debug;
    /**
     * 返回结果描述
     */
    private String message;
    /** 返回的业务数据 */
    private T response;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "YYYY-mm-dd HH:mm:ss");
    }

}
