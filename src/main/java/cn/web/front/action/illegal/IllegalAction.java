package cn.web.front.action.illegal;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.service.IAccountService;
import cn.handle.bean.vo.HandleTemplateVo;
import cn.illegal.bean.AppealInfoBack;
import cn.illegal.bean.AppealInfoBean;
import cn.illegal.bean.CarInfoBean;
import cn.illegal.bean.CustInfoBean;
import cn.illegal.bean.IllegalBusiness;
import cn.illegal.bean.IllegalInfoBean;
import cn.illegal.bean.IllegalInfoClaim;
import cn.illegal.bean.IllegalInfoSheet;
import cn.illegal.bean.IllegalProcessPointBean;
import cn.illegal.bean.ReportingNoParking;
import cn.illegal.bean.SubcribeBean;
import cn.illegal.service.IIllegalService;
import cn.message.model.wechat.MessageChannelModel;
import cn.message.model.wechat.TemplateDataModel;
import cn.message.model.wechat.WechatUserInfo;
import cn.message.model.wechat.TemplateDataModel.Property;
import cn.message.service.IMobileMessageService;
import cn.message.service.ITemplateMessageService;
import cn.message.service.IWechatService;
import cn.sdk.bean.BaseBean;
import cn.sdk.bean.BusinessType;
import cn.sdk.util.DateUtil;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.MsgCode;
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
	@Autowired
	@Qualifier("wechatService")
	private IWechatService wechatService;
	@Autowired
	@Qualifier("mobileMessageService")
	private IMobileMessageService mobileMessageService;
	@Autowired
	@Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;
	    
	/**
	 * 
	 * 获取业务列表
	 */
	@RequestMapping(value = "illegalBusinessListQuery")
	public void getBusinessInfos() {
		List<IllegalBusiness> list = new ArrayList<IllegalBusiness>();
		IllegalBusiness bean1 = new IllegalBusiness();
		bean1.setBusinessName("交通违法查询");
		bean1.setAction("xxx");
		bean1.setBusinessCode("M001");
		IllegalBusiness bean2 = new IllegalBusiness();
		bean2.setBusinessName("违法在线确认");
		bean2.setAction("xxx");
		bean2.setBusinessCode("M002");
		List<IllegalBusiness> clids = new ArrayList<IllegalBusiness>();
		IllegalBusiness clid1 = new IllegalBusiness();
		clid1.setBusinessName("违法缴款");
		clid1.setAction("xxx");
		clid1.setBusinessCode("M011");
		clid1.setParentCode("M003");
		IllegalBusiness clid2 = new IllegalBusiness();
		clid2.setBusinessName("交通违法预约处理");
		clid2.setAction("xxx");
		clid2.setBusinessCode("M012");
		clid2.setParentCode("M003");
		IllegalBusiness clid3 = new IllegalBusiness();
		clid3.setBusinessName("交通违法申述");
		clid3.setAction("xxx");
		clid3.setBusinessCode("M013");
		clid3.setParentCode("M003");
		clids.add(clid1);
		clids.add(clid2);
		clids.add(clid3);
		IllegalBusiness bean3 = new IllegalBusiness();
		bean3.setBusinessName("违法在线处理");
		bean3.setAction("xxx");
		bean3.setBusinessCode("M013");
		bean3.setChildren(clids);
		list.add(bean1);
		list.add(bean2);
		list.add(bean3);
		BaseBean base = new BaseBean();
		base.setCode("0000");
		base.setMsg("成功！");
		base.setData(list);
		renderJSON(base);
	}
	/**
	 * 根据车牌号获取违章信息
	 * 
	 * @param licensePlateNo
	 *            车牌号
	 * @param licensePlateType
	 *            车辆类型
	 * @param vehicleIdentifyNoLast4
	 *            车架号后四位
	 */
	@RequestMapping(value = "queryInfoByLicensePlateNo")
	public void queryInfoByLicensePlateNo(String licensePlateNo, String licensePlateType, String vehicleIdentifyNoLast4,
			String identityCard, String sourceOfCertification, String mobilephone, String openId) {
		BaseBean base = new BaseBean();
		try {
			// 参数校验
			if (StringUtil.isEmpty(licensePlateNo)) {
				base.setCode("0001");
				base.setMsg("licensePlateNo不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(licensePlateType)) {
				base.setCode("0001");
				base.setMsg("licensePlateType不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(sourceOfCertification)) {
				base.setCode("0001");
				base.setMsg("sourceOfCertification不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(openId)) {
				base.setCode("0001");
				base.setMsg("未获取到openId！");
				renderJSON(base);
			}
			// 判断客户是否已同步
			String isReg = illegalService.isRegisterUser(openId,sourceOfCertification);
			if (StringUtil.isEmpty(identityCard)) {
				isReg = "1";
			}
			if (StringUtil.isEmpty(mobilephone)) {
				isReg = "1";
			}
			// 未同步
			if ("0".equals(isReg)) {
				CustInfoBean cust = new CustInfoBean();
				List<CarInfoBean> carList = new ArrayList<>();
				// 获取客户信息
				AuthenticationBasicInformationVo custVo = accountService.getAuthenticationBasicInformation(identityCard,
						sourceOfCertification, mobilephone);
				if (custVo != null) {
					cust.setCertificateNo(custVo.getIdentityCard());
					cust.setCustName(custVo.getTrueName());
					cust.setCertificateType("02");
					cust.setMobileNo(custVo.getMobilephone());
					cust.setDrivingLicenceNo(custVo.getIdentityCard());
				}
				List<BindTheVehicleVo> carVo = accountService.getBndTheVehicles(identityCard, mobilephone,
						sourceOfCertification);
				for (BindTheVehicleVo bindTheVehicleVo : carVo) {
					CarInfoBean bean = new CarInfoBean();
					bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
					bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
					bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits());
					carList.add(bean);
				}
				// 同步客户信息
				String str = illegalService.custRegInfoReceive(cust, carList, openId,sourceOfCertification);
				logger.info("同步客户信息！"+str);
			}
			base = illegalService.queryInfoByLicensePlateNo(licensePlateNo, licensePlateType,
					vehicleIdentifyNoLast4, openId,sourceOfCertification);
	
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 根据驾驶证号获取违章记录
	 * 
	 * @param drivingLicenceNo
	 *            驾驶证号
	 * @param recordNo
	 *            档案编号
	 */
	@RequestMapping(value = "queryInfoByDrivingLicenceNo")
	public void queryInfoByDrivingLicenceNo(String drivingLicenceNo, String recordNo, String identityCard,
			String sourceOfCertification, String mobilephone, String openId) {
		BaseBean base = new BaseBean();
		try {
			if (StringUtil.isEmpty(drivingLicenceNo)) {
				base.setCode("0001");
				base.setMsg("drivingLicenceNo不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(recordNo)) {
				base.setCode("0001");
				base.setMsg("recordNo不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(sourceOfCertification)) {
				base.setCode("0001");
				base.setMsg("sourceOfCertification不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(openId)) {
				base.setCode("0001");
				base.setMsg("未获取到openId！");
				renderJSON(base);
			}
			// 判断客户是否已同步
			String isReg = illegalService.isRegisterUser(openId,sourceOfCertification);
			if (StringUtil.isEmpty(identityCard)) {
				isReg = "1";
			}
			if (StringUtil.isEmpty(mobilephone)) {
				isReg = "1";
			}
			// 未同步
			if ("0".equals(isReg)) {
				CustInfoBean cust = new CustInfoBean();
				List<CarInfoBean> carList = new ArrayList<>();
				// 获取客户信息
				AuthenticationBasicInformationVo custVo = accountService.getAuthenticationBasicInformation(identityCard,
						sourceOfCertification, mobilephone);
				if (custVo != null) {
					cust.setCertificateNo(custVo.getIdentityCard());
					cust.setCustName(custVo.getTrueName());
					cust.setCertificateType("02");
					cust.setMobileNo(custVo.getMobilephone());
					cust.setDrivingLicenceNo(custVo.getIdentityCard());
				}
				List<BindTheVehicleVo> carVo = accountService.getBndTheVehicles(identityCard, mobilephone,
						sourceOfCertification);
				for (BindTheVehicleVo bindTheVehicleVo : carVo) {
					CarInfoBean bean = new CarInfoBean();
					bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
					bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
					bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits());
					carList.add(bean);
				}
				// 同步客户信息
				String str = illegalService.custRegInfoReceive(cust, carList, openId,sourceOfCertification);
				System.out.println("同步：" + str);
			}
			List<IllegalInfoBean> list = illegalService.queryInfoByDrivingLicenceNo(drivingLicenceNo, recordNo, openId,sourceOfCertification);
			base.setCode("0000");
			if (list != null) {
				base.setData(list);
				base.setMsg("成功");
			} else {
				base.setMsg("驾驶人当前无未处理的违法");
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 打单注册接口
	 * 
	 * @param licensePlateNo
	 *            车牌号
	 * @param licensePlateType
	 *            车辆类型
	 * @param mobilephone
	 *            手机号码
	 */
	/*
	 * @RequestMapping(value = "trafficIllegalClaimReg") public void
	 * trafficIllegalClaimReg(CustInfoBean custInfo, CarInfoBean carInfo,String
	 * identityCard, String sourceOfCertification, String mobilephone){ BaseBean
	 * base=new BaseBean(); try { //判断客户是否已同步 String
	 * isReg=illegalService.isRegisterUser(); //未同步 if("0".equals(isReg)){
	 * CustInfoBean cust=new CustInfoBean(); List<CarInfoBean> carList=new
	 * ArrayList<>(); //获取客户信息 AuthenticationBasicInformationVo
	 * custVo=accountService.getAuthenticationBasicInformation(identityCard,
	 * sourceOfCertification, mobilephone);
	 * 
	 * if(custVo!=null){ cust.setCertificateNo(custVo.getIdentityCard());
	 * cust.setCustName(custVo.getTrueName()); cust.setCertificateType("02");
	 * cust.setMobileNo(custVo.getMobilephone());
	 * cust.setDrivingLicenceNo(custVo.getIdentityCard()); }
	 * List<BindTheVehicleVo>
	 * carVo=accountService.getBndTheVehicles(identityCard, mobilephone,
	 * sourceOfCertification); for (BindTheVehicleVo bindTheVehicleVo : carVo) {
	 * CarInfoBean bean=new CarInfoBean();
	 * bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
	 * bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
	 * bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits(
	 * )); carList.add(bean); }
	 * 
	 * //同步客户信息 String str= illegalService.custRegInfoReceive(cust, carList);
	 * System.out.println("同步："+str); }
	 * 
	 * base=illegalService.trafficIllegalClaimReg(custInfo,carInfo);
	 * 
	 * } catch (Exception e) { base.setCode("0001"); base.setMsg("注册失败！"); }
	 * renderJSON(base); }
	 */
	/**
	 * 打单前查询
	 * 
	 * @param licensePlateNo
	 *            车牌号
	 * @param licensePlateType
	 *            车辆类型
	 * @param mobilephone
	 *            手机号码
	 */
	@RequestMapping(value = "illegalOnlineConfirm")
	public void illegalOnlineConfirm(String licensePlateNo, String licensePlateType, String mobilephone,
			String identityCard, String sourceOfCertification, String vehicleIdentifyNoLast4,String openId) {
		BaseBean base = new BaseBean();
		try {
			// 参数校验
			if (StringUtil.isEmpty(licensePlateNo)) {
				base.setCode("0001");
				base.setMsg("车牌号不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(licensePlateType)) {
				base.setCode("0001");
				base.setMsg("车牌类型不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(sourceOfCertification)) {
				base.setCode("0001");
				base.setMsg("来源方式不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(openId)) {
				base.setCode("0001");
				base.setMsg("未获取到openId！");
				renderJSON(base);
			}
			// 判断客户是否已同步
			String isReg = illegalService.isRegisterUser(openId,sourceOfCertification);
			if (StringUtil.isEmpty(identityCard)) {
				isReg = "1";
			}
			if (StringUtil.isEmpty(mobilephone)) {
				isReg = "1";
			}
			// 未同步
			if ("0".equals(isReg)) {
				CustInfoBean cust = new CustInfoBean();
				List<CarInfoBean> carList = new ArrayList<>();
				// 获取客户信息
				AuthenticationBasicInformationVo custVo = accountService.getAuthenticationBasicInformation(identityCard,
						sourceOfCertification, mobilephone);
				if (custVo != null) {
					cust.setCertificateNo(custVo.getIdentityCard());
					cust.setCustName(custVo.getTrueName());
					cust.setCertificateType("02");
					cust.setMobileNo(custVo.getMobilephone());
					cust.setDrivingLicenceNo(custVo.getIdentityCard());
				}
				List<BindTheVehicleVo> carVo = accountService.getBndTheVehicles(identityCard, mobilephone,
						sourceOfCertification);
				for (BindTheVehicleVo bindTheVehicleVo : carVo) {
					CarInfoBean bean = new CarInfoBean();
					bean.setLicensePlateNo(bindTheVehicleVo.getNumberPlateNumber());
					bean.setLicensePlateType(bindTheVehicleVo.getPlateType());
					bean.setVehicleIdentifyNoLast4(bindTheVehicleVo.getBehindTheFrame4Digits());
					carList.add(bean);
				}
				// 同步客户信息
				String str = illegalService.custRegInfoReceive(cust, carList, openId,sourceOfCertification);
				System.out.println("同步：" + str);
			}
			List<IllegalInfoBean> returnList = new ArrayList<IllegalInfoBean>();
			List<IllegalInfoClaim> infos = new ArrayList<IllegalInfoClaim>();
			BaseBean reList =new BaseBean();
			BaseBean result = illegalService.trafficIllegalClaimBefore(licensePlateNo, licensePlateType, mobilephone,
					openId,sourceOfCertification);
			String msgCode = result.getCode();
			String msg = result.getMsg();
			if ("0000".equals(msgCode)) {
				infos = (List<IllegalInfoClaim>) JSON.parseArray(result.getData().toString(), IllegalInfoClaim.class);
				reList = illegalService.queryInfoByLicensePlateNo1(licensePlateNo, licensePlateType, vehicleIdentifyNoLast4, openId,sourceOfCertification);
			} else {
				if ("您好，没有查找到可用互联网方式处理的交通违法，感谢您对我局工作的支持！".equals(msg)
						|| "由于您的驾驶证处理此宗违法后累计分已经超过12分，请到各大队违例窗口现场办理。".equals(msg)) {
					reList = illegalService.queryInfoByLicensePlateNo1(licensePlateNo, licensePlateType,vehicleIdentifyNoLast4,
							openId,sourceOfCertification);
				} else {
					base.setCode(msgCode);
					base.setMsg(msg);
					renderJSON(base);
					return;
				}
			}
			if ("0000".equals(reList.getCode())) {
				returnList=(List<IllegalInfoBean>)reList.getData();
				// 拼接对象
				for (IllegalInfoBean bean : returnList) {
					// 直接缴款类型
					if (bean.getIsNeedClaim().equals("2") || (bean.getBillNo() != null && bean.getBillNo().length() > 0)) {
						IllegalInfoClaim claim = new IllegalInfoClaim();
						claim.setIllegalNo(bean.getBillNo());
						claim.setIllegalTime(bean.getIllegalTime());
						claim.setIllegalDesc(bean.getIllegalDesc());
						claim.setDealType(bean.getIsNeedClaim());
						claim.setPunishAmt(bean.getPunishAmt() * 100);
						claim.setLicensePlateNo(bean.getLicensePlateNo());
						claim.setLicensePlateType(bean.getLicensePlateType());
						claim.setPunishScore(bean.getPunishScore());
						claim.setIllegalAddr(bean.getIllegalAddr());
						claim.setAgency(bean.getIllegalUnit());
						claim.setDrivingLicenceNo("");
						infos.add(claim);
					}
				}
			}
			base.setCode(reList.getCode());
			base.setData(infos);
			base.setMsg(reList.getMsg());		
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 打单确认
	 * 
	 * @param illegalNo
	 *            违章编号
	 */
	@RequestMapping(value = "trafficIllegalClaim")
	public void trafficIllegalClaim(String illegalNo, String openId,String sourceOfCertification) {
		BaseBean base = new BaseBean();
		try {
			// 参数校验
			if (StringUtil.isEmpty(illegalNo)) {
				base.setCode("0001");
				base.setMsg("违法编号不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(openId)) {
				base.setCode("0001");
				base.setMsg("未获取到openId！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(sourceOfCertification)) {
				base.setCode("0001");
				base.setMsg("来源方式不能为空！");
				renderJSON(base);
			}
			IllegalInfoSheet bean = illegalService.trafficIllegalClaim(illegalNo, openId,sourceOfCertification);
			if (bean == null) {
				base.setCode("0001");
				base.setMsg("打单失败！");
			} else {
				base.setCode("0000");
				base.setMsg("成功！");
				base.setData(bean);
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("打单异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 二维码扫码信息查询
	 */
	@RequestMapping(value = "callback")
	public void qrCodeToQueryPage(HttpServletRequest req, HttpServletResponse resp) {
		String traffData = req.getParameter("traffData");
		BaseBean base = new BaseBean();
		try {
			resp.setCharacterEncoding("utf-8");
			String url = illegalService.callback(traffData);
			resp.sendRedirect(url);
		} catch (Exception e) {
			try {
				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("text/html;charset=utf-8");
				PrintWriter out = resp.getWriter();		
				out.print("目前系统繁忙，请稍后重试!");
			} catch (IOException e1) {
				logger.error("微信回调异常 ", e);
			}	
		}
		renderJSON(base);
	}
	/**
	 * 扫码缴费查询页面跳转
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "qrCodeToQueryPage")
	public void callback4OpenId(HttpServletRequest request, HttpServletResponse response) {
		BaseBean base = new BaseBean();
		String code = request.getParameter("code");
		String state = request.getParameter("state");// 前端会带过来一个url
		String traffData = request.getParameter("traffData");
		String userName = request.getParameter("userName");
		String mobileNo = request.getParameter("mobileNo");
		try {
			response.setCharacterEncoding("utf-8");
			logger.info(code + "" + state + traffData + userName + mobileNo);
			// 获取微信用户信息
			WechatUserInfo wechatUserInfo = wechatService.callback4OpenId(code, state);
			if (wechatUserInfo != null) {
				logger.info("Wechat 获取用户信息:" + wechatUserInfo.toString());
				String url = illegalService.qrCodeToQueryPage(userName, traffData, mobileNo,
						wechatUserInfo.getOpenId(),"C");
				response.sendRedirect(url);
			} else {
				logger.error("Wechat 获取用户信息为空！");
			}
		} catch (Exception e) {
			try {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();		
				out.print("目前系统繁忙，请稍后重试!");
			} catch (IOException e1) {
				logger.error("页面跳转异常 ", e);
			}	
		}
	}
	/**
	 * 违法缴费信息
	 * 
	 * @param billNo
	 *            违章编号
	 * @param licensePlateNo
	 *            车牌号
	 * @param mobilephone
	 *            手机号码
	 */
	@RequestMapping(value = "toQueryPunishmentPage")
	public void toQueryPunishmentPage(String billNo, String licensePlateNo, String mobilephone, String openId,String sourceOfCertification,
			HttpServletRequest req, HttpServletResponse resp) {
		// "4403010922403405","粤B8A3N2","18601174358");
		BaseBean base = new BaseBean();
		try {
			// 参数校验
			if (StringUtil.isEmpty(billNo)) {
				base.setCode("0001");
				base.setMsg("缴款编号不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(licensePlateNo)) {
				base.setCode("0001");
				base.setMsg("车牌号不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(openId)) {
				base.setCode("0001");
				base.setMsg("未获取到openId！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(sourceOfCertification)) {
				base.setCode("0001");
				base.setMsg("来源方式不能为空！");
				renderJSON(base);
			}
			billNo = billNo.replaceAll(" ", "");
			String url = illegalService.toQueryPunishmentPage(billNo, licensePlateNo, mobilephone, openId,sourceOfCertification);
			if (StringUtil.isEmpty(url)) {
				base.setCode("0001");
				base.setMsg("返回页面地址为空！");
			} else {
				base.setCode("0000");
				base.setMsg(url);
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("获取异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 获取所有违法处理点
	 */
	@RequestMapping(value = "getIllegalProcessingPoint")
	public void getIllegalProcessingPoint() {
		BaseBean base = new BaseBean();
		try {
			List<IllegalProcessPointBean> list = illegalService.getIllegalProcessingPoint();
			base.setCode("0000");
			if (list != null) {
				base.setData(list);
				base.setMsg("成功");
			} else {
				base.setMsg("违法处理点获取为空");
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 获取某站点的预约排期
	 * 
	 * @param 站点Id
	 */
	@RequestMapping(value = "toGetSubscribeSorts")
	public void toGetSubscribeSorts(String cldbmid) {
		BaseBean base = new BaseBean();
		try {
			// 参数校验
			if (StringUtil.isEmpty(cldbmid)) {
				base.setCode("0001");
				base.setMsg("违法站点编号不能为空！");
				renderJSON(base);
			}
			Map map = illegalService.toGetSubscribeSorts(cldbmid);
			if (map != null && "0".equals(map.get("code"))) {
				base.setCode("0000");
			} else {
				base.setCode("0001");
			}
			base.setData(map);
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 预约
	 * 
	 * @param billNo
	 * @param licensePlateNo
	 * @param mobilephone
	 */
	@RequestMapping(value = "toChangeSubscribe")
	public void toChangeSubscribe(String snm, String cldbmid, String cczb_id, CustInfoBean custInfo,
			CarInfoBean carInfo, String sourceOfCertification, String openId,
			String cldbmmc, String cldlxdh, String cldaddress, String yydate, String ccsjd) {
		// CarInfoBean carinfo=new CarInfoBean("粤B6F7M1", "2", "9094");
		// CustInfoBean custinfo=new CustInfoBean("王玉璞", "622822198502074110",
		// "01", "18601174358", "622822198502074110");
		BaseBean base = new BaseBean();
		try {
			if (StringUtil.isEmpty(snm)) {
				base.setCode("0001");
				base.setMsg("SNM编号不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(cldbmid)) {
				base.setCode("0001");
				base.setMsg("违法站点编号不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(cczb_id)) {
				base.setCode("0001");
				base.setMsg("预约场次编号不能为空！");
				renderJSON(base);
			}
			base = illegalService.toChangeSubscribe(snm, cldbmid, cczb_id, custInfo, carInfo, sourceOfCertification);
			
			/*base.setCode("0");//{"code":"0","message":"1170914100036"}
			base.setMsg("1170914100012");
			System.out.println("【base】"+JSON.toJSONString(base));*/
			//模板消息新增字段
			boolean flag = true;
			if (StringUtil.isEmpty(cldbmmc) && StringUtil.isEmpty(cldlxdh) && StringUtil.isEmpty(cldaddress) && StringUtil.isEmpty(yydate) && StringUtil.isEmpty(ccsjd)) {
				logger.info("cldbmmc="+cldbmmc+";cldlxdh="+cldlxdh+";cldaddress="+cldaddress+";yydate="+yydate+";ccsjd="+ccsjd);
				flag = false;
			}
			//预约成功，发送微信模板消息
			if(flag && "0".equals(base.getCode()) && "C".equals(sourceOfCertification)){
				String waterNumber = base.getMsg();
				String url = templateMessageService.getTemplateSendUrl() + "type=2&title=illegalAppointment&waterNumber="+waterNumber+"&orgName="+cldbmmc+"&serviceCall="+cldlxdh+"&orgAddr="+cldaddress+"&appointmentDate="+yydate+"&appointmentTime="+ccsjd;
				//String url = "http://gzh.stc.gov.cn/h5/#/submitSuccess?type=2&title=illegalAppointment&waterNumber=123&orgName=龙华交警大队违法处理点&serviceCall=13800138000&orgAddr=龙华新区大道创业花园向荣大厦188栋&appointmentDate=2017-09-21&appointmentTime=12:00-17:00";
				logger.info("【交通违法办理】返回的url是：" + url);
				
				MessageChannelModel model = new MessageChannelModel();
				model.setOpenid(openId);
				model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbv3QTn_7dV2OuKaPDHYBrNQ");
				model.setResult_page_style_id("4P3yuc5LgEgbuQ6w2ZEZzZw0J4Cpz8_qtEszelOARpU");
				model.setDeal_msg_style_id("4P3yuc5LgEgbuQ6w2ZEZzbEZz3IWDGV7iJiPSpYQCDw");
				model.setCard_style_id("");
				model.setOrder_no(waterNumber);
				model.setUrl(url);
				Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
				map.put("first", new MessageChannelModel().new Property("您好，您的业务办理预约申请已成功提交，具体信息如下。","#212121"));
				map.put("business", new MessageChannelModel().new Property("交通违法办理-违法处理预约","#212121"));
				map.put("order", new MessageChannelModel().new Property(waterNumber,"#212121"));
				map.put("time", new MessageChannelModel().new Property(yydate + " " + ccsjd,"#212121"));
				map.put("address", new MessageChannelModel().new Property(cldbmmc,"#212121"));
				map.put("remark", new MessageChannelModel().new Property("请您持身份证及业务办理所需材料在预约办理时间段内完成取号，不能办理业务请及时取消。","#212121"));
				model.setData(map);
				
				BaseBean msgBean = templateMessageService.sendServiceMessage(model);
				logger.info("【交通违法办理】发送模板消息结果：" + JSON.toJSONString(msgBean));
				
				//发送成功
				if("0".equals(msgBean.getCode())){
					base.setData(msgBean.getData().toString());//结果评价页url设置在data中
				}else{
					base.setData(url);//详情页url
				}
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("预约异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 取消预约
	 * 
	 * @param billNo
	 * @param licensePlateNo
	 * @param mobilephone
	 */
	@RequestMapping(value = "toCancelSubscribe")
	public void toCancleSubscribe(String subscribeNo, String sourceOfCertification, String openId, String businessName,
			String yydate, String ccsjd, String cldbmmc) {
		BaseBean bean = new BaseBean();
		try {
			if (StringUtil.isEmpty(subscribeNo)) {
				bean.setCode("0001");
				bean.setMsg("预约流水号不能为空！");
				renderJSON(bean);
			}
			bean = illegalService.toCancleSubscribe(subscribeNo);
			
			/*bean.setCode("0");//{"code":"0","message":"1170914100036"}
			bean.setMsg("取消成功!");
			System.out.println("【bean】"+JSON.toJSONString(bean));*/
			//模板消息新增字段
			boolean flag = true;
			if(StringUtil.isEmpty(businessName)&&StringUtil.isEmpty(yydate)&&StringUtil.isEmpty(ccsjd)&&StringUtil.isEmpty(cldbmmc)){
				logger.info("cldbmmc="+cldbmmc+";businessName="+businessName+";yydate="+yydate+";ccsjd="+ccsjd);
				flag = false;
			}
			//取消成功，发送微信模板消息
			if(flag && "0".equals(bean.getCode()) && "C".equals(sourceOfCertification)){
				MessageChannelModel model = new MessageChannelModel();
				model.setOpenid(openId);
				model.setBiz_template_id("s4ia2sLd4C-0IpkLLbGIbmdPgvKIb6VMfR1zxNIe_fw");
				model.setResult_page_style_id("PMw9-nhDOOQuMzL7-cVZ3DqyaaLEvpIWsopaXE1qvC0");
				model.setDeal_msg_style_id("PMw9-nhDOOQuMzL7-cVZ3CZoVDr0ojGdWvwZf7SZK6A");
				model.setCard_style_id("");
				model.setOrder_no(subscribeNo);
				model.setUrl("");
				Map<String, cn.message.model.wechat.MessageChannelModel.Property> map = new HashMap<String, cn.message.model.wechat.MessageChannelModel.Property>();
				map.put("first", new MessageChannelModel().new Property("您好，您的预约申请已取消，具体信息如下","#212121"));
				map.put("businessType", new MessageChannelModel().new Property("交通违法办理","#212121"));
				map.put("business", new MessageChannelModel().new Property(businessName,"#212121"));
				map.put("order", new MessageChannelModel().new Property(subscribeNo,"#212121"));
				map.put("time", new MessageChannelModel().new Property(yydate + " " + ccsjd,"#212121"));
				map.put("address", new MessageChannelModel().new Property(cldbmmc,"#212121"));
				map.put("remark", new MessageChannelModel().new Property("","#212121"));
				model.setData(map);
				
				BaseBean msgBean = templateMessageService.sendServiceMessage(model);
				logger.info("【交通违法办理】发送模板消息结果：" + JSON.toJSONString(msgBean));
				
				//发送成功
				if("0".equals(msgBean.getCode())){
					bean.setData(msgBean.getData().toString());//结果评价页url设置在data中
				}
			}
		} catch (Exception e) {
			DealException(bean, e);
			logger.error("取消预约异常：", e);
		}
		renderJSON(bean);
	}
	/**
	 * 预约查询
	 * 
	 * @param billNo
	 * @param licensePlateNo
	 * @param mobilephone
	 */
	@RequestMapping(value = "toQuerySubscribe")
	public void toQuerySubscribe(String licensePlateType, String licensePlateNo, String mobilephone) {
		BaseBean base = new BaseBean();
		if (StringUtil.isEmpty(licensePlateNo)) {
			base.setCode("0001");
			base.setMsg("车牌号不能为空！");
			renderJSON(base);
		}
		if (StringUtil.isEmpty(mobilephone)) {
			base.setCode("0001");
			base.setMsg("手机号不能为空！");
			renderJSON(base);
		}
		int type = 0;
		try {
			type = Integer.parseInt(licensePlateType);
		} catch (Exception e) {
			base.setCode("0001");
			base.setMsg("车牌号类型异常！");
			renderJSON(base);
		}
		try {
			List<SubcribeBean> list = illegalService.querySubscribe(licensePlateNo, type, mobilephone);
			base.setCode("0000");
			if (list != null) {
				base.setData(list);
				base.setMsg("成功");
			} else {
				base.setMsg("当前客户无预约");
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 提交申诉
	 * 
	 * @param info
	 *            申诉信息
	 */
	@RequestMapping(value = "trafficIllegalAppeal")
	public void trafficIllegalAppeal(AppealInfoBean info, String identityCard, String userCode,
			String sourceOfCertification) {
		BaseBean base = new BaseBean();
		try {
			if (StringUtil.isEmpty(identityCard)) {
				base.setCode("0001");
				base.setMsg("身份证不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(sourceOfCertification)) {
				base.setCode("0001");
				base.setMsg("来源方式不能为空！");
				renderJSON(base);
			}
			AppealInfoBean bean = new AppealInfoBean(info.getBillNo(), info.getLicensePlateNo(),
					info.getLicensePlateType(), info.getIllegalTime(), info.getIllegalAddress(), info.getIllegalDesc(),
					info.getAgency(), info.getClaimant(), info.getClaimantAddress(), info.getClaimantPhone(),
					info.getAppealType(), info.getAppealContent(), info.getMaterialPicture());
			base = illegalService.trafficIllegalAppeal(bean, identityCard, userCode, sourceOfCertification);
		} catch (Exception e) {
			DealException(base, e);
			logger.error("申诉异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 查询申诉
	 * 
	 * @param identityCard
	 *            身份证
	 */
	@RequestMapping(value = "trafficIllegalAppealFeedback")
	public void trafficIllegalAppealFeedback(String identityCard, String sourceOfCertification) {
		BaseBean base = new BaseBean();
		if (StringUtil.isEmpty(identityCard)) {
			base.setCode("0001");
			base.setMsg("身份证不能为空！");
			renderJSON(base);
		}
		if (StringUtil.isEmpty(sourceOfCertification)) {
			base.setCode("0001");
			base.setMsg("来源方式不能为空！");
			renderJSON(base);
		}
		try {
			List<AppealInfoBack> list = illegalService.trafficIllegalAppealFeedback(identityCard,
					sourceOfCertification);
			base.setCode("0000");
			if (list != null) {
				base.setData(list);
				base.setMsg("成功");
			} else {
				base.setMsg("当前客户无申诉信息");
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 违法图片查询
	 * 
	 * @param imgQueryCode
	 *            违法图片code
	 * @param sourceOfCertification
	 */
	@RequestMapping(value = "illegalPictureQuery")
	public void illegalPictureQuery(String imgQueryCode, String sourceOfCertification) {
		BaseBean base = new BaseBean();
		if (StringUtil.isEmpty(imgQueryCode)) {
			base.setCode("0001");
			base.setMsg("imgQueryCode不能为空！");
			renderJSON(base);
		}
		if (StringUtil.isEmpty(sourceOfCertification)) {
			base.setCode("0001");
			base.setMsg("sourceOfCertification不能为空！");
			renderJSON(base);
		}
		try {
			List<String> list = illegalService.illegalPictureQuery(imgQueryCode,sourceOfCertification);
			base.setCode("0000");
			if (list != null) {
				base.setData(list);
				base.setMsg("成功");
			} else {
				base.setMsg("没有 违法图片");
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 车辆临时停车违停申报
	 * 
	 * @param numberPlateNumber
	 * @param plateType
	 * @param IDcard
	 * @param parkingSpot
	 * @param parkingReason
	 * @param scenePhoto
	 * @param scenePhoto1
	 * @param scenePhoto2
	 * @param scenePhoto3
	 * @param stopNoticePhoto
	 * @param sourceOfCertification
	 */
	@RequestMapping(value = "reportingNoParking")
	public void reportingNoParking(String numberPlateNumber, String plateType, String IDcard, String parkingSpot,
			String scenePhoto,String sourceOfCertification ,String openId) {
		BaseBean base = new BaseBean();
		ReportingNoParking reportingNoParking = new ReportingNoParking();
		if (StringUtil.isBlank(numberPlateNumber)) {
			base.setCode("0001");
			base.setMsg("车牌号码不能为空！");
			renderJSON(base);
			return;
		} else {
			reportingNoParking.setNumberPlateNumber(numberPlateNumber);
		}
		if (StringUtil.isBlank(plateType)) {
			base.setCode("0001");
			base.setMsg("车牌种类不能为空！");
			renderJSON(base);
			return;
		} else {
			reportingNoParking.setPlateType(plateType);
		}
		if (StringUtil.isBlank(IDcard)) {
			base.setCode("0001");
			base.setMsg("星级用户身份证明号码不能为空！");
			renderJSON(base);
			return;
		} else {
			reportingNoParking.setIDcard(IDcard);
		}
		if (StringUtil.isBlank(parkingSpot)) {
			base.setCode("0001");
			base.setMsg("停车地点不能为空！");
			renderJSON(base);
			return;
		} else {
			reportingNoParking.setParkingSpot(parkingSpot);
		}
		if (StringUtil.isBlank(scenePhoto)) {
			base.setCode("0001");
			base.setMsg("驾离后照片不能为空！");
			renderJSON(base);
			return;
		} else {
			reportingNoParking.setScenePhoto(scenePhoto);
		}		
		if (StringUtil.isBlank(sourceOfCertification)) {
			base.setCode("0001");
			base.setMsg("来源方式不能为空！");
			renderJSON(base);
			return;
		} else {
			reportingNoParking.setSourceOfCertification(sourceOfCertification);
		}
		if (StringUtil.isBlank(openId)) {
			base.setCode("0001");
			base.setMsg("openId不能为空！");
			renderJSON(base);
			return;
		} else {
			reportingNoParking.setOpenId(openId);
		}
		try {
			Map<String, String> map = illegalService.reportingNoParking(reportingNoParking);
			String code = map.get("code");
			String msg = map.get("msg");
			if ("0000".equals(code)) {
				String cid = map.get("cid");
				base.setData(cid);
				base.setCode(MsgCode.success);
				base.setMsg(msg);
			} else {
				base.setCode(MsgCode.businessError);
				base.setMsg(msg);
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("车辆临时停车违停申报异常：", e);
		}
		renderJSON(base);
		logger.debug(JSON.toJSONString(base));
	}
	
	/**
	 * 单宗违停申报结果查询
	 * 
	 * @param orderNumber
	 * @param numberPlateNumber
	 * @param plateType
	 * @param sourceOfCertification
	 */
	@RequestMapping(value = "singleQueryOfReportingNoParking")
	public void singleQueryOfReportingNoParking(String orderNumber, String numberPlateNumber, String plateType,
			String sourceOfCertification) {
		BaseBean base = new BaseBean();
		if (StringUtil.isBlank(orderNumber)) {
			base.setCode("0001");
			base.setMsg("序号不能为空！");
			renderJSON(base);
			return;
		}
		if (StringUtil.isBlank(numberPlateNumber)) {
			base.setCode("0001");
			base.setMsg("车牌号码不能为空！");
			renderJSON(base);
			return;
		}
		if (StringUtil.isBlank(plateType)) {
			base.setCode("0001");
			base.setMsg("车牌种类不能为空！");
			renderJSON(base);
			return;
		}
		if (StringUtil.isBlank(sourceOfCertification)) {
			base.setCode("0001");
			base.setMsg("来源方式不能为空！");
			renderJSON(base);
			return;
		}
		try {
			Map<String, Object> map = illegalService.singleQueryOfReportingNoParking(orderNumber, numberPlateNumber,
					plateType, sourceOfCertification);
			String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if ("0000".equals(code)) {
				base.setCode(MsgCode.success);
				base.setData(map.get("data"));
				base.setMsg(msg);
			} else {
				base.setCode(MsgCode.businessError);
				base.setMsg(msg);
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("单宗违停申报结果查询异常：", e);
		}
		renderJSON(base);
	}
	/**
	 * 申报记录列表查询
	 * 
	 * @param orderNumber
	 * @param numberPlateNumber
	 * @param plateType
	 * @param sourceOfCertification
	 * http://192.168.1.243:8080/web/illegalHanding/recordOfReportingNoParking.html?numberPlateNumber=粤B6F7M1&plateType=02&sourceOfCertification=C
	 */
	@RequestMapping(value = "recordOfReportingNoParking")
	public void recordOfReportingNoParking(String numberPlateNumber, String plateType, String sourceOfCertification) {
		BaseBean base = new BaseBean();
		if (StringUtil.isBlank(numberPlateNumber)) {
			base.setCode("0001");
			base.setMsg("车牌号码不能为空！");
			renderJSON(base);
			return;
		}
		if (StringUtil.isBlank(plateType)) {
			base.setCode("0001");
			base.setMsg("车牌种类不能为空！");
			renderJSON(base);
			return;
		}
		if (StringUtil.isBlank(sourceOfCertification)) {
			base.setCode("0001");
			base.setMsg("来源方式不能为空！");
			renderJSON(base);
			return;
		}
		try {
			Map<String, Object> map = illegalService.recordOfReportingNoParking(numberPlateNumber, plateType,
					sourceOfCertification);
			String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if ("0000".equals(code)) {
				base.setCode(MsgCode.success);
				base.setData(map.get("data"));
				base.setMsg(msg);
			} else {
				base.setCode(MsgCode.businessError);
				base.setMsg(msg);
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("申报记录列表查询异常：", e);
		}
		renderJSON(base);
	}
	
	
	/**
	 * 电子回单列表查询
	 * 
	 * @param orderNumber
	 * @param numberPlateNumber
	 * @param plateType
	 * @param sourceOfCertification
	 */
	@RequestMapping(value = "toQueryElectronicReceiptPage")
	public void toQueryElectronicReceiptPage(String billNo, String licensePlateNo, String drivingLicenceNo,String sourceOfCertification) {
		BaseBean base = new BaseBean();
		if (StringUtil.isBlank(billNo)&&StringUtil.isBlank(licensePlateNo)&&StringUtil.isBlank(drivingLicenceNo)) {
			base.setCode("0001");
			base.setMsg("驾驶证/车牌/缴款编号 不能全部为空！");
			renderJSON(base);
			return;
		}

		try {
			billNo = billNo.replaceAll(" ", "");
			base=illegalService.toQueryElectronicReceiptPage(billNo, licensePlateNo, drivingLicenceNo,sourceOfCertification);
			/*String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if ("0000".equals(code)) {
				base.setCode(MsgCode.success);
				base.setData(map.get("data"));
				base.setMsg(msg);
			} else {
				base.setCode(MsgCode.businessError);
				base.setMsg(msg);
			}*/
		} catch (Exception e) {
			DealException(base, e);
			logger.error("电子回单记录列表查询异常：", e);
		}
		renderJSON(base);
	}
	
	/**
	 * 电子印章信息查询
	 * 
	 * @param orderNumber
	 * @param numberPlateNumber
	 * @param plateType
	 * @param sourceOfCertification
	 */
	@RequestMapping(value = "szTrafficPoliceElecBillQry")
	public void szTrafficPoliceElecBillQry(HttpServletResponse resp,String orderId) {
		BaseBean base = new BaseBean();
		if (StringUtil.isBlank(orderId)) {
			base.setCode("0001");
			base.setMsg("订单号不能为空！");
			renderJSON(base);
			return;
		}

		try {

			String url=illegalService.szTrafficPoliceElecBillQry(orderId);
			/*String code = (String) map.get("code");
			String msg = (String) map.get("msg");
			if ("0000".equals(code)) {
				base.setCode(MsgCode.success);
				base.setData(map.get("data"));
				base.setMsg(msg);
			} else {
				base.setCode(MsgCode.businessError);
				base.setMsg(msg);
			}*/
			resp.sendRedirect(url);
		} catch (Exception e) {
			DealException(base, e);
			logger.error("电子回单记录列表查询异常：", e);
		}
		renderJSON(base);
	}
	
	
	/**
	 * 车管规费缴款
	 * 
	 * @param orderNumber
	 * @param numberPlateNumber
	 * @param plateType
	 * @param sourceOfCertification
	 */
	@RequestMapping(value = "toQueryFeePage")
	public void toQueryFeePage(String billNo, String licensePlateNo, String mobilephone, String openId,String sourceOfCertification ) {
		BaseBean base = new BaseBean();
		try {
			// 参数校验
			if (StringUtil.isEmpty(billNo)) {
				base.setCode("0001");
				base.setMsg("缴款编号不能为空！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(openId)) {
				base.setCode("0001");
				base.setMsg("未获取到openId！");
				renderJSON(base);
			}
			if (StringUtil.isEmpty(sourceOfCertification)) {
				base.setCode("0001");
				base.setMsg("来源方式不能为空！");
				renderJSON(base);
			}
			billNo = billNo.replaceAll(" ", "");
			String url = illegalService.toPayPage(billNo, licensePlateNo, mobilephone, openId,sourceOfCertification);
			if (StringUtil.isEmpty(url)) {
				base.setCode("0001");
				base.setMsg("返回页面地址为空！");
			} else {
				base.setCode("0000");
				base.setMsg(url);
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("获取异常：", e);
		}
		renderJSON(base);
	}
	@RequestMapping(value = "sendMessage")
	public void sendMessage(String mobilephone) {
		BaseBean baseBean = new BaseBean();
		try {
			// 参数校验
			if (StringUtil.isEmpty(mobilephone)) {
				baseBean.setCode("0001");
				baseBean.setMsg("手机号不能为空！");
				renderJSON(baseBean);
			}
			String msgContent = "违停免罚申请";
			boolean flag = mobileMessageService.sendMessage(mobilephone, msgContent);
    		if(flag){
    			baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData("发送成功");
    		}else{
    			baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg("发送失败");
    		}
		} catch (Exception e) {
			DealException(baseBean, e);
			logger.error("违停免罚发送短信异常：", e);
		}
		renderJSON(baseBean);
		logger.debug(JSON.toJSONString(baseBean));
	}
	 
}