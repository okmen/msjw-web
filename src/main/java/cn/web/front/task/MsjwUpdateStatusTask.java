package cn.web.front.task;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.vo.DriverLicenseToSupplementThePermitBusinessVo;
import cn.account.bean.vo.MotorVehicleBusiness;
import cn.account.service.IAccountService;
import cn.convenience.bean.MsjwApplyingBusinessVo;
import cn.convenience.bean.MsjwApplyingRecordVo;
import cn.convenience.bean.MsjwVehicleInspectionVo;
import cn.convenience.service.IMsjwService;
import cn.handle.bean.vo.CarMortgageBean;
import cn.handle.bean.vo.VehicleInspectionVO;
import cn.handle.service.IHandleService;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.DateUtil2;
import cn.sdk.util.StringUtil;
import cn.web.front.common.NetWorkIp;

/**
 * 民生警务平台定时修改办理业务状态
 * @author jjy
 *
 */
@Component
public class MsjwUpdateStatusTask {
	private Logger logger = Logger.getLogger(MsjwUpdateStatusTask.class);
	
	@Autowired
	private IMsjwService msjwService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IHandleService handleService;
	
	@Scheduled(cron="0 0 17 * * ?")//每天下午5点更新
	public void execute(){
		String curIp = NetWorkIp.getIp();
		logger.info("定时任务，当前服务器ip：" + curIp);
		if(!"192.168.2.187".equals(curIp)){//阿里云服务器地址10.24.193.212
			return;
		}
		
		long begin = System.currentTimeMillis();
		try {
			logger.info("开始定时修改【民生警务】办理状态,服务器ip("+curIp+")-----------------------------------------------------------");
			//获取所有不同身份证
			List<String> identityIdList = msjwService.selectIdentityIdAll();
			logger.info("查询到不同身份证总数为：" + identityIdList.size() + "个");
			for (String identityId : identityIdList) {
				//驾驶证业务
				List<DriverLicenseToSupplementThePermitBusinessVo> driverLicenseList = accountService.getDriverLicenseBusiness(identityId, "M");
				if(driverLicenseList != null && driverLicenseList.size() > 0){
					for (DriverLicenseToSupplementThePermitBusinessVo vo : driverLicenseList) {
						String LYBZ = vo.getLYBZ();//来源标志
						String WWLSH = vo.getWWLSH();//流水号
						String ZHCLZT = vo.getZHCLZT();//业务状态
						updateStatus(LYBZ, WWLSH, ZHCLZT);
					}
				}
				//机动车业务
				List<MotorVehicleBusiness> motorVehicleList = accountService.getMotorVehicleBusiness(identityId, "M");
				if(motorVehicleList != null && motorVehicleList.size() > 0){
					for (MotorVehicleBusiness vo : motorVehicleList) {
						String LYBZ = vo.getLYBZ();//来源标志
						String WWLSH = vo.getWWLSH();//流水号
						String ZHCLZT = vo.getZHCLZT();//业务状态
						updateStatus(LYBZ, WWLSH, ZHCLZT);
					}
				}
				
				BaseBean queryCarMortgage = handleService.queryCarMortgage(identityId, "sqlx", "M");
				@SuppressWarnings("unchecked")
				List<CarMortgageBean> carMortgageList = (List<CarMortgageBean>) queryCarMortgage.getData();
				if (null != carMortgageList && carMortgageList.size()>0) {
					for (CarMortgageBean vo : carMortgageList) {
						String LYBZ = "M";
						String WWLSH = vo.getSerialNumber();//流水号
						String ZHCLZT = vo.getState();//业务状态
						updateCarMortgageStatus(LYBZ, WWLSH, ZHCLZT);
					}
				}
			}
			
			//更新六年免检状态
			updateVehicleInspectionStatus();
		} catch (Exception e) {
			logger.error("定时修改【民生警务】办理状态异常", e);
		} finally{
			long end = System.currentTimeMillis();
			logger.info("结束定时修改【民生警务】办理状态,耗时"+(end-begin)+"----------------------------------------------------------------------------");
		}
	}
	/**
	 * 更新
	 * @param LYBZ 来源标志
	 * @param WWLSH 流水号
	 * @param ZHCLZT 业务状态
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws Exception
	 */
	public void updateCarMortgageStatus(String LYBZ, String WWLSH, String ZHCLZT)throws Exception {
		String status = "车管已签注资料移交邮政，邮政回填EMS单号，车管所退办，邮政退办，车管退办资料移交邮政，资料退办";
		MsjwApplyingRecordVo msjwApplyingRecordVo = msjwService.selectMsjwApplyingRecordByTylsbh(WWLSH);
		if (status.contains(ZHCLZT)) {
			//根据流水号查询数据库
			//MsjwApplyingRecord表中有记录，修改状态并移除记录
			if(msjwApplyingRecordVo != null){
				MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
				BeanUtils.copyProperties(businessVo, msjwApplyingRecordVo);
				//修改msjw平台状态和显示
				businessVo.setShowstatus(ZHCLZT);
				businessVo.setListstatus("04");//只在msjw平台进度查询中显示
				msjwService.updateApplyingBusiness(businessVo);
				
				//从MsjwApplyingRecord表中根据id删除数据
				msjwService.deleteMsjwApplyingRecordById(msjwApplyingRecordVo.getId());
				
				//新增到MsjwFinishedRecord
				msjwApplyingRecordVo.setShowstatus(ZHCLZT);
				msjwApplyingRecordVo.setListstatus("04");
				msjwApplyingRecordVo.setStatus(ZHCLZT);
				msjwService.addMsjwFinishedRecord(msjwApplyingRecordVo);
			}
		}else{
			if(msjwApplyingRecordVo != null){
				MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
				BeanUtils.copyProperties(businessVo, msjwApplyingRecordVo);
				//修改msjw平台状态说明
				businessVo.setShowstatus(ZHCLZT);
				msjwService.updateApplyingBusiness(businessVo);
				//修改数据库状态
				msjwApplyingRecordVo.setStatus(ZHCLZT);//业务状态
				msjwApplyingRecordVo.setShowstatus(ZHCLZT);//状态说明
				msjwService.updateMsjwApplyingRecordById(msjwApplyingRecordVo);
			}
		}
		

	}
	/**
	 * 更新
	 * @param LYBZ 来源标志
	 * @param WWLSH 流水号
	 * @param ZHCLZT 业务状态
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws Exception
	 */
	public void updateStatus(String LYBZ, String WWLSH, String ZHCLZT)throws Exception {
		//获取来源为M
		if("M".equals(LYBZ)){
			//状态为2-车管已制证，TB-退办
			if("2".equals(ZHCLZT) || "TB".equals(ZHCLZT)){
				String showstatus = "2".equals(ZHCLZT) ? "车管已制证" : "退办";
				//根据流水号查询数据库
				MsjwApplyingRecordVo msjwApplyingRecordVo = msjwService.selectMsjwApplyingRecordByTylsbh(WWLSH);
				//MsjwApplyingRecord表中有记录，修改状态并移除记录
				if(msjwApplyingRecordVo != null){
					MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
					BeanUtils.copyProperties(businessVo, msjwApplyingRecordVo);
					//修改msjw平台状态和显示
					businessVo.setShowstatus(showstatus);
					businessVo.setListstatus("04");//只在msjw平台进度查询中显示
					msjwService.updateApplyingBusiness(businessVo);
					
					//从MsjwApplyingRecord表中根据id删除数据
					msjwService.deleteMsjwApplyingRecordById(msjwApplyingRecordVo.getId());
					
					//新增到MsjwFinishedRecord
					msjwApplyingRecordVo.setShowstatus(showstatus);
					msjwApplyingRecordVo.setListstatus("04");
					msjwApplyingRecordVo.setStatus(ZHCLZT);
					msjwService.addMsjwFinishedRecord(msjwApplyingRecordVo);
				}
			}
			//状态为0待初审，3待初审，1初审通过，待制证
			else{
				//根据流水号查询数据库
				MsjwApplyingRecordVo msjwApplyingRecordVo = msjwService.selectMsjwApplyingRecordByTylsbh(WWLSH);
				if(msjwApplyingRecordVo != null){
					//数据库的状态
					String status = msjwApplyingRecordVo.getStatus();
					//查询的状态与数据库状态不一样
					if(!ZHCLZT.equals(status)){
						String showstatus = "";//0待初审，3待初审，1初审通过，待制证
						if("0".equals(ZHCLZT)){
							showstatus = "待初审";
						}else if("3".equals(ZHCLZT)){
							showstatus = "待初审";
						}else if("1".equals(ZHCLZT)){
							showstatus = "初审通过，待制证";
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							BeanUtils.copyProperties(businessVo, msjwApplyingRecordVo);
							//修改msjw平台状态说明
							businessVo.setShowstatus(showstatus);
							msjwService.updateApplyingBusiness(businessVo);
						}
						
						//修改数据库状态
						msjwApplyingRecordVo.setStatus(ZHCLZT);//业务状态
						msjwApplyingRecordVo.setShowstatus(showstatus);//状态说明
						msjwService.updateMsjwApplyingRecordById(msjwApplyingRecordVo);
					}
				}
			}
		}
	}
	
	
	public void updateVehicleInspectionStatus() throws Exception{
		logger.info("【民生警务】开始-六年免检状态定时更新.............................");
		//查询数据库中审核状态为0-待审核记录
		List<MsjwVehicleInspectionVo> list = msjwService.selectMsjwVehicleInspectionStatusZero(null, null);
		logger.info("查询到数据库中审核状态为0-待审核记录总数：" + list.size());
		for (MsjwVehicleInspectionVo msjwVehicleInspectionVo : list) {
			String tylsbh = msjwVehicleInspectionVo.getTylsbh();
			String platNumber = msjwVehicleInspectionVo.getPlatNumber();
			JSONObject jsonObject = null;
			try {
				//根据tylsbh和platNumber查询车管所最新状态
				jsonObject = handleService.getVehicleInspection(tylsbh, platNumber, "");
			} catch (Exception e) {
				logger.error("【民生警务】定时任务，根据tylsbh和platNumber查询车管所最新状态异常", e);
				e.printStackTrace();
			}
			if(jsonObject != null){
				String code = jsonObject.getString("code");
				if("00".equals(code)){
					String resultStr = jsonObject.getString("result");
					VehicleInspectionVO vehicleInspectionVO = null;
					if(StringUtil.isNotBlank(resultStr)){
						JSONObject parseObject = JSON.parseObject(resultStr);
						vehicleInspectionVO = JSON.parseObject(parseObject.getString("VehicleInspectionVO"), VehicleInspectionVO.class);
					}
					if(vehicleInspectionVO != null){
						String approveState = vehicleInspectionVO.getApproveState();
						if("2".equals(approveState)){//审核状态为2-已审核
							//根据tylsbh更新msjw记录
							MsjwApplyingBusinessVo businessVo = new MsjwApplyingBusinessVo();
							BeanUtils.copyProperties(businessVo, msjwVehicleInspectionVo);
							//修改msjw平台状态，显示位置，url
							String msjwUrl = generateUrl(handleService.getMsjwSixyearsUrl(), vehicleInspectionVO);
							businessVo.setApplyingUrlWx(msjwUrl);
							businessVo.setJinduUrlWx(msjwUrl);
							businessVo.setShowstatus("已审核");
							businessVo.setListstatus("04");//只在msjw平台进度查询中显示
							try {
								msjwService.updateApplyingBusiness(businessVo);
							} catch (Exception e) {
								logger.error("【民生警务】定时任务，根据tylsbh更新民生警务个人中心异常", e);
								e.printStackTrace();
							}
							
							try {
								//根据tylsbh和platNumber更新数据库状态
								msjwVehicleInspectionVo.setApplyingUrlWx(msjwUrl);
								msjwVehicleInspectionVo.setJinduUrlWx(msjwUrl);
								msjwVehicleInspectionVo.setApproveState(approveState);
								msjwVehicleInspectionVo.setLastupddate(DateUtil2.date2str(new Date()));
								msjwVehicleInspectionVo.setListstatus("04");
								msjwVehicleInspectionVo.setShowstatus("已审核");
								msjwService.updateMsjwVehicleInspection(msjwVehicleInspectionVo);
							} catch (Exception e) {
								logger.error("【民生警务】定时任务，根据tylsbh和platNumber更新数据库状态异常", e);
								e.printStackTrace();
							}
							
							/*try {
								//根据tylsbh和platNumber删除记录
								msjwService.deleteMsjwVehicleInspection(tylsbh, platNumber);
							} catch (Exception e) {
								logger.error("【民生警务】定时任务，根据tylsbh和platNumber删除数据库记录异常", e);
								e.printStackTrace();
							}*/
						}
					}
				}
			}
		}
		logger.info("【民生警务】结束-六年免检状态定时更新.............................");
	}
	
	public String generateUrl(String baseUrl, VehicleInspectionVO vo){
		StringBuffer sb = new StringBuffer();
		sb.append(baseUrl)
		.append("&yyh=").append(vo.getBookNumber())//预约号
		.append("&cphm=").append(vo.getPlatNumber())//车牌号码
		.append("&xm=").append(vo.getName())//姓名
		.append("&sjhm=").append(vo.getMobile())//手机号码
		.append("&bxsxrq=").append(vo.getEffectiveDate())//保险生效日期
		.append("&slgzfs=").append(vo.getInform())//受理告知方式
		.append("&cjrq=").append(vo.getCreateDate())//创建日期
		.append("&yyzt=").append(vo.getBookState())//预约状态
		.append("&shzt=").append(vo.getApproveState())//审核状态
		.append("&shjg=").append(vo.getApproveFlag())//审核结果
		.append("&slyjnr=").append(vo.getApproveInfo());//受理意见内容
		return sb.toString();
	}
}
