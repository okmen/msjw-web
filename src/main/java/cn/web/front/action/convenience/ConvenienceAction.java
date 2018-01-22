package cn.web.front.action.convenience;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;

import cn.convenience.bean.ConvenienceBean;
import cn.convenience.bean.FeedbackResultBean;
import cn.convenience.bean.SzjjVote;
import cn.convenience.bean.SzjjVoteRecord;
import cn.convenience.service.IConvenienceService;
import cn.message.service.ITemplateMessageService;
import cn.message.service.IWechatService;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.WebServiceException;
import cn.sdk.util.DateUtil;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;
import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: ConvenienceAction 
 * @Description: TODO(便民服务类Action) 
 * @author yangzan 
 * @date 2017年4月10日 上午10:19:49 
 *
 */
@Controller
@RequestMapping(value="/convenience")
public class ConvenienceAction extends BaseAction{
	private final static Logger logger = LoggerFactory.getLogger(ConvenienceAction.class);

    @Autowired
    @Qualifier("convenienceService")
    private IConvenienceService convenienceService;
    
    @Autowired
	@Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;
	
    @Autowired
    @Qualifier("wechatService")
    private IWechatService wechatService;
	/**
	 * @Title: equipmentDamageReport 
	 * @Description: TODO(设备损坏通报) 
	 * @param @param request
	 * @param @param response 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/equipmentDamageReport.html")
	public void equipmentDamageReport(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			
			ConvenienceBean bean = new ConvenienceBean();
			String userName = request.getParameter("userName");  		//用户姓名
			String mobilephone = request.getParameter("mobilephone");   //手机号码
			String identityCard = request.getParameter("identityCard");  //身份证号
			String addressRegion = request.getParameter("addressRegion");  //地址区域
			String addressStreet = request.getParameter("addressStreet");  //地址街道
			String addressSite = request.getParameter("addressSite");  	   //地址站点
			String detailAddress = request.getParameter("detailAddress");  //详细地址
			String emergency = request.getParameter("emergency");  		   //紧急程度
			String selectTypeId = request.getParameter("selectTypeId");    //选择类型id
			String selectType = request.getParameter("selectType");  	   //选择类型
			String subTypeId = request.getParameter("subTypeId");  		   //子类型选择id
			String subType = request.getParameter("subType");  			   //子类型选择
			String description = request.getParameter("description");  	   //现场描述  可为空
			String sceneImg = request.getParameter("sceneImg");  		   //现场图片		可为空
			String imgTime = request.getParameter("imgTime");  			//发现时间
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			
			
			//验证参数用户姓名
			if (StringUtil.isBlank(userName)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "用户名称不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数详细地址
			if (StringUtil.isBlank(detailAddress)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "详细地址不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数紧急程度
			if (StringUtil.isBlank(emergency)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "紧急程度不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数选择类型
			if (StringUtil.isBlank(selectTypeId) || StringUtil.isBlank(selectType)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "选择类型或id不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数子类型选择
			if (StringUtil.isBlank(subTypeId) || StringUtil.isBlank(subType)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "子类型选择或id不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证拍照时间
/*			if (StringUtil.isBlank(imgTime)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "拍照时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }*/
			
