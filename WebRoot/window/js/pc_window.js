document.oncontextmenu = function() { return false; } //禁止右键功能,单击右键将无任何反应
document.onselectstart = function() { return false; } //禁止选择,也就是无法复制
window.onhelp = function() { return false; } //屏蔽F1

dwr.engine.setActiveReverseAjax(true);
dwr.engine.setNotifyServerOnPageUnload(true);

function login(data) {
	var a=$("#autopj").val("2");
	//window.open("http://www.baidu.com", "_blank", "width=200,height=100");
	//subSomething();//重启IE
	$("#_welcome").css("display", "none");
	$("#data_show").css("display", "");
	$("#operNum").val(data.code);
	$("#loginIP").val(data.loginIP);
	$("#police_code").html(data.code);
	$("#police_name").html(data.name);
	$("#div_zym").html(data.comments);
	$("#tootip_img").attr("src", data.photo);
	if ($("#myMarq").text() == "") {
		$("#mask_tip").css("display", "");
	} else {
		$("#mask_tip").css("display", "none");
	}
}

function aotologin(datas) {
	//subSomething();//重启IE
	var data = JSON.parse(datas);
	$("#_welcome").css("display", "none");
	$("#data_show").css("display", "");
	$("#operNum").val(data.code);
	$("#loginIP").val(data.loginIP);
	$("#police_code").html(data.code);
	$("#police_name").html(data.name);
	$("#div_zym").html(data.comments);
	$("#tootip_img").attr("src", data.photo);
	if ($("#myMarq").text() == "") {
		$("#mask_tip").css("display", "");
	} else {
		$("#mask_tip").css("display", "none");
	}
}

function reloadPcWindow(data){
	window.location.reload();
}
document.onreadystatechange = subSomething;//当页面加载状态改变的时候执行这个方法.
 function subSomething() { 
  if(document.readyState=="complete"){
   //alert("当前页面已加载完成！");
  }else{
  	subSomething();
  }
 }

function pause(data) {
	
	if (data == 1) {//暂停服务,显示暂停服务页,隐藏数据展示页
		/*珠海改动--将显示隐藏div改成替换图片*/
		/*$("#data_show").css("display", "none");
		$("#_pause").css("display", "");*/
		$("#imgzt").attr("src","/queue/images/queue/ztimg.jpg")
	} else {//暂停服务结束,开始服务
		/*$("#_pause").css("display", "none");
		$("#data_show").css("display", "");
		$("#mask_tip").css("display", "none");*/
		$("#imgzt").attr("src","/queue/images/queue/waitnext.jpg")
	}
}
function passshowdata(data) {
	$("#div1").css("display", "none");
	$("#div8").css("display", "");
	if ($("#myMarq").text() == "") {
		$("#mask_tip").css("display", "");
	} else {
		$("#mask_tip").css("display", "none");
	}
	$("#_jdc").css("display", "none");
	$("#_jsr").css("display", "none");
	$("#quhaophoto").find("img").remove();
	$("#div_dqhm").find("p").remove();
	$("#div_dqhm").find("br").remove();
}

function camera() {
	jsrzP.nW = 300;
	jsrzP.nH = 320;
	jsrzP.sZpImgFilePath = "c:\\99999.jpg";
	try{
		//jsrzP.OnCaptureZpNew();
		var pzpic = jsrzP.OnCaptureZpNew();
		if (pzpic == "" || pzpic == false) {
	    	//拍照失败
			okORerrorCamera(0);
		} else {
			$("#base64Code").val(pzpic);
		}
　	}catch(e){
　		okORerrorCamera(0);
　　}
}

//判断摄像头加载情况
function okORerrorCamera(params){
	//params:  0初始化失败，1拍照失败
	$.ajax({type: "POST", cache: false,
		url: '/queue/number/addLogContent.action?params=' + params,
		success: function (req) {}
	});
}

function audioAutoPlay() {
    window.svein.playMusic(0);
}
/*window.addEventListener("click", function () {
    function audioAutoPlay() {
        please.muted=true;
        please.play();
        window.svein.playMusic(0);
    }
    audioAutoPlay();
});*/

