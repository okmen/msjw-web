package cn.web.front.action.illegal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.account.service.IAccountService;
import cn.illegal.bean.appealInfoBean;
import cn.illegal.bean.illegalBusiness;
import cn.illegal.bean.illegalInfoBean;
import cn.illegal.bean.illegalInfoSheet;
import cn.illegal.bean.messageBean;
import cn.sdk.bean.BaseBean;
import cn.sdk.exception.ResultCode;
import cn.sdk.util.StringUtil;
import cn.web.front.support.BaseAction;


/**
 * 违法处理
 * 
 * @author lutaijun
 * 
 */
@Controller
@RequestMapping(value = "illegalHanding/")
public class IllegalAction extends BaseAction {
    private final static Logger logger = LoggerFactory.getLogger(IllegalAction.class);

    @Autowired
    @Qualifier("accountService")
    private IAccountService accountService;

  
    /**
     * 
     * 获取业务列表
     */
    @RequestMapping(value = "illegalBusinessListQuery")
    public void getBusinessInfos(){
    	List<illegalBusiness> list=new ArrayList<illegalBusiness>();
    	illegalBusiness bean1=new illegalBusiness();
    	bean1.setBusinessName("交通违法查询");
    	bean1.setAction("xxx");
    	bean1.setBusinessCode("M001");
    	
    	illegalBusiness bean2=new illegalBusiness();
    	bean2.setBusinessName("违法在线确认");
    	bean2.setAction("xxx");
    	bean2.setBusinessCode("M002");
    	
    	
    	List<illegalBusiness> clids=new ArrayList<illegalBusiness>();
    	
    	illegalBusiness clid1=new illegalBusiness();
    	clid1.setBusinessName("违法缴款");
    	clid1.setAction("xxx");
    	clid1.setBusinessCode("M011");
    	clid1.setParentCode("M003");
    	
    	illegalBusiness clid2=new illegalBusiness();
    	clid2.setBusinessName("交通违法预约处理");
    	clid2.setAction("xxx");
    	clid2.setBusinessCode("M012");
    	clid2.setParentCode("M003");
    	
    	illegalBusiness clid3=new illegalBusiness();
    	clid3.setBusinessName("交通违法申述");
    	clid3.setAction("xxx");
    	clid3.setBusinessCode("M013");
    	clid3.setParentCode("M003");
    	clids.add(clid1);
    	clids.add(clid2);
    	clids.add(clid3);
    	
    	illegalBusiness bean3=new illegalBusiness();
    	bean3.setBusinessName("违法在线处理");
    	bean3.setAction("xxx");
    	bean3.setBusinessCode("M013");
    	bean3.setChildren(clids);
    
    	list.add(bean1);
    	list.add(bean2);
    	list.add(bean3);
    	BaseBean base=new BaseBean();
    	base.setCode("0000");
    	base.setMsg("成功！");
    	base.setData(list);
    	renderJSON(base);
    }
    
    
   /**
    * 根据车牌号获取违章信息
    * @param licensePlateNo  车牌号
    * @param licensePlateType 车辆类型
    * @param vehicleIdentifyNoLast4 车架号后四位
    */
   @RequestMapping(value = "queryInfoByLicensePlateNo") 
   public void queryInfoByLicensePlateNo(String licensePlateNo,String licensePlateType,String vehicleIdentifyNoLast4){
	   List<illegalInfoBean> list=new ArrayList<illegalInfoBean>();
	   
	   illegalInfoBean bean=new illegalInfoBean();
	   bean.setBillNo("000000000001");
	   bean.setCarOwner("李四");
	   bean.setDealType("05");
	   bean.setIllegalAddress("深南大道");
	   bean.setIllegalDesc("闯红灯");
	   bean.setIllegalTime("2015-10-10 09:09:00");
	   bean.setIllegalUnit("南山交警队");
	   bean.setLicensePlateNo("粤A00001");
	   bean.setLicensePlateType("06");
	   bean.setPunishAmount(200);
	   bean.setPunishScore(3);
	
	   list.add(bean);
	   BaseBean base=new BaseBean();
	   base.setCode("0000");
	   base.setMsg(licensePlateNo);
	   base.setData(list);
	   renderJSON(base);
   }
      
