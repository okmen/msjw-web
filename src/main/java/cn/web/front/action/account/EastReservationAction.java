package cn.web.front.action.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;

import cn.account.bean.ReservationBean;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.web.front.support.BaseAction;
/**
 * 东部预约
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value="/reserve/")
public class EastReservationAction extends BaseAction {
	/**
     * 临时预约前查询
     * @param sourceOfCertification 认证来源 微信C 支付宝Z
     * @throws Exception
     */
    @RequestMapping(value="tempReservationAfterSearch")
    public void tempReservationAfterSearch(String sourceOfCertification) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	/*LoginReturnBeanVo loginReturnBeanVo = accountService.login(loginName,password,sourceOfCertification,openId,loginClient);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg(loginReturnBeanVo.getMsg());
            	baseBean.setData("");
        	}*/
        	List<ReservationBean> reservationBeans = new ArrayList<ReservationBean>();
        	ReservationBean reservationBean1 = new ReservationBean();
        	ReservationBean reservationBean2 = new ReservationBean();
        	reservationBean1.setAddress("梅沙片区");
        	reservationBean1.setDate("2017.05.28");
        	reservationBean1.setTime("am");
        	reservationBean1.setRemainingPercentage("100");
        	
        	reservationBean2.setAddress("大鹏岛片区");
        	reservationBean2.setDate("2017.05.28");
        	reservationBean2.setTime("bm");
        	reservationBean2.setRemainingPercentage("300");
        	
        	reservationBeans.add(reservationBean1);
        	reservationBeans.add(reservationBean2);
        	
        	baseBean.setCode("0000");
        	baseBean.setData(reservationBeans);
        	renderJSON(baseBean);
        	
		} catch (Exception e) {
			baseBean.setCode("0002");
        	baseBean.setData("");
        	baseBean.setMsg("查询失败");
        	renderJSON(baseBean);
        	
			DealException(baseBean, e);
        	logger.error("tempReservationAfterSearch 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
	/**
     * 预约前查询
     * @param sourceOfCertification 认证来源 微信C 支付宝Z
     * @throws Exception
     */
    @RequestMapping(value="reservationAfterSearch")
    public void reservationAfterSearch(String sourceOfCertification) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	/*LoginReturnBeanVo loginReturnBeanVo = accountService.login(loginName,password,sourceOfCertification,openId,loginClient);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg(loginReturnBeanVo.getMsg());
            	baseBean.setData("");
        	}*/
        	List<ReservationBean> reservationBeans = new ArrayList<ReservationBean>();
        	ReservationBean reservationBean1 = new ReservationBean();
        	ReservationBean reservationBean2 = new ReservationBean();
        	reservationBean1.setAddress("梅沙片区");
        	reservationBean1.setDate("2017.05.28");
        	reservationBean1.setTime("am");
        	reservationBean1.setRemainingPercentage("10");
        	
        	reservationBean2.setAddress("梅沙片区");
        	reservationBean2.setDate("2017.05.28");
        	reservationBean2.setTime("bm");
        	reservationBean2.setRemainingPercentage("20");
        	
        	reservationBeans.add(reservationBean1);
        	reservationBeans.add(reservationBean2);
        	
        	baseBean.setCode("0000");
        	baseBean.setData(reservationBeans);
        	
        	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0002");
        	baseBean.setData("");
        	baseBean.setMsg("查询失败");
        	renderJSON(baseBean);
        	
			DealException(baseBean, e);
        	logger.error("reservationAfterSearch 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
	
	/**
	 * 预约
	 * @param sourceOfCertification 认证来源  微信C 支付宝Z
	 * @param mobilephone 手机号
	 * @param validateCode 验证码
	 * @param plateNumber 车牌号码
	 * @param plateType 号牌种类
	 * @param vehicleType 车辆类型
	 * @param fourDigitsAfterTheEngine 发动机后4位
	 * @param time 上午-am  下午-bm
	 * @param date 预约日期
	 * @param address 预约地点 	1-梅沙片区、2-大鹏半岛片区
	 * @throws Exception
	 */
    @RequestMapping(value="Reservation")
    public void Reservation(String sourceOfCertification,String mobilephone,String validateCode,String plateNumber,String plateType,String vehicleType,
    		String fourDigitsAfterTheEngine,String time,String date,String address) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(validateCode)){
        		baseBean.setMsg("validateCode 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(plateNumber)){
        		baseBean.setMsg("plateNumber 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	
        	if(StringUtils.isBlank(plateType)){
        		baseBean.setMsg("plateType 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(vehicleType)){
        		baseBean.setMsg("vehicleType 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(fourDigitsAfterTheEngine)){
        		baseBean.setMsg("fourDigitsAfterTheEngine 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(time)){
        		baseBean.setMsg("time 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(date)){
        		baseBean.setMsg("time 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(address)){
        		baseBean.setMsg("address 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	/*LoginReturnBeanVo loginReturnBeanVo = accountService.login(loginName,password,sourceOfCertification,openId,loginClient);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg(loginReturnBeanVo.getMsg());
            	baseBean.setData("");
        	}*/
        	baseBean.setCode("0000");
        	baseBean.setData("");
        	baseBean.setMsg("预约成功");
        	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0002");
        	baseBean.setData("");
        	baseBean.setMsg("预约失败");
        	renderJSON(baseBean);
        	
			DealException(baseBean, e);
        	logger.error("login 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    /**
     * 预约查询
     * @param sourceOfCertification 认证来源 微信C 支付宝Z
     * @param mobilephone 手机号
     * @param validateCode 验证码
     * @throws Exception
     */
    @RequestMapping(value="getReservation")
    public void getReservation(String sourceOfCertification,String mobilephone,String validateCode) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(mobilephone)){
        		baseBean.setMsg("mobilephone 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(validateCode)){
        		baseBean.setMsg("validateCode 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	
        	List<ReservationBean> reservationBeans = new ArrayList<ReservationBean>();
        	ReservationBean reservationBean1 = new ReservationBean();
        	ReservationBean reservationBean2 = new ReservationBean();
        	reservationBean1.setAddress("梅沙片区");
        	reservationBean1.setDate("2017.05.28");
        	reservationBean1.setTime("am");
        	reservationBean1.setMobilephone("13688888888");
        	reservationBean1.setReservationNo("8989776755665");
        	reservationBean1.setPlateNumber("粤B877N9");
        	reservationBean1.setReservationStatus("已过期");
        	
        	reservationBean2.setAddress("梅沙片区");
        	reservationBean2.setDate("2017.05.28");
        	reservationBean2.setTime("bm");
        	reservationBean2.setMobilephone("136444444");
        	reservationBean2.setReservationNo("898977532665");
        	reservationBean2.setPlateNumber("粤B477N9");
        	reservationBean2.setReservationStatus("预约中");
        	
        	reservationBeans.add(reservationBean1);
        	reservationBeans.add(reservationBean2);
        	
        	baseBean.setCode("0000");
        	baseBean.setData(reservationBeans);
        	
        	/*LoginReturnBeanVo loginReturnBeanVo = accountService.login(loginName,password,sourceOfCertification,openId,loginClient);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg(loginReturnBeanVo.getMsg());
            	baseBean.setData("");
        	}*/
        	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0002");
        	baseBean.setData("");
        	baseBean.setMsg("查询失败");
        	renderJSON(baseBean);
        	
			DealException(baseBean, e);
        	logger.error("getReservation 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
    /**
     * 取消预约
     * @param sourceOfCertification 认证来源 微信C 支付宝Z
     * @param reservationNo 预约编号
     * @throws Exception
     */
    @RequestMapping(value="cancelReservation")
    public void cancelReservation(String sourceOfCertification,String reservationNo) throws Exception{
    	BaseBean baseBean = new BaseBean();
    	try {
        	if(StringUtils.isBlank(sourceOfCertification)){
        		baseBean.setMsg("sourceOfCertification 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	if(StringUtils.isBlank(reservationNo)){
        		baseBean.setMsg("reservationNo 不能为空!");
        		baseBean.setCode(MsgCode.paramsError);
        		renderJSON(baseBean);
        		return;
        	}
        	/*LoginReturnBeanVo loginReturnBeanVo = accountService.login(loginName,password,sourceOfCertification,openId,loginClient);
        	if(null != loginReturnBeanVo && MsgCode.success.equals(loginReturnBeanVo.getCode())){
        		baseBean.setCode(MsgCode.success);
            	baseBean.setMsg("");
            	baseBean.setData(loginReturnBeanVo);
        	}else{
        		baseBean.setCode(MsgCode.businessError);
            	baseBean.setMsg(loginReturnBeanVo.getMsg());
            	baseBean.setData("");
        	}*/
        	baseBean.setCode("0000");
        	baseBean.setData("");
        	baseBean.setMsg("取消成功");
        	renderJSON(baseBean);
		} catch (Exception e) {
			baseBean.setCode("0002");
        	baseBean.setData("");
        	baseBean.setMsg("取消失败");
        	renderJSON(baseBean);
        	
			DealException(baseBean, e);
        	logger.error("cancelReservation 错误", e);
		}
    	renderJSON(baseBean);
    	logger.debug(JSON.toJSONString(baseBean));
    }
}
