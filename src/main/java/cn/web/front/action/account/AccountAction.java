package cn.web.front.action.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
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
import cn.account.bean.UserBind;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.bean.vo.UserBasicVo;
import cn.account.service.IAccountService;
import cn.message.service.IMobileMessageService;
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
     */
    @RequestMapping("getDocumentationORMByNoticeKey")
    public void getDocumentationORMByNoticeKey(String noticeKey,HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(noticeKey)){
        		baseBean.setMsg("noticeKey 不能为空!");
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
    		Documentation documentation = accountService.getDocumentationByNoticeKey(noticeKey);
    		baseBean.setCode("0000");
        	baseBean.setMsg("查询成功");
        	baseBean.setData(documentation);
        	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0001");
        	baseBean.setMsg(e.getMessage());
        	baseBean.setData("查询成功");
        	logger.error(e.getMessage());
		}
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    /**
	 * 登录
	 * @param loginName 手机号或身份证
	 * @param password 密码
	 * @param request
	 * @param response
     * @throws Exception
     * http://localhost:8080/web/user/login.html?loginName=622822198502074110&password=168321
     * http://localhost:8080/web/user/login.html?loginName=440301199002101119&password=631312&openId=000000xxx&loginClient=weixin
	 */
    @RequestMapping(value="login")
    public void login(String loginName,String password,String openId,String loginClient,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(loginName)){
        		baseBean.setMsg("loginName 不能为空!");
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(password)){
        		baseBean.setMsg("password 不能为空!");
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(openId)){
        		baseBean.setMsg("openId 不能为空!");
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(loginClient)){
        		baseBean.setMsg("loginClient 不能为空!");
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
        	LoginReturnBeanVo loginReturnBeanVo = accountService.login(loginName,password,"C",openId,loginClient);
        	if(null != loginReturnBeanVo && "0000".equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode("0000");
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode("0001");
            	baseBean.setMsg(loginReturnBeanVo.getMsg());
            	baseBean.setData("");
        	}
        	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0009");
        	baseBean.setMsg(e.getMessage());
			renderJSON(baseBean);
		}
    	logger.info(JSON.toJSONString(baseBean));
    }
    /**
     * 发送短信验证码
     * @param mobilephone 用户手机号
     * @param request
     * @param response
     * http://localhost:8080/web/user/sendSMSVerificatioCode.html?mobilephone=13652311206
     */
    @RequestMapping("sendSMSVerificatioCode")
    public void sendSMSVerificatioCode(String mobilephone,HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();
    	try {
    		if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
    		//生成验证码，六位数
    		String valideteCode = StringUtil.createValidateCode();
    		String msgContent = "短信验证码："+valideteCode+"，您正在使用深圳交警微信“随手拍举报”业务，有效时间为5分钟。";
    		boolean flag = mobileMessageService.sendMessage(mobilephone, msgContent);
    		if(flag){
    			accountService.sendSMSVerificatioCode(mobilephone,valideteCode);
    			baseBean.setCode("0000");
            	baseBean.setMsg("");
            	baseBean.setData("发送成功");
    		}else{
    			baseBean.setCode("0001");
            	baseBean.setMsg("");
            	baseBean.setData("发送失败");
    		}
		} catch (Exception e) {
			baseBean.setCode("0001");
        	baseBean.setMsg(e.getMessage());
        	baseBean.setData("发送失败");
        	logger.error(e.getMessage());
		}
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
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
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(validateCode)){
        		baseBean.setMsg("validateCode 不能为空!");
        		baseBean.setCode("0001");
        		renderJSON(baseBean);
        		return;
        	}
        	// 0-验证成功，1-验证失败，2-验证码失效
    		int result = accountService.verificatioCode(mobilephone, validateCode);
    		if(0 == result){
    			baseBean.setCode("0000");
            	baseBean.setMsg("");
            	baseBean.setData("验证通过");
    		}
			if(1 == result){
				baseBean.setCode("0001");
	        	baseBean.setMsg("");
	        	baseBean.setData("验证码错误");		
			 }
			if(2 == result){
				baseBean.setCode("0002");
	        	baseBean.setMsg("");
	        	baseBean.setData("验证码失效,请重新获取");
			}
		} catch (Exception e) {
			baseBean.setCode("0001");
        	baseBean.setMsg(e.getMessage());
        	baseBean.setData("验证失败");
        	logger.error(e.getMessage());
		}
    	renderJSON(baseBean);
    	logger.info(JSON.toJSONString(baseBean));
    }
    
    /**
     * 用户中心-解绑微信
     * @param identityCard 身份证号码
     * @param openId  微信openId
     * @param unionId 支付宝unionId
     */
    @RequestMapping(value = "deleteVehicle",method = RequestMethod.POST)
    public void deleteVehicle( String identityCard,String userSource,
    		 String openId, String unionId) {
    	
    	String code="0000";
 		StringBuffer sb = new StringBuffer("下列参数有问题：");
    	UserBind userBind = new UserBind();
    	
    	userBind.setOpenId(openId);
    	if(StringUtil.isBlank(identityCard)){
 			code="500";
 			sb.append("身份证号码为空  ");
 		}else{
 			userBind.setIdCard(identityCard);
 		}
    	
    	if(StringUtil.isBlank(userSource)){
 			code="500";
 			sb.append("用户来源为空  ");
 		}else{
 			if("C".equals(userSource)){
 				userBind.setOpenId(openId);
 			}else if("Z".equals(userSource)){
 				userBind.setUnionId(unionId);
 			}
 		}
    	
    	 BaseBean basebean = new  BaseBean();
    		int re = 0;
            re = accountService.unbindVehicle(userBind); 
        	if(re==1){
        		basebean.setCode("0000");
            	basebean.setMsg("");
        	}else{
        		basebean.setCode("500");
            	basebean.setMsg("解绑出错");
        	}    
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
    }
    
    
    
    /**
     * 用户中心-添加车辆(个人和他人)
     * @param bindType 绑定类型
     * @param vehicleType 车辆类型
     * @param plateType 号牌种类
     * @param licensePlateNumber 车牌号码
     * @param frameNumber 车架号码
     * @param ownerName   车主姓名  
     * @param identityCard 身份证号
     * @param mobilephone 手机号
     */
    @RequestMapping(value = "addVehicle",method = RequestMethod.POST)
    public void addVehicle( int bindType, String vehicleType, String plateType, String licensePlateNumber
    		, String frameNumber, String ownerName, String identityCard, String mobilephone,String provinceAbbreviation) {
    	String code="0000";
 		StringBuffer sb = new StringBuffer("下列参数有问题：");   	
    	BindCarVo bindCarVo = new BindCarVo();
    	
    	if(bindType<0){
 			code="500";
 			sb.append("绑定类型错误  ");
 		}else{
 			bindCarVo.setBindType(bindType);
 		}
    	
    	if(StringUtil.isBlank(licensePlateNumber)){
 			code="500";
 			sb.append("车牌号码为空  ");
 		}else{
 			bindCarVo.setLicensePlateType(licensePlateNumber);
 		}
    	
    	if(StringUtil.isBlank(frameNumber)){
 			code="500";
 			sb.append("车架号码为空  ");
 		}else{
 			bindCarVo.setFrameNumber(frameNumber);
 		}

    	if(StringUtil.isBlank(identityCard)){
 			code="500";
 			sb.append("身份证号为空  ");
 		}else{
 			bindCarVo.setUserIdCard(identityCard);
 		}
    	
    	if(StringUtil.isBlank(plateType)){
 			code="500";
 			sb.append("号牌种类为空  ");
 		}else{
 			bindCarVo.setLicensePlateType(plateType);
 		}
    	
    	if(StringUtil.isBlank(ownerName)){
 			code="500";
 			sb.append("车主姓名为空  ");
 		}else{
 			bindCarVo.setOwnerName(ownerName);
 		}
    	BaseBean basebean = new  BaseBean();
    	try {
    		
    		if("0000".equals(code)){//参数校验通过
    			bindCarVo.setUserSource("C");
            	bindCarVo.setProvinceAbbreviation("粤");
            	bindCarVo.setInputIP("127.0.0.1");
        		
        		JSONObject json = accountService.addVehicle(bindCarVo);
    			System.out.println(json);
    			code =json.getString("CODE");
    			if(!"0000".equals(code)){
    				code="500";
    			}
    	    	basebean.setCode(code);
    	    	basebean.setMsg(json.getString("MSG"));   
    		}else{
    			basebean.setMsg(sb.toString());
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
   
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
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
    	String code="0000";
 		StringBuffer sb = new StringBuffer("下列参数有问题：");
    	UserBasicVo userBasicVo =new UserBasicVo();
    	if(StringUtil.isBlank(identityCard)){
 			code="500";
 			sb.append("身份证为空  ");
 		}else{
 			userBasicVo.setIdentityCard(identityCard);;
 		}
    	if(StringUtil.isBlank(mailingAddress)){
 			code="500";
 			sb.append("通讯地址为空  ");
 		}else{
 			userBasicVo.setMailingAddress(mailingAddress);
 		}
    	if(StringUtil.isBlank(mailingAddress)){
 			code="500";
 			sb.append("通讯地址为空  ");
 		}else{
 			userBasicVo.setMailingAddress(mailingAddress);
 		}
    	if(StringUtil.isBlank(idCardImgPositive)){
 			code="500";
 			sb.append("身份证正面为空  ");
 		}else{
 			userBasicVo.setIdCardImgPositive(idCardImgPositive);
 		}
 		
 		if(StringUtil.isBlank(idCardImgNegative)){
 			code="500";
 			sb.append("身份证反面为空 ");
 		}
 		
 		if(StringUtil.isBlank(idCardImgHandHeld)){
 			code="500";
 			sb.append("手持身份证为空  ");
 		}else{
 			userBasicVo.setIdCardImgHandHeld(idCardImgHandHeld);
 		}

    
    	BaseBean basebean = new  BaseBean();
    	try {  	
    		if("0000".equals(code)){//参数校验通过
    			userBasicVo.setUserSource("C");
            	userBasicVo.setIdCardValidityDate("2018-04-15");
            	userBasicVo.setNickname(tureName);
            	//logger.debug(userBasicVo.toString());
    			JSONObject json = accountService.updateUser(userBasicVo);   
    			//logger.debug(json.toString());
    			System.out.println(json);
    			code =json.getString("CODE");
    			if(!"0000".equals(code)){
    				code="500";
    			}
    	    	basebean.setCode(code);
    	    	basebean.setMsg(json.getString("MSG"));
    		}else{
    			basebean.setMsg(sb.toString());
    		}  		
		} catch (Exception e) {	
			e.printStackTrace();
		}   	
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
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
    	
    	String code="0000";
 		StringBuffer sb = new StringBuffer("下列参数有问题：");
    	UserBasicVo userBasicVo =new UserBasicVo();
    	if(StringUtil.isBlank(identityCard)){
 			code="500";
 			sb.append("身份证为空  ");
 		}else{
 			userBasicVo.setIdentityCard(identityCard);;
 		}
    	
    	if(StringUtil.isBlank(oldMobile)){
 			code="500";
 			sb.append("旧手机号为空  ");
 		}else{
 			userBasicVo.setOldMobile(oldMobile);
 		}
    	if(StringUtil.isBlank(newMobile)){
 			code="500";
 			sb.append("新手机号为空  ");
 		}else{
 			userBasicVo.setNewMobile(newMobile);
 		}
    	if(StringUtil.isBlank(newMobile)){
 			code="500";
 			sb.append("验证码为空  ");
 		}
    	
    	BaseBean basebean = new  BaseBean();
    	try {
    		if("0000".equals(code)){//参数校验通过    			
    			// 0-验证成功，1-验证失败，2-验证码失效
        		int result = accountService.verificatioCode(oldMobile, validateCode);
        		if(0 == result){
        			userBasicVo.setUserSource("C");
        			JSONObject json = accountService.updateMobile(userBasicVo);
        			System.out.println(json);
        			code =json.getString("CODE");
        			if(!"0000".equals(code)){
        				code="500";
        			}
        			basebean.setCode(code);
        	    	basebean.setMsg(json.getString("MSG"));
        		}
    			if(1 == result){
    				code="500";	
    				sb.append("验证码错误    ");
    				basebean.setMsg(sb.toString());
    			 }
    			if(2 == result){
    				code="500";	
    				sb.append("验证码错误    ");
    				basebean.setMsg(sb.toString());
    			}   	    	
    		}else{
    			basebean.setMsg(sb.toString());
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}  	
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
    }
    
    
    
   /**
    * 我的资料-修改密码
    * @param oldPwd
    * @param newPwd
    */
    @RequestMapping(value = "updatePwd",method = RequestMethod.POST)
    public void updatePwd( String oldPwd, String newPwd,String identityCard) {
    	String code="0000";
 		StringBuffer sb = new StringBuffer("下列参数有问题：");
    	
    	UserBasicVo userBasicVo =new UserBasicVo();
    	if(StringUtil.isBlank(identityCard)){
 			code="500";
 			sb.append("身份证为空  ");
 		}else{
 			userBasicVo.setIdentityCard(identityCard);;
 		}
    	if(StringUtil.isBlank(oldPwd)){
 			code="500";
 			sb.append("旧密码为空  ");
 		}else{
 			userBasicVo.setOldPwd(oldPwd);
 		}
    	if(StringUtil.isBlank(newPwd)){
 			code="500";
 			sb.append("新密码为空  ");
 		}else{
 			userBasicVo.setNewPwd(newPwd);
 		}
    	
    	
    	
    	BaseBean basebean = new  BaseBean();
    	try {
    		if("0000".equals(code)){//参数校验通过
    			userBasicVo.setUserSource("C");
    			JSONObject json = accountService.updatePwd(userBasicVo);
    			System.out.println(json);
    			code =json.getString("CODE");
    			if(!"0000".equals(code)){
    				code="500";
    			}
    	    	basebean.setCode(code);
    	    	basebean.setMsg(json.getString("MSG"));
    		}else{
    			basebean.setMsg(sb.toString());
    		}   		
		} catch (Exception e) {
			e.printStackTrace();
		}  	
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));	
    
    }

    
    /**
     * 
     * @Title: readilyShoot 
     * @author liuminkang
     * @Description: TODO(星级用户-随手拍) 
     * @param licensePlateNumber
     * @param licensePlateType
     * @param illegalActivitieOne
     * @param illegalTime
     * @param illegalSections
     * @param reportImgOne
     * @param reportImgTwo
     * @param reportImgThree
     * @param inputMan
     * @param inputManName
     * @param inputManPhone
     * @param identityCard
     * @param userSource
     * @param openId    设定文件 
     * @return void    返回类型 
     * @date 2017年4月20日 下午3:06:02
     */
    @RequestMapping(value = "readilyShoot",method = RequestMethod.POST)
    public void readilyShoot(String licensePlateNumber,String licensePlateType,String illegalActivitieOne, String illegalTime, String illegalSections, String reportImgOne, String reportImgTwo
    		, String reportImgThree, String inputMan,String inputManName,String inputManPhone,String identityCard,String userSource,String openId) {
    	String code="0000";
 		StringBuffer sb = new StringBuffer("");
 		int imgNumber=0;//传入的图片数量
    	ReadilyShootVo readilyShootVo = new ReadilyShootVo();
    	

 		readilyShootVo.setLicensePlateNumber(licensePlateNumber);

 		readilyShootVo.setLicensePlateType(licensePlateType);

    	if(StringUtil.isBlank(illegalActivitieOne)){
 			code="500";
 			sb.append("违法行为为空  ");
 		}else{
 			readilyShootVo.setIllegalActivitieOne(illegalActivitieOne);
 		}
    	
    	if(StringUtil.isBlank(identityCard)){
 			code="500";
 			sb.append("身份证为空  ");
 		}else{
 			readilyShootVo.setUserIdCard(identityCard);
 		}	
    	if(StringUtil.isBlank(illegalTime)){
 			code="500";
 			sb.append("违法时间为空  ");
 		}else{
 			readilyShootVo.setIllegalTime(illegalTime);
 		}
    	if(StringUtil.isBlank(illegalSections)){
 			code="500";
 			sb.append("违法地点为空  ");
 		}else{
 			readilyShootVo.setIllegalSections(illegalSections);;
 		}
    	
    	if(StringUtil.isBlank(inputManPhone)){
 			code="500";
 			sb.append("手机号码为空  ");
 		}else{
 			readilyShootVo.setInputManPhone(inputManPhone);
 		}
    	

 		readilyShootVo.setInputMan(inputMan);

    	
    	if(StringUtil.isBlank(inputManName)){
 			code="500";
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
    		code="500";
 			sb.append("举报图片数量不够  ");
    	}
    	
    	if(StringUtil.isBlank(userSource)){
 			code="500";
 			sb.append("用户来源为空  ");
 		}else{
 			readilyShootVo.setUserSource(userSource);
 		}
    	
    	if(StringUtil.isBlank(openId)){
 			code="500";
 			sb.append("微信openid为空  ");
 		}else{
 			readilyShootVo.setOpenId(openId);
 		}

       	BaseBean basebean = new  BaseBean();
    	try {
    		 if("0000".equals(code)){//参数校验通过
    			 JSONObject json = accountService.readilyShoot(readilyShootVo);
    				System.out.println(json);
    				code =json.getString("code");
    				if(!"0000".equals(code)){
    					code="500";
    				}else{
    					Map<String, Object> modelMap = new HashMap<String, Object>();
    					String msg=json.getString("msg");
    			     	modelMap.put("recordNumber", msg.substring(5, 19));
    			     	modelMap.put("queryPassword", json.getString("cxyzm"));
    			     	basebean.setData(modelMap);
    				}
    		    	basebean.setCode(code);
    		    	basebean.setMsg(json.getString("msg"));
    		 }else{
    			 basebean.setMsg(sb.toString());
    		 }
			
    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
    
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
    }
    
    @RequestMapping(value = "getPositioningAddress",method = RequestMethod.POST)
    public void getPositioningAddress(String keyword) throws Exception {
		JSONObject json= null;
		
		BaseBean basebean = new  BaseBean();
		try {
			 json  = accountService.getPositioningAddress(keyword);
			 System.out.println(json);
			 String code =json.getString("code");
			 if("0000".equals(code)){
				 basebean.setCode(code);
				 basebean.setMsg(json.getString("msg"));
				 basebean.setData(json.get("body"));
			 }
		} catch (Exception e) {
			logger.error("getPositioningAddress出错，错误="+ keyword,e);
		}
		renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));

	}
    
}
