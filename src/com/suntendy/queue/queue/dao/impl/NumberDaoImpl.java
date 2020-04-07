package com.suntendy.queue.queue.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suntendy.queue.base.BaseDao;
import com.suntendy.queue.queue.dao.INumberDao;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.Wclh;
import com.suntendy.queue.util.DateUtils;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.exception.SaveException;
import com.suntendy.queue.util.exception.UpdateException;

public class NumberDaoImpl extends BaseDao<Number, String> implements INumberDao {

	public List<Number> searchCurrentDayNumber(Number number) throws Exception {
		String[] properties = { "deptCode", "deptHall", "operNum", "id","code" };
		String[] propertyValues = { number.getDeptCode(), number.getDeptHall(), number.getOperNum(), number.getId(), number.getCode()};
		return findByMap(properties, propertyValues, "", "", "searchCurrentDayNumber");
	}
	
	public List<Number> queryNumberBypdh(Number number) throws Exception {
		String[] properties = { "deptCode", "deptHall", "serialNum"};
		String[] propertyValues = { number.getDeptCode(), number.getDeptHall(), number.getSerialNum()};
		return findByMap(properties, propertyValues, "", "", "queryNumberBypdh");
	}

	
	public List<Number> getTotalNumberOfBusinessNumber(String typeNames) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String serialNumType = cacheManager.getSystemConfig("serialNumType");
		String[] properties = { "deptCode", "deptHall", "serialNumType", "typeNames" };
		String[] propertyValues = { deptCode, deptHall, serialNumType, typeNames };
		return findByMap(properties, propertyValues, "", "", "getTotalNumberOfBusinessNumber");
	}

	public String saveNumber(Number number) throws SaveException {
		try {
			return insert(number, BaseDao.INSERT);
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}
	
	public String saveExceptdata(Number number) throws SaveException {
		try {
			return insert(number, "saveExceptdata");
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}

	public int updateNumber(Number number) throws UpdateException {
		try {
			String[] properties = { "code", "value", "valueTime", "beginTime", "endTime", "status", "barId", "photo", "reason" ,"signature","quhaoPhoto","gpyPhoto",
					"gpyPhoto1","gpyPhoto2","gpyPhoto3","gpyPhoto4","gpyPhoto5","gpyPhoto6","gpyPhoto7","gpyPhoto8","gpyPhoto9","gpyPhoto10","gpyPhoto11","gpyPhoto12",
					"ZWBase64","outFlag","gpyPhoto13","passReason","businessType","queueCode","enterTime","barIp","gqsj" };
			Object[] propertyValues = { number.getOperNum(),
					number.getEvaluResult(), number.getEvaluTime(),
					number.getBeginTime(), number.getEndTime(),
					number.getStatus(),	number.getBarId(),
					number.getPhotoBase64(), number.getReason(), number.getSignature(),
					number.getQuhaoPhoto(),number.getGpyPhotoBase64(),number.getGpyPhoto1(),number.getGpyPhoto2(),number.getGpyPhoto3(),
					number.getGpyPhoto4(),number.getGpyPhoto5(),number.getGpyPhoto6(),number.getGpyPhoto7(),number.getGpyPhoto8(),number.getGpyPhoto9(),
					number.getGpyPhoto10(),number.getGpyPhoto11(),number.getGpyPhoto12(),number.getZWBase64(),number.getOutFlag(),number.getGpyPhoto13(),
					number.getPassReason(),number.getBusinessType(),number.getQueueCode(),number.getEnterTime(),number.getBarIp(),number.getGqsj()};
			return updateByMap(number.getId(), properties, propertyValues, BaseDao.UPDATEBYMAP);
		} catch (Exception e) {
			if ("2".equals(number.getStatus())) {
				number.setStatus("1");
			}
			throw new UpdateException(e.getMessage());
		}
	}

	public String pauseOrRecover(String deptCode, String deptHall, String operNum,
			String status, String reason) throws SaveException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptCode", deptCode);
			map.put("deptHall", deptHall);
			map.put("operNum", operNum);
			map.put("status", status);
			map.put("reason", reason);
			map.put("id", "");
			return (String)getSqlMapClientTemplate().insert("Number.insertPauseOrRecover", map);
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}

	@Override
	public void updatePauseOrRecover(String id) throws Exception {
		String[] properties = {};
		String[] propertyValues = {};
		this.updateByMap(id, properties, propertyValues, "updatePauseOrRecover");
	}

	public List<Number> getNotAgentByIdCardCount(Number number, String deptCode, String deptHall) throws Exception {
		String[] properties = { "IDNumber","IDNumberB", "deptCode", "deptHall" };
		String[] propertyValues = { number.getIDNumber(),number.getIDNumberB(), deptCode, deptHall };
		return findByMap(properties, propertyValues, "", "", "getNotAgentByIdCardCount");
	}

	@Override
	public List<Number> getNumberAllByClientIP(String clientIp) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = { "clientIp" };
		String[] propertyValues = { clientIp };
		return findByMap(properties, propertyValues, "", "", "getNumberAllByClientIP");
	}
	
	@Override
	public List<Number> getNumberAllByClientIPForHF(String clientIp,String sxh) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = { "clientIp","sxh" };
		String[] propertyValues = { clientIp,sxh };
		return findByMap(properties, propertyValues, "", "", "getNumberAllByClientIPForHF");
	}

	@Override
	public List<Number> getValueRecordAllById(Number number) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = {"id"};
		String[] propertyValues = {number.getId()};
		return findByMap(properties, propertyValues, "", "", "getValueRecordById");
	}
	@Override
	public List<Number> getValueRecordById(Number number) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = {"id", "code", "value", "valueTime", "beginTime", "endTime", "status", "barId","lsh"};
		String[] propertyValues = {number.getId(), number.getOperNum(),number.getEvaluResult(), number.getEvaluTime(),
				number.getBeginTime(), number.getEndTime(),number.getStatus(),	number.getBarId(),number.getLsh()};
		return findByMap(properties, propertyValues, "", "", "getValueRecordAllById");
	}
	@Override
	public List<Number> getValueRecordAllByIdForAY(Number number) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = {"id", "code", "value", "valueTime", "beginTime", "endTime", "status", "barId","lsh"};
		String[] propertyValues = {number.getId(), number.getOperNum(),number.getEvaluResult(), number.getEvaluTime(),
				number.getBeginTime(), number.getEndTime(),number.getStatus(),	number.getBarId(),number.getLsh()};
		return findByMap(properties, propertyValues, "", "", "getValueRecordAllByIdForAY");
	}
	//获取二级菜单编号
	public List<Number> getErJiCaiDanBianHao(Number number) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = {"id", "code", "value", "valueTime", "beginTime", "endTime", "status", "barId"};
		String[] propertyValues = {number.getId(), number.getOperNum(),number.getEvaluResult(), number.getEvaluTime(),
				number.getBeginTime(), number.getEndTime(),number.getStatus(),	number.getBarId()};
		return findByMap(properties, propertyValues, "", "", "getErJiCaiDanBianHao");
	}

	/**
	 * 根据身份证ID查询当天是否取号
	 */
	public List<Number> getValueRecardbyIdnumber(String idnumber,String lsh)throws Exception {
		String[] properties = {"idnumber","lsh"};
		String[] propertyValues = {idnumber,lsh};
		return this.findByMap(properties, propertyValues, "", "", "getValueRecardbyIdnumber");
	}


	public List<Number> getValueRecardLz(Number number) throws Exception {
		String[] properties = {"valueRecodeId","barId","enterTime","serialNum", "deptCode", "deptHall"};
		String[] propertyValues = {number.getId(),number.getBarId(),number.getEndTime(),number.getSerialNum(),number.getDeptCode(),number.getDeptHall()};
		return this.findByMap(properties, propertyValues, "", "", "getValueRecardLz");
	}

	public void delValueRecardLz(Number number) throws Exception {
		this.deleteById(number.getId(), "delValueRecardLz");
		
	}

	public void insertValueRecardLz(Number number) throws Exception {
		this.insert(number, "insertValueRecardLz");
	}
	
	public void insertQHrzdbz(Number number) throws Exception {
		this.insert(number, "insertQHrzdbz");
	}

	public int updatelsh(Number number) throws UpdateException {
		try {
			String[] properties = {"lsh"};
			Object[] propertyValues = {number.getLsh()};
			return updateByMap(number.getId(), properties, propertyValues, "updateserialno");
		} catch (Exception e) {
			throw new UpdateException(e.getMessage());
		}
	}
	
	public int updateOut1(Number number) throws UpdateException {
		try {
			String[] properties = {"out1"};
			Object[] propertyValues = {number.getOut1()};
			return updateByMap(number.getId(), properties, propertyValues, "updateOut1");
		} catch (Exception e) {
			throw new UpdateException(e.getMessage());
		}
	}
	
	
	public int updateRzdbz(Number number) throws UpdateException{
		try {
			String[] properties = {"rzdbz","serialNum","quhaoPhoto"};
			Object[] propertyValues = {number.getRzdbz(),number.getSerialNum(),number.getQuhaoPhoto()};
			return updateByMap(number.getId(), properties, propertyValues, "updateRzdbz");
		} catch (Exception e) {
			throw new UpdateException(e.getMessage());
		}
	}
	
	public int updateGQlsh(Number number) throws UpdateException {
		try {
			String[] properties = {"lsh"};
			Object[] propertyValues = {number.getLsh()};
			return updateByMap(number.getId(), properties, propertyValues, "updateGQserialno");
		} catch (Exception e) {
			throw new UpdateException(e.getMessage());
		}
	}

	@Override
	public List<Number> showWaitRs(Number number) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = {"deptCode","deptHall"};
		String[] propertyValues = {number.getDeptCode(),number.getDeptHall()};
		return this.findByMap(properties, propertyValues, "", "", "showWaitRs");
	}
	
	@Override
	public List<Number> showyjrs(Number number) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = {"deptCode","deptHall"};
		String[] propertyValues = {number.getDeptCode(),number.getDeptHall()};
		return this.findByMap(properties, propertyValues, "", "", "showyjrs");
	}

	public List<Number> getBarIdBySerialNum(Number number) throws Exception {
		String[] properties = {"code","serialNum","enterTime"};
		String[] propertyValues = {number.getCode(),number.getSerialNum(),number.getEnterTime()};
		return this.findByMap(properties, propertyValues, "", "", "getBarIdBySerialNum");
	}


	@Override
	public void insertValueRecordGq(Number number) throws Exception {
		this.insert(number, "insertValueRecordGq");
	}


	@Override
	public List<Number> getValueRecordGq() throws Exception {
		String[] properties = {};
		String[] propertyValues = {};
		return this.findByMap(properties, propertyValues, "", "", "getValueRecordGq");
	}


	@Override
	public int updateValueRecordGqStatus(String id,String blridnumber) throws Exception {
		String[] properties = {"blridnumber"};
		String[] propertyValues = {blridnumber};
		return this.updateByMap(id, properties, propertyValues, "updateValueRecordGqStatus");
	}


	@Override
	public List<Number> query6he1Callout(String serialNum) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String calloutSQL = cacheManager.getSystemConfig("calloutSQL");
		String[] properties = {"serialNum","calloutSQL"};
		String[] propertyValues = {serialNum,calloutSQL};
		return this.findByMap(properties, propertyValues, "", "", "calloutSQL");
	}


	@Override
	public List<Number> query6he1Valuerecord(String serialNum) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String valuerecordSQL = cacheManager.getSystemConfig("valuerecordSQL");
		String[] properties = {"serialNum","valuerecordSQL"};
		String[] propertyValues = {serialNum,valuerecordSQL};
		return this.findByMap(properties, propertyValues, "", "", "valuerecordSQL");
	}


	@Override
	public int updateValuerecordStatus(String num, String serialNum)
			throws Exception {
		int res=0;
		try {
			Number number =new Number();
			number.setStatus(num);
			number.setSerialNum(serialNum);
			this.getSqlMapClientTemplate().update("Number.updateValuerecordStatus", number);
		} catch (RuntimeException e) {
			e.printStackTrace();
			res=1;
		}
		return res;
	}


	public void insertValuerecordRZ(Number number) throws Exception {
		this.insert(number, "insertValuerecordRZ");
	}

	public void insertValuerecordLZRZ(Number number) throws Exception {
		this.insert(number, "insertValuerecordLZRZ");
	}

	@Override
	public List<Number> getvaluerecordRZ(Number number,String tjms, String ksrq, String jsrq,String IDNumber) throws Exception {
		if (tjms.equals("0")) {
			if (ksrq != "" && jsrq != "") {
				ksrq = "to_date('" + ksrq + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
				jsrq = "to_date('" + jsrq + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
			}
		} else if (tjms.equals("1")) {
			String currentTime = DateUtils.dateToString("yyyy-MM-dd"); // 当天时间
			String currentTime1 = "'" + currentTime + " 00:00:00" + "'";
			String currentTime2 = "'" + currentTime + " 23:59:59" + "'";
			ksrq = "to_date(" + currentTime1 + ",'yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(" + currentTime2 + ",'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("2")) {
			ksrq = "to_date(to_char(sysdate,'YYYY-MM')||'01 00:00:00','YYYY-MM-DD hh24:mi:ss')";
			jsrq = "to_date(to_char(last_day(sysdate),'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("3")) {
			ksrq = "to_date(to_char(trunc(sysdate, 'Q'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(TRUNC(SYSDATE,'Q'),+3)-1,'YYYY-MM-DD')||' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("4")) {
			ksrq = "to_date(to_char(trunc(sysdate,'yyyy'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(trunc(sysdate,'yyyy'),12)-1,'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		
		String[] properties = {"deptCode","deptHall","barId","serialNum","ksrq", "jsrq","IDNumber"};
		String[] propertyValues = {number.getDeptCode(),number.getDeptHall(),number.getBarId(),number.getSerialNum(),ksrq, jsrq,number.getIDNumber()};
		return this.findByMap(properties, propertyValues, "", "", "getvaluerecordRZ");
	}

	public List<Number> queryAllGqBybarId(Number number) throws Exception {
		String[] properties = {"id","barId","serialNum","deptHall","deptCode"};
		String[] propertyValues = {number.getId(),number.getBarId(),number.getSerialNum(),number.getDeptHall(),number.getDeptCode()};
		return this.findByMap(properties, propertyValues, "", "", "queryAllGqBybarId");
	}

	public List<Number> gqInfoList(String code,String tjms, String ksrq, String jsrq)
			throws Exception {
		if (tjms.equals("0")) {
			if (ksrq != "" && jsrq != "") {
				ksrq = "to_date('" + ksrq + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
				jsrq = "to_date('" + jsrq + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
			}
		} else if (tjms.equals("1")) {
			String currentTime = DateUtils.dateToString("yyyy-MM-dd"); // 当天时间
			String currentTime1 = "'" + currentTime + " 00:00:00" + "'";
			String currentTime2 = "'" + currentTime + " 23:59:59" + "'";
			ksrq = "to_date(" + currentTime1 + ",'yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(" + currentTime2 + ",'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("2")) {
			ksrq = "to_date(to_char(sysdate,'YYYY-MM')||'01 00:00:00','YYYY-MM-DD hh24:mi:ss')";
			jsrq = "to_date(to_char(last_day(sysdate),'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("3")) {
			ksrq = "to_date(to_char(trunc(sysdate, 'Q'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(TRUNC(SYSDATE,'Q'),+3)-1,'YYYY-MM-DD')||' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("4")) {
			ksrq = "to_date(to_char(trunc(sysdate,'yyyy'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(trunc(sysdate,'yyyy'),12)-1,'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		String[] properties = {"code","ksrq", "jsrq"};
		String[] propertyValues = {code,ksrq, jsrq};
		return this.findByMap(properties, propertyValues, "", "", "gqInfoList");
	}

	public List<Number> gqInfoDtail(String id) throws Exception {
		String[] properties = {"id"};
		String[] propertyValues = {id};
		return this.findByMap(properties, propertyValues, "", "", "gqInfoDtail");
	}


	@Override
	public int updateValuerecordGqPhoto(Number number) throws Exception {
		String[] properties = {"gpyPhoto13"};
		Object[] propertyValues = {number.getGpyPhoto13()};
		return updateByMap(number.getId(), properties, propertyValues, "updateValuerecordGqPhoto");
	}
	
	public List<Number> queryAllSfz(String tjms, String ksrq, String jsrq,String rNumber,String barId,String operNum,String IDNumber,String deptcode,String depthall)
	throws Exception {
		if (tjms.equals("0")) {
			if (ksrq != "" && jsrq != "") {
				ksrq = "to_date('" + ksrq + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
				jsrq = "to_date('" + jsrq + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
			}
		} else if (tjms.equals("1")) {
			String currentTime = DateUtils.dateToString("yyyy-MM-dd"); // 当天时间
			String currentTime1 = "'" + currentTime + " 00:00:00" + "'";
			String currentTime2 = "'" + currentTime + " 23:59:59" + "'";
			ksrq = "to_date(" + currentTime1 + ",'yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(" + currentTime2 + ",'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("2")) {
			ksrq = "to_date(to_char(sysdate,'YYYY-MM')||'01 00:00:00','YYYY-MM-DD hh24:mi:ss')";
			jsrq = "to_date(to_char(last_day(sysdate),'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("3")) {
			ksrq = "to_date(to_char(trunc(sysdate, 'Q'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(TRUNC(SYSDATE,'Q'),+3)-1,'YYYY-MM-DD')||' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("4")) {
			ksrq = "to_date(to_char(trunc(sysdate,'yyyy'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(trunc(sysdate,'yyyy'),12)-1,'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		String[] properties = {"ksrq","jsrq","rNumber","barId","operNum","IDNumber","deptcode","depthall"};
		String[] propertyValues = {ksrq,jsrq,rNumber,barId,operNum,IDNumber,deptcode,depthall};
		return this.findByMap(properties, propertyValues, "", "", "queryAllSfzPhoto");
	}


	public List<Number> countWJHshuliang(String id,String waitingarea,String businessType,String queueCode,String deptCode,String deptHall) throws Exception {
		String[] properties = {"id","waitingarea","businessType","queueCode","deptCode","deptHall"};
		String[] propertyValues = {id,waitingarea,businessType,queueCode,deptCode,deptHall};
		return this.findByMap(properties, propertyValues, "", "", "countWJHshuliang");
	}


	public List<Number> detailSfz(String id) throws Exception {
		String[] properties = {"id"};
		String[] propertyValues = {id};
		return this.findByMap(properties, propertyValues, "", "", "detailSfz");
	}


	public List<Number> getCodeByRz(Number number) throws Exception {
		String[] properties = { "deptCode", "deptHall", "operNum"};
		String[] propertyValues = { number.getDeptCode(), number.getDeptHall(), number.getOperNum() };
		return findByMap(properties, propertyValues, "", "", "getCodeByRz");
	}



	public List<Number> getCardAndLshByAll(Number number) throws Exception {
			String[] properties = { "sfzmhm", "lsh"};
			String[] propertyValues = { number.getIDNumber(),number.getLsh() };
			return findByMap(properties, propertyValues, "", "", "getCardAndLshByAll");
	}


	public List<Number> getvaluerecordByClientno(Number number)
			throws Exception {
		String[] properties = {"clientno"};
		String[] propertyValues = {number.getClientno()};
		return findByMap(properties, propertyValues, "", "", "getValueRecordByClientno");
	}


	public String savaGpyPhoto(Number number) throws Exception {
		try {
			return insert(number, "savaGpyPhoto");
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}


	@Override
	public List<Number> countIdNumber(String tjms, String ksrq, String jsrq,Number number,String deptCode,String deptHall) throws Exception {
		
		if (tjms.equals("0")) {
			if (ksrq != "" && jsrq != "") {
//				ksrq = "to_date('" + ksrq + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
//				jsrq = "to_date('" + jsrq + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
				ksrq = "to_date('" + ksrq +":00','yyyy-mm-dd hh24:mi:ss')";
				jsrq = "to_date('" + jsrq +":59','yyyy-mm-dd hh24:mi:ss')";
			}
		} else if (tjms.equals("1")) {
			String currentTime = DateUtils.dateToString("yyyy-MM-dd"); // 当天时间
			String currentTime1 = "'" + currentTime + " 00:00:00" + "'";
			String currentTime2 = "'" + currentTime + " 23:59:59" + "'";
			ksrq = "to_date(" + currentTime1 + ",'yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(" + currentTime2 + ",'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("2")) {
			ksrq = "to_date(to_char(sysdate,'YYYY-MM')||'01 00:00:00','YYYY-MM-DD hh24:mi:ss')";
			jsrq = "to_date(to_char(last_day(sysdate),'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("3")) {
			ksrq = "to_date(to_char(trunc(sysdate, 'Q'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(TRUNC(SYSDATE,'Q'),+3)-1,'YYYY-MM-DD')||' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("4")) {
			ksrq = "to_date(to_char(trunc(sysdate,'yyyy'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(trunc(sysdate,'yyyy'),12)-1,'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		String[] properties = {"ksrq","jsrq","IDNumber","deptCode","deptHall"};
		System.out.println(number.getIDNumber());
		String[] propertyValues = {ksrq,jsrq,number.getIDNumber(),deptCode,deptHall};
		if ("1".equals(number.getTjry())) {
			return findByMap(properties, propertyValues, "", "", "countIdNumberB");
		}else{
			return findByMap(properties, propertyValues, "", "", "countIdNumber");
		}
	}

	public String addZhiWen(Number number) throws Exception {
		try {
			return insert(number, "addZhiWen");
		} catch (Exception e) {
			throw new SaveException(e.getMessage());
		}
	}

	public List<Number> getZhiWenByIdNumber(Number number) throws Exception {
		String[] properties = {"IDNumber"};
		String[] propertyValues = {number.getIDNumber()};
		return findByMap(properties, propertyValues, "", "", "getZhiWenByIdNumber");
	}

	public int updateZhiWen(Number number) throws Exception {
		String[] properties = {"IDNumber","ZWBase64"};
		Object[] propertyValues = {number.getIDNumber(),number.getZWBase64()};
		return updateByMap(number.getIDNumber(), properties, propertyValues, "updateZhiWen");
	}

	public int delZhiWen(Number number) throws Exception {
		return this.deleteById(number.getIDNumber(), "delZhiWen");
	}


	@Override
	public List<Number> queryIdnumberDifference(String tjms, String ksrq, String jsrq,Number number) throws Exception {
		if (tjms.equals("0")) {
			if (ksrq != "" && jsrq != "") {
				ksrq = "to_date('" + ksrq + " 00:00:00','yyyy-mm-dd hh24:mi:ss')";
				jsrq = "to_date('" + jsrq + " 23:59:59','yyyy-mm-dd hh24:mi:ss')";
			}
		} else if (tjms.equals("1")) {
			String currentTime = DateUtils.dateToString("yyyy-MM-dd"); // 当天时间
			String currentTime1 = "'" + currentTime + " 00:00:00" + "'";
			String currentTime2 = "'" + currentTime + " 23:59:59" + "'";
			ksrq = "to_date(" + currentTime1 + ",'yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(" + currentTime2 + ",'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("2")) {
			ksrq = "to_date(to_char(sysdate,'YYYY-MM')||'01 00:00:00','YYYY-MM-DD hh24:mi:ss')";
			jsrq = "to_date(to_char(last_day(sysdate),'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("3")) {
			ksrq = "to_date(to_char(trunc(sysdate, 'Q'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(TRUNC(SYSDATE,'Q'),+3)-1,'YYYY-MM-DD')||' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')";
		} else if (tjms.equals("4")) {
			ksrq = "to_date(to_char(trunc(sysdate,'yyyy'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
			jsrq = "to_date(to_char(add_months(trunc(sysdate,'yyyy'),12)-1,'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
		}
		
		String[] properties = {"ksrq","jsrq","IDNumber","IDNumberB","lsh"};
		String[] propertyValues = {ksrq,jsrq,number.getIDNumber(),number.getIDNumberB(),number.getLsh()};
		return findByMap(properties, propertyValues, "", "", "idnumberDifference");
	}


	@Override
	public List<Number> checkJDCStatus(Number number) throws Exception {
		String[] properties = {"hphm","hpzl"};
		String[] propertyValues = {number.getHphm(),number.getHpzl()};
		return findByMap(properties, propertyValues, "", "", "checkJDCStatus");
	}


	@Override
	public List<Number> checkJSRStatus(Number number) throws Exception {
		String[] properties = {"sfzmhm"};
		String[] propertyValues = {number.getSfzmhm()};
		return findByMap(properties, propertyValues, "", "", "checkJSRStatus");
	}


	@Override
	public List<Number> yzcfqh(String idnumber) throws Exception {
		String[] properties = {"idnumber"};
		String[] propertyValues = {idnumber};
		return findByMap(properties, propertyValues, "", "", "yzcfqh");
	}


	@Override
	public List<Number> selectValuecord(Number number) throws Exception {
		// TODO Auto-generated method stub
		String[] properties = {"barid"};
		String[] propertyValues = {number.getBarId()};
		return findByMap(properties, propertyValues, "", "", "selectValuecord");
	}
	public String findByValuerecord(Number number) throws Exception {
		String[] properties = {"ywid","deptCode","deptHall","beginTime","valuetime","endTime","endtime","rztime"};
		String[] propertyValues = {number.getId(),number.getDeptCode(),number.getDeptHall(),number.getBeginTime(),number.getValuetime(),number.getEndTime(),number.getEndtime(),number.getRztime()};
		HashMap<String,String> parameterMap = new HashMap<String, String>();
		for (int i = 0; i < properties.length; i++) {
			parameterMap.put(properties[i], propertyValues[i]);
		}
		return (String) this.getSqlMapClientTemplate().queryForObject("Number.findByValuerecord", parameterMap);
	}
	
	public String findByWait(String id,String code,String deptCode,String deptHall)throws Exception{
		String[] properties = {"id","code","deptCode","deptHall"};
		String[] propertyValues = {id,code,deptCode,deptHall};
		HashMap<String,String> parameterMap = new HashMap<String, String>();
		for (int i = 0; i < properties.length; i++) {
			parameterMap.put(properties[i], propertyValues[i]);
		}
		return (String) this.getSqlMapClientTemplate().queryForObject("Number.queuewjhs",parameterMap);
	}
	public String queueWaitTime(String code,String deptCode,String deptHall)throws Exception{
		HashMap<String,String> parameterMap = new HashMap<String, String>();
		parameterMap.put("code", code);
		parameterMap.put("deptCode", deptCode);
		parameterMap.put("deptHall", deptHall);
		return (String) this.getSqlMapClientTemplate().queryForObject("Number.queuewaittime",parameterMap);
	}


	@Override
	public List<Number> dayNumber(String deptCode, String deptHall) throws Exception {
		String[] properties = { "deptCode", "deptHall"};
		String[] propertyValues = { deptCode, deptHall};
		return findByMap(properties, propertyValues, "", "", "dayNumber");
	}


	@Override
	public String showyisj(String deptCode, String deptHall)
			throws Exception {
		HashMap<String,String> parameterMap = new HashMap<String, String>();
		parameterMap.put("deptCode", deptCode);
		parameterMap.put("deptHall", deptHall);
		return  (String) this.getSqlMapClientTemplate().queryForObject("Number.showYjblsj",parameterMap);
	}


	@Override
	public void guaQiUp(String clientno,String id,String deptCode,String deptHall) throws Exception {
		try {
			String[] properties = {"clientno","id","deptCode","deptHall"};
			Object[] propertyValues = {clientno,id,deptCode,deptHall};
			updateByMap(id, properties, propertyValues, "guaQiUpdate");
		} catch (Exception e) {
			throw new UpdateException(e.getMessage());
		}
	}


	@Override
	public List<Wclh> wblh(String idnum) throws Exception {
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("idnum", idnum);
			return  this.getSqlMapClientTemplate().queryForList("Number.wclhao",map);
	}


	@Override
	public String selectYWLbyWindowId(String windowId,String deptCode,String deptHall) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptCode", deptCode);
		map.put("deptHall", deptHall);
		map.put("windowId", windowId);
		return (String) this.getSqlMapClientTemplate().queryForObject("Number.selectYWLbyWindowId", map);
	}


	@Override
	public List<Number> queryywlxAndddrs(Number countnum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptCode", countnum.getDeptCode());
		map.put("deptHall", countnum.getDeptHall());
		return this.getSqlMapClientTemplate().queryForList("Number.queryywlxAndddrs", map);
	}

	@Override
	public List<Number> querydllxAndddrs(Number countnum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptCode", countnum.getDeptCode());
		map.put("deptHall", countnum.getDeptHall());
		return this.getSqlMapClientTemplate().queryForList("Number.querydllxAndddrs", map);
	}

	@Override
	public List<Number> queryAllForImageById(Number number) throws Exception {
		String[] properties = { "clientno","deptCode","deptHall"};
		String[] propertyValues = { number.getClientno(),number.getDeptCode(),number.getDeptHall()};
		return findByMap(properties, propertyValues, "", "", "queryAllForImageById");
	}



	@Override
	public int upStatusAll(Number number) throws Exception {
//		String[] properties = {};
//		String[] propertyValues = {};
		return this.update(number, BaseDao.UPDATE);
	
	}


	@Override
	public List<Number> getNameBySXH(Number number) throws Exception {
		String[] properties = { "clientno","deptCode","deptHall"};
		String[] propertyValues = { number.getClientno(),number.getDeptCode(),number.getDeptHall()};
		return findByMap(properties, propertyValues, "", "", "getNameBySXH");
	}



	@Override
	public void saveSxh(Number number) throws Exception {
		this.insert(number, "saveSxh");
	}


	@Override
	public List<Number> getBaripByBarid(Number number) throws Exception {
		String[] properties = { "barId","deptCode","deptHall"};
		String[] propertyValues = { number.getBarId(),number.getDeptCode(),number.getDeptHall()};
		return findByMap(properties, propertyValues, "", "", "getbaripBybarid");
	}


	@Override
	public List<Number> countByIdnumber(String idnumber, String deptCode,
			String deptHall) throws Exception {
		String[] properties = {"IDNumber","deptCode","deptHall"};
		String[] propertyValues = {idnumber,deptCode,deptHall};
		return findByMap(properties, propertyValues, "", "", "countByIdnumber");
	}


	@Override
	public List<Number> queryRzdbz(String idnumber)
			throws Exception {
		String[] properties = {"IDNumber"};
		String[] propertyValues = {idnumber,};
			return findByMap(properties, propertyValues, "", "", "queryRzdbz");
	}

	@Override
	public String searchwindowidByip(String loginIP) throws Exception {
		return (String)this.getSqlMapClientTemplate().queryForObject("Number.getwindowidByip",loginIP);
	}


	@Override
	public Number queryCarNumber(String SerialNum,String deptCode,String deptHall) throws Exception {
		String[] properties = {"SerialNum","deptCode","deptHall"};
		String[] propertyValues = {SerialNum,deptCode,deptHall};
		return  (Number) findByObject(properties, propertyValues, "", "", "queryCarNumber");
	}


	@Override
	public void saveCarNumber(String dataCY) throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		String[] data = dataCY.split("@");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptCode", deptCode);
		map.put("deptHall", deptHall);
		map.put("carType", data[0]);
		map.put("carNumber", data[1]);
		map.put("serialNum", data[2]);
		this.getSqlMapClientTemplate().update("saveCarNumber", map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Number> getNumberViaId(String id) throws Exception {
		String[] properties = {"id"};
		String[] propertyValues = {id};
		return (List<Number>) findByMap(properties, propertyValues, "", "", "getNumberViaId");
		
	}

	@Override
	public void insertBLXX(Number number) throws Exception {
		 this.insert(number, "insertBLXX");
	}


	@Override
	public List<Number> queryBLXX(String deptCode, String deptHall)
			throws Exception {
		String[] properties = { "deptCode", "deptHall" };
		String[] propertyValues = { deptCode, deptHall};
		return findByMap(properties, propertyValues, "", "", "queryBLXX");
	}


	@Override
	public void deleteBLXX(Number number) throws Exception {
		this.deleteById(number.getLrrq(), "deleteBLXX");
	}


	@Override
	public void updateWanJie(Number number) throws Exception {
		 this.getSqlMapClientTemplate().update("Number.updateWanJie", number);
	}

	public List<Number> getrzdbz(String tjms, String ckip, String sfzxm, String ksrq, String jsrq, Number number)
	        throws Exception
	    {
	        if(tjms.equals("0"))
	        {
	            if(ksrq != "" && jsrq != "")
	            {
	                ksrq = (new StringBuilder("to_date('")).append(ksrq).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')").toString();
	                jsrq = (new StringBuilder("to_date('")).append(jsrq).append(" 23:59:59','yyyy-mm-dd hh24:mi:ss')").toString();
	            }
	        } else
	        if(tjms.equals("1"))
	        {
	            String currentTime = DateUtils.dateToString("yyyy-MM-dd");
	            String currentTime1 = (new StringBuilder("'")).append(currentTime).append(" 00:00:00").append("'").toString();
	            String currentTime2 = (new StringBuilder("'")).append(currentTime).append(" 23:59:59").append("'").toString();
	            ksrq = (new StringBuilder("to_date(")).append(currentTime1).append(",'yyyy-mm-dd hh24:mi:ss')").toString();
	            jsrq = (new StringBuilder("to_date(")).append(currentTime2).append(",'yyyy-mm-dd hh24:mi:ss')").toString();
	        } else
	        if(tjms.equals("2"))
	        {
	            ksrq = "to_date(to_char(sysdate,'YYYY-MM')||'01 00:00:00','YYYY-MM-DD hh24:mi:ss')";
	            jsrq = "to_date(to_char(last_day(sysdate),'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
	        } else
	        if(tjms.equals("3"))
	        {
	            ksrq = "to_date(to_char(trunc(sysdate, 'Q'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
	            jsrq = "to_date(to_char(add_months(TRUNC(SYSDATE,'Q'),+3)-1,'YYYY-MM-DD')||' 23:59:59', 'yyyy-mm-dd hh24:mi:ss')";
	        } else
	        if(tjms.equals("4"))
	        {
	            ksrq = "to_date(to_char(trunc(sysdate,'yyyy'),'yyyy-mm-dd')||' 00:00:00','yyyy-mm-dd hh24:mi:ss')";
	            jsrq = "to_date(to_char(add_months(trunc(sysdate,'yyyy'),12)-1,'yyyy-mm-dd')||' 23:59:59','yyyy-mm-dd hh24:mi:ss')";
	        }
	        String properties[] = {
	            "ckip", "sfzxm", "ksrq", "jsrq", "sfzid","SerialNum"
	        };
	        String propertyValues[] = {
	            ckip, sfzxm, ksrq, jsrq, number.getIDNumber(),number.getSerialNum()
	        };
	        return findByMap(properties, propertyValues, "", "", "getrzdbz");
	    }
}