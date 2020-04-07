package com.suntendy.queue.login.action;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.business.domain.Business;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.domain.Role;
import com.suntendy.queue.employee.service.IEmployeeService;
import com.suntendy.queue.login.service.ILoginService;
import com.suntendy.queue.login.tree.TreeUtil;
import com.suntendy.queue.login.vo.MenuLevel;
import com.suntendy.queue.login.vo.MenuRoot;
import com.suntendy.queue.safety.domain.Safety;
import com.suntendy.queue.safety.service.ISafetyService;
import com.suntendy.queue.systemlog.domain.Loginls;
import com.suntendy.queue.systemlog.domain.Operate;
import com.suntendy.queue.systemlog.domain.Security;
import com.suntendy.queue.systemlog.operateLog.OperateLog;
import com.suntendy.queue.systemlog.securityLog.SecurityLog;
import com.suntendy.queue.systemlog.service.ISystemLogService;
import com.suntendy.queue.util.EncryptionUtil;
import com.suntendy.queue.util.RSAUtilOperate;
import com.suntendy.queue.util.TimeUtilFormat;
import com.suntendy.queue.util.loginKeyUtil;
import com.suntendy.queue.util.HttpRequestUtil.HttpRequestUtil;
import com.suntendy.queue.util.cache.CacheManager;

@SuppressWarnings("unchecked")
public class LoginAction extends BaseAction {
	private String LogUser = null;
	private String session_id;
	private String userName;
	private int sessSum = 0;
	private static final long serialVersionUID = 1L;

	private ILoginService iLoginService;

	private IEmployeeService iEmployeeService;

	private ISystemLogService iSystemLogService;

	private ISafetyService safetyService;

	public void setSafetyService(ISafetyService safetyService) {
		this.safetyService = safetyService;
	}

	public void setiSystemLogService(ISystemLogService iSystemLogService) {
		this.iSystemLogService = iSystemLogService;
	}

	public void setEmployeeService(IEmployeeService iEmployeeService) {
		this.iEmployeeService = iEmployeeService;
	}

	public void setLoginService(ILoginService iLoginService) {
		this.iLoginService = iLoginService;
	}

