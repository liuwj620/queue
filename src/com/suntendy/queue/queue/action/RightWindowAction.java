package com.suntendy.queue.queue.action;

import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.log.domain.LogVo;
import com.suntendy.queue.log.service.ILogService;
import com.suntendy.queue.passreason.domain.PassReason;
import com.suntendy.queue.passreason.service.IPassReasonService;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.PrintInfo;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.queue.service.impl.CodeServiceImpl;
import com.suntendy.queue.queue.util.cache.NumberManager;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.SaveException;
import com.suntendy.queue.util.exception.TrffException;
import com.suntendy.queue.util.exception.UpdateException;

public class RightWindowAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String operNum;
	private String loginIP;
	private String callback;
	private INumberService numberService;
	private ILogService logService;
	private IPassReasonService passReasonService;

	public IPassReasonService getPassReasonService() {
		return passReasonService;
	}

	public void setPassReasonService(IPassReasonService passReasonService) {
		this.passReasonService = passReasonService;
	}

	public String getOperNum() {
		return operNum;
	}

	public void setOperNum(String operNum) {
		this.operNum = operNum;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
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

	/**
	 * 获取当前号及剩余未办理号数
	 * 
	 * @return
	 */
	public String current() throws Exception {
		String numStr = "", strTotal = "";
		NumberManager numberManager = NumberManager.getInstance();
		Number callNumber = numberManager.fetchCallNumber(operNum);
		if (null != callNumber) {
			numStr = callNumber.getSerialNum();
		}

		try {
			strTotal = this.numberService
					.getTotalNumberOfBusinessNumber(loginIP);
		} catch (Exception e) {
			e.printStackTrace();
		}

		callback = callback + "({'msg':'" + numStr + "','count':'"
				+ URLEncoder.encode(strTotal, "UTF-8") + "'})";
		System.out.println(callback);
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}

	/**
	 * 叫号
	 * 
	 * @return 提示信息
	 * @throws Exception
	 */
	public String callNumber() throws Exception {
		String resultMsg = "";
		try {
			resultMsg = numberService.callNumber(operNum, loginIP);
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

		callback = callback + "({'msg':'"
				+ URLEncoder.encode(resultMsg, "UTF-8") + "'})";
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}

	/**
	 * 重呼
	 * 
	 * @return 提示信息
	 * @throws Exception
	 */
	public String recall() throws Exception {
		String resultMsg = "";
		try {
			resultMsg = numberService.recall(operNum, loginIP);
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				resultMsg = "未叫号，不能重呼";
				e.printStackTrace();
			} else {
				resultMsg = "在执行过程中发生异常，请查看当天日志";
				e.printStackTrace();
			}
		}

		callback = callback + "({'msg':'"
				+ URLEncoder.encode(resultMsg, "UTF-8") + "'})";
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}

	/**
	 * 过号
	 * 
	 * @return 提示信息
	 * @throws Exception
	 */
	public String pass() throws Exception {
		String resultMsg = "";
		String passReason = getRequest().getParameter("reason");
		if (passReason == null) {
			passReason = "";
		}
		try {
			resultMsg = numberService.pass(operNum, loginIP, passReason);
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

		callback = callback + "({'msg':'"
				+ URLEncoder.encode(resultMsg, "UTF-8") + "'})";
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}

	/**
	 * 提请评价
	 * 
	 * @return 提示信息
	 * @throws Exception
	 */
	public String evaluation() throws Exception {
		String resultMsg = "";
		CacheManager cacheManager = CacheManager.getInstance();
		String pjTime = cacheManager.getSystemConfig("pjTime");
		int ptime = Integer.parseInt(pjTime);
		int pt = ptime * 1000;
		try {
			resultMsg = numberService.toEvaluation(operNum, loginIP);
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

		callback = callback + "({'msg':'"
				+ URLEncoder.encode(resultMsg, "UTF-8") + "@" + pt + "'})";
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}

	/**
	 * 挂起
	 * 
	 * @return 提示信息
	 * @throws Exception
	 */
	public String hangup() throws Exception {
		String resultMsg = "";
		try {
			resultMsg = numberService.hangup(operNum, loginIP);
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				resultMsg = "未叫号，不能挂起";
				e.printStackTrace();
			} else if (e instanceof UpdateException) {
				resultMsg = "更新号码信息状态[挂起]失败，<br/>请查看当天日志";
				e.printStackTrace();
			} else if (e instanceof RemoteException
					|| e instanceof TrffException) {
				resultMsg = e.getMessage();
			} else {
				resultMsg = "在执行过程中发生异常，请查看当天日志";
				e.printStackTrace();
			}
		}

		callback = callback + "({'msg':'"
				+ URLEncoder.encode(resultMsg, "UTF-8") + "'})";
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}
	
	

	/**
	 * 获取挂起号码
	 * 
	 * @return 挂起号码
	 * @throws Exception
	 */
	public String hangupNumber() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String hthf = cacheManager.getSystemConfig("hthf");
		String resultMsg = "";
		if ("1".equals(hthf)) {
			try {
				resultMsg = numberService.toHangupRecover(operNum, loginIP);
			} catch (Exception e) {
				resultMsg = "在执行过程中发生异常，请查看当天日志";
				e.printStackTrace();
			}
		} else if ("0".equals(hthf)) {
			resultMsg = "您无权操作恢复功能!";
		}

		JSONObject object = new JSONObject();
		if (resultMsg.startsWith("true")) {
			object.put("isSuccess", true);
			resultMsg = resultMsg.substring(4);
		} else {
			object.put("isSuccess", false);
		}
		object.put("msg", URLEncoder.encode(resultMsg, "UTF-8"));
		callback = callback + "(" + object.toString() + ")";
		this.getResponse("text/html").getWriter().println(callback);
		System.out.println("获取挂起数据结果：[" + operNum + "][" + resultMsg + "]");
		return null;
	}

	/**
	 * 挂起恢复
	 * 
	 * @return
	 */
	public String hangupRecover() throws Exception {
		String resultMsg = "";
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		try {
			resultMsg = numberService.hangupRecover(operNum, loginIP, id);
		} catch (Exception e) {
			if (e instanceof UpdateException) {
				resultMsg = "更新号码信息状态[挂起恢复]失败，<br/>请查看当天日志";
				e.printStackTrace();
			} else if (e instanceof RemoteException
					|| e instanceof TrffException) {
				resultMsg = e.getMessage();
			} else {
				resultMsg = "在执行过程中发生异常，请查看当天日志";
				e.printStackTrace();
			}
		}

		callback = callback + "({'msg':'"
				+ URLEncoder.encode(resultMsg, "UTF-8") + "'})";
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}

	/**
	 * 后台挂起恢复
	 * 
	 * @return
	 */
	public String hTHangupRecover() throws Exception {
		String resultMsg = "";
		HttpServletRequest request = getRequest();
		HttpSession session = this.getHttpSession();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Number number = new Number();
		String id = request.getParameter("sid");
		System.out.println(id);
		number.setId(id);
		number.setDeptCode(deptCode);
		number.setDeptHall(deptHall);
		List<Number> list = numberService.queryAllGqBybarId(number);

		if (list.size() > 0) {
			try {
				if (!cacheManager.isSameIP(list.get(0).getCode(), list.get(0)
						.getBarIp())) {
					resultMsg = "账号已在其它窗口登录，<br/>不能挂起恢复";
				} else if (StringUtils.isNotEmpty(cacheManager.userCompare(list
						.get(0).getCode(), list.get(0).getBarIp(), "不能挂起恢复"))) {
					resultMsg = cacheManager.userCompare(list.get(0).getCode(),
							list.get(0).getBarIp(), "不能挂起恢复");
				} else if (cacheManager.isPause(list.get(0).getBarIp())) {
					resultMsg = "暂停受理";
				} else if (null != NumberManager.getInstance().fetchCallNumber(
						list.get(0).getCode())) {
					if ("2".equals(NumberManager.getInstance()
							.fetchCallNumber(list.get(0).getCode()).getStatus())) {
						resultMsg = "正在办理业务，不能挂起恢复";
					}
					if ("4".equals(NumberManager.getInstance()
							.fetchCallNumber(list.get(0).getCode()).getStatus())) {
						resultMsg = "正在评价，请耐心等待";
					}
				} else if ("1".equals(cacheManager.getSystemConfig("hthf"))) {
					resultMsg = "已启用窗口恢复功能!";
				} else {
					resultMsg = numberService.hangupRecover(list.get(0)
							.getCode(), list.get(0).getBarIp(), id);

					// 添加用户恢复挂起号日志
					Employee user = (Employee) session.getAttribute("user");
					LogVo log = new LogVo();
					log.setCode(user.getCode());
					log.setLogflag("0");
					log.setSxh(list.get(0).getSerialNum());
					logService.addUserLoginLog(log);
				}
				request.setAttribute("msg", resultMsg);
			} catch (Exception e) {
				if (e instanceof UpdateException) {
					resultMsg = "更新号码信息状态[挂起恢复]失败，<br/>请查看当天日志";
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
		}

		request.setAttribute("action", "queryAllGqBybarId.action");
		return "success";
	}

	/**
	 * 获取流转窗口号
	 * 
	 * @return 窗口号
	 * @throws Exception
	 */
	public String excChangeWin() throws Exception {
		String resultMsg = "";
		try {
			resultMsg = numberService.toChangeWin(operNum, loginIP);
		} catch (Exception e) {
			resultMsg = "在执行过程中发生异常，请查看当天日志";
			e.printStackTrace();
		}

		JSONObject object = new JSONObject();
		if (resultMsg.startsWith("true")) {
			object.put("isSuccess", true);
			resultMsg = resultMsg.substring(4);
		} else {
			object.put("isSuccess", false);
		}
		object.put("msg", URLEncoder.encode(resultMsg, "UTF-8"));
		callback = callback + "(" + object.toString() + ")";
		this.getResponse("text/html").getWriter().println(callback);
		System.out.println("获取流转窗口：[" + operNum + "][" + resultMsg + "]");
		return null;
	}

	/**
	 * 流转号码
	 * 
	 * @return
	 */
	public String transferenceNumber() throws Exception {
		String resultMsg = "";
		HttpServletRequest request = getRequest();
		String address = request.getParameter("id");
		try {
			resultMsg = numberService.transferenceNumber(operNum, loginIP,
					address);
		} catch (Exception e) {
			if (e instanceof UpdateException) {
				resultMsg = "流转号码失败，<br/>请查看当天日志";
				e.printStackTrace();
			} else if (e instanceof RemoteException
					|| e instanceof TrffException) {
				resultMsg = e.getMessage();
			} else {
				resultMsg = "在执行过程中发生异常，请查看当天日志";
				e.printStackTrace();
			}
		}

		callback = callback + "({'msg':'"
				+ URLEncoder.encode(resultMsg, "UTF-8") + "'})";
		this.getResponse("text/html").getWriter().println(callback);
		return null;
	}

	/**
	 * 获取暂停原因
	 * 
	 * @return 暂停原因
	 * @throws Exception
	 */
	public String pauseReason() throws Exception {
		String resultMsg = "";
		try {
			resultMsg = numberService.toPause(operNum, loginIP);
		} catch (Exception e) {
			resultMsg = "在执行过程中发生异常，请查看当天日志";
			e.printStackTrace();
		}

		JSONObject object = new JSONObject();
		if (resultMsg.startsWith("true")) {
			object.put("isSuccess", true);
			resultMsg = resultMsg.substring(4);
		} else {
			object.put("isSuccess", false);
		}
		object.put("msg", URLEncoder.encode(resultMsg, "UTF-8"));
		callback = callback + "(" + object.toString() + ")";
		this.getResponse("text/html").getWriter().println(callback);
		System.out.println("获取暂停原因结果：[" + operNum + "][" + resultMsg + "]");
		return null;
	}

	/**
	 * 暂停或恢复
	 * 
	 * @return 提示信息
	 * @throws Exception
	 */
	public String pauseOrRecover() throws Exception {
		HttpServletRequest request = getRequest();
		// 暂停原因
		String reason = request.getParameter("reason");
		PrintInfo info = new PrintInfo();
		try {
			info = numberService.pauseOrRecover(operNum, loginIP, reason);
		} catch (Exception e) {
			if (e instanceof SaveException) {
				String msg = StringUtils.isEmpty(reason) ? "保存恢复信息失败"
						: "保存暂停信息失败";
				info.setMsg(msg + "，<br/>请查看当天日志");
				e.printStackTrace();
			} else if (e instanceof RemoteException
					|| e instanceof TrffException) {
				info.setMsg(e.getMessage());
			} else {
				info.setMsg("在执行过程中发生异常，请查看当天日志");
				e.printStackTrace();
			}
		}

		String msg = StringUtils.trimToEmpty(info.getMsg());
		JSONObject object = new JSONObject();
		object.put("isSuccess", info.isSuccess());
		object.put("msg", URLEncoder.encode(msg, "UTF-8"));

		callback = callback + "(" + object.toString() + ")";
		this.getResponse("text/html").getWriter().println(callback);
		System.out.println("暂停或恢复结果：[" + operNum + "][" + msg + "]");
		return null;
	}

	/**
	 * 强制叫号
	 * 
	 * @return
	 */
	public String forcedToCallNumber_status() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String isForcedToNumber = cacheManager
				.getSystemConfig("isForcedToNumber");// 是否启用强制叫号 0:是, 1:否
		String forcedToNumberTime = cacheManager
				.getSystemConfig("forcedToNumberTime");// 强制叫号时间
		Number number = numberService.forcedToCallNumber_status(loginIP);
		String numberCountFlag = "0";// 0代表号码池有可呼叫号，1 号码池没有可呼叫的号
		if (null == number) {
			numberCountFlag = "1";
		}
		// 判断上一个号是否已办完
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		if (null != searchNumber) {
			numberCountFlag = "2";
		}
		// System.out.println("numberCountFlag==="+numberCountFlag);
		JSONObject obj = new JSONObject();
		obj.put("isForcedToNumber", isForcedToNumber);
		obj.put("forcedToNumberTime", forcedToNumberTime);
		obj.put("numberCountFlag", numberCountFlag);
		this.getResponse("text/html").getWriter().println(obj.toString());
		return null;
	}

	/**
	 * 判断是否有暂停原因
	 * 
	 * @throws Exception
	 */
	public void judgePassReason() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		PassReason passReason = new PassReason();
		passReason.setDeptCode(deptCode);
		passReason.setDeptHall(deptHall);
		NumberManager numberManager = NumberManager.getInstance();
		Number searchNumber = numberManager.fetchCallNumber(operNum);
		JSONObject jsonObject = new JSONObject();

		if (null == searchNumber) {
			jsonObject.put("iscall", "false");
			callback = callback + "(" + jsonObject.toString() + ")";
			getResponse().getWriter().print(callback);
		} else {
			List<PassReason> list = passReasonService
					.selectPassReason(passReason);
			StringBuffer stringBuffer = new StringBuffer();
			if (list.size() != 0) {

				for (PassReason item : list) {
					stringBuffer
							.append("<option value='" + item.getCode() + "'>")
							.append(item.getReason()).append("</option>");
				}
				jsonObject.put("hasPassReason", true);
				jsonObject.put("msg", stringBuffer);
				String resultMsg = jsonObject.toString();
				URLEncoder.encode(resultMsg, "UTF-8");
				callback = callback + "(" + resultMsg + ")";
				getResponse("text/html").getWriter().print(callback);
			} else {
				jsonObject.put("hasPassReason", "false");
				callback = callback + "(" + jsonObject.toString() + ")";
				getResponse().getWriter().print(callback);
			}
		}
	}

	// 验证页面是否开启，LoginValidateAutoJob调用
	public String ValidateWP() {
		String msg = "";
		try {
			msg = numberService.validateYz(loginIP);
		} catch (Exception e) {
			msg="定時器調用失敗";
		}
		return msg;
	}

}