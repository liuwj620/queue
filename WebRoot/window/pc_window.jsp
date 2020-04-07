<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="/webwork" prefix="ww"%>
<%@ page import="com.suntendy.queue.util.*"%>
<%
	String ip = request.getHeader("x-forwarded-for");
	if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("Proxy-Client-IP");
	}
	if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("WL-Proxy-Client-IP");
	}
	if (null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getRemoteAddr();
	}
	//防止js缓存
    String jsVer = new DateUtils().jsVer();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>用户办理信息显示窗口</title>
	<link rel="stylesheet" type="text/css" href="/queue/queue/css/fetch.css" />
	<link rel="stylesheet" type="text/css" href="/queue/window/css/PcWinCss.css" />
	<script src="js/jquery-3.3.1.js" type="text/javascript"></script> 
	<script type="text/javascript">
	    
	   $(document).ready(function() { 
	       $('html, body').animate({scrollTop:0}, 'slow');     
	    });  
	    
	    window.onload = function(){
			var url = '/queue/number/autoLogin.action';
		    var operatorInfo = {loginIP:'<%=ip%>'};
		    $.post(url, operatorInfo, function(datas) {
				var jsonReturn = JSON.parse(datas);
	           	if (jsonReturn.bj) {
		        	aotologin(datas);
	           	} else {
	               window.alert("登录失败");
	           	}
	        });
			
		}
		
	
	
	
	
		//	var obj;//加载手写板
		//	penwidth = 3;
		//	window.onload = function(){
       //           obj = document.getElementById("HWPenSign"); 
       //           obj.HWSetBkColor(0xE0F8E0);  
		//  		  obj.HWSetCtlFrame(2, 0x000000);
		//  		  obj.HWInitialize();
        //    }
        </script>
         <script language="javascript">
		// 	function codefans(){
		// 	var box=document.getElementById("test");
		// 	box.style.display="none"; 
		// 	}
			// setTimeout("codefans()",5000);//2秒，可以改动
		 </script>
	
	<style type="text/css">
	.style1{
			width: 800px;
			height: 200px;
			margin: 0px auto;
			border:1px solid #96C2F1;
			background-color: #EFF7FF;
			font-size: 38px;
			text-algin:center;
			position:absolute;
			margin-top:10px;
			left:300px;
			top:220px;
			padding-top:5px;
			}
			.style1 h5{
			margin: 1px;
			background-color: #B2D3F5;
			height: 24px;
			font-size: 50px
			}
	.btn_ywlx{
			display:block;
			width:340px;
			height:65px;
			background:url(/queue/images/queue/btn_ywlx.gif) no-repeat;
			color:#fff;
			font-size:30px;
			font-weight:bold;
			text-align:left;
			padding:15px 0 0 80px;
			text-decoration:none;
			}
			.btn_ywlx:hover{
			background:url(/queue/images/queue/btn_ywlx_h.gif) no-repeat;
			width:338px;
			height:63px;
			padding:17px 0 0 82px;
			text-decoration:none;
			
			}
			.btn_ywlx:hover{
			padding:15px 0 0 80px;
			background:url(/queue/images/queue/btn_ywlx.gif) no-repeat;
			}
			.btn_ywlx:active{
			padding:17px 0 0 82px;	
			background:url(/queue/images/queue/btn_ywlx_h.gif) no-repeat;
			}
	
	</style>
	</head>
	<body scroll="no" ondragstart="return false">

	<input type="hidden" id="autopj" value="1" />
	<input type="hidden" id="base64Photo" name="base64Photo"/>
	<audio id="please" src="/queue/plugs/voice/pleaseEval.wav"></audio>
	<audio id="thanks" src="/queue/plugs/voice/thanks.wav"></audio>  
	<object id="jsrzP" classid="clsid:C3FF8650-C1AC-4E41-9474-6C27A58502D6" width="0" height="0" align="middle" CODEBASE="/queue/plugs/camera/zdywzp.CAB#version=1.0.0.3"></object>
		<div style="height: 0;width: 0" style="display:none">
			<input type="hidden" id="operNum" name="operNum" />
			<input type="hidden" id="loginIP" name="loginIP" />
			<input type="hidden" id="base64Code" name="base64Code" />
		</div>
		
		<div id="retMsg"></div>
		<img  id="user_pic" src=""/>
		
	    <!-- 欢迎页 -->
		<div id="_welcome" class="outer2">
		<div class="inner jh_qz_welcome"></div>
			<!-- <div class="top"></div>
			<div class="inner jh_qz_welcome"></div>
			<div class="bottom"></div> -->
		</div>
		
		<!-- 暂停服务 -->
		<div id="_pause" class="outer" style="display:none">
			<!-- <div class="top"></div>
			<div class="inner jh_qz_pause"></div>
			<div class="bottom"></div> -->
			<div class="inner jh_qz_pause"></div>
		</div>
		
		
	<!-- 机动车信息 -->
	<div id="_jdc" class="message" style="display:none">
		<table class="messagetable">
			<tr>
				<td
					style="background-color: powderblue;height: 30px;width: 100%;font-size: 40px;font-weight:800;"
					colspan="4">机动车基本信息</td>
			</tr>
			<tr>
				<td style="background-color: powderblue;">号牌种类</td>
				<td id="hpzl"></td>
				<td style="background-color: powderblue;">号牌号码</td>
				<td id="hpzm"></td>
			</tr>
			<tr>
				<td style="background-color: powderblue;">车辆类型</td>
				<td id="cllx"></td>
				<td style="background-color: powderblue;">车辆品牌</td>
				<td id="clpp1"></td>
			</tr>
			<tr>
				<td style="background-color: powderblue;">定检日期</td>
				<td id="djrq"></td>
				<td style="background-color: powderblue;">检验有效期止</td>
				<td id="yxqz"></td>
			</tr>
			<tr>
				<td style="background-color: powderblue;">状态</td>
				<td id="zt"></td>
				<td style="background-color: powderblue;"></td>
				<td></td>
			</tr>
		</table>
	</div>

	<!-- 驾驶人信息 -->
	<div id="_jsr" class="message" style="display:none">
		<table class="messagetable">
			<tr>
				<td
					style="background-color: powderblue;height: 30px;width: 100%;font-size: 40px;font-weight:800;"
					colspan="4">驾驶人基本信息</td>
			</tr>
			<tr>
				<td style="background-color: powderblue;">姓名</td>
				<td id="xm"></td>
				<td style="background-color: powderblue;">邮政编码</td>
				<td id="lxzsyzbm"></td>
			</tr>
			<tr>
				<td colspan="1" style="background-color: powderblue;">联系地址</td>
				<td colspan="3" id="lxzsxxdz"></td>
			</tr>
			<tr>
				<td style="background-color: powderblue;">手机号码</td>
				<td id="sjhm"></td>
				<td style="background-color: powderblue;">固定电话</td>
				<td id="lxdh"></td>
			</tr>
			<tr>
				<td colspan="1" style="background-color: powderblue;">办理业务</td>
				<td colspan="3" id="binss"></td>
			</tr>
		</table>
	</div>
		
		<div id="data_show" class="outer" style="display:none">
			<div class="inner jh_qz">			
					<!-- 显示时间 -->
	            <div id="showtime" class="showtime" style="position:absolute;top:655px;left:160px;">
	            	<label id="currentime" style="font-size:20px;font-weight:bold;font-size:32px;color:red"></label>
	            	<script language=JavaScript>
	            		setInterval(function(){
	            			today=new Date();
	            			var currentime=document.getElementById("currentime");
	            			
	            			currentime.innerHTML="当前时间:  "+today.getFullYear()+"年"+(today.getMonth()+1)+"月"+today.getDate()+"日  "+today.getHours()+"时"+today.getMinutes()+"分"+today.getSeconds()+"秒";
	            		},1000)
	            				
	            				
	            			
	      			</script>
	      			
	            </div>
			
				<!-- 显示当前时间 -->
	            <div id="showtime" class="showtime" style="position:absolute;top:660px;left:250px;">
	            	<label id="currentime" style="font-size:20px;font-weight:bold"></label>
	            	<script language=JavaScript>
	            		setInterval(function(){
	            			today=new Date();
	            			var currentime=document.getElementById("currentime");
	            			
	            			currentime.innerHTML="当前时间:  "+today.getFullYear()+"年"+(today.getMonth()+1)+"月"+today.getDate()+"日  "+today.getHours()+"时"+today.getMinutes()+"分"+today.getSeconds()+"秒";
	            		},1000)
	      			</script>
	      			
	            </div>
			
				<div id="div1" class="div1">
					<table class="tab1" id="tipTab">
						<caption id="_showTitle"></caption>
					</table>
					<br/>
					<table class="tab1" id="wfxxTab">
						<caption id="_showWfxxTitle"></caption>
					</table>
				</div>
				<div class="mask" id="mask_tip" align="center" style="width: 820px;height: 591px;">
				<!-- 珠海改动--添加了一个id属性 -->
	            	<img id="imgzt" style="width: 815px;height: 591px;" src="/queue/images/welcome.jpg" />
	            </div>
	            
				<%-- <div class="div2">
					<marquee direction="left" scrollamount="3">${allMsg}</marquee>
				</div> --%>
				
				
				
				<div class="div8" id="div8">
					<c:if test="${publicAd.msg_state eq 1}">
						<marquee id="myMarq" direction="up" height="400px;" scrollamount="3">${publicAd.message}</marquee>
					</c:if>
					<c:if test="${publicAd.msg_state eq 0}">
						<div id="myMarq"  height="400px;" >${publicAd.message}</div>
					</c:if>
				    
				</div>
				<div class="div3">
					<p><img id="tootip_img" src="/queue/images/jgzp.jpg" onerror="this.src='/queue/images/jgzp.jpg'" /><br /><span class="name" style="font-size: 50px" id="police_name"></span><!-- 字体大小改为50px原大小为30px --></p>
					<table class="tab2">
						<tr>
							<th width="30%" style="border-top:1px solid #C7B38E;font-size: 25px;">工号</th>
							<td width="70%" style="border-top:1px solid #C7B38E;font-size: 25px" id="police_code"></td>	
						</tr>
						<tr>
							<th style="font-size: 25px">座右铭</th>
							<td><div class="div_zym" style="font-size: 25px" id="div_zym"></div></td>	
						</tr>
						
