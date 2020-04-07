package com.suntendy.queue.employee.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.suntendy.queue.employee.dao.IEmployeeDao;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.domain.Role;
import com.suntendy.queue.employee.service.IEmployeeService;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.SearchException;
import com.suntendy.queue.util.exception.UpdateException;
import com.suntendy.queue.util.scriptsession.Publisher;
import com.suntendy.queue.util.scriptsession.event.UserLoginEvent;

/*******************************************************************************
 * 描述: 员工相关操作业务逻辑接口实现类 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-10-09 16:48:58 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class EmployeeServiceImpl implements IEmployeeService{
	private Publisher publisher;
	private IEmployeeDao employeeDao;
	
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public void setEmployeeDao(IEmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	public Employee getEmployeeByCode(String operNum) {
		Employee employee = new Employee();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		employee.setCode(operNum);
		employee.setDeptHall(deptHall);
		try {
			List<Employee> employeeList = employeeDao.searchEmployee(employee);
			
			if (!employeeList.isEmpty()) {
				employee = employeeList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return employee;
	}

	public void save(Employee employee) throws Exception {
		Employee epy = this.getEmployeeByCode(employee.getCode());
		if (StringUtils.isEmpty(epy.getId())) {
			employee.setId(employeeDao.save(employee));
			System.out.println("保存成功");
		}
		
		employeeDao.saveLoginStatus(employee);
		
		if (StringUtils.isEmpty(employee.getId())) {
			employee.setId(epy.getId());
		}
		employee.setPhoto(epy.getPhoto());
		employee.setWSIp(epy.getWSIp());
		employee.setComments(StringUtils.trimToEmpty(epy.getComments()));
		CacheManager.getInstance().addOfLoginCache(employee.getCode(), employee.getLoginIp());
		publisher.publish(new UserLoginEvent(employee));
	}
	
	public String savestaff(File file,Employee employee) throws Exception {
		try {
			if (file != null) {
				employee.setFile("true");
				employee.setEditdate("true");
				
				FileInputStream stream = new FileInputStream(file);
			    ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			    byte[] temp = new byte[1000];
			    int n;
			    while (-1 != (n = stream.read(temp)))
			        out.write(temp, 0, n);
			    stream.close();
			    out.close();
				employee.setPhoto(out.toByteArray());
			}
			String result = employeeDao.savestaff(employee);
			employeeDao.saveLoginStatus(employee);
			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Employee> getEmpList(Employee employee) {
		List<Employee> empList = null;
		try {
			empList = employeeDao.searchEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empList;
	}

	public int updateEmp(File file, Employee employee) {
		try {
			if (file != null) {
				employee.setFile("true");
				employee.setEditdate("true");
				
				FileInputStream stream = new FileInputStream(file);
			    ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			    byte[] temp = new byte[1000];
			    int n;
			    while (-1 != (n = stream.read(temp)))
			        out.write(temp, 0, n);
			    stream.close();
			    out.close();
				employee.setPhoto(out.toByteArray());
			}
			
			int result = employeeDao.updateEmp(employee);
			publisher.publish(new UserLoginEvent(employee));
			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UpdateException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int deleteEmp(String id) {
		try {
			return employeeDao.deleteEmp(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Employee> checkCode(Employee employee) {
		List<Employee> codeExistsList = null;
		try {
			codeExistsList = employeeDao.checkCode(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codeExistsList;
	}

	public String saveEmployee(Employee employee) throws Exception {
		return employeeDao.saveEmployee(employee);
	}

	@Override
	public List<Employee> getLoginStateByCode(Employee employee) throws SearchException {
		// TODO Auto-generated method stub
		return employeeDao.getLoginStateByCode(employee);
	}

	@Override
	public List<Employee> searchWithoutPhoto(Employee employee) throws Exception {
		
		return employeeDao.searchWithoutPhoto(employee);
	}

	@Override
	public List<Employee> searchEmployeeByIp(Employee employee)
			throws SearchException {
		return employeeDao.searchEmployeeByIp(employee);
	}

	@Override
	public List<Employee> check(Employee employee) throws Exception {
		// TODO Auto-generated method stub
		return employeeDao.check(employee);
	}

	@Override
	public int deleteSuoEmp(String name) throws Exception{
		// TODO Auto-generated method stub
		return employeeDao.deleteSuoEmp(name);
	}

	@Override
	public String saveRole(Role role) throws Exception {
		return employeeDao.saveRole(role);
	}

	@Override
	public List<Role> getRoleList(Role role) throws Exception {
		return employeeDao.getRoleList(role);
	}

	@Override
	public int deleteRole(String id) throws Exception {
		return employeeDao.deleteRole(id);
	}

	@Override
	public int updateRole(Role role) throws Exception {
		// TODO Auto-generated method stub
		return employeeDao.updateRole(role);
	}
}