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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.booking.business.bean.BookingTemplateVo;
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
import cn.booking.business.bean.UseCharater;
import cn.booking.business.service.IBookingBusinessService;
import cn.handle.bean.vo.HandleTemplateVo;
import cn.message.model.wechat.MessageChannelModel;
import cn.message.model.wechat.TemplateDataModel;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.bean.BusinessType;
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
	 * @param bookNumber  预约号,订单号
	 * @param mobile 手机号
	 * @param organizationName 预约地点
	 * @param appointmentDate 预约时间
	 * 
	 */
	@RequestMapping("cancel")
	public void cancel(String businessType, String bookerNumber, String mobile,String organizationName,
			String appointmentDate,String appointmentTime,String openId,String businessTypeName) {
		BaseBean baseBean = new BaseBean();
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
		if (StringUtil.isBlank(organizationName)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("organizationName 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(appointmentDate)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("appointmentDate 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(appointmentTime)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("appointmentTime 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(openId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("openId 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(businessTypeName)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessTypeName 不能为空!");
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
				
				/*String templateId = "nR0-6Nfw9VvmEEcq8Kih24j1Q5X0e7ozbM5dqkV1BXo";
				//HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, "", bookerNumber, appointmentDate);
				//baseBean.setData(handleTemplateVo);
				//String url = HandleTemplateVo.getUrl(handleTemplateVo,"");
				Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
				tmap.put("first", new TemplateDataModel().new Property("您的预约申请已取消，具体信息如下", "#212121"));
				if("1".equals(businessType)){
					tmap.put("businessType",new TemplateDataModel().new Property("驾驶证业务", "#212121"));
				}else if("2".equals(businessType)){
					tmap.put("businessType",new TemplateDataModel().new Property("机动车业务", "#212121"));	
				}
				tmap.put("business",new TemplateDataModel().new Property(businessTypeName, "#212121"));
				tmap.put("order", new TemplateDataModel().new Property(bookerNumber, "#212121"));
				tmap.put("time", new TemplateDataModel().new Property(appointmentDate +" "+ appointmentTime, "#212121"));
				tmap.put("address", new TemplateDataModel().new Property(organizationName, "#212121"));
				tmap.put("remark", new TemplateDataModel().new Property("", "#212121"));
				boolean flag = templateMessageService.sendMessage(openId, templateId, "", tmap);
				logger.info("发送模板消息结果：" + flag);*/
				
				MessageChannelModel model = new MessageChannelModel();
				model.setOpenid(openId);
				if("1".equals(businessType)){//1-驾驶证业务
					model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbhCx1GDBVM-D9RlY_RkA01E");
				}else if("2".equals(businessType)){//2-机动车业务
					model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIblH0oc9K9CXnemA0qAazPdI");
				}
				model.setResult_page_style_id("PMw9-nhDOOQuMzL7-cVZ3DqyaaLEvpIWsopaXE1qvC0");
				model.setDeal_msg_style_id("PMw9-nhDOOQuMzL7-cVZ3CZoVDr0ojGdWvwZf7SZK6A");
				model.setCard_style_id("");
				model.setOrder_no(bookerNumber);
				model.setUrl("");
				Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
				map.put("first", new MessageChannelModel().new Property("您好，您的预约申请已取消，具体信息如下","#212121"));
				map.put("businessType", new MessageChannelModel().new Property("1".equals(businessType)?"驾驶证业务":"机动车业务","#212121"));
				map.put("business", new MessageChannelModel().new Property(businessTypeName,"#212121"));
				map.put("order", new MessageChannelModel().new Property(bookerNumber,"#212121"));
				map.put("time", new MessageChannelModel().new Property(appointmentDate +" "+ appointmentTime,"#212121"));
				map.put("address", new MessageChannelModel().new Property(organizationName,"#212121"));
				map.put("remark", new MessageChannelModel().new Property("","#212121"));
				model.setData(map);
				
				BaseBean msgBean = templateMessageService.sendServiceMessage(model);
				logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
				
				//发送成功
				if("0".equals(msgBean.getCode())){
					baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
				}
				
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
	public void getIdTypeId(String code, String businessTypeId, String arg0, String arg1 ,String type) {
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
			List<IdTypeVO> idTypeVOs = bookingBusinessService.getIdTypes(businessTypeId, arg0, arg1 ,type);
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
	 * 获取所有的证件类型
	 * localhost/web/bookingbusiness/getIdTypes.html
	 */
	@RequestMapping("getIdTypes")
	public void getIdTypes(String businessTypeId ,String type) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(businessTypeId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessTypeId 不能为空!");
			renderJSON(baseBean);
			return;
		}
		try {
			List<IdTypeVO> idTypeVOs = bookingBusinessService.getIdTypes(businessTypeId, "", "" ,type);
			baseBean.setCode(MsgCode.success);
			baseBean.setMsg("");
			baseBean.setData(idTypeVOs);
		} catch (Exception e) {
			logger.error("getIdTypes 异常:" + e);
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
			List<String> appointmentDates = bookingBusinessService.getAppointmentDate(orgId, businessTypeId, arg0,arg1);
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
			if ("00".equals(appTimes.getCode())) {
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(appTimes.getMsg());
				baseBean.setData(appTimes.getData());
			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(appTimes.getData().toString());
				baseBean.setData(appTimes.getMsg());
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
				baseBean.setMsg("代办人（或本人）身份证号码不能为空!");
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
				baseBean.setMsg(vo.getResult());
				baseBean.setData(vo.getMsg());
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName=request.getParameter("orgName");
		String orgAddr=request.getParameter("orgAddr");
		String businessCode=request.getParameter("businessCode");
		String businessName=request.getParameter("businessName");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","businessCode","businessName","carTypeName");
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
        		//预约成功发送微信模板消息
				if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, businessCode, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						//String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						/*Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property(businessName,"#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);*/
						
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbug42B1mdjrfnGyqfgbmMb8");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("机动车在线预约-" + businessName,"#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}
						
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(refBean.getData().toString());
        		baseBean.setData(refBean.getMsg());
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			if(StringUtil.isBlank(orgName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位名称不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgAddr)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位地址不能为空!");
				renderJSON(baseBean);
				return;
			}
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
				String appTime = appointmentDate +" "+appointmentTime;
				baseBean.setCode(MsgCode.success);
//				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						/*String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						baseBean.setData(bookingTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
						tmap.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						tmap.put("business", new TemplateDataModel().new Property("满分学习考试", "#212121"));
						tmap.put("order", new TemplateDataModel().new Property(waterNumber, "#212121"));
						tmap.put("time", new TemplateDataModel().new Property(appTime, "#212121"));
						tmap.put("address", new TemplateDataModel().new Property(orgName, "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ10, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						baseBean.setData(bookingTemplateVo);
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbsPL8FV6yk3G67Y4HQKo3tA");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("驾驶证在线预约","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			if(StringUtil.isBlank(orgName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位名称不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgAddr)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位地址不能为空!");
				renderJSON(baseBean);
				return;
			}
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
				String appTime = appointmentDate +" "+appointmentTime;
				baseBean.setCode(MsgCode.success);
				if (sourceOfCertification.equals("C")) {
					try {
						/*String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ11, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
						tmap.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						tmap.put("business", new TemplateDataModel().new Property("持军队、武装警察部队机动车驾驶证申领", "#212121"));
						tmap.put("order", new TemplateDataModel().new Property(waterNumber, "#212121"));
						tmap.put("time", new TemplateDataModel().new Property(appTime, "#212121"));
						tmap.put("address", new TemplateDataModel().new Property(orgName, "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ11, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						baseBean.setData(bookingTemplateVo);
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbsPL8FV6yk3G67Y4HQKo3tA");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("驾驶证在线预约","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			if(StringUtil.isBlank(orgName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位名称不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgAddr)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位地址不能为空!");
				renderJSON(baseBean);
				return;
			}
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
				String appTime = appointmentDate +" "+appointmentTime;
				baseBean.setCode(MsgCode.success);
				if (sourceOfCertification.equals("C")) {
					try {
						/*String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ13, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
						tmap.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						tmap.put("business", new TemplateDataModel().new Property("香港机动车驾驶证免试换证", "#212121"));
						tmap.put("order", new TemplateDataModel().new Property(waterNumber, "#212121"));
						tmap.put("time", new TemplateDataModel().new Property(appTime, "#212121"));
						tmap.put("address", new TemplateDataModel().new Property(orgName, "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ13, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						baseBean.setData(bookingTemplateVo);
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbsPL8FV6yk3G67Y4HQKo3tA");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("驾驶证在线预约","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			if(StringUtil.isBlank(orgName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位名称不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgAddr)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位地址不能为空!");
				renderJSON(baseBean);
				return;
			}
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
				String appTime = appointmentDate +" "+appointmentTime;
				baseBean.setCode(MsgCode.success);
				if (sourceOfCertification.equals("C")) {
					try {
						/*String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ17, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
						tmap.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						tmap.put("business", new TemplateDataModel().new Property("持境外驾驶证申请换证", "#212121"));
						tmap.put("order", new TemplateDataModel().new Property(waterNumber, "#212121"));
						tmap.put("time", new TemplateDataModel().new Property(appTime, "#212121"));
						tmap.put("address", new TemplateDataModel().new Property(orgName, "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ17, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbsPL8FV6yk3G67Y4HQKo3tA");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("驾驶证在线预约","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			if(StringUtil.isBlank(orgName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位名称不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgAddr)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位地址不能为空!");
				renderJSON(baseBean);
				return;
			}
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
				String appTime = appointmentDate +" "+appointmentTime;
				baseBean.setCode(MsgCode.success);
				if (sourceOfCertification.equals("C")) {
					try {
						/*String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ20, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
						tmap.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						tmap.put("business", new TemplateDataModel().new Property("其他业务(驾驶证)", "#212121"));
						tmap.put("order", new TemplateDataModel().new Property(waterNumber, "#212121"));
						tmap.put("time", new TemplateDataModel().new Property(appTime, "#212121"));
						tmap.put("address", new TemplateDataModel().new Property(orgName, "#212121"));
						tmap.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, tmap);
						logger.info("发送模板消息结果：" + flag);*/
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ20, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbsPL8FV6yk3G67Y4HQKo3tA");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("驾驶证在线预约","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			if(StringUtil.isBlank(orgName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位名称不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgAddr)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位地址不能为空!");
				renderJSON(baseBean);
				return;
			}
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
				String appTime = appointmentDate +" "+appointmentTime;
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ21, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
						tmap.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						tmap.put("business", new TemplateDataModel().new Property("恢复驾驶资格（逾期一年以上未换证类）", "#212121"));
						tmap.put("order", new TemplateDataModel().new Property(waterNumber, "#212121"));
						tmap.put("time", new TemplateDataModel().new Property(appTime, "#212121"));
						tmap.put("address", new TemplateDataModel().new Property(orgName, "#212121"));
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			if(StringUtil.isBlank(orgName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位名称不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(orgAddr)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约单位地址不能为空!");
				renderJSON(baseBean);
				return;
			}
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
				String appTime = appointmentDate +" "+appointmentTime;
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg(msg);
				if (sourceOfCertification.equals("C")) {
					try {
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ22, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
						tmap.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						tmap.put("business", new TemplateDataModel().new Property("恢复驾驶资格（逾期一年以上未体检类）", "#212121"));
						tmap.put("order", new TemplateDataModel().new Property(waterNumber, "#212121"));
						tmap.put("time", new TemplateDataModel().new Property(appTime, "#212121"));
						tmap.put("address", new TemplateDataModel().new Property(orgName, "#212121"));
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
	@RequestMapping("createDriveInfo_ZJ16")
	public void createDriveInfo_ZJ16(HttpServletRequest request,HttpServletResponse response){
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
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = appointmentDate + " " + appointmentTime;
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(orgId).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(orgId).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createDriveInfo_ZJ16, waterNumber, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						/*String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("驾驶证在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("临时机动车驾驶许可申领","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);*/
						String url = BookingTemplateVo.getUrl(bookingTemplateVo,bookingBusinessService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbsPL8FV6yk3G67Y4HQKo3tA");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("驾驶证在线预约","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							baseBean.setMsg(url);
						}
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
	public void createTemporaryLicenseVehicleInfo(CreateTemporaryLicenseVehicleInfoVo vo, HttpServletRequest request){
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
			
			String sourceOfCertification = request.getParameter("sourceOfCertification");
			String openId = request.getParameter("openId");
			String orgName = request.getParameter("orgName");
			String orgAddr = request.getParameter("orgAddr");
			
			//接口调用
			baseBean = bookingBusinessService.createTemporaryLicenseVehicleInfo(vo);
			
			//预约成功发送微信模板消息
			if(MsgCode.success.equals(baseBean.getCode()) && "C".equals(sourceOfCertification)){
				try {
					String waterNumber = baseBean.getData().toString();
					String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
					//获取预约地点
					if(StringUtil.isBlank(orgName)){
						orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
					}
					//获取预约地址
					if(StringUtil.isBlank(orgAddr)){
						orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
					}
					BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createTemporaryLicenseVehicleInfo, waterNumber, orgName, orgAddr, vo.getAppointmentDate(), vo.getAppointmentTime(), vo.getName());
					baseBean.setData(bookingTemplateVo);
					String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
					String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
					logger.info("返回的url是：" + url);
					logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
					map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
					map.put("business", new TemplateDataModel().new Property("核发临牌","#212121"));
					map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
					map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
					map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
					map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
			}
			
		} catch (Exception e) {
			logger.error("【预约类服务】核发临牌 Action异常:" + e);
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName = request.getParameter("orgName");
		String orgAddr = request.getParameter("orgAddr");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","carTypeName");
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(orgId).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createVehicleInfo_JD27, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("核发校车标牌","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName = request.getParameter("orgName");
		String orgAddr = request.getParameter("orgAddr");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","carTypeName");
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createVehicleInfo_JD37, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("抵押/解押登记现场办理","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
			logger.error("【预约类服务】抵押/解押登记现场办理 Action异常:"+e);
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName = request.getParameter("orgName");
		String orgAddr = request.getParameter("orgAddr");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","carTypeName");
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createVehicleInfo_JD38, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("机动车委托异地年审现场办理","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName = request.getParameter("orgName");
		String orgAddr = request.getParameter("orgAddr");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","carTypeName");
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createVehicleInfo_JD41, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("转出、注销恢复","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(2, BusinessType.cancleDriveInfo, waterNumber, DateUtil2.date2dayStr(new Date()));
						baseBean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,bookingBusinessService.getTemplateSendUrl());
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> tmap = 
								new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						tmap.put("first", new TemplateDataModel().new Property("您的业务办理申请已申请，具体信息如下", "#212121"));
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName = request.getParameter("orgName");
		String orgAddr = request.getParameter("orgAddr");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","carTypeName");
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createVehicleInfo_JD28, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						//String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						/*Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("机动车打刻原车发动机号码变更备案","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);*/
						
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbug42B1mdjrfnGyqfgbmMb8");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("机动车在线预约-机动车打刻原车发动机号码变更备案","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}
						
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName = request.getParameter("orgName");
		String orgAddr = request.getParameter("orgAddr");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","carTypeName");
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createVehicleInfo_JD29, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("机动车打刻原车辆识别代号变更备案","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		String openId = request.getParameter("openId");
		String orgName = request.getParameter("orgName");
		String orgAddr = request.getParameter("orgAddr");
		String carTypeName = request.getParameter("carTypeName");
		
		try {
			//校验参数
			boolean bool = checkParamNotNull(request,response,"orgId","businessTypeId","name","idTypeId","idNumber","mobile",
					"appointmentDate","appointmentTime","carTypeId","carFrame","platNumber","bookerName","bookerIdNumber",
					"bookerType","useCharater","msgNumber","bookerMobile","carTypeName");
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
				//预约成功发送微信模板消息
				if("C".equals(sourceOfCertification)){
					try {
						String waterNumber = refBean.getData().toString();
						String appTime = vo.getAppointmentDate() + " " + vo.getAppointmentTime();
						//获取预约地点
						if(StringUtil.isBlank(orgName)){
							orgName = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getName();
						}
						//获取预约地址
						if(StringUtil.isBlank(orgAddr)){
							orgAddr = bookingBusinessService.findOrgByOrgId(vo.getOrgId()).getDescription();
						}
						BookingTemplateVo bookingTemplateVo = new BookingTemplateVo(2, BusinessType.createVehicleInfo_JD33, waterNumber, platNumber, carTypeName, orgName, orgAddr, appointmentDate, appointmentTime, name);
						baseBean.setData(bookingTemplateVo);
						String url = bookingTemplateVo.getUrl(bookingTemplateVo, bookingBusinessService.getTemplateSendUrl());
						//String templateId = "kS7o4u0btdEciJTbJe03LcPIwmxv1bxj95MhWqwuB84";
						logger.info("返回的url是：" + url);
						logger.info("bookingTemplateVo 是：" + bookingTemplateVo);
						/*Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
						map.put("first", new TemplateDataModel().new Property("您的业务办理预约申请已成功提交，具体信息如下","#212121"));
						map.put("businessType", new TemplateDataModel().new Property("机动车在线预约","#212121"));
						map.put("business", new TemplateDataModel().new Property("档案更正","#212121"));
						map.put("order", new TemplateDataModel().new Property(waterNumber,"#212121"));
						map.put("time", new TemplateDataModel().new Property(appTime,"#212121"));
						map.put("address", new TemplateDataModel().new Property(orgName,"#212121"));
						map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
						logger.info("发送模板消息结果：" + flag);*/
						
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbug42B1mdjrfnGyqfgbmMb8");
						model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
						model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
						map.put("business", new MessageChannelModel().new Property("机动车在线预约-档案更正","#212121"));
						map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
						map.put("time", new MessageChannelModel().new Property(appTime,"#212121"));
						map.put("address", new MessageChannelModel().new Property(orgName,"#212121"));
						map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
						model.setData(map);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}
						
					} catch (Exception e) {
						logger.error("发送模板消息  失败===", e);
					}
				}
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
	/*
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
	
	*/
	/**
	 * 获取车辆型号列表
	 * @throws Exception 
	 */
	@RequestMapping("getCarModelArray")
	public void getCarModelArray() throws Exception{
		BaseBean baseBean = new BaseBean();		//创建返回结果
		Map<String, String> map = new LinkedHashMap<>();
		map = bookingBusinessService.getCarModelArray();
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
			list = bookingBusinessService.getIndexTypes();
			baseBean.setCode(MsgCode.success);
			baseBean.setData(list);
		} catch (Exception e) {
			logger.error("【预约类】获取指标类型Action异常: baseBean = " + baseBean, e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 使用性质
	 * @Description TODO(使用性质)
	 */
	@RequestMapping("getUseCharater")
	public void getUseCharater(){
		BaseBean baseBean = new BaseBean();
		List<UseCharater> list = new ArrayList<>();
		try {
			list = bookingBusinessService.getUseCharater();
			baseBean.setCode(MsgCode.success);
			baseBean.setData(list);
		} catch (Exception e) {
			logger.error("【预约类】获取使用性质Action异常: baseBean = " + baseBean, e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	/**
	 * 获取页面初始化数据
	 */
	@RequestMapping("getPageInit")
	public void getPageInit(String businessTypeId ,String type) {
		BaseBean baseBean = new BaseBean();
		if (StringUtil.isBlank(businessTypeId)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("businessTypeId 不能为空!");
			renderJSON(baseBean);
			return;
		}
		if (StringUtil.isBlank(type)) {
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("type 不能为空!");
			renderJSON(baseBean);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//获取车辆类型列表
			List<CarTypeVO> carTypeVOs = bookingBusinessService.getCarTypes();
			map.put("carTypeVOs", carTypeVOs);
			//使用性质(后端写死不需要缓存)
			List<UseCharater> useCharaters = bookingBusinessService.getUseCharater();
			map.put("useCharaters", useCharaters);
			//指标类型(后端写死不需要缓存)
			List<IndexTypeVo> indexTypeVos = bookingBusinessService.getIndexTypes();
			map.put("indexTypeVos", indexTypeVos);
			//车辆型号列表(后端写死不需要缓存)
			Map<String, String> carModelArray = bookingBusinessService.getCarModelArray();
			List<CarModelVo> carModelVos = new ArrayList<>();
			for (String key : carModelArray.keySet()) {
				CarModelVo carModelVo = new CarModelVo();
				carModelVo.setStr(carModelArray.get(key).toString());
				carModelVo.setId(key);
				carModelVos.add(carModelVo);
			}
			map.put("carModelArray", carModelVos);
			//预约地点
			List<OrgVO> orgVOs = bookingBusinessService.getOrgsByBusinessTypeId(businessTypeId, "", "");
			map.put("orgVOs", orgVOs);
			//获取身份证明类型
			List<IdTypeVO> idTypeVOs = bookingBusinessService.getIdTypes(businessTypeId, "", "",type);
			map.put("idTypeVOs", idTypeVOs);
			baseBean.setCode(MsgCode.success);
			baseBean.setData(map);
			baseBean.setMsg("");
		} catch (Exception e) {
			logger.error("getCarTypes异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		System.out.println(JSON.toJSONString(baseBean));
		logger.debug(JSON.toJSONString(baseBean));
	}
}