   /**
    * 根据驾驶证号获取违章记录
    * @param drivingLicenceNo 驾驶证号
    * @param recordNo 档案编号
    */
   @RequestMapping(value = "queryInfoByDrivingLicenceNo")
   public void queryInfoByDrivingLicenceNo(String drivingLicenceNo,String recordNo){
	   List<illegalInfoBean> list=new ArrayList<illegalInfoBean>();
	   illegalInfoBean bean=new illegalInfoBean();
	   bean.setBillNo("000000000001");
	   bean.setCarOwner("李四");
	   bean.setDealType("05");
	   bean.setIllegalAddress("深南大道");
	   bean.setIllegalDesc("闯红灯");
	   bean.setIllegalTime("2015-10-10 09:09:00");
	   bean.setIllegalUnit("南山交警队");
	   bean.setLicensePlateNo("粤A00001");
	   bean.setLicensePlateType("06");
	   bean.setPunishAmount(200);
	   bean.setPunishScore(3);
	
	   list.add(bean);
	   
	   BaseBean base=new BaseBean();
	   base.setCode("0000");
	   base.setMsg("成功！");
	   base.setData(list);
	   renderJSON(base);
   } 
    
   
    /**
     * 违章在线确认
     * @param licensePlateNo 车牌号
     * @param licensePlateType 车辆类型
     * @param mobilephone 手机号码
     */
    @RequestMapping(value = "illegalOnlineConfirm")
    public void illegalOnlineConfirm(String licensePlateNo,String licensePlateType,String mobilephone){
 	   List<illegalInfoBean> list=new ArrayList<illegalInfoBean>();
 	   illegalInfoBean bean=new illegalInfoBean();
	   bean.setBillNo("000000000001");
	   bean.setCarOwner("李四");
	   bean.setDealType("05");
	   bean.setIllegalAddress("深南大道");
	   bean.setIllegalDesc("闯红灯");
	   bean.setIllegalTime("2015-10-10 09:09:00");
	   bean.setIllegalUnit("南山交警队");
	   bean.setLicensePlateNo("粤A00001");
	   bean.setLicensePlateType("06");
	   bean.setPunishAmount(200);
	   bean.setPunishScore(3);
	
	   list.add(bean);
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(list);
 	   renderJSON(base);
    } 
     
