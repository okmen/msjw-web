package cn.web.front.action.wechat.model;

import cn.web.front.action.wechat.util.GsonUtil;

public class OutJsonModel {
	private String code;
	private Object data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public OutJsonModel(String code,Object data){
		this.code = code;
		this.data = data;
	}
	public OutJsonModel(String code){
		this.code = code;
	}
	/**
	 * 格式化该对象,返回json字符串
	 * @return
	 */
	public String toJson(){
		String json = GsonUtil.toJson(this);
		System.out.println(json);
		return json;
	}
}
