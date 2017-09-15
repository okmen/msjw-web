package cn.web.front.action.account;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.vo.RegisterVo;
import cn.account.service.IAccountService;
import cn.message.model.wechat.MessageChannelModel;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
import cn.sdk.util.SXStringUtils;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;

/**
 * 注册中心
 * 
 * @author suntao
 * 
 */
@Controller
@RequestMapping(value = "/register/")
@SuppressWarnings(value = "all")
public class RegisterAction extends BaseAction {
	private final static Logger logger = LoggerFactory.getLogger(AccountAction.class);

	@Autowired
	private IAccountService accountService;
	
	@Autowired
    @Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;

	/**
	 * 星级用户认证-我是车主
	 * 
	 * @param licensePlateType 车牌类型
	 * @param provinceAbbreviation  省简称
	 * @param licensePlateNumber 车牌号码
	 * @param identityCard 身份证
	 * @param linkAddress 联系地址
	 * @param mobilephone 手机号码
	 * @param validateCode 验证码
	 * @param isDriverLicense  是否有驾驶证
	 * @param driverLicenseIssuedAddress 驾驶证核发地
	 * @param idCardImgPositive 身份证正面
	 * @param idCardImgNegative  身份证反面
	 * @param idCardImgHandHeld  手持身份证
	 * @param openId 微信openId
	 *           
	 */
	@RequestMapping(value = "iAmTheOwner")
	public void iAmTheOwner(String licensePlateType, String provinceAbbreviation, String licensePlateNumber,
			String identityCard, String linkAddress, String mobilephone, String validateCode, Integer isDriverLicense,
			String driverLicenseIssuedAddress, String idCardImgPositive, String idCardImgNegative,
			String idCardImgHandHeld,String openId, String sourceOfCertification, String businessType) {
		String code = MsgCode.success;
		StringBuffer sb = new StringBuffer("");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(identityCard)) {
			code=MsgCode.paramsError;
			sb.append("身份证为空  ");
		} else {
			registerVo.setUserIdCard(identityCard);
		}
		if (StringUtil.isBlank(openId)) {
			code=MsgCode.paramsError;
			sb.append("openId为空  ");
		} else {
			registerVo.setOpenId(openId);
		}
		registerVo.setLinkAddress(linkAddress);

		if (StringUtil.isBlank(mobilephone)) {
			code=MsgCode.paramsError;
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (isDriverLicense < 0) {
			code=MsgCode.paramsError;
			sb.append("是否有驾驶证错误  ");
		} else {
			if(isDriverLicense==1){//1-有驾驶证
				if (StringUtil.isBlank(driverLicenseIssuedAddress)) {
					code=MsgCode.paramsError;
					sb.append("驾驶证核发地为空  ");
				} else {
					registerVo.setDriverLicenseIssuedAddress(driverLicenseIssuedAddress);
				}
			}else{
				registerVo.setDriverLicenseIssuedAddress("");
			}
		}
		if (StringUtil.isBlank(validateCode)) {
			code=MsgCode.paramsError;
			sb.append("验证码为空  ");
		}
		if (StringUtil.isBlank(idCardImgPositive)) {
			code=MsgCode.paramsError;
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgNegative)) {
			code=MsgCode.paramsError;
			sb.append("身份证反面为空 ");
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code=MsgCode.paramsError;
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}

		if (StringUtil.isBlank(licensePlateType)) {
			code=MsgCode.paramsError;
			sb.append("车牌类型为空  ");
		} else {
			registerVo.setLicensePlateType(licensePlateType);
		}

		if (StringUtil.isBlank(provinceAbbreviation)) {
			code=MsgCode.paramsError;
			sb.append("省简称为空  ");
		} else {
			registerVo.setProvinceAbbreviation(provinceAbbreviation);
		}

		if (StringUtil.isBlank(licensePlateNumber)) {
			code=MsgCode.paramsError;
			sb.append("车牌号码为空  ");
		} else {
			registerVo.setLicensePlateNumber(licensePlateNumber);
		}