    /**
     * 打单确认
     * @param illegalNo 违章编号
     */
    @RequestMapping(value = "trafficIllegalClaim")
    public void trafficIllegalClaim(String illegalNo){	   
       illegalInfoSheet bean=new illegalInfoSheet();
	   bean.setBillNo("000000000001");
	   bean.setCarOwner("李四");
	   bean.setDealType("05");
	   bean.setIllegalAddress("深南大道");
	   bean.setIllegalDesc("闯红灯");
	   bean.setIllegalTime("2015-10-10 09:09:00");
	   bean.setIllegalUnit("南山交警队");
	   bean.setLicensePlateNo("粤A00001");
	   bean.setLicensePlateType("06");
	   bean.setPunishAmount(200);
	   bean.setPunishScore(3);
	   bean.setAgency("南山交警队");
	   bean.setDealPolice("小明");
	   bean.setTotalScore(6);
	   bean.setWaitingDealNum(1);
	   bean.setReturnMsg("打单成功");
	   
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(bean);
 	   renderJSON(base);
    } 
    
     
    /**
     * 跳转支付页面
     */
    @RequestMapping(value = "toPayPage")
    public void toPayPage(){
 	   Map<String,String> map=new HashMap<String,String>();
       map.put("redirectUrl", "http://szjjapi.chudaokeji.com/yywfcl/services/yywfcl?WSDL ");
 	   
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(map);
 	   renderJSON(base);
    } 
    
    
    /**
     * 违章缴款信息查询
     * @param billNo 违章编号
     * @param licensePlateNo 车牌号
     * @param mobilephone 手机号码
     */
    @RequestMapping(value = "toQueryPunishmentPage")
    public void toQueryPunishmentPage(String billNo,String  licensePlateNo,String mobilephone){
 	   List<illegalInfoBean> list=new ArrayList<illegalInfoBean>();
 	   illegalInfoBean bean=new illegalInfoBean();
	   bean.setBillNo("000000000001");
	   bean.setCarOwner("李四");
	   bean.setDealType("05");
	   bean.setIllegalAddress("深南大道");
	   bean.setIllegalDesc("闯红灯");
	   bean.setIllegalTime("2015-10-10 09:09:00");
	   bean.setIllegalUnit("南山交警队");
	   bean.setLicensePlateNo("粤A00001");
	   bean.setLicensePlateType("06");
	   bean.setPunishAmount(200);
	   bean.setPunishScore(3);
	
	   list.add(bean);
 	   
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(list);
 	   renderJSON(base);
    } 
    
    
    /**
     * 预约/取消预约
     * @param billNo
     * @param licensePlateNo
     * @param mobilephone
     */
    @RequestMapping(value = "toChangeSubscribe")
    public void toChangeSubscribe(String billNo,String  licensePlateNo,String mobilephone){
       messageBean  bean=new messageBean();
       bean.setBusinessType("01");
       bean.setSubscribeNo("000000000123");
       bean.setReminder("预约成功");
       
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(bean);
 	   renderJSON(base);
    } 
    
    
    /**
     * 预约查询
     * @param billNo
     * @param licensePlateNo
     * @param mobilephone
     */
    @RequestMapping(value = "toQuerySubscribe")
    public void toQuerySubscribe(String billNo,String  licensePlateNo,String mobilephone){
 	   List<illegalInfoBean> list=new ArrayList<illegalInfoBean>();
 	   illegalInfoBean bean=new illegalInfoBean();
	   bean.setBillNo("000000000001");
	   bean.setCarOwner("李四");
	   bean.setDealType("05");
	   bean.setIllegalAddress("深南大道");
	   bean.setIllegalDesc("闯红灯");
	   bean.setIllegalTime("2015-10-10 09:09:00");
	   bean.setIllegalUnit("南山交警队");
	   bean.setLicensePlateNo("粤A00001");
	   bean.setLicensePlateType("06");
	   bean.setPunishAmount(200);
	   bean.setPunishScore(3);
	
	   list.add(bean);
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(list);
 	   renderJSON(base);
    } 
   
    /**
     * 提交申诉
     * @param info 申诉信息
     */
    @RequestMapping(value = "trafficIllegalAppeal")
    public void trafficIllegalAppeal(appealInfoBean info){
       messageBean  bean=new messageBean();
       bean.setBusinessType("03");
       bean.setSubscribeNo("000000000123");
       bean.setReminder("申诉成功");
	   
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(bean);
 	   renderJSON(base);
    } 
   
   
    /**
     * 查询申诉
     * @param identityCard 身份证
     */
    @RequestMapping(value = "trafficIllegalAppealFeedback")
    public void trafficIllegalAppealFeedback(String identityCard){
       appealInfoBean bean =new appealInfoBean();
       bean.setBillNo("000000000001");
 	   bean.setIllegalAddress("深南大道");
 	   bean.setIllegalDesc("闯红灯");
 	   bean.setIllegalTime("2015-10-10 09:09:00");
 	   bean.setPunishAmount(200);
 	   bean.setPunishScore(3);
 	   bean.setAgency("南山交警队");
 	   bean.setAppealType("01");
 	   bean.setAppealContent("处罚有误");
 	   bean.setClaimant("薛申");
 	   bean.setClaimantAddress("平洲三路");
 	   bean.setClaimantPhone("13898028923");
 	   BaseBean base=new BaseBean();
 	   base.setCode("0000");
 	   base.setMsg("成功！");
 	   base.setData(bean);
 	   renderJSON(base);
    }
}