	/**
	 * 用户登陆
	 */
	@SuppressWarnings("unused")
	public String login() throws Exception {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String yfip =cacheManager.getOfDeptCache("yfIp");
		String yfport =cacheManager.getOfDeptCache("yfPort");
		String jym = request.getParameter("JYM");
		String licenseKey = request.getParameter("licenseKey");
		String XTBS = cacheManager.getOfDeptCache("XTBS");
		String yfaqm = cacheManager.getOfDeptCache("yfaqm");
		session_id = request.getParameter("SESSION_ID");
		List<Employee> userlist = new ArrayList<Employee>();
		String yhdh = null;
		String pass = null;
		String username =null;
		String password =null;
		String yfur=yfip+":"+yfport;
		int flag=0;
		if(jym!=null){
			String bdjym;
			int key;
			String masterkeys = cacheManager.getOfDeptCache("masterkeys");//授权码有效期始
			if("1".equals(masterkeys)){
				key=0;
			}else {
				InetAddress addr = InetAddress.getLocalHost();
				String[] serIP=addr.toString().split("/");
				String serverIP=serIP[1];
				String serverMark=getLocalMac(addr);
				String deptCodeTop=deptCode.substring(1, 6);
				String deptCodeTail=deptCode.substring(7, 12);
				String masterkeyEnd =cacheManager.getOfDeptCache("masterkeyEnd");//授权码有效期止
			    bdjym = serverIP+masterkeys+deptCodeTop+serverMark+masterkeyEnd+deptCodeTail;//本机校验码组合
			    System.out.println("本机校验码组合"+bdjym);
	            key=yfJym(bdjym, jym);
			}
			
			if(key==0){
				String jymString=session_id+XTBS+yfaqm;
				String outputStrJym=EncryptionUtil.encodingMd5(jymString);
				String outputStr = "SESSION_ID="+session_id+"&XTBS="+XTBS+"&JYM="+outputStrJym;
				System.out.println("传入参数："+outputStr.toString());
				net.sf.json.JSONObject yfJson = HttpRequestUtil.httpRequestYf("http://"+yfur+"/compare/inf/SearchLoginInfo.do", "POST", outputStr);
				//net.sf.json.JSONObject yfJson = HttpRequestUtil.httpRequestYf("http://114.116.34.246:8181/compare/inf/SearchLoginInfo.do", "POST", outputStr);
				//net.sf.json.JSONObject yfJson = net.sf.json.JSONObject.fromObject("{\"CODE\":1001,\"MESSAGE\":\"查询成功\",\"DATA\":[{\"KEY\":\"1512460887aNSukePAvF\",\"JH\":\"J001\",\"XM\":\"张\",\"SFZH\":\"440882199411160510\",\"ROLE_ID\":\"suntendy\",\"SSBM _ID\":\"I90PO39DM7DJQPDIROW9JKR7DJS890M3\",\"SSBM_NA ME\":\"XX大队\"}]}");
				if(yfJson ==null){
					System.out.println("接口调用获取json为null");
					alipayforward(yfur, request, response);
					return ERROR;
				}
				
				if(yfJson.isEmpty()){
					System.out.println("接口调用获取json为空");
					alipayforward(yfur, request, response);
					return ERROR;
				}
				
				String codes = yfJson.get("CODE").toString();
				int code=Integer.parseInt(codes);
				
				if(1001!=code){
					System.out.println("code:["+code+"]--1001：查询成功 1002：查询失败 1003：用户未登录 1004 : 参数传递不完整 1005：系统标示错误 1006：安全码校验失败 1007：该系统无用户角色信息");
					alipayforward(yfur, request, response);
					return ERROR;
				}else {
					String yfCode1 = (String) yfJson.get("ACCOUNT");//账号
					userlist = this.iLoginService.login(yfCode1, "", "",
							"");
					if(userlist!=null&&userlist.size()>0){
						pass="123";
						LogUser=yfCode1;
						userName=yfCode1;
					}else{
						Employee employee = new Employee();
						String policeCode = (String) yfJson.get("JH");//警号
						String yfCode = (String) yfJson.get("ACCOUNT");//账号
						String yfName = (String) yfJson.get("XM");//姓名
						String yfDeptCode = (String) yfJson.get("SSBM_ID");//部门编号
						String yfDepartment = (String) yfJson.get("SSBM_NAME");//部门名称
						String yfsfz = (String) yfJson.get("SFZH");//身份证
						String roleID = (String) yfJson.get("ROLE_IDS");//权限id
						
						
						Role role = new Role();
						role.setId(roleID);
						List<Role> r=iEmployeeService.getRoleList(role);
						if(r!=null && r.size()>0){
							employee.setLevelid(r.get(0).getLevelid());
							employee.setModuleRdac(r.get(0).getCode());
						}else {
							employee.setLevelid("78");
							employee.setModuleRdac("无授权角色");
						}
						employee.setRole("1");
						String UUID = RSAUtilOperate.RSAOperate(yfCode + yfsfz + policeCode, 0);
						employee.setPoliceCode(policeCode);
						employee.setCode(yfCode);
						employee.setName(yfName);
						employee.setDeptCode(yfDeptCode);
						employee.setIdnumber(yfsfz);
						employee.setDepartment(yfDepartment);
						employee.setSex("男");
						employee.setPolice("0");
						employee.setYhyxq("2100-12-25");
						employee.setKdlip("");
						employee.setKdlsjd("7-23");
						employee.setPassword("123");
						employee.setDeptHall(deptHall);
						employee.setPassCode("2100-12-25");
						employee.setUUID(UUID);
						
						iEmployeeService.saveEmployee(employee);
							userlist = this.iLoginService.login(yfCode, "", "",
									"");
							pass="123";
							LogUser=yfCode;
							userName=yfCode;
					}
				}
			}else {
				System.out.println("安全码校验失败!返回云帆系统主页!");
				alipayforward(yfur, request, response);
				return ERROR;
			}
		}else {//原有的登录方式
			yhdh = request.getParameter("J_userName");
			pass = request.getParameter("J_passWrod");
			if(yhdh!=null||pass!=null){
				byte[] userkey = loginKeyUtil.decode(yhdh);
				byte[] passkey = loginKeyUtil.decode(pass);
				username = new String(userkey);
				password = new String(passkey);
			}else {
				flag=1;
			}
		}
		
		if(flag==0){
		String dlip = request.getRemoteAddr();
		Employee employee = new Employee();
		employee.setYhdh(username);
		employee.setDeptCode(deptCode);
		employee.setDeptHall(deptHall);
		employee.setDlip(dlip);
		
		List<Employee> dliplist = new ArrayList<Employee>();
		List<Employee> yhdhlist = new ArrayList<Employee>();
		SimpleDateFormat rq = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		dliplist = this.iLoginService.countLogin(null, dlip);
		yhdhlist = this.iLoginService.countLogin(username, null);
		if(userlist!=null&&userlist.isEmpty()){
			if ("suntendy".equals(username)) {
				userlist = this.iLoginService.login(username, password, "", "");
			} else {
				userlist = this.iLoginService.login(username, password, deptCode,
						deptHall);
			}
		}
		int loginSum = 99;// 一小时高频访问
		int ipSum = 99;// ip登录错误次数锁定
		int userSum = 99;// 用户登录次数锁定
		int sessionSum = 99;// 最大在綫人數
		Loginls loginls = new Loginls();
		loginls.setUserName(username);
		String loginCountls = iSystemLogService.queryLoginCount(loginls);
		List<Safety> list = safetyService.searchSafety();
		if (list.size() > 0) {
			Safety safety = list.get(0);
			loginSum = Integer.parseInt(safety.getVisits());
			ipSum = Integer.parseInt(safety.getIpSum());
			userSum = Integer.parseInt(safety.getUserSum());
			sessionSum = Integer.parseInt(safety.getSessionSum());
		}
		int loginCount = Integer.parseInt(loginCountls);
		if (sessSum >= sessionSum) {
			request.setAttribute("msg", "当前在线人数:" + sessSum
					+ "人,超出系统设定的最大在线人数!");
			return ERROR;
		} else {

			if (loginCount >= loginSum) {
				request.setAttribute("msg", "非法高频访问!");
				// 这里实现登录成功，插入到日志中的功能
				/*
				 * 0 成功 1 失败
				 */
				login_Log("1");

				// 安全操作进入日志功能 开始

				String event = (String) request.getAttribute("msg");
				String eventType = "查";
				String result = "1";
				String resultDepict = "非法高频访问";
				String content = "【" + username + "】用户一小时内共登录" + loginSum
						+ "次,已达到设定阀值!";
				SecurityLog securityLog = new SecurityLog();

				Security security = securityLog.security_log(username, event,
						eventType, result, resultDepict);
				security.setResult(result);
				security.setOpName("无");
				security.setContent(content.getBytes());
				iSystemLogService.addSecurity(security);

				// 安全进入日志功能代码块结束
				return ERROR;
			} else {
				if (dliplist.size() >= ipSum) {// ip错误5次以上锁定

					request.setAttribute("msg", "此电脑已锁定登录,请联系管理员!");
					// 这里实现登录成功，插入到日志中的功能
					/*
					 * 0 成功 1 失败
					 */
					login_Log("1");

					// 安全操作进入日志功能 开始

					String event = (String) request.getAttribute("msg");
					String eventType = "增";
					String result = "1";
					String resultDepict = "IP次数超出阀值";
					String content = "IP为【" + dlip + "】设备所有用户共计登录"
							+ dliplist.size() + "次,已达到设定阀值!";
					SecurityLog securityLog = new SecurityLog();

					Security security = securityLog.security_log(username,
							event, eventType, result, resultDepict);
					security.setResult(result);
					security.setContent(content.getBytes());
					OperateLog operateLog = new OperateLog();
					Operate operate = operateLog.operate_log(username,event, "用户登录", "登录界面", eventType, "1", result, resultDepict);
					operate.setContent(content.getBytes());
					iSystemLogService.addOperate(operate);
					iSystemLogService.addSecurity(security);

					// 安全进入日志功能代码块结束

					return ERROR;
				} else {
					if (yhdhlist.size() >= userSum) {
						request.setAttribute("msg", "此用户已锁定,请明天再试!");
						Employee em = new Employee();
						em.setCode(username);
						List<Employee> list1 = iEmployeeService.getEmpList(em);
						String id = list1.get(0).getId();
						em.setId(id);
						em.setStatus("0");
						iEmployeeService.updateEmp(null, em);
						// 这里实现登录失败，插入到日志中的功能
						/*
						 * 0 成功 1 失败
						 */
						login_Log("1");

						// 安全操作进入日志功能 开始

						String event = "此用户已锁定,被拉入黑名单";
						String eventType = "查";
						String result = "1";
						String resultDepict = "该用户登录次数超出设定值";
						String content = "用户【" + username + "】当天共计登录"
								+ yhdhlist.size() + "次,已达到设定阀值!次日恢复登录,有紧急情况请联系管理员";
						SecurityLog securityLog = new SecurityLog();
						Security security = securityLog.security_log(username,
								event, eventType, result, resultDepict);
						security.setResult(result);
						security.setOpName(username);
						security.setContent(content.getBytes());
						OperateLog operateLog = new OperateLog();
						Operate operate = operateLog.operate_log(username,event, "用户登录", "登录界面", eventType, "1", result, resultDepict);
						operate.setContent(content.getBytes());
						iSystemLogService.addOperate(operate);
						iSystemLogService.addSecurity(security);

						// 安全进入日志功能代码块结束

						return ERROR;
					} else {
						if (!userlist.isEmpty()) {
							Employee user = userlist.get(0);
							Role role =new Role();
							role.setCode(user.getModuleRdac());
							List<Role> r=iEmployeeService.getRoleList(role);
							if(!user.getModuleRdac().equals("0")){
								user.setLevelid(r.get(0).getLevelid());
							}
							
							if (user.getYhyxq() != null) {

								Date date = new Date();
								Date passdate = new Date();
								try {
									date = rq.parse(user.getYhyxq());
									passdate = rq.parse(user.getPassCode());
								} catch (ParseException e) {
									request.setAttribute("msg", "用户有效期格式不正确");
									e.printStackTrace();
									// 这里实现登录失败，插入到日志中的功能
									/*
									 * 0 成功 1 失败
									 */
									login_Log("1");

									// 安全操作进入日志功能 开始

									String event = (String) request
											.getAttribute("msg");
									String eventType = "增";
									String result = "1";
									String resultDepict = "有效期异常";
									SecurityLog securityLog = new SecurityLog();
									Security security = securityLog
											.security_log(username, event,
													eventType, result,
													resultDepict);
									security.setResult(result);

									iSystemLogService.addSecurity(security);

									// 安全进入日志功能代码块结束

									return ERROR;
								}
								if (date.getTime() > new Date().getTime()
										&& passdate.getTime() > new Date()
												.getTime()) {
									if (user.getKdlip() != null) {
										int j = 0;
										String[] ip = user.getKdlip()
												.split(",");
										for (int i = 0; i < ip.length; i++) {
											if (dlip.equals(ip[i])) {
												j = 1;
												break;
											}
										}
										if (j == 1) {
											if (user.getKdlsjd() != null) {
												String[] sjd = user.getKdlsjd()
														.split("-");
												if (sjd.length == 2) {
													int kssj = Integer
															.parseInt(sjd[0]);
													int jssj = Integer
															.parseInt(sjd[1]);
													if (kssj >= hour
															|| hour >= jssj) {
														request.setAttribute(
																"msg",
																"非规定时间外访问!");
														// 这里实现登录失败，插入到日志中的功能
														/*
														 * 0 成功 1 失败
														 */
														login_Log("1");

														// 安全操作进入日志功能 开始

														String event = (String) request
																.getAttribute("msg");
														String eventType = "增";
														String result = "1";
														String resultDepict = "非规定时间外访问";
														SecurityLog securityLog = new SecurityLog();
														Security security = securityLog
																.security_log(
																		username,
																		event,
																		eventType,
																		result,
																		resultDepict);
														security.setResult(result);
														String content="用户【"+username+"】于"+TimeUtilFormat.timeType(security.getRecordTime(), 0)
																+"登录,非规定时间"+kssj+":00-"+jssj+":59范围内";
														security.setContent(content.getBytes());
														iSystemLogService
																.addSecurity(security);
													}

													String[] jihe = user
															.getZxbj().split(
																	",");
													if (user.getZxbj() == null
															|| dlip.equals(jihe[1])
															|| "0".equals(jihe[0])) {
														String levelid = null;
														if ("3".equals(user
																.getStatus())) {
															levelid = "78";
														} else {

															levelid = user
																	.getLevelid(); // 叶子id
														}
														List menuroot = this.iLoginService
																.getMenurootidByLevelid(levelid);// 根结点
														List menu = this.iLoginService
																.getMenuidAllByLevelid(levelid,""); // 所有节点
														String tree = TreeUtil
																.createTreeInfo1(
																		menuroot,
																		menu,
																		username);
														HttpSession session = getHttpSession();
														session.setAttribute(
																"user", user);
														session.setAttribute(
																"tree", tree);
														Employee empzxbj = new Employee();
														empzxbj.setId(user
																.getId());
														empzxbj.setZxbj("1,"
																+ dlip);
														int result = this.iEmployeeService
																.updateEmp(
																		null,
																		empzxbj);
														// 查询出离上一次登录成功的失败数据
														user = loginShow(
																request,
																session, user);
														return SUCCESS;
													} else {
														request.setAttribute(
																"msg",
																"该用户已在"
																		+ user.getZxbj()
																		+ "登陆,请退出后再试!");
														// 这里实现登录失败，插入到日志中的功能
														/*
														 * 0 成功 1 失败
														 */
														login_Log("1");

														// 安全操作进入日志功能 开始

														String event = "用户已在线";
														String eventType = "增";
														String result = "1";
														String resultDepict = "IP:"
																+ user.getZxbj()
																+ "用户已在线";
														SecurityLog securityLog = new SecurityLog();
														Security security = securityLog
																.security_log(
																		username,
																		event,
																		eventType,
																		result,
																		resultDepict);
														security.setResult(result);
														iSystemLogService
																.addSecurity(security);

														// 安全进入日志功能代码块结束

														return ERROR;
													}

												} else {
													request.setAttribute("msg",
															"该用户登录时间段格式不正确!");
													return ERROR;
												}
											} else {
												String levelid = user
														.getLevelid(); // 叶子id
												List menuroot = this.iLoginService
														.getMenurootidByLevelid(levelid);// 根结点
												List menu = this.iLoginService
														.getMenuidAllByLevelid(levelid,""); // 所有节点
												String tree = TreeUtil
														.createTreeInfo1(
																menuroot, menu,
																username);
												HttpSession session = getHttpSession();
												session.setAttribute("user",
														user);
												session.setAttribute("tree",
														tree);
												// 查询出离上一次登录成功的失败数据
												user = loginShow(request,
														session, user);
												return SUCCESS;
											}

										} else {
											request.setAttribute("msg",
													"该用户不能在此设备登录!");
											// 这里实现登录失败，插入到日志中的功能
											/*
											 * 0 成功 1 失败
											 */
											login_Log("1");

											// 安全操作进入日志功能 开始

											String event = (String) request
													.getAttribute("msg");
											String eventType = "查";
											String result = "1";
											String resultDepict = "非规定设备访问失败";
											SecurityLog securityLog = new SecurityLog();
											Security security = securityLog
													.security_log(username,
															event, eventType,
															result,
															resultDepict);
											security.setResult(result);
											String content="可登录IP【"+user.getKdlip()+"】---在非规定IP【"+dlip+"】存在登录记录！";
											security.setContent(content.getBytes());
											iSystemLogService
													.addSecurity(security);

											// 安全进入日志功能代码块结束

											return ERROR;
										}
									} else {
										String levelid = user.getLevelid(); // 叶子id
										List menuroot = this.iLoginService
												.getMenurootidByLevelid(levelid);// 根结点
										List menu = this.iLoginService
												.getMenuidAllByLevelid(levelid,""); // 所有节点
										String tree = TreeUtil.createTreeInfo1(
												menuroot, menu, username);
										HttpSession session = getHttpSession();
										session.setAttribute("user", user);
										session.setAttribute("tree", tree);
										// 查询出离上一次登录成功的失败数据
										user = loginShow(request, session, user);
										return SUCCESS;
									}
								} else {
									String event = "用户已过期";
									String resultDepict = "用户过期";
									if (passdate.getTime() > new Date()
											.getTime()) {
										request.setAttribute("msg", "该用户已在"
												+ user.getZxbj()
												+ "登陆,请退出后再试!!");
										event = "用户已登录";
										resultDepict = "用户已登录";
									} else {
										request.setAttribute("msg",
												"用户或密码已过期!!");
										event = "用户已过期";
										resultDepict = "用户过期";
									}
									// 这里实现登录失败，插入到日志中的功能
									/*
									 * 0 成功 1 失败
									 */
									login_Log("1");

									// 安全操作进入日志功能 开始

									String eventType = "查";
									String result = "1";
									SecurityLog securityLog = new SecurityLog();
									Security security = securityLog
											.security_log(username, event,
													eventType, result,
													resultDepict);
									security.setResult(result);
									iSystemLogService.addSecurity(security);

									// 安全进入日志功能代码块结束

									return ERROR;
								}
							} else {
								String levelid = user.getLevelid(); // 叶子id
								List menuroot = this.iLoginService
										.getMenurootidByLevelid(levelid);// 根结点
								List menu = this.iLoginService
										.getMenuidAllByLevelid(levelid,""); // 所有节点
								String tree = TreeUtil.createTreeInfo1(
										menuroot, menu, username);
								HttpSession session = getHttpSession();

								session.setAttribute("tree", tree);

								// 这里实现登录成功，插入到日志中的功能
								/*
								 * 0 成功 1 失败
								 */
								// 查询出离上一次登录成功的失败数据
								user = loginShow(request, session, user);
								session.setAttribute("user", user);
								return SUCCESS;
							}
						} else {
							this.iLoginService.addLoginLog(employee);
							request.setAttribute("msg", "用户名或密码错误，请重新登录！");
							// 这里实现登录失败，插入到日志中的功能
							/*
							 * 0 成功 1 失败
							 */
							login_Log("1");

							// 安全操作进入日志功能 开始

							String event = (String) request.getAttribute("msg");
							String eventType = "增";
							String result = "1";
							String resultDepict = "用户名或密码错误";
							SecurityLog securityLog = new SecurityLog();
							Security security = securityLog.security_log(
									username, event, eventType, result,
									resultDepict);
							security.setResult(result);
							iSystemLogService.addSecurity(security);

							// 安全进入日志功能代码块结束

							return ERROR;
						}
					}
				}

			}
		}// 同时在线人数
		
		}else{
			request.setAttribute("msg", "用户名或密码错误，请重新登录！");
			return ERROR;
		}
	}

