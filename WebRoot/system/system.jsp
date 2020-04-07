<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/webwork" prefix="ww"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>系统设置</title>
		<link href="css/mainframe.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="currentDept()">
	<EMBED id="lady" SRC="/queue/plugs/voice1/1window.wav"  autostart=false hidden=true loop=0></EMBED>
	<EMBED id="lady2" SRC="/queue/plugs/voice2/1window.wav"  autostart=false hidden=true loop=0></EMBED>
	<EMBED id="man" SRC="/queue/plugs/voice3/1window.wav" autostart=false hidden=true loop=0></EMBED>
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
			<input id="currentDept" type="hidden" value="${currentDept}"/>
			<tr><td height="5" colspan="3"></td></tr>
			<tr>
				<td width="7" height="51"><img src="images/t-1.jpg" width="7" height="51"></td>
				<td height="51" valign="middle" background="images/t-2.jpg">
					<span style="font-family: '黑体', '宋体'; font-size: 18px; color: #fff; padding-left: 15px; letter-spacing: 10px; letter-spacing: 10px;">系统设置</span>
				</td>
				<td width="3" height="51"><img src="images/t-3.jpg" width="8" height="51"></td>
			</tr>
			<tr>
				<td width="7" background="images/t-4.jpg">&nbsp;</td>
				<td height="100" style="text-align: center" valign="middle" bgcolor="#f7f7f7">
					<table width="55%" border="0" cellpadding="1" cellspacing="1" class="table_back">
						<c:if test="${role eq 0}">
							<tr>
								<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">大厅名称</td>
								<td width="78%" height="35" bgcolor="#F1F9FD">
									&nbsp;
									<select id="codeAndHall" name="codeAndHall" onchange="getSystemSetInfo()">
										<c:forEach items="${deptInfo}" var="item" >
											<option id="${item.key}" value="${item.key}">${item.value}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
						</c:if>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">六合一接口IP及端口号</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="ip" value="${ip}"/>(IP:端口号)
								<font color="red">多个IP端口号之间用“,”号分开</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">六合一接口系统类别</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="systemId" value="${systemId}"/>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">六合一接口序列号</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="interfaceId" value="${interfaceId}" style="width:300px;"/>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">启用代理人验证</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" id="isCheckAgent" name="isCheckAgent" value="0" <ww:if test="${isCheckAgent eq 0}">checked</ww:if>  onclick="isdlrrzdb(0);"/>是
								<input type="radio" id="isCheckAgent" name="isCheckAgent" value="1" <ww:if test="${isCheckAgent eq 1}">checked</ww:if>  onclick="isdlrrzdb(1);"/>否
							</td>
						</tr>
						<tr id="dlrrzdb" <ww:if test="${isCheckAgent eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">代理人人证对比</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="rzdb" value="0" <ww:if test="${rzdb eq 0}">checked</ww:if> />是
								<input type="radio" name="rzdb" value="1" <ww:if test="${rzdb eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">队列类型</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="serialNumType" value="1" <ww:if test="${serialNumType eq 1}">checked</ww:if> />一个大队列
								<input type="radio" name="serialNumType" value="2" <ww:if test="${serialNumType eq 2}">checked</ww:if> />小队列
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">启用接口</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isUseInterface" value="0" <ww:if test="${isUseInterface eq 0}">checked</ww:if>  onclick="isshowjklx(0);"/>是
								<input type="radio" name="isUseInterface" value="1" <ww:if test="${isUseInterface eq 1}">checked</ww:if>  onclick="isshowjklx(1);"/>否
							</td>
						</tr>
						<tr id="jklxtr" <ww:if test="${isUseInterface eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">接口类型</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="jklx" value="0" <ww:if test="${jklx eq 0}">checked</ww:if> />旧
								<input type="radio" name="jklx" value="1" <ww:if test="${jklx eq 1}">checked</ww:if> />新
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">启用硬件评价器</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    <input type="radio" name="isUseOldDevice" value="0" <ww:if test="${isUseOldDevice eq 0}">checked</ww:if> onclick="isPjqtype(0)"/>是
								<input type="radio" name="isUseOldDevice" value="1" <ww:if test="${isUseOldDevice eq 1}">checked</ww:if> onclick="isPjqtype(1)"/>否
							</td>
						</tr>
						<tr id="isDeviceType" <ww:if test="${isUseOldDevice eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">硬件评价器类型</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    <input type="radio" name="deviceType" value="0" <ww:if test="${deviceType eq 0}">checked</ww:if> />新评价器
								<input type="radio" name="deviceType" value="1" <ww:if test="${deviceType eq 1}">checked</ww:if> />旧评价器
							</td>
						</tr>
						<tr>
							<td width="20%" height="35" bgcolor="#FFFFFF" class="tableheader1">音频设置</td>
							<td width="80%" height="35" bgcolor="#F1F9FD">
                                <input type="radio" name="voiceType" value="1" <ww:if test="${voiceType eq 1}">checked</ww:if> />女音1
                                <input type="radio" name="voiceType" value="2" <ww:if test="${voiceType eq 2}">checked</ww:if> />女音2
                                <input type="radio" name="voiceType" value="3" <ww:if test="${voiceType eq 3}">checked</ww:if> />男音
								&nbsp;&nbsp;<input type="button" value="试听" onclick="test()" /> 
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">身份证读卡器类型</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    <input  type="radio" name="readID" value="0" <ww:if test="${readID eq 0}">checked</ww:if> />新版新中新读卡器
								<input type="radio" name="readID" value="1" <ww:if test="${readID eq 1}">checked</ww:if> />旧版新中新读卡器 
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">手写板启用</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    <input type="radio" name="isSignature" value="0" <ww:if test="${isSignature eq 0}">checked</ww:if> />是
								<input type="radio" name="isSignature" value="1" <ww:if test="${isSignature eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
						<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">摄像头启用</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    <input type="radio" name="isCamera" value="0" <ww:if test="${isCamera eq 0}">checked</ww:if> />是
								<input type="radio" name="isCamera" value="1" <ww:if test="${isCamera eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
						<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">电视窗口设置</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    <input type="radio" name="winMode" value="0" <ww:if test="${winMode eq 0}">checked</ww:if> />简单模式
								<input type="radio" name="winMode" value="1" <ww:if test="${winMode eq 1}">checked</ww:if> />复杂模式
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">每月取号次数设置</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="numberSet" value="${numberSet}"/>&nbsp;次 <font color="red">（默认次数为5次）</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">取号是否审核信息</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenPhoto" value="0" <ww:if test="${isOpenPhoto eq 0}">checked</ww:if> />是
								<input type="radio" name="isOpenPhoto" value="1" <ww:if test="${isOpenPhoto eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">重启IE工具IP地址</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="ieRestart" value="${ieRestart}"/><font color="red">（更改后重启服务器生效）</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用LED队列屏(发屏模式)</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenQueueTV" value="0" <ww:if test="${isOpenQueueTV eq 0}">checked</ww:if> />是
								<input type="radio" name="isOpenQueueTV" value="1" <ww:if test="${isOpenQueueTV eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr style="display: none;">
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">LED队列显示方向</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="ledQueueShow" value="0" <ww:if test="${ledQueueShow eq 0}">checked</ww:if> />纵向
								<input type="radio" name="ledQueueShow" value="1" <ww:if test="${ledQueueShow eq 1}">checked</ww:if> />横向
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用领证信息发屏</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenInformation" value="0" <ww:if test="${isOpenInformation eq 0}">checked</ww:if> onclick="isshowlzxx(0);" />是
								<input type="radio" name="isOpenInformation" value="1" <ww:if test="${isOpenInformation eq 1}">checked</ww:if> onclick="isshowlzxx(1);"/>否
							</td>
						</tr>
						<tr id="lzxxSend" <ww:if test="${isOpenInformation eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">领证信息发屏方式</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="lzxxSendMode" value="0" <ww:if test="${lzxxSendMode eq 0}">checked</ww:if> />异步
								<input type="radio" name="lzxxSendMode" value="1" <ww:if test="${lzxxSendMode eq 1}">checked</ww:if> />同步
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否强制叫号</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isForcedToNumber" value="0" <ww:if test="${isForcedToNumber eq 0}">checked</ww:if> onclick="isShow(0)"/>是
								<input type="radio" name="isForcedToNumber" value="1" <ww:if test="${isForcedToNumber eq 1}">checked</ww:if> onclick="isShow(1)"/>否
							</td>
						</tr>
						<tr id="callTime" <ww:if test="${isForcedToNumber eq 1}">style="display:none"</ww:if> >
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">强制叫号时间设置</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="forcedToNumberTime" value="${forcedToNumberTime}"/>&nbsp;秒
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用静脉验证</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenJm" value="0" <ww:if test="${isOpenJm eq 0}">checked</ww:if>/>是
								<input type="radio" name="isOpenJm" value="1" <ww:if test="${isOpenJm eq 1}">checked</ww:if>/>否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用取号拍照</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenIndexCamera" value="0" <ww:if test="${isOpenIndexCamera eq 0}">checked</ww:if>  onclick="isShowPz(0)"/>是
								<input type="radio" name="isOpenIndexCamera" value="1" <ww:if test="${isOpenIndexCamera eq 1}">checked</ww:if>  onclick="isShowPz(1)"/>否
							</td>
						</tr>
						<tr id="autoOrManual" <ww:if test="${isOpenIndexCamera eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">取号拍照方式(手动和自动)</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="autoOrManualCapture" value="0" <ww:if test="${autoOrManualCapture eq 0}">checked</ww:if>/>自动
								<input type="radio" name="autoOrManualCapture" value="1" <ww:if test="${autoOrManualCapture eq 1}">checked</ww:if>/>手动
							</td>
						</tr>
						<%--
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">六合一Callout表SQL</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="calloutSQL" style="width: 300px" value="${calloutSQL}"/>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">六合一Evaluation表SQL</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="valuerecordSQL" style="width: 300px" value="${valuerecordSQL}"/>
							</td>
						</tr>
						--%>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">评价方式</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="pjtype" value="0" <ww:if test="${pjtype eq 0}">checked</ww:if>/>原页面评价
								<input type="radio" name="pjtype" value="1" <ww:if test="${pjtype eq 1}">checked</ww:if>/>弹出框评价
								<font color="red">（更改后刷新外屏页面生效）</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">启用后台挂起恢复</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="hthf" value="0" <ww:if test="${hthf eq 0}">checked</ww:if>/>是
								<input type="radio" name="hthf" value="1" <ww:if test="${hthf eq 1}">checked</ww:if>/>否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用警告提示</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenJgts" value="0" <ww:if test="${isOpenJgts eq 0}">checked</ww:if> onclick="isShowJg(0)"/>是
								<input type="radio" name="isOpenJgts" value="1" <ww:if test="${isOpenJgts eq 1}">checked</ww:if> onclick="isShowJg(1)"/>否
							</td>
						</tr>
						<tr id="warning" <ww:if test="${isOpenJgts eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">警告提示时间</td>
							<!--<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="jgtsTime" value="${jgtsTime}"/>分钟
							</td>
							-->	
				    			 <td width="22%" height="35" bgcolor="#F1F9FD">&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">警告时段：</font>
				    			    </br>
				    				<input name="swkssz" id="swkssz" value="${swkssz}" style="width:50px" onkeyup="NumKey(this);"/>&nbsp; 点&nbsp;
				    				<input name="swksfz" id="swksfz" value="${swksfz}" style="width: 50px" onkeyup="NumKey(this);"/>&nbsp; 分&nbsp;&nbsp;
				    				&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
				    				<input name="swjssz" id="swjssz" value="${swjssz}" style="width: 50px" onkeyup="NumKey(this);"/>&nbsp; 点&nbsp;
				    				<input name="swjsfz" id="swjsfz" value="${swjsfz}" style="width: 50px" onkeyup="NumKey(this);"/>&nbsp;  分&nbsp;&nbsp;
	                                 </br>
				    				<input name="xwkssz" id="xwkssz" value="${xwkssz}" style="width:50px" onkeyup="NumKey(this);"/>&nbsp; 点&nbsp;
				    				<input name="xwksfz" id="xwksfz" value="${xwksfz}" style="width: 50px" onkeyup="NumKey(this);"/>&nbsp; 分&nbsp;&nbsp;
				    					&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;	
				    				<input name="xwjssz" id="xwjssz" value="${xwjssz}" style="width: 50px" onkeyup="NumKey(this);"/>&nbsp; 点&nbsp;
				    				<input name="xwjsfz" id="xwjsfz" value="${xwjsfz}" style="width: 50px" onkeyup="NumKey(this);"/>&nbsp; 分&nbsp;&nbsp;<font color="red">（采用24小时制）</font>
				    				&nbsp;&nbsp;&nbsp;&nbsp; </br><font color="red">警告间隔时间：</font>&nbsp;&nbsp;<input id="jgtsTime" style="width: 50px" value="${jgtsTime}"/>&nbsp;&nbsp;分钟<font color="red">（不小于10分钟）</font>
				    			</td>	
				    									 
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">数据抽取百分比</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="rNumber" style="width: 50px" value="${rNumber}"/>%
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用强制评价</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenForceEnvalue" value="0" <ww:if test="${isOpenForceEnvalue eq 0}">checked</ww:if> />是
								<input type="radio" name="isOpenForceEnvalue" value="1" <ww:if test="${isOpenForceEnvalue eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">等待人数统计方式</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="waitingNum" value="0" <ww:if test="${waitingNum eq 0}">checked</ww:if> />全部人数
								<input type="radio" name="waitingNum" value="1" <ww:if test="${waitingNum eq 1}">checked</ww:if> />等待区域
								<input type="radio" name="waitingNum" value="2" <ww:if test="${waitingNum eq 2}">checked</ww:if> />业务类型
								<input type="radio" name="waitingNum" value="3" <ww:if test="${waitingNum eq 3}">checked</ww:if> />队列类型
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用通知提档</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenTztd" value="0" <ww:if test="${isOpenTztd eq 0}">checked</ww:if> />是
								<input type="radio" name="isOpenTztd" value="1" <ww:if test="${isOpenTztd eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">领证信息自动发屏</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="autoLzxx" value="0" <ww:if test="${autoLzxx eq 0}">checked</ww:if> />是
								<input type="radio" name="autoLzxx" value="1" <ww:if test="${autoLzxx eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">顺序号滚动</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isopenScreen" value="0" <ww:if test="${isopenScreen eq 0}">checked</ww:if> />是
								<input type="radio" name="isopenScreen" value="1" <ww:if test="${isopenScreen eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">退办审核有效期</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="tbnum" style="width: 50px" value="${tbnum}"/>天
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否查验违法</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="iscywf" value="0" <ww:if test="${iscywf eq 0}">checked</ww:if> onclick="iscywf(0);"/>是
								<input type="radio" name="iscywf" value="1" <ww:if test="${iscywf eq 1}">checked</ww:if> onclick="iscywf(1);"/>否
							</td>
						</tr>
						<tr id="cywf" <ww:if test="${iscywf eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">查验违法方式</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="cywffs" value="0" <ww:if test="${cywffs eq 0}">checked</ww:if> />人工
								<input type="radio" name="cywffs" value="1" <ww:if test="${cywffs eq 1}">checked</ww:if> />自动
								<input type="radio" name="cywffs" value="2" <ww:if test="${cywffs eq 2}">checked</ww:if> />接口
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否允许重复取号</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isyzcfqh" value="0" <ww:if test="${isyzcfqh eq 0}">checked</ww:if> />是
								<input type="radio" name="isyzcfqh" value="1" <ww:if test="${isyzcfqh eq 1}">checked</ww:if> />否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">LED窗口屏型号</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="ledxh" value="0" <ww:if test="${ledxh eq 0}">checked</ww:if> />2012
								<input type="radio" name="ledxh" value="1" <ww:if test="${ledxh eq 1}">checked</ww:if> />2014
								<input type="radio" name="ledxh" value="2" <ww:if test="${ledxh eq 2}">checked</ww:if> />电视屏
							</td>
						</tr>
					 <tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">确认评价结果</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="qrpjtype" value="0" <ww:if test="${qrpjtype eq 0}">checked</ww:if>/>是
								<input type="radio" name="qrpjtype" value="1" <ww:if test="${qrpjtype eq 1}">checked</ww:if>/>否
							</td>
					</tr>
					<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">LED队列屏型号</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="leddlpxh" value="0" <ww:if test="${leddlpxh eq 0}">checked</ww:if> />2012
								<input type="radio" name="leddlpxh" value="1" <ww:if test="${leddlpxh eq 1}">checked</ww:if> />2014
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">LED队列屏滚动数目</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="lzcknum" style="width: 50px" value="${lzcknum}"/>个
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否开启取号时间限制</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="islimitqhtime" value="0" <ww:if test="${islimitqhtime eq 0}">checked</ww:if>  onclick="isShowqhtime(1)"/>是
								<input type="radio" name="islimitqhtime" value="1" <ww:if test="${islimitqhtime eq 1}">checked</ww:if>  onclick="isShowqhtime(0)"/>否
							</td>
						</tr>
						<tr id="qhtime" <ww:if test="${islimitqhtime eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">取号时间段设置</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input type="text" id="qhtime1" name="qhtime1" value="${qhtime1}"/>
								<font color="red">(设置方式如:  8:30-11:00,13:00-16:30    两段时间用-分隔表示时间段,多个时间段以逗号,分隔)</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用短信告知</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="ismessage" value="0" <ww:if test="${ismessage eq 0}">checked</ww:if>/>是
								<input type="radio" name="ismessage" value="1" <ww:if test="${ismessage eq 1}">checked</ww:if>/>否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">预约接口调用模式</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="yyjkmode" value="0" <ww:if test="${yyjkmode eq 0}">checked</ww:if>/>不使用预约接口
								<input type="radio" name="yyjkmode" value="1" <ww:if test="${yyjkmode eq 1}">checked</ww:if>/>珠海模式
								<input type="radio" name="yyjkmode" value="2" <ww:if test="${yyjkmode eq 2}">checked</ww:if>/>南宁模式
								<input type="radio" name="yyjkmode" value="3" <ww:if test="${yyjkmode eq 3}">checked</ww:if>/>中山模式
								<input type="radio" name="yyjkmode" value="4" <ww:if test="${yyjkmode eq 4}">checked</ww:if>/>广州模式
								<input type="radio" name="yyjkmode" value="5" <ww:if test="${yyjkmode eq 5}">checked</ww:if>/>佛山模式
								<input type="radio" name="yyjkmode" value="6" <ww:if test="${yyjkmode eq 6}">checked</ww:if>/>微信预约模式
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用黑名单核查</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenHmd" value="0" <ww:if test="${isOpenHmd eq 0}">checked</ww:if> onclick="isShowHmd(0)"/>是
								<input type="radio" name="isOpenHmd" value="1" <ww:if test="${isOpenHmd eq 1}">checked</ww:if> onclick="isShowHmd(1)"/>否
							</td>
						</tr>
						<tr id="HmdYjCs" <ww:if test="${isOpenHmd eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">黑名单预警取号次数</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input type="text" id="HmdCs" name="HmdCs" value="${HmdCs}"/>
							</td>				 
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">需要打印表单客户端ip</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="bdip" style="width: 300px" value="${bdip}"/>
								<font color="red">(多个ip以逗号,分隔)</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">默认评价时间</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="pjTime" style="width: 50px" value="${pjTime}"/>
								<font color="red">秒</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">人证对比参考值</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="rzdbckz" style="width: 50px" value="${rzdbckz}"/>
								<font color="red">(默认为60)</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">选号等待时间</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="xhnum" style="width: 50px" value="${xhnum}"/>秒
								<font color="red">(默认为2秒)</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">过号等待时间</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input id="ghnum" style="width: 50px" value="${ghnum}"/>秒
								<font color="red">(默认为30秒)</font>
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用核查预警</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenYj" value="0" <ww:if test="${isOpenYj eq 0}">checked</ww:if> onclick="isOpenYj(0)"/>是
								<input type="radio" name="isOpenYj" value="1" <ww:if test="${isOpenYj eq 1}">checked</ww:if> onclick="isOpenYj(1)"/>否
							</td>
						</tr>
						<tr class="Yj" <ww:if test="${isOpenYj eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">差评数</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input type="text" id="cps" name="cps" value="${cps}"/>
								<font color="red">(差评数超过设定值，则生成业务预警信息)</font>
							</td>				 
						</tr>
						<tr class="Yj" <ww:if test="${isOpenYj eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">工作量</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input type="text" id="gzl" name="gzl" value="${gzl}"/>
								<font color="red">(工作量低于设定值，则生成业务预警信息)</font>
							</td>				 
						</tr>
						<tr class="Yj" <ww:if test="${isOpenYj eq 1}">style="display:none"</ww:if>>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">满意度</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								&nbsp;<input type="text" id="myd" name="myd" value="${myd}"/>
								<font color="red">(满意度低于设定值，则生成业务预警信息)</font>
							</td>				 
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">人证对比</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="hfhmrzdb" value="0" <ww:if test="${hfhmrzdb eq 0}">checked</ww:if> onclick="hfhmrzdb(0)"/>开启
								<input type="radio" name="hfhmrzdb" value="1" <ww:if test="${hfhmrzdb eq 1}">checked</ww:if> onclick="hfhmrzdb(1)"/>关闭
							</td>
						</tr>
						<!-- 梧州新增 -->
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否开启查验业务</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="sfkqcyyw" value="0" <ww:if test="${sfkqcyyw eq 0}">checked</ww:if> onclick="sfkqcyyw(0)"/>是
								<input type="radio" name="sfkqcyyw" value="1" <ww:if test="${sfkqcyyw eq 1}">checked</ww:if> onclick="sfkqcyyw(1)"/>否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">是否启用微信取号</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="isOpenwxqh" value="0" <ww:if test="${isOpenwxqh eq 0}">checked</ww:if> onclick="isOpenwxqh(0)"/>是
								<input type="radio" name="isOpenwxqh" value="1" <ww:if test="${isOpenwxqh eq 1}">checked</ww:if> onclick="isOpenwxqh(1)"/>否
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">选择一维码或者二维码</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
								<input type="radio" name="xzywmewm" value="0" <ww:if test="${xzywmewm eq 0}">checked</ww:if> onclick="xzywmewm(0)"/>一维码
								<input type="radio" name="xzywmewm" value="1" <ww:if test="${xzywmewm eq 1}">checked</ww:if> onclick="xzywmewm(1)"/>二维码
								<input type="radio" name="xzywmewm" value="2" <ww:if test="${xzywmewm eq 2}">checked</ww:if> onclick="xzywmewm(2)"/>用自己的图片（此选项需要将图片放入服务器D:\img  的文件夹中）
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">LED屏</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    &nbsp;<input type="button" value="初始化LED屏" onclick="initLED(1)" />
							    &nbsp;员工编号:<input id="operNum" size="3"/><input type="button" value="初始化号码缓存" onclick="initLED(2)" />
							</td>
						</tr>
						<tr>
							<td width="22%" height="35" bgcolor="#FFFFFF" class="tableheader1">重载系统设置缓存</td>
							<td width="78%" height="35" bgcolor="#F1F9FD">
							    &nbsp;<input type="button" value="重载系统设置缓存" onclick="initSystemSet()" />
							    <font color="red">（只能重载本地系统设置）</font>
							</td>
						</tr>
					</table>
					<br/>
					<input name="bt1" class="button" type="button" value=" 保存 " onClick="save()" />
					<br/><br/>
				</td>
				<td width="8" background="images/t-5.jpg">&nbsp;</td>
			</tr>
			<tr>
				<td><img src="images/t-6.jpg" width="7" height="11"></td>
				<td height="11" background="images/t-7.jpg"></td>
				<td><img src="images/t-8.jpg" width="8" height="11"></td>
			</tr>
		</table>
	</body>
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript">
	function save() {
		var result;
		result = checkislimitqhtime(); //检查是否限制取号时间,并检查填写的时间是否正确
		
		if(result[0]=="false"){
			alert(result[1]);
			return false;
		}else{
			result="";
			result =checkPjTime();
			
			if(result[0]=="false"){
				alert(result[1]);
				return false;
			}
		}
		var codeAndHall="-";
		var codeAndHallVal = $("#codeAndHall").val();
		if(codeAndHallVal!=undefined){
			codeAndHall = codeAndHallVal.split("-");
		}
		var deptCode = codeAndHall[0];
		var deptHall = codeAndHall[1];
	    var ip = $("#ip").val();
	    var systemId = $("#systemId").val();
	    var interfaceId = $("#interfaceId").val();
	    var isCheckAgent = $("input:radio[name='isCheckAgent']:checked").val();
	    var serialNumType = $("input:radio[name='serialNumType']:checked").val();
	    var isUseInterface = $("input:radio[name='isUseInterface']:checked").val();
	    var isUseOldDevice = $("input:radio[name='isUseOldDevice']:checked").val();
	    var deviceType = $("input:radio[name='deviceType']:checked").val();
	    var voiceType = $("input:radio[name='voiceType']:checked").val();
	    var readID = $("input:radio[name='readID']:checked").val();
	    var isSignature = $("input:radio[name='isSignature']:checked").val();
	    var isCamera = $("input:radio[name='isCamera']:checked").val();
	    var winMode = $("input:radio[name='winMode']:checked").val();
	    var numberSet = $("#numberSet").val();
	    var isOpenPhoto = $("input:radio[name='isOpenPhoto']:checked").val();
	    var ieRestart = $("#ieRestart").val();
	    var isOpenQueueTV = $("input:radio[name='isOpenQueueTV']:checked").val();
	    var ledQueueShow = $("input:radio[name='ledQueueShow']:checked").val();
	    var isOpenInformation = $("input:radio[name='isOpenInformation']:checked").val();
	    var lzxxSendMode = $("input:radio[name='lzxxSendMode']:checked").val();
	    var isForcedToNumber = $("input:radio[name='isForcedToNumber']:checked").val();
	    var forcedToNumberTime = $("#forcedToNumberTime").val();
	    var isOpenJm = $("input:radio[name='isOpenJm']:checked").val();
	    var isOpenIndexCamera = $("input:radio[name='isOpenIndexCamera']:checked").val();
	    var autoOrManualCapture = $("input:radio[name='autoOrManualCapture']:checked").val();
	    //var calloutSQL = $("#calloutSQL").val();
	    //var valuerecordSQL = $("#valuerecordSQL").val();
	    var pjtype = $("input:radio[name='pjtype']:checked").val();
	    var hthf = $("input:radio[name='hthf']:checked").val();
	    var isOpenJgts = $("input:radio[name='isOpenJgts']:checked").val();
	    var swkssz=$("#swkssz").val();
	    var swksfz=$("#swksfz").val();
	    var swjssz=$("#swjssz").val();
	    var swjsfz=$("#swjsfz").val();
	    var xwkssz=$("#xwkssz").val();
	    var xwksfz=$("#xwksfz").val();
	    var xwjssz=$("#xwjssz").val();
	    var xwjsfz=$("#xwjsfz").val();
	    var jgtsTime = $("#jgtsTime").val();
	    var rNumber = $("#rNumber").val();
	    var isOpenForceEnvalue = $("input:radio[name='isOpenForceEnvalue']:checked").val();
	    var waitingNum = $("input:radio[name='waitingNum']:checked").val();
	    var isOpenTztd = $("input:radio[name='isOpenTztd']:checked").val();
	    var autoLzxx = $("input:radio[name='autoLzxx']:checked").val();
	    //var lzxxSendMode = $("input:radio[name='lzxxSendMode']:checked").val();
	    var isopenScreen = $("input:radio[name='isopenScreen']:checked").val();
	    var tbnum = $("#tbnum").val();
	    var iscywf = $("input:radio[name='iscywf']:checked").val();
	    var cywffs = $("input:radio[name='cywffs']:checked").val();
	    var isyzcfqh = $("input:radio[name='isyzcfqh']:checked").val();
	    var ledxh = $("input:radio[name='ledxh']:checked").val();
	    var qrpjtype=$("input:radio[name='qrpjtype']:checked").val();
	    var leddlpxh = $("input:radio[name='leddlpxh']:checked").val();
	    var lzcknum = $("#lzcknum").val();
	    var islimitqhtime = $("input:radio[name='islimitqhtime']:checked").val();
	    var qhtime1 = $("#qhtime1").val();
	    var ismessage=$("input:radio[name='ismessage']:checked").val();
	    var yyjkmode=$("input:radio[name='yyjkmode']:checked").val();
	    var isOpenHmd = $("input:radio[name='isOpenHmd']:checked").val();
	    var HmdCs = $("#HmdCs").val();
	    var bdip = $("#bdip").val();
	    var pjTime = $("#pjTime").val();
	    var rzdbckz = $("#rzdbckz").val();
	    var xhnum = $("#xhnum").val();
	    var ghnum = $("#ghnum").val();
	    var isOpenYj = $("input:radio[name='isOpenYj']:checked").val();
	    var cps = $("#cps").val();
	    var gzl = $("#gzl").val();
	    var myd = $("#myd").val();
	    var hfhmrzdb = $("input:radio[name='hfhmrzdb']:checked").val();
	    var sfkqcyyw = $("input:radio[name='sfkqcyyw']:checked").val();
	    var xzywmewm = $("input:radio[name='xzywmewm']:checked").val();
	    var isOpenwxqh = $("input:radio[name='isOpenwxqh']:checked").val();
	    var rzdb = $("input:radio[name='rzdb']:checked").val();
	    var jklx = $("input:radio[name='jklx']:checked").val();
	    
	    
	    
	    
	    
	    
	    var params = {deptCode:deptCode,deptHall:deptHall,ip:ip, systemId:systemId, interfaceId:interfaceId, isCheckAgent:isCheckAgent,
	    	serialNumType:serialNumType, isUseInterface:isUseInterface, isUseOldDevice:isUseOldDevice,deviceType:deviceType,
	    	voiceType:voiceType,readID:readID,isSignature:isSignature,isCamera:isCamera,winMode:winMode,
	    	numberSet:numberSet,isOpenPhoto:isOpenPhoto,ieRestart:ieRestart,isOpenQueueTV:isOpenQueueTV,
	    	ledQueueShow:ledQueueShow,isOpenInformation:isOpenInformation,lzxxSendMode:lzxxSendMode,isForcedToNumber:isForcedToNumber,
	    	forcedToNumberTime:forcedToNumberTime,isOpenJm:isOpenJm,isOpenIndexCamera:isOpenIndexCamera,
	    	autoOrManualCapture:autoOrManualCapture,pjtype:pjtype,hthf:hthf,isOpenJgts:isOpenJgts,swkssz:swkssz,swksfz:swksfz,swjssz:swjssz,
	    	swjsfz:swjsfz,xwkssz:xwkssz,xwksfz:xwksfz,xwjssz:xwjssz,xwjsfz:xwjsfz,jgtsTime:jgtsTime,rNumber:rNumber,
	    	isOpenForceEnvalue:isOpenForceEnvalue,waitingNum:waitingNum,isOpenTztd:isOpenTztd,
	    	autoLzxx:autoLzxx,isopenScreen:isopenScreen,tbnum:tbnum,iscywf:iscywf,cywffs:cywffs,
	    	isyzcfqh:isyzcfqh,ledxh:ledxh,qrpjtype:qrpjtype,leddlpxh:leddlpxh,lzcknum:lzcknum,islimitqhtime:islimitqhtime,
	    	qhtime1:qhtime1,ismessage:ismessage,yyjkmode:yyjkmode,isOpenHmd:isOpenHmd,HmdCs:HmdCs,bdip:bdip,pjTime:pjTime,rzdbckz:rzdbckz,xhnum:xhnum,
	    	ghnum:ghnum,isOpenYj:isOpenYj,cps:cps,gzl:gzl,myd:myd,hfhmrzdb:hfhmrzdb,sfkqcyyw:sfkqcyyw,xzywmewm:xzywmewm,rzdb:rzdb,isOpenwxqh:isOpenwxqh,jklx:jklx};
	    $.post("system/update.action", params, function(data){
	        if (eval(data)) {
	            window.alert("保存成功");
	        } else {
	            window.alert("保存失败，请稍候再试");
	        }
	   	});
	}
	
	function initLED(val) {
	    var operNum = $("#operNum").val();
	    if (2 == val && "" == operNum) {
	        alert("请填写员工编号");
	        return;
	    }
	    $.post("system/initLED.action", {val: val, operNum: operNum}, function(data){
	        if (-1 == data) {
	            window.alert("初始化完成");
	        } else {
	            window.alert("有未能成功初始化LED屏");
	        }
	    });
	}
	
	function test() {
		var voiceType = $("input:radio[name='voiceType']:checked").val();
		if ("1" == voiceType) {
		   lady.play();
		} else if ("2" == voiceType) {
		   lady2.play();
		} else if ("3" == voiceType) {
		   man.play();
		}
	}
	function isShow(isValue){
		if("0"==isValue){
			$("#callTime").css("display","")
		}else{
			$("#callTime").css("display","none")
		}
	}
	function isShowPz(isValue){
		if("0"==isValue){
			$("#autoOrManual").css("display","")
		}else{
			$("#autoOrManual").css("display","none")
		}
	}
	function isShowJg(isValue){
		if("0"==isValue){
			$("#warning").css("display","")
		}else{
			$("#warning").css("display","none")
		}
	}
	function isPjqtype(isValue){
		if("0"==isValue){
			$("#isDeviceType").css("display","")
		}else{
			$("#isDeviceType").css("display","none")
		}
	}
	function isshowlzxx(isValue){
		if("0"==isValue){
			$("#lzxxSend").css("display","")
		}else{
			$("#lzxxSend").css("display","none")
		}
	}
	function isshowjklx(isValue){
		if("0"==isValue){
			$("#jklxtr").css("display","")
		}else{
			$("#jklxtr").css("display","none")
		}
	}
	function isdlrrzdb(isValue){
		if("0"==isValue){
			$("#dlrrzdb").css("display","")
		}else{
			$("#dlrrzdb").css("display","none")
		}
	}
	function iscywf(isValue){
		if("0"==isValue){
			$("#cywf").css("display","")
		}else{
			$("#cywf").css("display","none")
		}
	}
	
		/*警告时间设置提醒只能输入数字*/
		function NumKey(obj) {  
    	obj.value = obj.value.replace(/[^0-9]/g,'');  
		}
		//根据选择获取相应系统设置信息(异步请求) 
		function getSystemSetInfo(){
			var value = $("#codeAndHall option:selected").attr("value");
			var infoArray = value.split("-");
			$.ajax({
				type:"POST",
				url:"system/getSystemSetInfo.action",
				cache:false,
				dataType:"json",
				data:{deptCode:infoArray[0],deptHall:infoArray[1]},
				success:function(data){
					$("#ip").val(data.ip);
					$("#systemId").val(data.systemId);
					$("#interfaceId").val(data.interfaceId);
					$("input[name='isCheckAgent'][value="+data.isCheckAgent+"]").attr("checked",true);
					$("input[name='serialNumType'][value="+data.serialNumType+"]").attr("checked",true);
					$("input[name='isUseInterface'][value="+data.isUseInterface+"]").attr("checked",true);
					$("input[name='isUseOldDevice'][value="+data.isUseOldDevice+"]").attr("checked",true);
					$("input[name='deviceType'][value="+data.deviceType+"]").attr("checked",true);
					$("input[name='voiceType'][value="+data.voiceType+"]").attr("checked",true);
					$("input[name='readID'][value="+data.readID+"]").attr("checked",true);
					$("input[name='isSignature'][value="+data.isSignature+"]").attr("checked",true);
					$("input[name='isCamera'][value="+data.isCamera+"]").attr("checked",true);
					$("input[name='winMode'][value="+data.winMode+"]").attr("checked",true);
					$("input[name='winMode'][value="+data.winMode+"]").attr("checked",true);
					$("#numberSet").val(data.numberSet);
					$("input[name='isOpenPhoto'][value="+data.isOpenPhoto+"]").attr("checked",true);
					$("#ieRestart").val(data.ieRestart);
					$("input[name='isOpenQueueTV'][value="+data.isOpenQueueTV+"]").attr("checked",true);
					$("input[name='ledQueueShow'][value="+data.ledQueueShow+"]").attr("checked",true);
					$("input[name='isOpenInformation'][value="+data.isOpenInformation+"]").attr("checked",true);
					$("input[name='lzxxSendMode'][value="+data.lzxxSendMode+"]").attr("checked",true);
					$("input[name='isForcedToNumber'][value="+data.isForcedToNumber+"]").attr("checked",true);
					$("#forcedToNumberTime").val(data.forcedToNumberTime);
					$("input[name='isOpenJm'][value="+data.isOpenJm+"]").attr("checked",true);
					$("input[name='isOpenIndexCamera'][value="+data.isOpenIndexCamera+"]").attr("checked",true);
					$("input[name='autoOrManualCapture'][value="+data.autoOrManualCapture+"]").attr("checked",true);
					//$("#calloutSQL").val(data.calloutSQL);
					//$("#valuerecordSQL").val(data.valuerecordSQL);
					$("input[name='pjtype'][value="+data.pjtype+"]").attr("checked",true);
					$("input[name='hthf'][value="+data.hthf+"]").attr("checked",true);
					$("input[name='isOpenJgts'][value="+data.isOpenJgts+"]").attr("checked",true);
					$("#swkssz").val(data.swkssz);
					$("#swksfz").val(data.swksfz);
					$("#swjssz").val(data.swjssz);
					$("#swjsfz").val(data.swjsfz);
					$("#xwkssz").val(data.xwkssz);
					$("#xwksfz").val(data.xwksfz);
					$("#xwjssz").val(data.xwjssz);
					$("#xwjsfz").val(data.xwjsfz);
					$("#jgtsTime").val(data.jgtsTime);
					$("#rNumber").val(data.rNumber);
					$("input[name='isOpenForceEnvalue'][value="+data.isOpenForceEnvalue+"]").attr("checked",true);
					$("input[name='waitingNum'][value="+data.waitingNum+"]").attr("checked",true);
					$("input[name='isOpenTztd'][value="+data.isOpenTztd+"]").attr("checked",true);
					$("input[name='autoLzxx'][value="+data.autoLzxx+"]").attr("checked",true);
					$("input[name='isopenScreen'][value="+data.isopenScreen+"]").attr("checked",true);
					$("#tbnum").val(data.tbnum);
					$("input[name='iscywf'][value="+data.iscywf+"]").attr("checked",true);
					$("input[name='cywffs'][value="+data.cywffs+"]").attr("checked",true);
					$("input[name='isyzcfqh'][value="+data.isyzcfqh+"]").attr("checked",true);
					$("input[name='ledxh'][value="+data.ledxh+"]").attr("checked",true);
					$("input[name='qrpjtype'][value="+data.qrpjtype+"]").attr("checked",true);
					$("input[name='leddlpxh'][value="+data.leddlpxh+"]").attr("checked",true);
					$("#lzcknum").val(data.lzcknum);	
					$("input[name='islimitqhtime'][value="+data.islimitqhtime+"]").attr("checked",true);
					$("#qhtime1").val(data.qhtime1);
					$("input[name='ismessage'][value="+data.ismessage+"]").attr("checked",true);
					$("input[name='yyjkmode'][value="+data.yyjkmode+"]").attr("checked",true);
					$("input[name='isOpenHmd'][value="+data.isOpenHmd+"]").attr("checked",true);
					$("#HmdCs").val(data.HmdCs);
					$("#bdip").val(data.bdip);
					$("#pjTime").val(data.pjTime);
					$("#rzdbckz").val(data.rzdbckz);
					$("#xhnum").val(data.xhnum);
					$("#ghnum").val(data.ghnum);
					$("input[name='isOpenYj'][value="+data.isOpenYj+"]").attr("checked",true);
					$("#cps").val(data.cps);
					$("#gzl").val(data.gzl);
					$("#myd").val(data.myd);
					$("input[name='hfhmrzdb'][value="+data.hfhmrzdb+"]").attr("checked",true);
					$("input[name='sfkqcyyw'][value="+data.sfkqcyyw+"]").attr("checked",true);
					$("input[name='xzywmewm'][value="+data.xzywmewm+"]").attr("checked",true);
					$("input[name='rzdb'][value="+data.rzdb+"]").attr("checked",true);
					$("input[name='isOpenwxqh'][value="+data.isOpenwxqh+"]").attr("checked",true);
					$("input[name='jklx'][value="+data.jklx+"]").attr("checked",true);
				}
			});
		}
		//页面打开 使相应下拉框选项选中
		function currentDept(){
			var currentDept = $("#currentDept").val();
			$("option[value="+currentDept+"]").attr("selected",true);
		}
		//重新加载系统设置到缓存
		function initSystemSet(){
			var currentDept = $("#currentDept").val().split("-");
			var deptCode = currentDept[0];
			var deptHall = currentDept[1];
			$.ajax({
				type:"post",
				cache:false,
				url:"system/resetSystemSetInfo.action",
				data:{deptCode:deptCode,deptHall:deptHall},
				dataType:"json",
				success:function(data){
					if(data.flag){
						$("#ip").val(data.ip);
						$("#systemId").val(data.systemId);
						$("#interfaceId").val(data.interfaceId);
						$("input[name='isCheckAgent'][value="+data.isCheckAgent+"]").attr("checked",true);
						$("input[name='serialNumType'][value="+data.serialNumType+"]").attr("checked",true);
						$("input[name='isUseInterface'][value="+data.isUseInterface+"]").attr("checked",true);
						$("input[name='isUseOldDevice'][value="+data.isUseOldDevice+"]").attr("checked",true);
						$("input[name='deviceType'][value="+data.deviceType+"]").attr("checked",true);
						$("input[name='voiceType'][value="+data.voiceType+"]").attr("checked",true);
						$("input[name='readID'][value="+data.readID+"]").attr("checked",true);
						$("input[name='isSignature'][value="+data.isSignature+"]").attr("checked",true);
						$("input[name='isCamera'][value="+data.isCamera+"]").attr("checked",true);
						$("input[name='winMode'][value="+data.winMode+"]").attr("checked",true);
						$("input[name='winMode'][value="+data.winMode+"]").attr("checked",true);
						$("#numberSet").val(data.numberSet);
						$("input[name='isOpenPhoto'][value="+data.isOpenPhoto+"]").attr("checked",true);
						$("#ieRestart").val(data.ieRestart);
						$("input[name='isOpenQueueTV'][value="+data.isOpenQueueTV+"]").attr("checked",true);
						$("input[name='ledQueueShow'][value="+data.ledQueueShow+"]").attr("checked",true);
						$("input[name='isOpenInformation'][value="+data.isOpenInformation+"]").attr("checked",true);
						$("input[name='lzxxSendMode'][value="+data.lzxxSendMode+"]").attr("checked",true);
						$("input[name='isForcedToNumber'][value="+data.isForcedToNumber+"]").attr("checked",true);
						$("#forcedToNumberTime").val(data.forcedToNumberTime);
						$("input[name='isOpenJm'][value="+data.isOpenJm+"]").attr("checked",true);
						$("input[name='isOpenIndexCamera'][value="+data.isOpenIndexCamera+"]").attr("checked",true);
						$("input[name='autoOrManualCapture'][value="+data.autoOrManualCapture+"]").attr("checked",true);
						//$("#calloutSQL").val(data.calloutSQL);
						//$("#valuerecordSQL").val(data.valuerecordSQL);
						$("input[name='pjtype'][value="+data.pjtype+"]").attr("checked",true);
						$("input[name='hthf'][value="+data.hthf+"]").attr("checked",true);
						$("input[name='isOpenJgts'][value="+data.isOpenJgts+"]").attr("checked",true);
						$("#swkssz").val(data.swkssz);
						$("#swksfz").val(data.swksfz);
						$("#swjssz").val(data.swjssz);
						$("#swjsfz").val(data.swjsfz);
						$("#xwkssz").val(data.xwkssz);
						$("#xwksfz").val(data.xwksfz);
						$("#xwjssz").val(data.xwjssz);
						$("#xwjsfz").val(data.xwjsfz);
						$("#jgtsTime").val(data.jgtsTime);
						$("#rNumber").val(data.rNumber);
						$("input[name='isOpenForceEnvalue'][value="+data.isOpenForceEnvalue+"]").attr("checked",true);
						$("input[name='waitingNum'][value="+data.waitingNum+"]").attr("checked",true);
						$("input[name='isOpenTztd'][value="+data.isOpenTztd+"]").attr("checked",true);
						$("input[name='autoLzxx'][value="+data.autoLzxx+"]").attr("checked",true);
						$("input[name='isopenScreen'][value="+data.isopenScreen+"]").attr("checked",true);
						$("#tbnum").val(data.tbnum);
						$("input[name='iscywf'][value="+data.iscywf+"]").attr("checked",true);
						$("input[name='cywffs'][value="+data.cywffs+"]").attr("checked",true);
						$("input[name='isyzcfqh'][value="+data.isyzcfqh+"]").attr("checked",true);
						$("input[name='ledxh'][value="+data.ledxh+"]").attr("checked",true);
						$("input[name='qrpjtype'][value="+data.qrpjtype+"]").attr("checked",true);
						$("input[name='leddlpxh'][value="+data.leddlpxh+"]").attr("checked",true);
						$("#lzcknum").val(data.lzcknum);
						$("input[name='islimitqhtime'][value="+data.islimitqhtime+"]").attr("checked",true);
						$("#qhtime1").val(data.qhtime1);
						$("input[name='ismessage'][value="+data.ismessage+"]").attr("checked",true);
						$("input[name='yyjkmode'][value="+data.yyjkmode+"]").attr("checked",true);
						$("input[name='isOpenHmd'][value="+data.isOpenHmd+"]").attr("checked",true);
						$("#HmdCs").val(data.HmdCs);
						$("#bdip").val(data.bdip);
	    				$("#pjTime").val(data.pjTime);
	    				$("#rzdbckz").val(data.rzdbckz);
	    				$("#xhnum").val(data.xhnum);
	    				$("#ghnum").val(data.ghnum);
	    				$("input[name='isOpenYj'][value="+data.isOpenYj+"]").attr("checked",true);
	    				$("#cps").val(data.cps);
						$("#gzl").val(data.gzl);
						$("#myd").val(data.myd);
						$("input[name='hfhmrzdb'][value="+data.hfhmrzdb+"]").attr("checked",true);
						$("input[name='sfkqcyyw'][value="+data.sfkqcyyw+"]").attr("checked",true);
						$("input[name='rzdb'][value="+data.rzdb+"]").attr("checked",true);
						$("input[name='isOpenwxqh'][value="+data.isOpenwxqh+"]").attr("checked",true);
						$("input[name='jklx'][value="+data.jklx+"]").attr("checked",true);
	    			
						alert("重载完成");
					}else{
						alert("重载失败,该大厅没有添加系统设置");
					}
				}
			});
		}
		//点击按钮 显示取号时间段填写框
		function isShowqhtime(flag){
			if(flag){
				$("#qhtime").css("display","");
			}else{
				$("#qhtime").css("display","none");
			}
		}
		//点击按钮 显示黑名单预警次数
		function isShowHmd(flag){
			if(flag){
				$("#HmdYjCs").css("display","none");
			}else{
				$("#HmdYjCs").css("display","");
			}
		}
		//点击按钮 显示预警设定值
		function isOpenYj(flag){
			if(flag){
				$(".Yj").css("display","none");
			}else{
				$(".Yj").css("display","");
			}
		}
		//检查是否限制取号时间段,及取号时间段是否正确填写
		function checkislimitqhtime(){
			var islimitqhtime = $("input:radio[name='islimitqhtime']:checked").val();
			var qhtime1 = $.trim($("#qhtime1").val());
			var flag = true;
			var meg = "";
			if(islimitqhtime==false){
				if(qhtime1!=""&&qhtime1!=null){
					var qhtimeArray = qhtime1.split(",");
					var slot;//时间段 10:00-11:00
					var time;//时间 10:00
					var hour;//时位
					var second;//分位
					for(var i =0;i<qhtimeArray.length;i++){
						slot = qhtimeArray[i].split("-");
						if(slot.length==2){
							for(var a =0;a<slot.length;a++){
								time = slot[a].split(":");
								if(time.length==2){
									hour = time[0];
									second = time[1];
									if(hour<1||hour>23){
										flag = false;
										meg += "请检查所填时间"+slot[a]+"的时位是否在1~23小时内";
									}
									if(second<0||second>59){
										flag = false;
										meg += "请检查所填时间"+slot[a]+"的分位是否在0~59分钟内";
									}
								}else if(time.length==3){
									flag = false;
									meg +="时间段只精确到分钟,格式为: 12:00";
								}else{
									flag = false;
									meg +="请检查所填时间段"+qhtimeArray[i]+"是否符合格式: 12:00";
								}
							}
						}else{
							flag = false;
							meg +="请检查所填时间段"+qhtimeArray[i]+"是否符合格式: 12:00-13:00";
						}
					}
				}else{
					flag = false;
					meg += "如果选择了限制取号时间段,则所限制的时间段不能为空";
				}
			}
			if(flag == false){
				var result=["false",meg];
				return result;
			}else{
				var result=["true","成功"];
				return result;
			}
		}
		
		function checkPjTime(){
			var pjTime = $.trim($("#pjTime").val());
			var flag = true;
			var meg = "";
			var reg = /^[1-9]\d*$/;
			if(!reg.test(pjTime)){
				meg="默认评价时间必须是大于0的整数!";
				flag=false;
			}
			
			if(flag == false){
				var result=["false",meg];
				return result;
			}else{
				var result=["true","成功"];
				return result;
			}
		}
	</script>
</html>