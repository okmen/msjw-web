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
	 * @param licensePlateType
	 *            车牌类型
	 * @param provinceAbbreviation
	 *            省简称
	 * @param licensePlateNumber
	 *            车牌号码
	 * @param identityCard
	 *            身份证
	 * @param linkAddress
	 *            联系地址
	 * @param mobilephone
	 *            手机号码
	 * @param validateCode
	 *            验证码
	 * @param isDriverLicense
	 *            是否有驾驶证
	 * @param driverLicenseIssuedAddress
	 *            驾驶证核发地
	 * @param idCardImgPositive
	 *            身份证正面
	 * @param idCardImgNegative
	 *            身份证反面
	 * @param idCardImgHandHeld
	 *            手持身份证
	 */
	@RequestMapping(value = "iAmTheOwner", method = RequestMethod.POST)
	public void iAmTheOwner(String licensePlateType, String provinceAbbreviation, String licensePlateNumber,
			String identityCard, String linkAddress, String mobilephone, String validateCode, int isDriverLicense,
			String driverLicenseIssuedAddress, String idCardImgPositive, String idCardImgNegative,
			String idCardImgHandHeld) {
		String code = "0000";
		StringBuffer sb = new StringBuffer("下列参数有问题：");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(identityCard)) {
			code = "500";
			sb.append("身份证为空  ");
		} else {
			registerVo.setUserIdCard(identityCard);
		}
		if (StringUtil.isBlank(linkAddress)) {
			code = "500";
			sb.append("联系地址为空  ");
		} else {
			registerVo.setLinkAddress(linkAddress);
		}

		if (StringUtil.isBlank(mobilephone)) {
			code = "500";
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (isDriverLicense < 0) {
			code = "500";
			sb.append("是否有驾驶证错误  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (StringUtil.isBlank(validateCode)) {
			code = "500";
			sb.append("验证码为空  ");
		}

		if (StringUtil.isBlank(driverLicenseIssuedAddress)) {
			code = "500";
			sb.append("驾驶证核发地为空  ");
		} else {
			registerVo.setDriverLicenseIssuedAddress(driverLicenseIssuedAddress);
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code = "500";
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgNegative)) {
			code = "500";
			sb.append("身份证反面为空 ");
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code = "500";
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}

		if (StringUtil.isBlank(licensePlateType)) {
			code = "500";
			sb.append("车牌类型为空  ");
		} else {
			registerVo.setLicensePlateType(licensePlateType);
		}

		if (StringUtil.isBlank(provinceAbbreviation)) {
			code = "500";
			sb.append("省简称为空  ");
		} else {
			registerVo.setProvinceAbbreviation(provinceAbbreviation);
		}

		if (StringUtil.isBlank(licensePlateNumber)) {
			code = "500";
			sb.append("车牌号码为空  ");
		} else {
			registerVo.setLicensePlateNumber(licensePlateNumber);
		}

		BaseBean basebean = new BaseBean();
		try {
			if ("0000".equals(code)) {// 参数校验通过
				registerVo.setCallAccount("WX02_TEST");
				registerVo.setCertifiedType("1");
				registerVo.setCertifiedRole("1");
				// registerVo
				JSONObject json = accountService.iAmTheOwner(registerVo);
				System.out.println(json);
				code = json.getString("CODE");
				if (!"0000".equals(code)) {
					code = "500";
				}
				basebean.setCode(code);
				basebean.setMsg(json.getString("MSG"));
			} else {
				basebean.setMsg(sb.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJSON(basebean);
		logger.info(JSON.toJSONString(basebean));

	}

	/**
	 * 星级用户认证-我是长期使用人
	 * 
	 * @param licensePlateType
	 *            车牌类型
	 * @param provinceAbbreviation
	 *            省简称
	 * @param licensePlateNumber
	 *            车牌号码
	 * @param ownerName
	 *            车主姓名
	 * @param ownerIdCard
	 *            车主身份证
	 * @param userIdCard
	 *            使用人身份证
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
	 */
	@RequestMapping(value = "iamALongtimeUser", method = RequestMethod.POST)
	public void iamALongtimeUser(String licensePlateType, String provinceAbbreviation, String licensePlateNumber,
			String ownerName, String ownerIdCard, String userIdCard, String linkAddress, String mobilephone,
			String validateCode, String driverLicenseIssuedAddress, String idCardImgPositive, String idCardImgHandHeld,
			String idCardImgNegative, String ownerIdCardImgPositive, String ownerIdCardImgHandHeld) {
		String code = "0000";
		StringBuffer sb = new StringBuffer("下列参数有问题：");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(userIdCard)) {
			code = "500";
			sb.append("使用人身份证为空  ");
		} else {
			registerVo.setUserIdCard(userIdCard);
		}
		if (StringUtil.isBlank(ownerIdCard)) {
			code = "500";
			sb.append("车主身份证为空  ");
		} else {
			registerVo.setOwnerIdCard(ownerIdCard);
		}

		if (StringUtil.isBlank(linkAddress)) {
			code = "500";
			sb.append("联系地址为空  ");
		} else {
			registerVo.setLinkAddress(linkAddress);
		}

		if (StringUtil.isBlank(mobilephone)) {
			code = "500";
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (StringUtil.isBlank(validateCode)) {
			code = "500";
			sb.append("验证码为空  ");
		}

		if (StringUtil.isBlank(driverLicenseIssuedAddress)) {
			code = "500";
			sb.append("驾驶证核发地为空  ");
		} else {
			registerVo.setDriverLicenseIssuedAddress(driverLicenseIssuedAddress);
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code = "500";
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgNegative)) {
			code = "500";
			sb.append("身份证反面为空 ");
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code = "500";
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}

		if (StringUtil.isBlank(licensePlateType)) {
			code = "500";
			sb.append("车牌类型为空  ");
		} else {
			registerVo.setLicensePlateType(licensePlateType);
		}

		if (StringUtil.isBlank(provinceAbbreviation)) {
			code = "500";
			sb.append("省简称为空  ");
		} else {
			registerVo.setProvinceAbbreviation(provinceAbbreviation);
		}

		if (StringUtil.isBlank(licensePlateNumber)) {
			code = "500";
			sb.append("车牌号码为空  ");
		} else {
			registerVo.setLicensePlateNumber(licensePlateNumber);
		}

		if (StringUtil.isBlank(ownerName)) {
			code = "500";
			sb.append("车主姓名为空  ");
		} else {
			registerVo.setOwnerIdName(ownerName);
			registerVo.setOwnerName(ownerName);
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code = "500";
			sb.append("车主身份证正面为空  ");
		} else {
			registerVo.setOwnerIdCardImgPositive(ownerIdCardImgPositive);
		}

		if (StringUtil.isBlank(ownerIdCardImgHandHeld)) {
			code = "500";
			sb.append("车主手持身份证为空  ");
		} else {
			registerVo.setOwnerIdCardImgHandHeld(ownerIdCardImgHandHeld);
		}

		BaseBean basebean = new BaseBean();
		try {
			if ("0000".equals(code)) {// 参数校验通过

				// 0-验证成功，1-验证失败，2-验证码失效
				int result = accountService.verificatioCode(mobilephone, validateCode);
				if (0 == result) {
					registerVo.setCertifiedType("2");
					registerVo.setCallAccount("WX02_TEST");
					registerVo.setCertifiedRole("1");
					registerVo.setCertifiedSource("C");
					// registerVo.setOwnerMobilephone("13725512400");
					JSONObject json = accountService.iamALongtimeUser(registerVo);
					System.out.println(json);
					code = json.getString("CODE");
					if (!"0000".equals(code)) {
						code = "500";
					}
					basebean.setCode(code);
					basebean.setMsg(json.getString("MSG"));
				}
				if (1 == result) {
					code = "500";
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}
				if (2 == result) {
					code = "500";
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}

			} else {
				basebean.setMsg(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		renderJSON(basebean);
		logger.info(JSON.toJSONString(basebean));

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
	 */
	@RequestMapping(value = "haveDriverLicenseNotCar", method = RequestMethod.POST)
	public void haveDriverLicenseNotCar(String identityCard, String linkAddress, String mobilephone,
			String validateCode, String driverLicenseIssuedAddress, String idCardImgPositive, String idCardImgNegative,
			String idCardImgHandHeld) {

		String code = "0000";
		StringBuffer sb = new StringBuffer("下列参数有问题：");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(identityCard)) {
			code = "500";
			sb.append("身份证为空  ");
		} else {
			registerVo.setUserIdCard(identityCard);
		}

		if (StringUtil.isBlank(linkAddress)) {
			code = "500";
			sb.append("联系地址为空  ");
		} else {
			registerVo.setLinkAddress(linkAddress);
		}

		if (StringUtil.isBlank(mobilephone)) {
			code = "500";
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (StringUtil.isBlank(validateCode)) {
			code = "500";
			sb.append("验证码为空  ");
		}

		if (StringUtil.isBlank(driverLicenseIssuedAddress)) {
			code = "500";
			sb.append("驾驶证核发地为空  ");
		} else {
			registerVo.setDriverLicenseIssuedAddress(driverLicenseIssuedAddress);
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code = "500";
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgNegative)) {
			code = "500";
			sb.append("身份证反面为空 ");
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code = "500";
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}

		System.out.println(sb.toString());

		BaseBean basebean = new BaseBean();
		try {

			if ("0000".equals(code)) {// 参数校验通过

				// 0-验证成功，1-验证失败，2-验证码失效
				int result = accountService.verificatioCode(mobilephone, validateCode);
				if (0 == result) {
					registerVo.setCertifiedType("3");
					registerVo.setCallAccount("WX02_TEST");
					registerVo.setCertifiedRole("1");
					registerVo.setCertifiedSource("C");
					JSONObject json = accountService.haveDriverLicenseNotCar(registerVo);
					System.out.println(json);
					code = json.getString("CODE");
					if (!"0000".equals(code)) {
						code = "500";
					}
					basebean.setCode(code);
					basebean.setMsg(json.getString("MSG"));
				}
				if (1 == result) {
					code = "500";
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}
				if (2 == result) {
					code = "500";
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}

			} else {
				basebean.setMsg(sb.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		renderJSON(basebean);
		logger.info(JSON.toJSONString(basebean));

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
	 */
	@RequestMapping(value = "isPedestrianNotDriver", method = RequestMethod.POST)
	public void isPedestrianNotDriver(String identityCard, String mobilephone, String validateCode,
			String idCardImgPositive, String idCardImgNegative, String idCardImgHandHeld) {
		String code = "0000";
		StringBuffer sb = new StringBuffer("下列参数有问题：");
		RegisterVo registerVo = new RegisterVo();
		if (StringUtil.isBlank(identityCard)) {
			code = "500";
			sb.append("身份证为空  ");
		} else {
			registerVo.setUserIdCard(identityCard);
		}

		if (StringUtil.isBlank(mobilephone)) {
			code = "500";
			sb.append("手机号码为空  ");
		} else {
			registerVo.setMobilephone(mobilephone);
		}

		if (StringUtil.isBlank(validateCode)) {
			code = "500";
			sb.append("验证码为空  ");
		}

		if (StringUtil.isBlank(idCardImgPositive)) {
			code = "500";
			sb.append("身份证正面为空  ");
		} else {
			registerVo.setIdCardImgPositive(idCardImgPositive);
		}

		if (StringUtil.isBlank(idCardImgNegative)) {
			code = "500";
			sb.append("身份证反面为空 ");
		}

		if (StringUtil.isBlank(idCardImgHandHeld)) {
			code = "500";
			sb.append("手持身份证为空  ");
		} else {
			registerVo.setIdCardImgHandHeld(idCardImgHandHeld);
		}

		System.out.println(sb.toString());
		BaseBean basebean = new BaseBean();
		try {
			if ("0000".equals(code)) {

				// 0-验证成功，1-验证失败，2-验证码失效
				int result = accountService.verificatioCode(mobilephone, validateCode);
				if (0 == result) {
					registerVo.setCertifiedSource("C");
					registerVo.setCertifiedType("4");
					JSONObject json = accountService.isPedestrianNotDriver(registerVo);
					System.out.println(json);
					code = json.getString("CODE");
					if (!"0000".equals(code)) {
						code = "500";
					}
					basebean.setCode(code);
					basebean.setMsg(json.getString("MSG"));
				}
				if (1 == result) {
					code = "500";
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}
				if (2 == result) {
					code = "500";
					sb.append("验证码错误    ");
					basebean.setMsg(sb.toString());
				}

			} else {
				basebean.setMsg(sb.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(basebean.getMsg());
		renderJSON(basebean);
		logger.info(JSON.toJSONString(basebean));

	}

}