	/**
	 * 退出登录
	 */
	public String exit() throws Exception {
		HttpSession session = getHttpSession();
		Employee employee = new Employee();
		Employee user = (Employee) session.getAttribute("user");
		if (user != null && !user.equals("")) {
			employee.setId(user.getId());
			employee.setZxbj("0,0");
			this.iEmployeeService.updateEmp(null, employee);
		}
		session.removeAttribute("user");
		session.removeAttribute("tree");

		Date leavetime = new Date();
		iSystemLogService.updateLoginlsExit(leavetime);
		
		sessSum = sessSum - 1;
		
		if(session_id!=null){
			HttpServletRequest request = getRequest();
			HttpServletResponse response = getResponse();
			CacheManager cacheManager = CacheManager.getInstance();
			String yfip =cacheManager.getOfDeptCache("yfIp");
			String yfport =cacheManager.getOfDeptCache("yfPort");
			String yfur=yfip+":"+yfport;
			alipayforward(yfur, session_id, request, response);
		}
		
		return "";
	}

	/*
	 * 登录成功 or 失败，插入到日志中的功能 统一实现方法 result 通过result的值判断是否登录成功
	 */
	public Loginls login_Log(String result) throws Exception {
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		Loginls loginls = new Loginls();
		String username;
		if(LogUser!=null){
			username= LogUser;
		}else {
			String yhdh = request.getParameter("J_userName");
			if("".equals(yhdh) || yhdh==null){
				username=userName;
			}else {
				byte[] userkey = loginKeyUtil.decode(yhdh);
				username= new String(userkey);
			}
		}
		
		if (result == "0") {

			// 管理部门
			String deptCode = cacheManager.getOfDeptCache("deptCode");
			// 大厅编号
			String deptHall = cacheManager.getOfDeptCache("deptHall");
			// 来源ip
			String originIp = request.getRemoteAddr();
			// String originIp =
			// StringUtils.trimToEmpty(request.getParameter("originIp"));

			// 事件类型
			// String eventType = request.getParameter("eventType");
			String eventType = "登录";
			// 事件
			// String event="系统"+eventType;
			String event = "系统登录";
			// 结果
			// String result =
			// StringUtils.trimToEmpty(request.getParameter("result"));
			result = "0";
			// 结果描述
			String resultDepict = "登录成功";
			// 登录时间
			Date recordTime = new Date();

			loginls.setUserName(username);
			loginls.setDeptCode(deptCode);
			loginls.setDeptHall(deptHall);
			loginls.setEvent(event);
			loginls.setEventType(eventType);
			loginls.setName(username);
			loginls.setOriginIp(originIp);
			loginls.setRecordTime(recordTime);
			loginls.setResult(result);
			loginls.setResultDepict(resultDepict);

			String RSA = RSAUtilOperate.RSAOperate(username + originIp, 0);
			loginls.setRSACheck(RSA);
			iSystemLogService.addLoginls(loginls);

			// 登录成功结束 -----------------------------------

		} else {
			// 管理部门
			String deptCode = cacheManager.getOfDeptCache("deptCode");
			// 大厅编号
			String deptHall = cacheManager.getOfDeptCache("deptHall");
			// 用户名

			// 来源ip
			String originIp = request.getRemoteAddr();
			// String originIp =
			// StringUtils.trimToEmpty(request.getParameter("originIp"));

			// 事件类型
			// String eventType = request.getParameter("eventType");
			String eventType = "登录";
			// 事件
			// String event="系统"+eventType;
			String event = "系统登录";
			// 结果
			// String result =
			// StringUtils.trimToEmpty(request.getParameter("result"));
			result = "1";
			// 获取提示信息 结果描述
			String resultDepict = (String) request.getAttribute("msg");

			// 登录时间
			Date recordTime = new Date();
			Date leaveTime = recordTime;

			loginls.setUserName(username);
			loginls.setDeptCode(deptCode);
			loginls.setDeptHall(deptHall);
			loginls.setEvent(event);
			loginls.setEventType(eventType);
			loginls.setName(username);
			loginls.setOriginIp(originIp);
			loginls.setRecordTime(recordTime);
			loginls.setLeaveTime(leaveTime);
			loginls.setResult(result);
			loginls.setResultDepict(resultDepict);
			String RSA = RSAUtilOperate.RSAOperate(username + originIp, 0);
			loginls.setRSACheck(RSA);
			iSystemLogService.addLoginls(loginls);
		}
		return loginls;

	}

