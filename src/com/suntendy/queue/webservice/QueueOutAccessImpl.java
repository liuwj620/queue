package com.suntendy.queue.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import com.suntendy.queue.business.domain.Business;
import com.suntendy.queue.business.service.IBusinessService;
import com.suntendy.queue.dept.domain.Dept;
import com.suntendy.queue.dept.service.DeptService;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.service.IEmployeeService;
import com.suntendy.queue.queue.dao.INumberDao;
import com.suntendy.queue.queue.domain.Code;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.PrintInfo;
import com.suntendy.queue.queue.service.ICodeService;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.reservation.domain.Reservation;
import com.suntendy.queue.reservation.service.IReservationService;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.SaveException;
import com.suntendy.queue.util.exception.UpdateException;
import com.suntendy.queue.util.scriptsession.Publisher;
import com.suntendy.queue.util.scriptsession.event.ChuangKouPing;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventIMG;
import com.suntendy.queue.util.scriptsession.event.tsLzxx;
import com.suntendy.queue.util.trff.XMLUtils;
import com.suntendy.queue.window.domain.Screen;
import com.suntendy.queue.window.service.ISetWindowService;


@WebService(endpointInterface = "com.suntendy.queue.webservice.IQueueOutAccess")
public class QueueOutAccessImpl implements IQueueOutAccess {

	
	private Publisher publisher;
	private IEmployeeService employeeService;
	private INumberDao numberDao;
	private INumberService numberService;
	private IReservationService reservationService;
	private IBusinessService businessService;
	private ICodeService codeService;
	private ISetWindowService setWindowService;
	private DeptService deptService;
	
	
	public void setNumberDao(INumberDao numberDao) {
		this.numberDao = numberDao;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	public INumberService getNumberService() {
		return numberService;
	}
	public void setNumberService(INumberService numberService) {
		this.numberService = numberService;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public IEmployeeService getEmployeeService() {
		return employeeService;
	}
	public INumberDao getNumberDao() {
		return numberDao;
	}
	public IReservationService getReservationService() {
		return reservationService;
	}
	public void setReservationService(IReservationService reservationService) {
		this.reservationService = reservationService;
	}
	public IBusinessService getBusinessService() {
		return businessService;
	}
	public void setBusinessService(IBusinessService businessService) {
		this.businessService = businessService;
	}
	public ICodeService getCodeService() {
		return codeService;
	}
	public void setCodeService(ICodeService codeService) {
		this.codeService = codeService;
	}
	public ISetWindowService getSetWindowService() {
		return setWindowService;
	}
	public void setSetWindowService(ISetWindowService setWindowService) {
		this.setWindowService = setWindowService;
	}
	public DeptService getDeptService() {
		return deptService;
	}
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}
	
	public String queryNumbersByIp(String xmls) {
		System.out.println("xmls="+xmls);
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><QueryCondition><ip>127.0.0.1</ip><glbm>371300000400</glbm></QueryCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		
		
		String hqip = map.get("ip");
		String hqglbm = map.get("glbm");
		
		
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		Number number = new Number();
//		if (deptCode.equals(glbm)) {
		if (deptCode.equals(hqglbm)) {
				List<Number> list = new ArrayList<Number>();
				try {
//					list = numberDao.getNumberAllByClientIP(ip);
					list = numberDao.getNumberAllByClientIP(hqip);
					if (null != list && list.size()>0) {
						number = list.get(0);
						xml +="<code>1</code><message>数据查询成功</message><rownum>"+list.size()+"</rownum></head>";
						xml +="<body><queue><ygbh>"+number.getOperNum()+"</ygbh>";
						xml +="<ckbh>"+number.getBarId()+"</ckbh>";
						xml +="<sxh>"+number.getSerialNum()+"</sxh>";
						xml +="<ywlx>"+number.getBusinessType()+"</ywlx>";
						xml +="<qhsj>"+number.getEnterTime()+"</qhsj>";
						xml +="<jhsj>"+number.getBeginTime()+"</jhsj>";
						xml +="<glbm>"+number.getDeptCode()+"</glbm>";
						xml +="<dtbh>"+number.getDeptHall()+"</dtbh></queue></body></root>";
					}else{
						xml +="<code>1</code><message>无数据信息</message><rownum>0</rownum></head></root>";
					}
				} catch (Exception e) {
					xml +="<code>2</code><message>数据查询失败</message><rownum>0</rownum></head></root>";
					e.printStackTrace();
				}
		}else{
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message><rownum>0</rownum></head></root>";
		}
		return xml;
	}

	@Override
	public String toEvalu(String xmls) {
		System.out.println("xmls="+xmls);
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><QueryCondition><yhdh>111</yhdh><ip>192.168.47.128</ip><glbm>371300000400</glbm></QueryCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String hqyhdh = map.get("yhdh");
		String hqip = map.get("ip");
		System.out.println("ip="+hqip);
		String hqglbm = map.get("glbm");
		String xml = "";
		String remsg = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		if (deptCode.equals(hqglbm)) {
			try {
				remsg = numberService.toEvaluationForQueueOut(hqyhdh, hqip);
				xml +="<code>1</code><message>"+remsg+"</message></head></root>";
			} catch (Exception e) {
				if (e instanceof NullPointerException) {
					remsg = "未叫号，不能评价";
				} else if (e instanceof UpdateException) {
					remsg = "更新号码信息状态[等待评价]失败，<br/>请查看当天日志";
					e.printStackTrace();
				} else {
					remsg = "在执行过程中发生异常，请查看当天日志";
					e.printStackTrace();
				}
				xml +="<code>2</code><message>"+remsg+"</message></head></root>";
			}
		}else {
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message><rownum>0</rownum></head></root>";
		}
		return xml;
	}

	@Override
	public String reservationEdit(String xmls) {
		System.out.println("xmls="+xmls);
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><WriteCondition><zjlx>1</zjlx><sfzmhm>123</sfzmhm><ywlx>1</ywlx><yykssj>2015-8-25 16:02:08</yykssj><yyjssj>2015-8-25 16:02:17</yyjssj><czlx>4</czlx><dtbh>A</dtbh><glbm>371300000400</glbm></WriteCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String zjlx = map.get("zjlx");
		String sfzmhm = map.get("sfzmhm");
		String ywlx = map.get("ywlx");
		String yykssj = map.get("yykssj");
		String yyjssj = map.get("yyjssj");
		String czlx = map.get("czlx");
		String dtbh = map.get("dtbh");
		String glbm = map.get("glbm");
		String xml = "";
		Reservation res = new Reservation();
		res.setZjlx(zjlx);
		res.setSfzmhm(sfzmhm);
		res.setYwlx(ywlx);
		res.setYykssj(yykssj);
		res.setYyjssj(yyjssj);
		res.setDeptCode(glbm);
		res.setDeptHall(dtbh);
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		if (deptCode.equals(glbm)) {
			if ("0".equals(czlx)) {
				try {
					reservationService.saveReservation(res);
					xml +="<code>1</code><message>数据保存成功</message></head></root>";
				} catch (Exception e) {
					e.printStackTrace();
					xml +="<code>2</code><message>数据保存失败</message></head></root>";
				}
			}else if("2".equals(czlx)){
				try {
					reservationService.updateReservation(res);
					xml +="<code>1</code><message>数据修改成功</message></head></root>";
				} catch (Exception e) {
					e.printStackTrace();
					xml +="<code>2</code><message>数据修改失败</message></head></root>";
				}
			}else if ("1".equals(czlx)) {
				try {
					reservationService.delReservation(sfzmhm);
					xml +="<code>1</code><message>数据删除成功</message></head></root>";
				} catch (Exception e) {
					e.printStackTrace();
					xml +="<code>2</code><message>数据删除失败</message></head></root>";
				}
			}else{
				xml +="<code>2</code><message>操作类型无法识别</message></head></root>";
			}
		}else{
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message><rownum>0</rownum></head></root>";
		}
		return xml;
	}

	@Override
	public String reservationQuery(String xmls) {
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><QueryCondition><sfzmhm>123</sfzmhm><yykssj>2015-8-25 16:53:15</yykssj><yyjssj>2015-8-25 16:53:22</yyjssj><dtbh>A</dtbh><glbm>371300000400</glbm></QueryCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String sfzmhm = map.get("sfzmhm");
		String yykssj = map.get("yykssj");
		String yyjssj = map.get("yyjssj");
		String dtbh = map.get("dtbh");
		String glbm = map.get("glbm");
		Reservation res = new Reservation();
		res.setSfzmhm(sfzmhm);
		res.setYykssj(yykssj);
		res.setYyjssj(yyjssj);
		res.setDeptCode(glbm);
		res.setDeptHall(dtbh);
		if (deptCode.equals(glbm)) {
			try {
				List<Reservation> list = reservationService.queryReservation(res);
				if (null != list && list.size()>0) {
					xml +="<code>1</code><message>数据查询成功</message><rownum>"+list.size()+"</rownum></head>";
					xml +="<body><queue>";
					for (int i = 0; i < list.size(); i++) {
						xml +="<yyxx><zjlx>"+list.get(i).getZjlx()+"</zjlx>";
						xml +="<sfzmhm>"+list.get(i).getSfzmhm()+"</sfzmhm>";
						xml +="<ywlx>"+list.get(i).getYwlx()+"</ywlx>";
						xml +="<yykssj>"+list.get(i).getYykssj()+"</yykssj>";
						xml +="<yyjssj>"+list.get(i).getYyjssj()+"</yyjssj>";
						xml +="<dtbh>"+list.get(i).getDeptHall()+"</dtbh>";
						xml +="<glbm>"+list.get(i).getDeptCode()+"</glbm></yyxx>";
					}
					xml +="</queue></body></root>";
				}else{
					xml +="<code>1</code><message>无数据信息</message><rownum>0</rownum></head></root>";
				}
			} catch (Exception e) {
				e.printStackTrace();
				xml +="<code>2</code><message>数据查询失败</message></head></root>";
			}
			
		}else {
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message><rownum>0</rownum></head></root>";
		}
		return xml;
	}

	@Override
	public String businessQueryForReservation(String xmls) {
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><QueryCondition><dtbh>A</dtbh><glbm>371300000400</glbm></QueryCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String dtbh = map.get("dtbh");
		String glbm = map.get("glbm");
		if (deptCode.equals(glbm)) {
			try {
				List<Business> list = businessService.getBusinessList("", "", "", "", glbm, dtbh);
				List<Business> listVo = new ArrayList<Business>();
				if (null != list && list.size()>0) {
					for (int i = 0; i < list.size(); i++) {
						if ("0".equals(list.get(i).getOutflag())) {
							listVo.add(list.get(i));
						}
					}
					if (null != listVo && listVo.size()>0) {
						xml +="<code>1</code><message>数据查询成功</message><rownum>"+listVo.size()+"</rownum></head>";
						xml +="<body><queue>";
						for (int i = 0; i < listVo.size(); i++) {
							xml +="<ywlxxx><ywlxbh>"+list.get(i).getId()+"</ywlxbh>";
							xml +="<ywlxmc>"+list.get(i).getName()+"</ywlxmc>";
							xml +="<dtbh>"+deptHall+"</dtbh>";
							xml +="<glbm>"+deptCode+"</glbm></ywlxxx>";
						}
						xml +="</queue></body></root>";
					}else {
						xml +="<code>1</code><message>无数据信息</message><rownum>0</rownum></head></root>";
					}
				}else {
					xml +="<code>1</code><message>无数据信息</message><rownum>0</rownum></head></root>";
				}
			} catch (Exception e) {
				e.printStackTrace();
				xml +="<code>2</code><message>数据查询失败</message></head></root>";
			}
		}else {
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message><rownum>0</rownum></head></root>";
		}
		return xml;
	}
	
	@Override
	public String codeQueryForReservation(String xmls) {
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><QueryCondition><dtbh>A</dtbh><glbm>371300000400</glbm></QueryCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String dtbh = map.get("dtbh");
		String glbm = map.get("glbm");
		if (deptCode.equals(glbm)) {
			List<Code> listCode = codeService.getAllGredentials("0001", deptCode, deptHall);
			if (null !=listCode &&listCode.size()>0) {
				xml +="<code>1</code><message>数据查询成功</message><rownum>"+listCode.size()+"</rownum></head>";
				xml +="<body><queue>";
				for (int i = 0; i < listCode.size(); i++) {
					xml +="<zjlxxx><zjlx>"+listCode.get(i).getDm()+"</zjlx>";
					xml +="<zjlxmc>"+listCode.get(i).getMc()+"</zjlxmc></zjlxxx>";
				}
				xml +="</queue></body></root>";
			}else {
				xml +="<code>1</code><message>无数据信息</message><rownum>0</rownum></head></root>";
			}
		}else {
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message><rownum>0</rownum></head></root>";
		}
		return xml;
	}
	@Override
	public String realTimeMonitoring(String xmls) {
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><QueryCondition><dtbh>A</dtbh><glbm>371300000400</glbm></QueryCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number number = new Number();
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String dtbh = map.get("dtbh");
		String glbm = map.get("glbm");
		number.setDeptCode(glbm);
		number.setDeptHall(dtbh);
		if (deptCode.equals(glbm)) {
			try {
				List<Number> numbersCountList = numberService.showWaitRs(number);//查询业务类型等待人数
				List<Screen> shulList = setWindowService.getCountShul(glbm, dtbh);
				if (null !=shulList && shulList.size()>0) {
					xml +="<code>1</code><message>数据查询成功</message></head>";
					xml +="<body><queue>";
					xml +="<zqhrs>"+shulList.get(0).getQhrs()+"</zqhrs>";//总取号人数
					xml +="<yjhrs>"+shulList.get(0).getJhsl()+"</yjhrs>";//已叫号人数
					xml +="<wjhrs>"+shulList.get(0).getWjhsl()+"</wjhrs>";//未叫号人数
					if (null !=numbersCountList && numbersCountList.size()>0) {
						for (int i = 0; i < numbersCountList.size(); i++) {
							xml +="<ywlxmc>"+numbersCountList.get(i).getTypeName()+"</ywlxmc>";
							xml +="<ywlxpd>"+numbersCountList.get(i).getTypeCount()+"</ywlxpd>";
						}
					}
					xml +="</queue></body></root>";
				}else {
					xml +="<code>1</code><message>无数据信息</message></head></root>";
				}
			} catch (Exception e) {
				e.printStackTrace();
				xml +="<code>2</code><message>数据查询失败</message></head></root>";
			}
			
		}else {
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message></head></root>";
		}
		return xml;
	}
	@Override
	public String queryDept(String xmls) {
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><QueryCondition><dtbh>A</dtbh><glbm>371300000400</glbm></QueryCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String dtbh = map.get("dtbh");
		String glbm = map.get("glbm");
		if (deptCode.equals(glbm)) {
			try {
				List<Dept> list = deptService.getDeptList(new Dept());
				if (null !=list && list.size()>0) {
					xml +="<code>1</code><message>数据查询成功</message><rownum>"+list.size()+"</rownum></head>";
					xml +="<body><queue>";
					for (int i = 0; i < list.size(); i++) {
						xml +="<dtxx><dtmc>"+list.get(i).getDeptname()+"</dtmc>";
						xml +="<dtbh>"+list.get(i).getDepthall()+"</dtbh>";
						xml +="<glbm>"+list.get(i).getDeptcode()+"</glbm>";
						xml +="<ip>"+list.get(i).getServersip()+"</ip></dtxx>";
					}
					xml +="</queue></body></root>";
				}else {
					xml +="<code>1</code><message>无数据信息</message><rownum>0</rownum></head></root>";
				}
			} catch (Exception e) {
				e.printStackTrace();
				xml +="<code>2</code><message>数据查询失败</message></head></root>";
			}
			
		}else {
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message></head></root>";
		}
		return xml;
	}
	@Override
	public String tsLzxx(String xmls) {
		System.out.println(xmls);
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><WriteCondition><xxbh>1</xxbh><nr>1</nr><bj>1</bj><glbm>371300000400</glbm></WriteCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String xml = "",datas="";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String glbm = map.get("glbm");
		String xxbh = map.get("xxbh");
		String nr = map.get("nr");
		String bj = map.get("bj");
		if (deptCode.equals(glbm)){
			if (!"".equals(xxbh) && !"".equals(nr) && !"".equals(bj) && null != xxbh && null != nr && null != bj) {
				datas = bj+"@"+xxbh+"@"+nr;
				try {
					publisher.publish(new tsLzxx(datas));
					xml +="<code>1</code><message>发送领证信息成功!</message></head></root>";
				} catch (Exception e) {
					xml +="<code>2</code><message>推送页面没有打开!</message></head></root>";
				}
			}else {
				xml +="<code>2</code><message>发送的内容或者编号为空,不能成功显示</message></head></root>";
			}
		}else {
			xml +="<code>2</code><message>此调用未经授权,请联系相关人员</message></head></root>";
		}
		return xml;
	}
	@Override
	public String crjhxx(String xmls) {
		System.out.println(xmls);
		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><WriteCondition><yhdh>123</yhdh><barid>1</barid><sxh>00001</sxh></WriteCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number number = new Number();
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String yhdh = map.get("yhdh");
		String barid = map.get("barid");
		String sxh = map.get("sxh");
		List<Business> list = new ArrayList<Business>();
		try {
			list = businessService.getBusinessList(null, null, null, null, deptCode, deptHall);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (!"".equals(yhdh) && !"".equals(barid) && !"".equals(sxh) & null != yhdh && null != barid && null != sxh) {
			if (sxh.length() == 5) {
				number.setCode(yhdh);
				number.setBarId(barid);
				number.setQueueCode(list.get(0).getQueueCode());
				number.setBusinessType(list.get(0).getId());
				number.setClientno(deptCode+deptHall+sxh);
				number.setDeptCode(deptCode);
				number.setDeptHall(deptHall);
				try {
					numberService.saveSxh(number);
//					CallOutStatusAction a=new CallOutStatusAction();
//					a.yiChangDHHC();
					xml +="<code>1</code><message>插入数据成功</message></head></root>";
				} catch (Exception e) {
					xml +="<code>2</code><message>插入数据失败</message></head></root>";
					e.printStackTrace();
				}
			}else {
				xml +="<code>2</code><message>顺序号长度不符合要求,不能插入数据</message></head></root>";
			}
		}else {
			xml +="<code>2</code><message>字段为空,不能插入数据</message></head></root>";
		}
		return xml;
	}
	
	
	@Resource
    private WebServiceContext wsContext;
	@Override
	public String tszpl(String xmls) {
		System.out.println("xmls="+xmls);
		String jym ="";
//		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><WriteCondition><key>6CDFFDDAD8826BACDAE3FFBEBBFBD6AD</key><bj>1</bj><message>张三王明武</message><barid>1</barid><img>data:image/jpeg;base64,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</img></WriteCondition></root>";
//		String hqxml = "<?xml version='1.0' encoding='GBK'?><root><WriteCondition><key>6CDFFDDAD8826BACDAE3FFBEBBFBD6AD</key><bj>2</bj><message>张三王明武</message><barid>1</barid><img>1231231231231232131231231</img></WriteCondition></root>";
		//获取对象
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest request = (HttpServletRequest)(mc.get(MessageContext.SERVLET_REQUEST));
        //获取客户端IP
        String remortAddress = request.getRemoteAddr();
        System.out.println("remortAddress:"+remortAddress);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number number = new Number();
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String barid = map.get("barid");
		String img = map.get("img");
		String message = map.get("message");
		String bj = map.get("bj");
		List<Number> list = new ArrayList<Number>();
		List<Code> clist = new ArrayList<Code>();
		Code code = new Code();
		code.setIp(remortAddress);
		code.setDeptcode(deptCode);
		code.setDepthall(deptHall);
		try {
			clist = codeService.querySQXX(code);
		} catch (Exception e2) {
			xml +="<code>2</code><message>查询授权信息异常</message></head></root>";
			e2.printStackTrace();
		}
		number.setBarId(barid);
		number.setDeptCode(deptCode);
		number.setDeptHall(deptHall);
		if (clist.size() >0) {
			jym = deptCode+"#"+deptHall+"#"+clist.get(0).getIp();
			try {
				jym = codeService.jm(jym);
			} catch (Exception e1) {
				xml +="<code>2</code><message>授权码查询异常</message></head></root>";
				e1.printStackTrace();
			}
			if (!"".equals(map.get("key")) && null != map.get("key") && jym.equals(map.get("key"))) {
				try {
					list = numberDao.getBaripByBarid(number);
				} catch (Exception e) {
					xml +="<code>2</code><message>查询窗口ip失败</message></head></root>";
					e.printStackTrace();
				}
				String datas="";
				if (img.length() > 0 && list.size() > 0) {
					Employee emp = new Employee();
					emp.setLoginIp(list.get(0).getBarIp());
					List<Employee> employee = employeeService.getEmpList(emp);
					if(null!=employee && !employee.isEmpty()){
						for(Employee a:employee){
							if(a.getWSIp()!=null){
								datas = a.getWSIp() + "@" + img + "@" +message + "@" + bj;
								break;
							}else {
								//datas = list.get(0).getBarIp() + "@" + img + "@" + message + "@" +bj;
							}
						}
					}
				
					publisher.publish(new DualScreenEventIMG(datas));
//					publisher.publish(new ChuangKouPing("127.0.0.1@22@业务受理@A0020"));
//					publisher.publish(new ChuangKouPing("192.168.2.157@23@业务办理@A0002"));
					xml +="<code>1</code><message>照片推送成功</message></head></root>";
				}else {
					xml +="<code>2</code><message>照片流为空或者窗口号不正确</message></head></root>";	
				}
			}else{
				xml +="<code>2</code><message>非法授权调用</message></head></root>";
			}
		}else{
			xml +="<code>2</code><message>调用ip未授权</message></head></root>";
		}
		return xml;
	}
	
	
	@Override
	public String wxqh(String xmls) {
		System.out.println(xmls);
		String csxml = "<?xml version='1.0' encoding='GBK'?><root><WriteCondition><key>6CDFFDDAD8826BACDAE3FFBEBBFBD6AD</key><idnumber>432314242424</idnumber><name>某某某</name><ywid>19</ywid></WriteCondition></root>";
		HashMap<String, String> map = new HashMap<String, String>();
		map = XMLUtils.readXMLForQueueOut(xmls);
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String xml = "";
		xml += "<?xml version='1.0' encoding='GBK'?><root><head>";
		String jym ="";
		String idnumber = map.get("idnumber");
		String hyxx ="";
		try {
			hyxx = numberService.qhsfzyz(idnumber, deptCode, deptHall);
		} catch (Exception e3) {
			xml +="<code>2</code><message>身份核验异常</message></head></root>";
			e3.printStackTrace();
		}
		String DBidnumber = map.get("DBidnumber");
		String DBname = map.get("DBname");
		String name = map.get("name");
		String ywid = map.get("ywid");
		if(idnumber.isEmpty() || idnumber.isEmpty() || idnumber.isEmpty()){
			xml +="<code>2</code><message>取号参数有空</message></head></root>";
			System.out.println("wx取号接口返回数据----"+xml);
			return xml;
		}
		Number number = new Number();
		List<Business> Busines = new ArrayList<Business>();
		
		//getNotAgentByIdCardCount 根据身份证获取非代理人当月取号数量
		
		//获取对象
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest request = (HttpServletRequest)(mc.get(MessageContext.SERVLET_REQUEST));
        //获取客户端IP
        String remortAddress = request.getRemoteAddr();
        System.out.println("请求地址:"+remortAddress);
        
        List<Code> clist = new ArrayList<Code>();
		Code code = new Code();
		code.setIp(remortAddress);
		code.setDeptcode(deptCode);
		code.setDepthall(deptHall);
		try {
			clist = codeService.querySQXX(code);
		} catch (Exception e2) {
			xml +="<code>2</code><message>查询授权信息异常</message></head></root>";
			e2.printStackTrace();
		}
		if (clist.size() >0) {
			jym = deptCode+"#"+deptHall+"#"+clist.get(0).getIp();
			try {
				jym = codeService.jm(jym);
				System.out.println("请求的授权码"+map.get("key")+"授权码---"+jym);
			} catch (Exception e1) {
				xml +="<code>2</code><message>授权码查询异常</message></head></root>";
				e1.printStackTrace();
			}
			if (!"".equals(map.get("key")) && null != map.get("key") && jym.equals(map.get("key"))) {
				if(!"".equals(hyxx)){
					xml +="<code>2</code><message>"+hyxx+"</message></head></root>";
					System.out.println("wx取号接口返回数据----"+xml);
					return xml;
				}
				
				try {
					Busines = businessService.getBusinessList(ywid, "", "", "", deptCode, deptHall);
					System.out.println(Busines.size());
					if(null!=Busines && Busines.size()>0){
						number.setQueueCode(Busines.get(0).getQueueCode());//队列编号
						number.setBusinessPrefix(Busines.get(0).getPreNum());// 队列前缀
						number.setTypeName(Busines.get(0).getName());// 业务类型名称
					}else{
						xml +="<code>2</code><message>业务类型异常</message></head></root>";
						System.out.println("wx取号接口返回数据----"+xml);
						return xml;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				PrintInfo info = new PrintInfo();
				number.setBusinessType(ywid);// 业务类型编号
				number.setIDNumber(idnumber.trim());//本人身份证
				number.setPhoneNumber("");
				number.setCarType("");
				number.setCarNumber("");
				//if (null != DBidnumber && !"".equals(DBidnumber)) {
					number.setIDNumberB("");//代办人身份证
				//}
				number.setServerIp(remortAddress);//请求的IP地址
				number.setFlag("01");//业务类型
				number.setIDType("A");//身份证类别
				
				number.setPersonType("1");// 人员类型
				number.setBusinessCount("1");// 业务笔数
				number.setNameA(name);//本人姓名
				number.setNameB(DBname);//代办人姓名
				
				try {
					info = numberService.getNewNumber(number);
				} catch (Exception e) {
					if (e instanceof SaveException) {
						System.out.println("保存数据到本地数据库失败，<br/>请查看当天日志");
						xml +="<code>2</code><message>微信取号失败</message></head></root>";
						e.printStackTrace();
					} else {
						xml +="<code>2</code><message>微信取号失败</message></head></root>";
						System.out.println("在执行过程中发生异常，异常信息请查看当天日志");
						e.printStackTrace();
					}
				}
				if(info.isSuccess()){
					xml +="<code>1</code><message>微信取号成功</message><sxh>"+deptCode+deptHall+info.getSerialNum()+"</sxh><peoNum>"+info.getPeoNum()+"</peoNum></head></root>";
				}else{
					xml +="<code>2</code><message>取号出现异常</message></head></root>";
				}
			}else{
				xml +="<code>2</code><message>非法授权调用</message></head></root>";
			}
		}else{
			xml +="<code>2</code><message>调用ip未授权</message></head></root>";
		}
		System.out.println("wx取号接口返回数据----"+xml);
		return xml;
	}
	
	
//	data:image/jpeg;base64,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	
	
	
	
	
	
	
	
}
