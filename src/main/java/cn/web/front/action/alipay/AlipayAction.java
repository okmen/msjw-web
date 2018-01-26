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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;

import cn.account.bean.UserBindAlipay;
import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.service.IAccountService;
import cn.illegal.bean.IllegalInfoBean;
import cn.illegal.service.IIllegalService;
import cn.message.bean.CardReceive;
import cn.message.model.alipay.AlipayServiceEnvConstants;
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
	 * 是否领过卡
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/isReceiveCard.html")
	public void isReceiveCard(HttpServletRequest request, HttpServletResponse response){
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
				logger.info("【支付宝卡包】用户授权异常，userId=" + userId);
				//用户授权异常
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
						logger.info("【支付宝卡包】跳转到支付宝“证件夹”页面，certNo=" + certNo);
						//已领取，跳转到支付宝“证件夹”页面
						baseBean.setCode("0003");
						baseBean.setData("alipays://platformapi/startapp?appId=60000032");
						renderJSON(baseBean);
						return;
					}else{
						if(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER.equals(type)){
							logger.info("【支付宝卡包】未领取，获取我的驾驶证信息给前端展示，certNo=" + certNo);
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
							logger.info("【支付宝卡包】未领取，获取绑定行驶证信息给前端展示，certNo=" + certNo);
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
					logger.info("【支付宝卡包】xxcj08 获取用户状态zt="+zt+"，certNo=" + certNo);
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
			
			logger.info("【支付宝卡包】非星级用户！，certNo=" + certNo);
			//非星级用户
			baseBean.setCode(MsgCode.businessError);
			baseBean.setMsg("亲，需要成为星级用户才能享受服务！");
			renderJSON(baseBean);
			return;
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】isReceiveCard异常：userId="+userId+",type="+type+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
	}
	
	
	/**
	 * 领取驾驶证
	 * @descrption TODO(领取驾驶证)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/receiveJsCard.html")
	public void receiveJsCard(HttpServletRequest request, HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		
		String userId = request.getParameter("openId");
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		if(StringUtils.isBlank(userId)){
    		baseBean.setMsg("openId 不能为空!");
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
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			//用户授权异常
			if(userBindAlipay == null){
				logger.info("【支付宝卡包】用户授权异常，userId=" + userId);
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("用户授权异常!");
				renderJSON(baseBean);
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
			/*AuthenticationBasicInformationVo baseInfo = accountService.authenticationBasicInformationQuery(certNo, sourceOfCertification);
			if(baseInfo != null){
				String zt = baseInfo.getZt();//用户状态
				if("已激活".equals(zt)){
					logger.info("【支付宝卡包】用户状态zt="+zt);*/
			
			
					//是否已领取证件
					int count = alipayService.queryReceiveCardCount(certNo, CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER);
					if(count > 0){
						//已领取，跳转到领取成功页面
						logger.info("【支付宝卡包】已领取驾驶证卡包，certNo=" + certNo);
						baseBean.setCode(MsgCode.businessError);
						baseBean.setMsg("您好！您的深圳交警电子驾驶证已经领取");
						renderJSON(baseBean);
						return;
					}else{
						//未领取驾驶证
						
						//获取驾驶证信息
						MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, sourceOfCertification);
						if(!MsgCode.success.equals(myDriverLicense.getCode())){
							logger.info("【支付宝卡包】获取驾驶证信息失败，certNo=" + certNo + "，msg=" + myDriverLicense.getMsg());
							baseBean.setCode(myDriverLicense.getCode());
							baseBean.setMsg(myDriverLicense.getMsg());
							renderJSON(baseBean);
							return;
						}
						
						//未处理违章数
						int unresolvedIllegalCount = getUnresolvedIllegalCount(certNo, mobileNumber, sourceOfCertification, userId);
						
						//获取电子驾驶证照片
						ElectronicDriverLicenseVo eCardInfo = accountService.getElectronicDriverLicense(certNo, realName, mobileNumber, sourceOfCertification);
						if(!MsgCode.success.equals(eCardInfo.getCode())){
							logger.info("【支付宝卡包】获取电子驾驶证照片失败，certNo=" + certNo + "，msg=" + eCardInfo.getMsg());
							baseBean.setCode(eCardInfo.getCode());
							baseBean.setMsg(eCardInfo.getMsg());
							renderJSON(baseBean);
							return;
						}
						
						String eCardImgBase64 = eCardInfo.getElectronicDriverLicense();//证件图片base64
						
						//调支付宝sdk上传证件照片
						BaseBean uploadJsCardImg = alipayService.uploadJsCardImg(eCardImgBase64);
						//上传失败
						if(!MsgCode.success.equals(uploadJsCardImg.getCode())){
							logger.info("【支付宝卡包】上传驾驶证正面失败，certNo=" + certNo + "，上传结果：" + JSON.toJSONString(uploadJsCardImg));
							baseBean.setCode(MsgCode.businessError);
							baseBean.setMsg("传输异常！");
							renderJSON(baseBean);
							return;
						}
						
						
						//封装驾驶证信息
						String imgUrl = (String) uploadJsCardImg.getData();
						JSONObject jsCardInfo = jsCardInfo(userBindAlipay, myDriverLicense, String.valueOf(unresolvedIllegalCount), imgUrl);
						
						//调支付宝sdk提交驾驶证信息
						BaseBean sendCardInfo = alipayService.sendCardInfo(jsCardInfo.toJSONString());
						//提交失败
						if(!MsgCode.success.equals(sendCardInfo.getCode())){
							logger.info("【支付宝卡包】提交驾驶证信息失败，certNo=" + certNo + "，提交结果：" + JSON.toJSONString(sendCardInfo));
							baseBean.setCode(MsgCode.businessError);
							baseBean.setMsg("提交异常！");
							renderJSON(baseBean);
							return;
						}
						
						//提交成功，写入数据库
						CardReceive cardReceive = new CardReceive();
						cardReceive.setAlipayUserId(userId);
						cardReceive.setArchiveNumber(myDriverLicense.getFileNumber());//档案编号
						cardReceive.setCertNo(certNo);
						cardReceive.setGender("1".equals(myDriverLicense.getGender()) ? "男" : "女");
						cardReceive.setRealName(realName);
						cardReceive.setType(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER);
						alipayService.insertCardReceive(cardReceive);
						
						//领取成功
						logger.info("【支付宝卡包】电子驾驶证领取成功！certNo=" + certNo);
						baseBean.setCode(MsgCode.success);
						baseBean.setMsg("您好！您的深圳交警电子驾驶证已经领取");
						renderJSON(baseBean);
						return;
					}
					
				/*}
			}*/
					
			//}
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】receiveJsCard异常：userId="+userId+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
	}
	
	/**
	 * TODO:未处理违章数
	 * @param certNo 
	 * @param mobileNumber 
	 * @param sourceOfCertification 
	 * @param userId 
	 */
	public int getUnresolvedIllegalCount(String certNo, String mobileNumber, String sourceOfCertification, String userId){
		int unresolvedIllegalCount = 0;
		try {
			//查询本人机动车
			List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
			for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
				String isMyself = bindTheVehicleVo.getIsMyself();
				if("本人".equals(isMyself)){
					String licensePlateNo = bindTheVehicleVo.getNumberPlateNumber();
					String licensePlateType = bindTheVehicleVo.getPlateType();
					String vehicleIdentifyNoLast4 = bindTheVehicleVo.getBehindTheFrame4Digits();
					//查询未处理违章数
					BaseBean respBean = illegalService.queryInfoByLicensePlateNo(licensePlateNo, licensePlateType, vehicleIdentifyNoLast4, userId, sourceOfCertification);
					if(MsgCode.success.equals(respBean.getCode())){
						List<IllegalInfoBean> illegalInfoBeans = (List<IllegalInfoBean>) respBean.getData();
						unresolvedIllegalCount = illegalInfoBeans.size() + unresolvedIllegalCount;
					}
				}
			}
			logger.info("【支付宝卡包】未处理违章数：" + unresolvedIllegalCount + "，certNo=" + certNo);
		} catch (Exception e) {
			logger.error("【支付宝卡包】查询未处理违章数异常：certNo=" + certNo + "，userId=" + userId, e);
			e.printStackTrace();
		}
		return unresolvedIllegalCount;
	}
	
	/**
	 * 领取行驶证
	 * @descrption TODO(领取行驶证)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/receiveXsCard.html")
	public void receiveXsCard(HttpServletRequest request, HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		
		String userId = request.getParameter("openId");
		//String gender = request.getParameter("gender");
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		if(StringUtils.isBlank(userId)){
    		baseBean.setMsg("openId 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		/*if(StringUtils.isBlank(gender)){
			baseBean.setMsg("gender 不能为空!");
			baseBean.setCode(MsgCode.paramsError);
			renderJSON(baseBean);
			return;
		}*/
		if(StringUtils.isBlank(sourceOfCertification)){
    		baseBean.setMsg("sourceOfCertification 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		
		try {
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			//用户授权异常
			if(userBindAlipay == null){
				logger.info("【支付宝卡包】用户授权异常，userId=" + userId);
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("用户授权异常!");
				renderJSON(baseBean);
				return;
			}
			
			String certNo = userBindAlipay.getIdCard();
			String realName = userBindAlipay.getRealName();
			String mobileNumber = userBindAlipay.getMobileNumber();
			
			//是否已领取证件
			int count = alipayService.queryReceiveCardCount(certNo, CardReceiveConstants.CARD_RECEIVE_TYPE_CAR);
			if(count > 0){
				//已领取，跳转到领取成功页面
				logger.info("【支付宝卡包】已领取行驶证卡包，certNo=" + certNo);
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("您好！您的深圳交警电子行驶证已经领取");
				renderJSON(baseBean);
				return;
			}else{
				//未领取行驶证
				
				//获取所有绑定机动车
				List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
				for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
					String code = bindTheVehicleVo.getCode();
					//没有绑定机动车
					if(code != null){
						logger.info("【支付宝卡包】没有绑定车辆信息，certNo=" + certNo);
						baseBean.setCode(code);
						baseBean.setMsg(bindTheVehicleVo.getMsg());
						renderJSON(baseBean);
						return;
					}
					
					//查询电子证件照片
					String plateNo = bindTheVehicleVo.getNumberPlateNumber();
					String plateType = bindTheVehicleVo.getPlateType();
					String mobilephone = bindTheVehicleVo.getMobilephone();
					DrivingLicenseVo eCardInfo = accountService.getDrivingLicense(plateNo, plateType, mobilephone, sourceOfCertification);
					if(MsgCode.success.equals(eCardInfo.getCode())){
						String eCardImgBase64 = eCardInfo.getElectronicDrivingLicense();//证件图片base64
						
						//调支付宝sdk上传证件照片
						BaseBean uploadXsCardImg = alipayService.uploadXsCardImg(eCardImgBase64);
						//上传失败
						if(!MsgCode.success.equals(uploadXsCardImg.getCode())){
							logger.info("【支付宝卡包】上传行驶证正面失败，certNo=" + certNo + "，上传结果：" + JSON.toJSONString(uploadXsCardImg));
							baseBean.setCode(MsgCode.businessError);
							baseBean.setMsg("传输异常！");
							renderJSON(baseBean);
							return;
						}
						
						//封装行驶证信息
						String imgUrl = (String) uploadXsCardImg.getData();
						JSONObject xsCardInfo = xsCardInfo(userBindAlipay, bindTheVehicleVo, imgUrl);
						
						//上传成功，调支付宝sdk提交行驶证信息
						BaseBean sendCardInfo = alipayService.sendCardInfo(xsCardInfo.toJSONString());
						//提交失败
						if(!MsgCode.success.equals(sendCardInfo.getCode())){
							logger.info("【支付宝卡包】提交行驶证信息失败，certNo=" + certNo + "，提交结果：" + JSON.toJSONString(sendCardInfo));
							baseBean.setCode(MsgCode.businessError);
							baseBean.setMsg("提交异常！");
							renderJSON(baseBean);
							return;
						}
					}
				}
				
				//提交成功，写入数据库
				CardReceive cardReceive = new CardReceive();
				cardReceive.setAlipayUserId(userId);
				cardReceive.setArchiveNumber("");//档案编号
				cardReceive.setCertNo(certNo);
				cardReceive.setGender("");//gender
				cardReceive.setRealName(realName);
				cardReceive.setType(CardReceiveConstants.CARD_RECEIVE_TYPE_CAR);
				alipayService.insertCardReceive(cardReceive);
				
				//领取成功
				logger.info("【支付宝卡包】电子行驶证领取成功！certNo=" + certNo);
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg("您好！您的深圳交警电子行驶证已经领取");
				renderJSON(baseBean);
				return;
				
			}
		} catch (Exception e) {
			logger.error("【支付宝卡包】receiveXsCard异常：userId="+userId+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
	}
	
	
	/**
	 * TODO:驾驶证信息组装
	 * @return
	 */
	public JSONObject jsCardInfo(UserBindAlipay userInfo, MyDriverLicenseVo vo, String unresolvedIllegalCount, String imgUrl){
		
		JSONObject cardImg = new JSONObject();
		cardImg.put("driving_license_home_page", imgUrl);//驾照正面图片
		cardImg.put("driving_license_sub_page", "");//驾照副页图片
		
		JSONObject cardInfo = new JSONObject();
		cardInfo.put("vd_license_number", vo.getDriverLicenseNumber());			//驾驶证号
		cardInfo.put("vd_name", vo.getName());									//姓名
		cardInfo.put("gender", "1".equals(vo.getGender()) ? "男" : "女");			//性别
		cardInfo.put("vd_license_u_number", vo.getFileNumber());				//驾驶证档案编号(U)
		cardInfo.put("vd_grant_time", "");										//发证时间
		cardInfo.put("valid_thru", vo.getEffectiveDate());						//有效期限
		cardInfo.put("vd_address", "");											//住址
		cardInfo.put("vd_first_time", "");										//初次领证的时间
		cardInfo.put("vd_driving_type", vo.getCarType());						//准驾车型
		cardInfo.put("vd_nationality", "");										//国籍
		cardInfo.put("issuing_authority", "");									//签发机关
		cardInfo.put("vd_city_code", "");										//发证的地市Code
		cardInfo.put("vd_city_name", "");										//发证的地市名称
		cardInfo.put("license_cetify_level", "ORG");							//证件认证等级
		cardInfo.put("illegal_violation_count", unresolvedIllegalCount);		//未处理违章条数
		cardInfo.put("cumulative_score", vo.getDeductScore());					//累计记分
		cardInfo.put("factual_validity_check", vo.getPhysicalExaminationDate());//审验有效期止
		cardInfo.put("date_of_inspection", vo.getPhysicalExaminationDate());	//审验日期
		cardInfo.put("status", vo.getStatus());									//状态
		cardInfo.put("cert_doc_type", "SZ_E_DRIVING_LICENSE");					//证件类型
		cardInfo.put("mobile_no", userInfo.getMobileNumber());					//手机号码
		cardInfo.put("license_pics", cardImg);									//驾照图片
		
		JSONObject sceneData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(cardInfo);
		sceneData.put("info", jsonArray);
		
		JSONObject baseInfo = new JSONObject();
		baseInfo.put("scene_code", "vehicle_driving_licence");
		baseInfo.put("op_code", "data_save");
		baseInfo.put("channel", "SZJJ");
		baseInfo.put("version", "1.0");
		baseInfo.put("target_id", userInfo.getUserId());
		baseInfo.put("target_id_type", "");
		baseInfo.put("scene_data", sceneData);
		
		return baseInfo;
		
		/*{ "scene_code", "vehicle_driving_licence" },
        { "op_code", "data_save" },
        { "channel", "SZJJ" },
        { "version", "1.0" },
        { "target_id", AliUserInfo["alipay_user_id"].ToString() },
        { "target_id_type", "" },
        { "scene_data", 
            new JObject(){
                { "info", 
                    new JArray(){
                        new JObject(){
                            { "vd_license_number", Data["result"]["sfzmhm"] },                            //驾驶证号
                            { "vd_name", Data["result"]["xm"] },                                          //姓名
                            { "gender", (Data["result"]["xb"].ToString() == "1" ? "男" : "女") },         //性别
                            { "vd_license_u_number", Data["result"]["dabh"] },                            //驾驶证档案编号(U)
                            { "vd_grant_time", "" },                                                      //发证时间
                            { "valid_thru", Data["result"]["yxqz"] },                                     //有效期限
                            { "vd_address", "" },                                                         //住址
                            { "vd_first_time", "" },                                                      //初次领证的时间
                            { "vd_driving_type", Data["result"]["zjcx"] },                                //准驾车型
                            { "vd_nationality", "" },                                                     //国籍
                            { "issuing_authority", "" },                                                  //签发机关
                            { "vd_city_code", "" },                                                       //发证的地市Code
                            { "vd_city_name", "" },                                                       //发证的地市名称
                            { "license_cetify_level", "ORG" },                                            //证件认证等级
                            { "illegal_violation_count", EndorsedNumber },                                //未处理违章条数
                            { "cumulative_score", Data["result"]["ljjf"] },                               //累计记分
                            { "factual_validity_check", Data["result"]["syyxqz"] },                       //审验有效期止
                            { "date_of_inspection", Data["result"]["syrq"] },                             //审验日期
                            { "status", Data["result"]["zt"] },                                           //状态
                            { "cert_doc_type", "SZ_E_DRIVING_LICENSE" },                                  //证件类型
                            { "mobile_no", AliUserInfo["mobile"].ToString() },                                //手机号码
                            { "license_pics",                                                             //驾照图片
                                new JObject(){
                                    { "driving_license_home_page", ImageUploadResult["alipay_zdatafront_datatransfered_fileupload_response"]["result_data"] },                        //驾照正面图片
                                    { "driving_license_sub_page", "" },                                   //驾照副页图片
                                }
                            },
                        }
                    }
                }
            }
        },
		*/
	}
	
	/**
	 * TODO:行驶证信息组装
	 * @return
	 */
	public JSONObject xsCardInfo(UserBindAlipay userInfo, BindTheVehicleVo vo, String imgUrl){
		
		JSONObject cardImg = new JSONObject();
		cardImg.put("vehicle_license_home_page", imgUrl);//行驶证正面图片
		cardImg.put("vehicle_license_sub_page", "");//行驶证副页图片
		
		JSONObject cardInfo = new JSONObject();
		cardInfo.put("plate_no", vo.getNumberPlateNumber());	//车牌号
		cardInfo.put("plate_type", vo.getPlateType());			//车牌号类型
		cardInfo.put("vehicle_type", "");						//车辆类型
		cardInfo.put("owner", vo.getName());					//车辆所有人
		cardInfo.put("owener_cert_no", vo.getIdentityCard());	//车辆所有人身份证号码
		cardInfo.put("vin", vo.getBehindTheFrame4Digits());		//车辆识别代号
		cardInfo.put("engine_no", "");							//发动机号
		cardInfo.put("check_date", vo.getAnnualReviewDate());	//车辆审验日期
		cardInfo.put("whether_one_self", "本人".equals(vo.getIsMyself()) ? "是" : "否");//是否本人车辆
		cardInfo.put("check_date_remind", "");					//下次审验日期提醒
		cardInfo.put("status_remind", "");						//状态提醒？？
		cardInfo.put("abandoned_remind", "");					//强制报废期止提醒？？
		cardInfo.put("mobile_no", vo.getMobilephone());			//申领人手机号码
		cardInfo.put("user_character", "");						//使用性质
		cardInfo.put("regisger_date", "");						//注册时间
		cardInfo.put("issue_date", "");							//发证日期
		cardInfo.put("city_code", "");							//车辆归属地
		cardInfo.put("city_name", "");							//车辆归属地名称
		cardInfo.put("ac", "");									//核载人数
		cardInfo.put("cert_doc_type", "SZ_E_VEHICLE_LICENSE");	//类型
		cardInfo.put("license_pics", cardImg);					//行驶证图片
		
		JSONObject sceneData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(cardInfo);
		sceneData.put("info", jsonArray);
		
		JSONObject baseInfo = new JSONObject();
		baseInfo.put("scene_code", "vehicle_license");
		baseInfo.put("op_code", "data_save");
		baseInfo.put("channel", "SZJJ");
		baseInfo.put("version", "1.0");
		baseInfo.put("target_id", userInfo.getUserId());
		baseInfo.put("target_id_type", "ALIPAY_USER_ID");
		baseInfo.put("scene_data", sceneData);
		
		return baseInfo;
		
		/*{ "scene_code", "vehicle_license" },
        { "op_code", "data_save" },
        { "channel", "SZJJ" },
        { "version", "1.0" },
        { "target_id", AliUserInfo["alipay_user_id"].ToString() },
        { "target_id_type", "ALIPAY_USER_ID" },
        { "scene_data", 
            new JObject(){
                { "info", sendInfoData }
            }
        },*/
		
		/*{ "plate_no", plate_no },                                                                                       //车牌号
        { "plate_type", carData["result"]["row"][i]["hpzl"].ToString() },                                               //车牌号类型
        { "vehicle_type", "" },                                                                                         //车辆类型
        { "owner", carData["result"]["row"][i]["czxm"].ToString() },                                                    //车辆所有人
        { "owener_cert_no", carData["result"]["row"][i]["czsfzmhm"].ToString() },                                       //车辆所有人身份证号码
        { "vin", carData["result"]["row"][i]["cjh4"].ToString() },                                                      //车辆识别代号
        { "engine_no", "" },                                                                                            //发动机号
        { "check_date", carData["result"]["row"][i]["syrq"].ToString() },                                               //车辆审验日期
        { "whether_one_self", (carData["result"]["row"][i]["sfbr"].ToString() == "本人") ? "是" : "否" },               //是否本人车辆
        { "check_date_remind", "" },                                                                                    //下次审验日期提醒
        { "status_remind", carData["result"]["row"][i]["zt"].ToString() },                                              //状态提醒
        { "abandoned_remind", carData["result"]["row"][i]["qzbfqz"].ToString() },                                       //强制报废期止提醒
        { "mobile_no", AliUserInfo["mobile"].ToString() },                                                              //申领人手机号码
        { "user_character", "" },                                                                                       //使用性质
        { "regisger_date", "" },                                                                                        //注册时间
        { "issue_date", "" },                                                                                           //发证日期
        { "city_code", "" },                                                                                            //车辆归属地
        { "city_name", "" },                                                                                            //车辆归属地名称
        { "ac", "" },                                                                                                   //核载人数
        { "cert_doc_type", "SZ_E_VEHICLE_LICENSE" },                                                                    //类型
        { "license_pics",                                                                                               //行驶证图片
            new JObject(){
                { "vehicle_license_home_page", ImageUploadResult["alipay_zdatafront_datatransfered_fileupload_response"]["result_data"] },                                                                    //行驶证正面图片
                { "vehicle_license_sub_page", "" },                                                                     //行驶证副页图片
            }
        },*/
	}
	
	/**
	 * TODO:（供支付宝调用）驾驶证信息查询
	 */
	@RequestMapping(value = "/jsCardInfo.html")
	public JSONObject jsCardInfo(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		
		String userId = request.getParameter("userId");
		if(StringUtils.isBlank(userId)){
			json.put("code", MsgCode.paramsError);
			json.put("msg", "userId不能为空");
			json.put("result", "");
			return json;
    	}
		
		try {
			String certNo = "";
			String mobileNo = "";
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				json.put("code", MsgCode.businessError);
				json.put("msg", "获取用户信息异常");
				json.put("result", "");
				return json;
			}else{
				certNo = userBindAlipay.getIdCard();
				mobileNo = userBindAlipay.getMobileNumber();
			}
				
			//获取驾驶证信息
			MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, "Z");
			JSONObject info = new JSONObject();
			String code = myDriverLicense.getCode();
			String msg = myDriverLicense.getMsg();
			String result = "";
			if(MsgCode.success.equals(code)){
				int unresolvedIllegalCount = getUnresolvedIllegalCount(certNo, mobileNo, "Z", userId);
				info.put("illegal_violation_count", String.valueOf(unresolvedIllegalCount));//未处理违章条数
				info.put("cumulative_score", myDriverLicense.getDeductScore());//累计记分
				info.put("factual_validity_check", myDriverLicense.getEffectiveDate());//审验有效期止
				info.put("date_of_inspection", myDriverLicense.getPhysicalExaminationDate());//审验日期
				info.put("status", myDriverLicense.getStatus());
				
				result = AlipaySignature.rsaEncrypt(info.toJSONString(), AlipayServiceEnvConstants.PUBLIC_KEY, AlipayServiceEnvConstants.CHARSET);
			}
			
			json.put("code", code);
			json.put("msg", msg);
			json.put("result", result);
		} catch (Exception e) {
			logger.error("【支付宝卡包】jsCardInfo驾驶证信息查询异常：userId="+userId, e);
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * TODO:（供支付宝调用）驾驶证二维码查询
	 */
	@RequestMapping(value = "/jsCardQRCode.html")
	public JSONObject jsCardQRCode(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		
		String userId = request.getParameter("userId");
		if(StringUtils.isBlank(userId)){
			json.put("code", MsgCode.paramsError);
			json.put("msg", "userId不能为空");
			json.put("result", "");
			return json;
    	}
		
		try {
			String certNo = "";
			String realName = "";
			String mobileNo = "";
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				json.put("code", MsgCode.businessError);
				json.put("msg", "获取用户信息异常");
				json.put("result", "");
				return json;
			}else{
				certNo = userBindAlipay.getIdCard();
				realName = userBindAlipay.getRealName();
				mobileNo = userBindAlipay.getMobileNumber();
			}
			//获取电子驾驶证照片
			ElectronicDriverLicenseVo eCardInfo = accountService.getElectronicDriverLicense(certNo, realName, mobileNo, "Z");
			JSONObject info = new JSONObject();
			String code = eCardInfo.getCode();
			String msg = eCardInfo.getMsg();
			String result = "";
			if(MsgCode.success.equals(code)){
				info.put("QRCode", eCardInfo.getElectronicDriverLicenseQRCode());
				
				result = AlipaySignature.rsaEncrypt(info.toJSONString(), AlipayServiceEnvConstants.PUBLIC_KEY, AlipayServiceEnvConstants.CHARSET);
			}
			
			json.put("code", code);
			json.put("msg", msg);
			json.put("result", result);
		} catch (Exception e) {
			logger.error("【支付宝卡包】jsCardQRCode驾驶证二维码查询异常：userId="+userId, e);
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * TODO:（供支付宝调用）行驶证二维码查询
	 */
	@RequestMapping(value = "/xsCardQRCode.html")
	public JSONObject xsCardQRCode(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		
		String plateNo = request.getParameter("plateNo");
		String plateType = request.getParameter("plateType");
		String mobileNo = request.getParameter("mobileNo");
		if(StringUtils.isBlank(plateNo)){
			json.put("code", MsgCode.paramsError);
			json.put("msg", "plateNo不能为空");
			json.put("result", "");
			return json;
    	}
		if(StringUtils.isBlank(plateType)){
			json.put("code", MsgCode.paramsError);
			json.put("msg", "plateType不能为空");
			json.put("result", "");
			return json;
		}
		if(StringUtils.isBlank(mobileNo)){
			json.put("code", MsgCode.paramsError);
			json.put("msg", "mobileNo不能为空");
			json.put("result", "");
			return json;
		}
		
		try {
			//获取电子行驶证照片
			DrivingLicenseVo eCardInfo = accountService.getDrivingLicense(plateNo, plateType, mobileNo, "Z");
			JSONObject info = new JSONObject();
			String code = eCardInfo.getCode();
			String msg = eCardInfo.getMsg();
			String result = "";
			if(MsgCode.success.equals(code)){
				info.put("QRCode", eCardInfo.getElectronicDrivingLicenseQRCode());
				
				result = AlipaySignature.rsaEncrypt(info.toJSONString(), AlipayServiceEnvConstants.PUBLIC_KEY, AlipayServiceEnvConstants.CHARSET);
			}
			
			json.put("code", code);
			json.put("msg", msg);
			json.put("result", result);
		} catch (Exception e) {
			logger.error("【支付宝卡包】xsCardQRCode行驶证二维码查询异常：plateNo="+plateNo, e);
			e.printStackTrace();
		}
		return json;
	}
	
	
}
