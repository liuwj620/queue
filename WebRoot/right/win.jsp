<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.suntendy.queue.util.*"%>
<%
	String clientIp = request.getHeader("x-forwarded-for");//客户端IP
	if (null == clientIp || clientIp.length() == 0
			|| "unknown".equalsIgnoreCase(clientIp)) {
		clientIp = request.getHeader("Proxy-Client-IP");
	}
	if (null == clientIp || clientIp.length() == 0
			|| "unknown".equalsIgnoreCase(clientIp)) {
		clientIp = request.getHeader("WL-Proxy-Client-IP");
	}
	if (null == clientIp || clientIp.length() == 0
			|| "unknown".equalsIgnoreCase(clientIp)) {
		clientIp = request.getRemoteAddr();
	}

	String serverIP = "127.0.0.1:8080"; //服务端IP 192.168.1.105:8088 10.118.243.58:8080 10.119.20.103:8080
	String serverDwrPath = "//" + serverIP + "/queue/dwr";
	//防止js缓存
	String jsVer = new DateUtils().jsVer();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>小窗口</title>
<link rel="stylesheet" type="text/css" href="css/styles.css">
<script type="text/javascript">
		serip = "<%=serverIP%>";
		  $(document).ready(function() {    
		       $('html, body').animate({scrollTop:0}, 'slow');     
		    });  
		</script>
</head>

<body>
	<input type="hidden" id="clientIp" name="clientIp"
		value="<%=clientIp%>" />

	<div id="callCon" class="div1">
			<div class="divtop">排队控制平台</div>
		<table class="tad1">
			<tr>
				<td style="width:40%;text-align: right;font-size:30px">警号:</td>
				<td style="text-align: left;"><INPUT class=login_input id="jh"></td>
			</tr>
			<tr>
				<td style="text-align: right;font-size:30px;">姓名:</td>
				<td style="text-align: left;"><INPUT class=login_input id="name"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="button" class="btn_tj" value=""
					onclick="login()"></td>
			</tr>
			<tr id="rzhy" style="display: none;">
				<td colspan="2" style="text-align: right;"><input type="button"  value="人证对比"
					onclick="rzdb()"></td>
			</tr>
		</table>
	</div>

	<div id="newnotice">
		<!-- <p id="jhse" name="showtime"></p> -->
		<!--<p>
			<span class="title" id="jhse" name="showtime">呼&nbsp;号&nbsp;台</span>
			 <span id="bts"> <label class="button" id="tomin" title="最小化"
				onclick="sw.minOrMax(this)"></label>
			</span> 
		</p>-->
		<div id="noticecon">
			<div id="changebtn" style="margin-top: 6px;"></div>
			<!--需要添加的部分开始-->
			<div style="color: #07338b;text-align:left">
				&nbsp;当前办理【<b id="queMsg" style="font-size:15px;"></b>】 <span
					id="spanTotal"></span>
			</div>
			<!--需要添加的部分结束-->
			<div class="iframe_float_line"></div>
			<!--需要添加的部分开始-->
			<!-- 
				<OBJECT id="PJQ" CLASSID="CLSID:5DFC144D-86AC-48B4-8DF9-5F4C25286F65" codebase="http://<%=serverIP%>/queue/plugs/cab/PJControlX.CAB#version=1,0,0,1" width="0" height="0"></OBJECT>
				<OBJECT id="XJPJQ"  width="0" height="0"  classid="CLSID:0FB5A45F-6829-4A53-860B-F867C6064D4D"></OBJECT>
				 -->
			<!--需要添加的部分结束-->
		</div>
	</div>

</body>
<script type="text/javascript"
	src="//<%=serverIP%>/queue/right/js/jquery.js?ver=<%=jsVer%>"></script>
<script type="text/javascript"
	src="<%=serverDwrPath%>/util.js?ver=<%=jsVer%>"></script>
<script type="text/javascript"
	src="<%=serverDwrPath%>/engine.js?ver=<%=jsVer%>" id="ldEngine"></script>
<script type="text/javascript"
	src="<%=serverDwrPath%>/interface/JspRegister.js?ver=<%=jsVer%>"></script>
<script type="text/javascript"
	src="//<%=serverIP%>/queue/right/js/queue_window.js?ver=<%=jsVer%>"></script>
<!--    <script type="text/javascript">sw.login("012371", "<%=clientIp%>", "<%=serverIP%>", "<%=serverDwrPath%>");</script>-->


<!-- 需要添加的部分开始 -->
<!-- 
	<script language="javascript" for="PJQ" event="RecvEvaluation(address,result)">
		PJQ.ClosePort();
	    window.clearTimeout(sw.evalUSBSetTimeouResult);
		sw.submitEval({flag: 1, evaluResult: result,typeFlag: 1});
	</script>
	 -->
