package com.suntendy.queue.webservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;

import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.service.IEmployeeService;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.util.Base64Util;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.UpdateException;
import com.suntendy.queue.util.scriptsession.Publisher;
import com.suntendy.queue.util.scriptsession.event.DualScreenEvent;
import com.suntendy.queue.util.scriptsession.event.RzdbTs;
import com.suntendy.queue.window.domain.Screen;
import com.suntendy.queue.window.service.ISetWindowService;

public class LoginWSAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private IEmployeeService employeeService;
	private ISetWindowService setWindowService;
	private Publisher publisher;
	private INumberService numberService;
	
	private static Map<String, String> photos = new HashMap<String, String>();

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public void setSetWindowService(ISetWindowService setWindowService) {
		this.setWindowService = setWindowService;
	}
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
	public void setNumberService(INumberService numberService) {
		this.numberService = numberService;
	}
	public String autoLogin() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		JSONObject datas = new JSONObject();
		Employee employeeVo = new Employee();
		System.out.println("进入自动登录");
		String loginIP = request.getParameter("loginIP");
		System.out.println("loginIP="+loginIP);
		employeeVo.setLoginIp(loginIP);
		employeeVo.setDeptHall(deptHall);
		boolean bj = false;
		List<Employee> list = employeeService.searchEmployeeByIp(employeeVo);
		if (list.size()>0) {
			bj = true;
			if (null != list.get(0).getPhoto()
					&& null == photos.get(list.get(0).getCode())) {
				String photoDir = "photoTmp";
				String code = list.get(0).getCode();
				String realPath = this.getServletContext().getRealPath("/") + photoDir;
				File fileDir = new File(realPath);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				try {
					String photoName = code + ".jpg";
					realPath = realPath + "/" + photoName;
					FileOutputStream fout = new FileOutputStream(realPath);
					fout.write(list.get(0).getPhoto());
					fout.flush();
					fout.close();

					photos.put(code, this.getServletContext().getContextPath() + "/" + photoDir
							+ "/" + photoName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String photo = photos.get(list.get(0).getCode());
			datas.put("code", list.get(0).getCode());
			datas.put("loginIP", list.get(0).getLoginIp());
			datas.put("name", list.get(0).getName());
			datas.put("comments", list.get(0).getComments());
			datas.put("photo", StringUtils.trimToEmpty(photo));
		}
		datas.put("bj", bj);
		this.getResponse("text/html").getWriter().println(datas.toString());
		return null;
	}
	
	public String dsAutoInit() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		JSONObject datas = new JSONObject();
		System.out.println("进入初始化电视窗口屏");
		String loginIP = request.getParameter("loginIP");
		System.out.println("loginIP="+loginIP);
		boolean bj = false;
//		List<Screen> list = setWindowService.getWindowList(deptCode, deptHall, "", "", "", "", loginIP);
		List<Screen> list = setWindowService.getckxx(deptCode, deptHall, loginIP);
		if (list.size()>0) {
			bj = true;
			String ckip = list.get(0).getCkip();
			String ckid = list.get(0).getWindowId();
			if (ckid.length() ==1) {
				ckid = "0"+ckid;
			}
			String ckmc = list.get(0).getContent();
			if ("".equals(ckmc) || null == ckmc) {
				ckmc = "欢迎光临";
			}
			String sxh = list.get(0).getContent2();
			if ("".equals(sxh) || null == sxh) {
				sxh = " ";
			}
			if (null != list.get(0).getPhoto()
					&& null == photos.get(list.get(0).getCode())) {
				String photoDir = "photoTmp";
				String code = list.get(0).getCode();
				String realPath = this.getServletContext().getRealPath("/") + photoDir;
				File fileDir = new File(realPath);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				try {
					String photoName = code + ".jpg";
					realPath = realPath + "/" + photoName;
					FileOutputStream fout = new FileOutputStream(realPath);
					fout.write(list.get(0).getPhoto());
					fout.flush();
					fout.close();

					photos.put(code, this.getServletContext().getContextPath() + "/" + photoDir
							+ "/" + photoName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String photo = photos.get(list.get(0).getCode());
			System.out.println(ckip+"````"+ckid+"````"+ckmc+"````"+sxh+"````"+list.get(0).getCode()+"````"+list.get(0).getXm()+"````"+list.get(0).getComments()+"````"+photo);
			if (!"".equals(list.get(0).getCode()) && null != list.get(0).getCode()) {
				datas.put("ckip", ckip);
				datas.put("ckid", ckid);
				datas.put("ckmc", ckmc);
				datas.put("sxh", sxh);
				datas.put("code", list.get(0).getCode());
				datas.put("name", list.get(0).getXm());
				datas.put("comments", list.get(0).getComments());
				datas.put("photo", StringUtils.trimToEmpty(photo));
			}else {
				datas.put("ckip", ckip);
				datas.put("ckid", ckid);
				datas.put("ckmc", ckmc);
				datas.put("sxh", sxh);
			}
		}
		datas.put("bj", bj);
		this.getResponse("text/html").getWriter().println(datas.toString());
		return null;
	}
	
	public String login() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		Employee employee = new Employee();
		employee.setFmt_status("0");
		employee.setCode(request.getParameter("operNum"));
		employee.setLoginIp(request.getParameter("loginIP"));
		employee.setDepartment(request.getParameter("operDept"));
		employee.setDeptCode(cacheManager.getOfDeptCache("deptCode"));
		employee.setDeptHall(cacheManager.getOfDeptCache("deptHall"));
		employee.setName(URLDecoder.decode(request.getParameter("operName"), "UTF-8"));
		
		boolean result = false;
		try {
		/*	 Employee loyee = new Employee();
		      Employee employeeStatus = new Employee();
		      loyee.setCode(employee.getCode());
		      List employeeList = this.employeeService.getLoginStateByCode(loyee);
		      if (!employeeList.isEmpty()) {
		        employeeStatus = (Employee)employeeList.get(0);
		      }
		      if (employeeStatus.getCode() != null) {
		        System.out.println("进入重启");
		        restartIE(employee.getLoginIp());
		        System.out.println("结束");
		        Thread.sleep(2000L);
		      }*/
			employeeService.save(employee);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.getResponse().getWriter().print(result);
		return null;
	}
	public void restartIE(String ieRestartIP) {
		System.out.println("C:\\服务器上运行的文件\\JhDoublePServer.exe 0#"+ieRestartIP+"#");
		Runtime r = Runtime.getRuntime();
	    try {
	    	r.exec("C:\\服务器上运行的文件\\JhDoublePServer.exe 0#"+ieRestartIP+"#");
			System.out.println("完成IE关闭");
			Thread.sleep(10000);
			r.exec("C:\\服务器上运行的文件\\JhDoublePServer.exe 1#"+ieRestartIP+"#");
			System.out.println("完成IE开启");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("执行过程中发生异常！");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("执行过程中发生异常！");
		}
		
	}
	public static String getEncoding(String str) {  
        String encode = "GB2312";  
        try {  
            if (str.equals(new String(str.getBytes(encode), encode))) {  
                String s = encode;  
                return s;  
            }  
        } catch (Exception exception) {  
        }  
        encode = "ISO-8859-1";  
        try {  
            if (str.equals(new String(str.getBytes(encode), encode))) {  
                String s1 = encode;  
                return s1;  
            }  
        } catch (Exception exception1) {  
        }  
        encode = "UTF-8";  
        try {  
            if (str.equals(new String(str.getBytes(encode), encode))) {  
                String s2 = encode;  
                return s2;  
            }  
        } catch (Exception exception2) {  
        }  
        encode = "GBK";  
        try {  
            if (str.equals(new String(str.getBytes(encode), encode))) {  
                String s3 = encode;  
                return s3;  
            }  
        } catch (Exception exception3) {  
        }  
        return "";  
    } 
	
	public void Rzdbts() throws IOException{
		CacheManager cacheManager = CacheManager.getInstance();
		String rzkg = cacheManager.getSystemConfig("hfhmrzdb");
		HttpServletRequest request = getRequest();
		String operNum = request.getParameter("operNum");
		Employee employee=employeeService.getEmployeeByCode(operNum);
		boolean result=false;
		if("0".equals(rzkg)){
			publisher.publish(new RzdbTs("0@"+employee.getWSIp()+"@"+employee.getLoginIp()));
			//publisher.publish(new RzdbTs("0@"+"62.170.100.6"+"@"+"62.170.100.22"));
			result=true;
		}
		this.getResponse().getWriter().print(result);
	}
	
	public void rzdbResult() throws Exception{
		CacheManager cacheManager = CacheManager.getInstance();
		HttpServletRequest request = getRequest();
		String loginIP = request.getParameter("loginIP");
		String retCode = request.getParameter("retCode");
		String nowNumber = request.getParameter("nowNumber");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String ywPhoto = request.getParameter("ywPhoto");
		nowNumber=deptCode+deptHall+nowNumber;
		Number number = new Number();
		if("0".equals(retCode)){
			retCode="90";
		}else {
			retCode="100";
		}
		byte[] bytes = Base64Util.decode(ywPhoto);
		number.setQuhaoPhoto(bytes);
		number.setRzdbz(retCode);
		number.setSerialNum(nowNumber);
		numberService.updateRzdbz(number);
	    publisher.publish(new RzdbTs("1@"+retCode+"@"+loginIP));
	}
	
}