		BaseBean basebean = new BaseBean();
		try {
			if (MsgCode.success.equals(code)) {// 参数校验通过
				int result = accountService.verificatioCode(mobilephone, validateCode);
				if (0 == result) {
					registerVo.setCallAccount("WX02_TEST"); //默认写死，玉璞发的
					registerVo.setCertifiedType("1");
					registerVo.setCertifiedRole("1");
					registerVo.setCertifiedSource(sourceOfCertification);
					// registerVo
					JSONObject json = accountService.iAmTheOwner(registerVo);
					code = json.getString("CODE");
					if (!MsgCode.success.equals(code)) {
						code=MsgCode.businessError;
					}
					basebean.setCode(code);
					basebean.setMsg(json.getString("MSG"));
					
					//申请成功，发送微信模板消息
					sendServiceMessage(openId, sourceOfCertification, code, basebean, json, businessType);
					
				}
				if (1 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}
				if (2 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码失效    ");
					basebean.setMsg(sb.toString());
				}			
			} else {
				basebean.setCode(code);
				basebean.setMsg(sb.toString());
			}

		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("iAmTheOwner出错",e);
		}
		renderJSON(basebean);
		logger.debug(JSON.toJSONString(basebean));

	}

	/**
	 * 星级用户认证-我是长期使用人
	 * 
	 * @param licensePlateType 车牌类型
	 * @param provinceAbbreviation 省简称
	 * @param licensePlateNumber 车牌号码
	 * @param ownerName 车主姓名
	 * @param ownerIdCard 车主身份证
	 *           
	 * @param userIdCard 使用人身份证
	 *            
	 * @param linkAddress
	 *            联系地址
	 * @param mobilephone
	 *            手机号码
	 * @param validateCode
	 *            验证码
	 * @param driverLicenseIssuedAddress
	 *            驾驶证核发地
	 * @param idCardImgPositive
	 *            身份证正面
	 * @param idCardImgNegative
	 *            身份证反面
	 * @param idCardImgHandHeld
	 *            手持身份证
	 *  @param openId
	 *  			微信openId
	 */
	@RequestMapping(value = "iamALongtimeUser")
	public void iamALongtimeUser(String licensePlateType, String provinceAbbreviation, String licensePlateNumber,
			String ownerName, String ownerIdCard, String userIdCard, String linkAddress, String mobilephone,
			String validateCode, String driverLicenseIssuedAddress, String idCardImgPositive, String idCardImgHandHeld,
			String idCardImgNegative, String ownerIdCardImgPositive, String ownerIdCardImgHandHeld,String openId,
			String sourceOfCertification, String businessType) {
		String code = MsgCode.success;
		StringBuffer sb = new StringBuffer("");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(userIdCard)) {
			code=MsgCode.paramsError;
			sb.append("使用人身份证为空  ");
		} else {
			registerVo.setUserIdCard(userIdCard);
		}
		if (StringUtil.isBlank(ownerIdCard)) {
			code=MsgCode.paramsError;
			sb.append("车主身份证为空  ");
		} else {
			registerVo.setOwnerIdCard(ownerIdCard);
		}
		if (StringUtil.isBlank(openId)) {
			code=MsgCode.paramsError;
			sb.append("openId为空  ");
		} else {
			registerVo.setOpenId(openId);
		}

//		if (StringUtil.isBlank(linkAddress)) {
//			code=MsgCode.paramsError;
//			sb.append("联系地址为空  ");
//		} else {
			registerVo.setLinkAddress(linkAddress);
