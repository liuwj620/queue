package com.suntendy.queue.employee.domain;

import java.util.Arrays;

/*******************************************************************************
 * 描述: 员工信息 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-10-09 16:53:21 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class Employee {
	private String id;
	private String code;
	private String name;
	private String password;
	private String sex;
	private String queue;
	private String comments;
	private String department;
	private String userFlag;
	private String ts;
	private String editdate;
	private String status; //用户状态
	private String barId;
	private String loginIp;
	private String fmt_status;
	private String resetPwd;
	private String operate;
	private String levelid;
	private byte[] photo;
	private Object photos;
	private String file;
	private String role;
	private String deptCode;
	private String deptHall;
	private String idnumber;
	private String police;
	private String yhdh;
	private String dlip;
	private String dlsj;
	private String zt;
	private String yhyxq;
	private String kdlip;
	private String kdlsjd;
	private String zxbj;//在线标记
	private String moduleRdac;
	private String RSACheck;
	private String policeCode;
	private String passCode;
	private String WSIp;
	private String UUID;
	
	
	public String getWSIp() {
		return WSIp;
	}

	public void setWSIp(String wSIp) {
		WSIp = wSIp;
	}

	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	
	
	
	@Override
	public String toString() {
		return "Employee [id=" + id + ", code=" + code + ", name=" + name
				+ ", password=" + password + ", sex=" + sex + ", queue="
				+ queue + ", comments=" + comments + ", department="
				+ department + ", userFlag=" + userFlag + ", ts=" + ts
				+ ", editdate=" + editdate + ", status=" + status + ", barId="
				+ barId + ", loginIp=" + loginIp + ", fmt_status=" + fmt_status
				+ ", resetPwd=" + resetPwd + ", operate=" + operate
				+ ", levelid=" + levelid + ", photo=" + Arrays.toString(photo)
				+ ", photos=" + photos + ", file=" + file + ", role=" + role
				+ ", deptCode=" + deptCode + ", deptHall=" + deptHall
				+ ", idnumber=" + idnumber + ", police=" + police + ", yhdh="
				+ yhdh + ", dlip=" + dlip + ", dlsj=" + dlsj + ", zt=" + zt
				+ ", yhyxq=" + yhyxq + ", kdlip=" + kdlip + ", kdlsjd="
				+ kdlsjd + ", zxbj=" + zxbj + ", moduleRdac=" + moduleRdac
				+ ", RSACheck=" + RSACheck + ", policeCode=" + policeCode
				+ ", passCode=" + passCode + ", UUID=" + UUID + "]";
	}

	public String getPoliceCode() {
		return policeCode;
	}

	public void setPoliceCode(String policeCode) {
		this.policeCode = policeCode;
	}

	public String getModuleRdac() {
		return moduleRdac;
	}

	public void setModuleRdac(String moduleRdac) {
		this.moduleRdac = moduleRdac;
	}

	public String getRSACheck() {
		return RSACheck;
	}

	public void setRSACheck(String rSACheck) {
		RSACheck = rSACheck;
	}

	public String getZxbj() {
		return zxbj;
	}

	public void setZxbj(String zxbj) {
		this.zxbj = zxbj;
	}

	public String getYhyxq() {
		return yhyxq;
	}

	public void setYhyxq(String yhyxq) {
		this.yhyxq = yhyxq;
	}

	public String getKdlip() {
		return kdlip;
	}

	public void setKdlip(String kdlip) {
		this.kdlip = kdlip;
	}

	public String getKdlsjd() {
		return kdlsjd;
	}

	public void setKdlsjd(String kdlsjd) {
		this.kdlsjd = kdlsjd;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getYhdh() {
		return yhdh;
	}

	public void setYhdh(String yhdh) {
		this.yhdh = yhdh;
	}

	public String getDlip() {
		return dlip;
	}

	public void setDlip(String dlip) {
		this.dlip = dlip;
	}

	public String getDlsj() {
		return dlsj;
	}

	public void setDlsj(String dlsj) {
		this.dlsj = dlsj;
	}

	public String getPolice() {
		return police;
	}

	public void setPolice(String police) {
		this.police = police;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}
	
	public String getEditdate() {
		return editdate;
	}

	public void setEditdate(String editdate) {
		this.editdate = editdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBarId() {
		return barId;
	}

	public void setBarId(String barId) {
		this.barId = barId;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getFmt_status() {
		return fmt_status;
	}

	public void setFmt_status(String fmt_status) {
		this.fmt_status = fmt_status;
	}

	public String getResetPwd() {
		return resetPwd;
	}

	public void setResetPwd(String resetPwd) {
		this.resetPwd = resetPwd;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getLevelid() {
		return levelid;
	}

	public void setLevelid(String levelid) {
		this.levelid = levelid;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Object getPhotos() {
		return photos;
	}

	public void setPhotos(Object photos) {
		this.photos = photos;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptHall() {
		return deptHall;
	}
	
	public void setDeptHall(String deptHall) {
		this.deptHall = deptHall;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}