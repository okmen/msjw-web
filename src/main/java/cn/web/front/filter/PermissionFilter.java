package cn.web.front.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.account.service.IAccountService;
import cn.sdk.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PermissionFilter extends HttpServlet implements Filter {

    private static final long serialVersionUID = 9031161978940345250L;

    private static final List<String> FILTER_URI = Arrays.asList(
    		"/wechat/doGet.html"
			);
    
    private FilterConfig filterConfig;
    private boolean noAccessToken = false;
    private Set<String> excludeUri = new HashSet<>();

    @Autowired
    private IAccountService accountService;


    private final static Logger logger = LoggerFactory.getLogger(PermissionFilter.class);

    public void setNoAccessToken(boolean noAccessToken) {
        this.noAccessToken = noAccessToken;
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

    	HttpServletResponse response = (HttpServletResponse) arg1;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        
        final HttpServletRequest request = (HttpServletRequest) arg0;

        if (logger.isInfoEnabled()) {
            this.printRequest(request);
        }

        arg2.doFilter(arg0, arg1);
        return;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        this.filterConfig = arg0;

        final String excludeUriParameter = filterConfig.getInitParameter("excludeUri");

        if (StringUtils.isNotBlank(excludeUriParameter)) {

            final String[] slices = excludeUriParameter.split(";");

            for (String slice : slices) {

                if (StringUtils.isNotBlank(slice)) {
                    excludeUri.add(slice.trim());
                }
            }
        }
    }


    private void printRequest(HttpServletRequest request) {

    	String requestURI = request.getRequestURI();
    	logger.info("URI: " + requestURI);
        
    	if(StringUtil.isNotBlank(requestURI) && FILTER_URI.contains(requestURI)){
    		return;
    	}

        @SuppressWarnings("unchecked")
        Map<String, String[]> parameters = request.getParameterMap();

        for (Map.Entry<String, String[]> entity : parameters.entrySet()) {

        	if ("SHJYPXB,YQZMZP,JDCXSZ,RZZP,STTJSQB,scenePhoto,copyOfOwnerIdentityCard,copyOfDriverLicense,copyOfVehicleTravelLicense,copyOfLegalEntity,copyOfLegalEntityA,copyOfApplicant,photo6,photo32,photo33,photo9,XSZZP,JSZZP,JZZA,JZZB,DJZSFYJ,PHOTO26,PHOTO27,PHOTO9,PHOTO10,PHOTO28,PHOTO31,PHOTO29,delayphoto,driverLicensePhoto,IDCardPhoto1,IDCardPhoto2,livePhoto1,livePhoto2,educationDrawingtable,idCardImgPositive,idCardImgHandHeld,idCardImgNegative,reportImgOne,reportImgTwo,reportImgThree,ownerIdCardImgPositive,ownerIdCardImgHandHeld,sceneImg".contains(entity.getKey())) {
        		logger.info(String.format("%s = %s", entity.getKey(), "照片流字符太长，不打印"));
			}else {
				logger.info(String.format("%s = %s", entity.getKey(), StringUtils.join(entity.getValue(), ",")));
			}
        }

    }
}