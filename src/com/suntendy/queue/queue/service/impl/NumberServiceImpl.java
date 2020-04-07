package com.suntendy.queue.queue.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.suntendy.queue.business.dao.IBusinessDao;
import com.suntendy.queue.business.domain.Business;
import com.suntendy.queue.dept.dao.DeptDao;
import com.suntendy.queue.dept.domain.Dept;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.service.IEmployeeService;
import com.suntendy.queue.led.domain.LED;
import com.suntendy.queue.led.service.LedService;
import com.suntendy.queue.lzxx.action.LzckAction;
import com.suntendy.queue.queue.action.CallOutStatusAction;
import com.suntendy.queue.queue.dao.INumberDao;
import com.suntendy.queue.queue.dao.IQueueDao;
import com.suntendy.queue.queue.dao.ISaveSfzPhotoDao;
import com.suntendy.queue.queue.dao.IZhpcallnumDao;
import com.suntendy.queue.queue.domain.Code;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.NumberIdPhoto;
import com.suntendy.queue.queue.domain.PrintInfo;
import com.suntendy.queue.queue.domain.Wclh;
import com.suntendy.queue.queue.domain.Zhpcallnum;
import com.suntendy.queue.queue.service.ICodeService;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.queue.util.cache.NumberManager;
import com.suntendy.queue.queue.util.cache.VoiceManager;
import com.suntendy.queue.util.Base64Util;
import com.suntendy.queue.util.Bitmap;
import com.suntendy.queue.util.DateUtils;
import com.suntendy.queue.util.PjTimerTask;
import com.suntendy.queue.util.screenGDContetTimerTask;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.TrffException;
import com.suntendy.queue.util.exception.UpdateException;
import com.suntendy.queue.util.led.LEDUtils;
import com.suntendy.queue.util.scriptsession.Publisher;
import com.suntendy.queue.util.scriptsession.event.ChuangKouPing;
import com.suntendy.queue.util.scriptsession.event.DualScreenEvent;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventFail;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventWP;
import com.suntendy.queue.util.scriptsession.event.EvaluateEvent;
import com.suntendy.queue.util.scriptsession.event.GuaQiTuiSong;
import com.suntendy.queue.util.scriptsession.event.GuoHaoTuiSong;
import com.suntendy.queue.util.scriptsession.event.ModifyWindowCountEvent;
import com.suntendy.queue.util.scriptsession.event.PassEvent;
import com.suntendy.queue.util.scriptsession.event.PauseOrRecoverEvent;
import com.suntendy.queue.util.scriptsession.event.PauseOrRecoverEventZS;
import com.suntendy.queue.util.scriptsession.event.SendStatusCY;
import com.suntendy.queue.util.scriptsession.event.ShowCallInfoEvent;
import com.suntendy.queue.util.scriptsession.event.ShowYWDDRSEvent;
import com.suntendy.queue.util.trff.TrffUtils;
import com.suntendy.queue.util.trff.XMLUtils;
import com.suntendy.queue.window.dao.ISetWindowDao;
import com.suntendy.queue.window.domain.Screen;

