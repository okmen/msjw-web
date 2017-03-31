package cn.web.front.support;

import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * Commons constants.
 * 
 * @author wubinhong
 * @since 5.5
 */
public class Consts {

    /** encoding*/
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    
    /** environment variables: dev / test*/
    public static final int ENV_DEV = 0;
    /** environment variables: uat / online*/
    public static final int ENV_ONLINE = 1;
    
    //代预约造型师推荐码前缀
    public static final String AGENT_FLAG = "a";
    
    public static final BigDecimal DEV_PAYABLE = new BigDecimal("0.01");
}
