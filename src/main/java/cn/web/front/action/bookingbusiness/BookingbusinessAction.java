package cn.web.front.action.bookingbusiness;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import cn.booking.business.bean.AppTimeHelper;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.CreateVehicleInfoVo;
import cn.booking.business.bean.DriveInfoVO;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.bean.SmsInfoVO;
import cn.booking.business.bean.VehicleInfoVO;
import cn.booking.business.service.IBookingBusinessService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;

/**
 * 预约类Action
 * 
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value = "/bookingbusiness/")
@SuppressWarnings(value = "all")
public class BookingbusinessAction extends BaseAction {

	
	@Autowired
	@Qualifier("bookingBusinessService")
	private IBookingBusinessService bookingBusinessService;

	/**
	 * 取消预约
	 * @param businessType 业务类型 必填 ‘1’驾驶证业务 ‘2’机动车业务
	 * @param bookNumber  预约号
	 * @param mobile 手机号
	 */
	@RequestMapping("cancel")
	public void cancel(String businessType, String bookerNumber, String mobile) {
		BaseBean baseBean = new BaseBean();
		boolean flag = false;
		if (StringUtil.isBlank(businessType)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessType 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(bookerNumber)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("bookerNumber 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(mobile)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("mobile 不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			SmsInfoVO smsInfoVO = bookingBusinessService.cancel(businessType, bookerNumber, mobile);
			if(null != smsInfoVO && "00".equals(smsInfoVO.getCode())){
				//成功
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(smsInfoVO.getMsg());
				baseBean.setData(smsInfoVO.getResult());
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(smsInfoVO.getMsg());
				baseBean.setData(smsInfoVO.getResult());
			}
		} catch (Exception e) {
			logger.error("cancel异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 获取驾驶证预约信息
	 * @param bookerNumber 预约号 必填
	 * @param idNumber 证件号码 必填
	 * @param businessTypeId 业务类型ID
	 * @param organizationId 预约单位ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getDriveInfo")
	public void getDriveInfo(String bookerNumber, String idNumber, String businessTypeId, String organizationId) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(bookerNumber)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("bookerNumber 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(idNumber)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("idNumber 不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			DriveInfoVO driveInfoVO = bookingBusinessService.getDriveInfo(bookerNumber, idNumber, businessTypeId, organizationId);
			baseBean.setCode(MsgCode.success);
			baseBean.setMsg("");
			baseBean.setData(driveInfoVO);
		} catch (Exception e) {
			logger.error("getDriveInfo异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 获取机动车预约信息 
	 * @param bookerNumber 预约号  必填
	 * @param idNumber 证件号码  必填
	 * @param platNumber 车牌号
	 * @param businessTypeId 业务类型ID
	 * @param organizationId 预约单位ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getVehicleInfo")
	public void getVehicleInfo(String bookerNumber,String idNumber,String platNumber,String businessTypeId,String organizationId) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(bookerNumber)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("bookerNumber 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(idNumber)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("idNumber 不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			VehicleInfoVO vehicleInfoVO = bookingBusinessService.getVehicleInfo(bookerNumber, idNumber, platNumber, businessTypeId, organizationId);
			baseBean.setCode(MsgCode.success);
			baseBean.setMsg("");
			baseBean.setData(vehicleInfoVO);
		}  catch (Exception e) {
			logger.error("getVehicleInfo异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 获取车辆类型Id
	 * 
	 * @param code
	 *            车辆类型code
	 */
	@RequestMapping("getCarTypeId")
	public void getCarTypeId(String code) {
		BaseBean baseBean = new BaseBean();
		boolean flag = false;
		if (StringUtil.isBlank(code)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("车牌类型code不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			List<CarTypeVO> carTypeVOs = bookingBusinessService.getCarTypes();
			if (null != carTypeVOs) {
				baseBean.setCode(MsgCode.success);
				for (CarTypeVO idTypeVO : carTypeVOs) {
					String code2 = idTypeVO.getCode();
					String id = idTypeVO.getId();
					if (code.equals(code2)) {
						flag = true;
						baseBean.setData(id);
						baseBean.setMsg("查询成功");
					}
				}
				if (flag = false) {
					baseBean.setCode(MsgCode.businessError);
					baseBean.setMsg("未找到该车辆类型对应的id");
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("查询车辆类型id失败");
			}

		} catch (Exception e) {
			logger.error("查询车辆类型id异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}

	/**
	 * 获取业务类型Id
	 * 
	 * @param code
	 * @param type
	 * @param part
	 * @param arg0
	 * @param arg1
	 */

	@RequestMapping("getBusinessTypeId")
	public void getBusinessTypeId(String code, String type, String part, String arg0, String arg1) {
		BaseBean baseBean = new BaseBean();
		boolean flag = false;
		if (StringUtil.isBlank(code)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("车牌类型code不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(type)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("预约类型不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(type)) {
			type = "";
		}
		try {
			List<BusinessTypeVO> businessTypes = bookingBusinessService.getBusinessTypes(type, part, arg0, arg1);
			if (null != businessTypes) {
				baseBean.setCode(MsgCode.success);
				for (BusinessTypeVO businessTypeVO : businessTypes) {
					String code2 = businessTypeVO.getCode();
					String id = businessTypeVO.getId();
					if (code.equals(code2)) {
						flag = true;
						baseBean.setData(id);
						baseBean.setMsg("查询成功");
					}
				}
				if (flag == false) {
					baseBean.setCode(MsgCode.businessError);
					baseBean.setMsg("未找到该业务类型对应的id");
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("查询业务类型id失败");
			}

		} catch (Exception e) {
			logger.error("查询业务类型id异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}

	/**
	 * 获取证件种类Id
	 * 
	 * @param code
	 * @param businessTypeId
	 * @param arg0
	 * @param arg1
	 */

	@RequestMapping("getIdTypeId")
	public void getIdTypeId(String code, String businessTypeId, String arg0, String arg1) {
		BaseBean baseBean = new BaseBean();
		boolean flag = false;
		if (StringUtil.isBlank(code)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("证件种类code不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(businessTypeId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessTypeId不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			List<IdTypeVO> idTypeVOs = bookingBusinessService.getIdTypes(businessTypeId, arg0, arg1);
			if (null != idTypeVOs) {
				baseBean.setCode(MsgCode.success);
				for (IdTypeVO idTypeVO : idTypeVOs) {
					String code2 = idTypeVO.getCode();
					String id = idTypeVO.getId();
					if (code.equals(code2)) {
						flag = true;
						baseBean.setData(id);
						baseBean.setMsg("查询成功");
					}
				}
				if (flag == false) {
					baseBean.setCode(MsgCode.businessError);
					baseBean.setMsg("未找到该证件类型对应的id");
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("查询证件类型id失败");
			}
		} catch (Exception e) {
			logger.error("获取证件种类id异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}

	/**
	 * 获取预约地点
	 * 
	 * @param businessTypeId
	 * @param arg0
	 * @param arg1
	 */

	@RequestMapping("getOrgsByBusinessTypeId")
	public void getOrgsByBusinessTypeId(String businessTypeId, String arg0, String arg1) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(businessTypeId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessTypeId不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			List<OrgVO> orgVOs = bookingBusinessService.getOrgsByBusinessTypeId(businessTypeId, arg0, arg1);
			if (null != orgVOs) {
				baseBean.setCode(MsgCode.success);
				baseBean.setData(orgVOs);
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("查询车辆类型id失败");
			}

		} catch (Exception e) {
			logger.error("查询业务类型id异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}

	/**
	 * 根据单位ID获取单位详细信息
	 * 
	 * @param orgId
	 */
	@RequestMapping("findOrgByOrgId")
	public void findOrgByOrgId(String orgId) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(orgId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("orgId不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			OrgVO orgVO = bookingBusinessService.findOrgByOrgId(orgId);
			if (null != orgVO) {
				baseBean.setCode(MsgCode.success);
				baseBean.setData(orgVO);
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("根据单位ID获取单位详细信息失败");
			}

		} catch (Exception e) {
			logger.error("根据单位ID获取单位详细信息异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}

	/**
	 * 获取可预约日期
	 * 
	 * @param orgId
	 * @param businessTypeId
	 * @param arg0
	 * @param arg1
	 */
	@RequestMapping("getAppointmentDate")
	public void getAppointmentDate(String orgId, String businessTypeId, String arg0, String arg1) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(orgId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("orgId不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(businessTypeId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessTypeId不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			List<String> appointmentDates = bookingBusinessService.getAppointmentDate(orgId, businessTypeId, arg0,
					arg1);
			if (null != appointmentDates) {
				baseBean.setCode(MsgCode.success);
				baseBean.setData(appointmentDates);
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("获取可预约日期失败");
			}

		} catch (Exception e) {
			logger.error("获取可预约日期异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}

	/**
	 * 获取可预约时间段
	 * 
	 * @param date
	 * @param orgId
	 * @param businessTypeId
	 * @param carTypeId
	 * @param optlittleCar
	 */

	@RequestMapping("getAppTimes")
	public void getAppTimes(String date, String orgId, String businessTypeId, String carTypeId, String optlittleCar) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(date)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("date不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(orgId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("orgId不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(businessTypeId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessTypeId不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(carTypeId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("carTypeId不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(optlittleCar)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("optlittleCar不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			List<AppTimeHelper> appTimes = bookingBusinessService.getAppTimes(date, orgId, businessTypeId, carTypeId,
					optlittleCar);
			if (null != appTimes) {
				baseBean.setCode(MsgCode.success);
				baseBean.setData(appTimes);
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("获取可预约时间段失败");
			}

		} catch (Exception e) {
			logger.error("获取可预约时间段异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * @Title:
	 * @Description: TODO(预约信息写入)
	 * @param @param request
	 * @param @param response    参数
	 * @return void    返回类型
	 * @throws
	 */
	@RequestMapping(value="/createVehicleInfo.html")
	public void createVehicleInfo(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
    	String orgId = request.getParameter("orgId");   //预约地点id    e4e48584399473d201399b0c4ad62b39
    	String businessTypeId = request.getParameter("businessTypeId"); //预约类型ID
		String name = request.getParameter("name");  //姓名
		String idTypeId = request.getParameter("idTypeId");  //证件种类ID  e4e48584399473d20139947f125e2b2c
		String idNumber = request.getParameter("idNumber");  //证件号码  622822198502074112
		String mobile = request.getParameter("mobile");  //手机号码
		String appointmentDate = request.getParameter("appointmentDate");  //预约日期
		String appointmentTime = request.getParameter("appointmentTime");  //预约时间
		String carTypeId = request.getParameter("carTypeId");  //号牌种类ID 小型汽车（蓝色）  e4e48584399473d20139947fff4e2b2e
		String carFrame = request.getParameter("carFrame");  //车架号
		String platNumber = request.getParameter("platNumber");  //车牌号或车架号
		String bookerName = request.getParameter("bookerName");  //预约人姓名
		String bookerIdNumber = request.getParameter("bookerIdNumber");  //预约人身份证号码
		String bookerType = request.getParameter("bookerType");  //预约方式
		String useCharater = request.getParameter("useCharater");  //使用性质
		String arg2 = request.getParameter("arg2");  //短信验证码
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","arg2");
			if(!bool) return;
			
			CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
			vo.setOrgId(orgId);  //预约地点Id
			vo.setBusinessTypeId(businessTypeId);  //业务类型id
			vo.setName(name); //姓名
			vo.setIdTypeId(idTypeId);		//证件种类id
			vo.setIdNumber(idNumber); //证件号码
			vo.setMobile(mobile);	//手机号码
			vo.setAppointmentDate(appointmentDate);  //预约日期
			vo.setAppointmentTime(appointmentTime);	//预约时间
			vo.setCarTypeId(carTypeId); 	//车辆类型 号牌种类  小型汽车（蓝色）
			vo.setCarFrame(carFrame); 	//车架号
			vo.setPlatNumber(platNumber);   //车牌号或车架号
			vo.setBookerName(bookerName);  //预约人姓名
			vo.setBookerIdNumber(bookerIdNumber); //预约人身份证号码
			vo.setBookerType(bookerType); 	//预约方式  ‘0’ 非代办(本人)，’1’ 普通代办 ‘2’专业代办
			vo.setOptlittleCar(""); 	//车辆产地  可为空
			vo.setIndexType(""); 	//指标类型   可为空
			vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号  可为空
			vo.setUseCharater(useCharater); 	//使用性质
			vo.setArg0("");  //车辆型号  可为空
			vo.setArg1(mobile); 	//手机号码
			vo.setArg2(arg2);	 	//短信验证码
			vo.setRzjs("");    //可为空
			
			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(vo);
			
			if("00".equals(refBean.getCode())){
        		baseBean.setCode("0000");
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getMsg());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】换领机动车登记证书 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
}
