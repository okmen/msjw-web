package cn.web.front.action.account;

import java.beans.beancontext.BeanContextServiceProviderBeanInfo;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.Documentation;
import cn.account.bean.ElectronicPolicyBean;
import cn.account.bean.IssuingLicenceAuthority;
import cn.account.bean.ReadilyShoot;
import cn.account.bean.UserBind;
import cn.account.bean.UserBindAlipay;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.BindCompanyCarVo;
import cn.account.bean.vo.BindDriverLicenseVo;
import cn.account.bean.vo.BrushFaceVo;
import cn.account.bean.vo.CompanyRegisterVo;
import cn.account.bean.vo.IdentificationOfAuditResultsVo;
import cn.account.bean.vo.InformationCollectionVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.bean.vo.ReauthenticationVo;
import cn.account.bean.vo.TrafficQueryVo;
import cn.account.bean.vo.UnbindTheOtherDriverUseMyCarVo;
import cn.account.bean.vo.UnbindVehicleVo;
import cn.account.bean.vo.UserBasicVo;
import cn.account.service.IAccountService;
import cn.file.service.IFileService;
import cn.handle.bean.vo.HandleTemplateVo;
import cn.handle.service.IHandleService;
import cn.illegal.service.IIllegalService;
import cn.message.model.wechat.MessageChannelModel;
import cn.message.model.wechat.TemplateDataModel;
import cn.message.model.wechat.MessageChannelModel.Property;
import cn.message.service.IMobileMessageService;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.bean.BusinessType;
import cn.sdk.bean.StVo;
import cn.sdk.exception.ResultCode;
import cn.sdk.msg.MsgTemplate;
import cn.sdk.thread.BilinThreadPool;
import cn.sdk.util.DateUtil;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
import cn.sdk.util.SXStringUtils;
import cn.sdk.util.StringUtil;
import cn.web.front.action.account.task.AccountTask;
import cn.web.front.action.account.task.AccountTaskExecute;
import cn.web.front.support.BaseAction;
import cn.file.bean.vo.ProblemFeedbackVo;


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
    @Qualifier("illegalService")
    private IIllegalService illegalService;
    
    @Autowired
    @Qualifier("mobileMessageService")
    private IMobileMessageService mobileMessageService;
    
    @Autowired
    @Qualifier("fileService")
    private IFileService fileService;
    
    @Autowired
	@Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;
    
    @Autowired
	@Qualifier("bilinThreadPool")
	private BilinThreadPool bilinThreadPool;
    
    @Autowired
    @Qualifier("accountTaskExecute")
    private AccountTaskExecute accountTaskExecute;
    
    @Autowired
    @Qualifier("handleService")
    private IHandleService handleService;

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
    
    @RequestMapping("gziImgOld")
    public void gziImgOld() throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
    		fileService.gziImgOld();
    		baseBean.setCode(MsgCode.success);
        	baseBean.setMsg("历史图片压缩成功");
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("gziImgOld 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
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
        	if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if (StringUtil.isBlank(sourceOfCertification)) {
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
    		}else if (!"A".equals(sourceOfCertification)) {
    			if(StringUtils.isBlank(openId)){
            		baseBean.setMsg("openId 不能为空!");
            		baseBean.setCode(MsgCode.paramsError);
            		renderJSON(baseBean);
            		return;
            	}
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
     * 支付宝生活号 登录接口
     * http://192.168.1.161/web/user/alipayLogin.html?loginName=18603017278&openId=000000xxx&sourceOfCertification=Z
     * @param sourceOfCertification 认证来源 支付宝Z
     * @param loginName 手机号/身份证
     * @param openId 支付宝id
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="alipayLogin")
    public void alipayLogin(String sourceOfCertification,String loginName,String openId,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(loginName)){
        		baseBean.setMsg("loginName 不能为空!");
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
        	LoginReturnBeanVo loginReturnBeanVo = accountService.alipayLogin(loginName,sourceOfCertification,openId);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg("登录用户不存在");
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
     * 免密登录
     * http://192.168.1.163/web/user/getLoginInfoByLoginName.html?loginName=18603017278&sourceOfCertification=Z
     * @param sourceOfCertification 认证来源 支付宝Z
     * @param loginName 手机号/身份证
     * @param openId 支付宝id
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="getLoginInfoByLoginName")
    public void getLoginInfoByLoginName(String sourceOfCertification,String loginName,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(loginName)){
        		baseBean.setMsg("loginName 不能为空!");
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
        	LoginReturnBeanVo loginReturnBeanVo = accountService.getLoginInfoByLoginName(loginName, sourceOfCertification);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg("登录用户不存在");
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
     * @param businessType 业务类型(交警公众号-szjj、东部预约-easternReservation)
     * @param request
     * @param response
     * http://localhost:8080/web/user/sendSMSVerificatioCode.html?mobilephone=18601174358&businessType=szjj
     * @throws Exception 
     */
    @RequestMapping("sendSMSVerificatioCode")
    public void sendSMSVerificatioCode(String mobilephone,String businessType,HttpServletRequest request,HttpServletResponse response) throws Exception{
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
    		String msgContent = MsgTemplate.getSzjjSendMsgTemplate(valideteCode,businessType);
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
    		String ownerName, String ownerIdCard,String userIdCard, String idCardImgPositive,String idCardImgHandHeld,String businessType ,HttpServletRequest request) {
    	String code=MsgCode.success;
    	StringBuffer sb = new StringBuffer("");   	
    	BindCarVo bindCarVo = new BindCarVo();
    	BaseBean basebean = new  BaseBean();
    	String openId = request.getParameter("openId");
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
			basebean.setCode(code);
			if ("0000".equals(code)) {
				if (StringUtil.isNotBlank(businessType)&&"C".equals(certifiedSource)) {
					JSONObject body = json.getJSONObject("BODY");
					if (null != body) {
						String waterNumber = body.getString("CID");
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, "addVehicle", waterNumber, DateUtil2.date2str(new Date()));
						basebean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						if ("1".equals(businessType)) {
	 						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbn3H9wpHz8dKjXPL9J_xC5s");
	 					}else if ("2".equals(businessType)) {
	 						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbjAEGcUfJBYRRfOgme0SPuk");
	 					}
						model.setResult_page_style_id("23ClyLHM5Fr790uz7t-fxiodPnL9ohRzcnlGWEudkL8");
						model.setDeal_msg_style_id("23ClyLHM5Fr790uz7t-fxlzJePTelFGvOKtKR4udm1o");
						model.setCard_style_id("");
						model.setOrder_no(waterNumber);
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> tmap = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						tmap.put("first", new MessageChannelModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
						tmap.put("keyword1",
								new MessageChannelModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new MessageChannelModel().new Property("添加车辆", "#212121"));
						tmap.put("keyword3", new MessageChannelModel().new Property("待初审", "#212121"));
						tmap.put("remark", new MessageChannelModel().new Property("","#212121"));
						model.setData(tmap);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							basebean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							basebean.setMsg(url);
						}
					}else{
						HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, "addVehicle",  DateUtil2.date2str(new Date()));
						basebean.setData(handleTemplateVo);
						String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
						logger.info("返回的url是：" + url);
						logger.info("handleTemplateVo 是：" + handleTemplateVo);
						MessageChannelModel model = new MessageChannelModel();
						model.setOpenid(openId);
						if ("1".equals(businessType)) {
	 						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbn3H9wpHz8dKjXPL9J_xC5s");
	 					}else if ("2".equals(businessType)) {
	 						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbjAEGcUfJBYRRfOgme0SPuk");
	 					}
						model.setResult_page_style_id("23ClyLHM5Fr790uz7t-fxiodPnL9ohRzcnlGWEudkL8");
						model.setDeal_msg_style_id("23ClyLHM5Fr790uz7t-fxlzJePTelFGvOKtKR4udm1o");
						model.setCard_style_id("");
						model.setOrder_no("");
						model.setUrl(url);
						Map<String, cn.message.model.wechat.MessageChannelModel.Property> tmap = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
						tmap.put("first", new MessageChannelModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
						tmap.put("keyword1",
								new MessageChannelModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
						tmap.put("keyword2", new MessageChannelModel().new Property("添加车辆", "#212121"));
						tmap.put("keyword3", new MessageChannelModel().new Property(json.getString("MSG"), "#212121"));
						tmap.put("remark", new MessageChannelModel().new Property("更多信息请点击详情查看","#212121"));
						model.setData(tmap);
						BaseBean msgBean = templateMessageService.sendServiceMessage(model);
						logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
						
						//发送成功
						if("0".equals(msgBean.getCode())){
							basebean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
						}else{
							basebean.setMsg(url);
						}
					}
				}else{
					basebean.setCode("0000");
					basebean.setMsg(json.getString("MSG"));
				}
				
			}
			if(!MsgCode.success.equals(code)){
				code=MsgCode.businessError;
				basebean.setMsg(json.getString("MSG"));
			}
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
     * http://192.168.1.245:8080/web/user/updateMobile.html?oldMobile=13652311206&validateCode=478467&newMobile=18601174358&identityCard=420881198302280017
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
    			// 0-验证成功，1-验证失败，2-验证码失效     以前是给老手机号码发送短信，业务变成给新手机号发短信  2017年6月29日又还原了
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
     * 随手拍
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
     * @param shsm 是否实名 0  -非实名  1-实名认证过
     * @return void    返回类型 
     * @date 2017年4月20日 下午3:06:02
     */
    @RequestMapping(value = "readilyShoot")
    public void readilyShoot(String licensePlateNumber,String licensePlateType,String illegalActivitieOne, String illegalTime, String illegalSections, 
    		String reportImgOne, String reportImgTwo,String reportImgThree,String reportImgOneT1,String reportImgOneT2,String reportImgOneT3,
    		String inputMan,String inputManName,String inputManPhone,String identityCard,String userSource,String openId,String wfxw1,
    		String sourceOfCertification,String shsm) {
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
 			
 			if (!"A".equals(userSource)) {
 				if (StringUtil.isBlank(openId)) {
 					code=MsgCode.paramsError;
 					sb.append("openId为空  ");
 				} else {
 					readilyShootVo.setOpenId(openId);
 				}
 			}
 			
 			if("Z".equals(userSource)){
 				if(StringUtil.isBlank(shsm)){
 					code=MsgCode.paramsError;
 		 			sb.append("shsm 不能为空");
 				}
 			}
 		}
    	if(StringUtil.isBlank(sourceOfCertification)){
 			code=MsgCode.paramsError;
 			sb.append("sourceOfCertification为空  ");
 		}else{
 			readilyShootVo.setUserSource(userSource);
 		}
    	
 		
       	BaseBean basebean = new  BaseBean();
       	ReadilyShoot readilyShoot = new ReadilyShoot();
    	try {
    		 if(MsgCode.success.equals(code)){//参数校验通过
    			 JSONObject json = accountService.readilyShoot(readilyShootVo);
    				code =json.getString("code");
    				String msg=json.getString("msg");
    				if(!MsgCode.success.equals(code)){
    					code=MsgCode.businessError;
    				}else{
    					String reportSerialNumber = SXStringUtils.deleteChineseCharactertoString(msg);
    					reportSerialNumber = reportSerialNumber.replace(":", "");
    					//String reportSerialNumber = msg.substring(5, 20);
						String password = json.getString("cxyzm");
    					Map<String, Object> modelMap = new HashMap<String, Object>();
    			     	modelMap.put("recordNumber", reportSerialNumber);
    			     	modelMap.put("queryPassword", password);
    			     	basebean.setData(modelMap);
    			     	
    			     	List<StVo> base64Imgs = new ArrayList<StVo>();
    			     	if(StringUtils.isNotBlank(reportImgOne)){
    			     		StVo stVo1 = new StVo(reportImgOne, reportImgOneT1);
    			     		base64Imgs.add(stVo1);
    			     	}
    			     	if(StringUtils.isNotBlank(reportImgTwo)){
    			     		StVo stVo2 = new StVo(reportImgTwo,reportImgOneT2);
    			     		base64Imgs.add(stVo2);
    			     	}
    			     	if(StringUtils.isNotBlank(reportImgThree)){
    			     		StVo stVo3 = new StVo(reportImgThree,reportImgOneT3);
    			     		base64Imgs.add(stVo3);
    			     	}
    			     	List<String> imgs = new ArrayList<String>();
    			     	try {
    			     		imgs = fileService.writeImgReadilyShoot(reportSerialNumber, base64Imgs);
						} catch (Exception e) {
							logger.error("写图片到225服务器  失败", e);
						}
    			     	
    			     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    			        Date date = sdf.parse(illegalTime);
    			     	readilyShoot.setAddDate(new Date());
    					readilyShoot.setIllegalTime(date);
    					readilyShoot.setIllegalSections(illegalSections);
    					readilyShoot.setReportSerialNumber(reportSerialNumber);
    					readilyShoot.setPassword(password);
    					readilyShoot.setSituationStatement(illegalActivitieOne);
    					
    					if(null != imgs && imgs.size() > 0){
    						for(int i = 0; i< imgs.size(); i++){
    				    		String img = imgs.get(i);
    				    		if(0 == i){
    				    			readilyShoot.setIllegalImg1(img);
    				    		}
    				    		if(1 == i){
    				    			readilyShoot.setIllegalImg2(img);
    				    		}
    				    		if(2 == i){
    				    			readilyShoot.setIllegalImg3(img);
    				    		}
    				    	}
    					}
    					int count = accountService.saveReadilyShoot(readilyShoot);
    					logger.info("saveReadilyShoot返回值：" + count);
    					if(1 == count && sourceOfCertification.equals("C")){
    						 //举报成功发送模板消息
        					try {
        						String templateId = "pFy7gcEYSklRmg32165BUBwM3PFbUbBSLe0IPw3ZuY4";
            					String url = "http://gzh.stc.gov.cn/h5/#/takePicturesSuccess1?reportSerialNumber=" + reportSerialNumber + "&password=" + password;
        						Map<String, cn.message.model.wechat.TemplateDataModel.Property> map = new HashMap<String, cn.message.model.wechat.TemplateDataModel.Property>();
        						map.put("first", new TemplateDataModel().new Property("随手拍举报通知","#212121"));
        						map.put("keyword1", new TemplateDataModel().new Property(reportSerialNumber,"#212121"));
        						map.put("keyword2", new TemplateDataModel().new Property(password,"#212121"));
        						map.put("remark", new TemplateDataModel().new Property("举报状态：已记录\r\n您已完成本次举报流程，可通过深圳交警微信公众平台【交警互动】板块《举报信息查询》栏目输入您的记录号与查询密码进行查询，感谢您使用深圳交警微信公众平台。", "#212121"));
        						boolean flag = templateMessageService.sendMessage(openId, templateId, url, map);
        						logger.info("发送模板消息结果：" + flag);
    						} catch (Exception e) {
    							logger.error("发送模板消息  失败===", e);
    						}
    					}
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
    	try {
    	 if (bilinThreadPool != null) {
			bilinThreadPool.execute(new AccountTask(accountTaskExecute, readilyShoot, readilyShootVo));
    	 }
		} catch (Exception e) {
			logger.error("随手拍发送数据给php系统 错误", e);
		}
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    
    }
    
    
    public static void main(String[] args) {
		
    	List<String> aa = new ArrayList<String>();
    	for(int i=0;i<aa.size();i++){
    		String x1 = aa.get(i);
        	System.out.println(x1);
    	}
    	
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
    
    /**
     * 根据id范围取openId数据
     * @param startId
     * @param endId
     */
    @RequestMapping(value = "getBetweenAndId")
	public void getBetweenAndId(String startId, String endId) {
		BaseBean baseBean = new BaseBean();
		if(StringUtils.isBlank(startId)){
    		baseBean.setMsg("startId 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		if(StringUtils.isBlank(endId)){
    		baseBean.setMsg("endId 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		List<UserBind> userBinds = null;
		try {
			userBinds =	accountService.getBetweenAndId(startId, endId);
			baseBean.setData(userBinds);
			baseBean.setCode(MsgCode.success);
	    	baseBean.setMsg("");
	    	renderJSON(baseBean);
		} catch (Exception e) {
			logger.error("getBetweenAndId 错误", e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}

    /**
     * 根据时间范围取openId数据
     * @param startDate
     * @param endDate
     */
    @RequestMapping(value = "getBetweenAndBindDate")
   	public void getBetweenAndBindDate(String startDate, String endDate) {
   		BaseBean baseBean = new BaseBean();
   		if(StringUtils.isBlank(startDate)){
       		baseBean.setMsg("startDate 不能为空!");
       		baseBean.setCode(MsgCode.paramsError);
       		renderJSON(baseBean);
       		return;
       	}
   		if(StringUtils.isBlank(endDate)){
       		baseBean.setMsg("endDate 不能为空!");
       		baseBean.setCode(MsgCode.paramsError);
       		renderJSON(baseBean);
       		return;
       	}
   		List<UserBind> userBinds = null;
   		try {
   			userBinds =	accountService.getBetweenAndBindDate(startDate, endDate);
   			baseBean.setData(userBinds);
   			baseBean.setCode(MsgCode.success);
   	    	baseBean.setMsg("");
   	    	renderJSON(baseBean);
   		} catch (Exception e) {
   			logger.error("getBetweenAndBindDate 错误", e);
   			DealException(baseBean, e);
   		}
   		renderJSON(baseBean);
   		logger.debug(JSON.toJSONString(baseBean));
   	}
    

    
    /**
     * 
     * @Title: getAllResourcesAbsoluteUrl 
     * @author jiangjiayi
     * @Description: TODO(加载所有资源绝对路径url) 
     * @param 无
     * @throws Exception    
     * @return void    返回类型 
     */
   /* @RequestMapping(value = "getAllResourcesAbsoluteUrl")
    public void getAllResourcesAbsoluteUrl(){
    	BaseBean baseBean = new BaseBean();
    	
    	
    	
    	
    	List list = null;
    	try {
			list = convenienceService.getAllResourcesAbsoluteUrl();
			baseBean.setCode(MsgCode.success);
			baseBean.setMsg("");
			baseBean.setData(list);
		} catch (Exception e) {
			logger.error("getAllResourcesAbsoluteUrl 错误", e);
   			DealException(baseBean, e);
		}
    	renderJSON(baseBean);
   		logger.debug(JSON.toJSONString(baseBean));
    }*/
    
    /**
     * 驾驶证绑定
     * @Title: bindDriverLicense 
     * @author wufan
     * @Description: TODO(星级用户-驾驶证绑定) 
     * @param loginName 登录账户
     * @param intype
     * @param userSource 用户来源（A app C微信 Z支付宝  E邮政 W外网星火）
     * @param identityCard 我的身份证
     * @param driverLicenseIssuedAddress 驾驶证核发地（1-本地、2-本省外市、3-外省）
     * @param name 姓名
     * @param sourceOfCertification 认证来源(A app C微信 Z支付宝  E邮政 W外网星火)
     * @param http://localhost/web/user/bindDriverLicense.html?loginName=440301199002101119&intype=0&userSource=C&identityCard=440301199002101119&driverLicenseIssuedAddress=1&name=杨明畅&sourceOfCertification=C
     * @return void    返回类型 
     */
    @RequestMapping(value = "bindDriverLicense")
    public void bindDriverLicense(String loginName,String intype,String userSource,String identityCard,String driverLicenseIssuedAddress,String name,String sourceOfCertification) {
    	String code=MsgCode.success;
 		StringBuffer sb = new StringBuffer("");
    	BindDriverLicenseVo bindDriverLicenseVo = new BindDriverLicenseVo();
    	if(StringUtil.isBlank(loginName)){
    		bindDriverLicenseVo.setLoginName("");
    	}else{
    		bindDriverLicenseVo.setLoginName(loginName);
    	}
    	if (StringUtil.isBlank(userSource)) {
			bindDriverLicenseVo.setUserSource("");
		}else{
			bindDriverLicenseVo.setUserSource(userSource);
		}
    	if (StringUtil.isBlank(identityCard)) {
			code = MsgCode.paramsError;
			sb.append("身份证为空");
		}else{
			bindDriverLicenseVo.setIdentityCard(identityCard);
		}
    	if (StringUtil.isBlank(driverLicenseIssuedAddress)) {
			code = MsgCode.paramsError;
			sb.append("驾驶证核发地为空");
		}else{
			bindDriverLicenseVo.setDriverLicenseIssuedAddress(driverLicenseIssuedAddress);
		}
    	if (StringUtil.isBlank(name)) {
			code = MsgCode.paramsError;
			sb.append("姓名为空");
		}else{
			bindDriverLicenseVo.setName(name);
		}
    	if (StringUtil.isBlank(sourceOfCertification)) {
			bindDriverLicenseVo.setSourceOfCertification("");
		}else{
			bindDriverLicenseVo.setSourceOfCertification(sourceOfCertification);
		}
       	BaseBean basebean = new  BaseBean();
    	try {
    		 if(MsgCode.success.equals(code)){//参数校验通过
    			 	JSONObject json = accountService.bindDriverLicense(bindDriverLicenseVo);
    			 	logger.info("绑返回数据是：" + json);
    				
    			 	code =json.getString("CODE");
    				if(!MsgCode.success.equals(code)){
    					code=MsgCode.businessError;
    				}else{
    					//绑定成功处理
    					Map<String, Object> modelMap = new HashMap<String, Object>();
    					String msg=json.getString("MSG");
    					JSONObject  jsonObject = json.getJSONObject("BODY");
    					String cId = jsonObject.getString("CID");
    			     	modelMap.put("recordNumber", cId);
    			     	basebean.setData(modelMap);
    			     	basebean.setCode(code);
    				}
    		    	basebean.setCode(code);
    		    	basebean.setMsg(json.getString("MSG"));
    		 }else{
    			 basebean.setCode(code);
    			 basebean.setMsg(sb.toString());
    		 }
    	} catch (Exception e) {
    		DealException(basebean, e);
    		logger.error("bindDriverLicense出错",e);
		}
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    }
    
    /**
	 * 提交无车证明申请
	 * @Description: TODO(提交无车证明申请) 
	 * @param applyType 申请类型
	 * @param applyName 姓名
	 * @param identityCard 身份证号
	 * @param applyPhone 联系电话
	 * @param sourceOfCertification 来源方式
	 * @return
	 */
    @RequestMapping("addNoneCarCertification")
    public void addNoneCarCertification(String applyType, String applyName, String identityCard, String applyPhone, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
		
		try {
			if(StringUtil.isBlank(applyType) || !"3".equals(applyType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请类型输入有误!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(applyName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(applyPhone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("联系电话不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源方式不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			baseBean = accountService.addNoneCarCertification(applyType, applyName, identityCard, applyPhone, sourceOfCertification);
		} catch (Exception e) {
			logger.error("提交无车证明申请Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    
    /**
     * 提交驾驶人安全事故信用表申请
     * @Description: TODO(提交驾驶人安全事故信用表申请)
     * @param applyType 申请类型
	 * @param applyName 姓名
	 * @param identityCard 身份证号
	 * @param applyPhone 联系电话
	 * @param sourceOfCertification 来源方式
     */
    @RequestMapping("addSafeAccidentCredit")
    public void addSafeAccidentCredit(String applyType, String applyName, String identityCard, String applyPhone, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
		
		try {
			if(StringUtil.isBlank(applyType) || !"4".equals(applyType)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请类型输入有误!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(applyName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(applyPhone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("联系电话不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源方式不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			baseBean = accountService.addSafeAccidentCredit(applyType, applyName, identityCard, applyPhone, sourceOfCertification);
		} catch (Exception e) {
			logger.error("提交驾驶人安全事故信用表申请Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
	/**
	 * 车辆解绑
	 * 
	 * @param loginUser 登录账户
	 * @param licensePlateNumber 车牌号码
	 * @param licensePlateType 车牌种类
	 * @param identificationNO 身份证明名称
	 * @param IDcard 身份证明号码
	 * @param sourceOfCertification 认证来源
	 * @param http://192.168.1.245:8080/web/user/unbindVehicle.html?loginUser=440301199002101119&licensePlateNumber=粤B701NR&licensePlateType=02&identificationNO=A&IDcard=440301199002101119&sourceOfCertification=C
	 * 
	 * 
	 */
	@RequestMapping("unbindVehicle")
	public void unbindVehicle(String loginUser, String licensePlateNumber, String licensePlateType,String identificationNO,String IDcard,
			String sourceOfCertification) {

		BaseBean baseBean = new BaseBean(); // 创建返回结果
		UnbindVehicleVo unbindVehicleVo = new UnbindVehicleVo();
		String jblx = "1";
		unbindVehicleVo.setJblx(jblx);
		try {
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindVehicleVo.setLoginUser(loginUser);
			}

			if (StringUtil.isBlank(licensePlateNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindVehicleVo.setLicensePlateNumber(licensePlateNumber);
			}

			if (StringUtil.isBlank(licensePlateType)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌种类不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindVehicleVo.setLicensePlateType(licensePlateType);
			}
			if (StringUtil.isBlank(identificationNO)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindVehicleVo.setIdentificationNO(identificationNO);
			}
			if (StringUtil.isBlank(IDcard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindVehicleVo.setIDcard(IDcard);
			}


			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindVehicleVo.setSourceOfCertification(sourceOfCertification);
			}

			Map<String, String> map = accountService.unbindVehicle(unbindVehicleVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if("0000".equals(code)){
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		if ("9999".equals(code)) {
        			baseBean.setMsg("输入信息格式有误！");
				}else{
					baseBean.setMsg(msg);
				}
        	}

		} catch (Exception e) {
			logger.error("车辆解绑异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	
	
	
	/**
	 * 车主解绑车辆其他驾驶人
	 * 
	 * @param loginUser 登录账户
	 * @param numberPlateNumber 号牌号码
	 * @param plateType 号牌种类
	 * @param IDcard 其他使用人身份证号
	 * @param userSource 认证来源
	 * 
	 * 
	 * @param sourceOfCertification 申请途径
	 * @param http://192.168.1.245:8080/web/user/unbindTheOtherDriverUseMyCar.html?loginUser=440301199002101119&numberPlateNumber=粤B701NR&plateType=02&IDcard=440301199002101119&userSource=C&sourceOfCertification=C
	 * 
	 * 
	 */
	@RequestMapping("unbindTheOtherDriverUseMyCar")
	public void unbindTheOtherDriverUseMyCar(String loginUser, String numberPlateNumber, String plateType,String IDcard,String userSource ,
			String sourceOfCertification) {

		BaseBean baseBean = new BaseBean(); // 创建返回结果 
		UnbindTheOtherDriverUseMyCarVo unbindTheOtherDriverUseMyCarVo = new UnbindTheOtherDriverUseMyCarVo();
		try {
			if (StringUtil.isBlank(loginUser)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindTheOtherDriverUseMyCarVo.setLoginUser(loginUser);
			}

			if (StringUtil.isBlank(numberPlateNumber)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindTheOtherDriverUseMyCarVo.setNumberPlateNumber(numberPlateNumber);
			}

			if (StringUtil.isBlank(plateType)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌种类不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindTheOtherDriverUseMyCarVo.setPlateType(plateType);
			}
			if (StringUtil.isBlank(IDcard)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号码不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindTheOtherDriverUseMyCarVo.setIDcard(IDcard);
			}
			if (StringUtil.isBlank(sourceOfCertification)) {
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			} else {
				unbindTheOtherDriverUseMyCarVo.setSourceOfCertification(sourceOfCertification);
			}

			Map<String, String> map = accountService.unbindTheOtherDriverUseMyCar(unbindTheOtherDriverUseMyCarVo);
			String code = map.get("code");
			String msg = map.get("msg");
			if("0000".equals(code)){
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		if ("9999".equals(code)) {
        			baseBean.setMsg("输入信息格式有误！");
				}else{
					baseBean.setMsg(msg);
				}
        	}

		} catch (Exception e) {
			logger.error("车主解绑车辆其他驾驶人异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
    
    
    
    /**                                                                                
     * 发证机关参数转换
     */
    @RequestMapping("getIssuingLicenceAuthorityArray")
    public void getIssuingLicenceAuthorityArray(HttpServletRequest request,HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		Map<String, String> map = new LinkedHashMap<>();
		JSONArray jsonArray = new JSONArray();
    	try{
			map.put("藏A:拉萨市公安局交通警察支队车辆管理所", "藏A");
			map.put("藏B:昌都公安局交通警察支队车辆管理所", "藏B");
			map.put("藏C:山南公安局交通警察支队车辆管理所", "藏C");
			map.put("藏D:日喀则公安局交通警察支队车辆管理所", "藏D");
			map.put("藏E:那曲公安局交通警察支队车辆管理所", "藏E");
			map.put("藏F:阿里公安局交通警察支队车辆管理所", "藏F");
			map.put("藏G:林芝公安局交通警察支队车辆管理所", "藏G");
			map.put("藏H:西藏自治区公安厅交通警察总队(驻双流车辆管理所)", "藏H");
			map.put("藏O:西藏公安厅交通警察总队车辆管理所", "藏O");
			map.put("川A:成都市公安局交通警察支队车辆管理所", "川A");
			map.put("川B:绵阳市公安局交通警察支队车辆管理所", "川B");
			map.put("川C:自贡市公安局交通警察支队车辆管理所", "川C");
			map.put("川D:攀枝花市公安局交通警察支队车辆管理所", "川D");
			map.put("川E:泸州市公安局车交通警察支队辆管理所", "川E");
			map.put("川F:德阳市公安局交通警察支队车辆管理所", "川F");
			map.put("川H:广元市公安局交通警察支队车辆管理所", "川H");
			map.put("川J:遂宁市公安局交通警察支队车辆管理所", "川J");
			map.put("川K:内江市公安局交通警察支队车辆管理所", "川K");
			map.put("川L:乐山市公安局交通警察支队车辆管理所", "川L");
			map.put("川M:资阳地区公安局交通警察支队车辆管理所", "川M");
			map.put("川O:四川省公安厅交通警察总队车辆管理所", "川O");
			map.put("川Q:宜宾市公安局交通警察支队车辆管理所", "川Q");
			map.put("川R:南充市公安局交通警察支队车辆管理所", "川R");
			map.put("川S:达州市公安局交通警察支队车辆管理所", "川S");
			map.put("川T:雅安地区公安局交通警察支队车辆管理所", "川T");
			map.put("川U:阿坝州公安局交通警察支队车辆管理所", "川U");
			map.put("川V:甘孜州公安局交通警察支队车辆管理所", "川V");
			map.put("川W:凉山州公安局交通警察支队车辆管理所", "川W");
			map.put("川X:广安市公安局交通警察支队车辆管理所", "川X");
			map.put("川Y:巴中地区公安局交通警察支队车辆管理所", "川Y");
			map.put("川Z:眉山地区公安局交通警察支队车辆管理所", "川Z");
			map.put("鄂A:武汉市公安局交通警察支队车辆管理所", "鄂A");
			map.put("鄂B:黄石市公安局交通警察支队车辆管理所", "鄂B");
			map.put("鄂C:十堰市公安局交通警察支队车辆管理所", "鄂C");
			map.put("鄂D:荆州市公安局交通警察支队车辆管理所", "鄂D");
			map.put("鄂E:宜昌市公安局交通警察支队车辆管理所", "鄂E");
			map.put("鄂F:襄樊市公安局交通警察支队车辆管理所", "鄂F");
			map.put("鄂G:鄂州市公安局交通警察支队车辆管理所", "鄂G");
			map.put("鄂H:荆门市公安局交通警察支队车辆管理所", "鄂H");
			map.put("鄂J:黄冈市公安局交通警察支队车辆管理所", "鄂J");
			map.put("鄂K:孝感市公安局交通警察支队车辆管理所", "鄂K");
			map.put("鄂L:咸宁市公安局交通警察支队车辆管理所", "鄂L");
			map.put("鄂M:仙桃市公安局交通警察支队车辆管理所", "鄂M");
			map.put("鄂N:潜江市公安局交通警察支队车辆管理所", "鄂N");
			map.put("鄂O:湖北省公安厅交通警察总队车辆管理所", "鄂O");
			map.put("鄂P:神农架林区公安局交通警察支队车辆管理所", "鄂P");
			map.put("鄂Q:恩施州公安局交通警察支队车辆管理所", "鄂Q");
			map.put("鄂R:天门市公安局交通警察支队车辆管理所", "鄂R");
			map.put("鄂S:随州市公安局交通警察支队车辆管理所", "鄂S");
			map.put("甘A:兰州市公安局交通警察支队车辆管理所", "甘A");
			map.put("甘B:嘉峪关市公安局交通警察支队车辆管理所", "甘B");
			map.put("甘C:金昌市公安局交通警察支队车辆管理所", "甘C");
			map.put("甘D:白银市公安局交通警察支队车辆管理所", "甘D");
			map.put("甘E:天水市公安局交通警察支队车辆管理所", "甘E");
			map.put("甘F:酒泉行署公安处交通警察支队车辆管理所", "甘F");
			map.put("甘G:张掖行署公安处交通警察支队车辆管理所", "甘G");
			map.put("甘H:武威行署公安处交通警察支队车辆管理所", "甘H");
			map.put("甘J:定西行署公安处交通警察支队车辆管理所", "甘J");
			map.put("甘K:陇南行署公安处交通警察支队车辆管理所", "甘K");
			map.put("甘L:平凉行署公安处交通警察支队车辆管理所", "甘L");
			map.put("甘M:庆阳行署公安处交通警察支队车辆管理所", "甘M");
			map.put("甘N:临夏州公安局交通警察支队车辆管理所", "甘N");
			map.put("甘O:甘肃公安厅交通警察总队车辆管理所", "甘O");
			map.put("甘P:甘南州公安局交通警察支队车辆管理所", "甘P");
			map.put("赣A:南昌市公安局交通警察支队车辆管理所", "赣A");
			map.put("赣B:赣州市公安处交通警察支队车辆管理所", "赣B");
			map.put("赣C:宜春地区公安处交通警察支队车辆管理所", "赣C");
			map.put("赣D:吉安地区公安处交通警察支队车辆管理所", "赣D");
			map.put("赣E:上饶地区公安处交通警察支队车辆管理所", "赣E");
			map.put("赣F:抚州地区公安处交通警察支队车辆管理所", "赣F");
			map.put("赣G:九江市公安局交通警察支队车辆管理所", "赣G");
			map.put("赣H:景德镇市公安局交通警察支队车辆管理所", "赣H");
			map.put("赣J:萍乡市公安局交通警察支队车辆管理所", "赣J");
			map.put("赣K:新余市公安局交通警察支队车辆管理所", "赣K");
			map.put("赣L:鹰潭市公安局交通警察支队车辆管理所", "赣L");
			map.put("赣O:江西省公安厅交通警察总队车辆管理所", "赣O");
			map.put("桂A:南宁市公安局交通警察支队车辆管理所", "桂A");
			map.put("桂B:柳州市公安局交通警察支队车辆管理所", "桂B");
			map.put("桂C:桂林市公安局交通警察支队车辆管理所", "桂C");
			map.put("桂D:梧州市公安局交通警察支队车辆管理所", "桂D");
			map.put("桂E:北海市公安局交通警察支队车辆管理所", "桂E");
			map.put("桂F:崇左市公安局交通警察支队车辆管理所", "桂F");
			map.put("桂G:来宾市公安局交通警察支队车辆管理所", "桂G");
			map.put("桂J:贺州市公安局交通警察支队车辆管理所", "桂J");
			map.put("桂K:玉林市公安局交通警察支队车辆管理所", "桂K");
			map.put("桂L:百色市公安局交通警察支队车辆管理所", "桂L");
			map.put("桂M:河池市公安局交通警察支队车辆管理所", "桂M");
			map.put("桂N:钦州市公安局交通警察支队车辆管理所", "桂N");
			map.put("桂O:广西省公安厅交通警察总队车辆管理所", "桂O");
			map.put("桂P:防城港市公安局交通警察支队车辆管理所", "桂P");
			map.put("桂R:贵港市公安局交通警察支队车辆管理所", "桂R");
			map.put("贵A:贵阳市公安局交通警察支队车辆管理所", "贵A");
			map.put("贵B:六盘水市公安局交通警察支队车辆管理所", "贵B");
			map.put("贵C:遵义市公安局交通警察支队车辆管理所", "贵C");
			map.put("贵D:铜仁地区公安处交通警察支队车辆管理所", "贵D");
			map.put("贵E:黔西南州公安局交通警察支队车辆管理所", "贵E");
			map.put("贵F:毕节地区公安处交通警察支队车辆管理所", "贵F");
			map.put("贵G:安顺市公安局交通警察支队车辆管理所", "贵G");
			map.put("贵H:黔东南州公安局交通警察支队车辆管理所", "贵H");
			map.put("贵J:黔南州公安局交通警察支队车辆管理所", "贵J");
			map.put("贵O:贵州省公安厅交通警察总队车辆管理所", "贵O");
			map.put("黑A:哈尔滨市公安局交通警察支队车辆管理所", "黑A");
			map.put("黑B:齐齐哈尔市公安局交通警察支队车辆管理所", "黑B");
			map.put("黑C:牡丹江市公安局交通警察支队车辆管理所", "黑C");
			map.put("黑D:佳木斯市公安局交通警察支队车辆管理所", "黑D");
			map.put("黑E:大庆市公安局交通警察支队车辆管理所", "黑E");
			map.put("黑F:伊春市公安局交通警察支队车辆管理所", "黑F");
			map.put("黑G:鸡西市公安局交通警察支队车辆管理所", "黑G");
			map.put("黑H:鹤岗市公安局交通警察支队车辆管理所", "黑H");
			map.put("黑J:双鸭山市公安局交通警察支队车辆管理所", "黑J");
			map.put("黑K:七台河市公安局交通警察支队车辆管理所", "黑K");
			map.put("黑M:绥化行署公安局交通警察支队车辆管理所", "黑M");
			map.put("黑N:黑河市公安局交通警察支队车辆管理所", "黑N");
			map.put("黑O:黑龙江省公安厅交通警察总队车辆管理所", "黑O");
			map.put("黑P:大兴安岭地区公安局交通警察支队车辆管理所", "黑P");
			map.put("黑R:垦区公安局交通警察支队车辆管理所", "黑R");
			map.put("沪A:上海市公安局交通警察总队车辆管理所", "沪A");
			map.put("吉A:长春市公安局交通警察支队车辆管理所", "吉A");
			map.put("吉B:吉林市公安局交通警察支队车辆管理所", "吉B");
			map.put("吉C:四平市公安局交通警察支队车辆管理所", "吉C");
			map.put("吉D:辽源市公安局交通警察支队车辆管理所", "吉D");
			map.put("吉E:通化市公安局交通警察支队车辆管理所", "吉E");
			map.put("吉F:白山市公安局交通警察支队车辆管理所", "吉F");
			map.put("吉G:白城市公安局交通警察支队车辆管理所", "吉G");
			map.put("吉H:延边州公安局交通警察支队车辆管理所", "吉H");
			map.put("吉J:松原市公安局交通警察支队车辆管理所", "吉J");
			map.put("吉K:吉林省公安厅交通警察总队长白山保护开发区车辆管理所", "吉K");
			map.put("吉O:吉林省公安厅交通警察总队车辆管理所", "吉O");
			map.put("冀A:石家庄市公安局交通警察支队车辆管理所", "冀A");
			map.put("冀B:唐山市公安局交通警察支队车辆管理所", "冀B");
			map.put("冀C:秦皇岛市公安局交通警察支队车辆管理所", "冀C");
			map.put("云F:玉溪市公安局交通警察支队车辆管理所", "云F");
			map.put("云G:红河州公安局交通警察支队车辆管理所", "云G");
			map.put("云H:文山州公安局交通警察支队车辆管理所", "云H");
			map.put("云J:思茅行署公安局交通警察支队车辆管理所", "云J");
			map.put("云K:西双版纳州公安局交通警察支队车辆管理所", "云K");
			map.put("云L:大理州公安局交通警察支队车辆管理所", "云L");
			map.put("云M:保山行署公安局交通警察支队车辆管理所", "云M");
			map.put("云N:德宏州公安局交通警察支队车辆管理所", "云N");
			map.put("云O:云南省公安厅交通警察总队车辆管理所", "云O");
			map.put("云P:丽江行署公安局交通警察支队车辆管理所", "云P");
			map.put("云Q:怒江州公安局交通警察支队车辆管理所", "云Q");
			map.put("云R:迪庆州公安局交通警察支队车辆管理所", "云R");
			map.put("云S:临沧行署公安局交通警察支队车辆管理所", "云S");
			map.put("浙A:杭州市公安局交通警察支队车辆管理所", "浙A");
			map.put("浙B:宁波市公安局交通警察支队车辆管理所", "浙B");
			map.put("浙C:温州市公安局交通警察支队车辆管理所", "浙C");
			map.put("浙D:绍兴市公安局交通警察支队车辆管理所", "浙D");
			map.put("浙E:湖州市公安局交通警察支队车辆管理所", "浙E");
			map.put("浙F:嘉兴市公安局交通警察支队车辆管理所", "浙F");
			map.put("浙G:金华市公安局交通警察支队车辆管理所", "浙G");
			map.put("浙H:衢州市公安局交通警察支队车辆管理所", "浙H");
			map.put("浙J:台州市公安局交通警察支队车辆管理所", "浙J");
			map.put("浙K:丽水地区公安局交通警察支队车辆管理所", "浙K");
			map.put("浙L:舟山市公安局交通警察支队车辆管理所", "浙L");
			map.put("浙O:浙江省公安厅交通警察总队车辆管理所", "浙O");
			map.put("冀D:邯郸市公安局交通警察支队车辆管理所", "冀D");
			map.put("冀E:邢台市公安局交通警察支队车辆管理所", "冀E");
			map.put("冀F:保定市公安局交通警察支队车辆管理所", "冀F");
			map.put("冀G:张家口市公安局交通警察支队车辆管理所", "冀G");
			map.put("冀H:承德市公安局交通警察支队车辆管理所", "冀H");
			map.put("冀J:沧州市公安局交通警察支队车辆管理所", "冀J");
			map.put("冀O:河北省公安厅交通警察总队车辆管理所", "冀O");
			map.put("冀R:廊坊市公安局交通警察支队车辆管理所", "冀R");
			map.put("冀T:衡水市公安局交通警察支队车辆管理所", "冀T");
			map.put("津A:天津市公安局交通警察总队车辆管理所", "津A");
			map.put("晋A:太原市公安局交通警察支队车辆管理所", "晋A");
			map.put("晋B:大同市公安局交通警察支队车辆管理所", "晋B");
			map.put("晋C:阳泉市公安局交通警察支队车辆管理所", "晋C");
			map.put("晋D:长治市公安局交通警察支队车辆管理所", "晋D");
			map.put("晋E:晋城市公安局交通警察支队车辆管理所", "晋E");
			map.put("晋F:朔州市公安局交通警察支队车辆管理所", "晋F");
			map.put("晋H:忻州行署公安处交通警察支队车辆管理所", "晋H");
			map.put("晋J:吕梁行署公安处交通警察支队车辆管理所", "晋J");
			map.put("晋K:晋中行署公安处交通警察支队车辆管理所", "晋K");
			map.put("晋L:临汾行署公安处交通警察支队车辆管理所", "晋L");
			map.put("晋M:运城行署公安处交通警察支队车辆管理所", "晋M");
			map.put("晋O:山西省公安厅交通警察总队车辆管理所", "晋O");
			map.put("京A:北京市公安交通管理局车辆管理所", "京A");
			map.put("辽A:沈阳市公安局交通警察支队车辆管理所", "辽A");
			map.put("辽B:大连市公安局交通警察支队车辆管理所", "辽B");
			map.put("辽C:鞍山市公安局交通警察支队车辆管理所", "辽C");
			map.put("辽D:抚顺市公安局交通警察支队车辆管理所", "辽D");
			map.put("辽E:本溪市公安局交通警察支队车辆管理所", "辽E");
			map.put("辽F:丹东市公安局交通警察支队车辆管理所", "辽F");
			map.put("辽G:锦州市公安局交通警察支队车辆管理所", "辽G");
			map.put("辽H:营口市公安局交通警察支队车辆管理所", "辽H");
			map.put("辽J:阜新市公安局交通警察支队车辆管理所", "辽J");
			map.put("辽K:辽阳市公安局交通警察支队车辆管理所", "辽K");
			map.put("辽L:盘锦市公安局交通警察支队车辆管理所", "辽L");
			map.put("辽M:铁岭市公安局交通警察支队车辆管理所", "辽M");
			map.put("辽N:朝阳市公安局交通警察支队车辆管理所", "辽N");
			map.put("辽O:辽宁省公安厅交通警察总队车辆管理所", "辽O");
			map.put("辽P:葫芦岛市公安局交通警察支队车辆管理所", "辽P");
			map.put("鲁A:济南市公安局交通警察支队车辆管理所", "鲁A");
			map.put("鲁B:青岛市公安局交通警察支队车辆管理所", "鲁B");
			map.put("鲁C:淄博市公安局交通警察支队车辆管理所", "鲁C");
			map.put("鲁D:枣庄市公安局交通警察支队车辆管理所", "鲁D");
			map.put("鲁E:东营市公安局交通警察支队车辆管理所", "鲁E");
			map.put("鲁F:烟台市公安局交通警察支队车辆管理所", "鲁F");
			map.put("鲁G:潍坊市公安局交通警察支队车辆管理所", "鲁G");
			map.put("鲁H:济宁市公安局交通警察支队车辆管理所", "鲁H");
			map.put("鲁J:泰安市公安局交通警察支队车辆管理所", "鲁J");
			map.put("鲁K:威海市公安局交通警察支队车辆管理所", "鲁K");
			map.put("鲁L:日照市公安局交通警察支队车辆管理所", "鲁L");
			map.put("鲁M:滨州市公安局交通警察支队车辆管理所", "鲁M");
			map.put("鲁N:德州市公安局交通警察支队车辆管理所", "鲁N");
			map.put("鲁O:山东省公安厅交通警察总队车辆管理所", "鲁O");
			map.put("鲁P:聊城市公安局交通警察支队车辆管理所", "鲁P");
			map.put("鲁Q:临沂市公安局交通警察支队车辆管理所", "鲁Q");
			map.put("鲁R:菏泽市公安局交通警察支队车辆管理所", "鲁R");
			map.put("鲁S:莱芜市公安局交通警察支队车辆管理所", "鲁S");
			map.put("鲁U:青岛市公安局交通警察支队车辆管理所", "鲁U");
			map.put("蒙A:呼和浩特市公安局交通警察支队车辆管理所", "蒙A");
			map.put("蒙B:包头市公安局交通警察支队车辆管理所", "蒙B");
			map.put("蒙C:乌海市公安局交通警察支队车辆管理所", "蒙C");
			map.put("蒙D:赤峰市公安局交通警察支队车辆管理所", "蒙D");
			map.put("蒙E:呼伦贝尔盟公安局交通警察支队车辆管理所", "蒙E");
			map.put("蒙F:兴安盟公安局交通警察支队车辆管理所", "蒙F");
			map.put("蒙G:通辽市公安局交通警察支队车辆管理所", "蒙G");
			map.put("蒙H:锡林郭勒盟公安局交通警察支队车辆管理所", "蒙H");
			map.put("蒙J:乌兰察布盟公安局交通警察支队车辆管理所", "蒙J");
			map.put("蒙K:鄂尔多斯市公安局交通警察支队车辆管理所", "蒙K");
			map.put("蒙L:巴彦淖尔盟公安局交通警察支队车辆管理所", "蒙L");
			map.put("蒙M:阿拉善盟公安局交通警察支队车辆管理所", "蒙M");
			map.put("蒙O:内蒙古公安厅交通警察总队车辆管理所", "蒙O");
			map.put("闽A:福州市公安局交通警察支队车辆管理所", "闽A");
			map.put("闽B:莆田市公安局交通警察支队车辆管理所", "闽B");
			map.put("闽C:泉州市公安局交通警察支队车辆管理所", "闽C");
			map.put("闽D:厦门市公安局交通警察支队车辆管理所", "闽D");
			map.put("闽E:漳州市公安局交通警察支队车辆管理所", "闽E");
			map.put("闽F:龙岩市公安局交通警察支队车辆管理所", "闽F");
			map.put("闽G:三明市公安局交通警察支队车辆管理所", "闽G");
			map.put("闽H:南平市公安局交通警察支队车辆管理所", "闽H");
			map.put("闽J:宁德市公安局交通警察支队车辆管理所", "闽J");
			map.put("闽K:福建省公安厅交通警察总队车辆管理所", "闽K");
			map.put("闽O:福建省公安厅交通警察总队车辆管理所", "闽O");
			map.put("宁A:银川市公安局交通警察支队车辆管理所", "宁A");
			map.put("宁B:石嘴山市公安局交通警察支队车辆管理所", "宁B");
			map.put("宁C:吴忠市公安局交通警察支队车辆管理所", "宁C");
			map.put("宁D:固原市公安局交通警察支队车辆管理所", "宁D");
			map.put("宁E:中卫市公安局交通警察支队车辆管理所", "宁E");
			map.put("宁O:宁夏公安厅交通警察总队车辆管理所", "宁O");
			map.put("青A:西宁市公安局交通警察支队车辆管理所", "青A");
			map.put("青B:海东地区公安局交通警察支队车辆管理所", "青B");
			map.put("青C:海北州公安局交通警察支队车辆管理所", "青C");
			map.put("青D:黄南州公安局交通警察支队车辆管理所", "青D");
			map.put("青E:海南州公安局交通警察支队车辆管理所", "青E");
			map.put("青F:果洛州公安局交通警察支队车辆管理所", "青F");
			map.put("青G:玉树州公安局交通警察支队车辆管理所", "青G");
			map.put("青H:海西州公安局交通警察支队车辆管理所", "青H");
			map.put("青O:青海公安厅交通警察总队车辆管理所", "青O");
			map.put("琼A:海口市公安局交通警察支队车辆管理所", "琼A");
			map.put("琼B:三亚市公安局交通警察支队车辆管理所", "琼B");
			map.put("琼C:海南省公安厅交通警察总队(琼北车辆管理所)", "琼C");
			map.put("琼D:海南省公安厅交通警察总队(琼南车辆管理所)", "琼D");
			map.put("琼E:洋埔市公安局交通警察支队车辆管理所（省所代管）", "琼E");
			map.put("琼F:海南省儋州市公安局交通警察支队车辆管理所", "琼F");
			map.put("琼O:海南省公安厅交通警察总队车辆管理所", "琼O");
			map.put("陕A:西安市公安局交通警察支队车辆管理所", "陕A");
			map.put("陕B:铜川市公安局交通警察支队车辆管理所", "陕B");
			map.put("陕C:宝鸡市公安局交通警察支队车辆管理所", "陕C");
			map.put("陕D:咸阳市公安局交通警察支队车辆管理所", "陕D");
			map.put("陕E:渭南市公安局交通警察支队车辆管理所", "陕E");
			map.put("陕F:汉中市公安局交通警察支队车辆管理所", "陕F");
			map.put("陕G:安康地区公安局交通警察支队车辆管理所", "陕G");
			map.put("陕H:商洛市公安局交通警察支队车辆管理所", "陕H");
			map.put("陕J:延安市公安局交通警察支队车辆管理所", "陕J");
			map.put("陕K:榆林地区公安局交通警察支队车辆管理所", "陕K");
			map.put("陕O:陕西公安厅交通警察总队车辆管理所", "陕O");
			map.put("陕U:陕西省公安厅交通警察总队车辆管理所", "陕U");
			map.put("陕V:陕西省杨凌示范区公安局交通警察支队车辆管理所", "陕V");
			map.put("苏A:南京市公安局交通警察支队车辆管理所", "苏A");
			map.put("苏B:无锡市公安局交通警察支队车辆管理所", "苏B");
			map.put("苏C:徐州市公安局交通警察支队车辆管理所", "苏C");
			map.put("苏D:常州市公安局交通警察支队车辆管理所", "苏D");
			map.put("苏E:苏州市公安局交通警察支队车辆管理所", "苏E");
			map.put("苏F:南通市公安局交通警察支队车辆管理所", "苏F");
			map.put("苏G:连云港市公安局交通警察支队车辆管理所", "苏G");
			map.put("苏H:淮安市公安局交通警察支队车辆管理所", "苏H");
			map.put("苏J:盐城市公安局交通警察支队车辆管理所", "苏J");
			map.put("苏K:扬州市公安局交通警察支队车辆管理所", "苏K");
			map.put("苏L:镇江市公安局交通警察支队车辆管理所", "苏L");
			map.put("苏M:泰州市公安局交通警察支队车辆管理所", "苏M");
			map.put("苏N:宿迁市公安局交通警察支队车辆管理所", "苏N");
			map.put("苏O:江苏省公安厅交通警察总队车辆管理所", "苏O");
			map.put("皖A:合肥市公安局交通警察支队车辆管理所", "皖A");
			map.put("皖B:芜湖市公安局交通警察支队车辆管理所", "皖B");
			map.put("皖C:蚌埠市公安局交通警察支队车辆管理所", "皖C");
			map.put("皖D:淮南市公安局交通警察支队车辆管理所", "皖D");
			map.put("皖E:马鞍山市公安局交通警察支队车辆管理所", "皖E");
			map.put("皖F:淮北市公安局交通警察支队车辆管理所", "皖F");
			map.put("皖G:铜陵市公安局交通警察支队车辆管理所", "皖G");
			map.put("皖H:安庆市公安局交通警察支队车辆管理所", "皖H");
			map.put("皖J:黄山市公安局交通警察支队车辆管理所", "皖J");
			map.put("皖K:阜阳市公安局交通警察支队车辆管理所", "皖K");
			map.put("皖L:宿县市公安局交通警察支队车辆管理所", "皖L");
			map.put("皖M:滁州市公安局交通警察支队车辆管理所", "皖M");
			map.put("皖N:六安市公安局交通警察支队车辆管理所", "皖N");
			map.put("皖O:安徽省公安厅交通警察总队车辆管理所", "皖O");
			map.put("皖P:宣城市公安局交通警察支队车辆管理所", "皖P");
			map.put("皖Q:巢湖市公安局交通警察支队车辆管理所", "皖Q");
			map.put("皖R:池州市公安局交通警察支队车辆管理所", "皖R");
			map.put("皖S:亳州市公安局交通警察支队车辆管理所", "皖S");
			map.put("湘A:长沙市公安局交通警察支队车辆管理所", "湘A");
			map.put("湘B:株洲市公安局交通警察支队车辆管理所", "湘B");
			map.put("湘C:湘潭市公安局交通警察支队车辆管理所", "湘C");
			map.put("湘D:衡阳市公安局交通警察支队车辆管理所", "湘D");
			map.put("湘E:邵阳市公安局交通警察支队车辆管理所", "湘E");
			map.put("湘F:岳阳市公安局交通警察支队车辆管理所", "湘F");
			map.put("湘G:张家界市公安局交通警察支队车辆管理所", "湘G");
			map.put("湘H:益阳市公安局交通警察支队车辆管理所", "湘H");
			map.put("湘J:常德市公安局交通警察支队车辆管理所", "湘J");
			map.put("湘K:娄底市公安处交通警察支队车辆管理所", "湘K");
			map.put("湘L:郴州市公安局交通警察支队车辆管理所", "湘L");
			map.put("湘M:永州市公安局交通警察支队车辆管理所", "湘M");
			map.put("湘N:怀化市公安局交通警察支队车辆管理所", "湘N");
			map.put("湘O:湖南省公安厅交通警察总队车辆管理所", "湘O");
			map.put("湘S:湖南省公安厅交通警察总队车辆管理所", "湘S");
			map.put("湘U:湘西自治州公安局交通警察支队车辆管理所", "湘U");
			map.put("新A:乌鲁木齐市公安局交通警察支队车辆管理所", "新A");
			map.put("新B:昌吉回族自治州公安局交通警察支队车辆管理所", "新B");
			map.put("新C:石河子市公安局交通警察支队车辆管理所", "新C");
			map.put("新D:奎屯市公安局交通警察支队车辆管理所", "新D");
			map.put("新E:博尔塔拉蒙古自治州公安局交通警察支队车辆管理所", "新E");
			map.put("新F:伊犁哈萨克自治州公安局交通警察支队车辆管理所", "新F");
			map.put("新G:塔城行署公安处交通警察支队车辆管理所", "新G");
			map.put("新H:阿勒泰行署公安处交通警察支队车辆管理所", "新H");
			map.put("新J:克拉玛依市公安局交通警察支队车辆管理所", "新J");
			map.put("新K:吐鲁番行署公安处交通警察支队车辆管理所", "新K");
			map.put("新L:哈密行署公安处交通警察支队车辆管理所", "新L");
			map.put("新M:巴音郭楞蒙古自治州公安局交通警察支队车辆管理所", "新M");
			map.put("新N:阿克苏行署公安处交通警察支队车辆管理所", "新N");
			map.put("新O:新疆公安厅交通警察总队车辆管理所", "新O");
			map.put("新P:克孜勒苏尔克孜自治区公安局交通警察支队车辆管理所", "新P");
			map.put("新Q:喀什行署公安处交通警察支队车辆管理所", "新Q");
			map.put("新R:和田行署公安处交通警察支队车辆管理所", "新R");
			map.put("渝A:重庆市公安局交通警察支队车辆管理所", "渝A");
			map.put("豫A:郑州市公安局交通警察支队车辆管理所", "豫A");
			map.put("豫B:开封市公安局交通警察支队车辆管理所", "豫B");
			map.put("豫C:洛阳市公安局交通警察支队车辆管理所", "豫C");
			map.put("豫D:平顶山市公安局交通警察支队车辆管理所", "豫D");
			map.put("豫E:安阳市公安局交通警察支队车辆管理所", "豫E");
			map.put("豫F:鹤壁市公安局交通警察支队车辆管理所", "豫F");
			map.put("豫G:新乡市公安局交通警察支队车辆管理所", "豫G");
			map.put("豫H:焦作市公安局交通警察支队车辆管理所", "豫H");
			map.put("豫J:濮阳市公安局交通警察支队车辆管理所", "豫J");
			map.put("豫K:许昌市公安局交通警察支队车辆管理所", "豫K");
			map.put("豫L:漯河市公安局交通警察支队车辆管理所", "豫L");
			map.put("豫M:三门峡市公安局交通警察支队车辆管理所", "豫M");
			map.put("豫N:商丘市公安局交通警察支队车辆管理所", "豫N");
			map.put("豫O:河南省公安厅交通警察总队车辆管理所", "豫O");
			map.put("豫P:周口店地区公安处车辆管理所", "豫P");
			map.put("豫Q:驻马店地区公安处车辆管理所", "豫Q");
			map.put("豫R:南阳市公安局交通警察支队车辆管理所", "豫R");
			map.put("豫S:信阳市公安局交通警察支队车辆管理所", "豫S");
			map.put("豫U:济源市公安局交通警察支队车辆管理所", "豫U");
			map.put("粤A:广州市公安局交通警察支队车辆管理所", "粤A");
			map.put("粤C:珠海市公安局交通警察支队车辆管理所", "粤C");
			map.put("粤D:汕头市公安局交通警察支队车辆管理所", "粤D");
			map.put("粤E:佛山市公安局交通警察支队车辆管理所", "粤E");
			map.put("粤F:韶关市公安局交通警察支队车辆管理所", "粤F");
			map.put("粤G:湛江市公安局交通警察支队车辆管理所", "粤G");
			map.put("粤H:肇庆市公安局交通警察支队车辆管理所", "粤H");
			map.put("粤J:江门市公安局交通警察支队车辆管理所", "粤J");
			map.put("粤K:茂名市公安局交通警察支队车辆管理所", "粤K");
			map.put("粤L:惠州市公安局交通警察支队车辆管理所", "粤L");
			map.put("粤M:梅州市公安局交通警察支队车辆管理所", "粤M");
			map.put("粤N:汕尾市公安局交通警察支队车辆管理所", "粤N");
			map.put("粤O:广东省公安厅交通警察总队车辆管理所", "粤O");
			map.put("粤P:河源市公安局交通警察支队车辆管理所", "粤P");
			map.put("粤Q:阳江市公安局交通警察支队车辆管理所", "粤Q");
			map.put("粤R:清远市公安局交通警察支队车辆管理所", "粤R");
			map.put("粤S:东莞市公安局交通警察支队车辆管理所", "粤S");
			map.put("粤T:中山市公安局交通警察支队车辆管理所", "粤T");
			map.put("粤U:潮州市公安局交通警察支队车辆管理所", "粤U");
			map.put("粤V:揭阳市公安局交通警察支队车辆管理所", "粤V");
			map.put("粤W:云浮市公安局交通警察支队车辆管理所", "粤W");
			map.put("粤X:佛山市公安局顺德分局交通警察大队车辆管理所", "粤X");
			map.put("粤Y:佛山市公安局南海分局交通警察大队车辆管理所", "粤Y");
			map.put("粤Z:广东省公安厅交通警察总队车辆管理所", "粤Z");
			map.put("云A:昆明市公安局交通警察支队车辆管理所", "云A");
			map.put("云B:东川市公安局交通警察支队车辆管理所", "云B");
			map.put("云C:昭通行署公安局交通警察支队车辆管理所", "云C");
			map.put("云D:曲靖市公安局交通警察支队车辆管理所", "云D");
			map.put("云E:楚雄州公安局交通警察支队车辆管理所", "云E");
			Object[] objArr = new Object[386];
			ArrayList<JSONObject> list = new ArrayList<>();

			for (String key : map.keySet()) {
				IssuingLicenceAuthority issuingLicenceAuthority = new IssuingLicenceAuthority();
				issuingLicenceAuthority.setLongName(key);
				issuingLicenceAuthority.setShortName(map.get(key));
				JSONObject json = (JSONObject) JSONObject.toJSON(issuingLicenceAuthority);
				list.add(json);
			}

			for (int i = 0; i < 386; i++) {
				objArr[i] = list.get(i);
			}

			baseBean.setData(objArr);
		} catch (Exception e) {
			logger.error("发证机关参数转换异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
    
   /**
    * 路况查询
    * @param sourceOfCertification
    * http://192.168.1.245:8080/web/user/trafficQuery.html?sourceOfCertification=C
    */
    @RequestMapping("trafficQuery")
    public void trafficQuery(String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();	
    	try{
    		//创建返回结果
			Map<String, Object> map = accountService.trafficQuery(sourceOfCertification);
			String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if("0000".equals(code)){
				List<TrafficQueryVo> list = (List<TrafficQueryVo>) map.get("data");
        		baseBean.setCode(MsgCode.success);
        		baseBean.setData(list);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg);
				baseBean.setData("");
        	}
			
		} catch (Exception e) {
			logger.error("路况查询异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 单条路况查询
     * @param zjz 事件编号
     * @param sourceOfCertification
     *  http://192.168.1.245:8080/web/user/detailsTrafficQuery.html?zjz=537535&sourceOfCertification=C
     */
     @RequestMapping("detailsTrafficQuery")
	public void detailsTrafficQuery(String zjz, String sourceOfCertification) {
		BaseBean baseBean = new BaseBean();
		boolean falg = false;
		try {
			// 创建返回结果
			Map<String, String> map = accountService.detailsTrafficQuery(zjz, sourceOfCertification);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String pic = map.get("data");
				try {
					Map<String, Object> tmap = accountService.trafficQuery(sourceOfCertification);
					String tcode = (String) tmap.get("code");
					String tmsg = (String) tmap.get("msg");
					if ("0000".equals(tcode)) {
						List<TrafficQueryVo> trafficQueryVos = (List<TrafficQueryVo>) tmap.get("data");
						for (TrafficQueryVo trafficQueryVo : trafficQueryVos) {
							if (zjz.equals(trafficQueryVo.getId())) {
								falg =true;
								trafficQueryVo.setPic(pic);
								baseBean.setCode(MsgCode.success);
								baseBean.setData(trafficQueryVo);
							}
						}
					} else {
						baseBean.setCode(MsgCode.businessError);
						baseBean.setMsg(tmsg);
						baseBean.setData("");
					}

				} catch (Exception e) {
					logger.error("路况查询异常:" + e);
					DealException(baseBean, e);
				}
				
				if (!falg) {
					baseBean.setCode(MsgCode.businessError);
					baseBean.setMsg("未查询到相关信息");
				}

			} else {
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg(msg);
				baseBean.setData("");
			}

		} catch (Exception e) {
			logger.error("单条路况查询异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
     
     
 /**
  * 查询
  * @param 
  * @param 
  *  http://192.168.1.245:8080/web/user/detailsTrafficQuery.html?zjz=537535&sourceOfCertification=C
  */
   @RequestMapping("getUserBinds")
 	public void getUserBinds(Integer page,Integer pageSize) {
 		BaseBean baseBean = new BaseBean();
 		try{
 			List<UserBind> userBinds = accountService.getUserBinds(page, pageSize);
 			baseBean.setData(userBinds);
 		} catch (Exception e) {
 			logger.error("getUserBinds:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 	}
      
  /**
   * 查询
   * @param 
   * @param 
   *  http://192.168.1.245:8080/web/user/detailsTrafficQuery.html?zjz=537535&sourceOfCertification=C
   */
    @RequestMapping("getUserBindAlipays")
  	public void getUserBindAlipays(Integer page,Integer pageSize) {
  		BaseBean baseBean = new BaseBean();
  		try{
  			List<UserBindAlipay> userBindAlipays = accountService.getUserBindAlipays(page, pageSize);
  			baseBean.setData(userBindAlipays);
  		} catch (Exception e) {
  			logger.error("getUserBinds:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  	}
     
    /**
     * 重新认证 
     * @param identityCard
     * @param mobilephone
     * @param authenticationType
     * @param sourceOfCertification
     * @param photo6
     * @param photo9
     */
     @RequestMapping("reauthentication")
     public void reauthentication(String identityCard , String mobilephone ,String authenticationType ,String sourceOfCertification ,String photo6 ,String photo9,String businessType ,String openId) {
     	BaseBean baseBean = new BaseBean();
     	ReauthenticationVo reauthenticationVo = new ReauthenticationVo();
     	if(StringUtil.isBlank(identityCard)){
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("身份证号不能为空!");
			renderJSON(baseBean);
			return;
		}else{
			reauthenticationVo.setIdentityCard(identityCard);
		}
     	if(StringUtil.isBlank(mobilephone)){
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("移动电话不能为空!");
			renderJSON(baseBean);
			return;
		}else{
			reauthenticationVo.setMobilephone(mobilephone);
		}
     	if(StringUtil.isBlank(authenticationType)){
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("认证类型不能为空!");
			renderJSON(baseBean);
			return;
		}else{
			reauthenticationVo.setAuthenticationType(authenticationType);
		}
     	if(StringUtil.isBlank(sourceOfCertification)){
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("来源标志不能为空!");
			renderJSON(baseBean);
			return;
		}else{
			reauthenticationVo.setSourceOfCertification(sourceOfCertification);
		}
     	if(StringUtil.isBlank(photo6)){
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("当事人手持身份证照片不能为空!");
			renderJSON(baseBean);
			return;
		}else{
			reauthenticationVo.setPhoto6(photo6);
		}
     	if(StringUtil.isBlank(photo9)){
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("车主身份证正面照片不能为空!");
			renderJSON(baseBean);
			return;
		}else{
			reauthenticationVo.setPhoto9(photo9);
		}
     	if(StringUtil.isBlank(openId)){
			baseBean.setCode(MsgCode.paramsError);
			baseBean.setMsg("openId不能为空!");
			renderJSON(baseBean);
			return;
		}
     	
     	try{
     		//创建返回结果
 			Map<String, String> map = accountService.reauthentication(reauthenticationVo);
 			logger.info(map.toString());
 			String code = map.get("code");
 			String msg = map.get("msg");
 			if ("0000".equals(code)) {
 				if(StringUtil.isNotBlank(businessType)&&"C".equals(sourceOfCertification)){
 	         		baseBean.setCode(MsgCode.success);
 	         		String waterNumber = map.get("data");
 					HandleTemplateVo handleTemplateVo = new HandleTemplateVo(1, "reauthentication", waterNumber, DateUtil2.date2str(new Date()));
 					baseBean.setData(handleTemplateVo);
 					String url = HandleTemplateVo.getUrl(handleTemplateVo,handleService.getTemplateSendUrl());
 					logger.info("返回的url是：" + url);
 					logger.info("handleTemplateVo 是：" + handleTemplateVo);
 					MessageChannelModel model = new MessageChannelModel();
 					model.setOpenid(openId);
 					if ("1".equals(businessType)) {
 						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbn3H9wpHz8dKjXPL9J_xC5s");
 					}else if ("2".equals(businessType)) {
 						model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbjAEGcUfJBYRRfOgme0SPuk");
 					}
 					model.setResult_page_style_id("23ClyLHM5Fr790uz7t-fxiodPnL9ohRzcnlGWEudkL8");
 					model.setDeal_msg_style_id("23ClyLHM5Fr790uz7t-fxlzJePTelFGvOKtKR4udm1o");
 					model.setCard_style_id("");
 					model.setOrder_no(waterNumber);
 					model.setUrl(url);
 					Map<String, cn.message.model.wechat.MessageChannelModel.Property> tmap = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
 					tmap.put("first", new MessageChannelModel().new Property("您好，您的业务办理申请已申请，具体信息如下：","#212121"));
 					tmap.put("keyword1",
 							new MessageChannelModel().new Property(DateUtil.formatDateTime(new Date()), "#212121"));
 					tmap.put("keyword2", new MessageChannelModel().new Property("重新认证", "#212121"));
 					tmap.put("keyword3", new MessageChannelModel().new Property("待初审", "#212121"));
 					tmap.put("remark", new MessageChannelModel().new Property("","#212121"));
 					model.setData(tmap);
 					BaseBean msgBean = templateMessageService.sendServiceMessage(model);
 					logger.info("发送模板消息结果：" + JSON.toJSONString(msgBean));
 					
 					//发送成功
 					if("0".equals(msgBean.getCode())){
 						baseBean.setMsg(msgBean.getData().toString());//结果评价页url设置在msg中
 					}else{
 						baseBean.setMsg(url);
 					}
// 	         		baseBean.setMsg(msg);
// 	         		baseBean.setData(map.get("data"));
 	         	}else{
 	         		baseBean.setCode("0000");
 	         		baseBean.setData(map.get("data"));
 	         		baseBean.setMsg(msg);
 	         	}
			}else{
         		baseBean.setCode(MsgCode.businessError);
 				baseBean.setMsg(msg);
         	}
 			
 			
 		} catch (Exception e) {
 			logger.error("重新认证异常:" + e);
 			DealException(baseBean, e);
 		}
 		renderJSON(baseBean);
 		logger.debug(JSON.toJSONString(baseBean));
     }
     
     
     
     /**
      * 查询星级用户认证
      * @param identityCard 身份证
      * @param sourceOfCertification 认证来源
      * http://localhost/web/user/getIdentificationOfAuditResults.html?identityCard=440301199002101119&sourceOfCertification=C
      */
      @RequestMapping("getIdentificationOfAuditResults")
      public void getIdentificationOfAuditResults(String identityCard ,String sourceOfCertification) {
      	BaseBean baseBean = new BaseBean();
      	if(StringUtil.isBlank(identityCard)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("identityCard 不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("sourceOfCertification 不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	try{
      		//创建返回结果
      		List<IdentificationOfAuditResultsVo> identificationOfAuditResultsVos = accountService.getIdentificationOfAuditResults(identityCard, sourceOfCertification);
  			if(null != identificationOfAuditResultsVos){
  				baseBean.setCode(MsgCode.success);
          		baseBean.setMsg("");
          		baseBean.setData(identificationOfAuditResultsVos);
  			}else{
          		baseBean.setCode(MsgCode.businessError);
  				baseBean.setMsg("未查询到数据，请确认您的输入信息是否正确！");
          	}
  		} catch (Exception e) {
  			logger.error("getIdentificationOfAuditResults:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 接入授权
       * @param mobilephone
       * @param identityCard
       * @param userSource
       */
      @RequestMapping("accessAuthorization")
      public void accessAuthorization(String mobilephone, String identityCard, String userSource) {
      	BaseBean baseBean = new BaseBean();
      	if(StringUtil.isBlank(mobilephone)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("手机号号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(identityCard)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("身份证号不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(userSource)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("用户来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	try{
      		baseBean = accountService.accessAuthorization(mobilephone, identityCard, userSource); 
  		} catch (Exception e) {
  			logger.error("接入授权异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 问题反馈
       * @param name
       * @param identityCard
       * @param mobilephone
       * @param userSource
       * @param certificationType
       * @param photo6
       * @param openId
       */
      @RequestMapping("weChatBrushFaceAuthentication")
      public void weChatBrushFaceAuthentication(String name, String identityCard, String mobilephone ,String userSource ,String certificationType ,String photo6 ,String openId) {
      	BaseBean baseBean = new BaseBean();
      	if(StringUtil.isBlank(name)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("姓名不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(certificationType)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("认证类型不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(photo6)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("照片不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(openId)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("openId不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(mobilephone)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("手机号号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(identityCard)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("身份证号不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(userSource)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("用户来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	BrushFaceVo brushFaceVo= new BrushFaceVo();
      	brushFaceVo.setCertificationType(certificationType);
      	brushFaceVo.setIdentityCard(identityCard);
      	brushFaceVo.setMobilephone(mobilephone);
      	brushFaceVo.setName(name);
      	brushFaceVo.setOpenId(openId);
      	brushFaceVo.setPhoto6(photo6);
      	brushFaceVo.setUserSource(userSource);
      	try{
      		baseBean = accountService.weChatBrushFaceAuthentication(brushFaceVo); 
  		} catch (Exception e) {
  			logger.error("接入授权异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 星级用户问题反馈
       * @param openId
       * @param remark
       * @param status
       * @param img1
       * @param img2
       * @param img3
       */
      @RequestMapping("problemFeedback")
      public void problemFeedback(String openId ,String remark ,String status ,String img1 ,String img2 ,String img3) {
      	BaseBean baseBean = new BaseBean();
      	if(StringUtil.isBlank(openId)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("微信openId不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(remark)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("反馈内容不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(img1)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("图片不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
    	if(StringUtil.isBlank(status)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("状态不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	try{
      		ProblemFeedbackVo problemFeedbackVo = new ProblemFeedbackVo();
      		List<StVo> base64Imgs = new ArrayList<StVo>();
	     	if(StringUtils.isNotBlank(img1)){
	     		StVo stVo1 = new StVo(img1, DateUtil.formatDateTime(new Date()));
	     		base64Imgs.add(stVo1);
	     	}
	     	if(StringUtils.isNotBlank(img2)){
	     		StVo stVo2 = new StVo(img2,DateUtil.formatDateTime(new Date()));
	     		base64Imgs.add(stVo2);
	     	}
	     	if(StringUtils.isNotBlank(img3)){
	     		StVo stVo3 = new StVo(img3,DateUtil.formatDateTime(new Date()));
	     		base64Imgs.add(stVo3);
	     	}
	     	List<String> imgs = new ArrayList<String>();
	     	try {
	     		imgs = fileService.problemFeedback(openId, base64Imgs);
			} catch (Exception e) {
				logger.error("写图片到225服务器失败", e);
			}
	     	StringBuffer sb = new StringBuffer();
	     	if (null != imgs && imgs.size()>0) {
				for (String string : imgs) {
					sb.append(string + ";");
				}
			}
	     	problemFeedbackVo.setJpgurl(sb.toString());
          	problemFeedbackVo.setIntime(new Date());
          	problemFeedbackVo.setRemark(remark);
          	problemFeedbackVo.setStatus(Integer.parseInt(status));
          	problemFeedbackVo.setWxcode(openId);
      		int result = fileService.saveProblemFeedback(problemFeedbackVo);
      		if (1 == result) {
				logger.info("数据写入数据库成功，problemFeedbackVo ="+problemFeedbackVo);
			}else{
				logger.info("数据写入数据库失败，problemFeedbackVo ="+problemFeedbackVo);
			}
  		} catch (Exception e) {
  			logger.error("深圳交警问题反馈异常:" + e);
//  			DealException(baseBean, e);
  		}finally{
  			baseBean.setCode("0000");
  			baseBean.setData("反馈成功!");
  			renderJSON(baseBean);
  	  		logger.debug(JSON.toJSONString(baseBean));
  		}
      } 

    /**
     * 车辆绑定审核结果查询
     * @param identityCardNo
     * @param sourceOfCertification
     */
	@RequestMapping("queryVehicleBindAuditResult")
	public void queryVehicleBindAuditResult(String identityCardNo, String sourceOfCertification) {
		BaseBean baseBean = new BaseBean();
		try{
			if(StringUtil.isBlank(identityCardNo)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("用户来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			baseBean = accountService.queryVehicleBindAuditResult(identityCardNo, sourceOfCertification);
		} catch (Exception e) {
			logger.error("车辆绑定审核结果查询Action异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
      /**
       * 单位注册
       * @param request
       * @param response
       */
      @RequestMapping("companyRegister")
      public void companyRegister(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();	
      	String organizationCodeNumber = request.getParameter("organizationCodeNumber");
      	String companyName = request.getParameter("companyName");
      	String applicantIdentityCard  = request.getParameter("applicantIdentityCard");
      	String applicantName = request.getParameter("applicantName");
      	String applicantMobilephone = request.getParameter("applicantMobilephone");
      	String applicantAddress = request.getParameter("applicantAddress");
      	String photo9 = request.getParameter("photo9");
      	String photo32 = request.getParameter("photo32");
      	String photo33 = request.getParameter("photo33");
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
      	if(StringUtil.isBlank(organizationCodeNumber)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("组织机构代码证号不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(companyName)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("单位名称不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(applicantIdentityCard)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("申请人身份证号不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(applicantName)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("申请人姓名不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(applicantMobilephone)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("申请人联系电话不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(applicantAddress)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("申请人联系地址不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(photo9)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("身份证正面照片不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(photo32)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("组织机构代码证照片不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(photo33)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("申请人手持身份证正面和组织机构代码证照片不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("注册来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	CompanyRegisterVo companyRegisterVo = new CompanyRegisterVo();
      	companyRegisterVo.setApplicantAddress(applicantAddress);
      	companyRegisterVo.setApplicantIdentityCard(applicantIdentityCard);
      	companyRegisterVo.setApplicantMobilephone(applicantMobilephone);
      	companyRegisterVo.setApplicantName(applicantName);
      	companyRegisterVo.setCompanyName(companyName);
      	companyRegisterVo.setOrganizationCodeNumber(organizationCodeNumber);
      	companyRegisterVo.setPhoto32(photo32);
      	companyRegisterVo.setPhoto33(photo33);
      	companyRegisterVo.setPhoto9(photo9);
      	companyRegisterVo.setSourceOfCertification(sourceOfCertification);
      	try{
  			baseBean = accountService.companyRegister(companyRegisterVo);
  		} catch (Exception e) {
  			logger.error("单位注册异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 单位注册查询
       * @param request
       * @param response
       */
      @RequestMapping("queryCompanyRegister")
      public void queryCompanyRegister(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();	
      	String organizationCodeNumber = request.getParameter("organizationCodeNumber");
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
      	if(StringUtil.isBlank(organizationCodeNumber)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("组织机构代码证号不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
    	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("查询来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	try{
  			baseBean = accountService.queryCompanyRegister(organizationCodeNumber, sourceOfCertification);
  		} catch (Exception e) {
  			logger.error("单位注册查询异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 单位用户登录
       * @param request
       * @param response
       */
      @RequestMapping("companyUserLogin")
      public void companyUserLogin(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();	
      	String loginUser = request.getParameter("loginUser");
      	String loginPwd = request.getParameter("loginPwd");
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
      	if(StringUtil.isBlank(loginUser)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车辆管理人身份证号码/手机号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(loginPwd)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("登录密码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("用户来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	try{
  			baseBean = accountService.companyUserLogin(loginUser, loginPwd, sourceOfCertification);
  		} catch (Exception e) {
  			logger.error("单位用户登录异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 单位用户修改密码
       * @param request
       * @param response
       */
      @RequestMapping("companyUserChangePwd")
      public void companyUserChangePwd(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();	
      	String loginUser = request.getParameter("loginUser");
      	String oldPwd = request.getParameter("oldPwd");
      	String newPwd  = request.getParameter("newPwd");
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
      	if(StringUtil.isBlank(loginUser)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车辆管理人身份证号码/手机号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(oldPwd)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("旧密码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(newPwd)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("新密码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("用户来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	try{
  			baseBean = accountService.companyUserChangePwd(loginUser, oldPwd, newPwd, sourceOfCertification);
  		} catch (Exception e) {
  			logger.error("密码修改异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 单位用户绑定车辆
       * @param request
       * @param response
       */
      @RequestMapping("bindCompanyCar")
      public void bindCompanyCar(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();	
      	String licenseNumber = request.getParameter("licenseNumber");
      	String loginUser = request.getParameter("loginUser");
      	String provinceCode  = request.getParameter("provinceCode");
      	String numberPlate = request.getParameter("numberPlate");
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
      	if(StringUtil.isBlank(loginUser)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车辆管理人身份证号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(provinceCode)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("省份简称不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(licenseNumber)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车牌号码不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(numberPlate)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("号牌种类不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("用户来源不能为空!");
 			renderJSON(baseBean);
 			return;
 		}
      	BindCompanyCarVo bindCompanyCarVo = new BindCompanyCarVo();
      	bindCompanyCarVo.setLicenseNumber(licenseNumber);
      	bindCompanyCarVo.setLoginUser(loginUser);
      	bindCompanyCarVo.setProvinceCode(provinceCode);
      	bindCompanyCarVo.setSourceOfCertification(sourceOfCertification);
      	bindCompanyCarVo.setNumberPlate(numberPlate);
      	try{
  			baseBean = accountService.bindCompanyCar(bindCompanyCarVo);
  		} catch (Exception e) {
  			logger.error("单位用户绑定车辆异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      /**
       * 本人名下车辆查询
       * @param request
       * @param response
       */
      @RequestMapping("getMyCompanyCars")
      public void getMyCompanyCars(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();	
      	String loginUser = request.getParameter("loginUser");
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
      	if(StringUtil.isBlank(loginUser)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车辆管理人身份证号码不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("用户来源不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
      	CompanyRegisterVo companyRegisterVo = new CompanyRegisterVo();
      	try{
  			baseBean = accountService.getMyCompanyCars(loginUser, sourceOfCertification);
  		} catch (Exception e) {
  			logger.error("本人名下车辆查询异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      
      
      /**
       * 信息采集
       * @param request
       * @param response
       */
      @RequestMapping("informationCollection")
      public void informationCollection(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();
      	String licenseNumber = request.getParameter("licenseNumber");                  
      	String numberPlate = request.getParameter("numberPlate");                     
      	String carType = request.getParameter("carType");                         
      	String vehicleIdentificationNumber = request.getParameter("vehicleIdentificationNumber");     
      	String ownerIdentityCard = request.getParameter("ownerIdentityCard");               
      	String ownerMobilephone = request.getParameter("ownerMobilephone");                
      	String ownerAddress = request.getParameter("ownerAddress");                    
      	String identityCard = request.getParameter("identityCard");                    
      	String mobilephone = request.getParameter("mobilephone");                    
      	String address = request.getParameter("address");                         
      	String copyOfOwnerIdentityCard = request.getParameter("copyOfOwnerIdentityCard");         
      	String copyOfDriverLicense = request.getParameter("copyOfDriverLicense");             
      	String copyOfVehicleTravelLicense = request.getParameter("copyOfVehicleTravelLicense");      
      	String copyOfLegalEntity = request.getParameter("copyOfLegalEntity"); 
      	String copyOfLegalEntityA = request.getParameter("copyOfLegalEntityA"); 
      	String copyOfApplicant = request.getParameter("copyOfApplicant");                 
      	String loginUser = request.getParameter("loginUser");                      
      	String userMobilepbone = request.getParameter("userMobilepbone");                
      	String certificationType = request.getParameter("certificationType");              
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
      	String issuingBrigade = request.getParameter("issuingBrigade");
      	String isAttached = request.getParameter("isAttached");
      	String legalEntityName = request.getParameter("legalEntityName");
      	if(StringUtil.isBlank(certificationType)){
      		baseBean.setCode(MsgCode.paramsError);
      		baseBean.setMsg("申请认证类型不能为空!");
      		renderJSON(baseBean);
      		return;
      	}else if("2" == certificationType){
      		if(StringUtil.isBlank(copyOfLegalEntity)){
     			baseBean.setCode(MsgCode.paramsError);
     			baseBean.setMsg("单位法人反面照片不能为空!");
     			renderJSON(baseBean);
     			return;
          	}
        	if(StringUtil.isBlank(copyOfLegalEntityA)){
     			baseBean.setCode(MsgCode.paramsError);
     			baseBean.setMsg("单位法人正面照片不能为空!");
     			renderJSON(baseBean);
     			return;
          	}
        	if(StringUtil.isBlank(copyOfApplicant)){
     			baseBean.setCode(MsgCode.paramsError);
     			baseBean.setMsg("申请人手持身份证+组织代码证照片不能为空!");
     			renderJSON(baseBean);
     			return;
          	}
      	}
      	if(StringUtil.isBlank(isAttached)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("是否挂靠不能为空!");
 			renderJSON(baseBean);
 			return;
      	}else{
      		if ("1".equals(isAttached)) {
      			if(StringUtil.isBlank(copyOfApplicant)){
         			baseBean.setCode(MsgCode.paramsError);
         			baseBean.setMsg("申请人手持身份证+组织代码证照片不能为空!");
         			renderJSON(baseBean);
         			return;
              	}
			}
      	}
    	if(StringUtil.isBlank(licenseNumber)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车牌号码不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(legalEntityName)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车主姓名不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(numberPlate)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("号牌种类不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(carType)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车辆类型不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(vehicleIdentificationNumber)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车架号不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(ownerIdentityCard)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车主身份证号不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(ownerMobilephone)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车主联系电话不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(ownerAddress)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车主联系地址不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(identityCard)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("身份证号不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(mobilephone)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("联系电话不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(address)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("联系地址不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(copyOfOwnerIdentityCard)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车主身份证复印件不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(copyOfDriverLicense)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车辆驾驶人驾驶证复印件不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(copyOfVehicleTravelLicense)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("机动车行驶证复印件不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(loginUser)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("星级用户身份证号不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(userMobilepbone)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("星级用户手机号不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("认证来源不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(issuingBrigade)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("发卡大队不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	InformationCollectionVo informationCollectionVo = new InformationCollectionVo();
    	informationCollectionVo.setAddress(address);
    	informationCollectionVo.setCarType(carType);
    	informationCollectionVo.setCertificationType(certificationType);
    	informationCollectionVo.setCopyOfApplicant(copyOfApplicant);
    	informationCollectionVo.setCopyOfDriverLicense(copyOfDriverLicense);
    	informationCollectionVo.setCopyOfLegalEntity(copyOfLegalEntity);
    	informationCollectionVo.setCopyOfLegalEntityA(copyOfLegalEntityA);
    	informationCollectionVo.setCopyOfOwnerIdentityCard(copyOfOwnerIdentityCard);
    	informationCollectionVo.setCopyOfVehicleTravelLicense(copyOfVehicleTravelLicense);
    	informationCollectionVo.setIdentityCard(identityCard);
    	informationCollectionVo.setLicenseNumber(licenseNumber);
    	informationCollectionVo.setLoginUser(loginUser);
    	informationCollectionVo.setMobilephone(mobilephone);
    	informationCollectionVo.setNumberPlate(numberPlate);
    	informationCollectionVo.setOwnerAddress(ownerAddress);
    	informationCollectionVo.setOwnerIdentityCard(ownerIdentityCard);
    	informationCollectionVo.setOwnerMobilephone(ownerMobilephone);
    	informationCollectionVo.setSourceOfCertification(sourceOfCertification);
    	informationCollectionVo.setUserMobilepbone(userMobilepbone);
    	informationCollectionVo.setVehicleIdentificationNumber(vehicleIdentificationNumber);
    	informationCollectionVo.setIssuingBrigade(issuingBrigade);
    	informationCollectionVo.setIsAttached(isAttached);
    	informationCollectionVo.setLegalEntityName(legalEntityName);
      	try{
  			baseBean = accountService.informationCollection(informationCollectionVo);
  			if ("9999".equals(baseBean.getCode())) {
				baseBean.setMsg("信息采集异常,请重试！");
			}
  		} catch (Exception e) {
  			logger.error("信息采集异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      
      /**
       * 信息采集查询
       * @param request
       * @param response
       */
      @RequestMapping("queryInformationCollection")
      public void queryInformationCollection(HttpServletRequest request,HttpServletResponse response){
      	BaseBean baseBean = new BaseBean();	
      	String licenseNumber = request.getParameter("licenseNumber");
      	String numberPlate = request.getParameter("numberPlate");
      	String loginUser = request.getParameter("loginUser");
      	String sourceOfCertification = request.getParameter("sourceOfCertification");
    	if(StringUtil.isBlank(licenseNumber)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("车牌号码不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if (!licenseNumber.startsWith("粤B")) {
    		baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("必须粤B车牌才能办理此业务!");
 			renderJSON(baseBean);
 			return;
		}
    	if(StringUtil.isBlank(numberPlate)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("号牌种类不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(loginUser)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("星级用户不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	if(StringUtil.isBlank(sourceOfCertification)){
 			baseBean.setCode(MsgCode.paramsError);
 			baseBean.setMsg("认证来源不能为空!");
 			renderJSON(baseBean);
 			return;
      	}
    	InformationCollectionVo informationCollectionVo = new InformationCollectionVo();
    	informationCollectionVo.setLicenseNumber(licenseNumber);
    	informationCollectionVo.setNumberPlate(numberPlate);
    	informationCollectionVo.setLoginUser(loginUser);
    	informationCollectionVo.setSourceOfCertification(sourceOfCertification);
      	try{
  			baseBean = accountService.queryInformationCollection(informationCollectionVo);
  			if ("9999".equals(baseBean.getCode())) {
				baseBean.setMsg("信息查询异常，请重试！");
			}
  		} catch (Exception e) {
  			logger.error("信息采集查询异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
      
      
      /**
       * 获取芝麻信用biz_no
       */
      @RequestMapping("getBizNo")
      public void getBizNo(String certName, String certNo){
      	BaseBean baseBean = new BaseBean();	
      	try{
	    	if(StringUtil.isBlank(certName)){
	 			baseBean.setCode(MsgCode.paramsError);
	 			baseBean.setMsg("certName不能为空!");
	 			renderJSON(baseBean);
	 			return;
	      	}
	    	if(StringUtil.isBlank(certNo)){
	    		baseBean.setCode(MsgCode.paramsError);
	    		baseBean.setMsg("certNo不能为空!");
	    		renderJSON(baseBean);
	    		return;
	    	}
  			baseBean = accountService.getBizNo(certName, certNo);
  		} catch (Exception e) {
  			logger.error("获取芝麻信用biz_no异常:" + e);
  			DealException(baseBean, e);
  		}
  		renderJSON(baseBean);
  		logger.debug(JSON.toJSONString(baseBean));
      }
}
