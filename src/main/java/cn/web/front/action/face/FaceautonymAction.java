package cn.web.front.action.face;

import java.util.Date;

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

import cn.convenience.bean.SzjjToken;
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
	
	/*@Autowired
	@Qualifier("fileService")
	private IFileService fileService;*/
	
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
	@RequestMapping(value="/getdetectinfo.html",method=RequestMethod.POST)
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
				 String yterrorcode=json.getString("yt_errorcode");
				 if(!"0".equals(yterrorcode)){
			     	 baseBean.setCode(MsgCode.paramsError);
			     	 baseBean.setMsg("验证获取用户信息失败");
			     	 baseBean.setData("");
			     	logger.info("验证获取用户信息失败");
			         renderJSON(baseBean);
			         return;
				 }
			 }else{
				 baseBean.setMsg("未获取到用户信息");
		     	 baseBean.setCode(MsgCode.paramsError);
		     	logger.info("未获取到用户信息");
		         renderJSON(baseBean);
		     	 return;
			 }
			renderJSON(baseBean);
			logger.info("获取用户信息成功");
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("获取基本用户信息出现异常", e);
		}
	}
	
	/**
	 * 录入刷脸token
	 * @param request
	 * @param response
	 * @param szjjToken
	 */
	@RequestMapping(value="/insertToken.html",method=RequestMethod.POST)
	public void insertToken(HttpServletRequest request,HttpServletResponse response,SzjjToken szjjToken){
		 BaseBean baseBean=new BaseBean();
		 //验证身份证
		 if(StringUtils.isBlank(szjjToken.getIdentityCard())){
     		baseBean.setMsg("身份证不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	//验证token
     	}else if(StringUtils.isBlank(szjjToken.getToken())){
     		baseBean.setMsg("token不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	}
     	else if(StringUtils.isBlank(szjjToken.getPhone())){
     		baseBean.setMsg("手机号不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	}
     	else if(StringUtils.isBlank(szjjToken.getOpenId())){
     		baseBean.setMsg("openId不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	}else if(StringUtils.isBlank(szjjToken.getSource())){
     		baseBean.setMsg("来源不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	}
		 try {
			 szjjToken.setCreateTime(new Date());
			 int result = faceautonymService.insertSzjjToken(szjjToken);
			 logger.info("微信刷脸返回结果 result : " + result);
			 if (result > 0) {
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg("录入token成功");
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("录入token失败");
			}
			renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("微信刷脸录入token出现异常", e);
		}
	}
	
	/**
	 * 根据身份证获取用户信息
	 * @param identityCard
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getTokenByIdentityCard.html",method=RequestMethod.POST)
	public void getTokenByIdentityCard(String identityCard, HttpServletRequest request,HttpServletResponse response){
		 BaseBean baseBean=new BaseBean();
		 //验证身份证不能为空
		 if(StringUtils.isBlank(identityCard)){
     		baseBean.setMsg("idnetityCard不能为空!");
     		baseBean.setCode(MsgCode.paramsError);
     		renderJSON(baseBean);
     		return;
     	//验证token
     	}
		 SzjjToken szjjToken = null;
		 try {
			szjjToken = faceautonymService.querySzjjToken(identityCard);
			if (szjjToken != null) {
				baseBean.setCode(MsgCode.success);
				baseBean.setData(szjjToken);
			}else{
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("未查询到相关数据");
			}
			renderJSON(baseBean);
			logger.info("获取用户信息成功");
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("获取基本用户信息出现异常", e);
		}
	}
}