/*******************************************************************************
 * 描述: 取号、叫号相关操作业务逻辑接口实现类 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-09-24 17:33:32 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class NumberServiceImpl implements INumberService {
	private Publisher publisher;
	private INumberDao numberDao;
	private ICodeService codeService;
	private IQueueDao queueDao;
	private ISetWindowDao setWindowDao;
	private LedService ledService;
	private static IBusinessDao businessDao;
	private ISaveSfzPhotoDao saveSfzPhotoDao;
	private IZhpcallnumDao	zhpcallnumDao;
	private DeptDao deptDao;
	private String Pjflag;
	private IEmployeeService iEmployeeService;
	
	
	public void setiEmployeeService(IEmployeeService iEmployeeService) {
		this.iEmployeeService = iEmployeeService;
	}
	
	public IEmployeeService getiEmployeeService() {
		return iEmployeeService;
	}

	public void setDeptDao(DeptDao deptDao) {
		this.deptDao = deptDao;
	}
	
	public IBusinessDao getBusinessDao() {
		return businessDao;
	}

	public void setBusinessDao(IBusinessDao businessDao) {
		this.businessDao = businessDao;
	}

	public LedService getLedService() {
		return ledService;
	}

	public void setLedService(LedService ledService) {
		this.ledService = ledService;
	}

	public void setSetWindowDao(ISetWindowDao setWindowDao) {
		this.setWindowDao = setWindowDao;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public void setNumberDao(INumberDao numberDao) {
		this.numberDao = numberDao;
	}
	public ICodeService getCodeService() {
		return codeService;
	}

	public void setCodeService(ICodeService codeService) {
		this.codeService = codeService;
	}
	
	public ISaveSfzPhotoDao getSaveSfzPhotoDao() {
		return saveSfzPhotoDao;
	}

	public void setSaveSfzPhotoDao(ISaveSfzPhotoDao saveSfzPhotoDao) {
		this.saveSfzPhotoDao = saveSfzPhotoDao;
	}
	
	public IZhpcallnumDao getZhpcallnumDao() {
		return zhpcallnumDao;
	}

	public void setZhpcallnumDao(IZhpcallnumDao zhpcallnumDao) {
		this.zhpcallnumDao = zhpcallnumDao;
	}

	public List<Number> searchCurrentDayNumber(Number number) {
		try {
			return numberDao.searchCurrentDayNumber(number);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Number>();
	}
	
	public List<Number> queryNumberBypdh(Number number) {
		try {
			return numberDao.queryNumberBypdh(number);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Number>();
	}

	@Override
	public String getTotalNumberOfBusinessNumber(String loginIP) throws Exception {
		String strTotal = "未办理";
		CacheManager cacheManager = CacheManager.getInstance();
		Screen screen = cacheManager.getWindow(loginIP);
		if (null != screen) { //绑定窗口
			String businessType = screen.getBusinessid();
			if (StringUtils.isNotEmpty(businessType)) { //有可办理的业务
				String serialNumType = CacheManager.getInstance().getSystemConfig("serialNumType");
				List<Number> datas = numberDao.getTotalNumberOfBusinessNumber(businessType);
				if (null != datas && !datas.isEmpty()) {
					if ("1".equals(serialNumType)) {
						strTotal = StringUtils.trimToEmpty(datas.get(0).getStatus());
						strTotal = "【<b style='font-size:15px'>" + strTotal + "</b>】";
					} else {
						strTotal += "：";
						for (Number number : datas) {
							String temp = StringUtils.trimToEmpty(number.getStatus());
							strTotal += number.getTypeName() + "【<b style='font-size:15px'>" + temp + "</b>】";
						}
					}
				}
			}
		}
		
		return strTotal;
	}

	public PrintInfo getNewNumber(Number number) throws Exception {
		PrintInfo info = new PrintInfo();
		
		//判断能不能取号
		String[] result = null;
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");//是否启用接口
		String jklx = cacheManager.getSystemConfig("jklx");
		String idnumber6he1 = "";
		if (!"".equals(number.getIDNumberB()) && number.getIDNumberB()!= null) {
			idnumber6he1 = number.getIDNumberB();
		}else{
			idnumber6he1 = number.getIDNumber();
		}
		if ("0".equals(isUseInterface) && "0".equals(jklx)) {
			System.out.println("调用判断可否取号接口开始="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
			String jym = number.getFlag() + deptCode.substring(0, 6) + "#"
					+ idnumber6he1 + "#" + number.getServerIp() + "#"
					+ number.getPersonType() + "#" + number.getBusinessCount() + "#"
					+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			result = TrffUtils.isFetchNumber(number.getFlag(), idnumber6he1,
				number.getServerIp(), number.getPersonType(), number.getBusinessCount(),
				codeService.jm(jym));
			System.out.println("判断可否取号信息结果：[" + idnumber6he1 + "][" + result[1] + "]");
			System.out.println("调用判断可否取号接口结束="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
		} else {
			result = new String[] {"1", ""};
		}
		
		if ("1".equals(result[0]) && null != result[1]) { //可取
			NumberManager numberManager = NumberManager.getInstance();
			String serialNum = numberManager.generateNumber(number.getBusinessPrefix(), number.getQueueCode());
			String queueValue = serialNum.substring(serialNum.length() - 4);
			//保存取号信息到数据库
			number.setStatus("1");//未叫
			number.setDeptCode(deptCode);
			number.setDeptHall(deptHall);
			number.setSerialNum(serialNum);
			String pattern = "yyyy-MM-dd HH:mm:ss SSS";
			number.setEnterTime(DateUtils.dateToString(pattern));
			number.setQueueValue(String.valueOf(Integer.parseInt(queueValue)));
			
			if ("0".equals(isUseInterface) && "0".equals(jklx)) {
				System.out.println("调用取号接口开始="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
				//上传取号信息
				String jym = serialNum + deptCode.substring(0, 6) + "#"
						+ number.getFlag() + "#" + number.getIDType() + "#"
						+ idnumber6he1 + "#" + number.getServerIp() + "#"
						+ number.getPersonType() + "#" + number.getBusinessCount() + "#"
						+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
				String[] result1 = TrffUtils.saveFetchNumberInfo(serialNum, number.getFlag(), number.getIDType(),
						idnumber6he1, number.getServerIp(), number.getPersonType(), number.getBusinessCount(),
					codeService.jm(jym));
				System.out.println("上传取号信息结果：[" + serialNum + "][" + result[1] + "]");
				System.out.println("上传六合一返回结果result1[0]="+result1[0]);
				System.out.println("调用取号接口结束="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
				if ("2".equals(result1[0])) {
					throw new TrffException(result1[1]);
				} else if (!"1".equals(result1[0]) && !"2".equals(result1[0])) {
					throw new Exception(result1[1]);
				}
			}
			
			number.setId(this.numberDao.saveNumber(number));
			//添加日志
			numberDao.insertValuerecordRZ(number);
			//配置打印信息
			String regex = deptCode + deptHall;
			number.setSerialNum(serialNum.replaceAll(regex, ""));
			List<Number> list1 = numberDao.countWJHshuliang(number.getId(),"","","",deptCode,deptHall);
			String waitingNum = cacheManager.getSystemConfig("waitingNum");
			int count = 0;
			if ("0".equals(waitingNum)) {
				//大厅总人数
				//List<Number> listCount = numberDao.countWJHshuliang("", "", "","",deptCode,deptHall);
				//count = listCount.size()-1;
				List<Number> listCount = numberDao.showWaitRs(number);
				int AllSum=0;
				if(null != listCount && listCount.size()>0){
					for (Number nu : listCount) {
						if(null != nu.getTypeCount()){
							AllSum += Integer.parseInt(nu.getTypeCount());
						}
					}
				}
				count=AllSum;
			} else if("1".equals(waitingNum)){
				//等待区域--大厅人数
				List<Number> listCount = numberDao.countWJHshuliang("", list1.get(0).getWaitingarea(),"","",deptCode,deptHall);
				count = listCount.size()-1;
			}else if ("2".equals(waitingNum)) {
				//业务类型--大厅人数
				if (list1.size()>0) {
					List<Number> listCount = numberDao.countWJHshuliang("", "",list1.get(0).getBusinessType(),"",deptCode,deptHall);
					count  = listCount.size()-1;					
				}else {
					System.out.println("请检查应用服务器和数据服务器时间");
					count = 0;
				}
			}else if ("3".equals(waitingNum)) {
				//队列类型--大厅人数
				if (list1.size()>0) {
					List<Number> listCount = numberDao.countWJHshuliang("", "","",list1.get(0).getQueueCode(),deptCode,deptHall);
					count  = listCount.size()-1;					
				}else {
					System.out.println("请检查应用服务器和数据服务器时间");
					count = 0;
				}
			}else {
				List<Number> listCount = numberDao.countWJHshuliang("", "", "","",deptCode,deptHall);
				count = listCount.size()-1;
			}
			int numCount = count;
			numberManager.saveNewNumber(number);
			Business business = new Business();
			business.setId(number.getBusinessType());
			List<Business> listB = businessDao.queryWaitingareaByID(business);
			info.setQueueName(number.getTypeName());
			info.setSerialNum(number.getSerialNum());
			info.setPeoNum(numCount);
			info.setSuccess(true);
			info.setId(number.getId());
			info.setIDNumber(idnumber6he1);//打印条身份证从后台读取
			String waitingarea = listB.get(0).getWaitingarea();
			if ("".equals(waitingarea) || null==waitingarea) {
				waitingarea = "等待区域";
			}
			info.setWaitingarea(waitingarea);//打印条等待区域后台读取
			
			publisher.publish(new ModifyWindowCountEvent(number.getBusinessType() + "@+"));
			String ddrs =  businessDao.queryYWNoCallNumber(number.getBusinessType());
			String dlddrs = businessDao.queryDLNoCallNumber(number.getQueueCode());
			try {
//				publisher.publish(new ShowYWDDRSEvent(number.getBusinessType()+"@"+ddrs));
				publisher.publish(new ShowYWDDRSEvent(number.getBusinessType()+"@"+ddrs+"@"+number.getQueueCode()+"@"+dlddrs));
			} catch (Exception e) {
				
			}
			
			
			//查询等待人数
			Number countnum = new Number();
			countnum.setDeptCode(deptCode);
			countnum.setDeptHall(deptHall);
			List<Number> numbersCountList = numberDao.showWaitRs(countnum);
			String nameAndShul="";
			int AllSum=0;
			if(null != numbersCountList && numbersCountList.size()>0){
				for (Number nu : numbersCountList) {
					if(null != nu.getTypeCount()){
						AllSum += Integer.parseInt(nu.getTypeCount());
					}
					nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
				}
			}
			info.setCountAll(String.valueOf(AllSum));
//			publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		} else if ("2".equals(result[0])) { //不可取
			throw new TrffException(result[1]);
		}else if(null==result[1]){
			info.setSuccess(false);
			info.setMsg("公安网网络异常，请稍后再试！");
		} else {
			throw new Exception(result[1]);
		}
		if ("1".equals(jklx) && "0".equals(isUseInterface)) {
			String resultbl = "";
			List<Number> listblxx = numberDao.queryBLXX(deptCode, deptHall);
			if (listblxx.size()>0) {
				for (int i = 0; i < listblxx.size(); i++) {
					resultbl = this.callNumber_new(listblxx.get(i).getCode(), listblxx.get(i).getBarIp(),"1");
					String[] results = resultbl.split("@");	
					if ("2".equals(results[0])) {
						System.out.println("补录信息成功,ip="+listblxx.get(i).getBarIp()+"员工编号="+listblxx.get(i).getCode());
						numberDao.deleteBLXX(listblxx.get(i));
						break;
					}else {
						System.out.println("补录信息失败ip"+listblxx.get(i).getBarIp()+"员工编号="+listblxx.get(i).getCode()+"~~原因="+results[0]);
					}
				}
			}
		}
		return info;
	}
	
	static String sendContent ="";//不分区域
	static String sendContent1 ="";//东大厅
	static String sendContent2 ="";//西大厅
	static int sendstatus0 =0;//查询发屏小车状态
	static int sendstatus1 =0;//查询发屏大车状态
	public synchronized String callNumber(String operNum, String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String sfkqcyyw = cacheManager.getSystemConfig("sfkqcyyw");
		
		//判断是否同一IP登录
		if (!cacheManager.isSameIP(operNum, loginIP)) {
			return "此账号已在其它窗口登录，<br/>无法叫号";
		}
		String ucr = cacheManager.userCompare(operNum, loginIP, "请稍候再试");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		if (cacheManager.isPause(loginIP)) {
			return "暂停受理";
		}
		//判断上一个号是否已办完
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if (null != searchNumber) {
			System.out.println("叫号时上个号码状态="+searchNumber.getStatus());
			if ("4".equals(searchNumber.getStatus())||"5".equals(searchNumber.getStatus())) {
				return "正在评价，请耐心等待";
			} else {
				return "上一个号未办完，不能叫号";
			}
		}
		//根据IP获取窗口，判断IP有没有绑定窗口
		Screen screen = cacheManager.getWindow(loginIP);
		if (null == screen) {
			return "此IP未与窗口绑定，无法叫号";
		}
		//获取绑定窗口可办理的业务
		if (StringUtils.isEmpty(screen.getBusinessid())) {
			return "此窗口未指定可办理的业务，无法叫号";
		}
		
		Number number = new Number();
		Business bin=new Business();
		Number numberLz = new Number();
		numberLz.setBarId(screen.getWindowId());
		numberLz.setDeptCode(deptCode);
		numberLz.setDeptHall(deptHall);
		
		List<Number> list=numberDao.getValueRecardLz(numberLz);
		if(null != list && list.size()>0){
			Number queueNumber = list.get(0);
			Number numVo = new Number();
			numVo.setDeptCode(deptCode);
			numVo.setDeptHall(deptHall);
			numVo.setOperNum("");
			numVo.setId(queueNumber.getId());
			numVo.setCode("");
			List<Number> listAll=numberDao.searchCurrentDayNumber(numVo);
			Number queueAll = listAll.get(0);
			number = queueAll;
			number.setSerialNum(number.getSerialNum().substring(13,number.getSerialNum().length()));
			number.setTypeName(number.getTypeName());
			number.setStatus("2");
		}else{
			number = fetchNumber(screen);
			//判断有无可叫的号
			if (null == number) {
				return "没有可呼叫的号";
			}
		}
		//推送查验信息
		if(number.getTypeName()!= null){
			if("0".equals(sfkqcyyw) && number.getTypeName().indexOf("查验")>=0){
				
				Number number2=numberDao.queryCarNumber(deptCode+deptHall+number.getSerialNum(), deptCode, deptHall);
				if(number2.getCarNumber()!=null && number2.getCarNumber()!="undefined"){
				if(number2.getCarType().equals("0")){
					sendstatus0++;
					}else if (number2.getCarType().equals("1")) {
						sendstatus1++;
					}
				System.out.println("小车状态:"+sendstatus0+"____________________"+"大车状态:"+sendstatus1+"______________"+"顺序号:"+number.getSerialNum());
				publisher.publish(new SendStatusCY(number2.getCarType()+ "@"+number2.getCarNumber()+"@"+sendstatus0+"@"+sendstatus1+"@"+number.getSerialNum())); //推送梧州查验信息
				}
			}
		}
		//更此号新数据库状态
		System.out.println("赋值前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
		number.setOperNum(operNum);
		number.setBarId(screen.getWindowId());
		number.setBeginTime(DateUtils.dateToString());
		numberDao.delValueRecardLz(number);
		


		
		

		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) && "0".equals(screen.getOpenInterFace()) && "0".equals(cacheManager.getSystemConfig("jklx"))) {
			//上传呼叫信息
			String serialNum = deptCode + deptHall + number.getSerialNum();
			String jym = serialNum + deptCode.substring(0, 6) + "#" + operNum + "#01#"
				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveCallInfo(serialNum, operNum, "01", codeService.jm(jym));
			System.out.println("上传呼叫信息结果：[" + operNum + "][" + number.getSerialNum() + "][" + result[1] + "]");
			System.out.println("上传呼叫信息六合一返回结果result1[0]="+result[0]);
			if ("2".equals(result[0])) {
				number.setStatus("1");
				throw new TrffException(result[1]);
			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
				number.setStatus("1");
				throw new Exception(result[1]);
			}
		}
		System.out.println("叫号时当前号码状态="+number.getStatus());
		System.out.println("第一次更新前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
		numberDao.updateNumber(number);
		numberManager.saveCallNumber(operNum, number);
		
		String tempContent = screen.getContent2();
		if ("150300000400".equals(deptCode)||"510900000400".equals(deptCode)) {//
			if (number.getBarId().length() ==1) {
				screen.setContent2("(0"+number.getBarId()+") "+number.getSerialNum());
			}else {
				screen.setContent2("("+number.getBarId()+") "+number.getSerialNum());
			}
		}else {
			screen.setContent2(number.getSerialNum() + "号");
		}
		
		
//		String tempContent = "";
//		if ("2".equals(screen.getShowNum())) {
//			tempContent = screen.getContent2();
//			screen.setContent2(number.getSerialNum() + "号");
//		}else if ("3".equals(screen.getShowNum())) {
//			tempContent = screen.getContent3();
//			screen.setContent3(number.getSerialNum() + "号");
//		}else if ("4".equals(screen.getShowNum())) {
//			tempContent = screen.getContent4();
//			screen.setContent4(number.getSerialNum() + "号");
//		}
		String pattern = "yyyy-MM-dd HH:mm:ss SSS";
		String orderDate = DateUtils.dateToString(pattern);
		String strVoice = number.getSerialNum()+"@"+screen.getWindowId()+"@"+orderDate;
		if (StringUtils.isNotEmpty(screen.getMenuAddress())) {
			strVoice += "@" + screen.getMenuAddress();
		}
		
		VoiceManager.getInstance().add(strVoice);
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
		
		LEDUtils.sendText(screen, publisher, number, true);
		//电视屏发屏
		if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
			String ckip = screen.getCkip();
			if ("".equals(ckip) || null == ckip) {
				ckip = "127.0.0.1";
			}
			String ckid = screen.getWindowId();
			if (screen.getWindowId().length() == 1) {
				ckid = "0"+screen.getWindowId();
			}
			String ckmc = screen.getContent();
			String sxh = screen.getContent2();
			if (sxh.length()>5) {
				sxh = sxh.substring(0,5);
			}
			publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
		}
		if (!"".equals(screen.getAddress()) && screen.getAddress() != null) {
			System.out.println("窗口LED屏推送信息----窗口屏地址="+screen.getAddress()+"---com口="+screen.getComnum()+"---内容="+screen.getContent()+"----"+screen.getContent2());
		}
		String ywl = numberDao.selectYWLbyWindowId(screen.getWindowId(),deptCode,deptHall); //根据窗口号查询  返回窗口对应的业务办理量
		String windowCode = screen.getWindowId() + StringUtils.trimToEmpty(screen.getMenuAddress());
		String queue = number.getQueueCode();
		System.out.println("推送综合屏页面信息");
		publisher.publish(new ShowCallInfoEvent(number.getSerialNum() + "@" + windowCode + "@" + number.getBusinessType()+"@"+ ywl)); //推送叫号信息到综合评

		//判断是否发送窗口滚动(如： xxxx号正在办理中 )
		String isopenScreen = cacheManager.getSystemConfig("isopenScreen");
		if("0".equals(isopenScreen)){
			screenGDContetTimerTask sendGunDongTimerTask = new screenGDContetTimerTask(screen,operNum);
		}
		
		if("0".equals(cacheManager.getSystemConfig("isOpenQueueTV"))){//判断是否开启LED队列发屏
			System.out.println("进入队列发屏");
			//临沂发屏
			if ("371300000401".equals(deptCode)) {
					System.out.println("临沂大屏已经删除，2017-7-20");
					}else if ("510106000400".equals(deptCode)) {//成都六分所违法队列屏
						System.out.println("成都六分所违法队列屏"+number.getFlag());
						Number numberEJ= new Number();
						numberEJ.setId(number.getId());
						List<Number> listEJ = numberDao.getErJiCaiDanBianHao(numberEJ);
						if (listEJ.size()>0) {
							System.out.println("二级菜单编号为=="+listEJ.get(0).getEjcdbh());
						}
						if (listEJ.size()>0 &&"04".equals(listEJ.get(0).getEjcdbh())) {
							//叫号队列屏发屏
							LED led = new LED();
							led.setDeptCode(deptCode);
							led.setDeptHall(deptHall);
							String sendContenttemp="";
							led.setFlag("2");
							//获取LED屏信息
							List<LED> led_TVlist = ledService.getLedInfo_TV(led);
							for(int i=0;i<led_TVlist.size();i++){
								sendContenttemp = "";
								sendContenttemp = sendContent;
								
								String ledQueueShow = cacheManager.getSystemConfig("ledQueueShow");//获取发屏方式（纵向和横向）
								String content = led_TVlist.get(i).getContent();
								System.out.println("模板="+content);
								//个位数窗口屏补位
								//String windowCode = screen.getWindowId() + StringUtils.trimToEmpty(screen.getMenuAddress());
								if(10>Integer.parseInt(screen.getAddress())){
									screen.setAddress("0"+Integer.parseInt(screen.getAddress()));
								}
								if (windowCode.length()==1) {
									windowCode = "0"+windowCode;
								}
								//空格数量转换空格
								String kongge = "";
								if(null!=led_TVlist.get(i).getSpace() && !"".equals(led_TVlist.get(i).getSpace())&& !"0".equals(led_TVlist.get(i).getSpace())){
									for(int x=0;x<Integer.parseInt(led_TVlist.get(i).getSpace());x++){
										kongge += " ";
									}
								}
								//模板替换
								content = content.replaceAll("@", number.getSerialNum()).replaceAll("#", windowCode)+kongge;
								System.out.println("模板(替换后)="+content);
//						sendContent += content;
								sendContenttemp += content;
								
								
								//if(null != led_TVlist.get(i).getWaitingArea() && null != businessList.get(0).getWaitingarea()){
								//System.out.println("businessList.get(0).getWaitingarea()="+businessList.get(0).getWaitingarea());
//												sendContenttemp = sendContent;
								//System.out.println("sendContenttemp="+sendContenttemp);
								//字符长度
								int contentLength =0;
								int len = sendContenttemp.replaceAll("[^\\x00-\\xff]", "**").length();
								if(null != led_TVlist.get(i).getContent()){
									contentLength = Integer.parseInt(led_TVlist.get(i).getWidth())*Integer.parseInt(led_TVlist.get(i).getHeight())/((Integer.parseInt(led_TVlist.get(i).getLattice())/2)*(Integer.parseInt(led_TVlist.get(i).getLattice())/2))/2;
//													contentLength = (led_TVlist.get(i).getContent()+kongge).replaceAll("[^\\x00-\\xff]", "**").length() * Integer.parseInt(led_TVlist.get(i).getLattice()) /2+Integer.parseInt(led_TVlist.get(i).getLattice())/2;
								}
								//行数(高度/点阵数)
								int hangshu = Integer.parseInt(led_TVlist.get(i).getHeight())/Integer.parseInt(led_TVlist.get(i).getLattice());
								//列数(屏宽/字符长度)
//												int lieshu = (int) Math.floor(Integer.parseInt(led_TVlist.get(i).getWidth())/contentLength);
								int lieshu = Integer.parseInt(led_TVlist.get(i).getWidth())*2/Integer.parseInt(led_TVlist.get(i).getLattice())/(content).replaceAll("[^\\x00-\\xff]", "**").length();
								System.out.println("当前字符长度="+len);
								System.out.println("列数="+lieshu);
								System.out.println("行数="+hangshu);
								System.out.println("宽="+Integer.parseInt(led_TVlist.get(i).getWidth())+"长="+Integer.parseInt(led_TVlist.get(i).getHeight())+""+"总长度="+contentLength);
								System.out.println("未替换当前字符长度="+sendContenttemp.length());
								System.out.println("未替换总字符长度="+((content).length()*hangshu*lieshu));
								
//												if("0".equals(ledQueueShow)){
//													sendContenttemp+=kongge+" @";
//												}else{
//													sendContenttemp+=kongge+" ";
//												}
								//System.out.println("sendContenttemp="+sendContenttemp);
								
								if(len  >  contentLength){
									System.out.println("开始字符="+(sendContenttemp.length()-(content).length()*hangshu*lieshu));
									System.out.println("结束字符="+sendContenttemp.length());
									sendContenttemp = sendContenttemp.substring(sendContenttemp.length()-(content).length()*hangshu*lieshu,sendContenttemp.length());
//													sendContenttemp = sendContenttemp.substring(len-contentLength,len);
								}
								
								if("".equals(sendContenttemp)||null==sendContenttemp){
									LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow," ");
//													System.out.println(sendContent);
								}else{
									LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow,sendContenttemp);
									System.out.println(led_TVlist.get(i).getAddress()+"地址队列屏结果="+sendContenttemp);
								}
								Thread.sleep(100);					
							}
							sendContent = sendContenttemp ;
//						System.out.println(sendContent);
						}
					}else{
						
						//叫号队列屏发屏
						LED led = new LED();
						led.setDeptCode(deptCode);
						led.setDeptHall(deptHall);
						String sendContenttemp="";
						led.setFlag("2");
						//获取LED屏信息
						List<LED> led_TVlist = ledService.getLedInfo_TV(led);
						Business b = new Business();
						String No = new String();
						No = deptCode + deptHall + number.getSerialNum();
						List<Business> listW = businessDao.queryWaitingareaByNo(No);
						for(int i=0;i<led_TVlist.size();i++){
								if (led_TVlist.get(i).getWaitingArea().equals(listW.get(0).getWaitingarea())) {
									
							sendContenttemp = "";
							sendContenttemp = sendContent;
						
						String ledQueueShow = cacheManager.getSystemConfig("ledQueueShow");//获取发屏方式（纵向和横向）
						String content = led_TVlist.get(i).getContent();
						System.out.println("模板="+content);
						//个位数窗口屏补位
						//String windowCode = screen.getWindowId() + StringUtils.trimToEmpty(screen.getMenuAddress());
						if(10>Integer.parseInt(screen.getAddress())){
							screen.setAddress("0"+Integer.parseInt(screen.getAddress()));
						}
						if (windowCode.length()==1) {
							windowCode = "0"+windowCode;
						}
						//空格数量转换空格
						String kongge = "";
						if(null!=led_TVlist.get(i).getSpace() && !"".equals(led_TVlist.get(i).getSpace())&& !"0".equals(led_TVlist.get(i).getSpace())){
							for(int x=0;x<Integer.parseInt(led_TVlist.get(i).getSpace());x++){
								kongge += " ";
							}
						}
						//模板替换
						content = content.replaceAll("@", number.getSerialNum()).replaceAll("#", windowCode)+kongge;
						System.out.println("模板(替换后)="+content);
//						sendContent += content;
						sendContenttemp += content;
						
						
								//if(null != led_TVlist.get(i).getWaitingArea() && null != businessList.get(0).getWaitingarea()){
									//System.out.println("businessList.get(0).getWaitingarea()="+businessList.get(0).getWaitingarea());
//												sendContenttemp = sendContent;
												//System.out.println("sendContenttemp="+sendContenttemp);
												//字符长度
												int contentLength =0;
												int len = sendContenttemp.replaceAll("[^\\x00-\\xff]", "**").length();
												if(null != led_TVlist.get(i).getContent()){
													contentLength = Integer.parseInt(led_TVlist.get(i).getWidth())*Integer.parseInt(led_TVlist.get(i).getHeight())/((Integer.parseInt(led_TVlist.get(i).getLattice())/2)*(Integer.parseInt(led_TVlist.get(i).getLattice())/2))/2;
//													contentLength = (led_TVlist.get(i).getContent()+kongge).replaceAll("[^\\x00-\\xff]", "**").length() * Integer.parseInt(led_TVlist.get(i).getLattice()) /2+Integer.parseInt(led_TVlist.get(i).getLattice())/2;
												}
												//行数(高度/点阵数)
												int hangshu = Integer.parseInt(led_TVlist.get(i).getHeight())/Integer.parseInt(led_TVlist.get(i).getLattice());
												//列数(屏宽/字符长度)
//												int lieshu = (int) Math.floor(Integer.parseInt(led_TVlist.get(i).getWidth())/contentLength);
												int lieshu = Integer.parseInt(led_TVlist.get(i).getWidth())*2/Integer.parseInt(led_TVlist.get(i).getLattice())/(content).replaceAll("[^\\x00-\\xff]", "**").length();
												System.out.println("当前字符长度="+len);
												System.out.println("列数="+lieshu);
												System.out.println("行数="+hangshu);
												System.out.println("宽="+Integer.parseInt(led_TVlist.get(i).getWidth())+"长="+Integer.parseInt(led_TVlist.get(i).getHeight())+""+"总长度="+contentLength);
												System.out.println("未替换当前字符长度="+sendContenttemp.length());
												System.out.println("未替换总字符长度="+((content).length()*hangshu*lieshu));

//												if("0".equals(ledQueueShow)){
//													sendContenttemp+=kongge+" @";
//												}else{
//													sendContenttemp+=kongge+" ";
//												}
												//System.out.println("sendContenttemp="+sendContenttemp);
												
												if(len  >  contentLength){
													System.out.println("开始字符="+(sendContenttemp.length()-(content).length()*hangshu*lieshu));
													System.out.println("结束字符="+sendContenttemp.length());
													sendContenttemp = sendContenttemp.substring(sendContenttemp.length()-(content).length()*hangshu*lieshu,sendContenttemp.length());
//													sendContenttemp = sendContenttemp.substring(len-contentLength,len);
												}
												
												if("".equals(sendContenttemp)||null==sendContenttemp){
													LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow," ");
//													System.out.println(sendContent);
												}else{
													LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow,sendContenttemp);
													System.out.println(led_TVlist.get(i).getAddress()+"地址队列屏结果="+sendContenttemp);
												}
									Thread.sleep(100);
									}
						        }
						sendContent = sendContenttemp ;
//						System.out.println(sendContent);
					}
				}
		screen.setContent2(tempContent);
		
		//添加日志
		String jdctype="";
		String sfzmhm="";
		String jdcnum="";
		String binss=number.getTypeName();
		Number num = new Number();
		num.setId(number.getId());
		if (numberDao.getValueRecordAllById(num).size()>0) {
			Number numberVO = numberDao.getValueRecordAllById(num).get(0);
			numberVO.setStatus(number.getStatus());
			numberVO.setSerialNum(deptCode+deptHall+number.getSerialNum());
			numberVO.setOperNum(number.getOperNum());
			numberVO.setBarId(number.getBarId());
			numberVO.setBeginTime(number.getBeginTime());
			numberVO.setDeptCode(deptCode);
			numberVO.setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(numberVO);
			jdctype=numberVO.getJdctypes();
			jdcnum=numberVO.getJdcnum();
			sfzmhm=numberVO.getIDNumber();
			bin.setId(numberVO.getBusinessType());
			//成都特有
			Number nameNum = new Number();
			System.out.println("第二次赋值前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
			nameNum = number;
			System.out.println("第二次赋值后"+nameNum.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
			System.out.println("第二次更新前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
			numberDao.updateNumber(number);
			nameNum = null;
			//添加综合屏叫号信息
			Zhpcallnum callnum = new Zhpcallnum();
			callnum.setId(numberVO.getId());
			callnum.setBarId(numberVO.getBarId());
			callnum.setClientno(numberVO.getSerialNum());
			zhpcallnumDao.addZhpcallnum(callnum);
		}
		String flag="";
		if(bin.getId()!=null&&!bin.getId().equals("")){
		List<Business> businessList=businessDao.queryWaitingareaByID(bin);
		flag= businessList.get(0).getFlag();
		System.out.println("业务类型:"+flag+"(01:机动车业务 02:驾驶人业务)");
		}
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		String WSIp=employee.getWSIp();
		if(flag!=null&&!flag.equals("")){
		//推送驾驶人或机动车信息:打开通知提档可存入相关信息
			if(jdctype!=null&&!jdctype.equals("")&&flag.equals("01")){
				showMessage(WSIp,jdctype,jdcnum);
			}else if(flag.equals("02")){
				showJSRMessage(WSIp,sfzmhm,binss);
			}
		}
		
		//将所叫号推送到双屏
		NumberIdPhoto numphoto = new NumberIdPhoto();
		numphoto.setNumberId(number.getIDNumber());
		String sphoto = null;
		List<NumberIdPhoto> numphotolist =this.queryPhotoBy(numphoto);
		if (numphotolist!=null&&numphotolist.size()>0) {
			byte[] sfzPhoto=numphotolist.get(0).getSfzphoto();
			sphoto=bitmaptoString(sfzPhoto);
			System.out.println("sphoto:"+sphoto);
		}
		String isOpenIndexCamera = cacheManager.getSystemConfig("isOpenIndexCamera");
		String datas = WSIp + "@" + number.getSerialNum()+":"+number.getId()+":"+isOpenIndexCamera+":"+sphoto;
		System.out.println("双屏号："+number.getSerialNum());
		publisher.publish(new DualScreenEvent(datas));
		
		System.out.println("叫号结果：[" + operNum + "][" + number.getSerialNum() + "][叫号成功]");
		//查询等待人数
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberDao.showWaitRs(countnum);
		String nameAndShul="";
		int AllSum=0;
		if(null != numbersCountList && numbersCountList.size()>0){
			for (Number nu : numbersCountList) {
				if(null != nu.getTypeCount()){
					AllSum += Integer.parseInt(nu.getTypeCount());
				}
				nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
			}
		}
		
//		publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		String isOpenwxqh = cacheManager.getSystemConfig("isOpenwxqh"); //0开启微信接口 1关闭微信接口
		String windowid = "";
		if("0".equals(isOpenwxqh)){
			windowid = numberDao.searchwindowidByip(loginIP);
			String sxh = deptCode+deptHall+number.getSerialNum();
//			System.out.println(windowid+"---"+sxh);
			String[] result = TrffUtils.AyUpdateStatus(sxh, windowid, "1");
			System.out.println("返回数据为："+result[0]+"----"+result[1]);
		}else{
			System.out.println("微信接口已关闭");
		}
		return "叫号成功";
	}
	
	private void showMessage(String loginIP,String hpzl,String hpzm) {
		CacheManager cacheManager = CacheManager.getInstance();
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");//是否启用接口
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String jklx = cacheManager.getSystemConfig("jklx");
		String flag="1",clpp1="",cllx="",djrq="",yxqz="",zt="";
		String sbkzjsjip = "";//设备控制计算机ip
		Map<String, Object> result = new HashMap<String, Object>();
		if ("0".equals(isUseInterface) && "1".equals(jklx)) {
			try {
				sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			try {
				result = TrffUtils.query_QueryCondition(hpzl, hpzm,deptCode,sbkzjsjip);
				if (!result.isEmpty()) {
					for (String s:result.keySet()) {
						Map<String, String> map = (Map<String, String>) result.get(s);
						hpzl = map.get("hpzl");
						hpzm = map.get("hphm");
						clpp1 = map.get("clpp1");
						cllx = map.get("cllx");
						djrq = map.get("djrq");
						yxqz = map.get("yxqz");
						zt = map.get("zt");
					}
					String datas = loginIP + "@" +flag+":"+ hpzl +":"+hpzm+":"+clpp1+":"+cllx+":"+yxqz+":"+djrq+":"+zt;
					System.out.println("推送机动车信息到双屏:"+hpzl+"---"+hpzm+"---"+clpp1+"---"+cllx+":"+yxqz+":"+djrq+":"+zt);
					publisher.publish(new DualScreenEvent(datas));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("接口没有打开，机动车信息无法获取!");
		}
		
	}

	private void showJSRMessage(String loginIP,String sfzmhm,String binss) {
		CacheManager cacheManager = CacheManager.getInstance();
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");//是否启用接口
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String jklx = cacheManager.getSystemConfig("jklx");
		String flag="2",lxzsyzbm="",xm="",lxzsxxdz="",cllx="",lxdh="",sjhm="";
		String sbkzjsjip = "";//设备控制计算机ip
		Map<String, Object> result = new HashMap<String, Object>();
		if ("0".equals(isUseInterface) && "1".equals(jklx) && sfzmhm.length()>=15) {
			try {
				sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			try {
				result = TrffUtils.query_JSR(sfzmhm,deptCode,sbkzjsjip);
				if (!result.isEmpty()) {
						Map<String, String> map = (Map<String, String>) result.get("JSRMessage");;
						sfzmhm = map.get("sfzmhm");
						lxzsyzbm = map.get("lxzsyzbm");
						lxzsxxdz = map.get("lxzsxxdz");
						xm = map.get("xm");
						lxdh = map.get("lxdh");
						sjhm = map.get("sjhm");
					String datas = loginIP + "@" +flag+":"+sfzmhm+":"+lxzsyzbm+":"+lxzsxxdz+":"+cllx+":"+xm+":"+lxdh+":"+sjhm+":"+binss;
					System.out.println("推送驾驶人信息到双屏:"+sfzmhm+":"+lxzsyzbm+":"+lxzsxxdz+":"+xm+":"+lxdh+":"+sjhm+":"+binss);
					publisher.publish(new DualScreenEvent(datas));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("接口没有打开，驾驶人信息无法获取!");
		}
		
	}
	public synchronized String callNumber_new(String operNum, String loginIP,String blbj) throws Exception {
		
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String sfkqcyyw = cacheManager.getSystemConfig("sfkqcyyw");
		String jklx = cacheManager.getSystemConfig("jklx");
		
		//判断是否同一IP登录
		if (!cacheManager.isSameIP(operNum, loginIP)) {
			return "此账号已在其它窗口登录，<br/>无法叫号";
		}
		String ucr = cacheManager.userCompare(operNum, loginIP, "请稍候再试");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		if (cacheManager.isPause(loginIP)) {
			return "暂停受理";
		}
		if ("1".equals(jklx)){CacheManager.getInstance().addOfLoginCache(operNum, loginIP);}
		
		//判断上一个号是否已办完
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if (null != searchNumber) {
			System.out.println("叫号时上个号码状态="+searchNumber.getStatus());
			if ("4".equals(searchNumber.getStatus())) {
				return "正在评价，请耐心等待";
			} else {
				return "上一个号未办完，不能叫号";
			}
		}
		//根据IP获取窗口，判断IP有没有绑定窗口
		Screen screen = cacheManager.getWindow(loginIP);
		if (null == screen) {
			return "此IP未与窗口绑定，无法叫号";
		}
		//获取绑定窗口可办理的业务
		if (StringUtils.isEmpty(screen.getBusinessid())) {
			return "此窗口未指定可办理的业务，无法叫号";
		}
		Number numgq = new Number();
		numgq.setCode(operNum);
		numgq.setDeptCode(deptCode);
		numgq.setDeptHall(deptHall);
		List<Number> listgq = numberDao.searchCurrentDayNumber(numgq);
		String resultgq = "";
		if (listgq.size()>0) {
			resultgq = this.hangupRecover_new(listgq.get(0).getOperNum(), listgq.get(0).getBarIp(), listgq.get(0).getId());
			System.out.println(resultgq);
			return resultgq;
		}
		
		Number number = new Number();
		
		Number numberLz = new Number();
		numberLz.setBarId(screen.getWindowId());
		numberLz.setDeptCode(deptCode);
		numberLz.setDeptHall(deptHall);
		
		List<Number> list=numberDao.getValueRecardLz(numberLz);
		if(null != list && list.size()>0){
			Number queueNumber = list.get(0);
			Number numVo = new Number();
			numVo.setDeptCode(deptCode);
			numVo.setDeptHall(deptHall);
			numVo.setOperNum("");
			numVo.setId(queueNumber.getId());
			numVo.setCode("");
			List<Number> listAll=numberDao.searchCurrentDayNumber(numVo);
			Number queueAll = listAll.get(0);
			number = queueAll;
			number.setSerialNum(number.getSerialNum().substring(13,number.getSerialNum().length()));
			number.setTypeName(number.getTypeName());
			number.setStatus("2");
			
		}else{
			number = fetchNumber(screen);
			//判断有无可叫的号
			if (null == number) {
				return "没有可呼叫的号";
			}
		}
		//推送查验信息
		if(number.getTypeName()!= null){
			if("0".equals(sfkqcyyw) && number.getTypeName().indexOf("查验")>=0){
				
				Number number2=numberDao.queryCarNumber(deptCode+deptHall+number.getSerialNum(), deptCode, deptHall);
				if(number2.getCarNumber()!=null && number2.getCarNumber()!="undefined"){
				if(number2.getCarType().equals("0")){
					sendstatus0++;
					}else if (number2.getCarType().equals("1")) {
						sendstatus1++;
					}
				System.out.println("小车状态:"+sendstatus0+"____________________"+"大车状态:"+sendstatus1+"______________"+"顺序号:"+number.getSerialNum());
				publisher.publish(new SendStatusCY(number2.getCarType()+ "@"+number2.getCarNumber()+"@"+sendstatus0+"@"+sendstatus1+"@"+number.getSerialNum())); //推送梧州查验信息
				}
			}
		}
		//更此号新数据库状态
		System.out.println("赋值前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
		number.setOperNum(operNum);
		number.setBarId(screen.getWindowId());
		number.setBeginTime(DateUtils.dateToString());
		numberDao.delValueRecardLz(number);
		


		
		

		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) 
				&& "0".equals(screen.getOpenInterFace()) 
				&& "0".equals(jklx)) {
			//上传呼叫信息
			String serialNum = deptCode + deptHall + number.getSerialNum();
			String jym = serialNum + deptCode.substring(0, 6) + "#" + operNum + "#01#"
				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveCallInfo(serialNum, operNum, "01", codeService.jm(jym));
			System.out.println("上传呼叫信息结果：[" + operNum + "][" + number.getSerialNum() + "][" + result[1] + "]");
			System.out.println("上传呼叫信息六合一返回结果result1[0]="+result[0]);
			if ("2".equals(result[0])) {
				number.setStatus("1");
				throw new TrffException(result[1]);
			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
				number.setStatus("1");
				throw new Exception(result[1]);
			}
		}else if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) 
				&& "1".equals(jklx) && "1".equals(blbj)) {
			String sbkzjsjip = "";//设备控制计算机ip
			String qhxxxlh = "";//取号信息序列号
			String sbkzjsjbh = "";//设备控制计算机编号
			String qhrxm = "";
			String dlrsfzmhm ="";
			String flag = "01";
			String rylb="1";
			try {
				sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			
//			sbkzjsjip = "127.0.0.1";
			
			Dept dept = new Dept();
			dept.setServersip(sbkzjsjip);
			List<Dept> listdept = new ArrayList<Dept>();
			try {
				listdept = deptDao.searchDept(dept);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if (listdept.size()<=0) {
				return "获取设备控制计算机编号失败";
			}
			sbkzjsjbh = listdept.get(0).getSbkzjsjbh();
			if (!"".equals(number.getNameB()) && null != number.getNameB()) {
				qhrxm = number.getNameB();
			}else if (!"".equals(number.getNameA()) && null != number.getNameA()) {
				qhrxm = number.getNameA();
			}
			if (!"".equals(number.getIDNumberB()) && null != number.getIDNumberB()) {
				dlrsfzmhm = number.getIDNumberB();
				rylb = "2";
			}
			if (!"".equals(number.getFlag()) && null != number.getFlag()) {
				flag = number.getFlag();
			}
			qhxxxlh = DateUtils.dateToString("yyMMdd")+sbkzjsjbh+number.getSerialNum();
			String[] result = TrffUtils.bcqhxxxr(loginIP, sbkzjsjip, qhxxxlh, number.getSerialNum(), flag, number.getIDNumber(), dlrsfzmhm, qhrxm, number.getEnterTime().substring(0,19), rylb, operNum,deptCode);
			if ("0".equals(result[0])) {
				return "补录叫号信息调取接口失败!";
			}
		}
		System.out.println("叫号时当前号码状态="+number.getStatus());
		System.out.println("第一次更新前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
		number.setBarIp(loginIP);
		numberDao.updateNumber(number);
		numberManager.saveCallNumber(operNum, number);
		
		String tempContent = screen.getContent2();
		if ("150300000400".equals(deptCode)||"510900000400".equals(deptCode)) {//
			if (number.getBarId().length() ==1) {
				screen.setContent2("(0"+number.getBarId()+") "+number.getSerialNum());
			}else {
				screen.setContent2("("+number.getBarId()+") "+number.getSerialNum());
			}
		}else {
			screen.setContent2(number.getSerialNum() + "号");
		}
		
		
//		String tempContent = "";
//		if ("2".equals(screen.getShowNum())) {
//			tempContent = screen.getContent2();
//			screen.setContent2(number.getSerialNum() + "号");
//		}else if ("3".equals(screen.getShowNum())) {
//			tempContent = screen.getContent3();
//			screen.setContent3(number.getSerialNum() + "号");
//		}else if ("4".equals(screen.getShowNum())) {
//			tempContent = screen.getContent4();
//			screen.setContent4(number.getSerialNum() + "号");
//		}
		String pattern = "yyyy-MM-dd HH:mm:ss SSS";
		String orderDate = DateUtils.dateToString(pattern);
		String strVoice = number.getSerialNum()+"@"+screen.getWindowId()+"@"+orderDate;
		if (StringUtils.isNotEmpty(screen.getMenuAddress())) {
			strVoice += "@" + screen.getMenuAddress();
		}
		
		VoiceManager.getInstance().add(strVoice);
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
		
		LEDUtils.sendText(screen, publisher, number, true);
		//电视屏发屏
		if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
			String ckip = screen.getCkip();
			if ("".equals(ckip) || null == ckip) {
				ckip = "127.0.0.1";
			}
			String ckid = screen.getWindowId();
			if (screen.getWindowId().length() == 1) {
				ckid = "0"+screen.getWindowId();
			}
			String ckmc = screen.getContent();
			String sxh = screen.getContent2();
			if (sxh.length()>5) {
				sxh = sxh.substring(0,5);
			}
			publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
		}
		if (!"".equals(screen.getAddress()) && screen.getAddress() != null) {
			System.out.println("窗口LED屏推送信息----窗口屏地址="+screen.getAddress()+"---com口="+screen.getComnum()+"---内容="+screen.getContent()+"----"+screen.getContent2());
		}
		String ywl = numberDao.selectYWLbyWindowId(screen.getWindowId(),deptCode,deptHall); //根据窗口号查询  返回窗口对应的业务办理量
		String windowCode = screen.getWindowId() + StringUtils.trimToEmpty(screen.getMenuAddress());
		String queue = number.getQueueCode();
		LED ledContent = new LED();
		ledContent.setDeptCode(deptCode);
		ledContent.setDeptHall(deptHall);
		List<LED> listLED = ledService.getLED_Content(ledContent);
		if (listLED.size()>0 || "320200000400".equals(deptHall)) {
			publisher.publish(new ShowCallInfoEvent(number.getSerialNum() + "@" + windowCode + "@" + number.getBusinessType()+"@"+ ywl)); //推送叫号信息到综合评
		}

		//判断是否发送窗口滚动(如： xxxx号正在办理中 )
		String isopenScreen = cacheManager.getSystemConfig("isopenScreen");
		if("0".equals(isopenScreen)){
			screenGDContetTimerTask sendGunDongTimerTask = new screenGDContetTimerTask(screen,operNum);
		}
		
		if("0".equals(cacheManager.getSystemConfig("isOpenQueueTV"))){//判断是否开启LED队列发屏
			System.out.println("进入队列发屏");
			//临沂发屏
			if ("371300000401".equals(deptCode)) {
					System.out.println("临沂大屏已经删除，2017-7-20");
					}else if ("510106000400".equals(deptCode)) {//成都六分所违法队列屏
						System.out.println("成都六分所违法队列屏"+number.getFlag());
						Number numberEJ= new Number();
						numberEJ.setId(number.getId());
						List<Number> listEJ = numberDao.getErJiCaiDanBianHao(numberEJ);
						if (listEJ.size()>0) {
							System.out.println("二级菜单编号为=="+listEJ.get(0).getEjcdbh());
						}
						if (listEJ.size()>0 &&"04".equals(listEJ.get(0).getEjcdbh())) {
							//叫号队列屏发屏
							LED led = new LED();
							led.setDeptCode(deptCode);
							led.setDeptHall(deptHall);
							String sendContenttemp="";
							led.setFlag("2");
							//获取LED屏信息
							List<LED> led_TVlist = ledService.getLedInfo_TV(led);
							for(int i=0;i<led_TVlist.size();i++){
								sendContenttemp = "";
								sendContenttemp = sendContent;
								
								String ledQueueShow = cacheManager.getSystemConfig("ledQueueShow");//获取发屏方式（纵向和横向）
								String content = led_TVlist.get(i).getContent();
								System.out.println("模板="+content);
								//个位数窗口屏补位
								//String windowCode = screen.getWindowId() + StringUtils.trimToEmpty(screen.getMenuAddress());
								if(10>Integer.parseInt(screen.getAddress())){
									screen.setAddress("0"+Integer.parseInt(screen.getAddress()));
								}
								if (windowCode.length()==1) {
									windowCode = "0"+windowCode;
								}
								//空格数量转换空格
								String kongge = "";
								if(null!=led_TVlist.get(i).getSpace() && !"".equals(led_TVlist.get(i).getSpace())&& !"0".equals(led_TVlist.get(i).getSpace())){
									for(int x=0;x<Integer.parseInt(led_TVlist.get(i).getSpace());x++){
										kongge += " ";
									}
								}
								//模板替换
								content = content.replaceAll("@", number.getSerialNum()).replaceAll("#", windowCode)+kongge;
								System.out.println("模板(替换后)="+content);
//						sendContent += content;
								sendContenttemp += content;
								
								
								//if(null != led_TVlist.get(i).getWaitingArea() && null != businessList.get(0).getWaitingarea()){
								//System.out.println("businessList.get(0).getWaitingarea()="+businessList.get(0).getWaitingarea());
//												sendContenttemp = sendContent;
								//System.out.println("sendContenttemp="+sendContenttemp);
								//字符长度
								int contentLength =0;
								int len = sendContenttemp.replaceAll("[^\\x00-\\xff]", "**").length();
								if(null != led_TVlist.get(i).getContent()){
									contentLength = Integer.parseInt(led_TVlist.get(i).getWidth())*Integer.parseInt(led_TVlist.get(i).getHeight())/((Integer.parseInt(led_TVlist.get(i).getLattice())/2)*(Integer.parseInt(led_TVlist.get(i).getLattice())/2))/2;
//													contentLength = (led_TVlist.get(i).getContent()+kongge).replaceAll("[^\\x00-\\xff]", "**").length() * Integer.parseInt(led_TVlist.get(i).getLattice()) /2+Integer.parseInt(led_TVlist.get(i).getLattice())/2;
								}
								//行数(高度/点阵数)
								int hangshu = Integer.parseInt(led_TVlist.get(i).getHeight())/Integer.parseInt(led_TVlist.get(i).getLattice());
								//列数(屏宽/字符长度)
//								int lieshu = (int) Math.floor(Integer.parseInt(led_TVlist.get(i).getWidth())/contentLength);
								int lieshu = Integer.parseInt(led_TVlist.get(i).getWidth())*2/Integer.parseInt(led_TVlist.get(i).getLattice())/(content).replaceAll("[^\\x00-\\xff]", "**").length();
								System.out.println("当前字符长度="+len);
								System.out.println("列数="+lieshu);
								System.out.println("行数="+hangshu);
								System.out.println("宽="+Integer.parseInt(led_TVlist.get(i).getWidth())+"长="+Integer.parseInt(led_TVlist.get(i).getHeight())+""+"总长度="+contentLength);
								System.out.println("未替换当前字符长度="+sendContenttemp.length());
								System.out.println("未替换总字符长度="+((content).length()*hangshu*lieshu));
								
//												if("0".equals(ledQueueShow)){
//													sendContenttemp+=kongge+" @";
//												}else{
//													sendContenttemp+=kongge+" ";
//												}
								//System.out.println("sendContenttemp="+sendContenttemp);
								
								if(len  >  contentLength){
									System.out.println("开始字符="+(sendContenttemp.length()-(content).length()*hangshu*lieshu));
									System.out.println("结束字符="+sendContenttemp.length());
									sendContenttemp = sendContenttemp.substring(sendContenttemp.length()-(content).length()*hangshu*lieshu,sendContenttemp.length());
//													sendContenttemp = sendContenttemp.substring(len-contentLength,len);
								}
								
								if("".equals(sendContenttemp)||null==sendContenttemp){
									LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow," ");
//													System.out.println(sendContent);
								}else{
									LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow,sendContenttemp);
									System.out.println(led_TVlist.get(i).getAddress()+"地址队列屏结果="+sendContenttemp);
								}
								Thread.sleep(100);					
							}
							sendContent = sendContenttemp ;
//						System.out.println(sendContent);
						}
					}else{
						
						//叫号队列屏发屏
						LED led = new LED();
						led.setDeptCode(deptCode);
						led.setDeptHall(deptHall);
						String sendContenttemp="";
						led.setFlag("2");
						//获取LED屏信息
						List<LED> led_TVlist = ledService.getLedInfo_TV(led);
						Business b = new Business();
						String No = new String();
						No = deptCode + deptHall + number.getSerialNum();
						List<Business> listW = businessDao.queryWaitingareaByNo(No);
						for(int i=0;i<led_TVlist.size();i++){
								if (led_TVlist.get(i).getWaitingArea().equals(listW.get(0).getWaitingarea())) {
									
							sendContenttemp = "";
							sendContenttemp = sendContent;
						
						String ledQueueShow = cacheManager.getSystemConfig("ledQueueShow");//获取发屏方式（纵向和横向）
						String content = led_TVlist.get(i).getContent();
						System.out.println("模板="+content);
						//个位数窗口屏补位
						//String windowCode = screen.getWindowId() + StringUtils.trimToEmpty(screen.getMenuAddress());
						if(10>Integer.parseInt(screen.getAddress())){
							screen.setAddress("0"+Integer.parseInt(screen.getAddress()));
						}
						if (windowCode.length()==1) {
							windowCode = "0"+windowCode;
						}
						//空格数量转换空格
						String kongge = "";
						if(null!=led_TVlist.get(i).getSpace() && !"".equals(led_TVlist.get(i).getSpace())&& !"0".equals(led_TVlist.get(i).getSpace())){
							for(int x=0;x<Integer.parseInt(led_TVlist.get(i).getSpace());x++){
								kongge += " ";
							}
						}
						//模板替换
						content = content.replaceAll("@", number.getSerialNum()).replaceAll("#", windowCode)+kongge;
						System.out.println("模板(替换后)="+content);
//						sendContent += content;
						sendContenttemp += content;
						
						
								//if(null != led_TVlist.get(i).getWaitingArea() && null != businessList.get(0).getWaitingarea()){
									//System.out.println("businessList.get(0).getWaitingarea()="+businessList.get(0).getWaitingarea());
//												sendContenttemp = sendContent;
												//System.out.println("sendContenttemp="+sendContenttemp);
												//字符长度
												int contentLength =0;
												int len = sendContenttemp.replaceAll("[^\\x00-\\xff]", "**").length();
												if(null != led_TVlist.get(i).getContent()){
													contentLength = Integer.parseInt(led_TVlist.get(i).getWidth())*Integer.parseInt(led_TVlist.get(i).getHeight())/((Integer.parseInt(led_TVlist.get(i).getLattice())/2)*(Integer.parseInt(led_TVlist.get(i).getLattice())/2))/2;
//													contentLength = (led_TVlist.get(i).getContent()+kongge).replaceAll("[^\\x00-\\xff]", "**").length() * Integer.parseInt(led_TVlist.get(i).getLattice()) /2+Integer.parseInt(led_TVlist.get(i).getLattice())/2;
												}
												//行数(高度/点阵数)
												int hangshu = Integer.parseInt(led_TVlist.get(i).getHeight())/Integer.parseInt(led_TVlist.get(i).getLattice());
												//列数(屏宽/字符长度)
//												int lieshu = (int) Math.floor(Integer.parseInt(led_TVlist.get(i).getWidth())/contentLength);
												int lieshu = Integer.parseInt(led_TVlist.get(i).getWidth())*2/Integer.parseInt(led_TVlist.get(i).getLattice())/(content).replaceAll("[^\\x00-\\xff]", "**").length();
												System.out.println("当前字符长度="+len);
												System.out.println("列数="+lieshu);
												System.out.println("行数="+hangshu);
												System.out.println("宽="+Integer.parseInt(led_TVlist.get(i).getWidth())+"长="+Integer.parseInt(led_TVlist.get(i).getHeight())+""+"总长度="+contentLength);
												System.out.println("未替换当前字符长度="+sendContenttemp.length());
												System.out.println("未替换总字符长度="+((content).length()*hangshu*lieshu));

//												if("0".equals(ledQueueShow)){
//													sendContenttemp+=kongge+" @";
//												}else{
//													sendContenttemp+=kongge+" ";
//												}
												//System.out.println("sendContenttemp="+sendContenttemp);
												
												if(len  >  contentLength){
													System.out.println("开始字符="+(sendContenttemp.length()-(content).length()*hangshu*lieshu));
													System.out.println("结束字符="+sendContenttemp.length());
													sendContenttemp = sendContenttemp.substring(sendContenttemp.length()-(content).length()*hangshu*lieshu,sendContenttemp.length());
//													sendContenttemp = sendContenttemp.substring(len-contentLength,len);
												}
												
												if("".equals(sendContenttemp)||null==sendContenttemp){
													LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow," ");
//													System.out.println(sendContent);
												}else{
													LEDUtils.sendLED_TVTextTY(led_TVlist.get(i), ledQueueShow,sendContenttemp);
													System.out.println(led_TVlist.get(i).getAddress()+"地址队列屏结果="+sendContenttemp);
												}
									Thread.sleep(100);
									}
						        }
						sendContent = sendContenttemp ;
//						System.out.println(sendContent);
					}
				}
		screen.setContent2(tempContent);
		
		//添加日志
		Number num = new Number();
		num.setId(number.getId());
		if (numberDao.getValueRecordAllById(num).size()>0) {
			Number numberVO = numberDao.getValueRecordAllById(num).get(0);
			numberVO.setStatus(number.getStatus());
			numberVO.setSerialNum(deptCode+deptHall+number.getSerialNum());
			numberVO.setOperNum(number.getOperNum());
			numberVO.setBarId(number.getBarId());
			numberVO.setBeginTime(number.getBeginTime());
			numberVO.setDeptCode(deptCode);
			numberVO.setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(numberVO);
			
			//成都特有
			Number nameNum = new Number();
			System.out.println("第二次赋值前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
			nameNum = number;
			System.out.println("第二次赋值后"+nameNum.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
			System.out.println("第二次更新前"+number.getSerialNum()+"号,员工编号为"+operNum+"窗口号为"+screen.getWindowId()+"开始时间"+DateUtils.dateToString()+"状态"+number.getStatus());
			numberDao.updateNumber(number);
			nameNum = null;
			//添加综合屏叫号信息
			Zhpcallnum callnum = new Zhpcallnum();
			callnum.setId(numberVO.getId());
			callnum.setBarId(numberVO.getBarId());
			callnum.setClientno(numberVO.getSerialNum());
			zhpcallnumDao.addZhpcallnum(callnum);
		}
		//将所叫号推送到双屏
		String isOpenIndexCamera = cacheManager.getSystemConfig("isOpenIndexCamera");
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		//将所叫号推送到双屏
		NumberIdPhoto numphoto = new NumberIdPhoto();
		numphoto.setNumberId(number.getIDNumber());
		String sphoto = null;
		List<NumberIdPhoto> numphotolist =this.queryPhotoBy(numphoto);
		if (numphotolist!=null&&numphotolist.size()>0) {
			byte[] sfzPhoto=numphotolist.get(0).getSfzphoto();
			sphoto=bitmaptoString(sfzPhoto);
			System.out.println("sphoto:"+sphoto.length());
		}
		String datas = employee.getWSIp() + "@" + number.getSerialNum()+":"+number.getId()+":"+isOpenIndexCamera+":"+sphoto;
		System.out.println("双屏号："+number.getSerialNum());
		publisher.publish(new DualScreenEvent(datas));
		
		System.out.println("叫号结果：[" + operNum + "][" + number.getSerialNum() + "][叫号成功]");
		//查询等待人数
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberDao.showWaitRs(countnum);
		String nameAndShul="";
		int AllSum=0;
		if(null != numbersCountList && numbersCountList.size()>0){
			for (Number nu : numbersCountList) {
				if(null != nu.getTypeCount()){
					AllSum += Integer.parseInt(nu.getTypeCount());
				}
				nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
			}
		}
		
//		publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		String isOpenwxqh = cacheManager.getSystemConfig("isOpenwxqh"); //0开启微信接口 1关闭微信接口
		String windowid = "";
		if("0".equals(isOpenwxqh)){
			windowid = numberDao.searchwindowidByip(loginIP);
			String sxh = deptCode+deptHall+number.getSerialNum();
//			System.out.println(windowid+"---"+sxh);
			String[] result = TrffUtils.AyUpdateStatus(sxh, windowid, "1");
			System.out.println("返回数据为："+result[0]+"----"+result[1]);
		}else{
			System.out.println("微信接口已关闭");
		}
		if ("1".equals(jklx) && "0".equals(cacheManager.getSystemConfig("isUseInterface"))) {
			String qhrxm = "";
			String dlrsfzmhm ="";
			String flag = "01";
			if (!"".equals(number.getNameB()) && null != number.getNameB()) {
				qhrxm = number.getNameB();
			}else if (!"".equals(number.getNameA()) && null != number.getNameA()) {
				qhrxm = number.getNameA();
			}
			if (!"".equals(number.getIDNumberB()) && null != number.getIDNumberB()) {
				dlrsfzmhm = number.getIDNumberB();
			}
			if (!"".equals(number.getFlag()) && null != number.getFlag()) {
				flag = number.getFlag();
			}
			return "2@"+number.getSerialNum()+"@"+flag+"@"+number.getIDNumber()+"@"+dlrsfzmhm+"@"+qhrxm+"@"+number.getEnterTime();
		}else {
			return "叫号成功";
		}
	}
	
	private Number fetchNumber(Screen screen) {
		Number number = null;
		NumberManager numberManager = NumberManager.getInstance();
		//判断是否有优先级
		if (StringUtils.isNotEmpty(screen.getPriority())) {
			String[] businessArray = screen.getPriority().split(";");
			for (String businessType : businessArray) {
				number = numberManager.fetchNumber(businessType);
				if (null != number) {
					break;
				}
			}
		} else {
			number = numberManager.fetchNumber(screen.getBusinessid());
		}
		return number;
	}

	public String recall(String operNum, String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String jklx = cacheManager.getSystemConfig("jklx");
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能重呼");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		
		Number searchNumber = NumberManager.getInstance().fetchCallNumber(operNum);
		if (null == searchNumber) {
			return "未叫号，不能重呼";
		}
		
		if ("4".equals(searchNumber.getStatus())) {
			return "正在评价，请耐心等待";
		}
		
		String pattern = "yyyy-MM-dd HH:mm:ss SSS";
		String orderDate = DateUtils.dateToString(pattern);
		Screen screen = cacheManager.getWindow(loginIP);
		String numAndAddress = searchNumber.getSerialNum()+"@"+searchNumber.getBarId()+"@"+orderDate;
		if (StringUtils.isNotEmpty(screen.getMenuAddress())) {
			numAndAddress += "@" + screen.getMenuAddress();
		}
		VoiceManager.getInstance().add(numAndAddress);
		//将所叫号推送到双屏
		String isOpenIndexCamera = cacheManager.getSystemConfig("isOpenIndexCamera");
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		String datas = employee.getWSIp() + "@" + searchNumber.getSerialNum()+":"+searchNumber.getId()+":"+isOpenIndexCamera;
		publisher.publish(new DualScreenEvent(datas));
		
		//添加日志
		Number num = new Number();
		num.setId(searchNumber.getId());
		if (numberDao.getValueRecordAllById(num).size()>0) {
			Number numberVO = numberDao.getValueRecordAllById(num).get(0);
			numberVO.setStatus("9");
			numberVO.setDeptCode(deptCode);
			numberVO.setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(numberVO);
		}
		System.out.println("重呼结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][重呼成功]");
		if ("1".equals(jklx)) {
//			return "9@1@重呼成功";
			return "9@"+searchNumber.getSerialNum()+"@重呼成功";
		}else {
			return "重呼成功";
		}
	}

	public String pass(String operNum, String loginIP,String passReason) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String jklx = cacheManager.getSystemConfig("jklx");
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能过号");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if (null == searchNumber) {
			return "未叫号，不能过号";
		}
		String isOpenForceEnvalue = cacheManager.getSystemConfig("isOpenForceEnvalue");//强制评价开关
		if ("0".equals(isOpenForceEnvalue)) {//判断是否启用强制评价 0 启用 1未启用
			return "您无权操作过号功能";
		}
		
		//过号时间增加限制
		String ghnum = cacheManager.getSystemConfig("ghnum");//过号限制时间
		int ghn = 30;
		if (!"".equals(ghnum) && null != ghnum) {
			ghn = Integer.parseInt(ghnum);
		}else {
			ghn = 30;
		}
		String beginTime = searchNumber.getBeginTime();
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (!"".equals(beginTime) && null != beginTime) {
			long from = simpleFormat.parse(beginTime).getTime();
			long to = simpleFormat.parse(DateUtils.dateToString()).getTime();
			if (ghn > (int)(to-from)/1000) {
				return "过号限制"+ghnum+"秒,时间未到!";
			}
		}
		
		//if ("4".equals(searchNumber.getStatus())) {
			//return "正在评价，请耐心等待";
		//}
		Screen screen = cacheManager.getWindow(loginIP);
		
		List<Business> busList = businessDao.getBusinessList(searchNumber.getBusinessType(), null, null, null, deptCode, deptHall);
		
		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) 
				&& "0".equals(screen.getOpenInterFace()) 
				&& "0".equals(busList.get(0).getLiuzhuan())
				&& "0".equals(jklx)) {//增加流转末进入六合一
			//上传过号信息
			String serialNum = deptCode + deptHall + searchNumber.getSerialNum();
			String jym = serialNum + deptCode.substring(0, 6) + "#0#"
				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveEvaluationInfo(serialNum, "0", codeService.jm(jym));
			System.out.println("上传过号信息结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][" + result[1] + "]");
			//更新6合1流水号
			/*System.out.println("流水号为："+result[3]);
			if (null != result[3] || !result[3].equals("")) {
				Number numberVO  = new Number();
				numberVO.setId(searchNumber.getId());
				numberVO.setLsh(result[3]);
				numberDao.updatelsh(numberVO);
			}*/
			System.out.println("上传过号信息六合一返回结果result1[0]="+result[0]);
			if ("2".equals(result[0])) {
				throw new TrffException(result[1]);
			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
				throw new Exception(result[1]);
			}
		}

		String id = searchNumber.getId();
		//梧州新增过号推送
		List<Number> list = numberDao.getNumberViaId(id);
		String str = list.get(0).getClientno();
		String strings = str.substring(13)+"@"+list.get(0).getBarId();