<!--需要添加的部分结束-->
<script type="text/javascript">
	var picPhoto="";
	
	function rzdb(){
		var url = 'http://<%=serverIP%>/queue/number/Rzdbts.action';
		 var operatorInfo = {operNum:$("#jh").val(), loginIP:'<%=clientIp%>'};
		 $.post(url, operatorInfo, function(data) {
				if (eval(data)) {
	           	} else {
	               window.alert("人证对比功能【未开启】！需要使用请联系管理员！");
	           	}
        });
	}
	
	function login() {
		var jh =$.trim($("#jh").val());
		var name =$.trim($("#name").val());
		//var name =$("#name").val();
		if("undefined" == typeof(jh) || "" == jh || null == jh){
			alert("工号不能为空");
			$("#jh").val("");
			return false;
		}
		if("undefined" == typeof(name) || "" == name || null == name){
			alert("姓名不能为空");
			$("#name").val("");
			return false;
		}
		
		$("#rzhy").css("display","");
		//$("#callCon").css("display","none");
		$("#newnotice").css("display","");
		displayWin();
	    var url = 'http://<%=serverIP%>/queue/number/loginWS.action';
	    var operatorInfo = {operNum:$("#jh").val(), operName:encodeURI($("#name").val()), operDept:$("#dept").val(), loginIP:'<%=clientIp%>'};
	    $.post(url, operatorInfo, function(data) {
           	if (eval(data)) {
	        	window.alert("登录成功");
	           	sw.login($("#jh").val(), "<%=clientIp%>", "<%=serverIP%>", "<%=serverDwrPath%>");
           	} else {
               window.alert("登录失败");
           	}
        });
	}
	
	function sveinRet(data){
		var ret=data.retCode;
		if(ret==90){
			alert("人证对比【成功】");
		}else{
			alert("人证对比【不一致】");
		}
	}
	
	function send() {
     <%--    var soapMessage ='<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">' +
			'<SOAP-ENV:Header/>' +
			'<SOAP-ENV:Body>' +
				'<ns:showScreenInfo xmlns:ns="http://webservice.queue.suntendy.com/">' +
					'<loginIP><%=clientIp%></loginIP>' +
					'<tableTitle>123123</tableTitle>' +
					'<screenInfo>{"cm":"5555555"}</screenInfo>' +
				'</ns:showScreenInfo>' +
			'</SOAP-ENV:Body>' +
		'</SOAP-ENV:Envelope>'; --%>
		
        var soapMessage ='<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">' +
			'<SOAP-ENV:Header/>' +
			'<SOAP-ENV:Body>' +
				'<ns:queryCondition xmlns:ns="http://webservice.queue.suntendy.com/">' +
					'<sfzmhm>11111</sfzmhm>' +
					'<lsh></lsh>' +
				'</ns:queryCondition>' +
			'</SOAP-ENV:Body>' +
		'</SOAP-ENV:Envelope>';
		
	    $.ajax({
	        type: 'POST',
	        cache: false,
	        async: false,
	        contentType: 'text/xml; charset="UTF-8"',
	        url: 'http://<%=serverIP%>/queue/ws/OpenInter',
	        data: soapMessage,
	        dataType: "xml",
	        success: function(req) {
	           alert(true);
	        }
	    });
	}

	</script>
<style type="text/css">
<!--

<
style type ="text/css"><!--* {
	margin: 0;
	padding: 0;
	border: 0;
}

BODY {
	FONT-SIZE: 11px;
	font-family: "宋体";
	FONT-FAMILY: Verdana, Geneva, Arial, Helvetica, sans-serif;
	background-image: url(images/login_bk.jpg);
}

INPUT {
	FONT-SIZE: 11px;
	FONT-FAMILY: Verdana, Geneva, Arial, Helvetica, sans-serif
}

TD {
	FONT-SIZE: 11px;
	FONT-FAMILY: Verdana, Geneva, Arial, Helvetica, sans-serif
}

.login_input {
	BORDER: #9BADB8 1px solid;
	FONT-SIZE: 16pt;
	HEIGHT: 18px;
	TEXT-DECORATION: none;
	tabIndex:1;
	size:30;
	width: 200px;
	height: 30px;
	border-bottom-color: blue;
}

.btn_tj {
	background: url(images/qd_but2.gif) no-repeat;
	width: 67px;
	height: 26px;
	cursor: hand;
}

.btn_cz {
	width: 47px;
	height: 20px;
	background: url(images/qx_but.gif) no-repeat;
	margin-left: 20px;
	cursor: hand;
}

.tad1{
	width: 600px;
	height: 200px;
	padding:0px auto;
	border:solid #4d7fbc 1px;
	text-align: center;
}

.divtop{
	position:relative;
	background-color:#4d7fbc; 
	width:600px;
	height: 80px;
	font-size:50px;
	text-align: center;
}

.div1{
	position:absolute;
	background-color:white;
	margin:200px 650px;
}
-->
</style>

</html>