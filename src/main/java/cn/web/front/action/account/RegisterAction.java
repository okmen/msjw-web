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

import cn.account.service.IAccountService;
import cn.sdk.bean.BaseBean;
import cn.web.front.support.BaseAction;

/**
 * 注册中心
 * 
 * @author suntao
 * 
 */
@Controller
@RequestMapping(value="/register/")
@SuppressWarnings(value="all")
public class RegisterAction extends BaseAction{
	 private final static Logger logger = LoggerFactory.getLogger(AccountAction.class);
	
	 
	 @Autowired
	 private IAccountService accountService;
	 
	 /**
	  * 星级用户认证-我是车主
	  * @param licensePlateType 车牌类型
	  * @param provinceAbbreviation 省简称
	  * @param licensePlateNumber 车牌号码
	  * @param identityCard 身份证
	  * @param linkAddress 联系地址
	  * @param mobilephone 手机号码
	  * @param validateCode 验证码
	  * @param isDriverLicense 是否有驾驶证
	  * @param driverLicenseIssuedAddress 驾驶证核发地
	  * @param idCardImgPositive 身份证正面
	  * @param idCardImgNegative 身份证反面
	  * @param idCardImgHandHeld 手持身份证
	  */
	 @RequestMapping(value = "iAmTheOwner",method=RequestMethod.POST)
	    public void iAmTheOwner(@RequestParam("licensePlateType") String licensePlateType,@RequestParam("provinceAbbreviation") String provinceAbbreviation,
	    		@RequestParam("licensePlateNumber") String licensePlateNumber,@RequestParam("identityCard") String identityCard
	    		,@RequestParam("linkAddress") String linkAddress,@RequestParam("mobilephone") String mobilephone
	    		,@RequestParam("validateCode") String validateCode,@RequestParam("isDriverLicense") int isDriverLicense
	    		,@RequestParam("driverLicenseIssuedAddress") String driverLicenseIssuedAddress,@RequestParam("idCardImgPositive") String idCardImgPositive
	    		,@RequestParam("idCardImgNegative") String idCardImgNegative,@RequestParam("idCardImgHandHeld") String idCardImgHandHeld) {
		 try {
			// JSONObject json =	accountService.iAmTheOwner(licensePlateType, provinceAbbreviation, licensePlateNumber, identityCard, linkAddress, mobilephone, driverLicenseIssuedAddress, idCardImgPositive, idCardImgHandHeld);
			// System.out.println(json);
		 } catch (Exception e) {
			e.printStackTrace();
		}
	

		 	BaseBean basebean = new  BaseBean();
	    	basebean.setCode("0000");
	    	basebean.setMsg("");   	
	    	renderJSON(basebean);
	    	logger.info(JSON.toJSONString(basebean));
	    
	    }
	 