	public Employee loginShow(HttpServletRequest request, HttpSession session,
			Employee user) throws Exception {
		Loginls loginls = new Loginls();
		String originIp = request.getRemoteAddr();
		loginls.setUserName(user.getCode());
		String date = TimeUtilFormat.timeType(new Date(), 1);
		String nowLoginTime = TimeUtilFormat.timeType(new Date(), 0);
		user.setRSACheck(nowLoginTime);
		user.setLoginIp(originIp);
		String yxq = user.getYhyxq();
		String passcode = user.getPassCode();
		long day = TimeUtilFormat.timeLong(date, yxq);
		long pass = TimeUtilFormat.timeLong(date, passcode);
		user.setYhyxq(String.valueOf(day));
		user.setPassCode(String.valueOf(pass));

		List<Loginls> list = iSystemLogService.queryLoginErr(loginls);
		String context = queryLoginEr(list);
		// 获取上次登录成功的数据
		List<Loginls> loginl = iSystemLogService.queryLoginSuu(loginls);

		loginls = login_Log("0");
		loginls.setrTime(TimeUtilFormat.timeType(loginls.getRecordTime(), 0));
		loginls.setRSACheck(context);

		if (loginl != null && !loginl.isEmpty()) {
			loginl.get(0).setrTime(
					TimeUtilFormat.timeType(loginl.get(0).recordTime, 0));
			session.setAttribute("loginl", loginl.get(0));

			long time = TimeUtilFormat.timeLong(loginl.get(0).getrTime(), date);
			int longTime = Integer.parseInt(String.valueOf(time));
			List<Safety> safetyList = safetyService.searchSafety();
			int loginTime = 30;
			if (safetyList.size() > 0) {
				Safety safety = safetyList.get(0);
				loginTime = Integer.parseInt(safety.getTimelimit());
			}
			if (longTime >= loginTime) {// 长时间没登录
				String event = "该用户账号长时间未使用";
				String eventType = "查";
				String result = "0";
				String resultDepict = "该用户账号长时间未使用";
				String userName = request.getParameter("yhdh");

				SecurityLog securityLog = new SecurityLog();

				Security security = securityLog.security_log(userName, event,
						eventType, result, resultDepict);
				security.setResult(result);
				iSystemLogService.addSecurity(security);
			}
		} else {
			Loginls loginli = new Loginls();
			loginli.setrTime("首次登录");
			loginli.setOriginIp("首次登录");
			session.setAttribute("loginl", loginli);
		}
		sessSum += 1;
		session.setAttribute("loginls", loginls);
		return user;
	}

