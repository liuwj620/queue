package com.suntendy.queue.queue.dao;

import java.util.List;

import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.Wclh;
import com.suntendy.queue.util.exception.SaveException;
import com.suntendy.queue.util.exception.UpdateException;

public interface INumberDao {

	/**
	 * 获取当天号码
	 * @return 号码信息
	 * @throws Exception
	 */
	public List<Number> searchCurrentDayNumber(Number number) throws Exception;
	
	public List<Number> getTotalNumberOfBusinessNumber(String typeNames) throws Exception;
	
	public List<Number> queryNumberBypdh(Number number) throws Exception;
	
	/**
	 * 保存新号
	 * @param number
	 * @return id
	 * @throws SaveException
	 */
	public String saveNumber(Number number) throws SaveException;
	
	/**
	 * 保存异常数据
	 * @param number
	 * @return id
	 * @throws SaveException
	 */
	public String saveExceptdata(Number number) throws SaveException;
	
	/**
	 * 更新号码信息
	 * @param number
	 * @return 修改数据影响的条数
	 * @throws UpdateException
	 */
	public int updateNumber(Number number) throws UpdateException;
	
	/**
	 * 保存暂停/恢复信息
	 * @param deptCode 所属部门
	 * @param deptHall 大厅号
	 * @param operNum 操作员编号
	 * @param status 状态 1暂停 2恢复
	 * @param reason 暂停原因
	 * @return
	 */
	public String pauseOrRecover(String deptCode, String deptHall, String operNum,
			String status, String reason) throws SaveException;

	public void updatePauseOrRecover(String id) throws Exception;
	
	/**
	 * 根据身份证获取非代理人当月取号数量
	 */
	public List<Number> getNotAgentByIdCardCount(Number number,String deptCode, String deptHall) throws Exception;
	/**
	 * 根据窗口ip获取窗口当前办理业务
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public List<Number> getNumberAllByClientIP(String clientIp) throws Exception;
	
	public List<Number> getNumberAllByClientIPForHF(String clientIp,String sxh) throws Exception;
	/**
	 * 根据id获取取号人员所有信息
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecordAllById(Number number) throws Exception;
	/**
	 * 根据id所有信息
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecordById(Number number) throws Exception;
	
	
	
	
	public List<Number> getValueRecordAllByIdForAY(Number number) throws Exception;
	
	/**
	 * 获取二级菜单编号
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public List<Number> getErJiCaiDanBianHao(Number number) throws Exception;
	
	/**
	 * 根据身份证ID查询当天是否取号
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecardbyIdnumber(String idnumber,String lsh) throws Exception;
	
	/**
	 * 查询流转号码
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecardLz(Number number) throws Exception;
	/**
	 * 添加流转号码
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public void insertValueRecardLz(Number number) throws Exception;
	/**
	 * 删除流转号码
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public void delValueRecardLz(Number number) throws Exception;
	
	/**
	 * 更新流水号
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public int updatelsh(Number number) throws UpdateException;
	
	/**
	 * 更新out1
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public int updateOut1(Number number) throws UpdateException;
	/**
	 * 更新人证对比值
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public int updateRzdbz(Number number) throws UpdateException;
	
	
	/**
	 * 更新挂起流水号
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public int updateGQlsh(Number number) throws UpdateException;
	/**
	 * 查询等待人数
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public List<Number> showWaitRs(Number number) throws Exception;
	
	public List<Number> showyjrs(Number number) throws Exception;
	
	/**
	 * 根据号码查询窗口
	 * @param number
	 * @return
	 */
	public List<Number> getBarIdBySerialNum(Number number) throws Exception;
	
	/**
	 * 根据号码、窗口号查询日志
	 * @param number
	 * @return
	 */
	public List<Number> getvaluerecordRZ(Number number,String tjms, String ksrq, String jsrq,String IDNumber) throws Exception;
	