//		publisher.publish(new GuoHaoTuiSong(strings));
		
		
		Number number = new Number();
		number.setId(searchNumber.getId());
		number.setStatus("3");
		number.setEndTime(DateUtils.dateToString());
		if (!passReason.equals("NoReason")) {
			number.setPassReason(passReason);
		}
		numberDao.updateNumber(number);
		number = null;
		System.out.println("过号移除缓存前号码状态="+searchNumber.getStatus());
		numberManager.removeCallNumber(operNum);
		cacheManager.updateLoginIpStatus(operNum);
		
		
		
		//流转功能
		Number numLZ = new Number();
//		numLZ = searchNumber;
		
		if (busList.size() >0 
				&& busList.get(0).getLiuzhuan() != null 
				&& !"".equals(busList.get(0).getLiuzhuan()) 
				&& !"0".equals(busList.get(0).getLiuzhuan()) 
				&& "0".equals(busList.get(0).getIsautolz())) {
			
			
			//添加流转日志
			System.out.println("进入流转日志");
			Number numberLZVo = new Number();
			numberLZVo.setId(id);
			List<Number> listVo= numberDao.getValueRecordAllById(numberLZVo);
			if(listVo.size()>0){
				listVo.get(0).setEndTime(listVo.get(0).getEndtime());
				listVo.get(0).setDeptCode(deptCode);
				listVo.get(0).setDeptHall(deptHall);
				numberDao.insertValuerecordLZRZ(listVo.get(0));
				
				//删除综合屏叫号信息
//				zhpcallnumDao.delZhpcallnum(listVo.get(0).getId());
			}
			
			List<Business> busVO = businessDao.getBusinessList(busList.get(0).getLiuzhuan(), null, null, null, deptCode, deptHall);
			if (busVO.size() > 0) {
				numLZ.setBusinessType(busVO.get(0).getId());
				numLZ.setQueueCode(busVO.get(0).getQueueCode());
				numLZ.setStatus("1");
				numLZ.setId(id);
				String patternVo = "yyyy-MM-dd HH:mm:ss SSS";
				numLZ.setEnterTime(DateUtils.dateToString(patternVo));
				numberDao.updateNumber(numLZ);
			}
			CallOutStatusAction a=new CallOutStatusAction();
			a.yiChangDHCL();
		}
		
		
		
		
		
		if (!"1".equals(screen.getShowNum())) {
			if (screen.getWindowGDContent() != null) {
				screen.setContent2(screen.getWindowGDContent());
			}else{
				screen.setContent2(" ");
			}
		}
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
//		LEDUtils.sendText(screen, null, null, false);
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		String datas = employee.getWSIp() +"@"+loginIP;
		publisher.publish(new PassEvent(datas));
		
		//定时警告 0 启用 1 不启用
		/*String isOpenJgts = cacheManager.getSystemConfig("isOpenJgts");
		String jgtsTime = cacheManager.getSystemConfig("jgtsTime");
		String swkssz=cacheManager.getSystemConfig("swkssz");
		String swksfz=cacheManager.getSystemConfig("swksfz");
		String swjssz=cacheManager.getSystemConfig("swjssz");
		String swjsfz=cacheManager.getSystemConfig("swjsfz");
		String xwkssz=cacheManager.getSystemConfig("xwkssz");
		String xwksfz=cacheManager.getSystemConfig("xwksfz");
		String xwjssz=cacheManager.getSystemConfig("xwjssz");
		String xwjsfz=cacheManager.getSystemConfig("xwjsfz");
		if("0".equals(isOpenJgts)){
			System.out.println(searchNumber.getSerialNum());
			WarningTimerTask warningTimerTask = new WarningTimerTask(deptCode, deptHall,operNum,loginIP,jgtsTime,publisher,
								swkssz,swksfz,swjssz,swjsfz,xwkssz,xwksfz,xwjssz,xwjsfz
					);
		}*/
		//查询等待人数
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberDao.showWaitRs(countnum);
		String nameAndShul="";
		int AllSum=0;
		if(null != numbersCountList && numbersCountList.size()>0){
			for (Number nu : numbersCountList) {
				if(null != nu.getTypeCount()){
					AllSum += Integer.parseInt(nu.getTypeCount());
				}
				nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
			}
		}
