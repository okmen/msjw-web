package cn.web.front.action.microClassroom;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSON;

import cn.microclass.bean.studyclassroom.Study;
import cn.microclass.service.IMicroclassService;
/*import cn.account.bean.studyclassroom.Answeroptions;
import cn.account.bean.studyclassroom.Study;
import cn.account.bean.studyclassroom.StudyRecord;*/
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
import cn.web.front.support.BaseAction;

/**
 * 微课堂
 * 创建时间:2017-4-10
 * 创建人:曾令成
 *
 */
@Controller
public class MicroClassroomAction extends BaseAction {
	@Autowired
	private IMicroclassService iMicroclassServer;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 微课堂须知页面
	 * 创建时间:2017-4-10
	 * 创建人:曾令成
	 *
	 */
	@RequestMapping(value="Classroom/noticeIndex.html")
	public void noticeIndex(HttpServletRequest request, Study study){
		 study.setClassroomId(request.getParameter("classroomId"));
		Map<String, Object>map=new  HashMap<>();
		 BaseBean baseBean = new BaseBean();
		 if(study!=null&&study.getClassroomId()!=null){
			 
		 
		if(study.getClassroomId().equals("1")){
			map.put("classroomId", 1);
			map.put("noticeTitle", "学习须知");
			map.put("noticeContent",
					  "1、答题条件：准驾车型为C1、C2、C3的驾驶证，有记分交通违法未处理，累计记分小于9分（含9分）的，可以通过手机在线学习交通安全知识消分学习。B类以上驾驶人有交通违法记分的，应当参加审验教育培训学习。"
					+ "2、答题规则：驾驶人在线完成相应道交通安全知识问答，正确率达到100%。每天答题以10题为一个单位进行学习记录，未完成10题中途退出不记分。以10题、累计30题和累计60题对应1分、3分、6分。每次答题正确率达不到100%的，后台不予记录成绩，可多次作答直至正确率达到100%。一个驾驶人在一个记分周期内最多可预存不超过6分（含6分）。"
					+ "3、驾驶人一旦出现交通违法需要记分的，可以立即在线消分学习再提交违法打单，使用学习后获取的记分进行抵扣。"
					+ "4、一次性记满12分，或已经累计达12分的驾驶人不能参加消分学习，需到辖区交警大队报名参加交通安全教育培训，并到车管所预约科目一考试。");
			
		}else if(study.getClassroomId().equals("2")){
			map.put("classroomId", 2);
			map.put("noticeTitle", "满分驾驶人在线培训须知");
			map.put("noticeContent",
					 "为了落实公安部《机动车驾驶证申领与使用规定》（以下简称公安部《139号令》）关于机动车驾驶人在一个记分周期内累积记分达到12分的，机动车驾驶人应当在十五日内到机动车驾驶证核发地或者违法行为地公安机关交通管理部门参加为期七日的道路交通安全法律、法规和相关知识学习的规定，深圳市公安局交通警察局在“深圳交警”APP、服务号中开设交通安全教育微学堂，方便驾驶人利用手机在线完成满分学习教育。"
					+ "一、在线培训学习申请条件："
					+ "1、深圳本地驾驶人在本年度记分周期内，已处理深圳违法行为累计计分达12分或12分以上未达24分的；"
					+ "2、凡是存在处理过一次性扣12分的违法行为均不可申请在线培训，必须到交警大队违例窗口进行现场预约，接受现场培训；"
					+ "3、驾驶人在一个计分周期内只能完成一次满分在线培训，如第二次达12分或12分以上，必须到交警大队违例窗口进行现场预约，接受现场培训；"
					+ "二、在线培训学习流程"
					+ "1、预约报名：凡持有深圳驾驶证且已成为深圳交警星级用户的驾驶人，在一个记分周期内累计计分达12分或12分以上未达24分的驾驶人；如在线学习超过30天（从第一天在线学习做题起算）还未合格的驾驶人，则只能选择到交警大队违例窗口进行现场预约。"
					+ "2、在线培训：当事人申请审核成功之后，可利用手机进行在线交通安全教育培训。在培训过程中，一旦用户选中了选项，则不允许修改。如答题正确，将自动跳转至下一题；如答错，则立即提示，但不可修改。一天内有两次在线培训机会，如果第一次在线培训不合格，可再进行第二次在线培训。一天内两次培训均不合格，则当天在线培训不合格。"
					+ "当事人应在10天内累计在线培训合格5天，则视为当前在线交通安全教育培训合格。"
					+ "3、培训结业查询：在线培训合格后，系统会自推送短信至当事人手机，当事人可于第二日后通过系统查询。"
					+ "4、在线培训证书：当事人在完成在线培训合格后，将收到系统生成的电子结业证书，当事人可凭此电子结业证书通过登陆“网上深圳交警” www.stc.gov.cn—网上车管所—驾驶证业务—满分考试，进行网上预约满分考试。如考试合格，则凭合格成绩单到所扣证的交警大队期满后方可取证。如考试不合格，则只能选择到交警大队违例窗口进行现场预约。");
		}else if(study.getClassroomId().equals("3")){
			map.put("classroomId", 3);
			map.put("noticeTitle", "B类及B类以上驾驶人在线培训须知");
			map.put("noticeContent",
					"为了落实公安部《机动车驾驶证申领与使用规定》（以下简称公安部《139号令》）关于持有大型客车、牵引车、城市公交车、中型客车、大型货车驾驶证的驾驶人，在一个记分周期内有记分记录须参加审验教育培训的规定。深圳市公安局交通警察局在“深圳交警”APP、服务号中开设交通安全教育微学堂，方便B类及B类以上驾驶员（校车驾驶人、大型客车驾驶人、危化品汽车驾驶人除外）利用手机完成在线交通安全审验教育学习的便民服务。"
					+ "一、在线培训学习申请条件："
					+ "1、持有大型客车、牵引车、城市公交车、中型客车、大型货车驾驶证的驾驶人；"
					+ "2、选择在线学习的日期必须在本人驾驶证年度记分周期结束日之后一个月内完成；"
					+ "二、在线培训学习流程："
					+ "1、预约报名：凡持有大型客车、牵引车、城市公交车、中型客车、大型货车驾驶证的在一个记分周期内有记分记录的且为深圳交警星级用户的驾驶人；如在线学习日期超过记分周期一个月的则只能选择现场培训。"
					+ "2、在线培训：当事人申请审核成功之后，可利用手机进行在线交通安全教育培训。在培训过程中，一旦用户选中了选项，则不允许修改。如答题正确，将自动跳转至下一题；如答错，则立即提示，但不可修改。一天内有两次在线培训机会，如果第一次在线培训不合格，可再进行第二次在线培训。一天内两次培训均不合格，则当天在线培训不合格。 当事人应在7天内累计在线培训合格3天，则视为当前年度远程交通安全教育培训合格。"
					+ "3、培训转换：当事人可根据自身实际情况进行在线培训与现场培训的转换； （1）现场培训转在线培训：如当事人预约了现场培训，可在培训前两个工作日转为在线培训，在转为在线培训前，需取消现场培训预约记录。（2）在线培训转现场培训：已通过审核且在线进行培训但未达到合格的当事人，可通过登陆“网上深圳交警” www.stc.gov.cn进入“驾驶人教育培训互联网预约系统”进行预约现场培训。 现场培训或在线培训，年度内只要有其中一项合格记录，则此年度审验教育学习合格。"
					+ "4、培训结业查询：在线培训合格后，系统会自推送短信至当事人手机，当事人可于第二日后通过系统查询。"
					+ "5、在线培训证书：当事人在完成年度在线培训合格后，将收到系统生成的电子结业证书，当事人可凭此电子结业证书并携带体验证明、驾驶证等相关资料到就近交警大队窗口办理年度驾驶证的审验业务。");
			
		}else if(study.getClassroomId().equals("4")){
			map.put("classroomId", 4);
			map.put("noticeTitle", "特殊行业电动自行车备案及违法培训学习须知");
			map.put("noticeContent",
					 "尊敬的特殊行业电动自行车驾驶人，根据《深圳经济特区道路交通安全管理条例》第三十六条“市公安机关交通管理部门可以根据道路通行条件，划定区域、路段、时段，对摩托车、电瓶车以及电动自行车和其他非机动车采取限制通行或者禁止通行的措施，但经市公安机关交通管理部门批准并核发专用标志的车辆除外”。按照此规定，特殊行业电动自行车需到公安机关交通管理部门实施备案。为便于特殊行业电动自行车管理，特殊行业电动自行车驾驶人可以参加交通安全教育培训。深圳市公安局交通警察局在“深圳交警”APP、服务号中开设特行电动自行车管理教育微学堂，方便特殊行业电动自行车驾驶人利用手机完成在线交通安全教育学习的培训学习服务。考试合格后方可驾驶特殊行业电动自行车上路行驶。"
					+ "一、在线培训学习申请条件:"
					+ "特殊行业电动自行车的驾驶人。"
					+ "二、在线培训学习流程："
					+ "1、注册特殊行业电动自行车驾驶人星级用户：特殊行业电动自行车驾驶人可以注册深圳交警星级用户。"
					+ "2、首次登入在线备案培训：特殊行业电动自行车驾驶人可使用手机在微信、支付宝、深圳交警APP客户端等平台登入进行首次登入备案考试。"
					+ "3、考题及分值：考试题库将随机抽取20道涉及非机动车、电动自行车交通法律、法规考题，每题5分，共100分，合格标准为90分以上（含90分）。"
					+ "4、违法学习培训：特殊行业电动自行车遇有交通违法时，需通过特殊行业电动自行车驾驶人违法学习考试后，方可处理。并实施每年首违免罚管理政策。"
					+ "5、培训结业查询：在线培训合格后，系统会自推送短信至特殊行业电动自行车驾驶人手机，可于第二日后通过系统查询。"
					+ "特殊行业是指在本市行政区域内从事邮政、报刊投递、快递、公共设施抢修以及运送瓶装燃气、桶装饮用水、鲜奶等涉及社会生产生活、民生的行业。");
		}else if(study.getClassroomId().equals("5")){
			map.put("classroomId", 5);
			map.put("noticeTitle", "行人、非机动车驾驶人安全学习");
			map.put("noticeContent",
					  "（1）行人、非机动车驾驶人注册成为星级用户后可以参加学习。"
					  + "（2）行人、非机动车驾驶人参加完安全学习并合格通过的，三个月内首次交通违法的，予以警告处罚。"
					  + "（３）学习规则：题库随机抽取20道题，每道题5分，90分为合格。");
		 }
		 }else{
			List<Object>list=new ArrayList<>();
			Map<String, Object>map1=new  HashMap<>();
			map1.put("classroomId", "1");
			map1.put("classroomName","消分学习");
			list.add(map1);
			Map<String, Object>map2=new  HashMap<>();
			map2.put("classroomId","2");
			map2.put("classroomName", "满分学习");
			list.add(map2);
			Map<String, Object>map3=new  HashMap<>();
			map3.put("classroomId", "3");
			map3.put("classroomName", "满分学习");
			list.add(map3);
			Map<String, Object>map4=new  HashMap<>();
			map4.put("classroomId", "4");
			map4.put("classroomName", "满分学习");
			list.add(map4);
			Map<String, Object>map5=new  HashMap<>();
			map5.put("classroomId", "5");
			map5.put("classroomName", "满分学习");
			list.add(map5);
			baseBean.setCode("0000");
			baseBean.setMsg("查询成功");
			baseBean.setData(list);
			renderJSON(baseBean);
		}
		baseBean.setCode("0000");
		baseBean.setMsg("查询成功");
		renderJSON(map);
				
	}

