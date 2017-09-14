package cn.web.front.action.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.vo.RegisterVo;
import cn.account.service.IAccountService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.Base64;
import cn.sdk.util.MsgCode;
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
			String idCardImgHandHeld,String openId,String sourceOfCertification) {
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
			String idCardImgNegative, String ownerIdCardImgPositive, String ownerIdCardImgHandHeld,String openId,String sourceOfCertification) {
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
			String idCardImgHandHeld,String openId,String sourceOfCertification) {

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
			String idCardImgPositive, String idCardImgNegative, String idCardImgHandHeld,String openId,String sourceOfCertification) {
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

}