<!--						<tr><th>监督电话</th><td>88414036</td></tr>-->
					</table>
					<!-- <br/>
					<br/>
					<br/> -->
					
						<div id="div_dqhm" style="z-index: 8;"></div>
   						<div id="quhaophoto" style="width:360px;height:180px;border:2px;position:relative;left:-3px;top:1px;background-color: #EBDBC4;display: none;"></div>
				</div>
				<!-- 不弹确认评价框-->
				<ww:if test="${(pjtype eq 0) &&(qrpjtype eq 1)}">
				<%-- 	<div class="div4" id="_eval" style="display:none">
						<c:forEach var="item" items="${evalueList}">
							<c:if test="${item.state=='1'}">
								<a href="#" class="${item.evalueclass }" onclick="doEval('${item.id}','1');return false;">${item.name }</a>
							</c:if>
						</c:forEach>
					</div> --%>
				</ww:if >
				<ww:elseif test="${(pjtype eq 1)  &&(qrpjtype eq 1)}">
					<div id="_eval" class="div_pop" style="width:60%;height:300px;background-color:#008DD4;display:none">
						<a><font size="50px;"><b>评价区域</b></font></a>
						<ul>
							<c:forEach var="item" items="${evalueList}">
								<c:if test="${item.state=='1'}">
									<a href="#" class="${item.evalueclass }" onclick="doEval('${item.id}','1');return false;">${item.name }</a>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</ww:elseif>
				<!-- 不弹确认评价框 -->
				<div class="PJFG" style="position: absolute; left: 903px; top: 506px;background-color:white;width: 500px;height: 300px; z-index: -1;">
					</div>
				<!-- 弹确认评价框 -->
				<ww:if test="${(pjtype eq 0) &&(qrpjtype eq 0)}">
					<%-- <div class="div4" id="_eval" style="display:none">
						<c:forEach var="item" items="${evalueList}">
							<c:if test="${item.state=='1'}">
								<a href="#" class="${item.evalueclass }" onclick="confirmBox('${item.id}','1');return false;">${item.name }</a>
							</c:if>
						</c:forEach>
					</div> --%>
				</ww:if>
				<ww:elseif test="${(pjtype eq 1) &&(qrpjtype eq 0)}">
					<div id="_eval" class="div_pop" style="width:60%;height:300px;background-color:#008DD4;display:none">
						<a><font size="50px;"><b>评价区域</b></font></a>
						<ul>
							<c:forEach var="item" items="${evalueList}">
								<c:if test="${item.state=='1'}">
									<a href="#" class="${item.evalueclass }" onclick="confirmBox('${item.id}','1');return false;">${item.name }</a>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</ww:elseif>
				<input type="hidden" value="" id="hidden" name="hidden"/>
				<input type="hidden" value="" id="hidden2" name="hidden2"/>
				
				<!-- <input type="button" value="测试" onclick="confirmBox()"/>  -->
				<div id="test" class="div_pop" style="top:300px;left:400px;width:40%;height:200px;background-color:#008DD4;display:none"> 
					<a><font size="25px;"><b>确认评价结果</b></font></a><br/><br/><br/>
						<img src="images/queue/ok.jpg" onclick="ok()"/>
						 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<img src="images/queue/cancel.jpg" onclick="cancle();"/>
				</div>
				<!-- 弹确认评价框 -->
				
				<div id="pop" class="div_pop" style="display:none">
					<h1>请选择不满意原因</h1>
					<ul id="reasons"></ul>
				</div>
				<div id="next" class="style1" style="display:none"></div>
			</div>
		</div>
		
	<script type="text/javascript" src="/queue/js/ajax.js?ver=<%=jsVer%>"></script>
    <script type="text/javascript" src="/queue/js/jquery.js?ver=<%=jsVer%>"></script>
	<script type="text/javascript" src="/queue/dwr/util.js?ver=<%=jsVer%>"></script>
	<script type="text/javascript" src="/queue/dwr/engine.js?ver=<%=jsVer%>"></script>
	<script type="text/javascript" src="/queue/dwr/interface/JspRegister.js?ver=<%=jsVer%>"></script>
	<script type="text/javascript" src="/queue/window/js/qrcode.js?ver=<%=jsVer%>"></script>
	<script type="text/javascript" src="/queue/window/js/pc_window.js?ver=<%=jsVer%>"></script>
	<script type="text/javascript">JspRegister.register("<%=ip%>", "S");</script>
	
<!-- 	<div style="width:100%;padding-top:300px;background:url(../images/queue/bg_jh_qz_all.jpg) repeat;">
		<object id="HWPenSign" name="HWPenSign" classid="clsid:E8F5278C-0C72-4561-8F7E-CCBC3E48C2E3" width="600" height="300"></object>
	</div> -->
	
	</body>
</html>