	 /**
	  * 微课堂所有查询相关信息方法
	 * @throws Exception 
	  */
	 @RequestMapping(value="Classroom/StudyHomepages.html")
	 public void StudyHomepages(HttpServletRequest request, Study study) throws Exception{
		 String ip=  MicroClassroomAction.getIpAddr(request);
		 BaseBean base=new BaseBean();
		 Study  s=new Study();
		 List<BaseBean>list=null;
		try { 
		 if(study!=null){
			 //公用参数
			 if(study.getIdentityCard()!=null){
				 s.setIdentityCard(study.getIdentityCard());
			 }else{
				  base.setMsg("身份证号码不能为空");
				  base.setCode("0002");
				  renderJSON(base);	
				  return;
			 }
			 if(study.getMobilephone()!=null){
				 s.setMobilephone(study.getMobilephone());
			 }else{
				 base.setMsg("手机号码不能为空");
				  base.setCode("0002");
				  renderJSON(base);	
				  return;
			 }
			 
			 if(study.getUserSource()!=null){
				 s.setUserSource(study.getUserSource());
			 }else{
				 base.setMsg("用户来源不能为空");
				  base.setCode("0002");
				  renderJSON(base);	
				  return;
			 }
			 
			 if(ip!=null){
				 s.setIpAddress(ip);				  
			 }else{
				 base.setMsg("ip地址不能为空");
				  base.setCode("0002");
				  renderJSON(base);	
				  return;
			 }
			 if(study.getClassroomId()!=null){
				 s.setClassroomId(study.getClassroomId());
			 }else{
				 base.setMsg("列表ID不能为空");
				  base.setCode("0002");
				  renderJSON(base);	
				  return;
			 }
			 
			 if(study.getClassroomId().equals("1")){  //当列表ID等于1的时候 进入消分学习
					s.setInterfaceId("exam003");
					list= iMicroclassServer.xfStudyQuery(s);
			 }else if(study.getClassroomId().equals("2")){  
				  s.setInterfaceId("mfyydtjgcx");			//满分学习查询接口
				  list=iMicroclassServer.xrStudyQuery(s);	
			 }else if(study.getClassroomId().equals("3")){				 
				 s.setInterfaceId("blyydtjgcx");  //	B类驾驶人培训学习结果查询 接口
				 list=iMicroclassServer.xrStudyQuery(s);
			 }else if(study.getClassroomId().equals("4")){ //6.33.3	电动车违法学习结果查询
				 	s.setInterfaceId("DDC2003");
					s.setServiceType("");
					s.setDecisionId("");
					list=iMicroclassServer.xrStudyQuery(s);
			 }else if(study.getClassroomId().equals("5")){  //6.34	行人、非机动车驾驶人道路交通安全学习
				 	s.setInterfaceId("DDC3003");
					s.setServiceType("AQ");
					list=iMicroclassServer.xrStudyQuery(s);	
			 }
			 if(list!=null){
				for(BaseBean b:list){
							base.setCode(b.getCode());
							base.setMsg(b.getMsg());
							base.setData(b.getData());
				}
			 }
		 }
		 renderJSON(base);	
		// logger.info("查询日志"+JSON.toJSONString(base));
		// logger.info(JSON.toJSONString(base));
		} catch (Exception e) {
			DealException(base, e);
			logger.error("StudyHomepages方法："+study, e);
			throw e;
		}
	 }

