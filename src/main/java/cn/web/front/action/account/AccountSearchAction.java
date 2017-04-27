package cn.web.front.action.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DriverLicenseInformationSheetVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.InformationSheetVo;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.bean.vo.MyBusinessVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.service.IAccountService;
import cn.illegal.bean.IllegalInfoBean;
import cn.illegal.service.IIllegalService;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.WebServiceException;
import cn.sdk.util.MsgCode;
import cn.web.front.support.BaseAction;
/**
 * 个人中心查询
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value="/user/search")
@SuppressWarnings(value="all")
public class AccountSearchAction extends BaseAction {
	@Autowired
    @Qualifier("accountService")
    private IAccountService accountService;
	
	@Autowired
    @Qualifier("illegalService")
	private IIllegalService illegalService;
	/**
	 * 查询我的证明(机动车信息单、驾驶人信息单、无车证明、驾驶人安全事故信用表) 进度查询4个公用一个接口，传一个类型
	 * @param identityCard 身份证
	 * @param sourceOfCertification 认证来源
	 * @param applyType 
	 * http://localhost:8080/web/user/search/queryMachineInformationSheet.html?identityCard=440301199002101119&sourceOfCertification=C&applyType=1
	 * @throws Exception 
	 */
	@RequestMapping(value="queryMachineInformationSheet")
	public void queryMachineInformationSheet(String identityCard,String sourceOfCertification,String applyType) throws Exception{
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
			if(StringUtils.isBlank(applyType)){
        		baseBean.setMsg("applyType 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			List<InformationSheetVo> informationSheetVos = new ArrayList<InformationSheetVo>();
			Map<String, Object> map = accountService.queryMachineInformationSheet(applyType,identityCard,sourceOfCertification);
			String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if("0000".equals(code)){
				informationSheetVos = (List<InformationSheetVo>) map.get("data");
				baseBean.setData(informationSheetVos);
				baseBean.setCode(MsgCode.success);
		    	baseBean.setMsg("");
			}else{
				baseBean.setData("");
				baseBean.setCode(MsgCode.businessError);
		    	baseBean.setMsg(msg);
			}
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("getMyDriverLicense 错误!", e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	
	/**
	 * 获取  机动车信息单 
	 * @param identityCard 身份证
	 * @param sourceOfCertification 认证来源
	 * http://localhost:8080/web/user/search/getMotorVehicleInformationSheet.html?identityCard=440301199002101119&sourceOfCertification=C
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
        	logger.error("getMotorVehicleInformationSheet 错误!", e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 获取	驾驶证信息单/无车证明/驾驶人安全事故信用表
	 * @param identityCard 身份证
	 * @param mobilephone 认证来源
	 * http://localhost:8080/web/user/search/getDriverLicenseInformationSheet.html?identityCard=440301199002101119&sourceOfCertification=C
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
        	logger.error("getDriverLicenseInformationSheet 错误!", e);
		}
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 电子驾驶证
	 * @param driverLicenseNumber
	 * @param userName
	 * @param mobileNumber
	 * @param request
	 * @param response
	 * @throws Exception
	 * http://localhost:8080/web/user/search/getElectronicDriverLicense.html?driverLicenseNumber=440301199002101119&userName=王玉璞&mobileNumber=15920071829
	 * http://localhost:8080/web/user/search/getElectronicDriverLicense.html?driverLicenseNumber=440301199002101119&userName=杨明畅&mobileNumber=18603017278
	 */
    @RequestMapping(value="getElectronicDriverLicense")
    public void getElectronicDriverLicense(String driverLicenseNumber,String userName,String mobileNumber,String sourceOfCertification,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(driverLicenseNumber)){
        		baseBean.setMsg("driverLicenseNumber 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(userName)){
        		baseBean.setMsg("userName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(mobileNumber)){
        		baseBean.setMsg("mobileNumber 不能为空!");
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
        	ElectronicDriverLicenseVo electronicDriverLicenseVo = accountService.getElectronicDriverLicense(driverLicenseNumber, userName, mobileNumber,sourceOfCertification);
        	if(StringUtils.isBlank(electronicDriverLicenseVo.getElectronicDriverLicense())){
        		baseBean.setMsg("未查询到该用户的星级用户身份认证信息");
        	}
        	baseBean.setData(electronicDriverLicenseVo);
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
    		logger.error("getElectronicDriverLicense 错误!", e);
    		renderJSON(baseBean);
		}
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    
	/**
     * 电子行驶证
     * @param businessType
     * @param businessStatus
     * @param identityCard
     * @param request
     * @param response
	 * @throws Exception
	 * getDrivingLicense
	 * url
	 * http://localhost:8080/web/user/search/getDrivingLicense.html?numberPlatenumber=粤B701NR&plateType=02&mobileNumber=18603017278
	 * 
	 * 
     */
    @RequestMapping(value="getDrivingLicense")
    public void getDrivingLicense(String numberPlatenumber,String plateType,String mobileNumber,String sourceOfCertification,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(numberPlatenumber)){
        		baseBean.setMsg("numberPlatenumber 不能为空!");
        		baseBean.setCode(MsgCode.businessError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(plateType)){
        		baseBean.setMsg("plateType 不能为空!");
        		baseBean.setCode(MsgCode.businessError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(mobileNumber)){
        		baseBean.setMsg("mobileNumber 不能为空!");
        		baseBean.setCode(MsgCode.businessError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.businessError);
        		renderJSON(baseBean);
        		return;
        	}
    		baseBean.setCode(MsgCode.success);
        	baseBean.setMsg("");
        	DrivingLicenseVo drivingLicenseVo = accountService.getDrivingLicense(numberPlatenumber, plateType, mobileNumber, sourceOfCertification);
        	if(StringUtils.isBlank(drivingLicenseVo.getElectronicDrivingLicense())){
        		baseBean.setMsg("未查询到该用户的星级用户身份认证信息");
        	}
        	baseBean.setData(drivingLicenseVo);
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("getDrivingLicense 错误!", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
     * 我的驾驶证
     * @param businessType
     * @param businessStatus
     * @param identityCard
     * @param request
     * @param response
	 * @throws Exception
	 * http://localhost:8080/web/user/search/getMyDriverLicense.html?identityCard=440301199002101119
     */
    @RequestMapping(value="getMyDriverLicense")
    public void getMyDriverLicense(String identityCard,HttpServletRequest request,HttpServletResponse response,String sourceOfCertification) throws Exception{
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
        	MyDriverLicenseVo myDriverLicenseVo = accountService.getMyDriverLicense(identityCard, sourceOfCertification);
        	baseBean.setData(myDriverLicenseVo);
        	baseBean.setMsg("");
		} catch (Exception e) {
			DealException(baseBean,e);
		}
    	logger.debug(JSON.toJSONString(baseBean));
    	renderJSON(baseBean);
    }
	/**
     * 查询已绑车辆
     * @param businessType
     * @param businessStatus
     * @param identityCard
     * @param request
     * @param response
	 * @throws Exception
	 * http://localhost:8080/web/user/search/getBndTheVehicles.html?identityCard=440301199002101119&mobilephone=15920071829
     */
    @RequestMapping(value="getBndTheVehicles")
    public void getBndTheVehicles(String identityCard,String mobilephone,String sourceOfCertification,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
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
    		baseBean.setCode(MsgCode.success);
        	baseBean.setMsg("");
        	List<BindTheVehicleVo> bindTheVehicleVos =  accountService.getBndTheVehicles(identityCard, mobilephone, sourceOfCertification);
        	for(BindTheVehicleVo bindTheVehicleVo : bindTheVehicleVos){
        		String numberPlateNumber = bindTheVehicleVo.getNumberPlateNumber();
        		String plateType = bindTheVehicleVo.getPlateType();
        		//车牌号、车牌类型、车架后4位
        		List<IllegalInfoBean> illegalInfoBeans = illegalService.queryInfoByLicensePlateNo(numberPlateNumber, plateType, "");
        		if(null != illegalInfoBeans && illegalInfoBeans.size() > 0){
        			//bindTheVehicleVo.setIllegalNumber("当前本车有" + illegalInfoBeans.size() + "宗违法尚未处理");
        			bindTheVehicleVo.setIllegalNumber(String.valueOf(illegalInfoBeans.size()));
        		}else{
        			//bindTheVehicleVo.setIllegalNumber("当前本车有0宗违法尚未处理");
        			bindTheVehicleVo.setIllegalNumber("0");
        		}
        	}
        	baseBean.setData(bindTheVehicleVos);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("getBndTheVehicles 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    /**
	 * 我的业务(切换查询)
	 * @param loginName
	 * @param password
	 * @param request
	 * @param response
     * @throws Exception
     * http://192.168.1.161:8080/web/user/search/getMyBusiness.html?identityCard=440301199002101119&businessType=0&businessStatus=0&sourceOfCertification=C
	 */
    @RequestMapping(value="getMyBusiness")
    public void getMyBusiness(Integer businessType,Integer businessStatus,String identityCard,String sourceOfCertification,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(null == businessType){
        		baseBean.setMsg("businessType 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(null == businessStatus){
        		baseBean.setMsg("businessStatus 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(null == identityCard){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(null == sourceOfCertification){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		baseBean.setCode(MsgCode.success);
        	baseBean.setMsg("");
        	List<MyBusinessVo> myBusinessVos = accountService.getMyBusiness(businessType, businessStatus, identityCard, sourceOfCertification);
        	baseBean.setData(myBusinessVos);
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("getMyBusiness 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
}
