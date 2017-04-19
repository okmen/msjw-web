package cn.web.front.action.microClassroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.io.Output;

import cn.microclass.bean.studyclassroom.Answeroptions;
import cn.microclass.bean.studyclassroom.Study;
import cn.microclass.bean.studyclassroom.StudyRecord;
import cn.microclass.service.IMicroclassServer;
/*import cn.account.bean.studyclassroom.Answeroptions;
import cn.account.bean.studyclassroom.Study;
import cn.account.bean.studyclassroom.StudyRecord;*/
import cn.sdk.bean.BaseBean;
import cn.sdk.util.StringUtil;
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
	private IMicroclassServer iMicroclassServer;
	
	
	/**
	 * 微课堂须知页面
	 * 创建时间:2017-4-10
	 * 创建人:曾令成
	 *
	 */
	@RequestMapping(value="Classroom/noticeIndex.html")
	public void noticeIndex(){
		Map<String, Object>map=new  HashMap<>();
		 BaseBean baseBean = new BaseBean();
		map.put("noticeId", 1);
		map.put("noticeTitle", "消分学习");
		map.put("noticeContent","尊敬的各位学员：  您好！欢迎参加“枫香岗中心学校第一期中华传统美德学习班”。为确保学习效果，为学员营造一个稳定和谐的学习氛围，让大家轻松愉快地度过三天的学习时光，请您认真阅读并严格遵守以下学习要求：  1、要求枫香岗中心学校全体教职工（包括民办幼儿园教职工）参加，与会者须按时到会，不迟到、不早退、不旷会，确有特殊事情，须向覃太平校长书面请假。每次请提前5分钟进入会场，做好准备，按座位名就坐。  2、本次学习出勤，在职公办教师纳入继续教育记分，共30学分，旷课一小时扣10分，迟到、早退一次扣5分，旷课一天不计分，请事假半天扣5分；聘请教师无故不到会者每天扣除奖励性工资50元；民办幼儿园教职工出勤与幼儿园园长岗位合格证书挂钩，并纳入幼儿园年终检查考核评估。无故未参加此次学习的师德考核扣5分，不评优评先。  3、本次学习由区教育局指导，枫香岗中心学校举办，请学员安排好家中的事情，严格遵守学习纪律，安心全程参与，高质量地完成学习任务。  4、学习期间由枫香岗中心学校免费提供早餐、中餐、晚餐，请大家文明礼让，按需取用，不浪费。晚上免费提供车辆送到市区。  5、一分诚敬得一份收获，十分诚敬得十份收获。上课期间请远离不文明陋习：关闭手机或调成震动状态，不在会场接打手机；不在会场抽烟、嚼槟榔、吃零食、乱丢垃圾、做私事等，不做与会议无关的事；不打瞌睡，不搞小动作，不交头接耳；不带小孩入会场，不会客，不随意走动；未经同意，不在会上私发任何物品。  6、学习期间，请自备水杯，请利用休息时间处理好个人事情，上课时尽量不下座位。     7、集中学习后要加强自身生活实践、在岗实践和自主学习，切实提高自身的师德修养，3月31日前须向中心学校上交一篇1000字以上的学习心得体会。  考虑不周的地方，敬请您的谅解！有什么意见和建议请向班委会反映，我们将虚心采纳及时改进。  感恩您的理解、支持！祝您生活愉快、学习顺利！");
		
		baseBean.setCode("0000");
		baseBean.setMsg("成功");
		renderJSON(map);
				
	}
	/**
	 * 微课堂查询用户信息
	 * 创建时间:2017-4-10
	 * 创建人:曾令成
	 *
	 */
	 @RequestMapping(value = "Classroom/StudyHomepage.html")
	 public void studyHomepage(){
		 Study study=new  Study();
		 BaseBean baseBean = new BaseBean();
		 StudyRecord record=new StudyRecord();
		 List<Study>list=new ArrayList<>();
		 List<StudyRecord>recordList=new ArrayList<>();
		 study.setClassroomId("001");
		 study.setUserName("小明");
		 study.setDrive("431022199612260078");
		 study.setIdentityCard("431022199612260078");
		 study.setScoreStartDate("2013-07-10");
		 study.setScoreEndDate("2014-07-10");
		 study.setIntegral("2");
		 record.setAnsLogarithm(5);  //答题对数
		 record.setAnswerDate("2017-08-01"); //答题时间
		 /**
		  * 无法编译
		  */
//		 record.setIsComplete("未完成");//是否完成
		 recordList.add(record);
		 study.setStudyRecord(recordList);
		 list.add(study);
		 baseBean.setCode("0000");
		 baseBean.setMsg("取题成功");
		 baseBean.setData(list);
		 renderJSON(baseBean);
	 }
	 /**
	  * 微课堂随机取题
	  * 创建时间:2017-4-10
	  * 创建人:曾令成
	  *
	  */
	 @RequestMapping(value = "Classroom/Study.html")
	 public void studySubject(){
		 try {
			 Study study=new  Study();
			 List<Study>list=new ArrayList<>();
			 List<Answeroptions>answerList=new ArrayList<>();
			 BaseBean baseBean = new BaseBean();
			 study.setSubjectId("1");
			 study.setSubjectName("这个标志是什么意思?");
			 study.setSubjecttype(1);
			 study.setTestQuestionsType("2");
			 study.setSubjectImg("http://web/test.jpg");
			 Answeroptions answer=new  Answeroptions();
			 answer.setAnswerId("A");
			 answer.setAnswerName("靠右停车");
			 Answeroptions answer1=new  Answeroptions();
			 answer1.setAnswerId("B");
			 answer1.setAnswerName("靠左停车");
			 Answeroptions answer2=new  Answeroptions();
			 answer2.setAnswerId("C");
			 answer2.setAnswerName("前方直行");
			 Answeroptions answer3=new  Answeroptions();
			 answer3.setAnswerId("D");
			 answer3.setAnswerName("前方不允许路过");
			 answerList.add(answer);
			 answerList.add(answer1);
			 answerList.add(answer2);
			 answerList.add(answer3);
			 study.setAnsweroptions(answerList);
			 list.add(study);			 
			 baseBean.setCode("取题成功");
			 baseBean.setMsg("1");
			 baseBean.setData(list);
			 renderJSON(baseBean);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	 }
	 
	 /**
	  * 微课堂答题方法
	  * 创建时间:2017-4-10
	  * 创建人:曾令成
	  *
	  */
	 @RequestMapping(value = "Classroom/Answer.html")
	 public void  answer(){
		 try {
			 Study study=new  Study();
			 BaseBean base=new BaseBean();
			 List<Study>list=new ArrayList<>();
			 study.setSubjectName("这个标志是什么意思？");
			 study.setSubjectImg("http://test.jpg");
			 Answeroptions answer=new Answeroptions();
			 study.setSubjectAnswer("靠右边停车");
			 study.setAnswerDate("29");
			 study.setAnswererror(2);
			 study.setSurplusAnswe(10);
			 study.setAnswerState(1);
			 list.add(study);
			 base.setCode("1");
			 base.setMsg("答题正确");
			 base.setData(list);
			 renderJSON(base);	 
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 /**
	  * 微课堂批次答题结果
	  * 创建时间:2017-4-10
	  * 创建人:曾令成
	  *
	  */
	 @RequestMapping(value="Classroom/anserEnd.html")
	 public void answerResult(){
		 Study study=new  Study();
		 BaseBean base=new BaseBean();
		 List<Study>list=new ArrayList<>();
		 study.setDrive("431022199612250036");
		 study.setAnswerDate("2013-08-01");
		 study.setAnswerCount(20);
		 study.setAnswererror(5);
		 study.setAnswerCorrect(15);
		 study.setAnswerDate("2017-09-08");
		 study.setAnswerTime("29");
		 study.setIntegral("5");
		 list.add(study);
		 base.setMsg("答题结束");
		 base.setCode("0000");
		 base.setData(list);
		renderJSON(base);
	 }

	 /***
	  * 随机取题目接口
	  */
	 @RequestMapping(value="Classroom/xfanswerJk.html")
	 public void xfanswerJk(){
		 BaseBean base=new BaseBean();
		 System.out.println("进入消分学习方法");
		 Study st =new Study();	
		 st.setMobilephone("17708404197");
		 st.setIdentityCard("431022199612250036");
		 List<BaseBean>list= iMicroclassServer.xfStudyAnswer(st);
		 for(BaseBean b:list){
			 System.out.println("b="+b.getData());
			 base.setCode(b.getCode());
			 base.setData(b.getData());
			 base.setMsg(b.getMsg());
		 }
		 //String kais=(String) base.getData();
		 renderJSON(base);	 
	 }
	 
	 /**
	  * 消分学习查询信息接口
	  */
	 @RequestMapping(value="Classroom/xfcx.html")
	 public void xfcx(HttpServletRequest request){
		 BaseBean base=new BaseBean();
		 Study  s=new Study();
			s.setInterfaceId("exam003");
			s.setIdentityCard("431022199612250036");
			s.setMobilephone("17708404197");
			s.setIpAddress("123.56.180.216");
			s.setUserSource("C");
			List<BaseBean>list= iMicroclassServer.xfStudyQuery(s);
			for(BaseBean b:list){
				base.setCode(b.getCode());
				base.setMsg(b.getMsg());
				base.setData(b.getData());
			}
		 renderJSON(base);	 
	 }
	 
	 /**
	  * 非机动人 查询
	  * @param request
	  */
	 @RequestMapping(value="Classroom/xrStudyQuery.html")
	 public void xrStudyQuery(HttpServletRequest request){
		 BaseBean base=new BaseBean();
		 Study  s=new Study();
		 s.setInterfaceId("DDC3003");
			s.setIdentityCard("431022199612250036");
			s.setMobilephone("17708404197");
			s.setServiceType("AQ");
			s.setIpAddress("123.56.180.216");
			s.setUserSource("C");
			List<BaseBean>list=iMicroclassServer.xrStudyQuery(s);
			for(BaseBean b:list){
				base.setCode(b.getCode());
				base.setMsg(b.getMsg());
				base.setData(b.getData());
			}
			renderJSON(base);
	 }
	 /**
	  * 非机动车人学习随机取题
	  * @param request
	  */
	 @RequestMapping(value="Classroom/xrStudyAnswer.html")
	 public void xrStudyAnswer(HttpServletRequest request){
		 BaseBean base=new BaseBean();
		 Study  s=new Study();
			s.setInterfaceId("DDC3001");
			s.setIdentityCard("431022199612250036");
			s.setMobilephone("17708404197");
			s.setServiceType("AQ");
			s.setIpAddress("123.56.180.216");
			s.setUserSource("C");
			List<BaseBean>list=iMicroclassServer.xrStudyAnswer(s);
			for(BaseBean b:list){
				base.setCode(b.getCode());
				base.setMsg(b.getMsg());
				base.setData(b.getData());
			}
			renderJSON(base);
		 
	 }
	 
	 
	 
	 

	 /**
	  * 微课堂所有查询相关信息方法
	  */
	 @RequestMapping(value="Classroom/StudyHomepages.html")
	 public void StudyHomepages(HttpServletRequest request, Study study){
		 BaseBean base=new BaseBean();
		 Study  s=new Study();
		 List<BaseBean>list=null;
		/* String identityCard= request.getParameter("identityCard"); //获取身份证号码
		 String classroomId = request.getParameter("classroomId");  //获取列表ID,根据列表ID来判断进行不同的方法
		 String mobilephone = request.getParameter("mobilephone");  //获取手机号码
		 String ipAddress = request.getParameter("ipAddress");  //ip地址
		 String userSource = request.getParameter("userSource");  //用户来源
*/		 study.setIdentityCard(request.getParameter("identityCard"));//获取身份证号码 
		 study.setClassroomId(request.getParameter("classroomId"));//获取列表ID,根据列表ID来判断进行不同的方法
		 study.setMobilephone(request.getParameter("mobilephone"));
		 study.setIpAddress(request.getParameter("ipAddress"));
		 study.setUserSource(request.getParameter("userSource"));
		 if(study!=null){
			 
		 
			 //公用参数
			 if(study.getIdentityCard()!=null){
				 s.setIdentityCard(study.getIdentityCard());
			 }else if(study.getMobilephone()!=null){
				 s.setMobilephone(study.getMobilephone());
			 }else if(study.getIpAddress()!=null){
				 s.setIpAddress(study.getIpAddress());
			 }else if(study.getUserSource()!=null){
				 s.setUserSource(study.getUserSource());
			 }else{
				 base.setMsg("身份证,手机号,用户来源,用户IP地址不能为空！");
			 }
			 
			 
		 }
		 if(study.getClassroomId()!=null){	
			 if(study.getClassroomId().equals("1")){  //当列表ID等于1的时候 进入消分学习
					s.setInterfaceId("exam003");
					s.setIdentityCard("431022199612250036");
					s.setMobilephone("17708404197");
					s.setIpAddress("123.56.180.216");
					s.setUserSource("C");
					list= iMicroclassServer.xfStudyQuery(s);
			 }else if(study.getClassroomId().equals("2")){  //当列表ID等于2的时候进入学习非机动车学习
				 	
			 }else if(study.getClassroomId().equals("3")){
				 	
			 }else if(study.getClassroomId().equals("4")){ //6.33.3	电动车违法学习结果查询
				 	s.setInterfaceId("DDC2003");
					s.setIdentityCard("431022199612250036");
					s.setMobilephone("17708404197");
					s.setServiceType("BA");
					s.setDecisionId("12345");
					s.setIpAddress("123.56.180.216");
					s.setUserSource("C");
					list=iMicroclassServer.ddcStudyQuery(s);
			 }else if(study.getClassroomId().equals("5")){  //6.34	行人、非机动车驾驶人道路交通安全学习
				 	s.setInterfaceId("DDC3003");
					s.setIdentityCard("431022199612250036");
					s.setMobilephone("17708404197");
					s.setServiceType("AQ");
					s.setIpAddress("123.56.180.216");
					s.setUserSource("C");
					list=iMicroclassServer.xrStudyQuery(s);	
			 }
			for(BaseBean b:list){
						base.setCode(b.getCode());
						base.setMsg(b.getMsg());
						base.setData(b.getData());
			}
		 }else{
			 base.setData("必传参数不能为空！");
		 }
		 renderJSON(base);	
		// logger.info(JSON.toJSONString(base));
	 }

	 @RequestMapping(value="Classroom/Studys.html")
	 public void Studys(HttpServletRequest request){
		 String classroomId = request.getParameter("classroomId");  //获取列表ID,根据列表ID来判断进行不同的方法
		 BaseBean base=new BaseBean();
		 List<BaseBean>list=null;
		 Study  s=new Study();
		 if(classroomId.equals("1")){ //当列表ID等于1的时候 进入消分学习取题 
			 s.setInterfaceId("exam001");
			 s.setMobilephone("17708404197");
			 s.setIdentityCard("431022199612250036");
			 s.setIpAddress("123.56.180.216");
			 s.setUserSource("C");
			 list= iMicroclassServer.xfStudyAnswer(s);	 
		 }else if(classroomId.equals("2")){        //当列表ID等于2的时候进入学习非机动车学习取题
				
		 }else if(classroomId.equals("3")){
			  
		 }else if(classroomId.equals("4")){
			 	s.setInterfaceId("DDC2001"); //电动车违法取题编号
				s.setSubjectId("6910099");
				s.setIdentityCard("431022199612250036");
				s.setMobilephone("17708404197");
				s.setServiceType("WF");
				s.setIpAddress("123.56.180.216");
				s.setUserSource("C");
				s.setSubjectAnswer("A");
				list=iMicroclassServer.ddcStudyAnswer(s);
		 }else if(classroomId.equals("5")){
			    s.setInterfaceId("DDC3001");
				s.setIdentityCard("431022199612250036");
				s.setMobilephone("17708404197");
				s.setServiceType("AQ");
				s.setIpAddress("123.56.180.216");
				s.setUserSource("C");
			    list=iMicroclassServer.xrStudyAnswer(s);
		 }
			for(BaseBean b:list){
				base.setCode(b.getCode());
				base.setMsg(b.getMsg());
				base.setData(b.getData());
			}
			renderJSON(base);
	 }
	 
	 
	 /**
	  * 微课所有答题方法
	  * @param request
	  */
	 @RequestMapping("Classroom/Answers.html")
	 public void  Answer(HttpServletRequest request){
		 String classroomId = request.getParameter("classroomId");  //获取列表ID,根据列表ID来判断进行不同的方法
		 String subjectId =request.getParameter("subjectId");
		 BaseBean base=new BaseBean();
		 List<BaseBean>list=null;
		 Study  s=new Study();
		 if(classroomId.equals("1")){ //当列表ID等于1的时候 进入消分学习答题
			 	s.setInterfaceId("exam002");
				s.setSubjectId(subjectId);  //取题ID
				s.setUserName("曾令成");		
				s.setIdentityCard("431022199612250036"); //身份证号码
				s.setMobilephone("17708404197"); //手机号码
				s.setIpAddress("123.56.180.216"); //答题IP地址
				s.setSubjectAnswer("A"); //答题答案 
				s.setAnswerDateTime("2017-04-17 10:59:49");
				s.setScoreStartDate("2016-07-09"); //取题时的计分周期始
				s.setScoreEndDate("2017-07-09"); //取题时的计分周期末
				s.setUserSource("C");
				list=iMicroclassServer.xfAnswerQuey(s); 
		 }else if(classroomId.equals("2")){ 
			 
		 }else if(classroomId.equals("3")){ 
			 	
			 
		 }else if(classroomId.equals("4")){ 
			 s.setInterfaceId("DDC2002");
				s.setSubjectId(subjectId);
				s.setIdentityCard("431022199612250036");
				s.setMobilephone("17708404197");
				s.setServiceType("WF");
				s.setIpAddress("123.56.180.216");
				s.setUserSource("C");
				s.setSubjectAnswer("A");
				s.setDecisionId("12345");
				list=iMicroclassServer.ddcAnswerQuey(s);
		 }else if(classroomId.equals("5")){ 
			 	s.setInterfaceId("DDC3002");
				s.setSubjectId(subjectId);
				s.setIdentityCard("431022199612250036");
				s.setMobilephone("17708404197");
				s.setServiceType("AQ");
				s.setIpAddress("123.56.180.216");
				s.setUserSource("C");
				s.setSubjectAnswer("A");
				list=iMicroclassServer.xrAnswerQuey(s);	
		 }
		 for(BaseBean b:list){
				base.setCode(b.getCode());
				base.setMsg(b.getMsg());
				base.setData(b.getData());	
			}
			renderJSON(base);
	 }
	 

	public IMicroclassServer getiMicroclassServer() {
		return iMicroclassServer;
	}
	public void setiMicroclassServer(IMicroclassServer iMicroclassServer) {
		this.iMicroclassServer = iMicroclassServer;
	}

	 
	 
	 
	 
	
}
