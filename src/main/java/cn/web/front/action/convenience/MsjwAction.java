package cn.web.front.action.convenience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.vo.BrushFaceVo;
import cn.account.service.IAccountService;
import cn.convenience.bean.SzjjToken;
import cn.convenience.service.IFaceautonymService;
import cn.convenience.service.IMsjwService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;

/**
 * 
 * @ClassName: MsjwAction 
 * @Description: TODO(民生警务Action) 
 * @author jjy 
 * @date 2017年12月02日 上午10:19:49 
 *
 */
@Controller
@RequestMapping(value="/msjw")
public class MsjwAction extends BaseAction{
	private final static Logger logger = LoggerFactory.getLogger(MsjwAction.class);

    @Autowired
    @Qualifier("msjwService")
    private IMsjwService msjwService;
    
    @Autowired
    @Qualifier("accountService")
    private IAccountService accountService;
    
    @Autowired
	@Qualifier("faceautonymService")
	private IFaceautonymService faceautonymService;
    /**
     * 获取民生警务个人信息
     * @param identityCard 身份证号
     * @param sourceOfCertification 认证来源
     */
    @RequestMapping("getMSJWinfo")
    public void getMSJWinfo(String openId, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();	
    	try{
    		if(StringUtil.isBlank(openId)){
	 			baseBean.setCode(MsgCode.paramsError);
	 			baseBean.setMsg("openId不能为空!");
	 			renderJSON(baseBean);
	 			return;
	      	}
	    	if(StringUtil.isBlank(sourceOfCertification)){
	    		baseBean.setCode(MsgCode.paramsError);
	    		baseBean.setMsg("sourceOfCertification不能为空!");
	    		renderJSON(baseBean);
	    		return;
	    	}else{
	    		if(!"M".equals(sourceOfCertification)){
	    			baseBean.setCode(MsgCode.paramsError);
		    		baseBean.setMsg("sourceOfCertification非法!");
		    		renderJSON(baseBean);
		    		return;
	    		}
	    	}
	    	
	    	//根据openid获取民生警务平台用户信息
	    	JSONObject json = msjwService.getUserInfoFromMsjw(openId);
	    	String code = json.getString("code");
	    	
	    	//用户已登录，拉取警视通用户信息
	    	if("200".equals(code)){
	    		JSONArray jsonArray = json.getJSONArray("datas");
				for(int i = 0; i < jsonArray.size(); i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (jsonObject != null) {
						String loginType = jsonObject.getString("loginType");//个人用户信息(loginType=1)
						if("1".equals(loginType)){//1-个人 2-企业
							//authStatus 3-已认证
							String authStatus = jsonObject.getString("authStatus");
							if(!"3".equals(authStatus)){//未认证通过，返回状态
								baseBean.setCode(MsgCode.businessError);
								JSONObject authStatusMap = new JSONObject();
								authStatusMap.put("authStatus", authStatus);
					    		baseBean.setData(authStatusMap);
					    		renderJSON(baseBean);
					    		return;
							}
							
							String identityId = jsonObject.getString("identityId");
				    		if(StringUtil.isBlank(identityId)){
				    			baseBean.setCode(MsgCode.businessError);
					    		baseBean.setMsg("获取identityId为空！");
					    		renderJSON(baseBean);
					    		return;
				    		}else{
				    			baseBean = msjwService.getMSJWinfo(identityId, sourceOfCertification);
				    			//未注册星级用户,调用一键注册接口
				    			if("0001".equals(baseBean.getCode())){
				    				SzjjToken szjjToken = faceautonymService.querySzjjToken(identityId);
				    				logger.info("【民生警务】警视通多合一接口返回结果： baseBean = " + net.sf.json.JSONObject.fromObject(baseBean));
				    				BrushFaceVo bf = new BrushFaceVo();
				    				bf.setIdentityCard(identityId);
				    				bf.setMobilephone(jsonObject.getString("phone"));
				    				bf.setName(jsonObject.getString("username"));
				    				bf.setOpenId(openId);
				    				bf.setPhoto6(""); //图片为空 — 当事人手持身份证图片
				    				bf.setUserSource("M"); //民生警务来源
									bf.setCertificationType("4"); // 4-自然人
									if (szjjToken != null) {
										bf.setToken(szjjToken.getToken());
									}
				    				logger.info("【民生警务】刷脸一键注册接口请求参数： BrushFaceVo = " + bf);
									baseBean = accountService.weChatBrushFaceAuthentication(bf);
									logger.info("【民生警务】刷脸一键注册接口返回结果： baseBean = " + net.sf.json.JSONObject.fromObject(baseBean));
				    			}
				    		}
						}
					}
				}
	    	}else{
	    		baseBean.setCode(code);
	    		baseBean.setMsg(json.getString("message"));
	    	}
	    	
		} catch (Exception e) {
			logger.error("【民生警务】getMSJWinfo接口Action异常: openId = " + openId + ",sourceOfCertification = " + sourceOfCertification);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
}
