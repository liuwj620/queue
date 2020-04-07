package com.suntendy.queue.queue.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.dept.domain.Dept;
import com.suntendy.queue.dept.service.DeptService;
import com.suntendy.queue.lzxx.action.LzckAction;
import com.suntendy.queue.lzxx.domain.Lzxx;
import com.suntendy.queue.lzxx.service.ILzxxService;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.PrintInfo;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.util.DateUtils;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.SaveException;
import com.suntendy.queue.util.exception.TrffException;
import com.suntendy.queue.util.exception.UpdateException;
import com.suntendy.queue.util.scriptsession.event.RzdbTs;
import com.suntendy.queue.util.trff.XMLUtils;
import com.suntendy.queue.window.domain.Screen;
import com.suntendy.queue.window.service.ISetWindowService;

public class QueueJieKou extends BaseAction {
	
	private INumberService numberService;
	private ISetWindowService setWindowService;
	private DeptService deptService;
	private ILzxxService lzxxService;
	private LzckAction lzckAction;
	
	public LzckAction getLzckAction() {
		return lzckAction;
	}
	public void setLzckAction(LzckAction lzckAction) {
		this.lzckAction = lzckAction;
	}
	public ILzxxService getLzxxService() {
		return lzxxService;
	}
	public void setLzxxService(ILzxxService lzxxService) {
		this.lzxxService = lzxxService;
	}
	public DeptService getDeptService() {
		return deptService;
	}
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}
	public INumberService getNumberService() {
		return numberService;
	}
	public void setNumberService(INumberService numberService) {
		this.numberService = numberService;
	}
	public void setSetWindowService(ISetWindowService setWindowService) {
		this.setWindowService = setWindowService;
	}




	public String execute() {
		System.out.println("进入接口");
		
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");//是否启用接口
		String jklx = cacheManager.getSystemConfig("jklx");
		String cz = this.getRequest().getParameter("cz");
		String remortAddress = this.getRequest().getRemoteAddr();
		System.out.println("jklx="+jklx);
		String sbkzjsjip = "";//设备控制计算机ip
		String opType = null;
		String reqdata = null;
		String charset = null;
		String bodyStr = null;
		String respMsg = "";
		String respCode = "";
		String code = "";
		JSONObject outdatas = new JSONObject();
		JSONObject indatas = new JSONObject();
		JSONObject respData = new JSONObject();
		String shijian = DateUtils.dateToString("yyMMdd");
		PrintInfo info = new PrintInfo();
		List<Screen> listScreen = new ArrayList<Screen>();
		String str = this.getRequest().getQueryString();
		try {
			sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		try {
//			BufferedReader bufferedReader = this.getRequest().getReader();		
//			bodyStr = IOUtils.getStringFromReader(bufferedReader);
			
			 BufferedReader br = new BufferedReader(new InputStreamReader(this.getRequest().getInputStream()));
		        String line = null;
		        StringBuilder sb = new StringBuilder();
		        while((line = br.readLine())!=null){
		            sb.append(line);
		        }
		        bodyStr = sb.toString();

		        if ("1".equals(cz)) {
		        	bodyStr = "{'opType':'TMRI_CALLOUT','reqdata':{'jbr':'gaojp','glbm':'"+deptCode+"','ywckjsjip':'"+remortAddress+"'},'charset':'UTF-8'}";//叫号
				}else if ("2".equals(cz)) {
					bodyStr = "{'opType':'TMRI_RECALL','reqdata':{'jbr':'gaojp','jsjip':'"+remortAddress+"','qhxxxlh':'1812061310000005A10001'},'charset':'UTF-8'}";//重呼
				}else if ("3".equals(cz)) {
					bodyStr = "{'opType':'TMRI_SKIP','reqdata':{'jbr':'gaojp','jsjip':'"+remortAddress+"','qhxxxlh':'1812061310000005A10001'},'charset':'UTF-8'}";//过号
				}else if ("4".equals(cz)) {
					bodyStr = "{'opType':'TMRI_EVALUATION','reqdata':{'qhxxxlh':'1812061310000005A0001'},'charset':'UTF-8'}";//发起评价
				}else if ("5".equals(cz)) {
					bodyStr = "{'opType':'TMRI_SUSPEND','reqdata':{'ywckjsjip':'"+remortAddress+"'},'charset':'UTF-8'}";//暂停
				}else if ("6".equals(cz)) {
					bodyStr = "{'opType':'TMRI_RECOVER','reqdata':{'ywckjsjip':'"+remortAddress+"'},'charset':'UTF-8'}";//恢复
				}else if ("7".equals(cz)) {
					bodyStr = "{'opType':'TMRI_RECEIVE','reqdata':{'lsh':'19880216','zzjsjip':'"+remortAddress+"','xm':'张三','pzlx':'02','lzckbh':'1'},'charset':'UTF-8'}";//领证	        
				}else if ("8".equals(cz)) {
					bodyStr = "{'opType':'TMRI_COMPLETE','reqdata':{'qhxxxlh':'1812061310000005A0002'},'charset':'UTF-8'}";
				}
			System.out.println("bodyStr="+bodyStr);
		} catch (IOException e2) {
			System.out.println("解析六合一传值出错");
			e2.printStackTrace();
		}
		if (!"".equals(bodyStr) && null != bodyStr) {
			try {
				JSONObject strjson = new JSONObject(bodyStr);
				opType = strjson.getString("opType");
				reqdata = strjson.getString("reqdata");
				charset = strjson.getString("charset");
				System.out.println("opType="+opType);
				System.out.println("reqdata="+reqdata);
				System.out.println("charset="+charset);
			} catch (JSONException e1) {
				System.out.println("解析reqdata报错");
				e1.printStackTrace();
			}
		}else {
			try {
				this.getResponse("text/html").getWriter().println("此地址为六合一调取排队叫号系统接口地址!<br/>服务器ip地址为:"+sbkzjsjip);
				this.getResponse().flushBuffer();
				return ERROR;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if ("".equals(opType) || "".equals(reqdata)) {//参数完整性
			try {
				respData.put("respData", "");
				respData.put("respCode", 100);
				respData.put("respMsg", "参数不完整!");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				this.getResponse("text/html").getWriter().println(respData);
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ERROR;
		}
		
//		String opType = this.getRequest().getParameter("opType");//接口操作类型(TMRI_CALLOUT:叫号,TMRI_RECALL:重呼,TMRI_SKIP:过号,TMRI_EVALUATION:请求评价,TMRI_SUSPEND:暂停,TMRI_RECOVER:恢复,TMRI_RECEIVE:待领取牌证信息写入)
//		System.out.println("opType="+opType);
//		String reqdata = this.getRequest().getParameter("reqdata");//数据
//		System.out.println("reqdata="+reqdata);
//		String charset = this.getRequest().getParameter("charset");//reqdata编码字符集,仅支持 utf-8/gbk
//		System.out.println("charset="+charset);
		
		
		try {
			indatas = new JSONObject(reqdata);
		} catch (JSONException e1) {
			System.out.println("解析reqdata报错");
			e1.printStackTrace();
		}
		String resultMsg = "";//返回信息
		
		String ywckjsjip = "";//业务窗口计算机IP
		String glbm = "";//管理部门
		String jbr = "";//经办人
		String qhxxxlh = "";//取号信息序列号
		String lsh = "";//流水号
		String xm = "";//申请人姓名
		String pzlx = "";//牌证类型定义：01-驾驶证；02-行驶证；03-登记证书；05-临牌；06-检验合格标志。如果某业务需打印一个以上牌证，则各个牌证类型中间用#号分隔，如02#03#05。
		String zzjsjip = "";//制证计算机ip
		String lzckbh = "";//领证窗口编号
		String pdh = "";//排队号
		String ywlb = "";//业务类别
		String sfzmhm = "";//申请人证件号
		String dlrsfzmhm = "";//代理人证件号
		String qhrxm = "";//取号人姓名
		String qhsj = "";//取号时间
		String rylb = "";//人员类别
		String sbkzjsjbh = "";//设备控制计算机编号
		
		
		
		
//		sbkzjsjip = "127.0.0.1";
		
		
		
		Dept dept = new Dept();
		dept.setServersip(sbkzjsjip);
		List<Dept> listdept = new ArrayList<Dept>();
		try {
			listdept = deptService.getDeptList(dept);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		if (listdept.size()<=0) {
			try {
				respData.put("respData", "");
				respData.put("respCode", 100);
				respData.put("respMsg", "获取设备控制计算机编号失败");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				this.getResponse("text/html").getWriter().println(respData);
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ERROR;
		}
		sbkzjsjbh = listdept.get(0).getSbkzjsjbh();
		System.out.println("sbkzjsjbh="+sbkzjsjbh);
		
		if ("TMRI_CALLOUT".equals(opType)) {//叫号
			System.out.println("进入叫号");
			try {
				ywckjsjip = indatas.getString("ywckjsjip");
				glbm = indatas.getString("glbm");
				jbr = indatas.getString("jbr");
				System.out.println("ywckjsjip="+ywckjsjip);
				System.out.println("glbm="+glbm);
				System.out.println("jbr="+jbr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if ("".equals(ywckjsjip) || "".equals(glbm)) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "参数不完整!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}else if (!glbm.equals(deptCode)) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 400);
					respData.put("respMsg", "管理部门不匹配!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			Screen screen = new Screen();
			screen.setBarip(ywckjsjip);
			try {
				
				listScreen =  setWindowService.querybarid(screen);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (listScreen.size()<=0) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "此窗口未录入!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			
			
			try {
				cacheManager.putYhCache(ywckjsjip, jbr);
				resultMsg = numberService.callNumber_new(jbr, ywckjsjip,"0");
				//1@A0002@01@222@null@@2018-12-09 21:10:15 124
				System.out.println("resultMsg="+resultMsg);
			} catch (Exception e) {
				if (e instanceof UpdateException) {
					resultMsg = "更新号码信息状态[正在办理]失败，<br/>请查看当天日志";
					e.printStackTrace();
				} else if (e instanceof RemoteException
						|| e instanceof TrffException) {
					resultMsg = e.getMessage();
				} else {
					resultMsg = "在执行过程中发生异常，请查看当天日志";
					e.printStackTrace();
				}
			}
			String[] result = resultMsg.split("@");
			
			if ("2".equals(result[0])) {
				qhxxxlh = shijian+sbkzjsjbh+result[1];
				pdh = result[1];
				ywlb = result[2];
				sfzmhm = result[3];
				dlrsfzmhm = result[4];
				qhrxm = result[5];
				qhsj = result[6].substring(0,19);
				if (!"".equals(dlrsfzmhm) && null != dlrsfzmhm) {
					rylb = "2";
				}else {
					rylb = "1";
				}
				respMsg = "叫号成功!";
			}else {
				respMsg = resultMsg;
				String date = DateUtils.dateToString("yyyy-MM-dd HH:mm:ss");
				Number number = new Number();
				number.setBarIp(ywckjsjip);
				number.setCode(jbr);
				number.setDeptCode(deptCode);
				number.setDeptHall(deptHall);
				number.setLrrq(date);
				try {
//					numberService.insertBLXX(number);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
					System.out.println("sbkzjsjip="+sbkzjsjip);
				outdatas.put("sbkzjsjip", sbkzjsjip);
					System.out.println("qhxxxlh="+qhxxxlh);
				outdatas.put("qhxxxlh", qhxxxlh);//格式为6位日期+10位设备控制计算机编号+6位排队号
					System.out.println("pdh="+pdh);
				outdatas.put("pdh", pdh);//serialNum	"A0001" (id=218)
					System.out.println("ywlb="+ywlb);
				outdatas.put("ywlb", ywlb);//01-机动车业务；02-驾驶证业务；04-违法业务      flag	"01" (id=210)
					System.out.println("sfzmhm="+sfzmhm);
				outdatas.put("sfzmhm", sfzmhm);//IDNumber	"123" (id=212)	
					System.out.println("dlrsfzmhm="+dlrsfzmhm);
				outdatas.put("dlrsfzmhm", dlrsfzmhm);//可空  IDNumberB	null	
					System.out.println("qhrxm="+qhrxm);
				outdatas.put("qhrxm", qhrxm);//可空nameA	"" (id=162)	nameB	"" (id=162)	
					System.out.println("qhsj="+qhsj);
				outdatas.put("qhsj", qhsj);//enterTime	"2018-12-09 20:42:23 889" (id=209)	
					System.out.println("rylb="+rylb);
				outdatas.put("rylb", rylb);//1-申请人；2-代理人
				respData.put("respData", outdatas);
				respData.put("respCode", 200);
				respData.put("respMsg", respMsg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				System.out.println(respData.toString());
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else if ("TMRI_RECALL".equals(opType)) {//重呼
			System.out.println("进入重呼!");
			try {
				qhxxxlh = indatas.getString("qhxxxlh");
				ywckjsjip = indatas.getString("jsjip");
				jbr = indatas.getString("jbr");
//				jbr = "CGS903";
				System.out.println("qhxxxlh="+qhxxxlh);
				System.out.println("ywckjsjip="+ywckjsjip);
				System.out.println("jbr="+jbr);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if ("".equals(qhxxxlh) || "".equals(ywckjsjip)) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "参数不完整!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			try {
				resultMsg = numberService.recall(jbr, ywckjsjip);
				System.out.println("resultMsg="+resultMsg);
			} catch (Exception e) {
				if (e instanceof NullPointerException) {
					resultMsg = "未叫号，不能重呼";
					e.printStackTrace();
				} else {
					resultMsg = "在执行过程中发生异常，请查看当天日志";
					e.printStackTrace();
				}
			}
			String[] result = resultMsg.split("@");
			if ("9".equals(result[0])) {
//				code = result[1];
				pdh = result[1];
				qhrxm = "";
				resultMsg = result[2];
				respMsg = result[2];
			}else {
				code = "2";
				respMsg = resultMsg;
			}
			try {
//				outdatas.put("code", code);//1-调用成功；2-调用失败
//				outdatas.put("message", resultMsg);//调用失败时返回错误描述信息
				outdatas.put("qhxxxlh", qhxxxlh);
				outdatas.put("pdh", pdh);
				outdatas.put("qhrxm", qhrxm);
				respData.put("respData", outdatas);
				respData.put("respCode", 200);
				respData.put("respMsg", respMsg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println(respData.toString());
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("TMRI_SKIP".equals(opType)) {//过号
			System.out.println("进入过号");
			try {
				qhxxxlh = indatas.getString("qhxxxlh");
				ywckjsjip = indatas.getString("jsjip");
				jbr = indatas.getString("jbr");
//				jbr = "CGS903";
				System.out.println("qhxxxlh="+qhxxxlh);
				System.out.println("ywckjsjip="+ywckjsjip);
				System.out.println("jbr="+jbr);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if ("".equals(qhxxxlh) || "".equals(ywckjsjip)) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "参数不完整!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			try {
				resultMsg = numberService.pass(jbr, ywckjsjip, "");//过号原因
				System.out.println("resultMsg="+resultMsg);
			} catch (Exception e) {
				if (e instanceof NullPointerException) {
					resultMsg = "未叫号，不能过号!";
					e.printStackTrace();
				} else if (e instanceof UpdateException) {
					resultMsg = "更新号码信息状态[过号]失败，<br/>请查看当天日志";
					e.printStackTrace();
				} else if (e instanceof RemoteException
						|| e instanceof TrffException) {
					resultMsg = e.getMessage();
				} else {
					resultMsg = "在执行过程中发生异常，请查看当天日志";
					e.printStackTrace();
				}
			}
			String[] result = resultMsg.split("@");
			if ("3".equals(result[0])) {
				qhxxxlh = "";
				try {
					resultMsg = numberService.callNumber_new(jbr, ywckjsjip,"0");
					//1@A0002@01@222@null@@2018-12-09 21:10:15 124
					System.out.println("resultMsg="+resultMsg);
				} catch (Exception e) {
					if (e instanceof UpdateException) {
						resultMsg = "更新号码信息状态[正在办理]失败，<br/>请查看当天日志";
						e.printStackTrace();
					} else if (e instanceof RemoteException
							|| e instanceof TrffException) {
						resultMsg = e.getMessage();
					} else {
						resultMsg = "在执行过程中发生异常，请查看当天日志";
						e.printStackTrace();
					}
				}
				String[] resultJH = resultMsg.split("@");
				
				if ("2".equals(resultJH[0])) {
					qhxxxlh = shijian+sbkzjsjbh+resultJH[1];
					pdh = resultJH[1];
					ywlb = resultJH[2];
					sfzmhm = resultJH[3];
					dlrsfzmhm = resultJH[4];
					qhrxm = resultJH[5];
					qhsj = resultJH[6].substring(0,19);
					if (!"".equals(dlrsfzmhm) && null != dlrsfzmhm) {
						rylb = "2";
					}else {
						rylb = "1";
					}
					respMsg = "叫号成功!";
				}else {
					respMsg = resultMsg;
				}
				
			}else {
				respMsg = resultMsg;
			}
			
			try {
					System.out.println("ywckjsjip="+ywckjsjip);
				outdatas.put("ywckjsjip", ywckjsjip);
					System.out.println("sbkzjsjip="+sbkzjsjip);
				outdatas.put("sbkzjsjip", sbkzjsjip);
					System.out.println("qhxxxlh="+qhxxxlh);
				outdatas.put("qhxxxlh",qhxxxlh);
					System.out.println("pdh="+pdh);
				outdatas.put("pdh", pdh);
					System.out.println("ywlb="+ywlb);
				outdatas.put("ywlb", ywlb);
					System.out.println("sfzmhm="+sfzmhm);
				outdatas.put("sfzmhm", sfzmhm);
					System.out.println("dlrsfzmhm="+dlrsfzmhm);
				outdatas.put("dlrsfzmhm", dlrsfzmhm);
					System.out.println("qhrxm="+qhrxm);
				outdatas.put("qhrxm", qhrxm);
					System.out.println("qhsj="+qhsj);
				outdatas.put("qhsj", qhsj);
					System.out.println("rylb="+rylb);
				outdatas.put("rylb", rylb);
				respData.put("respData", outdatas);
				respData.put("respCode", 200);
				respData.put("respMsg", respMsg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("respData="+respData);
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("TMRI_EVALUATION".equals(opType)) {//提交评价
			
			try {
				qhxxxlh = indatas.getString("qhxxxlh");
				jbr = indatas.getString("jbr");
				System.out.println("qhxxxlh="+qhxxxlh);
				System.out.println("jbr="+jbr);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if ("".equals(qhxxxlh)) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "参数不完整!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			pdh = qhxxxlh.substring(16);
			pdh = deptCode+deptHall+pdh;
			System.out.println("pdh="+pdh);
			Number num = new Number();
			num.setClientno(pdh);
			List<Number> listnum = new ArrayList<Number>();
			try {
				listnum = numberService.getvaluerecordByClientno(num);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (listnum.size()<=0) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "查询不到该排队号!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}else {
				jbr = listnum.get(0).getCode();
				ywckjsjip = listnum.get(0).getBarIp();
			}
			try {
				resultMsg = numberService.toEvaluation(jbr, ywckjsjip);
			} catch (Exception e) {
				if (e instanceof NullPointerException) {
					resultMsg = "未叫号，不能评价";
					e.printStackTrace();
				} else if (e instanceof UpdateException) {
					resultMsg = "更新号码信息状态[等待评价]失败，<br/>请查看当天日志";
					e.printStackTrace();
				} else {
					resultMsg = "在执行过程中发生异常，请查看当天日志";
					e.printStackTrace();
				}
			}
			String[] result = resultMsg.split("@");
			if ("4".equals(result[0])) {
				code = result[1];
				resultMsg = result[2];
				respMsg = result[2];
			}else {
				code = "2";
				respMsg = resultMsg;
			}
			try {
				outdatas.put("code", code);//1-调用成功；2-调用失败
				outdatas.put("message", resultMsg);//调用失败时返回错误描述信息
				respData.put("respData", outdatas);
				respData.put("respCode", 200);
				respData.put("respMsg", respMsg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("respData="+respData);
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("TMRI_SUSPEND".equals(opType)) {//暂停办理
			String reason = "1";
			try {
				ywckjsjip = indatas.getString("ywckjsjip");
				jbr = indatas.getString("jbr");
				if ("".equals(jbr) || null == jbr) {
					jbr = cacheManager.getYhCache(ywckjsjip);
				}
				List<Screen> listscreen = new ArrayList<Screen>();
				if ("".equals(jbr) || null == jbr) {
					Screen screen = new Screen();
					screen.setBarip(ywckjsjip);
					try {
						listscreen = setWindowService.querybarid(screen);
						if (listscreen.size()>0) {
							jbr = listscreen.get(0).getJbr();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("ywckjsjip="+ywckjsjip);
				System.out.println("jbr="+jbr);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if ("".equals(ywckjsjip) || "".equals(jbr) || null == ywckjsjip || null == jbr) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "参数不完整!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			try {
				info = numberService.pauseOrRecover(jbr, ywckjsjip, reason);
			} catch (Exception e) {
				if (e instanceof SaveException) {
					String msg = StringUtils.isEmpty(reason) ? "保存恢复信息失败"
							: "保存暂停信息失败";
					resultMsg = msg + "，<br/>请查看当天日志";
					e.printStackTrace();
				} else if (e instanceof RemoteException
						|| e instanceof TrffException) {
					resultMsg = e.getMessage();
				} else {
					resultMsg = "在执行过程中发生异常，请查看当天日志";
					e.printStackTrace();
				}
			}
			if (!"".equals(info.getResultZT()) && null != info.getResultZT()) {
				String[] result = info.getResultZT().split("@");
				if ("10".equals(result[0])) {
					code = result[1];
					resultMsg = result[2];
					respMsg = result[2];
				}else {
					code = "2";
					resultMsg = info.getMsg();
					respMsg = info.getMsg();
				}
			}else {
				code = "2";
				resultMsg = info.getMsg();
				respMsg = info.getMsg();
			}
			try {
				outdatas.put("code", code);//1-调用成功；2-调用失败
				outdatas.put("message", resultMsg);//调用失败时返回错误描述信息
				respData.put("respCode", 200);
				respData.put("respMsg", respMsg);
				respData.put("respData", outdatas);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("respData="+respData);
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("TMRI_RECOVER".equals(opType)) {//恢复办理
			String reason = "";
			try {
				ywckjsjip = indatas.getString("ywckjsjip");
				jbr = indatas.getString("jbr");
				if ("".equals(jbr) || null == jbr) {
					jbr = cacheManager.getYhCache(ywckjsjip);
				}
				List<Screen> listscreen = new ArrayList<Screen>();
				if ("".equals(jbr) || null == jbr) {
					Screen screen = new Screen();
					screen.setBarip(ywckjsjip);
					try {
						listscreen = setWindowService.querybarid(screen);
						if (listscreen.size()>0) {
							jbr = listscreen.get(0).getJbr();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("ywckjsjip="+ywckjsjip);
				System.out.println("jbr="+jbr);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if ("".equals(ywckjsjip) || "".equals(jbr) || null == ywckjsjip || null == jbr) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "参数不完整!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					System.out.println("respData="+respData);
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			try {
				info = numberService.pauseOrRecover(jbr, ywckjsjip, reason);
			} catch (Exception e) {
				if (e instanceof SaveException) {
					String msg = StringUtils.isEmpty(reason) ? "保存恢复信息失败"
							: "保存暂停信息失败";
					resultMsg = msg + "，<br/>请查看当天日志";
					e.printStackTrace();
				} else if (e instanceof RemoteException
						|| e instanceof TrffException) {
					resultMsg = e.getMessage();
				} else {
					resultMsg = "在执行过程中发生异常，请查看当天日志";
					e.printStackTrace();
				}
			}
			if (!"".equals(info.getResultZT()) && null != info.getResultZT()) {
				String[] result = info.getResultZT().split("@");
				if ("11".equals(result[0])) {
					code = result[1];
					resultMsg = result[2];
					respMsg = result[2];
				}else {
					code = "2";
					resultMsg = info.getMsg();
					respMsg = info.getMsg();
				}
			}else {
				code = "2";
				resultMsg = info.getMsg();
				respMsg = info.getMsg();
			}
			try {
				outdatas.put("code", "1");//1-调用成功；2-调用失败
				outdatas.put("message", resultMsg);//调用失败时返回错误描述信息
				respData.put("respCode", 200);
				respData.put("respMsg", "恢复成功!");
				respData.put("respData", outdatas);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("respData="+respData);
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("TMRI_RECEIVE".equals(opType)) {//待领取牌证
			
			try {
				lsh = indatas.getString("lsh");
				xm = indatas.getString("xm");
				pzlx = indatas.getString("pzlx");
				zzjsjip = indatas.getString("zzjsjip");
				lzckbh = indatas.getString("lzckbh");
				System.out.println("lsh="+lsh+"~~xm="+xm+"~~pzlx="+pzlx+"~~zzjsjip="+zzjsjip+"~~lzckbh="+lzckbh);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			String[] pzlxs = pzlx.split("#");
			Lzxx lzxx = new Lzxx();
			lzxx.setLsh(lsh);
			lzxx.setXm(xm);
			lzxx.setIp(zzjsjip);
			lzxx.setBarid(lzckbh);
			lzxx.setZllx(pzlx);
			try {
				lzxxService.insertLzxx(lzxx);
				lzckAction.sendLzxx_new(lsh,zzjsjip);
			} catch (Exception e) {
				try {
					outdatas.put("code", "2");//1-调用成功；2-调用失败
					outdatas.put("message", resultMsg);//调用失败时返回错误描述信息
					respData.put("respCode", 200);
					respData.put("respMsg", "领证发屏失败!");
					respData.put("respData", outdatas);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
			
			
			
			
			try {
				outdatas.put("code", "1");//1-调用成功；2-调用失败
				outdatas.put("message", resultMsg);//调用失败时返回错误描述信息
				respData.put("respCode", 200);
				respData.put("respMsg", "领证发屏成功!");
				respData.put("respData", outdatas);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("respData="+respData);
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if ("TMRI_COMPLETE".equals(opType)) {//完结
			
			try {
				qhxxxlh = indatas.getString("qhxxxlh");
				System.out.println("qhxxxlh="+qhxxxlh);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if ("".equals(qhxxxlh)) {
				try {
					respData.put("respData", "");
					respData.put("respCode", 100);
					respData.put("respMsg", "参数不完整!");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					this.getResponse("text/html").getWriter().println(respData);
					this.getResponse().flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ERROR;
			}
			Number number = new Number();
			pdh = deptCode+deptHall+qhxxxlh.substring(16);
			number.setClientno(pdh);
			try {
				numberService.updateWanJie(number);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				outdatas.put("code", "1");//1-调用成功；2-调用失败
				outdatas.put("message", "完结标记完成");//调用失败时返回错误描述信息
				respData.put("respCode", 200);
				respData.put("respMsg", "完结标记完成!");
				respData.put("respData", outdatas);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("respData="+respData);
				this.getResponse("text/html").getWriter().println(respData.toString());
				this.getResponse().flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {
		QueueJieKou jk = new QueueJieKou();
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
