package cn.web.front.action.alipay;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.UserBindAlipay;
import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.service.IAccountService;
import cn.illegal.bean.IllegalInfoBean;
import cn.illegal.service.IIllegalService;
import cn.message.bean.CardReceive;
import cn.message.model.alipay.CardReceiveConstants;
import cn.message.service.IAlipayService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayAction extends BaseAction {
	Logger logger = Logger.getLogger(AlipayAction.class);
	
	@Autowired
	@Qualifier("alipayService")
	private IAlipayService alipayService;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	@Autowired
    @Qualifier("illegalService")
	private IIllegalService illegalService;
	
	/**
	 * 是否领过卡
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/isReceiveCard.html")
	public void isReceiveCard(HttpServletRequest request, HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		
		String userId = request.getParameter("openId");
		String type = request.getParameter("type");//领取类型
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		if(StringUtils.isBlank(userId)){
    		baseBean.setMsg("openId 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		if(StringUtils.isBlank(type)){
    		baseBean.setMsg("type 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}else{
    		if("1".equals(type)){
    			type = CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER;//驾驶证
    		}else if("2".equals(type)){
    			type = CardReceiveConstants.CARD_RECEIVE_TYPE_CAR;//行驶证
    		}else{
    			baseBean.setMsg("type 非法！");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
    		}
    	}
		if(StringUtils.isBlank(sourceOfCertification)){
    		baseBean.setMsg("sourceOfCertification 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		
		try {
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				logger.info("【支付宝卡包】用户授权异常");
				//用户授权异常
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("用户授权异常!");
				renderJSON(baseBean);
				return;
			}
			
			String certNo = userBindAlipay.getIdCard();
			String mobileNumber = userBindAlipay.getMobileNumber();
			
			//查询认证基本信息
			AuthenticationBasicInformationVo baseInfo = accountService.authenticationBasicInformationQuery(certNo, sourceOfCertification);
			if(baseInfo != null){
				String zt = baseInfo.getZt();//用户状态
				if("已激活".equals(zt)){
					logger.info("【支付宝卡包】用户状态zt="+zt);
					//是否已领取证件
					int count = alipayService.queryReceiveCardCount(certNo, type);
					if(count > 0){
						logger.info("【支付宝卡包】跳转到支付宝“证件夹”页面");
						//已领取，跳转到支付宝“证件夹”页面
						baseBean.setCode("0003");
						baseBean.setData("alipays://platformapi/startapp?appId=60000032");
						renderJSON(baseBean);
						return;
					}else{
						if(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER.equals(type)){
							logger.info("【支付宝卡包】未领取，获取我的驾驶证信息给前端展示");
							//获取我的驾驶证信息
							MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, sourceOfCertification);
							if(MsgCode.success.equals(myDriverLicense.getCode())){
								baseBean.setCode(MsgCode.success);
								baseBean.setData(myDriverLicense);
								renderJSON(baseBean);
								return;
							}else{
								baseBean.setCode(myDriverLicense.getCode());
								baseBean.setMsg(myDriverLicense.getMsg());
								renderJSON(baseBean);
								return;
							}
						}else if(CardReceiveConstants.CARD_RECEIVE_TYPE_CAR.equals(type)){
							logger.info("【支付宝卡包】未领取，获取绑定行驶证信息给前端展示");
							//获取绑定行驶证信息
							List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
							for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
								String code = bindTheVehicleVo.getCode();
								if(code != null){
									//没有绑定机动车
									baseBean.setCode(code);
									baseBean.setMsg(bindTheVehicleVo.getMsg());
									renderJSON(baseBean);
									return;
								}
							}
							//有绑定机动车
							baseBean.setCode(MsgCode.success);
							baseBean.setData(bndTheVehicles);
							renderJSON(baseBean);
							return;
						}
					}
				}else{
					logger.info("【支付宝卡包】xxcj08 获取用户状态zt="+zt);
				}
			}
			
			//查询身份认证是否审核通过
			/*List<IdentificationOfAuditResultsVo> auditResults = accountService.getIdentificationOfAuditResults(certNo, sourceOfCertification);
			if(auditResults != null){
				for (IdentificationOfAuditResultsVo vo : auditResults) {
					if("1".equals(vo.getSHZT()) || "-1".equals(vo.getSHZT())){//审核状态为1-审核通过    还有-1？？？
						logger.info("【支付宝卡包】身份认证审核通过SHZT="+vo.getSHZT());
						//是否已领取证件
						int count = alipayService.queryReceiveCardCount(certNo, type);
						if(count > 0){
							logger.info("【支付宝卡包】跳转到支付宝“证件夹”页面");
							//已领取，跳转到支付宝“证件夹”页面
							response.sendRedirect("alipays://platformapi/startapp?appId=60000032");
							return;
						}else{
							if(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER.equals(type)){
								logger.info("【支付宝卡包】未领取，获取我的驾驶证信息给前端展示");
								//获取我的驾驶证信息
								MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, sourceOfCertification);
								if(MsgCode.success.equals(myDriverLicense.getCode())){
									baseBean.setCode(MsgCode.success);
									baseBean.setData(myDriverLicense);
									renderJSON(baseBean);
									return;
								}else{
									baseBean.setCode(myDriverLicense.getCode());
									baseBean.setMsg(myDriverLicense.getMsg());
									renderJSON(baseBean);
									return;
								}
							}else if(CardReceiveConstants.CARD_RECEIVE_TYPE_CAR.equals(type)){
								logger.info("【支付宝卡包】未领取，获取绑定行驶证信息给前端展示");
								//获取绑定行驶证信息
								List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
								for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
									String code = bindTheVehicleVo.getCode();
									if(code != null){
										//没有绑定机动车
										baseBean.setCode(code);
										baseBean.setMsg(bindTheVehicleVo.getMsg());
										renderJSON(baseBean);
										return;
									}
								}
								//有绑定机动车
								baseBean.setCode(MsgCode.success);
								baseBean.setData(bndTheVehicles);
								renderJSON(baseBean);
								return;
							}
						}
					}
				}
			}*/
			
			logger.info("【支付宝卡包】非星级用户！");
			//非星级用户
			baseBean.setCode(MsgCode.businessError);
			baseBean.setMsg("亲，需要成为星级用户才能享受服务！");
			renderJSON(baseBean);
			return;
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】isReceiveCard异常：userId="+userId+",type="+type+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
	}
	
	
	/**
	 * 领取驾驶证
	 * @descrption TODO(领取驾驶证)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/receiveJsCard.html")
	public void receiveJsCard(HttpServletRequest request, HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		
		String userId = request.getParameter("openId");
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		if(StringUtils.isBlank(userId)){
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
		
		try {
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				logger.info("【支付宝卡包】用户授权异常");
				//用户授权异常
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("用户授权异常!");
				renderJSON(baseBean);
				return;
			}
			
			String certNo = userBindAlipay.getIdCard();
			String realName = userBindAlipay.getRealName();
			String mobileNumber = userBindAlipay.getMobileNumber();
			
			//查询身份认证是否审核通过
			/*List<IdentificationOfAuditResultsVo> auditResults = accountService.getIdentificationOfAuditResults(certNo, sourceOfCertification);
			if(auditResults != null){
				for (IdentificationOfAuditResultsVo vo : auditResults) {
					if("1".equals(vo.getSHZT()) || "-1".equals(vo.getSHZT())){//审核状态为1-审核通过    还有-1？？？
						logger.info("【支付宝卡包】身份认证审核通过SHZT="+vo.getSHZT());*/
			
			
			//查询认证基本信息
			/*AuthenticationBasicInformationVo baseInfo = accountService.authenticationBasicInformationQuery(certNo, sourceOfCertification);
			if(baseInfo != null){
				String zt = baseInfo.getZt();//用户状态
				if("已激活".equals(zt)){
					logger.info("【支付宝卡包】用户状态zt="+zt);*/
			
			
					//是否已领取证件
					int count = alipayService.queryReceiveCardCount(certNo, CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER);
					if(count > 0){
						logger.info("【支付宝卡包】跳转到已领取页面");
						//已领取，跳转到领取成功页面
						baseBean.setCode(MsgCode.businessError);
						baseBean.setMsg("您好！您的深圳交警电子驾驶证已经领取");
						renderJSON(baseBean);
						return;
					}else{
						//未领取驾驶证
						
						logger.info("【支付宝卡包】未领取驾驶证");
						//调JST接口获取电子驾驶证
						ElectronicDriverLicenseVo eCardInfo = accountService.getElectronicDriverLicense(certNo, realName, mobileNumber, "Z");
						if(!MsgCode.success.equals(eCardInfo.getCode())){
							//获取失败
							baseBean.setCode(eCardInfo.getCode());
							baseBean.setMsg(eCardInfo.getMsg());
							renderJSON(baseBean);
							return;
						}
						
						String eCardImgBase64 = eCardInfo.getElectronicDriverLicense();//证件图片base64
						//String encryptQRCodeBase64 = eCardInfo.getElectronicDriverLicenseQRCode();//加密二维码base64
						
						/*{ "file_type", "trans_picture" },
		                { "type_id", "cif_driving_shenzhen_police_pic" },
		                { "file_description", "深圳交警电子驾照正面" },
		                { "file_name", "certificate.png" },
		                { "file", Convert.FromBase64String(CertificateResult["result"]["dzz"].ToString()) },*/
						JSONObject json = new JSONObject();
						json.put("file_type", "trans_picture");
						json.put("type_id", "cif_driving_shenzhen_police_pic");
						json.put("file_description", "深圳交警电子驾照正面");
						json.put("file_name", "certificate.png");
						json.put("file", eCardImgBase64);
						
						//调支付宝sdk上传证件照片
						
						//上传失败，提示领取失败
						
						//上传成功，获取我的驾驶证信息
						MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(certNo, sourceOfCertification);
						if(!MsgCode.success.equals(myDriverLicense.getCode())){
							baseBean.setCode(myDriverLicense.getCode());
							baseBean.setMsg(myDriverLicense.getMsg());
							renderJSON(baseBean);
							return;
						}
						
						String unresolvedIllegalCount = "0";//未处理违章数
						//查询绑定机动车
						List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
						for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
			        		String isMyself = bindTheVehicleVo.getIsMyself();
			        		if("本人".equals(isMyself)){
			        			String licensePlateNo = bindTheVehicleVo.getNumberPlateNumber();
			        			String licensePlateType = bindTheVehicleVo.getPlateType();
			        			String vehicleIdentifyNoLast4 = bindTheVehicleVo.getBehindTheFrame4Digits();
			        			//查询未处理违章数
			        			BaseBean respBean = illegalService.queryInfoByLicensePlateNo(licensePlateNo, licensePlateType, vehicleIdentifyNoLast4, userId, sourceOfCertification);
			        			if(MsgCode.success.equals(respBean.getCode())){
			        				List<IllegalInfoBean> illegalInfoBeans = (List<IllegalInfoBean>) respBean.getData();
			        				unresolvedIllegalCount = illegalInfoBeans.size()+"";
			        			}
			        		}
						}
						logger.info("【支付宝卡包】领取驾驶证查询到未处理违章数："+unresolvedIllegalCount);
						
						//封装驾驶证信息
						
						//调支付宝sdk提交驾驶证信息
						
						//提交成功，写入数据库
						CardReceive cardReceive = new CardReceive();
						cardReceive.setAlipayUserId(userId);
						cardReceive.setArchiveNumber(myDriverLicense.getFileNumber());//档案编号
						cardReceive.setCertNo(certNo);
						cardReceive.setGender("1".equals(myDriverLicense.getGender()) ? "男" : "女");
						cardReceive.setRealName(realName);
						cardReceive.setType(CardReceiveConstants.CARD_RECEIVE_TYPE_DRIVER);
						alipayService.insertCardReceive(cardReceive);
						
						logger.info("【支付宝卡包】跳转到领取成功页面");
						//领取成功，跳转到领取成功页面
						baseBean.setCode(MsgCode.success);
						baseBean.setMsg("您好！您的深圳交警电子驾驶证已经领取");
						renderJSON(baseBean);
						return;
						
						//提交失败，提示用户
						
						
					}
					
				/*}
			}*/
					
			//}
			
		} catch (Exception e) {
			logger.error("【支付宝卡包】receiveJsCard异常：userId="+userId+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
		logger.info("【支付宝卡包】receiveJsCard结果："+JSON.toJSONString(baseBean));
	}
	
	
	/**
	 * 领取行驶证
	 * @descrption TODO(领取行驶证)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/receiveXsCard.html")
	public void receiveXsCard(HttpServletRequest request, HttpServletResponse response){
		BaseBean baseBean = new BaseBean();
		
		String userId = request.getParameter("openId");
		String gender = request.getParameter("gender");
		String sourceOfCertification = request.getParameter("sourceOfCertification");
		
		if(StringUtils.isBlank(userId)){
    		baseBean.setMsg("openId 不能为空!");
    		baseBean.setCode(MsgCode.paramsError);
    		renderJSON(baseBean);
    		return;
    	}
		if(StringUtils.isBlank(gender)){
			baseBean.setMsg("gender 不能为空!");
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
		
		try {
			//获取数据库的用户信息
			UserBindAlipay userBindAlipay = accountService.queryUserBindAlipayByUserid(userId);
			if(userBindAlipay == null){
				logger.info("【支付宝卡包】用户授权异常");
				//用户授权异常
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("用户授权异常!");
				renderJSON(baseBean);
				return;
			}
			
			String certNo = userBindAlipay.getIdCard();
			String realName = userBindAlipay.getRealName();
			String mobileNumber = userBindAlipay.getMobileNumber();
			
			//是否已领取证件
			int count = alipayService.queryReceiveCardCount(certNo, CardReceiveConstants.CARD_RECEIVE_TYPE_CAR);
			if(count > 0){
				logger.info("【支付宝卡包】跳转到已领取页面");
				//已领取，跳转到领取成功页面
				baseBean.setCode(MsgCode.businessError);
				baseBean.setMsg("您好！您的深圳交警电子行驶证已经领取");
				renderJSON(baseBean);
				return;
			}else{
				//未领取行驶证
				
				logger.info("【支付宝卡包】未领取行驶证");
				//调JST接口获取所有绑定机动车
				List<BindTheVehicleVo> bndTheVehicles = accountService.getBndTheVehicles(certNo, mobileNumber, sourceOfCertification);
				for (BindTheVehicleVo bindTheVehicleVo : bndTheVehicles) {
					String code = bindTheVehicleVo.getCode();
					if(code != null){
						//没有绑定机动车
						baseBean.setCode(code);
						baseBean.setMsg(bindTheVehicleVo.getMsg());
						renderJSON(baseBean);
						return;
					}
					
					//查询电子证件
					String plateNo = bindTheVehicleVo.getNumberPlateNumber();
					String plateType = bindTheVehicleVo.getPlateType();
					String mobilephone = bindTheVehicleVo.getMobilephone();
					DrivingLicenseVo eCardInfo = accountService.getDrivingLicense(plateNo, plateType, mobilephone, sourceOfCertification);
					if(MsgCode.success.equals(eCardInfo.getCode())){
						String eCardImgBase64 = eCardInfo.getElectronicDrivingLicense();//证件图片base64
						//String encryptQRCodeBase64 = eCardInfo.getElectronicDrivingLicenseQRCode();//加密二维码base64
						JSONObject json = new JSONObject();
						json.put("file_type", "trans_picture");
						json.put("type_id", "cif_electronic_driving_shenzhen_police_pic");
						json.put("file_description", "深圳交警电子行驶证正面");
						json.put("file_name", "driving_license.png");
						json.put("file", eCardImgBase64);
						//调支付宝sdk上传证件照片
						
						//上传成功，调支付宝sdk提交行驶证信息
						//提交失败，提示错误
						
						//上传失败，提示错误
						
					}
				}
				
				//提交成功，写入数据库
				CardReceive cardReceive = new CardReceive();
				cardReceive.setAlipayUserId(userId);
				cardReceive.setArchiveNumber("");//档案编号
				cardReceive.setCertNo(certNo);
				cardReceive.setGender(gender);
				cardReceive.setRealName(realName);
				cardReceive.setType(CardReceiveConstants.CARD_RECEIVE_TYPE_CAR);
				alipayService.insertCardReceive(cardReceive);
				
				logger.info("【支付宝卡包】跳转到领取成功页面");
				//领取成功，跳转到领取成功页面
				baseBean.setCode(MsgCode.success);
				baseBean.setMsg("您好！您的深圳交警电子行驶证已经领取");
				renderJSON(baseBean);
				return;
				
			}
		} catch (Exception e) {
			logger.error("【支付宝卡包】receiveXsCard异常：userId="+userId+",sourceOfCertification="+sourceOfCertification, e);
			DealException(baseBean, e);
		}
	}
	
	
	/**
	 * 驾驶证信息组装
	 * @return
	 */
	public JSONObject jsCardInfo(){
		/*{ "scene_code", "vehicle_driving_licence" },
        { "op_code", "data_save" },
        { "channel", "SZJJ" },
        { "version", "1.0" },
        { "target_id", AliUserInfo["alipay_user_id"].ToString() },
        { "target_id_type", "" },
        { "scene_data", 
            new JObject(){
                { "info", 
                    new JArray(){
                        new JObject(){
                            { "vd_license_number", Data["result"]["sfzmhm"] },                            //驾驶证号
                            { "vd_name", Data["result"]["xm"] },                                          //姓名
                            { "gender", (Data["result"]["xb"].ToString() == "1" ? "男" : "女") },         //性别
                            { "vd_license_u_number", Data["result"]["dabh"] },                            //驾驶证档案编号(U)
                            { "vd_grant_time", "" },                                                      //发证时间
                            { "valid_thru", Data["result"]["yxqz"] },                                     //有效期限
                            { "vd_address", "" },                                                         //住址
                            { "vd_first_time", "" },                                                      //初次领证的时间
                            { "vd_driving_type", Data["result"]["zjcx"] },                                //准驾车型
                            { "vd_nationality", "" },                                                     //国籍
                            { "issuing_authority", "" },                                                  //签发机关
                            { "vd_city_code", "" },                                                       //发证的地市Code
                            { "vd_city_name", "" },                                                       //发证的地市名称
                            { "license_cetify_level", "ORG" },                                            //证件认证等级
                            { "illegal_violation_count", EndorsedNumber },                                //未处理违章条数
                            { "cumulative_score", Data["result"]["ljjf"] },                               //累计记分
                            { "factual_validity_check", Data["result"]["syyxqz"] },                       //审验有效期止
                            { "date_of_inspection", Data["result"]["syrq"] },                             //审验日期
                            { "status", Data["result"]["zt"] },                                           //状态
                            { "cert_doc_type", "SZ_E_DRIVING_LICENSE" },                                  //证件类型
                            { "mobile_no", AliUserInfo["mobile"].ToString() },                                //手机号码
                            { "license_pics",                                                             //驾照图片
                                new JObject(){
                                    { "driving_license_home_page", ImageUploadResult["alipay_zdatafront_datatransfered_fileupload_response"]["result_data"] },                        //驾照正面图片
                                    { "driving_license_sub_page", "" },                                   //驾照副页图片
                                }
                            },
                        }
                    }
                }
            }
        },
		*/
		
		return null;
	}
	
	/**
	 * 行驶证信息组装
	 * @return
	 */
	public JSONObject xsCardInfo(){
		/*{ "plate_no", plate_no },                                                                                       //车牌号
        { "plate_type", carData["result"]["row"][i]["hpzl"].ToString() },                                               //车牌号类型
        { "vehicle_type", "" },                                                                                         //车辆类型
        { "owner", carData["result"]["row"][i]["czxm"].ToString() },                                                    //车辆所有人
        { "owener_cert_no", carData["result"]["row"][i]["czsfzmhm"].ToString() },                                       //车辆所有人身份证号码
        { "vin", carData["result"]["row"][i]["cjh4"].ToString() },                                                      //车辆识别代号
        { "engine_no", "" },                                                                                            //发动机号
        { "check_date", carData["result"]["row"][i]["syrq"].ToString() },                                               //车辆审验日期
        { "whether_one_self", (carData["result"]["row"][i]["sfbr"].ToString() == "本人") ? "是" : "否" },               //是否本人车辆
        { "check_date_remind", "" },                                                                                    //下次审验日期提醒
        { "status_remind", carData["result"]["row"][i]["zt"].ToString() },                                              //状态提醒
        { "abandoned_remind", carData["result"]["row"][i]["qzbfqz"].ToString() },                                       //强制报废期止提醒
        { "mobile_no", AliUserInfo["mobile"].ToString() },                                                              //申领人手机号码
        { "user_character", "" },                                                                                       //使用性质
        { "regisger_date", "" },                                                                                        //注册时间
        { "issue_date", "" },                                                                                           //发证日期
        { "city_code", "" },                                                                                            //车辆归属地
        { "city_name", "" },                                                                                            //车辆归属地名称
        { "ac", "" },                                                                                                   //核载人数
        { "cert_doc_type", "SZ_E_VEHICLE_LICENSE" },                                                                    //类型
        { "license_pics",                                                                                               //行驶证图片
            new JObject(){
                { "vehicle_license_home_page", ImageUploadResult["alipay_zdatafront_datatransfered_fileupload_response"]["result_data"] },                                                                    //行驶证正面图片
                { "vehicle_license_sub_page", "" },                                                                     //行驶证副页图片
            }
        },*/
		
		return null;
	}
	
}