//		publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		//添加日志
		Number numberVo = new Number();
		numberVo.setId(id);
		List<Number> listVo= numberDao.getValueRecordAllById(numberVo);
		if(listVo.size()>0){
			listVo.get(0).setEndTime(listVo.get(0).getEndtime());
			listVo.get(0).setDeptCode(deptCode);
			listVo.get(0).setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(listVo.get(0));
			
			//删除综合屏叫号信息
			zhpcallnumDao.delZhpcallnum(listVo.get(0).getId());
		}
		
		System.out.println("过号结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][过号成功@1]");
		//判断是否发送滚动信息
		String resultVO = "";
		if("0".equals(screen.getIsgd()) && null != screen.getWindowGDContent()){
			resultVO = LEDUtils.sendGunDongContent(screen);//发送滚动信息
			java.lang.System.out.println("过号时滚动字幕发送结果："+resultVO);
		}else{
//		java.lang.System.out.println("LED设置高=="+screen.getLed().getHeight()+"LED设置宽=="+screen.getLed().getWidth()+"窗口设置高=="+screen.getLedWindowHeight()+"窗口设置宽=="+screen.getLedWindowWidth());
		//初始化窗口发屏
			resultVO = LEDUtils.sendText(screen, null, null, false);
			//电视屏发屏
			if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
				String ckip = screen.getCkip();
				if ("".equals(ckip) || null == ckip) {
					ckip = "127.0.0.1";
				}
				String ckid = screen.getWindowId();
				if (screen.getWindowId().length() == 1) {
					ckid = "0"+screen.getWindowId();
				}
				String ckmc = screen.getContent();
				String sxh = screen.getContent2();
				if (sxh.length()>5) {
					sxh = sxh.substring(0,5);
				}
				publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
			}
		}
		
		String isOpenwxqh = cacheManager.getSystemConfig("isOpenwxqh"); //0开启微信接口 1关闭微信接口
		String barid = "";

		if("0".equals(isOpenwxqh)){
			barid = numberDao.searchwindowidByip(loginIP);
			String sxh = deptCode+deptHall+searchNumber.getSerialNum();
//			System.out.println("窗口号"+barid+"---顺序号"+sxh);
			String[] result = TrffUtils.AyUpdateStatus(sxh, barid, "2");
			System.out.println("返回数据为："+result[0]+"----"+result[1]);
		}else{
			System.out.println("微信接口已关闭");
		}
		
		if ("1".equals(jklx)) {
			return "3@1@过号成功";
		}else {
			return "过号成功@1";
		}
	}
	private ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	//保存摄像头图片
	public String savePhoto(Number number,String flag) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		//String deptCode = cacheManager.getOfDeptCache("deptCode");
		//String deptHall = cacheManager.getOfDeptCache("deptHall");
		String upflag="0";//0成功 1本地失败 2上传六合一失败
		try {
			if("0".equals(flag)){//flag标记 0：摄像头 1:手写板
				
				if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) && "0".equals(cacheManager.getSystemConfig("jklx"))) {
					//上传摄像头照片
				/*	if (null != number.getPhotoBase64()) {
						String photo = new BASE64Encoder().encode(number.getPhotoBase64());
						photo = new String(photo.getBytes("8859_1"), "UTF-8");
						//String serialNum = deptCode + deptHall + number.getSerialNum();
						
						//String[] result = TrffUtils.savePhoto(serialNum, photo);
						String[] result = TrffUtils.savePhoto(number.getSerialNum(), photo);
						System.out.println("上传照片结果：[" + number.getOperNum() + "][" + number.getSerialNum() + "][" + result[1] + "]");
						if ("2".equals(result[0])) {
							System.out.println("摄像头照片上传六合一失败");
							upflag = "2";
							throw new TrffException(result[1]);
						} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
							System.out.println("摄像头照片上传六合一失败");
							upflag = "2";
							throw new Exception(result[1]);
						}
					}*/
				}
			}else if("1".equals(flag)){
					//是否调用接口传入手写板内容
				/*	if (null != number.getSignature() && "0".equals(cacheManager.getSystemConfig("isUseInterface"))) {
						String signature = new BASE64Encoder().encode(number.getSignature());
						signature = new String(signature.getBytes("8859_1"), "UTF-8");
						
						Number num = NumberManager.getInstance().fetchCallNumber(number.getOperNum());
						String[] result = TrffUtils.saveSignName(num.getIDNumber(), signature);
						System.out.println("上传手写板结果：[" + number.getOperNum() + "][" + number.getSerialNum() + "][" + result[1] + "]");
						System.out.println("上传手写板信息六合一返回结果result1[0]="+result[0]);
						if ("2".equals(result[0])) {
							throw new TrffException(result[1]);
						} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
							throw new Exception(result[1]);
						}
					}*/
			}else if("2".equals(flag)){
				//是否调用接口传入高拍仪内容
				/*if (null != number.getGpyPhotoBase64() && "0".equals(cacheManager.getSystemConfig("isUseInterface"))) {
					String getGpyPhotoBase64 = new BASE64Encoder().encode(number.getSignature());
					getGpyPhotoBase64 = new String(getGpyPhotoBase64.getBytes("8859_1"), "UTF-8");
					
					Number num = NumberManager.getInstance().fetchCallNumber(number.getOperNum());
					String[] result = TrffUtils.saveSignName(num.getIDNumber(), getGpyPhotoBase64);
					System.out.println("上传高拍仪结果：[" + number.getOperNum() + "][" + number.getSerialNum() + "][" + result[1] + "]");
					System.out.println("上传高拍仪信息六合一返回结果result1[0]="+result[0]);
					if ("2".equals(result[0])) {
						throw new TrffException(result[1]);
					} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
						throw new Exception(result[1]);
					}
				}*/
			}
			//更新本地照片或手写板
			numberDao.updateNumber(number);
			//将照片保存到挂起库
			Number num = new Number();
			num.setId(number.getId());
			num.setGpyPhoto13(number.getGpyPhoto13());
			numberDao.updateValuerecordGqPhoto(num);
			
		} catch (Exception e) {
			upflag = "1";
		}
		return upflag;
	}
	static String LZZT ="";//流转状态
	static Map<String, String> LZZTMAP = new HashMap<String, String>();
	static Map<String, String> LZ6HE1 = new HashMap<String, String>();
	public String toEvaluation(final String operNum, final String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String jklx = cacheManager.getSystemConfig("jklx");
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能评价");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if (null == searchNumber) {
			return "未叫号，不能评价";
		}
		
		if ("4".equals(searchNumber.getStatus())) {
			return "正在评价，请耐心等待";
		}
		System.out.println("评价前号码状态="+searchNumber.getStatus());
		Number number = new Number();
		number.setStatus("4");
		number.setId(searchNumber.getId());
		numberDao.updateNumber(number);
		number = null;
		
		
		
		
		//流转功能
		Number numLZ = new Number();
