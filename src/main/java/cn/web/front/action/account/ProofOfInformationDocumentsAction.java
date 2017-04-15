package cn.web.front.action.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;

import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.DriverLicenseInformationSheetVo;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.service.IAccountService;
import cn.sdk.bean.BaseBean;
import cn.web.front.support.BaseAction;

/**
 * 信息单据证明
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value="/user/proofOfInformationDocuments")
@SuppressWarnings(value="all")
public class ProofOfInformationDocumentsAction extends BaseAction{
	
	@Autowired
    @Qualifier("accountService")
    private IAccountService accountService;
	
	/**
	 * 获取  机动车信息单 
	 * @param identityCard 身份证
	 * @param sourceOfCertification 认证来源
	 * http://localhost:8080/web/user/search/getMotorVehicleInformationSheet.html?identityCard=622822198502074110&sourceOfCertification=C
	 */
	@RequestMapping(value="getMotorVehicleInformationSheet",method=RequestMethod.GET)
	public void getMotorVehicleInformationSheet(@RequestParam("identityCard")String identityCard,@RequestParam("sourceOfCertification")String sourceOfCertification){
		BaseBean baseBean = new BaseBean();
		baseBean.setCode("0000");
    	baseBean.setMsg("");
		try {
			MotorVehicleInformationSheetVo motorVehicleInformationSheetVo = new MotorVehicleInformationSheetVo();
			motorVehicleInformationSheetVo = accountService.getMotorVehicleInformationSheet(identityCard, sourceOfCertification);
			baseBean.setData(motorVehicleInformationSheetVo);
		} catch (Exception e) {
			baseBean.setCode("0009");
        	baseBean.setMsg(e.getMessage());
        	baseBean.setData("");
		}
		renderJSON(baseBean);
		logger.info(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 获取	驾驶证信息单/无车证明/驾驶人安全事故信用表
	 * @param identityCard 身份证
	 * @param mobilephone 认证来源
	 * http://localhost:8080/web/user/search/getDriverLicenseInformationSheet.html?identityCard=622822198502074110&sourceOfCertification=C
	 */
	@RequestMapping(value="getDriverLicenseInformationSheet",method=RequestMethod.GET)
	public void getDriverLicenseInformationSheet(@RequestParam("identityCard")String identityCard,@RequestParam("sourceOfCertification")String sourceOfCertification){
		BaseBean baseBean = new BaseBean();
		baseBean.setCode("0000");
    	baseBean.setMsg("");
		try {
			DriverLicenseInformationSheetVo driverLicenseInformationSheetVo = new DriverLicenseInformationSheetVo();
			sourceOfCertification = "C";
			AuthenticationBasicInformationVo authenticationBasicInformationVo = accountService.authenticationBasicInformationQuery(identityCard, sourceOfCertification);
			baseBean.setData(authenticationBasicInformationVo);
	    	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0009");
        	baseBean.setMsg(e.getMessage());
		}
		logger.info(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 提交驾驶证信息单
	 * @param userName
	 * @param identityCard
	 * @param mobilephone
	 * @param request
	 * @param response
	 * @throws Exception 
	 * http://localhost:8080/web/user/proofOfInformationDocuments/commitDriverLicenseInformationSheet.html?userName=杨明畅&identityCard=440301199002101119&mobilephone=18603017278&sourceOfCertification=C
	 */
    @RequestMapping(value="commitDriverLicenseInformationSheet")
    public void commitDriverLicenseInformationSheet(@RequestParam("userName") String userName,@RequestParam("identityCard") String identityCard,@RequestParam("mobilephone") String mobilephone,
    		String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setData("");
    	String applyType = "1";
    	try {
    		Map<String, String> map = accountService.commitDriverLicenseInformationSheet(applyType,userName, identityCard, mobilephone, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 提交无车证明
	 * @param userName
	 * @param identityCard
	 * @param mobilephone
	 * @param request
	 * @param response
	 * @throws Exception 
	 * http://localhost:8080/web/user/proofOfInformationDocuments/commitCertificateOfAbsence.html?userName=杨明畅&identityCard=440301199002101119&mobilephone=18603017278&sourceOfCertification=C
	 */
    @RequestMapping(value="commitCertificateOfAbsence")
    public void commitCertificateOfAbsence(@RequestParam("userName") String userName,@RequestParam("identityCard") String identityCard,@RequestParam("mobilephone") String mobilephone,
    		String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setData("");
    	try {
    		String applyType = "3";
    		Map<String, String> map = accountService.commitDriverLicenseInformationSheet(applyType,userName, identityCard, mobilephone, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 提交 驾驶人安全事故信用表
	 * @param userName
	 * @param identityCard
	 * @param mobilephone
	 * @param request
	 * @param response
	 * @throws Exception 
	 * http://localhost:8080/web/user/proofOfInformationDocuments/commitDriverSafetyAccident.html?userName=杨明畅&identityCard=440301199002101119&mobilephone=18603017278&sourceOfCertification=C
	 */
    @RequestMapping(value="commitDriverSafetyAccident")
    public void commitDriverSafetyAccident(@RequestParam("userName") String userName,@RequestParam("identityCard") String identityCard,@RequestParam("mobilephone") String mobilephone,
    		String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setData("");
    	try {
    		String applyType = "4";
    		Map<String, String> map = accountService.commitDriverLicenseInformationSheet(applyType,userName, identityCard, mobilephone, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    /**
     * 提交机动车信息单
     * @param userName 姓名
     * @param identityCard 身份证号
     * @param mobilephone 联系电话
     * @param provinceAbbreviation 省份简称 例如粤
     * @param sourceOfCertification 认证来源 微信-C
     * @param numberPlateNumber 号牌号码 
     * @param plateType 号牌种类 例如 02-蓝牌
     * @param request
     * @param response
     * @throws Exception
     * http://localhost:8080/web/user/proofOfInformationDocuments/commitMotorVehicleInformationSheet.html?userName=杨明畅&identityCard=440301199002101119&mobilephone=18603017278&sourceOfCertification=C&provinceAbbreviation=粤&numberPlateNumber=B701NR&plateType=02
     */
    @RequestMapping(value="commitMotorVehicleInformationSheet")
    public void commitMotorVehicleInformationSheet(@RequestParam("userName") String userName,@RequestParam("identityCard") String identityCard,@RequestParam("mobilephone") String mobilephone,
    		String provinceAbbreviation,String sourceOfCertification,String numberPlateNumber,String plateType,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setData("");
    	try {
    		Map<String, String> map = accountService.commitMotorVehicleInformationSheet(userName, identityCard, mobilephone, provinceAbbreviation, numberPlateNumber, plateType, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
	
	/**
	 * 获取驾驶证信息单
	 * @param identityCard
	 * @param request
	 * @param response
	 * @throws IOException
	 * URL
	 * http://localhost:8080/web/user/proofOfInformationDocuments/getDriverLicenseInformationSheet.html?identityCard=543333339
	 *//*
    @RequestMapping(value="getDriverLicenseInformationSheet",method=RequestMethod.POST)
    public void getDriverLicenseInformationSheet(@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	DriverLicenseInformationSheetVo driverLicenseInformationSheetVo = new DriverLicenseInformationSheetVo();
    	driverLicenseInformationSheetVo.setIdentityCard("431225199111123433");
    	driverLicenseInformationSheetVo.setUserName("张小龙");
    	driverLicenseInformationSheetVo.setMobilephone("13666666666");
    	
    	baseBean.setData(driverLicenseInformationSheetVo);
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
	
	*//**
	 * 获取机动车信息单
	 * @param identityCard
	 * @param request
	 * @param response
	 * @throws IOException
	 * URL
	 * http://localhost:8080/web/user/proofOfInformationDocuments/getMotorVehicleInformationSheet.html?identityCard=543333339
	 *//*
    @RequestMapping(value="getMotorVehicleInformationSheet",method=RequestMethod.POST)
    public void getMotorVehicleInformationSheet(@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	MotorVehicleInformationSheetVo motorVehicleInformationSheetVo = new MotorVehicleInformationSheetVo();
    	motorVehicleInformationSheetVo.setIdentityCard("431225199111123433");
    	motorVehicleInformationSheetVo.setUserName("张小龙");
    	motorVehicleInformationSheetVo.setMobilephone("13666666666");
    	motorVehicleInformationSheetVo.setProvinceAbbreviation("粤");
    	
    	List<String> numberPlateNumbers = new ArrayList<String>();
    	numberPlateNumbers.add("B54349");
    	numberPlateNumbers.add("B33337");
    	
    	List<String> plateTypes = new ArrayList<String>();
    	plateTypes.add("小型轿车");
    	plateTypes.add("小型专用客车");
    	
    	motorVehicleInformationSheetVo.setNumberPlateNumbers(numberPlateNumbers);
    	motorVehicleInformationSheetVo.setPlateTypes(plateTypes);
    	
    	baseBean.setData(motorVehicleInformationSheetVo);
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }*/
    
	/**
     * 提交机动车信息单
     * @param userName 姓名
     * @param identityCard 身份证号
     * @param mobilephone 联系电话 
     * @param provinceAbbreviation 车牌核发省简称 例如：粤
     * @param numberPlateNumber 号牌号码 例如：B701NR
     * @param plateType 车辆类型 例如:小型汽车
     * @param request
     * @param response
     * @throws IOException
     * URL 
     * http://localhost:8080/web/user/proofOfInformationDocuments/commitMotorVehicleInformationSheet.html?userName=张小龙&identityCard=431225199222222222&mobilephone=13666666666&plateType=小型汽车&numberPlateNumber=B701NR&provinceAbbreviation=粤&
     *//*
    @RequestMapping(value="commitMotorVehicleInformationSheet",method=RequestMethod.POST)
    public void commitMotorVehicleInformationSheet(@RequestParam("userName") String userName,@RequestParam("identityCard") String identityCard,
    		@RequestParam("mobilephone") String mobilephone,@RequestParam("provinceAbbreviation") String provinceAbbreviation,
    		@RequestParam("numberPlateNumber") String numberPlateNumber,@RequestParam("plateType") String plateType,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	MotorVehicleInformationSheetVo authenticationBasicInformation = new MotorVehicleInformationSheetVo();
    	
    	baseBean.setData("");
    	
    	renderJSON(baseBean);
    	
    	logger.info(JSON.toJSONString(baseBean));
    }*/
}
