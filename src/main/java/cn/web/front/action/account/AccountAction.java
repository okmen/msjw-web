package cn.web.front.action.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.IdentityVerificationAuditResultsVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.service.IAccountService;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.ResultCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;


/**
 * 个人中心
 * 
 * @author suntao
 * 
 */
@Controller
@RequestMapping(value="/user/")
@SuppressWarnings(value="all")
public class AccountAction extends BaseAction {
    @Autowired
    @Qualifier("accountService")
    private IAccountService accountService;

    @RequestMapping(value = "get-wechat-userInfo-by-id")
    public ModelAndView getWechatUserInfoById(HttpServletRequest request) {
        String version = request.getParameter("version");
        String idStr = request.getParameter("id");
        if (StringUtil.isBlank(idStr)) {
            return getErrorView(ResultCode.SYS_INVALID_REQUEST_PARAMS, version, "传递参数格式或类型有问题","");
        }
        int id = Integer.valueOf(idStr);
        if (id <= 0) {
            return getErrorView(ResultCode.SYS_INVALID_REQUEST_PARAMS, version, "传递参数错误", "");
        }
        
        cn.account.bean.WechatUserInfoBean wechatUserInfo = accountService.getWechatUserInfoById(id);
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (wechatUserInfo != null) {
            modelMap.put("openId", wechatUserInfo.getOpenId());
            modelMap.put("nickname", wechatUserInfo.getNickname());
            modelMap.put("headimgurl", wechatUserInfo.getHeadImgUrl());
        } else {
            return getErrorView(ResultCode.SYS_DB_NO_RESULT_EXCEPTION, version, "无结果","");
        }
        
        return getBaseView(request, modelMap, "/user/getWechatUserInfo.vm");
    }
    @RequestMapping(value="getInfo")
    public void getInfo(@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response) throws IOException{
    	 cn.account.bean.WechatUserInfoBean wechatUserInfo = accountService.getWechatUserInfoById(id);
    	 
    	 logger.info("info is:" + wechatUserInfo);
    	PrintWriter out =  response.getWriter();
    	out.print(JSON.toJSONString(wechatUserInfo));
    }
    
    /**
	 * 登录
	 * @param loginName
	 * @param password
	 * @param request
	 * @param response
	 * @throws IOException
	 * URL
	 * http://localhost:8080/web/user/login.html?loginName=13666666666&password=123456
	 */
    @RequestMapping(value="login",method=RequestMethod.POST)
    public void login(@RequestParam("loginName") String loginName,@RequestParam("password") String password,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    
    	BaseBean baseBean = new BaseBean();
    	baseBean.setCode("0000");
    	baseBean.setMsg("");
    	
    	AuthenticationBasicInformationVo authenticationBasicInformation = new AuthenticationBasicInformationVo();
    	authenticationBasicInformation.setMobilephone("13666666666");
    	authenticationBasicInformation.setMyAvatar("http://gaoboy.com/tpl/polo/Public/images/my_wechat_qrcode.jpg");
    	authenticationBasicInformation.setIdentityCard("431225199122222112");
    	authenticationBasicInformation.setMyNumberPlate("粤B868686");
    	authenticationBasicInformation.setTrueName("张小龙");
    	
    	
    	IdentityVerificationAuditResultsVo identityVerificationAuditResults = new IdentityVerificationAuditResultsVo();
    	identityVerificationAuditResults.setAuthenticationType(1);
    	identityVerificationAuditResults.setStatus(3);
    	identityVerificationAuditResults.setRetirementResult("身份证正面不清晰");
    	identityVerificationAuditResults.setReviewDate("2017-1-3 19:00:01");
    	
    	LoginReturnBeanVo loginReturnBean = new LoginReturnBeanVo();
    	loginReturnBean.setAuthenticationBasicInformation(authenticationBasicInformation);
    	loginReturnBean.setIdentityVerificationAuditResults(identityVerificationAuditResults);
    	
    	baseBean.setData(loginReturnBean);
    	
    	renderJSON(baseBean);
    	
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    
}