//		numLZ = searchNumber;
		List<Business> busList = businessDao.getBusinessList(searchNumber.getBusinessType(), null, null, null, deptCode, deptHall);
		if (busList.size() >0 && "0".equals(busList.get(0).getLiuzhuan())) {
			LZ6HE1.put(searchNumber.getId(), "0");
		}else {
			LZ6HE1.put(searchNumber.getId(), "1");
		}
		
		if (busList.size() >0 
				&& busList.get(0).getLiuzhuan() != null 
				&& !"".equals(busList.get(0).getLiuzhuan()) 
				&& !"0".equals(busList.get(0).getLiuzhuan()) 
				&& "0".equals(busList.get(0).getIsautolz())) {
			LZZT = searchNumber.getId()+"@0";
			LZZTMAP.put(searchNumber.getId(), "0");
			
			
			//添加流转日志
			Number numberLZVo = new Number();
			numberLZVo.setId(searchNumber.getId());
			List<Number> listVo= numberDao.getValueRecordAllById(numberLZVo);
			if(listVo.size()>0){
				listVo.get(0).setEndTime(listVo.get(0).getEndtime());
				listVo.get(0).setDeptCode(deptCode);
				listVo.get(0).setDeptHall(deptHall);
				numberDao.insertValuerecordLZRZ(listVo.get(0));
				
				//删除综合屏叫号信息
//				zhpcallnumDao.delZhpcallnum(listVo.get(0).getId());
			}
			
			
			List<Business> busVO = businessDao.getBusinessList(busList.get(0).getLiuzhuan(), null, null, null, deptCode, deptHall);
			if (busVO.size() > 0) {
				numLZ.setBusinessType(busVO.get(0).getId());
				numLZ.setQueueCode(busVO.get(0).getQueueCode());
				numLZ.setStatus("1");
				numLZ.setId(searchNumber.getId());
				String patternVo = "yyyy-MM-dd HH:mm:ss SSS";
				numLZ.setEnterTime(DateUtils.dateToString(patternVo));
				numberDao.updateNumber(numLZ);
			}
			CallOutStatusAction a=new CallOutStatusAction();
			a.yiChangDHCL();
		}else {
			LZZT = searchNumber.getId()+"@1";
			LZZTMAP.put(searchNumber.getId(), "1");
		}
		searchNumber.setStatus("4");
		
		String isUseOldDevice = cacheManager.getSystemConfig("isUseOldDevice");
		String deviceType = cacheManager.getSystemConfig("deviceType");//评价器类型 0新评价器 1旧评价器
		String isCamera = cacheManager.getSystemConfig("isCamera");
		String isSignature = cacheManager.getSystemConfig("isSignature");
		String isOpenForceEnvalue = cacheManager.getSystemConfig("isOpenForceEnvalue");//强制评价开关
		String pjTime = cacheManager.getSystemConfig("pjTime");//定时器默认时间
		int ptime = Integer.parseInt(pjTime);
		int pt = ptime*1000;
		if ("0".equals(isUseOldDevice)) {
			Screen screen = cacheManager.getWindow(loginIP);
			//判断窗口是否启用评价器 0 启用 1 不启用
			if("1".equals(screen.getIsOpenOldDevice())){
				isUseOldDevice = "1";
			}
		}
		if ("1".equals(isUseOldDevice)) {
			//不满意状态可用则读取不满意评价原因
			List<Code> codeList = codeService.getCodeByDmlbAndDmz("0003", "", "1", deptCode, deptHall);
			JSONObject obj = new JSONObject();
			for (Code code : codeList) {
				obj.put(code.getDm(), code.getMc());
			}
			Employee employee=iEmployeeService.getEmployeeByCode(operNum);
			String datas = employee.getWSIp()  + "@" + obj.toString() + "@" + isCamera + "@" + isSignature + "@" + isOpenForceEnvalue+"@"+pt;
			publisher.publish(new EvaluateEvent(datas));
			if("0".equals(isOpenForceEnvalue)){//判断是否启用强制评价
				//判断是否超时12秒，是：自动提交
				PjTimerTask pjtimertask = new PjTimerTask(deptCode, deptHall,searchNumber.getId(),loginIP);
			}
		}
		System.out.println("提请评价结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][正在评价，请耐心等待@" + isUseOldDevice + "]");
		
		String isOpenwxqh = cacheManager.getSystemConfig("isOpenwxqh"); //0开启微信接口 1关闭微信接口
		String barid = "";
		if("0".equals(isOpenwxqh)){
			barid = numberDao.searchwindowidByip(loginIP);
			String sxh = deptCode+deptHall+searchNumber.getSerialNum();
//			System.out.println("窗口号"+barid+"---顺序号"+sxh);
			String[] result = TrffUtils.AyUpdateStatus(sxh, barid, "2");
			System.out.println("返回数据为："+result[0]+"----"+result[1]);
		}else{
			System.out.println("微信接口已关闭");
		}
		if ("1".equals(jklx)) {
			return "4@1@正在评价";
		}else {
			return "正在评价，请耐心等待@" + isUseOldDevice + "@" +deviceType;
		}
	}

	public String toEvaluationForQueueOut(final String operNum, final String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能评价");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if (null == searchNumber) {
			return "未叫号，不能评价!";
		}
		
		if ("4".equals(searchNumber.getStatus())) {
			return "正在评价，请耐心等待";
		}
		System.out.println("评价前号码状态="+searchNumber.getStatus());
		Number number = new Number();
		number.setStatus("4");
		number.setId(searchNumber.getId());
		number.setOutFlag("1");
		numberDao.updateNumber(number);
		number = null;
		
		searchNumber.setStatus("4");
		
		String isUseOldDevice = cacheManager.getSystemConfig("isUseOldDevice");
		String deviceType = cacheManager.getSystemConfig("deviceType");//评价器类型 0新评价器 1旧评价器
		String isCamera = cacheManager.getSystemConfig("isCamera");
		String isSignature = cacheManager.getSystemConfig("isSignature");
		String isOpenForceEnvalue = cacheManager.getSystemConfig("isOpenForceEnvalue");//强制评价开关
		String pjTime = cacheManager.getSystemConfig("pjTime");//定时器默认时间
		int ptime = Integer.parseInt(pjTime);
		int pt = ptime*1000;
		if ("0".equals(isUseOldDevice)) {
			Screen screen = cacheManager.getWindow(loginIP);
			//判断窗口是否启用评价器 0 启用 1 不启用
			if("1".equals(screen.getIsOpenOldDevice())){
				isUseOldDevice = "1";
			}
		}
		if ("1".equals(isUseOldDevice)) {
			//不满意状态可用则读取不满意评价原因
			String deptCode = cacheManager.getOfDeptCache("deptCode");
			String deptHall = cacheManager.getOfDeptCache("deptHall");
			List<Code> codeList = codeService.getCodeByDmlbAndDmz("0003", "", "1", deptCode, deptHall);
			JSONObject obj = new JSONObject();
			for (Code code : codeList) {
				obj.put(code.getDm(), code.getMc());
			}
			String datas = loginIP + "@" + obj.toString() + "@" + isCamera + "@" + isSignature + "@" + isOpenForceEnvalue+"@"+pt;
			publisher.publish(new EvaluateEvent(datas));
			if("0".equals(isOpenForceEnvalue)){//判断是否启用强制评价
				//判断是否超时12秒，是：自动提交
				PjTimerTask pjtimertask = new PjTimerTask(deptCode, deptHall,searchNumber.getId(),loginIP);
			}
		}
		System.out.println("提请评价结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][正在评价，请耐心等待@" + isUseOldDevice + "]");
		return "正在评价，请耐心等待@" + isUseOldDevice + "@" +deviceType;
	}
	
	
	
	
	public void evaluation(Number number, String loginIP) throws Exception {
		String flag = number.getCarType();
		if(flag==null){
			flag = number.getStatus();
		}
		Pjflag=flag;
		CacheManager cacheManager = CacheManager.getInstance();
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");
		String jklx = cacheManager.getSystemConfig("jklx");
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String pjlb = "2",pjjg="2",qhxxxlh="",fqpjsj="",pjsj="";
		String sbkzjsjbh = "";//设备控制计算机编号;
		String sbkzjsjip = "";//设备控制计算机ip
		//流转功能
//		if (number.getId().equals(LZZT.split("@")[0])) {
//			if ("0".equals(LZZT.split("@")[1])) {
//				number.setStatus("1");
//			}else {
//				if ("1".equals(flag)) {
//					number.setStatus("6");
//				} else if ("2".equals(flag)) {
//					number.setStatus("5");
//					Screen screen = cacheManager.getWindow(loginIP);
//					number.setEvaluResult(screen.getValuemust());
//				}
//			}
//		}else {
//			if ("1".equals(flag)) {
//				number.setStatus("6");
//			} else if ("2".equals(flag)) {
//				number.setStatus("5");
//				Screen screen = cacheManager.getWindow(loginIP);
//				number.setEvaluResult(screen.getValuemust());
//			}
//		}
		if (LZZTMAP.containsKey(number.getId())) {
			if ("0".equals(LZZTMAP.get(number.getId()))) {
				number.setStatus("1");
			}else {
				if ("1".equals(flag)) {
					number.setStatus("6");
					pjlb = "1";
				} else if ("2".equals(flag)) {
					number.setStatus("5");
					Screen screen = cacheManager.getWindow(loginIP);
					number.setEvaluResult(screen.getValuemust());
				}
			}
		}else {
			if ("1".equals(flag)) {
				number.setStatus("6");
				pjlb = "1";
			} else if ("2".equals(flag)) {
				number.setStatus("5");
				Screen screen = cacheManager.getWindow(loginIP);
				number.setEvaluResult(screen.getValuemust());
			}
		}
		if ("1".equals(jklx)) {
			try {
				sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			
			
//			sbkzjsjip = "127.0.0.1";
			
			
			List<Dept> listdept = new ArrayList<Dept>();
			Dept dept = new Dept();
			dept.setServersip(sbkzjsjip);
			try {
				listdept = deptDao.searchDept(dept);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if (!"".equals(number.getEvaluResult()) && null != number.getEvaluResult()) {
				pjjg = number.getEvaluResult();
			}
			if (listdept.size()>0) {
				sbkzjsjbh = listdept.get(0).getSbkzjsjbh();
				System.out.println("sbkzjsjbh="+sbkzjsjbh);
			}
			if (number.getSerialNum().length()>5) {
				qhxxxlh = DateUtils.dateToString("yyMMdd")+sbkzjsjbh+number.getSerialNum().substring(13);
			}else {
				qhxxxlh = DateUtils.dateToString("yyMMdd")+sbkzjsjbh+number.getSerialNum();
			}
			fqpjsj = DateUtils.dateToString("yyyy-MM-dd HH:mm:ss");
			pjsj = DateUtils.dateToString("yyyy-MM-dd HH:mm:ss");
		}
		
		String operNum = number.getOperNum();
		Screen screen = cacheManager.getWindow(loginIP);
		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) 
				&& "0".equals(screen.getOpenInterFace())
				&& "0".equals(LZ6HE1.get(number.getId()))
				&& "0".equals(jklx)) {//增加流转末进入六合一
			
			//上传评价信息
			String serialNum ="";
			if(number.getSerialNum().length()>10){
				//定时器评价
				serialNum = number.getSerialNum();
			}else{
				//外屏页面评价
				serialNum = deptCode + deptHall + number.getSerialNum();
			}
			
			//评价成功调用过号  
			String jym = serialNum + deptCode.substring(0, 6) + "#0#"+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveEvaluationInfo(serialNum, "0", codeService.jm(jym));
			//评价成功调用过号end
			//调用评价接口
			/*String jym = serialNum + deptCode.substring(0, 6) + "#" + number.getEvaluResult() + "#"
				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveEvaluationInfo(serialNum, number.getEvaluResult(), codeService.jm(jym));
			System.out.println("上传评价信息结果：[" + operNum + "][" + number.getSerialNum() + "][" + result[1] + "]");
			//更新6合1流水号
			if (result.length>0 && result[1].split("#").length==4) {
				if (null != result[1].split("#")[3] || !result[1].split("#")[3].equals("")) {
					System.out.println("流水号为："+result[1].split("#")[3]);
					Number numberVO  = new Number();
					numberVO.setId(number.getId());
					numberVO.setLsh(result[1].split("#")[3]);
					numberDao.updatelsh(numberVO);
					numberDao.updateGQlsh(numberVO);
				}
			}else{
				//评价不成功调用过号
				jym = serialNum + deptCode.substring(0, 6) + "#0#"+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
				result = TrffUtils.saveEvaluationInfo(serialNum, "0", codeService.jm(jym));
			}*/ 
			//调用评价接口 end
			System.out.println("上传评价信息六合一返回结果result1[0]="+result[0]);
			if ("2".equals(result[0])) {
				throw new TrffException(result[1]);
			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
				throw new Exception(result[1]);
			}
		}else if ("0".equals(isUseInterface) && "1".equals(jklx)) {
			String[] result = TrffUtils.pjxxxr(qhxxxlh, pjlb, pjjg, fqpjsj, pjsj,deptCode,sbkzjsjip);
			System.out.println("上传评价结果="+result[0]+"评价顺序号="+number.getSerialNum());
		}
		
		numberDao.updateNumber(number);
		System.out.println("评价时移除缓存前号码状态="+number.getStatus());
		NumberManager.getInstance().removeCallNumber(number.getOperNum());
		cacheManager.updateLoginIpStatus(number.getOperNum());
		
		
		if (!"1".equals(screen.getShowNum())) {
			if (screen.getWindowGDContent() != null) {
				screen.setContent2(screen.getWindowGDContent());
			}else{
				screen.setContent2(" ");
			}
		}
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
//		LEDUtils.sendText(screen, null, null, false);
		String resultVO = "";
		if("0".equals(screen.getIsgd()) && null != screen.getWindowGDContent()){
			resultVO = LEDUtils.sendGunDongContent(screen);//发送滚动信息
			java.lang.System.out.println("评价时滚动字幕发送结果："+resultVO);
		}else{
		java.lang.System.out.println("LED设置高=="+screen.getLed().getHeight()+"LED设置宽=="+screen.getLed().getWidth()+"窗口设置高=="+screen.getLedWindowHeight()+"窗口设置宽=="+screen.getLedWindowWidth());
		//初始化窗口发屏
			resultVO = LEDUtils.sendText(screen, null, null, false);
			//电视屏发屏
			if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
				String ckip = screen.getCkip();
				if ("".equals(ckip) || null == ckip) {
					ckip = "127.0.0.1";
				}
				String ckid = screen.getWindowId();
				if (screen.getWindowId().length() == 1) {
					ckid = "0"+screen.getWindowId();
				}
				String ckmc = screen.getContent();
				String sxh = screen.getContent2();
				if (sxh.length()>5) {
					sxh = sxh.substring(0,5);
				}
				publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
			}
		}
		
		//添加日志
		Number numberVo = new Number();
		numberVo.setId(number.getId());
		List<Number> listVo= numberDao.getValueRecordAllById(numberVo);
			if(listVo.size()>0){
			listVo.get(0).setEndTime(listVo.get(0).getEndtime());
			listVo.get(0).setDeptCode(deptCode);
			listVo.get(0).setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(listVo.get(0));
			
			//删除综合屏叫号信息
			zhpcallnumDao.delZhpcallnum(listVo.get(0).getId());
		}
		
		System.out.println("评价结果：[" + operNum + "][" + number.getSerialNum() + "][完成评价]");
		//判断是否有下一级队列，如果有，添加到表中
		Number numVo = new Number();
		numVo.setDeptCode(deptCode);
		numVo.setDeptHall(deptHall);
		numVo.setOperNum("");
		numVo.setId(number.getId());
		numVo.setCode("");
		List<Number> list=numberDao.searchCurrentDayNumber(numVo);
		if(null != list && list.size()>0){
			Number queueNumber = list.get(0);
			if(null != queueNumber.getNextQueueName() && null != queueNumber.getNextType()){
				//顺序号说明： N1为开始第一级  N：代表next 每次叫到同一号N的值会加1 例如： N1 → N2  号码 3202000004N1AD0001 变为 3202000004N2AD0001
				String serialNum ="";
				if(queueNumber.getSerialNum().substring(10,12).indexOf("N")>=0){
					String value = queueNumber.getSerialNum().substring(11,12);
					serialNum = "N"+(Integer.parseInt(value)+1);
				}else{
					String jq = queueNumber.getSerialNum().substring(10,12);
					serialNum = jq.replace(jq, "N1");
				}
				// 上传六合一
				if ("0".equals(isUseInterface) && "0".equals(jklx)) {
					//上传下一级队列信息
					String jym = serialNum + deptCode.substring(0, 6) + "#"
							+ number.getFlag() + "#" + number.getIDType() + "#"
							+ number.getIDNumber() + "#" + number.getServerIp() + "#"
							+ number.getPersonType() + "#" + number.getBusinessCount() + "#"
							+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
					String[] result1 = TrffUtils.saveFetchNumberInfo(serialNum, number.getFlag(), number.getIDType(),
						number.getIDNumber(), number.getServerIp(), number.getPersonType(), number.getBusinessCount(),
						codeService.jm(jym));
					System.out.println("上传下一级队列信息结果：[" + serialNum + "][" + result1[1] + "]");
					System.out.println("上传下一级队列返回结果result1[0]="+result1[0]);
					if ("2".equals(result1[0])) {
						throw new TrffException(result1[1]);
					} else if (!"1".equals(result1[0]) && !"2".equals(result1[0])) {
						throw new Exception(result1[1]);
					}
				}
				queueNumber.setSerialNum(queueNumber.getSerialNum().substring(0,10) + serialNum + deptHall + number.getSerialNum());
				queueNumber.setQueueCode(queueNumber.getNextQueueName());
				queueNumber.setBusinessType(queueNumber.getNextType());
				queueNumber.setBusinessPrefix(queueNumber.getNextPreNum());
				queueNumber.setStatus("1");
				String pattern = "yyyy-MM-dd HH:mm:ss SSS";
				queueNumber.setEnterTime(DateUtils.dateToString(pattern));
				queueNumber.setDeptCode(deptCode);
				queueNumber.setDeptHall(deptHall);
				this.numberDao.saveNumber(queueNumber);
				//更新缓存
				String regex = deptCode + deptHall;
				queueNumber.setSerialNum(number.getSerialNum().replaceAll(regex, ""));
				NumberManager.getInstance().saveNewNumber(queueNumber);
				publisher.publish(new ModifyWindowCountEvent(queueNumber.getBusinessType() + "@+"));
			}
		}
		//定时警告 0 启用 1 不启用
		/*String isOpenJgts = cacheManager.getSystemConfig("isOpenJgts");
		String jgtsTime = cacheManager.getSystemConfig("jgtsTime");
		String swkssz=cacheManager.getSystemConfig("swkssz");
		String swksfz=cacheManager.getSystemConfig("swksfz");
		String swjssz=cacheManager.getSystemConfig("swjssz");
		String swjsfz=cacheManager.getSystemConfig("swjsfz");
		String xwkssz=cacheManager.getSystemConfig("xwkssz");
		String xwksfz=cacheManager.getSystemConfig("xwksfz");
		String xwjssz=cacheManager.getSystemConfig("xwjssz");
		String xwjsfz=cacheManager.getSystemConfig("xwjsfz");
		if("0".equals(isOpenJgts)){
			WarningTimerTask warningTimerTask = new WarningTimerTask(deptCode, deptHall,number.getOperNum(),loginIP,jgtsTime,publisher,
					swkssz,swksfz,swjssz,swjsfz,xwkssz,xwksfz,xwjssz,xwjsfz
					);
		}*/
		//查询等待人数
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberDao.showWaitRs(countnum);
		String nameAndShul="";
		int AllSum=0;
		if(null != numbersCountList && numbersCountList.size()>0){
			for (Number nu : numbersCountList) {
				if(null != nu.getTypeCount()){
					AllSum += Integer.parseInt(nu.getTypeCount());
				}
				nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
			}
		}
		
