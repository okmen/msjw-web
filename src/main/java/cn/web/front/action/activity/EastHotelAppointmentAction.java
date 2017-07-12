package cn.web.front.action.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.activity.bean.ApptDistrictAndTimeVo;
import cn.activity.bean.ApptInfoDetailVo;
import cn.activity.bean.HotelApptInfoVo;
import cn.activity.bean.HotelApptResultVo;
import cn.activity.bean.HotelInfoVo;
import cn.activity.service.IActivityService;
import cn.message.service.IMobileMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.msg.MsgTemplate;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;

/**
 * 东部景区-酒店预约
 * @author Mbenben
 *
 */

@Controller
@RequestMapping(value="/activity/")
@SuppressWarnings(value="all")
public class EastHotelAppointmentAction extends BaseAction {

	@Autowired
    @Qualifier("activityService")
    private IActivityService activityService;
	
	@Autowired
	@Qualifier("mobileMessageService")
	private IMobileMessageService mobileMessageService;
	
	/**
	 * 获取酒店分店信息
	 * @Description: TODO(获取酒店分店信息)
	 * @param agencyCode 组织机构代码或社会统一信用代码
	 */
    @RequestMapping("getHotelInfoByCode")
    public void getHotelInfoByCode(String agencyCode){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
		try {
			if(StringUtils.isBlank(agencyCode)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("组织机构代码不能为空!");
				renderJSON(baseBean);
				return;
			}
			baseBean = activityService.getHotelInfoByCode(agencyCode);
			
		} catch (Exception e) {
			logger.error("获取酒店分店信息Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
	
    /**
	 * 酒店分店登录
	 * @Description: TODO(酒店分店登录)
	 * @param vo 酒店信息
	 * @param password 登录密码
	 * @param sourceOfCertification 请求来源(可为空)
	 */
    @RequestMapping("loginViaHotel")
    public void loginViaHotel(HotelInfoVo vo, String password, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
    	try {
    		if(StringUtils.isBlank(vo.getAgencyCode())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("组织机构代码不能为空!");
				renderJSON(baseBean);
				return;
			}
    		if(StringUtils.isBlank(vo.getBranchCode())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("分店编号不能为空!");
				renderJSON(baseBean);
				return;
			}
    		if(StringUtils.isBlank(password)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("登录密码不能为空!");
				renderJSON(baseBean);
				return;
			}
    		
    		baseBean = activityService.loginViaHotel(vo, password, sourceOfCertification);
    		
    	} catch (Exception e) {
    		logger.error("酒店分店登录Action异常:" + e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 获取酒店配额信息
     * @Description: TODO(获取酒店配额信息)
     * @param info 酒店信息
     * @param sourceOfCertification 请求来源
     */
    @RequestMapping("getHotelApptDistrictAndTime")
    public void getHotelApptDistrictAndTime(HotelInfoVo info, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	List<ApptDistrictAndTimeVo> list = new ArrayList<>();
    	String apptDistrict = "1";	//暂时只做1-梅沙片区
    	
    	try {
    		if(StringUtils.isBlank(info.getAgencyCode())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("组织机构代码不能为空!");
				renderJSON(baseBean);
				return;
			}
    		if(StringUtils.isBlank(info.getBranchCode())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("分店编号不能为空!");
				renderJSON(baseBean);
				return;
			}
    		
    		//查询可预约日期
    		BaseBean dateBean = activityService.getNormalApptDate(sourceOfCertification);
    		
    		if(dateBean.getData() != null){
    			String dateStr = dateBean.getData().toString();
    			String[] apptDates = dateStr.split(",");	//多个日期用逗号分隔	2017-06-10,2017-06-11
    			
    			BaseBean refBean = null;
    			for (String apptDate : apptDates) {
					//根据预约日期获取配额信息
					refBean = activityService.getHotelQuotaInfo(apptDate, apptDistrict, info, sourceOfCertification);
					ApptDistrictAndTimeVo vo = (ApptDistrictAndTimeVo) refBean.getData();
					if(vo != null){
						list.add(vo);	//封装片区及日期信息
					}
				}
    			baseBean.setData(list);
    			baseBean.setMsg(refBean.getMsg());
				baseBean.setCode(refBean.getCode());
    		}
    		
    	} catch (Exception e) {
    		logger.error("获取酒店配额信息Action异常:" + e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 酒店预约信息写入
	 * @Description: TODO(酒店预约信息写入)
	 * @param agencyCode 组织机构代码
	 * @param branchCode 分店编号
	 * @param apptInfoList 预约信息集合
	 */
    @RequestMapping("addHotelApptInfo")
	public void addHotelApptInfo(String agencyCode, String branchCode, String apptInfoList, String hotelName){
    	
		BaseBean baseBean = new BaseBean();		//创建返回结果
		
    	try {
    		if(StringUtils.isBlank(apptInfoList)){
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("预约信息不能为空!");
    			renderJSON(baseBean);
    			return;
    		}
    		if(StringUtils.isBlank(hotelName)){
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("酒店名称不能为空!");
    			renderJSON(baseBean);
    			return;
    		}
    		
    		List<HotelApptInfoVo> list = JSON.parseArray(apptInfoList, HotelApptInfoVo.class);
    		
    		if(list != null && list.size() > 0){
    			for (HotelApptInfoVo info : list) {
    				//参数非空校验
    				if(StringUtils.isBlank(info.getApptName())){
    					baseBean.setCode(MsgCode.paramsError);
    					baseBean.setMsg("驾驶人不能为空!");
    					renderJSON(baseBean);
    					return;
    				}
    				if(StringUtils.isBlank(info.getIdentityCard())){
    					baseBean.setCode(MsgCode.paramsError);
    					baseBean.setMsg("驾驶证号不能为空!");
    					renderJSON(baseBean);
    					return;
    				}
    				if(StringUtils.isBlank(info.getPlateNo())){
    					baseBean.setCode(MsgCode.paramsError);
    					baseBean.setMsg("车牌号码不能为空!");
    					renderJSON(baseBean);
    					return;
    				}
    				if(StringUtils.isBlank(info.getPlateType())){
    					baseBean.setCode(MsgCode.paramsError);
    					baseBean.setMsg("号牌种类不能为空!");
    					renderJSON(baseBean);
    					return;
    				}
    				if(StringUtils.isBlank(info.getMobilePhone())){
    					baseBean.setCode(MsgCode.paramsError);
    					baseBean.setMsg("手机号码不能为空!");
    					renderJSON(baseBean);
    					return;
    				}
    				if(StringUtil.isBlank(info.getApptDate()) || StringUtil.isBlank(info.getApptDistrict()) || StringUtil.isBlank(info.getApptInterval())){
    					baseBean.setCode(MsgCode.paramsError);
    					baseBean.setMsg("选择预约时间不能为空!");
    					renderJSON(baseBean);
    					return;
    				}
    				
    				String apptInterval = info.getApptInterval();
    				//预约的是上午
    				if("1".equals(apptInterval)){
    					//表示 "2017-06-10 12:00:00"
    					String strTwelve = info.getApptDate() + " " + "12:00:00";	//上午预约结束时间点
    					//转化为date
    					Date dateTwelve = DateUtil2.str2date(strTwelve);
    					//预约时间在可预约当天12点整之后,不能预约上午
    					if(new Date().after(dateTwelve)){
    						baseBean.setCode(MsgCode.paramsError);
    						baseBean.setMsg("车牌号为："+info.getPlateNo()+"，预约的上午时段已过，请选择其他时段！");
    						renderJSON(baseBean);
    						logger.info("预约时间在可预约当天12点整之后,不能预约上午");
    						return;
    					}
    				}
    			}
    			
    			baseBean = activityService.addHotelApptInfo(agencyCode, branchCode, list);
    			
    			//选取预约结果为成功,发送短信提醒
    			List<HotelApptResultVo> resultList= (List<HotelApptResultVo>) baseBean.getData();
    			if(resultList != null && resultList.size() > 0){
    				for (HotelApptResultVo resultVo : resultList) {
    					if("1".equals(resultVo.getResult())){//预约结果 0-失败，1-成功
    						//根据预约id查询预约信息
    						BaseBean detailBean = activityService.getApptInfoDetailByApptId(resultVo.getApptId());
    						ApptInfoDetailVo detail = (ApptInfoDetailVo) detailBean.getData();
							String msgContent = MsgTemplate.getEastHotelApptSuccessMsgTemplate(hotelName, detail.getApptDate(), detail.getApptInterval(), detail.getApptDistrict());
							boolean flag = mobileMessageService.sendMessage(detail.getMobilePhone(), msgContent);
							if(flag){
								logger.info("发送短信提醒成功:" + msgContent);
							}else{
								logger.info("发送短信提醒失败:" + msgContent);
							}
    					}
    				}
    			}
    		}
    		
    		
    	} catch (Exception e) {
    		logger.error("酒店预约信息写入Action异常:" + e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
	}
    
    /**
	 * 获取酒店预约信息列表
	 * @Description: TODO(获取酒店预约信息列表)
	 * @param vo 酒店信息
	 * @param apptDate 预约日期
	 */
    @RequestMapping("getHotelApptHistoryByDate")
    public void getHotelApptHistoryByDate(HotelInfoVo vo, String apptDate){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
    	try {
    		//apptDate为空,默认查询最近可预约日期
    		if(StringUtils.isBlank(apptDate)){
    			BaseBean dateBean = activityService.getNormalApptDate("C");
    			if(MsgCode.success.equals(dateBean.getCode())){
    				String[] dates = dateBean.getData().toString().split(",");
    				if(dates != null && dates.length > 0){
    					for (String date : dates) {
    						baseBean = activityService.getHotelApptHistoryByDate(vo, date);
    						if(MsgCode.success.equals(baseBean.getCode())){
    							break;
    						}
    					}
    				}
    			}
    		}else{
    			baseBean = activityService.getHotelApptHistoryByDate(vo, apptDate);
    		}
    		
    		
    	} catch (Exception e) {
    		logger.error("获取酒店预约信息列表Action异常:" + e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    	
    }
    
    /**
	 * 获取酒店预约信息根据查询类型
	 * @Description: TODO(获取酒店预约信息根据查询类型)
	 * @param apptInfo 预约信息
	 * @param hotelInfo 酒店信息
	 * @param queryType 查询类型 1:根据号牌号码查询，2:根据姓名查询，3:根据手机号码查询
	 */
    @RequestMapping("getHotelApptInfoByQueryType")
    public void getHotelApptInfoByQueryType(HotelApptInfoVo apptInfo, HotelInfoVo hotelInfo, String queryType){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
    	try {
    		if("1".equals(queryType)){//1:根据号牌号码查询
    			if(StringUtils.isBlank(apptInfo.getPlateNo())){
    				baseBean.setCode(MsgCode.paramsError);
    				baseBean.setMsg("车牌号码不能为空!");
    				renderJSON(baseBean);
    				return;
    			}
    			if(StringUtils.isBlank(apptInfo.getPlateType())){
    				baseBean.setCode(MsgCode.paramsError);
    				baseBean.setMsg("号牌种类不能为空!");
    				renderJSON(baseBean);
    				return;
    			}
    		}
    		else if("2".equals(queryType)){//2:根据姓名查询
    			if(StringUtils.isBlank(apptInfo.getApptName())){
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("驾驶人不能为空!");
					renderJSON(baseBean);
					return;
				}
    		}
    		else if("3".equals(queryType)){//3:根据手机号码查询
    			if(StringUtils.isBlank(apptInfo.getMobilePhone())){
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("手机号码不能为空!");
					renderJSON(baseBean);
					return;
				}
    		}
    		
    		
    		baseBean = activityService.getHotelApptInfoByQueryType(apptInfo, hotelInfo, queryType);
    		
    	} catch (Exception e) {
    		logger.error("获取酒店预约信息根据查询类型Action异常:" + e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    	
    }

    /**
     * 取消酒店预约信息
     * @Description: TODO(取消酒店预约信息)
     * @param apptId 预约编号
     * @param cancelReason 取消原因
	 * @param sourceOfCertification 请求来源
	 */
    @RequestMapping("cancelHotelApptInfo")
    public void cancelHotelApptInfo(String apptId, String cancelReason, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
		try {
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
			}else{
				if(cancelReason.length() > 100){
					baseBean.setCode(MsgCode.paramsError);
					baseBean.setMsg("取消原因不能超过100个字符，请重新输入！");
					renderJSON(baseBean);
					return;
				}
			}
			
			baseBean = activityService.cancelHotelApptInfo(apptId, cancelReason, sourceOfCertification);
			
		} catch (Exception e) {
			logger.error("取消个人预约信息Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 酒店预约信息详情
	 * @Description: TODO(酒店预约信息详情)
	 * @param apptId 预约编号
	 * @throws Exception 
	 */
    @RequestMapping("getApptInfoDetailByApptId")
    public void getApptInfoDetailByApptId(String apptId, HotelInfoVo info, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	
    	try {
    		if(StringUtil.isBlank(apptId)){
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("预约编号不能为空!");
    			renderJSON(baseBean);
    			return;
    		}
    		if(StringUtils.isBlank(info.getAgencyCode())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("组织机构代码不能为空!");
				renderJSON(baseBean);
				return;
			}
    		if(StringUtils.isBlank(info.getBranchCode())){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("分店编号不能为空!");
				renderJSON(baseBean);
				return;
			}
    		
    		baseBean = activityService.getApptInfoDetailByApptId(apptId);
    		
    		ApptInfoDetailVo detail = (ApptInfoDetailVo) baseBean.getData();
			if(detail != null){
				BaseBean quotaBean = activityService.getHotelQuotaInfo(detail.getApptDate(), detail.getApptDistrict(), info, sourceOfCertification);
				ApptDistrictAndTimeVo vo = (ApptDistrictAndTimeVo) quotaBean.getData();
				if(vo != null){
					detail.setTotalQuota(vo.getTotalQuota());
					detail.setLeftQuota(vo.getLeftQuota());
				}
    		}
    		
    	} catch (Exception e) {
    		logger.error("酒店预约信息详情Action异常:" + e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
}
