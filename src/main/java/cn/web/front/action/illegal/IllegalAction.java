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

import com.alibaba.fastjson.JSON;

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
import cn.illegal.bean.SubcribeBean;
import cn.illegal.service.IIllegalService;
import cn.message.model.wechat.WechatUserInfo;
import cn.message.service.IWechatService;
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

	@Autowired
	@Qualifier("wechatService")
	private IWechatService wechatService;

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
			String isReg = illegalService.isRegisterUser(openId);

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
				String str = illegalService.custRegInfoReceive(cust, carList, openId);
				logger.info("同步客户信息！");
			}
			List<IllegalInfoBean> list = illegalService.queryInfoByLicensePlateNo(licensePlateNo, licensePlateType,
					vehicleIdentifyNoLast4, openId);
			base.setCode("0000");
			if (list != null) {
				base.setData(list);
				base.setMsg("成功");
			} else {
				base.setMsg("当前车辆无未处理违法");
			}
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
			String isReg = illegalService.isRegisterUser(openId);

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
				String str = illegalService.custRegInfoReceive(cust, carList, openId);
				System.out.println("同步：" + str);
			}

			List<IllegalInfoBean> list = illegalService.queryInfoByDrivingLicenceNo(drivingLicenceNo, recordNo, openId);
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
			String identityCard, String sourceOfCertification, String openId) {
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
			String isReg = illegalService.isRegisterUser(openId);

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
				String str = illegalService.custRegInfoReceive(cust, carList, openId);
				System.out.println("同步：" + str);
			}
			List<IllegalInfoBean> returnList = new ArrayList<IllegalInfoBean>();
			List<IllegalInfoClaim> infos = new ArrayList<IllegalInfoClaim>();

			BaseBean result = illegalService.trafficIllegalClaimBefore(licensePlateNo, licensePlateType, mobilephone,
					openId);
			String msgCode = result.getCode();
			String msg = result.getMsg();
			if ("0000".equals(msgCode)) {
				infos = (List<IllegalInfoClaim>) JSON.parseArray(result.getData().toString(), IllegalInfoClaim.class);
				returnList = illegalService.queryInfoByLicensePlateNo1(licensePlateNo, licensePlateType, "", openId);
			} else {
				if ("您好，没有查找到可用互联网方式处理的交通违法，感谢您对我局工作的支持！".equals(msg)
						|| "由于您的驾驶证处理此宗违法后累计分已经超过12分，请到各大队违例窗口现场办理。".equals(msg)) {
					returnList = illegalService.queryInfoByLicensePlateNo1(licensePlateNo, licensePlateType, "",
							openId);
				} else {
					base.setCode(msgCode);
					base.setMsg(msg);
					renderJSON(base);
				}
			}

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
					claim.setAgency("");
					claim.setDrivingLicenceNo("");
					infos.add(claim);
				}
			}

			base.setCode("0000");
			if (infos.size() > 0) {
				base.setData(infos);
				base.setMsg("成功");
			} else {
				base.setMsg("当前无未处理的违法");
			}
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
	public void trafficIllegalClaim(String illegalNo, String openId) {
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
			IllegalInfoSheet bean = illegalService.trafficIllegalClaim(illegalNo, openId);
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
			DealException(base, e);
			logger.error("微信回调异常 ", e);
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
						wechatUserInfo.getOpenId());
				response.sendRedirect(url);
			} else {
				logger.error("Wechat 获取用户信息为空！");
			}
		} catch (Exception e) {
			DealException(base, e);
			logger.error("页面跳转异常 ", e);
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
	public void toQueryPunishmentPage(String billNo, String licensePlateNo, String mobilephone, String openId,
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

			String url = illegalService.toQueryPunishmentPage(billNo, licensePlateNo, mobilephone, openId);
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
			CarInfoBean carInfo, String sourceOfCertification) {
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
	public void toCancleSubscribe(String subscribeNo) {
		BaseBean bean = new BaseBean();
		try {
			if (StringUtil.isEmpty(subscribeNo)) {
				bean.setCode("0001");
				bean.setMsg("预约流水号不能为空！");
				renderJSON(bean);
			}
			bean = illegalService.toCancleSubscribe(subscribeNo);
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
}
