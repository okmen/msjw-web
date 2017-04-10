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
    
    
    /**
     * 用户中心-解绑微信
     * @param identityCard 身份证号码
     * @param openId  微信openId
     * @param unionId 支付宝unionId
     */
    @RequestMapping(value = "deleteVehicle",method = RequestMethod.POST)
    public void deleteVehicle(@RequestParam("identityCard") String identityCard,
    		@RequestParam("openId") String openId,@RequestParam("unionId") String unionId) {
    	
    	
    	
    	BaseBean basebean = new  BaseBean();
    	basebean.setCode("0000");
    	basebean.setMsg("");   	    
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
    	
    	
    	
    	
    	
    	BaseBean basebean = new  BaseBean();
    	basebean.setCode("0000");
    	basebean.setMsg("");   	
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
    public void updateUser(@RequestParam("tureName") String tureName,@RequestParam("identityCard") String identityCard
    		,@RequestParam("mailingAddress") String mailingAddress,@RequestParam("idCardImgPositive") String idCardImgPositive
    		,@RequestParam("idCardImgNegative") String idCardImgNegative,@RequestParam("IdCardImgHandHeld") String IdCardImgHandHeld) {
    	
    	
    	
    	
    	
    	BaseBean basebean = new  BaseBean();
    	basebean.setCode("0000");
    	basebean.setMsg("");   	
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
    	
    	
    	
    	
    	
    	BaseBean basebean = new  BaseBean();
    	basebean.setCode("0000");
    	basebean.setMsg("");   	
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
    	

    	BaseBean basebean = new  BaseBean();
    	basebean.setCode("0000");
    	basebean.setMsg("");   	
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
    }

    
    /**
     * 随手拍举报-星级用户/普通用户
     * @param illegalTime 违法时间
     * @param illegalSections 违法路段
     * @param idCardImgPositive 身份证正面
     * @param idCardImgNegative 身份证反面
     * @param IdCardImgHandHeld 手持身份证
     * @param situationStatement 情况说明
     * @param whistleblower 举报人
     * @param identityCard 身份证
     * @param mobilephone 联系电话
     * @param locationAddress 定位地址
     */
    @RequestMapping(value = "readilyShoot",method = RequestMethod.POST)
    public void readilyShoot(@RequestParam("illegalTime") String illegalTime,@RequestParam("illegalSections") String illegalSections    		
    		,@RequestParam("idCardImgPositive") String idCardImgPositive,@RequestParam("idCardImgNegative") String idCardImgNegative
    		,@RequestParam("IdCardImgHandHeld") String IdCardImgHandHeld,@RequestParam("situationStatement") String situationStatement
    		,@RequestParam("whistleblower") String whistleblower,@RequestParam("identityCard") String identityCard
    		,@RequestParam("mobilephone") String mobilephone,@RequestParam("locationAddress") String locationAddress) {
    	
    	
    	
    	
    	
    	BaseBean basebean = new  BaseBean();
    	basebean.setCode("0000");
    	basebean.setMsg("");   	
    	Map<String, Object> modelMap = new HashMap<String, Object>();
     	modelMap.put("recordNumber", "000000000");
     	modelMap.put("queryPassword", "123456");
     	basebean.setData(modelMap);
    	renderJSON(basebean);
    	logger.info(JSON.toJSONString(basebean));
    
    }
    
}
