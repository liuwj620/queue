<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="ec" uri="http://www.ecside.org"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>">
    
    <title>照片详细信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="css/mainframe.css" rel="stylesheet" type="text/css" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	
	-->
	<link type="text/css" href="css/zzsc.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
$(function(){ 
	$('li').hover(function() {
	    $(this).addClass('on');
	    var wl = $(this).find('img').attr('width');
	    if (wl < 190) {
	        $(this).find('.in').css('left', '0')
	    } else {
	        $(this).find('.in').css('left', -wl / 4)
	    }
	},
	function() {
	    $(this).animate({
	        height: "200px"
	    },
	    100).removeClass('on');
	    $(this).find('.in').css('left', '0')
	});
})
</script>

  </head>
  
  <body>
  <ul class="box01">
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
		
			<tr><td height="5" colspan="3"></td></tr>
			<tr>
				<td width="7" height="51"><img src="images/t-1.jpg" width="7" height="51"></td>
				<td height="51" valign="middle" background="images/t-2.jpg">
					<span style="font-family: '黑体', '宋体'; font-size: 18px; color: #fff; padding-left: 15px; letter-spacing: 10px; letter-spacing: 10px;">照片详细信息</span>
				</td>
				<td width="3" height="51"><img src="images/t-3.jpg" width="8" height="51"></td>
			</tr>
			<tr>
				<td width="7" background="images/t-4.jpg">&nbsp;</td>
				<td height="100" style="text-align: center" valign="middle" bgcolor="#f7f7f7">
					<form action="" name="form1" method="post">
						<table id="myTable" width="60%" border="0" cellpadding="1" cellspacing="1" class="table_back">
							<tr>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">员工姓名：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD">&nbsp;&nbsp;&nbsp;${number.xm}</td>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">业务类型：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD">&nbsp;&nbsp;&nbsp;${number.typeName}</td>
				    		</tr>
							<tr>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">顺 序 号：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD">&nbsp;&nbsp;&nbsp;${number.serialNum}</td>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">证件号码：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD">&nbsp;&nbsp;&nbsp;${number.IDNumber}</td>
				    		</tr>
				    		<tr>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">取号时间：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD">&nbsp;&nbsp;&nbsp;${number.enterTime}</td>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">叫号时间：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD">&nbsp;&nbsp;&nbsp;${number.beginTime}</td>
				    		</tr>
				    	
				    		<tr>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1" rowspan="4">业务办理照片：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD" rowspan="4">
				    				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table_back">
				    					<tr>
				    						<td width="100%" height="100%" bgcolor="#F1F9FD">
				    							<img alt="取号人照片(双击放大，单击复原)" src="getVQhPhoto.action?id=${number.id}" onerror="this.src='./images/zwtp.jpg'" style="width:180px;height:160px;border:2px;" ondblclick="this.style.zoom='2'" onclick="this.style.zoom='1'">
				    						</td>
				    					</tr>
				    				</table>
				    			</td>
				    			<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1" rowspan="4">身份证照片：</td>
				    			<td width="35%" height="35" bgcolor="#F1F9FD" rowspan="4">
				    				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table_back">
				    					<tr>
				    						<td width="100%" height="100%" bgcolor="#F1F9FD">
				    							<img name="pic" alt="身份证照片(双击放大，单击复原)" src="getVSfzPhoto.action?id=${number.id}" onerror="this.src='./images/zwtp.jpg'" style="width:180px;height:160px;border:2px;" ondblclick="this.style.zoom='2'" onclick="this.style.zoom='1'">
				    						</td>
				    					</tr>
				    				</table>
				    			</td>
				    		</tr>
						</table>
					</form>
					<input name="bt2" class="button" type="button" value=" 返回 " onClick="window.history.back(-1);" />
					<br>
				</td>
				<td width="8" background="images/t-5.jpg">&nbsp;</td>
			</tr>
			<tr>
				<td><img src="images/t-6.jpg" width="7" height="11"></td>
				<td height="11" background="images/t-7.jpg"></td>
				<td><img src="images/t-8.jpg" width="8" height="11"></td>
			</tr>
		</table>
		<div id="divPop" style="background-color:#f0f0f0; display:none;">
		<iframe id="divIframe" style="position:absolute;z-index:9;background-color:#f0f0f0;" frameborder="0"></iframe>
		</div>
		</ul>
		<div style="clear:both;"></div>
	</body>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/jquery.js"></script>
</html>
