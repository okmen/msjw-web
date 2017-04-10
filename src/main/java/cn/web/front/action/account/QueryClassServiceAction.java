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
import cn.account.bean.vo.queryclassservice.IdentityVerificationAuditApartFromVo;
import cn.account.bean.vo.queryclassservice.IdentityVerificationAuditVo;
import cn.account.bean.vo.queryclassservice.MakeAnAppointmentVo;
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
