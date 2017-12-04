package cn.web.front.action.convenience;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;

import cn.convenience.bean.ConvenienceBean;
import cn.convenience.bean.FeedbackResultBean;
import cn.convenience.service.IConvenienceService;
import cn.message.service.ITemplateMessageService;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.WebServiceException;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;
import net.sf.json.JSONObject;

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
    @Qualifier("convenienceService")
    private IConvenienceService convenienceService;
    
    /**
     * 获取民生警务个人信息
     * @param identityCard 身份证号
     * @param sourceOfCertification 认证来源
     */
    @RequestMapping("getMSJWinfo")
    public void getMSJWinfo(String identityCard, String sourceOfCertification){
    	BaseBean baseBean = new BaseBean();	
    	try{
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
	    	
	    	baseBean = convenienceService.getMSJWinfo(identityCard, sourceOfCertification);
	    	
		} catch (Exception e) {
			logger.error("【民生警务】getMSJWinfo接口Action异常: identityCard = " + identityCard + ",sourceOfCertification = " + sourceOfCertification);
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
    }
}
