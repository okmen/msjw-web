package cn.web.front.action.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.UserBind;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.LoginReturnBeanVo;
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
    public void deleteVehicle(@RequestParam("identityCard") String identityCard,
    		@RequestParam("openId") String openId,@RequestParam("unionId") String unionId) {
    	
    	
    	UserBind userBind = new UserBind();
    	userBind.setIdCard(identityCard);
    	userBind.setOpenId(openId);
    	
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
    public void addVehicle(@RequestParam("bindType") int bindType,@RequestParam("vehicleType") String vehicleType,
    		@RequestParam("plateType") String plateType,@RequestParam("licensePlateNumber") String licensePlateNumber
    		,@RequestParam("frameNumber") String frameNumber,@RequestParam("ownerName") String ownerName
    		,@RequestParam("identityCard") String identityCard,@RequestParam("mobilephone") String mobilephone) {
    	
    	
    	BindCarVo bindCarVo = new BindCarVo();
    	bindCarVo.setBindType(bindType);
    	bindCarVo.setLicensePlateType(licensePlateNumber);
    	bindCarVo.setFrameNumber(frameNumber);
    	bindCarVo.setUserIdCard(identityCard);
    	bindCarVo.setUserSource("C");
    	bindCarVo.setLicensePlateType(plateType);
    	bindCarVo.setProvinceAbbreviation("粤");
    	bindCarVo.setLicensePlateNumber(licensePlateNumber);
    	bindCarVo.setOwnerName(ownerName);
    	bindCarVo.setBindType(bindType);
    	bindCarVo.setInputIP("127.0.0.1");
    	try {
    		BaseBean basebean = new  BaseBean();
    		JSONObject json = accountService.addVehicle(bindCarVo);
			System.out.println(json);
			String code =json.getString("CODE");
			if(!"0000".equals(code)){
				code="500";
			}
	    	basebean.setCode(code);
	    	basebean.setMsg(json.getString("MSG"));   	
	    	renderJSON(basebean);
	    	logger.info(JSON.toJSONString(basebean));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
    public void updateUser(@RequestParam("tureName") String tureName,@RequestParam("identityCard") String identityCard
    		,@RequestParam("mailingAddress") String mailingAddress,@RequestParam("idCardImgPositive") String idCardImgPositive
    		,@RequestParam("idCardImgNegative") String idCardImgNegative,@RequestParam("IdCardImgHandHeld") String IdCardImgHandHeld) {
    	
    	UserBasicVo userBasicVo =new UserBasicVo();
    	userBasicVo.setIdentityCard(identityCard);
    	userBasicVo.setMailingAddress(mailingAddress);
    	userBasicVo.setIdCardImgPositive(idCardImgPositive);
    	userBasicVo.setIdCardImgHandHeld(IdCardImgHandHeld);
    	userBasicVo.setUserSource("C");
    	userBasicVo.setIdCardValidityDate("2018-04-15");
    	BaseBean basebean = new  BaseBean();
    	try {
    	
			JSONObject json = accountService.updateUser(userBasicVo);
			System.out.println(json);
			String code =json.getString("CODE");
			if(!"0000".equals(code)){
				code="500";
			}
	    	basebean.setCode(code);
	    	basebean.setMsg(json.getString("MSG"));   		
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
    public void updateMobile(@RequestParam("oldMobile") String oldMobile,@RequestParam("validateCode") String validateCode
    		,@RequestParam("newMobile") String newMobile) {
    	
    	UserBasicVo userBasicVo =new UserBasicVo();
    	userBasicVo.setIdentityCard("360924199006071671");
    	userBasicVo.setOldMobile(oldMobile);
    	userBasicVo.setNewMobile(newMobile);
    	userBasicVo.setUserSource("C");
    	BaseBean basebean = new  BaseBean();
    	try {
			JSONObject json = accountService.updateUser(userBasicVo);
			System.out.println(json);
			String code =json.getString("CODE");
			if(!"0000".equals(code)){
				code="500";
			}
	    	basebean.setCode(code);
	    	basebean.setMsg(json.getString("MSG"));
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
    public void updatePwd(@RequestParam("oldPwd") String oldPwd,@RequestParam("newPwd") String newPwd) {
    	UserBasicVo userBasicVo =new UserBasicVo();
    	userBasicVo.setIdentityCard("360924199006071671");
    	userBasicVo.setOldPwd(oldPwd);
    	userBasicVo.setNewPwd(newPwd);
    	userBasicVo.setUserSource("C");
    	BaseBean basebean = new  BaseBean();
    	try {
			JSONObject json = accountService.updateUser(userBasicVo);
			System.out.println(json);
			String code =json.getString("CODE");
			if(!"0000".equals(code)){
				code="500";
			}
	    	basebean.setCode(code);
	    	basebean.setMsg(json.getString("MSG"));
		} catch (Exception e) {
			e.printStackTrace();
		}  	
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));	
    
    }

    
    /**
     * 随手拍举报-星级用户/普通用户
     * @param illegalTime 违法时间
     * @param illegalSections 违法路段
     * @param imgOne 证据材料图片1
     * @param imgTwo 证据材料图片2
     * @param imgThree 证据材料图片3
     * @param situationStatement 情况说明
     * @param whistleblower 举报人
     * @param identityCard 身份证
     * @param mobilephone 联系电话
     * @param locationAddress 定位地址
     */
    @RequestMapping(value = "readilyShoot",method = RequestMethod.POST)
    public void readilyShoot(@RequestParam("illegalTime") String illegalTime,@RequestParam("illegalSections") String illegalSections    		
    		,@RequestParam("imgOne") String imgOne,@RequestParam("imgTwo") String imgTwo
    		,@RequestParam("imgThree") String imgThree,@RequestParam("situationStatement") String situationStatement
    		,@RequestParam("whistleblower") String whistleblower,@RequestParam("identityCard") String identityCard
    		,@RequestParam("mobilephone") String mobilephone,@RequestParam("locationAddress") String locationAddress) {
    	
    	
       	BaseBean basebean = new  BaseBean();
    	try {
			JSONObject json = accountService.readilyShoot(illegalTime, illegalSections, imgOne, situationStatement, whistleblower, identityCard, mobilephone);
			System.out.println(json);
			String code =json.getString("CODE");
			if(!"0000".equals(code)){
				code="500";
			}
	    	basebean.setCode(code);
	    	basebean.setMsg(json.getString("MSG"));
    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
//    	Map<String, Object> modelMap = new HashMap<String, Object>();
//     	modelMap.put("recordNumber", "000000000");
//     	modelMap.put("queryPassword", "123456");
//     	basebean.setData(modelMap);
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
    }
    
}
