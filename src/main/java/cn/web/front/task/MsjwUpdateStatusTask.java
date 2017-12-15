package cn.web.front.task;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.account.bean.vo.DriverLicenseToSupplementThePermitBusinessVo;
import cn.account.bean.vo.MotorVehicleBusiness;
import cn.account.service.IAccountService;
import cn.convenience.bean.MsjwApplyingBusinessVo;
import cn.convenience.bean.MsjwApplyingRecordVo;
import cn.convenience.service.IMsjwService;
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
	
	@Scheduled(cron="0 0 17 * * ?")//每天下午5点更新
	public void execute(){
		String curIp = NetWorkIp.getIp();
		logger.info("定时任务，当前服务器ip：" + curIp);
		if(!"10.24.193.212".equals(curIp)){//阿里云服务器地址10.24.193.212
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
			}
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
}
