package cn.web.front.action.wechat.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * 特殊字符gson转换类
 * @author gaoxigang
 *
 */
public class GsonBuilderUtil {
	static public Gson gson = null;
	static{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.disableHtmlEscaping();
		gson = gsonBuilder.create();  
	}
	static public <T> T fromJson(String src,Class<T> tClass){
		return gson.fromJson(src, tClass);
	}
	
	static public String toJson(Object obj){
		return gson.toJson(obj);
	}
	
}
