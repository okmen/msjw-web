package cn.web.front.action.wechat.util;

/**
 * 获取用户授权url
 * @author gaoxigang
 *
 */
public class OauthUrlMain {
	public static void main(String[] args) {
		//ckuckUrl是验证的入口
		String clickUrlToSellerList=
				"https://open.weixin.qq.com/connect/oauth2/authorize" +
						"?appid=" +Constants.APP_ID +
						"&redirect_uri=" + EncodeUtil.encodeUTF8("http://gxg.tunnel.qydev.com/web/oauth/callback.html")+
						"&response_type=code" +
						"&scope=snsapi_base" +
						"&state=STATE" +
						"#wechat_redirect";
		System.out.println("clickUrlToSellerList:"+clickUrlToSellerList);
	}
}
