package cn.web.front.action.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.Documentation;
import cn.account.bean.ElectronicPolicyBean;
import cn.account.bean.UserBind;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.InformationSheetVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.bean.vo.UserBasicVo;
import cn.account.service.IAccountService;
import cn.message.service.IMobileMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.ResultCode;
import cn.sdk.util.MsgCode;
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
    
    @Autowired
    @Qualifier("mobileMessageService")
    private IMobileMessageService mobileMessageService;

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
     * 获取须知文档
     * @param noticeKey 须知文档key
     * @param request
     * @param response
     * http://192.168.1.161:8080/web/user/getDocumentationORMByNoticeKey.html?noticeKey=testKey
     * @throws Exception 
     */
    @RequestMapping("getDocumentationORMByNoticeKey")
    public void getDocumentationORMByNoticeKey(String noticeKey,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(noticeKey)){
        		baseBean.setMsg("noticeKey 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		Documentation documentation = accountService.getDocumentationByNoticeKey(noticeKey);
    		baseBean.setCode(MsgCode.success);
        	baseBean.setMsg("查询成功");
        	baseBean.setData(documentation);
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("getDocumentationORMByNoticeKey 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    /**
     * 重置密码
     * @param validateCode 验证码
     * @param identityCard 身份证
     * @param mobilephone 手机号
     * @param userName 用户名
     * @param sourceOfCertification 认证来源
     * @param request
     * @param response
     * @throws Exception
     * http://192.168.1.161:8080/web/user/resetPwd.html?validateCode=1111&identityCard=420881198302280017&mobilephone=18601174358&userName=孙涛&sourceOfCertification=C
     */
    @RequestMapping("resetPwd")
    public void resetPwd(String validateCode,String identityCard,String mobilephone,String userName,String sourceOfCertification , HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("认证来源不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(validateCode)){
        		baseBean.setMsg("验证码不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("身份证不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("手机号不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		if(StringUtils.isBlank(userName)){
        		baseBean.setMsg("用户名不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
    		//验证码是否正确
    		// 0-验证成功，1-验证失败，2-验证码失效
    		int result = accountService.verificatioCode(mobilephone, validateCode);
    		if(0 == result){
            	baseBean.setMsg("验证通过");
            	Map<String, String> map = accountService.resetPwd(identityCard, userName, mobilephone, sourceOfCertification);
            	if(null != map){
            		String code = map.get("code");
            		String msg = map.get("msg");
            		if(MsgCode.success.equals(code)){
            			baseBean.setCode(MsgCode.success);
            			baseBean.setMsg(msg);
            		}else{
            			baseBean.setCode(MsgCode.businessError);
            			baseBean.setMsg(msg);
            		}
            	}
    		}
			if(1 == result){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码错误");	
			 }
			if(2 == result){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("验证码失效,请重新获取");
			}
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("resetPwd 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 登录
	 * @param loginName 手机号或身份证
	 * @param password 密码
	 * @param request
	 * @param response
     * @throws Exception
     * http://localhost:8080/web/user/login.html?loginName=440301199002101119&password=631312&openId=000000xxx&loginClient=weixin&sourceOfCertification=C
	 */
    @RequestMapping(value="login")
    public void login(String sourceOfCertification,String loginName,String password,String openId,String loginClient,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(loginName)){
        		baseBean.setMsg("loginName 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(password)){
        		baseBean.setMsg("password 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(openId)){
        		baseBean.setMsg("openId 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	LoginReturnBeanVo loginReturnBeanVo = accountService.login(loginName,password,sourceOfCertification,openId,loginClient);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg(loginReturnBeanVo.getMsg());
            	baseBean.setData("");
        	}
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("login 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    /**
     * 发送短信验证码
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
    		String msgContent = "短信验证码："+valideteCode+"，您正在使用深圳交警微信业务，有效时间为5分钟。";
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
     * 验证验证码是否正确
     * @param mobilephone 手机号
     * @param validateCode 用户输入的验证码
     * @param request
     * @param response
     * http://localhost:8080/web/user/verificatioCode.html?mobilephone=13652311206&validateCode=1221
     */
    @RequestMapping("verificatioCode")
    public void verificatioCode(String mobilephone,String validateCode,HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(validateCode)){
        		baseBean.setMsg("validateCode 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	// 0-验证成功，1-验证失败，2-验证码失效
    		int result = accountService.verificatioCode(mobilephone, validateCode);
    		if(0 == result){
    			baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData("验证通过");
    		}
			if(1 == result){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("");
	        	baseBean.setData("验证码错误");		
			 }
			if(2 == result){
				baseBean.setCode(MsgCode.businessError);
	        	baseBean.setMsg("");
	        	baseBean.setData("验证码失效,请重新获取");
			}
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("verificatioCode 错误!", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 用户中心-解绑微信
     * @param identityCard 身份证号码
     * @param openId  微信openId
     * @param unionId 支付宝unionId
     */
    @RequestMapping(value = "deleteVehicle",method = RequestMethod.POST)
    public void deleteVehicle( String identityCard,String userSource,String openId, String unionId,String sourceOfCertification) {
    	
    	String code=MsgCode.success;
 		StringBuffer sb = new StringBuffer("");
    	UserBind userBind = new UserBind();
    	
    	userBind.setOpenId(openId);
    	if(StringUtil.isBlank(identityCard)){
 			code=MsgCode.paramsError;
 			sb.append("身份证号码为空  ");
 		}else{
 			userBind.setIdCard(identityCard);
 		}
    	
    	if(StringUtil.isBlank(userSource)){
 			code=MsgCode.paramsError;
 			sb.append("用户来源为空  ");
 		}else{
 			if("C".equals(sourceOfCertification)){
 				userBind.setOpenId(openId);
 			}else if("Z".equals(sourceOfCertification)){
 				userBind.setUserId(openId);
 			}
 		}
    	 BaseBean basebean = new  BaseBean();
    	try {
    		int re = 0;
    		userBind.setClientType(sourceOfCertification);
            re = accountService.unbindVehicle(userBind);
            //都是返回解绑成功
        	basebean.setCode(MsgCode.success);
            basebean.setMsg("");
		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("deleteVehicle出错",e);
		}

    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));  
    }
    
    /**
     * 用户中心-添加车辆(个人和他人)
     * @param bindType 绑定类型 1-绑定个人车辆信息、0-绑定他人车辆
     * @param licensePlateType 车辆类型 蓝牌、黄牌、黑牌、个性牌、小型新能源车号牌、大型新能源车号牌
     * @param provinceAbbreviation 省简称 例如：粤
     * @param licensePlateNumber 车牌号码
     * @param frameNumber 车架号码
     * @param certifiedSource  A app C微信 Z支付宝  E邮政 W外网星火
     * @param ownerName 车主姓名
     * @param ownerIdCard 车主身份证号
     * @param userIdCard 登录用户身份证
     * @param idCardImgPositive 身份证正面
     * @param idCardImgHandHeld 手持身份证
     */
    @RequestMapping(value = "addVehicle")
    public void addVehicle( Integer bindType, String licensePlateType,String provinceAbbreviation,String licensePlateNumber,String frameNumber,String certifiedSource,
    		String ownerName, String ownerIdCard,String userIdCard, String idCardImgPositive,String idCardImgHandHeld,HttpServletRequest request) {
    	String code=MsgCode.success;
    	StringBuffer sb = new StringBuffer("");   	
    	BindCarVo bindCarVo = new BindCarVo();
    	BaseBean basebean = new  BaseBean();
    	if(null == bindType || bindType<0){
    		basebean.setMsg("bindType 绑定类型错误!");
    		basebean.setCode(MsgCode.paramsError);
    		renderJSON(basebean);
    		return;
    	}else{
    		bindCarVo.setBindType(bindType);
    		if(StringUtils.isBlank(userIdCard)){
    			basebean.setMsg("userIdCard 不能为空!");
        		basebean.setCode(MsgCode.paramsError);
        		renderJSON(basebean);
        		return;
    		}else{
    			bindCarVo.setUserIdCard(userIdCard);
    		}
    		if(StringUtils.isBlank(licensePlateType)){
    			basebean.setMsg("licensePlateType 不能为空!");
        		basebean.setCode(MsgCode.paramsError);
        		renderJSON(basebean);
        		return;
    		}else{
    			bindCarVo.setLicensePlateType(licensePlateType);
    		}
    		if(StringUtils.isBlank(provinceAbbreviation)){
    			basebean.setMsg("provinceAbbreviation 不能为空!");
        		basebean.setCode(MsgCode.paramsError);
        		renderJSON(basebean);
        		return;
    		}else{
    			bindCarVo.setProvinceAbbreviation(provinceAbbreviation);
    		}
    		if(StringUtils.isBlank(licensePlateNumber)){
    			basebean.setMsg("licensePlateNumber 不能为空!");
        		basebean.setCode(MsgCode.paramsError);
        		renderJSON(basebean);
        		return;
    		}else{
    			bindCarVo.setLicensePlateNumber(licensePlateNumber);
    		}
    		if(StringUtils.isBlank(frameNumber)){
    			basebean.setMsg("frameNumber 不能为空!");
        		basebean.setCode(MsgCode.paramsError);
        		renderJSON(basebean);
        		return;
    		}else{
    			bindCarVo.setFrameNumber(frameNumber);
    		}
    		if(StringUtils.isBlank(certifiedSource)){
    			basebean.setMsg("certifiedSource 不能为空!");
        		basebean.setCode(MsgCode.paramsError);
        		renderJSON(basebean);
        		return;
    		}else{
    			bindCarVo.setCertifiedSource(certifiedSource);
    		}
    		if(1 == bindType){
    			//1-绑定个人车辆信息
    		}
    		if(0 == bindType){
    			//0-绑定他人车辆
    			if(StringUtils.isBlank(ownerName)){
        			basebean.setMsg("ownerName 不能为空!");
            		basebean.setCode(MsgCode.paramsError);
            		renderJSON(basebean);
            		return;
        		}else{
        			bindCarVo.setOwnerName(ownerName);
        		}
    			if(StringUtils.isBlank(ownerIdCard)){
        			basebean.setMsg("ownerIdCard 不能为空!");
            		basebean.setCode(MsgCode.paramsError);
            		renderJSON(basebean);
            		return;
        		}else{
        			bindCarVo.setOwnerIdCard(ownerIdCard);
        		}
    			if(StringUtils.isBlank(idCardImgPositive)){
    				basebean.setMsg("idCardImgPositive 不能为空!");
            		basebean.setCode(MsgCode.paramsError);
            		renderJSON(basebean);
            		return;
    			}else{
    				bindCarVo.setIdCardImgPositive(idCardImgPositive);
    			}
    			if(StringUtils.isBlank(idCardImgHandHeld)){
    				basebean.setMsg("idCardImgHandHeld 不能为空!");
            		basebean.setCode(MsgCode.paramsError);
            		renderJSON(basebean);
            		return;
    			}else{
    				bindCarVo.setIdCardImgHandHeld(idCardImgHandHeld);
    			}
    		}
    		bindCarVo.setInputIP(getIp2(request));
    	}
    	try {
    		JSONObject json = accountService.addVehicle(bindCarVo);
			code =json.getString("CODE");
			if(!MsgCode.success.equals(code)){
				code=MsgCode.businessError;
			}
	    	basebean.setCode(code);
	    	basebean.setMsg(json.getString("MSG"));   
		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("addVehicle出错",e);
		}
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    
    }
    
    
    /**
     *我的资料-修改个人资料
     * @param tureName 姓名
     * @param identityCard 身份证号码
     * @param mailingAddress 通讯地址
     * @param idCardImgPositive 身份证正面
     * @param idCardImgNegative 身份证反面
     * @param IdCardImgHandHeld 手持身份证
     */
    @RequestMapping(value = "updateUser",method = RequestMethod.POST)
    public void updateUser( String tureName, String identityCard, String mailingAddress, String idCardImgPositive
    		, String idCardImgNegative,String idCardImgHandHeld) {
    	String code=MsgCode.success;
 		StringBuffer sb = new StringBuffer("");
    	UserBasicVo userBasicVo =new UserBasicVo();
    	if(StringUtil.isBlank(identityCard)){
 			code=MsgCode.paramsError;
 			sb.append("身份证为空  ");
 		}else{
 			userBasicVo.setIdentityCard(identityCard);;
 		}
    	if(StringUtil.isBlank(mailingAddress)){
 			code=MsgCode.paramsError;
 			sb.append("通讯地址为空  ");
 		}else{
 			userBasicVo.setMailingAddress(mailingAddress);
 		}
    	if(StringUtil.isBlank(mailingAddress)){
 			code=MsgCode.paramsError;
 			sb.append("通讯地址为空  ");
 		}else{
 			userBasicVo.setMailingAddress(mailingAddress);
 		}
    	if(StringUtil.isBlank(idCardImgPositive)){
 			code=MsgCode.paramsError;
 			sb.append("身份证正面为空  ");
 		}else{
 			userBasicVo.setIdCardImgPositive(idCardImgPositive);
 		}
 		
 		if(StringUtil.isBlank(idCardImgNegative)){
 			code=MsgCode.paramsError;
 			sb.append("身份证反面为空 ");
 		}
 		
 		if(StringUtil.isBlank(idCardImgHandHeld)){
 			code=MsgCode.paramsError;
 			sb.append("手持身份证为空  ");
 		}else{
 			userBasicVo.setIdCardImgHandHeld(idCardImgHandHeld);
 		}

    
    	BaseBean basebean = new  BaseBean();
    	try {  	
    		if(MsgCode.success.equals(code)){//参数校验通过
    			userBasicVo.setUserSource("C");
            	userBasicVo.setIdCardValidityDate("2018-04-15"); //身份证有效期
            	userBasicVo.setNickname(tureName);
    			JSONObject json = accountService.updateUser(userBasicVo);   
    			code =json.getString("CODE");
    			if(!MsgCode.success.equals(code)){
    				code=MsgCode.businessError;
    			}
    	    	basebean.setCode(code);
    	    	basebean.setMsg(json.getString("MSG"));
    		}else{
    			basebean.setCode(code);
    			basebean.setMsg(sb.toString());
    		}  		
		} catch (Exception e) {	
			DealException(basebean, e);
			logger.error("updateUser出错",e);
		}   	
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    
    }
    
    
    
    /**
     * 我的资料-修改手机号
     * @param oldMobile 旧手机号
     * @param validateCode 验证码
     * @param newMobile 新手机号
     */
    @RequestMapping(value = "updateMobile",method = RequestMethod.POST)
    public void updateMobile( String oldMobile, String validateCode
    		,String newMobile,String identityCard) {
    	
    	String code=MsgCode.success;
 		StringBuffer sb = new StringBuffer("");
    	UserBasicVo userBasicVo =new UserBasicVo();
    	if(StringUtil.isBlank(identityCard)){
 			code=MsgCode.paramsError;
 			sb.append("身份证为空  ");
 		}else{
 			userBasicVo.setIdentityCard(identityCard);;
 		}
    	
    	if(StringUtil.isBlank(oldMobile)){
 			code=MsgCode.paramsError;
 			sb.append("旧手机号为空  ");
 		}else{
 			userBasicVo.setOldMobile(oldMobile);
 		}
    	if(StringUtil.isBlank(newMobile)){
 			code=MsgCode.paramsError;
 			sb.append("新手机号为空  ");
 		}else{
 			userBasicVo.setNewMobile(newMobile);
 		}
    	if(StringUtil.isBlank(newMobile)){
 			code=MsgCode.paramsError;
 			sb.append("验证码为空  ");
 		}
    	
    	BaseBean basebean = new  BaseBean();
    	try {
    		if(MsgCode.success.equals(code)){//参数校验通过    			
    			// 0-验证成功，1-验证失败，2-验证码失效
        		int result = accountService.verificatioCode(oldMobile, validateCode);
        		if(0 == result){
        			userBasicVo.setUserSource("C");
        			JSONObject json = accountService.updateMobile(userBasicVo);
        			code =json.getString("CODE");
        			if(!MsgCode.success.equals(code)){
        				code=MsgCode.businessError;
        			}
        			basebean.setCode(code);
        	    	basebean.setMsg(json.getString("MSG"));
        		}
    			if(1 == result){
    				code=MsgCode.paramsError;	
    				sb.append("验证码错误    ");
    				basebean.setMsg(sb.toString());
    			 }
    			if(2 == result){
    				code=MsgCode.paramsError;	
    				sb.append("验证码失效    ");
    				basebean.setMsg(sb.toString());
    			}   	    	
    		}else{
    			basebean.setCode(code);
    			basebean.setMsg(sb.toString());
    		}
    		
		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("updateMobile出错",e);
		}  	
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    
    }
    
    
    
   /**
    * 我的资料-修改密码
    * @param oldPwd
    * @param newPwd
    */
    @RequestMapping(value = "updatePwd",method = RequestMethod.POST)
    public void updatePwd( String oldPwd, String newPwd,String identityCard) {
    	String code=MsgCode.success;
 		StringBuffer sb = new StringBuffer("");
    	
    	UserBasicVo userBasicVo =new UserBasicVo();
    	if(StringUtil.isBlank(identityCard)){
 			code=MsgCode.paramsError;
 			sb.append("身份证为空  ");
 		}else{
 			userBasicVo.setIdentityCard(identityCard);;
 		}
    	if(StringUtil.isBlank(oldPwd)){
 			code=MsgCode.paramsError;
 			sb.append("旧密码为空  ");
 		}else{
 			userBasicVo.setOldPwd(oldPwd);
 		}
    	if(StringUtil.isBlank(newPwd)){
 			code=MsgCode.paramsError;
 			sb.append("新密码为空  ");
 		}else{
 			userBasicVo.setNewPwd(newPwd);
 		}
    	
    	
    	
    	BaseBean basebean = new  BaseBean();
    	try {
    		if(MsgCode.success.equals(code)){//参数校验通过
    			userBasicVo.setUserSource("C");
    			JSONObject json = accountService.updatePwd(userBasicVo);
    			code =json.getString("CODE");
    			if(!MsgCode.success.equals(code)){
    				code=MsgCode.businessError;
    			}
    	    	basebean.setCode(code);
    	    	basebean.setMsg(json.getString("MSG"));
    		}else{
    			basebean.setCode(code);
    			basebean.setMsg(sb.toString());
    		}   		
		} catch (Exception e) {
			DealException(basebean, e);
			logger.error("updatePwd出错",e);
		}  	
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    
    }

    
    /**
     * 随手拍s
     * @Title: readilyShoot 
     * @author liuminkang
     * @Description: TODO(星级用户-随手拍) 
     * @param licensePlateNumber 车牌号
     * @param licensePlateType 车牌类型
     * @param illegalActivitieOne 情况说明
     * @param illegalTime 违法时间
     * @param illegalSections 违法地点
     * @param reportImgOne 举报图片1
     * @param reportImgTwo 举报图片2
     * @param reportImgThree 举报图片3
     * @param inputMan 举报人 (暂时无用 )
     * @param inputManName  举报人 姓名
     * @param inputManPhone  举报人手机号
     * @param identityCard 举报人身份证
     * @param userSource 认证来源(微信C，支付宝Z)
     * @param openId    openId(暂时无用)
     * @param wfxw1 违法行为1
     * 
     * @return void    返回类型 
     * @date 2017年4月20日 下午3:06:02
     */
    @RequestMapping(value = "readilyShoot",method = RequestMethod.POST)
    public void readilyShoot(String licensePlateNumber,String licensePlateType,String illegalActivitieOne, String illegalTime, String illegalSections, String reportImgOne, String reportImgTwo
    		, String reportImgThree, String inputMan,String inputManName,String inputManPhone,String identityCard,String userSource,String openId,String wfxw1) {
    	String code=MsgCode.success;
 		StringBuffer sb = new StringBuffer("");
 		int imgNumber=0;//传入的图片数量
    	ReadilyShootVo readilyShootVo = new ReadilyShootVo();
    	if(StringUtil.isBlank(licensePlateNumber)){
    		readilyShootVo.setLicensePlateNumber("");
    	}else{
   		readilyShootVo.setLicensePlateNumber(licensePlateNumber);
    	}

    	if(StringUtil.isBlank(licensePlateType)){
    		readilyShootVo.setLicensePlateType("");
    	}else{
    		readilyShootVo.setLicensePlateType(licensePlateType);	
    	}
    	if(StringUtil.isBlank(illegalActivitieOne)){
 			code=MsgCode.paramsError;
 			sb.append("违法行为为空  ");
 		}else{
 			readilyShootVo.setIllegalActivitieOne(illegalActivitieOne);
 		}
    	if(StringUtil.isBlank(identityCard)){
 			code=MsgCode.paramsError;
 			sb.append("身份证为空  ");
 		}else{
 			readilyShootVo.setUserIdCard(identityCard);
 		}	
    	if(StringUtil.isBlank(illegalTime)){
 			code=MsgCode.paramsError;
 			sb.append("违法时间为空  ");
 		}else{
 			readilyShootVo.setIllegalTime(illegalTime);
 		}
    	if(StringUtil.isBlank(illegalSections)){
 			code=MsgCode.paramsError;
 			sb.append("违法地点为空  ");
 		}else{
 			readilyShootVo.setIllegalSections(illegalSections);
 		}
    	
    	if(StringUtil.isBlank(inputManPhone)){
 			code=MsgCode.paramsError;
 			sb.append("手机号码为空  ");
 		}else{
 			readilyShootVo.setInputManPhone(inputManPhone);
 		}
    	
    	if(StringUtil.isBlank(inputMan)){
    		readilyShootVo.setInputMan("");
 		}else{
 			readilyShootVo.setInputMan(inputMan);
 		}
    	

 		

    	
    	if(StringUtil.isBlank(inputManName)){
 			code=MsgCode.paramsError;
 			sb.append("举报人姓名为空  ");
 		}else{
 			readilyShootVo.setInputManName(inputManName);
 		}
    	   	
    	if(StringUtil.isBlank(reportImgOne)){
    		imgNumber++;
 		}else{
 			readilyShootVo.setReportImgOne(reportImgOne);
 		}
    	
    	if(StringUtil.isBlank(reportImgTwo)){
    		imgNumber++;
 		}else{
 			readilyShootVo.setReportImgTwo(reportImgTwo);
 		}
    	
    	if(StringUtil.isBlank(reportImgThree)){
    		imgNumber++;
 		}else{
 			readilyShootVo.setReportImgThree(reportImgThree);
 		}
    	
    	if(imgNumber>1){
    		code=MsgCode.paramsError;
 			sb.append("举报图片数量不少于2张  ");
    	}
    	
    	if(StringUtil.isBlank(userSource)){
 			code=MsgCode.paramsError;
 			sb.append("用户来源为空  ");
 		}else{
 			readilyShootVo.setUserSource(userSource);
 		}
    	
    	if(StringUtil.isBlank(openId)){
 			code=MsgCode.paramsError;
 			sb.append("微信openid为空  ");
 		}else{
 			readilyShootVo.setOpenId(openId);
 		}

       	BaseBean basebean = new  BaseBean();
    	try {
    		 if(MsgCode.success.equals(code)){//参数校验通过
    			 JSONObject json = accountService.readilyShoot(readilyShootVo);
    				code =json.getString("code");
    				if(!MsgCode.success.equals(code)){
    					code=MsgCode.businessError;
    				}else{
    					Map<String, Object> modelMap = new HashMap<String, Object>();
    					String msg=json.getString("msg");
    			     	modelMap.put("recordNumber", msg.substring(5, 20));
    			     	modelMap.put("queryPassword", json.getString("cxyzm"));
    			     	basebean.setData(modelMap);
    				}
    		    	basebean.setCode(code);
    		    	basebean.setMsg(json.getString("msg"));
    		 }else{
    			 basebean.setCode(code);
    			 basebean.setMsg(sb.toString());
    		 }
			
    	
    	} catch (Exception e) {
    		DealException(basebean, e);
    		logger.error("readilyShoot出错",e);
		}
    
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    
    }
    
    /**
     * 
     * @Title: getPositioningAddress 
     * @author liuminkang
     * @Description: TODO(根据关键字获取违法地点) 
     * @param keyword
     * @throws Exception    设定文件 
     * @return void    返回类型 
     * @date 2017年4月28日 上午10:36:45
     */
    @RequestMapping(value = "getPositioningAddress",method = RequestMethod.POST)
    public void getPositioningAddress(String keyword) throws Exception {
		JSONObject json= null;
		
		BaseBean basebean = new  BaseBean();
		try {
			 json  = accountService.getPositioningAddress(keyword);
			 String code =json.getString("code");
			 if(MsgCode.success.equals(code)){
				 basebean.setCode(code);
				 basebean.setMsg(json.getString("msg"));
				 basebean.setData(json.get("body"));
			 }
		} catch (Exception e) {
			logger.error("getPositioningAddress出错，错误="+ keyword,e);
		}
		renderJSON(basebean);
		logger.debug(JSON.toJSONString(basebean));

	}
    
    /**
     * 违法行为的选择项目
     * @param keyword 关键字   机动车
     * @throws Exception
     */
    @RequestMapping(value = "getTheChoiceOfIllegalActivities")
    public void getTheChoiceOfIllegalActivities(String keyword) throws Exception {
		JSONObject json= null;
		
		BaseBean basebean = new  BaseBean();
		try {
			 json  = accountService.getTheChoiceOfIllegalActivities(keyword);
			 String code =json.getString("code");
			 if(MsgCode.success.equals(code)){
				 basebean.setCode(code);
				 basebean.setMsg(json.getString("msg"));
				 basebean.setData(json.get("body"));
			 }
		} catch (Exception e) {
			logger.error("getPositioningAddress出错，错误="+ keyword,e);
		}
		renderJSON(basebean);
		logger.debug(JSON.toJSONString(basebean));
	}
    /**
     * 电子保单查询
     * @param identityCard 身份证号
     * @param mobileNumber 手机号
     * @param licensePlateNumber 车牌号码
     * @param licensePlateType 车辆类型
     * @param sourceOfCertification 认证来源
     */
    @RequestMapping(value = "getElectronicPolicy")
    public void getElectronicPolicy(String identityCard,String mobileNumber,String licensePlateNumber,String licensePlateType,String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();
		try {
			if(StringUtils.isBlank(identityCard)){
        		baseBean.setMsg("identityCard 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(mobileNumber)){
        		baseBean.setMsg("mobileNumber 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(licensePlateNumber)){
        		baseBean.setMsg("licensePlateNumber 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(licensePlateType)){
        		baseBean.setMsg("licensePlateType 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
			List<ElectronicPolicyBean> electronicPolicyBeans = new ArrayList<ElectronicPolicyBean>();
			Map<String, Object> map = accountService.getElectronicPolicy(identityCard, mobileNumber, licensePlateNumber, licensePlateType, sourceOfCertification);
			String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if("0000".equals(code)){
				electronicPolicyBeans = (List<ElectronicPolicyBean>) map.get("data");
				baseBean.setData(electronicPolicyBeans);
				baseBean.setCode(MsgCode.success);
		    	baseBean.setMsg("");
			}else{
				baseBean.setData("");
				baseBean.setCode(MsgCode.businessError);
		    	baseBean.setMsg(msg);
			}
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("getElectronicPolicy 错误!", e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
}
