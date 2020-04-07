package com.suntendy.queue.employee.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suntendy.queue.base.BaseDao;
import com.suntendy.queue.employee.dao.IEmployeeDao;
import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.employee.domain.Role;
import com.suntendy.queue.util.exception.SaveException;
import com.suntendy.queue.util.exception.SearchException;
import com.suntendy.queue.util.exception.UpdateException;

/*******************************************************************************
 * 描述: 员工相关操作业务数据库操作接口实现类 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-10-09 17:12:00 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class EmployeeDaoImpl extends BaseDao<Employee, String> implements IEmployeeDao {
	
	public List<Employee> searchEmployee(Employee employee) throws SearchException {
		try {
			String[] properties = {"id", "code", "loginIp", "name", "flag", "status", "password" ,"deptHall","police","moduleRdac","policeCode","password"};
			Object[] propertyValues = {employee.getId(), employee.getCode(), employee.getLoginIp(), 
					employee.getName(),employee.getUserFlag(),employee.getStatus(),employee.getPassword(),employee.getDeptHall(),employee.getPolice(),employee.getModuleRdac(),employee.getPoliceCode(),employee.getPassCode() };
			return this.findByMap(properties, propertyValues, "t.id", "ASC", BaseDao.SELECTBYMAP);
		} catch (Exception e) {
			throw new SearchException(e.getMessage());
		}
	}
	public List<Employee> searchEmployeeByIp(Employee employee) throws SearchException {
		try {
			String[] properties = {"loginIp" ,"deptHall"};
			Object[] propertyValues = {employee.getLoginIp(),employee.getDeptHall()};
			return this.findByMap(properties, propertyValues, "t.id", "ASC", "queryEmpByIp");
		} catch (Exception e) {
			throw new SearchException(e.getMessage());
		}
	}
	public List<Employee> getLoginStateByCode(Employee employee) throws SearchException {
		try {
			String[] properties = {"id", "code", "loginIp"};
			Object[] propertyValues = {employee.getId(), employee.getCode(), employee.getLoginIp()};
			return this.findByMap(properties, propertyValues, "", "", "getLoginStateByCode");
		} catch (Exception e) {
			throw new SearchException(e.getMessage());
		}
	}

	public String save(Employee employee) throws SaveException {
		try {
			return this.insert(employee, BaseDao.INSERT);
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}
	
	public String savestaff(Employee employee) throws SaveException {
		try {
			return this.insert(employee, "savestaff");
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}

	public void saveLoginStatus(Employee employee) throws SaveException {
		try {
			this.insert(employee, "insertLoginStatus");
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}

	public int updateEmp(Employee employee) throws UpdateException {
		String[] properties = { "id", "status","code","name","password","sex","police","yhyxq","kdlip","kdlsjd","comments","department","editdate","levelid","editdate","photo","department","zxbj","loginIp","RSACheck","moduleRdac","policeCode","passCode","WSIp"};
	    Object[] propertyValues = { employee.getId(),employee.getStatus(),employee.getCode(),employee.getName(),employee.getPassword(),
	    		employee.getSex(),employee.getPolice(),employee.getYhyxq(),employee.getKdlip(),employee.getKdlsjd(),employee.getComments(),employee.getDepartment(),employee.getEditdate(),
	    		employee.getLevelid(),employee.getEditdate(),employee.getPhoto(),employee.getDepartment(),employee.getZxbj(),employee.getLoginIp(),employee.getRSACheck(),employee.getModuleRdac(),employee.getPoliceCode(),employee.getPassCode(),employee.getWSIp()};
	    try {
			return this.updateByMap(employee.getId(), properties, propertyValues, BaseDao.UPDATEBYMAP);
		} catch (Exception e) {
			throw new UpdateException(e.getMessage());
		}
	}

	public int deleteEmp(String id) throws Exception {
		return this.deleteById(id,BaseDao.DELETEBYID);
	}

	public List<Employee> checkCode(Employee employee) throws SearchException{
		try {
			String[] properties = { "code","deptHall" };
			Object[] propertyValues = { employee.getCode(),employee.getDeptHall()};
			return this.findByMap(properties, propertyValues, "", "", "checkCode");
		} catch (Exception e) {
			throw new SearchException(e.getMessage());
		}
	}

	public String saveEmployee(Employee employee) throws Exception {
		return this.insert(employee, "insertEmp");
	}
	
	@Override
	public List<Employee> searchWithoutPhoto(Employee employee) throws Exception {
		String[] properties = { "flag","deptHall","code","name","status" };
		String[] propertyValues = { employee.getUserFlag(),employee.getDeptHall(),employee.getCode(),employee.getName(),employee.getStatus()};
		HashMap<String, String> parameterObject =new HashMap<String, String>();
		for (int i = 0; i < properties.length; i++) {
			parameterObject.put(properties[i],propertyValues[i]);
		}
		return getSqlMapClientTemplate().queryForList("Employee.searchWithoutPhoto", parameterObject);
	}
	@Override
	public List<Employee> check(Employee employee) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = { "idNumber","policeCode" };
		String[] propertyValues = {employee.getIdnumber(),employee.getPoliceCode() };
		return this.findByMap(properties, propertyValues, "", "", "check");
	}
	@Override
	public int deleteSuoEmp(String name) throws Exception {
		// TODO Auto-generated method stub
		return this.deleteById(name,"delcountLogin");
	}
	/**
	 * 保存用户信息
	 * @param employee
	 * @return
	 */
	public String saveRole(Role role) throws Exception{
		this.getSqlMapClientTemplate().insert("Employee.insertRole", role);
		return null;
	}
	@Override
	public List<Role> getRoleList(Role role) throws Exception {
		String[] properties = {"id","code" ,"depthall","deptcode"};
		String[] propertyValues = {role.getId(),role.getCode(),role.getDepthall(),role.getDeptcode()};
		HashMap<String, String> parameterObject =new HashMap<String, String>();
		for (int i = 0; i < properties.length; i++) {
			parameterObject.put(properties[i],propertyValues[i]);
		}
		return getSqlMapClientTemplate().queryForList("Employee.queryRole", parameterObject);
	}
	@Override
	public int deleteRole(String id) throws Exception {
		return getSqlMapClientTemplate().delete("Employee.deleteRole", id);
	}
	@Override
	public int updateRole(Role role) throws Exception {
		String[] properties = {"code" ,"content","levelid","depthall","deptcode"};
	    Object[] propertyValues = {role.getCode(),role.getContent(),role.getLevelid(),role.getDepthall(),role.getDeptcode()};
	    Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < properties.length; i++) {
			map.put(properties[i], propertyValues[i]);
		}
		map.put("id", role.getId());
		return getSqlMapClientTemplate().update("Employee.upRole", map);
	}
}