var setTimeoutReturn, isok, isSignatureisok,isOpenIndexCamera;
function toEvaluation(data) {
	/*please.muted=false;
	please.play();*/
    //可以评价的状态下首先打开摄像头,拍取一张照片用于自动评价照片存录
	isok = data.isCamera; //判断是否启用摄像头
	if (isok == 0) {
		//camera();
	}
	var reasons = "";
	$.each(eval("(" + data.reason + ")"), function (key, val) {
		reasons += "<li><a href=\"#\" class=\"btn_ywlx\" onfocus=\"this.blur()\" onclick=\"nonePop('4','1','" + val + "')\">" + val + "</a></li>";
	});
	$("#reasons").html(reasons);
	//播放声音 请你对我的服务进行评价
	window.svein.playMusic(0);
	$("#_eval").css("display", "");
	
	if("1"==data.isOpenForceEnvalue){//判断是否启用强制评价 0启用 1不启用
		setTimeoutReturn = setTimeout("enterEval('','2','')", data.pt); //设置10秒默认评价10000
	}
}

/*
function dispNone(){
	$("#_eval").css("display", "none");
	$("#base64Code").val("");
	$("#div1").css("display", "none");
	$("#div8").css("display", "");
	if ($("#myMarq").text() == "") {
		$("#mask_tip").css("display", "");
	} else {
		$("#mask_tip").css("display", "none");
	}
	$("#div_dqhm").find("p").remove();
	$("#div_dqhm").find("br").remove();
}*/

//差评原因div
function nonePop(subvalue, sub_flag, reason) {
	
	$("#pop").css("display", "none");
	if (reason != "") {
		window.clearInterval(setTimeoutReturn);
	}
	
	enterEval(subvalue, sub_flag, reason);
}

function doEval(selectValue, name) {
	if (4 == selectValue) {
		window.clearInterval(setTimeoutReturn);
		$("#_eval").css("display", "none");
		if (0 < $("#reasons li").size()) {
			$("#pop").css("display", "");
			setTimeoutReturn = setTimeout("nonePop('4', '1', '')", 10000);//10秒后关闭差评原因div
		} else {
			nonePop("4", "1", "");
		}
	} else {
		enterEval(selectValue, 1, "");
	}
	$("#_jdc").css("display", "none");
	$("#_jsr").css("display", "none");
	$("#quhaophoto").find("img").remove();
	$("#div_dqhm").find("p").remove();
	$("#div_dqhm").find("br").remove();
}

/* 弹框确认提交评价
function rusure(selectValue, name){  
	question = confirm("确认评价结果吗?")
	setTimeout("self.close()", 2000);
	if (question !="1"){
	document.getElementById(obj).style.display = '';
//	$("#_eval").css("display", "");
	}
	if(question ="1"){
		if (4 == selectValue) {
			window.clearInterval(setTimeoutReturn);
			$("#_eval").css("display", "none");
			if (0 < $("#reasons li").size()) {
				$("#pop").css("display", "");
				setTimeoutReturn = setTimeout("nonePop('4', '1', '')", 10000);//10秒后关闭差评原因div
			} else {
				nonePop("4", "1", "");
			}
		} else {
			enterEval(selectValue, 1, "");
		}
		$("#div_dqhm").find("p").remove();
		$("#div_dqhm").find("br").remove();
	}
}*/ 

function confirmBox(selectValue, name){
	var hidden;
	hidden = document.getElementById("hidden").value;
	document.getElementById("hidden2").value=selectValue;
	//alert(hidden);
	if(hidden=="true"){
		//alert("@@@@@@@@@@@@@@@@@@@@");
		if (4 == selectValue) {
			window.clearInterval(setTimeoutReturn);
			$("#_eval").css("display", "none");
			if (0 < $("#reasons li").size()) {
				$("#pop").css("display", "");
				setTimeoutReturn = setTimeout("nonePop('4', '1', '')", 10000);//10秒后关闭差评原因div
			} else {
				nonePop("4", "1", "");
			}
		} else {
			enterEval(selectValue, 1, "");
		}
		$("#_jdc").css("display", "none");
		$("#_jsr").css("display", "none");
		$("#quhaophoto").find("img").remove();
		$("#div_dqhm").find("p").remove();
		$("#div_dqhm").find("br").remove();
	}
	//alert("#####################");
	$("#test").css("display", "");
	document.getElementById("hidden").value="";
	//return hidden;
}
function ok(){
	document.getElementById("hidden").value = "true";
	var ww=document.getElementById("hidden2").value;
	confirmBox(ww,'1');
	$("#_eval").css("display", "none");
	$("#test").css("display", "none");
}
function cancle(){ 
	document.getElementById("hidden").value = "false"; 
	$("#_eval").css("display", "");
	$("#test").css("display", "none");
}  

