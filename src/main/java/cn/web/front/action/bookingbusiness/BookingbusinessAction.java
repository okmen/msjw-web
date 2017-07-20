package cn.web.front.action.bookingbusiness;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.booking.business.bean.BusinessTypeVO;
import cn.booking.business.bean.CarModelVo;
import cn.booking.business.bean.CarTypeVO;
import cn.booking.business.bean.CreateDriveinfoVo;
import cn.booking.business.bean.CreateTemporaryLicenseVehicleInfoVo;
import cn.booking.business.bean.CreateVehicleInfoVo;
import cn.booking.business.bean.IdTypeVO;
import cn.booking.business.bean.IndexTypeVo;
import cn.booking.business.bean.OrgVO;
import cn.booking.business.bean.SmsInfoVO;
import cn.booking.business.service.IBookingBusinessService;
import cn.handle.bean.vo.HandleTemplateVo;
import cn.message.model.wechat.TemplateDataModel;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
import cn.sdk.util.SXStringUtils;
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
	
	@Autowired
    @Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;
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
	 * @Description TODO(获取驾驶证预约信息)
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
		if (StringUtil.isBlank(businessTypeId)) {
			businessTypeId ="";
		}
		if (StringUtil.isBlank(organizationId)) {
			organizationId = "";
		}
		try {
			baseBean = bookingBusinessService.getDriveInfo(bookerNumber, idNumber, businessTypeId, organizationId);
			
		} catch (Exception e) {
			logger.error("getDriveInfo异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	/**
	 * 获取机动车预约信息 
	 * @Description TODO(获取机动车预约信息)
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
		if (StringUtil.isBlank(platNumber)) {
			platNumber ="";
		}
		if (StringUtil.isBlank(businessTypeId)) {
			businessTypeId ="";
		}
		if (StringUtil.isBlank(organizationId)) {
			organizationId = "";
		}
		try {
			baseBean = bookingBusinessService.getVehicleInfo(bookerNumber, idNumber, platNumber, businessTypeId, organizationId);
			
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
	 * 获取所有车辆类型
	 */
	@RequestMapping("getCarTypes")
	public void getCarTypes() {
		BaseBean baseBean = new BaseBean();
		try {
			List<CarTypeVO> carTypeVOs = bookingBusinessService.getCarTypes();
			baseBean.setCode(MsgCode.success);
			baseBean.setData(carTypeVOs);
			baseBean.setMsg("");
		} catch (Exception e) {
			logger.error("getCarTypes异常:" + e);
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
	 *  获取所有的业务(机动车，驾驶证)
	 * @param type 业务类型 ‘0’驾驶证业务 ‘1’机动车业务
	 * @param part 暂不需要传值 0’获取所有业务类型 ‘1’获取可统一接口
	 * @param arg0
	 * @param arg1
	 */
	@RequestMapping("getBusinessTypes")
	public void getBusinessTypes(String type, String part) {
		BaseBean baseBean = new BaseBean();
		boolean flag = false;
		if (StringUtil.isBlank(type)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("type 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(part)) {
			part = "";
		}
		try {
			List<BusinessTypeVO> businessTypes = bookingBusinessService.getBusinessTypes(type, part, "", "");
			baseBean.setData(businessTypes);
			baseBean.setMsg("查询成功");
			baseBean.setCode(MsgCode.success);
		} catch (Exception e) {
			logger.error("getBusinessTypes异常:" + e);
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
		/*if (StringUtil.isBlank(businessTypeId)) {
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
		}*/

		try {
			BaseBean  appTimes = bookingBusinessService.getAppTimes(date, orgId, businessTypeId, carTypeId,
					optlittleCar);
			if ("00".equals(baseBean.getCode())) {
				baseBean.setCode(MsgCode.success);
				baseBean.setData(appTimes.getData());
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("获取预约时间失败");
			}
		} catch (Exception e) {
			logger.error("获取可预约时间段异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 发送短信验证码 
	 * Description TODO(发送短信验证码)
	 * @param mobile 获取短信验证码的手机号
	 * @param idType 本次预约所填写的证件种类ID,由于六年免检没有证件种类，请传”0”
	 * @param lx 1:驾驶证业务  2:机动车业务（六年免检业务传3）  3:其他（包括六年免检业务）
	 * @param ip 用户客户端IP
	 * @param bookerType ‘0’非代办（或本人） ‘1’普通代办 ‘2’专业代办（企业代办）
	 * @param bookerName 代办人姓名： 0’非代办（或本人）情况请传本次预约业务填写的姓名
	 * @param bookerIdNumber 代办人身份证号码： 0’非代办（或本人）情况请传本次预约业务填写的证件号码
	 * @param idNumber 本次预约业务填写的证件号码
	 * @param codes 本次预约的具体业务类型（例如：JD01）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("simpleSendMessage")
	public void simpleSendMessage(String mobile,String idType,String lx,String bookerType,String bookerName,String bookerIdNumber,String idNumber,String codes,HttpServletRequest request){
		BaseBean baseBean = new BaseBean();
		
		try{
			if (StringUtil.isBlank(mobile)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(idType)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(lx)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerType)) {//‘0’非代办（或本人） ‘1’普通代办 ‘2’专业代办（企业代办）
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约方式不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("代办人身份证号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(idNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(codes)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务代码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			String ip = getIp2(request);
			
			SmsInfoVO vo = bookingBusinessService.simpleSendMessage(mobile, idType, lx, ip, bookerType, bookerName, bookerIdNumber, idNumber, codes);
			if(vo != null && "00".equals(vo.getCode())){
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg("验证码已发送");
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(vo.getMsg());
			}
			
		} catch (Exception e) {
			logger.error("发送短信验证码Action异常:" + e);
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
		String modelName = request.getParameter("modelName");  //预约方式
		String bookerMobile=request.getParameter("bookerMobile");  //预约手机号
		String useCharater = request.getParameter("useCharater");  //使用性质
		String msgNumber = request.getParameter("msgNumber");  //短信验证码
		String indexType = request.getParameter("indexType");  //指标类型
		String indexNo = request.getParameter("indexNo");  //指标号/公证号/车辆识别代号
		String optlittleCar= request.getParameter("optlittleCar");  //车辆产地
		String rzjs =request.getParameter("rzjs");  //认证角色
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
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
			vo.setOptlittleCar(optlittleCar); 	//车辆产地  可为空
			vo.setIndexType(indexType); 	//指标类型   可为空
			vo.setIndexNo(indexNo); 		//指标号/公证号/车辆识别代号  可为空
			vo.setUseCharater(useCharater); 	//使用性质
			vo.setModelName(modelName);  //车辆型号  可为空
			vo.setBookerMobile(bookerMobile); 	//手机号码
			vo.setMsgNumber(msgNumber);	 	//短信验证码
			vo.setRzjs(rzjs);    //可为空
			
			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(vo);
			
			if("00".equals(refBean.getCode())){
        		baseBean.setCode("0000");
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getMsg());
        		baseBean.setData(refBean.getData());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】换领机动车登记证书 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 满分学习
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveInfo_ZJ10")
    public void createDriveInfo_ZJ10(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerMobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			String code = refBean.getCode();
			String msg = refBean.getMsg();
			if ("00".equals(code)) {
				String waterNumber = (String) refBean.getData();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("满分学习预约", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg +":"+refBean.getData());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	/**
	 * 持军队、武装警察部队机动车驾驶证申领
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveInfo_ZJ11")
    public void createDriveInfo_ZJ11(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerMobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			String code = refBean.getCode();
			String msg = refBean.getMsg();
			if ("00".equals(code)) {
				String waterNumber = (String) refBean.getData();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("持军队、武装警察部队机动车驾驶证申领", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg +":"+refBean.getData());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 香港机动车驾驶证免试换证
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveInfo_ZJ13")
    public void createDriveInfo_ZJ13(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerMobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			String code = refBean.getCode();
			String msg = refBean.getMsg();
			if ("00".equals(code)) {
				String waterNumber = (String) refBean.getData();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("香港机动车驾驶证免试换证", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg +":"+refBean.getData());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 持境外驾驶证申请换证
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveInfo_ZJ17")
    public void createDriveInfo_ZJ17(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerMobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			String code = refBean.getCode();
			String msg = refBean.getMsg();
			if ("00".equals(code)) {
				String waterNumber = (String) refBean.getData();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("持境外驾驶证申请换证", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg +":"+refBean.getData());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 其他业务(驾驶证)
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveInfo_ZJ20")
    public void createDriveInfo_ZJ20(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerMobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			String code = refBean.getCode();
			String msg = refBean.getMsg();
			if ("00".equals(code)) {
				String waterNumber = (String) refBean.getData();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("其他业务(驾驶证)", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg +":"+refBean.getData());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 恢复驾驶资格（逾期一年以上未换证类）
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveInfo_ZJ21")
    public void createDriveInfo_ZJ21(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerMobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			String code = refBean.getCode();
			String msg = refBean.getMsg();
			if ("00".equals(code)) {
				String waterNumber = (String) refBean.getData();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("恢复驾驶资格（逾期一年以上未换证类）", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg +":"+refBean.getData());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 恢复驾驶资格（逾期一年以上未体检类）
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveInfo_ZJ22")
    public void createDriveInfo_ZJ22(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型Id不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerMobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			String code = refBean.getCode();
			String msg = refBean.getMsg();
			if ("00".equals(code)) {
				String waterNumber = (String) refBean.getData();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("恢复驾驶资格（逾期一年以上未体检类）", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg +":"+refBean.getData());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】驾驶证预约 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 临时机动车驾驶许可申领
	 * @Description TODO(ZJ16临时机动车驾驶许可申领)
	 * @param request
	 * @param response
	 */
	@RequestMapping("createDriveinfo_ZJ16")
	public void createDriveinfo_ZJ16(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
    	CreateDriveinfoVo createDriveinfoVo = new CreateDriveinfoVo();
    	try {
	    	String orgId = request.getParameter("orgId");
	    	String businessTypeId = request.getParameter("businessTypeId");
			String name = request.getParameter("name");
			String idTypeId = request.getParameter("idTypeId");
			String idNumber = request.getParameter("idNumber");
			String mobile = request.getParameter("mobile");
			String appointmentDate = request.getParameter("appointmentDate");
			String appointmentTime = request.getParameter("appointmentTime");
			String bookerName = request.getParameter("bookerName");
			String bookerIdNumber = request.getParameter("bookerIdNumber");
			String bookerType = request.getParameter("bookerType");
			String bookerMobile = request.getParameter("bookerMobile");
			String msgNumber = request.getParameter("msgNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			
			if(StringUtil.isBlank(businessTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idTypeId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(idNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				bookerMobile = mobile;
			}
			if(StringUtil.isBlank(msgNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("短信验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(appointmentTime)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(bookerName)) {
				bookerName ="";
			}
			if (StringUtil.isBlank(bookerIdNumber)) {
				bookerIdNumber = "";
			}
			if (StringUtil.isBlank(bookerType)) {
				bookerType = "";
			}
			createDriveinfoVo.setOrgId(orgId);
			createDriveinfoVo.setBusinessTypeId(businessTypeId);
			createDriveinfoVo.setName(name);
			createDriveinfoVo.setIdTypeId(idTypeId);
			createDriveinfoVo.setMobile(mobile);
			createDriveinfoVo.setIdNumber(idNumber);
			createDriveinfoVo.setBookerMobile(mobile);//预约手机号
			createDriveinfoVo.setBookerMobile(bookerMobile);
			createDriveinfoVo.setMsgNumber(msgNumber);
			createDriveinfoVo.setAppointmentDate(appointmentDate);
			createDriveinfoVo.setAppointmentTime(appointmentTime);
			createDriveinfoVo.setBookerName(bookerName);
			createDriveinfoVo.setBookerIdNumber(bookerIdNumber);
			createDriveinfoVo.setBookerType(bookerType);
			//接口调用
			BaseBean refBean = bookingBusinessService.createDriveinfo(createDriveinfoVo);
			
			String code = refBean.getCode();
			if("00".equals(code)){
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(refBean.getMsg());
				baseBean.setData(refBean.getData());
				/*//预约成功发送微信模板消息
				if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(vo.getSourceOfCertification())){
					try {
						String waterNumber = baseBean.getData().toString();
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.xxxxxxxxxxxxxxx, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String openId = vo.getOpenId();
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下：","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车业务","#212121"));
						map.put("business", new TemplateDataModel().new Property("核发临牌","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(vo.getAppointmentDate() + " " + vo.getAppointmentTime(),"#212121"));
						map.put("address", new TemplateDataModel().new Property(organizationName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}*/
			}else if("01".equals(code) || "02".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getMsg());
			}else if("03".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getData().toString());
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(refBean.getMsg());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】临时机动车驾驶许可申领 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug("【预约类服务】临时机动车驾驶许可申领Action返回结果:" + JSON.toJSONString(baseBean));
	}
	
	/**
	 * 核发临牌
	 * @Description: TODO(JD34核发临牌)
	 * @param vo 核发临牌Vo
	 */
	@RequestMapping("createTemporaryLicenseVehicleInfo")
	public void createTemporaryLicenseVehicleInfo(CreateTemporaryLicenseVehicleInfoVo vo){
		BaseBean baseBean = new BaseBean();
		
		try {
			if (StringUtil.isBlank(vo.getBusinessTypeId())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getName())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车主姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getIdTypeId())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getIdNumber())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getMobile())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				vo.setBookerMobile(vo.getMobile());
			}
			if (StringUtil.isBlank(vo.getMsgNumber())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getAdress())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getChineseBrand())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("中文品牌不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			String passengerNumber = vo.getPassengerNumber();
			if (StringUtil.isBlank(passengerNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("载客人数不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				//请确保载客人数是数值的字符串：“整数值”
				if(!StringUtils.isNumeric(passengerNumber) || passengerNumber.length() > 3){
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("载客人数只能为纯数字（最多3位）");
					renderJSON(baseBean);
					return;
				}
			}
			
			if (StringUtil.isBlank(vo.getEngineNumber())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("发动机号不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getCarTypeId())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getVehicleType())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆型号不能为空!");
				renderJSON(baseBean);
				return;
			}
			/*if (StringUtil.isBlank(vo.getUseCharater())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("使用性质不能为空!");
				renderJSON(baseBean);
				return;
			}*/
			if (StringUtil.isBlank(vo.getCarFrame())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车身架号不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				vo.setPlatNumber(vo.getCarFrame());
			}
			if (StringUtil.isBlank(vo.getOrgId())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约地点ID不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getAppointmentDate())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			if (StringUtil.isBlank(vo.getAppointmentTime())) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//参数可为空赋值
			if (StringUtil.isBlank(vo.getBookerName())) {//预约人
				vo.setBookerName("");
			}
			if (StringUtil.isBlank(vo.getBookerIdNumber())) {//预约人身份证号码
				vo.setBookerIdNumber("");
			}
			if (StringUtil.isBlank(vo.getBookerType())) {//预约方式 0-本人
				vo.setBookerType("");
			}
			if (StringUtil.isBlank(vo.getRzjs())) {//认证角色 2-企业星级用户，其他，非企业星级用户
				vo.setRzjs("");
			}
			
			//接口调用
			baseBean = bookingBusinessService.createTemporaryLicenseVehicleInfo(vo);
			
			/*//预约成功发送微信模板消息
			if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(vo.getSourceOfCertification())){
				try {
					String waterNumber = baseBean.getData().toString();
					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.xxxxxxxxxxxxxxx, waterNumber, DateUtil2.date2str(new Date()));
					baseBean.setData(handleTemplateVo);
					String url = HandleTemplateVo.getUrl(handleTemplateVo, bookingBusinessService.getTemplateSendUrl());
					String openId = vo.getOpenId();
					String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下：","#212121"));
					map.put("businessType", new TemplateDataModel().new Property("机动车业务","#212121"));
					map.put("business", new TemplateDataModel().new Property("核发临牌","#212121"));
					map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
					map.put("time", new TemplateDataModel().new Property(vo.getAppointmentDate() + " " + vo.getAppointmentTime(),"#212121"));
					map.put("address", new TemplateDataModel().new Property(organizationName,"#212121"));
					map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
			}*/
			
		} catch (Exception e) {
			logger.error("核发临牌 Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 核发校车标牌
	 * @Description TODO(JD27核发校车标牌)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="createVehicleInfo_JD27")
	public void createVehicleInfo_JD27(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
    	String orgId = request.getParameter("orgId");					//预约地点ID
    	String businessTypeId = request.getParameter("businessTypeId"); //预约类型ID
		String name = request.getParameter("name");						//姓名
		String idTypeId = request.getParameter("idTypeId");				//证件种类ID
		String idNumber = request.getParameter("idNumber");				//证件号码
		String mobile = request.getParameter("mobile");					//手机号码
		String appointmentDate = request.getParameter("appointmentDate");//预约日期
		String appointmentTime = request.getParameter("appointmentTime");//预约时间
		String carTypeId = request.getParameter("carTypeId");			//号牌种类ID
		String carFrame = request.getParameter("carFrame");				//车架号
		String platNumber = request.getParameter("platNumber");			//车牌号或车架号
		String bookerName = request.getParameter("bookerName");			//预约人姓名*
		String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码*
		String bookerType = request.getParameter("bookerType");			//预约方式*
		String rzjs =request.getParameter("rzjs");						//认证角色*
		String optlittleCar= request.getParameter("optlittleCar");		//车辆产地*
		String indexType = request.getParameter("indexType");			//指标类型*
		String indexNo = request.getParameter("indexNo");				//指标号/公证号/车辆识别代号*
		String useCharater = request.getParameter("useCharater");		//使用性质
		String modelName = request.getParameter("modelName");			//车辆型号*
		String bookerMobile=request.getParameter("bookerMobile");		//预约手机号
		String msgNumber = request.getParameter("msgNumber");			//短信验证码
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
			if(!bool) return;
			
			CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
			vo.setOrgId(orgId);						//预约地点Id
			vo.setBusinessTypeId(businessTypeId);	//业务类型id
			vo.setName(name);						//姓名
			vo.setIdTypeId(idTypeId);				//证件种类id
			vo.setIdNumber(idNumber);				//证件号码
			vo.setMobile(mobile);					//手机号码
			vo.setAppointmentDate(appointmentDate);	//预约日期
			vo.setAppointmentTime(appointmentTime);	//预约时间
			vo.setCarTypeId(carTypeId);				//车辆类型 号牌种类  小型汽车（蓝色）
			vo.setCarFrame(carFrame);				//车架号
			vo.setPlatNumber(platNumber);			//车牌号或车架号
			vo.setBookerName(bookerName);			//预约人姓名*
			vo.setBookerIdNumber(bookerIdNumber);	//预约人身份证号码*
			vo.setBookerType(bookerType);			//预约方式*  ‘0’ 非代办(本人)，’1’ 普通代办 ‘2’专业代办
			vo.setRzjs(SXStringUtils.toString(rzjs));						//认证角色*
			vo.setOptlittleCar(SXStringUtils.toString(optlittleCar));		//车辆产地*
			vo.setIndexType(SXStringUtils.toString(indexType));				//指标类型*
			vo.setIndexNo(SXStringUtils.toString(indexNo));					//指标号/公证号/车辆识别代号*
			vo.setUseCharater(useCharater);			//使用性质
			vo.setModelName(SXStringUtils.toString(modelName));				//车辆型号*
			vo.setBookerMobile(bookerMobile);		//手机号码
			vo.setMsgNumber(msgNumber);				//短信验证码
			
			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(vo);
			
			String code = refBean.getCode();
			if("00".equals(code)){
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(refBean.getMsg());
				baseBean.setData(refBean.getData());
				/*//预约成功发送微信模板消息
				if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(vo.getSourceOfCertification())){
					try {
						String waterNumber = baseBean.getData().toString();
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.xxxxxxxxxxxxxxx, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String openId = vo.getOpenId();
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下：","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车业务","#212121"));
						map.put("business", new TemplateDataModel().new Property("核发临牌","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(vo.getAppointmentDate() + " " + vo.getAppointmentTime(),"#212121"));
						map.put("address", new TemplateDataModel().new Property(organizationName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}*/
			}else if("01".equals(code) || "02".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getMsg());
			}else if("03".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getData().toString());
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(refBean.getMsg());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】核发校车标牌 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 抵押/解押登记现场办理
	 * @Description TODO(JD37抵押/解押登记现场办理)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="createVehicleInfo_JD37")
	public void createVehicleInfo_JD37(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
		
		String orgId = request.getParameter("orgId");					//预约地点ID
    	String businessTypeId = request.getParameter("businessTypeId"); //预约类型ID
		String name = request.getParameter("name");						//姓名
		String idTypeId = request.getParameter("idTypeId");				//证件种类ID
		String idNumber = request.getParameter("idNumber");				//证件号码
		String mobile = request.getParameter("mobile");					//手机号码
		String appointmentDate = request.getParameter("appointmentDate");//预约日期
		String appointmentTime = request.getParameter("appointmentTime");//预约时间
		String carTypeId = request.getParameter("carTypeId");			//号牌种类ID
		String carFrame = request.getParameter("carFrame");				//车架号
		String platNumber = request.getParameter("platNumber");			//车牌号或车架号
		String bookerName = request.getParameter("bookerName");			//预约人姓名*
		String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码*
		String bookerType = request.getParameter("bookerType");			//预约方式*
		String rzjs =request.getParameter("rzjs");						//认证角色*
		String optlittleCar= request.getParameter("optlittleCar");		//车辆产地*
		String indexType = request.getParameter("indexType");			//指标类型*
		String indexNo = request.getParameter("indexNo");				//指标号/公证号/车辆识别代号*
		String useCharater = request.getParameter("useCharater");		//使用性质
		String modelName = request.getParameter("modelName");			//车辆型号*
		String bookerMobile=request.getParameter("bookerMobile");		//预约手机号
		String msgNumber = request.getParameter("msgNumber");			//短信验证码
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
			if(!bool) return;
			
			CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
			vo.setOrgId(orgId);						//预约地点Id
			vo.setBusinessTypeId(businessTypeId);	//业务类型id
			vo.setName(name);						//姓名
			vo.setIdTypeId(idTypeId);				//证件种类id
			vo.setIdNumber(idNumber);				//证件号码
			vo.setMobile(mobile);					//手机号码
			vo.setAppointmentDate(appointmentDate);	//预约日期
			vo.setAppointmentTime(appointmentTime);	//预约时间
			vo.setCarTypeId(carTypeId);				//车辆类型 号牌种类  小型汽车（蓝色）
			vo.setCarFrame(carFrame);				//车架号
			vo.setPlatNumber(platNumber);			//车牌号或车架号
			vo.setBookerName(bookerName);			//预约人姓名*
			vo.setBookerIdNumber(bookerIdNumber);	//预约人身份证号码*
			vo.setBookerType(bookerType);			//预约方式*  ‘0’ 非代办(本人)，’1’ 普通代办 ‘2’专业代办
			vo.setRzjs(SXStringUtils.toString(rzjs));						//认证角色*
			vo.setOptlittleCar(SXStringUtils.toString(optlittleCar));		//车辆产地*
			vo.setIndexType(SXStringUtils.toString(indexType));				//指标类型*
			vo.setIndexNo(SXStringUtils.toString(indexNo));					//指标号/公证号/车辆识别代号*
			vo.setUseCharater(useCharater);			//使用性质
			vo.setModelName(SXStringUtils.toString(modelName));				//车辆型号*
			vo.setBookerMobile(bookerMobile);		//手机号码
			vo.setMsgNumber(msgNumber);				//短信验证码
			
			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(vo);
			
			String code = refBean.getCode();
			if("00".equals(code)){
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(refBean.getMsg());
				baseBean.setData(refBean.getData());
				/*//预约成功发送微信模板消息
				if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(vo.getSourceOfCertification())){
					try {
						String waterNumber = baseBean.getData().toString();
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.xxxxxxxxxxxxxxx, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String openId = vo.getOpenId();
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下：","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车业务","#212121"));
						map.put("business", new TemplateDataModel().new Property("核发临牌","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(vo.getAppointmentDate() + " " + vo.getAppointmentTime(),"#212121"));
						map.put("address", new TemplateDataModel().new Property(organizationName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}*/
			}else if("01".equals(code) || "02".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getMsg());
			}else if("03".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getData().toString());
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(refBean.getMsg());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】抵押/解押登记现场办理:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 机动车委托异地年审现场办理
	 * @Description TODO(JD38机动车委托异地年审现场办理)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="createVehicleInfo_JD38")
	public void createVehicleInfo_JD38(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
		
		String orgId = request.getParameter("orgId");					//预约地点ID
    	String businessTypeId = request.getParameter("businessTypeId"); //预约类型ID
		String name = request.getParameter("name");						//姓名
		String idTypeId = request.getParameter("idTypeId");				//证件种类ID
		String idNumber = request.getParameter("idNumber");				//证件号码
		String mobile = request.getParameter("mobile");					//手机号码
		String appointmentDate = request.getParameter("appointmentDate");//预约日期
		String appointmentTime = request.getParameter("appointmentTime");//预约时间
		String carTypeId = request.getParameter("carTypeId");			//号牌种类ID
		String carFrame = request.getParameter("carFrame");				//车架号
		String platNumber = request.getParameter("platNumber");			//车牌号或车架号
		String bookerName = request.getParameter("bookerName");			//预约人姓名*
		String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码*
		String bookerType = request.getParameter("bookerType");			//预约方式*
		String rzjs =request.getParameter("rzjs");						//认证角色*
		String optlittleCar= request.getParameter("optlittleCar");		//车辆产地*
		String indexType = request.getParameter("indexType");			//指标类型*
		String indexNo = request.getParameter("indexNo");				//指标号/公证号/车辆识别代号*
		String useCharater = request.getParameter("useCharater");		//使用性质
		String modelName = request.getParameter("modelName");			//车辆型号*
		String bookerMobile=request.getParameter("bookerMobile");		//预约手机号
		String msgNumber = request.getParameter("msgNumber");			//短信验证码
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
			if(!bool) return;
			
			CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
			vo.setOrgId(orgId);						//预约地点Id
			vo.setBusinessTypeId(businessTypeId);	//业务类型id
			vo.setName(name);						//姓名
			vo.setIdTypeId(idTypeId);				//证件种类id
			vo.setIdNumber(idNumber);				//证件号码
			vo.setMobile(mobile);					//手机号码
			vo.setAppointmentDate(appointmentDate);	//预约日期
			vo.setAppointmentTime(appointmentTime);	//预约时间
			vo.setCarTypeId(carTypeId);				//车辆类型 号牌种类  小型汽车（蓝色）
			vo.setCarFrame(carFrame);				//车架号
			vo.setPlatNumber(platNumber);			//车牌号或车架号
			vo.setBookerName(bookerName);			//预约人姓名*
			vo.setBookerIdNumber(bookerIdNumber);	//预约人身份证号码*
			vo.setBookerType(bookerType);			//预约方式*  ‘0’ 非代办(本人)，’1’ 普通代办 ‘2’专业代办
			vo.setRzjs(SXStringUtils.toString(rzjs));						//认证角色*
			vo.setOptlittleCar(SXStringUtils.toString(optlittleCar));		//车辆产地*
			vo.setIndexType(SXStringUtils.toString(indexType));				//指标类型*
			vo.setIndexNo(SXStringUtils.toString(indexNo));					//指标号/公证号/车辆识别代号*
			vo.setUseCharater(useCharater);			//使用性质
			vo.setModelName(SXStringUtils.toString(modelName));				//车辆型号*
			vo.setBookerMobile(bookerMobile);		//手机号码
			vo.setMsgNumber(msgNumber);				//短信验证码
			
			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(vo);
			
			String code = refBean.getCode();
			if("00".equals(code)){
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(refBean.getMsg());
				baseBean.setData(refBean.getData());
				/*//预约成功发送微信模板消息
				if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(vo.getSourceOfCertification())){
					try {
						String waterNumber = baseBean.getData().toString();
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.xxxxxxxxxxxxxxx, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String openId = vo.getOpenId();
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下：","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车业务","#212121"));
						map.put("business", new TemplateDataModel().new Property("核发临牌","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(vo.getAppointmentDate() + " " + vo.getAppointmentTime(),"#212121"));
						map.put("address", new TemplateDataModel().new Property(organizationName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}*/
			}else if("01".equals(code) || "02".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getMsg());
			}else if("03".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getData().toString());
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(refBean.getMsg());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】机动车委托异地年审现场办理 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 转出、注销恢复
	 * @Description TODO(JD41转出、注销恢复)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="createVehicleInfo_JD41")
	public void createVehicleInfo_JD41(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();		//创建返回结果
		
		String orgId = request.getParameter("orgId");					//预约地点ID
    	String businessTypeId = request.getParameter("businessTypeId"); //预约类型ID
		String name = request.getParameter("name");						//姓名
		String idTypeId = request.getParameter("idTypeId");				//证件种类ID
		String idNumber = request.getParameter("idNumber");				//证件号码
		String mobile = request.getParameter("mobile");					//手机号码
		String appointmentDate = request.getParameter("appointmentDate");//预约日期
		String appointmentTime = request.getParameter("appointmentTime");//预约时间
		String carTypeId = request.getParameter("carTypeId");			//号牌种类ID
		String carFrame = request.getParameter("carFrame");				//车架号
		String platNumber = request.getParameter("platNumber");			//车牌号或车架号
		String bookerName = request.getParameter("bookerName");			//预约人姓名*
		String bookerIdNumber = request.getParameter("bookerIdNumber");	//预约人身份证号码*
		String bookerType = request.getParameter("bookerType");			//预约方式*
		String rzjs =request.getParameter("rzjs");						//认证角色*
		String optlittleCar= request.getParameter("optlittleCar");		//车辆产地*
		String indexType = request.getParameter("indexType");			//指标类型*
		String indexNo = request.getParameter("indexNo");				//指标号/公证号/车辆识别代号*
		String useCharater = request.getParameter("useCharater");		//使用性质
		String modelName = request.getParameter("modelName");			//车辆型号*
		String bookerMobile=request.getParameter("bookerMobile");		//预约手机号
		String msgNumber = request.getParameter("msgNumber");			//短信验证码
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
			if(!bool) return;
			
			CreateVehicleInfoVo vo = new CreateVehicleInfoVo();
			vo.setOrgId(orgId);						//预约地点Id
			vo.setBusinessTypeId(businessTypeId);	//业务类型id
			vo.setName(name);						//姓名
			vo.setIdTypeId(idTypeId);				//证件种类id
			vo.setIdNumber(idNumber);				//证件号码
			vo.setMobile(mobile);					//手机号码
			vo.setAppointmentDate(appointmentDate);	//预约日期
			vo.setAppointmentTime(appointmentTime);	//预约时间
			vo.setCarTypeId(carTypeId);				//车辆类型 号牌种类  小型汽车（蓝色）
			vo.setCarFrame(carFrame);				//车架号
			vo.setPlatNumber(platNumber);			//车牌号或车架号
			vo.setBookerName(bookerName);			//预约人姓名*
			vo.setBookerIdNumber(bookerIdNumber);	//预约人身份证号码*
			vo.setBookerType(bookerType);			//预约方式*  ‘0’ 非代办(本人)，’1’ 普通代办 ‘2’专业代办
			vo.setRzjs(SXStringUtils.toString(rzjs));						//认证角色*
			vo.setOptlittleCar(SXStringUtils.toString(optlittleCar));		//车辆产地*
			vo.setIndexType(SXStringUtils.toString(indexType));				//指标类型*
			vo.setIndexNo(SXStringUtils.toString(indexNo));					//指标号/公证号/车辆识别代号*
			vo.setUseCharater(useCharater);			//使用性质
			vo.setModelName(SXStringUtils.toString(modelName));				//车辆型号*
			vo.setBookerMobile(bookerMobile);		//手机号码
			vo.setMsgNumber(msgNumber);				//短信验证码
			
			//接口调用
			BaseBean refBean = bookingBusinessService.createVehicleInfo(vo);
			
			String code = refBean.getCode();
			if("00".equals(code)){
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(refBean.getMsg());
				baseBean.setData(refBean.getData());
				/*//预约成功发送微信模板消息
				if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(vo.getSourceOfCertification())){
					try {
						String waterNumber = baseBean.getData().toString();
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, BusinessType.xxxxxxxxxxxxxxx, waterNumber, DateUtil2.date2str(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String openId = vo.getOpenId();
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下：","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车业务","#212121"));
						map.put("business", new TemplateDataModel().new Property("核发临牌","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(vo.getAppointmentDate() + " " + vo.getAppointmentTime(),"#212121"));
						map.put("address", new TemplateDataModel().new Property(organizationName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}*/
			}else if("01".equals(code) || "02".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getMsg());
			}else if("03".equals(code)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg(refBean.getData().toString());
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(refBean.getMsg());
			}
		} catch (Exception e) {
			logger.error("【预约类服务】转出、注销恢复 Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	
	/**
	 * 取消驾驶证预约
	 * @param request
	 * @param response
	 */
	@RequestMapping("cancleDriveInfo")
    public void cancleDriveInfo(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	try {
	    	String mobile = request.getParameter("mobile");
	    	String businessType = request.getParameter("businessType");
			String bookerNumber = request.getParameter("bookNumber");
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			if(StringUtil.isBlank(mobile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(bookerNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			if(StringUtil.isBlank(businessType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("业务类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//接口调用
			SmsInfoVO smsInfoVO = bookingBusinessService.cancel(businessType, bookerNumber, mobile); 
			
			if("00".equals(smsInfoVO.getCode())){
				String waterNumber  = smsInfoVO.getResult();
				String msg = smsInfoVO.getMsg();
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "nR0-6Nfw9VvmEEcq8Kih24j1Q5X0e7ozbM5dqkV1BXo";
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您好，您的业务办理申请已申请，具体信息如下：", "#212121"));
						tmap.put("keyword1",
								new TemplateDataModel().new Property(DateUtil2.date2dayStr(new Date()), "#212121"));
						tmap.put("keyword2", new TemplateDataModel().new Property("取消驾驶证预约业务", "#212121"));
						tmap.put("keyword3", new TemplateDataModel().new Property("待受理", "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}else{
					baseBean.setData(waterNumber);
				}
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(smsInfoVO.getMsg());
        	}
		} catch (Exception e) {
			logger.error("【预约类服务】取消驾驶证预约业务Action异常:"+e);
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
		String modelName = request.getParameter("modelName");  //预约方式
		String bookerMobile=request.getParameter("bookerMobile");  //预约手机号
		String useCharater = request.getParameter("useCharater");  //使用性质
		String msgNumber = request.getParameter("msgNumber");  //短信验证码
		String indexType = request.getParameter("indexType");  //指标类型
		String indexNo = request.getParameter("indexNo");  //指标号/公证号/车辆识别代号
		String optlittleCar= request.getParameter("optlittleCar");  //车辆产地
		String rzjs =request.getParameter("rzjs");  //认证角色
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
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
			vo.setOptlittleCar(optlittleCar); 	//车辆产地  可为空
			vo.setIndexType(indexType); 	//指标类型   可为空
			vo.setIndexNo(indexNo); 		//指标号/公证号/车辆识别代号  可为空
			vo.setUseCharater(useCharater); 	//使用性质
			vo.setModelName(modelName);  //车辆型号  可为空
			vo.setBookerMobile(bookerMobile); 	//手机号码
			vo.setMsgNumber(msgNumber);	 	//短信验证码
			vo.setRzjs(rzjs);    //可为空
			
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
		String modelName = request.getParameter("modelName");  //预约方式
		String bookerMobile=request.getParameter("bookerMobile");  //预约手机号
		String useCharater = request.getParameter("useCharater");  //使用性质
		String msgNumber = request.getParameter("msgNumber");  //短信验证码
		String indexType = request.getParameter("indexType");  //指标类型
		String indexNo = request.getParameter("indexNo");  //指标号/公证号/车辆识别代号
		String optlittleCar= request.getParameter("optlittleCar");  //车辆产地
		String rzjs =request.getParameter("rzjs");  //认证角色
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
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
			vo.setOptlittleCar(optlittleCar); 	//车辆产地  可为空
			vo.setIndexType(indexType); 	//指标类型   可为空
			vo.setIndexNo(indexNo); 		//指标号/公证号/车辆识别代号  可为空
			vo.setUseCharater(useCharater); 	//使用性质
			vo.setModelName(modelName);  //车辆型号  可为空
			vo.setBookerMobile(bookerMobile); 	//手机号码
			vo.setMsgNumber(msgNumber);	 	//短信验证码
			vo.setRzjs(rzjs);    //可为空
			
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
		String modelName = request.getParameter("modelName");  //预约方式
		String bookerMobile=request.getParameter("bookerMobile");  //预约手机号
		String useCharater = request.getParameter("useCharater");  //使用性质
		String msgNumber = request.getParameter("msgNumber");  //短信验证码
		String indexType = request.getParameter("indexType");  //指标类型
		String indexNo = request.getParameter("indexNo");  //指标号/公证号/车辆识别代号
		String optlittleCar= request.getParameter("optlittleCar");  //车辆产地
		String rzjs =request.getParameter("rzjs");  //认证角色
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
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
			vo.setOptlittleCar(optlittleCar); 	//车辆产地  可为空
			vo.setIndexType(indexType); 	//指标类型   可为空
			vo.setIndexNo(indexNo); 		//指标号/公证号/车辆识别代号  可为空
			vo.setUseCharater(useCharater); 	//使用性质
			vo.setModelName(modelName);  //车辆型号  可为空
			vo.setBookerMobile(bookerMobile); 	//手机号码
			vo.setMsgNumber(msgNumber);	 	//短信验证码
			vo.setRzjs(rzjs);    //可为空
			
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
		String modelName = request.getParameter("modelName");  //预约方式
		String bookerMobile=request.getParameter("bookerMobile");  //预约手机号
		String useCharater = request.getParameter("useCharater");  //使用性质
		String msgNumber = request.getParameter("msgNumber");  //短信验证码
		String indexType = request.getParameter("indexType");  //指标类型
		String indexNo = request.getParameter("indexNo");  //指标号/公证号/车辆识别代号
		String optlittleCar= request.getParameter("optlittleCar");  //车辆产地
		String rzjs =request.getParameter("rzjs");  //认证角色
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile");
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
			vo.setOptlittleCar(optlittleCar); 	//车辆产地  可为空
			vo.setIndexType(indexType); 	//指标类型   可为空
			vo.setIndexNo(indexNo); 		//指标号/公证号/车辆识别代号  可为空
			vo.setUseCharater(useCharater); 	//使用性质
			vo.setModelName(modelName);  //车辆型号  可为空
			vo.setBookerMobile(bookerMobile); 	//手机号码
			vo.setMsgNumber(msgNumber);	 	//短信验证码
			vo.setRzjs(rzjs);    //可为空
			
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
			logger.error("【预约类服务】出租客运车辆使用性质变更Action异常:"+e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	
	/**
	 * 获取车辆型号列表
	 */
	@RequestMapping("getCarModelArray")
	public void getCarModelArray(){
		BaseBean baseBean = new BaseBean();		//创建返回结果
		Map<String, String> map = new LinkedHashMap<>();
		map.put("K31", "小型普通客车");
		map.put("K32", "小型越野客车");
		map.put("K33", "小型轿车");
		map.put("K34", "小型专用客车");
		map.put("K41", "微型普通客车");
		map.put("K42", "微型越野客车");
		map.put("K43", "微型轿车");
		map.put("M11", "普通正三轮摩托车");
		map.put("M12", "轻便正三轮摩托车");
		map.put("M13", "正三轮载客摩托车");
		map.put("M14", "正三轮载货摩托车");
		map.put("M15", "侧三轮摩托车");
		map.put("M21", "普通二轮摩托车");
		map.put("M22", "轻便二轮摩托车");
		map.put("N11", "三轮汽车");
		map.put("K11", "大型普通客车");
		map.put("K12", "大型双层客车");
		map.put("K13", "大型卧铺客车");
		map.put("K14", "大型铰接客车");
		map.put("K15", "大型越野客车");
		map.put("K16", "大型轿车");
		map.put("K17", "大型专用客车");
		map.put("K21", "中型普通客车");
		map.put("K22", "中型双层客车");
		map.put("K23", "中型卧铺客车");
		map.put("K24", "中型铰接客车");
		map.put("K25", "中型越野客车");
		map.put("K26", "中型轿车");
		map.put("K27", "中型专用客车");
		map.put("B11", "重型普通半挂车");
		map.put("B12", "重型厢式半挂车");
		map.put("B13", "重型罐式半挂车");
		map.put("B14", "重型平板半挂车");
		map.put("B15", "重型集装箱半挂车");
		map.put("B16", "重型自卸半挂车");
		map.put("B17", "重型特殊结构半挂车");
		map.put("B18", "重型仓栅式半挂车");
		map.put("B19", "重型旅居半挂车");
		map.put("B1A", "重型专项作业半挂车");
		map.put("B1B", "重型低平板半挂车");
		map.put("B21", "中型普通半挂车");
		map.put("B22", "中型厢式半挂车");
		map.put("B23", "中型罐式半挂车");
		map.put("B24", "中型平板半挂车");
		map.put("B25", "中型集装箱半挂车");
		map.put("B26", "中型自卸半挂车");
		map.put("B27", "中型特殊结构半挂车");
		map.put("B28", "中型仓栅式半挂车");
		map.put("B29", "中型旅居半挂车");
		map.put("B2A", "中型专项作业半挂车");
		map.put("B2B", "中型低平板半挂车");
		map.put("B31", "轻型普通半挂车");
		map.put("B32", "轻型厢式半挂车");
		map.put("B33", "轻型罐式半挂车");
		map.put("B34", "轻型平板半挂车");
		map.put("B35", "轻型自卸半挂车");
		map.put("B36", "轻型仓栅式半挂车");
		map.put("B37", "轻型旅居半挂车");
		map.put("B38", "轻型专项作业半挂车");
		map.put("B39", "轻型低平板半挂车");
		map.put("D11", "无轨电车");
		map.put("D12", "有轨电车");
		map.put("G11", "重型普通全挂车");
		map.put("G12", "重型厢式全挂车");
		map.put("G13", "重型罐式全挂车");
		map.put("G14", "重型平板全挂车");
		map.put("G15", "重型集装箱全挂车");
		map.put("G16", "重型自卸全挂车");
		map.put("G17", "重型仓栅式全挂车");
		map.put("G18", "重型旅居全挂车");
		map.put("G19", "重型专项作业全挂车");
		map.put("G21", "中型普通全挂车");
		map.put("G22", "中型厢式全挂车");
		map.put("G23", "中型罐式全挂车");
		map.put("G24", "中型平板全挂车");
		map.put("G25", "中型集装箱全挂车");
		map.put("G26", "中型自卸全挂车");
		map.put("G27", "中型仓栅式全挂车");
		map.put("G28", "中型旅居全挂车");
		map.put("G29", "中型专项作业全挂车");
		map.put("G31", "轻型普通全挂车");
		map.put("G32", "轻型厢式全挂车");
		map.put("G33", "轻型罐式全挂车");
		map.put("G34", "轻型平板全挂车");
		map.put("G35", "轻型自卸全挂车");
		map.put("G36", "轻型仓栅式全挂车");
		map.put("G37", "轻型旅居全挂车");
		map.put("G38", "轻型专项作业全挂车");
		map.put("H11", "重型普通货车");
		map.put("H12", "重型厢式货车");
		map.put("H13", "重型封闭货车");
		map.put("H14", "重型罐式货车");
		map.put("H15", "重型平板货车");
		map.put("H16", "重型集装厢车");
		map.put("H17", "重型自卸货车");
		map.put("H18", "重型特殊结构货车");
		map.put("H19", "重型仓栅式货车");
		map.put("H21", "中型普通货车");
		map.put("H22", "中型厢式货车");
		map.put("H23", "中型封闭货车");
		map.put("H24", "中型罐式货车");
		map.put("H25", "中型平板货车");
		map.put("H26", "中型集装厢车");
		map.put("H27", "中型自卸货车");
		map.put("H28", "中型特殊结构货车");
		map.put("H29", "中型仓栅式货车");
		map.put("H31", "轻型普货车");
		map.put("H32", "轻型厢式货车");
		map.put("H33", "轻型封闭货车");
		map.put("H34", "轻型罐式货车");
		map.put("H35", "轻型平板货车");
		map.put("H37", "轻型自卸货车");
		map.put("H38", "轻型特殊结构货车");
		map.put("H39", "轻仓栅式货车");
		map.put("H41", "微型普通货车");
		map.put("H42", "微型厢式货车");
		map.put("H43", "微型封闭货车");
		map.put("H44", "微型罐式货车");
		map.put("H45", "微型自卸货车");
		map.put("H46", "微型特殊结构货车");
		map.put("H47", "微型仓栅式货车");
		map.put("H51", "普通低速货车");
		map.put("H52", "厢式低速货车");
		map.put("H53", "罐式低速货车");
		map.put("H54", "自卸低速货车");
		map.put("H55", "仓栅式低速货车");
		map.put("J11", "轮式装载机械");
		map.put("J12", "轮式挖掘机械");
		map.put("J13", "轮式平地机械");
		map.put("Q11", "重型半挂牵引车");
		map.put("Q12", "重型全挂牵引车");
		map.put("Q21", "中型半挂牵引车");
		map.put("Q22", "中型全挂牵引车");
		map.put("Q31", "轻型半挂牵引车");
		map.put("Q32", "轻型全挂牵引车");
		map.put("T11", "大型轮式拖拉机");
		map.put("T21", "小型轮式拖拉机");
		map.put("T22", "手扶拖拉机");
		map.put("T23", "手扶变形运输机");
		map.put("X99", "其它");
		map.put("Z11", "大型专项作业车");
		map.put("Z21", "中型专项作业车");
		map.put("Z31", "小型专项作业车");
		map.put("Z41", "微型专项作业车");
		map.put("Z51", "重型专项作业车");
		map.put("Z71", "轻型专项作业车");
		Object[] objArr = new Object[146];
		ArrayList<JSONObject> list = new ArrayList<>();
		for (String key : map.keySet()) {
			CarModelVo carModelVo = new CarModelVo();
			carModelVo.setStr(map.get(key));
			carModelVo.setId(key);
			JSONObject json = (JSONObject) JSONObject.toJSON(carModelVo);
			list.add(json);
		}
		for (int i = 0; i < 146; i++) {
			objArr[i] = list.get(i);
		}
		baseBean.setData(objArr);
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 指标类型
	 * @Description TODO(指标类型)
	 */
	@RequestMapping("getIndexTypes")
	public void getIndexTypes(){
		BaseBean baseBean = new BaseBean();
		List<IndexTypeVo> list = new ArrayList<>();
		try {
			list.add(new IndexTypeVo("ZLZB", "增量指标"));
			list.add(new IndexTypeVo("GXZB", "更新指标"));
			list.add(new IndexTypeVo("QTZB", "其他指标"));
			list.add(new IndexTypeVo("BAZB", "备案车辆指标"));
			list.add(new IndexTypeVo("ESCLZB", "二手车辆指标"));
			list.add(new IndexTypeVo("ESCZZZB", "二手车周转指标"));
			list.add(new IndexTypeVo("WZB", "无指标"));
			
			baseBean.setCode(MsgCode.success);
			baseBean.setData(list);
			
		} catch (Exception e) {
			logger.error("【预约类】获取指标类型Action异常: baseBean = " + baseBean, e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
}