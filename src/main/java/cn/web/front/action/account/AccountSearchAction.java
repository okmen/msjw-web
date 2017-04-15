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
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DriverLicenseInformationSheetVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.InformationSheetVo;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.service.IAccountService;
import cn.sdk.bean.BaseBean;
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
	/**
	 * 查询我的证明(机动车信息单、驾驶人信息单、无车证明、驾驶人安全事故信用表) 进度查询4个公用一个接口，传一个类型
	 * @param identityCard 身份证
	 * @param sourceOfCertification 认证来源
	 * @param applyType 
	 * http://localhost:8080/web/user/search/queryMachineInformationSheet.html?identityCard=622822198502074110&sourceOfCertification=C&applyType=1
	 */
	@RequestMapping(value="queryMachineInformationSheet",method=RequestMethod.GET)
	public void queryMachineInformationSheet(@RequestParam("identityCard")String identityCard,@RequestParam("sourceOfCertification")String sourceOfCertification,String applyType){
		BaseBean baseBean = new BaseBean();
		try {
			List<InformationSheetVo> informationSheetVos = new ArrayList<InformationSheetVo>();
			sourceOfCertification = "C";
//			Map<String, Object> map = accountService.queryMachineInformationSheet(applyType,identityCard,sourceOfCertification);
//			String code = (String) map.get("code");
//			String msg = (String) map.get("msg");
//			if("0000".equals(code)){
//				informationSheetVos = (List<InformationSheetVo>) map.get("data");
//				baseBean.setData(informationSheetVos);
//				baseBean.setCode("0000");
//		    	baseBean.setMsg("");
//			}else{
//				baseBean.setData("");
//				baseBean.setCode("0001");
//		    	baseBean.setMsg(msg);
//			}
		} catch (Exception e) {
//			baseBean.setCode("0009");
//        	baseBean.setMsg(e.getMessage());
//        	baseBean.setData("");
		}
		renderJSON(baseBean);
//		logger.info(JSON.toJSONString(baseBean));
	}
	
	
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
			sourceOfCertification = "C";
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
	 * 电子驾驶证
	 * @param driverLicenseNumber
	 * @param userName
	 * @param mobileNumber
	 * @param request
	 * @param response
	 * @throws Exception
	 * http://localhost:8080/web/user/search/getElectronicDriverLicense.html?driverLicenseNumber=622822198502074110&userName=王玉璞&mobileNumber=15920071829
	 * http://localhost:8080/web/user/search/getElectronicDriverLicense.html?driverLicenseNumber=440301199002101119&userName=杨明畅&mobileNumber=18603017278
	 */
    @RequestMapping(value="getElectronicDriverLicense",method=RequestMethod.GET)
    public void getElectronicDriverLicense(@RequestParam("driverLicenseNumber") String driverLicenseNumber,@RequestParam("userName") String userName,@RequestParam("mobileNumber") String mobileNumber,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	ElectronicDriverLicenseVo electronicDriverLicenseVo = accountService.getElectronicDriverLicense(driverLicenseNumber, userName, mobileNumber, "C");
    	
    	baseBean.setData(electronicDriverLicenseVo);
    	
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
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
    @RequestMapping(value="getDrivingLicense",method=RequestMethod.GET)
    public void getDrivingLicense(@RequestParam("numberPlatenumber") String numberPlatenumber,@RequestParam("plateType") String plateType,@RequestParam("mobileNumber") String mobileNumber,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		baseBean.setCode("0000");
        	baseBean.setMsg("");
        	String sourceOfCertification = "C";
        	DrivingLicenseVo drivingLicenseVo = accountService.getDrivingLicense(numberPlatenumber, plateType, mobileNumber, sourceOfCertification);
        	baseBean.setData(drivingLicenseVo);
        	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0009");
        	baseBean.setMsg(e.getMessage());
		}
    	logger.info(JSON.toJSONString(baseBean));
    }
	
	/**
     * 我的驾驶证
     * @param businessType
     * @param businessStatus
     * @param identityCard
     * @param request
     * @param response
	 * @throws Exception
	 * http://localhost:8080/web/user/search/getMyDriverLicense.html?identityCard=622822198502074110
     */
    @RequestMapping(value="getMyDriverLicense",method=RequestMethod.GET)
    public void getMyDriverLicense(@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		baseBean.setCode("0000");
        	MyDriverLicenseVo myDriverLicenseVo = accountService.getMyDriverLicense(identityCard, "C");
        	baseBean.setData(myDriverLicenseVo);
        	baseBean.setMsg("");
		} catch (Exception e) {
			baseBean.setCode("0009");
        	baseBean.setMsg(e.getMessage());
        	logger.error(e.getMessage());
		}
    	logger.info(JSON.toJSONString(baseBean));
    	
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
	 * http://localhost:8080/web/user/search/getBndTheVehicles.html?identityCard=622822198502074110&mobilephone=15920071829
     */
    @RequestMapping(value="getBndTheVehicles",method=RequestMethod.GET)
    public void getBndTheVehicles(@RequestParam("identityCard") String identityCard,@RequestParam("mobilephone") String mobilephone,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	String sourceOfCertification = "C";
    	List<BindTheVehicleVo> bindTheVehicleVos =  accountService.getBndTheVehicles(identityCard, mobilephone, sourceOfCertification);
    	
    	/*BindTheVehicleVo bindTheVehicleVo1 = new BindTheVehicleVo();
    	bindTheVehicleVo1.setNumberPlateNumber("粤B8888NR");
    	bindTheVehicleVo1.setPlateType("蓝牌");
    	bindTheVehicleVo1.setAnnualReviewDate("2017-08-04");
    	bindTheVehicleVo1.setName("刘小明");
    	bindTheVehicleVo1.setIdentityCard("4415445678788");
    	bindTheVehicleVo1.setIsMyself("是");
    	bindTheVehicleVo1.setAnnualReviewDateRemind("距离年审时间还有123天");
    	bindTheVehicleVo1.setMobilephone("13666666666");
    	bindTheVehicleVo1.setIllegalNumber("100");
    	bindTheVehicleVo1.setOtherPeopleUse("车辆是否有其他人使用");
    	
    	BindTheVehicleVo bindTheVehicleVo2 = new BindTheVehicleVo();
    	bindTheVehicleVo2.setNumberPlateNumber("粤B6666NR");
    	bindTheVehicleVo2.setPlateType("黄牌");
    	bindTheVehicleVo2.setAnnualReviewDate("2017-08-04");
    	bindTheVehicleVo2.setName("黄小明");
    	bindTheVehicleVo2.setIdentityCard("4515445678788");
    	bindTheVehicleVo2.setIsMyself("否");
    	bindTheVehicleVo2.setAnnualReviewDateRemind("距离年审时间还有333天");
    	bindTheVehicleVo2.setMobilephone("13743333333");
    	bindTheVehicleVo2.setIllegalNumber("20");
    	bindTheVehicleVo2.setOtherPeopleUse("车辆是否有其他人使用");*/
    	baseBean.setData(bindTheVehicleVos);
    	
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    /**
	 * 我的业务(切换查询)
	 * @param loginName
	 * @param password
	 * @param request
	 * @param response
	 * @throws IOException
	 */
    @RequestMapping(value="getMyBusiness",method=RequestMethod.GET)
    public void getMyBusiness(@RequestParam("businessType") Integer businessType,@RequestParam("businessStatus") String businessStatus,
    		@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	baseBean.setData("");
    	
    	renderJSON(baseBean);
    	
    	logger.info(JSON.toJSONString(baseBean));
    }
}
