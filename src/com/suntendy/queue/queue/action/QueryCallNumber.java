package com.suntendy.queue.queue.action;

import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.evaluation.domain.BadEvaluation;
import com.suntendy.queue.log.domain.LogVo;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.queue.util.cache.NumberManager;
import com.suntendy.queue.systemlog.domain.Operate;
import com.suntendy.queue.systemlog.operateLog.OperateLog;
import com.suntendy.queue.systemlog.service.ISystemLogService;
import com.suntendy.queue.util.DateUtils;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.TrffException;
import com.suntendy.queue.util.exception.UpdateException;
import com.suntendy.queue.util.trff.TrffUtils;

public class QueryCallNumber extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	private INumberService numberService;
	private ISystemLogService iSystemLogService;
	
	public void setiSystemLogService(ISystemLogService iSystemLogService) {
		this.iSystemLogService = iSystemLogService;
	}

	
	public INumberService getNumberService() {
		return numberService;
	}
	public void setNumberService(INumberService numberService) {
		this.numberService = numberService;
	}
	//根据号码查询窗口
	public String getBarId() throws Exception{
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String ksrq = StringUtils.trimToEmpty(request.getParameter("ksrq"));
		String jsrq = StringUtils.trimToEmpty(request.getParameter("jsrq"));
		String tjms = StringUtils.trimToEmpty(request.getParameter("tjms"));
		String IDNumber = StringUtils.trimToEmpty(request.getParameter("IDNumber"));
		Number number = new Number();
		String cxhm = StringUtils.trimToEmpty(request.getParameter("cxhm")).toUpperCase();
		String barId = StringUtils.trimToEmpty(request.getParameter("barId"));
		
		if (cxhm !=null && !cxhm.isEmpty()) {
			number.setSerialNum(deptCode+deptHall+cxhm+IDNumber);
		}
		number.setBarId(barId);
		number.setDeptCode(deptCode);
		number.setDeptHall(deptHall);
		number.setIDNumber(IDNumber);
		JSONObject objPrint = new JSONObject();
		List<Number> list = numberService.getvaluerecordRZ(number, tjms, ksrq, jsrq,IDNumber);
		for (int i = 0; i < list.size(); i++) {
			Number numberVo = list.get(i);
			if (numberVo.getCode()== null) {
				numberVo.setCode("无");
			}
			if (numberVo.getBarId() ==null) {
				numberVo.setBarId("无");
			}else{
				numberVo.setBarId(numberVo.getBarId()+"号");
			}
			if (numberVo.getBeginTime() == null) {
				numberVo.setBeginTime("无");
			}
			if (numberVo.getEndTime() == null) {
				numberVo.setEndTime("无");
			}
			numberVo.setSerialNum(numberVo.getSerialNum().substring(13, numberVo.getSerialNum().length()));
			if (numberVo.getStatus().equals("1")) {
				numberVo.setStatus("取号");
			}else if (numberVo.getStatus().equals("2")) {
				numberVo.setStatus("已叫号");
			}else if (numberVo.getStatus().equals("3")) {
				numberVo.setStatus("已过号");
			}else if (numberVo.getStatus().equals("4")) {
				numberVo.setStatus("正在评价");
			}else if (numberVo.getStatus().equals("5")) {
				numberVo.setStatus("已评价");
			}else if (numberVo.getStatus().equals("6")) {
				numberVo.setStatus("已评价");
			}else if (numberVo.getStatus().equals("7")) {
				numberVo.setStatus("挂起");
			}else if (numberVo.getStatus().equals("8")) {
				numberVo.setStatus("挂起恢复");
			}else if (numberVo.getStatus().equals("9")) {
				numberVo.setStatus("重呼");
			}
			if (numberVo.getCzyh() == null) {
				numberVo.setCzyh("无");
			}
			if(i==1){
				objPrint.put("winId", numberVo.getBarId());
			}
		}
		this.getResponse("text/html").getWriter().println(objPrint.toString());
		request.setAttribute("list", list);
		
		// 完成 查询操作进入日志功能 开始
		
		String event="查询";
		String module="查询管理";
		String moduleType="顺序号信息查询";
		String eventType="查";
		String coreBusiness="1";
		String result="0";
		String resultDepict="查询成功";
		//用户名 
		//String userName = request.getParameter("yhdh");  --这种方式取不到
		HttpSession session = getHttpSession();
		Employee employee = new Employee();
		Employee user = (Employee) session.getAttribute("user");
		String userName =user.getName();
		
		OperateLog operateLog = new OperateLog();
		Operate operate = operateLog.operate_log(userName,event, module, moduleType, eventType, coreBusiness, result, resultDepict);
		operate.setResult(result);
		try {
			iSystemLogService.addOperate(operate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 查询操作进入日志功能代码块结束
		

		return SUCCESS;
	}
	//根据窗口获取当天挂起信息
	public String queryAllGqBybarId() throws Exception{
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String barId = StringUtils.trimToEmpty(request.getParameter("barId"));
		String cxhm = StringUtils.trimToEmpty(request.getParameter("cxhm")).toUpperCase();
		Number number = new Number();
		number.setDeptCode(deptCode);
		number.setDeptHall(deptHall);
		number.setBarId(barId);
		if (cxhm !=null && !cxhm.isEmpty()) {
			number.setSerialNum(deptCode+deptHall+cxhm);
		}
		List<Number> list = numberService.queryAllGqBybarId(number);
		for (int i = 0; i < list.size(); i++) {
			Number numberVo = list.get(i);
			numberVo.setSerialNum(numberVo.getSerialNum().substring(13, numberVo.getSerialNum().length()));
			
			String operate ="<a onclick=huifu('" + numberVo.getId()
			+ "')><img src='/queue/images/button_hf.jpg' style='cursor:hand'/></a>";
			numberVo.setOperation(operate);
		}
		
		request.setAttribute("list", list);
		
		// 完成 查询操作进入日志功能 开始
		
		String event="查询";
		String module="权限管理";
		String moduleType="挂起管理";
		String eventType="查";
		String coreBusiness="1";
		String result="0";
		String resultDepict="查询成功";
		//用户名 
		//String userName = request.getParameter("yhdh");  --这种方式取不到
		HttpSession	session = getHttpSession();
		Employee employee = new Employee();
		Employee user = (Employee) session.getAttribute("user");
		String userName =user.getName();
		
		OperateLog operateLog = new OperateLog();
		Operate operate = operateLog.operate_log(userName,event, module, moduleType, eventType, coreBusiness, result, resultDepict);
		operate.setResult(result);
		try {
			iSystemLogService.addOperate(operate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 查询操作进入日志功能代码块结束
		
		
		return "allGq";
	}
	//挂起信息列表及详细信息
	public String gqInfoListOrDetail() throws Exception{
		HttpServletRequest request = getRequest();
		String ksrq = StringUtils.trimToEmpty(request.getParameter("hksrq"));
		String jsrq = StringUtils.trimToEmpty(request.getParameter("hjsrq"));
		String tjms = StringUtils.trimToEmpty(request.getParameter("tjms"));
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		if (null != id) {
			List<Number> list = numberService.gqInfoDtail(id);
				Number numberVo = list.get(0);
				numberVo.setSerialNum(numberVo.getSerialNum().substring(13, numberVo.getSerialNum().length()));
			request.setAttribute("number", numberVo);
			return "gqInfoDetail";
		}else{
			List<Number> list = numberService.gqInfoList(code,tjms, ksrq, jsrq);
			for (int i = 0; i < list.size(); i++) {
				Number numberVo = list.get(i);
				numberVo.setSerialNum(numberVo.getSerialNum().substring(13, numberVo.getSerialNum().length()));
				String operate ="<a onclick=queryDetail('" + numberVo.getId()
				+ "')><img src='/queue/images/button_ck.jpg' style='cursor:hand'/></a>";
				numberVo.setOperation(operate);
			}
			request.setAttribute("list", list);
			request.setAttribute("code", code);
			return "gqInfoList";
			
		}
	}
	//获取挂起表取号照片
	public String getQhPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.gqInfoDtail(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getQuhaoPhoto()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getQuhaoPhoto());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取挂号表评价照片
	public String getPjPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.gqInfoDtail(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getPhotoBase64()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getPhotoBase64());
			out.flush();
			out.close();
		}
		return null;
	}
	//查询所有照片信息
	public String queryAllPhoto() throws Exception{
		HttpServletRequest request = getRequest();
		Employee user = (Employee)getHttpSession().getAttribute("user");
		String role = user.getRole();
		CacheManager cacheManager = CacheManager.getInstance();
		String ksrq = StringUtils.trimToEmpty(request.getParameter("ksrq"));
		String jsrq = StringUtils.trimToEmpty(request.getParameter("jsrq"));
		String tjms = StringUtils.trimToEmpty(request.getParameter("tjms"));
		String id = request.getParameter("id");
		String rNumber = cacheManager.getSystemConfig("rNumber");
		String operNum = StringUtils.trimToEmpty(request.getParameter("operNum"));
		String barId = StringUtils.trimToEmpty(request.getParameter("barId"));
		String IDNumber = StringUtils.trimToEmpty(request.getParameter("IDNumber"));
		String deptcode;
		String depthall;
		List<Number> list;
		if (!"".equals(id) && null!=id) {
			list = numberService.detailSfz(id);
			if(null != list && list.size()>0){
				Number number = list.get(0);
				if(null != number.getSerialNum() && !"".equals(number.getSerialNum())){
					number.setSerialNum(number.getSerialNum().substring(13, number.getSerialNum().length()));
				}
				request.setAttribute("number", number);
			}
			return "queryPhotoDetail";
		}else{
			if ("0".equals(role)) {
				deptcode="";
				depthall="";
				list = numberService.queryAllSfz(tjms, ksrq, jsrq,rNumber,barId,operNum,IDNumber,deptcode,depthall);
				if(null != list && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Number number = list.get(i);
						String operate ="<a onclick=queryDetail('" + number.getId()
						+ "')><img src='/queue/images/button_ck.jpg' style='cursor:hand'/></a>";
						number.setOperation(operate);
						if (!"".equals(number.getSfzphoto()) && null != number.getSfzphoto()) {
							number.setSfzphotoStr("有");
						}else{
							number.setSfzphotoStr("无");
						}
						if (!"".equals(number.getQuhaoPhoto()) && null != number.getQuhaoPhoto()) {
							number.setQuhaoPhotoStr("有");
						}else{
							number.setQuhaoPhotoStr("无");
						}
						if (!"".equals(number.getPhotoBase64()) && null != number.getPhotoBase64()) {
							number.setPhotoBase64Str("有");
						}else{
							number.setPhotoBase64Str("无");
						}
						if ("".equals(number.getBeginTime()) || null == number.getBeginTime()) {
							number.setBeginTime("未叫号");
						}
						if(null != number.getSerialNum() && !"".equals(number.getSerialNum())){
							number.setSerialNum(number.getSerialNum().substring(13, number.getSerialNum().length()));
						}
					}
					request.setAttribute("list", list);
				}
			}else if("1".equals(role)){
				deptcode=cacheManager.getOfDeptCache("deptCode");;
				depthall="";
				list = numberService.queryAllSfz(tjms, ksrq, jsrq,rNumber,barId,operNum,IDNumber,deptcode,depthall);
				if(null != list && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Number number = list.get(i);
						String operate ="<a onclick=queryDetail('" + number.getId()
						+ "')><img src='/queue/images/button_ck.jpg' style='cursor:hand'/></a>";
						number.setOperation(operate);
						if (!"".equals(number.getSfzphoto()) && null != number.getSfzphoto()) {
							number.setSfzphotoStr("有");
						}else{
							number.setSfzphotoStr("无");
						}
						if (!"".equals(number.getQuhaoPhoto()) && null != number.getQuhaoPhoto()) {
							number.setQuhaoPhotoStr("有");
						}else{
							number.setQuhaoPhotoStr("无");
						}
						if (!"".equals(number.getPhotoBase64()) && null != number.getPhotoBase64()) {
							number.setPhotoBase64Str("有");
						}else{
							number.setPhotoBase64Str("无");
						}
						if ("".equals(number.getBeginTime()) || null == number.getBeginTime()) {
							number.setBeginTime("未叫号");
						}
						if(null != number.getSerialNum() && !"".equals(number.getSerialNum())){
							number.setSerialNum(number.getSerialNum().substring(13, number.getSerialNum().length()));
						}
					}
					request.setAttribute("list", list);
				}
			}else if ("2".equals(role)) {
				deptcode=cacheManager.getOfDeptCache("deptCode");
				depthall=cacheManager.getOfDeptCache("deptHall");
				list = numberService.queryAllSfz(tjms, ksrq, jsrq,rNumber,barId,operNum,IDNumber,deptcode,depthall);
				if(null != list && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Number number = list.get(i);
						String operate ="<a onclick=queryDetail('" + number.getId()
						+ "')><img src='/queue/images/button_ck.jpg' style='cursor:hand'/></a>";
						number.setOperation(operate);
						if (!"".equals(number.getSfzphoto()) && null != number.getSfzphoto()) {
							number.setSfzphotoStr("有");
						}else{
							number.setSfzphotoStr("无");
						}
						if (!"".equals(number.getQuhaoPhoto()) && null != number.getQuhaoPhoto()) {
							number.setQuhaoPhotoStr("有");
						}else{
							number.setQuhaoPhotoStr("无");
						}
						if (!"".equals(number.getPhotoBase64()) && null != number.getPhotoBase64()) {
							number.setPhotoBase64Str("有");
						}else{
							number.setPhotoBase64Str("无");
						}
						if ("".equals(number.getBeginTime()) || null == number.getBeginTime()) {
							number.setBeginTime("未叫号");
						}
						if(null != number.getSerialNum() && !"".equals(number.getSerialNum())){
							number.setSerialNum(number.getSerialNum().substring(13, number.getSerialNum().length()));
						}
					}
					request.setAttribute("list", list);
				}
			}
			
			return "queryPhotoList";
			
		}
	}
	
	//获取valuerecord评价照片
	public String getVPjPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getPhotoBase64()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getPhotoBase64());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord身份证照片
	public String getVSfzPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getSfzphoto()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getSfzphoto());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord取号照片
	public String getVQhPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getQuhaoPhoto()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getQuhaoPhoto());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（身份证A）照片
	public String getIDnumber_APhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto1()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto1());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（身份证B）照片
	public String getIDnumber_BPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto2()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto2());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（登记证书）照片
	public String getDjzsPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto3()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto3());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（行驶证）照片
	public String getXszPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto4()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto4());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（驾驶证）照片
	public String getJszPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto5()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto5());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（企业代码证书）照片
	public String getQydmzsPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto6()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto6());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（士兵证）照片
	public String getSbzPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto7()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto7());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（退伍证书）照片
	public String getITwzsPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto8()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto8());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（护照）照片
	public String getHzPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto9()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto9());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（号牌号码）照片
	public String getHphmPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto13()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto13());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（代办人身份证A）照片
	public String getDbrCard_APhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto10()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto10());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（代办人身份证B）照片
	public String getDbrCard_BPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto11()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto11());
			out.flush();
			out.close();
		}
		return null;
	}
	//获取valuerecord高拍仪（居住证）照片
	public String getJzzPhoto() throws Exception{
		HttpServletResponse response=getResponse("image/jpeg");
		String id = getRequest().getParameter("id");
		List<Number> list = numberService.detailSfz(id);
		Number numberVo = list.get(0);
		if (null != numberVo.getGpyPhoto12()) {
			ServletOutputStream out=response.getOutputStream();
			out.write(numberVo.getGpyPhoto12());
			out.flush();
			out.close();
		}
		return null;
	}
	
	public String countIdNumber() throws Exception{
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		HttpSession session=getHttpSession();
		//role 0:超级管理员 1:部门系统管理员 2:普通大厅管理员
		Employee user=(Employee) session.getAttribute("user");
		String role =user.getRole();
		String deptCode = "";
		String deptHall = "";
		if("0".equals(role)){
			deptCode=request.getParameter("deptCode");
			deptHall=request.getParameter("deptHall");
			if("null".equals(deptHall)){
				deptHall="";
			}
		}else if("1".equals(role)) {
			deptCode=cacheManager.getOfDeptCache("deptCode");
			deptHall=request.getParameter("deptHall");
			
		}else if ("2".equals(role)) {
			deptCode=cacheManager.getOfDeptCache("deptCode");
			deptHall=cacheManager.getOfDeptCache("deptHall");
		}
		String ksrq = StringUtils.trimToEmpty(request.getParameter("ksrq"));
		String jsrq = StringUtils.trimToEmpty(request.getParameter("jsrq"));
		String tjms = StringUtils.trimToEmpty(request.getParameter("tjms"));
		if("".equals(tjms)||tjms==null){
			tjms="1";
		}
		String idnumber = request.getParameter("idnumber");
		String tjry = request.getParameter("tjry");
		Number number = new Number();
		number.setTjry(tjry);
		number.setIDNumber(idnumber);
		List<Number> list = numberService.countIdNumber(tjms, ksrq, jsrq, number,deptCode,deptHall);
		request.setAttribute("deptCode",deptCode);
		request.setAttribute("deptHall",deptHall);
		request.setAttribute("list", list);
		
		// 完成 查询操作进入日志功能 开始
		
		String event="查询";
		String module="统计报表";
		String moduleType="取号次数统计";
		String eventType="查";
		String coreBusiness="1";
		String result="0";
		String resultDepict="查询成功";
		//用户名 
		//String userName = request.getParameter("yhdh");  --这种方式取不到
		session = getHttpSession();
		Employee employee = new Employee();
		user = (Employee) session.getAttribute("user");
		String userName =user.getName();
		
		OperateLog operateLog = new OperateLog();
		Operate operate = operateLog.operate_log(userName,event, module, moduleType, eventType, coreBusiness, result, resultDepict);
		operate.setResult(result);
		try {
			iSystemLogService.addOperate(operate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 查询操作进入日志功能代码块结束
		
		
		
		
		
		
		return "countidnumber";
	}
	
	
	public String queryIdnumberDifference() throws Exception{
		
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String ksrq = StringUtils.trimToEmpty(request.getParameter("ksrq"));
		String jsrq = StringUtils.trimToEmpty(request.getParameter("jsrq"));
		String tjms = StringUtils.trimToEmpty(request.getParameter("tjms"));
		String idnumber = request.getParameter("idnumber");
		String idnumberb = request.getParameter("idnumberb");
		String lsh = request.getParameter("lsh");
		Number number = new Number();
		if (!"".equals(idnumber)) {
			number.setIDNumber(idnumber);
		}
		if (!"".equals(idnumberb)) {
			number.setIDNumberB(idnumberb);
		}
		if (!"".equals(lsh)) {
			number.setLsh(lsh);
		}
		List<Number> list = numberService.queryIdnumberDifference(tjms, ksrq, jsrq, number);
		for (int i = 0; i < list.size(); i++) {
			Number numberVO = list.get(i);
			numberVO.setSerialNum(numberVO.getSerialNum().substring(13, numberVO.getSerialNum().length()));
			numberVO.setEnterTime(numberVO.getEnterTime().substring(0, 19));
		}
		request.setAttribute("list", list);
		
		
		// 完成 查询操作进入日志功能 开始
		
		String event="查询";
		String module="统计报表";
		String moduleType="身份证异常处理";
		String eventType="查";
		String coreBusiness="1";
		String result="0";
		String resultDepict="查询成功";
		//用户名 
		//String userName = request.getParameter("yhdh");  --这种方式取不到
		HttpSession session = getHttpSession();
		Employee employee = new Employee();
		Employee user = (Employee) session.getAttribute("user");
		String userName =user.getName();
		
		OperateLog operateLog = new OperateLog();
		Operate operate = operateLog.operate_log(userName,event, module, moduleType, eventType, coreBusiness, result, resultDepict);
		operate.setResult(result);
		try {
			iSystemLogService.addOperate(operate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 查询操作进入日志功能代码块结束
		
		
		
		return "queryIdnumberDifference";
	}
	
	//安阳评价信息写入
	public void anYangPJUpload() throws Exception{
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number number = new Number();
		number.setLsh("1");
		List<Number> list = numberService.getValueRecordAllForAY(number);
		if (list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				try {
					if ("0".equals(cacheManager.getSystemConfig("isUseInterface"))) {
						System.out.println("进入安阳评价上传");
						String pjjg = "1";
							if ("1".equals(list.get(i).getValue())) {
								pjjg = "1";
							}else if ("2".equals(list.get(i).getValue()) || "3".equals(list.get(i).getValue()) || "4".equals(list.get(i).getValue())) {
								pjjg = "2";
							}
						String ywlxdm = "AA";
						String ywlxmc = "注册登记";
							if (!"1".equals(list.get(i).getBusinessType())) {
								ywlxdm = "CB";
								ywlxmc = "补证换证";
							}
						String xh = deptCode+"1"+"999999999";
						if ("A".equals(list.get(i).getClientno().substring(13, 14))) {
							xh = deptCode+"100001"+list.get(i).getClientno().substring(14, 18);
						}else {
							xh = deptCode+"100002"+list.get(i).getClientno().substring(14, 18);
						}
						String[] result = 
							TrffUtils.anYangEvaluationWrite(deptCode, 
									list.get(i).getCode(), 
									list.get(i).getXm(), 
									list.get(i).getCode(), 
									deptCode, 
									ywlxdm, 
									ywlxmc, 
									list.get(i).getEndTime(), 
									pjjg, 
									list.get(i).getLsh(), 
									list.get(i).getBarIp(), 
									xh);
							if (result.length>0) {
								System.out.println("写入评价接口接口="+result[0]);
								if ("1".equals(result[0])) {
									number.setOut1("1");
									number.setId(list.get(i).getId());
									numberService.updateOut1(number);
								}
							}else {
								System.out.println("写入评价接口无返回结果");
							}
					}
				} catch (Exception e) {
					System.out.println("写入评价接口抛出异常!");
				}
			}
		}
	}
	
	/**
	 * 挂起
	 * 
	 * @return 提示信息
	 * @throws Exception
	 */
	public String hangup_new() throws Exception {
		String resultMsg = "";
		HttpServletRequest request = getRequest();
		HttpSession session = this.getHttpSession();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number number = new Number();
		String pdh = request.getParameter("pdh");
		if ("".equals(pdh) || null == pdh) {
			resultMsg = "排队号为空!";
			request.setAttribute("msg", resultMsg);
			request.setAttribute("action", "hangup_new.action");
			return "success";
		}
		number.setSerialNum(deptCode+deptHall+pdh);
		number.setDeptCode(deptCode);
		number.setDeptHall(deptHall);
		List<Number> list = numberService.queryNumberBypdh(number);

		if (list.size() > 0) {
			if ("".equals(list.get(0).getIschuli()) || null == list.get(0).getIschuli()) {
				try {
					resultMsg = numberService.hangup_new(list.get(0));
					request.setAttribute("msg", resultMsg);
				} catch (Exception e) {
					if (e instanceof UpdateException) {
						resultMsg = "更新号码信息状态失败，<br/>请查看当天日志";
						e.printStackTrace();
						request.setAttribute("msg", resultMsg);
					} else if (e instanceof RemoteException
							|| e instanceof TrffException) {
						resultMsg = e.getMessage();
						request.setAttribute("msg", resultMsg);
					} else {
						resultMsg = "在执行过程中发生异常，请查看当天日志";
						e.printStackTrace();
						request.setAttribute("msg", resultMsg);
					}
				}
			}else {
				resultMsg = "该顺序号已办结,无法挂起!";
				request.setAttribute("msg", resultMsg);
			}
		}else {
			resultMsg = "无法查询到该顺序号!";
			request.setAttribute("msg", resultMsg);
		}

		request.setAttribute("action", "hangup_new.action");
		return "success";
	}
	
	public String getrzdbz()
	        throws Exception
	    {	
			CacheManager cacheManager = CacheManager.getInstance();
			String deptCode = cacheManager.getOfDeptCache("deptCode");
			String deptHall = cacheManager.getOfDeptCache("deptHall");
	        HttpServletRequest request = getRequest();
	        String ksrq = StringUtils.trimToEmpty(request.getParameter("ksrq"));
	        String jsrq = StringUtils.trimToEmpty(request.getParameter("jsrq"));
	        String xm = StringUtils.trimToEmpty(request.getParameter("xm"));
	        String IDNumber = StringUtils.trimToEmpty(request.getParameter("IDNumber"));
	        String Serial = StringUtils.trimToEmpty(request.getParameter("SerialNum"));
	        String SerialNum = null;
	        if(!"".equals(Serial)){
	        	 SerialNum = deptCode + deptHall + Serial;
	        }
	        String ckip = StringUtils.trimToEmpty(request.getParameter("barId"));
	        String tjms = StringUtils.trimToEmpty(request.getParameter("tjms"));
	        Number number = new Number();
	        number.setIDNumber(IDNumber);
	        number.setSerialNum(SerialNum);
	        List<Number> list = numberService.getrzdbz(tjms, ckip, xm, ksrq, jsrq, number);
	        //编译证件
	        if(!"null".equals(list) && list.size()>0){
		        for(Number num:list){
		        	String operate ="<a onclick=queryDetail('" + num.getId()
							+ "')><img src='/queue/images/button_ck.jpg' style='cursor:hand'/></a>";
		        			num.setOperation(operate);
							if (!"".equals(num.getSfzphoto()) && null != num.getSfzphoto()) {
								num.setSfzphotoStr("有");
							}else {
								num.setSfzphotoStr("无");
							}
							if (!"".equals(num.getQuhaoPhoto()) && null != num.getQuhaoPhoto()) {
								num.setQuhaoPhotoStr("有");
							}else{
								num.setQuhaoPhotoStr("无");
							}
							if ("".equals(num.getNameA()) || null == num.getNameA()) {
								num.setNameA("非身份证取号");
							}
							if (!"".equals(num.getRzdbz()) && null != num.getRzdbz()) {
								if(num.getRzdbz().equals("90")){
									num.setRzdbz("通过");
								}else {
									num.setRzdbz("不通过");
								}
							}else{
								num.setRzdbz("不通过");
							}
		        }
	        }
	        request.setAttribute("list", list);
	        return "success";
	    }
}
