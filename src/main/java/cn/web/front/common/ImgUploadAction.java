package cn.web.front.common;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.web.front.support.BaseAction;
import net.sf.json.JSONObject;

/**
 * @ClassName: ImgUploadAction 
 * @Description: TODO(图片上传工具类) 
 * @author yangzan 
 * @date 2017年4月10日 下午3:11:26 
 *
 */
@Controller
@RequestMapping(value="/common")
public class ImgUploadAction extends BaseAction{

	/**
	 * @Title: imgUpload 
	 * @Description: TODO(图片上传) 
	 * @param @param request
	 * @param @param response 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/imgUpload.html")
	public void imgUpload(HttpServletRequest request,HttpServletResponse response){
		PrintWriter out=null;
		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
		
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			
			jsonMap.put("code", "0000");
			jsonMap.put("msg", "图片上传成功！");
			jsonMap.put("imgUrl", "http://image.baidu.com/");
			out.print(JSONObject.fromObject(jsonMap));
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", "1010");
			jsonMap.put("msg", "服务器繁忙！");
			out.print(JSONObject.fromObject(jsonMap));
		}
	}
}
