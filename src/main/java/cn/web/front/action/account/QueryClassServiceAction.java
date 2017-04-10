package cn.web.front.action.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;

import cn.account.bean.vo.queryclassservice.CertificationProgressQueryVo;
import cn.account.bean.vo.queryclassservice.DriverLicenseBusinessVo;
import cn.account.bean.vo.queryclassservice.IdentityVerificationAuditApartFromVo;
import cn.account.bean.vo.queryclassservice.IdentityVerificationAuditVo;
import cn.account.bean.vo.queryclassservice.MakeAnAppointmentVo;
import cn.account.bean.vo.queryclassservice.MotorVehicleBusinessVo;
import cn.sdk.bean.BaseBean;
import cn.web.front.support.BaseAction;
/**
 * 查询类服务
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value="/user/queryClassService")
@SuppressWarnings(value="all")
public class QueryClassServiceAction extends BaseAction{
	/**
	 * 用户中心-查询类服务-业务办理进度查询-驾驶证业务
	 * @param identityCard 身份证号
	 * @param request
	 * @param response
	 * @throws IOException
	 * URL
	 * http://localhost:8080/web/user/queryClassService/getDriverLicenseBusiness.html?identityCard=431225199223343344X
	 */
    @RequestMapping(value="getDriverLicenseBusiness",method=RequestMethod.GET)
    public void getDriverLicenseBusiness(@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	List<DriverLicenseBusinessVo> driverLicenseBusinessVos = new ArrayList<DriverLicenseBusinessVo>();
    	DriverLicenseBusinessVo driverLicenseBusinessVo1 = new DriverLicenseBusinessVo();
    	driverLicenseBusinessVo1.setUserName("小明1");
    	driverLicenseBusinessVo1.setIdentityCard("45644444332222");
    	driverLicenseBusinessVo1.setGradeNumber("粤B88668");
    	driverLicenseBusinessVo1.setGradeType("蓝牌");
    	driverLicenseBusinessVo1.setReceptionTime("2017-04-08 14:12:09");
    	driverLicenseBusinessVo1.setVehicleNumber("车辆号码");
    	driverLicenseBusinessVo1.setBusinessName("换领驾驶证");
    	driverLicenseBusinessVo1.setHandleTheState("待初审");
    	
    	DriverLicenseBusinessVo driverLicenseBusinessVo2 = new DriverLicenseBusinessVo();
    	driverLicenseBusinessVo2.setUserName("小明2");
    	driverLicenseBusinessVo2.setIdentityCard("42344444332222");
    	driverLicenseBusinessVo2.setGradeNumber("粤B8888");
    	driverLicenseBusinessVo2.setGradeType("蓝牌");
    	driverLicenseBusinessVo2.setReceptionTime("2017-04-08 14:12:09");
    	driverLicenseBusinessVo2.setVehicleNumber("车辆号码");
    	driverLicenseBusinessVo2.setBusinessName("补领驾驶证");
    	driverLicenseBusinessVo2.setHandleTheState("已制证");
    	
    	driverLicenseBusinessVos.add(driverLicenseBusinessVo1);
    	driverLicenseBusinessVos.add(driverLicenseBusinessVo2);
    	
    	baseBean.setData(driverLicenseBusinessVos);
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
	
	
	/**
	 * 用户中心-查询类服务-业务办理进度查询-机动车业务
	 * @param identityCard 身份证号
	 * @param request
	 * @param response
	 * @throws IOException
	 * URL
	 * http://localhost:8080/web/user/queryClassService/getMotorVehicleBusiness.html?identityCard=431225199223343344X
	 */
    @RequestMapping(value="getMotorVehicleBusiness",method=RequestMethod.GET)
    public void getMotorVehicleBusiness(@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	List<MotorVehicleBusinessVo> makeAnAppointmentVos = new ArrayList<MotorVehicleBusinessVo>();
    	MotorVehicleBusinessVo makeAnAppointmentVo1 = new MotorVehicleBusinessVo();
    	makeAnAppointmentVo1.setUserName("小明1");
    	makeAnAppointmentVo1.setIdentityCard("45644444332222");
    	makeAnAppointmentVo1.setApplicationTime("2017-04-08 14:12:09");
    	makeAnAppointmentVo1.setGradeNumber("蓝牌");
    	makeAnAppointmentVo1.setGradeType("蓝牌");
    	makeAnAppointmentVo1.setReceptionTime("2017-04-08 14:12:09");
    	makeAnAppointmentVo1.setVehicleNumber("车辆号码");
    	makeAnAppointmentVo1.setBusinessName("换领机动车行驶证");
    	makeAnAppointmentVo1.setHandleTheState("待初审");
    	
    	MotorVehicleBusinessVo makeAnAppointmentVo2 = new MotorVehicleBusinessVo();
    	makeAnAppointmentVo2.setUserName("小明2");
    	makeAnAppointmentVo2.setIdentityCard("42344444332222");
    	makeAnAppointmentVo2.setApplicationTime("2017-04-08 14:12:09");
    	makeAnAppointmentVo2.setGradeNumber("蓝牌");
    	makeAnAppointmentVo2.setGradeType("蓝牌");
    	makeAnAppointmentVo2.setReceptionTime("2017-04-08 14:12:09");
    	makeAnAppointmentVo2.setVehicleNumber("车辆号码");
    	makeAnAppointmentVo2.setBusinessName("补领机动车行驶证");
    	makeAnAppointmentVo2.setHandleTheState("已制证");
    	
    	makeAnAppointmentVos.add(makeAnAppointmentVo1);
    	makeAnAppointmentVos.add(makeAnAppointmentVo2);
    	
    	baseBean.setData(makeAnAppointmentVos);
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 用户中心-查询类服务-预约查询-机动车业务/驾驶证业务
	 * @param businessType 业务类型 	1-机动车业务、2-驾驶证业务
	 * @param reservationNumber 预约编号
	 * @param identityCard 身份证号
	 * @param request
	 * @param response
	 * @throws IOException
	 * URL
	 * http://localhost:8080/web/user/queryClassService/getMakeAnAppointment.html?businessType=1&reservationNumber=7654534343&identityCard=431225199223343344X
	 */
    @RequestMapping(value="getMakeAnAppointment",method=RequestMethod.GET)
    public void commitDriverLicenseInformationSheet(@RequestParam("businessType") String businessType,@RequestParam("reservationNumber") String reservationNumber,
    		@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	MakeAnAppointmentVo makeAnAppointmentVo = new MakeAnAppointmentVo();
    	makeAnAppointmentVo.setBusinessType("动车业务");
    	makeAnAppointmentVo.setUserName("刘小明");
    	makeAnAppointmentVo.setIdentityCard("43555555553333333X");
    	makeAnAppointmentVo.setMobilephone("13666666666");
    	makeAnAppointmentVo.setPlaceOfAppointment("深圳南山区");
    	makeAnAppointmentVo.setPlaceOfDate("2017-04-08");
    	makeAnAppointmentVo.setPlaceOfTime("09:00:00");
    	makeAnAppointmentVo.setNumberPlateNumber("B701NR");
    	makeAnAppointmentVo.setVehicleType("小型汽车");
    	makeAnAppointmentVo.setReservationStatus("预约中");
    	
    	baseBean.setData(makeAnAppointmentVo);
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 用户中心-查询类服务-预约取消-机动车业务/驾驶证业务
	 * @param businessType 业务类型 	1-机动车业务、2-驾驶证业务
	 * @param reservationNumber 预约编号
	 * @param identityCard 身份证号
	 * @param request
	 * @param response
	 * @throws IOException
	 * URL
	 * http://localhost:8080/web/user/queryClassService/bookingCancellation.html?businessType=1&reservationNumber=7654534343&identityCard=431225199223343344X
	 */
    @RequestMapping(value="bookingCancellation",method=RequestMethod.GET)
    public void bookingCancellation(@RequestParam("businessType") String businessType,@RequestParam("reservationNumber") String reservationNumber,
    		@RequestParam("identityCard") String identityCard,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	baseBean.setData("");
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    /**
     * 用户中心-查询类服务-预约取消-机动车业务/驾驶证业务
     * @param businessType 业务类型 	1-身份认证审核、2-自然人认证审核、3-公车注册、4-车辆绑定审核、5-驾驶证绑定审核
     * @param identityCard	身份证号
     * @param serialNumber 流水号
     * @param agencyCode 机构代码
     * @param request
     * @param response
     * @throws IOException
     * URL
	 * http://localhost:8080/web/user/queryClassService/getCertificationProgressQuery.html?businessType=1&identityCard=431225199223343344X&serialNumber=3322222&agencyCode=12121212
     */
    @RequestMapping(value="getCertificationProgressQuery",method=RequestMethod.GET)
    public void getCertificationProgressQuery(@RequestParam("businessType") String businessType,@RequestParam("identityCard") String identityCard,
    		@RequestParam("serialNumber") String serialNumber,@RequestParam("agencyCode") String agencyCode,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	
    	IdentityVerificationAuditVo identityVerificationAuditVo = new IdentityVerificationAuditVo();
    	identityVerificationAuditVo.setInformationRegistrationTime("2016-09-09");
    	identityVerificationAuditVo.setCurrentState("审核通过");
    	identityVerificationAuditVo.setAuthenticationType("车主本人");
    	identityVerificationAuditVo.setSerialNumber("32442232322");
    	identityVerificationAuditVo.setBusinessType(1);
    	
    	List<IdentityVerificationAuditApartFromVo> identityVerificationAuditApartFromVos = new ArrayList<IdentityVerificationAuditApartFromVo>();
    	IdentityVerificationAuditApartFromVo identityVerificationAuditApartFromVo1 = new IdentityVerificationAuditApartFromVo();
    	identityVerificationAuditApartFromVo1.setApprovalStatus("退办");
    	identityVerificationAuditApartFromVo1.setBusinessType(2);
    	identityVerificationAuditApartFromVo1.setNumberPlateNumber("粤B877NR");
    	identityVerificationAuditApartFromVo1.setPlateType("黄牌");
    	identityVerificationAuditApartFromVo1.setReasonForWithdrawal("xxx原因导致退办");
    	
    	IdentityVerificationAuditApartFromVo identityVerificationAuditApartFromVo2 = new IdentityVerificationAuditApartFromVo();
    	identityVerificationAuditApartFromVo1.setApprovalStatus("退办");
    	identityVerificationAuditApartFromVo1.setBusinessType(2);
    	identityVerificationAuditApartFromVo1.setNumberPlateNumber("粤B6547NR");
    	identityVerificationAuditApartFromVo1.setPlateType("蓝牌");
    	identityVerificationAuditApartFromVo1.setReasonForWithdrawal("xxx原因导致退办");
    	
    	identityVerificationAuditApartFromVos.add(identityVerificationAuditApartFromVo1);
    	identityVerificationAuditApartFromVos.add(identityVerificationAuditApartFromVo2);
    	
    	CertificationProgressQueryVo certificationProgressQueryVo = new CertificationProgressQueryVo();
    	certificationProgressQueryVo.setIdentityVerificationAudit(identityVerificationAuditVo);
    	certificationProgressQueryVo.setIdentityVerificationAuditApartFromVos(identityVerificationAuditApartFromVos);
    	
    	
    	baseBean.setData(certificationProgressQueryVo);
    	
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
}
