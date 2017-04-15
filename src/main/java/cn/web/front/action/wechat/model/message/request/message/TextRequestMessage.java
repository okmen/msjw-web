package cn.web.front.action.wechat.model.message.request.message;


public class TextRequestMessage extends BaseRequestMessage {
	
    private String content; 
    
    public String getContent() {
		return content;
	}
    
    public void setContent(String content) {
		this.content = content;
	}
    
	public TextRequestMessage(String toUserName,String fromUserName,long createTime,String content) {
		super(toUserName,fromUserName,createTime);
		this.content = content;
		msgType = MESSAGE_TYPE_TEXT;  
	}
	
	@Override
	public String toXml(){
		return "";
	}
}
