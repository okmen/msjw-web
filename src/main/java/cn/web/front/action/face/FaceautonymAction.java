package cn.web.front.action.face;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.convenience.service.IFaceautonymService;
import cn.file.service.IFileService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.web.front.support.BaseAction;

/**
 * 
 * @ClassName: FaceautonymAction 
 * @Description: TODO(微信刷脸Action) 
 * @author zhongyulin 
 * 
 *
 */
@Controller
@RequestMapping(value="/faceautonym")
public class FaceautonymAction  extends BaseAction{
	private final static Logger logger = LoggerFactory.getLogger(FaceautonymAction.class);
	
	@Autowired
	@Qualifier("fileService")
	private IFileService fileService;
	
	@Autowired
	@Qualifier("faceautonymService")
	private IFaceautonymService faceautonymService;
	
	/**
	 * 获取用户基本信息
	 * @param appid
	 * @param token
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/applyDownDateReport.html",method=RequestMethod.POST)
	public void getdetectinfo(String appid,String token, HttpServletRequest request,HttpServletResponse response){
		 BaseBean baseBean=new BaseBean();
		 //验证appid不能为空
		 if(StringUtils.isBlank(appid)){
     		baseBean.setMsg("appid不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	//验证token
     	}else if(StringUtils.isBlank(token)){
     		baseBean.setMsg("token不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	}
		 try {
			 baseBean=faceautonymService.getdetectinfo(appid, token);
			 if("0000".equals(baseBean.getCode())){
				JSONObject json=JSONObject.fromObject(baseBean.getData());
				String frontpic=json.getString("frontpic");
				if(StringUtils.isBlank(frontpic)){
					String frontpicPath=fileService.uploadFile(frontpic, ".jpg");
					json.put("frontpic",frontpicPath);
				}
				String backpic=json.getString("backpic");
				if(StringUtils.isBlank(frontpic)){
					String backpicPath=fileService.uploadFile(backpic, ".jpg");
					json.put("backpic",backpicPath);
				}
				String videopic1=json.getString("videopic1");
				if(StringUtils.isBlank(frontpic)){
					String videopicPath=fileService.uploadFile(videopic1, ".jpg");
					json.put("videopic1",videopicPath);
				}
				baseBean.setData(json);
			 }
			 renderJSON(baseBean);
			 logger.info("获取用户信息成功");
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("获取基本用户信息出现异常", e);
		}
	}
}