	/**
	 * 添加挂起记录
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public void insertValueRecordGq(Number number) throws Exception;
	
	/**
	 * 根据当天挂起记录
	 * @param serialNum
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecordGq() throws Exception;
	/**
	 * 跟新挂起信息状态及办理人身份证id
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public int updateValueRecordGqStatus(String id,String blridnumber) throws Exception;
	/**
	 * 根据号码查询6合1数据库最新记录
	 * @param operNum
	 * @return
	 * @throws Exception
	 */
	public List<Number> query6he1Callout(String serialNum) throws Exception;
	/**
	 * 根据号码查询6合1数据库最新记录
	 * @param operNum
	 * @return
	 * @throws Exception
	 */
	public List<Number> query6he1Valuerecord(String serialNum) throws Exception;
	/**
	 * 根据号码更改本地valuerecord状态
	 * @param num
	 * @param serialNum
	 * @return
	 * @throws Exception
	 */
	public int updateValuerecordStatus(String num,String serialNum) throws Exception;
	
	/**
	 * 添加valuerecord日志
	 * @param number
	 * @throws Exception
	 */
	public void insertValuerecordRZ(Number number) throws Exception;
	
	/**
	 * 添加valuerecord日志
	 * @param number
	 * @throws Exception
	 */
	public void insertValuerecordLZRZ(Number number) throws Exception;
	
	/**
	 * 根据窗口获取当天挂起信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> queryAllGqBybarId(Number number) throws Exception;
	
	/**
	 * 挂起信息列表
	 * @param tjms
	 * @param ksrq
	 * @param jsrq
	 * @return
	 * @throws Exception
	 */
	public List<Number> gqInfoList(String code,String tjms, String ksrq, String jsrq) throws Exception;
	/**
	 * 挂起信息详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Number> gqInfoDtail(String id) throws Exception;
	/**
	 * 更新挂起库评价照片
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public int updateValuerecordGqPhoto(Number number) throws Exception;
	
	/**
	 * 根据时间查询信息
	 * @param tjms
	 * @param ksrq
	 * @param jsrq
	 * @return
	 * @throws Exception
	 */
	public List<Number> queryAllSfz(String tjms, String ksrq, String jsrq,String rNumber,String barId,String operNum,String IDNumber,String deptcode,String depthall) throws Exception;
	
	/**
	 * 根据条件查询照片信息
	 * @param tjms
	 * @param ksrq
	 * @param jsrq
	 * @return
	 * @throws Exception
	 */
	public List<Number> detailSfz(String id) throws Exception;
	
	/**
	 * 统计区域未叫号数量
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> countWJHshuliang(String id,String waitingarea,String businessType,String queueCode,String deptCode,String deptHall) throws Exception;
	
	/**
	 * 根据条件查询日志信息
	 * @param tjms
	 * @param ksrq
	 * @param jsrq
	 * @return
	 * @throws Exception
	 */
	public List<Number> getCodeByRz(Number number) throws Exception;
	/**
	 * 根据身份证号或流水号获取照片
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getCardAndLshByAll(Number number) throws Exception;

	/**
	 * 根据顺序号查询当天信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getvaluerecordByClientno(Number number) throws Exception;
	
	/**
	 * 保存高拍仪照片
	 */
	public String savaGpyPhoto(Number number) throws Exception;
	/**
	 * 统计身份证取号次数
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> countIdNumber(String tjms, String ksrq, String jsrq,Number number,String deptCode,String deptHall) throws Exception;
	
	/**
	 * 查询身份证是否存在指纹信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getZhiWenByIdNumber(Number number) throws Exception;
	
	/**
	 * 添加指纹信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public String addZhiWen(Number number) throws Exception;
	
	/**
	 * 修改指纹信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public int updateZhiWen(Number number) throws Exception;
	
	/**
	 * 删除指纹信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public int delZhiWen(Number number) throws Exception;
	
	/**
	 * 查询取号人与办理人身份不同信息
	 */
	public List<Number> queryIdnumberDifference(String tjms, String ksrq, String jsrq,Number number) throws Exception;
	
