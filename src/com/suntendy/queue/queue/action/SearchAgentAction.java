package com.suntendy.queue.queue.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;

import com.opensymphony.webwork.ServletActionContext;
import com.suntendy.queue.agent.services.IAgentService;
import com.suntendy.queue.agent.services.IWorkService;
import com.suntendy.queue.agent.vo.AgentVO;
import com.suntendy.queue.agent.vo.MessageVO;
import com.suntendy.queue.agent.vo.WorkVO;
import com.suntendy.queue.base.BaseAction;
import com.suntendy.queue.hmd.domain.Hmd;
import com.suntendy.queue.hmd.service.IhmdService;
import com.suntendy.queue.queue.domain.Number;
import com.suntendy.queue.queue.domain.NumberIdPhoto;
import com.suntendy.queue.queue.service.INumberService;
import com.suntendy.queue.util.Base64Util;
import com.suntendy.queue.util.DateUtils;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.trff.TrffClient;
import com.suntendy.queue.util.trff.XMLUtils;

/*******************************************************************************
 * 描述: 根据身份证号码查询代理人 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-10-09 10:109:42 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class SearchAgentAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String IDNumber;
	private IAgentService agentService;
	private INumberService numberService;
	private IWorkService workService;
	private IhmdService hmdService;

	public IhmdService getHmdService() {
		return hmdService;
	}

	public void setHmdService(IhmdService hmdService) {
		this.hmdService = hmdService;
	}

	public INumberService getNumberService() {
		return numberService;
	}

	public void setNumberService(INumberService numberService) {
		this.numberService = numberService;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String number) {
		IDNumber = number;
	}

	public IAgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(IAgentService agentService) {
		this.agentService = agentService;
	}
	public IWorkService getWorkService() {
		return workService;
	}

	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}

	@Override
	public String execute() throws Exception {
		HttpServletRequest request = getRequest();
		String flag = request.getParameter("flag");
		JSONObject object = new JSONObject();
		CacheManager cacheManager = CacheManager.getInstance();
		String deptCode = cacheManager.getOfDeptCache("deptCode");
		String deptHall = cacheManager.getOfDeptCache("deptHall");
		object.put("isAgnet", false);
		if("0".equals(flag)){
			String isrzdb = cacheManager.getSystemConfig("hfhmrzdb");
			object.put("isrzdb", isrzdb);
		}else {
			if ("0".equals(CacheManager.getInstance().getSystemConfig("isCheckAgent"))) {
				MessageVO messageVO = new MessageVO();
				messageVO.setPxzd("id");
				messageVO.setIdCard(IDNumber);
				messageVO.setDeptCode(CacheManager.getInstance().getOfDeptCache("deptCode"));
				
				List<AgentVO> agentList = agentService.searchAgent(messageVO);
				List<Number> numList = numberService.countByIdnumber(IDNumber, deptCode, deptHall);
				if (null != agentList && !agentList.isEmpty()) {
					AgentVO agent = agentList.get(0);
					String status = "";
					if(numList.size()>0) {
						if (Integer.parseInt(numList.get(0).getIdnumbercount()) >= Integer.parseInt(agent.getDlrqhcs())) {
							status = "此代理人取号次数超过限定次数";
						}else {
							status = "";
						}
					}else {
						status = "";
					}
					if ("2".equals(agent.getStatus())) {
						object.put("isAgnet", true);
						status = "此代理人已暂停，无法取号";
					} else if ("1".equals(agent.getStatus())) {
						object.put("isAgnet", true);
					} else {
						object.put("isAgnet", false);
					}
				
					object.put("agentId", agent.getId());
					object.put("IDNumber", IDNumber);
					object.put("agentStatus", status);
					object.put("nuit", agent.getUnit());
					object.put("businessTypes", agent.getStype());
					object.put("dayMax", agent.getMax_times_byday());
					object.put("businessTypeName", agent.getSname());
					object.put("fingerprint", agent.getFingerprint());
					object.put("isOpenJm", CacheManager.getInstance().getSystemConfig("isOpenJm"));
					
				}
			}
		}
		
		
		this.getResponse("text/html").getWriter().println(object.toString());
		return null;
	}
	
	/*
	 * 判断取号次数
	 */
	public String countSet() throws Exception {
		HttpServletRequest request = getRequest();
		String isAnget = request.getParameter("isAnget");
		String agentId = request.getParameter("agentId");
		String typeId = request.getParameter("typeId");
		String count = request.getParameter("count");
		JSONObject object = new JSONObject();
		//不是代理人返回1个月可取号总数，与已取号次数
		String businessCount ="0";
		String numberSet="0";
	/*	WorkVO workVO = new WorkVO();
		workVO.setAgent_id(agentId);
		workVO.setWork_id(typeId);
		List<WorkVO> workList = workService.searchWork(workVO);
		if(null != workList && workList.size()>0){
			WorkVO retWork = workList.get(0);
			object.put("stroke", retWork.getStroke());
		}*/
		
		Number numVO = new Number();
//		numVO.setIDNumber(IDNumber);
		numVO.setIDNumberB(IDNumber);
		businessCount = numberService.getNotAgentByIdCardCount(numVO, CacheManager.getInstance().getOfDeptCache("deptCode"), CacheManager.getInstance().getOfDeptCache("deptHall"));
		object.put("businessCount", "businessCount");
		
		String countcs="0";
		if("0".equals(isAnget)){//isAnget是否代理人标记
			//根据代理人id与业务类型id获取代理人业务类型笔数
			WorkVO workVO = new WorkVO();
			workVO.setAgent_id(agentId);
			workVO.setWork_id(typeId);
			List<WorkVO> workList = workService.searchWork(workVO);
			if(null != workList && workList.size()>0){
				WorkVO retWork = workList.get(0);
				if(null != retWork.getStroke() || !"".equals(retWork.getStroke())){
					numberSet = retWork.getStroke();
				}
				if(null != count && Integer.parseInt(count) > Integer.parseInt(numberSet)){
					object.put("msg", "此业务最多能办理"+numberSet+"笔业务！！！");
					countcs="1";
				}else{
					object.put("msg", "true");
				}
			}else{
				object.put("msg", "不能办理此业务！！！");
				countcs="1";
				//代理人没有的业务，按照普通业务次数限制
			/*	numberSet = CacheManager.getInstance().getSystemConfig("numberSet");
				if(Integer.parseInt(businessCount) >= Integer.parseInt(numberSet)){
					object.put("msg", "本月取号次数大于最大次数，无法取号");
				}else{
					object.put("msg", "本月取号次数为"+numberSet+"笔，当前为第"+String.valueOf(Integer.parseInt(businessCount)+1)+"笔");
				}*/
			}
		}else{
			numberSet = CacheManager.getInstance().getSystemConfig("numberSet");
			if(Integer.parseInt(businessCount) >= Integer.parseInt(numberSet)){
				object.put("msg", "本月取号次数大于最大次数，无法取号！！！");
				countcs="1";
			}else{
				object.put("msg", "true");
				//object.put("msg", "本月取号次数为"+numberSet+"次，当前为第"+String.valueOf(Integer.parseInt(businessCount)+1)+"次");
			}
		}
		object.put("countcs", countcs);
		this.getResponse("text/html").getWriter().println(object.toString());
		return null;
	}
	
	/*
	 * 判断代理人人证对比结果
	 */
	public String rzdbjy() throws Exception {
		System.out.println("进入人证对比校验");
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String dbckz = cacheManager.getSystemConfig("rzdbckz");
		String IDNumber = request.getParameter("IDNumber");
		System.out.println("IDNumber="+IDNumber);
		List<Number> list = numberService.queryRzdbz(IDNumber);
		String result = "1";
		String msg = "";
		if (list.size()>0) {
			System.out.println(list.get(0).getRzdbz());
			int rzdbckz = Integer.parseInt(dbckz);
			int rzdbz = Integer.parseInt(list.get(0).getRzdbz());
			if (rzdbz >= rzdbckz) {
				result = "0";
				msg = "对比通过";
			}else {
				result = "1";
				msg = "对比未通过";
			}
		}else {
			result ="1";
			msg = "今天没有此代理人对比结果";
		}
		JSONObject object = new JSONObject();
		object.put("result", result);
		object.put("msg", msg);
		this.getResponse("text/html").getWriter().println(object.toString());
		return null;
	}
	/*
	 * 验证黑名单
	 */
	public String validateBlack() throws Exception {
		CacheManager cacheManager = CacheManager.getInstance();
		String isOpenHmd = cacheManager.getSystemConfig("isOpenHmd");
		String HmdCs = cacheManager.getSystemConfig("HmdCs");
		HttpServletRequest request = getRequest();
		String id = request.getParameter("IDNumber");
		boolean hmdflag = false;
		boolean addhmd = false;
		JSONObject objhmd = new JSONObject();
		
		try {
			if(isOpenHmd.equals("0")){
				Hmd hmd = hmdService.queryById(id);
				if(null != hmd){
					hmdflag = true;
				}else{
					int Quhaos=hmdService.queryHmd(id);
						if(null==HmdCs){
							HmdCs="0";
						}
					int q =  Integer.parseInt(HmdCs);
					if(Quhaos>=q){
						hmdflag = true;
						//自动添加黑名单
						addhmd = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		objhmd.put("hmdflag", hmdflag);
		objhmd.put("addhmd", addhmd);
		this.getResponse("text/html").getWriter().println(objhmd.toString());
		return null;
	}
	

	public String yzcfqh() throws Exception{
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String isyzcfqh = cacheManager.getSystemConfig("isyzcfqh");
		String id = request.getParameter("IDNumber");
		boolean cfqh = false;
		JSONObject objcfqh = new JSONObject();
		if ("0".equals(isyzcfqh)) {
			cfqh = false;
		}else{
			try {
				List<Number> list = numberService.yzcfqh(id);
				if(list.size()>0){
					cfqh = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		objcfqh.put("cfqh", cfqh);
		this.getResponse("text/html").getWriter().println(objcfqh.toString());
		return null;
	}
	/*
	 * 根据身份证查询指纹信息
	 */
	public String getZWBase64ByIdNumber() throws Exception {
		HttpServletRequest request = getRequest();
		String idNumber = request.getParameter("idNumber");
		boolean flag = false;
		JSONObject obj = new JSONObject();
		Number number = new Number();
		number.setIDNumber(idNumber);
		
		try {
			List<Number> list = numberService.getZhiWenByIdNumber(number);
			if(list.size()>0){
				flag = false;
			}else{
				flag = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.put("res", flag);
		this.getResponse("text/html").getWriter().println(obj.toString());
		return null;
	}
	/*
	 * 保存指纹信息
	 */
	public String saveZWBase64() throws Exception {
		HttpServletRequest request = getRequest();
		String idNumber = StringUtils.trimToEmpty(request.getParameter("idNumber"));
		String zwbase64 = StringUtils.trimToEmpty(request.getParameter("zwbase64"));
		boolean zwflag = false;
		JSONObject obj = new JSONObject();
		Number number = new Number();
		number.setIDNumber(idNumber);
		number.setZWBase64(new BASE64Decoder().decodeBuffer(zwbase64));
		
		try {
			//numberService.savePhoto(number, "");
			List<Number> list = numberService.getZhiWenByIdNumber(number);
			if(list.size()>0){
				numberService.updateZhiWen(number);
			}else{
				numberService.addZhiWen(number);
			}
			zwflag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.put("res", zwflag);
		this.getResponse("text/html").getWriter().println(obj.toString());
		return null;
	}
	
	/*
	 * 检查驾驶人
	 */
	public String checkJSR() throws Exception{
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String id = request.getParameter("IDNumber");
		boolean JSRflag = true;
		JSONObject objJSR = new JSONObject();
		if (!"0".equals(cacheManager.getSystemConfig("isUseInterface"))) {
			//调用02C06查询驾驶人基本信息
//			String rows[][] = new String[0][0];
//			rows[0][0] = id;
//			String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//			String[] result =  XMLUtils.readXML(TrffClient.write("02C06", strXML));
//			if ("A".equals(result[0])||"G".equals(result[0])) {
				JSRflag = true;
//			}
		}else{
			JSRflag = true;
		}
//		System.out.println("驾驶人身份证ID=="+id);
//		boolean JSRflag = true;
		System.out.println("JSRflag="+JSRflag);
		objJSR.put("JSRflag", JSRflag);
		
		this.getResponse("text/html").getWriter().println(objJSR.toString());
		return null;
	}
	
	/*
	 * 检查机动车
	 */
	public String checkJDC() throws Exception{
		HttpServletRequest request = getRequest();
		CacheManager cacheManager = CacheManager.getInstance();
		String chePaiType = request.getParameter("chePaiType");
		String chePaiNum = request.getParameter("chePaiNum");
		boolean JDCflag = true;
		JSONObject objJDC = new JSONObject();
		if (!"0".equals(cacheManager.getSystemConfig("isUseInterface"))) {
		//调用01C21读取机动车全项信息
//			String rows[][] = new String[0][1];
//			rows[0][0] = chePaiType;
//			rows[0][1] = chePaiNum;
//			String strXML = XMLUtils.createXML(XMLUtils.TYPE_WRITE, rows);
//			String[] result =  XMLUtils.readXML(TrffClient.write("01C21", strXML));
//			if ("A".equals(result[0])||"H".equals(result[0])||"J".equals(result[0])||"M".equals(result[0])||"R".equals(result[0])||"S".equals(result[0])) {
				JDCflag = true;
//			}
		}else{
			JDCflag = true;
		}
//		System.out.println("chePaiType=="+chePaiType+"+++chePaiNum=="+chePaiNum);
//		boolean JDCflag = true;
		System.out.println("JDCflag="+JDCflag);
		objJDC.put("JDCflag", JDCflag);
		this.getResponse("text/html").getWriter().println(objJDC.toString());
		return null;
	}
	
	
	
	/*
	 * 显示身份证照片
	 */
	public String uploadPic() throws Exception {
		HttpServletRequest request = getRequest();
		String isOpenPhoto = CacheManager.getInstance().getSystemConfig("isOpenPhoto");//是否开启读取照片
		String base64picPhoto = request.getParameter("base64picPhoto");
		System.out.println("base64picPhoto="+base64picPhoto.length());
		String cardNumber = request.getParameter("cardNumber");
		
		if("0".equals(isOpenPhoto)){
			deleteIcardPicFile(cardNumber);//删除所有文件
			ServletContext d=ServletActionContext.getServletContext();
		    String path=d.getRealPath("/");
			//byte[] bytes = new BASE64Decoder().decodeBuffer(base64picPhoto+2);
		    byte[] bytes=Base64Util.decode(base64picPhoto);
		/*	for(int k=0;k<bytes.length-2;k++){
				if(bytes[k]<0){
					bytes[k]+=256;
				}
			}
			bytes[bytes.length-2]=(byte) 255;
			bytes[bytes.length-1]=(byte) 217;*/
			
			//将照片保存到照片库
			if (bytes.length>0) {
				NumberIdPhoto num = new NumberIdPhoto();
				num.setNumberId(cardNumber);
				List<NumberIdPhoto> list =numberService.queryPhotoBy(num);
				if (list.size()>0) {
					num.setSfzphoto(bytes);
					num.setLrsj(DateUtils.dateToString());
					numberService.updateSfzPhoto(num);
				}else{
					num.setSfzphoto(bytes);
					num.setLrsj(DateUtils.dateToString());
					numberService.insertSfzPhoto(num);
				}
			}
			
	        // 生成jpeg图片
	        OutputStream out = new FileOutputStream(path+"IcardPic/"+cardNumber+".jpg");
	        out.write(bytes);
	        out.flush();
	        out.close();
		}
        this.getResponse("text/html").getWriter().println(isOpenPhoto.toString());
		return null;
	}
	public String uploadPic1() throws Exception {
		HttpServletRequest request = getRequest();
		String base64picPhoto = request.getParameter("base64picPhoto");
		System.out.println("base64picPhoto="+base64picPhoto.length());
		String cardNumber = request.getParameter("cardNumber");
		deleteIcardPicFile(cardNumber);//删除所有文件
		ServletContext d=ServletActionContext.getServletContext();
	    String path=d.getRealPath("/");
		byte[] bytes = new BASE64Decoder().decodeBuffer(base64picPhoto+2);
		for(int k=0;k<bytes.length-2;k++){
			if(bytes[k]<0){
				bytes[k]+=256;
			}
		}
		bytes[bytes.length-2]=(byte) 255;
		bytes[bytes.length-1]=(byte) 217;
		
		//将照片保存到照片库
		if (bytes.length>0) {
			NumberIdPhoto num = new NumberIdPhoto();
			num.setNumberId(cardNumber);
			if (!"".equals(base64picPhoto) && null != base64picPhoto) {
				num.setBase64(base64picPhoto);
			}else {
				num.setBase64(null);
			}
			List<NumberIdPhoto> list =numberService.queryPhotoBy(num);
			if (list.size()>0) {
				num.setSfzphoto(bytes);
				num.setLrsj(DateUtils.dateToString());
				numberService.updateSfzPhoto(num);
			}else{
				num.setSfzphoto(bytes);
				num.setLrsj(DateUtils.dateToString());
				numberService.insertSfzPhoto(num);
			}
		}
		
        // 生成jpeg图片
        OutputStream out = new FileOutputStream(path+"IcardPic/"+cardNumber+".jpg");
        out.write(bytes);
        out.flush();
        out.close();
        this.getResponse("text/html").getWriter().println('1');
		return null;
	}
	
	/*
	 * 删除对应照片文件
	 */
	public String deleteIcardPicFile(String cardNumber) throws Exception {
		ServletContext d=ServletActionContext.getServletContext();
	    String path=d.getRealPath("/");
		File file =new File(path+"IcardPic/");
		if (!file.isDirectory()) { 
			file.delete(); 
		}else if (file.isDirectory()) { 
			String[] filelist = file.list(); 
			for (int i = 0; i < filelist.length; i++) { 
				File delfile = new File(path + "\\IcardPic\\" + filelist[i]); 
				if (!delfile.isDirectory()) { 
					delfile.delete(); 
				} 
				else if (delfile.isDirectory()) { 
					deleteIcardPicFile(path + "\\IcardPic\\" + filelist[i]); 
				}
			}
			//file.delete(); 
		}
		return null;
	}
	/*
	 * 是否取号
	 */
	public String checkDemandNum() throws Exception{
		HttpServletRequest request = this.getRequest();
		String cardNo = request.getParameter("cardNo");
		
		List<Number> list = numberService.getValueRecardbyIdnumber(cardNo,"");
		boolean falg = false;
		if (list.size()>0) {
			falg = true;
		}
		getResponse().getWriter().print(falg);
		return null;
	}
	/*
	 * 打印身份证所需照片
	 */
	public String printIdNumPic() throws Exception {
		HttpServletRequest request = getRequest();
		String base64picPhoto = request.getParameter("base64picPhoto");
		String cardNumber = request.getParameter("cardNumber");
			deleteIcardPicFile(cardNumber);//删除所有文件
			ServletContext d=ServletActionContext.getServletContext();
		    String path=d.getRealPath("/");
			byte[] bytes = new BASE64Decoder().decodeBuffer(base64picPhoto+2);
			for(int k=0;k<bytes.length-2;k++){
				if(bytes[k]<0){
					bytes[k]+=256;
				}
			}
			bytes[bytes.length-2]=(byte) 255;
			bytes[bytes.length-1]=(byte) 217;
	        // 生成jpeg图片
	        OutputStream out = new FileOutputStream(path+"IcardPic/"+cardNumber+".jpg");
	        out.write(bytes);
	        out.flush();
	        out.close();
        this.getResponse("text/html").getWriter().println(0);
		return null;
	}
	
	
	
	
}