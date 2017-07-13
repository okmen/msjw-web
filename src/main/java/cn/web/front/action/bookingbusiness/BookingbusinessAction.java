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

	private static final String BUSINESSTYPEID_JD28 = "40288282463ceca5014683c9e7b11fb7";//机动车打刻原车发动机号码变更备案
	private static final String BUSINESSTYPEID_JD29 = "40288282463ceca5014683cae5b81fc1";//机动车打刻原车辆识别代号变更备案
	private static final String BUSINESSTYPEID_JD33 = "402882824d2d900b014d3157b44c1064";//档案更正
	private static final String BUSINESSTYPEID_DQ = "4028823f5ad56e98015ad6793c4f291b";//出租客运车辆使用性质变更
	private static final String BUSINESSTYPEID_ZJ08 = "4028828240dd972e0140ddab17e30002";//机动车迁出
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
	public void cancel(String businessType, String bookNumber, String mobile) {
		BaseBean baseBean = new BaseBean();
		boolean flag = false;
		if (StringUtil.isBlank(businessType)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessType 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(bookNumber)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("bookNumber 不能为空!");
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
			SmsInfoVO smsInfoVO = bookingBusinessService.cancel(businessType, bookNumber, mobile);
			if(null != smsInfoVO && "00".equals(smsInfoVO.getCode())){
				//成功
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg("");
				baseBean.setData(smsInfoVO);
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(smsInfoVO.getMsg());
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
					}
				}
				if (flag = false) {
					baseBean.setCode(MsgCode.businessError);
					baseBean.setMsg("查询车辆类型id失败");
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
					}
				}
				if (flag = false) {
					baseBean.setCode(MsgCode.businessError);
					baseBean.setMsg("查询业务类型id失败");
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
					}
				}
				if (flag = false) {
					baseBean.setCode(MsgCode.businessError);
					baseBean.setMsg("查询车辆类型id失败");
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("查询车辆类型id失败");
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
		if (StringUtil.isBlank(businessTypeId)) {
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
	 * @Title: createVehicleInfo_JD06
	 * @Description: TODO(换领机动车登记证书)
	 * @param @param request
	 * @param @param response    参数
	 * @return void    返回类型
	 * @throws
	 */
	@RequestMapping(value="/createVehicleInfo_JD06.html")
	public void createVehicleInfo_JD06(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
    	String orgId = request.getParameter("orgId");   //预约地点id    e4e48584399473d201399b0c4ad62b39
		String businessTypeId = request.getParameter("businessTypeId");  //业务类型ID   4028828239a4a4c60139a4fb36ef0007
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
		String rzjs = request.getParameter("rzjs");  //认证角色
		String optlittleCar = request.getParameter("optlittleCar");  //车辆产地
		String indexType = request.getParameter("indexType");  //指标类型
		String indexNo = request.getParameter("indexNo");  //指标号/公证号/车辆识别代号
		String useCharater = request.getParameter("useCharater");  //使用性质
		String arg0 = request.getParameter("arg0");  //车辆型号
		String arg1 = request.getParameter("arg1");  //手机号码
		String arg2 = request.getParameter("arg2");  //短信验证码
		
		
		try {
			CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
			vo.setOrgId("e4e48584399473d201399b0c4ad62b39");  //预约地点Id
			vo.setBusinessTypeId("4028828239a4a4c60139a4fb36ef0007");  //业务类型id
			vo.setName("测试"); //姓名
			vo.setIdTypeId("e4e48584399473d20139947f125e2b2c");		//证件种类id
			vo.setIdNumber("622822198502074112"); //证件号码
			vo.setMobile("17688758320");	//手机号码
			vo.setAppointmentDate("2017-07-24");  //预约日期
			vo.setAppointmentTime("12:00-17:00");	//预约时间
			vo.setCarTypeId("e4e48584399473d20139947fff4e2b2e"); 	//号牌种类  小型汽车（蓝色）
			vo.setCarFrame("5563"); 	//车架号
			vo.setPlatNumber("粤B6A42Q");   //车牌号或车架号
			vo.setBookerName("测试");  //预约人姓名
			vo.setBookerIdNumber("622822198502074112"); //预约人身份证号码
			vo.setBookerType("0"); 	//预约方式
			vo.setOptlittleCar(""); 	//车辆产地
			vo.setIndexType(""); 	//指标类型
			vo.setIndexNo(""); 		//指标号/公证号/车辆识别代号
			vo.setUseCharater("123"); 	//使用性质
			vo.setArg0("DH");  //车辆型号
			vo.setArg1("17688758320"); 	//手机号码
			vo.setArg2("464032");	 	//短信验证码
			vo.setRzjs("11");
			
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
	/**
	 * 机动车打刻原车发动机号码变更备案
	 * @author lifangyong
	 * @param request
	 * @param response
	 */
	@RequestMapping("createVehicleInfo_JD28.html")
    public void createVehicleInfo_JD28(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateVehicleInfoVo createVehicleInfoVo = new CreateVehicleInfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");	//预约地点ID
			String name = request.getParameter("name");	//姓名
			String idTypeId = request.getParameter("idTypeId");	//证件种类ID
			String idNumber = request.getParameter("idNumber");	//证件号码
			String mobile = request.getParameter("mobile");	//手机号码
			String appointmentDate = request.getParameter("appointmentDate");	//预约日期
			String appointmentTime = request.getParameter("appointmentTime");	//预约时间
			String carTypeId = request.getParameter("carTypeId");	//号牌种类ID【1.2】
			String carFrame = request.getParameter("carFrame");	//车架号
			String platNumber = request.getParameter("platNumber");	//车牌号或车架号
			String bookerName = request.getParameter("bookerName");	//预约人姓名
			String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码
			String bookerType = request.getParameter("bookerType");	//预约方式
			String rzjs = request.getParameter("rzjs");	//认证角色
			String optlittleCar = request.getParameter("optlittleCar");	//车辆产地
			String indexType = request.getParameter("indexType"); 	//指标类型
			String indexNo = request.getParameter("indexNo"); 	//指标号/公证号/车辆识别代号
			String useCharater = request.getParameter("useCharater");	//使用性质
			String arg0 = request.getParameter("arg0");	//车辆型号
			String arg1 = request.getParameter("arg1");	//手机号码
			String arg2 = request.getParameter("arg2");	//短信验证码

			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("orgId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("name不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentDate不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentTime不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carFrame)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carFrame不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(platNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("platNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(useCharater)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("useCharater不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg0)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg0不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg1不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg2不能为空!");
				renderJSON(baseBean);
				return;
			}
			createVehicleInfoVo.setOrgId(orgId);
			createVehicleInfoVo.setBusinessTypeId(BUSINESSTYPEID_JD28);
			createVehicleInfoVo.setName(name);
			createVehicleInfoVo.setIdTypeId(idTypeId);
			createVehicleInfoVo.setIdNumber(idNumber);
			createVehicleInfoVo.setMobile(mobile);
			createVehicleInfoVo.setAppointmentDate(appointmentDate);
			createVehicleInfoVo.setAppointmentTime(appointmentTime);
			createVehicleInfoVo.setCarTypeId(carTypeId);
			createVehicleInfoVo.setCarFrame(carFrame);
			createVehicleInfoVo.setPlatNumber(platNumber);
			createVehicleInfoVo.setBookerName(bookerName);
			createVehicleInfoVo.setBookerIdNumber(bookerIdNumber);
			createVehicleInfoVo.setBookerType(bookerType);
			createVehicleInfoVo.setRzjs(rzjs);
			createVehicleInfoVo.setOptlittleCar(optlittleCar);
			createVehicleInfoVo.setIndexType(indexType);
			createVehicleInfoVo.setIndexNo(indexNo);
			createVehicleInfoVo.setUseCharater(useCharater);
			createVehicleInfoVo.setArg0(arg0);
			createVehicleInfoVo.setArg1(arg1);
			createVehicleInfoVo.setArg2(arg2);

			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(createVehicleInfoVo);
			
			if("00".equals(refBean.getCode())){
        		baseBean.setCode("0000");
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getMsg());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】机动车打刻原车发动机号码变更备案 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
		
	}
	
	/**
	 * 机动车打刻原车辆识别代号变更备案
	 * @author lifangyong
	 * @param request
	 * @param response
	 */
	@RequestMapping("createVehicleInfo_JD29.html")
    public void createVehicleInfo_JD29(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateVehicleInfoVo createVehicleInfoVo = new CreateVehicleInfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");	//预约地点ID
			String name = request.getParameter("name");	//姓名
			String idTypeId = request.getParameter("idTypeId");	//证件种类ID
			String idNumber = request.getParameter("idNumber");	//证件号码
			String mobile = request.getParameter("mobile");	//手机号码
			String appointmentDate = request.getParameter("appointmentDate");	//预约日期
			String appointmentTime = request.getParameter("appointmentTime");	//预约时间
			String carTypeId = request.getParameter("carTypeId");	//号牌种类ID【1.2】
			String carFrame = request.getParameter("carFrame");	//车架号
			String platNumber = request.getParameter("platNumber");	//车牌号或车架号
			String bookerName = request.getParameter("bookerName");	//预约人姓名
			String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码
			String bookerType = request.getParameter("bookerType");	//预约方式
			String rzjs = request.getParameter("rzjs");	//认证角色
			String optlittleCar = request.getParameter("optlittleCar");	//车辆产地
			String indexType = request.getParameter("indexType"); 	//指标类型
			String indexNo = request.getParameter("indexNo"); 	//指标号/公证号/车辆识别代号
			String useCharater = request.getParameter("useCharater");	//使用性质
			String arg0 = request.getParameter("arg0");	//车辆型号
			String arg1 = request.getParameter("arg1");	//手机号码
			String arg2 = request.getParameter("arg2");	//短信验证码

			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("orgId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("name不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentDate不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentTime不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carFrame)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carFrame不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(platNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("platNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(useCharater)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("useCharater不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg0)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg0不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg1不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg2不能为空!");
				renderJSON(baseBean);
				return;
			}
			createVehicleInfoVo.setOrgId(orgId);
			createVehicleInfoVo.setBusinessTypeId(BUSINESSTYPEID_JD29);
			createVehicleInfoVo.setName(name);
			createVehicleInfoVo.setIdTypeId(idTypeId);
			createVehicleInfoVo.setIdNumber(idNumber);
			createVehicleInfoVo.setMobile(mobile);
			createVehicleInfoVo.setAppointmentDate(appointmentDate);
			createVehicleInfoVo.setAppointmentTime(appointmentTime);
			createVehicleInfoVo.setCarTypeId(carTypeId);
			createVehicleInfoVo.setCarFrame(carFrame);
			createVehicleInfoVo.setPlatNumber(platNumber);
			createVehicleInfoVo.setBookerName(bookerName);
			createVehicleInfoVo.setBookerIdNumber(bookerIdNumber);
			createVehicleInfoVo.setBookerType(bookerType);
			createVehicleInfoVo.setRzjs(rzjs);
			createVehicleInfoVo.setOptlittleCar(optlittleCar);
			createVehicleInfoVo.setIndexType(indexType);
			createVehicleInfoVo.setIndexNo(indexNo);
			createVehicleInfoVo.setUseCharater(useCharater);
			createVehicleInfoVo.setArg0(arg0);
			createVehicleInfoVo.setArg1(arg1);
			createVehicleInfoVo.setArg2(arg2);

			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(createVehicleInfoVo);
			
			if("00".equals(refBean.getCode())){
        		baseBean.setCode("0000");
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getMsg());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】机动车打刻原车辆识别代号变更备案 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 档案更正
	 * @author lifangyong
	 * @param request
	 * @param response
	 */
	@RequestMapping("createVehicleInfo_JD33.html")
    public void createVehicleInfo_JD33(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateVehicleInfoVo createVehicleInfoVo = new CreateVehicleInfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");	//预约地点ID
			String name = request.getParameter("name");	//姓名
			String idTypeId = request.getParameter("idTypeId");	//证件种类ID
			String idNumber = request.getParameter("idNumber");	//证件号码
			String mobile = request.getParameter("mobile");	//手机号码
			String appointmentDate = request.getParameter("appointmentDate");	//预约日期
			String appointmentTime = request.getParameter("appointmentTime");	//预约时间
			String carTypeId = request.getParameter("carTypeId");	//号牌种类ID【1.2】
			String carFrame = request.getParameter("carFrame");	//车架号
			String platNumber = request.getParameter("platNumber");	//车牌号或车架号
			String bookerName = request.getParameter("bookerName");	//预约人姓名
			String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码
			String bookerType = request.getParameter("bookerType");	//预约方式
			String rzjs = request.getParameter("rzjs");	//认证角色
			String optlittleCar = request.getParameter("optlittleCar");	//车辆产地
			String indexType = request.getParameter("indexType"); 	//指标类型
			String indexNo = request.getParameter("indexNo"); 	//指标号/公证号/车辆识别代号
			String useCharater = request.getParameter("useCharater");	//使用性质
			String arg0 = request.getParameter("arg0");	//车辆型号
			String arg1 = request.getParameter("arg1");	//手机号码
			String arg2 = request.getParameter("arg2");	//短信验证码

			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("orgId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("name不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentDate不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentTime不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carFrame)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carFrame不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(platNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("platNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(useCharater)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("useCharater不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg0)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg0不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg1不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg2不能为空!");
				renderJSON(baseBean);
				return;
			}
			createVehicleInfoVo.setOrgId(orgId);
			createVehicleInfoVo.setBusinessTypeId(BUSINESSTYPEID_JD33);
			createVehicleInfoVo.setName(name);
			createVehicleInfoVo.setIdTypeId(idTypeId);
			createVehicleInfoVo.setIdNumber(idNumber);
			createVehicleInfoVo.setMobile(mobile);
			createVehicleInfoVo.setAppointmentDate(appointmentDate);
			createVehicleInfoVo.setAppointmentTime(appointmentTime);
			createVehicleInfoVo.setCarTypeId(carTypeId);
			createVehicleInfoVo.setCarFrame(carFrame);
			createVehicleInfoVo.setPlatNumber(platNumber);
			createVehicleInfoVo.setBookerName(bookerName);
			createVehicleInfoVo.setBookerIdNumber(bookerIdNumber);
			createVehicleInfoVo.setBookerType(bookerType);
			createVehicleInfoVo.setRzjs(rzjs);
			createVehicleInfoVo.setOptlittleCar(optlittleCar);
			createVehicleInfoVo.setIndexType(indexType);
			createVehicleInfoVo.setIndexNo(indexNo);
			createVehicleInfoVo.setUseCharater(useCharater);
			createVehicleInfoVo.setArg0(arg0);
			createVehicleInfoVo.setArg1(arg1);
			createVehicleInfoVo.setArg2(arg2);

			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(createVehicleInfoVo);
			
			if("00".equals(refBean.getCode())){
        		baseBean.setCode("0000");
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getMsg());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】档案更正Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 出租客运车辆使用性质变更
	 * @author lifangyong
	 * @param request
	 * @param response
	 */
	@RequestMapping("createVehicleInfo_DQ.html")
    public void createVehicleInfo_DQ(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateVehicleInfoVo createVehicleInfoVo = new CreateVehicleInfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");	//预约地点ID
			String name = request.getParameter("name");	//姓名
			String idTypeId = request.getParameter("idTypeId");	//证件种类ID
			String idNumber = request.getParameter("idNumber");	//证件号码
			String mobile = request.getParameter("mobile");	//手机号码
			String appointmentDate = request.getParameter("appointmentDate");	//预约日期
			String appointmentTime = request.getParameter("appointmentTime");	//预约时间
			String carTypeId = request.getParameter("carTypeId");	//号牌种类ID【1.2】
			String carFrame = request.getParameter("carFrame");	//车架号
			String platNumber = request.getParameter("platNumber");	//车牌号或车架号
			String bookerName = request.getParameter("bookerName");	//预约人姓名
			String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码
			String bookerType = request.getParameter("bookerType");	//预约方式
			String rzjs = request.getParameter("rzjs");	//认证角色
			String optlittleCar = request.getParameter("optlittleCar");	//车辆产地
			String indexType = request.getParameter("indexType"); 	//指标类型
			String indexNo = request.getParameter("indexNo"); 	//指标号/公证号/车辆识别代号
			String useCharater = request.getParameter("useCharater");	//使用性质
			String arg0 = request.getParameter("arg0");	//车辆型号
			String arg1 = request.getParameter("arg1");	//手机号码
			String arg2 = request.getParameter("arg2");	//短信验证码

			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("orgId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("name不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentDate不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentTime不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carFrame)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carFrame不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(platNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("platNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(useCharater)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("useCharater不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg0)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg0不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg1不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg2不能为空!");
				renderJSON(baseBean);
				return;
			}
			createVehicleInfoVo.setOrgId(orgId);
			createVehicleInfoVo.setBusinessTypeId(BUSINESSTYPEID_DQ);
			createVehicleInfoVo.setName(name);
			createVehicleInfoVo.setIdTypeId(idTypeId);
			createVehicleInfoVo.setIdNumber(idNumber);
			createVehicleInfoVo.setMobile(mobile);
			createVehicleInfoVo.setAppointmentDate(appointmentDate);
			createVehicleInfoVo.setAppointmentTime(appointmentTime);
			createVehicleInfoVo.setCarTypeId(carTypeId);
			createVehicleInfoVo.setCarFrame(carFrame);
			createVehicleInfoVo.setPlatNumber(platNumber);
			createVehicleInfoVo.setBookerName(bookerName);
			createVehicleInfoVo.setBookerIdNumber(bookerIdNumber);
			createVehicleInfoVo.setBookerType(bookerType);
			createVehicleInfoVo.setRzjs(rzjs);
			createVehicleInfoVo.setOptlittleCar(optlittleCar);
			createVehicleInfoVo.setIndexType(indexType);
			createVehicleInfoVo.setIndexNo(indexNo);
			createVehicleInfoVo.setUseCharater(useCharater);
			createVehicleInfoVo.setArg0(arg0);
			createVehicleInfoVo.setArg1(arg1);
			createVehicleInfoVo.setArg2(arg2);

			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(createVehicleInfoVo);
			
			if("00".equals(refBean.getCode())){
        		baseBean.setCode("0000");
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getMsg());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】出租客运车辆使用性质变更Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 机动车迁出
	 * @author lifangyong
	 * @param request
	 * @param response
	 */
	@RequestMapping("createVehicleInfo_ZJ08.html")
    public void createVehicleInfo_ZJ08(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateVehicleInfoVo createVehicleInfoVo = new CreateVehicleInfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");	//预约地点ID
			String name = request.getParameter("name");	//姓名
			String idTypeId = request.getParameter("idTypeId");	//证件种类ID
			String idNumber = request.getParameter("idNumber");	//证件号码
			String mobile = request.getParameter("mobile");	//手机号码
			String appointmentDate = request.getParameter("appointmentDate");	//预约日期
			String appointmentTime = request.getParameter("appointmentTime");	//预约时间
			String carTypeId = request.getParameter("carTypeId");	//号牌种类ID【1.2】
			String carFrame = request.getParameter("carFrame");	//车架号
			String platNumber = request.getParameter("platNumber");	//车牌号或车架号
			String bookerName = request.getParameter("bookerName");	//预约人姓名
			String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码
			String bookerType = request.getParameter("bookerType");	//预约方式
			String rzjs = request.getParameter("rzjs");	//认证角色
			String optlittleCar = request.getParameter("optlittleCar");	//车辆产地
			String indexType = request.getParameter("indexType"); 	//指标类型
			String indexNo = request.getParameter("indexNo"); 	//指标号/公证号/车辆识别代号
			String useCharater = request.getParameter("useCharater");	//使用性质
			String arg0 = request.getParameter("arg0");	//车辆型号
			String arg1 = request.getParameter("arg1");	//手机号码
			String arg2 = request.getParameter("arg2");	//短信验证码

			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("orgId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("name不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("idNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentDate不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("appointmentTime不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carTypeId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(carFrame)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("carFrame不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(platNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("platNumber不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(useCharater)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("useCharater不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg0)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg0不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg1不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(arg2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("arg2不能为空!");
				renderJSON(baseBean);
				return;
			}
			createVehicleInfoVo.setOrgId(orgId);
			createVehicleInfoVo.setBusinessTypeId(BUSINESSTYPEID_ZJ08);
			createVehicleInfoVo.setName(name);
			createVehicleInfoVo.setIdTypeId(idTypeId);
			createVehicleInfoVo.setIdNumber(idNumber);
			createVehicleInfoVo.setMobile(mobile);
			createVehicleInfoVo.setAppointmentDate(appointmentDate);
			createVehicleInfoVo.setAppointmentTime(appointmentTime);
			createVehicleInfoVo.setCarTypeId(carTypeId);
			createVehicleInfoVo.setCarFrame(carFrame);
			createVehicleInfoVo.setPlatNumber(platNumber);
			createVehicleInfoVo.setBookerName(bookerName);
			createVehicleInfoVo.setBookerIdNumber(bookerIdNumber);
			createVehicleInfoVo.setBookerType(bookerType);
			createVehicleInfoVo.setRzjs(rzjs);
			createVehicleInfoVo.setOptlittleCar(optlittleCar);
			createVehicleInfoVo.setIndexType(indexType);
			createVehicleInfoVo.setIndexNo(indexNo);
			createVehicleInfoVo.setUseCharater(useCharater);
			createVehicleInfoVo.setArg0(arg0);
			createVehicleInfoVo.setArg1(arg1);
			createVehicleInfoVo.setArg2(arg2);

			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(createVehicleInfoVo);
			
			if("00".equals(refBean.getCode())){
        		baseBean.setCode("0000");
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getMsg());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】机动车迁出 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
}
