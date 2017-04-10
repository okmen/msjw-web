package cn.web.front.action.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.IdentityVerificationAuditResultsVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.bean.vo.MyDriverLicenseVo;
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
	
	/**
	 * 电子驾驶证
	 * @param driverLicenseNumber
	 * @param userName
	 * @param mobileNumber
	 * @param request
	 * @param response
	 * @throws IOException
	 * url http://localhost:8080/web/user/search/getElectronicDriverLicense.html?driverLicenseNumber==138766&userName=张小波&mobileNumber=13666666666
	 */
    @RequestMapping(value="getElectronicDriverLicense",method=RequestMethod.GET)
    public void getElectronicDriverLicense(@RequestParam("driverLicenseNumber") String driverLicenseNumber,@RequestParam("userName") String userName,@RequestParam("mobileNumber") String mobileNumber,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	ElectronicDriverLicenseVo electronicDriverLicenseVo = new ElectronicDriverLicenseVo();
    	
    	
    	electronicDriverLicenseVo.setElectronicDriverLicense("http://images.cnblogs.com/cnblogs_com/liuhongfeng/737147/o_qrcode_for_gh_228cd30523bc_258.jpg");
    	electronicDriverLicenseVo.setElectronicDriverLicenseQRCode("http://images.cnblogs.com/cnblogs_com/liuhongfeng/737147/o_qrcode_for_gh_228cd30523bc_258.jpg");
    	
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
     * @throws IOException
     * url http://localhost:8080/web/user/search/getDrivingLicense.html?numberPlatenumber=粤B5455&plateType=%E9%BB%84%E7%89%8C&mobileNumber=13666666666
     */
    @RequestMapping(value="getDrivingLicense",method=RequestMethod.GET)
    public void getDrivingLicense(@RequestParam("numberPlatenumber") String numberPlatenumber,@RequestParam("plateType") String plateType,@RequestParam("mobileNumber") String mobileNumber,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	DrivingLicenseVo drivingLicenseVo = new DrivingLicenseVo();
    	drivingLicenseVo.setElectronicDriverLicense("张小龙");
    	drivingLicenseVo.setIsOwnerName("1");
    	drivingLicenseVo.setElectronicDrivingLicense("http://images.cnblogs.com/cnblogs_com/liuhongfeng/737147/o_qrcode_for_gh_228cd30523bc_258.jpg");
    	drivingLicenseVo.setElectronicDrivingLicenseQRCode("http://images.cnblogs.com/cnblogs_com/liuhongfeng/737147/o_qrcode_for_gh_228cd30523bc_258.jpg");
    	
    	baseBean.setData(drivingLicenseVo);
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
	
	/**
     * 我的驾驶证
     * @param businessType
     * @param businessStatus
     * @param identityCard
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value="getMyDriverLicense",method=RequestMethod.GET)
    public void getMyDriverLicense(@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	MyDriverLicenseVo myDriverLicenseVo = new MyDriverLicenseVo();
    	myDriverLicenseVo.setFileNumber("44032396102");
    	myDriverLicenseVo.setStatus("正常");
    	myDriverLicenseVo.setAvailableScore("12分");
    	myDriverLicenseVo.setPhysicalExaminationDate("2015-09-09");
    	myDriverLicenseVo.setEffectiveDate("2017-08-04");
    	myDriverLicenseVo.setIsReceive(1);
    	baseBean.setData(myDriverLicenseVo);
    	
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
	/**
     * 查询已绑车辆
     * @param businessType
     * @param businessStatus
     * @param identityCard
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value="getBndTheVehicles",method=RequestMethod.GET)
    public void getBndTheVehicles(@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	List<BindTheVehicleVo> bindTheVehicleVos = new ArrayList<BindTheVehicleVo>();
    	
    	BindTheVehicleVo bindTheVehicleVo1 = new BindTheVehicleVo();
    	bindTheVehicleVo1.setNumberPlateNumber("粤B8888NR");
    	bindTheVehicleVo1.setPlateType("蓝牌");
    	bindTheVehicleVo1.setAnnualReviewDate("2017-08-04");
    	bindTheVehicleVo1.setName("刘小明");
    	bindTheVehicleVo1.setIdentityCard("4415445678788");
    	bindTheVehicleVo1.setIsMyself("是");
    	bindTheVehicleVo1.setAnnualReviewDateRemind("距离年审时间还有123天");
    	bindTheVehicleVo1.setMobilephone("13666666666");
    	bindTheVehicleVo1.setIllegalNumber(100);
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
    	bindTheVehicleVo2.setIllegalNumber(20);
    	bindTheVehicleVo2.setOtherPeopleUse("车辆是否有其他人使用");
    	
    	bindTheVehicleVos.add(bindTheVehicleVo1);
    	bindTheVehicleVos.add(bindTheVehicleVo2);
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
