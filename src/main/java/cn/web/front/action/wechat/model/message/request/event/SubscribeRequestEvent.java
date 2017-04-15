package cn.web.front.action.wechat.model.message.request.event;

public class SubscribeRequestEvent extends BaseRequestEvent {

	public SubscribeRequestEvent(String toUserName, String fromUserName,
			long createTime) {
		super(toUserName, fromUserName, createTime);
		event = REQ_EVENT_TYPE_SUBSCRIBE;
	}

	@Override
	public String toXml() {
		// TODO Auto-generated method stub
		return null;
	}
}
