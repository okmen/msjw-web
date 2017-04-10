package cn.web.front.action.convenience;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
			
			jsonMap.put("code", "0000");
			jsonMap.put("msg", "操作成功！");
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", "1010");
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
			
			jsonMap.put("code", "0000");
			jsonMap.put("msg", "操作成功！");
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", "1010");
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
			
			jsonMap.put("code", "0000");
			jsonMap.put("msg", "操作成功！");
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", "1010");
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
			
			jsonMap.put("code", "0000");
			jsonMap.put("msg", "操作成功！");
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", "1010");
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
	public void oneKeyDodgen(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			
			jsonMap.put("code", "0000");
			jsonMap.put("msg", "操作成功！");
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", "1010");
			jsonMap.put("msg", "服务器繁忙！");
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
}
