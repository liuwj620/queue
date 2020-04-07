package com.suntendy.queue.util.trff;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.suntendy.queue.agent.vo.AgentVO;
import com.suntendy.queue.agent.vo.WorkVO;

/*****************************************************************
* 描述: 综合平台提供接口实现工具类 <br>
* //////////////////////////////////////////////////////// <br>
* 创建者: 刘东东 <br>
* 创建日期: 2013-09-26 14:10:49 <br>
* 修改者:  <br>
* 修改日期:  <br>
* 修改说明:  <br>
******************************************************************/
public class TrffUtils {
	
	
	/**
	 * 判断可否取号
	 * @param businessType 业务类型
	 * @param IDNumber 证件号码
	 * @param ip 取号机IP
	 * @param personType 人员类型
	 * @param businessNum 业务笔数
	 * @return 数组，[0]：可否取号的状态，1为可取，2不可取号；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] isFetchNumber(String businessType, String IDNumber,
			String ip, String personType, String businessNum, String jym) throws Exception {
		String[][] rows = new String[6][2];
		rows[0][0] = "ywlx";
		rows[0][1] = businessType;
		rows[1][0] = "sfzmhm";
		rows[1][1] = IDNumber;
		rows[2][0] = "qhjid";
		rows[2][1] = ip;
		rows[3][0] = "rylb";
		rows[3][1] = personType;
		rows[4][0] = "ywbs";
		rows[4][1] = businessNum;
		rows[5][0] = "jym";
		rows[5][1] = jym;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C31", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C31", strXML));
	}
	/**
	 * 根据叫号评价系统设备控制计算机IP地址，获取叫号评价相关备案信息
	 * @param sbkzjsjip
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> query_sbkzjsjip(String sbkzjsjip,String deptCode) throws Exception {
		String[][] rows = new String[1][2];
		rows[0][0] = "sbkzjsjip";
		rows[0][1] = sbkzjsjip;
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_QUERY, rows);
		String strXMLs = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>2</rownum></head><body><queue id='0'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>127.0.0.1</jsjip><jbr>gaojp</jbr><kbywlb>01#02#04</kbywlb><ckbh>A11</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='1'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.56</jsjip><jbr>gaoip</jbr><kbywlb>01#02#04</kbywlb><ckbh>A01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='2'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.37</jsjip><jbr>gaoip</jbr><kbywlb></kbywlb><ckbh>L01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>2</jsjlb></queue></body></root>";
//		return XMLUtils.readXMLs_25C10(strXMLs);
		return XMLUtils.readXMLs_25C10(TrffClient.query_new("25C10", strXML,deptCode,sbkzjsjip));
	}
	
	
	/**
	 * 补充取号信息写入
	 * @param sbkzjsjip
	 * @return
	 * @throws Exception
	 */
	public static String[] bcqhxxxr(String ywckjsjip,String sbkzjsjip,String qhxxxlh,String pdh,
			String ywlb,String sfzmhm,String dlrsfzmhm,String qhrxm,String qhsj,String rylb,String jbr,String deptCode) throws Exception {
		String[][] rows = new String[11][2];
		rows[0][0] = "ywckjsjip";
		rows[0][1] = ywckjsjip;
		rows[1][0] = "sbkzjsjip";
		rows[1][1] = sbkzjsjip;
		rows[2][0] = "qhxxxlh";
		rows[2][1] = qhxxxlh;
		rows[3][0] = "pdh";
		rows[3][1] = pdh;
		rows[4][0] = "ywlb";
		rows[4][1] = ywlb;
		rows[5][0] = "sfzmhm";
		rows[5][1] = sfzmhm;
		rows[6][0] = "dlrsfzmhm";
		rows[6][1] = dlrsfzmhm;
		rows[7][0] = "qhrxm";
		rows[7][1] = qhrxm;
		rows[8][0] = "qhsj";
		rows[8][1] = qhsj;
		rows[9][0] = "rylb";
		rows[9][1] = rylb;
		rows[10][0] = "jbr";
		rows[10][1] = jbr;
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_QUEUE, rows);
		String str = "<?xml version='1.0' encoding='GBK'?><root><head><xtlb></xtlb><cdbh></cdbh><code>1</code><rtncolname1></rtncolname1><msg3></msg3><msg></msg><msg1></msg1><msg2></msg2><rank>0</rank><level>S</level><displaymsg>[0S1]:</displaymsg><code1>0</code1><obj></obj><tzsbh></tzsbh><rtnmsg1></rtnmsg1><msgFlag></msgFlag></head></root>";
//		return XMLUtils.readXML_25C11or25C12(str);
		return XMLUtils.readXML_25C11or25C12(TrffClient.write_new("25C11", strXML,deptCode,sbkzjsjip));
	}
	
	/**
	 * 评价信息写入
	 * @param sbkzjsjip
	 * @return
	 * @throws Exception
	 */
	public static String[] pjxxxr(String qhxxxlh,String pjlb,String pjjg,String fqpjsj,String pjsj,String deptCode,String sbkzjsjip) throws Exception {
		String[][] rows = new String[5][2];
		rows[0][0] = "qhxxxlh";
		rows[0][1] = qhxxxlh;
		rows[1][0] = "pjlb";
		rows[1][1] = pjlb;
		rows[2][0] = "pjjg";
		rows[2][1] = pjjg;
		rows[3][0] = "fqpjsj";
		rows[3][1] = fqpjsj;
		rows[4][0] = "pjsj";
		rows[4][1] = pjsj;
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_QUEUE, rows);
		String str = "<?xml version='1.0' encoding='GBK'?><root><head><xtlb></xtlb><cdbh></cdbh><code>1</code><rtncolname1></rtncolname1><msg3></msg3><msg></msg><msg1></msg1><msg2></msg2><rank>0</rank><level>S</level><displaymsg>[0S1]:</displaymsg><code1>0</code1><obj></obj><tzsbh></tzsbh><rtnmsg1></rtnmsg1><msgFlag></msgFlag></head></root>";
