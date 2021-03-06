package cn.web.front.action.handleservice;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.convenience.bean.MsjwApplyingBusinessVo;
import cn.convenience.bean.MsjwVehicleInspectionVo;
import cn.convenience.service.IMsjwService;
import cn.handle.bean.vo.ApplyCarTemporaryLicenceVo;
import cn.handle.bean.vo.ApplyGatePassVo;
import cn.handle.bean.vo.ApplyInspectionMarkVo;
import cn.handle.bean.vo.ApplyRemoteEntrustedBusinessVo;
import cn.handle.bean.vo.CarMortgageVo;
import cn.handle.bean.vo.CreateVehicleInspectionVo;
import cn.handle.bean.vo.DelegateVehiclesVo;
import cn.handle.bean.vo.DriverChangeContactVo;
import cn.handle.bean.vo.DriverLicenseAnnualVerificationVo;
import cn.handle.bean.vo.DriverLicenseIntoVo;
import cn.handle.bean.vo.DriverLicenseVoluntaryDemotionVo;
import cn.handle.bean.vo.HandleTemplateVo;
import cn.handle.bean.vo.IocomotiveCarChangeContactVo;
import cn.handle.bean.vo.IocomotiveCarReplaceVo;
import cn.handle.bean.vo.RenewalDriverLicenseVo;
import cn.handle.bean.vo.RepairOrReplaceDriverLicenseVo;
import cn.handle.bean.vo.ReplaceMotorVehicleLicensePlateVo;
import cn.handle.bean.vo.VehicleDrivingLicenseVo;
import cn.handle.bean.vo.VehicleInspectionVO;
import cn.handle.service.IHandleService;
import cn.message.model.wechat.MessageChannelModel;
import cn.message.model.wechat.TemplateDataModel;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.bean.BusinessType;
import cn.sdk.util.Constants;
import cn.sdk.util.DateUtil;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
import cn.sdk.util.SXStringUtils;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;
/**
 * 办理类Action
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value="/handleservice/")
@SuppressWarnings(value="all")
public class HandleserviceAction extends BaseAction {
	@Autowired
    @Qualifier("handleService")
    private IHandleService handleService;
	
	@Autowired
    @Qualifier("templateMessageService")
	ITemplateMessageService templateMessageService;
	
	@Autowired
	@Qualifier("msjwService")
	private IMsjwService msjwService;
	
	/**
	 * 补领机动车行驶证
	 * @param name 车主姓名/机动车所有人
	 * @param identificationNum 证件号码
	 * @param numberPlate 号牌号码
	 * @param plateType 号牌种类
	 * @param placeOfDomicile 户籍所在地 1深户，0外籍户口
	 * @param address 住所详细地址
	 * @param receiverName 收件人姓名
	 * @param receiverNumber 收件人号码
	 * @param receiverAddress 收件人地址
	 * @param livePhoto1 居住证正面
	 * @param livePhoto2 居住证反面
	 * @param PHOTO9 身份证（正面）
	 * @param PHOTO10 身份证（反面）
	 * @param DJZSFYJ 机动车登记证书
	 * @param ip ip
	 * @param sourceOfCertification 申请来源
	 * @param foreignPeopleLivingOnTheTable 境外人员临住表
	 * @param openId 微信openId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping("complementTheMotorVehicleDrivingLicense")
    public void complementTheMotorVehicleDrivingLicense(String name,String identificationNum,String numberPlate,String plateType,
    		String placeOfDomicile,String address,String receiverName,String receiverNumber,String receiverAddress,
    		String livePhoto1,String livePhoto2,String PHOTO9, String PHOTO10, String DJZSFYJ,
    		String ip,String sourceOfCertification,String foreignPeopleLivingOnTheTable,String openId,String XSZZP,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("补领机动车行驶证参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		
		
    	BaseBean baseBean = new BaseBean();
    	VehicleDrivingLicenseVo vehicleDrivingLicenseVo = new VehicleDrivingLicenseVo();
    	try {
    		if(StringUtils.isBlank(XSZZP)){
        		baseBean.setMsg("XSZZP不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setXSZZP(XSZZP);
        	}
    		if(StringUtils.isBlank(openId)){
        		baseBean.setMsg("openId不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(name)){
        		baseBean.setMsg("name不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setName(name);
        	}
    		if(StringUtils.isBlank(identificationNum)){
        		baseBean.setMsg("identificationNum不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIDcard(identificationNum);
        	}
    		if(StringUtils.isBlank(numberPlate)){
        		baseBean.setMsg("numberPlate不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLicensePlate(numberPlate);
        	}
    		if(StringUtils.isBlank(plateType)){
        		baseBean.setMsg("plateType不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLicensePlateTpye(plateType);
        	}
    		if(StringUtils.isBlank(placeOfDomicile)){
        		baseBean.setMsg("placeOfDomicile不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setPlaceOfDomicile(placeOfDomicile);
        	}
    		/*if(StringUtils.isBlank(address)){
        		baseBean.setMsg("address不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setAddress(address);
        	}*/
    		if(StringUtils.isBlank(receiverName)){
        		baseBean.setMsg("receiverName不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setReceiverName(receiverName);
        	}
    		if(StringUtils.isBlank(receiverNumber)){
        		baseBean.setMsg("receiverNumber不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setReceiverNumber(receiverNumber);
        	}
    		
    		if(StringUtils.isBlank(receiverAddress)){
        		baseBean.setMsg("receiverAddress不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setMailingAddress(receiverAddress);
        	}
    		/*if(StringUtils.isBlank(livePhoto1)){
        		baseBean.setMsg("livePhoto1不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLivePhoto1(livePhoto1);
        	}
    		if(StringUtils.isBlank(livePhoto2)){
        		baseBean.setMsg("livePhoto2不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLivePhoto2(livePhoto2);
        	}*/
    		if(StringUtils.isBlank(PHOTO9)){
        		baseBean.setMsg("PHOTO9不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIDCardPhoto1(PHOTO9);
        	}
    		if(StringUtils.isBlank(PHOTO10)){
        		baseBean.setMsg("PHOTO10不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIDCardPhoto2(PHOTO10);
        	}
    		
    		if(StringUtils.isBlank(DJZSFYJ)){
        		baseBean.setMsg("DJZSFYJ不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setDriverLicensePhoto(DJZSFYJ);
        	}
    		/*if(StringUtils.isBlank(ip)){
        		baseBean.setMsg("ip不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIp(ip);
        	}*/
    		ip = getIp2(request);
    		if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setSourceOfCertification(sourceOfCertification);
        	}
    		/*if(StringUtils.isBlank(foreignPeopleLivingOnTheTable)){
        		baseBean.setMsg("foreignPeopleLivingOnTheTable不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setForeignPeopleLivingOnTheTable(foreignPeopleLivingOnTheTable);
        	}*/
    		Map<String, Object> map = handleService.complementTheMotorVehicleDrivingLicense(vehicleDrivingLicenseVo);
    		String code = map.get("code").toString();
			String msg = map.get("msg").toString();
			if(MsgCode.success.equals(baseBean.getCode()) && "M".equals(sourceOfCertification)){
				try {
					String waterNumber = "";
					waterNumber = SXStringUtils.deleteChineseCharactertoString(msg);
					waterNumber = waterNumber.replace("，", "");
					waterNumber = waterNumber.replace("：", "");
					waterNumber = waterNumber.replace("。", "");
					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.complementTheMotorVehicleDrivingLicense, waterNumber, DateUtil2.date2str(new Date()));
					baseBean.setData(handleTemplateVo);
					String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
					logger.info("【民生警务】结果页url：" + url);
					JSONObject templateData = new JSONObject();
					templateData.put("openid", openId);
					templateData.put("templateId", handleService.getMsjwHandleTemplateId());
					templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
					templateData.put("keyword1Data", "补领行驶证");	templateData.put("keyword1Color", "#212121");
					templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
					templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
					templateData.put("remarkData", "更多信息请点击详情查看");
					templateData.put("redirectUrl", url);
					String params = templateData.toJSONString();
					JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
					logger.info("【民生警务】发送模板消息结果：" + json);

					//新增到民生警务平台个人中心
					try {
						MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
						businessVo.setTylsbh(waterNumber);
						businessVo.setOpenid(openId);
						businessVo.setEventname("补领行驶证");
						businessVo.setApplyingUrlWx(url);//微信在办跳转地址
						businessVo.setJinduUrlWx(url);//进度查询跳转地址
						msjwService.addApplyingBusiness(businessVo);
					} catch (Exception e) {
						logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
						e.printStackTrace();
					}
					
				} catch (Exception e) {
					logger.error("【民生警务】发送模板消息  失败===", e);
				}
			}
			if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(sourceOfCertification)){
				String waterNumber = "";
				waterNumber = SXStringUtils.deleteChineseCharactertoString(msg);
				waterNumber = waterNumber.replace("，", "");
				waterNumber = waterNumber.replace("：", "");
				waterNumber = waterNumber.replace("。", "");
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		//成功需要发送模板消息
        		HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.complementTheMotorVehicleDrivingLicense, waterNumber, DateUtil2.date2str(new Date()));
        		baseBean.setData(handleTemplateVo);
				try {
					String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A_xszbhlbl";
					String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
					logger.info("返回的url是：" + url);
					logger.info("handleTemplateVo 是：" + handleTemplateVo);
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map1 = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					map1.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
					map1.put("keyword1", new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()),"#212121"));
					map1.put("keyword2", new TemplateDataModel().new Property("补领机动车行驶证","#212121"));
					map1.put("keyword3", new TemplateDataModel().new Property("待受理","#212121"));
					map1.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map1);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
			}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(msg);
        	}
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("complementTheMotorVehicleDrivingLicense 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
	/**
	 * 驾驶证年审
	 * 
	 * @param identificationNO
	 * @param userName
	 * @param identityCard
	 * @param mobilephone
	 * @param placeOfDomicile
	 * @param receiverName
	 * @param receiverNumber
	 * @param receiverAddress
	 * @param PHOTO9
	 * @param PHOTO10
	 * @param JZZA
	 * @param JZZB
	 * @param SHJYPXB
	 * @param PHOTO31
	 * @param postCode
	 * @param loginUser
	 * @param sourceOfCertification
	 * @param userSource
	 * @param request
	 * @param response
	 */
	@RequestMapping("driverLicenseAnnualVerification")
	public void driverLicenseAnnualVerification(String identificationNO, String userName, String identityCard,
			String mobilephone, String placeOfDomicile, String receiverName, String receiverNumber,
			String receiverAddress, String PHOTO9, String PHOTO10, String JZZA, String JZZB, String SHJYPXB,
			String PHOTO31, String postCode, String loginUser, String sourceOfCertification, String userSource,String openId ,
			HttpServletRequest request, HttpServletResponse response) {
		
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("驾驶证年审参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		
		BaseBean baseBean = new BaseBean(); // 创建返回结果
		DriverLicenseAnnualVerificationVo driverLicenseAnnualVerificationVo = new DriverLicenseAnnualVerificationVo();
		String businessType = "N";
		driverLicenseAnnualVerificationVo.setBusinessType(businessType);
		try {
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型!不能为空");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setIdentificationNO(identificationNO);
			}
			driverLicenseAnnualVerificationVo.setPostalcode(postCode);
			if (StringUtil.isBlank(userName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setName(userName);
			}
			if (StringUtil.isBlank(identityCard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setIDcard(identityCard);
			}
			if (StringUtil.isBlank(mobilephone)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setMobilephone(mobilephone);
			}
			if (StringUtil.isBlank(placeOfDomicile)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setPlaceOfDomicile(placeOfDomicile);
				if ("3".equals(placeOfDomicile)) {
					if (StringUtil.isBlank(PHOTO31)) {
						baseBean.setCode(MsgCode.paramsError);
						baseBean.setMsg("境外人员临住表不能为空!");
						renderJSON(baseBean);
						return;
					} else {
						driverLicenseAnnualVerificationVo.setForeignersLiveTable(PHOTO31);
					}
				}
			}
			if (StringUtil.isBlank(receiverName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setReceiverName(receiverName);
			}
			if (StringUtil.isBlank(receiverNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setReceiverNumber(receiverNumber);
			}
			if (StringUtil.isBlank(receiverAddress)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setMailingAddress(receiverAddress);
			}
			if (StringUtil.isBlank(PHOTO9)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setIDCardPhoto1(PHOTO9);
			}
			if (StringUtil.isBlank(PHOTO10)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setIDCardPhoto2(PHOTO10);
			}
			if (StringUtil.isBlank(JZZA)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setLivePhoto1(JZZA);
			}
			if (StringUtil.isBlank(JZZB)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setLivePhoto2(JZZB);
			}
			if (StringUtil.isBlank(SHJYPXB)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("教育审核表不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setEducationDrawingtable(SHJYPXB);
			}
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("登录用户不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setLoginUser(loginUser);
			}
			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setSourceOfCertification(sourceOfCertification);
			}
			if (StringUtil.isBlank(userSource)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("userSource 错误!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseAnnualVerificationVo.setUserSource(userSource);
			}
			if (!"G".equals(sourceOfCertification)) {
				if (StringUtil.isBlank(openId)) {
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("openId 不能为空!");
					renderJSON(baseBean);
					return;
				} 
			}
			
			String ip = getIp2(request);
			driverLicenseAnnualVerificationVo.setIp(ip);
			Map<String, String> map = handleService.driverLicenseAnnualVerification(driverLicenseAnnualVerificationVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String waterNumber = map.get("waterNumber");
				baseBean.setCode("0000");
				if (sourceOfCertification.equals("C")) {
					try {
						/*String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverLicenseAnnualVerification, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("驾驶证年审申请", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverLicenseAnnualVerification, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbn3H9wpHz8dKjXPL9J_xC5s");
						model.setResult_page_style_id("23ClyLHM5Fr790uz7t-fxiodPnL9ohRzcnlGWEudkL8");
						model.setDeal_msg_style_id("23ClyLHM5Fr790uz7t-fxlzJePTelFGvOKtKR4udm1o");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> tmap = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						tmap.put("first", new MessageChannelModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
						tmap.put("keyword1",
								new MessageChannelModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new MessageChannelModel().new Property("驾驶证年审", "#212121"));
						tmap.put("keyword3", new MessageChannelModel().new Property("待初审", "#212121"));
						tmap.put("remark", new MessageChannelModel().new Property("","#212121"));
						model.setData(tmap);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
				
				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverLicenseAnnualVerification, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "机动车驾驶证年度审验");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);
						
						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("机动车驾驶证年度审验");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				
				else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg);
			}
		} catch (Exception e) {
			logger.error("驾驶证年审异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 驾驶证延期换证
	 * 
	 * @param userName
	 * @param identificationNO
	 * @param identityCard
	 * @param driverLicense
	 * @param fileNumber
	 * @param delayDate
	 * @param delayReason
	 * @param sourceOfCertification
	 * @param loginUser
	 * @param PHOTO9
	 * @param PHOTO10
	 * @param JSZZP
	 * @param YQZMZP
	 * @param receiverName
	 * @param receiverNumber
	 * @param receiverAddress
	 * @param request
	 * @param response
	 */
	@RequestMapping("renewalDriverLicense")
	public void renewalDriverLicense(String userName, String identificationNO, String identityCard,
			String driverLicense, String fileNumber, String delayDate, String delayReason, String sourceOfCertification,
			String loginUser, String PHOTO9, String PHOTO10, String JSZZP, String YQZMZP, String receiverName,
			String receiverNumber, String receiverAddress, String openId ,HttpServletRequest request, HttpServletResponse response) {
		
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("驾驶证延期换证参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		
		BaseBean baseBean = new BaseBean(); // 创建返回结果
		RenewalDriverLicenseVo renewalDriverLicenseVo = new RenewalDriverLicenseVo();
		String businessType = "Y";
		renewalDriverLicenseVo.setBusinessType(businessType);
		try {
			if (StringUtil.isBlank(userName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setName(userName);
			}
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型 错误!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setIdentificationNO(identificationNO);
			}
			if (StringUtil.isBlank(identityCard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setIDcard(identityCard);
			}
			if (StringUtil.isBlank(driverLicense)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setDriverLicense(driverLicense);
			}
			if (StringUtil.isBlank(fileNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("档案编号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setFileNumber(fileNumber);
			}
			if (StringUtil.isBlank(delayDate)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("延期日期不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setDelayDate(delayDate);
			}
			if (StringUtil.isBlank(delayReason)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("延期原因不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setDelayReason(delayReason);
			}
			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setSourceOfCertification(sourceOfCertification);
			}
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setLoginUser(loginUser);
			}
			if (StringUtil.isBlank(PHOTO9)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setIDCardPhoto1(PHOTO9);
			}
			if (StringUtil.isBlank(PHOTO10)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setIDCardPhoto2(PHOTO10);
			}
			if (StringUtil.isBlank(JSZZP)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setDriverLicensePhoto(JSZZP);
			}
			if (StringUtil.isBlank(YQZMZP)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("延期证明照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setDelayPhoto(YQZMZP);
			}
			if (StringUtil.isBlank(receiverName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setReceiverName(receiverName);
			}
			if (StringUtil.isBlank(receiverNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setReceiverNumber(receiverNumber);
			}
			if (StringUtil.isBlank(receiverAddress)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				renewalDriverLicenseVo.setMailingAddress(receiverAddress);
			}
			if (StringUtil.isBlank(openId)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("openId 不能为空!");
				renderJSON(baseBean);
				return;
			} 
			String ip = getIp2(request);
			renewalDriverLicenseVo.setIp(ip);
			Map<String, String> map = handleService.renewalDriverLicense(renewalDriverLicenseVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String waterNumber = map.get("waterNumber");
				baseBean.setCode("0000");
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.renewalDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("驾驶证延期换证申请", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.renewalDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "驾驶证延期换证");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);

						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("驾驶证延期换证");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				if ("9999".equals(code)) {
					baseBean.setMsg("输入信息格式有误！");
				} else {
					baseBean.setMsg(msg);
				}
			}
		} catch (Exception e) {
			logger.error("驾驶证延期领证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 驾驶证转入
	 * 
	 * @param userName
	 * @param identificationNO
	 * @param identityCard
	 * @param driverLicense
	 * @param fileNumber
	 * @param issuingLicenceAuthority
	 * @param photoReturnNumberString
	 * @param receiverName
	 * @param receiverNumber
	 * @param receiverAddress
	 * @param sourceOfCertification
	 * @param loginUser
	 * @param PHOTO9
	 * @param PHOTO10
	 * @param JSZZP
	 * @param STTJSQB
	 * @param request
	 * @param response
	 */
	@RequestMapping("driverLicenseInto")
	public void driverLicenseInto(String userName, String identificationNO, String identityCard, String driverLicense,
			String fileNumber, String issuingLicenceAuthority, String photoReturnNumberString, String receiverName,
			String receiverNumber, String receiverAddress, String sourceOfCertification, String loginUser,
			String PHOTO9, String PHOTO10, String JSZZP, String STTJSQB, String openId ,HttpServletRequest request,
			HttpServletResponse response) {
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("驾驶证转入参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		
		BaseBean baseBean = new BaseBean(); // 创建返回结果
		DriverLicenseIntoVo driverLicenseIntoVo = new DriverLicenseIntoVo();
		String businessType = "Z";
		driverLicenseIntoVo.setBusinessType(businessType);
		try {
			if (StringUtil.isBlank(userName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setName(userName);
			}
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setIdentificationNO(identificationNO);
			}
			if (StringUtil.isBlank(identityCard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setIDcard(identityCard);
			}
			if (StringUtil.isBlank(driverLicense)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setDriverLicense(driverLicense);
			}
			if (StringUtil.isBlank(fileNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("档案编号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setFileNumber(fileNumber);
			}
			if (StringUtil.isBlank(issuingLicenceAuthority)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("发证机关不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setIssuingLicenceAuthority(issuingLicenceAuthority);
			}
			if (StringUtil.isBlank(photoReturnNumberString)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if (StringUtil.isBlank(receiverName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setReceiverName(receiverName);
			}
			if (StringUtil.isBlank(receiverNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setReceiverNumber(receiverNumber);
			}
			if (StringUtil.isBlank(receiverAddress)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setMailingAddress(receiverAddress);
			}
			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setSourceOfCertification(sourceOfCertification);
			}
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("登录用户不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setLoginUser(loginUser);
			}
			if (StringUtil.isBlank(PHOTO9)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setIDCardPhoto1(PHOTO9);
			}
			if (StringUtil.isBlank(PHOTO10)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setIDCardPhoto2(PHOTO10);
			}
			if (StringUtil.isBlank(JSZZP)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setDriverLicensePhoto(JSZZP);
			}
			if (StringUtil.isBlank(STTJSQB)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身体条件申请表不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseIntoVo.setBodyConditionForm(STTJSQB);
			}
			if (StringUtil.isBlank(openId)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("openId 不能为空!");
				renderJSON(baseBean);
				return;
			} 
			String ip = getIp2(request);
			driverLicenseIntoVo.setIp(ip);
			Map<String, String> map = handleService.driverLicenseInto(driverLicenseIntoVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String waterNumber = map.get("waterNumber");
				baseBean.setCode("0000");
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverLicenseInto, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("驾驶证转入申请", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverLicenseInto, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "驾驶证转入");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);

						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("驾驶证转入");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				if ("9999".equals(code)) {
					baseBean.setMsg("输入信息格式有误！");
				} else {
					baseBean.setMsg(msg);
				}
			}
		} catch (Exception e) {
			logger.error("驾驶证转入异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 驾驶证自愿降级
	 * 
	 * @param identificationNO
	 * @param loginUser
	 * @param identityCard
	 * @param driverLicense
	 * @param userName
	 * @param photoReturnNumberString
	 * @param placeOfDomicile
	 * @param receiverName
	 * @param receiverNumber
	 * @param receiverAddress
	 * @param sourceOfCertification
	 * @param userSource
	 * @param PHOTO9
	 * @param PHOTO10
	 * @param JSZZP
	 * @param request
	 * @param response
	 */
	@RequestMapping("driverLicenseVoluntaryDemotion")
	public void driverLicenseVoluntaryDemotion(String identificationNO, String loginUser, String identityCard,
			String driverLicense, String userName, String photoReturnNumberString, String placeOfDomicile,
			String receiverName, String receiverNumber, String receiverAddress, String sourceOfCertification,
			String userSource, String PHOTO9, String PHOTO10, String JSZZP, String openId ,HttpServletRequest request,
			HttpServletResponse response) {
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("驾驶证自愿降级参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		BaseBean baseBean = new BaseBean(); // 创建返回结果
		DriverLicenseVoluntaryDemotionVo driverLicenseVoluntaryDemotionVo = new DriverLicenseVoluntaryDemotionVo();
		String businessType = "J";
		driverLicenseVoluntaryDemotionVo.setBusinessType(businessType);
		try {
			if (StringUtil.isBlank(userName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setName(userName);
			}
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setIdentificationNO(identificationNO);
			}
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setLoginUser(loginUser);
			}
			if (StringUtil.isBlank(identityCard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setIDcard(identityCard);
			}
			if (StringUtil.isBlank(driverLicense)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setDriverLicense(driverLicense);
			}
			if (StringUtil.isBlank(userSource)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setUserSource(userSource);
			}
			if (StringUtil.isBlank(photoReturnNumberString)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if (StringUtil.isBlank(receiverName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setReceiverName(receiverName);
			}
			if (StringUtil.isBlank(receiverNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setReceiverNumber(receiverNumber);
			}
			if (StringUtil.isBlank(receiverAddress)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setMailingAddress(receiverAddress);
			}
			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setSourceOfCertification(sourceOfCertification);
			}
			if (StringUtil.isBlank(PHOTO9)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setIDCardPhoto1(PHOTO9);
			}
			if (StringUtil.isBlank(PHOTO10)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setIDCardPhoto2(PHOTO10);
			}
			if (StringUtil.isBlank(JSZZP)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setDriverLicensePhoto(JSZZP);
			}
			if (StringUtil.isBlank(placeOfDomicile)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverLicenseVoluntaryDemotionVo.setPlaceOfDomicile(placeOfDomicile);
			}
			if (StringUtil.isBlank(openId)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("openId 不能为空!");
				renderJSON(baseBean);
				return;
			} 
			String ip = getIp2(request);
			driverLicenseVoluntaryDemotionVo.setIp(ip);
			Map<String, String> map = handleService.driverLicenseVoluntaryDemotion(driverLicenseVoluntaryDemotionVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String waterNumber = map.get("waterNumber");
				baseBean.setCode("0000");
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverLicenseVoluntaryDemotion, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("驾驶证自愿降级申请", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverLicenseVoluntaryDemotion, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "驾驶证自愿降级");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);

						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("驾驶证自愿降级");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				else{
					baseBean.setData(waterNumber);
				}
				baseBean.setMsg(msg);
			} else {
				baseBean.setCode(MsgCode.businessError);
				if ("9999".equals(code)) {
					baseBean.setMsg("输入信息格式有误！");
				} else {
					baseBean.setMsg(msg);
				}
			}
		} catch (Exception e) {
			logger.error("驾驶证自愿降级异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 驾驶证补证
	 * 
	 * @param repairReason
	 * @param identificationNO
	 * @param identityCard
	 * @param userName
	 * @param mobilephone
	 * @param PHOTO9
	 * @param PHOTO10
	 * @param photoReturnNumberString
	 * @param PHOTO31
	 * @param placeOfDomicile
	 * @param postCode
	 * @param receiverName
	 * @param receiverNumber
	 * @param receiverAddress
	 * @param JZZA
	 * @param JZZB
	 * @param loginUser
	 * @param sourceOfCertification
	 * @param userSource
	 * @param request
	 * @param response
	 */
	@RequestMapping("repairDriverLicense")
	public void repairDriverLicense(String repairReason, String identificationNO, String identityCard, String userName,
			String mobilephone, String PHOTO9, String PHOTO10, String photoReturnNumberString, String PHOTO31,
			String placeOfDomicile, String postCode, String receiverName, String receiverNumber, String receiverAddress,
			String JZZA, String JZZB, String loginUser, String sourceOfCertification, String userSource,String openId ,
			HttpServletRequest request, HttpServletResponse response) {
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("驾驶证补证参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		BaseBean baseBean = new BaseBean(); // 创建返回结果
		RepairOrReplaceDriverLicenseVo repairOrReplaceDriverLicenseVo = new RepairOrReplaceDriverLicenseVo();
		try {
			String businessType = "B";
			repairOrReplaceDriverLicenseVo.setBusinessType(businessType);
			if (StringUtil.isBlank(userName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setName(userName);
			}
			if (StringUtil.isBlank(mobilephone)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setMobilephone(mobilephone);
			}
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIdentificationNO(identificationNO);
			}
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setLoginUser(loginUser);
			}
			if (StringUtil.isBlank(identityCard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIDcard(identityCard);
			}
			if (StringUtil.isBlank(userSource)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setUserSource(userSource);
			}
			if (StringUtil.isBlank(photoReturnNumberString)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if (StringUtil.isBlank(receiverName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setReceiverName(receiverName);
			}
			if (StringUtil.isBlank(receiverNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setReceiverNumber(receiverNumber);
			}
			if (StringUtil.isBlank(receiverAddress)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setMailingAddress(receiverAddress);
			}
			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setSourceOfCertification(sourceOfCertification);
			}
			if (StringUtil.isBlank(PHOTO9)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIDCardPhoto1(PHOTO9);
			}
			if (StringUtil.isBlank(PHOTO10)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIDCardPhoto2(PHOTO10);
			}
			if (StringUtil.isBlank(JZZA)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setLivePhoto1(JZZA);
			}
			if (StringUtil.isBlank(JZZB)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setLivePhoto2(JZZB);
			}
			if (StringUtil.isBlank(placeOfDomicile)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				if ("3".equals(placeOfDomicile)) {
					if (StringUtil.isBlank(PHOTO31)) {
						baseBean.setCode(MsgCode.paramsError);
						baseBean.setMsg("境外人员临住表不能为空!");
						renderJSON(baseBean);
						return;
					} else {
						repairOrReplaceDriverLicenseVo.setForeignersLiveTable(PHOTO31);
					}
				}
				repairOrReplaceDriverLicenseVo.setPlaceOfDomicile(placeOfDomicile);
			}
			repairOrReplaceDriverLicenseVo.setRepairReason(repairReason);
			repairOrReplaceDriverLicenseVo.setPostalcode(postCode);
			if (!"G".equals(sourceOfCertification)) {
				if (StringUtil.isBlank(openId)) {
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("openId 不能为空!");
					renderJSON(baseBean);
					return;
				} 
			}
			String ip = getIp2(request);
			repairOrReplaceDriverLicenseVo.setIp(ip);
			Map<String, String> map = handleService.repairDriverLicense(repairOrReplaceDriverLicenseVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String waterNumber = map.get("waterNumber");
				baseBean.setCode("0000");
//				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						/*String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.repairDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("驾驶证补证申请", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.repairDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbn3H9wpHz8dKjXPL9J_xC5s");
						model.setResult_page_style_id("23ClyLHM5Fr790uz7t-fxiodPnL9ohRzcnlGWEudkL8");
						model.setDeal_msg_style_id("23ClyLHM5Fr790uz7t-fxlzJePTelFGvOKtKR4udm1o");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> tmap = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						tmap.put("first", new MessageChannelModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
						tmap.put("keyword1",
								new MessageChannelModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new MessageChannelModel().new Property("驾驶证补证", "#212121"));
						tmap.put("keyword3", new MessageChannelModel().new Property("待初审", "#212121"));
						tmap.put("remark", new MessageChannelModel().new Property("","#212121"));
						model.setData(tmap);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
				
				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.repairDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "机动车驾驶证补办");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);

						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("机动车驾驶证补办");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				
				else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg);
			}
		} catch (Exception e) {
			logger.error("驾驶证补证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 驾驶证换证
	 * 
	 * @param identificationNO
	 * @param identityCard
	 * @param userName
	 * @param mobilephone
	 * @param PHOTO9
	 * @param PHOTO10
	 * @param photoReturnNumberString
	 * @param PHOTO31
	 * @param placeOfDomicile
	 * @param receiverName
	 * @param receiverNumber
	 * @param receiverAddress
	 * @param JZZA
	 * @param JZZB
	 * @param loginUser
	 * @param sourceOfCertification
	 * @param userSource
	 * @param request
	 * @param response
	 */
	@RequestMapping("replaceDriverLicense")
	public void replaceDriverLicense(String identificationNO, String identityCard, String userName, String mobilephone,
			String PHOTO9, String PHOTO10, String photoReturnNumberString, String PHOTO31, String placeOfDomicile,
			String receiverName, String receiverNumber, String receiverAddress, String JZZA, String JZZB,
			String loginUser, String sourceOfCertification, String userSource, String openId ,HttpServletRequest request,
			HttpServletResponse response) {
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("驾驶证换证参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		
		BaseBean baseBean = new BaseBean(); // 创建返回结果
		RepairOrReplaceDriverLicenseVo repairOrReplaceDriverLicenseVo = new RepairOrReplaceDriverLicenseVo();
		try {
			String businessType = "H";
			repairOrReplaceDriverLicenseVo.setBusinessType(businessType);
			if (StringUtil.isBlank(userName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setName(userName);
			}
			if (StringUtil.isBlank(mobilephone)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setMobilephone(mobilephone);
			}
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIdentificationNO(identificationNO);
			}
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setLoginUser(loginUser);
			}
			if (StringUtil.isBlank(identityCard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIDcard(identityCard);
			}
			if (StringUtil.isBlank(userSource)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setUserSource(userSource);
			}
			if (StringUtil.isBlank(photoReturnNumberString)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if (StringUtil.isBlank(receiverName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setReceiverName(receiverName);
			}
			if (StringUtil.isBlank(receiverNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setReceiverNumber(receiverNumber);
			}
			if (StringUtil.isBlank(receiverAddress)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setMailingAddress(receiverAddress);
			}
			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setSourceOfCertification(sourceOfCertification);
			}
			if (StringUtil.isBlank(PHOTO9)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIDCardPhoto1(PHOTO9);
			}
			if (StringUtil.isBlank(PHOTO10)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setIDCardPhoto2(PHOTO10);
			}
			if (StringUtil.isBlank(JZZA)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setLivePhoto1(JZZA);
			}
			if (StringUtil.isBlank(JZZB)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				repairOrReplaceDriverLicenseVo.setLivePhoto2(JZZB);
			}
			if (StringUtil.isBlank(placeOfDomicile)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				if ("3".equals(placeOfDomicile)) {
					if (StringUtil.isBlank(PHOTO31)) {
						baseBean.setCode(MsgCode.paramsError);
						baseBean.setMsg("境外人员临住表不能为空!");
						renderJSON(baseBean);
						return;
					} else {
						repairOrReplaceDriverLicenseVo.setForeignersLiveTable(PHOTO31);
					}
				}
				repairOrReplaceDriverLicenseVo.setPlaceOfDomicile(placeOfDomicile);
			}
			/*if (StringUtil.isBlank(openId)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("openId 不能为空!");
				renderJSON(baseBean);
				return;
			}*/ 
			String ip = getIp2(request);
			repairOrReplaceDriverLicenseVo.setIp(ip);
			Map<String, String> map = handleService.replaceDriverLicense(repairOrReplaceDriverLicenseVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String waterNumber = map.get("waterNumber");
				baseBean.setCode("0000");
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
/*						String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.replaceDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("驾驶证换证申请", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.replaceDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbn3H9wpHz8dKjXPL9J_xC5s");
						model.setResult_page_style_id("23ClyLHM5Fr790uz7t-fxiodPnL9ohRzcnlGWEudkL8");
						model.setDeal_msg_style_id("23ClyLHM5Fr790uz7t-fxlzJePTelFGvOKtKR4udm1o");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> tmap = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						tmap.put("first", new MessageChannelModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
						tmap.put("keyword1",
								new MessageChannelModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new MessageChannelModel().new Property("驾驶证换证", "#212121"));
						tmap.put("keyword3", new MessageChannelModel().new Property("待初审", "#212121"));
						tmap.put("remark", new MessageChannelModel().new Property("","#212121"));
						model.setData(tmap);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
				
				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.replaceDriverLicense, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "机动车驾驶证换证");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);
						
						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("机动车驾驶证换证");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				
				else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(code);
				baseBean.setMsg(msg);
			}
		} catch (Exception e) {
			logger.error("驾驶证换证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 驾驶人联系方式变更
	 * 
	 * @param userName
	 * @param gender
	 * @param identificationNO
	 * @param identificationNum
	 * @param driverLicense
	 * @param receiverAddress
	 * @param mobilephone
	 * @param loginUser
	 * @param userSource
	 * @param PHOTO9
	 * @param PHOTO10
	 * @param JSZZP
	 * @param request
	 * @param response
	 */
	@RequestMapping("driverChangeContact")
	public void driverChangeContact(String userName, String gender, String identificationNO, String identificationNum,
			String driverLicense, String receiverAddress, String mobilephone, String loginUser, String userSource,
			String PHOTO9, String PHOTO10, String JSZZP, String openId ,HttpServletRequest request, HttpServletResponse response) {
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("驾驶人联系方式变更参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		BaseBean baseBean = new BaseBean(); // 创建返回结果
		DriverChangeContactVo driverChangeContactVo = new DriverChangeContactVo();
		String businessType = "L";
		driverChangeContactVo.setBusinessType(businessType);
		try {
			if (StringUtil.isBlank(userName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setName(userName);
			}
			if (StringUtil.isBlank(gender)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("性别不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setGender(gender);
			}
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setIdentificationNO(identificationNO);
			}
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setLoginUser(loginUser);
			}
			if (StringUtil.isBlank(identificationNum)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setIDcard(identificationNum);
			}
			if (StringUtil.isBlank(driverLicense)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setDriverLicense(driverLicense);
			}
			if (StringUtil.isBlank(mobilephone)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setMobilephone(mobilephone);
			}
			if (StringUtil.isBlank(userSource)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setUserSource(userSource);
			}
			if (StringUtil.isBlank(receiverAddress)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("联系地址地址不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setMailingAddress(receiverAddress);
			}
			if (StringUtil.isBlank(PHOTO9)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setIDCardPhoto1(PHOTO9);
			}
			if (StringUtil.isBlank(PHOTO10)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setIDCardPhoto2(PHOTO10);
			}
			if (StringUtil.isBlank(JSZZP)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				driverChangeContactVo.setDriverLicensePhoto(JSZZP);
			}
			if (StringUtil.isBlank(openId)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("openId 不能为空!");
				renderJSON(baseBean);
				return;
			} 
			String ip = getIp2(request);
			driverChangeContactVo.setIp(ip);
			Map<String, String> map = handleService.driverChangeContact(driverChangeContactVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String waterNumber = map.get("waterNumber");
				baseBean.setCode("0000");
				baseBean.setMsg(msg);
				if (userSource.equals("C")) {
					try {
						String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverChangeContact, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("驾驶人联系方式变更申请", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
				//民生警务来源，模板推送
				else if("M".equals(userSource) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.driverChangeContact, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "驾驶人联系方式变更");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);

						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("驾驶人联系方式变更");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				if ("9999".equals(code)) {
					baseBean.setMsg("输入信息格式有误！");
				} else {
					baseBean.setMsg(msg);
				}
			}
		} catch (Exception e) {
			logger.error("驾驶人联系方式变更异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
    /**
     * @Title: applyGatePass
     * @Description: TODO(申请通行证-外地车)
     * @param @param request
     * @param @param response    参数
     * @return void    返回类型
     * @throws
     */
    @RequestMapping(value="/applyGatePass.html")
	public void applyGatePass(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	ApplyGatePassVo vo = new ApplyGatePassVo();
    	/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("applyGatePass参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		try {
			String plateType = request.getParameter("plateType");   //号牌种类
			String cartype = request.getParameter("cartype");  //车辆类型
			String abbreviation = request.getParameter("abbreviation");  //车牌简称
			String numberPlate = request.getParameter("numberPlate");  //车牌号码
			String vin = request.getParameter("behindTheFrame4Digits");  //车架号
			String userName = request.getParameter("name");  //车辆所有人
			String mobilephone = request.getParameter("mobilephone");  //手机号码
			String applyDate = request.getParameter("applyDate");  //申请日期
			String remarks = request.getParameter("remarks");  //备注
			String openId = request.getParameter("openId");  //openId
			//验证号牌种类
			if(StringUtil.isBlank(plateType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车辆类型
			if(StringUtil.isBlank(cartype)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车牌号码
			if(StringUtil.isBlank(abbreviation) || StringUtil.isBlank(numberPlate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车架号
			if(StringUtil.isBlank(vin)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车辆所有人
			if(StringUtil.isBlank(userName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆所有人不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证手机
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证申请日期
			if(StringUtil.isBlank(applyDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			vo.setAbbreviation(abbreviation); //车牌简称
			vo.setNumberPlate(numberPlate); //车牌号码
			vo.setPlateType(plateType); //号牌种类
			vo.setCartype(cartype);  //车辆类型
			vo.setVin(vin);  	//车架号后四位
			vo.setUserName(userName);	//车辆所有人
			vo.setMobilephone(mobilephone); //手机号码
			vo.setApplyDate(applyDate);
			vo.setRemarks(remarks);
			
			//接口调用
			Map<String, String> map = handleService.applyGatePass(vo);
			String code = map.get("code");
			String msg = map.get("msg");
			
			if("0000".equals(code)){
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.applyGatePass, plateType, abbreviation + numberPlate, mobilephone, applyDate);
        		baseBean.setData(handleTemplateVo);
        		//推送模板消息
				try {
					String templateId = "OHe4a5_6nqj3VuN3QKmKYKPiEk54Y_w3oYQRUn0I34o";
					String url = HandleTemplateVo.getUrl(handleTemplateVo, handleService.getTemplateSendUrl())+"&noTip=true";
					logger.info("返回的url是：" + url);
					logger.info("handleTemplateVo 是：" + handleTemplateVo);
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map1 = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					map1.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
					map1.put("keynote1", new TemplateDataModel().new Property("每月1天通行证申请","#212121"));
					map1.put("keynote2", new TemplateDataModel().new Property(abbreviation+numberPlate,"#212121"));
					map1.put("keynote3", new TemplateDataModel().new Property(applyDate,"#212121"));
					map1.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map1);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		if ("9999".equals(code)) {
        			baseBean.setMsg("输入信息格式有误！");
				}else{
					baseBean.setMsg(msg);
				}
        	}
		} catch (Exception e) {
			logger.error("申请通行证-外地车Action异常:"+e);
			DealException(baseBean, e);
			
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
    
    /**
     * @Title: applyCarTemporaryLicence
     * @Description: TODO(申请机动车临牌)
     * @param @param request
     * @param @param response    参数
     * @return void    返回类型
     * @throws
     */
    @RequestMapping(value="/applyCarTemporaryLicence.html")
	public void applyCarTemporaryLicence(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	ApplyCarTemporaryLicenceVo vo = new ApplyCarTemporaryLicenceVo();
    	/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("applyCarTemporaryLicence参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		try {
			String userName = request.getParameter("userName");   //姓名
			String identityCard = request.getParameter("identityCard");  //身份证号
			String mobilephone = request.getParameter("mobilephone");  //手机号码
			String cartModels = request.getParameter("cartModels");  //车辆型号
			String cartype = request.getParameter("cartype");  //车辆类型
			String engineNumber = request.getParameter("engineNumber");  //发动机号
			String vin = request.getParameter("behindTheFrame4Digits");  //车架号
			String carOrigin = request.getParameter("carOrigin");  //车辆产地 进口/国产
			String placeOfDomicile = request.getParameter("placeOfDomicile");  //户籍所在地
			String receiverName = request.getParameter("receiverName");  //收件人姓名
			String receiverNumber = request.getParameter("receiverNumber");  //收件人号码
			String receiverAddress = request.getParameter("receiverAddress");  //收件人地址
			String PHOTO26 = request.getParameter("PHOTO26");  //购置发票图
			String PHOTO27 = request.getParameter("PHOTO27");  //交强险单据
			String PHOTO9 = request.getParameter("PHOTO9");  //身份证正面
			String PHOTO10 = request.getParameter("PHOTO10");  //身份证反面
			String PHOTO28 = request.getParameter("PHOTO28");  //机动车合格证
			String PHOTO31 = request.getParameter("PHOTO31");  //境外人员临住表
			String PHOTO29 = request.getParameter("PHOTO29");  //进口货物证明书
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			String openId = request.getParameter("openId");  //openId
			
			//验证姓名
			if(StringUtil.isBlank(userName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证identityCard
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证mobilephone
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证cartModels
			if(StringUtil.isBlank(cartModels)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆型号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证cartype
			if(StringUtil.isBlank(cartype)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证engineNumber
			if(StringUtil.isBlank(engineNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("发动机号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证vin
			if(StringUtil.isBlank(vin)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证carOrigin
			if(StringUtil.isBlank(carOrigin)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆产地不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证placeOfDomicile
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证receiverName
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证receiverNumber
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证receiverAddress
			if(StringUtil.isBlank(receiverAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			vo.setUserName(userName);
			vo.setIdentityCard(identityCard);
			vo.setCartModels(cartModels);
			vo.setCartype(cartype);
			vo.setEngineNumber(engineNumber);
			vo.setVin(vin);
			vo.setCarOrigin(carOrigin);
			vo.setReceiverName(receiverName);
			vo.setReceiverAddress(receiverAddress);
			vo.setReceiverNumber(receiverNumber);
			vo.setPHOTO9(PHOTO9);
			vo.setPHOTO10(PHOTO10);
			vo.setPHOTO26(PHOTO26);
			vo.setPHOTO27(PHOTO27);
			vo.setPHOTO28(PHOTO28);
			vo.setPHOTO29(PHOTO29);
			vo.setPHOTO31(PHOTO31);
			vo.setIp(getIp2(request));
			vo.setSourceOfCertification(sourceOfCertification);
			vo.setPlaceOfDomicile(placeOfDomicile);
			
			//接口调用
			Map<String, String> map = handleService.applyCarTemporaryLicence(vo);
			String code = map.get("code");
			String msg = map.get("msg");
			
			if("0000".equals(code)){
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		
        		//openId不为空,发送微信消息推送
        		if("C".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
        			HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.applyCarTemporaryLicence, map.get("number"), DateUtil2.date2str(new Date()));
        			baseBean.setData(handleTemplateVo);
        			//推送模板消息
        			try {
        				String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A_sqjdclpbl";
        				//String url = HandleTemplateVo.getUrl(handleTemplateVo, handleService.getTemplateSendUrl());
        				String url = HandleTemplateVo.getUrl(handleTemplateVo, handleService.getTemplateSendUrl())+"&noTip=true";
        				logger.info("返回的url是：" + url);
        				logger.info("handleTemplateVo 是：" + handleTemplateVo);
        				Map<String, cn.message.model.wechat.TemplateDataModel.Property> map1 = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
        				map1.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
        				map1.put("keyword1", new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()),"#212121"));
        				map1.put("keyword2", new TemplateDataModel().new Property("申请机动车临牌","#212121"));
        				map1.put("keyword3", new TemplateDataModel().new Property("待受理","#212121"));
        				map1.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
        				boolean flag = templateMessageService.sendMessage(openId, templateId, url, map1);
        				logger.info("发送模板消息结果：" + flag);
        			} catch (Exception e) {
        				logger.error("发送模板消息  失败===", e);
        			}
        		}
        		
        		//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.applyCarTemporaryLicence, map.get("number"), DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "办理临时行驶车号牌");templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);

						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(map.get("number"));
							businessVo.setOpenid(openId);
							businessVo.setEventname("办理临时行驶车号牌");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
        		
        	}else{
        		baseBean.setCode(code);
				baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			logger.error("申请机动车临牌Action异常:"+e);
			DealException(baseBean, e);
			
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
	/**
	 * 机动车联系方式变更
	 * @author lifangyong
	 * @param request
	 * @param response
	 * @param 
	 */
	@RequestMapping("iocomotiveCarChangeContact")
	public void iocomotiveCarChangeContact(HttpServletRequest request,HttpServletResponse response){
		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("机动车联系方式变更参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		
		BaseBean baseBean = new BaseBean();		//创建返回结果
		IocomotiveCarChangeContactVo iocomotiveCarChangeContactVo = new IocomotiveCarChangeContactVo();
		try {
			String name = request.getParameter("name"); //车主姓名
			String identificationNO = request.getParameter("identificationNO"); //证件种类
			String identificationNum = request.getParameter("identificationNum"); //证件号码
			String numberPlate = request.getParameter("numberPlate"); //号牌号码
			String cartype = request.getParameter("cartype"); //车辆种类
			String placeOfDomicile = request.getParameter("placeOfDomicile"); //户籍所在地
			String behindTheFrame4Digits = request.getParameter("behindTheFrame4Digits"); //车架号
			String mobilephone = request.getParameter("mobilephone"); //变更号码
			String receiverAddress = request.getParameter("receiverAddress"); //地址
			String PHOTO9 = request.getParameter("PHOTO9"); //身份证（正面）
			String PHOTO10 = request.getParameter("PHOTO10");//身份证（反面）
			String JDCXSZ = request.getParameter("JDCXSZ"); //机动车行驶证照片
			String sourceOfCertification = request.getParameter("sourceOfCertification");//申请来源
			String openId = request.getParameter("openId");  //openId
			
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车主姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(identificationNum)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(numberPlate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(cartype)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(behindTheFrame4Digits)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(receiverAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(PHOTO9)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（正面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(PHOTO10)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（反面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(JDCXSZ)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("机动车行驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			String ip = getIp2(request);
			iocomotiveCarChangeContactVo.setName(name);
			iocomotiveCarChangeContactVo.setIdentificationNO(identificationNO);
			iocomotiveCarChangeContactVo.setIdentificationNum(identificationNum);
			iocomotiveCarChangeContactVo.setNumberPlate(numberPlate);
			iocomotiveCarChangeContactVo.setCartype(cartype);
			iocomotiveCarChangeContactVo.setPlaceOfDomicile(placeOfDomicile);
			iocomotiveCarChangeContactVo.setBehindTheFrame4Digits(behindTheFrame4Digits);
			iocomotiveCarChangeContactVo.setMobilephone(mobilephone);
			iocomotiveCarChangeContactVo.setReceiverAddress(receiverAddress);
			iocomotiveCarChangeContactVo.setPHOTO9(PHOTO9);
			iocomotiveCarChangeContactVo.setPHOTO10(PHOTO10);
			iocomotiveCarChangeContactVo.setJDCXSZ(JDCXSZ);
			iocomotiveCarChangeContactVo.setSourceOfCertification(sourceOfCertification);
			iocomotiveCarChangeContactVo.setIp(ip);
			
			Map<String, String> map = handleService.iocomotiveCarChangeContact(iocomotiveCarChangeContactVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if("0000".equals(code)){
				String waterNumber = "";
				waterNumber = SXStringUtils.deleteChineseCharactertoString(msg);
				waterNumber = waterNumber.replace("，", "");
				waterNumber = waterNumber.replace("：", "");
				waterNumber = waterNumber.replace("。", "");
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		if("M".equals(sourceOfCertification)){
    				try {
    					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.iocomotiveCarChangeContact, waterNumber, DateUtil2.date2str(new Date()));
    					baseBean.setData(handleTemplateVo);
    					String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
    					logger.info("【民生警务】结果页url：" + url);
    					JSONObject templateData = new JSONObject();
    					templateData.put("openid", openId);
    					templateData.put("templateId", handleService.getMsjwHandleTemplateId());
    					templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
    					templateData.put("keyword1Data", "机动车联系方式变更");	templateData.put("keyword1Color", "#212121");
    					templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
    					templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
    					templateData.put("remarkData", "更多信息请点击详情查看");
    					templateData.put("redirectUrl", url);
    					String params = templateData.toJSONString();
    					JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
    					logger.info("【民生警务】发送模板消息结果：" + json);

    					//新增到民生警务平台个人中心
    					try {
    						MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
    						businessVo.setTylsbh(waterNumber);
    						businessVo.setOpenid(openId);
    						businessVo.setEventname("机动车联系方式变更");
    						businessVo.setApplyingUrlWx(url);//微信在办跳转地址
    						businessVo.setJinduUrlWx(url);//进度查询跳转地址
    						msjwService.addApplyingBusiness(businessVo);
    					} catch (Exception e) {
    						logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
    						e.printStackTrace();
    					}
    					
    				} catch (Exception e) {
    					logger.error("【民生警务】发送模板消息  失败===", e);
    				}
    			}
        		
        		//推送模板消息
				try {
					String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.iocomotiveCarChangeContact, waterNumber, DateUtil2.date2str(new Date()));
					baseBean.setData(handleTemplateVo);
					String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
					logger.info("返回的url是：" + url);
					logger.info("handleTemplateVo 是：" + handleTemplateVo);
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map1 = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					map1.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
					map1.put("keyword1", new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()),"#212121"));
					map1.put("keyword2", new TemplateDataModel().new Property("机动车联系方式变更","#212121"));
					map1.put("keyword3", new TemplateDataModel().new Property("待受理","#212121"));
					map1.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map1);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		if ("9999".equals(code)) {
        			baseBean.setMsg("输入信息格式有误！");
				}else{
					baseBean.setMsg(msg);
				}
        	}
		} catch (Exception e) {
			logger.error("机动车联系方式变更异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
    
    /**
     * 换领机动车行驶证
     * @author lifangyong
     * @param request
     * @param response
     */
    @RequestMapping("iocomotiveCarReplace")
    public void iocomotiveCarReplace(HttpServletRequest request,HttpServletResponse response){
    	/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("换领机动车行驶证参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	IocomotiveCarReplaceVo iocomotiveCarReplaceVo = new IocomotiveCarReplaceVo();
    	try {
	    	String name = request.getParameter("name");					//	车主姓名
			String identificationNum = request.getParameter("identificationNum");					//	证件号码
			String numberPlate = request.getParameter("numberPlate");			//	号牌号码
			String plateType = request.getParameter("plateType");		//	号牌种类
			String placeOfDomicile = request.getParameter("placeOfDomicile");			//	户籍所在地
			String address = request.getParameter("address");					//	住所详细地址
			String receiverName = request.getParameter("receiverName");			//	收件人姓名
			String receiverNumber = request.getParameter("receiverNumber");			//	收件人号码
			String receiverAddress = request.getParameter("receiverAddress");			//	收件人地址
			String JZZA = request.getParameter("JZZA");				//	居住证正面
			String JZZB = request.getParameter("JZZB");				//	居住证反面
			String PHOTO9 = request.getParameter("PHOTO9");			//	身份证（正面）
			String PHOTO10 = request.getParameter("PHOTO10");			//	身份证（反面）
			String DJZSFYJ = request.getParameter("DJZSFYJ");		//	机动车登记证书
			String sourceOfCertification = request.getParameter("sourceOfCertification");	//	申请来源
			String openId = request.getParameter("openId");  //openId
			
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车主姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(identificationNum)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(numberPlate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(plateType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(receiverAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			/*if(StringUtil.isBlank(JZZA)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证（正面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(JZZB)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证（反面）不能为空!");
				renderJSON(baseBean);
				return;
			}*/
			if(StringUtil.isBlank(PHOTO9)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（正面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(PHOTO10)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（反面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(DJZSFYJ)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("机动车登记证书不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			String ip = getIp2(request);
			iocomotiveCarReplaceVo.setName(name);					//	车主姓名
			iocomotiveCarReplaceVo.setIdentificationNum(identificationNum);					//	证件号码
			iocomotiveCarReplaceVo.setNumberPlate(numberPlate);			//	号牌号码
			iocomotiveCarReplaceVo.setPlateType(plateType);		//	车辆种类
			iocomotiveCarReplaceVo.setPlaceOfDomicile(placeOfDomicile);			//	户籍所在地
			iocomotiveCarReplaceVo.setAddress(address);					//	住所详细地址
			iocomotiveCarReplaceVo.setReceiverName(receiverName);			//	收件人姓名
			iocomotiveCarReplaceVo.setReceiverNumber(receiverNumber);			//	收件人号码
			iocomotiveCarReplaceVo.setReceiverAddress(receiverAddress);			//	收件人地址
			iocomotiveCarReplaceVo.setJZZA(JZZA);				//	居住证正面
			iocomotiveCarReplaceVo.setJZZB(JZZB);				//	居住证反面
			iocomotiveCarReplaceVo.setPHOTO9(PHOTO9);			//	身份证（正面）
			iocomotiveCarReplaceVo.setPHOTO10(PHOTO10);			//	身份证（反面）
			iocomotiveCarReplaceVo.setDJZSFYJ(DJZSFYJ);		//	机动车登记证书
			iocomotiveCarReplaceVo.setSourceOfCertification(sourceOfCertification); //  申请来源
			iocomotiveCarReplaceVo.setIp(ip);//	ip
			Map<String, String> map = handleService.iocomotiveCarReplace(iocomotiveCarReplaceVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if("0000".equals(code)){
				String waterNumber = "";
				waterNumber = SXStringUtils.deleteChineseCharactertoString(msg);
				waterNumber = waterNumber.replace("，", "");
				waterNumber = waterNumber.replace("：", "");
				waterNumber = waterNumber.replace("。", "");
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		//民生警务来源，模板推送
				if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.iocomotiveCarReplace, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "换领行驶证");	templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);

						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(waterNumber);
							businessVo.setOpenid(openId);
							businessVo.setEventname("换领行驶证");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
        		try {
					String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A_xszbhlbl";
					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.iocomotiveCarReplace, waterNumber, DateUtil2.date2str(new Date()));
					baseBean.setData(handleTemplateVo);
					String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
					logger.info("返回的url是：" + url);
					logger.info("handleTemplateVo 是：" + handleTemplateVo);
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map1 = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					map1.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
					map1.put("keyword1", new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()),"#212121"));
					map1.put("keyword2", new TemplateDataModel().new Property("换领机动车行驶证","#212121"));
					map1.put("keyword3", new TemplateDataModel().new Property("待受理","#212121"));
					map1.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map1);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		if ("9999".equals(code)) {
        			baseBean.setMsg("输入信息格式有误！");
				}else{
					baseBean.setMsg(msg);
				}
        	}
		} catch (Exception e) {
			logger.error("换领机动车行驶证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
    * @Title: replaceInspectionMark
    * @Description: TODO(补换检验合格标志)
    * @param @param request
    * @param @param response    参数
    * @return void    返回类型
    * @throws
    */
   @RequestMapping(value="/replaceInspectionMark.html")
	public void replaceInspectionMark(HttpServletRequest request,HttpServletResponse response){
   		BaseBean baseBean = new BaseBean();		//创建返回结果
   		ApplyInspectionMarkVo vo = new ApplyInspectionMarkVo();
   		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("replaceInspectionMark参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		try {
			String userName = request.getParameter("userName");   //姓名
			String identityCard = request.getParameter("identityCard");  //身份证号
			String mobilephone = request.getParameter("mobilephone");  //手机号码
			
			String placeOfDomicile = request.getParameter("placeOfDomicile");  //户籍所在地
			String recipientName = request.getParameter("receiverName");  //收件人姓名
			String recipientPhone = request.getParameter("receiverNumber");  //收件人号码
			String recipientAddress = request.getParameter("receiverAddress");  //收件人地址
			
			String licensePlateNo = request.getParameter("numberPlate");  //车牌号
			String licensePlateType = request.getParameter("plateType");  //车牌类型
			
			String idCardImgPositive = request.getParameter("PHOTO9");  //身份证正面
			String idCardImgNegative = request.getParameter("PHOTO10");  //身份证反面
			String idCarImgTable = request.getParameter("DJZSFYJ");  //机动车登机证书
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			
			String foreignersLiveTable=request.getParameter("PHOTO31");
			String JZZA=request.getParameter("JZZA");
			String JZZB=request.getParameter("JZZB");
			String openId = request.getParameter("openId");  //openId
			
			//验证identityCard
			if(StringUtil.isBlank(userName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证identityCard
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证mobilephone
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证placeOfDomicile
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//此接口特殊转化，前台（1，深户，0外籍）   警司通（0，深户，1，外籍）
			if("0".equals(placeOfDomicile)){
				placeOfDomicile="1";				
			}else if("1".equals(placeOfDomicile)){
				placeOfDomicile="0";
			}
			
			//验证licensePlateNo
			if(StringUtil.isBlank(licensePlateNo)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证licensePlateType
			if(StringUtil.isBlank(licensePlateType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证recipientName
			if(StringUtil.isBlank(recipientName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证recipientAddress
			if(StringUtil.isBlank(recipientAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证recipientPhone
			if(StringUtil.isBlank(recipientPhone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人联系方式不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			
			
			
			//验证idCardImgPositive
			if(StringUtil.isBlank(idCardImgPositive)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面图片不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证idCardImgNegative
			if(StringUtil.isBlank(idCardImgNegative)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面图片不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证IdCardImgHandHeld
			if(StringUtil.isBlank(idCarImgTable)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("机动车登机证书不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证sourceOfCertification
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			vo.setUserName(userName);
			vo.setIdentityCard(identityCard);
			vo.setIdCarImgTable(idCarImgTable);
			vo.setIdCardImgNegative(idCardImgNegative);
			vo.setIdCardImgPositive(idCardImgPositive);
			vo.setLicensePlateNo(licensePlateNo);
			vo.setLicensePlateType(licensePlateType);
			vo.setMobilephone(mobilephone);
			vo.setPlaceOfDomicile(placeOfDomicile);
			vo.setRecipientAddress(recipientAddress);
			vo.setRecipientName(recipientName);
			vo.setRecipientPhone(recipientPhone);
			vo.setIp(getIp2(request));
			vo.setSourceOfCertification(sourceOfCertification);
			vo.setForeignersLiveTable(foreignersLiveTable);
			vo.setJZZA(JZZA);
			vo.setJZZB(JZZB);
			
			
			//接口调用
			Map<String, String> map = handleService.replaceInspectionMark(vo);
			String code = map.get("code");
			String msg = map.get("msg");
			String number=map.get("number");
			
			if("0000".equals(code)){
				baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		//成功需要发送模板消息
        		if("C".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
        			try {
        				String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
        				HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.replaceInspectionMark, number, DateUtil2.date2str(new Date()));
        				baseBean.setData(handleTemplateVo);
        				String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
        				logger.info("返回的url是：" + url);
        				logger.info("handleTemplateVo 是：" + handleTemplateVo);
        				Map<String, cn.message.model.wechat.TemplateDataModel.Property> map1 = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
        				map1.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
        				map1.put("keyword1", new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()),"#212121"));
        				map1.put("keyword2", new TemplateDataModel().new Property("检验合格标志","#212121"));
        				map1.put("keyword3", new TemplateDataModel().new Property("待受理","#212121"));
        				map1.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
        				boolean flag = templateMessageService.sendMessage(openId, templateId, url, map1);
        				logger.info("发送模板消息结果：" + flag);
        			} catch (Exception e) {
        				logger.error("发送模板消息  失败===", e);
        			}
        		}
				
				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.replaceInspectionMark, number, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "补换检验合格标志");templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);
						
						//新增到民生警务平台个人中心
						try {
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							businessVo.setTylsbh(number);
							businessVo.setOpenid(openId);
							businessVo.setEventname("补换检验合格标志");
							businessVo.setApplyingUrlWx(url);//微信在办跳转地址
							businessVo.setJinduUrlWx(url);//进度查询跳转地址
							msjwService.addApplyingBusiness(businessVo);
						} catch (Exception e) {
							logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
				
	       	}else{
	       		baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg);
	       	}
		} catch (Exception e) {
			logger.error("补换检验合格标志Action异常:"+e);
			DealException(baseBean, e);
			
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
   
   /**
    * @Title: inspectionDeclaration
    * @Description: TODO(机动车委托异地定期检验申报)
    * @param @param request
    * @param @param response    参数
    * @return void    返回类型
    * @throws
    */
   @RequestMapping(value="/inspectionDeclaration.html")
	public void inspectionDeclaration(HttpServletRequest request,HttpServletResponse response){
   		BaseBean baseBean = new BaseBean();		//创建返回结果
   		ApplyRemoteEntrustedBusinessVo vo = new ApplyRemoteEntrustedBusinessVo();
   		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("inspectionDeclaration参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
		try {
			String name = request.getParameter("name");   //车主姓名
			String identityCard = request.getParameter("identityCard");  //身份证号
			String carOwnerIdentityCard = request.getParameter("carOwnerIdentityCard");  //车主身份证号
			
			String recipientName = request.getParameter("receiverName");  //收件人姓名
			String recipientPhone = request.getParameter("receiverNumber");  //收件人号码
			String recipientAddress = request.getParameter("receiverAddress");  //收件人地址
			
			String licensePlateNo = request.getParameter("numberPlate");  //车牌号
			String licensePlateType = request.getParameter("cartype");  //车辆类型
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			
			String proprietorship = request.getParameter("proprietorship");   //车子所有权
			String vehicleIdentifyNoLast4 = request.getParameter("behindTheFrame4Digits");  //车架后4位
			
			String associatedAgency = request.getParameter("associatedAgency");  //委托机构
			String postalcode = request.getParameter("postCode");  //邮政编码
			String openId = request.getParameter("openId");  //openId
			
			
			//验证proprietorship
			if(StringUtil.isBlank(proprietorship)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车子所有权不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证identityCard
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车主姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证carOwnerIdentityCard
			if(StringUtil.isBlank(carOwnerIdentityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车主身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证星级用户identityCard
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("登录星级用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证vehicleIdentifyNoLast4
			if(StringUtil.isBlank(vehicleIdentifyNoLast4)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证associatedAgency
			if(StringUtil.isBlank(associatedAgency)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("委托机构不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证licensePlateNo
			if(StringUtil.isBlank(licensePlateNo)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证licensePlateType
			if(StringUtil.isBlank(licensePlateType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证recipientName
			if(StringUtil.isBlank(recipientName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证recipientAddress
			if(StringUtil.isBlank(recipientAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
	
			//验证recipientPhone
			if(StringUtil.isBlank(recipientPhone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证postalcode
			if(StringUtil.isBlank(postalcode)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("邮政编码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
	
						
			//验证sourceOfCertification
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			vo.setName(name);
			vo.setCarOwnerIdentityCard(carOwnerIdentityCard);
			vo.setIdentityCard(identityCard);
			vo.setAssociatedAgency(associatedAgency);
			vo.setLicensePlateNo(licensePlateNo);
			vo.setLicensePlateType(licensePlateType);
			vo.setPostalcode(postalcode);
			vo.setProprietorship(proprietorship);
			vo.setRecipientAddress(recipientAddress);
			vo.setRecipientName(recipientName);
			vo.setRecipientPhone(recipientPhone);
			vo.setVehicleIdentifyNoLast4(vehicleIdentifyNoLast4);
			vo.setSourceOfCertification(sourceOfCertification);
			
			//接口调用
			Map<String, String> map = handleService.inspectionDeclaration(vo);
			String code = map.get("code");
			String msg = map.get("msg");
			
			if("0000".equals(code)){
				String waterNumber = "";
				waterNumber = SXStringUtils.deleteChineseCharactertoString(msg);
				waterNumber = waterNumber.replace("，", "");
				waterNumber = waterNumber.replace("：", "");
				waterNumber = waterNumber.replace("。", "");
				baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		//成功需要发送模板消息
        		//openId不为空,发送微信消息推送
        		if("C".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
        			try {
        				String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
        				HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.inspectionDeclaration, waterNumber, DateUtil2.date2str(new Date()));
        				baseBean.setData(handleTemplateVo);
        				String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
        				logger.info("返回的url是：" + url);
        				logger.info("handleTemplateVo 是：" + handleTemplateVo);
        				Map<String, cn.message.model.wechat.TemplateDataModel.Property> map1 = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
        				map1.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
        				map1.put("keyword1", new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()),"#212121"));
        				map1.put("keyword2", new TemplateDataModel().new Property("机动车委托异地年审","#212121"));
        				map1.put("keyword3", new TemplateDataModel().new Property("待受理","#212121"));
        				map1.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
        				boolean flag = templateMessageService.sendMessage(openId, templateId, url, map1);
        				logger.info("发送模板消息结果：" + flag);
        			} catch (Exception e) {
        				logger.error("发送模板消息  失败===", e);
        			}
        		}
        		
        		//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.inspectionDeclaration, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "委托核发机动车检验合格标志");templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
        		
	       	}else{
	       		baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg);
	       	}
		} catch (Exception e) {
			logger.error("机动车委托异地定期检验申报Action异常:"+e);
			DealException(baseBean, e);
			
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
   
   /**
    * @Title: inspectionDeclarationQuery
    * @Description: TODO(机动车委托异地定期检验申报查询)
    * @param @param request
    * @param @param response    参数
    * @return void    返回类型
    * @throws
    */
   @RequestMapping(value="/inspectionDeclarationQuery.html")
	public void inspectionDeclarationQuery(HttpServletRequest request,HttpServletResponse response){
	   /*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("inspectionDeclarationQuery参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
   		BaseBean baseBean = new BaseBean();		//创建返回结果    	
		try {
			String identityCard = request.getParameter("proposerIdentityCard");  //身份证号
	
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
					
			
			//验证identityCard
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			
			//验证sourceOfCertification
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源不能为空!");
				renderJSON(baseBean);
				return;
			}
						
			//接口调用
			Map<String, String> map = handleService.inspectionDeclarationQuery(identityCard,sourceOfCertification);
			String code = map.get("code");
			String msg = map.get("msg");
			String body=map.get("body");
			if("0000".equals(code)){
       		baseBean.setCode(MsgCode.success);
       		baseBean.setMsg(msg);
       		baseBean.setData(body);    		
	       	}else{
	       		baseBean.setCode(MsgCode.businessError);
	       		if ("9999".equals(code)) {
	       			baseBean.setMsg("输入信息格式有误！");
					}else{
						baseBean.setMsg(msg);
					}
	       	}
		} catch (Exception e) {
			logger.error("机动车委托异地定期检验申报查询Action异常:"+e);
			DealException(baseBean, e);
			
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
   /**
 	 * 机动车六年免检预约
 	 * 
 	 * @param numberPlate
 	 * @param name
 	 * @param personType
 	 * @param driveLicenseNumber
 	 * @param mobile
 	 * @param telno
 	 * @param receiverName
 	 * @param receiverNumber
 	 * @param postCode
 	 * @param receiverAddress
 	 * @param effectiveDate
 	 * @param terminationDate
 	 * @param inform
 	 * @param bookerName
 	 * @param bookerIdentityCard
 	 * @param bookerType
 	 * @param carTypeId
 	 * @param arg0
 	 * @param arg1
 	 * @param arg2
 	 * @param arg3
 	 * @param arg4
 	 */
 	@RequestMapping("createVehicleInspection")
 	public void createVehicleInspection(String numberPlate, String name, String personType, String driveLicenseNumber,
 			String mobilephone, String validateCode ,String telno, String receiverName, String receiverNumber, String postCode,
 			String receiverAddress, String effectiveDate, String terminationDate, String inform, String bookerName,
 			String bookerIdentityCard, String bookerType, String carTypeId, String sourceOfCertification, String openId,HttpServletRequest request) {
 		/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("createVehicleInspection参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
 		BaseBean baseBean = new BaseBean();
 		CreateVehicleInspectionVo createVehicleInspectionVo = new CreateVehicleInspectionVo();
 		if (StringUtil.isBlank(numberPlate)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车牌号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setPlatNumber(numberPlate);
 		}
 		if (StringUtil.isBlank(name)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("机动车所有人不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setName(name);
 		}
 		if (StringUtil.isBlank(personType)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("申请人类型不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setPersonType(personType);
 		}
 		if (StringUtil.isBlank(driveLicenseNumber)) {
// 			baseBean.setCode(MsgCode.paramsError);
// 			baseBean.setMsg("行驶证编号不能为空!");
// 			renderJSON(baseBean);
// 			return;
 			createVehicleInspectionVo.setDriveLicenseNumber("");
 		} else {
 			createVehicleInspectionVo.setDriveLicenseNumber(driveLicenseNumber);
 		}
 		if (StringUtil.isBlank(mobilephone)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("手机号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setMobile(mobilephone);
 		}
 		if (StringUtil.isBlank(telno)) {
 			createVehicleInspectionVo.setTelno("");
 		}else{
 			createVehicleInspectionVo.setTelno(telno);
 		}
 		if (StringUtil.isBlank(receiverName)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("收件人姓名不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setRecipientsName(receiverName);
 		}
 		if (StringUtil.isBlank(receiverNumber)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("收件人电话不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setRecipientsMobile(receiverNumber);
 		}
 		if (StringUtil.isBlank(postCode)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("邮政编码不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setPostCode(postCode);
 		}
 		if (StringUtil.isBlank(receiverAddress)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("收件人地址不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setPostAddr(receiverAddress);
 		}
 		if (StringUtil.isBlank(effectiveDate)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("保险生效日期不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setEffectiveDate(effectiveDate);
 		}
 		if (StringUtil.isBlank(terminationDate)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("保险终止日期不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setTerminationDate(terminationDate);
 		}
 		if (StringUtil.isBlank(inform)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("受理结果告知方式不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setInform(inform);
 		}
 		if (StringUtil.isBlank(bookerName)) {
 			createVehicleInspectionVo.setBookerName("");
 		} else {
 			createVehicleInspectionVo.setBookerName(bookerName);
 		}
 		if (StringUtil.isBlank(bookerIdentityCard)) {
 			createVehicleInspectionVo.setBookerIdNumber("");
 		} else {
 			createVehicleInspectionVo.setBookerIdNumber(bookerIdentityCard);
 		}
 		if (StringUtil.isBlank(bookerType)) {
 			createVehicleInspectionVo.setBookerType("");
 		} else {
 			createVehicleInspectionVo.setBookerType(bookerType);
 		}
 		if (StringUtil.isBlank(carTypeId)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("号牌类型Id不能为空!");
 			renderJSON(baseBean);
 			return;
 		} else {
 			createVehicleInspectionVo.setCarTypeId(carTypeId);
 		}
 		
 		
 		try {
 			// 创建返回结果
 			BaseBean refBase= handleService.createVehicleInspection(createVehicleInspectionVo);
 			String code = refBase.getCode();
 			String msg = refBase.getMsg();
 			String result = (String) refBase.getData();
 			if ("00".equals(code)) {
 				baseBean.setCode("0000");
 				baseBean.setMsg(msg);
 				if (sourceOfCertification.equals("C")) {
 					try {
 						String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
 						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.createVehicleInspection, result, DateUtil2.date2str(new Date()));
 						baseBean.setData(handleTemplateVo);
 						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
 						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
 								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
 						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
 						tmap.put("keyword1",
 								new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
 						tmap.put("keyword2", new TemplateDataModel().new Property("六年免检预约申请", "#212121"));
 						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
 						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
 						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
 						logger.info("发送模板消息结果：" + flag);
 					} catch (Exception e) {
 						logger.error("发送模板消息  失败===", e);
 					}
 				}
 				//支付宝
 				else if (sourceOfCertification.equals("Z")) {
 						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.createVehicleInspection, result, DateUtil2.date2str(new Date()));
 						baseBean.setData(handleTemplateVo);
 				}
 				//民生警务来源，模板推送
				else if("M".equals(sourceOfCertification) && StringUtil.isNotBlank(openId)){
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.createVehicleInspection, result, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						logger.info("【民生警务】结果页url：" + url);
						JSONObject templateData = new JSONObject();
						templateData.put("openid", openId);
						templateData.put("templateId", handleService.getMsjwHandleTemplateId());
						templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
						templateData.put("keyword1Data", "核发机动车检验合格标志");templateData.put("keyword1Color", "#212121");
						templateData.put("keyword2Data", "待审核");templateData.put("keyword2Color", "#212121");
						templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
						templateData.put("remarkData", "更多信息请点击详情查看");
						templateData.put("redirectUrl", url);
						String params = templateData.toJSONString();
						JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
						logger.info("【民生警务】发送模板消息结果：" + json);
						
						//新增到民生警务平台个人中心
						try {
							//调用查询接口，获取业务数据
							JSONObject inspectionJson = handleService.getVehicleInspection(result, numberPlate, "");
							if("00".equals(inspectionJson.getString("code"))){
								String resultStr = inspectionJson.getString("result");
								VehicleInspectionVO vehicleInspectionVO = null;
								if(StringUtil.isNotBlank(resultStr)){
									JSONObject parseObject = JSON.parseObject(resultStr);
									vehicleInspectionVO = JSON.parseObject(parseObject.getString("VehicleInspectionVO"), VehicleInspectionVO.class);
									
									MsjwVehicleInspectionVo businessVo = new MsjwVehicleInspectionVo();
									businessVo.setTylsbh(result);
									businessVo.setOpenid(openId);
									businessVo.setEventname("核发机动车检验合格标志");
									String msjwUrl = generateUrl(handleService.getMsjwSixyearsUrl(), vehicleInspectionVO);
									businessVo.setApplyingUrlWx(msjwUrl);//微信在办跳转地址
									businessVo.setJinduUrlWx(msjwUrl);//进度查询跳转地址
									businessVo.setPlatNumber(numberPlate);//车牌号码
									msjwService.addVehicleInspectionBusiness(businessVo);
								}
							}
						} catch (Exception e) {
							logger.error("【民生警务】新增六年免检到民生警务平台异常", e);
							e.printStackTrace();
						}
						
					} catch (Exception e) {
						logger.error("【民生警务】发送模板消息  失败===", e);
					}
				}
 				
 				else{
 					baseBean.setData(result);
 				}
 			} else {
 				baseBean.setCode("0001");
 				baseBean.setMsg(msg+": " +result);
 				baseBean.setData(result);
 			}
 		} catch (Exception e) {
 			logger.error("预约六年免检异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
 	}
 	
 	/**
 	 * 生成六年免检结果页地址
 	 * @param baseUrl
 	 * @param vo
 	 * @return
 	 */
 	public String generateUrl(String baseUrl, VehicleInspectionVO vo){
		StringBuffer sb = new StringBuffer();
		sb.append(baseUrl)
		.append("&yyh=").append(vo.getBookNumber())//预约号
		.append("&cphm=").append(vo.getPlatNumber())//车牌号码
		.append("&xm=").append(vo.getName())//姓名
		.append("&sjhm=").append(vo.getMobile())//手机号码
		.append("&bxsxrq=").append(vo.getEffectiveDate())//保险生效日期
		.append("&slgzfs=").append(vo.getInform())//受理告知方式
		.append("&cjrq=").append(vo.getCreateDate())//创建日期
		.append("&yyzt=").append(vo.getBookState())//预约状态
		.append("&shzt=").append(vo.getApproveState());//审核状态
		return sb.toString();
	}
 	
 	/**
 	 * 获取车辆类型Id
 	 * 
 	 * @param arg0
 	 * @param arg1
 	 */
 	@RequestMapping("getCarTypeId")
 	public void getCarTypeId(String code) {
 		BaseBean baseBean = new BaseBean();
 		boolean flag = false;
 		if (StringUtil.isBlank(code)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车牌类型code不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		try {
 			// 创建返回结果
 			JSONObject jsonObject = handleService.getCarTypes();
 			String gcode = jsonObject.getString("code");
 			String msg = jsonObject.getString("msg");
 			if ("00".equals(gcode)) {
 				baseBean.setCode("0000");
 				JSONObject json = jsonObject.getJSONObject("result");
 				JSONArray jsonArray = json.getJSONArray("CarTypeVO");
 				Iterator iterator = jsonArray.iterator();
 				while (iterator.hasNext()) {
 					JSONObject json2 = (JSONObject) iterator.next();
 					String carCode = json2.getString("code");
 					if (carCode.equals(code)) {
 						String id = json2.getString("id");
 						baseBean.setData(id);
 						flag = true;
 					}
 				}
 			} else {
 				baseBean.setCode("0001");
 				baseBean.setMsg(msg);
 				baseBean.setData(jsonObject.getString("result"));
 			}
 			if (flag = false) {
 				baseBean.setCode("0001");
 				baseBean.setMsg("查询车辆类型id失败");
 			}
 		} catch (Exception e) {
 			logger.error("预约六年免检异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
 	}
 	/**
 	 * 取消机动车六年免检预约
 	 * 
 	 * @param bookNumber
 	 * @param numberPlate
 	 */
 	@RequestMapping("cancelVehicleInspection")
 	public void cancelVehicleInspection(String bookNumber, String numberPlate) {
 		BaseBean baseBean = new BaseBean();
 		
 		if (StringUtil.isBlank(bookNumber)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("bookNumber不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(numberPlate)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车牌号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		try {
 			// 创建返回结果
 			JSONObject jsonObject = handleService.cancelVehicleInspection(bookNumber,numberPlate);
 			String code = jsonObject.getString("code");
 			String msg = jsonObject.getString("msg");
 			String result = jsonObject.getString("result");
 			if ("00".equals(code)) {
 				//民生警务平台，删除记录
				try {
					//根据tylsbh和platNumber删除数据库记录
					msjwService.deleteMsjwVehicleInspection(bookNumber, numberPlate);
				} catch (Exception e) {
					logger.error("【民生警务】deleteMsjwVehicleInspection接口异常: bookNumber="+bookNumber+",numberPlate="+numberPlate, e);
					e.printStackTrace();
				}
				try {
					//根据tylsbh删除民生警务记录
					msjwService.deleteApplyingBusiness(bookNumber);
				} catch (Exception e) {
					logger.error("【民生警务】deleteApplyingBusiness接口异常: bookNumber="+bookNumber, e);
					e.printStackTrace();
				}
 				
 				baseBean.setCode("0000");
 				baseBean.setData(result);
 				baseBean.setMsg(msg);
 			} else {
 				baseBean.setCode("0001");
 				baseBean.setMsg(msg);
 				baseBean.setData(result);
 			}
 		} catch (Exception e) {
 			logger.error("取消六年免检预约异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
 	}
 	/**
 	 * 获取机动车六年免检预约信息
 	 * 
 	 * @param bookNumber
 	 * @param platNumber
 	 * @param driveLicenseNumber
 	 */
 	@RequestMapping("getVehicleInspection")
 	public void getVehicleInspection(String bookNumber, String numberPlate, String driveLicenseNumber) {
 		BaseBean baseBean = new BaseBean();
 		if (StringUtil.isBlank(bookNumber)) {
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车牌号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		try {
 			// 创建返回结果
 			JSONObject jsonObject = handleService.getVehicleInspection(bookNumber,numberPlate, driveLicenseNumber);
 			String code = jsonObject.getString("code");
 			String msg = jsonObject.getString("msg");
 			if ("00".equals(code)) {
 				Object obj = jsonObject.get("result");
 				if(obj instanceof JSONObject && obj != null){
				JSONObject result = (JSONObject) obj;
				baseBean.setCode(MsgCode.success);
				baseBean.setData(JSON.parseObject(result.getString("VehicleInspectionVO"), VehicleInspectionVO.class));
 				}else{
 					baseBean.setCode("0001");
 	 				baseBean.setMsg("未查询到相关数据");
 				}
 			} else {
 				baseBean.setCode("0001");
 				baseBean.setMsg(msg);
 			}
 		} catch (Exception e) {
 			logger.error("获取六年免检预约信息异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
 	}
	/** 
	 * 补领机动车号牌
	 * @Description TODO(补领机动车号牌)
	 * @param vo 补领机动车号牌 申请信息
	 */
    @RequestMapping("replaceMotorVehicleLicensePlate")
    public void replaceMotorVehicleLicensePlate(ReplaceMotorVehicleLicensePlateVo vo, HttpServletRequest request){
    	/*Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			String value = request.getParameter(paraName);
			if(null != value && value.length() <= 30){
				logger.info("replaceMotorVehicleLicensePlate参数为：" + paraName+": "+request.getParameter(paraName));
			}
		}*/
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(vo.getName())){
    			logger.error(JSON.toJSONString(vo));
    			
        		baseBean.setMsg("车主姓名不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getIdentityCard())){
        		baseBean.setMsg("证件号码不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getCarOwnerIdentityCard())){
    			baseBean.setMsg("车主证件号码不能为空!");
    			baseBean.setCode(MsgCode.paramsError);
    			renderJSON(baseBean);
    			return;
    		}
    		if(StringUtils.isBlank(vo.getNumberPlate())){
        		baseBean.setMsg("号牌号码不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getPlateType())){
        		baseBean.setMsg("号牌种类不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getPlaceOfDomicile())){
        		baseBean.setMsg("户籍所在地不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		//户籍所在地代号转换::前端表示1-深户,0-外籍; 警视通表示0-深户,1-外籍
        		if("1".equals(vo.getPlaceOfDomicile())){
        			vo.setPlaceOfDomicile("0");//前端传1-深户 转换为 警视通0-深户
        		}else if("0".equals(vo.getPlaceOfDomicile())){
        			vo.setPlaceOfDomicile("1");//前端传0-外籍 转换为 警视通1-外籍
        			
        			vo.setResidenceNo(vo.getIdentityCard());//设置居住证号码,居住证号码与身份证号码一样
        		}
        	}
    		
    		if(StringUtils.isBlank(vo.getResidenceNo())){//居住证号码
    			vo.setResidenceNo("");
    		}
    		if(StringUtils.isBlank(vo.getJZZA())){//居住证正面图片
    			vo.setJZZA("");
    		}
    		if(StringUtils.isBlank(vo.getJZZB())){//居住证反面图片
    			vo.setJZZB("");
    		}
			if(StringUtils.isBlank(vo.getPHOTO31())){//境外人员临住表
				vo.setPHOTO31("");
    		}
    		
    		if(StringUtils.isBlank(vo.getReceiverName())){
        		baseBean.setMsg("收件人姓名不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getReceiverNumber())){
    			baseBean.setMsg("收件人手机不能为空!");
    			baseBean.setCode(MsgCode.paramsError);
    			renderJSON(baseBean);
    			return;
    		}
    		if(StringUtils.isBlank(vo.getReceiverAddress())){
    			baseBean.setMsg("收件人地址不能为空!");
    			baseBean.setCode(MsgCode.paramsError);
    			renderJSON(baseBean);
    			return;
    		}
    		
    		//住所地址,可为空
    		if(StringUtils.isBlank(vo.getAddress())){
    			vo.setAddress("");
    		}
    		
    		if(StringUtils.isBlank(vo.getPHOTO9())){
        		baseBean.setMsg("身份证（正面）不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getPHOTO10())){
        		baseBean.setMsg("身份证（反面）不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getDJZSFYJ())){
        		baseBean.setMsg("机动车登记证书不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(vo.getSourceOfCertification())){
    			baseBean.setMsg("申请来源不能为空!");
    			baseBean.setCode(MsgCode.paramsError);
    			renderJSON(baseBean);
    			return;
    		}
    		
    		//设置外网ip
    		vo.setIp(getIp2(request));
    		//设置登录用户身份证号码
    		if(StringUtils.isBlank(vo.getLoginUserIdentityCard())){
        		vo.setLoginUserIdentityCard(vo.getIdentityCard());
        	}
    		
    		baseBean = handleService.replaceMotorVehicleLicensePlate(vo);
    		
    		//申请成功发送微信模板消息
    		if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(vo.getSourceOfCertification())){
				try {
					String msg = baseBean.getMsg();
					String waterNumber = "";
					if(baseBean.getData() != null){
						waterNumber = baseBean.getData().toString();
					}else{
						waterNumber = msg.substring(msg.indexOf("：")+1, msg.indexOf("。"));//截取流水号
					}
					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.replaceMotorVehicleLicensePlate, waterNumber, DateUtil2.date2str(new Date()));
					baseBean.setData(handleTemplateVo);
					String url = HandleTemplateVo.getUrl(handleTemplateVo, handleService.getTemplateSendUrl());
					logger.info("返回的url是：" + url);
					logger.info("handleTemplateVo 是：" + handleTemplateVo);
					String openId = vo.getOpenId();
					String templateId = "9k6RflslCxwEVw_Sz12vShnTzOUsw5hS2TdrjHXs_4A";
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					map.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
					map.put("keyword1", new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()),"#212121"));
					map.put("keyword2", new TemplateDataModel().new Property("补领机动车号牌","#212121"));
					map.put("keyword3", new TemplateDataModel().new Property("待受理","#212121"));
					map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
			}
    		
    		//民生警务来源，模板推送
			else if(MsgCode.success.equals(baseBean.getCode()) && "M".equals(vo.getSourceOfCertification())){
				try {
					String msg = baseBean.getMsg();
					String waterNumber = "";
					if(baseBean.getData() != null){
						waterNumber = baseBean.getData().toString();
					}else{
						waterNumber = msg.substring(msg.indexOf("：")+1, msg.indexOf("。"));//截取流水号
					}
					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.replaceMotorVehicleLicensePlate, waterNumber, DateUtil2.date2str(new Date()));
					baseBean.setData(handleTemplateVo);
					String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
					logger.info("【民生警务】结果页url：" + url);
					JSONObject templateData = new JSONObject();
					templateData.put("openid", vo.getOpenId());
					templateData.put("templateId", handleService.getMsjwHandleTemplateId());
					templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
					templateData.put("keyword1Data", "办理补领、换领机动车号牌");templateData.put("keyword1Color", "#212121");
					templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
					templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
					templateData.put("remarkData", "更多信息请点击详情查看");
					templateData.put("redirectUrl", url);
					String params = templateData.toJSONString();
					JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
					logger.info("【民生警务】发送模板消息结果：" + json);
					
					//新增到民生警务平台个人中心
					try {
						MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
						businessVo.setTylsbh(waterNumber);
						businessVo.setOpenid(vo.getOpenId());
						businessVo.setEventname("办理补领、换领机动车号牌");
						businessVo.setApplyingUrlWx(url);//微信在办跳转地址
						businessVo.setJinduUrlWx(url);//进度查询跳转地址
						msjwService.addApplyingBusiness(businessVo);
					} catch (Exception e) {
						logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
						e.printStackTrace();
					}
					
				} catch (Exception e) {
					logger.error("【民生警务】发送模板消息  失败===", e);
				}
			}
    		
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("replaceMotorVehicleLicensePlate Action异常", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
	/**
	 * 首违免罚查询
	 * 
	 * @param numberPlate
	 * @param plateType
	 * @param illegalNumber
	 * @param queryType
	 */
	@RequestMapping("getResultOfFirstIllegalImpunity")
	public void getResultOfFirstIllegalImpunity(String numberPlate, String plateType, String illegalNumber,
			String queryType) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(queryType)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("查询类型不能为空!");
			renderJSON(baseBean);
			return;
		} else {
			if("1".equals(queryType)) {
				if(StringUtil.isBlank(numberPlate)) {
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("车牌号码不能为空!");
					renderJSON(baseBean);
					return;
				}
				if(StringUtil.isBlank(plateType)) {
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("车牌类型不能为空!");
					renderJSON(baseBean);
					return;
				}
				illegalNumber="";
			}else{
				if(StringUtil.isBlank(illegalNumber)) {
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("流水号不能为空!");
					renderJSON(baseBean);
					return;
				}
				numberPlate="";
				plateType = "";
			}
		}
		try {
			// 创建返回结果
			Map<String, Object> map = handleService.getResultOfFirstIllegalImpunity(numberPlate, plateType,
					illegalNumber, queryType);
			String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if ("0000".equals(code)) {
				baseBean.setCode("0000");
				baseBean.setData(map.get("data"));
			} else {
				baseBean.setCode("0001");
				baseBean.setMsg(msg);
			}
		} catch (Exception e) {
			logger.error("首违免罚查询异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 
	 * @param request
	 */
	@RequestMapping("electronicDelegateVehicles")
 	public void electronicDelegateVehicles(HttpServletRequest request) {
 		BaseBean baseBean = new BaseBean();
 		String businessType = request.getParameter("businessType");
 		String businessReason = request.getParameter("businessReason");
 		String bailerName = request.getParameter("bailerName");
 		String bailerIdentityCard = request.getParameter("bailerIdentityCard");
 		String bailerNumberPlate = request.getParameter("bailerNumberPlate");
 		String bailerLicenseNumber = request.getParameter("bailerLicenseNumber");
 		String bailerValidTime = request.getParameter("bailerValidTime");
 		String baileeName = request.getParameter("baileeName");
 		String baileeIdentitycard = request.getParameter("baileeIdentitycard");
 		String baileeMobilephone = request.getParameter("baileeMobilephone");
 		String userSource = request.getParameter("userSource");
 		if (StringUtil.isBlank(businessType)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("业务类型不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(businessReason)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("业务原因不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(bailerName)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("委托人姓名不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(bailerIdentityCard)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("委托人身份证号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(bailerLicenseNumber)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("委托人车牌号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(bailerNumberPlate)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("委托人号牌种类不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(bailerValidTime)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("委托有效时间不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(baileeName)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("受托人姓名不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(baileeIdentitycard)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("受托人身份证号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(baileeMobilephone)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("受托人手机号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(userSource)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("认证来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		DelegateVehiclesVo delegateVehiclesVo = new DelegateVehiclesVo();
 		delegateVehiclesVo.setBaileeIdentitycard(baileeIdentitycard);
 		delegateVehiclesVo.setBaileeMobilephone(baileeMobilephone);
 		delegateVehiclesVo.setBaileeName(baileeName);
 		delegateVehiclesVo.setBailerIdentityCard(bailerIdentityCard);
 		delegateVehiclesVo.setBailerLicenseNumber(bailerLicenseNumber);
 		delegateVehiclesVo.setBailerName(bailerName);
 		delegateVehiclesVo.setBailerNumberPlate(bailerNumberPlate);
 		delegateVehiclesVo.setBailerValidTime(bailerValidTime);
 		delegateVehiclesVo.setBusinessReason(businessReason);
 		delegateVehiclesVo.setBusinessType(businessType);
 		delegateVehiclesVo.setUserSource(userSource);
 		try {
 			// 创建返回结果
 			baseBean = handleService.electronicDelegateVehicles(delegateVehiclesVo);
 		} catch (Exception e) {
 			logger.error("车管电子委托机动车业务申报业务异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
 	}
	
	/**
	 * 机动车抵押/取消抵押
	 * @param request
	 */
	@RequestMapping("applyOrCancleCarMortgage")
 	public void applyOrCancleCarMortgage(HttpServletRequest request) {
 		BaseBean baseBean = new BaseBean();
 		
 		String businessType = request.getParameter("businessType");
 		String sqlx = request.getParameter("sqlx");          
 		String mainContractNo = request.getParameter("mainContractNo");
 		String mortgageContactNo = request.getParameter("mortgageContactNo");
 		String carNumber = request.getParameter("carNumber");     
 		String numberPlate = request.getParameter("numberPlate");   
 		String carCode = request.getParameter("carCode"); 
 		String registrationNO = request.getParameter("registrationNO");
 		String ownerPhone = request.getParameter("ownerPhone");  
 		String mortgageeIDcard = request.getParameter("mortgageeIDcard");
 		String mortgageeName = request.getParameter("mortgageeName");
 		String mortgageeSex = request.getParameter("mortgageeSex"); 
 		String mortgageeAddr = request.getParameter("mortgageeAddr");
 		String carType = request.getParameter("carType");
 		String mortgagerIDcard = request.getParameter("mortgagerIDcard");
 		String mortgagerName = request.getParameter("mortgagerName");
 		String mortgagerSex = request.getParameter("mortgagerSex");
 		String mortgagerAddr = request.getParameter("mortgagerAddr");
 		String recipientName = request.getParameter("recipientName");
 		String recipientPhone = request.getParameter("recipientPhone");
 		String recipientAddr = request.getParameter("recipientAddr");
 		String recipientCode = request.getParameter("recipientCode");
 		String receiverName = request.getParameter("receiverName");
 		String receiverPhone = request.getParameter("receiverPhone");
 		String receiverAddr = request.getParameter("receiverAddr");
 		String receiverCode = request.getParameter("receiverCode");
 		String sourceOfCertification = request.getParameter("sourceOfCertification");
 		String openId = request.getParameter("openId");
 		if (StringUtil.isBlank(businessType)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("业务类型不能为空!");
 			renderJSON(baseBean);
 			return;
 		}else if("A".equals(businessType)){
 			if (StringUtil.isBlank(mainContractNo)) {			
 	 			baseBean.setCode(MsgCode.paramsError);
 	 			baseBean.setMsg("mainContractNo不能为空!");
 	 			renderJSON(baseBean);
 	 			return;
 	 		}
 			if (StringUtil.isBlank(mortgageContactNo)) {			
 	 			baseBean.setCode(MsgCode.paramsError);
 	 			baseBean.setMsg("mortgageContactNo不能为空!");
 	 			renderJSON(baseBean);
 	 			return;
 	 		}
 		}
 		if (StringUtil.isBlank(sqlx)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("sqlx不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(carNumber)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("carNumber不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(numberPlate)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("numberPlate不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(carCode)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("carCode不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(registrationNO)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("registrationNO不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(ownerPhone)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("ownerPhone不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(mortgageeIDcard)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("mortgageeIDcard不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(mortgageeName)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("mortgageeName不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(mortgageeSex)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("mortgageeSex不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(mortgageeAddr)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("mortgageeAddr不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(carType)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("carType不能为空!");
 			renderJSON(baseBean);
 			return;
 		}else if ("0".equals(carType)) {
 			if (StringUtil.isBlank(mortgagerSex)) {			
 	 			baseBean.setCode(MsgCode.paramsError);
 	 			baseBean.setMsg("mortgagerSex不能为空!");
 	 			renderJSON(baseBean);
 	 			return;
 	 		}
		}
 		if (StringUtil.isBlank(mortgagerIDcard)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("mortgagerIDcard不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(mortgagerName)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("mortgagerName不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(mortgagerAddr)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("mortgagerAddr不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(recipientName)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("recipientName不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(recipientPhone)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("recipientPhone不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(recipientAddr)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("recipientAddr不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(recipientCode)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("recipientCode不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		
 		if (StringUtil.isBlank(receiverName)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("receiverName不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		
 		if (StringUtil.isBlank(receiverPhone)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("receiverPhone不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(receiverAddr)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("receiverAddr不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(receiverCode)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("receiverCode不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(sourceOfCertification)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("sourceOfCertification不能为空!");
 			renderJSON(baseBean);
 			return;
 		}

 		CarMortgageVo carMortgageVo = new CarMortgageVo();
 		carMortgageVo.setBusinessType(businessType);
		carMortgageVo.setCarCode(carCode);
		carMortgageVo.setCarNumber(carNumber);
		carMortgageVo.setCarType(carType);
		carMortgageVo.setMainContractNo(mainContractNo);
		carMortgageVo.setMortgageContactNo(mortgageContactNo);
		carMortgageVo.setMortgageeAddr(mortgageeAddr);
		carMortgageVo.setMortgageeIDcard(mortgageeIDcard);
		carMortgageVo.setMortgageeName(mortgageeName);
		carMortgageVo.setMortgageeSex(mortgageeSex);
		carMortgageVo.setMortgagerAddr(mortgagerAddr);
		carMortgageVo.setMortgagerIDcard(mortgagerIDcard);
		carMortgageVo.setMortgagerName(mortgagerName);
		carMortgageVo.setMortgagerSex(mortgagerSex);
		carMortgageVo.setNumberPlate(numberPlate);
		carMortgageVo.setOwnerPhone(ownerPhone);
		carMortgageVo.setReceiverAddr(receiverAddr);
		carMortgageVo.setReceiverCode(receiverCode);
		carMortgageVo.setReceiverName(receiverName);
		carMortgageVo.setReceiverPhone(receiverPhone);
		carMortgageVo.setRecipientAddr(recipientAddr);
		carMortgageVo.setRecipientCode(recipientCode);
		carMortgageVo.setRecipientName(recipientName);
		carMortgageVo.setRecipientPhone(recipientPhone);
		carMortgageVo.setRegistrationNO(registrationNO);
		carMortgageVo.setSourceOfCertification(sourceOfCertification);
		carMortgageVo.setSqlx(sqlx);
		
 		try {
 			// 创建返回结果
 			baseBean = handleService.applyOrCancleCarMortgage(carMortgageVo);
 			logger.info("机动车抵押业务返回结果：" + baseBean.toJson());
 			String code = baseBean.getCode();
 			String msg = baseBean.getMsg();
 			if (MsgCode.success.equals(code)) {
 				String waterNumber = msg.substring(msg.indexOf("：")+1);
				String url = handleService.getMsjwCarMortgageUrl()+"loginUser="+mortgageeIDcard+"&sqlx="+sqlx+"&source="+sourceOfCertification;
				logger.info("机动车抵押业务返回的url : " + url);
				if("M".equals(sourceOfCertification)){
    				try {
    					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, "applyOrCancleCarMortgage", waterNumber, DateUtil2.date2str(new Date()));
    					baseBean.setData(handleTemplateVo);
    					String url2 = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
    					logger.info("【民生警务】结果页url：" + url);
    					JSONObject templateData = new JSONObject();
    					templateData.put("openid", openId);
    					templateData.put("templateId", handleService.getMsjwHandleTemplateId());
    					templateData.put("firstData", "您好，您的业务办理申请已提交，具体信息如下：");
    					templateData.put("keyword1Data", "机动车个人抵押解押登记");	templateData.put("keyword1Color", "#212121");
    					templateData.put("keyword2Data", "待初审");templateData.put("keyword2Color", "#212121");
    					templateData.put("keyword3Data", DateUtil.formatDateTime(new Date()));templateData.put("keyword3Color", "#212121");
    					templateData.put("remarkData", "更多信息请点击详情查看");
    					templateData.put("redirectUrl", url2);
    					String params = templateData.toJSONString();
    					JSONObject json = msjwService.sendTemplateMsg2Msjw(params);
    					logger.info("【民生警务】发送模板消息结果：" + json);
    					
    					//新增到民生警务平台个人中心
    	 				try {
    	 					MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
    	 					businessVo.setTylsbh(waterNumber);
    	 					businessVo.setOpenid(openId);
    	 					businessVo.setEventname("机动车个人抵押解押登记");
    	 					businessVo.setApplyingUrlWx(url);//微信在办跳转地址
    	 					businessVo.setJinduUrlWx(url);//进度查询跳转地址
    	 					msjwService.addCarMortgageBusiness(businessVo);
    	 				} catch (Exception e) {
    	 					logger.error("【民生警务】新增在办业务到民生警务平台异常", e);
    	 					e.printStackTrace();
    	 				}
    					
    				} catch (Exception e) {
    					logger.error("【民生警务】发送模板消息  失败===", e);
    				}
    			}
 				
			}
 		
 		} catch (Exception e) {
 			logger.error("机动车抵押业务异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
 	}
	
	/**
	 * 机动车抵押业务查询
	 * @param request
	 */
	@RequestMapping("queryCarMortgage")
 	public void queryCarMortgage(HttpServletRequest request) {
 		BaseBean baseBean = new BaseBean();
 		
 		String loginUser = request.getParameter("loginUser");
 		String sqlx = request.getParameter("sqlx");          
 		String sourceOfCertification = request.getParameter("sourceOfCertification");
 		
 		if (StringUtil.isBlank(loginUser)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("loginUser不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(sqlx)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("sqlx不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		if (StringUtil.isBlank(sourceOfCertification)) {			
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("sourceOfCertification不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
 		
 		try {
 			// 创建返回结果
 			baseBean = handleService.queryCarMortgage(loginUser, sqlx, sourceOfCertification);
 			logger.info("机动车抵押业务查询返回结果：" + baseBean.toJson());
 		} catch (Exception e) {
 			logger.error("机动车抵押业务查询异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
 	}
}
