package cn.web.front.action.wechat.model.message.response;

import cn.web.front.action.wechat.model.message.IMessage;

/**
 * 回复消息
 * @author gaoxigang
 *
 */
public abstract class BaseResponseMessage implements IMessage{
	/**
	 * 音乐消息
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	
	/**
	 * 图文消息
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	
	// 接收方帐号（收到的OpenID）  
    private String toUserName;  
    // 开发者微信号  
    private String fromUserName;  
    // 消息创建时间 （整型）  
    private long createTime; 
    // 位0x0001被标志时，星标刚收到的消息  
    private int funcFlag;
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

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(int funcFlag) {
		this.funcFlag = funcFlag;
	}
	
	public String getMsgType() {
		return msgType;
	}
	
	public BaseResponseMessage(String toUserName, String fromUserName,
			long createTime) {
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
	}

	public BaseResponseMessage(){}
}
