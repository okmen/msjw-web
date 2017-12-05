package cn.web.front.action.convenience;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
    
    /**
     * 获取民生警务个人信息
     * @param identityCard 身份证号
     * @param sourceOfCertification 认证来源
     */
    @RequestMapping("getMSJWinfo")
    public void getMSJWinfo(String openId, String identityCard, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();	
    	try{
    		if(StringUtil.isBlank(openId)){
	 			baseBean.setCode(MsgCode.paramsError);
	 			baseBean.setMsg("openId不能为空!");
	 			renderJSON(baseBean);
	 			return;
	      	}
	    	if(StringUtil.isBlank(identityCard)){
	 			baseBean.setCode(MsgCode.paramsError);
	 			baseBean.setMsg("identityCard不能为空!");
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
	    	
	    	//校验是否为民生警务平台合法用户
	    	/*JSONObject json = msjwService.checkIsValidUser(openId, identityCard);
	    	String code = json.getString("code");
	    	
	    	if("200".equals(code)){*/
	    		//用户已登录，拉取警视通用户信息
	    		baseBean = msjwService.getMSJWinfo(identityCard, sourceOfCertification);
	    	/*}else{
	    		baseBean.setCode(code);
	    		baseBean.setMsg(json.getString("message"));
	    	}*/
	    	
		} catch (Exception e) {
			logger.error("【民生警务】getMSJWinfo接口Action异常: identityCard = " + identityCard + ",sourceOfCertification = " + sourceOfCertification);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
}
