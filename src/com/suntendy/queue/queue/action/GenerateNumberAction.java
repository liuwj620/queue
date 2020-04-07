package com.suntendy.queue.queue.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;

import com.opensymphony.webwork.ServletActionContext;
import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.dept.domain.Dept;
import com.suntendy.queue.dept.service.DeptService;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.log.domain.LogVo;
import com.suntendy.queue.log.service.ILogService;
import com.suntendy.queue.printSet.domain.Print;
import com.suntendy.queue.printSet.service.IPrintSetService;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.PrintInfo;
import com.suntendy.queue.queue.domain.Wclh;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.reservation.domain.Reservation;
import com.suntendy.queue.reservation.service.IReservationService;
import com.suntendy.queue.systemlog.domain.Operate;
import com.suntendy.queue.systemlog.domain.Security;
import com.suntendy.queue.systemlog.operateLog.OperateLog;
import com.suntendy.queue.systemlog.securityLog.SecurityLog;
import com.suntendy.queue.systemlog.service.ISystemLogService;
import com.suntendy.queue.tuiban.domain.TuiBan;
import com.suntendy.queue.tuiban.service.ITuiBanService;
import com.suntendy.queue.util.loginKeyUtil;
import com.suntendy.queue.util.HttpRequestUtil.HttpRequestUtil;
import com.suntendy.queue.util.barcode.BarCode;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.SaveException;
import com.suntendy.queue.util.exception.TrffException;
import com.suntendy.queue.util.scriptsession.event.DualScreenEvent;
import com.suntendy.queue.util.trff.TrffUtils;
import com.suntendy.queue.yyjk.action.YyjkAction;
import com.suntendy.queue.yyjk.domain.NanNingYYXX;
import com.suntendy.queue.yyjk.domain.ZhongShanYYXX;
import com.suntendy.queue.yyjk.service.IYyjkService;
import com.suntendy.queue.zzj.domain.Zzj;
import com.suntendy.queue.zzj.service.IZzjService;
import com.swetake.util.Qrcode;

