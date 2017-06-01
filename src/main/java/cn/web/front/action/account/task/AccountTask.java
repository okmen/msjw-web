package cn.web.front.action.account.task;

import java.io.Serializable;

import cn.account.bean.ReadilyShoot;
import cn.account.bean.vo.ReadilyShootVo;
@SuppressWarnings(value="all")
public class AccountTask implements Runnable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AccountTaskExecute AccountTaskExecute;
	
	private ReadilyShoot readilyShoot;
	
	private ReadilyShootVo readilyShootVo;
	
	
	public AccountTask(AccountTaskExecute accountTaskExecute, ReadilyShoot readilyShoot, ReadilyShootVo readilyShootVo) {
		AccountTaskExecute = accountTaskExecute;
	}
	
	@Override
	public void run() {
		AccountTaskExecute.sendReadilyShootVoDataToPhp(readilyShoot, readilyShootVo);
	}
}
