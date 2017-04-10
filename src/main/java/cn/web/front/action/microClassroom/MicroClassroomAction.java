package cn.web.front.action.microClassroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.studyclassroom.Answeroptions;
import cn.account.bean.studyclassroom.Study;
import cn.account.bean.studyclassroom.StudyRecord;
import cn.sdk.bean.BaseBean;
import cn.web.front.support.BaseAction;



/**
 * 微课堂
 * 创建时间:2017-4-10
 * 创建人:曾令成
 *
 */
@Controller
public class MicroClassroomAction extends BaseAction {
	
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
		 study.setIntegral(2);
		 record.setAnsLogarithm(5);  //答题对数
		 record.setAnswerDate("2017-08-01"); //答题时间
		 record.setIsComplete(1);//是否完成
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
			 study.setSubjectId(1);
			 study.setSubjectName("这个标志是什么意思?");
			 study.setSubjecttype(1);
			 study.setTestQuestionsType(2);
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
		 study.setIntegral(5);
		 list.add(study);
		 base.setMsg("答题结束");
		 base.setCode("0000");
		 base.setData(list);
		renderJSON(base);
	 }

	 
	
}