//		publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		//发送滚动信息
//		if("0".equals(screen.getLed().getIsOpenGunDong())){
//			SendGunDondContetTimerTask sendGunDongTimerTask = new SendGunDondContetTimerTask(screen,operNum);
//		}
	}
	
	public void evaluationForQueueOut(Number number, String loginIP) throws Exception {
		String flag = number.getStatus();
		CacheManager cacheManager = CacheManager.getInstance();
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");
		if ("1".equals(flag)) {
			number.setStatus("6");
		} else if ("2".equals(flag)) {
			number.setStatus("5");
			Screen screen = cacheManager.getWindow(loginIP);
			number.setEvaluResult(screen.getValuemust());
		}
		
		String operNum = number.getOperNum();
		Screen screen = cacheManager.getWindow(loginIP);
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		
		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) && "0".equals(screen.getOpenInterFace()) && "0".equals(cacheManager.getSystemConfig("jklx"))) {
			//上传过号信息
			String serialNum = deptCode + deptHall + number.getSerialNum();
			String jym = serialNum + deptCode.substring(0, 6) + "#0#"
				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveEvaluationInfo(serialNum, "0", codeService.jm(jym));
			System.out.println("外部上传过号信息结果：[" + operNum + "][" + number.getSerialNum() + "][" + result[1] + "]");
			//更新6合1流水号
			/*System.out.println("流水号为："+result[3]);
			if (null != result[3] || !result[3].equals("")) {
				Number numberVO  = new Number();
				numberVO.setId(number.getId());
				//numberVO.setId(searchNumber.getId());
				numberVO.setLsh(result[3]);
				numberDao.updatelsh(numberVO);
			}*/
			System.out.println("外部上传过号信息六合一返回结果result1[0]="+result[0]);
			if ("2".equals(result[0])) {
				throw new TrffException(result[1]);
			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
				throw new Exception(result[1]);
			}
		}
		
//		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) && "0".equals(screen.getOpenInterFace())) {
//			
//			//上传评价信息
//			String serialNum = deptCode + deptHall + number.getSerialNum();
//			String jym = serialNum + deptCode.substring(0, 6) + "#" + number.getEvaluResult() + "#"
//				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
//			String[] result = TrffUtils.saveEvaluationInfo(serialNum, number.getEvaluResult(), codeService.jm(jym));
//			System.out.println("上传评价信息结果：[" + operNum + "][" + number.getSerialNum() + "][" + result[1] + "]");
//			//更新6合1流水号
//			if (result.length>0 && result[1].split("#").length==4) {
//				if (null != result[1].split("#")[3] || !result[1].split("#")[3].equals("")) {
//					System.out.println("流水号为："+result[1].split("#")[3]);
//					Number numberVO  = new Number();
//					numberVO.setId(number.getId());
//					numberVO.setLsh(result[1].split("#")[3]);
//					numberDao.updatelsh(numberVO);
//					numberDao.updateGQlsh(numberVO);
//				}
//			}
//			System.out.println("上传评价信息六合一返回结果result1[0]="+result[0]);
//			if ("2".equals(result[0])) {
//				throw new TrffException(result[1]);
//			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
//				throw new Exception(result[1]);
//			}
//		}
		
		numberDao.updateNumber(number);
		System.out.println("评价时移除缓存前号码状态="+number.getStatus());
		if(!"2".equals(Pjflag)){
			NumberManager.getInstance().removeCallNumber(number.getOperNum());
		}
		cacheManager.updateLoginIpStatus(number.getOperNum());
		if (!"1".equals(screen.getShowNum())) {
			if (screen.getWindowGDContent() != null) {
				screen.setContent2(screen.getWindowGDContent());
			}else{
				screen.setContent2(" ");
			}
		}
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
//		LEDUtils.sendText(screen, null, null, false);
		String resultVO = "";
		if("0".equals(screen.getIsgd()) && null != screen.getWindowGDContent()){
			resultVO = LEDUtils.sendGunDongContent(screen);//发送滚动信息
			java.lang.System.out.println("评价时滚动字幕发送结果："+resultVO);
		}else{
		java.lang.System.out.println("LED设置高=="+screen.getLed().getHeight()+"LED设置宽=="+screen.getLed().getWidth()+"窗口设置高=="+screen.getLedWindowHeight()+"窗口设置宽=="+screen.getLedWindowWidth());
		//初始化窗口发屏
			resultVO = LEDUtils.sendText(screen, null, null, false);
			//电视屏发屏
			if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
				String ckip = screen.getCkip();
				if ("".equals(ckip) || null == ckip) {
					ckip = "127.0.0.1";
				}
				String ckid = screen.getWindowId();
				if (screen.getWindowId().length() == 1) {
					ckid = "0"+screen.getWindowId();
				}
				String ckmc = screen.getContent();
				String sxh = screen.getContent2();
				if (sxh.length()>5) {
					sxh = sxh.substring(0,5);
				}
				publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
			}
		}
		
		//添加日志
		Number numberVo = new Number();
		numberVo.setId(number.getId());
		List<Number> listVo= numberDao.getValueRecordAllById(numberVo);
			if(listVo.size()>0){
			listVo.get(0).setEndTime(listVo.get(0).getEndtime());
			listVo.get(0).setDeptCode(deptCode);
			listVo.get(0).setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(listVo.get(0));
			
			//删除综合屏叫号信息
			zhpcallnumDao.delZhpcallnum(listVo.get(0).getId());
		}
		
		System.out.println("评价结果：[" + operNum + "][" + number.getSerialNum() + "][完成评价]");
		//判断是否有下一级队列，如果有，添加到表中
		Number numVo = new Number();
		numVo.setDeptCode(deptCode);
		numVo.setDeptHall(deptHall);
		numVo.setOperNum("");
		numVo.setId(number.getId());
		numVo.setCode("");
		List<Number> list=numberDao.searchCurrentDayNumber(numVo);
		if(null != list && list.size()>0){
			Number queueNumber = list.get(0);
			if(null != queueNumber.getNextQueueName() && null != queueNumber.getNextType()){
				//顺序号说明： N1为开始第一级  N：代表next 每次叫到同一号N的值会加1 例如： N1 → N2  号码 3202000004N1AD0001 变为 3202000004N2AD0001
				String serialNum ="";
				if(queueNumber.getSerialNum().substring(10,12).indexOf("N")>=0){
					String value = queueNumber.getSerialNum().substring(11,12);
					serialNum = "N"+(Integer.parseInt(value)+1);
				}else{
					String jq = queueNumber.getSerialNum().substring(10,12);
					serialNum = jq.replace(jq, "N1");
				}
				// 上传六合一
				if ("0".equals(isUseInterface) && "0".equals(cacheManager.getSystemConfig("jklx"))) {
					//上传下一级队列信息
					String jym = serialNum + deptCode.substring(0, 6) + "#"
							+ number.getFlag() + "#" + number.getIDType() + "#"
							+ number.getIDNumber() + "#" + number.getServerIp() + "#"
							+ number.getPersonType() + "#" + number.getBusinessCount() + "#"
							+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
					String[] result1 = TrffUtils.saveFetchNumberInfo(serialNum, number.getFlag(), number.getIDType(),
						number.getIDNumber(), number.getServerIp(), number.getPersonType(), number.getBusinessCount(),
						codeService.jm(jym));
					System.out.println("上传下一级队列信息结果：[" + serialNum + "][" + result1[1] + "]");
					System.out.println("上传下一级队列返回结果result1[0]="+result1[0]);
					if ("2".equals(result1[0])) {
						throw new TrffException(result1[1]);
					} else if (!"1".equals(result1[0]) && !"2".equals(result1[0])) {
						throw new Exception(result1[1]);
					}
				}
				queueNumber.setSerialNum(queueNumber.getSerialNum().substring(0,10) + serialNum + deptHall + number.getSerialNum());
				queueNumber.setQueueCode(queueNumber.getNextQueueName());
				queueNumber.setBusinessType(queueNumber.getNextType());
				queueNumber.setBusinessPrefix(queueNumber.getNextPreNum());
				queueNumber.setStatus("1");
				String pattern = "yyyy-MM-dd HH:mm:ss SSS";
				queueNumber.setEnterTime(DateUtils.dateToString(pattern));
				queueNumber.setDeptCode(deptCode);
				queueNumber.setDeptHall(deptHall);
				this.numberDao.saveNumber(queueNumber);
				//更新缓存
				String regex = deptCode + deptHall;
				queueNumber.setSerialNum(number.getSerialNum().replaceAll(regex, ""));
				NumberManager.getInstance().saveNewNumber(queueNumber);
				publisher.publish(new ModifyWindowCountEvent(queueNumber.getBusinessType() + "@+"));
			}
		}
		//定时警告 0 启用 1 不启用
		/*String isOpenJgts = cacheManager.getSystemConfig("isOpenJgts");
		String jgtsTime = cacheManager.getSystemConfig("jgtsTime");
		String swkssz=cacheManager.getSystemConfig("swkssz");
		String swksfz=cacheManager.getSystemConfig("swksfz");
		String swjssz=cacheManager.getSystemConfig("swjssz");
		String swjsfz=cacheManager.getSystemConfig("swjsfz");
		String xwkssz=cacheManager.getSystemConfig("xwkssz");
		String xwksfz=cacheManager.getSystemConfig("xwksfz");
		String xwjssz=cacheManager.getSystemConfig("xwjssz");
		String xwjsfz=cacheManager.getSystemConfig("xwjsfz");
		if("0".equals(isOpenJgts)){
			WarningTimerTask warningTimerTask = new WarningTimerTask(deptCode, deptHall,number.getOperNum(),loginIP,jgtsTime,publisher,
					swkssz,swksfz,swjssz,swjsfz,xwkssz,xwksfz,xwjssz,xwjsfz
					);
		}*/
		//查询等待人数
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberDao.showWaitRs(countnum);
		String nameAndShul="";
		int AllSum=0;
		if(null != numbersCountList && numbersCountList.size()>0){
			for (Number nu : numbersCountList) {
				if(null != nu.getTypeCount()){
					AllSum += Integer.parseInt(nu.getTypeCount());
				}
				nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
			}
		}
		
//		publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		//发送滚动信息
//		if("0".equals(screen.getLed().getIsOpenGunDong())){
//			SendGunDondContetTimerTask sendGunDongTimerTask = new SendGunDondContetTimerTask(screen,operNum);
//		}
	}
	
	
	@Override
	public String hangup(String operNum, String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能挂起");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if(null!=searchNumber){
			System.out.println("shunxuhao:"+searchNumber.getSerialNum());
		}
		if (null == searchNumber) {
			return "未叫号，不能挂起";
		}
	
		if ("4".equals(searchNumber.getStatus())) {
			return "正在评价，请耐心等待";
		}
		
		Screen screen = cacheManager.getWindow(loginIP);
		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) && "0".equals(screen.getOpenInterFace()) && "0".equals(cacheManager.getSystemConfig("jklx"))) {
			//上传挂起信息
			
			String serialNum = deptCode + deptHall + searchNumber.getSerialNum();
			String jym = serialNum + deptCode.substring(0, 6) + "#" + operNum + "#02#"
				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveCallInfo(serialNum, operNum, "02", codeService.jm(jym));
			System.out.println("上传挂起信息结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][" + result[1] + "]");
			System.out.println("上传挂起信息六合一返回结果result1[0]="+result[0]);
			if ("2".equals(result[0])) {
				throw new TrffException(result[1]);
			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
				throw new Exception(result[1]);
			}
		}
		
		Number number = new Number();
		number.setStatus("7");
		number.setId(searchNumber.getId());
		numberDao.updateNumber(number);
		//梧州挂起推送上屏
		List<Number> list = numberDao.getNumberViaId(searchNumber.getId());
		String str = list.get(0).getClientno();
		String strings = str.substring(13)+"@"+list.get(0).getBarId();
		publisher.publish(new GuaQiTuiSong(strings));
		
		
		number = null;
		System.out.println("挂起时移除缓存前号码状态="+searchNumber.getStatus());
		numberManager.removeCallNumber(operNum);
		if (!"1".equals(screen.getShowNum())) {
			if (screen.getWindowGDContent() != null) {
				screen.setContent2(screen.getWindowGDContent());
			}else{
				screen.setContent2(" ");
			}
		}
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
//		LEDUtils.sendText(screen, null, null, false);
		
		String resultVO = "";
		if("0".equals(screen.getIsgd()) && null != screen.getWindowGDContent()){
			resultVO = LEDUtils.sendGunDongContent(screen);//发送滚动信息
			java.lang.System.out.println("过号时滚动字幕发送结果："+resultVO);
		}else{
		java.lang.System.out.println("LED设置高=="+screen.getLed().getHeight()+"LED设置宽=="+screen.getLed().getWidth()+"窗口设置高=="+screen.getLedWindowHeight()+"窗口设置宽=="+screen.getLedWindowWidth());
		//初始化窗口发屏
			resultVO = LEDUtils.sendText(screen, null, null, false);
			//电视屏发屏
			if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
				String ckip = screen.getCkip();
				if ("".equals(ckip) || null == ckip) {
					ckip = "127.0.0.1";
				}
				String ckid = screen.getWindowId();
				if (screen.getWindowId().length() == 1) {
					ckid = "0"+screen.getWindowId();
				}
				String ckmc = screen.getContent();
				String sxh = screen.getContent2();
				if (sxh.length()>5) {
					sxh = sxh.substring(0,5);
				}
				publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
			}
		}
		
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		String datas = employee.getWSIp() +"@"+loginIP;
		publisher.publish(new PassEvent(datas));
		//添加挂起记录
		Number num = new Number();
		num.setId(searchNumber.getId());
		Number numbers = numberDao.getValueRecordAllById(num).get(0);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beginTime = df.format(new Date());
		numbers.setSerialNum(cacheManager.getOfDeptCache("deptCode")+cacheManager.getOfDeptCache("deptHall")+searchNumber.getSerialNum());
		numbers.setStatus("7");
		numbers.setBeginTime(beginTime);
		numberDao.insertValueRecordGq(numbers);
		System.out.println("挂起结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][挂起成功@1]");
		//定时警告 0 启用 1 不启用
		/*String isOpenJgts = cacheManager.getSystemConfig("isOpenJgts");
		String jgtsTime = cacheManager.getSystemConfig("jgtsTime");
		String swkssz=cacheManager.getSystemConfig("swkssz");
		String swksfz=cacheManager.getSystemConfig("swksfz");
		String swjssz=cacheManager.getSystemConfig("swjssz");
		String swjsfz=cacheManager.getSystemConfig("swjsfz");
		String xwkssz=cacheManager.getSystemConfig("xwkssz");
		String xwksfz=cacheManager.getSystemConfig("xwksfz");
		String xwjssz=cacheManager.getSystemConfig("xwjssz");
		String xwjsfz=cacheManager.getSystemConfig("xwjsfz");
		if("0".equals(isOpenJgts)){
			WarningTimerTask warningTimerTask = new WarningTimerTask(deptCode, deptHall,operNum,loginIP,jgtsTime,publisher,
					swkssz,swksfz,swjssz,swjsfz,xwkssz,xwksfz,xwjssz,xwjsfz
					);
		}*/
		//查询等待人数
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberDao.showWaitRs(countnum);
		String nameAndShul="";
		int AllSum=0;
		if(null != numbersCountList && numbersCountList.size()>0){
			for (Number nu : numbersCountList) {
				if(null != nu.getTypeCount()){
					AllSum += Integer.parseInt(nu.getTypeCount());
				}
				nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
			}
		}
//		publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		//添加日志
		Number numberVo = new Number();
		numberVo.setId(searchNumber.getId());
		List<Number> listVo= numberDao.getValueRecordAllById(numberVo);
		if(listVo.size()>0){
			listVo.get(0).setDeptCode(deptCode);
			listVo.get(0).setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(listVo.get(0));
		}
		System.out.println("挂起结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][挂起成功@1]");
		//发送滚动信息
