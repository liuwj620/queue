<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
  <html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="ec" uri="http://www.ecside.org"%>
<%@ taglib uri="/webwork" prefix="ww"%>
<%@ page isELIgnored="false"%> 
<html>
	<head>
		<title>员工信息管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="/queue/css/mainframe.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/queue/js/jquery.js"></script>
		<script type="text/javascript" src="/queue/js/ajaxfileupload.js"></script>
		<script type="text/javascript" src="/queue/js/cutpic.js"></script>
	</head>
	<body>
		<form action="" name="form1" method="post" enctype="multipart/form-data">
			<input type="hidden" id="flag_id" name="flag_id" value="${id}"/>
          	<input type="hidden" size="4" id="flag" name="flag" value="0" />
          	<input type="hidden" size="4" id="filename" name="filename"/>
			<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td height="5" colspan="3"></td>
				</tr>
				<tr>
					<td width="7" height="51"><img src="/queue/images/t-1.jpg" width="7" height="51"></td>
				    <td height="51" valign="middle"  background="/queue/images/t-2.jpg">
				    	<span style="font-family:'黑体', '宋体';font-size:18px; color:#fff;padding-left:15px;letter-spacing: 10px;letter-spacing: 10px;">员工管理</span>
				    </td>
				    <td width="3" height="51"><img src="/queue/images/t-3.jpg" width="8" height="51"></td>
				</tr>
				<tr>
					<td width="7" background="/queue/images/t-4.jpg">&nbsp;</td>
					<td height="100" style="text-align: center" valign="middle" bgcolor="#f7f7f7">
						<fieldset>
							<legend class="leg">
								员工信息
							</legend>
							<table width="50%" border="0" cellpadding="1" cellspacing="1" class="table_back">
								<tr>
									<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">
										账号
									</td>
									<td width="80%" height="35" bgcolor="#F1F9FD">
										&nbsp;
										<input type="text" id="code" name="code" style="width: 100px" value="${employee.code }" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">
										姓名
									</td>
									<td width="80%" height="35" bgcolor="#F1F9FD">
										&nbsp;
										<input name="name" id="name" maxlength="15" style="width: 100px" value="${employee.name }" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">
										关联ip
									</td>
									<td width="80%" height="35" bgcolor="#F1F9FD">
										&nbsp;
										<input type="text" id="loginIp" name="loginIp" value="${employee.loginIp }" style="width: 300px" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">
										双屏ip
									</td>
									<td width="80%" height="35" bgcolor="#F1F9FD">
										&nbsp;
										<input type="text" id="WSIp" name="WSIp" value="${employee.WSIp }" style="width: 300px" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">
										座右铭
									</td>
									<td width="80%" height="35" bgcolor="#F1F9FD">
										<textarea rows="5" style="width: 260px;height: 150px" name="comments">${employee.comments }</textarea>
									</td>
								</tr>
								<tr>
									<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">
										照片
									</td>
									<td width="80%" height="35" bgcolor="#F1F9FD">
									<input type="file" name="file1" id="file1" title="选择照片" /><br/>
									<img id="oldimg" src="employee!getImg.action?id=${id }" onerror="this.src='../images/jgzp.jpg'" 
											 style="width: 130px;height: 150px;border: 2px solid #B1A080;" />&nbsp;&nbsp;
											<!--  <a href="javascript:void(0);" onclick="javascript:PopUpWindow('user/uploadimage.jsp?id=${id }',100,100,600,500);">更换照片</a> -->
									</td>
								</tr>
							</table>
						</fieldset>
						<br/>
						<input name="bt1" class="button" type="button" value="提 交" onClick="updateStaff()" />
						<input name="bt2" class="button" type="button" value="返 回" onClick="window.history.back(-1);" />
						<br/>
						<br/>
					</td>
					<td width="8" background="/queue/images/t-5.jpg">&nbsp;</td>
				</tr>
				<tr>
				    <td ><img src="/queue/images/t-6.jpg" width="7" height="11"></td>
				    <td height="11" background="/queue/images/t-7.jpg"></td>
				    <td ><img src="/queue/images/t-8.jpg" width="8" height="11"></td>
				  </tr>
			</table>
		</form>
		
		<script type="text/javascript">
			function updateStaff(){
				if($("#name").val()==''){
					alert('请填写姓名！');
					$('#name').select();
					return false;
				}
				form1.action = "employee!updateStaff.action";
				form1.submit();
			}
		</script>
	</body>
</html>