	 /**
	  * 微课堂所有学习取题方法
	  * @param request
	  * @param study
	 * @throws Exception 
	  */
	 @RequestMapping(value="Classroom/Studys.html")
	 public void Studys(HttpServletRequest request,Study study) throws Exception{
		 String ip=  MicroClassroomAction.getIpAddr(request);
		 BaseBean base=new BaseBean();
		 List<BaseBean>list=null;
		 Study  s=new Study();
		 try {
 
		 if(study!=null){
			 //公用参数
			 if(study.getIdentityCard()!=null){
				 s.setIdentityCard(study.getIdentityCard());
			 }else{
				 base.setMsg("身份证号码不能为空！");
				 base.setCode(MsgCode.paramsError);
				 renderJSON(base);	
			 }
			 if(study.getMobilephone()!=null){
				 s.setMobilephone(study.getMobilephone());
			 }else{
				 base.setMsg("手机号码不能为空！");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);
			 }
			 if(study.getUserSource()!=null){
				 s.setUserSource(study.getUserSource());
			 }else{
				 base.setMsg("用户来源不能为空！");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);
			 }
			 if(ip!=null){
				 s.setIpAddress(ip);				  
			 }else{
				 base.setMsg("ip地址不能为空");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);	
			 }		 
		
			 if(study.getClassroomId()!=null){
				 s.setClassroomId(study.getClassroomId());
			 }else{
				 base.setMsg("列表ID不能为空");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);	
			 }
		 if(study.getClassroomId().equals("1")){ //当列表ID等于1的时候 进入消分学习取题 
			 s.setInterfaceId("exam001");
			 list= iMicroclassServer.xfStudyAnswer(s);	 
		 }else if(study.getClassroomId().equals("2")){        //当列表ID等于2的时候进入学习非机动车学习取题
			 s.setInterfaceId("mfyyqt");
			 list= iMicroclassServer.xfStudyAnswer(s);
		 }else if(study.getClassroomId().equals("3")){
			 s.setInterfaceId("blyyqt");
			 list= iMicroclassServer.xfStudyAnswer(s); 
		 }else if(study.getClassroomId().equals("4")){
			 	s.setInterfaceId("DDC2001"); //电动车违法取题编号
				s.setSubjectId("");
				s.setServiceType("");		
				list=iMicroclassServer.ddcStudyAnswer(s);
		 }else if(study.getClassroomId().equals("5")){
			    s.setInterfaceId("DDC3001");
				s.setServiceType("AQ");
			    list=iMicroclassServer.xrStudyAnswer(s);
		 }
		 if(list!=null){
			 
		 
			for(BaseBean b:list){
				base.setCode(b.getCode());
				base.setMsg(b.getMsg());
				base.setData(b.getData());
			}
		 }
			renderJSON(base);
			logger.debug("随机取题日志"+JSON.toJSONString(base));
	 }
	 } catch (Exception e) {
		 DealException(base, e);
		 logger.error("Studys方法："+study, e);
		 throw e;
	 }
	 }
	 
	 
	 /**
	  * 微课所有答题方法
	  * @param request
	 * @throws Exception 
	  */
	 @RequestMapping("Classroom/Answers.html")
	 public void  Answer(HttpServletRequest request,Study study) throws Exception{
		 String ip=MicroClassroomAction.getIpAddr(request);
		 String subjectId =request.getParameter("subjectId");
		 BaseBean base=new BaseBean();
		 List<BaseBean>list=null;
		 Study  s=new Study();
		 try {
			
		
	        
		 if(study!=null){
			 //公用参数
			 if(study.getIdentityCard()!=null){
				 s.setIdentityCard(study.getIdentityCard());
			 }else{
				 base.setMsg("身份证号码不能为空！");
				 base.setCode(MsgCode.paramsError);
				 renderJSON(base);	
			 }
			 if(study.getMobilephone()!=null){
				 s.setMobilephone(study.getMobilephone());
			 }else{
				 base.setMsg("手机号码不能为空！");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);
			 }
			 if(study.getUserSource()!=null){
				 s.setUserSource(study.getUserSource());
			 }else{
				 base.setMsg("用户来源不能为空！");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);
			 }
			 if(ip!=null){
				 s.setIpAddress(ip);				  
			 }else{
				 base.setMsg("ip地址不能为空");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);	
			 }
			 if(study.getSubjectId()!=null){
				 s.setSubjectId(study.getSubjectId());
			 }else{
				 base.setMsg("答题编号不能为空！");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);	
			 }
			 if(study.getTestQuestionsType()!=null){
				 s.setTestQuestionsType(study.getTestQuestionsType());
			 }else{
				 base.setMsg("答题类型不能为空！");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);	
			 }
			 if(study.getSubjectAnswer()!=null){
				 if(s.getTestQuestionsType().equals("判断题")){
					 if(study.getSubjectAnswer().equals("A")){
						 s.setSubjectAnswer("Y");
					 }else{
						 s.setSubjectAnswer("N");
					 }
				 }else{
					 s.setSubjectAnswer(study.getSubjectAnswer());
				 }
				 
				 
			 }else{
				 base.setMsg("答题答案不能为空！");
				 base.setCode(MsgCode.paramsError);
				  renderJSON(base);	
			 }
			 if(study.getClassroomId()!=null){
				 s.setClassroomId(study.getClassroomId());
			 }
		 
		 Date date=new Date();
		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		 String time=format.format(date);
		 s.setAnswerDate(time);
		 if(s.getClassroomId().equals("1")){ //当列表ID等于1的时候 进入消分学习答题
			 	s.setInterfaceId("exam002");
			 	if(study.getScoreStartDate()==null){
					 base.setMsg("计分周期开始时间不能为空");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);	
				 }else{
					 s.setScoreStartDate(study.getScoreStartDate());
				 }
				 if(study.getScoreEndDate()==null){
					 base.setMsg("计分周期结束时间不能为空");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);	
				 }else{
					 s.setScoreEndDate(study.getScoreEndDate());
				 }
				 if(study.getUserName()==null){
					 base.setMsg("考生姓名不能为空！");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);
				 }else{
					 s.setUserName(study.getUserName());
				 }
				s.setAnswerDateTime(time);
				list=iMicroclassServer.xfAnswerQuey(s); 
		 }else if(s.getClassroomId().equals("2")){
			 	s.setInterfaceId("mfyydt");
			 	if(study.getScoreStartDate()==null){
					 base.setMsg("计分周期开始时间不能为空");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);	
				 }else{
					 s.setScoreStartDate(study.getScoreStartDate());
				 }
				 if(study.getScoreEndDate()==null){
					 base.setMsg("计分周期结束时间不能为空");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);	
				 }else{
					 s.setScoreEndDate(study.getScoreEndDate());
				 }
				 if(study.getUserName()==null){
					 base.setMsg("考生姓名不能为空！");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);
				 }else{
					 s.setUserName(study.getUserName());
				 }
				s.setAnswerDateTime(time);
				list=iMicroclassServer.xfAnswerQuey(s); 
		 	}else if(s.getClassroomId().equals("3")){
			 s.setInterfaceId("blyydt");
			 	if(study.getScoreStartDate()==null){
					 base.setMsg("计分周期开始时间不能为空");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);	
				 }else{
					 s.setScoreStartDate(study.getScoreStartDate());
				 }
				 if(study.getScoreEndDate()==null){
					 base.setMsg("计分周期结束时间不能为空");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);	
				 }else{
					 s.setScoreEndDate(study.getScoreEndDate());
				 }
				 if(study.getUserName()==null){
					 base.setMsg("考生姓名不能为空！");
					 base.setCode(MsgCode.paramsError);
					 renderJSON(base);
				 }else{
					 s.setUserName(study.getUserName());
				 }
				s.setAnswerDateTime(time);
				list=iMicroclassServer.xfAnswerQuey(s); 
		 }
		 else if(s.getClassroomId().equals("4")){ 
			   s.setInterfaceId("DDC2002");
				s.setServiceType("");
				s.setDecisionId("");
				s.setAnswerDate(s.getAnswerDate());
				list=iMicroclassServer.ddcAnswerQuey(s);
		 }else if(s.getClassroomId().equals("5")){ 
			 	s.setInterfaceId("DDC3002");
				s.setSubjectId(subjectId);
				s.setServiceType("AQ");
				s.setAnswerDate(s.getAnswerDate());
				list=iMicroclassServer.xrAnswerQuey(s);	
		 }else{
			 base.setCode("0000");
			 base.setMsg("暂时没有其他选择类型");
		 }
		 if(list!=null){
			 for(BaseBean b:list){
					base.setCode(b.getCode());
					base.setMsg(b.getMsg());
					base.setData(b.getData());	
				}
		 }
			renderJSON(base);
			logger.debug("答题日志"+JSON.toJSONString(base));
	 }
		 } catch (Exception e) {
			 	DealException(base, e);
				logger.error("Answer方法"+study, e);
				throw e;
		}
		
	 }

	public IMicroclassService getiMicroclassServer() {
		return iMicroclassServer;
	}

	public void setiMicroclassServer(IMicroclassService iMicroclassServer) {
		this.iMicroclassServer = iMicroclassServer;
	}

	public Logger getLogger() {
		return logger;
	}
	 


	 public static String getIpAddr(HttpServletRequest request) {
		          String ip = request.getHeader("X-Real-IP");
		         if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
		             return ip;
		          }
		          ip = request.getHeader("X-Forwarded-For");
		          if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
		              // 多次反向代理后会有多个IP值，第一个为真实IP。
		              int index = ip.indexOf(',');
		              if (index != -1) {
		                  return ip.substring(0, index);
		              } else {
		                  return ip;
		              }
		          } else {
		              return request.getRemoteAddr();
		          }
		          
		      }

	 
	 /**
	  * 获取app最新版本信息
	  * @param request
	  */
	 /*@RequestMapping("/app/newestVersion.html")
	 public void newestVersion(HttpServletRequest request) {
		 BaseBean baseBean = new BaseBean();
		 String system = request.getParameter("system");
		 try {
			if(StringUtil.isBlank(system)){
				 baseBean.setCode(MsgCode.paramsError);
				 baseBean.setMsg("system不能为空");
				 renderJSON(baseBean);
				 return;
			 }
			 
			 AppVersion appVersion = iMicroclassServer.queryNewestAppVersion(system);
			 if(appVersion != null){
				 baseBean.setCode(MsgCode.success);
				 baseBean.setData(appVersion);
			 }else{
				 baseBean.setCode(MsgCode.businessError);
				 baseBean.setMsg("业务异常");
			 }
		} catch (Exception e) {
			e.printStackTrace();
			DealException(baseBean, e);
		}
		renderJSON(baseBean);
	 }*/
	
}