	 	/**
	 	 * 星级用户认证-我是长期使用人
	 	 * @param licensePlateType 车牌类型
	 	 * @param provinceAbbreviation 省简称
	 	 * @param licensePlateNumber 车牌号码
	 	 * @param ownerName 车主姓名
	 	 * @param ownerIdCard 车主身份证
	 	 * @param userIdCard 使用人身份证
	 	 * @param linkAddress 联系地址
	 	 * @param mobilephone 手机号码
	 	 * @param validateCode 验证码
	 	 * @param driverLicenseIssuedAddress 驾驶证核发地
	 	 * @param idCardImgPositive 身份证正面
	 	 * @param idCardImgNegative 身份证反面
	 	 * @param idCardImgHandHeld 手持身份证
	 	 */
	 	@RequestMapping(value = "iamALongtimeUser",method = RequestMethod.POST)
	    public void iamALongtimeUser(@RequestParam("licensePlateType") String licensePlateType,@RequestParam("provinceAbbreviation") String provinceAbbreviation,
	    		@RequestParam("licensePlateNumber") String licensePlateNumber,@RequestParam("ownerName") String ownerName
	    		,@RequestParam("ownerIdCard") String ownerIdCard,@RequestParam("userIdCard") String userIdCard
	    		,@RequestParam("linkAddress") String linkAddress,@RequestParam("mobilephone") String mobilephone
	    		,@RequestParam("validateCode") String validateCode,@RequestParam("driverLicenseIssuedAddress") String driverLicenseIssuedAddress
	    		,@RequestParam("idCardImgPositive") String idCardImgPositive,@RequestParam("idCardImgNegative") String idCardImgNegative
	    		,@RequestParam("idCardImgHandHeld") String idCardImgHandHeld) {
	 		 try {
				 JSONObject json =	accountService.iamALongtimeUser(licensePlateType, provinceAbbreviation, licensePlateNumber, ownerName, ownerIdCard, userIdCard, linkAddress, mobilephone, driverLicenseIssuedAddress, idCardImgPositive, idCardImgHandHeld);
				 System.out.println(json);
			 } catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	BaseBean basebean = new  BaseBean();
	    	basebean.setCode("0000");
	    	basebean.setMsg("");   	
	    	renderJSON(basebean);
	    	logger.info(JSON.toJSONString(basebean));
	    
	    }
	 	
	 	
	 	/**
	 	 * 星级用户认证-我有驾驶证,但没有固定车辆
	 	 * @param identityCard 身份证
	 	 * @param linkAddress 联系地址
	 	 * @param mobilephone 手机号码
	 	 * @param validateCode 验证码
	 	 * @param driverLicenseIssuedAddress 驾驶证核发地
	 	 * @param idCardImgPositive 身份证正面
	 	 * @param idCardImgNegative 身份证反面
	 	 * @param idCardImgHandHeld 手持身份证
	 	 */
	 	@RequestMapping(value = "haveDriverLicenseNotCar",method = RequestMethod.POST)
	    public void haveDriverLicenseNotCar(@RequestParam("identityCard") String identityCard,@RequestParam("linkAddress") String linkAddress,
	    		@RequestParam("mobilephone") String mobilephone,@RequestParam("validateCode") String validateCode
	    		,@RequestParam("driverLicenseIssuedAddress") String driverLicenseIssuedAddress,@RequestParam("imgOne") String idCardImgPositive
	    		,@RequestParam("idCardImgNegative") String idCardImgNegative,@RequestParam("idCardImgHandHeld") String idCardImgHandHeld) {
	    	
	    	
	 		 try {
				 JSONObject json =	accountService.haveDriverLicenseNotCar(identityCard, linkAddress, mobilephone, driverLicenseIssuedAddress, idCardImgPositive, idCardImgHandHeld);
				 System.out.println(json);
			 } catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	
	    	BaseBean basebean = new  BaseBean();
	    	basebean.setCode("0000");
	    	basebean.setMsg("");   	
	    	renderJSON(basebean);
	    	logger.info(JSON.toJSONString(basebean));
	    
	    }
	 
	 	
	 	/**
	 	 * 星级用户认证-我是行人,非机动车驾驶人
	 	 * @param identityCard 身份证
	 	 * @param mobilephone 手机号码
	 	 * @param validateCode 验证码
	 	 * @param idCardImgPositive 身份证正面
	 	 * @param idCardImgNegative 身份证反面
	 	 * @param idCardImgHandHeld 手持身份证
	 	 */
	 	@RequestMapping(value = "isPedestrianNotDriver",method = RequestMethod.POST)
	    public void isPedestrianNotDriver(@RequestParam("identityCard") String identityCard,
	    		@RequestParam("mobilephone") String mobilephone,@RequestParam("validateCode") String validateCode
	    		,@RequestParam("idCardImgPositive") String idCardImgPositive,@RequestParam("idCardImgNegative") String idCardImgNegative
	    		,@RequestParam("idCardImgHandHeld") String idCardImgHandHeld) {
	 		 try {
				 JSONObject json =	accountService.isPedestrianNotDriver(identityCard, mobilephone, idCardImgPositive, idCardImgHandHeld);
				 System.out.println(json);
			 } catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	
	    	BaseBean basebean = new  BaseBean();
	    	basebean.setCode("0000");
	    	basebean.setMsg("");   	
	    	renderJSON(basebean);
	    	logger.info(JSON.toJSONString(basebean));
	    
	    }
	 	
	 	
	

}