function showData(data) {
	$("#_welcome").css("display", "none");
	$("#mask_tip").css("display", "none");
	$("#div8").css("display", "none");
	$("#data_show").css("display", "");
	$("#div1").css("display", "");
	
	var index = 0, content = "<tr>";
//	, wfxxcontent = "<tr>";
	$.each(data.content, function (key, val) {
		content += "<th width='20%'>" + key + "</th><td width='30%'>" + val + "</td>";
		index = index + 1;
		if (index % 2 == 0) {
			content += "</tr><tr>";
		}
	});
	if (index % 2 != 0) {
		content += "<th></th><td></td></tr>";
	}
	content +="<tr><td colspan='4' align='center'><p style='color: red'>请核对您的信息，如有错误请及时与业务员联系！</p></td></tr>";
	
//	//违法信息
//	$.each(data.datas, function (key, val) {
//		wfxxcontent += "<tr><th width='20%'>违法时间</th><td width='20%'>" + val.wfsj + "</td><th width='20%'>违法地点</th><td width='20%'>" + val.wfdd + "</td></tr>"+
//					   "<tr><th width='20%'>罚款金额</th><td width='20%'>" + val.fkje + "</td><th width='20%'>是否缴款</th><td width='20%'>" + val.jkbj + "</td></tr>";
//	});
	$("#_showTitle").html(data.title);
	$("#tipTab").find("tr").remove();
	$("#tipTab").append(content);
//	$("#_showWfxxTitle").html("机动车违法信息");
//	$("#wfxxTab").find("tr").remove();
//	$("#wfxxTab").append(wfxxcontent);
}

//推送机动车信息到双屏
function showJDCMessage(datas){
	$("#_jdc").css("display", "");
	var lhpzl = datas.hpzl;
	var lhpzm = datas.hpzm;
	var lclpp1 = datas.clpp1;
	var lcllx = datas.cllx;
	var lzt=datas.zt;
	var ldjrq = datas.djrq;
	var lyxqz = datas.yxqz;
	
	var hpzl = document.getElementById('hpzl');
	hpzl.innerText = lhpzl;
	var hpzm = document.getElementById('hpzm');
	hpzm.innerText = lhpzm;
	var clpp1 = document.getElementById('clpp1');
	clpp1.innerText = lclpp1;
	var cllx = document.getElementById('cllx');
	cllx.innerText = lcllx;
	var yxqz = document.getElementById('yxqz');
	yxqz.innerText = lyxqz;
	var djrq = document.getElementById('djrq');
	djrq.innerText = ldjrq;
	var zt = document.getElementById('zt');
	zt.innerText = lzt;
	
}
//推送驾驶人信息到双屏
function showJSRMessage(datas){
	$("#_jsr").css("display", "");
	var lsfzmhm = datas.sfzmhm;
	var llxzsyzbm = datas.lxzsyzbm;
	var llxzsxxdz = datas.lxzsxxdz;
	var lxm = datas.xm;
	var llxdh = datas.lxdh;
	var lsjhm = datas.sjhm;
	var lbinss = datas.binss;
	
	var lxzsyzbm = document.getElementById('lxzsyzbm');
	lxzsyzbm.innerText = llxzsyzbm;
	var lxzsxxdz = document.getElementById('lxzsxxdz');
	lxzsxxdz.innerText = llxzsxxdz;
	var xm = document.getElementById('xm');
	xm.innerText = lxm;
	var lxdh = document.getElementById('lxdh');
	lxdh.innerText = llxdh;
	var sjhm = document.getElementById('sjhm');
	sjhm.innerText = lsjhm;
	var binss = document.getElementById('binss');
	binss.innerText =lbinss;
}
var loginIpTs;
function sveinDb(data){
	loginIpTs=data.loginIp;
	var msg = $("#base64Photo").val();
	//var msg="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";
	window.svein.recognize(msg,0,15);
	//setMessage("0");
}
//获取sveinDb认证信息
function setMessage(retCode,msg){
	var params = 'loginIP=' + loginIpTs +'&retCode='+retCode+"&nowNumber="+nowNum+"&ywPhoto="+encodeURIComponent(msg);
	$.ajax({type:"POST", cache:false, data:params,
		url:"/queue/number/rzdbResult.action",
		error:function (req) {alert("人证对比【模件出错】！");}
	});
}

