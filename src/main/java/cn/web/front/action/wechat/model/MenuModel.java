package cn.web.front.action.wechat.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.web.front.action.wechat.util.ConstantsErrorCode;
import cn.web.front.action.wechat.util.GsonBuilderUtil;
import cn.web.front.action.wechat.util.GsonUtil;

public class MenuModel {
	private Button [] button;
	public Button[] getButton() {
		return button;
	}
	public void setButton(Button[] button) {
		this.button = button;
	}
	
	public class Button{
		private String key;
		private String name;
		private String type;
		private String url;
		private String media_id;
		private Button [] sub_button;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getMedia_id() {
			return media_id;
		}
		public void setMedia_id(String media_id) {
			this.media_id = media_id;
		}
		public Button[] getSub_button() {
			return sub_button;
		}
		public void setSub_button(Button[] sub_button) {
			this.sub_button = sub_button;
		}
		public Button(String key, String name, String type, String url) {
			this.key = key;
			this.name = name;
			this.type = type;
			this.url = url;
		}
		public Button(){}
	}
	
	/**
	 * 初始化菜单
	 * @return
	 */
	public String init(){
		MenuModel menuModel = new MenuModel();
		//Button button = menuModel.new Button("start_level_user","星级用户","view","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx48a8104946507c1e&amp;redirect_uri=http%3A%2F%2Fgxg.tunnel.qydev.com%2Fweb%2Foauth%2Fcallback.html&amp;response_type=code&amp;scope=snsapi_base&amp;state=STATE#wechat_redirect");
		Button button = menuModel.new Button("start_level_user","星级用户","view","http://np.tunnel.qydev.com");
		Button button2 = menuModel.new Button("info_query","信息查询",null,null);
		button2.setSub_button(new Button[]{
				menuModel.new Button("convenience_information","便民信息","click",null),
				menuModel.new Button("service_guide","办事指南","click",null),
				menuModel.new Button("trailer_query","拖车查询","view","http://zsc.tunnel.qydev.com/"),
				menuModel.new Button("electronic_police_distributed_query","电子警察分布查询","view","http://zsc.tunnel.qydev.com/"),
				menuModel.new Button("traffic_information_query","交通违法信息查询","click","http://zsc.tunnel.qydev.com/")});
		
		
		Button button3 = menuModel.new Button("traffic_police_interaction","交警互动",null,null);
		button3.setSub_button(new Button[]{
			menuModel.new Button("user_center","个人中心","view","http://zsc.tunnel.qydev.com/"),
			menuModel.new Button("fault_reporting","故障报错和使用建议","view","http://zsc.tunnel.qydev.com/"),
			menuModel.new Button("report_information_inquiry","举报信息查询","view","http://zsc.tunnel.qydev.com/"),
			menuModel.new Button("readily_report","随手拍举报","view","http://zsc.tunnel.qydev.com/"),
			menuModel.new Button("emergency_traffic","突发路况","view","http://zsc.tunnel.qydev.com/")
		});
		
		menuModel.setButton(new Button[]{button,button2,button3});
		
		String json = GsonBuilderUtil.toJson(menuModel);
		System.out.println(json);
		return json;
	}
}	
