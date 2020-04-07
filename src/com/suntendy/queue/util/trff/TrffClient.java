package com.suntendy.queue.util.trff;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import com.suntendy.queue.util.cache.CacheManager;

/*****************************************************************
* 描述: 综合平台提供接口客户端 <br>
* //////////////////////////////////////////////////////// <br>
* 创建者: 刘东东 <br>
* 创建日期: 2013-09-26 14:38:49 <br>
* 修改者:  <br>
* 修改日期:  <br>
* 修改说明:  <br>
******************************************************************/
public class TrffClient {
	
	/**
	 * 查询类新接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String query_new(String interfaceID, String strXML,String deptCode,String sbkzjsjip) throws Exception {
		String result = "";
//		String METHOD_NAME = "queryObjectOutNew";
		String METHOD_NAME = "queryObjectOut";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
//			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess?wsdl";
//			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess";
			
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess";
			
			
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId"); // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("yhbz", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("dwmc", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("dwjgdm", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("yhxm", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("zdbs", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("cjsqbh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("QueryXmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
//					result = URLDecoder.decode((String) call.invoke(new Object[] { SYSTEM_ID, INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
					result = URLDecoder.decode((String) call.invoke(new Object[] { SYSTEM_ID, INTERFACE_SERIAL_NUM, interfaceID,"","",deptCode,"",sbkzjsjip, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		System.out.println("调取"+interfaceID+"接口结果="+result);
		return result;	
   }
	
	public static String queryJKPD() throws Exception {
		String result = "";
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
//			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess?wsdl";
//			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess";
			
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess";
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	result = "1";
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		return result;	
   }
	
	
	/**
	 * 写入类新接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String write_new(String interfaceID, String strXML,String deptCode,String sbkzjsjip) throws Exception {
		String result = "";
//		String METHOD_NAME = "writeObjectOutNew";
		String METHOD_NAME = "writeObjectOut";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
//			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess?wsdl";
//			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess";
			
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess";
			
			
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId"); // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("yhbz", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("dwmc", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("dwjgdm", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("yhxm", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("zdbs", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("cjsqbh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("WriteXmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
//					result = URLDecoder.decode((String) call.invoke(new Object[] { SYSTEM_ID, INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
					result = URLDecoder.decode((String) call.invoke(new Object[] { SYSTEM_ID, INTERFACE_SERIAL_NUM, interfaceID,"","",deptCode,"",sbkzjsjip, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		System.out.println("调取"+interfaceID+"接口结果="+result);
		return result;	
   }
	/**
	 * 写入类接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String write(String interfaceID, String strXML) throws Exception {
		String result = "";
		String METHOD_NAME = "writeObjectOut";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess";
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId"); // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("UTF8XmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
					result = URLDecoder.decode((String) call.invoke(new Object[] { SYSTEM_ID, INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		return result;	
   }
	
	/**
	 * 写入类内部接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String write_nbjk(String interfaceID, String strXML) throws Exception {
		String result = "";
		String METHOD_NAME = "writeTpobject";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess";
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId").split("#")[1]; // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					//call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("UTF8XmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
					result = URLDecoder.decode((String) call.invoke(new Object[] {INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		return result;	
   }
	
	
	/**
	 * 读取类内部接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String query_nbjk(String interfaceID, String strXML) throws Exception {
		String result = "";
		String METHOD_NAME = "queryTpobject";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess";
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId").split("#")[1]; // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					//call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("UTF8XmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
					result = URLDecoder.decode((String) call.invoke(new Object[] {INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		return result;	
   }
	
	/**
	 * 读取窗口信息
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String query_sbkzjsjip(String interfaceID, String strXML) throws Exception {
		String result = "";
		String METHOD_NAME = "queryTpobject";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess";
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId").split("#")[1]; // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					//call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("UTF8XmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
					result = URLDecoder.decode((String) call.invoke(new Object[] {INTERFACE_SERIAL_NUM,interfaceID,strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		
		System.out.println("query_sbkzjsjip返回xml="+result);
		return result;	
   }
	
	/**
	 * 补充取号信息
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String write_bcqhxx(String interfaceID, String strXML) throws Exception {
		String result = "";
		String METHOD_NAME = "writeTpobject";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriTpAccess";
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId").split("#")[1]; // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("UTF8XmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
					result = URLDecoder.decode((String) call.invoke(new Object[] {INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		System.out.println("write_bcqhxx返回xml="+result);
		return result;	
   }
	
	/**
	 * 安阳评价信息写入类接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static String write_aypj(String strXML){
		String result = "no result";
		String SERVER_URL = "http://10.56.60.3/Jjpj_ga/services/trffSource";
		String METHOD_NAME = "writeObject";
		Service service = new Service();
		int state = -1;
//		Call call;
		// 判断接口连接URL是否可用
		URLAvailability u = new URLAvailability();
		state = u.isConnect(SERVER_URL);
		if (state == 200) {
			try {
				System.out.println("进入安阳接口");
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(SERVER_URL);//调用远程路径
				call.setUseSOAPAction(true);
				call.setSOAPActionURI("http://WebXml.com.cn/" + METHOD_NAME);
				call.setOperationName(new QName("http://WebXml.com.cn/", METHOD_NAME));
				
				//设置参数名
				call.addParameter(new QName("http://WebXml.com.cn/", "xtlb"), XMLType.XSD_STRING, ParameterMode.IN);
				call.addParameter(new QName("http://WebXml.com.cn/", "jkxlh"), XMLType.XSD_STRING, ParameterMode.IN);
				call.addParameter(new QName("http://WebXml.com.cn/", "jkbs"), XMLType.XSD_STRING, ParameterMode.IN);
				call.addParameter(new QName("http://WebXml.com.cn/", "writeXmlDoc"), XMLType.XSD_STRING, ParameterMode.IN);
				
				//设置返回值类型
				call.setReturnType(XMLType.XSD_STRING);
//				call.setReturnClass(java.lang.String[].class);
				result = URLDecoder.decode((String) call.invoke(new Object[] {"07",null,"07F70",strXML}),"UTF-8");
				
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("安阳评价写入接口不通！");
		}
		return result;	
   }
	
	public static String write_cs(String xmls) throws Exception{
		System.out.println("输入sfzmhm="+xmls);
		String result = "no result";
		String SERVER_URL = "http://127.0.0.1:8080/queue/services/QueueForImage";
		String METHOD_NAME = "queryNumbersById";
		Service service = new Service();
			try {
				System.out.println("进入接口测试");
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(SERVER_URL);//调用远程路径
				call.setUseSOAPAction(false);
				call.setSOAPActionURI("http://webservice.queue.suntendy.com/" + METHOD_NAME);
				call.setOperationName(new QName("http://webservice.queue.suntendy.com/", METHOD_NAME));
				
				//设置参数名
				call.addParameter("pdh", XMLType.XSD_STRING, ParameterMode.IN);
				
				//设置返回值类型
				call.setReturnType(XMLType.XSD_STRING);
//				call.setReturnClass(java.lang.String[].class);
				result = URLDecoder.decode((String) call.invoke(new Object[] {"123123"}),"UTF-8");
			      
	            System.out.println("result is " + result);  
				
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		return result;
   }
	
	public static void main(String[] args) {
		TrffClient t = new TrffClient();
		String w;
		try {
			System.out.println(123);
			w = t.write_cs("123");
			System.out.println(w);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新叫号状态
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 * 
	 */
	public static String update_status(String strXML) throws Exception{
		String result = "no result";
		String SERVER_URL = "http://10.58.206.23:9090/WebService.asmx";//http://127.0.0.1:8080/queue/services/QueueOutAccess
		String METHOD_NAME = "wxjhtz";
		Service service = new Service();
		int state = -1;
		// 判断接口连接URL是否可用
		URLAvailability u = new URLAvailability();
		state = u.isConnect(SERVER_URL);
		if (state == 200) {
			try {
				System.out.println("进入叫号状态推送微信接口");
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(SERVER_URL);//调用远程路径
				call.setUseSOAPAction(true);
				call.setSOAPActionURI("http://tempuri.org/" + METHOD_NAME);
				call.setOperationName(new QName("http://tempuri.org/", METHOD_NAME));
				
				call.addParameter(new QName("http://tempuri.org/", "xml"), XMLType.XSD_STRING, ParameterMode.IN);//new QName("http://webservice.queue.suntendy.com/", "xml")
				//设置返回值类型
				call.setReturnType(XMLType.XSD_STRING);
				System.out.println("请求参数----------"+strXML);
				result = URLDecoder.decode((String) call.invoke(new Object[] {strXML}),"UTF-8");
				System.out.println("返回数据-----------"+result);
				
			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("叫号状态推送微信接口不通！");
		}
		return result;
	}
	/**
	 * 查询类新接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String query_Condition(String interfaceID, String strXML) throws Exception {
		String result = "";
		String METHOD_NAME = "queryObjectOut";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess";
			
			
			
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId"); // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("QueryXmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
					result = URLDecoder.decode((String) call.invoke(new Object[] { 
							SYSTEM_ID, INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		System.out.println("调取"+interfaceID+"接口结果="+result);
		return result;	
   }
	/**
	 * 查询类新接口
	 * @param interfaceID 接口标识
	 * @param strXML XML格式封装的数据
	 * @return XML格式封装的结果
	 * @throws Exception 
	 */
	public static String query_new_1(String interfaceID, String strXML,String deptCode,String sbkzjsjip,String jkxlh) throws Exception {
		String result = "";
//		String METHOD_NAME = "queryObjectOutNew";
		String METHOD_NAME = "queryObjectOut";
		String SYSTEM_ID; // 系统类别
		String INTERFACE_SERIAL_NUM; // 接口序列号
		CacheManager config = CacheManager.getInstance();
		String[] ip = config.getSystemConfig("ip").split(",");
		String[] SERVER_URL = new String[ip.length];
		String[] TARGET_NAMESPACE = new String[ip.length];
		int i = 0;
		int state = -1;
		while (i < ip.length) {
//			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess?wsdl";
//			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutAccess";
			
			SERVER_URL[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess?wsdl";
			TARGET_NAMESPACE[i] = "http://" + ip[i] + "/trffweb/services/TmriOutNewAccess";
			
			
			SYSTEM_ID = config.getSystemConfig("systemId"); // 系统类别
			INTERFACE_SERIAL_NUM = config.getSystemConfig("interfaceId"); // 接口序列号
			// 判断接口连接URL是否可用
			URLAvailability u = new URLAvailability();
			state = u.isConnect(SERVER_URL[i]);
			if (state == 200) {
		    	try {
					Service service = new Service();
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new URL(SERVER_URL[i]));
					call.setUseSOAPAction(true);
					call.setSOAPActionURI(TARGET_NAMESPACE + METHOD_NAME);
					call.setOperationName(new QName(TARGET_NAMESPACE[i], METHOD_NAME));
					call.addParameter("xtlb", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkxlh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("jkid", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("cjsqbh", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("dwjgdm", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("dwmc", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("yhbz", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("yhxm", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("zdbs", XMLType.XSD_STRING, ParameterMode.IN);
					call.addParameter("QueryXmlDoc", XMLType.XSD_STRING, ParameterMode.IN);
					call.setReturnType(XMLType.XSD_STRING);
//					result = URLDecoder.decode((String) call.invoke(new Object[] { SYSTEM_ID, INTERFACE_SERIAL_NUM, interfaceID, strXML }), "UTF-8");
					result = URLDecoder.decode((String) call.invoke(new Object[] { jkxlh, INTERFACE_SERIAL_NUM, interfaceID,deptCode,"","","",sbkzjsjip, strXML }), "UTF-8");
				  } catch (ServiceException e) {
		               e.printStackTrace();
		               i++;
					   continue;
	              } catch (RemoteException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              } catch (MalformedURLException e) {
	                   e.printStackTrace();
	                   i++;
					   continue;
	              }catch (Exception e) {
		               e.printStackTrace();
					   i++;
					   continue;
				}
				break;
		    } else {
		    	System.out.println("第"+(i+1)+"个IP的接口不通！");
				i++;
				continue;
			}
		}
		System.out.println("调取"+interfaceID+"接口结果="+result);
		return result;	
   }
}