var nowNum;
function showDualScreenInfo(data){
	nowNum=data.number;
	isOpenIndexCamera=data.isOpenIndexCamera;
		$("#base64Photo").val("");
		$("#base64Photo").val(data.numphoto);
	if(isOpenIndexCamera==1){
		$("#quhaophoto").css("display", "none");
		document.getElementById('quhaophoto').innerHTML="";
		msg = "<p align='center' style='color: red;font-size: 30px'>当前办理号码为：</p>";
		msg +="<p id='numbers' align='center' style='color: red;font-size: 60px;font-weight: bold'>";
		msg +=data.number;
		msg +="</p>";
	}else{
		//贵港车管所需求：取号人检验
		msg = "<p align='center' style='color: red;font-size: 20px'>当前办理号码为：</p>";
		msg +="<p id='numbers' align='center' style='color: red;font-size: 35px;font-weight: bold'>";
		msg +=data.number;
		msg +="</p>";
		
		if(data.id!=null && data.id!=""){
			$("#quhaophoto").css("display", "");
			document.getElementById('quhaophoto').innerHTML="";
			onestr="'/queue/images/zwtp.jpg'";
			//str ='<img alt="取号人照片" src="http://www.baidu.com/img/bd_logo1.png" >';
		    str ='<img alt="取号人照片" src="/queue/getVQhPhoto.action?id='+data.id+'"onerror="this.src='+onestr+'"style="width:220px;height:190px;border:2px;" >';
			document.getElementById('quhaophoto').innerHTML=str;
		}
	}
	var timeOut="3";//60秒
//	window.svein.recognize(timeOut);
//	$("#numbers").html(data.number);
	$("#div_dqhm").find("p").remove();
	$("#div_dqhm").find("br").remove();
	$("#div_dqhm").append(msg);
}

//验证双屏页面状态
function showDualScreenInfoYZ(datas){
	var yzxx = datas.yzxx;
	if(yzxx == 'cjs'){
//		$.post("/number/spyzxx.action",
//		function(data){
//		});
		
		var pos = window.document.location.href.indexOf(window.document.location.pathname);
		var path = window.document.location.href.substring(0,pos);
		$.ajax({type:"POST", cache:false,dataType: "json",
		        url: path+"/queue/number/spyzxx.action?"+parseInt(Math.random()*100000),
		        success: function(data) {
				}
	        });
	}
	
}

//推送照片
function showDualScreenInfoIMG(datas){
	var image = datas.image;
	var message = datas.message;
	var bj = datas.bj;
	$("#div8").css("display", "none");
	$("#mask_tip").css("display", "");
	document.getElementById('mask_tip').innerHTML="";
	$("#tipTab").find("tr").remove();
	if('1' == bj || '1#3' == bj){
		onestr="'/queue/images/queue/waitnext.jpg'";
				//str ='<img alt="取号人照片" src="http://www.baidu.com/img/bd_logo1.png" >';
		str ='<div align="center" style="font-size: 24px;color: red">'+message+'</div><br/><img align="middle" alt="取号人照片" src="'+image+'"onerror="this.src='+onestr+'"style="width:512px;height:512px;border:1px;" >';
		document.getElementById('mask_tip').innerHTML=str;
	}else if('2' == bj || '2#3' == bj){
		str ='<div align="center" style="font-size: 24px;color: red">'+message+'</div><br/><div id="mask_tip_1" align="center" style="width:512px;height:512px;border:1px;" >';
		document.getElementById('mask_tip').innerHTML=str;
		var qrcode = new QRCode('mask_tip_1', {
		text: image,
		width: 512,
		height: 512,
		colorDark : '#000000',
		colorLight : '#ffffff',
		correctLevel : QRCode.CorrectLevel.H
		});
	}
	if('1' == bj || '2' == bj){
		setTimeout(function(){
			$("#mask_tip").css("display", "");
		document.getElementById('mask_tip').innerHTML="";
	//	onestr="'/queue/images/queue/waitnext.jpg'";
	//	str ='<img align="middle" alt="取号人照片" src="/queue/images/queue/waitnext.jpg" style="width:744px;height:591px;border:1px;" >';
	//	document.getElementById('mask_tip').innerHTML=str;
		$("#div8").css("display", "");
		if ($("#myMarq").text() == "") {
			$("#mask_tip").css("display", "");
		} else {
			$("#mask_tip").css("display", "none");
		}
		}, 30000);
	}else if('1#4' == bj || '2#4' == bj){
		$("#mask_tip").css("display", "");
		document.getElementById('mask_tip').innerHTML="";
	//	onestr="'/queue/images/queue/waitnext.jpg'";
	//	str ='<img align="middle" alt="取号人照片" src="/queue/images/queue/waitnext.jpg" style="width:744px;height:591px;border:1px;" >';
	//	document.getElementById('mask_tip').innerHTML=str;
		$("#div8").css("display", "");
		if ($("#myMarq").text() == "") {
			$("#mask_tip").css("display", "");
		} else {
			$("#mask_tip").css("display", "none");
		}
	}
}

