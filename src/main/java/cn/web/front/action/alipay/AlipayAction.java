package cn.web.front.action.alipay;

import java.io.PrintWriter;
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
import com.alipay.api.AlipayApiException;
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
						
						/*//未处理违章数
						int unresolvedIllegalCount = getUnresolvedIllegalCount(certNo, mobileNumber, sourceOfCertification, userId);
						*/
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
						
						/*//调支付宝sdk上传证件照片
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
						JSONObject jsCardInfo = jsCardInfo(userBindAlipay, myDriverLicense, String.valueOf(unresolvedIllegalCount), imgUrl);*/
						
						JSONObject jsCardInfo = jsCardInfo(userBindAlipay, myDriverLicense, eCardImgBase64);
						
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
						
						/*//调支付宝sdk上传证件照片
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
						JSONObject xsCardInfo = xsCardInfo(userBindAlipay, bindTheVehicleVo, imgUrl);*/
						
						JSONObject xsCardInfo = xsCardInfo(userBindAlipay, bindTheVehicleVo, eCardImgBase64);
						
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
	public JSONObject jsCardInfo(UserBindAlipay userInfo, MyDriverLicenseVo vo, String eCardImgBase64){
		JSONObject extInfo = new JSONObject();
		extInfo.put("userId", userInfo.getUserId());// 支付宝id
		extInfo.put("mobileNo", userInfo.getMobileNumber());// 手机号码
		extInfo.put("drivingLicenseNo", vo.getDriverLicenseNumber());// 证号
		extInfo.put("sex", "1".equals(vo.getGender()) ? "男" : "女");// 性别
		extInfo.put("nationality", "");// 国籍
		extInfo.put("address", "");// 住址
		extInfo.put("dateOfBirth", "");// 出生日期
		extInfo.put("dateOfFirstIssue", "");// 初次领证日期
		extInfo.put("clazz", vo.getCarType());// 准驾车型
		extInfo.put("fileNo", vo.getFileNumber());// 档案编号
		extInfo.put("issuingAuthority", "");// 签发机关
		extInfo.put("valideDate", "");// 生效日期
		extInfo.put("expireDate", vo.getEffectiveDate());// 失效日期
		extInfo.put("source", "");// 采集来源
		
		JSONObject homePage = new JSONObject();
		homePage.put("type", "DRIVING_LICENSE_HOME_PAGE");
		homePage.put("data", eCardImgBase64);//图片的base64字符串，不需要base64头(data:image/jpeg;base64,)
		JSONArray picList = new JSONArray();
		picList.add(homePage);
		
		JSONObject cardInfo = new JSONObject();
		cardInfo.put("cert_type", "SZ_E_DRIVING_LICENSE");//驾驶证
		cardInfo.put("cert_no", userInfo.getIdCard());//证件号
		cardInfo.put("user_id", userInfo.getUserId());//蚂蚁统一会员ID
		cardInfo.put("name", userInfo.getRealName());//证件主体用户姓名。证件类型+证件号+证件姓名需要唯一。
		cardInfo.put("ext_info", extInfo);
		cardInfo.put("pic_list", picList);
		
		return cardInfo;
	}
	
	/**
	 * TODO:行驶证信息组装
	 * @return
	 */
	public JSONObject xsCardInfo(UserBindAlipay userInfo, BindTheVehicleVo vo, String eCardImgBase64){
		JSONObject extInfo = new JSONObject();
		//extInfo.put("mobileNo", userInfo.getMobileNumber());// 手机号码
		extInfo.put("plateNo", vo.getNumberPlateNumber());// 号牌号码
		extInfo.put("vehicleType", vo.getPlateType());// 车辆类型
		extInfo.put("owner", vo.getName());// 所有人
		extInfo.put("address", "");// 住址
		extInfo.put("useCharacter", "");// 使用性质
		extInfo.put("model", "");// 品牌型号
		extInfo.put("vin", vo.getBehindTheFrame4Digits());// 车辆识别代号
		extInfo.put("engineNo", "");// 发动机号码
		extInfo.put("registerDate", "");// 注册日期
		extInfo.put("issueDate", "");// 发证日期
		extInfo.put("issuingAuthority", "");// 签发机关
		extInfo.put("approvedPassengersCapacity", "");// 核定载人数
		extInfo.put("weight", "");// 总质量
		extInfo.put("equipmentWeight", "");// 装备质量
		extInfo.put("approvedLoad", "");// 核定载质量
		extInfo.put("overallDimension", "");// 外廓尺寸
		extInfo.put("tractionMass", "");// 牵引总质量
		extInfo.put("checkDate", vo.getAnnualReviewDate());// 校验有效期
		extInfo.put("source", "");// 采集来源
		
		JSONObject homePage = new JSONObject();
		homePage.put("type", "VEHICLE_LICENSE_HOME_PAGE");
		homePage.put("data", eCardImgBase64);//图片的base64字符串，不需要base64头(data:image/jpeg;base64,)
		JSONArray picList = new JSONArray();
		picList.add(homePage);
		
		JSONObject cardInfo = new JSONObject();
		cardInfo.put("cert_type", "SZ_E_VEHICLE_LICENSE");//行驶证
		cardInfo.put("cert_no", userInfo.getIdCard());//证件号
		cardInfo.put("user_id", userInfo.getUserId());//蚂蚁统一会员ID
		cardInfo.put("name", userInfo.getRealName());//证件主体用户姓名。证件类型+证件号+证件姓名需要唯一。
		cardInfo.put("ext_info", extInfo);
		cardInfo.put("pic_list", picList);
		
		return cardInfo;
	}
	
	
	public String rsaEncrypt(String content){
		try {
			return AlipaySignature.rsaEncrypt(content, AlipayServiceEnvConstants.CARD_PUBLIC_KEY, AlipayServiceEnvConstants.CHARSET);
		} catch (AlipayApiException e) {
			logger.error("【支付宝卡包】rsaEncrypt数据加密异常：content=" + content, e);
			e.printStackTrace();
		}
		return null;
	}
	
	public String rsaDecrypt(String content){
		try {
			return AlipaySignature.rsaDecrypt(content, AlipayServiceEnvConstants.PRIVATE_KEY, AlipayServiceEnvConstants.CHARSET);
		} catch (AlipayApiException e) {
			logger.error("【支付宝卡包】rsaDecrypt数据解密异常：content=" + content, e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * TODO:（供支付宝调用）驾驶证信息查询
	 */
	@RequestMapping(value = "/jsCardInfo.html")
	public void jsCardInfo(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		
		String requestBody = getPostParams(request);
		String decryptBody = rsaDecrypt(requestBody);
		JSONObject parseObject = JSONObject.parseObject(decryptBody);
		String userId = parseObject.getString("userId");
		
		/*String encryptUserId = request.getParameter("userId");
		if(StringUtils.isBlank(encryptUserId)){
			json.put("result", "false");
			json.put("resultMsg", "userId不能为空");
			outEncryptString(response, json.toJSONString());
			return;
		}
		String userId = rsaDecrypt(encryptUserId);*/
		logger.info("【支付宝卡包】支付宝调用驾驶证信息查询入参，userId="+userId);
		
		try {
			String certNo = "";
			String mobileNo = "";
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				json.put("result", "false");
				json.put("resultMsg", "获取用户信息异常");
				outEncryptString(response, json.toJSONString());
				return;
			}else{
				certNo = userBindAlipay.getIdCard();
				mobileNo = userBindAlipay.getMobileNumber();
			}
				
			//获取驾驶证信息
			MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, "Z");
			String code = myDriverLicense.getCode();
			String msg = myDriverLicense.getMsg();
			if(MsgCode.success.equals(code)){
				int unresolvedIllegalCount = getUnresolvedIllegalCount(certNo, mobileNo, "Z", userId);
				json.put("result", "true");
				json.put("cumulative_score", myDriverLicense.getDeductScore());//累计记分
				json.put("date_of_inspection", myDriverLicense.getPhysicalExaminationDate());//审验日期
				json.put("illegal_violation_count", String.valueOf(unresolvedIllegalCount));//未处理违章条数
			}else{
				json.put("result", "false");
				json.put("resultMsg", msg);
			}
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】jsCardInfo驾驶证信息查询异常：userId="+userId, e);
			e.printStackTrace();
			json.put("result", "false");
			json.put("resultMsg", "系统异常，请稍后重试");
		}
		outEncryptString(response, json.toJSONString());
		return;
	}

	/**
	 * TODO:（供支付宝调用）驾驶证二维码查询
	 */
	@RequestMapping(value = "/jsCardQRCode.html")
	public void jsCardQRCode(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		
		String requestBody = getPostParams(request);
		String decryptBody = rsaDecrypt(requestBody);
		JSONObject parseObject = JSONObject.parseObject(decryptBody);
		String userId = parseObject.getString("userId");
		
		/*String encryptUserId = request.getParameter("userId");
		if(StringUtils.isBlank(encryptUserId)){
			json.put("result", "false");
			json.put("resultMsg", "userId不能为空");
			outEncryptString(response, json.toJSONString());
			return;
		}
		String userId = rsaDecrypt(encryptUserId);*/
		logger.info("【支付宝卡包】支付宝调用驾驶证二维码查询入参，userId="+userId);
		
		try {
			String certNo = "";
			String realName = "";
			String mobileNo = "";
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				json.put("result", "false");
				json.put("resultMsg", "获取用户信息异常");
				outEncryptString(response, json.toJSONString());
				return;
			}else{
				certNo = userBindAlipay.getIdCard();
				realName = userBindAlipay.getRealName();
				mobileNo = userBindAlipay.getMobileNumber();
			}
			//获取电子驾驶证照片
			ElectronicDriverLicenseVo eCardInfo = accountService.getElectronicDriverLicense(certNo, realName, mobileNo, "Z");
			String code = eCardInfo.getCode();
			String msg = eCardInfo.getMsg();
			if(MsgCode.success.equals(code)){
				json.put("result", "true");
				json.put("QRCode", eCardInfo.getElectronicDriverLicenseQRCode());
			}else{
				json.put("result", "false");
				json.put("resultMsg", msg);
			}
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】jsCardQRCode驾驶证二维码查询异常：userId="+userId, e);
			e.printStackTrace();
			json.put("result", "false");
			json.put("resultMsg", "系统异常，请稍后重试");
		}
		outEncryptString(response, json.toJSONString());
		return;
	}

	/**
	 * TODO:（供支付宝调用）行驶证二维码查询
	 */
	@RequestMapping(value = "/xsCardQRCode.html")
	public void xsCardQRCode(HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		
		String requestBody = getPostParams(request);
		String decryptBody = rsaDecrypt(requestBody);
		JSONObject parseObject = JSONObject.parseObject(decryptBody);
		String plateNo = parseObject.getString("plateNo");
		String plateType = parseObject.getString("plateType");
		String mobileNo = parseObject.getString("mobileNo");
		
		/*String encryptPlateNo = request.getParameter("plateNo");
		String encryptPlateType = request.getParameter("plateType");
		String encryptMobileNo = request.getParameter("mobileNo");
		if(StringUtils.isBlank(encryptPlateNo)){
			json.put("result", "false");
			json.put("resultMsg", "plateNo不能为空");
			outEncryptString(response, json.toJSONString());
			return;
		}
		if(StringUtils.isBlank(encryptPlateType)){
			json.put("result", "false");
			json.put("resultMsg", "plateType不能为空");
			outEncryptString(response, json.toJSONString());
			return;
		}
		if(StringUtils.isBlank(encryptMobileNo)){
			json.put("result", "false");
			json.put("resultMsg", "mobileNo不能为空");
			outEncryptString(response, json.toJSONString());
			return;
		}
		String plateNo = rsaDecrypt(encryptPlateNo);
		String plateType = rsaDecrypt(encryptPlateType);
		String mobileNo = rsaDecrypt(encryptMobileNo);*/
		logger.info("【支付宝卡包】支付宝调用行驶证二维码查询入参，plateNo="+plateNo+"，plateType="+plateType+"，mobileNo="+mobileNo);
		
		try {
			//获取电子行驶证照片
			DrivingLicenseVo eCardInfo = accountService.getDrivingLicense(plateNo, plateType, mobileNo, "Z");
			String code = eCardInfo.getCode();
			String msg = eCardInfo.getMsg();
			if(MsgCode.success.equals(code)){
				json.put("result", "true");
				json.put("QRCode", eCardInfo.getElectronicDrivingLicenseQRCode());
			}else{
				json.put("result", "false");
				json.put("resultMsg", msg);
			}
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】xsCardQRCode行驶证二维码查询异常：plateNo="+plateNo+"，plateType="+plateType+"，mobileNo="+mobileNo, e);
			e.printStackTrace();
			json.put("result", "false");
			json.put("resultMsg", "系统异常，请稍后重试");
		}
		outEncryptString(response, json.toJSONString());
		return;
	}
	
	/**
	 * 加密返回结果
	 * @param response
	 * @param str
	 */
	public void outEncryptString(HttpServletResponse response,String str){
    	try {
    		logger.info("【支付宝卡包】返回结果待加密前：" + str);
    		PrintWriter out = response.getWriter();
        	out.print(rsaEncrypt(str));//加密返回
		} catch (Exception e) {
			logger.error("写字符串异常");
		}
    }
}
