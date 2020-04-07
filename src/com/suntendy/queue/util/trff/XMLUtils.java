package com.suntendy.queue.util.trff;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/*****************************************************************
* 描述: 封装、解析XML工具类 <br>
* //////////////////////////////////////////////////////// <br>
* 创建者: 刘东东 <br>
* 创建日期: 2013-09-26 14:37:54 <br>
* 修改者:  <br>
* 修改日期:  <br>
* 修改说明:  <br>
******************************************************************/
public class XMLUtils {
	//XML文档的类型 写入
	public static final String TYPE_WRITE = "tmri";
	public static final String TYPE_QUERY = "QueryCondition";
	public static final String TYPE_ANYANG = "cjglBaseinfo";
	public static final String TYPE_ANYANG1 = "WriteCondition";
	public static final String TYPE_QUEUE = "queue";

	/**
	 * 创建XML文档
	 * @param type XML文档的类型
	 * @param rows 要封装的节点名和内容
	 * @return XML格式字符串
	 */
	public static String createXML(String type, String[][] rows) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element typeElement = root.addElement(type);
		
		for (String[] row : rows) {
			Element node = typeElement.addElement(row[0]);
			try {
				node.addText(URLEncoder.encode(StringUtils.trimToEmpty(row[1]), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		System.out.println("传给六合一的xml====="+document.asXML().replaceAll("%23", "#").replaceAll("%3A", ":").replaceAll("%2C", ",").replaceAll("\\+", " "));
		return document.asXML().replaceAll("%23", "#").replaceAll("%3A", ":").replaceAll("%2C", ",").replaceAll("\\+", " ");
	}
	
	/**
	 * 创建XML文档
	 * @param type XML文档的类型
	 * @param rows 要封装的节点名和内容
	 * @return XML格式字符串
	 */
	public static String createXMLUTF8(String type, String[][] rows) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element typeElement = root.addElement(type);
		
		for (String[] row : rows) {
			Element node = typeElement.addElement(row[0]);
			try {
				node.addText(URLEncoder.encode(StringUtils.trimToEmpty(row[1]), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String strXML = document.asXML().replaceAll("%23", "#").replaceAll("%3A", ":").replaceAll("%2C", ",");
		try {
			strXML = URLDecoder.decode(strXML, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//System.out.println("传给六合一的xml====="+document.asXML().replaceAll("%23", "#").replaceAll("%3A", ":").replaceAll("%2C", ","));
		System.out.println("传给六合一的xml====="+strXML);
		return document.asXML().replaceAll("%23", "#").replaceAll("%3A", ":").replaceAll("%2C", ",");
	}
	/**
	 * 创建XML文档GBK
	 * @param type XML文档的类型
	 * @param rows 要封装的节点名和内容
	 * @return XML格式字符串
	 */
	public static String createXMLGBK(String type, String[][] rows) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element typeElement = root.addElement(type);
		
		for (String[] row : rows) {
			Element node = typeElement.addElement(row[0]);
			try {
				node.addText(URLEncoder.encode(StringUtils.trimToEmpty(row[1]), "GBK"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		System.out.println("传给六合一的xml====="+document.asXML());
		return document.asXML();
	}
	
	/**
	 * 读取XML返回的结果
	 * @param strXML XML格式封装的字符串
	 * @return 可否取号的状态及相关提示信息
	 */
	public static String[] readXML(String strXML) {
		String[] result = new String[2];
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();
				result[0] = ((Node) root.selectNodes("/root/head/code").get(0)).getText();
				result[1] = ((Node) root.selectNodes("/root/head/message").get(0)).getText();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 读取XML返回的结果
	 * @param strXML XML格式封装的字符串
	 * @return 可否取号的状态及相关提示信息
	 */
	public static String[] readXML_25C11or25C12(String strXML) {
		String[] result = new String[2];
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();
				result[0] = ((Node) root.selectNodes("/root/head/code").get(0)).getText();
//				result[1] = ((Node) root.selectNodes("/root/head/message").get(0)).getText();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 读取安阳评价接口XML返回的结果
	 * @param strXML XML格式封装的字符串
	 * @return 可否取号的状态及相关提示信息
	 */
	public static String[] readXMLAY(String strXML) {
		String[] result = new String[2];
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();
				result[0] = ((Node) root.selectNodes("/root/head/rtcode").get(0)).getText();
				result[1] = ((Node) root.selectNodes("/root/head/msg").get(0)).getText();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 读取XML返回的多条结果
	 * @param strXML XML格式封装的字符串
	 * @return 可否取号的状态及相关提示信息
	 */
	public static String[] readXMLs(String strXML) {
		System.out.println("进入XMLs解析");
		System.out.println("StrXML的长度="+strXML.length());
		String[] result = null;
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();
				if ("1".equals(((Node) root.selectNodes("//code").get(0)).getText())) {
					if (!"0".equals(((Node) root.selectNodes("//rownum").get(0)).getText())) {
						int num = root.selectNodes("//lsh").size();
						result = new String[num*4+2];
						System.out.println("解析size="+(num*4));
						for(int i=0;i<num;i++){
							result[i*4] = ((Node) root.selectNodes("//lsh").get(i)).getText();
							System.out.println("lsh="+result[i*4]);
							result[i*4+1] = ((Node) root.selectNodes("//xm").get(i)).getText();
//							System.out.println("xm="+result[i*4+1]);
							result[i*4+2] = ((Node) root.selectNodes("//zllx").get(i)).getText();
//							System.out.println("zllx="+result[i*4+2]);
							result[i*4+3] = ((Node) root.selectNodes("//sfzmhm").get(i)).getText();
//							System.out.println("rksj="+result[i*4+3]);
						}
						result[num*4] = ((Node) root.selectNodes("//code").get(0)).getText();
						result[num*4+1] = ((Node) root.selectNodes("//rownum").get(0)).getText();
//						for (int i = 0; i < result.length; i++) {
//							System.out.println(result[i]);
//						}
					}
				}

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 读取25C10备案信息结果
	 * @param strXML XML格式封装的字符串
	 * @return 可否取号的状态及相关提示信息
	 */
	public static Map<String, Object> readXMLs_25C10(String strXML) {
		System.out.println("进入XMLs_25C10解析");
		System.out.println("XMLs_25C10的长度="+strXML.length());
//		String xmls = "<?xml version='1.0' edcoding='GBK'?><root><head><code>1</code><message>数据下载成功！</messags><rownum>2</rownum></head><body><queue id='0'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>127.0.0.1</jsjip><jbr>gaoip</jbr><kbywlb>01#02#04</kbywlb><ckbh>A11</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='1'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.56</jsjip><jbr>gaoip</jbr><kbywlb>01#02#04</kbywlb><ckbh>A01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='2'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.37</jsjip><jbr>gaoip</jbr><kbywlb></kbywlb><ckbh>L01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>2</jsjlb></queue></body></root>";
//		System.out.println(strXML);
		Map<String, Object> result = new HashMap<String, Object>();
		String jsjip="",jbr="",kbywlb="",sbkzjsjbh="",jsjlb="";
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();			
				List<Object> listqueue = root.selectNodes("//queue");
				System.out.println("listcode="+listqueue.size());
				if ("1".equals(((Node) root.selectNodes("//code").get(0)).getText())) {
					if (!"0".equals(((Node) root.selectNodes("//rownum").get(0)).getText())) {
						sbkzjsjbh = ((Node)root.selectNodes("//queue/sbkzjsjbh").get(0)).getText();
						result.put("sbkzjsjbh", sbkzjsjbh);
						int num = root.selectNodes("//queue").size();
						for (int i = 0; i < num; i++) {
							if ("1".equals(((Node)root.selectNodes("//queue/jsjlb").get(i)).getText())) {
								jsjip = ((Node)root.selectNodes("//queue/jsjip").get(i)).getText();
								jbr = ((Node)root.selectNodes("//queue/jbr").get(i)).getText();
								kbywlb = ((Node)root.selectNodes("//queue/kbywlb").get(i)).getText();
								Map<String, String> map = new HashMap<String, String>();
								map.put("jsjip", jsjip);
								map.put("jbr", jbr);
								map.put("kbywlb", kbywlb);
								result.put(jsjip, map);
							}
						}
					}
				}

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据号牌种类和号牌号码查询机动车信息
	 * @param strXML XML格式封装的字符串
	 * @return 机动车信息
	 */
	public static Map<String, Object> readXMLs_01C21(String strXML) {
		System.out.println("进入机动车信息解析");
		System.out.println("机动车信息的长度="+strXML.length());
		Map<String, Object> result = new HashMap<String, Object>();
		String hpzl="",hphm="",clpp1="",cllx="",zt="",djrq="",yxqz="";
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();			
				if ("1".equals(((Node) root.selectNodes("//code").get(0)).getText())) {
					if (!"0".equals(((Node) root.selectNodes("//rownum").get(0)).getText())) {
								hpzl = ((Node)root.selectNodes("//veh/hpzl").get(0)).getText();
								hphm = ((Node)root.selectNodes("//veh/hphm").get(0)).getText();
								clpp1 = ((Node)root.selectNodes("//veh/clpp1").get(0)).getText();
								cllx = ((Node)root.selectNodes("//veh/cllx").get(0)).getText();
								yxqz = ((Node)root.selectNodes("//veh/yxqz").get(0)).getText();
								djrq = ((Node)root.selectNodes("//veh/djrq").get(0)).getText();
								zt = ((Node)root.selectNodes("//veh/zt").get(0)).getText();
								Map<String, String> map = new HashMap<String, String>();
								map.put("hpzl", hpzl);
								map.put("hphm", hphm);
								map.put("clpp1", clpp1);
								map.put("cllx", cllx);
								map.put("yxqz", yxqz);
								map.put("djrq", djrq);
								map.put("zt", zt);
								result.put("ConditionMessage", map);
					}
				}

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据身份证号码查询驾驶人信息
	 * @param strXML XML格式封装的字符串
	 * @return 驾驶人信息
	 */
	public static Map<String, Object> readXMLs_02C06(String strXML) {
		System.out.println("进入驾驶人信息解析");
		System.out.println("驾驶人信息的长度="+strXML.length());
//		String xmls = "<?xml version='1.0' edcoding='GBK'?><root><head><code>1</code><message>数据下载成功！</messags><rownum>2</rownum></head><body><queue id='0'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>127.0.0.1</jsjip><jbr>gaoip</jbr><kbywlb>01#02#04</kbywlb><ckbh>A11</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='1'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.56</jsjip><jbr>gaoip</jbr><kbywlb>01#02#04</kbywlb><ckbh>A01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='2'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.37</jsjip><jbr>gaoip</jbr><kbywlb></kbywlb><ckbh>L01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>2</jsjlb></queue></body></root>";
//		System.out.println(strXML);
		Map<String, Object> result = new HashMap<String, Object>();
		String sfzmhm="",xm="",lxzsxxdz="",lxzsyzbm="",lxdh="",sjhm="",zt="";
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();			
				if ("1".equals(((Node) root.selectNodes("//code").get(0)).getText())) {
					if (!"0".equals(((Node) root.selectNodes("//rownum").get(0)).getText())) {
						sfzmhm = ((Node)root.selectNodes("//DrvPerson/sfzmhm").get(0)).getText();
						xm = ((Node)root.selectNodes("//DrvPerson/xm").get(0)).getText();
						lxzsxxdz = ((Node)root.selectNodes("//DrvPerson/lxzsxxdz").get(0)).getText();
						lxzsyzbm = ((Node)root.selectNodes("//DrvPerson/lxzsyzbm").get(0)).getText();
						lxdh = ((Node)root.selectNodes("//DrvPerson/lxdh").get(0)).getText();
						sjhm = ((Node)root.selectNodes("//DrvPerson/sjhm").get(0)).getText();
						zt = ((Node)root.selectNodes("//DrvPerson/zt").get(0)).getText();
								Map<String, String> map = new HashMap<String, String>();
								map.put("sfzmhm", sfzmhm);
								map.put("xm", xm);
								map.put("lxzsxxdz", lxzsxxdz);
								map.put("lxzsyzbm", lxzsyzbm);
								map.put("sjhm", sjhm);
								map.put("lxdh", lxdh);
								map.put("zt", zt);
								result.put("JSRMessage", map);
					}
				}

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据身份证号码查询违法信息
	 * @param strXML XML格式封装的字符串
	 * @return 违法信息
	 */
	public static Map<String, Object> readXMLs_04C01(String strXML) {
		System.out.println("进入违法信息解析");
		System.out.println("违法信息的长度="+strXML.length());
		Map<String, Object> result = new HashMap<String, Object>();
		String jkbj="";
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();			
				if ("1".equals(((Node) root.selectNodes("//code").get(0)).getText())) {
					if (!"0".equals(((Node) root.selectNodes("//rownum").get(0)).getText())) {
						jkbj = ((Node)root.selectNodes("//vio/jkbj").get(0)).getText();
								Map<String, String> map = new HashMap<String, String>();
								map.put("jkbj", jkbj);
								result.put("WfMessage", map);
					}
				}

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据身份证号码查询违法信息
	 * @param strXML XML格式封装的字符串
	 * @return 违法信息
	 */
	public static Map<String, Object> readXMLs_04C10(String strXML) {
		System.out.println("进入机动车违法信息解析");
		System.out.println("机动车违法信息的长度="+strXML.length());
		Map<String, Object> result = new HashMap<String, Object>();
		String wfcs="";
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();			
				if ("1".equals(((Node) root.selectNodes("//code").get(0)).getText())) {
					if (!"0".equals(((Node) root.selectNodes("//rownum").get(0)).getText())) {
						wfcs = ((Node)root.selectNodes("//violation/wfcs").get(0)).getText();
								Map<String, String> map = new HashMap<String, String>();
								map.put("wfcs", wfcs);
								result.put("JDCWfMessage", map);
					}
				}

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static HashMap<String, String> readXMLForQueueOut(String strXML) {
		System.out.println("进入readXMLForQueueOut解析");
		System.out.println("StrXML的长度="+strXML.length());
		HashMap<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(strXML)) {
			try {
				Document document = DocumentHelper.parseText(strXML);
				Element root = document.getRootElement();
				String ip="",yhdh="",zjlx="",sfzmhm="",ywlx="",yykssj="",yyjssj="",
				czlx="",glbm="",dtbh="",xxbh="",nr="",bj="",barid="",sxh="",img="",
				key="",idnumber="",name="",DBidnumber="",DBname="",ywid="",message="";

				if (root.selectNodes("//ip").size()>0) {
					ip = ((Node) root.selectNodes("//ip").get(0)).getText();//ip
					System.out.println("ip="+ip);
				}
				if (root.selectNodes("//yhdh").size()>0) {
					yhdh = ((Node) root.selectNodes("//yhdh").get(0)).getText();//用户代号
					System.out.println("yhdh="+yhdh);
				}
				if (root.selectNodes("//zjlx").size()>0) {
					zjlx = ((Node) root.selectNodes("//zjlx").get(0)).getText();//证件类型
					System.out.println("zjlx="+zjlx);
				}
				if (root.selectNodes("//sfzmhm").size()>0) {
					sfzmhm = ((Node) root.selectNodes("//sfzmhm").get(0)).getText();//身份证明号码
					System.out.println("sfzmhm="+sfzmhm);
				}
				if (root.selectNodes("//ywlx").size()>0) {
					ywlx = ((Node) root.selectNodes("//ywlx").get(0)).getText();//业务类型
					System.out.println("ywlx="+ywlx);
				}
				if (root.selectNodes("//yykssj").size()>0) {
					yykssj = ((Node) root.selectNodes("//yykssj").get(0)).getText();//预约开始时间
					System.out.println("yykssj="+yykssj);
				}
				if (root.selectNodes("//yyjssj").size()>0) {
					yyjssj = ((Node) root.selectNodes("//yyjssj").get(0)).getText();//预约结束时间
					System.out.println("yyjssj="+yyjssj);
				}
				if (root.selectNodes("//czlx").size()>0) {
					czlx = ((Node) root.selectNodes("//czlx").get(0)).getText();//操作类型
					System.out.println("czlx="+czlx);
				}
				if (root.selectNodes("//glbm").size()>0) {
					glbm = ((Node) root.selectNodes("//glbm").get(0)).getText();//管理部门
					System.out.println("glbm="+glbm);
				}
				if (root.selectNodes("//dtbh").size()>0) {
					dtbh = ((Node) root.selectNodes("//dtbh").get(0)).getText();//大厅编号
					System.out.println("dtbh="+dtbh);
				}
				if (root.selectNodes("//xxbh").size()>0) {
					xxbh = ((Node) root.selectNodes("//xxbh").get(0)).getText();//信息编号
					System.out.println("xxbh="+xxbh);
				}
				if (root.selectNodes("//nr").size()>0) {
					nr = ((Node) root.selectNodes("//nr").get(0)).getText();//领证信息内容
					System.out.println("nr="+nr);
				}
				if (root.selectNodes("//bj").size()>0) {
					bj = ((Node) root.selectNodes("//bj").get(0)).getText();//上屏/下屏标记
					System.out.println("bj="+bj);
				}
				if (root.selectNodes("//barid").size()>0) {
					barid = ((Node) root.selectNodes("//barid").get(0)).getText();//窗口号
					System.out.println("barid="+barid);
				}
				if (root.selectNodes("//sxh").size()>0) {
					sxh = ((Node) root.selectNodes("//sxh").get(0)).getText();//顺序号
					System.out.println("sxh="+sxh);
				}
				if (root.selectNodes("//img").size()>0) {
					img = ((Node) root.selectNodes("//img").get(0)).getText();//照片
					System.out.println("img="+img);
				}
				if (root.selectNodes("//idnumber").size()>0) {
					idnumber = ((Node) root.selectNodes("//idnumber").get(0)).getText();//本人身份证
					System.out.println("idnumber="+idnumber);
				}
				if (root.selectNodes("//name").size()>0) {
					name = ((Node) root.selectNodes("//name").get(0)).getText();//本人姓名
					System.out.println("name="+name);
				}
				
				if (root.selectNodes("//DBidnumber").size()>0) {
					DBidnumber = ((Node) root.selectNodes("//DBidnumber").get(0)).getText();//代办人身份证
					System.out.println("DBidnumber="+DBidnumber);
				}
				if (root.selectNodes("//DBname").size()>0) {
					DBname = ((Node) root.selectNodes("//DBname").get(0)).getText();//代办人姓名
					System.out.println("DBname="+DBname);
				}
				if (root.selectNodes("//ywid").size()>0) {
					ywid = ((Node) root.selectNodes("//ywid").get(0)).getText();//业务类型编号
					System.out.println("ywid="+ywid);
				}
				if (root.selectNodes("//key").size()>0) {
					key = ((Node) root.selectNodes("//key").get(0)).getText();//照片
					System.out.println("key="+key);
				}
				if (root.selectNodes("//message").size()>0) {
					message = ((Node) root.selectNodes("//message").get(0)).getText();//照片
					System.out.println("message="+message);
				}
				map.put("ip", ip);
				map.put("yhdh", yhdh);
				map.put("zjlx", zjlx);
				map.put("sfzmhm", sfzmhm);
				map.put("ywlx", ywlx);
				map.put("yykssj", yykssj);
				map.put("yyjssj", yyjssj);
				map.put("czlx", czlx);
				map.put("glbm", glbm);
				map.put("dtbh", dtbh);
				map.put("xxbh", xxbh);
				map.put("nr", nr);
				map.put("bj", bj);
				map.put("barid", barid);
				map.put("sxh", sxh);
				map.put("img", img);
				map.put("idnumber", idnumber);
				map.put("name", name);
				map.put("DBidnumber", DBidnumber);
				map.put("DBname", DBname);
				map.put("ywid", ywid);
				map.put("key", key);
				map.put("message", message);

			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return map;
	}


	 public static void main(String[] args)

     {

           // TODO Auto-generated method stub
//           String strXML="<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>1144</rownum></head><body><tmri id='0'><lsh>2120305055635</lsh><xm>贺庆华</xm><zllx>01</zllx><rksj>2015-03-16 17:20:37.0</rksj></tmri><tmri id='1'><lsh>2120328094362</lsh><xm>邢华</xm><zllx>01</zllx><rksj>2015-03-16 15:36:22.0</rksj></tmri><tmri id='2'><lsh>2120406107269</lsh><xm>杨平</xm><zllx>01</zllx><rksj>2015-03-16 18:15:47.0</rksj></tmri></body></root>";
           String strXML = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>2</rownum></head><body><queue id='0'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>127.0.0.1</jsjip><jbr>gaoip</jbr><kbywlb>01#02#04</kbywlb><ckbh>A11</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='1'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.56</jsjip><jbr>gaoip</jbr><kbywlb>01#02#04</kbywlb><ckbh>A01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>1</jsjlb></queue><queue id='2'><gxsj>Wed Dec 05 13:34:38 GMT+08:00 2018</gxsj><cclrsj>Wed Dec 05 13:34:38 GMT+08:00 2018</cclrsj><jsjip>172.19.100.37</jsjip><jbr>gaoip</jbr><kbywlb></kbywlb><ckbh>L01</ckbh><sbkzjsjbh>1310000005</sbkzjsjbh><jsjlb>2</jsjlb></queue></body></root>";
//           String strXML = "<?xml version='1.0' encoding='GBK'?><root><head><code>1</code><message>数据下载成功！</message><rownum>2</rownum></head><body></body></root>";
           XMLUtils xmlUtils = new XMLUtils();
		  Map<String, Object> result = xmlUtils.readXMLs_25C10(strXML);
		  Map<String, String> map = (Map<String, String>) result.get("127.0.0.1");
		  System.out.println("经办人="+map.get("jbr"));

     }


	
}