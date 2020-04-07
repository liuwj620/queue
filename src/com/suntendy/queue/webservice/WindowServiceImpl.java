package com.suntendy.queue.webservice;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;

import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.service.IEmployeeService;
import com.suntendy.queue.led.domain.LED;
import com.suntendy.queue.queue.dao.INumberDao;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.service.ICodeService;
import com.suntendy.queue.queue.service.impl.CodeServiceImpl;
import com.suntendy.queue.util.DateUtils;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.led.LEDUtils;
import com.suntendy.queue.util.scriptsession.Publisher;
import com.suntendy.queue.util.scriptsession.event.DualScreenEvent;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventYZ;
import com.suntendy.queue.util.scriptsession.event.ReadDataEvent;
import com.suntendy.queue.util.scriptsession.event.ShowLzxxDataEvent;
import com.suntendy.queue.util.trff.TrffClient_wfxx;
import com.suntendy.queue.util.trff.XMLUtils_wfxx;

@WebService(endpointInterface = "com.suntendy.queue.webservice.IWindowService")
public class WindowServiceImpl implements IWindowService {
	private Publisher publisher;
	private IEmployeeService employeeService;
	private INumberDao numberDao;
	private ICodeService codeService;
	
	public void setNumberDao(INumberDao numberDao) {
		this.numberDao = numberDao;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public ICodeService getCodeService() {
		return codeService;
	}

	public void setCodeService(ICodeService codeService) {
		this.codeService = codeService;
	}

	public boolean saveOperatorInfo(String operatorInfo) {
		System.out.println("进入登陆接口");
		boolean result = false;
		Employee employee = new Employee();
		CacheManager cacheManager = CacheManager.getInstance();
		try {
			JSONObject obj = new JSONObject(operatorInfo);
			
			employee.setDeptCode(cacheManager.getOfDeptCache("deptCode"));
			employee.setDeptHall(cacheManager.getOfDeptCache("deptHall"));
			employee.setDepartment(obj.getString("operDept"));
			employee.setLoginIp(obj.getString("loginIP"));
			employee.setName(obj.getString("operName"));
			employee.setCode(obj.getString("operNum"));
			employee.setFmt_status("0");
			System.out.println("operDept=="+obj.getString("operDept")+"```loginIP=="+obj.getString("loginIP")+"```operName=="+obj.getString("operName")+"```operNum=="+obj.getString("operNum"));
			
			employeeService.save(employee);
			result = true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public boolean showScreenInfo(String loginIP, String tableTitle, String screenInfo) {
		System.out.println("窗口显示数据信息[loginIP:" + loginIP + ";tableTitle:" + tableTitle + ";screenInfo:" + screenInfo + "]");
	/*	
	 	if(tableTitle.equals("机动车")){
		 	String wfres ="";
			if(null != tableTitle && null != screenInfo){
				String[] wftj=screenInfo.split(",");
			//根据号牌种类和号牌号码查询机动车违法信息
			String[][] rows = new String[3][2];
			for(int i=0;i<wftj.length;i++){
				if("号牌种类".equals(wftj[i].split(":")[0]) && null!=wftj[i].split(":")[0] && !"--".equals(wftj[i].split(":")[0])){
					rows[0][0] = "hpzl";
					rows[0][1] = "";
				}
				if("号牌号码".equals(wftj[i].split(":")[1]) && null!=wftj[i].split(":")[1] && !"--".equals(wftj[i].split(":")[1])){
					rows[1][0] = "hphm";
					rows[1][1] = "";
				}
			}
			//wfres ="'datas':[{'fkje':'100','wfsj':'2014-10-27','wfdd':'','jkbj':'1'},{'fkje':'','wfsj':'违法时间:2014-10-27','wfdd':'十八里店南桥','jkbj':''}]";
			try {
				String strXML = XMLUtils_wfxx.createXML(XMLUtils_wfxx.TYPE_WRITE, rows);
				System.out.println("查询违法信息调用六合一封装xml内容："+strXML);
				wfres = XMLUtils_wfxx.readXML(TrffClient_wfxx.write("04C03", strXML));
	//			System.out.println("查询违法信息调用六合一返回xml内容："+ss[0]+"#"+ss[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		publisher.publish(new ReadDataEvent(loginIP+"&"+tableTitle+"&"+screenInfo+"&"+wfres));*/
		publisher.publish(new ReadDataEvent(loginIP+"&"+tableTitle+"&"+screenInfo));
		return true;
	}
	
	public boolean showInformationMessage(String loginIP, String jsz, String xsz,String djzs) {
		System.out.println("领证信息数据[loginIP:" + loginIP + ";jsz:" + jsz + ";xsz:" + xsz + ";djzs:"+ djzs + "]");
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		if("0".equals(cacheManager.getSystemConfig("isOpenInformation"))){//是否启用领证信息发屏
			System.out.println("领证信息直接发屏");
			//叫号队列屏发屏
			LED led = cacheManager.getLed_tvCache(deptCode+deptHall);
			String ledQueueShow = cacheManager.getSystemConfig("ledQueueShow");//获取发屏方式（纵向和横向）
			LEDUtils.sendLED_TVText(led, ledQueueShow, jsz+":"+xsz+":"+djzs);
		}else{
			System.out.println("领证信息综合屏发送");
			publisher.publish(new ShowLzxxDataEvent(loginIP+"&"+jsz+"&"+xsz+"&"+djzs));
		}
		return true;
	}

	public String queryCondition(String sfzmhm, String lsh) {
		// TODO Auto-generated method stub
		String res="";
		String code="",wfxx="";
		
		res +="<?xml version=”1.0” encoding=”GBK”?><root>";
		Number number =new Number();
		number.setIDNumber(sfzmhm);
		number.setLsh(lsh);
		try {
			List<Number> list=numberDao.getCardAndLshByAll(number);
			if(null != list && list.size()>0){
				code +=" <code>1</code><message>数据查询成功</message><rownum>"+list.size()+"</rownum>";
				wfxx+="<body>";
				for(Number numbers : list){
					wfxx+="</queue>";
						if(null != numbers.getQuhaoPhoto() && !"".equals(numbers.getQuhaoPhoto())){
							wfxx+="<quhaophoto>"+new BASE64Encoder().encode(numbers.getQuhaoPhoto())+"</ quhaophoto>";
						}else{
							wfxx+="<quhaophoto>"+numbers.getQuhaoPhoto()+"</quhaophoto>";
						}
						if(null != numbers.getPhotoBase64() && !"".equals(numbers.getPhotoBase64())){
							wfxx+="<evaluphoto>"+new BASE64Encoder().encode(numbers.getPhotoBase64())+"</evaluphoto>";
						}else{
							wfxx+="<evaluphoto>"+numbers.getPhotoBase64()+"</evaluphoto>";
						}
					wfxx+="</queue>";
				}
				wfxx+="</body>";
			}else{
				code +="<code>1</code><message>数据查询成功</message><rownum>"+list.size()+"</rownum>";
			}
		} catch (Exception e) {
			code="<code>0</code><message>数据查询失败</message><rownum>0</rownum>";
			e.printStackTrace();
		}
		res+=code+wfxx;
		res+="</root>";
		System.out.println(res);
		return res;
	}

	public String showYHXX(String zt, String jym, String url) {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		int xhnum =  Integer.parseInt(cacheManager.getSystemConfig("xhnum"));
		System.out.println(zt+"````"+jym+"````"+url);
		String res = "";
		String code = "";
		String datas = "";
		
		//jym = 410500000400#10.58.139.49#
		res +="<?xml version=”1.0” encoding=”GBK”?><root><head>";
		if ("0".equals(zt)) {//合法性验证的请求
			if (jym.split("#").length == 3 && jym.split("#")[0].length() == 12) {
				//验证双屏页面状态
				datas = jym.split("#")[1]+"@cjs@0";
				publisher.publish(new DualScreenEventYZ(datas));
				
				try {
					System.out.println(xhnum);
					Thread.sleep(xhnum*1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				String flag = CodeServiceImpl.YZFLAG;
				CodeServiceImpl.YZFLAG = "0";
				if ("1".equals(flag)) {
					code = jym.split("#")[0].substring(0, 6)+"#"+jym.split("#")[1]+"#"+jym.split("#")[2]+"#"+
					DateUtils.dateToString("yyyyMMdd")+"#"+jym.split("#")[0].substring(6, 12);
					try {
						System.out.println("加密前code="+code);
						code = codeService.jm(code);
						System.out.println("code="+code);
					} catch (Exception e) {
						System.out.println("摇号验证校验码生成失败");
						e.printStackTrace();
					}
					res += "<code>1</code><message>接口调用成功#"+code+"</message></head></root>";
				}else {
					System.out.println("双屏页面通讯失败");
					res += "<code>0</code><message>接口调用失败</message></head></root>";
				}
			}else {
				System.out.println("校验码参数不正确="+jym);
				res += "<code>0</code><message>接口调用失败</message></head></root>";
			}
		}else if ("1".equals(zt)) {//打开选号页面
			if (jym.split("#").length == 3 && jym.split("#")[0].length() == 12 && !"".equals(url)) {
				//选号疑问
				datas = jym.split("#")[1]+"@"+url+"@1";
				publisher.publish(new DualScreenEventYZ(datas));
				code = jym.split("#")[0].substring(0, 6)+"#"+jym.split("#")[1]+"#"+jym.split("#")[2]+"#"+
				DateUtils.dateToString("yyyyMMdd")+"#"+jym.split("#")[0].substring(6, 12);
				try {
					System.out.println("加密前code="+code);
					code = codeService.jm(code);
					System.out.println("code="+code);
				} catch (Exception e) {
					System.out.println("摇号验证校验码生成失败");
					e.printStackTrace();
				}
				res += "<code>1</code><message>接口调用成功#"+code+"</message></head></root>";
			}else {
				System.out.println("校验码参数不正确="+jym);
				System.out.println("URL参数不正确="+url);
				res += "<code>0</code><message>接口调用失败</message></head></root>";
			}
		}else {//不合法状态
			System.out.println("zt参数不正确="+zt);
			res += "<code>0</code><message>接口调用失败</message></head></root>";
		}
		System.out.println("传给六合一的xml="+res);
		return res;
	}

	private void encodeURIComponent(Object encodeURIComponent) {
		// TODO Auto-generated method stub
		
	}

	private Object encodeURIComponent(String url, String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}