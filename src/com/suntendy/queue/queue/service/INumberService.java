package com.suntendy.queue.queue.service;

import java.util.List;

import com.suntendy.queue.business.domain.Business;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.NumberIdPhoto;
import com.suntendy.queue.queue.domain.PrintInfo;
import com.suntendy.queue.queue.domain.Wclh;
import com.suntendy.queue.util.exception.UpdateException;

/*****************************************************************
* 描述: 取号、叫号相关操作业务逻辑接口 <br>
* //////////////////////////////////////////////////////// <br>
* 创建者: 刘东东 <br>
* 创建日期: 2013-09-24 17:32:54 <br>
* 修改者:  <br>
* 修改日期:  <br>
* 修改说明:  <br>
******************************************************************/
public interface INumberService {

	/**
	 * 获取当天号码
	 * @return 号码信息
	 */
	public List<Number> searchCurrentDayNumber(Number number);
	
	/**
	 * 获取当天号码
	 * @return 号码信息
	 */
	public List<Number> queryNumberBypdh(Number number);
	
	/**
	 * 获取业务号码总数
	 * @param loginIP 登录系统IP
	 * @return
	 * @throws Exception 
	 */
	public String getTotalNumberOfBusinessNumber(String loginIP) throws Exception;
	
	/**
	 * 取号
	 * @param number 取号相关信息
	 * @return 打印参数信息
	 * @throws Exception
	 */
	public PrintInfo getNewNumber(Number number) throws Exception;
	
	/**
	 * 叫号
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return 提示信息
	 * @throws Exception
	 */
	public String callNumber(String operNum, String loginIP) throws Exception;
	
	/**
	 * 新叫号
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return 提示信息
	 * @throws Exception
	 */
	public String callNumber_new(String operNum, String loginIP,String blbj) throws Exception;
	
	/**
	 * 重呼
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @throws Exception
	 */
	public String recall(String operNum, String loginIP) throws Exception;
	
	/**
	 * 过号
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @throws Exception
	 */
	public String pass(String operNum, String loginIP,String passReason) throws Exception;
	
	/**
	 * 提请评价
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @throws Exception
	 */
	public String toEvaluation(String operNum, String loginIP) throws Exception;
	
	/**
	 * 提请评价ForQueueOut
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @throws Exception
	 */
	public String toEvaluationForQueueOut(String operNum, String loginIP) throws Exception;
	
	/**
	 * 评价
	 * @param number 更新信息
	 * @param clientIp 登录系统IP
	 * @throws Exception
	 */
	public void evaluation(Number number, String loginIP) throws Exception;
	
	/**
	 * 评价ForQueueOut
	 * @param number 更新信息
	 * @param clientIp 登录系统IP
	 * @throws Exception
	 */
	public void evaluationForQueueOut(Number number, String loginIP) throws Exception;	
	
	/**
	 * 挂起
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return 
	 * @throws Exception
	 */
	public String hangup(String operNum, String loginIP) throws Exception;
	/**
	 * 新接口挂起
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return 
	 * @throws Exception
	 */
	public String hangup_new(Number number) throws Exception;
	
	/**
	 * 获取挂起号码
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return
	 * @throws Exception
	 */
	public String toHangupRecover(String operNum, String loginIP) throws Exception;
	
	/**
	 * 挂起恢复
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @param id 数据ID
	 * @return
	 * @throws Exception
	 */
	public String hangupRecover(String operNum, String loginIP, String id) throws Exception;
	
	public String hangupRecover_new(String operNum, String loginIP, String id) throws Exception;
	
	/**
	 * 获取流转窗口号
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return 
	 * @throws Exception
	 */
	public String toChangeWin(String operNum, String loginIP) throws Exception;
	
	/**
	 * 流转号码
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return 
	 * @throws Exception
	 */
	public String transferenceNumber(String operNum, String loginIP, String address) throws Exception;
	
	/**
	 * 获取暂停原因
	 * @param operNum 操作员编号
	 * @param loginIP 登录系统IP
	 * @return 
	 * @throws Exception
	 */
	public String toPause(String operNum, String loginIP) throws Exception;
	
	/**
	 * 暂停或恢复
	 * @param operNum 操作员编号
	 * @param loginIP 登录IP
	 * @param reason 暂停原因
	 * @return 提示信息
	 * @throws Exception
	 */
	public PrintInfo pauseOrRecover(String operNum, String loginIP, String reason) throws Exception;
	
	public void updateNumberByID(String id, String operNum) throws UpdateException;
	