//		}

		if (StringUtil.isBlank(mobilephone)) {
			code=MsgCode.paramsError;
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (StringUtil.isBlank(validateCode)) {
			code=MsgCode.paramsError;
			sb.append("验证码为空  ");
		}

		if (StringUtil.isBlank(driverLicenseIssuedAddress)) {
			code=MsgCode.paramsError;
			sb.append("驾驶证核发地为空  ");
		} else {
			registerVo.setDriverLicenseIssuedAddress(driverLicenseIssuedAddress);
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code=MsgCode.paramsError;
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code=MsgCode.paramsError;
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}

		if (StringUtil.isBlank(licensePlateType)) {
			code=MsgCode.paramsError;
			sb.append("车牌类型为空  ");
		} else {
			registerVo.setLicensePlateType(licensePlateType);
		}

		if (StringUtil.isBlank(provinceAbbreviation)) {
			code=MsgCode.paramsError;
			sb.append("省简称为空  ");
		} else {
			registerVo.setProvinceAbbreviation(provinceAbbreviation);
		}

		if (StringUtil.isBlank(licensePlateNumber)) {
			code=MsgCode.paramsError;
			sb.append("车牌号码为空  ");
		} else {
			registerVo.setLicensePlateNumber(licensePlateNumber);
		}

		if (StringUtil.isBlank(ownerName)) {
			code=MsgCode.paramsError;
			sb.append("车主姓名为空  ");
		} else {
			registerVo.setOwnerIdName(ownerName);
			registerVo.setOwnerName(ownerName);
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code=MsgCode.paramsError;
			sb.append("车主身份证正面为空  ");
		} else {
			registerVo.setOwnerIdCardImgPositive(ownerIdCardImgPositive);
		}

		if (StringUtil.isBlank(ownerIdCardImgHandHeld)) {
			code=MsgCode.paramsError;
			sb.append("车主手持身份证为空  ");
		} else {
			registerVo.setOwnerIdCardImgHandHeld(ownerIdCardImgHandHeld);
		}

		BaseBean basebean = new BaseBean();
		try {
			if (MsgCode.success.equals(code)) {// 参数校验通过

				// 0-验证成功，1-验证失败，2-验证码失效
				int result = accountService.verificatioCode(mobilephone, validateCode);
				if (0 == result) {
					registerVo.setCertifiedType("2");
					registerVo.setCallAccount("WX02_TEST");
					registerVo.setCertifiedRole("1");
					registerVo.setCertifiedSource(sourceOfCertification);
					JSONObject json = accountService.iamALongtimeUser(registerVo);
					code = json.getString("CODE");
					if (!MsgCode.success.equals(code)) {
						code=MsgCode.businessError;
					}
					basebean.setCode(code);
					basebean.setMsg(json.getString("MSG"));
					
					//申请成功，发送微信模板消息
					sendServiceMessage(openId, sourceOfCertification, code, basebean, json, businessType);
					
				}
				if (1 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}
				if (2 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码失效    ");
					basebean.setMsg(sb.toString());
				}

			} else {
				basebean.setCode(code);
				basebean.setMsg(sb.toString());
			}
		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("iamALongtimeUser出错",e);
		}
		renderJSON(basebean);
		logger.debug(JSON.toJSONString(basebean));
	}

	/**
	 * 星级用户认证-我有驾驶证,但没有固定车辆
	 * 
	 * @param identityCard
	 *            身份证
	 * @param linkAddress
	 *            联系地址
	 * @param mobilephone
	 *            手机号码
	 * @param validateCode
	 *            验证码
	 * @param driverLicenseIssuedAddress
	 *            驾驶证核发地
	 * @param idCardImgPositive
	 *            身份证正面
	 * @param idCardImgNegative
	 *            身份证反面
	 * @param idCardImgHandHeld
	 *            手持身份证
	 * @param openId
	 *            微信openId
	 * url
	 * http://localhost:8080/web/register/haveDriverLicenseNotCar.html?identityCard=33333333&linkAddress=1111111&mobilephone=13652311206&validateCode=222222&driverLicenseIssuedAddress=33333&idCardImgPositive=444444&idCardImgNegative=55555&idCardImgHandHeld=555555
	 */
	@RequestMapping(value = "haveDriverLicenseNotCar")
	public void haveDriverLicenseNotCar(String identityCard, String linkAddress, String mobilephone,
			String validateCode, String driverLicenseIssuedAddress, String idCardImgPositive, String idCardImgNegative,
			String idCardImgHandHeld,String openId,String sourceOfCertification, String businessType) {

		String code = MsgCode.success;
		StringBuffer sb = new StringBuffer("");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(identityCard)) {
			code=MsgCode.paramsError;
			sb.append("身份证为空  ");
		} else {
			registerVo.setUserIdCard(identityCard);
		}

//		if (StringUtil.isBlank(linkAddress)) {
//			code=MsgCode.paramsError;
//			sb.append("联系地址为空  ");
//		} else {
			registerVo.setLinkAddress(linkAddress);