//		if("0".equals(screen.getLed().getIsOpenGunDong())){
//			SendGunDondContetTimerTask sendGunDongTimerTask = new SendGunDondContetTimerTask(screen,operNum);
//		}
		return "挂起成功@1";
	}
	
	public String hangup_new(Number numvo) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		
		Number number = new Number();
		number.setStatus("7");
		number.setGqsj(DateUtils.dateToString("yyyy-MM-dd HH:mm:ss"));
		number.setId(numvo.getId());
		numberDao.updateNumber(number);
		number = null;
		//添加日志
		Number numberVo = new Number();
		numberVo.setId(numvo.getId());
		List<Number> listVo= numberDao.getValueRecordAllById(numberVo);
		if(listVo.size()>0){
			listVo.get(0).setDeptCode(deptCode);
			listVo.get(0).setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(listVo.get(0));
		}
		System.out.println("挂起结果：[" + numvo.getOperNum() + "][" + numvo.getSerialNum() + "][挂起成功@1]");
		return "挂起成功@1";
	}

	@Override
	public String toHangupRecover(String operNum, String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		if (!cacheManager.isSameIP(operNum, loginIP)) {
			return "此账号已在其它窗口登录，<br/>不能挂起恢复";
		}
		
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能挂起恢复");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		
		if (cacheManager.isPause(loginIP)) {
			return "暂停受理";
		}
		
		Number num = NumberManager.getInstance().fetchCallNumber(operNum);
		if (null != num) {
			if ("2".equals(num.getStatus())) {
				return "您正在办理业务，不能挂起恢复";
			}
			if ("4".equals(num.getStatus())) {
				return "正在评价，请耐心等待";
			}
		}
		
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number numVo = new Number();
		numVo.setDeptCode(deptCode);
		numVo.setDeptHall(deptHall);
		numVo.setOperNum(operNum);
		numVo.setId("");
		numVo.setCode("");
		List<Number> numberList = searchCurrentDayNumber(numVo);
		if (numberList.isEmpty()) {
			return "没有可恢复的号";
		}
		
		JSONObject object = new JSONObject();
		for (Number number : numberList) {
			String regex = deptCode + deptHall;
			String serialNum = "";
			if(number.getSerialNum().substring(10,12).indexOf("S")>=0 || number.getSerialNum().substring(10,12).indexOf("N")>=0){
				serialNum = number.getSerialNum().substring(13,number.getSerialNum().length());
			}else{
				serialNum = number.getSerialNum().replaceAll(regex, "");
			}
			object.put(number.getId(), serialNum);
		}
		return "true" + object.toString();
	}

	@Override
	public String hangupRecover(String operNum, String loginIP, String id) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number numVo = new Number();
		numVo.setDeptCode(deptCode);
		numVo.setDeptHall(deptHall);
		numVo.setOperNum("");
		numVo.setId(id);
		numVo.setCode("");
		List<Number> numberList = searchCurrentDayNumber(numVo);
		if (numberList.isEmpty()) {
			return "恢复的号码不存在";
		}
		System.out.println("恢复的号码ip为"+operNum+"``````"+loginIP+"``````"+id);
		Number number = numberList.get(0);
		System.out.println("恢复号码="+number.getSerialNum());
		if (number.getRzdbz() == null) {number.setRzdbz("0");}
		System.out.println("人证对比值="+number.getRzdbz());
		/*if ("0".equals(cacheManager.getSystemConfig("hfhmrzdb")) && Integer.parseInt(number.getRzdbz()) < Integer.parseInt(cacheManager.getSystemConfig("rzdbckz"))) {
			return "人证对比值过低,不能恢复号码";
		}*/
		Screen screen = cacheManager.getWindow(loginIP);
		if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) && "0".equals(screen.getOpenInterFace()) && "0".equals(cacheManager.getSystemConfig("jklx"))) {
			//上传挂起恢复信息
			String jym = number.getSerialNum() + deptCode.substring(0, 6) + "#" + operNum + "#01#"
				+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
			String[] result = TrffUtils.saveCallInfo(number.getSerialNum(), operNum, "01", codeService.jm(jym));
			System.out.println("上传挂起恢复信息结果：[" + operNum + "][" + number.getSerialNum() + "][" + result[1] + "]");
			System.out.println("上传挂起恢复信息六合一返回结果result1[0]="+result[0]);
			if ("2".equals(result[0])) {
				number.setStatus("1");
				throw new TrffException(result[1]);
			} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
				number.setStatus("1");
				throw new Exception(result[1]);
			}
		}
		
		Number updateNumber = new Number();
		updateNumber.setStatus("2");
		updateNumber.setId(number.getId());
		numberDao.updateNumber(updateNumber);
		//梧州挂起恢复推送上屏
		List<Number> list = numberDao.getNumberViaId(id);
		String str = list.get(0).getClientno();
		String strings = str.substring(13)+"@"+list.get(0).getBarId()+"@"+number.getBusinessType()+"@"+"aa";
		publisher.publish(new ShowCallInfoEvent(strings));
		
		updateNumber = null;
		
		number.setStatus("2");
		String regex = deptCode + deptHall;
		String serialNum = "";
		if(number.getSerialNum().substring(10,12).indexOf("S")>=0 || number.getSerialNum().substring(10,12).indexOf("N")>=0){
			serialNum = number.getSerialNum().substring(13,number.getSerialNum().length());
		}else{
			serialNum = number.getSerialNum().replaceAll(regex, "");
		}
		number.setSerialNum(serialNum);
		NumberManager numberManager = NumberManager.getInstance();
		System.out.println("挂起恢复时号码状态="+number.getStatus());
		numberManager.saveCallNumber(operNum, number);
		String tempContent = screen.getContent2();
		if ("150300000400".equals(deptCode)||"510900000400".equals(deptCode)) {//150300000400
			if (number.getBarId().length() ==1) {
				screen.setContent2("(0"+number.getBarId()+") "+number.getSerialNum());
			}else {
				screen.setContent2("("+number.getBarId()+") "+number.getSerialNum());
			}
		}else {
			screen.setContent2(number.getSerialNum() + "号");
		}
		String pattern = "yyyy-MM-dd HH:mm:ss SSS";
		String orderDate = DateUtils.dateToString(pattern);
		String strVoice = number.getSerialNum()+"@"+screen.getWindowId()+"@"+orderDate;
		if (StringUtils.isNotEmpty(screen.getMenuAddress())) {
			strVoice += "@" + screen.getMenuAddress();
		}
		VoiceManager.getInstance().add(strVoice);
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
		LEDUtils.sendText(screen, publisher, number, true);
		//电视屏发屏
		if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
			String ckip = screen.getCkip();
			if ("".equals(ckip) || null == ckip) {
				ckip = "127.0.0.1";
			}
			String ckid = screen.getWindowId();
			if (screen.getWindowId().length() == 1) {
				ckid = "0"+screen.getWindowId();
			}
			String ckmc = screen.getContent();
			String sxh = screen.getContent2();
			if (sxh.length()>5) {
				sxh = sxh.substring(0,5);
			}
			publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
		}
		//判断是否发送窗口滚动(如： xxxx号正在办理中 )
		String isopenScreen = cacheManager.getSystemConfig("isopenScreen");
		if("0".equals(isopenScreen)){
			screenGDContetTimerTask sendGunDongTimerTask = new screenGDContetTimerTask(screen,operNum);
		}
		screen.setContent2(tempContent);
		//将所叫号推送到双屏
		NumberIdPhoto numphoto = new NumberIdPhoto();
		numphoto.setNumberId(number.getIDNumber());
		String sphoto = null;
		List<NumberIdPhoto> numphotolist =this.queryPhotoBy(numphoto);
		if (numphotolist!=null&&numphotolist.size()>0) {
			byte[] sfzPhoto=numphotolist.get(0).getSfzphoto();
			sphoto=bitmaptoString(sfzPhoto);
			System.out.println("sphoto:"+sphoto);
		}
		String isOpenIndexCamera=cacheManager.getSystemConfig("isOpenIndexCamera");
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		String datas = employee.getWSIp() + "@" + number.getSerialNum()+":"+number.getId()+":"+isOpenIndexCamera+":"+sphoto;
		publisher.publish(new DualScreenEvent(datas));
		
		//添加日志
		Number numberVo = new Number();
		numberVo.setId(number.getId());
		String clinentno=deptCode+deptHall+serialNum;
		numberDao.guaQiUp(clinentno,number.getId(),deptCode,deptHall);
		List<Number> listVo= numberDao.getValueRecordAllById(numberVo);
		if (listVo.size()>0) {
			Number num = listVo.get(0);
			num.setStatus("8");
			num.setDeptCode(deptCode);
			num.setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(num);
		}
		
		System.out.println("挂起恢复结果：[" + operNum + "][" + number.getSerialNum() + "][挂起恢复成功]");
		return "挂起恢复成功";
	}
	
	public String hangupRecover_new(String operNum, String loginIP, String id) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String jklx = cacheManager.getSystemConfig("jklx");
		Number numVo = new Number();
		numVo.setDeptCode(deptCode);
		numVo.setDeptHall(deptHall);
		numVo.setOperNum("");
		numVo.setId(id);
		numVo.setCode("");
		List<Number> numberList = searchCurrentDayNumber(numVo);
		if (numberList.isEmpty()) {
			return "恢复的号码不存在";
		}
		System.out.println("恢复的号码ip为"+operNum+"``````"+loginIP+"``````"+id);
		Number number = numberList.get(0);
		System.out.println("恢复号码="+number.getSerialNum());
		if (number.getRzdbz() == null) {number.setRzdbz("0");}
		System.out.println("人证对比值="+number.getRzdbz());
		/*if ("0".equals(cacheManager.getSystemConfig("hfhmrzdb")) && Integer.parseInt(number.getRzdbz()) < Integer.parseInt(cacheManager.getSystemConfig("rzdbckz"))) {
			return "人证对比值过低,不能恢复号码";
		}*/
		Screen screen = cacheManager.getWindow(loginIP);
		
		Number updateNumber = new Number();
		updateNumber.setStatus("2");
		updateNumber.setId(number.getId());
		numberDao.updateNumber(updateNumber);
		//梧州挂起恢复推送上屏
		List<Number> list = numberDao.getNumberViaId(id);
		String str = list.get(0).getClientno();
		String strings = str.substring(13)+"@"+list.get(0).getBarId()+"@"+number.getBusinessType()+"@"+"aa";
		publisher.publish(new ShowCallInfoEvent(strings));
		
		updateNumber = null;
		
		number.setStatus("2");
		String regex = deptCode + deptHall;
		String serialNum = "";
		if(number.getSerialNum().substring(10,12).indexOf("S")>=0 || number.getSerialNum().substring(10,12).indexOf("N")>=0){
			serialNum = number.getSerialNum().substring(13,number.getSerialNum().length());
		}else{
			serialNum = number.getSerialNum().replaceAll(regex, "");
		}
		number.setSerialNum(serialNum);
		NumberManager numberManager = NumberManager.getInstance();
		System.out.println("挂起恢复时号码状态="+number.getStatus());
		numberManager.saveCallNumber(operNum, number);
		String tempContent = screen.getContent2();
		if ("150300000400".equals(deptCode)||"510900000400".equals(deptCode)) {//150300000400
			if (number.getBarId().length() ==1) {
				screen.setContent2("(0"+number.getBarId()+") "+number.getSerialNum());
			}else {
				screen.setContent2("("+number.getBarId()+") "+number.getSerialNum());
			}
		}else {
			screen.setContent2(number.getSerialNum() + "号");
		}
		String pattern = "yyyy-MM-dd HH:mm:ss SSS";
		String orderDate = DateUtils.dateToString(pattern);
		String strVoice = number.getSerialNum()+"@"+screen.getWindowId()+"@"+orderDate;
		if (StringUtils.isNotEmpty(screen.getMenuAddress())) {
			strVoice += "@" + screen.getMenuAddress();
		}
		VoiceManager.getInstance().add(strVoice);
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
		LEDUtils.sendText(screen, publisher, number, true);
		//电视屏发屏
		if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
			String ckip = screen.getCkip();
			if ("".equals(ckip) || null == ckip) {
				ckip = "127.0.0.1";
			}
			String ckid = screen.getWindowId();
			if (screen.getWindowId().length() == 1) {
				ckid = "0"+screen.getWindowId();
			}
			String ckmc = screen.getContent();
			String sxh = screen.getContent2();
			if (sxh.length()>5) {
				sxh = sxh.substring(0,5);
			}
			publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
		}
		//判断是否发送窗口滚动(如： xxxx号正在办理中 )
		String isopenScreen = cacheManager.getSystemConfig("isopenScreen");
		if("0".equals(isopenScreen)){
			screenGDContetTimerTask sendGunDongTimerTask = new screenGDContetTimerTask(screen,operNum);
		}
		screen.setContent2(tempContent);
		//将所叫号推送到双屏
		String isOpenIndexCamera=cacheManager.getSystemConfig("isOpenIndexCamera");
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		String datas = employee.getWSIp() + "@" + number.getSerialNum()+":"+number.getId()+":"+isOpenIndexCamera;
		publisher.publish(new DualScreenEvent(datas));
		
		//添加日志
		Number numberVo = new Number();
		numberVo.setId(number.getId());
		String clinentno=deptCode+deptHall+serialNum;
		numberDao.guaQiUp(clinentno,number.getId(),deptCode,deptHall);
		List<Number> listVo= numberDao.getValueRecordAllById(numberVo);
		if (listVo.size()>0) {
			Number num = listVo.get(0);
			num.setStatus("8");
			num.setDeptCode(deptCode);
			num.setDeptHall(deptHall);
			numberDao.insertValuerecordRZ(num);
		}
		
		System.out.println("挂起恢复结果：[" + operNum + "][" + number.getSerialNum() + "][挂起恢复成功]");
		if ("1".equals(jklx) && "0".equals(cacheManager.getSystemConfig("isUseInterface"))) {
			String qhrxm = "";
			String dlrsfzmhm ="";
			String flag = "01";
			if (!"".equals(number.getNameB()) && null != number.getNameB()) {
				qhrxm = number.getNameB();
			}else if (!"".equals(number.getNameA()) && null != number.getNameA()) {
				qhrxm = number.getNameA();
			}
			if (!"".equals(number.getIDNumberB()) && null != number.getIDNumberB()) {
				dlrsfzmhm = number.getIDNumberB();
			}
			if (!"".equals(number.getFlag()) && null != number.getFlag()) {
				flag = number.getFlag();
			}
			return "2@"+number.getSerialNum()+"@"+flag+"@"+number.getIDNumber()+"@"+dlrsfzmhm+"@"+qhrxm+"@"+number.getEnterTime();
		}
		return "挂起恢复成功";
	}
	
	public String toChangeWin(String operNum, String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能流转");
		if (StringUtils.isNotEmpty(ucr)) {
			return ucr;
		}
		
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if (null == searchNumber) {
			return "未叫号，不能流转";
		}
		
		
		if ("4".equals(searchNumber.getStatus())) {
			return "正在评价，请耐心等待";
		}
		
		//根据业务类型查找有此业务的所有窗口
		List<Screen> dataList = setWindowDao.getWindowList(deptCode, deptHall, "", "", "",searchNumber.getBusinessType(),"");

		JSONObject object = new JSONObject();
		for (Screen screen : dataList) {
			object.put(String.valueOf(screen.getAddress()), screen.getAddress()+"号窗口");
		}
		return "true" + object.toString();
	}
	
	public String transferenceNumber(String operNum, String loginIP, String address) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");
		
		//更新状态为过号
		Number number = new Number();
		number.setId(searchNumber.getId());
		number.setStatus("3");
		number.setEndTime(DateUtils.dateToString());
		numberDao.updateNumber(number);
		number = null;
		Screen screen = cacheManager.getWindow(loginIP);
		numberManager.removeCallNumber(operNum);
		cacheManager.updateLoginIpStatus(operNum);
		if (!"1".equals(screen.getShowNum())) {
			if (screen.getWindowGDContent() != null) {
				screen.setContent2(screen.getWindowGDContent());
			}else{
				screen.setContent2(" ");
			}
		}
		screen.setAddress(address);
		if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
			screen.getLed().setHeight(screen.getLedWindowHeight());
		}
		if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
			screen.getLed().setWidth(screen.getLedWindowWidth());
		}
//		LEDUtils.sendText(screen, null, null, false);
		
		String resultVO = "";
		if("0".equals(screen.getIsgd()) && null != screen.getWindowGDContent()){
			resultVO = LEDUtils.sendGunDongContent(screen);//发送滚动信息
			java.lang.System.out.println("过号时滚动字幕发送结果："+resultVO);
		}else{
		java.lang.System.out.println("LED设置高=="+screen.getLed().getHeight()+"LED设置宽=="+screen.getLed().getWidth()+"窗口设置高=="+screen.getLedWindowHeight()+"窗口设置宽=="+screen.getLedWindowWidth());
		//初始化窗口发屏
			resultVO = LEDUtils.sendText(screen, null, null, false);
			//电视屏发屏
			if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
				String ckip = screen.getCkip();
				if ("".equals(ckip) || null == ckip) {
					ckip = "127.0.0.1";
				}
				String ckid = screen.getWindowId();
				if (screen.getWindowId().length() == 1) {
					ckid = "0"+screen.getWindowId();
				}
				String ckmc = screen.getContent();
				String sxh = screen.getContent2();
				if (sxh.length()>5) {
					sxh = sxh.substring(0,5);
				}
				publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
			}
		}
		
		Employee employee=iEmployeeService.getEmployeeByCode(operNum);
		String datas = employee.getWSIp() +"@"+loginIP;
		publisher.publish(new PassEvent(datas));
		
		
		//将新号码添加到流转的窗口
		Number numVo = new Number();
		numVo.setDeptCode(deptCode);
		numVo.setDeptHall(deptHall);
		numVo.setOperNum("");
		numVo.setId(searchNumber.getId());
		numVo.setCode("");
		List<Number> list=numberDao.searchCurrentDayNumber(numVo);
		if(null != list && list.size()>0){
			Number queueNumber = list.get(0);
			String serialNum ="";
			if(queueNumber.getSerialNum().substring(10,12).indexOf("S")>=0){
				String value = queueNumber.getSerialNum().substring(11,12);
				serialNum = "S"+(Integer.parseInt(value)+1);
			}else{
				String jq = queueNumber.getSerialNum().substring(10,12);
				serialNum = jq.replace(jq, "S1");
				
			}
			// 上传六合一
			if ("0".equals(isUseInterface) && "0".equals(cacheManager.getSystemConfig("jklx"))) {
				//上传流转号码信息
				String jym = serialNum + deptCode.substring(0, 6) + "#"
						+ number.getFlag() + "#" + number.getIDType() + "#"
						+ number.getIDNumber() + "#" + number.getServerIp() + "#"
						+ number.getPersonType() + "#" + number.getBusinessCount() + "#"
						+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
				String[] result1 = TrffUtils.saveFetchNumberInfo(serialNum, number.getFlag(), number.getIDType(),
					number.getIDNumber(), number.getServerIp(), number.getPersonType(), number.getBusinessCount(),
					codeService.jm(jym));
				System.out.println("上传流转号码信息结果：[" + serialNum + "][" + result1[1] + "]");
				System.out.println("上传流转号码返回结果result1[0]="+result1[0]);
				if ("2".equals(result1[0])) {
					throw new TrffException(result1[1]);
				} else if (!"1".equals(result1[0]) && !"2".equals(result1[0])) {
					throw new Exception(result1[1]);
				}
			}
			queueNumber.setSerialNum(queueNumber.getSerialNum().substring(0,10) + serialNum + deptHall + searchNumber.getSerialNum());
			queueNumber.setStatus("1");
			queueNumber.setDeptCode(deptCode);
			queueNumber.setDeptHall(deptHall);
			this.numberDao.saveNumber(queueNumber);
			queueNumber.setBarId(address);
			this.numberDao.updateNumber(queueNumber);
			this.numberDao.insertValueRecardLz(queueNumber);
			//更新缓存
			queueNumber.setSerialNum(searchNumber.getSerialNum());
			NumberManager.getInstance().saveNewNumber(queueNumber);
		}
		
		System.out.println("流转号码结果：[" + operNum + "][" + searchNumber.getSerialNum() + "][流转号码成功@1]");
		//查询大厅等待人数
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberDao.showWaitRs(countnum);
		String nameAndShul="";
		int AllSum=0;
		if(null != numbersCountList && numbersCountList.size()>0){
			for (Number nu : numbersCountList) {
				if(null != nu.getTypeCount()){
					AllSum += Integer.parseInt(nu.getTypeCount());
				}
				nameAndShul += nu.getTypeName()+":"+nu.getTypeCount()+"人<br>";
			}
		}
		
//		publisher.publish(new ShowQuHaoSumEvent("大厅等待人数："+AllSum+"人<br>"+nameAndShul + "@"));//推送到取号外屏
		//发送滚动信息
