package com.suntendy.queue.employee.action;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.print.attribute.Size2DSyntax;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.webwork.ServletActionContext;
import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.domain.Role;
import com.suntendy.queue.employee.service.IEmployeeService;
import com.suntendy.queue.employee.util.ImageUtils;
import com.suntendy.queue.employee.util.teetaa.util.ImageHepler;
import com.suntendy.queue.login.service.ILoginService;
import com.suntendy.queue.login.vo.MenuLevel;
import com.suntendy.queue.systemlog.domain.Operate;
import com.suntendy.queue.systemlog.domain.Security;
import com.suntendy.queue.systemlog.operateLog.OperateLog;
import com.suntendy.queue.systemlog.securityLog.SecurityLog;
import com.suntendy.queue.systemlog.service.ISystemLogService;
import com.suntendy.queue.util.RSAUtilOperate;
import com.suntendy.queue.util.TimeUtilFormat;
import com.suntendy.queue.util.Unlawful;
import com.suntendy.queue.util.cache.CacheManager;

/*******************************************************************************
 * 描述: 用户管理 员工管理<br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘希臣 <br>
 * 创建日期: 2013-10-11 09:40:47 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class EmployeeAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IEmployeeService iEmployeeService;

	private ILoginService loginService;

	private File file1;

	private ISystemLogService iSystemLogService;

	public void setiSystemLogService(ISystemLogService iSystemLogService) {
		this.iSystemLogService = iSystemLogService;
	}

	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}

	public void setEmployeeService(IEmployeeService iEmployeeService) {
		this.iEmployeeService = iEmployeeService;
	}

	public File getFile1() {
		return file1;
	}

	public void setFile1(File file1) {
		this.file1 = file1;
	}

	/**
	 * 用户管理 信息记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String empManage() throws Exception {
		HttpServletRequest request = getRequest();
		Employee currentUser = (Employee) getHttpSession().getAttribute("user");
		String currentCode = currentUser.getCode();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Employee user = (Employee) this.getHttpSession().getAttribute("user");
		Employee emp = new Employee();
		List<Employee> list;
		String role = user.getRole();
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String status = request.getParameter("status");
		String RSA = "";
		String RSAkey = "";

		Employee employee = new Employee();
		employee.setCode(StringUtils.trimToEmpty(code)); // 编号
		employee.setName(StringUtils.trimToEmpty(name)); // 姓名
		employee.setStatus(StringUtils.trimToEmpty(status));// 是否有效
		employee.setUserFlag(StringUtils.trimToEmpty(request
				.getParameter("flag")));
		if (!role.equals("0")) {
			employee.setDeptHall(deptHall);
		}

		List<Employee> empList = null;
		try {
			empList = iEmployeeService.searchWithoutPhoto(employee);
		} catch (Exception e) {
			e.printStackTrace();
		} // 查询用户列表
		if (null != empList && !empList.isEmpty()) {
			for (int i = 0; i < empList.size(); i++) {
				employee = (Employee) empList.get(i);
				String id = employee.getId(); // 序号
				if (null != employee.getPolice()
						&& employee.getPolice().equals("0")) {
					employee.setPolice("是");
				} else {
					employee.setPolice("否");
				}
				
				Role role2=new Role();
				List<Role> list1=iEmployeeService.getRoleList(role2);
				/*if (null != employee.getModuleRdac()) {
					int key = Integer.parseInt(employee.getModuleRdac());
					switch (key) {
					case 0:
						employee.setModuleRdac("业务管理");
						break;
					case 1:
						employee.setModuleRdac("系统管理");
						break;
					case 2:
						employee.setModuleRdac("安全管理");
						break;
					case 3:
						employee.setModuleRdac("审计管理");
						break;
					default:
						break;
					}
				}*/
				if ("0".equals(employee.getRole())) {
					employee.setRole("超级管理员");
				} else if ("1".equals(employee.getRole())) {
					employee.setRole("系统管理员");
				} else if ("2".equals(employee.getRole())) {
					employee.setRole("普通用户");
				}
				if ("1".equals(employee.getUserFlag())) {// 用户
					request.setAttribute("role", currentUser.getRole());
					String fmtStatus = employee.getStatus(); // 状态
					String istatus = fmtStatus.equals("1") ? "置为失效" : "置为有效";
					// 状态信息
					String fmt_status = "<a href=\"#\" style=\"color:blue;\" onclick=\"editStatus('"
							+ id
							+ "','"
							+ fmtStatus
							+ "');return false;\">"
							+ istatus + "</a> ";
					// 密码复位
					String resetPwd = "<a href=\"#\" style=\"color:blue;\" onclick=\"resetPwd('"
							+ id + "');return false;\">密码复位</a>";
					// 操作按钮
					String operate = "<a href=\"employee!editEmp.action?id="
							+ id
							+ "&method=edit\"><img src=/queue/images/button_edit.jpg /></a>"
							+ "&nbsp;<a onclick=\"deleteEmp('"
							+ id
							+ "');\"><img src='/queue/images/button_del.jpg' style='cursor:hand' /></a>"
							+ "&nbsp;<a href=\"employee!editEmpYXQ.action?id="
							+ id
							+ "&method=edit\"><img src=/queue/images/button_edit.jpg /></a>";
					employee.setFmt_status(fmt_status);
					employee.setResetPwd(resetPwd);

					if ("admin".equals(currentCode)
							&& currentCode.equals(employee.getCode())) {
						employee.setFmt_status(null);
						operate = "<a href=\"employee!editEmp.action?id="
								+ id
								+ "&method=edit\"><img src=/queue/images/button_edit.jpg /></a>";
					}
					employee.setOperate(operate);
					emp.setId(id);
					list = iEmployeeService.getEmpList(emp);
					String a = String.valueOf(list.get(0).getName().length());
					RSA = list.get(0).getCode() + a + list.get(0).getPassword();
					if (list.get(0).getRSACheck() != null
							&& !list.get(0).getRSACheck().equals("")) {
						RSAkey = RSAUtilOperate.RSAOperate(list.get(0)
								.getRSACheck(), 1);
						if (RSA != null && !RSA.equals(RSAkey)) {
							// 非法篡改插入日志 只狼
							String eventType = "改";
							String event = "非法篡改";
							String result1 = "0";
							String content = list.get(0).getName()
									+ "用户数据发生非法篡改!";
							SecurityLog sLog = new SecurityLog();
							Security security = sLog.security_log(list.get(0)
									.getCode(), event, eventType, result1,
									content);
							security.setOpName(list.get(0).getCode());
							security.setContent(content.getBytes());
							iSystemLogService.addSecurity(security);
							employee = Unlawful.unlawfulEmp(employee);// 发生非法篡改
						}
					}

				} else {// 员工
					// 操作按钮
					String operate = "<a href=\"employee!editStaff.action?id="
							+ id
							+ "\"><img src=/queue/images/button_edit.jpg /></a>"
							+ "&nbsp;<a onclick=\"deleteEmp('"
							+ id
							+ "');\"><img src='/queue/images/button_del.jpg' style='cursor:hand' /></a>";
					employee.setOperate(operate);
					emp.setId(id);
					list = iEmployeeService.getEmpList(emp);
					if (list.get(0).getLoginIp() != null) {
						RSA = list.get(0).getCode() + list.get(0).getLoginIp()
								+ list.get(0).getDeptCode();
					} else {
						RSA = list.get(0).getCode() + list.get(0).getDeptCode();
					}
					if (list.get(0).getRSACheck() != null
							&& !list.get(0).getRSACheck().equals("")) {
						RSAkey = RSAUtilOperate.RSAOperate(list.get(0)
								.getRSACheck(), 1);
						if (RSA != null && !RSA.equals(RSAkey)) {
							employee = Unlawful.unlawfulEmp(employee);// 发生非法篡改
						}
					}
				}
			}
		}
		request.setAttribute("empList", empList);
		return "empManage";
	}

	/**
	 * 添加,用户修改 跳转控制
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public String editEmp() throws Exception{
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Employee currentUser = (Employee) getHttpSession().getAttribute("user");
		HttpServletRequest request = getRequest();
		String method = request.getParameter("method");
		String id = request.getParameter("id");
		String rolecode = request.getParameter("roleModule");
		if (id == null)
			id = "";
		id.trim(); // id唯一编号
		List menuRootList = loginService.getMenurootidByLevelid("");// 根结点
		List menuList = loginService.getMenuidAllByLevelid("",""); // 所有节点
		Role role3 =new Role();
		if(rolecode!=null){role3.setCode(rolecode);}
		
		List<Role> role2=iEmployeeService.getRoleList(role3);
		Employee employee = new Employee();
		if ("edit".equals(method)) { // 编辑信息
			employee.setId(id);
			List<Employee> roleList = iEmployeeService.getEmpList(employee);
			if (null != roleList && !roleList.isEmpty()) {
				employee = roleList.get(0);
				String levelid = employee.getLevelid();
				role2=iEmployeeService.getRoleList(role3);
				levelid=role2.get(0).getLevelid();
				String code = employee.getCode();
				String name = employee.getName();
				String sex = employee.getSex();
				String roleradio = employee.getRole();
				String department = employee.getDepartment();
				String idnumber = employee.getIdnumber();
				String police = employee.getPolice();
				String policeCode = employee.getPoliceCode();

				if (null != police && police.equals("0")) {
					police = "是";
				} else {
					police = "否";
				}
				String yhyxq = employee.getYhyxq();
				String kdlip = employee.getKdlip();
				String kdlsjd = employee.getKdlsjd();
				String passcode = employee.getPassCode();
				String roleModule = employee.getModuleRdac();
				request.setAttribute("listRole",
						role2);
				request.setAttribute("levelid", levelid);
				request.setAttribute("code", code);
				request.setAttribute("name", name);
				request.setAttribute("idnumber", idnumber);
				request.setAttribute("police", police);
				request.setAttribute("yhyxq", yhyxq);
				request.setAttribute("kdlip", kdlip);
				request.setAttribute("kdlsjd", kdlsjd);
				request.setAttribute("passcode", passcode);
				request.setAttribute("sex", sex);
				request.setAttribute("department", department);
				request.setAttribute("roleradio", roleradio);
				request.setAttribute("roleModule", roleModule);
				request.setAttribute("policeCode", policeCode);
			}
		} else
		
		request.setAttribute("listRole",
				role2);
		request.setAttribute("role", currentUser.getRole());
		request.setAttribute("menuRootList", menuRootList);
		request.setAttribute("menuList", menuList);
		request.setAttribute("method", method);
		request.setAttribute("id", id);

		return "editEmp";
	}

	/**
	 * 添加,用户修改 跳转控制
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public String editEmpYXQ() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		Employee currentUser = (Employee) getHttpSession().getAttribute("user");
		HttpServletRequest request = getRequest();
		String method = request.getParameter("method");
		String id = request.getParameter("id");
		if (id == null)
			id = "";
		id.trim(); // id唯一编号
		List menuRootList = loginService.getMenurootidByLevelid("");// 根结点
		List menuList = loginService.getMenuidAllByLevelid("",""); // 所有节点
		Employee employee = new Employee();
		if ("edit".equals(method)) { // 编辑信息
			employee.setId(id);
			List<Employee> roleList = iEmployeeService.getEmpList(employee);
			if (null != roleList && !roleList.isEmpty()) {
				employee = roleList.get(0);
				String levelid = employee.getLevelid();
				String code = employee.getCode();
				String name = employee.getName();
				String sex = employee.getSex();
				String roleradio = employee.getRole();
				String department = employee.getDepartment();
				String idnumber = employee.getIdnumber();
				String police = employee.getPolice();
				String passCode = employee.getPassCode();
				if (null != police && police.equals("0")) {
					police = "是";
				} else {
					police = "否";
				}
				String yhyxq = employee.getYhyxq();
				String kdlip = employee.getKdlip();
				String kdlsjd = employee.getKdlsjd();
				String roleModule = employee.getModuleRdac();
				request.setAttribute("levelid", levelid);
				request.setAttribute("code", code);
				request.setAttribute("name", name);
				request.setAttribute("idnumber", idnumber);
				request.setAttribute("police", police);
				request.setAttribute("yhyxq", yhyxq);
				request.setAttribute("kdlip", kdlip);
				request.setAttribute("kdlsjd", kdlsjd);
				request.setAttribute("sex", sex);
				request.setAttribute("department", department);
				request.setAttribute("roleradio", roleradio);
				request.setAttribute("roleModule", roleModule);
				request.setAttribute("passCode", passCode);
			}
		} else
			request.setAttribute("role", currentUser.getRole());
		request.setAttribute("menuRootList", menuRootList);
		request.setAttribute("menuList", menuList);
		request.setAttribute("method", method);
		request.setAttribute("id", id);

		return "editEmpYXQ";
	}

	/**
	 * 保存用户相关信息
	 * 
	 * @return
	 */
	public String saveEmp() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String[] powers = request.getParameterValues("power");
		String levelid = "78";
		/*
		 * for (int i = 0; i < powers.length; i++) { levelid += "," + powers[i];
		 * } levelid = levelid.substring(1);
		 */
		String UUID = RSAUtilOperate.RSAOperate(
				request.getParameter("code").trim()
						+ request.getParameter("idnumber")
						+ request.getParameter("policeCode"), 0);
		Employee employee = new Employee();

		employee.setCode(request.getParameter("code").trim());
		employee.setDepartment(request.getParameter("dept").trim());
		employee.setName(request.getParameter("name").trim());
		employee.setSex(request.getParameter("sex"));
		employee.setPolice(request.getParameter("police"));
		employee.setIdnumber(request.getParameter("idnumber"));
		employee.setYhyxq(request.getParameter("yhyxq"));
		employee.setKdlip(request.getParameter("kdljsjip"));
		employee.setKdlsjd(request.getParameter("kdlsjd"));
		employee.setPassword(request.getParameter("password"));
		employee.setLevelid(levelid);
		employee.setDeptCode(cacheManager.getOfDeptCache("deptCode"));
		employee.setDeptHall(cacheManager.getOfDeptCache("deptHall"));
		employee.setRole(request.getParameter("roleradio"));
		String roleModule=request.getParameter("roleModule");
		employee.setModuleRdac(request.getParameter("roleModule"));
		employee.setPassCode(request.getParameter("yhyxq"));
		employee.setPoliceCode(request.getParameter("policeCode"));
		employee.setUUID(UUID);
		iEmployeeService.saveEmployee(employee);
		// 已改
		Employee emp1 = new Employee();
		emp1.setCode(request.getParameter("code").trim());
		emp1.setDeptCode(cacheManager.getOfDeptCache("deptCode"));
		emp1.setDeptHall(cacheManager.getOfDeptCache("deptHall"));
		List<Employee> emp = iEmployeeService.getEmpList(emp1);
		String id = emp.get(0).getId();
		iEmployeeService.deleteEmp(id);
		String a = String.valueOf(employee.getName().length());
		String context = emp.get(0).getCode() + a + emp.get(0).getPassword();
		String RSA = RSAUtilOperate.RSAOperate(context, 0);
		employee.setRSACheck(RSA);
		Role role3 =new Role();
		if(roleModule!=null){
			role3.setCode(roleModule);
			List<Role> role2=iEmployeeService.getRoleList(role3);
			levelid=role2.get(0).getLevelid();
			employee.setLevelid(levelid);
			}
		String flag = iEmployeeService.saveEmployee(employee);
		if (flag == null) {
			request.setAttribute("msg", "用户添加成功！");

			// 完成 新增操作进入日志功能 开始 只狼
			
			String event = "新增";
			String module = "权限管理";
			String moduleType = "用户管理";
			String eventType = "增";
			String coreBusiness = "0";
			String result = "0";
			String resultDepict = "新增成功";
			String content = Unlawful.unlawfulEmpMx(employee, 0);
			String opName = employee.getCode();
			// 用户名
			HttpSession session = getHttpSession();
			employee = new Employee();
			Employee user = (Employee) session.getAttribute("user");
			String userName = user.getName();

			OperateLog operateLog = new OperateLog();
			Operate operate = operateLog.operate_log(userName, event, module,
					moduleType, eventType, coreBusiness, result, resultDepict);
			operate.setResult(result);
			operate.setOpName(opName);
			operate.setContent(content.getBytes());
			try {
				iSystemLogService.addOperate(operate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 新增操作进入日志功能代码块结束

		} else {
			request.setAttribute("msg", "用户添加失败，<br>在执行过程中发生异常！");
		}
		
		request.setAttribute("action",
				"employee/employee!empManage.action?flag=1");
		return "addSuccess";
	}

	/**
	 * 修改用户 相关信息
	 */
	public void updateEmp() throws Exception {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		String id = request.getParameter("id");
		if (id == null)
			id = "";
		String status = request.getParameter("status");
		String resultDepict;
		String event;
		String event1;
		String content = "";
		if (status == null)
			status = "";
		if (status.equals("1")) {
			status = "0";
			resultDepict = "拉入黑名单，强制锁定！";
			event = "用户被锁定";
			event1 = "用户被锁定";
		} else if (status.equals("3")) {
			status = "1";
			event1 = "黑名单解除";
			resultDepict = "黑名单解锁成功";
			event = "用户解锁";
		} else {
			status = "1";
			event1 = "黑名单解除";
			resultDepict = "黑名单解锁成功";
			event = "用户解锁";
		}

		String password = request.getParameter("password");
		if (password == null)
			password = "";
		HttpSession session = getHttpSession();
		Employee user = (Employee) session.getAttribute("user");
		Employee employee = new Employee();
		employee.setId(id);
		// employee.setRole(role);
		List<Employee> list = iEmployeeService.getEmpList(employee);
		String code = list.get(0).getCode();
		employee.setStatus(status);
		employee.setPassword(password);
		int result = iEmployeeService.updateEmp(null, employee);
		if (status.equals("3") || status.equals("1")) {
			iEmployeeService.deleteSuoEmp(code);
		}
		// 完成 修改操作进入日志功能 开始
		String module = "权限管理";
		String moduleType = "用户管理";
		String eventType = "改";
		String coreBusiness = "0";
		String result1 = "0";

		// 只狼
		if (status.equals("3") || status.equals("1")) {
			content = user.getCode() + "对" + code + "用户进行【" + event1 + "】操作!";
		} else {
			content = user.getCode() + "对" + code + "用户进行【" + resultDepict
					+ "】操作!";
		}
		OperateLog operateLog = new OperateLog();
		Operate operate = operateLog.operate_log(user.getCode(), event, module,
				moduleType, eventType, coreBusiness, result1, resultDepict);
		operate.setResult(result1);
		operate.setOpName(code);
		operate.setContent(content.getBytes());
		iSystemLogService.addOperate(operate);
		SecurityLog sLog = new SecurityLog();
		Security security = sLog.security_log(user.getCode(), event1,
				eventType, result1, resultDepict);
		security.setOpName(code);
		security.setContent(content.getBytes());
		iSystemLogService.addSecurity(security);
		response.getWriter().print(result);
	}

	/**
	 * 删除用户数据
	 */
	public void deleteEmp() throws Exception {

		HttpServletRequest request = getRequest();
		// 用户为1 员工为0
		String flag = request.getParameter("flag");
		HttpServletResponse response = getResponse();
		String id = request.getParameter("id");
		int result = iEmployeeService.deleteEmp(id);

		if (result != 0) {

			if ("1".equals(flag)) {// 用户
				// 完成 删除操作进入日志功能 开始

				String event = "删除";
				String module = "权限管理";
				String moduleType = "用户管理";
				String eventType = "删";
				String coreBusiness = "0";
				String result1 = "0";
				String resultDepict = "删除成功";
				// 用户名
				// String userName = request.getParameter("yhdh"); --这种方式取不到
				HttpSession session = getHttpSession();
				Employee employee = new Employee();
				Employee user = (Employee) session.getAttribute("user");
				String userName = user.getName();

				OperateLog operateLog = new OperateLog();
				Operate operate = operateLog.operate_log(userName, event,
						module, moduleType, eventType, coreBusiness, result1,
						resultDepict);
				operate.setResult(result1);
				try {
					iSystemLogService.addOperate(operate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 删除操作进入日志功能代码块结束

			} else {// 员工

				// 完成 删除操作进入日志功能 开始

				String event = "删除";
				String module = "权限管理";
				String moduleType = "员工管理";
				String eventType = "删";
				String coreBusiness = "0";
				String result1 = "0";
				String resultDepict = "删除成功";
				// 用户名
				// String userName = request.getParameter("yhdh"); --这种方式取不到
				HttpSession session = getHttpSession();
				Employee employee = new Employee();
				Employee user = (Employee) session.getAttribute("user");
				String userName = user.getName();

				OperateLog operateLog = new OperateLog();
				Operate operate = operateLog.operate_log(userName, event,
						module, moduleType, eventType, coreBusiness, result1,
						resultDepict);
				operate.setResult(result1);
				try {
					iSystemLogService.addOperate(operate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 删除操作进入日志功能代码块结束

			}
		}

		response.getWriter().print(result);
	}

	/**
	 * 校验code唯一性
	 * 
	 * @throws Exception
	 */
	public String checkCode() throws Exception {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");

		String code = request.getParameter("code");
		if (code == null)
			code = "";
		code.trim();
		Employee employee = new Employee();
		employee.setCode(code);
		employee.setDeptHall(deptHall);
		String result = "0";
		List<Employee> codeExistsList = iEmployeeService.checkCode(employee);
		if (null != codeExistsList && !codeExistsList.isEmpty()) {
			result = "1";
		}
		response.getWriter().print(result);
		return null;
	}

	/**
	 * 校验idNumber唯一性
	 * 
	 * @throws Exception
	 */

	public String checkIdNumber() throws Exception {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();

		String idNumber = request.getParameter("idNumber");
		Employee employee = new Employee();
		employee.setIdnumber(idNumber);

		String result = "0";
		List<Employee> idNumberExistsList = iEmployeeService.check(employee);
		if (null != idNumberExistsList && !idNumberExistsList.isEmpty()) {
			result = "1";
		}
		response.getWriter().print(result);
		return null;
	}

	/**
	 * 校验PoliceCode唯一性
	 * 
	 * @throws Exception
	 */

	public String checkPoliceCode() throws Exception {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();

		String policeCode = request.getParameter("policeCode");
		Employee employee = new Employee();
		employee.setPoliceCode(policeCode);

		String result = "0";
		List<Employee> idNumberExistsList = iEmployeeService.check(employee);
		if (null != idNumberExistsList && !idNumberExistsList.isEmpty()) {
			result = "1";
		}
		response.getWriter().print(result);
		return null;
	}

	/**
	 * 修改权限
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateRole() throws Exception {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("flag_id");
		String dept = request.getParameter("dept");
		String name = request.getParameter("name").trim();
		String sex = request.getParameter("sex");
		String police = request.getParameter("police");
		String yhyxq = request.getParameter("yhyxq");
		String kdlip = request.getParameter("kdlip");
		String kdlsjd = request.getParameter("kdlsjd");
		String policeCode = request.getParameter("policeCode");
		String roleModule = request.getParameter("roleModule");
		Employee employee = new Employee();
		employee.setId(id);
		List<Employee> emp = iEmployeeService.getEmpList(employee);
		employee.setDepartment(dept);
		employee.setName(name);
		employee.setSex(sex);
		employee.setPolice(police);
		employee.setYhyxq(yhyxq);
		employee.setKdlip(kdlip);
		employee.setKdlsjd(kdlsjd);
		employee.setPoliceCode(policeCode);
		employee.setModuleRdac(roleModule);
		Role role3 =new Role();
		if(roleModule!=null){
			role3.setCode(roleModule);
			List<Role> role2=iEmployeeService.getRoleList(role3);
			String levelid=role2.get(0).getLevelid();
			employee.setLevelid(levelid);
			}
		
		int result = iEmployeeService.updateEmp(null, employee);
		if (result == 1) {
			request.setAttribute("msg", "用户修改成功！");

			// 完成 修改操作进入日志功能 开始
			// 只狼
			String event = "修改";
			String module = "权限管理";
			String moduleType = "用户管理";
			String eventType = "改";
			String coreBusiness = "0";
			String result1 = "0";
			String resultDepict = "修改用户成功";
			String content = Unlawful.unlawfulEmpbj(employee, emp.get(0));
			// 用户名
			HttpSession session = getHttpSession();
			employee = new Employee();
			Employee user = (Employee) session.getAttribute("user");
			String userName = user.getName();

			OperateLog operateLog = new OperateLog();
			Operate operate = operateLog.operate_log(userName, event, module,
					moduleType, eventType, coreBusiness, result1, resultDepict);
			operate.setResult(result1);
			operate.setUserName(userName);
			operate.setOpName(emp.get(0).getCode());
			operate.setContent(content.getBytes());
			try {
				iSystemLogService.addOperate(operate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 修改操作进入日志功能代码块结束

		} else {
			request.setAttribute("msg", "权限修改失败，<br>在执行过程中发生异常！");
		}
		request.setAttribute("action",
				"employee/employee!empManage.action?flag=1");
		return "addSuccess";
	}

	/**
	 * 修改权限
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateRoleYXQ() throws Exception {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("flag_id");
		String dept = request.getParameter("dept");
		String name = request.getParameter("name").trim();
		String sex = request.getParameter("sex");
		String police = request.getParameter("police");
		String yhyxq = request.getParameter("yhyxq");
		String kdlip = request.getParameter("kdlip");
		String kdlsjd = request.getParameter("kdlsjd");
		String passCode = request.getParameter("passCode");
		String levelid = "";
		Employee employee = new Employee();
		employee.setId(id);
		List<Employee> emp = iEmployeeService.getEmpList(employee);
		employee.setDepartment(dept);
		employee.setLevelid(levelid);
		employee.setName(name);
		employee.setSex(sex);
		employee.setPolice(police);
		employee.setYhyxq(yhyxq);
		employee.setKdlip(kdlip);
		employee.setKdlsjd(kdlsjd);
		employee.setPassCode(passCode);
		employee.setStatus("3");
		int result = iEmployeeService.updateEmp(null, employee);
		if (result == 1) {
			request.setAttribute("msg", "修改有效期成功！");
			// 只狼
			// 完成 修改操作进入日志功能 开始
			String event = "修改有效期";
			String module = "权限管理";
			String moduleType = "用户管理";
			String eventType = "改";
			String coreBusiness = "0";
			String result1 = "0";
			String resultDepict = "用户密码有效期修改成功";
			String content = Unlawful.unlawfulEmpbj(employee, emp.get(0));
			// 用户名
			HttpSession session = getHttpSession();
			employee = new Employee();
			Employee user = (Employee) session.getAttribute("user");
			String userName = user.getName();

			OperateLog operateLog = new OperateLog();
			Operate operate = operateLog.operate_log(userName, event, module,
					moduleType, eventType, coreBusiness, result1, resultDepict);
			operate.setResult(result1);
			operate.setUserName(userName);
			operate.setOpName(emp.get(0).getCode());
			operate.setContent(content.getBytes());
			try {
				iSystemLogService.addOperate(operate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 修改操作进入日志功能代码块结束

		} else {
			request.setAttribute("msg", "权限修改失败，<br>在执行过程中发生异常！");
		}
		request.setAttribute("action",
				"employee/employee!empManage.action?flag=1");
		return "addSuccess";
	}

	/**
	 * 员工修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editStaff() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String id = request.getParameter("id");
		if (id == null)
			id = "";
		id.trim(); // id唯一编号
		Employee employee = new Employee();
		employee.setId(id);
		employee.setUserFlag("0");
		employee.setDeptHall(deptHall);
		List<Employee> staffList = iEmployeeService.getEmpList(employee);
		if (null != staffList && !staffList.isEmpty() && !"".equals(id)) {
			employee = staffList.get(0);
			request.setAttribute("employee", employee);
		}
		request.setAttribute("id", id);
		return "editStaff";
	}

	/**
	 * 上传图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadImg() throws Exception {
		HttpServletRequest request = getRequest();
		String empliyeeid = request.getParameter("id");
		String filename = request.getParameter("filename");
		// filename = empliyeeid+filename.substring(filename.indexOf("."));
		Date currTime = new Date();
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddhhmmssS",
				Locale.US);
		filename = new String(
				(formatter2.format(currTime)).getBytes("iso-8859-1"))
				+ filename.substring(filename.indexOf("."));
		ServletContext d = ServletActionContext.getServletContext();
		String path = d.getRealPath("/");
		File uploadDirFile = new File(path + "/upload");
		try {
			ImageUtils.delAllFile(path + "/upload"); // 删除完里面所有内容
			String filePath = path + "/upload";
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!uploadDirFile.exists()) {
			uploadDirFile.mkdirs();
		}
		try {
			FileInputStream inputStream = new FileInputStream(this.getFile1());
			FileOutputStream outputStream = new FileOutputStream(path
					+ "upload\\" + filename);
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, length);
			}
			inputStream.close();
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("empliyeeid", empliyeeid);
		request.setAttribute("Picurl", filename);
		request.setAttribute("step", "2");
		return "uploadImg";
	}

	/**
	 * 显示图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getImg() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		HttpServletResponse response = getResponse("image/jpeg");
		String id = request.getParameter("id");
		Employee employee = new Employee();
		employee.setId(id);
		employee.setUserFlag("0");
		employee.setDeptHall(deptHall);
		List<Employee> staffList = iEmployeeService.getEmpList(employee);
		if (null != staffList && !staffList.isEmpty()) {
			employee = staffList.get(0);
			if (employee.getPhoto() != null) {
				ServletOutputStream out = response.getOutputStream();
				out.write(employee.getPhoto());
				out.flush();
				out.close();
			}
		}
		return null;
	}

	/**
	 * 保存剪切图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveCutPic() throws Exception {
		HttpServletRequest request = getRequest();
		// 获取剪切图片信息begin
		File files = null;
		String path = getHttpSession().getServletContext().getRealPath("/");
		int imageWidth = Integer.parseInt(request.getParameter("txt_width"));// 图片实际宽度
		int imageHeight = Integer.parseInt(request.getParameter("txt_height"));// 图片实际高度
		String txt_top = request.getParameter("txt_top");
		if (txt_top.indexOf("-") > 0) {
			txt_top = "0";
		}
		int cutTop = Integer.parseInt(txt_top);// 距离顶部
		String txt_left = request.getParameter("txt_left");
		if (txt_left.indexOf("-") > 0) {
			txt_left = "0";
		}
		int cutLeft = Integer.parseInt(txt_left);// 距离左边
		int dropWidth = Integer.parseInt(request.getParameter("txt_DropWidth"));// 截取框的宽
		int dropHeight = Integer.parseInt(request
				.getParameter("txt_DropHeight"));// 截取框的高
		// float imageZoom =
		// Float.parseFloat(request.getParameter("txt_Zoom"));//放大倍数
		String picture = request.getParameter("picture");
		String filename = request.getParameter("filename");
		String empliyeeid = request.getParameter("empliyeeid");
		filename = new String(filename.getBytes("iso-8859-1"), "utf-8");

		try {
			/*
			 * System.out.println("imageWidth : "+imageWidth);
			 * System.out.println("imageHeight : "+imageHeight);
			 * System.out.println("cutTop : "+cutTop);
			 * System.out.println("cutLeft : "+cutLeft);
			 * System.out.println("dropWidth : "+dropWidth);
			 * System.out.println("dropHeight : "+dropHeight);
			 * System.out.println("imageZoom : "+imageZoom);
			 * System.out.println("picture : "+picture);
			 * System.out.println("url :"+path+"/upload/"+picture);
			 */
			Rectangle rec = new Rectangle(cutLeft, cutTop, dropWidth,
					dropHeight);
			File file = new File(path + "/upload/" + picture);
			saveSubImage(new File(path + "/upload/" + picture), file,
					imageWidth, imageHeight, rec);
			files = new File(path + "upload\\" + filename);
			Employee employee = new Employee();
			employee.setId(empliyeeid);
			if (1 == iEmployeeService.updateEmp(files, employee)) {
				request.setAttribute("msg", "数据修改成功！");
			} else {
				request.setAttribute("msg", "数据修改失败，<br>在执行过程中发生异常！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 员工信息更新
	 * 
	 * @return
	 */
	public String updateStaff() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String id = request.getParameter("flag_id");
		String loginIp = request.getParameter("loginIp");
		String WSIp = request.getParameter("WSIp");
		String comments = request.getParameter("comments");
		String department = request.getParameter("department");
		String RSA = code + loginIp + deptCode;
		// flag是判断是否有剪切图片的标记 0没有 1有剪切图片
		Employee employee = new Employee();
		employee.setDepartment(deptCode);
		employee.setComments(comments);
		employee.setLoginIp(loginIp);
		employee.setName(name);
		employee.setCode(code);
		employee.setWSIp(WSIp);
		employee.setRSACheck(RSAUtilOperate.RSAOperate(RSA, 0));
		if (!"".equals(id)) {
			employee.setId(id);
			if (1 == iEmployeeService.updateEmp(file1, employee)) {
				request.setAttribute("msg", "数据修改成功！");

				// 完成 修改操作进入日志功能 开始

				String event = "修改";
				String module = "权限管理";
				String moduleType = "员工管理";
				String eventType = "改";
				String coreBusiness = "0";
				String result = "0";
				String resultDepict = "修改成功";
				// 用户名
				// String userName = request.getParameter("yhdh"); --这种方式取不到
				HttpSession session = getHttpSession();
				employee = new Employee();
				Employee user = (Employee) session.getAttribute("user");
				String userName = user.getName();

				OperateLog operateLog = new OperateLog();
				Operate operate = operateLog.operate_log(userName, event,
						module, moduleType, eventType, coreBusiness, result,
						resultDepict);
				operate.setResult(result);
				try {
					iSystemLogService.addOperate(operate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 修改操作进入日志功能代码块结束

			} else {
				request.setAttribute("msg", "数据修改失败，<br>在执行过程中发生异常！");
			}
		} else {
			employee.setDeptCode(deptCode);
			employee.setDeptHall(deptHall);
			String result = iEmployeeService.savestaff(file1, employee);
			if (!"".equals(result)) {
				request.setAttribute("msg", "数据添加成功！");

				// 完成 修改操作进入日志功能 开始

				String event = "修改";
				String module = "权限管理";
				String moduleType = "员工管理";
				String eventType = "改";
				String coreBusiness = "0";
				String result1 = "0";
				String resultDepict = "修改成功";
				// 用户名
				// String userName = request.getParameter("yhdh"); --这种方式取不到
				HttpSession session = getHttpSession();
				employee = new Employee();
				Employee user = (Employee) session.getAttribute("user");
				String userName = user.getName();

				OperateLog operateLog = new OperateLog();
				Operate operate = operateLog.operate_log(userName, event,
						module, moduleType, eventType, coreBusiness, result1,
						resultDepict);
				operate.setResult(result1);
				try {
					iSystemLogService.addOperate(operate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 修改操作进入日志功能代码块结束

			} else {
				request.setAttribute("msg", "数据添加失败，<br>在执行过程中发生异常！");
			}
		}

		file1 = null;
		request.setAttribute("action",
				"employee/employee!empManage.action?flag=0");
		return "addSuccess";
	}

	// 剪切
	private static void saveSubImage(File srcImageFile, File descDir,
			int width, int height, Rectangle rect) throws IOException {
		ImageHepler.cut(srcImageFile, descDir, width, height, rect);
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String resetPwd() {
		HttpServletRequest request = getRequest();
		Employee employee = (Employee) getHttpSession().getAttribute("user");
		request.setAttribute("code", employee.getCode());
		request.setAttribute("id", employee.getId());
		return "resetPwd";
	}

	/**
	 * 密码修改 原密码校验 _before
	 * 
	 * @throws Exception
	 */
	public String checkBeforePwd() throws Exception {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String id = request.getParameter("id");
		String before = request.getParameter("_before");
		Employee employee = new Employee();
		employee.setId(id);
		employee.setPassword(before);
		employee.setDeptHall(deptHall);
		List<Employee> empList = iEmployeeService.getEmpList(employee);
		if (null != empList && !empList.isEmpty()) {
			response.getWriter().print("1");
		} else {
			response.getWriter().print("0");
		}
		return null;
	}

	/**
	 * 密码修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updatePwd() throws Exception {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		String password = request.getParameter("password");

		Employee employee = new Employee();

		employee.setId(id);
		employee.setPassword(password);
		employee.setStatus("1");
		iEmployeeService.updateEmp(null, employee);
		// 评测改
		List<Employee> emp = iEmployeeService.getEmpList(employee);
		String a = String.valueOf(emp.get(0).getName().length());
		String context = emp.get(0).getCode() + a + emp.get(0).getPassword();
		String RSA = RSAUtilOperate.RSAOperate(context, 0);
		employee.setRSACheck(RSA);
		int result = iEmployeeService.updateEmp(null, employee);
		if (result == 1) {
			request.setAttribute("msg", "密码修改成功！");

			// 完成 修改操作进入日志功能 开始

			String event = "修改";
			String module = "权限管理";
			String moduleType = "修改密码";
			String eventType = "改";
			String coreBusiness = "0";
			String result1 = "0";
			String resultDepict = "修改密码成功";
			// 用户名
			// String userName = request.getParameter("yhdh"); --这种方式取不到
			HttpSession session = getHttpSession();
			employee = new Employee();
			Employee user = (Employee) session.getAttribute("user");
			String userName = user.getCode();

			OperateLog operateLog = new OperateLog();

			try {
				Operate operate = operateLog.operate_log(userName, event,
						module, moduleType, eventType, coreBusiness, result1,
						resultDepict);
				operate.setResult(result1);
				iSystemLogService.addOperate(operate);
				SecurityLog sLog = new SecurityLog();
				resultDepict = "修改用户密码成功";
				event = "修改";
				Security security = sLog.security_log(userName, event,
						eventType, result1, resultDepict);
				iSystemLogService.addSecurity(security);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 修改操作进入日志功能代码块结束

		} else {
			request.setAttribute("msg", "密码修改失败，<br>在执行过程中发生异常！");
		}
		request.setAttribute("action", "employee/employee!resetPwd.action");
		return "addSuccess";
	}

	/**
	 * 权限管理获取用户信息 旧
	 * 
	 * @return
	 * 
	 *         public String empRdac() throws Exception { HttpServletRequest
	 *         request = getRequest(); CacheManager cacheManager =
	 *         CacheManager.getInstance(); String deptCode =
	 *         cacheManager.getOfDeptCache("deptCode"); String deptHall =
	 *         cacheManager.getOfDeptCache("deptHall"); String name =
	 *         request.getParameter("code"); String moduleRdac =
	 *         request.getParameter("moduleRdac"); String police =
	 *         request.getParameter("police");
	 * 
	 *         Employee employee = new Employee();
	 *         employee.setDeptHall(deptHall); employee.setDeptCode(deptCode);
	 *         employee.setName(name); if (null != moduleRdac &&
	 *         !moduleRdac.equals("null")) { employee.setModuleRdac(moduleRdac);
	 *         }
	 * 
	 *         if (null != police && !police.equals("null")) {
	 *         employee.setPolice(police); } String Rsa = ""; String RSA = "";
	 *         List<Employee> list = iEmployeeService.getEmpList(employee); for
	 *         (Employee emp : list) { String id = emp.getId(); // 序号 RSA =
	 *         RSAUtilOperate.RSAOperate(emp.getRSACheck(), 1); if
	 *         ("1".equals(emp.getUserFlag())) { if (null != emp.getPolice() &&
	 *         emp.getPolice().equals("0")) { emp.setPolice("是"); } else {
	 *         emp.setPolice("否"); } if (null != emp.getModuleRdac()) { int key
	 *         = Integer.parseInt(emp.getModuleRdac()); switch (key) { case 0:
	 *         emp.setModuleRdac("业务管理"); break; case 1:
	 *         emp.setModuleRdac("系统管理"); break; case 2:
	 *         emp.setModuleRdac("安全管理"); break; case 3:
	 *         emp.setModuleRdac("审计管理"); break; default: break; } } String a =
	 *         String.valueOf(emp.getName().length());
	 *         emp.setRSACheck(emp.getCode() + a + emp.getPassword()); if (RSA
	 *         != null && RSA.equals(emp.getRSACheck())) { String operate =
	 *         "<a href=\"updateRdac.action?id=" + id +
	 *         "\"><img src=/queue/images/button_edit.jpg /></a>";
	 *         emp.setOperate(operate); } else { emp =
	 *         Unlawful.unlawfulEmp(emp);// 发生非法篡改 } } else {
	 *         emp.setPolice("否"); emp.setModuleRdac("窗口员工"); if
	 *         (emp.getLoginIp() != null) { Rsa = emp.getCode() +
	 *         emp.getLoginIp() + emp.getDeptCode(); } else { Rsa =
	 *         emp.getCode() + emp.getDeptCode(); }
	 *         emp.setRSACheck(emp.getCode() + emp.getLoginIp() +
	 *         emp.getDeptCode()); if (RSA != null && RSA.equals(Rsa)) {
	 *         emp.setOperate("请到员工管理模块操作"); } else { emp =
	 *         Unlawful.unlawfulEmp(emp);// 发生非法篡改 } } }
	 *         request.setAttribute("list", list); return "success"; }
	 */
	/**
	 * 权限管理获取用户信息 旧
	 * 
	 * @return
	 */
	public String empRdac() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptcode = cacheManager.getOfDeptCache("deptCode");
		String depthall = cacheManager.getOfDeptCache("deptHall");
		String code = request.getParameter("code");
		String moduleRdac = request.getParameter("moduleRdac");
		String police = request.getParameter("police");
		Role role = new Role();
		role.setCode(code);
		role.setDeptcode(deptcode);
		role.setDepthall(depthall);
		List<Role> list = iEmployeeService.getRoleList(role);
		for (Role emp : list) {
			String id = emp.getId(); // 序号
			String operate = "<a href=\"updateRdac.action?id="
					+ id
					+ "\"><img src=/queue/images/button_edit.jpg /></a>"
					+ "&nbsp;<a href=\"delRole.action?id="
					+ id
					+ "\"><img src='/queue/images/button_del.jpg' style='cursor:hand' /></a>";
			emp.setOperate(operate);
		}
		request.setAttribute("list", list);
		return "success";
	}

	/**
	 * 添加角色 跳转控制
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public String addRole() throws Exception {
		HttpServletRequest request = getRequest();
		List menuRootList = loginService.getMenurootidByLevelid("");// 根结点
		List<MenuLevel> menuList = loginService.getMenuidAllByLevelid("","0"); // 所有节点
		List<MenuLevel> menuList1 = loginService.getMenuidAllByLevelid("","1"); // 所有节点
		List<MenuLevel> menuList2 = loginService.getMenuidAllByLevelid("","2"); // 所有节点
		List<MenuLevel> menuList3 = loginService.getMenuidAllByLevelid("","3"); // 所有节点
			request.setAttribute("menuRootList", menuRootList);
			request.setAttribute("menuRootList1", menuRootList);
			request.setAttribute("menuRootList2", menuRootList);
			request.setAttribute("menuRootList3", menuRootList);
			request.setAttribute("menuList", menuList);
			request.setAttribute("menuList1", menuList1);
			request.setAttribute("menuList2", menuList2);
			request.setAttribute("menuList3", menuList3);
		return "success";
	}

	/**
	 * 添加角色
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public String addRoled() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		HttpServletRequest request = getRequest();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String code = request.getParameter("code");
		String roleID = request.getParameter("RoleID");
		String content = request.getParameter("content");
		String[] powers = request.getParameterValues("power");
		String levelid = "";
		for (int i = 0; i < powers.length; i++) {
			levelid += "," + powers[i];
		}
		levelid = levelid.substring(1);
		Role role = new Role(code, content, levelid, deptHall, deptCode);
		role.setRoleID(roleID);
		iEmployeeService.saveRole(role);
		request.setAttribute("levelid", role.getLevelid());
		request.setAttribute("msg", "添加角色成功！");
		request.setAttribute("action", "employee/rdac.action");
		return "addSuccess";
	}

	/**
	 * 权限修改 跳转控制
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public String editRdac() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptcode = cacheManager.getOfDeptCache("deptCode");
		String depthall = cacheManager.getOfDeptCache("deptHall");
		HttpServletRequest request = getRequest();
		String method = request.getParameter("method");
		String id = request.getParameter("id");
		if (id == null)
			id = "";
		id.trim(); // id唯一编号
		List menuRootList = loginService.getMenurootidByLevelid("");// 根结点
		List menuList = loginService.getMenuidAllByLevelid("",""); // 所有节点
		Role role = new Role();
		role.setId(id);
		role.setDepthall(depthall);
		role.setDeptcode(deptcode);
		List<Role> list = iEmployeeService.getRoleList(role);
		if (null != list && !list.isEmpty()) {
			role = list.get(0);
			request.setAttribute("menuRootList", menuRootList);
			menuList = loginService.getMenuidAllByLevelid("",role.getRoleID()); // 所有节点
			request.setAttribute("menuList", menuList);
			request.setAttribute("levelid", role.getLevelid());
			request.setAttribute("code", role.getCode());
			request.setAttribute("id", role.getId());
			request.setAttribute("content", role.getContent());
		}
		return "success";
	}

	/**
	 * 保存修改权限
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveRdac() throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptcode = cacheManager.getOfDeptCache("deptCode");
		String depthall = cacheManager.getOfDeptCache("deptHall");
		String id = request.getParameter("flag_id");
		String code = request.getParameter("code");
		String content = request.getParameter("content");
		String[] powers = request.getParameterValues("power");
		String police = request.getParameter("police");
		String levelid = "";
		for (int i = 0; i < powers.length; i++) {
			levelid += "," + powers[i];
		}
		levelid = levelid.substring(1);
		Role role=new Role();
		role.setId(id);
		role.setCode(code);
		role.setContent(content);
		role.setLevelid(levelid);
		role.setDepthall(depthall);
		role.setDeptcode(deptcode);
		//List<Role> emp = iEmployeeService.getRoleList(role);
		//role.setId(emp.get(0).getId());
		int result = 0;
		result = iEmployeeService.updateRole(role);
		/*boolean b = true;
		/*String[] policeLevelid = new String[] { "42", "43", "45", "76", "77",
				"111", "119", "129", "130", "131", "132", "133", "135", "136",
				"137", "138" };
		if (null != police && police.equals("1")) {// 非警员判断权限
			for (int i = 0; i < policeLevelid.length; i++) {
				b = levelid.contains(policeLevelid[i]);
				if (b == true) {
					result = 2;
					break;
				}
			}
			if (result != 2) {
				result = iEmployeeService.updateEmp(null, employee);
			}

		} else {
			result = iEmployeeService.updateEmp(null, employee);
		}*/

		// 完成 修改操作进入日志功能 开始
		// 只狼
		/*String event = "修改权限";
		String module = "权限管理";
		String moduleType = "用户管理";
		String eventType = "改";
		String coreBusiness = "0";
		String result1 = "0";
		String resultDepict = "修改权限成功";
		String a = "";
		String b1 = "";
		String content = "";
		String con = Unlawful.unlawfulEmpRdac(employee, emp.get(0));
		String[] cond = con.split(":");
		if (cond[0].contains("有")) {
			content = "新增权限【" + cond[0] + "】";
		} else {
			List<MenuLevel> menu = loginService.getMenuidAllByLevelid(cond[0]);
			for (int i = 0; i < menu.size(); i++) {
				a += menu.get(i).getLeveltext() + "---";
			}
			content = "新增权限【" + a + "】";
		}

		if (cond[1].contains("有")) {
			content += "减少权限【" + cond[1] + "】";
		} else {
			List<MenuLevel> menu = loginService.getMenuidAllByLevelid(cond[1]);
			for (int i = 0; i < menu.size(); i++) {
				b1 += menu.get(i).getLeveltext() + "---";
			}
			content += "减少权限【" + b1 + "】";

		}

		// 用户名
		HttpSession session = getHttpSession();
		employee = new Employee();
		Employee user = (Employee) session.getAttribute("user");
		String userName = user.getName();

		OperateLog operateLog = new OperateLog();
		Operate operate = operateLog.operate_log(userName, event, module,
				moduleType, eventType, coreBusiness, result1, resultDepict);
		operate.setResult(result1);
		operate.setUserName(userName);
		operate.setOpName(emp.get(0).getCode());
		operate.setContent(content.getBytes());
		try {
			iSystemLogService.addOperate(operate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		// 修改操作进入日志功能代码块结束
		if (result == 1) {
			request.setAttribute("msg", "权限修改成功！");
		} else if (result == 2) {
			request.setAttribute("msg", "权限修改失败，<br>不能为非民警用户赋予民警专用权限！");
		} else {
			request.setAttribute("msg", "权限修改失败，<br>在执行过程中发生异常！");
		}
		request.setAttribute("action", "employee/rdac.action");
		return "addSuccess";
	}
	
	/**
	 * 删除用户数据
	 */
	public String delRole() throws Exception {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		int result = iEmployeeService.deleteRole(id);
		if (result == 1) {
			request.setAttribute("msg", "删除角色成功！");
		} else {
			request.setAttribute("msg", "角色已被删除！");
		} 
		request.setAttribute("action", "employee/rdac.action");
		return "success";
	}
	@SuppressWarnings({ "unchecked" })
	public void levelidRold()throws Exception{
		HttpServletRequest request = getRequest();
		String id = request.getParameter("flagid");
		
	}
}