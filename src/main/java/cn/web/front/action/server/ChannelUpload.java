package cn.web.front.action.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import cn.account.service.IThirdPartyInformationService;
import cn.file.service.IFileService;
import cn.message.service.IMobileMessageService;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.bean.StVo;
import cn.sdk.encryption.RSAEncrypt;
import cn.sdk.encryption.RSASignature;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value="/channelUpload/")
@SuppressWarnings(value="all")
public class ChannelUpload extends BaseAction{
	@Autowired
    @Qualifier("accountService")
	private IAccountService accountService;
	
	@Autowired
    @Qualifier("thirdPartyInformationService")
    private IThirdPartyInformationService thirdPartyInformationService;
    
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
     * @param signstr 签名得到的字符串
     * @param encode 编码，没有默认为UTF-8
     * 
     * 
     * @return void    返回类型 
     * @date 2017年4月20日 下午3:06:02
     */
    @RequestMapping(value = "readilyShoot")
    public void readilyShoot(String licensePlateNumber,String licensePlateType,String illegalActivitieOne, String illegalTime, String illegalSections,
    		String reportImgOne, String reportImgTwo,String reportImgThree, String inputMan,String inputManName,String inputManPhone,
    		String identityCard,String userSource,String openId,String wfxw1,String sign,String encode,String reportImgOneT1,String reportImgOneT2,String reportImgOneT3) {
    	String code=MsgCode.success;
 		StringBuffer sb = new StringBuffer("");
 		int imgNumber=0;//传入的图片数量
    	ReadilyShootVo readilyShootVo = new ReadilyShootVo();
    	if(StringUtil.isBlank(licensePlateNumber)){
    		code=MsgCode.paramsError;
 			sb.append("licensePlateNumber 不能为空");
    	}else{
    		readilyShootVo.setLicensePlateNumber(licensePlateNumber);
    	}
    	if(StringUtil.isBlank(licensePlateType)){
    		code=MsgCode.paramsError;
 			sb.append("licensePlateType 不能为空");
    	}else{
    		readilyShootVo.setLicensePlateType(licensePlateType);	
    	}
    	if(StringUtil.isBlank(illegalActivitieOne)){
 			code=MsgCode.paramsError;
 			sb.append("illegalActivitieOne 不能为空");
 		}else{
 			readilyShootVo.setIllegalActivitieOne(illegalActivitieOne);
 		}
    	if(StringUtil.isBlank(identityCard)){
 			code=MsgCode.paramsError;
 			sb.append("identityCard 不能为空");
 		}else{
 			readilyShootVo.setUserIdCard(identityCard);
 		}	
    	if(StringUtil.isBlank(illegalTime)){
 			code=MsgCode.paramsError;
 			sb.append("illegalTime 不能为空");
 		}else{
 			readilyShootVo.setIllegalTime(illegalTime);
 		}
    	if(StringUtil.isBlank(illegalSections)){
 			code=MsgCode.paramsError;
 			sb.append("illegalSections 不能为空");
 		}else{
 			readilyShootVo.setIllegalSections(illegalSections);
 		}
    	if(StringUtil.isBlank(inputManPhone)){
 			code=MsgCode.paramsError;
 			sb.append("inputManPhone 不能为空");
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
 			sb.append("inputManName 不能为空  ");
 		}else{
 			readilyShootVo.setInputManName(inputManName);
 		}
    	if(StringUtil.isBlank(reportImgOne)){
 			code=MsgCode.paramsError;
 			sb.append("reportImgOne 不能为空");
 		}else{
 			readilyShootVo.setReportImgOne(reportImgOne);
 		}
    	if(StringUtil.isBlank(reportImgTwo)){
 			code=MsgCode.paramsError;
 			sb.append("reportImgTwo 不能为空");
 		}else{
 			readilyShootVo.setReportImgTwo(reportImgTwo);
 		}
    	if(StringUtil.isBlank(reportImgThree)){
 			code=MsgCode.paramsError;
 			sb.append("reportImgThree 不能为空");
 		}else{
 			readilyShootVo.setReportImgThree(reportImgThree);
 		}
    	if(StringUtil.isBlank(userSource)){
 			code=MsgCode.paramsError;
 			sb.append("userSource 不能为空");
 		}else{
 			readilyShootVo.setUserSource(userSource);
 		}
    	if(StringUtil.isBlank(sign)){
 			code=MsgCode.paramsError;
 			sb.append("sign 不能为空");
 		}else{
 			readilyShootVo.setUserSource(userSource);
 		}
    	
    	/*if(StringUtil.isBlank(openId)){
 			code=MsgCode.paramsError;
 			sb.append("微信openid为空  ");
 		}else{
 			readilyShootVo.setOpenId(openId);
 		}*/
       	BaseBean basebean = new  BaseBean();
       	ReadilyShoot readilyShoot = new ReadilyShoot();
    	try {
    		 if(MsgCode.success.equals(code)){//参数校验通过
    			 //签名校验
    			 Map<String, Object> map = new HashMap<String, Object>();
    			 map.put("licensePlateNumber", licensePlateNumber);
    			 map.put("licensePlateType", licensePlateType);
    			 map.put("illegalActivitieOne", illegalActivitieOne);
    			 map.put("illegalTime", illegalTime);
    			 map.put("illegalSections", illegalSections);
    			 map.put("reportImgOne", reportImgOne);
    			 map.put("reportImgTwo", reportImgTwo);
    			 map.put("reportImgThree", reportImgThree);
    			 map.put("inputManName", inputManName);
    			 map.put("inputManPhone", inputManPhone);
    			 map.put("identityCard", identityCard);
    			 map.put("userSource", userSource);
    			 
    			 if(StringUtils.isBlank(encode)){
    				 encode = "UTF-8";
    			 }
    			 String sourceSign = createSourceSign(map);
    			 String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkyuU+H+gUIUOGazKey9wnryFjlRutBEuE4gpuVDgDG7Dji5OXJPV6U+p7r85D8rUQwDd0RLXKBlANd4/fkK8ZLF6uaedAZT27GrS1Q5+s8JpQS8eWSuUUXmfhdabtNsee9v3/Xwij7n1OBPCugaO29V3E/X0IuH09ZUDYHnzBbQIDAQAB";
    			 String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKTK5T4f6BQhQ4ZrMp7L3CevIWOVG60ES4TiCm5UOAMbsOOLk5ck9XpT6nuvzkPytRDAN3REtcoGUA13j9+QrxksXq5p50BlPbsatLVDn6zwmlBLx5ZK5RReZ+F1pu02x572/f9fCKPufU4E8K6Bo7b1XcT9fQi4fT1lQNgefMFtAgMBAAECgYBxITT1TDQntZpEqkzrBOqTd8AFPDTutxcdF4yvpzN4tsbdv1FIHsBDBV7hIJUKwpEY+cxYDl96XJESXbUwdoQtNELT5HtioK/ax/W4vYn/JLr82LAU3BI38VZt5xJEcbZkk9NNpzFLON9NBGZlxZeeJx1SjsF1xIu3VbVfkrhZBQJBAP8qvbNEZPMT3ZSP2arUULGBLhvMRkNxmLCicPPmOblAy+vqAshBMOOd+6FOy9n9KO6Z2Lag0MHVek0TORGF/+cCQQClVJ94Lkvf8bUxs3xYJVJL0gFC1r7RmanCWCH4YhWdbnsV6Z2zA2iVUDxAQU68rEwnjeL6lddkb5op5gt6sNmLAkBYi6ZkjPlS+LPNl9V62E5gKmmrr8k6IjNQKC52FJA3XurkpMHuZ+DlO69UHJnUvWr00WJIjamjeccE9AS7tfpFAkAdI8gjp41KAFEeXHM7GgDiSjZcsHrbeIj6Lwx1udvzboiVOSLJbX1ea9Dixl/5UyjtA84Qup5HHoB+iARX8Jm3AkEAoZkMewiaiK98GjAlZbzMuvtNqHjYiq6Ie/k1QpPKNxQ7a7sSBPN7NiZL/AghCf7VHNxbPHbIM8hBY35zSNmsZw==";
    			 String sign111 = RSASignature.sign(sourceSign,privateKey,"UTF-8");
    			 boolean validateResult = RSASignature.doCheck(sourceSign, sign111, publicKey,encode);
    			 
    			 if(validateResult){
    				JSONObject json = thirdPartyInformationService.readilyShoot(readilyShootVo);
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
 						 //举报成功发送模板消息
     					/*try {
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
 						}*/
     				}
     		    	basebean.setCode(code);
     		    	basebean.setMsg(json.getString("msg"));
    			 }else{
    				
    			 }
    		 }else{
    			 basebean.setCode(code);
    			 basebean.setMsg(sb.toString());
    		 }
    	} catch (Exception e) {
    		DealException(basebean, e);
    		logger.error("readilyShoot出错",e);
		}
    	try {
    		//sendReadilyShootVoDataToPhp(readilyShoot,readilyShootVo);
		} catch (Exception e) {
			logger.error("随手拍发送数据给php系统 错误", e);
		}
    	renderJSON(basebean);
    	logger.debug(JSON.toJSONString(basebean));
    }
    
    /**
     * 获取需要签名的参数
     * @param parameters
     * @return
     */
    public static String createSourceSign(Map<String,Object> parameters){  
        StringBuffer sb = new StringBuffer();  
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(parameters.entrySet());  
        //排序方法  
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {     
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) { 
                return (o1.getKey()).toString().compareTo(o2.getKey());  
            }  
        });
        for (final Map.Entry<String, Object> m : infoIds) {
           // System.out.println(m.getKey() + ":" + m.getValue());
            String k = (String)m.getKey();  
            Object v = m.getValue();  
            if(null != v && !"".equals(v)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }
        String sourceSign = sb.toString();
        sourceSign = sourceSign.substring(0, sourceSign.length() - 1);
        return sourceSign;
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
			 json  = thirdPartyInformationService.getPositioningAddress(keyword);
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
     * http://localhost/web/channelUpload/queryAccountValid.html
     * 查询是否星级用户
     * @throws Exception
     */
    @RequestMapping(value = "queryAccountValid")
    public void queryAccountValid() throws Exception {
		BaseBean basebean = new BaseBean();
		try {
			 List<DrivingLicense> drivingLicenses = thirdPartyInformationService.getLicensePlateTypes();
			 basebean.setCode("00");
			 basebean.setMsg("查询成功");
			 basebean.setData(drivingLicenses);
		} catch (Exception e) {
			logger.error("queryAccountValid出错",e);
		}
		logger.debug(JSON.toJSONString(basebean));
		renderJSON(basebean);
	}
}

