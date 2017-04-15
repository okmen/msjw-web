package cn.web.front.action.wechat.util;

import com.google.gson.Gson;

public class GsonUtil {
	static public Gson gson = null;
	static{
		gson = new Gson();
	}
	static public <T> T fromJson(String src,Class<T> tClass){
		return gson.fromJson(src, tClass);
	}
	
	static public String toJson(Object obj){
		return gson.toJson(obj);
	}
}
