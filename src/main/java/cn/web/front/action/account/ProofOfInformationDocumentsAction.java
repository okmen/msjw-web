package cn.web.front.action.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import cn.convenience.bean.MsjwApplyingBusinessVo;
import cn.convenience.service.IMsjwService;
import cn.handle.bean.vo.HandleTemplateVo;
import cn.handle.service.IHandleService;
import cn.sdk.bean.BaseBean;
import cn.sdk.bean.BusinessType;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
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
	
	@Autowired
    @Qualifier("handleService")
    private IHandleService handleService;
	@Autowired
	@Qualifier("msjwService")
	private IMsjwService msjwService;
	
	/**
	 * 获取  机动车信息单 
	 * @param identityCard 身份证
	 * @param sourceOfCertification 认证来源
	 * http://localhost:8080/web/user/search/getMotorVehicleInformationSheet.html?identityCard=622822198502074110&sourceOfCertification=C
	 */
	@RequestMapping(value="getMotorVehicleInformationSheet")
	public void getMotorVehicleInformationSheet(String identityCard,String sourceOfCertification){
		BaseBean baseBean = new BaseBean();
		try {
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
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
			baseBean.setCode(MsgCode.success);
	    	baseBean.setMsg("");
			MotorVehicleInformationSheetVo motorVehicleInformationSheetVo = new MotorVehicleInformationSheetVo();
			motorVehicleInformationSheetVo = accountService.getMotorVehicleInformationSheet(identityCard, sourceOfCertification);
			baseBean.setData(motorVehicleInformationSheetVo);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("getMotorVehicleInformationSheet 错误", e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 获取	驾驶证信息单/无车证明/驾驶人安全事故信用表
	 * @param identityCard 身份证
	 * @param mobilephone 认证来源
	 * http://localhost:8080/web/user/search/getDriverLicenseInformationSheet.html?identityCard=622822198502074110&sourceOfCertification=C
	 */
	@RequestMapping(value="getDriverLicenseInformationSheet")
	public void getDriverLicenseInformationSheet(String identityCard,String sourceOfCertification){
		BaseBean baseBean = new BaseBean();
		try {
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
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
			baseBean.setCode(MsgCode.success);
	    	baseBean.setMsg("");
			DriverLicenseInformationSheetVo driverLicenseInformationSheetVo = new DriverLicenseInformationSheetVo();
			AuthenticationBasicInformationVo authenticationBasicInformationVo = accountService.authenticationBasicInformationQuery(identityCard, sourceOfCertification);
			baseBean.setData(authenticationBasicInformationVo);
	    	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("getDriverLicenseInformationSheet 错误", e);
		}
		logger.debug(JSON.toJSONString(baseBean));
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
    public void commitDriverLicenseInformationSheet(String userName,String identityCard,String mobilephone,
    		String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(userName)){
        		baseBean.setMsg("userName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
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
			baseBean.setData("");
	    	String applyType = "1";
    		Map<String, String> map = accountService.commitDriverLicenseInformationSheet(applyType,userName, identityCard, mobilephone, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("commitDriverLicenseInformationSheet 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
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
    public void commitCertificateOfAbsence(String userName,String identityCard,String mobilephone,
    		String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setData("");
    	try {
    		if(StringUtils.isBlank(userName)){
        		baseBean.setMsg("userName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
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
			
    		String applyType = "3";
    		Map<String, String> map = accountService.commitDriverLicenseInformationSheet(applyType,userName, identityCard, mobilephone, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("commitCertificateOfAbsence",e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
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
    public void commitDriverSafetyAccident(String userName,String identityCard,String mobilephone,
    		String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setData("");
    	try {
    		if(StringUtils.isBlank(userName)){
        		baseBean.setMsg("userName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
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
			
    		String applyType = "4";
    		Map<String, String> map = accountService.commitDriverLicenseInformationSheet(applyType,userName, identityCard, mobilephone, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("commitDriverSafetyAccident",e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
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
    public void commitMotorVehicleInformationSheet(String userName,String identityCard,String mobilephone,
    		String provinceAbbreviation,String sourceOfCertification,String numberPlateNumber,String plateType,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setData("");
    	try {
    		if(StringUtils.isBlank(userName)){
        		baseBean.setMsg("userName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
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
			
    		Map<String, String> map = accountService.commitMotorVehicleInformationSheet(userName, identityCard, mobilephone, provinceAbbreviation, numberPlateNumber, plateType, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("commitMotorVehicleInformationSheet",e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
	
    
    /**
	 * 提交机动车信息单打印申请
	  *@param applyType 申请类型（1代表驾驶人信息单；2代表机动车信息单 3代表无车证明申请；4代表驾驶人安全事故信用表）
	 * @param userName 申请人姓名（必须是星级用户姓名）
	 * @param idnetityCard 申请人身份证号码（必须是星级用户身份证号码）
	 * @param mobilephone 申请人联系电话（必须是星级用户联系电话）
	 * @param licensePlateNumber 号牌号码 
     * @param plateType 号牌种类 例如 02-蓝牌
     * @param sourceOfCertification 申请来源（APP 传A，微信传C，支付宝传Z）
	 * @param request
	 * @param response
	 * @throws Exception 
	 * http://localhost:8080/web/user/proofOfInformationDocuments/submitApplicationForMotorVehicleInformation.html?applyName=张宇帆&identityCard=445222199209020034&applyPhone=15920050177&licensePlateNumber=粤B6A42E&plateType=02&sourceOfCertification=C
	 */
    @RequestMapping(value="submitApplicationForMotorVehicleInformation")
    public void submitApplicationForMotorVehicleInformation(String applyName,String identityCard,String applyPhone,
    		String licensePlateNumber,String plateType,String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(applyName)){
        		baseBean.setMsg("applyName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(applyPhone)){
        		baseBean.setMsg("applyPhone 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			
			if(StringUtils.isBlank(licensePlateNumber)){
        		baseBean.setMsg("licensePlateNumber 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(plateType)){
        		baseBean.setMsg("plateType 不能为空!");
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
			String openId=request.getParameter("openId");
			if(StringUtils.isBlank(openId)){
        		baseBean.setMsg("openId 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			baseBean.setData("");
	    	String applyType = "2";
    		Map<String, String> map = accountService.submitApplicationForMotorVehicleInformation(applyType,applyName, identityCard, applyPhone,licensePlateNumber,plateType, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
            	//新增到民生警务平台个人中心
            	if("0000".equals(code)){
            		//流水号
            		String cid=map.get("cid");
					try {
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.applyCarTemporaryLicence, map.get("number"), DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getMsjwTemplateSendUrl());
						
						MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
						businessVo.setTylsbh(cid);
						businessVo.setOpenid(openId);
						businessVo.setEventname("机动车信息单打印申请");
						businessVo.setApplyingUrlWx(url);//微信在办跳转地址
						businessVo.setJinduUrlWx(url);//进度查询跳转地址
						msjwService.addApplyingBusiness(businessVo);
					} catch (Exception e) {
						logger.error("【信息单据-机动车信息单打印申请】", e);
						e.printStackTrace();
					}
            	}
            	
        	}
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("submitApplicationForMotorVehicleInformation 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    
    /**
	 * 提交驾驶人信息单打印申请
	  *@param applyType 申请类型（1代表驾驶人信息单；2代表机动车信息单 3代表无车证明申请；4代表驾驶人安全事故信用表）
	 * @param userName 申请人姓名（必须是星级用户姓名）
	 * @param idnetityCard 申请人身份证号码（必须是星级用户身份证号码）
	 * @param mobilephone 申请人联系电话（必须是星级用户联系电话）
	 * @param sourceOfCertification 申请来源（APP 传A，微信传C，支付宝传Z）
	 * @param request
	 * @param response
	 * @throws Exception 
	 * http://localhost:8080/web/user/proofOfInformationDocuments/submitApplicationForDriverInformation.html?applyName=张宇帆&identityCard=445222199209020034&applyPhone=15920050177&sourceOfCertification=C
	 */
    @RequestMapping(value="submitApplicationForDriverInformation")
    public void submitApplicationForDriverInformation(String applyName,String identityCard,String applyPhone,
    		String sourceOfCertification,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(applyName)){
        		baseBean.setMsg("applyName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(applyPhone)){
        		baseBean.setMsg("applyPhone 不能为空!");
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
			baseBean.setData("");
	    	String applyType = "1";
    		Map<String, String> map = accountService.submitApplicationForDriverInformation(applyType,applyName, identityCard, applyPhone, sourceOfCertification);
        	if(null != map){
        		String code = map.get("code");
        		String msg = map.get("msg");
        		baseBean.setCode(code);
            	baseBean.setMsg(msg);
        	}
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("submitApplicationForDriverInformation 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
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