	/**
	 * 根据身份证获取非代理人当月取号数量
	 */
	public String getNotAgentByIdCardCount(Number number,String deptCode, String deptHall) throws Exception;
	/**
	 * 保存照片
	 */
	public String savePhoto(Number number,String flag) throws Exception;
	/**
	 * 保存高拍仪照片
	 */
	public String savaGpyPhoto(Number number) throws Exception;
	/**
	 * 根据窗口ip获取窗口当前办理业务
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public Number getNumberAllByClientIP(String clientIp) throws Exception;
	/**
	 * 
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public Number getNumberAllByClientIPForHF(String clientIp,String sxh) throws Exception;
	/**
	 * 根据id获取取号人员所有信息
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public Number getValueRecordAllById(Number number) throws Exception;
	/**
	 * 获取取号人员所有信息
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecordAll(Number number) throws Exception;
	
	public List<Number> getValueRecordAllForAY(Number number) throws Exception;
	/**
	 * 根据身份证ID查询当天是否取号
	 * @param idnumber
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecardbyIdnumber(String idnumber,String lsh) throws Exception;
	/**
	 * 强制叫号
	 * @param clientIp
	 * @return
	 * @throws Exception
	 */
	public Number forcedToCallNumber_status(String loginIP) throws Exception;
	/**
	 * 根据号码查询窗口
	 * @param number
	 * @return
	 */
	public List<Number> getBarIdBySerialNum(Number number) throws Exception;
	
	/**
	 * 查询等待人数
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public List<Number> showWaitRs(Number number) throws Exception;
	
	public List<Number> showyjrs(Number number) throws Exception;
	
	/**
	 * 查询当天挂起记录
	 * @param serialNum
	 * @return
	 * @throws Exception
	 */
	public List<Number> getValueRecordGq() throws Exception;
	/**
	 * 跟新挂起信息状态及办理人身份证号
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public int updateValueRecordGqStatus(String id,String blridnumber) throws Exception;
	
	/**
	 * 根据号码、窗口号查询日志
	 * @param number
	 * @return
	 */
	public List<Number> getvaluerecordRZ(Number number,String tjms, String ksrq, String jsrq,String IDNumber) throws Exception;
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
	 * 保存身份证照片
	 * @param numphoto
	 * @throws Exception
	 */
	public void insertSfzPhoto(NumberIdPhoto numphoto) throws Exception;
	
	/**
	 * 更新身份证照片
	 * @param numphoto
	 * @throws Exception
	 */
	public void updateSfzPhoto(NumberIdPhoto numphoto) throws Exception;
	/**
	 * 根据条件查询身份证照片
	 * @param numphoto
	 * @return
	 * @throws Exception
	 */
	public List<NumberIdPhoto> queryPhotoBy(NumberIdPhoto numphoto) throws Exception;
	
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
	 * 统计区域未叫号数量
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> countWJHshuliang(String id,String waitingarea,String businessType,String queueCode,String deptCode,String deptHall) throws Exception;
	
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
	 * 根据条件查询日志信息
	 * @param tjms
	 * @param ksrq
	 * @param jsrq
	 * @return
	 * @throws Exception
	 */
	public Number getCodeByRz(Number number) throws Exception;
	
	/**
	 * 根据身份证号或流水号获取照片
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getCardAndLshByAll(Number number) throws Exception;
	
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
	public void addZhiWen(Number number) throws Exception;
	
	/**
	 * 修改指纹信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public void updateZhiWen(Number number) throws Exception;
	
	/**
	 * 删除指纹信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public void delZhiWen(Number number) throws Exception;
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
	 * 查询当天信息
	 * @param idnumber
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
	 * 根据ID查询业务信息
	 */
	public List<Business> queryBus(Business bus)throws Exception;
	/**
	 * 根据队列查询等待时间
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
	 * 更新out1
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public int updateOut1(Number number) throws UpdateException;
	/**
	 * 根据证件查询未处理业务笔数
	 */
	public List<Wclh> wblh(String idnum) throws Exception;
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
	 * 更新人证对比值
	 * @param number
	 * @return
	 * @throws UpdateException
	 */
	public int updateRzdbz(Number number) throws UpdateException;
	/**
	 * 根据顺序号查询名字
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getNameBySXH(Number number) throws Exception;
	

	/**
	 * 保存sxh
	 */
	public void saveSxh(Number number) throws Exception;
	


	/**
	 * 更新数据库valurecord表状态为2的数据
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public int upStatusAll(Number number) throws Exception;
	
	/**
	 * 统计当天身份证取号次数
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> countByIdnumber(String idnumber,String deptCode,String deptHall) throws Exception;
	
	public void insertQHrzdbz(Number number) throws Exception;
	
	public List<Number> queryRzdbz(String idnumber) throws Exception;
	
	//驗證外屏通訊
	public String validateYz(String loginIP) throws Exception;
	
	/**
	 * 取号身份验证
	 * @param idnumber
	 * @param deptCode 
	 * @param deptHall
	 * @return String
	 */
	public String qhsfzyz(String idnumber, String deptcode, String depthall) throws Exception;
	/**
	 * 根据顺序号查询当天信息
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public List<Number> getvaluerecordByClientno(Number number) throws Exception;
	/**
	 * 插入补录信息
	 * @param number
	 * @throws Exception
	 */
	public void insertBLXX(Number number) throws Exception;
	
	/**
	 * 更新完结状态
	 * @param number
	 * @throws Exception
	 */
	public void updateWanJie(Number number) throws Exception;
	
	public List<Number> getrzdbz(String s, String s1, String s2, String s3, String s4, Number s5)
	        throws Exception;
}