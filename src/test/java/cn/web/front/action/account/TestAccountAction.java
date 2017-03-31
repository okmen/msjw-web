package cn.web.front.action.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import cn.sdk.exception.ResultCode;
import cn.web.front.ActionBaseTester;

import com.alibaba.fastjson.JSONObject;


/**
 * 账户模块单元测试
 * @author shengfeng
 */
public class TestAccountAction extends ActionBaseTester {

    @Test
   public void recordDevice() throws Exception {
       String url = "/account/device-reg.html?device-type=Android&device-uuid=3eeb7c42eb39ba04f32726ad5da5846376b2c";
       JSONObject request = new JSONObject();
       request.put("userId", encryptUserId(306782));
       this.mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON)
               .param("request", request.toString()))
               // print
               .andDo(print())
               // status
               .andExpect(status().isOk())
               // content
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               // jsonPath
               .andExpect(jsonPath("$.code").value(ResultCode.SUCCESS));
   }

}
