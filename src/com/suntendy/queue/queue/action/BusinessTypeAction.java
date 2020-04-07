package com.suntendy.queue.queue.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.business.domain.Business;
import com.suntendy.queue.business.service.IBusinessService;
import com.suntendy.queue.lzxx.domain.Lzxx;
import com.suntendy.queue.queue.domain.Code;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.Queue;
import com.suntendy.queue.queue.service.ICodeService;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.queue.service.IQueueService;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.scriptsession.event.DualScreenEvent;
import com.suntendy.queue.util.trff.TrffClient;
import com.suntendy.queue.util.trff.TrffUtils;
import com.suntendy.queue.util.trff.XMLUtils;
import com.suntendy.queue.window.domain.Screen;
import com.suntendy.queue.window.service.ISetWindowService;

/*******************************************************************************
 * 描述: 获取业务类型 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-11-20 13:48:42 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class BusinessTypeAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private IBusinessService businessService;
	private IQueueService queueService;
	private INumberService numberService;
	private ICodeService codeService;
	private ISetWindowService setWindowService;

	public ISetWindowService getSetWindowService() {
		return setWindowService;
	}

	public void setSetWindowService(ISetWindowService setWindowService) {
		this.setWindowService = setWindowService;
	}

	public void setBusinessService(IBusinessService businessService) {
		this.businessService = businessService;
	}

	@Override
	public String execute() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String iscywf = cacheManager.getSystemConfig("iscywf");
		String cywffs = cacheManager.getSystemConfig("cywffs");
		String isOpenTztd = cacheManager.getSystemConfig("isOpenTztd");//是否启用通知提档
		String ismessage = cacheManager.getSystemConfig("ismessage");//是否启用短信通知
		String sfkqcyyw = cacheManager.getSystemConfig("sfkqcyyw");//是否开启查验业务
		String xzywmewm = cacheManager.getSystemConfig("xzywmewm");//选择一维码或二维码
		String yybj =null;//预约标记0正常1预约页面
		yybj = this.getRequest().getParameter("yybj");
		if ("".equals(iscywf) || iscywf == null) {
			iscywf="1";
		}
		List<Business> bsnsListVo = businessService.getBusinessList("", "", "", "", deptCode,deptHall);
		List<Business> bsnsList = new ArrayList<Business>();
		if (null != bsnsListVo && bsnsListVo.size()>0) {
			for (int i = 0; i < bsnsListVo.size(); i++) {
				if ("1".equals(bsnsListVo.get(i).getOutflag()) && null == yybj) {
					bsnsList.add(bsnsListVo.get(i));
				}else if("0".equals(bsnsListVo.get(i).getOutflag()) && "1".equals(yybj)){
					bsnsList.add(bsnsListVo.get(i));
				}
			}
		}
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberService.showWaitRs(countnum);//查询等待人数
		JSONObject datas = new JSONObject();
		if (null != bsnsList && !bsnsList.isEmpty()) {
			datas.put("isSuccess", true);
			datas.put("iscywf", iscywf);
			datas.put("cywffs", cywffs);
			datas.put("isOpenTztd", isOpenTztd);
			datas.put("ismessage", ismessage);
			datas.put("sfkqcyyw", sfkqcyyw);
			
			JSONArray typeBsns = new JSONArray();
			for (Business bsns : bsnsList) {
				JSONObject obj = new JSONObject();
				//判断是否存在上一级
				Queue queue = null;
				queue = queueService.getQueueByCode(bsns.getQueueCode(), deptCode, deptHall);
				if(null == queue){
					String helpInfo=bsns.getHelp_info();
					String nameAndShul=bsns.getName()+"@0人";
						for (Number nu : numbersCountList) {
							if(nu.getId().equals(bsns.getId())){
								String shul ="0";
								if(null != nu.getTypeCount()){
									shul = nu.getTypeCount();
								}
								nameAndShul= bsns.getName()+"@"+shul+"人";
								break;
							}
						}
						obj.put("helps", helpInfo);
						obj.put("name", nameAndShul);
						obj.put("id", bsns.getId());//业务类型编号
						obj.put("names",bsns.getName());
						obj.put("flag", bsns.getFlag());//一级菜单编号
						obj.put("prefix", bsns.getPreNum());//前缀
						obj.put("code", bsns.getQueueCode());//队列编号
						obj.put("isOpenTztd", bsns.getIsOpenTztd());
						obj.put("ismessage", ismessage);
						obj.put("sfkqcyyw", sfkqcyyw);//是否开启查验业务
						obj.put("xzywmewm", xzywmewm);//选择一维码或二维码
						obj.put("isOpenZhiWen", bsns.getIsOpenZhiWen());
						obj.put("twotype", bsns.getTwotype());
						obj.put("waitingarea", bsns.getWaitingarea());
						obj.put("dybd", bsns.getBiaodan());
						String bdywmc = "";
						if (!"".equals(bsns.getBdywmc()) && bsns.getBdywmc() != null && bsns.getBdywmc().split("@").length>1) {
							if (bsns.getBdywmc().split(",").length>0) {
								for (int j = 0; j < bsns.getBdywmc().split(",").length; j++) {
									bdywmc += "H" + bsns.getBdywmc().split(",")[j].split("@")[1];
								}
							}
							bdywmc = bdywmc.substring(1);
							obj.put("bdywmc", bdywmc);
							System.out.println("表单业务名称1="+bdywmc);
						}else {
							obj.put("bdywmc", "no");
						}
						typeBsns.put(obj);
						datas.put("d" + bsns.getId(), obj);
						datas.put("datas", typeBsns);
					
				}
			}
		} else {
			datas.put("isSuccess", false);
			datas.put("error", "没有可办理的业务类型");
		}
		this.getResponse("text/html").getWriter().println(datas.toString());
		
		return null;
	}
	
	public void getYWandScreenInfo()throws Exception{
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode=cacheManager.getOfDeptCache("deptCode");
		String deptHall=cacheManager.getOfDeptCache("deptHall");
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		JSONObject datas = new JSONObject();
		List<Number> numbersCountList = numberService.queryywlxAndddrs(countnum);//这个才是查询业务类型及所对应的等待人数
		List<Screen> ckList = setWindowService.queryEveryScreenYWL(deptCode,deptHall);//查询每个业务窗口的叫号办理信息
		List<Number> numbersDlCountList = numberService.querydllxAndddrs(countnum);//这个才是查询业务类型及所对应的等待人数
		ArrayList<JSONObject> ywAndddrs = new ArrayList<JSONObject>(); //业务对应等待人数
		for(Number numbertemp:numbersCountList){
			JSONObject temp = new JSONObject();
			temp.put("id", numbertemp.getId());
			temp.put("name", numbertemp.getTypeName());
			temp.put("ddrs", numbertemp.getTypeCount());
			ywAndddrs.add(temp);
		}
		ArrayList<JSONObject> screenAndywl = new ArrayList<JSONObject>(); //窗口对应业务量
		for(Screen screentemp:ckList){
			JSONObject temp = new JSONObject();
			temp.put("windowNum", screentemp.getAddress());
			temp.put("ywl", screentemp.getJhsl());
			screenAndywl.add(temp);
		}
		ArrayList<JSONObject> dlAndddrs = new ArrayList<JSONObject>(); //队列对应等待人数
		for(Number numbertemp:numbersDlCountList){
			JSONObject temp = new JSONObject();
			temp.put("id", numbertemp.getId());
			temp.put("name", numbertemp.getQueueName());
			temp.put("dlddrs", numbertemp.getQueueCount());
			dlAndddrs.add(temp);
		}
		datas.put("ywAndddrs", ywAndddrs);
		datas.put("screenAndywl", screenAndywl);
		datas.put("dlAndddrs", dlAndddrs);
		this.getResponse("text/html").getWriter().println(datas.toString());
		
	}
	
	public String checkStatus() throws Exception{
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		JSONObject datas = new JSONObject();
		String flag = this.getRequest().getParameter("flag");
		Number number = new Number();
		String chepainum = "";
		String IDNumber = "";
		String jdctypes = "";
		if ("01".equals(flag)) {
			chepainum = this.getRequest().getParameter("chepainum");
			jdctypes  = this.getRequest().getParameter("jdctypes");
			number.setHphm(chepainum);
			number.setHpzl(jdctypes);
			List<Number> listjdc = new ArrayList<Number>();
			try {
				listjdc = numberService.checkJDCStatus(number);
				if (listjdc.size()>0) {
					for (int i = 0; i < listjdc.size(); i++) {
						Number nvo = listjdc.get(i);
						if ("01".equals(nvo.getHpzl())) {
							nvo.setHpzl("大型汽车");
						}else if ("02".equals(nvo.getHpzl())) {
							nvo.setHpzl("小型汽车");
						}else if ("15".equals(nvo.getHpzl())) {
							nvo.setHpzl("挂车");
						}else if ("16".equals(nvo.getHpzl())) {
							nvo.setHpzl("教练汽车");
						}else if ("23".equals(nvo.getHpzl())) {
							nvo.setHpzl("警用汽车");
						}
						
						String ztvo = "";
						for (int j = 0; j < nvo.getZt().length(); j++) {
							if ("A".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="正常.";
							}else if ("B".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="转出.";
							}else if ("C".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="被盗抢.";
							}else if ("D".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="停驶.";
							}else if ("E".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="注销.";
							}else if ("G".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="违法未处理.";
							}else if ("H".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="海关监管.";
							}else if ("I".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="事故未处理.";
							}else if ("J".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="嫌疑车.";
							}else if ("K".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="查封.";
							}else if ("L".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="扣留.";
							}else if ("M".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="强制注销.";
							}else if ("N".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="事故逃逸.";
							}else if ("O".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="锁定.";
							}
						}
						nvo.setZt(ztvo);
						
						if ("0".equals(nvo.getDybj())) {
							nvo.setDybj("正常");
						}else if ("1".equals(nvo.getDybj())) {
							nvo.setDybj("抵押");
						}
						
						
						
					}
					datas.put("success", true);
					datas.put("hpzl", listjdc.get(0).getHpzl());
					datas.put("hphm", listjdc.get(0).getHphm());
					datas.put("syr", listjdc.get(0).getSyr());
					datas.put("zt", listjdc.get(0).getZt());
					datas.put("dybj", listjdc.get(0).getDybj());
					datas.put("yxqz", listjdc.get(0).getYxqz());
					datas.put("qzbfqz", listjdc.get(0).getQzbfqz());
					datas.put("djzsbh", listjdc.get(0).getDjzsbh());
				}else{
					datas.put("success", false);
				}
			} catch (Exception e) {
				
					datas.put("success", false);
				
				e.printStackTrace();
			}
		
		}else if ("02".equals(flag)) {
			IDNumber = this.getRequest().getParameter("IDNumber");
			number.setSfzmhm(IDNumber);
			List<Number> listjsr = new ArrayList<Number>();
			try {
				listjsr = numberService.checkJSRStatus(number);
				if (listjsr.size()>0) {
					
					
					for (int i = 0; i < listjsr.size(); i++) {
						Number nvo = listjsr.get(i);
						
						String ztvo = "";
						for (int j = 0; j < nvo.getZt().length(); j++) {
							if ("A".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="正常.";
							}else if ("B".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="超分.";
							}else if ("C".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="转出.";
							}else if ("D".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="暂扣.";
							}else if ("E".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="撤销.";
							}else if ("F".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="吊销.";
							}else if ("G".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="注销.";
							}else if ("H".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="违法未处理.";
							}else if ("I".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="事故未处理.";
							}else if ("J".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="停止使用.";
							}else if ("K".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="扣押.";
							}else if ("L".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="锁定.";
							}else if ("M".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="逾期未换证.";
							}else if ("N".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="延期换证.";
							}else if ("P".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="延期体检.";
							}else if ("R".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="注销可恢复.";
							}else if ("S".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="逾期未审验.";
							}else if ("T".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="延期审验.";
							}else if ("U".equals(nvo.getZt().substring(j, j+1))) {
								ztvo +="扣留.";
							}
						}
						nvo.setZt(ztvo);
						
					}
					
					
					
					datas.put("success", true);
					datas.put("xm", listjsr.get(0).getXm());
					datas.put("zjcx", listjsr.get(0).getZjcx());
					datas.put("zt", listjsr.get(0).getZt());
					datas.put("ljjf", listjsr.get(0).getLjjf());
					datas.put("yxqz", listjsr.get(0).getYxqz());
					datas.put("syyxqz", listjsr.get(0).getSyyxqz());
				}else {
					datas.put("success", false);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				datas.put("success", false);
				e.printStackTrace();
			}
		}else{
			datas.put("success", false);
		}
		datas.put("flag", flag);
		this.getResponse("text/html").getWriter().println(datas.toString());
		
		
		return null;
	}
	
	public String checkStatusZD() throws Exception{
		JSONObject datas = new JSONObject();
		String flag = this.getRequest().getParameter("flag");
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String chepainum = "";
		String IDNumber = "";
		String jdctypes = "";
		String id = this.getRequest().getParameter("id");
		System.out.println("id="+id);
		String[] bkblyw = null;
		List<Business> listbus = businessService.getBusinessList(id, "", "", "", deptCode, deptHall);
		if (!"".equals(listbus.get(0).getBkbl()) && null != listbus.get(0).getBkbl()) {
			bkblyw = listbus.get(0).getBkbl().split(",");
		}
		
		if ("01".equals(flag)) {
			chepainum = this.getRequest().getParameter("chepainum").toUpperCase();
			jdctypes  = this.getRequest().getParameter("jdctypes");
			String[] result1 = null;
			
			try {
				//调用领证信息查询接口
				System.out.println("自动违法查询--机动车");
				String rows[][] = new String[2][2];
				rows[0][0] = "lsh";
				rows[0][1] = jdctypes+"#"+chepainum;
				rows[1][0] = "zllx";
				rows[1][1] = "5";
				String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
				String lzXML = TrffClient.write_nbjk("02C43", strXML);
				System.out.println("自动违法查询--机动车XML="+lzXML);
//				String lzXML = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>WVWVY73D388005230#AB</message></head></root>";
				String[] result =  XMLUtils.readXML(lzXML);
				if ("1".equals(result[0])) {
					result1 = result[1].split("#");
				}
				System.out.println("违法查询(接口)结果"+result[0]+"---车辆识别代码="+result1[0]+"状态="+result1[1]);
			} catch (Exception e) {
				System.out.println("领证接口(驾驶证)调用失败");
				e.printStackTrace();
			}
			
				if (result1.length==2 && bkblyw != null) {
					String ztvo = "";
					datas.put("success", true);
					for (int j = 0; j < result1[1].length(); j++) {
						System.out.println("result1[1].substring(j, j+1)="+result1[1].substring(j, j+1));
						for (int i = 0; i < bkblyw.length; i++) {
							List<Code> listcode = codeService.getCodeByDmlbAndSxh("2002", bkblyw[i], "1", deptCode, deptHall);
							if (listcode.size()>0) {
								System.out.println("listcode.get(0).getDm()="+listcode.get(0).getDm());
								if (result1[1].substring(j, j+1).equals(listcode.get(0).getDm())) {
									if ("A".equals(result1[1].substring(j, j+1))) {
										ztvo +="正常.";
									}else if ("B".equals(result1[1].substring(j, j+1))) {
										ztvo +="转出.";
									}else if ("C".equals(result1[1].substring(j, j+1))) {
										ztvo +="被盗抢.";
									}else if ("D".equals(result1[1].substring(j, j+1))) {
										ztvo +="停驶.";
									}else if ("E".equals(result1[1].substring(j, j+1))) {
										ztvo +="注销.";
									}else if ("G".equals(result1[1].substring(j, j+1))) {
										ztvo +="违法未处理.";
									}else if ("H".equals(result1[1].substring(j, j+1))) {
										ztvo +="海关监管.";
									}else if ("I".equals(result1[1].substring(j, j+1))) {
										ztvo +="事故未处理.";
									}else if ("J".equals(result1[1].substring(j, j+1))) {
										ztvo +="嫌疑车.";
									}else if ("K".equals(result1[1].substring(j, j+1))) {
										ztvo +="查封.";
									}else if ("L".equals(result1[1].substring(j, j+1))) {
										ztvo +="扣留.";
									}else if ("M".equals(result1[1].substring(j, j+1))) {
										ztvo +="强制注销.";
									}else if ("N".equals(result1[1].substring(j, j+1))) {
										ztvo +="事故逃逸.";
									}else if ("O".equals(result1[1].substring(j, j+1))) {
										ztvo +="锁定.";
									}
									datas.put("success", false);
								}
							}else{
								//datas.put("success", true);
							}
//							break;
						}
//						datas.put("zt", ztvo);
						
						
						
						
						
						
						
						
						
						/*原来的
						if ("O".equals(result1[1].subSequence(j, j+1)) || "K".equals(result1[1].subSequence(j, j+1)) || "C".equals(result1[1].subSequence(j, j+1))
								 || "I".equals(result1[1].subSequence(j, j+1)) || "L".equals(result1[1].subSequence(j, j+1))) {
							if ("A".equals(result1[1].substring(j, j+1))) {
								ztvo +="正常.";
							}else if ("B".equals(result1[1].substring(j, j+1))) {
								ztvo +="转出.";
							}else if ("C".equals(result1[1].substring(j, j+1))) {
								ztvo +="被盗抢.";
							}else if ("D".equals(result1[1].substring(j, j+1))) {
								ztvo +="停驶.";
							}else if ("E".equals(result1[1].substring(j, j+1))) {
								ztvo +="注销.";
							}else if ("G".equals(result1[1].substring(j, j+1))) {
								ztvo +="违法未处理.";
							}else if ("H".equals(result1[1].substring(j, j+1))) {
								ztvo +="海关监管.";
							}else if ("I".equals(result1[1].substring(j, j+1))) {
								ztvo +="事故未处理.";
							}else if ("J".equals(result1[1].substring(j, j+1))) {
								ztvo +="嫌疑车.";
							}else if ("K".equals(result1[1].substring(j, j+1))) {
								ztvo +="查封.";
							}else if ("L".equals(result1[1].substring(j, j+1))) {
								ztvo +="扣留.";
							}else if ("M".equals(result1[1].substring(j, j+1))) {
								ztvo +="强制注销.";
							}else if ("N".equals(result1[1].substring(j, j+1))) {
								ztvo +="事故逃逸.";
							}else if ("O".equals(result1[1].substring(j, j+1))) {
								ztvo +="锁定.";
							}
							datas.put("success", false);
							datas.put("zt", ztvo);
							break;
						}else{
							datas.put("success", true);
						}*/
					}
					datas.put("zt", ztvo);
