package cn.web.front.action.alipay;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import cn.account.bean.UserBindAlipay;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.IdentificationOfAuditResultsVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.service.IAccountService;
import cn.message.model.alipay.CardReceiveConstants;
import cn.message.service.IAlipayService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayAction extends BaseAction {
	Logger logger = Logger.getLogger(AlipayAction.class);
	
	@Autowired
	@Qualifier("alipayService")
	private IAlipayService alipayService;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	/**
	 * （测试）是否领过卡
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/isReceiveCardTest.html")
	public void isReceiveCardTest(HttpServletRequest request, HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		
		String certNo = request.getParameter("certNo");//身份证号
		String type = request.getParameter("type");//领取类型
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		if(StringUtils.isBlank(certNo)){
    		baseBean.setMsg("certNo 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		if(StringUtils.isBlank(type)){
    		baseBean.setMsg("type 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		if(StringUtils.isBlank(sourceOfCertification)){
    		baseBean.setMsg("sourceOfCertification 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		
		try {
			//查询身份认证是否审核通过
			List<IdentificationOfAuditResultsVo> auditResults = accountService.getIdentificationOfAuditResults(certNo, sourceOfCertification);
			if(auditResults != null){
				for (IdentificationOfAuditResultsVo vo : auditResults) {
					if("1".equals(vo.getSHZT()) || "-1".equals(vo.getSHZT())){//审核状态为1-审核通过    还有-1？？？
						logger.debug("【支付宝卡包】身份认证审核通过SHZT="+vo.getSHZT());
						//是否已领取证件
						int count = alipayService.queryReceiveCardCount(certNo, type);
						if(count > 0){
							logger.debug("【支付宝卡包】跳转到支付宝“证件夹”页面");
							//已领取，跳转到支付宝“证件夹”页面
							response.sendRedirect("alipays://platformapi/startapp?appId=60000032");
							return;
						}else{
							logger.debug("【支付宝】未领取，获取我的驾驶证信息给前端展示");
							//获取我的驾驶证信息
							MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, sourceOfCertification);
							if(MsgCode.success.equals(myDriverLicense.getCode())){
								baseBean.setCode(MsgCode.success);
								baseBean.setData(myDriverLicense);
								renderJSON(baseBean);
								return;
							}else{
								baseBean.setCode(myDriverLicense.getCode());
								baseBean.setMsg(myDriverLicense.getMsg());
								renderJSON(baseBean);
								return;
							}
						}
					}
				}
			}
			
			logger.debug("【支付宝卡包】非星级用户！");
			//非星级用户
			response.sendRedirect("http://testh5.chudaokeji.com/h5/#/getCardFail?type=1&msg=亲，需要成为星级用户才能享受服务！");
			return;
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】isReceiveCardTest异常：certNo="+certNo+",type="+type+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
	}
	
	
	/**
	 * （测试）领取驾驶证
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/receiveJsCardTest.html")
	public void receiveJsCardTest(HttpServletRequest request, HttpServletResponse response){
		
		String userId = request.getParameter("userId");
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		try {
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				//用户授权异常
				response.sendRedirect("http://testh5.chudaokeji.com/h5/#/getCardFail?type=1&msg=用户授权异常!");
				return;
			}
			
			String certNo = userBindAlipay.getIdCard();
			String realName = userBindAlipay.getRealName();
			String mobileNumber = userBindAlipay.getMobileNumber();
			
			//查询身份认证是否审核通过
			List<IdentificationOfAuditResultsVo> auditResults = accountService.getIdentificationOfAuditResults(certNo, sourceOfCertification);
			if(auditResults != null){
				for (IdentificationOfAuditResultsVo vo : auditResults) {
					if("1".equals(vo.getSHZT())){//审核状态为1-审核通过
						logger.debug("【支付宝卡包】审核通过SHZT="+vo.getSHZT());
						//是否已领取证件
						int count = alipayService.queryReceiveCardCount(certNo, CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER);
						if(count > 0){
							logger.debug("【支付宝卡包】跳转到领取成功页面");
							//已领取，跳转到领取成功页面
							response.sendRedirect("http://testh5.chudaokeji.com/h5/#/getCardSuccess?type=1&msg=您好！您的深圳交警电子驾驶证已经领取");
							return;
						}else{
							logger.debug("【支付宝卡包】未领取驾驶证");
							//未领取驾驶证
							//调JST接口获取电子驾驶证
							ElectronicDriverLicenseVo eCardInfo = accountService.getElectronicDriverLicense(certNo, realName, mobileNumber, "Z");
							if(!MsgCode.success.equals(eCardInfo.getCode())){
								//获取失败
								response.sendRedirect("http://testh5.chudaokeji.com/h5/#/getCardFail?type=1&msg="+eCardInfo.getMsg());
								return;
							}
							
							String eCardBase64 = eCardInfo.getElectronicDriverLicense();//证件图片base64
							//String encryptQRCodeBase64 = eCardInfo.getElectronicDriverLicenseQRCode();//加密二维码base64
							
							/*{ "file_type", "trans_picture" },
			                { "type_id", "cif_driving_shenzhen_police_pic" },
			                { "file_description", "深圳交警电子驾照正面" },
			                { "file_name", "certificate.png" },
			                { "file", Convert.FromBase64String(CertificateResult["result"]["dzz"].ToString()) },*/
							JSONObject json = new JSONObject();
							json.put("file_type", "trans_picture");
							json.put("type_id", "cif_driving_shenzhen_police_pic");
							json.put("file_description", "深圳交警电子驾照正面");
							json.put("file_name", "certificate.png");
							json.put("file", eCardBase64);
							
							//上传证件照片到支付宝
							
							
							//
							
						}
					}
				}
			}
			
			logger.debug("【支付宝卡包】非星级用户！");
			response.sendRedirect("http://testh5.chudaokeji.com/h5/#/getCardFail?type=1&msg=亲，需要成为星级用户才能享受服务！");
			return;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
