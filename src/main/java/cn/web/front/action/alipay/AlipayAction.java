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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.UserBindAlipay;
import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.service.IAccountService;
import cn.illegal.bean.IllegalInfoBean;
import cn.illegal.service.IIllegalService;
import cn.message.bean.CardReceive;
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
	
	@Autowired
    @Qualifier("illegalService")
	private IIllegalService illegalService;
	
	/**
	 * （测试）是否领过卡
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/isReceiveCardTest.html")
	public void isReceiveCardTest(HttpServletRequest request, HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		
		String userId = request.getParameter("openId");
		String type = request.getParameter("type");//领取类型
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		if(StringUtils.isBlank(userId)){
    		baseBean.setMsg("openId 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		if(StringUtils.isBlank(type)){
    		baseBean.setMsg("type 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}else{
    		if("1".equals(type)){
    			type = CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER;//驾驶证
    		}else if("2".equals(type)){
    			type = CardReceiveConstants.CARD_RECEIVE_TYPE_CAR;//行驶证
    		}else{
    			baseBean.setMsg("type 非法！");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
    		}
    	}
		if(StringUtils.isBlank(sourceOfCertification)){
    		baseBean.setMsg("sourceOfCertification 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		
		try {
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				//用户授权异常
				//response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardFail?type="+type+"&msg=用户授权异常!");
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("用户授权异常!");
				renderJSON(baseBean);
				return;
			}
			
			String certNo = userBindAlipay.getIdCard();
			String mobileNumber = userBindAlipay.getMobileNumber();
			
			//查询认证基本信息
			AuthenticationBasicInformationVo baseInfo = accountService.authenticationBasicInformationQuery(certNo, sourceOfCertification);
			if(baseInfo != null){
				String zt = baseInfo.getZt();//用户状态
				if("已激活".equals(zt)){
					logger.info("【支付宝卡包】用户状态zt="+zt);
					//是否已领取证件
					int count = alipayService.queryReceiveCardCount(certNo, type);
					if(count > 0){
						logger.info("【支付宝卡包】跳转到支付宝“证件夹”页面");
						//已领取，跳转到支付宝“证件夹”页面
						//response.sendRedirect("alipays://platformapi/startapp?appId=60000032");
						baseBean.setCode("0003");
						baseBean.setMsg("alipays://platformapi/startapp?appId=60000032");
						renderJSON(baseBean);
						return;
					}else{
						if(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER.equals(type)){
							logger.info("【支付宝卡包】未领取，获取我的驾驶证信息给前端展示");
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
						}else if(CardReceiveConstants.CARD_RECEIVE_TYPE_CAR.equals(type)){
							logger.info("【支付宝卡包】未领取，获取绑定行驶证信息给前端展示");
							//获取绑定行驶证信息
							List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
							for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
								String code = bindTheVehicleVo.getCode();
								if(code != null){
									//没有绑定机动车
									baseBean.setCode(code);
									baseBean.setMsg(bindTheVehicleVo.getMsg());
									renderJSON(baseBean);
									return;
								}
							}
							//有绑定机动车
							baseBean.setCode(MsgCode.success);
							baseBean.setData(bndTheVehicles);
							renderJSON(baseBean);
							return;
						}
					}
				}else{
					logger.info("【支付宝卡包】xxcj08 获取用户状态zt="+zt);
				}
			}
			
			//查询身份认证是否审核通过
			/*List<IdentificationOfAuditResultsVo> auditResults = accountService.getIdentificationOfAuditResults(certNo, sourceOfCertification);
			if(auditResults != null){
				for (IdentificationOfAuditResultsVo vo : auditResults) {
					if("1".equals(vo.getSHZT()) || "-1".equals(vo.getSHZT())){//审核状态为1-审核通过    还有-1？？？
						logger.info("【支付宝卡包】身份认证审核通过SHZT="+vo.getSHZT());
						//是否已领取证件
						int count = alipayService.queryReceiveCardCount(certNo, type);
						if(count > 0){
							logger.info("【支付宝卡包】跳转到支付宝“证件夹”页面");
							//已领取，跳转到支付宝“证件夹”页面
							response.sendRedirect("alipays://platformapi/startapp?appId=60000032");
							return;
						}else{
							if(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER.equals(type)){
								logger.info("【支付宝卡包】未领取，获取我的驾驶证信息给前端展示");
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
							}else if(CardReceiveConstants.CARD_RECEIVE_TYPE_CAR.equals(type)){
								logger.info("【支付宝卡包】未领取，获取绑定行驶证信息给前端展示");
								//获取绑定行驶证信息
								List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
								for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
									String code = bindTheVehicleVo.getCode();
									if(code != null){
										//没有绑定机动车
										baseBean.setCode(code);
										baseBean.setMsg(bindTheVehicleVo.getMsg());
										renderJSON(baseBean);
										return;
									}
								}
								//有绑定机动车
								baseBean.setCode(MsgCode.success);
								baseBean.setData(bndTheVehicles);
								renderJSON(baseBean);
								return;
							}
						}
					}
				}
			}*/
			
			logger.info("【支付宝卡包】非星级用户！");
			//非星级用户
			//response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardFail?type="+type+"&msg=亲，需要成为星级用户才能享受服务！");
			baseBean.setCode(MsgCode.businessError);
			baseBean.setMsg("亲，需要成为星级用户才能享受服务！");
			renderJSON(baseBean);
			return;
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】isReceiveCardTest异常：userId="+userId+",type="+type+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
		logger.info("【支付宝卡包】isReceiveCardTest结果："+JSON.toJSONString(baseBean));
	}
	
	
	/**
	 * （测试）领取驾驶证
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/receiveJsCardTest.html")
	public void receiveJsCardTest(HttpServletRequest request, HttpServletResponse response){
		
		String userId = request.getParameter("openId");
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		try {
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				//用户授权异常
				response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardFail?type=1&msg=用户授权异常!");
				return;
			}
			
			String certNo = userBindAlipay.getIdCard();
			String realName = userBindAlipay.getRealName();
			String mobileNumber = userBindAlipay.getMobileNumber();
			
			//查询身份认证是否审核通过
			/*List<IdentificationOfAuditResultsVo> auditResults = accountService.getIdentificationOfAuditResults(certNo, sourceOfCertification);
			if(auditResults != null){
				for (IdentificationOfAuditResultsVo vo : auditResults) {
					if("1".equals(vo.getSHZT()) || "-1".equals(vo.getSHZT())){//审核状态为1-审核通过    还有-1？？？
						logger.info("【支付宝卡包】身份认证审核通过SHZT="+vo.getSHZT());*/
			
			
			//查询认证基本信息
			AuthenticationBasicInformationVo baseInfo = accountService.authenticationBasicInformationQuery(certNo, sourceOfCertification);
			if(baseInfo != null){
				String zt = baseInfo.getZt();//用户状态
				if("已激活".equals(zt)){
					logger.info("【支付宝卡包】用户状态zt="+zt);
					//是否已领取证件
					int count = alipayService.queryReceiveCardCount(certNo, CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER);
					if(count > 0){
						logger.info("【支付宝卡包】跳转到领取成功页面");
						//已领取，跳转到领取成功页面
						response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardSuccess?type=1&msg=您好！您的深圳交警电子驾驶证已经领取");
						return;
					}else{
						//未领取驾驶证
						
						logger.info("【支付宝卡包】未领取驾驶证");
						//调JST接口获取电子驾驶证
						ElectronicDriverLicenseVo eCardInfo = accountService.getElectronicDriverLicense(certNo, realName, mobileNumber, "Z");
						if(!MsgCode.success.equals(eCardInfo.getCode())){
							//获取失败
							response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardFail?type=1&msg="+eCardInfo.getMsg());
							return;
						}
						
						String eCardImgBase64 = eCardInfo.getElectronicDriverLicense();//证件图片base64
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
						json.put("file", eCardImgBase64);
						
						//调支付宝sdk上传证件照片
						
						//上传失败，提示领取失败
						
						//上传成功，获取我的驾驶证信息
						MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, sourceOfCertification);
						if(!MsgCode.success.equals(myDriverLicense.getCode())){
							response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardFail?type=1&msg="+myDriverLicense.getMsg());
							return;
						}
						
						String unresolvedIllegalCount = "0";//未处理违章数
						//查询绑定机动车
						List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
						for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
			        		String isMyself = bindTheVehicleVo.getIsMyself();
			        		if("本人".equals(isMyself)){
			        			String licensePlateNo = bindTheVehicleVo.getNumberPlateNumber();
			        			String licensePlateType = bindTheVehicleVo.getPlateType();
			        			String vehicleIdentifyNoLast4 = bindTheVehicleVo.getBehindTheFrame4Digits();
			        			//查询未处理违章数
			        			BaseBean baseBean = illegalService.queryInfoByLicensePlateNo(licensePlateNo, licensePlateType, vehicleIdentifyNoLast4, userId, sourceOfCertification);
			        			if(MsgCode.success.equals(baseBean.getCode())){
			        				List<IllegalInfoBean> illegalInfoBeans = (List<IllegalInfoBean>) baseBean.getData();
			        				unresolvedIllegalCount = illegalInfoBeans.size()+"";
			        			}
			        		}
						}
						logger.info("【支付宝卡包】领取驾驶证查询未处理违章数："+unresolvedIllegalCount);
						
						//封装驾驶证信息
						
						//调支付宝sdk提交驾驶证信息
						
						//提交成功，写入数据库
						CardReceive cardReceive = new CardReceive();
						cardReceive.setAlipayUserId(userId);
						cardReceive.setArchiveNumber(myDriverLicense.getFileNumber());//档案编号
						cardReceive.setCertNo(certNo);
						cardReceive.setGender("1".equals(myDriverLicense.getGender()) ? "男" : "女");
						cardReceive.setRealName(realName);
						cardReceive.setType(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER);
						alipayService.insertCardReceive(cardReceive);
						
						logger.info("【支付宝卡包】跳转到领取成功页面");
						//领取成功，跳转到领取成功页面
						response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardSuccess?type=1&msg=您好！您的深圳交警电子驾驶证已经领取");
						return;
						
						//提交失败，提示用户
						
						
					}
				}
			}
			//}
			
			logger.info("【支付宝卡包】非星级用户！");
			response.sendRedirect("http://zhifubao.chudaokeji.com/#/getCardFail?type=1&msg=亲，需要成为星级用户才能享受服务！");
			return;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
