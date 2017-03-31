package cn.web.front.constants;

@Deprecated
public class FilterErrorConstants {
	public static final String errorType_accessToken_overTime = "1";//accessToken过期
	public static final String errorType_PARAM_FORMAT = "2"; //传递参数格式或类型有问题
	public static final String errorType_TOKEN_DECODE_ERROR = "10004";//请求验证解密token失败
	public static final String errorType_unKnown = "10000"; //未知错误
}
