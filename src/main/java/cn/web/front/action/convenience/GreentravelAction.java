package cn.web.front.action.convenience;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.convenience.bean.ApplyGreenRet;
import cn.convenience.bean.GreenTravelBean;
import cn.convenience.service.IGreentravelService;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.WebServiceException;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;


/**
 * 
 * @ClassName: GreentravelAction 
 * @Description: TODO(绿色出行Action) 
 * @author zhongyulin 
 * 
 *
 */
@Controller
@RequestMapping(value="/greentravel")
public class GreentravelAction extends BaseAction{
	private final static Logger logger = LoggerFactory.getLogger(ConvenienceAction.class);
	
	@Autowired
    @Qualifier("greentravelService")
	private IGreentravelService greentravelService;
	
	 /**
	 * 
	 *  @Title: applyDownDate 
	 * @Description: TODO(可申请停驶日期提取) 
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/applyDownDateReport.html")
	public void applyDownDateReport(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			GreenTravelBean greenBean=new GreenTravelBean();
			String hphm = request.getParameter("hphm");  		//车牌号
			String hpzl = request.getParameter("hpzl");  		//号牌种类
			String sfzmhm = request.getParameter("sfzmhm");    //身份证号码
			String month = request.getParameter("month");      //月份
			//验证参数车牌号
			if (StringUtil.isBlank(hphm)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "车牌号不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数号牌号码
			if (StringUtil.isBlank(hpzl)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "号牌种类不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			//验证参数身份证号码
			if (StringUtil.isBlank(sfzmhm)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "身份证号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			//验证参数月份
			if (StringUtil.isBlank(month)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "月份不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			greenBean.setHphm(hphm);
			greenBean.setHpzl(hpzl);
			greenBean.setSfzmhm(sfzmhm);
			greenBean.setMonth(month);
			BaseBean refBean=greentravelService.applyDownDate(greenBean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			jsonMap.put("date", refBean.getData());
			out.print(JSONObject.fromObject(jsonMap));
		}catch(Exception e){
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
	 * 
	 *  @Title: downDatedeclareReport 
	 * @Description: TODO(提交申报停驶日期) 
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/downDatedeclareReport.html")
	public void downDatedeclareReport(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			String sname = request.getParameter("sname");      //车主姓名
			String sfzmhm = request.getParameter("sfzmhm");      //身份证号码
			String mobile = request.getParameter("mobile");      //手机号码
			String hphm = request.getParameter("hphm");        //车牌号码
			String hpzl=request.getParameter("hpzl");        //号牌种类 （01,02）
			String sfbr=request.getParameter("sfbr");        //1是本人，0是非本人
			String lrly=request.getParameter("lrly");        //申请来源（WX:微信，ZFB支付宝，APP:手机App）
			String cdate=request.getParameter("cdate");      //申请停驶日期
			String type=request.getParameter("type");       //申请类型
			
			//验证参数车主姓名
			if (StringUtil.isBlank(sname)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "车主姓名不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			//验证参数身份证号码
			if (StringUtil.isBlank(sfzmhm)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "身份证号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数手机号码
			if (StringUtil.isBlank(mobile)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "手机号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数车牌号码
			if (StringUtil.isBlank(hphm)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "车牌号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证参数号牌种类
			if (StringUtil.isBlank(hpzl)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "号牌种类不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			//验证参数是否是本人
			if (StringUtil.isBlank(sfbr)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "是否为本人不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			//验证参数申请来源
			if (StringUtil.isBlank(lrly)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "申请来源不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			//验证参数申请停驶日期
			if (StringUtil.isBlank(cdate)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "申请停驶日期不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
		   //验证参数申请类型
			if (StringUtil.isBlank(type)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "申请类型不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			String[] ret=cdate.split(",");
			String[] retType=type.split(",");
			//验证参数时间日期与申请类型个数是否一致
			if (ret.length!=retType.length) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "申请日期与申请类型参数格式不一致！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			GreenTravelBean greenTravelBean=new GreenTravelBean();
			greenTravelBean.setSname(sname);
			greenTravelBean.setHphm(hphm);   //车牌号码(带’粤’)
			greenTravelBean.setHpzl(hpzl);
			greenTravelBean.setSfzmhm(sfzmhm);   //身份证明号码
			greenTravelBean.setMobile(mobile);
			greenTravelBean.setSfbr(sfbr);
			greenTravelBean.setLrly(lrly);
			List<ApplyGreenRet> list=new ArrayList<ApplyGreenRet>();
			for (int i = 0; i < ret.length; i++) {
				ApplyGreenRet appret=new ApplyGreenRet();
				appret.setCdate(ret[i]);
				appret.setType(retType[i]);
				list.add(appret);
			}
			greenTravelBean.setApplyGreenRetList(list);
			BaseBean refBean=greentravelService.downDatedeclare(greenTravelBean);
			jsonMap.put("code", refBean.getCode());
			if("0002".equals(refBean.getCode())){
				jsonMap.put("msg", "数据处理出现异常");
			}else{
				jsonMap.put("msg", refBean.getMsg());
			}
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
	 * 
	 *  @Title: applyrunningQuery 
	 * @Description: TODO(申请流水查询) 
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/applyrunningQuery.html")
	public void applyrunningQuery(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			GreenTravelBean greenBean=new GreenTravelBean();
			String hphm = request.getParameter("hphm");      //车牌号码
			String hpzl = request.getParameter("hpzl");      //号码种类
			String sqrq = request.getParameter("sqrq");      //申请日期不能为空
			//验证参数车牌号码
			if (StringUtil.isBlank(hphm)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "车牌号码不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			//验证参数号码种类
			if (StringUtil.isBlank(hpzl)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "号牌种类不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			
			//验证申请日期
			if (StringUtil.isBlank(sqrq)) {
				jsonMap.put("code", MsgCode.paramsError);
				jsonMap.put("msg", "申请日期不能为空！");
				out.print(JSONObject.fromObject(jsonMap));
				return;
		    }
			greenBean.setHphm(hphm);
			greenBean.setHpzl(hpzl);
			greenBean.setSqrq(sqrq);
			BaseBean refBean=greentravelService.applyrunningQuery(greenBean);
			jsonMap.put("code", refBean.getCode());
			jsonMap.put("msg", refBean.getMsg());
			jsonMap.put("date", refBean.getData());
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
}
