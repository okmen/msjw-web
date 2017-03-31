package cn.web.front.common;

public class Response implements IResponse {

    private String version;
    private int code = 0;
    private String debug;
    private String message;
    private IResponse response;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public IResponse getResponse() {
        return response;
    }

    public void setResponse(IResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {

        return String.format("version:%s, code:%s, debug:%s, message:%s, response:[%s]", this.version, this.code, this.debug, this.message, this.response);
    }
}
