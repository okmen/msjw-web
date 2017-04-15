package cn.web.front.action.wechat.model.message.request.event;

import cn.web.front.action.wechat.model.message.IEvent;
import cn.web.front.action.wechat.model.message.IMessage;

/**
 * 接收请求事件父类
 * @author gaoxigang
 *
 */
public abstract class BaseRequestEvent implements IMessage,IEvent {
	private String toUserName;  
	private String fromUserName;  
	private long createTime;  
	//事件的消息类型固定为event
	protected String msgType = MESSAGE_TYPE_EVENT;
	protected String event;
	
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public BaseRequestEvent(String toUserName, String fromUserName,
			long createTime) {
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
	}
}
