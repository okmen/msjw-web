package cn.web.front.action.illegal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.service.IAccountService;
import cn.illegal.bean.AppealInfoBack;
import cn.illegal.bean.AppealInfoBean;
import cn.illegal.bean.CarInfoBean;
import cn.illegal.bean.CustInfoBean;
import cn.illegal.bean.IllegalBusiness;
import cn.illegal.bean.IllegalInfoBean;
import cn.illegal.bean.IllegalInfoClaim;
import cn.illegal.bean.IllegalInfoSheet;
import cn.illegal.bean.IllegalProcessPointBean;
import cn.illegal.bean.MessageBean;
import cn.illegal.bean.ReservationDay;
import cn.illegal.bean.SubcribeBean;
import cn.illegal.service.IIllegalService;
import cn.sdk.bean.BaseBean;
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
    @Qualifier("illegalService")
    private IIllegalService illegalService;

    @Autowired
    @Qualifier("accountService")
    private IAccountService accountService;
    
    /**
     * 
     * 获取业务列表
     */
    @RequestMapping(value = "illegalBusinessListQuery")
    public void getBusinessInfos(){
    	List<IllegalBusiness> list=new ArrayList<IllegalBusiness>();
    	IllegalBusiness bean1=new IllegalBusiness();
    	bean1.setBusinessName("交通违法查询");
    	bean1.setAction("xxx");
    	bean1.setBusinessCode("M001");
    	
    	IllegalBusiness bean2=new IllegalBusiness();
    	bean2.setBusinessName("违法在线确认");
    	bean2.setAction("xxx");
    	bean2.setBusinessCode("M002");
    	
    	
    	List<IllegalBusiness> clids=new ArrayList<IllegalBusiness>();
    	
    	IllegalBusiness clid1=new IllegalBusiness();
    	clid1.setBusinessName("违法缴款");
    	clid1.setAction("xxx");
    	clid1.setBusinessCode("M011");
    	clid1.setParentCode("M003");
    	
    	IllegalBusiness clid2=new IllegalBusiness();
    	clid2.setBusinessName("交通违法预约处理");
    	clid2.setAction("xxx");
    	clid2.setBusinessCode("M012");
    	clid2.setParentCode("M003");
    	
    	IllegalBusiness clid3=new IllegalBusiness();
    	clid3.setBusinessName("交通违法申述");
    	clid3.setAction("xxx");
    	clid3.setBusinessCode("M013");
    	clid3.setParentCode("M003");
    	clids.add(clid1);
    	clids.add(clid2);
    	clids.add(clid3);
    	
    	IllegalBusiness bean3=new IllegalBusiness();
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
   public void queryInfoByLicensePlateNo(String licensePlateNo,String licensePlateType,String vehicleIdentifyNoLast4,String identityCard, String sourceOfCertification, String mobilephone){  
	   BaseBean base=new BaseBean();
	 
	   try {
		   //判断客户是否已同步
		   String isReg=illegalService.isRegisterUser();
		   //未同步
		   if("1".equals(isReg)){
			   CustInfoBean cust=new CustInfoBean();
			   List<CarInfoBean> carList=new ArrayList<>();
			   //获取客户信息
			   AuthenticationBasicInformationVo  custVo=accountService.getAuthenticationBasicInformation(identityCard, sourceOfCertification, mobilephone);
			  
			   if(custVo!=null){
				   cust.setCertificateNo(custVo.getIdentityCard());
				   cust.setCustName(custVo.getTrueName());
				   cust.setCertificateType("02");
				   cust.setMobileNo(custVo.getMobilephone());
				   cust.setDrivingLicenceNo(custVo.getIdentityCard());
			   }
			   List<BindTheVehicleVo>   carVo=accountService.getBndTheVehicles(identityCard, mobilephone, sourceOfCertification);
			   for (BindTheVehicleVo bindTheVehicleVo : carVo) {
				   CarInfoBean bean=new CarInfoBean();
				    bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
				    bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
				    bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits());
				    carList.add(bean);
			   }
			   
			   //同步客户信息
			  String str= illegalService.custRegInfoReceive(cust, carList);
			  System.out.println("同步："+str);
		   }
		   
		   
		   List<IllegalInfoBean> list= illegalService.queryInfoByLicensePlateNo(licensePlateNo, licensePlateType, vehicleIdentifyNoLast4);
		   base.setCode("0000");
		   if(list!=null){
			   base.setData(list);
			   base.setMsg("成功");
		   }else{
			   base.setMsg("当前车辆无未处理违法");
		   }		   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("查询失败！");
			e.printStackTrace();
		} 
	   renderJSON(base);
   }
      
   /**
    * 根据驾驶证号获取违章记录
    * @param drivingLicenceNo 驾驶证号
    * @param recordNo 档案编号
    */
   @RequestMapping(value = "queryInfoByDrivingLicenceNo")
   public void queryInfoByDrivingLicenceNo(String drivingLicenceNo,String recordNo,String identityCard, String sourceOfCertification, String mobilephone){
	   BaseBean base=new BaseBean();		 
	   try {
		   //判断客户是否已同步
		   String isReg=illegalService.isRegisterUser();
		   //未同步
		   if("0".equals(isReg)){
			   CustInfoBean cust=new CustInfoBean();
			   List<CarInfoBean> carList=new ArrayList<>();
			   //获取客户信息
			   AuthenticationBasicInformationVo  custVo=accountService.getAuthenticationBasicInformation(identityCard, sourceOfCertification, mobilephone);
			  
			   if(custVo!=null){
				   cust.setCertificateNo(custVo.getIdentityCard());
				   cust.setCustName(custVo.getTrueName());
				   cust.setCertificateType("02");
				   cust.setMobileNo(custVo.getMobilephone());
				   cust.setDrivingLicenceNo(custVo.getIdentityCard());
			   }
			   List<BindTheVehicleVo>   carVo=accountService.getBndTheVehicles(identityCard, mobilephone, sourceOfCertification); 
			   for (BindTheVehicleVo bindTheVehicleVo : carVo) {
				   CarInfoBean bean=new CarInfoBean();
				    bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
				    bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
				    bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits());
				    carList.add(bean);
			   }
			   
			   //同步客户信息
			  String str= illegalService.custRegInfoReceive(cust, carList);
			  System.out.println("同步："+str);
		   }
		   
		   List<IllegalInfoBean> list=illegalService.queryInfoByDrivingLicenceNo(drivingLicenceNo, recordNo);
		   base.setCode("0000");
		   if(list!=null){
			   base.setData(list);
			   base.setMsg("成功");
		   }else{
			   base.setMsg("驾驶人当前无未处理的违法");
		   }		   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("查询失败！");
			e.printStackTrace();
		} 
	   renderJSON(base);
   } 
    
   
   /**
    * 打单注册接口
    * @param licensePlateNo 车牌号
    * @param licensePlateType 车辆类型
    * @param mobilephone 手机号码
    */
   @RequestMapping(value = "trafficIllegalClaimReg")
   public void trafficIllegalClaimReg(CustInfoBean custInfo, CarInfoBean carInfo,String identityCard, String sourceOfCertification, String mobilephone){  	
	  BaseBean base=new BaseBean();		 
	   try {
		 //判断客户是否已同步
		   String isReg=illegalService.isRegisterUser();
		   //未同步
		   if("0".equals(isReg)){
			   CustInfoBean cust=new CustInfoBean();
			   List<CarInfoBean> carList=new ArrayList<>();
			   //获取客户信息
			   AuthenticationBasicInformationVo  custVo=accountService.getAuthenticationBasicInformation(identityCard, sourceOfCertification, mobilephone);
			  
			   if(custVo!=null){
				   cust.setCertificateNo(custVo.getIdentityCard());
				   cust.setCustName(custVo.getTrueName());
				   cust.setCertificateType("02");
				   cust.setMobileNo(custVo.getMobilephone());
				   cust.setDrivingLicenceNo(custVo.getIdentityCard());
			   }
			   List<BindTheVehicleVo>   carVo=accountService.getBndTheVehicles(identityCard, mobilephone, sourceOfCertification); 
			   for (BindTheVehicleVo bindTheVehicleVo : carVo) {
				   CarInfoBean bean=new CarInfoBean();
				    bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
				    bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
				    bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits());
				    carList.add(bean);
			   }
			   
			   //同步客户信息
			  String str= illegalService.custRegInfoReceive(cust, carList);
			  System.out.println("同步："+str);
		   }
		   	   
		   base=illegalService.trafficIllegalClaimReg(custInfo,carInfo);
	   		   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("注册失败！");
			e.printStackTrace();
		}
	   renderJSON(base);
   } 
   
   
    /**
     * 打单前查询
     * @param licensePlateNo 车牌号
     * @param licensePlateType 车辆类型
     * @param mobilephone 手机号码
     */
    @RequestMapping(value = "illegalOnlineConfirm")
    public void illegalOnlineConfirm(String licensePlateNo,String licensePlateType,String mobilephone,String identityCard, String sourceOfCertification){  	
 	  BaseBean base=new BaseBean();		 
	   try {
		 //判断客户是否已同步
		   String isReg=illegalService.isRegisterUser();
		   //未同步
		   if("0".equals(isReg)){
			   CustInfoBean cust=new CustInfoBean();
			   List<CarInfoBean> carList=new ArrayList<>();
			   //获取客户信息
			   AuthenticationBasicInformationVo  custVo=accountService.getAuthenticationBasicInformation(identityCard, sourceOfCertification, mobilephone);
			  
			   if(custVo!=null){
				   cust.setCertificateNo(custVo.getIdentityCard());
				   cust.setCustName(custVo.getTrueName());
				   cust.setCertificateType("02");
				   cust.setMobileNo(custVo.getMobilephone());
				   cust.setDrivingLicenceNo(custVo.getIdentityCard());
			   }
			   List<BindTheVehicleVo>   carVo=accountService.getBndTheVehicles(identityCard, mobilephone, sourceOfCertification); 
			   for (BindTheVehicleVo bindTheVehicleVo : carVo) {
				   CarInfoBean bean=new CarInfoBean();
				    bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
				    bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
				    bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits());
				    carList.add(bean);
			   }
			   
			   //同步客户信息
			  String str= illegalService.custRegInfoReceive(cust, carList);
			  System.out.println("同步："+str);
		   }
		   
		   List<IllegalInfoClaim> list=illegalService.trafficIllegalClaimBefore(licensePlateNo, licensePlateType, mobilephone);
		   base.setCode("0000");
		   if(list!=null){
			   base.setData(list);
			   base.setMsg("成功");
		   }else{
			   base.setMsg("当前无未处理的违法");
		   }		   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("查询失败！");
			e.printStackTrace();
		}
 	   renderJSON(base);
    } 
     
    /**
     * 打单确认
     * @param illegalNo 违章编号
     */
    @RequestMapping(value = "trafficIllegalClaim")
    public void trafficIllegalClaim(String illegalNo){	        
       BaseBean base=new BaseBean();		 
	   try {
		   IllegalInfoSheet bean=illegalService.trafficIllegalClaim(illegalNo);
		   base.setCode("0000");
		   base.setMsg("成功！");
           base.setData(bean);	   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("查询失败！");
			e.printStackTrace();
		}
 	   renderJSON(base);
    } 
    
     
    /**
     * 规费信息查询
     */
    @RequestMapping(value = "toPayPage")
    public void toPayPage(String billNo,String  licensePlateNo,String mobilephone,HttpServletRequest req, HttpServletResponse resp){     
       String url=illegalService.toPayPage(billNo,licensePlateNo,mobilephone);
       try {
	    	if(StringUtil.isEmpty(url)){
	    	BaseBean base=new BaseBean();
	    	base.setCode("0001");
	    	base.setMsg("跳转页面失败！");
	    	renderJSON(base);
	    	}else{
	    		resp.sendRedirect(url);
	    	}  		
	   	} catch (IOException e) {
	   		e.printStackTrace();
	   	} 
    } 
    
    
    /**
     * 违法缴费信息
     * @param billNo 违章编号
     * @param licensePlateNo 车牌号
     * @param mobilephone 手机号码
     */
    @RequestMapping(value = "toQueryPunishmentPage")
    public void toQueryPunishmentPage(String billNo,String  licensePlateNo,String mobilephone,HttpServletRequest req, HttpServletResponse resp){
 	  //"4403010922403405","粤B8A3N2","18601174358");
	 	try {
	 		String url=illegalService.toQueryPunishmentPage(billNo,licensePlateNo,mobilephone);
	 		if(StringUtil.isEmpty(url)){
		    	BaseBean base=new BaseBean();
		    	base.setCode("0001");
		    	base.setMsg("跳转页面失败！");
		    	renderJSON(base);
		    	}else{
		    		resp.sendRedirect(url);
		    	} 
		} catch (Exception e) {	
			e.printStackTrace();
		} 

    } 
    
    
    /**
     * 获取所以违法处理点
     */
    @RequestMapping(value = "getIllegalProcessingPoint")
    public void getIllegalProcessingPoint(){  	
       BaseBean base=new BaseBean();  
       try {
		   List<IllegalProcessPointBean> list=illegalService.getIllegalProcessingPoint();
		   base.setCode("0000");
		   if(list!=null){
			   base.setData(list);
			   base.setMsg("成功");
		   }else{
			   base.setMsg("违法处理点获取为空");
		   }		   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("查询失败！");
			e.printStackTrace();
		}

  	   renderJSON(base);
    }
    
    /**
     * 获取某站点的预约排期
     * @param 站点Id
     */
    @RequestMapping(value = "toGetSubscribeSorts")
    public void toGetSubscribeSorts(String cldbmid){
         BaseBean base=new BaseBean();
         try {
           Map map=illegalService.toGetSubscribeSorts(cldbmid);
  		   base.setCode("0000");
  		   if(map!=null){
  			   base.setData(map);
  			   base.setMsg("成功");
  		   }else{
  			   base.setMsg("预约排期获取为空");
  		   }		   
  		} catch (Exception e) {
  			base.setCode("0001");
  			base.setMsg("查询失败！");
  			e.printStackTrace();
  		}
    	 renderJSON(base);
    }
    
    /**
     * 预约
     * @param billNo
     * @param licensePlateNo
     * @param mobilephone
     */
    @RequestMapping(value = "toChangeSubscribe")
    public void toChangeSubscribe(String snm,String cldbmid,String cczb_id,CustInfoBean custInfo,CarInfoBean carInfo,String sourceType){
      // CarInfoBean carinfo=new CarInfoBean("粤B6F7M1",  "2", "9094");
 	   //CustInfoBean custinfo=new CustInfoBean("王玉璞", "622822198502074110", "01", "18601174358",  "622822198502074110");
 	   BaseBean bean= illegalService.toChangeSubscribe(snm, cldbmid, cczb_id, custInfo, carInfo, sourceType);
 	   renderJSON(bean);
    } 
    
    
    /**
     * 取消预约
     * @param billNo
     * @param licensePlateNo
     * @param mobilephone
     */
    @RequestMapping(value = "toCancelSubscribe")
    public void toCancleSubscribe(String subscribeNo){
       /*MessageBean  bean=new MessageBean();
       bean.setBusinessType("01");
       bean.setSubscribeNo("000000000123");
       bean.setReminder("预约成功");*/
       BaseBean bean=illegalService.toCancleSubscribe(subscribeNo);

 	   renderJSON(bean);
    } 
    
    /**
     * 预约查询
     * @param billNo
     * @param licensePlateNo
     * @param mobilephone
     */
    @RequestMapping(value = "toQuerySubscribe")
    public void toQuerySubscribe(String licensePlateType,String  licensePlateNo,String mobilephone){	   
 	  BaseBean base=new BaseBean();		 
	   try {
		   List<SubcribeBean> list=illegalService.querySubscribe(licensePlateNo, licensePlateType, mobilephone);
		   base.setCode("0000");
		   if(list!=null){
			   base.setData(list);
			   base.setMsg("成功");
		   }else{
			   base.setMsg("当前客户无预约");
		   }		   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("查询失败！");
			e.printStackTrace();
		}
 
 	   renderJSON(base);
    } 
   
    /**
     * 提交申诉
     * @param info 申诉信息
     */
    @RequestMapping(value = "trafficIllegalAppeal")
    public void trafficIllegalAppeal(AppealInfoBean info,String identityCard, String userCode, String sourceType){   	
       AppealInfoBean bean=new AppealInfoBean(info.getBillNo(), info.getLicensePlateNo(),info.getLicensePlateType(), info.getIllegalTime(), info.getIllegalAddress(),
    		info.getIllegalDesc(), info.getAgency(), info.getClaimant(),
    		info.getClaimantAddress(), info.getClaimantPhone(), info.getAppealType(), info.getAppealContent(),
    		info.getMaterialPicture());
       BaseBean result=illegalService.trafficIllegalAppeal(bean,identityCard,userCode,sourceType);// "622822198502074110", "", "C");	   
 	   renderJSON(result);
    } 
   
   
    /**
     * 查询申诉
     * @param identityCard 身份证
     */
    @RequestMapping(value = "trafficIllegalAppealFeedback")
    public void trafficIllegalAppealFeedback(String identityCard,String sourceType){    
 	  BaseBean base=new BaseBean();
 	  try {
 		 List<AppealInfoBack> list= illegalService.trafficIllegalAppealFeedback(identityCard, sourceType);
		   base.setCode("0000");
		   if(list!=null){
			   base.setData(list);
			   base.setMsg("成功");
		   }else{
			   base.setMsg("当前客户无申诉信息");
		   }		   
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("查询失败！");
			e.printStackTrace();
		}

 	   renderJSON(base);
    }
}
