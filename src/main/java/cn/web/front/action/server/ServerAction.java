package cn.web.front.action.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.DrivingLicense;
import cn.account.bean.ReadilyShoot;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.service.IAccountService;
import cn.file.service.IFileService;
import cn.message.model.wechat.TemplateDataModel;
import cn.message.model.wechat.TemplateDataModel.Property;
import cn.message.service.IMobileMessageService;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value="/server/")
@SuppressWarnings(value="all")
public class ServerAction extends BaseAction{
	@Autowired
    @Qualifier("accountService")
    private IAccountService accountService;
    
    @Autowired
    @Qualifier("mobileMessageService")
    private IMobileMessageService mobileMessageService;
    
    @Autowired
    @Qualifier("fileService")
    private IFileService fileService;
    
    @Autowired
	@Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;
	/**
     * 随手拍
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
    public void readilyShoot(String licensePlateNumber,String licensePlateType,String illegalActivitieOne, String illegalTime, String illegalSections,
    		String reportImgOne, String reportImgTwo,String reportImgThree, String inputMan,String inputManName,String inputManPhone,
    		String identityCard,String userSource,String openId,String wfxw1) {
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
       	ReadilyShoot readilyShoot = new ReadilyShoot();
    	try {
    		 if(MsgCode.success.equals(code)){//参数校验通过
    			 JSONObject json = accountService.readilyShoot(readilyShootVo);
    				code =json.getString("code");
    				String msg=json.getString("msg");
    				if(!MsgCode.success.equals(code)){
    					code=MsgCode.businessError;
    				}else{
    					String reportSerialNumber = msg.substring(5, 20);
						String password = json.getString("cxyzm");
						
    					Map<String, Object> modelMap = new HashMap<String, Object>();
    			     	modelMap.put("recordNumber", reportSerialNumber);
    			     	modelMap.put("queryPassword", password);
    			     	basebean.setData(modelMap);
    			     	
    			     	List<String> base64Imgs = new ArrayList<String>();
    			     	if(StringUtils.isNotBlank(reportImgOne)){
    			     		base64Imgs.add(reportImgOne);
    			     	}
    			     	if(StringUtils.isNotBlank(reportImgTwo)){
    			     		base64Imgs.add(reportImgTwo);
    			     	}
    			     	if(StringUtils.isNotBlank(reportImgThree)){
    			     		base64Imgs.add(reportImgThree);
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
						 //举报成功发送模板消息
    					try {
    						String templateId = "pFy7gcEYSklRmg32165BUBwM3PFbUbBSLe0IPw3ZuY4";
        					String url = "http://szjj.u-road.com/h5/#/takePicturesSuccess1?reportSerialNumber=" + reportSerialNumber + "&password=" + password;
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
    					readilyShoot.setIllegalImg1(reportImgOne);
    					readilyShoot.setIllegalImg2(reportImgTwo);
    					readilyShoot.setIllegalImg3(reportImgThree);
    					readilyShoot.setSituationStatement(illegalActivitieOne);
    					accountService.saveReadilyShoot(readilyShoot);
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
    	/*try {
    		sendReadilyShootVoDataToPhp(readilyShoot,readilyShootVo);
		} catch (Exception e) {
			logger.error("随手拍发送数据给php系统 错误", e);
		}*/
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    }
    /**
     * http://localhost/web/server/getPositioningAddress.html
     * 根据关键字获取违法地点
     * @param keyword 关键字
     * @throws Exception
     */
    @RequestMapping(value = "getPositioningAddress",method = RequestMethod.GET)
    public void getPositioningAddress(String keyword) throws Exception {
    	JSONObject json= null;
		BaseBean basebean = new BaseBean();
    	if(StringUtils.isBlank(keyword)){
    		basebean.setMsg("keyword 不能为空!");
    		basebean.setCode(MsgCode.paramsError);
    		renderJSON(basebean);
    		return;
    	}
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
     * http://localhost/web/server/getLicensePlateTypes.html
     * 号牌类型查询接口
     * @throws Exception
     */
    @RequestMapping(value = "getLicensePlateTypes",method = RequestMethod.GET)
    public void getLicensePlateTypes() throws Exception {
		JSONObject json= null;
		BaseBean basebean = new BaseBean();
		try {
			 List<DrivingLicense> drivingLicenses = new ArrayList<DrivingLicense>();
			 DrivingLicense drivingLicense1 = new DrivingLicense("02", "蓝牌");
			 DrivingLicense drivingLicense2 = new DrivingLicense("01", "黄牌");
			 DrivingLicense drivingLicense3 = new DrivingLicense("06", "黑牌");
			 DrivingLicense drivingLicense4 = new DrivingLicense("02", "个性牌");
			 DrivingLicense drivingLicense5 = new DrivingLicense("02", "小型新能源车号牌");
			 DrivingLicense drivingLicense6 = new DrivingLicense("02", "大型新能源车号牌");
			 drivingLicenses.add(drivingLicense1);
			 drivingLicenses.add(drivingLicense2);
			 drivingLicenses.add(drivingLicense3);
			 drivingLicenses.add(drivingLicense4);
			 drivingLicenses.add(drivingLicense5);
			 drivingLicenses.add(drivingLicense6);
			 basebean.setCode("0000");
			 basebean.setMsg("查询成功");
			 basebean.setData(drivingLicenses);
		} catch (Exception e) {
			logger.error("getLicensePlateTypes出错",e);
		}
		renderJSON(basebean);
		logger.debug(JSON.toJSONString(basebean));
	}
}