function waiPingXinXi(datas){
	var ckh = datas.ckidh;
	var ckm = datas.ckmch;
	var sx = datas.sxhh;
	alert(ckh);
	
}

//打开选号页面
function showXhym(datas){
	var yzxx = datas.yzxx;
	var http = yzxx.substring(4,5);
	if('s' == http){
		var qian1 = yzxx.split(":");
		var dizhi = "http:"+qian[1]+":9080"+qian1[2].substring(4);
		yzxx = dizhi;
	}
	var features = "height=900px,width=1440px,status=no,resizable=no,top=0,left=0,scrollbars=no,titlebar=no,menubar=no,location=no,toolbar=no,z-look=yes,fullcsreen=yes"
							window.open(encodeURI(encodeURI(yzxx,'utf-8')), "_blank", features);
	
//* oNewWindow：被打开的窗口的对象
//* sUrl：被打开窗口的url
//* sName：在哪个窗口打开新的url链接，例如可以为_blank（新窗口）、_top（最外层窗口）等等
//* sFeatures：对窗口的一些控制属性
//* fullscreen：是否为全屏模式（相当于F11的效果），可取值：yes/1、no/0
//* directories：是否带有目录按钮（例如收藏夹中的’链接’目录），可取值同上
//* location：是否带有地址栏，可取值同上
//* channelmode：是否为影院模式，可取值同上
//* menubar：是否带有菜单条，可取值同上
//* resizable：是否可以改变窗口的尺寸，可取值同上
//* scrollbars：是否带有滚动条，可取值同上
//* status：是否带有状态栏，可取值同上
//* titlebar：是否带有标题栏，可取值同上
//* toolbar：是否带有快捷工具栏，可取值同上
//* height：窗口高度
//* width：窗口宽度
//* top：距屏幕上边缘的距离
//* left：距屏幕左边缘的距离
//* bReplace：如果在同一窗口打开新窗口，该值用于指定是否在history中替换原窗口的url链接，可取值：true/false
}


/* 提交评价 */
function enterEval(evaluResult, sub_flag, reason) {
	window.svein.playMusic(1);
	/*please.muted=true;
	please.play();
	thanks.load();
	thanks.play();*/
	$("#_eval").css("display", "none");
	$("#test").css("display", "none");
	//拍照,sub_flag判断是否拍取 0不拍取
	if (sub_flag == 1) {//手动提交评价
		window.clearInterval(setTimeoutReturn);
		if (isok == 0) {
			//camera();
		}
	}
	
	//flag提交标记(1手动提交2自动);evaluResult评价结果;id工号;base64Code图片base64编码
	var params = 'flag=' + sub_flag + '&evaluResult=' + evaluResult + '&reason=' + reason
		+ /*'&photoBase64=' + encodeURIComponent($("#base64Code").val()) +*/ '&operNum=' + $("#operNum").val()
		+ '&loginIP=' + $("#loginIP").val() + '&stream=' ;//+ encodeURIComponent(obj.HWGetBase64Stream(2));
	$.ajax({type:"POST", cache:false, data:params,
		url:"/queue/number/saveEvalu.action",
		success:function (req) {
			if (req == null || req == "") {
				$("#next").css("display", "none");
			} else {
				$("#next").css("display", "").html("<h5>\u6e29\u99a8\u63d0\u793a</h5>" + req);
				setTimeout("$('#next').css('display', 'none')", 10000);
			}
		}
	});
	$("#base64Code").val("");
	$("#div1").css("display", "none");
	$("#" +
	"").css("display", "");
	if ($("#myMarq").text() == "") {
		$("#mask_tip").css("display", "");
	} else {
		$("#mask_tip").css("display", "none");
	}
	$("#quhaophoto").find("img").remove();
}