//		if("0".equals(screen.getLed().getIsOpenGunDong())){
//			SendGunDondContetTimerTask sendGunDongTimerTask = new SendGunDondContetTimerTask(screen,operNum);
//		}
		return "流转号码成功@1";
	}
	
	public String toPause(String operNum, String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		if (!cacheManager.isSameIP(operNum, loginIP)) {
			return "此账号已在其它窗口登录，<br/>不能暂停";
		}
		
		String ucr = cacheManager.userCompare(operNum, loginIP, "不能暂停");
		if (StringUtils.isNotEmpty(ucr)) {
			return "此账号正在其它窗口办理业务，<br/>不能暂停";
		}
		
		Number num = NumberManager.getInstance().fetchCallNumber(operNum);
		if (null != num) {
			if ("2".equals(num.getStatus())) {
				return "您正在办理业务，不能暂停";
			}
			if ("4".equals(num.getStatus())) {
				return "正在评价，请耐心等待";
			}
		}
		
		List<Code> codeList = codeService.getAllGredentials("9090",deptCode,deptHall);
		JSONObject object = new JSONObject();
		for (Code code : codeList) {
			object.put(code.getDm(), code.getMc());
		}
		return "true" + object.toString();
	}

	public PrintInfo pauseOrRecover(String operNum, String loginIP, String reason) throws Exception {
		PrintInfo info = new PrintInfo();
		try{
		Number searchNumber = NumberManager.getInstance().fetchCallNumber(operNum);
		if (null == searchNumber) { //已办完
			CacheManager cacheManager = CacheManager.getInstance();
			String jklx = cacheManager.getSystemConfig("jklx");
			Screen screen = cacheManager.getWindow(loginIP);
			if (null != screen) {
				String isPause = "";
				String deptCode = cacheManager.getOfDeptCache("deptCode");
				String deptHall = cacheManager.getOfDeptCache("deptHall");
				if (StringUtils.isEmpty(reason)) {
					isPause = "0";
					screen.setPause(false);
					info.setMsg("恢复成功");
					info.setResultZT("11@1@恢复成功");
					String[] temp = cacheManager.pause(loginIP).split("@");
					reason = temp[1];
					numberDao.updatePauseOrRecover(temp[2]);
				} else {
					publisher.publish(new PauseOrRecoverEventZS(screen.getWindowId()));
					screen.setPause(true);
					info.setMsg("暂停成功，恢复之前不要关闭此窗口");
					info.setResultZT("10@1@暂停成功");
					String id = numberDao.pauseOrRecover(deptCode, deptHall, operNum, "1", reason);
					isPause = "1@" + reason + "@" + id;
				}
				
				if ("0".equals(cacheManager.getSystemConfig("isUseInterface")) 
						&& "0".equals(screen.getOpenInterFace()) && "0".equals(jklx) 
						&& "0".equals(jklx)) {
					//上传暂停/恢复信息
					String jym = operNum + deptCode.substring(0, 6) + "#" + reason + "#"
						+ DateUtils.dateToString("yyyyMMdd") + deptCode.substring(6);
					String[] result = TrffUtils.savePauseAndRecover(operNum, reason, codeService.jm(jym));
					System.out.println("上传暂停/恢复信息结果：[" + operNum + "][" + result[1] + "]");
					System.out.println("上传暂停信息六合一返回结果result1[0]="+result[0]);
					if ("2".equals(result[0])) {
						throw new TrffException(result[1]);
					} else if (!"1".equals(result[0]) && !"2".equals(result[0])) {
						throw new Exception(result[1]);
					}
				}
				
				info.setSuccess(true);
				cacheManager.setPauseCache(loginIP, isPause);
//				if (StringUtils.isEmpty(screen.getContent())) {
//					//screen.setContent2("业务受理");
//					screen.setContent2(screen.getContent2());
//				} else {
//					if (screen.getWindowGDContent() != null) {
//						screen.setContent2(screen.getWindowGDContent());
//					}else{
//						screen.setContent2(" ");
//					}
//				}
				if (!"1".equals(screen.getShowNum())) {
					if (screen.isPause()) {
						screen.setContent2(" ");
					}else{
						if (screen.getWindowGDContent() != null) {
							screen.setContent2(screen.getWindowGDContent());
						}else{
							screen.setContent2(" ");
						}
					}
				}
				if(null != screen.getLedWindowHeight() && !"0".equals(screen.getLedWindowHeight())){
					screen.getLed().setHeight(screen.getLedWindowHeight());
				}
				if(null != screen.getLedWindowWidth() && !"0".equals(screen.getLedWindowWidth())){
					screen.getLed().setWidth(screen.getLedWindowWidth());
				}
				//LEDUtils.sendText(screen, null, null, false);
				String resultVO = "";
				if("0".equals(screen.getIsgd()) && null != screen.getWindowGDContent()){
					resultVO = LEDUtils.sendGunDongContent(screen);//发送滚动信息
					java.lang.System.out.println("暂停恢复时滚动字幕发送结果："+resultVO);
				}else{
				java.lang.System.out.println("LED设置高=="+screen.getLed().getHeight()+"LED设置宽=="+screen.getLed().getWidth()+"窗口设置高=="+screen.getLedWindowHeight()+"窗口设置宽=="+screen.getLedWindowWidth());
				//初始化窗口发屏
					resultVO = LEDUtils.sendText(screen, null, null, false);
					//电视屏发屏
					if ("2".equals(cacheManager.getSystemConfig("ledxh")) && !"".equals(screen.getCkip()) && null != screen.getCkip()) {
						String ckip = screen.getCkip();
						if ("".equals(ckip) || null == ckip) {
							ckip = "127.0.0.1";
						}
						String ckid = screen.getWindowId();
						if (screen.getWindowId().length() == 1) {
							ckid = "0"+screen.getWindowId();
						}
						String ckmc = screen.getContent();
						String sxh = screen.getContent2();
						if (!"0".equals(isPause)) {
							sxh = "暂停";
						}
						publisher.publish(new ChuangKouPing(ckip+"@"+ckid+"@"+ckmc+"@"+sxh));
					}
				}
				Employee employee=iEmployeeService.getEmployeeByCode(operNum);
				publisher.publish(new PauseOrRecoverEvent(isPause.split("@")[0] + "@" + employee.getWSIp()));
			} else {
				info.setMsg("此IP未与窗口绑定，不能暂停");
				info.setResultZT("10@2@此IP未与窗口绑定，不能暂停");
			}
		} else {
			info.setMsg("您正在办理业务，不能暂停");
			info.setResultZT("10@2@您正在办理业务，不能暂停");
			}
		}catch(Exception e){
			
		}
		
		return info;
	}

	public void updateNumberByID(String id, String operNum) throws UpdateException {
		NumberManager.getInstance().fetchCallNumber(operNum).setStatus("2");
		Number number = new Number();
		number.setId(id);
		number.setStatus("2");
		numberDao.updateNumber(number);
		number = null;
	}
	/**
	 * //根据身份证获取非代理人当月取号数量
	 */
	public String getNotAgentByIdCardCount(Number numbers,String deptCode, String deptHall) throws Exception {
		String count="0";
		List<Number> list = numberDao.getNotAgentByIdCardCount(numbers,deptCode,deptHall);
		for(Number number : list){
			count = number.getBusinessCount();
		}
		return count;
	}
	/**
	 * 根据窗口ip获取窗口当前办理业务
	 */
	public Number getNumberAllByClientIP(String clientIp) throws Exception {
		List<Number> list = numberDao.getNumberAllByClientIP(clientIp);
		Number number = null;
		if(null != list && list.size()>0){
			number = list.get(0);
		}
		return number;
	}
	public Number getNumberAllByClientIPForHF(String clientIp,String sxh) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		sxh = deptCode+deptHall+sxh;
		List<Number> list = numberDao.getNumberAllByClientIPForHF(clientIp,sxh);
		Number number = null;
		if(null != list && list.size()>0){
			number = list.get(0);
		}
		return number;
	}
	/**
	 * 根据id获取取号人员所有信息
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public Number getValueRecordAllById(Number number) throws Exception {
		List<Number> list = numberDao.getValueRecordAllById(number);
		Number numbers = null;
		if(null != list && list.size()>0){
			numbers = list.get(0);
		}
		return numbers;
	}
	
	/**
	 * 获取取号人员所有信息
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecordAll(Number number) throws Exception {
		List<Number> list = numberDao.getValueRecordAllById(number);
		return list;
	}
	
	public List<Number> getValueRecordAllForAY(Number number) throws Exception {
		List<Number> list = numberDao.getValueRecordAllByIdForAY(number);
		return list;
	}
	
	public static void cleanSendContent(){
		sendContent ="";
		sendContent1 ="";
		sendContent2 ="";
		LzckAction.sendLzxxContent =""; 
	}

	public IQueueDao getQueueDao() {
		return queueDao;
	}

	public void setQueueDao(IQueueDao queueDao) {
		this.queueDao = queueDao;
	}

	/**
	 * 根据身份证ID查询当天是否取号
	 */
	public List<Number> getValueRecardbyIdnumber(String idnumber,String lsh)
			throws Exception {
		return this.numberDao.getValueRecardbyIdnumber(idnumber,lsh);
	}
	
	/**
	 * 强制叫号
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public Number forcedToCallNumber_status(String loginIP) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		//根据IP获取窗口，判断IP有没有绑定窗口
		Screen screen = cacheManager.getWindow(loginIP);
		Number number = fetchNumber(screen);
		return number;
	}

	public int updatelsh(Number number) throws UpdateException {
		return numberDao.updatelsh(number);
	}

	@Override
	public List<Number> showWaitRs(Number number) throws Exception {
		return numberDao.showWaitRs(number);
	}
	
	public List<Number> showyjrs(Number number) throws Exception {
		return numberDao.showyjrs(number);
	}

	public List<Number> getBarIdBySerialNum(Number number) throws Exception {
		return numberDao.getBarIdBySerialNum(number);
	}

	@Override
	public List<Number> getValueRecordGq() throws Exception {
		return numberDao.getValueRecordGq();
	}

	@Override
	public int updateOut1(Number number) throws UpdateException {
		return numberDao.updateOut1(number);
	}
	
	@Override
	public int updateRzdbz(Number number) throws UpdateException {
		return numberDao.updateRzdbz(number);
	}
	
	@Override
	public int updateValueRecordGqStatus(String id,String blridnumber) throws Exception {
		return numberDao.updateValueRecordGqStatus(id,blridnumber);
	}

	@Override
	public List<Number> getvaluerecordRZ(Number number, String tjms,
			String ksrq, String jsrq,String IDNumber) throws Exception {
		return numberDao.getvaluerecordRZ(number, tjms, ksrq, jsrq,IDNumber);
	}

	@Override
	public List<Number> queryAllGqBybarId(Number number) throws Exception {
		return numberDao.queryAllGqBybarId(number);
	}

	public List<Number> gqInfoList(String code,String tjms, String ksrq, String jsrq) throws Exception {
		return numberDao.gqInfoList(code,tjms, ksrq, jsrq);
	}

	@Override
	public List<Number> gqInfoDtail(String id) throws Exception {
		return numberDao.gqInfoDtail(id);
	}

	public void insertSfzPhoto(NumberIdPhoto numphoto) throws Exception {
		saveSfzPhotoDao.insertSfzPhoto(numphoto);
	}

	public List<NumberIdPhoto> queryPhotoBy(NumberIdPhoto numphoto)
		throws Exception {
	return saveSfzPhotoDao.queryPhotoBy(numphoto);
	}

	public void updateSfzPhoto(NumberIdPhoto numphoto) throws Exception {
		saveSfzPhotoDao.updateSfzPhoto(numphoto);
	}
	
	public List<Number> queryAllSfz(String tjms, String ksrq, String jsrq,String rNumber,String barId,String operNum,String IDNumber,String deptcode,String depthall)
		throws Exception {
	return numberDao.queryAllSfz(tjms, ksrq, jsrq,rNumber,barId,operNum,IDNumber,deptcode,depthall);
}

	public List<Number> countWJHshuliang(String id,String waitingarea,String businessType,String queueCode,String deptCode,String deptHall) throws Exception {
		return numberDao.countWJHshuliang(id,waitingarea,businessType,queueCode,deptCode,deptHall);
	}

	@Override
	public List<Number> detailSfz(String id) throws Exception {
		return numberDao.detailSfz(id);
	}

	@Override
	public Number getCodeByRz(Number number) throws Exception {
		List<Number> list = numberDao.getCodeByRz(number);
		Number numbers = null;
		if(null != list && list.size()>0){
			numbers = list.get(0);
		}
		return numbers;
	}

	public List<Number> getCardAndLshByAll(Number number) throws Exception {
		return numberDao.getCardAndLshByAll(number);
	}
	//保存高拍仪照片
	public String savaGpyPhoto(Number number) throws Exception {
		String upflag="0";//0成功 1本地失败
		try {
			numberDao.savaGpyPhoto(number);
		} catch (Exception e) {
			upflag = "1";
		}
		return upflag;
	}

	// 验证外屏通讯
		public String validateYz(String loginIP) throws Exception {
			String date = loginIP + "@cjs";
			publisher.publish(new DualScreenEventWP(date));
			String flag = CodeServiceImpl.YZFLAG;
			CodeServiceImpl.YZFLAG = "0";

			if ("0".equals(flag)) {
				String msg = "进入推送方法：上一行显示-页面通讯验证任务开始-则推送失败！";
				publisher.publish(new DualScreenEventFail(loginIP));
				return msg;
			} else {
				return "外屏页面通讯正常";
			}
		}
	public List<Number> countIdNumber(String tjms, String ksrq, String jsrq,
			Number number,String deptCode,String deptHall) throws Exception {
		return numberDao.countIdNumber(tjms, ksrq, jsrq, number,deptCode,deptHall);
	}

	public List<Number> getZhiWenByIdNumber(Number number) throws Exception {
		return numberDao.getZhiWenByIdNumber(number);
	}
	
	public void addZhiWen(Number number) throws Exception {
		numberDao.addZhiWen(number);
	}

	public void updateZhiWen(Number number) throws Exception {
		numberDao.updateZhiWen(number);
	}

	@Override
	public void delZhiWen(Number number) throws Exception {
		numberDao.delZhiWen(number);
		
	}

	@Override
	public List<Number> queryIdnumberDifference(String tjms, String ksrq,
			String jsrq, Number number) throws Exception {
		return numberDao.queryIdnumberDifference(tjms, ksrq, jsrq, number);
	}

	@Override
	public List<Number> checkJDCStatus(Number number) throws Exception {
		return numberDao.checkJDCStatus(number);
	}

	@Override
	public List<Number> checkJSRStatus(Number number) throws Exception {
		return numberDao.checkJSRStatus(number);
	}

	@Override
	public List<Number> yzcfqh(String idnumber) throws Exception {
		return numberDao.yzcfqh(idnumber);
	}

	@Override
	public List<Number> selectValuecord(Number number) throws Exception {
		return numberDao.selectValuecord(number);
	}
	@Override
	public String findByValuerecord(Number number) throws Exception {
		return numberDao.findByValuerecord(number);
	}

	@Override
	public String findByWait(String id,String code, String deptCode, String deptHall)
			throws Exception {
		return numberDao.findByWait(id,code,deptCode,deptHall);
	}

	@Override
	public List<Business> queryBus(Business bus) throws Exception {
		return  businessDao.queryWaitingareaByID(bus);
	}
	
	public String queueWaitTime(String code,String deptCode,String deptHall)throws Exception{
		return numberDao.queueWaitTime(code,deptCode,deptHall);
	}

	@Override
	public List<Number> dayNumber(String deptCode, String deptHall) throws Exception {
		return numberDao.dayNumber(deptCode,deptHall);
	}

	@Override
	public String showyisj(String deptCode, String deptHall)
			throws Exception {
		return numberDao.showyisj(deptCode,deptHall);
	}
	@Override
	public List<Wclh> wblh(String idnum) throws Exception {
		return numberDao.wblh(idnum);
	}

	@Override
	public List<Number> queryywlxAndddrs(Number countnum) {
		
		return numberDao.queryywlxAndddrs(countnum);
	}
	
	@Override
	public List<Number> querydllxAndddrs(Number countnum) {
		return numberDao.querydllxAndddrs(countnum);
	}


	@Override
	public int upStatusAll(Number number) throws Exception {
		return numberDao.upStatusAll(number);
	}


	@Override
	public List<Number> getNameBySXH(Number number) throws Exception {
		return numberDao.getNameBySXH(number);
	}


	@Override
	public void saveSxh(Number number) throws Exception {
		numberDao.saveSxh(number);
	}

	@Override
	public List<Number> countByIdnumber(String idnumber, String deptCode,
			String deptHall) throws Exception {
		return numberDao.countByIdnumber(idnumber, deptCode, deptHall);
	}

	@Override
	public void insertQHrzdbz(Number number) throws Exception {
		numberDao.insertQHrzdbz(number);
		
	}

	@Override
	public List<Number> queryRzdbz(String idnumber) throws Exception {
		return numberDao.queryRzdbz(idnumber);
	}

	@Override
	public String qhsfzyz(String idnumber, String deptcode, String depthall)
			throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String ret = "";
		String isyzcfqh = cacheManager.getSystemConfig("isyzcfqh");
		String isOpenwxqh = cacheManager.getSystemConfig("isOpenwxqh");
		if("1".equals(isOpenwxqh)){
			ret="未启用微信取号接口";
		}else{
			List<Number> list = null;
			Number number = new Number();
			number.setIDNumber(idnumber);
			number.setIDNumberB(idnumber);
			String qhcpunt = this.getNotAgentByIdCardCount(number,deptcode,depthall);
			String numberSet = CacheManager.getInstance().getSystemConfig("numberSet");
			if(Integer.parseInt(qhcpunt)>Integer.parseInt(numberSet)){
				ret = "本月取号次数大于最大次数，无法取号！！！";
			}else{
			    if(!"0".equals(isyzcfqh)){
			    	list = this.yzcfqh(idnumber);
			    	if(list.size()>0){
			    		ret = "您今天取的号尚未办结，请勿重复取号!";
			    	}
			    }else{
			    	
			    }
			}
		}
		return ret;
	}
	
	private boolean cardCodeVerify(String cardcode) {
	    int i = 0;
	    String r = "error";
	    String lastnumber = "";

	    i += Integer.parseInt(cardcode.substring(0, 1)) * 7;
	    i += Integer.parseInt(cardcode.substring(1, 2)) * 9;
	    i += Integer.parseInt(cardcode.substring(2, 3)) * 10;
	    i += Integer.parseInt(cardcode.substring(3, 4)) * 5;
	    i += Integer.parseInt(cardcode.substring(4, 5)) * 8;
	    i += Integer.parseInt(cardcode.substring(5, 6)) * 4;
	    i += Integer.parseInt(cardcode.substring(6, 7)) * 2;
	    i += Integer.parseInt(cardcode.substring(7, 8)) * 1;
	    i += Integer.parseInt(cardcode.substring(8, 9)) * 6;
	    i += Integer.parseInt(cardcode.substring(9, 10)) * 3;
	    i += Integer.parseInt(cardcode.substring(10,11)) * 7;
	    i += Integer.parseInt(cardcode.substring(11,12)) * 9;
	    i += Integer.parseInt(cardcode.substring(12,13)) * 10;
	    i += Integer.parseInt(cardcode.substring(13,14)) * 5;
	    i += Integer.parseInt(cardcode.substring(14,15)) * 8;
	    i += Integer.parseInt(cardcode.substring(15,16)) * 4;
	    i += Integer.parseInt(cardcode.substring(16,17)) * 2;
	    i = i % 11;
	    lastnumber =cardcode.substring(17,18);
	    if (i == 0) {
	        r = "1";
	    }
	    if (i == 1) {
	        r = "0";
	    }
	    if (i == 2) {
	        r = "x";
	    }
	    if (i == 3) {
	        r = "9";
	    }
	    if (i == 4) {
	        r = "8";
	    }
	    if (i == 5) {
	        r = "7";
	    }
	    if (i == 6) {
	        r = "6";
	    }
	    if (i == 7) {
	        r = "5";
	    }
	    if (i == 8) {
	        r = "4";
	    }
	    if (i == 9) {
	        r = "3";
	    }
	    if (i == 10) {
	        r = "2";
	    }
	    if (r.equals(lastnumber.toLowerCase())) {
	        return true;
	    }
	    return false;
	}
	
	//byte转String
	public String bitmaptoString(byte[] bytes) throws EnumConstantNotPresentException{
        StringBuffer string = new StringBuffer();
        string.append(Base64Util.encode(bytes));
        return string.toString();
    }
	
	@Override
	public List<Number> getvaluerecordByClientno(Number number)
			throws Exception {
		return numberDao.getvaluerecordByClientno(number);
	}

	@Override
	public void insertBLXX(Number number) throws Exception {
		this.numberDao.insertBLXX(number);
	}

	@Override
	public void updateWanJie(Number number) throws Exception {
		this.numberDao.updateWanJie(number);
	}

	@Override
	public List<Number> getrzdbz(String tjms, String ckip, String xm, String ksrq,
			String jsrq, Number Number) throws Exception {
		return numberDao.getrzdbz(tjms, ckip, xm, ksrq, jsrq, Number);
	}
}