//		return XMLUtils.readXML_25C11or25C12(str);
		return XMLUtils.readXML(TrffClient.write_new("25C12", strXML,deptCode,sbkzjsjip));
	}
	
	/**
	 * 通知提档
	 * @param IDNumber 证件号码
	 * @return 数组，[0]：机动车信息，1为成功，2失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] isNumberByHphm(String IDNumber) throws Exception {
		String[][] rows = new String[1][2];
		rows[0][0] = "sfzmhm";
		rows[0][1] = IDNumber;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
		return XMLUtils.readXML(TrffClient.write("01C20", strXML));
//		return XMLUtils.readXML(TrffClient.write_nbjk("01C20", strXML));
	}
	
	/**
	 * 保存取号信息
	 * @param serialNum 顺序号
	 * @param businessType 业务类型编号
	 * @param IDType 证件类型
	 * @param IDNumber 证件号码
	 * @param ip 取号机IP
	 * @param personType 人员类型
	 * @param businessNum 业务笔数
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] saveFetchNumberInfo(String serialNum,
			String businessType, String IDType, String IDNumber, String ip,
			String personType, String businessNum, String jym) throws Exception {
		String[][] rows = new String[7][2];
		rows[0][0] = "sxh";
		rows[0][1] = serialNum;
		rows[1][0] = "ywlx";
		rows[1][1] = businessType;
		rows[2][0] = "sfzmhm";
		rows[2][1] = "A".equals(IDType) ? IDNumber : IDType + IDNumber;
		rows[3][0] = "qhjid";
		rows[3][1] = ip;
		rows[4][0] = "rylb";
		rows[4][1] = personType;
		rows[5][0] = "ywbs";
		rows[5][1] = businessNum;
		rows[6][0] = "jym";
		rows[6][1] = jym;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C32", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C32", strXML));
	}
	
	/**
	 * 保存呼叫信息
	 * @param serialNum 顺序号
	 * @param operNum 操作员编号
	 * @param mark 数据标记 01:呼叫 02:挂起
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] saveCallInfo(String serialNum, String operNum, String mark, String jym) throws Exception {
		String[][] rows = new String[4][2];
		rows[0][0] = "sxh";
		rows[0][1] = serialNum;
		rows[1][0] = "yhdh";
		rows[1][1] = operNum;
		rows[2][0] = "jhlb";
		rows[2][1] = mark;
		rows[3][0] = "jym";
		rows[3][1] = jym;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C33", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C33", strXML));
	}
	
	/**
	 * 保存评价结果
	 * @param serialNum serialNum
	 * @param evaluResult 评价结果编号
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] saveEvaluationInfo(String serialNum, String evaluResult, String jym) throws Exception {
		String[][] rows = new String[3][2];
		rows[0][0] = "sxh";
		rows[0][1] = serialNum;
		rows[1][0] = "pjjg";
		rows[1][1] = evaluResult;
		rows[2][0] = "jym";
		rows[2][1] = jym;
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
		System.out.println("保存评价结果调用六合一封装xml内容："+strXML);
//		String[] ss=XMLUtils.readXML(TrffClient.write("02C34", strXML));
		String fhjg = TrffClient.write_nbjk("02C34", strXML);
		System.out.println("六合一评价返回XML="+fhjg);
		String[] ss=XMLUtils.readXML(fhjg);
		System.out.println("保存评价结果调用六合一返回xml内容："+ss[0]+"#"+ss[1]);
		return ss;
	}
	
	/**
	 * 保存暂停/恢复信息
	 * @param operNum 操作员编号
	 * @param status 状态
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] savePauseAndRecover(String operNum, String status, String jym) throws Exception {
		String[][] rows = new String[3][2];
		rows[0][0] = "yhdh";
		rows[0][1] = operNum;
		rows[1][0] = "ztyy";
		rows[1][1] = status;
		rows[2][0] = "jym";
		rows[2][1] = jym;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C37", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C37", strXML));
	}
	
	/**
	 * 代理人更新
	 * @param agent 代理人信息
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] updateAgent(AgentVO agent) throws Exception {
		String[][] rows = new String[19][2];
		rows[0][0] = "id";
		rows[0][1] = String.valueOf(agent.getId());
		rows[1][0] = "name";
		rows[1][1] = StringUtils.trimToEmpty(agent.getName());
		rows[2][0] = "idcard";
		rows[2][1] = StringUtils.trimToEmpty(agent.getIdCard());
		rows[3][0] = "unit";
		rows[3][1] = StringUtils.trimToEmpty(agent.getUnit());
		rows[4][0] = "register_date";
		rows[4][1] = StringUtils.trimToEmpty(agent.getRegister_date());
		rows[5][0] = "validity";
		rows[5][1] = StringUtils.trimToEmpty(agent.getValidity());
		rows[6][0] = "logout_date";
		rows[6][1] = StringUtils.trimToEmpty(agent.getLogout_date());
		rows[7][0] = "status";
		rows[7][1] = StringUtils.trimToEmpty(agent.getStatus());
		rows[8][0] = "cellphone";
		rows[8][1] = StringUtils.trimToEmpty(agent.getCellphone());
		rows[9][0] = "unit_phone";
		rows[9][1] = StringUtils.trimToEmpty(agent.getUnit_phone());
		rows[10][0] = "incorporator";
		rows[10][1] = StringUtils.trimToEmpty(agent.getIncorporator());
		rows[11][0] = "max_lshs";
		rows[11][1] = String.valueOf(agent.getMax_lshs());
		rows[12][0] = "max_times_byday";
		rows[12][1] = String.valueOf(agent.getMax_times_byday());
		rows[13][0] = "agent_level";
		rows[13][1] = StringUtils.trimToEmpty(agent.getAgent_level());
		rows[14][0] = "check_date";
		rows[14][1] = StringUtils.trimToEmpty(agent.getCheck_date());
		rows[15][0] = "jyw";
		rows[15][1] = StringUtils.trimToEmpty(agent.getJyw());
		rows[16][0] = "remark";
		rows[16][1] = StringUtils.trimToEmpty(agent.getRemark());
		rows[17][0] = "fzjg";
		rows[17][1] = "";//StringUtils.trimToEmpty(agent.getFzjg());
		rows[18][0] = "bj";
		rows[18][1] = agent.getBj();
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
		System.out.println("保存/更新代理人信息调用六合一封装xml内容："+strXML);
//		String[] ss=XMLUtils.readXML(TrffClient.write("02C38", strXML));
		String[] ss=XMLUtils.readXML(TrffClient.write_nbjk("02C38", strXML));
		System.out.println("保存/更新代理人信息调用六合一返回xml内容："+ss[0]+"#"+ss[1]);
		return ss;
	}
	
	/**
	 * 代理人权限更新
	 * @param work 权限信息
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] updateAgentLevel(WorkVO work) throws Exception {
		String[][] rows = new String[5][2];
		rows[0][0] = "agent_level";
		rows[0][1] = "";
		rows[1][0] = "agent_level_caption";
		rows[1][1] = "";
		rows[2][0] = "work_id";
		rows[2][1] = work.getWork_id();
		rows[3][0] = "agent_level_status";
		rows[3][1] = work.getAgent_workl_status();
		rows[4][0] = "creat_date";
		rows[4][1] = work.getCreat_date();
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C39", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C39", strXML));
	}
	
	/**
	 * 保存电子签名
	 * @param IDCard 身份证
	 * @param name 签名(base64编码)
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] saveSignName(String IDCard, String name) throws Exception {
		String[][] rows = new String[2][2];
		rows[0][0] = "sfzmhm";
		rows[0][1] = IDCard;
		rows[1][0] = "qm";
		rows[1][1] = name;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C40", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C40", strXML));
	}
	
	/**
	 * 保存办事人员照片
	 * @param serialNum 顺序号
	 * @param photo 照片(base64编码)
	 * @return 数组，[0]：保存的状态，1为成功，2为失败；[1]：返回信息
	 * @throws Exception 
	 */
	public static String[] savePhoto(String serialNum, String photo) throws Exception {
		String[][] rows = new String[2][2];
		rows[0][0] = "sxh";
		rows[0][1] = serialNum;
		rows[1][0] = "zp";
		rows[1][1] = photo;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C41", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C41", strXML));
	}
	
	/**
	 * 删除领证信息
	 * @传值（lsh-流水号）    传值（zllx-资料类型）      			接口说明
     *  	1                  sfzmhm                	查询驾驶人违法记录
     *  	2                  hpzl#hphm拼成的值       	查询机动车违法记录
     * 		3                  hpzl#hphm拼成的值       	查询机动车非现场违法记录
     *  	4                  sfzmhm                 	查询机动车信息
     *  	5                  hpzl#hphm拼成的值       	查询机动车信息
     *  	6                  sfzmhm                 	查询驾驶证信息
	 */
	public static String[] delLzxx(String lsh, String zllx) throws Exception {
		String[][] rows = new String[2][2];
		rows[0][0] = "lsh";
		rows[0][1] = lsh;
		rows[1][0] = "zllx";
		rows[1][1] = zllx;
		
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//		return XMLUtils.readXML(TrffClient.write("02C41", strXML));
		return XMLUtils.readXML(TrffClient.write_nbjk("02C43", strXML));
	}
	
	/**
	 * 安阳评价信息写入
	 * @传值（glbm-管理部门）  
	 *  传值（ryid-工作人员的身份证号（没有就传空值））
	 *  传值（rymc-工作人员姓名）
	 *  传值（jh-工号）
	 *  传值（ssgajgdm-管理部门代码）
	 *  传值（ywlxdm-业务类型代码）
	 *  传值（ywlxmc-业务类型名称（中文））
	 *  传值（pjsj-评价时间(yyyy-mm-dd hh24:mi:ss)）
	 *  传值（pjjg-评价结果：1 非常满意 2 基本满意 3 不满意）
	 *  传值（ywlsh-业务流水号，对应六合一业务流水号）
	 *  传值（pjip-评价ip，办公电脑ip）
	 *  传值（xh-序号：管理部门+1+九位数字）
	 *       			接口说明
	 */
	public static String[] anYangEvaluationWrite(String glbm, String ryid,String rymc,String jh,String ssgajgdm,
			String ywlxdm,String ywlxmc,String pjsj,String pjjg,String ywlsh,String pjip,String xh) throws Exception {
		String[][] rows = new String[12][12];
		rows[0][0] = "glbm";
		rows[0][1] = glbm;
		rows[1][0] = "ryid";
		rows[1][1] = ryid;
		rows[2][0] = "rymc";
		rows[2][1] = rymc;
		rows[3][0] = "jh";
		rows[3][1] = jh;
		rows[4][0] = "ssgajgdm";
		rows[4][1] = ssgajgdm;
		rows[5][0] = "ywlxdm";
		rows[5][1] = ywlxdm;
		rows[6][0] = "ywlxmc";
		rows[6][1] = ywlxmc;
		rows[7][0] = "pjsj";
		rows[7][1] = pjsj;
		rows[8][0] = "pjjg";
		rows[8][1] = pjjg;
		rows[9][0] = "ywlsh";
		rows[9][1] = ywlsh;
		rows[10][0] = "pjip";
		rows[10][1] = pjip;
		rows[11][0] = "xh";
		rows[11][1] = xh;
		
		String strXML = XMLUtils.createXMLUTF8(XMLUtils.TYPE_ANYANG, rows);
//		strXML = "<?xml version='1.0' encoding='GBK'?><root><head><result>0</result><rtcode>1</rtcode><msg>数据保存成功</msg></head></root>";
//		return XMLUtils.readXMLAY(strXML);
		return XMLUtils.readXMLAY(TrffClient.write_aypj(strXML));
	}
	
	/**
	 * 更新叫号状态
	 */
	public static String[] AyUpdateStatus(String sxh,String barid,String status)throws Exception{
		String[][] rows = new String[3][3];
		rows[0][0] = "sxh";
		rows[0][1] = sxh;
		rows[1][0] = "barid";
		rows[1][1] = barid;
		rows[2][0] = "status";
		rows[2][1] = status;
		String strXML = XMLUtils.createXMLUTF8(XMLUtils.TYPE_ANYANG1, rows);
		return XMLUtils.readXML(TrffClient.update_status(strXML));
	}

	/**
	 * 根据号牌种类和号牌号码查询机动车信息
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> query_QueryCondition(String hpzl,String hpzm,String deptCode,String sbkzjsjip) throws Exception {
		String[][] rows = new String[3][2];
		rows[0][0] = "hpzl";
		rows[0][1] = hpzl;
		rows[1][0] = "hphm";
		rows[1][1] = hpzm;
		rows[2][0] = "dabh";
		rows[2][1] = "";
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_QUERY, rows);
		System.out.println("strXML:"+strXML);
		String strXMLs = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>1</rownum></head><body><veh id='0'><zt>ABC</zt><hpzl>号牌种类</hpzl><hphm>12342342</hphm><clpp1>雪佛兰</clpp1><cllx>S12</cllx><djrq>最近定检日期</djrq><yxqz>检验有效期止</yxqz></veh></body></root>";
	  //return XMLUtils.readXMLs_01C21(strXMLs);
		return XMLUtils.readXMLs_01C21(TrffClient.query_new_1("01C21", strXML, deptCode, sbkzjsjip,"01"));
	}
	
	/**
	 * 根据身份证号码查询驾驶人信息
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> query_JSR(String sfzmhm,String deptCode,String sbkzjsjip) throws Exception {
		String[][] rows = new String[1][2];
		rows[0][0] = "sfzmhm";
		rows[0][1] = sfzmhm;
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_QUERY, rows);
		String strXMLs = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>1</rownum></head><body><DrvPerson id='0'><zt>A</zt><sfzmhm>440982199312313433</sfzmhm><xm>小陈</xm><lxzsxxdz>广州市天河区</lxzsxxdz><lxzsyzbm>邮政编码</lxzsyzbm><lxdh>联系电话</lxdh><sjhm>手机号码</sjhm></DrvPerson></body></root>";
		//	return XMLUtils.readXMLs_02C06(strXMLs);
		return XMLUtils.readXMLs_02C06(TrffClient.query_new_1("02C06", strXML, deptCode, sbkzjsjip,"02"));
	}
	
	/**
	 * 根据身份证号码查询违法信息
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> query_WfMessage(String jdsbh,String wfbh,String sfzmhm,String dabh,String jkbj,String deptCode,String sbkzjsjip) throws Exception {
		String[][] rows = new String[5][2];
		rows[0][0] = "jdsbh";
		rows[0][1] = jdsbh;
		rows[1][0] = "wfbh";
		rows[1][1] = wfbh;
		rows[2][0] = "sfzmhm";
		rows[2][1] = sfzmhm;
		rows[3][0] = "dabh";
		rows[3][1] = "360300367883";
		rows[4][0] = "jkbj";
		rows[4][1] = jkbj;
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_QUERY, rows);
		String strXMLs = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>1</rownum></head><body><violation id='0'><jkbj>1</jkbj></violation></body></root>";
	//	return XMLUtils.readXMLs_04C01(strXMLs);
		return XMLUtils.readXMLs_04C01(TrffClient.query_new_1("04C01", strXML, deptCode, sbkzjsjip,"04"));
	}
	
	/**
	 * 根据号牌种类和号牌号码查询机动车违法信息 没有节点
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> query_QueryJDCWf(String hpzl,String hpzm,String deptCode,String sbkzjsjip) throws Exception {
		String[][] rows = new String[2][2];
		rows[0][0] = "hpzl";
		rows[0][1] = hpzl;
		rows[1][0] = "hphm";
		rows[1][1] = hpzm;
		String strXML = XMLUtils.createXML(XMLUtils.TYPE_QUERY, rows);
		System.out.println("strXML:"+strXML);
		String strXMLs = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>1</rownum></head><body><violation id='0'><wfcs>0</wfcs></violation></body></root>";
	//	return XMLUtils.readXMLs_04C10(strXMLs);
		return XMLUtils.readXMLs_04C10(TrffClient.query_new_1("04C10", strXML, deptCode, sbkzjsjip,"04"));
	}
}