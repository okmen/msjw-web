package cn.web.front.action.wechat.util;
import java.util.Map;
import cn.web.front.action.wechat.model.WeiXinOauth2Token;
/**
 * 实际通过微信用户授权换取code完成之后,通过该类方法获取openId以及用户accessToken(这个token只能获取用户信息)
 * @author gaoxigang
 *
 */
public class OpenIdUtil {
	public static WeiXinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        WeiXinOauth2Token wat = new WeiXinOauth2Token();
        Map<String, Object> map= WebService4Wechat.sendOauth(appId, appSecret, code);
        if (null != map) {
        	try {
        		wat = new WeiXinOauth2Token();
                wat.setAccessToken(getString(map.get("access_token")));
                wat.setExpiresIn(7200);  //getString(map.get("expires_in") 7200.0
                wat.setRefeshToken(getString(map.get("refresh_token")));
                wat.setOpenId(getString(map.get("openid")));
                wat.setScope(getString(map.get("scope")));
        	} catch (Exception e) {
                wat = null;
                String errorCode = getString(map.get("errcode"));
                String errorMsg = getString(map.get("errmsg"));
                System.out.println("获取网页授权凭证失败 "+errorCode+":"+ errorMsg);
            }
        }
        return wat;
	}
	
	private static String getString(Object obj){
		return null != obj ? obj.toString() : "";
	}
}
