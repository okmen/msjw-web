package cn.web.front.action.wechat.util;

import java.io.UnsupportedEncodingException;

public class EncodeUtil {
	//加密redirect_uri
	public static String encodeUTF8(String source){
        String result = source;
        try {
        	result = java.net.URLEncoder.encode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
	}
	
	
}
