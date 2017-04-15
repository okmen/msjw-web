package cn.web.front.action.wechat.model.message.request.message;

import cn.web.front.action.wechat.model.message.IMessage;

/**
 * 接收消息
 * @author gaoxigang
 *
 */
public abstract class BaseRequestMessage implements IMessage{
	/**
	 * 图片消息
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	
	/**
	 * 小视频消息
	 */
	public static final String REQ_MESSAGE_TYPE_SHORT_VIDEO = "shortvideo";
	
	/**
	 * 定位消息
	 */
	public static final String REQ_MESSAGE_TYPE_SHORT_LOCATION = "location";
	
	/**
	 * 链接消息
	 */
	public static final String REQ_MESSAGE_TYPE_SHORT_LINK = "link";
	
	/**
	 * 事件消息
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";
	
    private String toUserName;  
    private String fromUserName;  
    private long createTime;  
    private long msgId;
    protected String msgType;
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public long getMsgId() {
		return msgId;
	}
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	public String getMsgType() {
		return msgType;
	}
	
	public BaseRequestMessage(String toUserName, String fromUserName,
			long createTime) {
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
	}
}
