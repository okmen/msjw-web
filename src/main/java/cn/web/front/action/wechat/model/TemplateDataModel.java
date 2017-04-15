package cn.web.front.action.wechat.model;

import java.util.Map;

public class TemplateDataModel {
	private String touser;
	private String template_id;
	private String url;
	private Map<String, Property> data;
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, Property> getData() {
		return data;
	}
	public void setData(Map<String, Property> data) {
		this.data = data;
	}
	
	public class Property{
		private String value;
		private String color;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
	}
}	
