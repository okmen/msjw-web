package cn.web.front.action.wechat.model.message;

public interface IEvent {
	/**
	 * 扫描二维码(未关注时扫描,微信会自动关注)/关注事件
	 */
	public static final String REQ_EVENT_TYPE_SUBSCRIBE = "subscribe";
	
	/**
	 * 取消订阅事件
	 */
	public static final String REQ_EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 扫描二维码(已关注时)
	 */
	public static final String REQ_EVENT_TYPE_SCAN = "SCAN";
	
	/**
	 * 上报地理位置
	 */
	public static final String REQ_EVENT_TYPE_LOCATION = "LOCATION";
	
	/**
	 * 用户点击自定义菜单拉取消息
	 */
	public static final String REQ_EVENT_TYPE_CLICK = "CLICK";
	
	/**
	 * 用户点击自定义菜单跳转
	 */
	public static final String REQ_EVENT_TYPE_VIEW = "VIEW";
}