//					datas.put("zt", result1[1]);
				}else{
					datas.put("success", true);
				}
		
		}else if ("02".equals(flag)) {
			IDNumber = this.getRequest().getParameter("IDNumber");
			String[] result1 = null;
			try {
				//调用领证信息查询接口
				System.out.println("自动违法查询--驾驶人");
				String rows[][] = new String[2][2];
				rows[0][0] = "lsh";
				rows[0][1] = IDNumber;
				rows[1][0] = "zllx";
				rows[1][1] = "6";
				String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
				String lzXML = TrffClient.write_nbjk("02C43", strXML);
				System.out.println("自动违法查询--驾驶人XML="+lzXML);
//				String lzXML = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>640100878988#C1#2020-04-08#2014-04-08#1#2014-04-08#2020-04-08#0#ABCL#A#王双龙#1#1985-12-04#156#330000#甘肃省环县合道乡陶洼子行政村小王河畔队#640105#宁夏银川市西夏区朔方路散居69号#750021##13895308918#505289</message></head></root>";
				String[] result =  XMLUtils.readXML(lzXML);
//				String[] result =  {"1","未去到身份证明号码为520123198509105816的驾驶证信息"}; 
			
				if ("1".equals(result[0])) {
					result1 = result[1].split("#");
					if(result1.length>1){
						System.out.println("违法查询(接口)结果"+result[0]+"---档案编号="+result1[0]+"状态="+result1[8]);
					}else {
						System.out.println(result[1]);
					}
				}
				
			} catch (Exception e) {
				System.out.println("领证接口(驾驶证)调用失败");
				e.printStackTrace();
			}
				if(result1.length>1){
					if (result1.length>=9 && bkblyw != null) {
						String ztvo = "";
						datas.put("success", true);
						for (int j = 0; j < result1[8].length(); j++) {
							System.out.println("result1[8].substring(j, j+1)="+result1[8].substring(j, j+1));
							for (int i = 0; i < bkblyw.length; i++) {
								List<Code> listcode = codeService.getCodeByDmlbAndSxh("2003", bkblyw[i], "1", deptCode, deptHall);
								if (listcode.size()>0) {
									System.out.println("listcode.get(0).getDm()="+listcode.get(0).getDm());
									if (result1[8].substring(j, j+1).equals(listcode.get(0).getDm())) {
										if ("A".equals(result1[8].substring(j, j+1))) {
											ztvo +="正常.";
										}else if ("B".equals(result1[8].substring(j, j+1))) {
											ztvo +="超分.";
										}else if ("C".equals(result1[8].substring(j, j+1))) {
											ztvo +="转出.";
										}else if ("D".equals(result1[8].substring(j, j+1))) {
											ztvo +="暂扣.";
										}else if ("E".equals(result1[8].substring(j, j+1))) {
											ztvo +="撤销.";
										}else if ("F".equals(result1[8].substring(j, j+1))) {
											ztvo +="吊销.";
										}else if ("G".equals(result1[8].substring(j, j+1))) {
											ztvo +="注销.";
										}else if ("H".equals(result1[8].substring(j, j+1))) {
											ztvo +="违法未处理.";
										}else if ("I".equals(result1[8].substring(j, j+1))) {
											ztvo +="事故未处理.";
										}else if ("J".equals(result1[8].substring(j, j+1))) {
											ztvo +="停止使用.";
										}else if ("K".equals(result1[8].substring(j, j+1))) {
											ztvo +="扣押.";
										}else if ("L".equals(result1[8].substring(j, j+1))) {
											ztvo +="锁定.";
										}else if ("M".equals(result1[8].substring(j, j+1))) {
											ztvo +="逾期未换证.";
										}else if ("N".equals(result1[8].substring(j, j+1))) {
											ztvo +="延期换证.";
										}else if ("P".equals(result1[8].substring(j, j+1))) {
											ztvo +="延期体检.";
										}else if ("R".equals(result1[8].substring(j, j+1))) {
											ztvo +="注销可恢复.";
										}else if ("S".equals(result1[8].substring(j, j+1))) {
											ztvo +="逾期未审验.";
										}else if ("T".equals(result1[8].substring(j, j+1))) {
											ztvo +="延期审验.";
										}else if ("U".equals(result1[8].substring(j, j+1))) {
											ztvo +="扣留.";
										}
										datas.put("success", false);
									}
								}else {
									//datas.put("success", true);
								}
								//							break;
							}
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							//						if ("L".equals(result1[8].substring(j, j+1)) || "G".equals(result1[8].substring(j, j+1))
							//								|| "F".equals(result1[8].substring(j, j+1)) || "I".equals(result1[8].substring(j, j+1))) {
							//							if ("A".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="正常.";
							//							}else if ("B".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="超分.";
							//							}else if ("C".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="转出.";
							//							}else if ("D".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="暂扣.";
							//							}else if ("E".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="撤销.";
							//							}else if ("F".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="吊销.";
							//							}else if ("G".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="注销.";
							//							}else if ("H".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="违法未处理.";
							//							}else if ("I".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="事故未处理.";
							//							}else if ("J".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="停止使用.";
							//							}else if ("K".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="扣押.";
							//							}else if ("L".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="锁定.";
							//							}else if ("M".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="逾期未换证.";
							//							}else if ("N".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="延期换证.";
							//							}else if ("P".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="延期体检.";
							//							}else if ("R".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="注销可恢复.";
							//							}else if ("S".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="逾期未审验.";
							//							}else if ("T".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="延期审验.";
							//							}else if ("U".equals(result1[8].substring(j, j+1))) {
							//								ztvo +="扣留.";
							//							}
							//							datas.put("success", false);
							//							datas.put("zt", ztvo);
							//							break;
							//						}else{
							//							datas.put("success", true);
							//						}
							
							
							
							
							
							
							
							
							
							
							
							
							
						}
						datas.put("zt", ztvo);
					}else{
						datas.put("success", true);
					}
				}else {
					datas.put("success", true);
				}
			
		}else{
			datas.put("success", true);
		}
		datas.put("flag", flag);
		this.getResponse("text/html").getWriter().println(datas.toString());
		
		return null;
	}
	
	//接口查询违法状态
	public String checkStatusWf() throws Exception{

		JSONObject datas = new JSONObject();
		String flag = this.getRequest().getParameter("flag");
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String chepainum = "";
		String IDNumber = "";
		String jdctypes = "",zt="";
		String sbkzjsjip="";
		Map<String, Object> result = new HashMap<String, Object>();
		String id = this.getRequest().getParameter("id");
		String[] bkblyw = null;
		List<Business> listbus = businessService.getBusinessList(id, "", "", "", deptCode, deptHall);
		if (!"".equals(listbus.get(0).getBkbl()) && null != listbus.get(0).getBkbl()) {
			bkblyw = listbus.get(0).getBkbl().split(",");
		}
		
		if ("01".equals(flag)) {
			chepainum = this.getRequest().getParameter("chepainum").toUpperCase();
			jdctypes  = this.getRequest().getParameter("jdctypes");
			
			try {
				sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			try {
				result = TrffUtils.query_QueryCondition(jdctypes, chepainum,deptCode,sbkzjsjip);
				if (!result.isEmpty()) {
					for (String s:result.keySet()) {
						Map<String, String> map = (Map<String, String>) result.get(s);
						zt = map.get("zt");
					}
					System.out.println("机动车违法状态查询"+"</br>车牌号码:"+chepainum+"---状态:"+zt+"---状态长度"+zt.length());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
				if (zt.length() >2 && bkblyw != null) {
					String ztvo = "";
					datas.put("success", true);
					for (int j = 0; j < zt.length(); j++) {
						System.out.println("zt.substring(j, j+1)="+zt.substring(j, j+1));
						for (int i = 0; i < bkblyw.length; i++) {
							List<Code> listcode = codeService.getCodeByDmlbAndSxh("2002", bkblyw[i], "1", deptCode, deptHall);
							if (listcode.size()>0) {
								if (zt.substring(j, j+1).equals(listcode.get(0).getDm())) {
									if ("A".equals(zt.substring(j, j+1))) {
										ztvo +="正常.";
									}else if ("B".equals(zt.substring(j, j+1))) {
										ztvo +="转出.";
									}else if ("C".equals(zt.substring(j, j+1))) {
										ztvo +="被盗抢.";
									}else if ("D".equals(zt.substring(j, j+1))) {
										ztvo +="停驶.";
									}else if ("E".equals(zt.substring(j, j+1))) {
										ztvo +="注销.";
									}else if ("G".equals(zt.substring(j, j+1))) {
										ztvo +="违法未处理.";
									}else if ("H".equals(zt.substring(j, j+1))) {
										ztvo +="海关监管.";
									}else if ("I".equals(zt.substring(j, j+1))) {
										ztvo +="事故未处理.";
									}else if ("J".equals(zt.substring(j, j+1))) {
										ztvo +="嫌疑车.";
									}else if ("K".equals(zt.substring(j, j+1))) {
										ztvo +="查封.";
									}else if ("L".equals(zt.substring(j, j+1))) {
										ztvo +="扣留.";
									}else if ("M".equals(zt.substring(j, j+1))) {
										ztvo +="强制注销.";
									}else if ("N".equals(zt.substring(j, j+1))) {
										ztvo +="事故逃逸.";
									}else if ("O".equals(zt.substring(j, j+1))) {
										ztvo +="锁定.";
									}
									datas.put("success", false);
								}
							}
						}
					}
					datas.put("zt", ztvo);
				}else{
					datas.put("success", true);
				}
		
		}else if ("02".equals(flag)) {
			IDNumber = this.getRequest().getParameter("IDNumber");
			try {
				sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			try {
				result = TrffUtils.query_JSR(IDNumber,deptCode,sbkzjsjip);
				if (!result.isEmpty()) {
						Map<String, String> map = (Map<String, String>) result.get("JSRMessage");;
						zt = map.get("zt");
						System.out.println("驾驶人违法状态查询"+"</br>驾驶人身份证:"+IDNumber+"---状态:"+zt);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
					if (zt.length()>1 && bkblyw != null) {
						String ztvo = "";
						datas.put("success", true);
						for (int j = 0; j < zt.length(); j++) {
							for (int i = 0; i < bkblyw.length; i++) {
								List<Code> listcode = codeService.getCodeByDmlbAndSxh("2003", bkblyw[i], "1", deptCode, deptHall);
								if (listcode.size()>0) {
									System.out.println("listcode.get(0).getDm()="+listcode.get(0).getDm());
									if (zt.substring(j, j+1).equals(listcode.get(0).getDm())) {
										if ("A".equals(zt.substring(j, j+1))) {
											ztvo +="正常.";
										}else if ("B".equals(zt.substring(j, j+1))) {
											ztvo +="超分.";
										}else if ("C".equals(zt.substring(j, j+1))) {
											ztvo +="转出.";
										}else if ("D".equals(zt.substring(j, j+1))) {
											ztvo +="暂扣.";
										}else if ("E".equals(zt.substring(j, j+1))) {
											ztvo +="撤销.";
										}else if ("F".equals(zt.substring(j, j+1))) {
											ztvo +="吊销.";
										}else if ("G".equals(zt.substring(j, j+1))) {
											ztvo +="注销.";
										}else if ("H".equals(zt.substring(j, j+1))) {
											ztvo +="违法未处理.";
										}else if ("I".equals(zt.substring(j, j+1))) {
											ztvo +="事故未处理.";
										}else if ("J".equals(zt.substring(j, j+1))) {
											ztvo +="停止使用.";
										}else if ("K".equals(zt.substring(j, j+1))) {
											ztvo +="扣押.";
										}else if ("L".equals(zt.substring(j, j+1))) {
											ztvo +="锁定.";
										}else if ("M".equals(zt.substring(j, j+1))) {
											ztvo +="逾期未换证.";
										}else if ("N".equals(zt.substring(j, j+1))) {
											ztvo +="延期换证.";
										}else if ("P".equals(zt.substring(j, j+1))) {
											ztvo +="延期体检.";
										}else if ("R".equals(zt.substring(j, j+1))) {
											ztvo +="注销可恢复.";
										}else if ("S".equals(zt.substring(j, j+1))) {
											ztvo +="逾期未审验.";
										}else if ("T".equals(zt.substring(j, j+1))) {
											ztvo +="延期审验.";
										}else if ("U".equals(zt.substring(j, j+1))) {
											ztvo +="扣留.";
										}
										datas.put("success", false);
									}
								}
							}
						}
						datas.put("zt", ztvo);
					}else{
						datas.put("success", true);
					}
			
		}else{
			datas.put("success", true);
		}
		datas.put("flag", flag);
		this.getResponse("text/html").getWriter().println(datas.toString());
		
		return null;
	
	}
	
	
	public IQueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(IQueueService queueService) {
		this.queueService = queueService;
	}

	public INumberService getNumberService() {
		return numberService;
	}

	public void setNumberService(INumberService numberService) {
		this.numberService = numberService;
	}

	public ICodeService getCodeService() {
		return codeService;
	}

	public void setCodeService(ICodeService codeService) {
		this.codeService = codeService;
	}
	
}
