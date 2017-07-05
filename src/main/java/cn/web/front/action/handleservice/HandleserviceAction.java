package cn.web.front.action.handleservice;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import cn.convenience.bean.ConvenienceBean;
import cn.handle.bean.vo.ApplyCarTemporaryLicenceVo;
import cn.handle.bean.vo.ApplyGatePassVo;
import cn.handle.bean.vo.DriverChangeContactVo;
import cn.handle.bean.vo.DriverLicenseAnnualVerificationVo;
import cn.handle.bean.vo.DriverLicenseIntoVo;
import cn.handle.bean.vo.DriverLicenseVoluntaryDemotionVo;
import cn.handle.bean.vo.IocomotiveCarChangeContactVo;
import cn.handle.bean.vo.IocomotiveCarReplaceVo;
import cn.handle.bean.vo.RenewalDriverLicenseVo;
import cn.handle.bean.vo.RepairOrReplaceDriverLicenseVo;
import cn.handle.bean.vo.VehicleDrivingLicenseVo;
import cn.handle.service.IHandleService;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.WebServiceException;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;
import net.sf.json.JSONObject;

/**
 * 办理类Action
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value="/handleservice/")
@SuppressWarnings(value="all")
public class HandleserviceAction extends BaseAction {
	@Autowired
    @Qualifier("handleService")
    private IHandleService handleService;
	/**
	 * 补领机动车行驶证
	 * @param name 车主姓名/机动车所有人
	 * @param IDcard 证件号码
	 * @param licensePlate 号牌号码
	 * @param licensePlateTpye 号牌种类
	 * @param placeOfDomicile 户籍所在地 1深户，0外籍户口
	 * @param address 住所详细地址
	 * @param receiverName 收件人姓名
	 * @param receiverNumber 收件人号码
	 * @param mailingAddress 收件人地址
	 * @param livePhoto1 居住证正面
	 * @param livePhoto2 居住证反面
	 * @param IDCardPhoto1 身份证（正面）
	 * @param IDCardPhoto2 身份证（反面）
	 * @param driverLicensePhoto 机动车登记证书
	 * @param ip ip
	 * @param sourceOfCertification 申请来源
	 * @param foreignPeopleLivingOnTheTable 境外人员临住表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping("complementTheMotorVehicleDrivingLicense")
    public void complementTheMotorVehicleDrivingLicense(String name,String IDcard,String licensePlate,String licensePlateTpye,
    		String placeOfDomicile,String address,String receiverName,String receiverNumber,String mailingAddress,
    		String livePhoto1,String livePhoto2,String IDCardPhoto1, String IDCardPhoto2, String driverLicensePhoto,
    		String ip,String sourceOfCertification,String foreignPeopleLivingOnTheTable, 
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	VehicleDrivingLicenseVo vehicleDrivingLicenseVo = new VehicleDrivingLicenseVo();
    	try {
    		if(StringUtils.isBlank(name)){
        		baseBean.setMsg("name不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setName(name);
        	}
    		if(StringUtils.isBlank(IDcard)){
        		baseBean.setMsg("IDcard不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIDcard(IDcard);
        	}
    		if(StringUtils.isBlank(licensePlate)){
        		baseBean.setMsg("licensePlate不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLicensePlate(licensePlate);
        	}
    		if(StringUtils.isBlank(licensePlateTpye)){
        		baseBean.setMsg("licensePlateTpye不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLicensePlateTpye(licensePlateTpye);
        	}
    		if(StringUtils.isBlank(placeOfDomicile)){
        		baseBean.setMsg("placeOfDomicile不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setPlaceOfDomicile(placeOfDomicile);
        	}
    		/*if(StringUtils.isBlank(address)){
        		baseBean.setMsg("address不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setAddress(address);
        	}*/
    		if(StringUtils.isBlank(receiverName)){
        		baseBean.setMsg("receiverName不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setReceiverName(receiverName);
        	}
    		if(StringUtils.isBlank(receiverNumber)){
        		baseBean.setMsg("receiverNumber不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setReceiverNumber(receiverNumber);
        	}
    		
    		if(StringUtils.isBlank(mailingAddress)){
        		baseBean.setMsg("mailingAddress不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setMailingAddress(mailingAddress);
        	}
    		/*if(StringUtils.isBlank(livePhoto1)){
        		baseBean.setMsg("livePhoto1不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLivePhoto1(livePhoto1);
        	}
    		if(StringUtils.isBlank(livePhoto2)){
        		baseBean.setMsg("livePhoto2不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setLivePhoto2(livePhoto2);
        	}*/
    		if(StringUtils.isBlank(IDCardPhoto1)){
        		baseBean.setMsg("IDCardPhoto1不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIDCardPhoto1(IDCardPhoto1);
        	}
    		if(StringUtils.isBlank(IDCardPhoto2)){
        		baseBean.setMsg("IDCardPhoto2不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIDCardPhoto2(IDCardPhoto2);
        	}
    		
    		if(StringUtils.isBlank(driverLicensePhoto)){
        		baseBean.setMsg("driverLicensePhoto不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setDriverLicensePhoto(driverLicensePhoto);
        	}
    		/*if(StringUtils.isBlank(ip)){
        		baseBean.setMsg("ip不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setIp(ip);
        	}*/
    		ip = getIp2(request);
    		if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setSourceOfCertification(sourceOfCertification);
        	}
    		/*if(StringUtils.isBlank(foreignPeopleLivingOnTheTable)){
        		baseBean.setMsg("foreignPeopleLivingOnTheTable不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}else{
        		vehicleDrivingLicenseVo.setForeignPeopleLivingOnTheTable(foreignPeopleLivingOnTheTable);
        	}*/
    		Map<String, Object> map = handleService.complementTheMotorVehicleDrivingLicense(vehicleDrivingLicenseVo);
    		String code = map.get("code").toString();
			String msg = map.get("msg").toString();
			if("0000".equals(code)){
        		baseBean.setCode(MsgCode.success);
        		baseBean.setMsg(msg);
        		//成功需要发送模板消息
        	}else{
        		baseBean.setCode(MsgCode.businessError);
        		baseBean.setMsg(msg);
        	}
        	renderJSON(baseBean);
		} catch (Exception e) {
			DealException(baseBean, e);
        	logger.error("complementTheMotorVehicleDrivingLicense 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    /**
     * 驾驶证年审 
     * @param identificationNO 身份证明类型
     * @param name 姓名                        
	 * @param IDcard 身份证号                       
	 * @param mobilephone 电话                    
	 * @param placeOfDomicile 户籍所在地             
	 * @param receiverName 收件人姓名                
	 * @param receiverNumber 收件人号码              
	 * @param mailingAddress 收件人地址             
	 * @param IDCardPhoto1 身份证正面照片              
	 * @param IDCardPhoto2 身份证反面照片              
	 * @param livePhoto1 居住证正面照片                
	 * @param livePhoto2 居住证反面照片                
	 * @param educationDrawingtable 审验教育培训表[图片] 
	 * @param foreignersLiveTable 境外人员临住表 
	 * @param postalcode 邮编号码       
	 * @param loginUser 认证用户身份证号码               
	 * @param sourceOfCertification 认证来源        
	 * @param userSource 申报途径（A移动APP C微信Z支付宝E邮政）
	 * @param http://192.168.1.245:8080/web/user/driverLicenseAnnualVerification.html?identificationNO=A&name=张宇帆&IDcard=445222199209020034&mobilephone=15920050177&placeOfDomicile=深圳&receiverName=11&receiverNumber=15920050177&mailingAddress=深圳市宝安区&IDCardPhoto1=111&IDCardPhoto2=222&livePhoto1=111&livePhoto2=222 &educationDrawingtable=111&foreignersLiveTable=222&postalcode=1&loginUser=445222199209020034&sourceOfCertification=C&userSource=C
	 * 
     */
    @RequestMapping("driverLicenseAnnualVerification")
    public void driverLicenseAnnualVerification(String identificationNO ,String name ,String IDcard ,String mobilephone ,String placeOfDomicile ,String receiverName ,String receiverNumber ,String  mailingAddress ,String IDCardPhoto1 ,String IDCardPhoto2 ,String livePhoto1 ,String livePhoto2 ,String educationDrawingtable ,String foreignersLiveTable ,String postalcode ,String loginUser ,String sourceOfCertification ,String userSource,HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
		DriverLicenseAnnualVerificationVo driverLicenseAnnualVerificationVo = new DriverLicenseAnnualVerificationVo();
		String businessType = "N";
		driverLicenseAnnualVerificationVo.setBusinessType(businessType);
		try {
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型!不能为空");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setIdentificationNO(identificationNO);
			}
			
			driverLicenseAnnualVerificationVo.setPostalcode(postalcode);
			
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setName(name);
			}
			
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setIDcard(IDcard);
			}
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setMobilephone(mobilephone);
			}
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setPlaceOfDomicile(placeOfDomicile);
				if ("3".equals(placeOfDomicile)) {
					if(StringUtil.isBlank(foreignersLiveTable)){
						baseBean.setCode(MsgCode.paramsError);
						baseBean.setMsg("境外人员临住表不能为空!");
						renderJSON(baseBean);
						return;
					}else{
						driverLicenseAnnualVerificationVo.setForeignersLiveTable(foreignersLiveTable);
					}
				}
				
			}
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setReceiverName(receiverName);
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setReceiverNumber(receiverNumber);
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setMailingAddress(mailingAddress);
			}
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setIDCardPhoto1(IDCardPhoto1);
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setIDCardPhoto2(IDCardPhoto2);
			}
			
			if(StringUtil.isBlank(livePhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setLivePhoto1(livePhoto1);
			}
			if(StringUtil.isBlank(livePhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setLivePhoto2(livePhoto2);
			}
			if(StringUtil.isBlank(educationDrawingtable)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("教育审核表不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setEducationDrawingtable(educationDrawingtable);
			}
			
			
			if(StringUtil.isBlank(loginUser)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("登录用户不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setLoginUser(loginUser);
			}
			
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setSourceOfCertification(sourceOfCertification);
			}
			if(StringUtil.isBlank(userSource)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("userSource 错误!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseAnnualVerificationVo.setUserSource(userSource);
			}
			String ip = getIp2(request);
			driverLicenseAnnualVerificationVo.setIp(ip);
			Map<String, String> map = handleService.driverLicenseAnnualVerification(driverLicenseAnnualVerificationVo);
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
			logger.error("驾驶证年审异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    /**
     * 驾驶证延期换证 
     * @param  name 姓名                  
	 * @param  identificationNO 身份证明类型  
	 * @param  IDcard  身份证号     
	 * @param  driverLicense 驾驶证号         
	 * @param  fileNumber 档案编号          
	 * @param  delayDate 延期日期           
	 * @param  delayReason 延期原因         
	 * @param  sourceOfCertification 来源标志
	 * @param  loginUser 登录账户           
	 * @param  IDCardPhoto1 身份证正面照片     
	 * @param  IDCardPhoto2 身份证反面照片     
	 * @param  driverLicensePhoto 驾驶证照片 
	 * @param  delayPhoto 延期证明照片
	 * @param receiverName 收件人姓名         
     * @param receiverNumber 收件人号码       
     * @param mailingAddress 收件人地址       
	 * @param http://192.168.1.245:8080/web/user/renewalDriverLicense.html?name=张宇帆&identificationNO=A&IDcard=445222199209020034&driverLicense=445222199209020034&fileNumber=123456&delayDate=20170701&delayReason=gg&sourceOfCertification=C&loginUser=445222199209020034&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111&delayphoto=111&receiverName=张宇帆&receiverNumber=15920050177&mailingAddress=深圳市宝安区
	 *         
     */
    @RequestMapping("renewalDriverLicense")
    public void renewalDriverLicense(String name ,String identificationNO ,String IDcard ,String driverLicense ,String fileNumber ,String delayDate ,String delayReason,String sourceOfCertification,String loginUser ,String IDCardPhoto1,String IDCardPhoto2,String driverLicensePhoto,String delayphoto ,String receiverName ,String receiverNumber ,String  mailingAddress,HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	RenewalDriverLicenseVo renewalDriverLicenseVo = new RenewalDriverLicenseVo();
    	String businessType = "Y";
    	renewalDriverLicenseVo.setBusinessType(businessType);
		try {
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setName(name);
			}
			
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型 错误!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setIdentificationNO(identificationNO);
			}
			
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setIDcard(IDcard);
			}
			
			if(StringUtil.isBlank(driverLicense)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setDriverLicense(driverLicense);
			}
			if(StringUtil.isBlank(fileNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("档案编号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setFileNumber(fileNumber);
			}
			if(StringUtil.isBlank(delayDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("延期日期不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setDelayDate(delayDate);
			}
			if(StringUtil.isBlank(delayReason)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("延期原因不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setDelayReason(delayReason);
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setSourceOfCertification(sourceOfCertification);
			}
			
			if(StringUtil.isBlank(loginUser)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setLoginUser(loginUser);
			}
			
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setIDCardPhoto1(IDCardPhoto1);
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setIDCardPhoto2(IDCardPhoto2);
			}
			
			if(StringUtil.isBlank(driverLicensePhoto)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setDriverLicensePhoto(driverLicensePhoto);
			}
			
			if(StringUtil.isBlank(delayphoto)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("延期证明照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setDelayPhoto(delayphoto);
			}
			
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setReceiverName(receiverName);
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setReceiverNumber(receiverNumber);
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				renewalDriverLicenseVo.setMailingAddress(mailingAddress);
			}
			
			String ip = getIp2(request);
			renewalDriverLicenseVo.setIp(ip);
			Map<String, String> map = handleService.renewalDriverLicense(renewalDriverLicenseVo);
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
			logger.error("驾驶证延期领证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    

    /**
     * 驾驶证转入
     * @param  name 姓名                  
	 * @param  identificationNO 身份证明类型  
	 * @param  IDcard  身份证号  
	 * @param  driverLicense  驾驶证号             
	 * @param  fileNumber 档案编号          
	 * @param  issuingLicenceAuthority 发证机关           
	 * @param  photoReturnNumberString 相片回执编号        
	 * @param  receiverName 收件人姓名         
	 * @param  receiverNumber 收件人号码       
	 * @param  mailingAddress 收件人地址       
	 * @param  sourceOfCertification 来源标志
	 * @param  loginUser 登录账户           
	 * @param  IDCardPhoto1 身份证正面照片     
	 * @param  IDCardPhoto2 身份证反面照片     
	 * @param  driverLicensePhoto 驾驶证照片 
	 * @param  bodyConditionForm 身体条件申请表
	 * @param http://192.168.1.245:8080/web/user/driverLicenseInto.html?name=张宇帆&identificationNO=A&IDcard=445222199209020034&driverLicense=445222199209020034&fileNumber=123456&issuingLicenceAuthority=藏A:拉萨市公安局&photoReturnNumberString=111&receiverName=张宇帆&receiverNumber=15920050177&mailingAddress=深圳市宝安区&sourceOfCertification=C&loginUser=445222199209020034&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111&bodyConditionForm=222    
         
	 *         
     */
    @RequestMapping("driverLicenseInto")
    public void driverLicenseInto( String name ,String identificationNO ,String IDcard ,String driverLicense ,String fileNumber ,String  issuingLicenceAuthority ,String photoReturnNumberString ,String receiverName , String receiverNumber , String mailingAddress ,String sourceOfCertification ,String loginUser ,String IDCardPhoto1,String IDCardPhoto2,String driverLicensePhoto,String bodyConditionForm,HttpServletRequest request,HttpServletResponse response){
    	
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	DriverLicenseIntoVo driverLicenseIntoVo = new DriverLicenseIntoVo();
    	String businessType = "Z";
    	driverLicenseIntoVo.setBusinessType(businessType);
		try {
			

			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setName(name);
			}
			
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setIdentificationNO(identificationNO);
			}
			
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setIDcard(IDcard);
			}
			
			if(StringUtil.isBlank(driverLicense)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setDriverLicense(driverLicense);
			}
			
			if(StringUtil.isBlank(fileNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("档案编号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setFileNumber(fileNumber);
			}
			if(StringUtil.isBlank(issuingLicenceAuthority)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("发证机关不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setIssuingLicenceAuthority(issuingLicenceAuthority);
			}
			if(StringUtil.isBlank(photoReturnNumberString)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setReceiverName(receiverName);
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setReceiverNumber(receiverNumber);
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setMailingAddress(mailingAddress);
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setSourceOfCertification(sourceOfCertification);
			}
			
			if(StringUtil.isBlank(loginUser)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("登录用户不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setLoginUser(loginUser);
			}
			
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setIDCardPhoto1(IDCardPhoto1);
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setIDCardPhoto2(IDCardPhoto2);
			}
			
			if(StringUtil.isBlank(driverLicensePhoto)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setDriverLicensePhoto(driverLicensePhoto);
			}
			
			if(StringUtil.isBlank(bodyConditionForm)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身体条件申请表不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseIntoVo.setBodyConditionForm(bodyConditionForm);
			}
			
			
			String ip = getIp2(request);
			driverLicenseIntoVo.setIp(ip);
			Map<String, String> map = handleService.driverLicenseInto(driverLicenseIntoVo);
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
			logger.error("驾驶证转入异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }                                                                          
                                                                         
                                                                                   
    /**                                                                                
     * 驾驶证自愿降级                                                          
     * @param   identificationNO;//身份证明名称                              
	 * @param   loginUser;//认证用户身份证号                      
	 * @param   IDcard;//申请人身份证号 
	 * @param   driverLicense;//驾驶证号码                             
	 * @param   name;//申请人姓名                                          
	 * @param   photoReturnNumberString;//相片回执号                            
	 * @param   placeOfDomicile;//户籍所在地                                   
	 * @param   receiverName;//收件人姓名                                  
	 * @param   receiverNumber;//收件人号码                                 
	 * @param   mailingAddress;//联系住所地址                                  
	 * @param   sourceOfCertification;//用户认证来源                          
	 * @param   userSource;//申请来源                                        
	 * @param   IDCardPhoto1;//身份证正面图片                       
	 * @param   IDCardPhoto2;//身份证背面图片                                 
	 * @param   driverLicensePhoto;//驾驶证图片     
	 * @param http://192.168.1.245:8080/web/user/driverLicenseVoluntaryDemotion.html?identificationNO=A&loginUser=445222199209020034&IDcard=445222199209020034&driverLicense=445222199209020034&name=张宇帆&photoReturnNumberString=11111&placeOfDomicile=深圳&receiverName=张宇帆&receiverNumber=15920050177&mailingAddress=深圳市宝安区&sourceOfCertification=C&userSource=C&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111      
        
	 *         
     */
    @RequestMapping("driverLicenseVoluntaryDemotion")
    public void driverLicenseVoluntaryDemotion( String identificationNO ,String loginUser ,String IDcard ,String driverLicense ,String name ,String photoReturnNumberString ,String placeOfDomicile , String receiverName , String receiverNumber , String mailingAddress ,String sourceOfCertification ,String userSource ,String IDCardPhoto1,String IDCardPhoto2,String driverLicensePhoto,HttpServletRequest request,HttpServletResponse response){
    	
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	DriverLicenseVoluntaryDemotionVo driverLicenseVoluntaryDemotionVo = new DriverLicenseVoluntaryDemotionVo();
    	String businessType = "J";
    	driverLicenseVoluntaryDemotionVo.setBusinessType(businessType);
		try {
			

			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setName(name);
			}
			
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setIdentificationNO(identificationNO);
			}
			
			
			if(StringUtil.isBlank(loginUser)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setLoginUser(loginUser);
			}
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setIDcard(IDcard);
			}
			
			if(StringUtil.isBlank(driverLicense)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setDriverLicense(driverLicense);
			}
			
			if(StringUtil.isBlank(userSource)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setUserSource(userSource);
			}
			
			
			if(StringUtil.isBlank(photoReturnNumberString)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setReceiverName(receiverName);
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setReceiverNumber(receiverNumber);
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setMailingAddress(mailingAddress);
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setSourceOfCertification(sourceOfCertification);
			}
			
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setIDCardPhoto1(IDCardPhoto1);
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setIDCardPhoto2(IDCardPhoto2);
			}
			
			if(StringUtil.isBlank(driverLicensePhoto)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setDriverLicensePhoto(driverLicensePhoto);
			}
			
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverLicenseVoluntaryDemotionVo.setPlaceOfDomicile(placeOfDomicile);
			}
			
			
			String ip = getIp2(request);
			driverLicenseVoluntaryDemotionVo.setIp(ip);
			Map<String, String> map = handleService.driverLicenseVoluntaryDemotion(driverLicenseVoluntaryDemotionVo);
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
			logger.error("驾驶证自愿降级异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 驾驶证补证
	 * @param  repairReason 补证原因               
	 * @param  identificationNO 身份证明名称        
	 * @param  IDcard 身份证明号码                  
	 * @param  name 姓名    
	 * @param  mobilephone 电话                        
	 * @param  IDCardPhoto1 身份证正面图片                
	 * @param  IDCardPhoto2 身份证背面图片           
	 * @param  photoReturnNumberString 相片回执编号 
	 * @param  foreignersLiveTable 境外人员临住表    
	 * @param  placeOfDomicile 户籍所在地          
	 * @param  postalcode 邮政编码                
	 * @param  receiverName 收件人姓名             
	 * @param  receiverNumber 收件人手机号码         
	 * @param  mailingAddress 联系住所地址          
	 * @param  livePhoto1 居住证正面图片                  
	 * @param  livePhoto2 居住证背面图片            
	 * @param  loginUser 认证用户身份证号码                 
	 * @param  sourceOfCertification 认证来源          
	 * @param  userSource 来源标志  
	 * @param  http://192.168.1.245:8080/web/user/repairDriverLicense.html?repairReason=1&identificationNO=A&IDcard=445222199209020034&name=张宇帆&mobilephone=15920050177&IDCardPhoto1=111&IDCardPhoto2=222&photoReturnNumberString=111&foreignersLiveTable=111&placeOfDomicile=深圳&postalcode=1&receiverName=111&receiverNumber=15920050177&mailingAddress=深圳市宝安区&livePhoto1=111&livePhoto2=222&loginUser=445222199209020034&sourceOfCertification=C&userSource=C                    
           
     */
    @RequestMapping("repairDriverLicense")
    public void repairDriverLicense(String repairReason ,String identificationNO ,String IDcard ,String name ,String mobilephone ,String IDCardPhoto1 ,String IDCardPhoto2 ,String photoReturnNumberString ,String foreignersLiveTable ,String placeOfDomicile,String postalcode ,String receiverName ,String receiverNumber ,String mailingAddress ,String livePhoto1 ,String livePhoto2 ,String loginUser ,String sourceOfCertification ,String userSource ,                           
HttpServletRequest request,HttpServletResponse response){
    	
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	RepairOrReplaceDriverLicenseVo repairOrReplaceDriverLicenseVo = new RepairOrReplaceDriverLicenseVo();
    	
		try {
			String businessType = "B";
			repairOrReplaceDriverLicenseVo.setBusinessType(businessType);
			
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setName(name);
			}
			
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setMobilephone(mobilephone);
			}
			
			
			
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIdentificationNO(identificationNO);
			}
			
			
			if(StringUtil.isBlank(loginUser)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setLoginUser(loginUser);
			}
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIDcard(IDcard);
			}
			
			if(StringUtil.isBlank(userSource)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setUserSource(userSource);
			}
			
			
			if(StringUtil.isBlank(photoReturnNumberString)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setReceiverName(receiverName);
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setReceiverNumber(receiverNumber);
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setMailingAddress(mailingAddress);
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setSourceOfCertification(sourceOfCertification);
			}
			
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIDCardPhoto1(IDCardPhoto1);
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIDCardPhoto2(IDCardPhoto2);
			}
			
			if(StringUtil.isBlank(livePhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setLivePhoto1(livePhoto1);
			}
			if(StringUtil.isBlank(livePhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setLivePhoto2(livePhoto2);
			}
			
			
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				if ("3".equals(placeOfDomicile)) {
					if(StringUtil.isBlank(foreignersLiveTable)){
						baseBean.setCode(MsgCode.paramsError);
						baseBean.setMsg("境外人员临住表不能为空!");
						renderJSON(baseBean);
						return;
					}else{
						repairOrReplaceDriverLicenseVo.setForeignersLiveTable(foreignersLiveTable);
					}
				}
				repairOrReplaceDriverLicenseVo.setPlaceOfDomicile(placeOfDomicile);
			}
			repairOrReplaceDriverLicenseVo.setRepairReason(repairReason);
			repairOrReplaceDriverLicenseVo.setPostalcode(postalcode);
			String ip = getIp2(request);
			repairOrReplaceDriverLicenseVo.setIp(ip);
			Map<String, String> map = handleService.repairDriverLicense(repairOrReplaceDriverLicenseVo);
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
			logger.error("驾驶证补证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * 驾驶证换证
	 * @param  identificationNO 身份证明名称        
	 * @param  IDcard 身份证明号码                  
	 * @param  name 姓名    
	 * @param  mobilephone 电话                        
	 * @param  IDCardPhoto1 身份证正面图片                
	 * @param  IDCardPhoto2 身份证背面图片           
	 * @param  photoReturnNumberString 相片回执编号 
	 * @param  foreignersLiveTable 境外人员临住表    
	 * @param  placeOfDomicile 户籍所在地          
	 * @param  receiverName 收件人姓名             
	 * @param  receiverNumber 收件人手机号码         
	 * @param  mailingAddress 联系住所地址          
	 * @param  livePhoto1 居住证正面图片                  
	 * @param  livePhoto2 居住证背面图片            
	 * @param  loginUser 认证用户身份证号码                 
	 * @param  sourceOfCertification 认证来源          
	 * @param  userSource 来源标志  
	 * @param  http://192.168.1.245:8080/web/user/replaceDriverLicense.html?identificationNO=A&IDcard=445222199209020034&name=张宇帆&mobilephone=15920050177&IDCardPhoto1=111&IDCardPhoto2=222&photoReturnNumberString=111&foreignersLiveTable=111&placeOfDomicile=深圳&receiverName=111&receiverNumber=15920050177&mailingAddress=深圳市宝安区&livePhoto1=111&livePhoto2=222&loginUser=445222199209020034&sourceOfCertification=C&userSource=C                    
           
     */
    @RequestMapping("replaceDriverLicense")
    public void replaceDriverLicense(String identificationNO ,String IDcard ,String name ,String mobilephone ,String IDCardPhoto1 ,String IDCardPhoto2 ,String photoReturnNumberString ,String foreignersLiveTable ,String placeOfDomicile,String receiverName ,String receiverNumber ,String mailingAddress ,String livePhoto1 ,String livePhoto2 ,String loginUser ,String sourceOfCertification ,String userSource ,                           
    			HttpServletRequest request,HttpServletResponse response){
    	
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	RepairOrReplaceDriverLicenseVo repairOrReplaceDriverLicenseVo = new RepairOrReplaceDriverLicenseVo();
    	
		try {
			
			String businessType = "H";
			repairOrReplaceDriverLicenseVo.setBusinessType(businessType);
			
			
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setName(name);
			}
			
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setMobilephone(mobilephone);
			}
			
			
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIdentificationNO(identificationNO);
			}
			
			
			if(StringUtil.isBlank(loginUser)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setLoginUser(loginUser);
			}
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIDcard(IDcard);
			}
			
			if(StringUtil.isBlank(userSource)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setUserSource(userSource);
			}
			
			
			if(StringUtil.isBlank(photoReturnNumberString)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("相片回执编号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setPhotoReturnNumberString(photoReturnNumberString);
			}
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setReceiverName(receiverName);
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setReceiverNumber(receiverNumber);
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setMailingAddress(mailingAddress);
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("来源标志不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setSourceOfCertification(sourceOfCertification);
			}
			
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIDCardPhoto1(IDCardPhoto1);
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setIDCardPhoto2(IDCardPhoto2);
			}
			
			if(StringUtil.isBlank(livePhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setLivePhoto1(livePhoto1);
			}
			if(StringUtil.isBlank(livePhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				repairOrReplaceDriverLicenseVo.setLivePhoto2(livePhoto2);
			}
			
			
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				if ("3".equals(placeOfDomicile)) {
					if(StringUtil.isBlank(foreignersLiveTable)){
						baseBean.setCode(MsgCode.paramsError);
						baseBean.setMsg("境外人员临住表不能为空!");
						renderJSON(baseBean);
						return;
					}else{
						repairOrReplaceDriverLicenseVo.setForeignersLiveTable(foreignersLiveTable);
					}
				}
				repairOrReplaceDriverLicenseVo.setPlaceOfDomicile(placeOfDomicile);
			}
			
			String ip = getIp2(request);
			repairOrReplaceDriverLicenseVo.setIp(ip);
			Map<String, String> map = handleService.replaceDriverLicense(repairOrReplaceDriverLicenseVo);
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
			logger.error("驾驶证换证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    
    /**                                                                                
     * 驾驶人联系方式变更                                                          
     * @param  name 姓名                                     
	 * @param  gender 性别                        
	 * @param  identificationNO 身份证明名称             
	 * @param  IDcard 身份证明号码   
	 * @param  driverLicense 驾驶证号码                          
	 * @param  mailingAddress 联系住所地址                          
	 * @param  mobilephone 手机号码                              
	 * @param  loginUser 登录账户                            
	 * @param  userSource 申请来源                            
	 * @param  IDCardPhoto1 身份证正面照片                          
	 * @param  IDCardPhoto2 身份证反面照片                         
	 * @param  driverLicensePhoto 驾驶证照片
	 * @param http://192.168.1.245:8080/web/user/driverChangeContact.html?name=张宇帆&gender=1&identificationNO=A&IDcard=622822198502074110&driverLicense=622822198502074110&mailingAddress=深圳市宝安区&mobilephone=15920050177&loginUser=15920050177&userSource=C&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111 
              
     */
    @RequestMapping("driverChangeContact")
    public void driverChangeContact( String name ,String gender ,String identificationNO ,String IDcard ,String driverLicense ,String mailingAddress ,String mobilephone , String loginUser ,String userSource ,String IDCardPhoto1,String IDCardPhoto2,String driverLicensePhoto,HttpServletRequest request,HttpServletResponse response){
    	
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	DriverChangeContactVo driverChangeContactVo = new DriverChangeContactVo();
    	String businessType = "L";
    	driverChangeContactVo.setBusinessType(businessType);
		try {
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setName(name);
			}
			
			if(StringUtil.isBlank(gender)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("性别不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setGender(gender);
			}
			
			if(StringUtil.isBlank(identificationNO)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明类型不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setIdentificationNO(identificationNO);
			}
			
			
			if(StringUtil.isBlank(loginUser)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证用户身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setLoginUser(loginUser);
			}
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证明号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setIDcard(IDcard);
			}
			if(StringUtil.isBlank(driverLicense)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setDriverLicense(driverLicense);
			}
			
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("电话号码不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setMobilephone(mobilephone);
			}
			if(StringUtil.isBlank(userSource)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请来源不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setUserSource(userSource);
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("联系地址地址不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setMailingAddress(mailingAddress);
			}
			
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证正面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setIDCardPhoto1(IDCardPhoto1);
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证反面照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setIDCardPhoto2(IDCardPhoto2);
			}
			if(StringUtil.isBlank(driverLicensePhoto)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("驾驶证照片不能为空!");
				renderJSON(baseBean);
				return;
			}else{
				driverChangeContactVo.setDriverLicensePhoto(driverLicensePhoto);
			}
			
			String ip = getIp2(request);
			driverChangeContactVo.setIp(ip);
			Map<String, String> map = handleService.driverChangeContact(driverChangeContactVo);
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
			logger.error("驾驶人联系方式变更异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
    /**
     * @Title: applyGatePass
     * @Description: TODO(申请通行证-外地车)
     * @param @param request
     * @param @param response    参数
     * @return void    返回类型
     * @throws
     */
    @RequestMapping(value="/applyGatePass.html")
	public void applyGatePass(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	ApplyGatePassVo vo = new ApplyGatePassVo();
    	
		try {
			String plateType = request.getParameter("plateType");   //号牌种类
			String cartype = request.getParameter("cartype");  //车辆类型
			String abbreviation = request.getParameter("abbreviation");  //车牌简称
			String numberPlate = request.getParameter("numberPlate");  //车牌号码
			String vin = request.getParameter("vin");  //车架号
			String userName = request.getParameter("userName");  //车辆所有人
			String mobilephone = request.getParameter("mobilephone");  //手机号码
			String applyDate = request.getParameter("applyDate");  //申请日期
			String remarks = request.getParameter("remarks");  //备注
			
			//验证号牌种类
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车辆类型
			if(StringUtil.isBlank(cartype)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车牌号码
			if(StringUtil.isBlank(abbreviation) || StringUtil.isBlank(numberPlate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车架号
			if(StringUtil.isBlank(vin)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证车辆所有人
			if(StringUtil.isBlank(userName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆所有人不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证手机
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证申请日期
			if(StringUtil.isBlank(applyDate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("申请日期不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			vo.setAbbreviation(abbreviation); //车牌简称
			vo.setNumberPlate(numberPlate); //车牌号码
			vo.setPlateType(plateType); //号牌种类
			vo.setCartype(cartype);  //车辆类型
			vo.setVin(vin);  	//车架号后四位
			vo.setUserName(userName);	//车辆所有人
			vo.setMobilephone(mobilephone); //手机号码
			vo.setApplyDate(applyDate);
			vo.setRemarks(remarks);
			
			//接口调用
			Map<String, String> map = handleService.applyGatePass(vo);
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
			logger.error("申请通行证-外地车Action异常:"+e);
			DealException(baseBean, e);
			
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
    
    /**
     * @Title: applyCarTemporaryLicence
     * @Description: TODO(申请机动车临牌)
     * @param @param request
     * @param @param response    参数
     * @return void    返回类型
     * @throws
     */
    @RequestMapping(value="/applyCarTemporaryLicence.html")
	public void applyCarTemporaryLicence(HttpServletRequest request,HttpServletResponse response){
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	ApplyCarTemporaryLicenceVo vo = new ApplyCarTemporaryLicenceVo();
    	
		try {
			String userName = request.getParameter("userName");   //姓名
			String identityCard = request.getParameter("identityCard");  //身份证号
			String mobilephone = request.getParameter("mobilephone");  //手机号码
			String cartModels = request.getParameter("cartModels");  //车辆型号
			String cartype = request.getParameter("cartype");  //车辆类型
			String engineNumber = request.getParameter("engineNumber");  //发动机号
			String vin = request.getParameter("vin");  //车架号
			String carOrigin = request.getParameter("carOrigin");  //车辆产地 进口/国产
			String placeOfDomicile = request.getParameter("placeOfDomicile");  //户籍所在地
			String receiverName = request.getParameter("receiverName");  //收件人姓名
			String receiverNumber = request.getParameter("receiverNumber");  //收件人号码
			String receiverAddress = request.getParameter("receiverAddress");  //收件人地址
			String PHOTO26 = request.getParameter("PHOTO26");  //购置发票图
			String PHOTO27 = request.getParameter("PHOTO27");  //交强险单据
			String PHOTO9 = request.getParameter("PHOTO9");  //身份证正面
			String PHOTO10 = request.getParameter("PHOTO10");  //身份证反面
			String PHOTO28 = request.getParameter("PHOTO28");  //机动车合格证
			String PHOTO31 = request.getParameter("PHOTO31");  //境外人员临住表
			String PHOTO29 = request.getParameter("PHOTO29");  //进口货物证明书
			String sourceOfCertification = request.getParameter("sourceOfCertification");  	//来源
			
			//验证姓名
			if(StringUtil.isBlank(userName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证identityCard
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证mobilephone
			if(StringUtil.isBlank(mobilephone)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("手机号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证cartModels
			if(StringUtil.isBlank(cartModels)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆型号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证cartype
			if(StringUtil.isBlank(cartype)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆类型不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证engineNumber
			if(StringUtil.isBlank(engineNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("发动机号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证vin
			if(StringUtil.isBlank(vin)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证carOrigin
			if(StringUtil.isBlank(carOrigin)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车辆产地不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证placeOfDomicile
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证receiverName
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证receiverNumber
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			//验证receiverAddress
			if(StringUtil.isBlank(receiverAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			
			vo.setUserName(userName);
			vo.setIdentityCard(identityCard);
			vo.setCartModels(cartModels);
			vo.setCartype(cartype);
			vo.setEngineNumber(engineNumber);
			vo.setVin(vin);
			vo.setCarOrigin(carOrigin);
			vo.setReceiverName(receiverName);
			vo.setReceiverAddress(receiverAddress);
			vo.setReceiverNumber(receiverNumber);
			vo.setPHOTO9(PHOTO9);
			vo.setPHOTO10(PHOTO10);
			vo.setPHOTO26(PHOTO26);
			vo.setPHOTO27(PHOTO27);
			vo.setPHOTO28(PHOTO28);
			vo.setPHOTO29(PHOTO29);
			vo.setPHOTO31(PHOTO31);
			vo.setIp(getIp2(request));
			vo.setSourceOfCertification(sourceOfCertification);
			
			//接口调用
			Map<String, String> map = handleService.applyCarTemporaryLicence(vo);
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
			logger.error("申请机动车临牌Action异常:"+e);
			DealException(baseBean, e);
			
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
    
	/**
	 * 机动车联系方式变更
	* @author lifangyong
	* @param name 车主姓名
	* @param identificationNo 证件种类
	* @param identityCard 证件号码
	* @param licensePlate 号牌号码
	* @param licensePlateTpye 号牌种类
	* @param placeOfDomicile 户籍所在地
	* @param VIN 车架号
	* @param mobilephone 变更号码
	* @param mailingAddress 地址
	* @param IDCardPhoto1 身份证（正面）
	* @param IDCardPhoto2 身份证（反面）
	* @param driverLicensePhoto 机动车登记证书（当户籍选择境外人员时显示）·境外人员临住表
	* @param sourceOfCertification 申请来源
	* @param request
	* @param response
	* @param 
	*/
	@RequestMapping("iocomotiveCarChangeContact")
	public void iocomotiveCarChangeContact( String name,String identificationNo,String identityCard,String licensePlate,String licensePlateTpye,String placeOfDomicile,String VIN,String mobilephone,String mailingAddress,String IDCardPhoto1,String IDCardPhoto2,String driverLicensePhoto,String sourceOfCertification,HttpServletRequest request,HttpServletResponse response){
		// http://192.168.1.136:8080/web/handleservice/iocomotiveCarChangeContact.html?name=张三测试&identificationNo=A&identityCard=445222199209020034&licensePlate=2222&licensePlateTpye=A&placeOfDomicile=0&VIN=1212&mobilephone=13123212232&mailingAddress=深圳市南山区&IDCardPhoto1=323&IDCardPhoto2=2132&driverLicensePhoto=3123321&sourceOfCertification=C
		BaseBean baseBean = new BaseBean();		//创建返回结果
		IocomotiveCarChangeContactVo iocomotiveCarChangeContactVo = new IocomotiveCarChangeContactVo();
		try {
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车主姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(identificationNo)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(identityCard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(licensePlate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(licensePlateTpye)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(VIN)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车架号不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（正面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（反面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(driverLicensePhoto)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("机动车登记证书不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			String ip = getIp2(request);
			iocomotiveCarChangeContactVo.setName(name);
			iocomotiveCarChangeContactVo.setIdentificationNo(identificationNo);
			iocomotiveCarChangeContactVo.setIdentityCard(identityCard);
			iocomotiveCarChangeContactVo.setLicensePlate(licensePlate);
			iocomotiveCarChangeContactVo.setLicensePlateTpye(licensePlateTpye);
			iocomotiveCarChangeContactVo.setPlaceOfDomicile(placeOfDomicile);
			iocomotiveCarChangeContactVo.setVIN(VIN);
			iocomotiveCarChangeContactVo.setMobilephone(mobilephone);
			iocomotiveCarChangeContactVo.setMailingAddress(mailingAddress);
			iocomotiveCarChangeContactVo.setIDCardPhoto1(IDCardPhoto1);
			iocomotiveCarChangeContactVo.setIDCardPhoto2(IDCardPhoto2);
			iocomotiveCarChangeContactVo.setDriverLicensePhoto(driverLicensePhoto);
			iocomotiveCarChangeContactVo.setSourceOfCertification(sourceOfCertification);
			iocomotiveCarChangeContactVo.setIp(ip);
			Map<String, String> map = handleService.iocomotiveCarChangeContact(iocomotiveCarChangeContactVo);
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
			logger.error("机动车联系方式变更异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
    
    /**
     * 换领机动车行驶证
     * @author lifangyong
     * @param name 车主姓名
     * @param IDcard 证件号码
     * @param licensePlate 号牌号码
     * @param licensePlateTpye 号牌种类
     * @param placeOfDomicile 户籍所在地
     * @param address 住所详细地址
     * @param receiverName 收件人姓名
     * @param receiverNumber 收件人号码
     * @param mailingAddress 收件人地址
     * @param livePhoto1 居住证正面
     * @param livePhoto2 居住证反面
     * @param IDCardPhoto1 身份证（正面）
     * @param IDCardPhoto2 身份证（反面）
     * @param driverLicensePhoto 机动车登记证书
     * @param sourceOfCertification 申请来源
     * @param request
     * @param response
     */
    @RequestMapping("iocomotiveCarReplace")
    public void iocomotiveCarReplace(String name, String IDcard, String licensePlate, String licensePlateTpye, String placeOfDomicile, String address, String receiverName, String receiverNumber, String mailingAddress, String livePhoto1, String livePhoto2, String IDCardPhoto1, String IDCardPhoto2, String driverLicensePhoto, String sourceOfCertification, HttpServletRequest request,HttpServletResponse response){
    	// http://192.168.1.136:8080/web/handleservice/iocomotiveCarReplace.html?name=张三测试&IDcard=445222199209020034&licensePlate=2222&licensePlateTpye=A&placeOfDomicile=0&address=深圳市南山区&receiverName=张三&receiverNumber=13512452362&mailingAddress=深圳市南山区&livePhoto1=saas&livePhoto2=dsfds&IDCardPhoto1=dfdsfff&IDCardPhoto2=asqwe&driverLicensePhoto=fdfsfds&sourceOfCertification=C
    	BaseBean baseBean = new BaseBean();		//创建返回结果
    	IocomotiveCarReplaceVo iocomotiveCarReplaceVo = new IocomotiveCarReplaceVo();
		try {
			if(StringUtil.isBlank(name)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("车主姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(IDcard)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("证件号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(licensePlate)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(licensePlateTpye)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("号牌种类不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(placeOfDomicile)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("户籍所在地不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(receiverName)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人姓名不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(receiverNumber)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人号码不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(mailingAddress)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("收件人地址不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(livePhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证（正面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(livePhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("居住证（反面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(IDCardPhoto1)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（正面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(IDCardPhoto2)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("身份证（反面）不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(driverLicensePhoto)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("机动车登记证书不能为空!");
				renderJSON(baseBean);
				return;
			}
			if(StringUtil.isBlank(sourceOfCertification)){
				baseBean.setCode(MsgCode.paramsError);
				baseBean.setMsg("认证来源不能为空!");
				renderJSON(baseBean);
				return;
			}
			String ip = getIp2(request);
			iocomotiveCarReplaceVo.setName(name);					//	车主姓名
			iocomotiveCarReplaceVo.setIDcard(IDcard);					//	证件号码
			iocomotiveCarReplaceVo.setLicensePlate(licensePlate);			//	号牌号码
			iocomotiveCarReplaceVo.setLicensePlateTpye(licensePlateTpye);		//	号牌种类
			iocomotiveCarReplaceVo.setPlaceOfDomicile(placeOfDomicile);			//	户籍所在地
			iocomotiveCarReplaceVo.setAddress(address);					//	住所详细地址
			iocomotiveCarReplaceVo.setReceiverName(receiverName);			//	收件人姓名
			iocomotiveCarReplaceVo.setReceiverNumber(receiverNumber);			//	收件人号码
			iocomotiveCarReplaceVo.setMailingAddress(mailingAddress);			//	收件人地址
			iocomotiveCarReplaceVo.setLivePhoto1(livePhoto1);				//	居住证正面
			iocomotiveCarReplaceVo.setLivePhoto2(livePhoto2);				//	居住证反面
			iocomotiveCarReplaceVo.setIDCardPhoto1(IDCardPhoto1);			//	身份证（正面）
			iocomotiveCarReplaceVo.setIDCardPhoto2(IDCardPhoto2);			//	身份证（反面）
			iocomotiveCarReplaceVo.setDriverLicensePhoto(driverLicensePhoto);		//	机动车登记证书
			iocomotiveCarReplaceVo.setSourceOfCertification(sourceOfCertification); //  申请来源
			iocomotiveCarReplaceVo.setIp(ip);//	ip
			Map<String, String> map = handleService.iocomotiveCarReplace(iocomotiveCarReplaceVo);
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
			logger.error("换领机动车行驶证异常:" + e);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
}
