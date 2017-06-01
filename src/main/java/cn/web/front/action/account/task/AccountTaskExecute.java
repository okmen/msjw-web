package cn.web.front.action.account.task;

import org.apache.commons.lang.ObjectUtils.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.account.bean.ReadilyShoot;
import cn.account.bean.vo.ReadilyShootVo;
import cn.web.front.action.wechat.util.HttpRequest;

/**
 * 
 * @author Mbenben
 *
 */
@SuppressWarnings(value="all")
public class AccountTaskExecute {
	protected static Logger logger = LoggerFactory.getLogger(AccountTaskExecute.class);
	/**
     * 随手拍发送数据给php系统
     */
    public static void sendReadilyShootVoDataToPhp(ReadilyShoot readilyShoot,ReadilyShootVo readilyShootVo){
    	//http://szjj.u-road.com/SZJJAPIServer/index.php?/report/reportfornew?jbbh=1234&wfsj=0&querypwd=123456&wfroad=test&hphm=ABCDE&hpzl=1&wfname=asd&jbr=randy&fromopenid=test123456&imagepath=http://test&phone=123456789&token=Chudao4Wfjj
    	logger.info("readilyShoot=" + readilyShoot);
    	logger.info("readilyShootVo=" + readilyShootVo);
    	/*jbbh 举报成功后返回的编号
    	wfsj 违法时间
    	querypwd 返回的查询密码
    	wfroad 违法路段
    	hphm 号牌
    	hpzl 号牌各类 为数字 
    	wfname 违法行为名
    	jbr 举报人
    	fromopenid 举报人openid
    	imagepath 图片路径
    	phone 举报人手机号
    	token Chudao4Wfjj写死  */
    	//jbbh=1234&wfsj=0&querypwd=123456&wfroad=test&hphm=ABCDE&hpzl=1&wfname=asd&=randy&fromopenid=test123456&imagepath=http://test&phone=123456789&token=Chudao4Wfjj
    	StringBuffer sb = new StringBuffer();
    	StringBuffer imagepath = new StringBuffer();
    	imagepath.append(readilyShoot.getIllegalImg1()).append(",").append(readilyShoot.getIllegalImg2()).append(",").append(readilyShoot.getIllegalImg3());
    	String url = "http://szjj.u-road.com/SZJJAPIServer/index.php?/report/reportfornew?";
    	sb.append(url).append("jbbh=").append(null != readilyShoot.getReportSerialNumber() ? readilyShoot.getReportSerialNumber() : "" ).append("&");
    	sb.append("wfsj=").append(null != readilyShoot.getIllegalTime() ? readilyShoot.getIllegalTime() : "").append("&");
    	sb.append("querypwd=").append(null != readilyShoot.getPassword() ? readilyShoot.getPassword() : "").append("&");
    	sb.append("wfroad=").append(null != readilyShoot.getIllegalSections() ? readilyShoot.getIllegalSections() : "").append("&");
    	sb.append("hphm=").append(null != readilyShootVo.getLicensePlateNumber() ? readilyShootVo.getLicensePlateNumber() : "").append("&");
    	sb.append("hpzl=").append(null != readilyShootVo.getLicensePlateType() ? readilyShootVo.getLicensePlateType() : "").append("&");
    	sb.append("wfname=").append(null != readilyShootVo.getIllegalActivitieOne() ? readilyShootVo.getIllegalActivitieOne() : "").append("&");
    	sb.append("jbr=").append(null != readilyShootVo.getInputMan() ? readilyShootVo.getInputMan() : "").append("&");
    	sb.append("fromopenid=").append(null != readilyShootVo.getOpenId() ? readilyShootVo.getOpenId() : "").append("&");
    	sb.append("imagepath=").append(null != imagepath.toString() ? imagepath.toString() : "").append("&");
    	sb.append("phone=").append(null != readilyShootVo.getInputManPhone() ? readilyShootVo.getInputManPhone() : "").append("&");
    	sb.append("token=").append("Chudao4Wfjj");
    	
    	logger.info("php的url为：" + sb.toString());
    	
    	String respStr = HttpRequest.sendGet(sb.toString());
    	
    	logger.info("调用php返回的结果为：" + respStr);
    }
}