/*******************************************************************************
 * 描述: 通过选择的业务类型、证件名称和证件号码取号，如果是代理人则有办理业务笔数 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-09-25 14:53:08 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class GenerateNumberAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private String flag; // 所属业务类型
	private String code; // 队列编号
	private String prefix; // 队列前缀
	private String IDType; // 证件类型
	private String IDNumber; // 证件号码
	private String typeName; // 业务类型名称
	private String personType; // 人员类型
	private String businessType; // 业务类型编号
	private String businessCount; // 业务笔数
	private String RSA; // 校验位
	private INumberService numberService;
	private IPrintSetService printSetService;// 获取打印信息
	private ILogService logService;
	private ITuiBanService tuibanService;
	private IZzjService zzjService;
	private IReservationService reservationService;
	private IYyjkService yyjkService;
	private DeptService deptService;
	private ISystemLogService iSystemLogService;
	
	// private String IDNumberB;//代办人证件号码
	
	
	public DeptService getDeptService() {
		return deptService;
	}

	public ISystemLogService getiSystemLogService() {
		return iSystemLogService;
	}

	public void setiSystemLogService(ISystemLogService iSystemLogService) {
		this.iSystemLogService = iSystemLogService;
	}

	public String getRSA() {
		return RSA;
	}

	public void setRSA(String rSA) {
		RSA = rSA;
	}

	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}

	public IYyjkService getYyjkService() {
		return yyjkService;
	}

	public void setYyjkService(IYyjkService yyjkService) {
		this.yyjkService = yyjkService;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setIDType(String type) {
		IDType = type;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setIDNumber(String number) {
		IDNumber = number;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public void setBusinessCount(String businessCount) {
		this.businessCount = businessCount;
	}

	public void setNumberService(INumberService numberService) {
		this.numberService = numberService;
	}

	public ILogService getLogService() {
		return logService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	// public String getIDNumberB() {
	// return IDNumberB;
	// }
	//
	// public void setIDNumberB(String iDNumberB) {
	// IDNumberB = iDNumberB;
	// }

	public ITuiBanService getTuibanService() {
		return tuibanService;
	}

	public void setTuibanService(ITuiBanService tuibanService) {
		this.tuibanService = tuibanService;
	}

	public IZzjService getZzjService() {
		return zzjService;
	}

	public void setZzjService(IZzjService zzjService) {
		this.zzjService = zzjService;
	}

	public IReservationService getReservationService() {
		return reservationService;
	}

	public void setReservationService(IReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@SuppressWarnings("static-access")
	@Override
	public String execute() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		HttpSession session = this.getHttpSession();
		HttpServletRequest request = getRequest();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Employee user = (Employee) session.getAttribute("user");
		String IDNumberB = request.getParameter("IDNumberB");
		String nameA = request.getParameter("nameA");
		String nameB = request.getParameter("nameB");
		String pnum = request.getParameter("PNumber");
		String cartype = request.getParameter("cartype");
		String carnumber = URLDecoder.decode(request.getParameter("carNumber"),
				"UTF-8");
		String jdcnum = request.getParameter("jdcnum");
		String jdctypes = request.getParameter("jdctypes");
		Reservation res = new Reservation();
		String yybj = null;
		yybj = request.getParameter("yybj");
		Number number = new Number();
		number.setFlag(flag);
		number.setIDType(IDType);
		number.setQueueCode(code);
		number.setIDNumber(IDNumber.toString().trim());
		number.setPhoneNumber(pnum);
		number.setCarType(cartype);
		number.setCarNumber(carnumber);
		number.setJdcnum(jdcnum);
		number.setJdctypes(jdctypes);
		res.setSfzmhm(IDNumber.toString().trim());
		if (null != IDNumberB && !"".equals(IDNumberB)) {
			number.setIDNumberB(IDNumberB.toString().trim());
			res.setSfzmhm(IDNumberB.toString().trim());
		}
		// System.out.println("IDNumberB="+IDNumberB);
		number.setTypeName(typeName);
		number.setServerIp(clientIp()); // 取号机IP
		number.setBusinessPrefix(prefix);
		number.setPersonType(personType);
		number.setBusinessType(businessType);
		number.setBusinessCount(businessCount);
		number.setNameA(nameA);
		number.setNameB(nameB);
		String RSAa=new String(loginKeyUtil.decode(RSA));
		String RSAcontent="";
		String content="号票所属大厅编号【"+deptHall+"】,号票所属身份证【"+IDNumber+"】---发生篡改数据:";
		String[] RSAb = RSAa.split(",");
		if(null != IDNumberB){
			if(!RSAb[0].equals(IDNumberB)){
				//后期顺序改回来
				content+="代理人身份证由【"+IDNumberB+"】改为【"+RSAb[0]+"】";
			}
			RSAcontent=IDNumberB+","+IDNumber+","+businessType;
		}else {
			RSAcontent=","+IDNumber+","+businessType;
		}
		if(!RSAb[1].equals(IDNumber)){
		content+="---号牌所属身份证由【"+IDNumber+"】改为【"+RSAb[1]+"】";
		}
		if(!RSAb[2].equals(businessType)){
			content+="---业务类型代码由【"+businessType+"】改为【"+RSAb[2]+"】";
		}
		if(!RSAa.equals(RSAcontent)){
			//插入安全日志 只狼
			String eventType="改";
			String result1="0";
			String event="取号数据异常";
			String resultDepict="数据发生篡改";
			SecurityLog sLog=new SecurityLog();
			Security security=sLog.security_log(IDNumber, event, eventType, result1, resultDepict);
			security.setOpName("无");
			security.setContent(content.getBytes());
			iSystemLogService.addSecurity(security);
		}
		if (null != user) {
			number.setCzyh(user.getCode());// 操作用户
		}

		Zzj zzj = new Zzj();
		zzj.setQhjip(this.clientIp());
		PrintInfo info = new PrintInfo();
		try {
			info = numberService.getNewNumber(number);
			String str = "";
			List<Print> printInfo = printSetService
					.getPrint(deptCode, deptHall); // 获取编辑好的打印信息
			if (!printInfo.isEmpty()) {
				Print printVo = printInfo.get(0);
				str = printVo.getContent();
			}
			info.setStr(str);
			zzjService.updateZzj(zzj);
		} catch (Exception e) {
			if (e instanceof SaveException) {
				info.setMsg("保存数据到本地数据库失败，<br/>请查看当天日志");
				e.printStackTrace();
			} else if (e instanceof RemoteException
					|| e instanceof TrffException) {
				info.setMsg(e.getMessage());
			} else {
				info.setMsg("在执行过程中发生异常，异常信息请查看当天日志");
				e.printStackTrace();
			}
		}
		String xzywmewm = cacheManager.getSystemConfig("xzywmewm");
		if (xzywmewm != null && xzywmewm != "" && xzywmewm.equals("0")) {
			// 生成条码
			BarCode barcode = new BarCode();
			if (!"".equals(info.getSerialNum()) && null != info.getSerialNum()) {
				try {
					byte[] by = barcode.createDimensionalCode(info
							.getSerialNum());
					ServletContext d = ServletActionContext.getServletContext();
					String path = d.getRealPath("/");
					// 打开输入流
					FileImageOutputStream imageOutput = new FileImageOutputStream(
							new File(path + "IcardPic/" + info.getSerialNum()
									+ ".jpg"));
					// 将byte数组写入本地硬盘
					imageOutput.write(by, 0, by.length);
					// 关闭输入流
					imageOutput.close();

					barcode.scale(path + "IcardPic/" + info.getSerialNum()
							+ ".jpg", path + "IcardPic/" + info.getSerialNum()
							+ "D.jpg", 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("顺序号为空，没能生成条码!");
			}
		} else if (xzywmewm != null && xzywmewm != "" && xzywmewm.equals("2")) {
			ServletContext d1 = ServletActionContext.getServletContext();
			String path = d1.getRealPath("/");
			 // 打开输入流
	        FileInputStream fis = new FileInputStream("D:/img/"+info.getSerialNum()+".jpg");
	        // 打开输出流
	        FileOutputStream fos = new FileOutputStream(path + "IcardPic/"
					+ info.getSerialNum() + "D.jpg");
	        
	        // 读取和写入信息
	        int len = 0;
	        while ((len = fis.read()) != -1) {
	            fos.write(len);
	        }
	        
	        // 关闭流  先开后关  后开先关
	        fos.close(); // 后开先关
	        fis.close(); // 先开后关
		} else {
			// 生成二维码
			Qrcode qrcode = new Qrcode();
			qrcode.setQrcodeErrorCorrect('M');// 纠错等级（分为L、M、H三个等级）
			qrcode.setQrcodeEncodeMode('B');// N代表数字，A代表a-Z，B代表其它字符
			qrcode.setQrcodeVersion(7);// 版本
			// 生成二维码中要存储的信息
			String qrData = "您的号码为：" + info.getSerialNum() + "（＾ω＾）取号时您前面还有："
					+ info.getPeoNum() + "人在排队";
			// 设置一下二维码的像素
			int width = 139;
			int height = 139;
			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			// 绘图
			Graphics2D gs = bufferedImage.createGraphics();
			gs.setBackground(Color.WHITE);
			gs.setColor(Color.BLACK);
			gs.clearRect(0, 0, width, height);// 清除下画板内容

			// 设置下偏移量,如果不加偏移量，有时会导致出错。
			int pixoff = 2;

			byte[] d = qrData.getBytes("gb2312");
			if (d.length > 0 && d.length < 120) {
				boolean[][] s = qrcode.calQrcode(d);
				for (int i = 0; i < s.length; i++) {
					for (int j = 0; j < s.length; j++) {
						if (s[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			}
			gs.dispose();
			bufferedImage.flush();
			ServletContext d1 = ServletActionContext.getServletContext();
			String path = d1.getRealPath("/");
			ImageIO.write(bufferedImage, "png", new File(path + "IcardPic/"
					+ info.getSerialNum() + ".jpg"));
			ImageIO.write(bufferedImage, "png", new File(path + "IcardPic/"
					+ info.getSerialNum() + "D.jpg"));
		}

		// 添加用户登录取号日志
		if (null != user) {
			LogVo log = new LogVo();
			log.setCode(user.getCode());
			log.setLogflag("1");
			log.setSxh(deptCode + deptHall + info.getSerialNum());
			logService.addUserLoginLog(log);
		}
		// 退办信息
		List<TuiBan> listtuiban = new ArrayList<TuiBan>();
		String tuibanstate = "0";
		if (!"".equals(info.getIDNumber()) && info.getIDNumber() != null) {
			TuiBan tuiban = new TuiBan();
			tuiban.setIdnumber(info.getIDNumber());
			listtuiban = tuibanService.queryTuiBan(tuiban);
			if (listtuiban.size() > 0) {
				tuibanstate = "1";
			}
		}
		// 根据预约模式 取号完成需要做的操作
		String yyjkmode = cacheManager.getSystemConfig("yyjkmode"); // 预约模式 0-没有
																	// 1-珠海 2-南宁
		String iscywf = cacheManager.getSystemConfig("iscywf"); // 是否查验违法0 是1 否
		if ("2".equals(yyjkmode) && "0".equals(iscywf)) {// 南宁模式
			// 南宁完成预约取号之后需要 修改预约信息表的状态字段
			NanNingYYXX nanNingYYXX = new NanNingYYXX();
			if ("A".equals(IDType)) {// 证件类型 身份证
				nanNingYYXX.setNationalId(IDNumber.trim());
			} else if ("B".equals(IDType)) {// 证件类型 组织机构代码证
				nanNingYYXX.setOrgId(IDNumber.trim());
			}
			nanNingYYXX.setBookingStatusId("3");// 修改预约信息状态为3
			yyjkService.updateNanNingYYXX(nanNingYYXX);
		} else if ("3".equals(yyjkmode)) { // 中山模式
			JSONObject yuyueQueryJson = new JSONObject();
			yuyueQueryJson.put("service", "saveRecord");
			if (!"".equals(cacheManager.getYuYueIdInCache(IDNumber))) {
				yuyueQueryJson.put("yuyuelsh",
						cacheManager.getYuYueIdInCache(IDNumber));
				yuyueQueryJson.put("code", info.getSerialNum());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				yuyueQueryJson.put("update_time", sdf.format(new Date()));
				System.out.println("预约接口更新操作:" + yuyueQueryJson.toString());
				// 返回 {"code":1,"msg":"Success"}
				net.sf.json.JSONObject updateBackJson = HttpRequestUtil
						.httpRequest("http://10.44.142.68/yuyue/api/Bespeak",
								"POST", yuyueQueryJson);
				System.out.println("预约接口更新返回:" + updateBackJson.toString());
				cacheManager.deleteYuYueId(IDNumber);
				System.out.println("移除预约id");
			} else {
				// Dept dept = new Dept();
				// dept.setDeptcode(deptCode);
				// dept.setDepthall(deptHall);
				// List<Dept> deptList = deptService.getDeptList(dept);
				// JSONObject queryJson = new JSONObject();
				// queryJson.put("service", "getRecord");
				// queryJson.put("acode", deptList.get(0).getYydd());
				// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// queryJson.put("date",sdf.format(new Date()));
				// queryJson.put("id_number", IDNumber);
				// net.sf.json.JSONObject yyJson =
				// HttpRequestUtil.httpRequest("http://10.44.142.68/yuyue/api/Bespeak",
				// "POST", queryJson);
				// int i =0;
				// net.sf.json.JSONArray jsonArray =
				// yyJson.getJSONArray("result");
				// int size = jsonArray.size();
				// String yuyuelsh = null;
				// while(i<size){
				// net.sf.json.JSONObject item = (net.sf.json.JSONObject)
				// jsonArray.get(i);
				// String time = (String)item.get("time");
				// String begin = time.split("-")[0];
				// String end = time.split("-")[1];
				// if(!YyjkAction.isInTime(begin,end)){
				// i++;
				// continue;
				// }
				// yuyuelsh = (String) item.get("yuyuelsh");
				// yuyueQueryJson.put("yuyuelsh", yuyuelsh);
				// break;
				// }
			}

		} else if ("5".equals(yyjkmode)) { // 佛山模式
			if (!"".equals(cacheManager.getYuYueIdInCache(IDNumber))) {
				Dept dept = new Dept();
				dept.setDeptcode(deptCode);
				dept.setDepthall(deptHall);
				List<Dept> deptList = deptService.getDeptList(dept);
				StringBuffer sb = new StringBuffer();
				sb.append("auth_code=" + deptList.get(0).getAk()).append("&reservationNumber=" + cacheManager.getYuYueIdInCache(IDNumber)).append("&status=22");
				net.sf.json.JSONObject updateBackJson = HttpRequestUtil.httpsRequest("https://10.45.131.130:80/ram-api/rese-confirm", "POST", sb.toString());
				if(null==updateBackJson){
					System.out.println("网络不通,佛山预约接口状态回传失败");
				}else{
					System.out.println("佛山预约接口状态回传成功:"+updateBackJson.get("msg"));
				}
				cacheManager.deleteYuYueId(IDNumber);
			}
		}
		//查询违法信息
		Boolean flag=new Boolean(false);
		String context=showWfMessage(IDNumber,jdcnum,jdctypes);
		String[] red=context.split("@");
		System.out.println(red[0]+"违法信息"+red[1]);
		// 将打印参数信息以JSON格式返回
		JSONObject objPrint = new JSONObject();
		if(red[0]==null || red[0].equals("1")){
			if(red[1]!=null&&red[1].equals("0")){
		if (info.isSuccess()) {
			objPrint.put("serialNum", info.getSerialNum());
			objPrint.put("queueName", info.getQueueName().split("@")[0]);
			objPrint.put("peoNum", info.getPeoNum());
			objPrint.put("isSuccess", info.isSuccess());
			objPrint.put("error", info.getMsg());
			objPrint.put("str", info.getStr());
			objPrint.put("deptCode", deptCode);
			objPrint.put("deptHall", deptHall);
			objPrint.put("id", info.getId());
			objPrint.put("countAll", info.getCountAll());
			objPrint.put("businessType", businessType);
			objPrint.put("waitingarea", info.getWaitingarea());
			objPrint.put("idNumber", info.getIDNumber());
			objPrint.put("tb", tuibanstate);
		} else {
			objPrint.put("isSuccess", info.isSuccess());
			objPrint.put("error", info.getMsg());
		}
		objPrint.put("isOpenIndexCamera",
				cacheManager.getSystemConfig("isOpenIndexCamera"));// 是否启用取号拍照
																	// 0是 1 否);
			}else {
				objPrint.put("isSuccess", flag);
				System.out.println("机动车存在违法，不得取号！");
				objPrint.put("error", "机动车存在违法，不得取号！");
			}
		
		}else {
			objPrint.put("isSuccess", flag);
			System.out.println("存在违法罚款未交，请缴纳罚款后再取号！");
			objPrint.put("error", "存在违法罚款未交，请缴纳罚款后再取号！");
		}
		
		objPrint.put("sfkqcyyw", cacheManager.getSystemConfig("sfkqcyyw"));// );
		this.getResponse("text/html").getWriter().println(objPrint.toString());
		System.out.println("将打印信息传回页面="
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS")
						.format(new Date()));
		// 更新预约状态
		if ("1".equals(yybj)) {
			List<Reservation> listr = reservationService.queryReservation(res);
			if (null != listr && listr.size() > 0) {
				reservationService.updateReservationStatus(listr.get(0));
			}
		}

		return null;
	}

	/**
	 * 查询等待人数
	 * 
	 * @return
	 */
	public String showWaitRs() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number countnum = new Number();
		countnum.setDeptCode(deptCode);
		countnum.setDeptHall(deptHall);
		List<Number> numbersCountList = numberService.showWaitRs(countnum);
		JSONObject datas = new JSONObject();
		if (null != numbersCountList && !numbersCountList.isEmpty()) {
			datas.put("isSuccess", true);

			JSONArray typeGroup = new JSONArray();
			int shul = 0;
			for (Number nu : numbersCountList) {
				JSONObject obj = new JSONObject();
				try {
					int typesl = 0;
					if (null != nu.getTypeCount()) {
						typesl = Integer.parseInt(nu.getTypeCount());
					}
					shul = shul + typesl;
					obj.put("typeName", nu.getTypeName());
					obj.put("typeCount", nu.getTypeCount());
					obj.put("countAll", String.valueOf(shul));
					// System.out.println("typeName=="+nu.getTypeName()+"  typeCount=="+nu.getTypeCount());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				typeGroup.put(obj);
			}
			datas.put("datas", typeGroup);
		} else {
			datas.put("isSuccess", false);
			// datas.put("error", "获取大厅信息失败");
		}

		this.getResponse("text/html").getWriter().println(datas.toString());
		return null;
	}

	/**
	 * 查询所有指纹用户
	 * 
	 * @return
	 */
	public String getAllZhiWen() {
		HttpServletRequest request = getRequest();
		String idNumber = request.getParameter("idNumber");
		Number number = new Number();
		number.setIDNumber(idNumber);

		try {
			List<Number> list = numberService.getZhiWenByIdNumber(number);
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Number numbers = list.get(i);
					String operate = "<a onclick=updateZhiWen('"
							+ numbers.getIDNumber()
							+ "')><img src='/queue/images/button_edit.jpg' style='cursor:hand' ></a>&nbsp;&nbsp;"
							+ "&nbsp;&nbsp;<a onclick=delZhiWen('"
							+ numbers.getIDNumber()
							+ "')><img src='/queue/images/button_del.jpg' style='cursor:hand' ></a>";
					numbers.setOperation(operate);
				}
			}
			request.setAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// getRequest().setAttribute("action", "number/getAllZhiWen.action");
		return "success";
	}

	/*
	 * 跳转保存指纹信息
	 */
	public String to_addZhiWen() throws Exception {
		HttpServletRequest request = getRequest();
		return "success";
	}

	/*
	 * 跳转保存指纹信息
	 */
	public String to_updateZhiWen() throws Exception {
		HttpServletRequest request = getRequest();
		String idNumber = request.getParameter("idNumber");
		Number number = new Number();
		number.setIDNumber(idNumber);
		try {
			List<Number> list = numberService.getZhiWenByIdNumber(number);
			if (!list.isEmpty()) {
				Number numbers = list.get(0);
				request.setAttribute("idNumber", numbers.getIDNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success";
	}

	/*
	 * 保存指纹信息
	 */
	public String saveZhiWen() throws Exception {
		HttpServletRequest request = getRequest();
		String idNumber = StringUtils.trimToEmpty(request
				.getParameter("idNumber"));
		String flag = StringUtils.trimToEmpty(request.getParameter("flag"));// 标记
		// 0添加，1修改,
		// 2
		// 删除
		String zwbase64 = StringUtils.trimToEmpty(request
				.getParameter("zwbase64"));
		Number number = new Number();
		number.setIDNumber(idNumber);
		number.setZWBase64(new BASE64Decoder().decodeBuffer(zwbase64));

		try {
			if ("0".equals(flag)) {
				numberService.addZhiWen(number);
				request.setAttribute("msg", "保存成功！");
			} else if ("1".equals(flag)) {
				numberService.updateZhiWen(number);
				request.setAttribute("msg", "修改成功！");
			} else if ("2".equals(flag)) {
				numberService.delZhiWen(number);
				request.setAttribute("msg", "删除成功！");
			}
		} catch (Exception e) {
			request.setAttribute("msg", "处理失败！");
			e.printStackTrace();
		}

		request.setAttribute("action", "number/getAllZhiWen.action");
		return "success";
	}

	public void wclh() throws Exception {
		HttpServletRequest request = getRequest();
		String idNumber = request.getParameter("IDNumber");
		String msg = "";
		String status = "1";
		List<Wclh> list = numberService.wblh(idNumber);
		JSONObject objPrint = new JSONObject();
		if (null != list && list.size() != 0) {
			for (Wclh a : list) {
				msg = msg + "该证件在" + a.getDepthall() + "存在" + a.getSum()
						+ "笔取号未办理业务的异常情况" + "\n";
			}
			msg = msg + ",是否继续取号？";
			objPrint.put("msg", msg);
			objPrint.put("status", status = "2");
		}
		this.getResponse("text/html").getWriter().println(objPrint.toString());
	}
	
	private String showWfMessage(String sfzmhm,String hpzm,String hpzl) {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String isUseInterface = cacheManager.getSystemConfig("isUseInterface");//是否启用接口
		String iscywf = cacheManager.getSystemConfig("iscywf");
		String cywffs = cacheManager.getSystemConfig("cywffs");
		String jklx = cacheManager.getSystemConfig("jklx");
		String jkbj="1",wfcs="0",context="";
		String sbkzjsjip = "";//设备控制计算机ip
		Map<String, Object> result = new HashMap<String, Object>();
		if ("0".equals(iscywf) && "2".equals(cywffs) && sfzmhm.length()>=15) {
			if ("0".equals(isUseInterface) && "1".equals(jklx)) {
				try {
					sbkzjsjip = InetAddress.getLocalHost().getHostAddress().toString();
				} catch (UnknownHostException e2) {
					e2.printStackTrace();
				}
				try {
					result = TrffUtils.query_WfMessage("","",sfzmhm,"","",deptCode,sbkzjsjip);
					if (!result.isEmpty()) {
						Map<String, String> map = (Map<String, String>) result
								.get("WfMessage");
						jkbj = map.get("jkbj");
					}
					if (jkbj == null || jkbj.equals("1")) {// 查询机动车违法
						if (hpzl != null && !hpzl.equals("")) {
							result = TrffUtils.query_QueryJDCWf(hpzl, hpzm,deptCode,sbkzjsjip);
							if (!result.isEmpty()) {
								Map<String, String> map = (Map<String, String>) result
										.get("JDCWfMessage");
								wfcs = map.get("wfcs");
							}
						} else {
							System.out.println("查询机动车违法!!");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("六合一接口没有打开，无法查询违法信息");
			}
		}else {
			System.out.println("违法功能没有启用，无法查询身份证号码【"+sfzmhm+"】相关违法信息");
		}
		context=jkbj+"@"+wfcs;
		return context;
	}
	
	
	public IPrintSetService getPrintSetService() {
		return printSetService;
	}

	public void setPrintSetService(IPrintSetService printSetService) {
		this.printSetService = printSetService;
	}

	public INumberService getNumberService() {
		return numberService;
	}

}