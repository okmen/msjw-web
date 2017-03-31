package cn.web.front.constants;

@Deprecated
public class ErrorConstantUtils {
    public static final String ERR_NO_RESULT = "10601"; //根据传递参数，获取结果集失败，或无结果集
	public static final String ERR_USER_accessToken_overTime = "10609"; //accessToken过期
	public static final String ERR_REFRESH_TOKEN_INVALIDATE = "10614";//refreshToken失效
	public static final String ERR_TOKEN_DECODE_ERROR = "10689"; //请求验证解密token失败
	public static final String ERR_PARAM_FORMAT = "10702"; //传递参数格式或类型有问题
	public static final String ERR_SQL = "10901"; //数据库方面语句执行异常
	public static final String ERR_UPLOAD_FAILED = "10902"; //图片上传到图片服务器失败
	public static final String ERR_WECHAT_AUTHORIZE_FAIL = "-10022"; //获取微信授权失败
	public static final String ERR_WECHAT_OPENID_NOT_BIND = "11017"; //未绑定openId
}