			//验证来源
			if (StringUtil.isBlank(sourceOfCertification)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "来源不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			bean.setUserName(userName);    //用户姓名
			bean.setMobilephone(mobilephone);   //用户手机
			bean.setAddressRegion(addressRegion);   //区域
			bean.setAddressStreet(addressStreet);	//街道
			bean.setAddressSite(addressSite);	    //站点
			bean.setDetailAddress(detailAddress); //详细地址
			bean.setEmergency(emergency);		//紧急程度  紧急、普通
			bean.setSelectTypeId(selectTypeId);     //申诉类型id
			bean.setSelectType(selectType);	//申诉类型描述
			bean.setSubTypeId(subTypeId);       //子类型id
			bean.setSubType(subTypeId);		//子类型描述
			bean.setDescription(description);	//现场描述
			bean.setSceneImg(sceneImg); 			//现场照片
			bean.setIdentityCard(identityCard);  //身份证号
			bean.setStartTime(imgTime);			 //拍照时间
			bean.setSourceOfCertification(sourceOfCertification); //来源
			
			
			//接口调用
			BaseBean refBean = convenienceService.equipmentDamageReport(bean);
        	
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			logger.error("设备损坏Action异常:"+e);
			
			//
			if(e instanceof WebServiceException){
				WebServiceException webServiceException = (WebServiceException) e;
				jsonMap.put("code", String.valueOf(webServiceException.getCode()));
				jsonMap.put("msg", MsgCode.webServiceCallMsg);
			}else{
	        	jsonMap.put("code", MsgCode.exception);
				jsonMap.put("msg", MsgCode.systemMsg);
			}
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
	
	/**
	 * @Title: safeHiddenDanger 
	 * @Description: TODO(安全隐患通报) 
	 * @param @param request
	 * @param @param response 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/safeHiddenDanger.html")
	public void safeHiddenDanger(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			
			ConvenienceBean bean = new ConvenienceBean();
			String userName = request.getParameter("userName");  		//用户姓名
			String mobilephone = request.getParameter("mobilephone");   //手机号码
			String identityCard = request.getParameter("identityCard");  //身份证号
			String addressRegion = request.getParameter("addressRegion");  //地址区域
			String addressStreet = request.getParameter("addressStreet");  //地址街道
			String addressSite = request.getParameter("addressSite");  	   //地址站点
			String detailAddress = request.getParameter("detailAddress");  //详细地址
			String emergency = request.getParameter("emergency");  		   //紧急程度
			String selectTypeId = request.getParameter("selectTypeId");    //选择类型id
			String selectType = request.getParameter("selectType");  	   //选择类型
			String subTypeId = request.getParameter("subTypeId");  		   //子类型选择id
			String subType = request.getParameter("subType");  			   //子类型选择
			String description = request.getParameter("description");  	   //现场描述  可为空
			String sceneImg = request.getParameter("sceneImg");  		   //现场图片		可为空
			String imgTime = request.getParameter("imgTime");  			//发现时间
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			
			//验证参数用户姓名
			if (StringUtil.isBlank(userName)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "用户名称不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数详细地址
			if (StringUtil.isBlank(detailAddress)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "详细地址不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数紧急程度
			if (StringUtil.isBlank(emergency)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "紧急程度不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数选择类型
			if (StringUtil.isBlank(selectTypeId) || StringUtil.isBlank(selectType)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "选择类型或id不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数子类型选择
			if (StringUtil.isBlank(subTypeId) || StringUtil.isBlank(subType)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "子类型选择或id不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证来源
			if (StringUtil.isBlank(sourceOfCertification)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "来源不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			bean.setUserName(userName);    //用户姓名
			bean.setMobilephone(mobilephone);   //用户手机
			bean.setAddressRegion(addressRegion);   //区域
			bean.setAddressStreet(addressStreet);	//街道
			bean.setAddressSite(addressSite);	    //站点
			bean.setDetailAddress(detailAddress); //详细地址
			bean.setEmergency(emergency);		//紧急程度  紧急、普通
			bean.setSelectTypeId(selectTypeId);     //申诉类型id
			bean.setSelectType(selectType);	//申诉类型描述
			bean.setSubTypeId(subTypeId);       //子类型id
			bean.setSubType(subType);		//子类型描述
			bean.setDescription(description);	//现场描述
			bean.setSceneImg(sceneImg); 			//现场照片
			bean.setIdentityCard(identityCard);  //身份证号
			bean.setStartTime(imgTime);			 //拍照时间
			bean.setSourceOfCertification(sourceOfCertification); //来源
			
			//接口调用
			BaseBean refBean = convenienceService.safeHiddenDanger(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			logger.error("安全隐患Action异常:"+e);
			
			//
			if(e instanceof WebServiceException){
				WebServiceException webServiceException = (WebServiceException) e;
				jsonMap.put("code", String.valueOf(webServiceException.getCode()));
				jsonMap.put("msg", MsgCode.webServiceCallMsg);
			}else{
	        	jsonMap.put("code", MsgCode.exception);
				jsonMap.put("msg", MsgCode.systemMsg);
			}
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
	
	/**
	 * @Title: trafficCongestion 
	 * @Description: TODO(交通拥堵通报) 
	 * @param @param request
	 * @param @param response 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/trafficCongestion.html")
	public void trafficCongestion(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			
			ConvenienceBean bean = new ConvenienceBean();
			String mobilephone = request.getParameter("mobilephone");   //手机号码
			String identityCard = request.getParameter("identityCard");  //身份证号
			String address = request.getParameter("address");  //主题地点描述
			String addressCode = request.getParameter("addressCode");  //地点代码
//			String ip = request.getParameter("ip");  //ip地址
			String ip = request.getRemoteAddr();  //ip地址
			String startTime = request.getParameter("startTime");  //开始时间
			String endTime = request.getParameter("endTime");  //结束时间
			String direction = request.getParameter("direction");  //方向
			String congestionType = request.getParameter("congestionType");  //拥堵类型
			String congestionGrade = request.getParameter("congestionGrade");  //拥堵等级
			String roadServiceLevel = request.getParameter("roadServiceLevel");  //道路服务水平
			String congestionReason = request.getParameter("congestionReason");  //拥堵成因
			String improveAdvice = request.getParameter("improveAdvice");  //改善建议
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址
			if (StringUtil.isBlank(address)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "地点不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址代码
			if (StringUtil.isBlank(addressCode)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "地点代码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数开始时间
			if (StringUtil.isBlank(startTime)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "开始时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数结束时间
			if (StringUtil.isBlank(endTime)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "结束时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数方向
			if (StringUtil.isBlank(direction)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "方向不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数拥堵类型
			if (StringUtil.isBlank(congestionType)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "拥堵类型不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数拥堵等级
			if (StringUtil.isBlank(congestionGrade)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "拥堵等级不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数道路服务水平
			if (StringUtil.isBlank(roadServiceLevel)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "道路服务水平不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数拥堵成因
			if (StringUtil.isBlank(congestionReason)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "拥堵成因不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证来源
			if (StringUtil.isBlank(sourceOfCertification)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "来源不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			bean.setIdentityCard(identityCard);  //身份证号
			bean.setMobilephone(mobilephone);   //用户手机
			bean.setIp(ip);    //ip
			bean.setStartTime(startTime); //时间段
			bean.setEndTiem(endTime);  //时间段
			bean.setDirection(direction);   //拥堵方向
			bean.setCongestionType(congestionType); //拥堵类型
			bean.setCongestionGrade(congestionGrade); 	//拥堵等级
			bean.setRoadServiceLevel(roadServiceLevel);    //道路服务水平
			bean.setCongestionReason(congestionReason); 		//拥堵成因
			bean.setImproveAdvice(improveAdvice);  	//改善建议
			bean.setAddress(address);			//主题地点描述
			bean.setAddressCode(addressCode);  // 经纬度  主题地点代码
			bean.setSourceOfCertification(sourceOfCertification); //来源
			
			//接口调用
			BaseBean refBean = convenienceService.trafficCongestion(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			logger.error("交通拥堵Action异常:"+e);
			
			//
			if(e instanceof WebServiceException){
				WebServiceException webServiceException = (WebServiceException) e;
				jsonMap.put("code", String.valueOf(webServiceException.getCode()));
				jsonMap.put("msg", MsgCode.webServiceCallMsg);
			}else{
	        	jsonMap.put("code", MsgCode.exception);
				jsonMap.put("msg", MsgCode.systemMsg);
			}
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
	
	
	/**
	 * @Title: sequenceChaos 
	 * @Description: TODO(秩序混乱通报) 
	 * @param @param request
	 * @param @param response 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/sequenceChaos.html")
	public void sequenceChaos(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			
			ConvenienceBean bean = new ConvenienceBean();
			String mobilephone = request.getParameter("mobilephone");   //手机号码
			String identityCard = request.getParameter("identityCard");  //身份证号
			String address = request.getParameter("address");  //主题地点描述
			String addressCode = request.getParameter("addressCode");  //地点代码
//			String ip = request.getParameter("ip");  //ip地址
			String ip = request.getRemoteAddr();  //ip地址
			String startTime = request.getParameter("startTime");  //开始时间
			String endTime = request.getParameter("endTime");  //结束时间
			String congestionCode = request.getParameter("congestionCode");  //拥堵类型code
			String congestionType = request.getParameter("congestionType");  //拥堵类型
			String description = request.getParameter("description");  //现场描述
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址
			if (StringUtil.isBlank(address)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "地点不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址代码
			if (StringUtil.isBlank(addressCode)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "地点代码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数开始时间
			if (StringUtil.isBlank(startTime)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "开始时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数结束时间
			if (StringUtil.isBlank(endTime)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "结束时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数拥堵类型code
			if (StringUtil.isBlank(congestionCode) || StringUtil.isBlank(congestionType)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "拥堵类型或id不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证来源
			if (StringUtil.isBlank(sourceOfCertification)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "来源不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			bean.setIdentityCard(identityCard);  //身份证号
			bean.setMobilephone(mobilephone);   //用户手机
			bean.setIp(ip);    //ip
			bean.setStartTime(startTime); //时间段
			bean.setEndTiem(endTime);  //时间段
			bean.setImproveAdvice(description);  	//改善建议
			bean.setCongestionCode(congestionCode);  //拥堵类型代码
			bean.setCongestionType(congestionType);  //机动车违法停放
			bean.setAddress(address);			//主题地点描述
			bean.setAddressCode(addressCode);  // 经纬度  主题地点代码
			bean.setSourceOfCertification(sourceOfCertification); //来源
			
			//接口调用
			BaseBean refBean = convenienceService.sequenceChaos(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			logger.error("秩序混乱Action异常:"+e);
			
			//
			if(e instanceof WebServiceException){
				WebServiceException webServiceException = (WebServiceException) e;
				jsonMap.put("code", String.valueOf(webServiceException.getCode()));
				jsonMap.put("msg", MsgCode.webServiceCallMsg);
			}else{
	        	jsonMap.put("code", MsgCode.exception);
				jsonMap.put("msg", MsgCode.systemMsg);
			}
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
	
	
	/**
	 * @Title: oneKeyDodgen 
	 * @Description: TODO(一键挪车) 
	 * @param @param request
	 * @param @param response 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/oneKeyDodgen.html")
	public void oneKeyDodgen(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="abbreviation",required=false) String abbreviation,
			@RequestParam(value="numberPlate",required=false) String numberPlate,
			@RequestParam(value="carType",required=false) String carType,
			@RequestParam(value="doodgenAddress",required=false) String doodgenAddress,
			@RequestParam(value="identityCard",required=false) String identityCard,
			@RequestParam(value="sourceOfCertification",required=false) String sourceOfCertification
			){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			
			ConvenienceBean bean = new ConvenienceBean();
		/*	String abbreviation1 = request.getParameter("abbreviation");   //车牌简称
			String numberPlate1 = request.getParameter("numberPlate");   //车牌号码
			String carType1 = request.getParameter("carType");   //汽车种类
			String doodgenAddress1 = request.getParameter("doodgenAddress");   //挪车地址
			String identityCard1 = request.getParameter("identityCard");   //身份证号
			*/
			//验证参数车牌简称
			if (StringUtil.isBlank(abbreviation) || StringUtil.isBlank(numberPlate)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "车牌简称或车牌号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证汽车种类
			if (StringUtil.isBlank(carType)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "汽车种类不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证挪车地址
			if (StringUtil.isBlank(doodgenAddress)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "挪车地址不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证来源
			if (StringUtil.isBlank(sourceOfCertification)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "来源不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			bean.setIdentityCard(identityCard);  //身份证号
			bean.setNumberPlate(numberPlate);   //车牌号
			bean.setAbbreviation(abbreviation);		//车牌简称
			bean.setCarType(carType);		//车类型
			bean.setDoodgenAddress(doodgenAddress);
			bean.setSourceOfCertification(sourceOfCertification); //来源
			
			BaseBean refBean = convenienceService.oneKeyDodgen(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			logger.error("一键挪车Action异常:"+e);
			
			//
			if(e instanceof WebServiceException){
				WebServiceException webServiceException = (WebServiceException) e;
				jsonMap.put("code", String.valueOf(webServiceException.getCode()));
				jsonMap.put("msg", MsgCode.webServiceCallMsg);
			}else{
	        	jsonMap.put("code", MsgCode.exception);
				jsonMap.put("msg", MsgCode.systemMsg);
			}
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
	
	/**
     * 
     * @Title: historyNotice
     * @Description: TODO(历史通报)
     * @return void    返回类型
     */
    @RequestMapping(value = "getAllResourcesAbsoluteUrl")
    public void getAllResourcesAbsoluteUrl(){
    	BaseBean baseBean = new BaseBean();
    	
    	List<FeedbackResultBean> list = null;
    	try {
			list = convenienceService.getAllResourcesAbsoluteUrl();
			baseBean.setCode(MsgCode.success);
			baseBean.setMsg("");
			baseBean.setData(list);
		} catch (Exception e) {
			logger.error("历史通报 Action异常:", e);
   			DealException(baseBean, e);
		}
    	renderJSON(baseBean);
   		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 根据档案编号查询电动车档案信息
	 * @Description: TODO(根据档案编号查询电动车档案信息)
	 * @param fileNo 档案编号
     */
    @RequestMapping(value = "getEbikeInfoByFileNo")
    public void getEbikeInfoByFileNo(String fileNo){
    	BaseBean baseBean = new BaseBean();
    	
    	try {
    		if (StringUtil.isBlank(fileNo)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("档案编号不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		baseBean = convenienceService.getEbikeInfoByFileNo(fileNo);
    		
    		logger.info("根据档案编号查询电动车档案信息Action返回结果:" + JSON.toJSONString(baseBean));
    	} catch (Exception e) {
    		logger.error("根据档案编号查询电动车档案信息Action异常:", e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 深圳交警投票
     * @param request
     * @param response
     */
    @RequestMapping(value = "szjjVote")
    public void szjjVote(HttpServletRequest request,HttpServletResponse response){
    	String voteKey = "SZJJ_VOTE_KEY_";
    	BaseBean baseBean = new BaseBean();
    	String voteId = request.getParameter("voteId");
    	String openId = request.getParameter("openId");
    	String str="2018-01-30 00:00:00";
		String dataStr=DateUtil2.date2str(DateUtil2.dayStr2date(str));
		Date date=new Date();
	    boolean ret=date.before(DateUtil2.dayStr2date(str));
    	DateUtil.getEndOfDay(new Date()).getTime();
    	if (!ret) {
    		baseBean.setCode(MsgCode.businessError);
			baseBean.setMsg("活动已结束!");
			renderJSON(baseBean);
			return;
		}
    	try {
    		if (StringUtil.isBlank(voteId)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("id不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		
			if (StringUtil.isBlank(openId)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("openId不能为空!");
    			renderJSON(baseBean);
				return;
		    }
//    		String authOpenid = wechatService.getAuthOpenid(openId);
//    		if (StringUtil.isBlank(authOpenid)) {
//    			baseBean.setCode(MsgCode.businessError);
//				baseBean.setMsg("请先授权再进行投票！");
//				renderJSON(baseBean);
//				return;
//			}
    		
    		String[] voteIds = voteId.split(",");
    		long currentTimeMillis = System.currentTimeMillis();
			long endOfDayTime = DateUtil.getEndOfDay(new Date()).getTime();
			int remainTime = (int) (endOfDayTime - currentTimeMillis);
			
			boolean flag = convenienceService.exists(voteKey+openId);
    		if (flag) {
    			int szjjVote = convenienceService.getSzjjVote(voteKey+openId);
    			if (szjjVote == 3) {
    				baseBean.setCode(MsgCode.businessError);
    				baseBean.setMsg("您已参加过三次投票，请明天再来吧！");
    				renderJSON(baseBean);
    				return;
				}else{
					convenienceService.setSzjjVoteKey(voteKey+openId, szjjVote+1, remainTime/1000);
				}
			}else{
				convenienceService.setSzjjVoteKey(voteKey+openId, 1, remainTime/1000);
			}
    		int result  = convenienceService.updateBySzjjId(voteIds);
    		if (result > 0) {
    			try{
    				SzjjVoteRecord record = new SzjjVoteRecord();
    				record.setIp(getIp2(request));
    				record.setOpenId(openId);
    				record.setVoteDate(new Date());
    				record.setVoteId(voteId);
    				int addVoteRecord = convenienceService.addSzjjVoteRecord(record);
    				if (addVoteRecord>0) {
    					logger.info("插入投票记录成功");
    				}
    			}catch(Exception e){
    				logger.info("插入投票记录异常："+e);
    				e.printStackTrace();
    			}
				baseBean.setCode(MsgCode.success);
				baseBean.setData("投票成功");
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("投票失败");
			}
    		logger.info("深圳交警投票返回结果:" + JSON.toJSONString(baseBean));
    	} catch (Exception e) {
    		logger.error("深圳交警投票Action异常:", e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    
    /**
     * 获取所有信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "getAllVote")
    public void getAllVote(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	try {
   
    		List<SzjjVote> allVote = convenienceService.getAllVote();
    		if (allVote != null && allVote.size() > 0) {
 
				baseBean.setCode(MsgCode.success);
				baseBean.setData(allVote);
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("未查询到相关数据");
			}
    		logger.info("深圳交警投票返回结果:" + JSON.toJSONString(baseBean));
    	} catch (Exception e) {
    		logger.error("深圳交警投票ActionAction异常:", e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
   /**
    * 平安好车主评选
    * @param request
    * @param response
    */
    /*@RequestMapping(value = "applyForPAGoodCarOwners")
    public void applyForPAGoodCarOwners(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	String ownerName = request.getParameter("ownerName");           
    	String driverLicenseNum  = request.getParameter("driverLicenseNum");       
    	String licenseNumber = request.getParameter("licenseNumber");        
    	String numberPlate = request.getParameter("numberPlate");            
    	String mobile = request.getParameter("mobile");                
    	String securityDeclaration = request.getParameter("securityDeclaration");    
    	String RZZP = request.getParameter("RZZP");                   
    	String sourceOfCertification = request.getParameter("sourceOfCertification");
    	String openId = request.getParameter("openId");
    	
    	try {
    		if (StringUtil.isBlank(ownerName)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("车主姓名不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		if (StringUtil.isBlank(driverLicenseNum)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("驾驶证号码不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		if (StringUtil.isBlank(mobile)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("手机号码不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		if (StringUtil.isBlank(securityDeclaration)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("安全宣言不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		if (StringUtil.isBlank(RZZP)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("认证照片不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		if (StringUtil.isBlank(sourceOfCertification)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("用户来源不能为空!");
    			renderJSON(baseBean);
				return;
		    } else {
		    	if(!"P".equals(sourceOfCertification)){
		    		if (StringUtil.isBlank(openId)) {
		    			baseBean.setCode(MsgCode.paramsError);
		    			baseBean.setMsg("openId不能为空!");
		    			renderJSON(baseBean);
		    			return;
		    		}
		    	}
		    }
    		ApplyForPAGoodCarOwners applyForPAGoodCarOwners =new ApplyForPAGoodCarOwners();
    		applyForPAGoodCarOwners.setDriverLicense(driverLicenseNum);
    		applyForPAGoodCarOwners.setLicenseNumber(licenseNumber);
    		applyForPAGoodCarOwners.setMobile(mobile);
    		applyForPAGoodCarOwners.setNumberPlate(numberPlate);
    		applyForPAGoodCarOwners.setOwnerName(ownerName);
    		applyForPAGoodCarOwners.setRZZP(RZZP);
    		applyForPAGoodCarOwners.setSecurityDeclaration(securityDeclaration);
    		applyForPAGoodCarOwners.setSourceOfCertification(sourceOfCertification);
    		baseBean = convenienceService.applyForPAGoodCarOwners(applyForPAGoodCarOwners);
    		if (MsgCode.success.equals(baseBean.getCode()) && "C".equals(sourceOfCertification)) {
				 //申请成功发送模板消息
				try {				   
					String templateId = "Cwi_5FWbVmJd5faWECiG7clOt4gts6hOxRHO8w4fdMU";
					String url = convenienceService.getTemplateSendUrl2()+"name="+ownerName+"&licenseNumber="+licenseNumber;
					logger.info("返回的url是：" + url);
					Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
					if (StringUtil.isBlank(licenseNumber)) {
						map.put("first", new TemplateDataModel().new Property("您好，尊敬的"+ownerName+"，您的申请已提交，具体信息如下：","#212121"));
					}else{
						map.put("first", new TemplateDataModel().new Property("您好，尊敬的"+ownerName+"，您的车牌号为"+licenseNumber+"的申请已提交，具体信息如下：","#212121"));
					}
					map.put("keyword1", new TemplateDataModel().new Property("平安好车主评选","#212121"));
					map.put("keyword2", new TemplateDataModel().new Property(DateUtil.formatDateTime(new Date()),"#212121"));
					map.put("remark", new TemplateDataModel().new Property("更多信息请点击详情查看", "#212121"));
					boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
					logger.info("发送模板消息结果：" + flag);
				} catch (Exception e) {
					logger.error("发送模板消息  失败===", e);
				}
				 
			}
    		logger.info("平安好车主评选Action返回结果:" + JSON.toJSONString(baseBean));
    	} catch (Exception e) {
    		logger.error("平安好车主评选Action异常:", e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }*/
    

   /* *//**
     * 获取所有信息
     * @param request
     * @param response
     *//*
    @RequestMapping(value = "getVoteByPage")
    public void getVoteByPage(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	String page = request.getParameter("page");
    	String pageSize = request.getParameter("pageSize");
    	try {
    		if (!StringUtil.isNumber(page)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("page不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		if (!StringUtil.isNumber(pageSize)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("pageSize不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		RequestPage requestPage = new RequestPage(Integer.parseInt(page), Integer.parseInt(pageSize));
    		List<ActivityVote> activityVoteList = convenienceService.getVoteByPage(requestPage.getPage(),requestPage.getPageSize());
    		int queryCount = convenienceService.queryCount();
    		Map<String, Object> map = new HashMap<>();
    		if (activityVoteList != null && activityVoteList.size() > 0) {
    			int sum = convenienceService.queryCountSum();
    			map.put("activityVoteList", activityVoteList);
    			map.put("totalCount", queryCount);
    			map.put("totalVote", sum);
				baseBean.setCode(MsgCode.success);
				baseBean.setData(map);
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("未查询到相关数据");
			}
    		logger.info("平安好车主投票Action返回结果:" + JSON.toJSONString(baseBean));
    	} catch (Exception e) {
    		logger.error("平安好车主投票ActionAction异常:", e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    *//**
     * 获取前多少位排名
     * @param request
     * @param response
     *//*
    @RequestMapping(value = "getFrontVote")
    public void getFrontVote(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	String total = request.getParameter("total");
    	List<ActivityVote>  activityVoteList  = null;
    	try {
    		if (!StringUtil.isNumber(total)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("total不能为空!");
    			renderJSON(baseBean);
				return;
		    }
    		activityVoteList = (List<ActivityVote>) convenienceService.getFront15();
    		if(activityVoteList == null){
    			activityVoteList = convenienceService.getFrontVote(Integer.parseInt(total));
    			convenienceService.setFront15(activityVoteList);
    		}
    		if (activityVoteList != null && activityVoteList.size() > 0) {
				baseBean.setCode(MsgCode.success);
				baseBean.setData(activityVoteList);
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("未查询到相关数据");
			}
    		logger.info("平安好车主投票Action返回结果:" + JSON.toJSONString(baseBean));
    	} catch (Exception e) {
    		logger.error("平安好车主投票ActionAction异常:", e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    *//**
     * 投票流量监控
     * @param request
     * @param response
     *//*
    @RequestMapping(value = "netCounter")
    public void netCounter(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	String sourceOfCertification = request.getParameter("sourceOfCertification");
    	try {
    		if (StringUtil.isBlank(sourceOfCertification)) {
    			baseBean.setCode(MsgCode.paramsError);
    			baseBean.setMsg("sourceOfCertification不能为空!");
    			renderJSON(baseBean);
				return;
		    }else if ("C".equals(sourceOfCertification)) {
				convenienceService.goodCarOwnerWechat();
			}else if ("Z".equals(sourceOfCertification)) {
				convenienceService.goodCarOwnerAlipay();
			}else if ("P".equals(sourceOfCertification)) {
				convenienceService.goodCarOwnerPingan();
			}
    		
    		//logger.info("平安好车主投票流量监控Action返回结果:" + JSON.toJSONString(baseBean));
    	} catch (Exception e) {
    		logger.error("平安好车主投票流量监控ActionAction异常:", e);
    		DealException(baseBean, e);
    	}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }*/
    

}
