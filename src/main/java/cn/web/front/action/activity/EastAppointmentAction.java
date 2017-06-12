package cn.web.front.action.activity;

import java.text.SimpleDateFormat;
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

import com.alibaba.fastjson.JSON;

import cn.account.service.IAccountService;
import cn.activity.bean.ApptDistrictAndTimeVo;
import cn.activity.bean.NormalApptInfoVo;
import cn.activity.service.IActivityService;
import cn.message.service.IMobileMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.msg.MsgTemplate;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;

/**
 * 东部景区预约
 * @author Mbenben
 *
 */

@Controller
@RequestMapping(value="/activity/")
@SuppressWarnings(value="all")
public class EastAppointmentAction extends BaseAction {
	
	@Autowired
    @Qualifier("activityService")
    private IActivityService activityService;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	@Autowired
	@Qualifier("mobileMessageService")
	private IMobileMessageService mobileMessageService;

	
	/**
     * 发送短信验证码
     * @Description: TODO(发送短信验证码)
     * @param mobilephone 用户手机号
     * @param request
     * @param response
     * http://localhost:8080/web/user/sendSMSVerificatioCode.html?mobilephone=13652311206
     * @throws Exception 
     */
    @RequestMapping("sendSMSVerificatioCode")
    public void sendSMSVerificatioCode(String mobilephone,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	//5秒钟发一次,处理
    	String key = accountService.getSendSmsFreqLimit(mobilephone);
    	if(StringUtils.isNotBlank(key)){
    		baseBean.setCode(MsgCode.businessError);
        	baseBean.setMsg("短信发送太频繁");
        	baseBean.setData("短信发送太频繁");
        	renderJSON(baseBean);
    		return;
    	}
    	try {
    		if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		//生成验证码，六位数
    		String valideteCode = StringUtil.createValidateCode();
    		String msgContent = MsgTemplate.getSzjjSendMsgTemplate(valideteCode, "easternReservation");	//easternReservation 业务类型：东部预约
    		boolean flag = mobileMessageService.sendMessage(mobilephone, msgContent);
    		if(flag){
    			accountService.sendSMSVerificatioCode(mobilephone,valideteCode);
    			accountService.sendSmsFreqLimit(mobilephone);
    			baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData("发送成功");
    		}else{
    			baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg(MsgCode.systemMsg);
            	baseBean.setData("发送失败");
    		}
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("sendSMSVerificatioCode 错误!", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    
	/**
	 * 获取预约场次及配额信息
	 * @Description: TODO(获取预约场次及配额信息)
	 * @param sourceOfCertification 获取来源
	 * @throws Exception 
	 */
    @RequestMapping("getNormalApptDistrictAndTime")
    public void getNormalApptDistrictAndTime(String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	List<ApptDistrictAndTimeVo> list = new ArrayList<>();
		//String[] apptDistricts = {"1", "2"};	//1-梅沙片区,2-大鹏片区
		String apptDistrict = "1";	//暂时只做1-梅沙片区
    	
		try {
			//查询可预约日期
			baseBean = activityService.getNormalApptDate(sourceOfCertification);
			
			if(baseBean.getData() != null){
				String dateStr = baseBean.getData().toString();
				BaseBean refBean = null;
				String[] apptDates = dateStr.split(",");		//多个日期用逗号分隔	2017-06-10,2017-06-11
				
				//暂时只做梅沙片区
				for (String apptDate : apptDates) {
					//根据预约日期获取配额信息
					refBean = activityService.getQuotaInfoByApptDate(apptDate, apptDistrict, sourceOfCertification);
					ApptDistrictAndTimeVo vo = (ApptDistrictAndTimeVo) refBean.getData();;
					if(vo != null){
						list.add(vo);	//封装片区及时间段信息
					}
				}
				
				/*for (String apptDistrict : apptDistricts) {
					for (String apptDate : apptDates) {
						//根据预约日期获取配额信息
						refBean = activityService.getQuotaInfoByApptDate(apptDate, apptDistrict, sourceOfCertification);
						ApptDistrictAndTimeVo vo = (ApptDistrictAndTimeVo) refBean.getData();;
						if(vo != null){
							list.add(vo);	//封装片区及时间段信息
						}
					}
				}*/
				
				baseBean.setData(list);
				baseBean.setCode(refBean.getCode());
			}
			
			logger.info("获取预约场次信息返回web数据:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取预约场次信息Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 个人预约信息写入
	 * @Description: TODO(个人预约信息写入)
	 * @param info 个人预约信息
	 * @param validateCode 手机验证码
	 * @param sourceOfCertification 获取来源
	 * @param openId 微信公众号唯一标识
	 * @throws Exception
	 */
    @RequestMapping("addNormalApptInfo")
    public void addNormalApptInfo(NormalApptInfoVo info, String validateCode, String sourceOfCertification, String openId,
    		HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
		try {
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("请求来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(openId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("openId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getPlateNo())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getPlateType())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getVehicleType())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getVinLastFour())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号后四位不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getMobilePhone())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(validateCode)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getApptDate()) || StringUtil.isBlank(info.getApptDistrict()) || StringUtil.isBlank(info.getApptInterval())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("选择预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证码是否正确
    		// 0-验证成功，1-验证失败，2-验证码失效
    		int result = accountService.verificatioCode(info.getMobilePhone(), validateCode);
    		if(result == 0){
    			
    			//预约的是上午
    			if("1".equals(info.getApptInterval())){
    				//表示 "2017-06-10 12:00:00"
    				String strTwelve = info.getApptDate() + " " + "12:00:00";	//上午预约结束时间点
    				//转化为date
    				Date dateTwelve = DateUtil2.str2date(strTwelve);
    				//当前时间在预约上午之前,才可预约
    				if(new Date().before(dateTwelve)){
    					baseBean = activityService.addNormalApptInfo(info, sourceOfCertification, openId);
    					logger.info("当前时间在预约上午之前,可预约!");
    				}else{
    					logger.info("预约时间过了当天上午,不能预约!");
    					baseBean.setCode(MsgCode.paramsError);
    					baseBean.setMsg("上午预约时段已过,请选择其他时段!");
    					renderJSON(baseBean);
    		        	return;
    				}
    			}
    			//预约下午
    			else{
    				baseBean = activityService.addNormalApptInfo(info, sourceOfCertification, openId);
    			}
    			
    			String code = baseBean.getCode();	//状态码
    			if("0000".equals(code)){
    				//预约成功,发送短信提醒
    				String msgContent = MsgTemplate.getEastApptSuccessMsgTemplate(info.getApptDate(), info.getApptInterval(), info.getApptDistrict());
    	    		boolean flag = mobileMessageService.sendMessage(info.getMobilePhone(), msgContent);
    	    		if(flag){
    	    			logger.info("发送短信提醒成功:" + msgContent);
    	    		}else{
    	    			logger.info("发送短信提醒失败:" + msgContent);
    	    		}
    			}else{
    				//预约失败,发送短信提醒
    				String msgContent = MsgTemplate.getEastApptFailMsgTemplate(baseBean.getMsg(), info.getApptDate(), info.getApptInterval(), info.getApptDistrict());
    	    		boolean flag = mobileMessageService.sendMessage(info.getMobilePhone(), msgContent);
    	    		if(flag){
    	    			logger.info("发送短信提醒成功:" + msgContent);
    	    		}else{
    	    			logger.info("发送短信提醒失败:" + msgContent);
    	    		}
    			}
    		}
			if(result == 1){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码错误");
	        	renderJSON(baseBean);
	        	return;
			 }
			if(result == 2){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码失效,请重新获取");
	        	renderJSON(baseBean);
	        	return;
			}
			
		} catch (Exception e) {
			logger.error("个人预约信息写入Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 查询个人预约信息
     * @Description: TODO(查询个人预约信息)
     * @param plateNo	号牌号码
     * @param plateType 号牌种类
     * @param vinLastFour 车架后4位
     * @param mobilePhone 手机号码
     * @param sourceOfCertification 查询来源
     */
    @RequestMapping("getApptHistoryRecord")
    public void getApptHistoryRecord(String plateNo, String plateType, String vinLastFour, String mobilePhone, String validateCode, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
		try {
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("请求来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(plateNo)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(plateType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(vinLastFour)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架后4位不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mobilePhone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(validateCode)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证码是否正确
    		// 0-验证成功，1-验证失败，2-验证码失效
    		int result = accountService.verificatioCode(mobilePhone, validateCode);
    		if(result == 0){
    			//验证成功,查询预约记录
    			baseBean = activityService.getApptHistoryRecord(plateNo, plateType, vinLastFour, mobilePhone, sourceOfCertification);
    		}
			if(result == 1){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码错误");
			 }
			if(result == 2){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码失效,请重新获取");
			}
			
			
		} catch (Exception e) {
			logger.error("查询个人预约信息Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 取消个人预约信息
     * @Description: TODO(取消个人预约信息)
     * @param apptId 预约编号
     * @param cancelReason 取消原因
	 * @param sourceOfCertification 获取来源
	 * @throws Exception 
	 */
    @RequestMapping("cancelNormalApptInfo")
    public void cancelNormalApptInfo(String mobilePhone, String apptId, String cancelReason, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
		try {
			/*if(StringUtil.isBlank(mobilePhone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("前端缓存的手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}*/
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("请求来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(apptId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("预约编号不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(cancelReason)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("取消原因不能为空!");
				renderJSON(baseBean);
				return;
			}
			baseBean = activityService.cancelNormalApptInfo(apptId, cancelReason, sourceOfCertification);
			
			String code = baseBean.getCode();
			if("0000".equals(code)){
				//取消预约成功,发送短信提醒
				String msgContent = MsgTemplate.getEastApptCancelMsgNotice();
	    		boolean flag = mobileMessageService.sendMessage(mobilePhone, msgContent);
	    		if(flag){
	    			logger.info("发送短信提醒成功:" + msgContent);
	    		}else{
	    			logger.info("发送短信提醒失败:" + msgContent);
	    		}
			}
			
			
		} catch (Exception e) {
			logger.error("取消个人预约信息Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 获取"临时"预约场次及配额信息
	 * @Description: TODO(获取"临时"预约场次及配额信息)
	 * @param sourceOfCertification 获取来源
	 * @throws Exception 
	 */
    @RequestMapping("getTempApptDistrictAndTime")
    public void getTempApptDistrictAndTime(String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	List<ApptDistrictAndTimeVo> list = new ArrayList<>();
		//String[] apptDistricts = {"1", "2"};	//1-梅沙片区,2-大鹏片区
		String apptDistrict = "1";	//暂时只做1-梅沙片区
    	
		try {
			String curDate = DateUtil2.date2dayStr(new Date());	//当前日期	格式yyyy-MM-dd
			
			//查询可预约日期
			BaseBean dateBean = activityService.getNormalApptDate(sourceOfCertification);
			
			if(dateBean.getData() != null){
				String dateStr = dateBean.getData().toString();
				String[] apptDates = dateStr.split(",");		//多个日期用逗号分隔	2017-06-10,2017-06-11
				for (String apptDate : apptDates) {
					//当前日期是节假日时,才查询显示
					if(curDate.equals(apptDate)){
						BaseBean refBean = null;
						//for (String apptDistrict : apptDistricts) {	//暂时只有梅沙片区
							//根据预约日期获取配额信息
							refBean = activityService.getQuotaInfoByApptDate(apptDate, apptDistrict, sourceOfCertification);
							ApptDistrictAndTimeVo vo = (ApptDistrictAndTimeVo) refBean.getData();
							
							if(vo != null){
								//上午,下午设置
								String strTwelve = apptDate + " " + "12:00:00";//可预约当天12点整,区别上,下午
								Date dateTwelve = DateUtil2.str2date(strTwelve);
								if(new Date().before(dateTwelve)){	//当前时间在当天12点之前
									vo.setApptInterval("1");//1-上午
								}else{								//12点之后
									vo.setApptInterval("2");//2-下午
								}
								
								list.add(vo);	//封装片区及时间段信息
							}
						//}
						baseBean.setCode(refBean.getCode());
						baseBean.setData(list);
					}
				}
			}
			//当前日期不是节假日
			if(baseBean.getData() == null){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("非节假日时段，无需预约！");
			}
			
			logger.info("获取临时预约场次信息返回web数据:" + JSON.toJSONString(baseBean));
		} catch (Exception e) {
			logger.error("获取临时预约场次信息Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    
    /**
	 * 临时个人预约信息写入
	 * @Description: TODO(临时个人预约信息写入)
	 * @param info 个人预约信息
	 * @param sourceOfCertification 获取来源
	 * @param openId 微信公众号唯一标识
	 * @throws Exception
	 */
    @RequestMapping("addTempApptInfo")
    public void addTempApptInfo(NormalApptInfoVo info, String validateCode, String sourceOfCertification, String openId){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
		try {
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("请求来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(openId)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("opendId不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getPlateNo())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getPlateType())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getVehicleType())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getVinLastFour())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号后四位不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getMobilePhone())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(validateCode)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机验证码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(info.getApptDate()) || StringUtil.isBlank(info.getApptDistrict()) || StringUtil.isBlank(info.getApptInterval())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("选择预约时间不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证码是否正确
    		// 0-验证成功，1-验证失败，2-验证码失效
    		int result = accountService.verificatioCode(info.getMobilePhone(), validateCode);
    		if(result == 0){
    			baseBean = activityService.addTempApptInfo(info, sourceOfCertification, openId);
    			
    			String code = baseBean.getCode();	//状态码
    			if("0000".equals(code)){
    				//预约成功,发送短信提醒
    				String msgContent = MsgTemplate.getEastApptSuccessMsgTemplate(info.getApptDate(), info.getApptInterval(), info.getApptDistrict());
    	    		boolean flag = mobileMessageService.sendMessage(info.getMobilePhone(), msgContent);
    	    		if(flag){
    	    			logger.info("发送短信提醒成功:" + msgContent);
    	    		}else{
    	    			logger.info("发送短信提醒失败:" + msgContent);
    	    		}
    			}else{
    				//预约失败,发送短信提醒
    				String msgContent = MsgTemplate.getEastApptFailMsgTemplate(baseBean.getMsg(), info.getApptDate(), info.getApptInterval(), info.getApptDistrict());
    	    		boolean flag = mobileMessageService.sendMessage(info.getMobilePhone(), msgContent);
    	    		if(flag){
    	    			logger.info("发送短信提醒成功:" + msgContent);
    	    		}else{
    	    			logger.info("发送短信提醒失败:" + msgContent);
    	    		}
    			}
    		}
			if(result == 1){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码错误");
	        	renderJSON(baseBean);
	        	return;
			 }
			if(result == 2){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码失效,请重新获取");
	        	renderJSON(baseBean);
	        	return;
			}
			
		} catch (Exception e) {
			logger.error("临时个人预约信息写入Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
}