	public String queryLoginEr(List<Loginls> list) {
		String contents = new String();
		String rtime;
		contents = "";
		if (list != null && !list.isEmpty()) {
			for (int j = 0; j < list.size(); j++) {
				rtime = TimeUtilFormat.timeType(list.get(j).getRecordTime(), 0);
				contents = contents + "</br>---" + (j + 1) + "---"
						+ "</br>---IP" + list.get(j).getOriginIp()
						+ "---<br>时间:" + rtime + "</br>失败原因:"
						+ list.get(j).getResultDepict() + "</br>";
			}
		} else {
			contents = "</br>没有失败记录";
		}
		return contents;

	}
	/**
	 * 重定向到云帆系统登录页面
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	  public void alipayforward(String ip,HttpServletRequest req, HttpServletResponse resp) throws Exception {
		  //resp.sendRedirect("http://"+ip+"/compare/blue/login.html");
		  resp.sendRedirect("http://"+ip+"/compare/loginPki.html");  
	  }
	  /**
	   * 退出登录重定向到云帆系统首页
	   * @param session_id
	   * @param req
	   * @param resp
	   * @throws Exception
	   */
	  public void alipayforward(String ip,String session_id, HttpServletRequest req, HttpServletResponse resp) throws Exception {  
		    //resp.sendRedirect("http://"+ip+"/compare/blue/sy.html?SESSION_ID="+session_id);
		  resp.sendRedirect("http://"+ip+"/compare/redirIndex.do?SESSION_ID="+session_id); 
	  }  
	  /**
	   * MD5加密验证校验码
	   * @param bdjym
	   * @param jym
	   * @return
	   * @throws Exception
	   */
	  public int yfJym(String bdjym, String jym) throws Exception {
		  bdjym=EncryptionUtil.encodingMd5(bdjym);
		  System.out.println("本地校验码:"+bdjym+"-对比-云帆校验码:"+jym);
		  if(bdjym.equals(jym)){
			  return 0;
		  }else {
			  return 1;
		}
	  } 
	  
	  private String getLocalMac(InetAddress ia) throws SocketException {
			// TODO Auto-generated method stub
			//获取网卡，获取地址
			byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			StringBuffer sb = new StringBuffer("");
			for(int i=0; i<mac.length; i++) {
				if(i!=0) {
					sb.append("-");
				}
				//字节转换为整数
				int temp = mac[i]&0xff;
				String str = Integer.toHexString(temp);
				if(str.length()==1) {
					sb.append("0"+str);
				}else {
					sb.append(str);
				}
			}
			return sb.toString().toUpperCase();
		}
}