	/**
	 * 检查机动车状态
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> checkJDCStatus(Number number) throws Exception;
	
	/**
	 * 检查驾驶人状态
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> checkJSRStatus(Number number) throws Exception;
	/**
	 * 验证重复取号
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public List<Number> yzcfqh(String idnumber) throws Exception; 
	
	/**
	 * 查询当天Valuecord
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> selectValuecord(Number number) throws Exception;
	/**
	 * 根据条件查询符合的数量
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public String findByValuerecord(Number number) throws Exception;
	/**
	 * 根据队列查询未叫号数
	 */
	public String findByWait(String id,String code,String deptCode,String deptHall)throws Exception;
	/**
	 * 根据队列查询等待人数
	 */
	public String queueWaitTime(String code,String deptCode,String deptHall)throws Exception;
	/**
	 * 查询当天未叫号票
	 */
	public List<Number> dayNumber(String deptCode,String deptHall)throws Exception;
	/**
	 * 查询预计办理时间
	 */
	public String showyisj(String deptCode,String deptHall)throws Exception;
	/**
	 * 查询预计办理时间
	 */
	public void guaQiUp(String clientno,String id,String deptCode,String deptHall)throws Exception;
	/**
	 * 根据证件查询未处理业务笔数
	 */
	public List<Wclh> wblh(String idnum)throws Exception;
	/**
	 * 根据窗口号查询 返回窗口业务办理量
	 * @param windowId 窗口号
	 * @param deptHall 
	 * @param deptCode 
	 * @return ywl 窗口业务办理量
	 */
	public String selectYWLbyWindowId(String windowId, String deptCode, String deptHall) throws Exception;
	/**
	 * 查询业务类型及对应的等待数量
	 * @param countnum
	 * @return
	 */
	public List<Number> queryywlxAndddrs(Number countnum);
	/**
	 * 查询队列类型及对应的等待数量
	 * @param countnum
	 * @return
	 */
	public List<Number> querydllxAndddrs(Number countnum);
	/**
	 * 根据顺序号查询numberForImage
	 * @param number
	 * @return
	 */
	public List<Number> queryAllForImageById(Number number) throws Exception;
	/**
	 * 根据顺序号查询名字
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getNameBySXH(Number number) throws Exception;
	/**
	 * 根据窗口id查询窗口ip
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getBaripByBarid(Number number) throws Exception;
	

	/**
	 * 保存高拍仪照片
	 */
	public void saveSxh(Number number) throws Exception;
	


	/**
	 * 更新主表所有状态为2号码信息
	 * @param number
	 * @return 
	 * @throws UpdateException
	 */
	public int upStatusAll(Number number) throws Exception;
	
	public List<Number> countByIdnumber(String idnumber,String deptCode,String deptHall) throws Exception;
	
	public void insertQHrzdbz(Number number) throws Exception;
	
	public List<Number> queryRzdbz(String idnumber) throws Exception;
	
	/*
	 * 保存车牌号
	 */
	public void saveCarNumber(String dataCY) throws Exception;
	
	/*
	 * 查询车牌号
	 */
	public Number queryCarNumber(String SerialNum,String deptCode,String deptHall) throws Exception;
	/*
	 * 梧州过号上屏
	 */
	public List<Number> getNumberViaId(String id) throws Exception;
	
	/**
	 * 根据barip获取窗口号
	 */
	public String searchwindowidByip(String loginIP) throws Exception;
	/**
	 * 插入补录信息
	 * @param number
	 * @throws Exception
	 */
	public void insertBLXX(Number number) throws Exception;
	/**
	 * 查询补录信息
	 */
	public List<Number> queryBLXX(String deptCode,String deptHall) throws Exception;
	/**
	 * 删除补录信息
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public void deleteBLXX(Number number) throws Exception;
	/**
	 * 更新完结状态
	 * @param number
	 * @throws Exception
	 */
	public void updateWanJie(Number number) throws Exception;
	
	public List<Number> getrzdbz(String s, String s1, String s2, String s3, String s4, Number s5)
	        throws Exception;
}