//		}

		if (StringUtil.isBlank(mobilephone)) {
			code=MsgCode.paramsError;
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}
		
		if (StringUtil.isBlank(openId)) {
			code=MsgCode.paramsError;
			sb.append("openId为空  ");
		} else {
			registerVo.setOpenId(openId);
		}

		if (StringUtil.isBlank(validateCode)) {
			code=MsgCode.paramsError;
			sb.append("验证码为空  ");
		}

		if (StringUtil.isBlank(driverLicenseIssuedAddress)) {
			code=MsgCode.paramsError;
			sb.append("驾驶证核发地为空  ");
		} else {
			registerVo.setDriverLicenseIssuedAddress(driverLicenseIssuedAddress);
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code=MsgCode.paramsError;
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgNegative)) {
			code=MsgCode.paramsError;
			sb.append("身份证反面为空 ");
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code=MsgCode.paramsError;
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}
		BaseBean basebean = new BaseBean();
		try {

			if (MsgCode.success.equals(code)) {// 参数校验通过

				// 0-验证成功，1-验证失败，2-验证码失效
				int result = accountService.verificatioCode(mobilephone, validateCode);
				if (0 == result) {
					registerVo.setCertifiedType("3"); 
					registerVo.setCallAccount("WX02_TEST");
					registerVo.setCertifiedRole("1");
					registerVo.setCertifiedSource(sourceOfCertification);
					JSONObject json = accountService.haveDriverLicenseNotCar(registerVo);
					code = json.getString("CODE");
					if (!MsgCode.success.equals(code)) {
						code=MsgCode.businessError;
					}
					basebean.setCode(code);
					basebean.setMsg(json.getString("MSG"));
					
					//申请成功，发送微信模板消息
					sendServiceMessage(openId, sourceOfCertification, code, basebean, json, businessType);
					
				}
				if (1 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}
				if (2 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码失效    ");
					basebean.setMsg(sb.toString());
				}

			} else {
				basebean.setCode(code);
				basebean.setMsg(sb.toString());
			}

		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("haveDriverLicenseNotCar出错",e);
		}
		renderJSON(basebean);
		logger.debug(JSON.toJSONString(basebean));
	}

	/**
	 * 星级用户认证-我是行人,非机动车驾驶人
	 * 
	 * @param identityCard
	 *            身份证
	 * @param mobilephone
	 *            手机号码
	 * @param validateCode
	 *            验证码
	 * @param idCardImgPositive
	 *            身份证正面
	 * @param idCardImgNegative
	 *            身份证反面
	 * @param idCardImgHandHeld
	 *            手持身份证
	 * @param openId 
	 * 			   微信openId
	 * url
	 * http://localhost:8080/web/register/isPedestrianNotDriver.html?identityCard=34343434343&mobilephone=13652311206&validateCode=402765&idCardImgPositive=1111111111&idCardImgNegative=2222&idCardImgHandHeld=3333&openId=222222
	 */
	@RequestMapping(value = "isPedestrianNotDriver")
	public void isPedestrianNotDriver(String identityCard, String mobilephone, String validateCode,
			String idCardImgPositive, String idCardImgNegative, String idCardImgHandHeld,String openId,
			String sourceOfCertification, String businessType) {
		String code = MsgCode.success;
		StringBuffer sb = new StringBuffer("");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(identityCard)) {
			code=MsgCode.paramsError;
			sb.append("身份证为空  ");
		} else {
			registerVo.setUserIdCard(identityCard);
		}

		if (StringUtil.isBlank(mobilephone)) {
			code=MsgCode.paramsError;
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (StringUtil.isBlank(openId)) {
			code=MsgCode.paramsError;
			sb.append("openId为空  ");
		} else {
			registerVo.setOpenId(openId);
		}
		
		if (StringUtil.isBlank(validateCode)) {
			code=MsgCode.paramsError;
			sb.append("验证码为空  ");
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code=MsgCode.paramsError;
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgNegative)) {
			code=MsgCode.paramsError;
			sb.append("身份证反面为空 ");
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code=MsgCode.paramsError;
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}
		BaseBean basebean = new BaseBean();
		try {
			if (MsgCode.success.equals(code)) {

				// 0-验证成功，1-验证失败，2-验证码失效
				int result = accountService.verificatioCode(mobilephone, validateCode);
				if (0 == result) {
					registerVo.setCertifiedSource(sourceOfCertification);
					registerVo.setCertifiedType("4");
					JSONObject json = accountService.isPedestrianNotDriver(registerVo);
					code = json.getString("CODE");
					if (!MsgCode.success.equals(code)) {
						code=MsgCode.businessError;
					}
					basebean.setCode(code);
					basebean.setMsg(json.getString("MSG"));
					
					//申请成功，发送微信模板消息
					sendServiceMessage(openId, sourceOfCertification, code, basebean, json, businessType);
					
				}
				if (1 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}
				if (2 == result) {
					code=MsgCode.paramsError;
					sb.append("验证码失效    ");
					basebean.setMsg(sb.toString());
				}

			} else {
				basebean.setCode(code);
				basebean.setMsg(sb.toString());
			}

		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("isPedestrianNotDriver出错",e);
		}
		renderJSON(basebean);
		logger.debug(JSON.toJSONString(basebean));
	}

	/**
	 * 发送模板消息
	 * @param openId
	 * @param sourceOfCertification
	 * @param code
	 * @param basebean
	 * @param json
	 */
	public void sendServiceMessage(String openId, String sourceOfCertification, String code, BaseBean basebean,
			JSONObject json, String businessType) {
		if(StringUtil.isNotBlank(businessType) && MsgCode.success.equals(code) && "C".equals(sourceOfCertification)){
			String msg = json.getString("MSG");
			//String waterNumber = msg.substring(msg.indexOf("：")+1, msg.indexOf("。"));//截取流水号
			String waterNumber = "";
			waterNumber = SXStringUtils.deleteChineseCharactertoString(msg);
			waterNumber = waterNumber.replace(",", "").replace(":", "").replace("。", "");
			logger.info("【星级用户认证】截取后的流水号：" + waterNumber);
			String dateStr = DateUtil2.date2str(new Date());
			String url = templateMessageService.getTemplateSendUrl() + "type=1&title=starUserAuth&waterNumber="+waterNumber+"&bidDate="+dateStr;
			logger.info("【星级用户认证】返回的url是：" + url);
			
			MessageChannelModel model = new MessageChannelModel();
			model.setOpenid(openId);
			if("1".equals(businessType)){//1-驾驶证业务入口
				model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbn3H9wpHz8dKjXPL9J_xC5s");
			}else if("2".equals(businessType)){//2-机动车业务入口
				model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbjAEGcUfJBYRRfOgme0SPuk");
			}else if("3".equals(businessType)){//3-交通违法办理入口
				model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbhcd1wTBLI3B0xsiP7KVObo");
			}else if("4".equals(businessType)){//4-随手拍举报入口
				model.setBiz_template_id("z0HL_wBSsoF7AR42tGzuFMalEi3RKowXmg9nKNTC0BM");
			}else{
				logger.info("业务类型businessType="+businessType);
			}
			model.setResult_page_style_id("23ClyLHM5Fr790uz7t-fxiodPnL9ohRzcnlGWEudkL8");
			model.setDeal_msg_style_id("23ClyLHM5Fr790uz7t-fxlzJePTelFGvOKtKR4udm1o");
			model.setCard_style_id("");
			model.setOrder_no(waterNumber);
			model.setUrl(url);
			Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
			map.put("first", new MessageChannelModel().new Property("您好，您的业务办理申请已成功提交，具体信息如下。","#212121"));
			map.put("keyword1", new MessageChannelModel().new Property(dateStr,"#212121"));
			map.put("keyword2", new MessageChannelModel().new Property("星级用户认证","#212121"));
			map.put("keyword3", new MessageChannelModel().new Property("待初审","#212121"));
			map.put("remark", new MessageChannelModel().new Property("","#212121"));
			model.setData(map);
			
			BaseBean msgBean = templateMessageService.sendServiceMessage(model);
			logger.info("【星级用户认证】发送模板消息结果：" + JSON.toJSONString(msgBean));
			
			//发送成功
			if("0".equals(msgBean.getCode())){
				basebean.setData(msgBean.getData().toString());//结果评价页url设置在data中
			}
		}
	}
}
