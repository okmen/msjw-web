package cn.web.front.action.convenience;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.convenience.bean.ConvenienceBean;
import cn.convenience.service.IConvenienceService;
import cn.sdk.bean.BaseBean;
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
			
			//验证参数用户姓名
			if (StringUtil.isBlank(userName)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "用户名称不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数详细地址
			if (StringUtil.isBlank(detailAddress)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "详细地址不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数紧急程度
			if (StringUtil.isBlank(emergency)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "紧急程度不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数选择类型
			if (StringUtil.isBlank(selectTypeId) || StringUtil.isBlank(selectType)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "选择类型或id不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数子类型选择
			if (StringUtil.isBlank(subTypeId) || StringUtil.isBlank(subType)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "子类型选择或id不能为空！");
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
			
			
			//接口调用
			BaseBean refBean = convenienceService.equipmentDamageReport(bean);
			
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("设备损坏Action异常:"+e);
			
			jsonMap.put("code", "1010");
			jsonMap.put("msg", "服务器繁忙！");
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
			
			//验证参数用户姓名
			if (StringUtil.isBlank(userName)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "用户名称不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数详细地址
			if (StringUtil.isBlank(detailAddress)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "详细地址不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数紧急程度
			if (StringUtil.isBlank(emergency)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "紧急程度不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数选择类型
			if (StringUtil.isBlank(selectTypeId) || StringUtil.isBlank(selectType)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "选择类型或id不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数子类型选择
			if (StringUtil.isBlank(subTypeId) || StringUtil.isBlank(subType)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "子类型选择或id不能为空！");
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
			
			//接口调用
			BaseBean refBean = convenienceService.safeHiddenDanger(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("安全隐患Action异常:"+e);
			
			jsonMap.put("code", "1010");
			jsonMap.put("msg", "服务器繁忙！");
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
			String ip = request.getParameter("ip");  //ip地址
			String startTime = request.getParameter("startTime");  //开始时间
			String endTime = request.getParameter("endTime");  //结束时间
			String direction = request.getParameter("direction");  //方向
			String congestionType = request.getParameter("congestionType");  //拥堵类型
			String congestionGrade = request.getParameter("congestionGrade");  //拥堵等级
			String roadServiceLevel = request.getParameter("roadServiceLevel");  //道路服务水平
			String congestionReason = request.getParameter("congestionReason");  //拥堵成因
			String improveAdvice = request.getParameter("improveAdvice");  //改善建议
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址
			if (StringUtil.isBlank(address)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "地点不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址代码
			if (StringUtil.isBlank(addressCode)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "地点代码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数开始时间
			if (StringUtil.isBlank(startTime)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "开始时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数结束时间
			if (StringUtil.isBlank(endTime)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "结束时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数方向
			if (StringUtil.isBlank(direction)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "方向不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数拥堵类型
			if (StringUtil.isBlank(congestionType)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "拥堵类型不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数拥堵等级
			if (StringUtil.isBlank(congestionGrade)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "拥堵等级不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数道路服务水平
			if (StringUtil.isBlank(roadServiceLevel)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "道路服务水平不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证参数拥堵成因
			if (StringUtil.isBlank(congestionReason)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "拥堵成因不能为空！");
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
			
			//接口调用
			BaseBean refBean = convenienceService.trafficCongestion(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("交通拥堵Action异常:"+e);
			
			jsonMap.put("code", "1010");
			jsonMap.put("msg", "服务器繁忙！");
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
			String ip = request.getParameter("ip");  //ip地址
			String startTime = request.getParameter("startTime");  //开始时间
			String endTime = request.getParameter("endTime");  //结束时间
			String congestionCode = request.getParameter("congestionCode");  //拥堵类型code
			String congestionType = request.getParameter("congestionType");  //拥堵类型
			String description = request.getParameter("description");  //现场描述
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobilephone)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址
			if (StringUtil.isBlank(address)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "地点不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数地址代码
			if (StringUtil.isBlank(addressCode)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "地点代码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数开始时间
			if (StringUtil.isBlank(startTime)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "开始时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数结束时间
			if (StringUtil.isBlank(endTime)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "结束时间不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数拥堵类型code
			if (StringUtil.isBlank(congestionCode) || StringUtil.isBlank(congestionType)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "拥堵类型或id不能为空！");
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
			
			//接口调用
			BaseBean refBean = convenienceService.sequenceChaos(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("秩序混乱Action异常:"+e);
			
			jsonMap.put("code", "1010");
			jsonMap.put("msg", "服务器繁忙！");
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
			@RequestParam(value="identityCard",required=false) String identityCard
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
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "车牌简称或车牌号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证汽车种类
			if (StringUtil.isBlank(carType)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "汽车种类不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证挪车地址
			if (StringUtil.isBlank(doodgenAddress)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "挪车地址不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			//验证身份证号
			if (StringUtil.isBlank(identityCard)) {
				jsonMap.put("code", "1001");
				jsonMap.put("msg", "身份证号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
			}
			
			bean.setIdentityCard(identityCard);  //身份证号
			bean.setNumberPlate(numberPlate);   //车牌号
			bean.setAbbreviation(abbreviation);		//车牌简称
			bean.setCarType(carType);		//车类型
			bean.setDoodgenAddress(doodgenAddress);
			
			BaseBean refBean = convenienceService.oneKeyDodgen(bean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("一键挪车Action异常:"+e);
			
			jsonMap.put("code", "1010");
			jsonMap.put("msg", "服务器繁忙！");
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
}
