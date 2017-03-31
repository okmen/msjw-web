package cn.web.front;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import cn.sdk.util.DESUtils;
import cn.web.BaseTester;
import cn.web.front.constants.SecretConstants;
import cn.web.front.filter.PermissionFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
@WebAppConfiguration
public class ActionBaseTester extends BaseTester {

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void setUp() {
     // spring mvc setup
        logger.info("*************** set up MockMvc... ***************");
        PermissionFilter permissionFilter = new PermissionFilter();
        permissionFilter.setNoAccessToken(true);
        this.mockMvc = webAppContextSetup(this.wac)
                .addFilters(permissionFilter)
                .build();
    }


    @After
    public void tearDown() {
        logger.info("*************** tear down MockMvc! ***************");
    }
    
    protected String encryptUserId(long userId) {
        try {
            return DESUtils.encrypt(userId + "", SecretConstants.CHOUMEI_OLD_DESKEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
