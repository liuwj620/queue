﻿function isEmpty(val) {
    return "undefined" == typeof(val) || "" == val || null == val;
}
jQuery.ajaxSetup ({cache:false}) ;
GenerateNumber = (function() {
	var Url = 'localhost:9001';
	var ip = '62.170.100.5'; //M21设备IP地址
	var overTimer = '10'; //超时时间 秒为单位
	var precision = '1'; //精度 0:低 1:中 2:高
	var checkAlive = '0'; //活体验证 0:不打开 1:打开
	var czflag=0;
	var phoneNumber;
    var instance;
    var bsnsTypes; //缓存业务类型
    var agentfalg;//判断是不是读卡器取号
    var isAgen=0;
    var banliTypes=0,banliTypesVo=0;//1为代办人
    var iscywf = 1,cywffs=1;//是否查验违法
    var isOpenTztd=1;
    var cfqhbj=0;//重复取号标记
    var qzbj = 0;//缺纸标记0正常 1警告 2禁止
    var tdtzjdcnum="",tdtzjdctype="";
    var qhstatus; //用于存储该时段是否可以取号的状态
	var ubdywmc = "no";//表单业务名称
    var IDType, IDNumber,IDNumberB="",NameA,nameB,hmdbj=0,Unit="",waitingarea="",SFZnumber,udybd=""; //证件类型及证件号码
    var SFZnumber="",SFZNameA="",SFZSex="",SFZNation="",SFZBorn="",SFZAddress="",SFZPolice="",SFZUserLifeB="",SFZUserLifeE="",SFZBase64="",DYnumber="",DYname="";//用于表单打印传值
    var DBSFZnumber="",DBSFZNameA="",DBSFZSex="",DBSFZNation="",DBSFZBorn="",DBSFZAddress="",DBSFZPolice="",DBSFZUserLifeB="",DBSFZUserLifeE="",DBSFZBase64="",DBDYnumber="",DBDYname="",DBbdbj=0;//用于表单打印代办传值
    var yzdybd = 0;
    var rzdbdlr=1;//人证对比代理人标记1不是0是
    var mainType = "<li><a href='javascript:void(0);' class='btn_ywlx_1' onfocus='this.blur()' onclick='gn.showChildType(\"01\")'></li>"
    	+ "<li><a href='javascript:void(0);' class='btn_ywlx_2' onfocus='this.blur()' onclick='gn.showChildType(\"02\")'></li>"
		+ "<li><a href='javascript:void(0);' class='btn_ywlx_3' onfocus='this.blur()' onclick='gn.showChildType(\"04\")'></li>";
    
    var jdctype = "<div id='jdctype' style='z-index:-1;position:absolute;left:110px;top:200px;'><table width='100%' height='200' border='1' style='background-color:#7AB4E2' cellpadding='0' cellspacing='0'><tr><td>号牌种类:</td><td><div id='hpzl'></div></td><td>号牌号码:</td><td><div id='hphm'></div></td></tr>"
    				+"<tr><td>所有人:</td><td><div id='syr'></div></td><td>状态:</td><td><div id='zt'></div></td></tr>"
    				+"<tr><td>抵押标记:</td><td><div id='dybj'></div></td><td>有效期止:</td><td><div id='yxqz'></div></td></tr>"
    				+"<tr><td>强制报废期止:</td><td><div id='qzbfqz'></div></td><td>登记证书编号:</td><td><div id='djzsbh'></div></td></tr>"
    				+"</table><input type='button' class='button' value='下一步' onclick='gn.checkOk(\"01\")'/></div>";
    
    var jsrtype = "<div id='jsrtype' style='z-index:-1;position:absolute;left:110px;top:200px;'><table width='100%' height='200' border='1' style='background-color:#7AB4E2' cellpadding='0' cellspacing='0'><tr><td>姓名:</td><td><div id='xm'></div></td><td>准驾车型:</td><td><div id='zjcx'></div></td></tr>"
    				+"<tr><td>状态:</td><td><div id='zt'></div></td><td>累计积分:</td><td><div id='ljjf'></div></td></tr>"
    				+"<tr><td>有效期止:</td><td><div id='yxqz'></div></td><td>审验有效期止:</td><td><div id='syyxqz'></div></td></tr>"
    				+"</table><input type='button' class='button' value='下一步' onclick='gn.checkOk(\"02\")'/></div>";
    
    var jdcxinxi = "<div class='inner_fsfz' id='jdctype'><p style='color: red;font-size: 30px;'>请输入查验机动车信息</p>"
						+"<select class='sel1' id='jdcsel'>"
						+"<option value=''>-车辆类型-</option>"
						+"<option value='01'>大型汽车</option>"
						+"<option value='02' selected='selected'>小型汽车</option>"
						+"<option value='15'>挂车</option>"
						+"<option value='16'>教练汽车</option>"
						+"<option value='23'>警用汽车</option></select>"
						+"<p><input id='jdcnum' class='input1'/></p>"
						+"<p><input type='button' value='查询' onclick='jdcbtnOk()'/><img src='images/queue/ok.jpg' style='border: 0px; cursor: pointer;' onclick='jdcbtnOk()' />&nbsp;&nbsp;"
				    	+"<input type='button' value='返回' onclick='jdcbtnCancel()'/><img src='images/queue/cancel.jpg' style='border: 0px; cursor: pointer;' onclick='jdcbtnCancel()' /></p></div>";
    
    var jdcwfquery = "<div class='inner_fsfz' id='jdctype'><p style='color: red;font-size: 30px;'>请输入查验机动车信息</p>"
		+"<select class='sel1' id='jdcsel'>"
		+"<option value=''>-车辆类型-</option>"
		+"<option value='01'>大型汽车</option>"
		+"<option value='02' selected='selected'>小型汽车</option>"
		+"<option value='15'>挂车</option>"
		+"<option value='16'>教练汽车</option>"
		+"<option value='23'>警用汽车</option></select>"
		+"<p><input id='jdcnum' class='input1'/></p>"
		+"<p><img src='images/queue/ok.jpg' style='border: 0px; cursor: pointer;' onclick='jdcwfbtnOk()' />&nbsp;&nbsp;"
    	+"</p></div>";
    
    var tdtzxinxi = "<div class='inner_fsfz' id='jdctype'><p style='color: red;font-size: 30px;'>请输入提档机动车信息</p>"
						+"<select class='sel1' id='tztdjdcsel'>"
						+"<option value=''>-车辆类型-</option>"
						+"<option value='01'>大型汽车</option>"
						+"<option value='02' selected='selected'>小型汽车</option>"
						+"<option value='15'>挂车</option>"
						+"<option value='16'>教练汽车</option>"
						+"<option value='23'>警用汽车</option></select>"
						+"<p><input id='tztdjdcnum' class='input1'/></p>"
						+"<p><img src='images/queue/ok.jpg' style='border: 0px; cursor: pointer;' onclick='tdtzbtnOk()' />&nbsp;&nbsp;"
				    	+"<img src='images/queue/cancel.jpg' style='border: 0px; cursor: pointer;' onclick='tdtzbtnCancel()' /></p></div>";
     
     var phonenumber = "<div class='inner_dxtz' ><p style='color: red;font-size: 30px;'>请输入办理人手机号码</p>"
			+"<p><input id='pnumber' class='input1'/></p>"
			+"<p><img src='images/queue/ok.jpg' style='border: 0px; cursor: pointer;' onclick='messagebtnOk()' />&nbsp;&nbsp;"
	    	+"<img src='images/queue/cancel.jpg' style='border: 0px; cursor: pointer;' onclick='messagebtnCancel()' /></p></div>";
     
     var Wxphonenumber = "<div class='inner_dxtz' ><p style='color: red;font-size: 30px;'>请输入办理人手机号码</p>"
			+"<p><input id='pnumber' class='input1'/></p>"
			+"<p><img src='images/queue/ok.jpg' style='border: 0px; cursor: pointer;' onclick='gn.WxmessagebtnOk()' />&nbsp;&nbsp;"
	    	+"<img src='images/queue/cancel.jpg' style='border: 0px; cursor: pointer;' onclick='gn.WxmessagebtnCancel()' /></p></div>";
     
     var phonenumber2 = "<div class='inner_dxtz' ><p style='color: red;font-size: 30px;'>请输入办理人手机号码</p>"
			+"<p><input id='pnumber' class='input1'/></p></div>";

     var cartype = "<div class='inner_dxtz'><p style='color: red;font-size: 30px;'>请选择查验的车辆类型</p>"
 	 		+"<input id='xc' type='radio' name='cartype' value='0' checked /><a id='xiaoche' style=' font-size:36px;' onclick='xiaoche(this)'>小车</a>"
			+"<input id='dc' type='radio' name='cartype' value='1' checked /><a id='dache' style=' font-size:36px;' onclick='dache(this)'>大车</a>"
//			+"<p style='color: red;font-size: 30px;'>请输入查验的车牌号</p>"
//			+"<p><input id='carnumber' class='input1'/></p>"
			+"<p><img src='images/queue/ok.jpg' style='border: 0px; cursor: pointer;' onclick='cartypebtnOk()' />&nbsp;&nbsp;"
			+"<img src='images/queue/cancel.jpg' style='border: 0px; cursor: pointer;' onclick='messagebtnCancel()' /></p></div>";
     var carnumber;
     var cartype2;
     var jdctypes;
     var jdcnum;
     var jdcwftypes;
     var jdcwfnum;
     //打印申请表的内容 页面加载完成之后会异步加载每个申请表的打印内容
     var jdcbgdjbasqb; //机动车变更登记、备案申请表
     var jdcdydjzybasqb; //机动车抵押登记/质押备案申请表
     var jdcjsrstqksbb;var jdcjsrstqksbbbm; //机动车驾驶人身体情况申报表
     var jdcjsrsttjzm; //机动车驾驶人身体条件证明
     var jdcjszsqb; //机动车驾驶证申报表
     var jdcjybzsqb; //机动车检验标志申请表
     var jdcpzsqb; //机动车牌证申请表
     var jdczczyzxdjzrsqb; //机动车注册、转移、注销登记/转入申请表
     var xcjszgsqb;//校车驾驶资格申请表
     
     //是否进行自动读卡
     var isAutoReadCard;
     //自动读卡次数
     var autoReadCount;
     //预约返回的数据对象用于打印条显示
     var timeQuantum; //预约时间段
     var businessDefName; //预约业务名称
     var reservationUserName; //预约人姓名
     var idNo; //预约人证件号
     var orderForOther; //是否为他人预约  是/否
     var otherIdNo; //他人证件号
     var otherName; //他人姓名
	/*隐藏证件类型*/
	function hidden() {
	    $("#inner_fsfz").css("display", "none");
	    $("#tab_keyboard").css("display", "none");
		$("#tIDType").val("");
	}
	
	/*
	 * 设置证件类型及证件号码并显示业务类型界面
	 * @param type 证件类型
	 * @param number 证件号码
	 */
	function displayType(type, number,NameA,waitingarea) {
	    IDType = type;
	    IDNumber = number;
	    
	    var isSuccess = true;
	    var isChange = false;
	 //   if (isEmpty(bsnsTypes)) {
	        $.ajax({type: "POST", cache: false, async: false, dataType: "json",
		        url: 'number/bsnsType.action?'+parseInt(Math.random()*100000),
		        success: function(request) {
	        		iscywf = request.iscywf;
	        		cywffs = request.cywffs;
	        		isOpenTztd = request.isOpenTztd;
		            if (request.isSuccess) {
		                isChange = true;
		                delete request.isSuccess;
						bsnsTypes = request;
					} else {
						
						//显示身份证取号界面
						hidden();
						$("#type").css("display", "none");
						$("#index").css("display", "");
						$("#backDiv").css("z-index", "-1");
						isSuccess = request.isSuccess;
						window.alert(request.error);
					}
		        },
		        error: function() {
		            isSuccess = false;
		            window.alert("连接服务器失败，请稍候再试");
		        }
		    });
	  //  }
	    if (!isSuccess) { return; }
	    if (isChange) {
	    	//显示驾驶人机动车违法
		    $("#tdiv").empty().html("<div class='tab'>" + mainType + "</div>").css("padding-left", "70px");
		    //不显示驾驶人机动车违法  注释上面一行 放开下面一行
		    //gn.showChildType('01');
	    }
	    $("#index").css("display", "none");
	  	$("#type").css("display", "");
	}
	
	 //显示二级菜单
	function showTwoType(flag){
		if(iscywf=='0'){
			if(cywffs=='0'){//人工查验
				/*显示违法信息*/
				if(flag=='01'){
				var objDiv = $("#tdiv").empty();
				objDiv.html(jdcxinxi);
				this.jdcbtnOk = function(){
					jdctypes = $("#jdcsel").val();
				    jdcnum = $("#jdcnum").val();
					$("#jdcnum").val().replace(/[^A-Za-z0-9_]/g, "");
					objDiv.html(jdctype);
			        objDiv.css("padding-left", "70px");
					 $.ajax({type: "POST", cache: false, async: false, dataType: "json",
					        url: 'number/checkStatus.action?flag='+flag+"&chepainum="+jdcnum+"&jdctypes="+jdctypes,
					        success: function(data) {
						 	if(data.success){
					                $("#hpzl").html(data.hpzl);
					                $("#hphm").html(data.hphm);
					                $("#syr").html(data.syr);
					                $("#zt").html(data.zt);
					                $("#dybj").html(data.dybj);
					                $("#yxqz").html(data.yxqz);
					                $("#qzbfqz").html(data.qzbfqz);
					                $("#djzsbh").html(data.djzsbh);
							 	}else{
							 		check(flag);
							 	}
					        },
					        error: function() {
					            isSuccess = false;
					            window.alert("连接服务器失败，请稍候再试");
					        }
					    });
				    
					}
				
				
				this.jdcbtnCancel = function(){
					check(flag);
				}
				
				}else if(flag=='02'){
					var objDiv = $("#tdiv").empty();
					objDiv.html(jsrtype);
		        	objDiv.css("padding-left", "70px");
				 	$.ajax({type: "POST", cache: false, async: false, dataType: "json",
				        url: 'number/checkStatus.action?flag='+flag+"&IDNumber="+IDNumber,
				        success: function(data) {
					 	if(data.success){
				                $("#xm").html(data.xm);
				                $("#zjcx").html(data.zjcx);
				                $("#zt").html(data.zt);
				                $("#ljjf").html(data.ljjf);
				                $("#yxqz").html(data.yxqz);
				                $("#syyxqz").html(data.syyxqz);
						 	}else{
						 		check(flag);
						 	}
				        },
				        error: function() {
				            isSuccess = false;
				            window.alert("连接服务器失败，请稍候再试");
				        }
				    });
					
				}else{
					check(flag);
				}
				
			}else if(cywffs=='2'){//新接口查询违法
				if(flag=='01'){//机动车违法
					var objDiv = $("#tdiv").empty();
					objDiv.html(jdcwfquery);
					this.jdcwfbtnOk = function(){
						jdcwftypes = $("#jdcsel").val();
					    jdcwfnum = $("#jdcnum").val();
						$("#jdcnum").val().replace(/[^A-Za-z0-9_]/g, "");
						 $.ajax({type: "POST", cache: false, async: false, dataType: "json",
						        url: 'number/checkStatusWf.action?flag='+flag+"&chepainum="+jdcwfnum+"&jdctypes="+jdcwftypes,
						        success: function(data) {
								 	if(data.success){
								 		check(flag);
								 	}else{
								 		alert("所办理机动车状态为:"+data.zt +"\n 不允许办理相关业务!");
								 		gn.returnindex();
								 	}
						        },
						        error: function() {
						            isSuccess = false;
						            window.alert("连接服务器失败，请稍候再试");
						        }
						    });
					    
						}
					}else if(flag=='02'){//驾驶员违法
					 	$.ajax({type: "POST", cache: false, async: false, dataType: "json",
					        url: 'number/checkStatusWf.action?flag='+flag+"&IDNumber="+IDNumber,
					        success: function(data) {
						 		if(data.success){
						 			check(flag);
							 	}else{
							 		alert("所办理驾驶人状态为:"+data.zt +"\n 不允许办理相关业务!");
							 		gn.returnindex();
							 	}
					        },
					        error: function() {
					            isSuccess = false;
					            window.alert("连接服务器失败，请稍候再试");
					        }
					    });
						
					}else{
						check(flag);
					}
			}else{//自动查验
				if(flag=='01'){
				var objDiv = $("#tdiv").empty();
				objDiv.html(jdcxinxi);
				this.jdcbtnOk = function(){
					jdctypes = $("#jdcsel").val();
				    jdcnum = $("#jdcnum").val();
					$("#jdcnum").val().replace(/[^A-Za-z0-9_]/g, "");
					 $.ajax({type: "POST", cache: false, async: false, dataType: "json",
					        url: 'number/checkStatusZD.action?flag='+flag+"&chepainum="+jdcnum+"&jdctypes="+jdctypes,
					        success: function(data) {
							 	if(data.success){
							 		check(flag);
							 	}else{
							 		alert("所办理机动车状态为:"+data.zt +"\n 不允许办理相关业务!");
							 		gn.returnindex();
							 	}
					        },
					        error: function() {
					            isSuccess = false;
					            window.alert("连接服务器失败，请稍候再试");
					        }
					    });
				    
					}
				
				
					this.jdcbtnCancel = function(){
						check(flag);
					}
				
				}else if(flag=='02'){
				 	$.ajax({type: "POST", cache: false, async: false, dataType: "json",
				        url: 'number/checkStatusZD.action?flag='+flag+"&IDNumber="+IDNumber,
				        success: function(data) {
					 		if(data.success){
						 		check(flag);
						 	}else{
						 		alert("所办理驾驶人状态为:"+data.zt +"\n 不允许办理相关业务!");
						 		gn.returnindex();
						 	}
				        },
				        error: function() {
				            isSuccess = false;
				            window.alert("连接服务器失败，请稍候再试");
				        }
				    });
					
				}else{
					check(flag);
				}
			}
		}else{
			check(flag);
		}
		
//		var index = 0 ,newLi="", content1 = "" , content2 = "" , bj=0;
		
//		$.each(bsnsTypes, function(key, obj) {
//			if(!isEmpty(obj.twotype) && obj.twotype == flag){
//				var names = obj.name.split("@");
//					//isOpenTztd 是否开启通知提档 0开启 1关闭
//					if("0"==obj.isOpenTztd){
//         				newLi = "<li><a href='javascript:void(0);' class='btn_ywlx' onfocus='this.blur()' onclick='gn.checkCl(\""+key+"\",\""+obj.id+"\",\""+flag+"\",\""+obj.isOpenZhiWen+"\")'>"+names[0]+" "+names[1]+"<a></li>";
//	         		}else{
//	         			newLi = "<li><a href='javascript:void(0);' class='btn_ywlx' onfocus='this.blur()' onclick='gn.saveZhiWen(\""+key+"\",\""+obj.id+"\",\""+flag+"\",\""+obj.isOpenZhiWen+"\")'>"+names[0]+" "+names[1]+"<a></li>";
//	         		}
//	         		if(4 >= index){content1 += newLi;} 
//	         		if(10 >= index && 5 <= index){content2 += newLi;}
//         			index ++;
//         			bj=1;
//         		}
//	     });
	
//	   if(bj == 0){
//	   		alert("没有可办理业务");
//	   		gn.returns();
//	   }else{
//	   		var divBack ="<table border='0' cellspacing='0' cellpadding='0' class='tab_nav'><tr>"+
//                    "<td><a class='back' onclick='gn.returns()' onfocus='this.blur()'>返回</a></td></tr></table>"
//	   		if (5 >= index) {
//		        objDiv.html("<div class='tab'>" + content1 + "</div>");
//		    } else {
//		        var newDiv1 = "<div class='tab tdiv'>" + content1 + "</div>";
//		        var newDiv2 = "<div class='tab tdiv'>" + content2 + "</div>";
//		        objDiv.html(newDiv1 + newDiv2);
//		        objDiv.css("padding-left", "70px");
//		    }
//		    //返回按钮显示
//		    $("#backDiv").empty().html(divBack);
//		    $("#backDiv").css("z-index", "");
//	   }
		    
	}
   
	function check(flag){
		var objDiv = $("#tdiv").empty();
		var index = 0 ,newLi="", content1 = "" , content2 = "" , bj=0;
		$.each(bsnsTypes, function(key, obj) {
			// 显示驾驶人机动车违法
			if(!isEmpty(obj.twotype) && obj.twotype == flag){
			//不显示驾驶人机动车违法 放开下面两行 注释上面一行
			//if(!isEmpty(obj.twotype)){
			//	flag = obj.twotype;
				 //判断是否空格
				var dispy=isNull(obj.helps);
					if (obj.helps !== undefined  && !dispy){
						dispy="";
					}else{ dispy="none"; }
					
				var helps=enterString(obj.helps);
				var helps=spaceString(helps);
				var names = obj.name.split("@");
				//isOpenTztd 是否开启通知提档 0开启 1关闭
//				if("0"==obj.isOpenTztd){
//     				newLi = "<li><a href='javascript:void(0);' class='btn_ywlx' onfocus='this.blur()' onclick='gn.checkCl(\""+key+"\",\""+obj.id+"\",\""+flag+"\",\""+obj.isOpenZhiWen+"\")'>"+names[0]+" "+names[1]+"<a></li>";
//         		}else{
//         			newLi = "<li><a href='javascript:void(0);' class='btn_ywlx' onfocus='this.blur()' onclick='gn.saveZhiWen(\""+key+"\",\""+obj.id+"\",\""+obj.isOpenZhiWen+"\",\""+flag+"\")'>"+names[0]+" "+names[1]+"<a></li>";
//         		}
				newLi = "<li><a href='javascript:void(0);' class='btn_ywlx' onmouseout='out()' onmouseover='over(\""+helps+"\",\""+dispy+"\")' onfocus='this.blur()' onclick='gn.checkCl(\""+key+"\",\""+obj.id+"\",\""+flag+"\",\""+obj.isOpenZhiWen+"\",\""+obj.isOpenTztd+"\",\""+obj.ismessage+"\",\""+obj.sfkqcyyw+"\",\""+obj.dybd+"\",\""+obj.bdywmc+"\",\""+names[0]+"\")'>"+names[0]+" "+names[1]+"<a></li>";
	         		if(4 >= index){content1 += newLi;} 
	         		if(10 >= index && 5 <= index){content2 += newLi;}
         			index ++;
         			bj=1;
         		}
	     });
		
		if(bj == 0){
	   		alert("没有可办理业务");
	   		gn.returns();
	   }else{
	   		var divBack ="<table border='0' cellspacing='0' cellpadding='0' class='tab_nav'><tr>"+
                    "<td><a class='back' onclick='gn.returnindex()' onfocus='this.blur()'>返回</a></td></tr></table>"
	   		if (5 >= index) {
		        objDiv.html("<div class='tab'>" + content1 + "</div>");
		    } else {
		        var newDiv1 = "<div class='tab tdiv'>" + content1 + "</div>";
		        var newDiv2 = "<div class='tab tdiv'>" + content2 + "</div>";
		        objDiv.html(newDiv1 + newDiv2);
		        objDiv.css("padding-left", "70px");
		    }
		    //返回按钮显示
		    $("#backDiv").empty().html(divBack);
		    $("#backDiv").css("z-index", "");
	   }
		
	}
	//显示帮助信息
	this.over=function(busHelp,dispy){
 		var oBus = $("#h1").empty();
 		var divBus ="<p>"+busHelp+"</p>";
 		oBus.append(divBus);
 		$("#h1").css("display",dispy);
 	//设置跟随鼠标
 	$('#tdiv li').mousemove(function(e) { 
 		var xx = e.originalEvent.x || e.originalEvent.layerX || 0; 
 		var yy = e.originalEvent.y || e.originalEvent.layerY || 0; 
 		$("#h1").css("top",yy).css("left",xx+180);
 	}); 
}
	 this.out=function(){
			$("#h1").css("display","none");
		}
	
	
	
    function constructor() {
        var agentInfo; //缓存代理人信息
	    var credentials; //缓存证件类型
         var readSysCard =SynCardOcx1;// IDCard1;//读卡器类型 0华视 1华旭
       //间隔读取身份证信息
         var idCardInterval;
         
         /*本人办理*/
         this.benren = function(){
        	//检查是否在取号时间段内
         	 var qhstatus = yzqhsj();
         	 if(!qhstatus){
         	 	alert("当前时间段,不能取号");
         	 	return false;
         	 }
         	 //初始化参数  
         	timeQuantum = "";
         	businessDefName = "";
         	reservationUserName = ""; //预约人姓名
            idNo = ""; //预约人证件号
            orderForOther = ""; //是否为他人预约  是/否
            otherIdNo = ""; //他人证件号
            otherName = ""; //他人姓名
			 banliTypesVo = 0;
			 isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";isAutoReadCard=true;autoReadCount=0;
        	 banliTypes=0;
        	 gn.banLi();
        	 showBackBtn();
        	 //验证缺纸
			 gn.yzquez();
			 if(qzbj==1){
				 alert("缺纸警告！");
			 }else if(qzbj==2){
				 alert("请更换打印纸！");
				 gn.returnindex();
			 }
			 gn.startIdCardInterval(); //启动自动读取身份证
			 document.getElementById("msg").innerHTML ="";
         };
         
         /*代办人办理*/
         this.daibanren = function(){
        	//检查是否在取号时间段内
         	 var qhstatus = yzqhsj();
         	 if(!qhstatus){
         	 	alert("当前时间段,不能取号");
         	 	return false;
         	 }
         	 //初始化参数  
         	timeQuantum = "";
         	businessDefName = "";
         	reservationUserName = ""; //预约人姓名
            idNo = ""; //预约人证件号
            orderForOther = ""; //是否为他人预约  是/否
            otherIdNo = ""; //他人证件号
            otherName = ""; //他人姓名
			 banliTypesVo = 0;
			 isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";isAutoReadCard=true;autoReadCount=0;
        	 banliTypes=1;
        	 gn.banLi();
        	 showBackBtn();
        	 //验证缺纸
			 gn.yzquez();
			 if(qzbj==1){
				 alert("缺纸警告！");
			 }else if(qzbj==2){
				 alert("请更换打印纸！");
				 gn.returnindex();
			 }
			 gn.startIdCardInterval(); //启动自动读取身份证
			 document.getElementById("msg").innerHTML ="请放入代办人身份证！";
         };
         
         
         
        /*身份证取号*/
        this.card = function() {
        	//判断是不是代理人
        	var rzdbFlag;
        	if(czflag==0){
        		$.post("number/searchAgent.action?"+parseInt(Math.random()*100000), {flag: '0'}, function(data) {rzdbFlag=data.isrzdb}, "json");
        	}
        	czflag=0;
        	agentfalg=1;
        	document.getElementById("msg").innerHTML ="";
		  	var tempCapt ="";
	    	
			$.ajax({
				type : 'GET',
				url : 'http://' + Url + '/sendReadCertNo',
				data : {
					
					'ip' : ip,
					'overTimer' : overTimer
				},
				dataType : 'jsonp',
				async : true,
				success : function(data) {
					if (data.isSucc) {
						var retMsg = data.Msg.split("|"); //分割
						//身份证信息
					    SFZnumber = retMsg[5];
					    SFZNameA = retMsg[0];
					    SFZSex = retMsg[1];//性别
					    SFZNation = retMsg[2];//民族
					    SFZBorn = retMsg[3];//出生日期
					    SFZAddress = retMsg[4];//地址
					    SFZPolice = retMsg[6];//发证机关
					    SFZUserLifeB = retMsg[7];//有效期开始
					    SFZUserLifeE = retMsg[8];//有效期结束
					    SFZBase64 = retMsg[9];//身份证流
					    var number = SFZnumber;
					    NameA = SFZNameA;
					    upIcardPic(SFZBase64,number);//获取身份证照片显示
					 //验证黑名单
					 gn.validateBlack(number);
					 if(hmdbj == 1){
						alert("此人已在黑名单中存在，无法取号！！！");
								hmdbj = 0;
								return;
					}
					
					 //验证重复取号
						 gn.checkcfqh(number);
						 if(cfqhbj==1){
							 alert("您今天取的号尚未办结，请勿重复取号!");
							 cfqhbj=0;
							 gn.returnindex();
							 return;
						 }
					 //判断代办人
					 if(banliTypes==0){
			  	        if(rzdbFlag==0){
			  				$.ajax({
			  					type : 'GET',
			  					url : 'http://' + Url + '/sendRecongnize',
			  					data : {
			  						'ip' : ip,
			  						'overTimer' : overTimer,
			  						'precision' : precision,
			  						'checkAlive' : checkAlive
			  					},
			  					dataType : 'jsonp',
			  					async : true,
			  					success : function(data) {
			  						if (data.isSucc) {
			  							gn.returns();
							  	        showBackBtn();
							  	        displayType("A", number,NameA);
			  						} else {
			  							//window.alert(data.Msg);
			  							gn.returnindex();
			  						}
			  					},
			  					error : function(data) {
			  						if (null != data && null != data.status) {
			  							alert(data.status + ":请检查服务是否启动");
			  							gn.returnindex();
			  						} else {
			  							alert('请求失败');
			  							gn.returnindex();
			  						}
			  					}
			  				});
			  	        }else{
						gn.returns();
			  	        showBackBtn();
			  	        displayType("A", number,NameA); 
			  	        }
					 }else{
						 IDNumberB = number;
						 nameB = "";
					 	nameB = NameA;
				  	//判断是不是代理人
				  	$.post("number/searchAgent.action?"+parseInt(Math.random()*100000), {IDNumber: number}, function(data) {//改动B
				  	        if (data.isAgnet) {
				  	        	if (data.agentStatus == ""){
						  	        agentInfo = data;
						  	        Unit = agentInfo.nuit;//获取单位
						  	        isAgen = 1;//判断是不是代理人
									 if("0" == rzdbFlag){
							  				$.ajax({
							  					type : 'GET',
							  					url : 'http://' + Url + '/sendRecongnize',
							  					data : {
							  						'ip' : ip,
							  						'overTimer' : overTimer,
							  						'precision' : precision,
							  						'checkAlive' : checkAlive
							  					},
							  					dataType : 'jsonp',
							  					async : true,
							  					success : function(data) {
							  						if (data.isSucc) {
							  							banliTypesVo = banliTypes;
														banliTypes = 0;
														czflag=1;
														hidden();
														document.getElementById("msg").innerHTML ="请放入车主身份证！";
														gn.startIdCardInterval(); //启动自动读取身份证
							  						} else {
							  							//window.alert(data.Msg);
							  							gn.returnindex();
							  						}
							  					},
							  					error : function(data) {
							  						if (null != data && null != data.status) {
							  							alert(data.status + ":请检查服务是否启动");
							  							gn.returnindex();
							  						} else {
							  							alert('请求失败');
							  							gn.returnindex();
							  						}
							  					}
							  				});
										
										
									 }else{
										banliTypesVo = banliTypes;
										banliTypes = 0;
										hidden();
										document.getElementById("msg").innerHTML ="请放入车主身份证！";
										gn.startIdCardInterval(); //启动自动读取身份证
										 
									 }
								 
						  	        
				  	        	}else{
				  	        		alert(data.agentStatus);
				  	        		gn.returnindex();
				  	        		return;
				  	        	}
					  	       
					  	       
					  	       
				  	 		 } else { 
								 if("0" == rzdbFlag){
						  				$.ajax({
						  					type : 'GET',
						  					url : 'http://' + Url + '/sendRecongnize',
						  					data : {
						  						'ip' : ip,
						  						'overTimer' : overTimer,
						  						'precision' : precision,
						  						'checkAlive' : checkAlive
						  					},
						  					dataType : 'jsonp',
						  					async : true,
						  					success : function(data) {
						  						if (data.isSucc) {
						  							isAgen = 0;
													banliTypesVo = banliTypes;
													banliTypes = 0;
													hidden();
													czflag=1;
													document.getElementById("msg").innerHTML ="请放入车主身份证！";
													gn.startIdCardInterval(); //启动自动读取身份证
						  						} else {
						  							window.alert(data.Msg);
						  							gn.returnindex();
						  						}
						  					},
						  					error : function(data) {
						  						if (null != data && null != data.status) {
						  							alert(data.status + ":请检查服务是否启动");
						  							gn.returnindex();
						  						} else {
						  							alert('请求失败');
						  							gn.returnindex();
						  						}
						  					}
						  				});
									
									
								 }else{
									 	isAgen = 0;
										banliTypesVo = banliTypes;
										banliTypes = 0;
										hidden();
										document.getElementById("msg").innerHTML ="请放入车主身份证！";
										gn.startIdCardInterval(); //启动自动读取身份证
								 }
									
				  	  		}
				  	}, "json");
				  	
				  	//代办身份证信息
					    DBSFZnumber = SFZnumber;
					    DBSFZNameA = SFZNameA;
					    DBSFZSex = SFZSex;//性别
					    DBSFZNation = SFZNation;//民族
					    DBSFZBorn = SFZBorn;//出生日期
					    DBSFZAddress = SFZAddress;//地址
					    DBSFZPolice = SFZPolice;//发证机关
					    DBSFZUserLifeB = SFZUserLifeB;//有效期开始
					    DBSFZUserLifeE = SFZUserLifeE;//有效期结束
					    DBSFZBase64 = SFZBase64;//身份证流
					    DBbdbj = 1;
				  		
				  	}
				
					    //abdend
					} else {
						window.alert("身份证阅读器故障失败！");
					}
				},
				error : function(data) {
					if (null != data && null != data.status) {
						window.alert(data.status + ":请检查服务是否启动");
					} else {
						window.alert('人证对比设备故障！');
					}
				}
			});
    
        };
        
        /*非身份证取号,显示证件类型*/
		this.noCard = function() {
		    agentfalg=0;
		    var isSuccess = true;
		    if (isEmpty(credentials)) {
		        $.ajax({type: "POST", cache: false, async: false, dataType: "json",
			        url: 'number/credentials.action?'+parseInt(Math.random()*100000),
			        success: function(request) {
			            if (request.isSuccess) {
							credentials = request.datas;
						} else {
							isSuccess = request.isSuccess;
							window.alert(request.error);
						}
			        },
			        error: function() {
			            isSuccess = false;
			            window.alert("连接服务器失败，请稍候再试");
			        }
			    });
		    }
		    
		    if (!isSuccess) { return; }
		    
		    $("#tIDNumber").val("");
		    var oSelect = $("#tIDType").empty();
			oSelect.append($("<option>", {value: ""}).html("-证件类型-"));
	        $.each(credentials, function(i, obj) {
	            var oOpt = $("<option>", {value: obj.dm}).html(obj.mc);
	            oSelect.append(oOpt);
	        });
	        $("#inner_fsfz").css("display", "");
	        $("#tab_keyboard").css("display", "");
		};
		
		
		/*获取证件类型及证件号码*/
		this.btnOk = function() {
			
		    var type = $("#tIDType").val();
		    var number = $("#tIDNumber").val();
		     document.getElementById("msg").innerHTML ="";
		    if (isEmpty(type) || isEmpty(number)) {
		        window.alert("证件类型及证件号码不能为空，\n请正确填写信息");
		        return;
		    }else if('A'==type || type == "M"){
		    	if(checkIdcardIsOrNo()=="1"){
					window.alert("非法身份证号！");
					return;
				}else if(checkIdcardIsOrNo()=="2"){
					window.alert("非法地区！");
					return;
				}else if(checkIdcardIsOrNo()=="3"){
					window.alert("非法生日！");
					return;
				}
		    }
		    
		    //验证黑名单----begin
			gn.validateBlack(number);
			if(hmdbj == 1){
				alert("此人已在黑名单中存在，无法取号！！！");
				hmdbj = 0;
				return;
			}
			
			//验证重复取号
			 gn.checkcfqh(number);
			 if(cfqhbj==1){
				 alert("您今天取的号尚未办结，请勿重复取号!");
				 cfqhbj=0;
				 gn.returnindex();
				 return;
			 }
			//-------------end
		    
			 //判断代办人
				 if(banliTypes==0){
					  if(rzdbdlr==0){
//					 rzdbjy(IDNumberB);
	  	        	alert("进入人证对比校验");
					$.ajax({type: "POST", cache: false,dataType: "json",async: false,
							url: 'number/rzdbjy.action?IDNumber='+IDNumberB+"&"+parseInt(Math.random()*100000),
							success: function (req) {
							if(req.result == "1"){
								alert(req.msg);
								hidden();
								gn.returnindex();
								}else{
									hidden();
									gn.returns();
						  	        showBackBtn();
	//								agentfalg=1;
	//								isAgen = 0;
						  	        displayType("A", number,NameA);
								}
							}
						});
					}else{
						hidden();
						gn.returns();
			  	        showBackBtn();
	//					agentfalg=1;
	//					isAgen = 0;
			  	        displayType("A", number,NameA); 
					}
					rzdbdlr = 1;  
				 }else{
					 IDNumberB = number;
					 //代办人
//					 if (type == "A" || type == "M") { 
//						var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
//						if (!reg.test(number)) {
//			       			alert("身份证输入不合法");  
//			       			return;
//						}

						//判断是不是代理人
			  	$.post("number/searchAgent.action?"+parseInt(Math.random()*100000), {IDNumber: IDNumberB}, function(data) {//改动B首个
			  	        if (data.isAgnet) {
						  	if (data.agentStatus == ""){
				  	        agentInfo = data;
//							  	        gn.returns();
				  	        Unit = agentInfo.nuit;//获取单位
				  	        isAgen = 1;//判断是不是代理人
				  	        if("0" == data.isrzdb){rzdbdlr = 0;}
//			  	        agentfalg=1;//判断是否为身份证读卡器取号
				  	      //判断是否启用静脉
				  	       if("0" == data.isOpenJm){
				  	            window.alert("请将指定的手指放入指静脉仪进行身份验证！");
				  	            //tempCapt = xhWebFingerCtrl.OnCaptureFinger();
								var stat = xhWebFingerCtrl.OnIdentifyFinger(agentInfo.fingerprint);
						    	if(0 == stat){
						    		
						    		if("0" == data.isrzdb){//代理人人证对比
						    			var db = "/queue/right/qhrzdbym.jsp";
										var features = "status=no,resizable=no,top=0,left=0,scrollbars=no,titlebar=no,menubar=no,location=no,toolbar=no,z-look=yes,fullcsreen=yes"
										window.open(encodeURI(encodeURI(db,'utf-8')), "_blank", features);
							    		
										banliTypesVo = banliTypes;
										banliTypes = 0;
										hidden();
										document.getElementById("msg").innerHTML ="请放入车主身份证！";
										gn.startIdCardInterval(); //启动自动读取身份证
						    		}else{
						    			
									//if (0 == stat && "" != tempCapt ) {
	//									    		gn.returns();
	//									    		showBackBtn();
	//									agentfalg=1;
	//									    	    displayType("A", agentInfo.IDNumber,NameA); 
							    		banliTypesVo = banliTypes;
										banliTypes = 0;
										hidden();
										document.getElementById("msg").innerHTML ="请放入车主身份证！";
										gn.startIdCardInterval(); //启动自动读取身份证
						    		}
						    		
						    		
						    		
								  } else { 
									  gn.returnindex();
									  agentInfo = "";
								  }
							 }else{
								 
								 if("0" == data.isrzdb){
									var db = "/queue/right/qhrzdbym.jsp";
									var features = "status=no,resizable=no,top=0,left=0,scrollbars=no,titlebar=no,menubar=no,location=no,toolbar=no,z-look=yes,fullcsreen=yes"
									window.open(encodeURI(encodeURI(db,'utf-8')), "_blank", features);
									 
	//										 	displayType("A", agentInfo.IDNumber);
									banliTypesVo = banliTypes;
									banliTypes = 0;
									hidden();
									document.getElementById("msg").innerHTML ="请放入车主身份证！";
									gn.startIdCardInterval(); //启动自动读取身份证
									 
								 }else{
									banliTypesVo = banliTypes;
									banliTypes = 0;
									hidden();
									document.getElementById("msg").innerHTML ="请放入车主身份证！";
									gn.startIdCardInterval(); //启动自动读取身份证
								 }
								 
							 }
				  	       		}else{
			  	        		alert(data.agentStatus);
			  	        		gn.returnindex();
			  	        		return;
			  	        	}
				  	       
				  	       
				  	       
				  	       
			  	 		 } else { 
//								  	        gn.returns();
//								  	        showBackBtn();
//								agentfalg=1;
								isAgen = 0;
//								  	        displayType("A", number,NameA); 
								banliTypesVo = banliTypes;
								banliTypes = 0;
								hidden();
								document.getElementById("msg").innerHTML ="请放入车主身份证！";
								gn.startIdCardInterval(); //启动自动读取身份证
			  	  		}
			  	}, "json");
						
						
						
//					  } else {
//						  banliTypesVo = banliTypes;
//							banliTypes = 0;
//							hidden();
//							document.getElementById("msg").innerHTML ="请放入车主身份证！";
//							gn.startIdCardInterval(); //启动自动读取身份证
//					  }
						
				 }
//		    gn.returns();
//		  	showBackBtn();
				 
				//---------------------------------------------
					//验证身份证明是否符合规则
					//---------------------------------------------
					function checkIdcardIsOrNo(){ 
					  var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
					  var iSum = 0;
					  //var info = "";
					  var strIDno = $("#tIDNumber").val();
					  var idCardLength = strIDno.length;
					  if(!/^\d{17}(\d|x)$/i.test(strIDno)&&!/^\d{15}$/i.test(strIDno))
					     return 1; //非法身份证号
					
					  if(aCity[parseInt(strIDno.substr(0,2))]==null)
					     return 2;// 非法地区
					
					  // 15位身份证转换为18位
					  if (idCardLength==15)
					 {
					    sBirthday = "19" + strIDno.substr(6,2) + "-" + Number(strIDno.substr(8,2)) + "-" + Number(strIDno.substr(10,2));
					    var d = new Date(sBirthday.replace(/-/g,"/"))
					    var dd = d.getFullYear().toString() + "-" + (d.getMonth()+1) + "-" + d.getDate();
					    if(sBirthday != dd)
					       return 3; //非法生日
					       strIDno=strIDno.substring(0,6)+"19"+strIDno.substring(6,15);
					       strIDno=strIDno+GetVerifyBit(strIDno);
					  }
					
					  // 判断是否大于2078年，小于1900年
					  var year =strIDno.substring(6,10);
					  if (year<1900 || year>2078 )
					      return 3;//非法生日
					
					  //18位身份证处理
					
					  //在后面的运算中x相当于数字10,所以转换成a
					  strIDno = strIDno.replace(/x$/i,"a");
					
					  sBirthday=strIDno.substr(6,4)+"-"+Number(strIDno.substr(10,2))+"-"+Number(strIDno.substr(12,2));
					  var d = new Date(sBirthday.replace(/-/g,"/"))
					  if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))
					                return 3; //非法生日
					  // 身份证编码规范验证
					  for(var i = 17;i>=0;i --)
					   iSum += (Math.pow(2,i) % 11) * parseInt(strIDno.charAt(17 - i),11);
					  if(iSum%11!=1)
					   return 1;// 非法身份证号
					
					  // 判断是否屏蔽身份证
					  var words = new Array();
					  words = new Array("11111119111111111","12121219121212121");
					
					  for(var k=0;k<words.length;k++){
					   if (strIDno.indexOf(words[k])!=-1){
					       return 1;
					     }
					   }
					       return 0;
					} 
					//15位转18位中,计算校验位即最后一位
					function GetVerifyBit(id){
					        var result;
					        var nNum=eval(id.charAt(0)*7+id.charAt(1)*9+id.charAt(2)*10+id.charAt(3)*5+id.charAt(4)*8+id.charAt(5)*4+id.charAt(6)*2+id.charAt(7)*1+id.charAt(8)*6+id.charAt(9)*3+id.charAt(10)*7+id.charAt(11)*9+id.charAt(12)*10+id.charAt(13)*5+id.charAt(14)*8+id.charAt(15)*4+id.charAt(16)*2);
					        nNum=nNum%11;
					        switch (nNum) {
					           case 0 :
					              result="1";
					              break;
					           case 1 :
					              result="0";
					              break;
					           case 2 :
					              result="X";
					              break;
					           case 3 :
					              result="9";
					              break;
					           case 4 :
					              result="8";
					              break;
					           case 5 :
					              result="7";
					              break;
					           case 6 :
					              result="6";
					              break;
					           case 7 :
					              result="5";
					              break;
					           case 8 :
					              result="4";
					              break;
					           case 9 :
					              result="3";
					              break;
					           case 10 :
					              result="2";
					              break;
					        }
					        //document.write(result);
					        return result;
					} 
					gn.stopIdCardInterval(); //停止判断是否放入身份证
		};
		
		/*隐藏证件类型*/
		this.btnCancel = function() {
			hidden();
		};
		
		/*
		 * 取号
		 * @param id 业务类型编号
		 */
		var isCountSet=0;
		this.createNumber = function(id) {
			//查询身份证的未办理业务
			 $.ajax({type: "POST", cache: false, async: false, dataType: "json",
			        url: "number/wclh.action?IDNumber="+IDNumber+"&"+parseInt(Math.random()*100000),
			        success: function(req) {
				 if(req.status==2){
					if(confirm(req.msg)){
						qhao(id);
					}else{
					//返回按钮隐藏
			  		 $("#backDiv").css("z-index", "-1");
			  		  agentInfo="";
			  		  gn.returnindex();
			  		 //window.location.reload();
				    }
				 }else{
					 qhao(id);
				 }
	 },
	        error: function() {
	            isSuccess = false;
	            window.alert("连接服务器失败，请稍候再试");
	        }
	    });
		};
		
		//取号
		function qhao(id){
			//显示身份证取号界面
			hidden();
			$("#type").css("display", "none");
			$("#index").css("display", "");
		    var obj = eval("bsnsTypes." + id);
		    var a=IDNumberB+","+IDNumber+","+obj.id;
		    var RSA=encode64(a);//只狼
		    //业务笔数及人员类型
			var count = 1, personType = 1;
		    //判断代理人可办的业务及办理笔数
		    if (!isEmpty(agentInfo) && agentInfo.isAgnet) {
			    count = window.prompt("请输入办理业务的笔数", "");
			    if(isNaN(count)){
			    	$("#backDiv").css("z-index", "-1");
			    	alert("请输入数字");
			    	return;
			    } else if(count == null){
			    	count = 1;
			    }
			    //判断取号次数
			    if(banliTypesVo==1){
			    	gn.numberCount(IDNumberB,0,id.substring(1),agentInfo.agentId,count);
			    }else{
//				    gn.numberCount(IDNumber,0,id.substring(1),agentInfo.agentId,count);
			    }
			    personType = 2; //代理人
		    }else{
		    	if(banliTypesVo==1){
		    		gn.numberCount(IDNumberB,1,id.substring(1),"",count);//判断取号次数
		    	}else{
//			    	gn.numberCount(IDNumber,1,id.substring(1),"",count);//判断取号次数
		    	}
		    }
		    if(isCountSet == 1){
		     	agentInfo="";
		     	//返回按钮隐藏
	  		 	$("#backDiv").css("z-index", "-1");
		    	return;
		    }
		    if(!isEmpty(count) && count != 0){
		    	var params = {businessType:obj.id, IDType:IDType, IDNumber:IDNumber,IDNumberB:IDNumberB,nameB:nameB,nameA:NameA, businessCount:count,
					personType:personType, prefix:obj.prefix, code:obj.code, typeName:obj.name, flag:obj.flag,RSA:RSA};
		    	$.post("number/generateNumber.action?PNumber="+phoneNumber+"&jdcnum="+jdcwfnum+"&jdctypes="+jdcwftypes+"&cartype="+cartype2+"&carNumber="+encodeURI(encodeURI(carnumber))+"&"+parseInt(Math.random()*100000), params, function (data, status) {
					if (data.isSuccess) {
						//IDNumberB="";//清空IDNumberB
						if("0" == data.isOpenIndexCamera){//判断是否开启取号拍照功能
							document.getElementById("sxh").value = data.deptCode + data.deptHall + data.serialNum;//给子窗口传值（取号拍照窗口）
							document.getElementById("sxhid").value = data.id;//给子窗口传值（取号拍照窗口）
							var features = "status=no,resizable=yes,top=0,left=0,scrollbars=no," 
					        + "titlebar=no,menubar=no,location=no,toolbar=no,z-look=yes," 
					        + "width=600,height=600";
							var newWin = window.open("index_pz.jsp", "_blank", features);
						}
						//大厅等待人数
						//$('#showWaitRs').html("<font size='5'>大厅等待人数为:"+data.countAll+"人</br></font>");
						var vToday = new Date();
						var vYear = vToday.getFullYear();
						var vMon = vToday.getMonth() + 1;
						var vDay = vToday.getDate();
						var vHours = vToday.getHours();
						var vMinutes = vToday.getMinutes();
						var vSeconds = vToday.getSeconds();
						var vDate = vYear + "-" + (vMon < 10 ? "0" + vMon : vMon) + "-" + (vDay < 10 ? "0" + vDay : vDay);
						var vTime = (vHours < 10 ? "0" + vHours : vHours) + ":" + (vMinutes < 10 ? "0" + vMinutes : vMinutes) +
								":" + (vSeconds < 10 ? "0" + vSeconds : vSeconds);
								
						//截取身份证号
						IDNumber = data.idNumber;
						//其它地方的截取方式
//						IDNumber = IDNumber.substring(0,5)+"****"+IDNumber.substring(14);
						//佛山的截取方式
						IDNumber = "*" + IDNumber.substring(IDNumber.length-6);
						
						
						//获取等待区域
//						$.each(bsnsTypes, function(key, obj) {
//							if(data.businessType == obj.id){
//								if(obj.waitingarea!=""){
//				         			waitingarea = obj.waitingarea;
//				         		}else{
//				         			waitingarea = "等候区域";
//				         		}
//							}
//					     });
						waitingarea = data.waitingarea;
						if(waitingarea=="" || waitingarea == "undefined" || waitingarea==null){
							waitingarea="等待区域";
						}
						//退办信息
						var tb = "";
						tb = data.tb;
						if(tb=='1'){
							tb = "有退办!";
						}else{
							tb="";
						}
						var printInfo = data.str;
						if (!isEmpty(timeQuantum)){
							printInfo.replace("%", "");
						}
						
						if (isEmpty(timeQuantum)) {
							printInfo = printInfo.replace("&timeQuantum", "");
						} else {
							printInfo = printInfo.replace("&timeQuantum", "预约时间 : " + timeQuantum + "<br>");
						}
						
						if (isEmpty(businessDefName)) {
							printInfo = printInfo.replace("&businessDefName", "");
						} else {
							printInfo = printInfo.replace("&businessDefName", "办理业务 :" + businessDefName + "<br>");
						}
						
						if (isEmpty(orderForOther)) {
							printInfo = printInfo.replace("&orderForOther", "");
						} else {
							printInfo = printInfo.replace("&orderForOther", "是否代约 :" + orderForOther + "<br>");
							if ("是" == orderForOther) {
								IDNumber = "*" + otherIdNo.substring(otherIdNo.length-6);
								if (!isEmpty(otherName)) {
									NameA = otherName;
								}
							} else {
								IDNumber = "*" + idNo.substring(idNo.length-6);
								NameA = reservationUserName;
							}
						}
						
						if (isEmpty(reservationUserName)) {
							printInfo = printInfo.replace("&reservationUserName", "申请人姓名：" + NameA + "<br>");
						} else {
							printInfo = printInfo.replace("&reservationUserName", "申请人姓名：" + NameA + "<br>");
						}
						
						if (isEmpty(idNo)) {
							printInfo = printInfo.replace("&idNo", "申请人证件：" + IDNumber + "<br>");
						} else {
							printInfo = printInfo.replace("&idNo", "申请人证件：" + IDNumber + "<br>");
						}
						
						if (isEmpty(nameB)) {
							printInfo = printInfo.replace("&nameB", "");
						} else {
							printInfo = printInfo.replace("&nameB", "代办人姓名 : " + nameB + "<br>");
						}
						
						if (isEmpty(IDNumberB)) {
							printInfo = printInfo.replace("&IDNumberB", "");
						} else {
							printInfo = printInfo.replace("&IDNumberB", "代办人证件号 : " + "*" + IDNumberB.substring(IDNumberB.length-6) + "<br>");
						}
						
						
						//agentfalg 身份证取号1非身份证 0
						if(agentfalg == 1){
							//判断是不是代理人取号 banliTypesVo  选择代理人取号为1 本人取号为0
							if(banliTypesVo==1){
								if(isAgen == 1){//0非代理人 1代理人 isAgen 初始是0 读取到录入代理人信息为1
									printInfo=printInfo.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","单位："+Unit).replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","<font size=3>姓名：</font>"+nameB+"<font size=3>,车主：</font>"+NameA+"").replace("?",waitingarea).replace("~",tb).replace("、",data.serialNum);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";NameA="";hmdbj=0;waitingarea="";
								}else{
									printInfo=printInfo.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","<font size=3>姓名：</font>"+nameB+"<font size=3>,车主：</font>"+NameA+"").replace("?",waitingarea).replace("~",tb).replace("、",data.serialNum);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";NameA="";hmdbj=0;waitingarea="";
								}
								banliTypesVo = 0;
							}else{
								printInfo=printInfo.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","<font size=3>姓名：</font>"+NameA+"").replace("?",waitingarea).replace("~",tb).replace("、",data.serialNum);
								//初始化变量
								isAgen=0;Unit="";IDType="";IDNumber="";NameA="";hmdbj=0;waitingarea="";
							}
						}else{
							if(banliTypesVo==1){
								if(isAgen == 1){//0非代理人 1代理人 
									printInfo=printInfo.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","单位："+Unit).replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","").replace("?",waitingarea).replace("~",tb).replace("、",data.serialNum);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";NameA="";hmdbj=0;waitingarea="";
								}else{
									printInfo=printInfo.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","<font size=3>代办人：</font>"+nameB+"<font size=3>,车主：</font>"+NameA+"").replace("?",waitingarea).replace("~",tb).replace("、",data.serialNum);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";NameA="";hmdbj=0;waitingarea="";
								}
								banliTypesVo = 0;
							}else{
								printInfo=printInfo.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","").replace("?",waitingarea).replace("~",tb).replace("、",data.serialNum);
								//初始化变量
								isAgen=0;Unit="";IDType="";IDNumber="";NameA="";hmdbj=0;waitingarea="";
							}
						}
						
						
						
			            
						SFZNameA = "张三";
						SFZnumber = "14262618980315255x";
						SFZSex = "1";
						SFZAddress = "北京市丰台区XX小区";
						
						DBbdbj = 1;
						DBSFZNameA = "李四";
						DBSFZnumber = "24262618980315255x";
						DBSFZSex = "2";
						DBSFZAddress = "北京市海淀区XX小区";
						if("" != udybd && undefined != udybd && "undefined" != udybd && agentfalg == 0 && yzdybd == 1){
							//有打印内容打开遮罩
							printMsgShow("申请表打印任务发送中...<br>请耐心等待");
							//获取打印内容数组
							var datas = udybd.split("H");
							//开始构建打印表单任务
							var LODOP=getLodop();  
							LODOP.PRINT_INIT("myprint"); //命名打印文档名称
							LODOP.SET_PRINTER_INDEX("-1"); //指定打印设备 默认打印机为-1
							LODOP.SET_PRINT_MODE("CATCH_PRINT_STATUS",true); //开启捕捉打印状态。 会有一定性能影响
							LODOP.SET_PRINT_MODE("DOUBLE_SIDED_PRINT",true);//普通打印机支持双面打印
							//遍历需要打印的表单 并添加打印页
							for(var i=0;i<datas.length;i++){
								//加载打印内容
								var tempPrintInfo = eval(datas[i]); //变量在页面加载完成时加载
								var tempPrintInfobm = null;
								alert("tempPrintInfo="+tempPrintInfo);
								if(datas[i] == 'jdcjsrstqksbb'){
									tempPrintInfobm = eval(datas[i]+"bm");
									alert("tempPrintInfobm="+tempPrintInfobm);
								}
								//填充打印信息
								var tempJQObj = $(tempPrintInfo);
//								alert("tempJQObj="+tempJQObj);
//								var tempbdtitle = tempJQObj.find("#bdtitle");
								
								
						//表单赋值
								tempJQObj.find("#SFZNameA").html(SFZNameA);//姓名
//								tempJQObj.find("#nameA").css('color','red');
								if(SFZSex == 1){
									SFZSex = '男';
								}else if(SFZSex == 2){
									SFZSex = '女';
								}
								tempJQObj.find("#SFZSex").html(SFZSex);//性别
									//身份证号码
								tempJQObj.find("#number_1").html(SFZnumber.substring(0,1));
								tempJQObj.find("#number_2").html(SFZnumber.substring(1,2));
								tempJQObj.find("#number_3").html(SFZnumber.substring(2,3));
								tempJQObj.find("#number_4").html(SFZnumber.substring(3,4));
								tempJQObj.find("#number_5").html(SFZnumber.substring(4,5));
								tempJQObj.find("#number_6").html(SFZnumber.substring(5,6));
								tempJQObj.find("#number_7").html(SFZnumber.substring(6,7));
								tempJQObj.find("#number_8").html(SFZnumber.substring(7,8));
								tempJQObj.find("#number_9").html(SFZnumber.substring(8,9));
								tempJQObj.find("#number_10").html(SFZnumber.substring(9,10));
								tempJQObj.find("#number_11").html(SFZnumber.substring(10,11));
								tempJQObj.find("#number_12").html(SFZnumber.substring(11,12));
								tempJQObj.find("#number_13").html(SFZnumber.substring(12,13));
								tempJQObj.find("#number_14").html(SFZnumber.substring(13,14));
								tempJQObj.find("#number_15").html(SFZnumber.substring(14,15));
								tempJQObj.find("#number_16").html(SFZnumber.substring(15,16));
								tempJQObj.find("#number_17").html(SFZnumber.substring(16,17));
								tempJQObj.find("#number_18").html(SFZnumber.substring(17,18));
								tempJQObj.find("#SFZAddress").html(SFZAddress);//地址
								tempJQObj.find("#birthdate").html(SFZnumber.substring(6,14));//生日
									//复选框选择
								var ubdywmcVo = ubdywmc.split("H");
								for(var j=0;j<ubdywmcVo.length;j++){
									tempJQObj.find("#"+ubdywmcVo[j]).attr("checked","checked");
								}
									//代办人标记复选框
								if(DBbdbj == 0){
									tempJQObj.find("#brsq").attr("checked","checked");
								}else if(DBbdbj == 1){
									tempJQObj.find("#dlsq").attr("checked","checked");
								}
								tempJQObj.find("#wtNameA").val(DBSFZNameA); //DBSFZNameA);
								tempJQObj.find("#dlrjhrxm").html(DBSFZNameA);//代理人监护人姓名
								tempJQObj.find("#DBSFZnumber").html(DBSFZnumber);//代理人身份证号码
								tempJQObj.find("#DBSFZAddress").html(DBSFZAddress);//代理人身份证号码
								
								
								
								
								
								
								
								
								var dayin = 
									tempJQObj[0].outerHTML;
								alert(dayin);
//						
								//构建新的打印页
								LODOP.NewPage(); 
								LODOP.ADD_PRINT_HTM('0%','0%','100%','100%',dayin); //指定打印内容及打印标签
								if(tempPrintInfobm != null){
									alert(tempPrintInfobm);
									LODOP.NewPage();
									LODOP.ADD_PRINT_HTM('0%','0%','100%','100%',tempPrintInfobm);
								}
								
							}
							//调用打印
//							var printPreview = LODOP.PREVIEW(); //调用预览
							var printJob = LODOP.PRINT(); //调用打印 返回打印Job代码
							//查询打印是否完成
							var print_ok = LODOP.GET_VALUE("PRINT_STATUS_OK",printJob); //返回打印状态  1-成功 0-不成功 int
							var print_exist = LODOP.GET_VALUE("PRINT_STATUS_EXIST",printJob); //1-在 0-不在,可能已打完或未打而删除 int
							var print_status_id = LODOP.GET_VALUE("PRINT_STATUS_ID",printJob); //获得该JOB当前打印状态代码 string
							var print_total_page = LODOP.GET_VALUE("PRINT_STATUS_TOTAL_PAGES",printJob); //打印总页数 string
							var print_pages_printed = LODOP.GET_VALUE("PRINT_STATUS_PAGES_PRINTED",printJob); //已打印页数 string
							var print_seconds = LODOP.GET_VALUE("PRINT_STATUS_SECONDS",printJob); //打印持续时间(单位:秒) int
							//alert("打印完成:"+print_ok+" 是否还在队列:"+print_exist+" 打印状态码:"+print_status_id+" 打印总页数:"+print_total_page+" 已打印页数:"+print_pages_printed+" 打印持续时间:"+print_seconds);
							var tempPage = 0; //记录页面显示的页数
							//每秒轮询一次 检查是否打印完成
							var printSetInterval = setInterval(function(){
								print_pages_printed = LODOP.GET_VALUE("PRINT_STATUS_PAGES_PRINTED",printJob); //已打印页数 string
								if(tempPage == 0||tempPage!=parseInt(print_pages_printed)){
									tempPage = parseInt(print_pages_printed);
									$("#printInfo").html("正在打印第 "+tempPage+" 页申请表...<br>请耐心等待");
								}
								//判断打印完成的条件
								print_ok = LODOP.GET_VALUE("PRINT_STATUS_OK",printJob); //返回打印状态  1-成功 0-不成功 int
								if(print_ok==1){
									window.clearInterval(printSetInterval);
									//关闭遮罩
									printMsgHidden();
								}
								print_seconds = LODOP.GET_VALUE("PRINT_STATUS_SECONDS",printJob); //打印持续时间(单位:秒) int
//								//判断打印超时的条件
								if(print_seconds>60){
									window.clearInterval(printSetInterval);
									//关闭遮罩
									printMsgHidden();
								}
								
							},1000)
						}
						yzdybd = 0;
						udybd = "";
						ubdywmc = "no";
						SFZnumber="",SFZNameA="",SFZSex="",SFZNation="",SFZBorn="",SFZAddress="",SFZPolice="",SFZUserLifeB="",SFZUserLifeE="",SFZBase64="",DYnumber="",DYname="";//用于表单打印传值
    					DBSFZnumber="",DBSFZNameA="",DBSFZSex="",DBSFZNation="",DBSFZBorn="",DBSFZAddress="",DBSFZPolice="",DBSFZUserLifeB="",DBSFZUserLifeE="",DBSFZBase64="",DBDYnumber="",DBDYname="",DBbdbj=0;//用于表单打印代办传值
						//小票信息
						var bgObj=document.createElement("div");
		                    bgObj.setAttribute('id','page1');
		                    bgObj.style.position="absolute";
		                    bgObj.style.width="300px";
		                    bgObj.style.height="300px";
		                document.body.appendChild(bgObj);
						$('#page1').html(printInfo);
//						var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
////						LODOP=getLodop();  
//						LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单一");
//						LODOP.ADD_PRINT_HTM(88,200,350,600,document.getElementById("page1").innerHTML);
//						LODOP.PREVIEW();
//						LODOP.PRINT();
						
						
						var myDoc = {
							settings:{topMargin:10,
					                  leftMargin:10,
					                  bottomMargin:10,
					                  rightMargin:10},
	         		        documents: document,
	         		        copyrights: '杰创软件拥有版权  www.jatools.com'
	                 	};
						jatoolsPrinter.print(myDoc ,false);
			           
						window.setTimeout("$('#page1').remove()", 4000);
					} else { window.alert(data.error); }
				}, "json");
		    	jdcwftypes="";
		    	jdcwfnum="";
		    }
			//返回按钮隐藏
	  		 $("#backDiv").css("z-index", "-1");
	  		  agentInfo="";
	  		  gn.returnindex();
	  		 //window.location.reload();
		};
		/*var isCountSet=0;
		this.createNumber = function(id) {
			//显示身份证取号界面
			hidden();
			$("#type").css("display", "none");
			$("#index").css("display", "");
		    var obj = eval("bsnsTypes." + id);
		    //业务笔数及人员类型
			var count = 1, personType = 1;
		    //判断代理人可办的业务及办理笔数
		    if (!isEmpty(agentInfo) && agentInfo.isAgnet) {
			    count = window.prompt("请输入办理业务的笔数", "");
			    if(isNaN(count)){
			    	$("#backDiv").css("z-index", "-1");
			    	alert("请输入数字");
			    	return;
			    } else if(count == null){
			    	count = 1;
			    }
			    //判断取号次数
			    if(banliTypesVo==1){
			    	gn.numberCount(IDNumberB,0,id.substring(1),agentInfo.agentId,count);
			    }else{
				    gn.numberCount(IDNumber,0,id.substring(1),agentInfo.agentId,count);
			    }
			    personType = 2; //代理人
		    }else{
		    	if(banliTypesVo==1){
		    		gn.numberCount(IDNumberB,1,id.substring(1),"",count);//判断取号次数
		    	}else{
			    	gn.numberCount(IDNumber,1,id.substring(1),"",count);//判断取号次数
		    	}
		    }
		    if(isCountSet == 1){
		     	agentInfo="";
		     	//返回按钮隐藏
	  		 	$("#backDiv").css("z-index", "-1");
		    	return;
		    }
		    if(!isEmpty(count) && count != 0){
		    	
		    	var params = {businessType:obj.id, IDType:IDType, IDNumber:IDNumber,IDNumberB:IDNumberB, businessCount:count,
					personType:personType, prefix:obj.prefix, code:obj.code, typeName:obj.name, flag:obj.flag};
				$.post("number/generateNumber.action?PNumber="+phoneNumber+"&"+parseInt(Math.random()*100000), params, function (data, status) {
					if (data.isSuccess) {
						IDNumberB="";//清空IDNumberB
						if("0" == data.isOpenIndexCamera){//判断是否开启取号拍照功能
							document.getElementById("sxh").value = data.deptCode + data.deptHall + data.serialNum;//给子窗口传值（取号拍照窗口）
							document.getElementById("sxhid").value = data.id;//给子窗口传值（取号拍照窗口）
							var features = "status=no,resizable=yes,top=0,left=0,scrollbars=no," 
					        + "titlebar=no,menubar=no,location=no,toolbar=no,z-look=yes," 
					        + "width=600,height=600";
							var newWin = window.open("index_pz.jsp", "_blank", features);
						}
						//大厅等待人数
						$('#showWaitRs').html("<font size='5'>大厅等待人数:"+data.countAll+"人</br></font>");
						var vToday = new Date();
						var vYear = vToday.getFullYear();
						var vMon = vToday.getMonth() + 1;
						var vDay = vToday.getDate();
						var vHours = vToday.getHours();
						var vMinutes = vToday.getMinutes();
						var vSeconds = vToday.getSeconds();
						var vDate = vYear + "-" + (vMon < 10 ? "0" + vMon : vMon) + "-" + (vDay < 10 ? "0" + vDay : vDay);
						var vTime = (vHours < 10 ? "0" + vHours : vHours) + ":" + (vMinutes < 10 ? "0" + vMinutes : vMinutes) +
								":" + (vSeconds < 10 ? "0" + vSeconds : vSeconds);
								
						//截取身份证号
						IDNumber = data.idNumber;
						IDNumber = IDNumber.substring(0,5)+"****"+IDNumber.substring(14);
						//获取等待区域
//						$.each(bsnsTypes, function(key, obj) {
//							if(data.businessType == obj.id){
//								if(obj.waitingarea!=""){
//				         			waitingarea = obj.waitingarea;
//				         		}else{
//				         			waitingarea = "等候区域";
//				         		}
//							}
//					     });
						waitingarea = data.waitingarea;
						if(waitingarea=="" || waitingarea == "undefined" || waitingarea==null){
							waitingarea="等待区域";
						}
						//退办信息
						var tb = "";
						tb = data.tb;
						if(tb=='1'){
							tb = "有退办!";
						}else{
							tb="";
						}
						
						
						
						if(agentfalg == 1){
							//判断是不是代理人取号
							if(banliTypesVo==1){
								if(isAgen == 1){//0非代理人 1代理人 
									var printInfo=data.str.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","单位："+Unit).replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","<font size=3>姓名：</font>"+nameB+"<font size=3>,车主：</font>"+NameA+"").replace("?",waitingarea).replace("~",tb);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";
								}else{
									var printInfo=data.str.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","<font size=3>姓名：</font>"+nameB+"<font size=3>,车主：</font>"+NameA+"").replace("?",waitingarea).replace("~",tb);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";
								}
								banliTypesVo = 0;
							}else{
								var printInfo=data.str.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","<font size=3>姓名：</font>"+NameA+"").replace("?",waitingarea).replace("~",tb);
								//初始化变量
								isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";
							}
						}else{
							if(banliTypesVo==1){
								if(isAgen == 1){//0非代理人 1代理人 
									var printInfo=data.str.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","单位："+Unit).replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","").replace("?",waitingarea).replace("~",tb);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";
								}else{
									var printInfo=data.str.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","").replace("?",waitingarea).replace("~",tb);
									//初始化变量
									isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";
								}
								banliTypesVo = 0;
							}else{
								var printInfo=data.str.replace("@",data.queueName).replace("*",data.serialNum).replace("#",data.peoNum).replace("+","").replace("!",count+"笔").replace("$",vDate+" "+vTime).replace("^",IDNumber).replace("%","").replace("?",waitingarea).replace("~",tb);
								//初始化变量
								isAgen=0;Unit="";IDType="";IDNumber="";IDNumberB="",NameA="";nameB="";hmdbj=0;waitingarea="";
							}
						}
						//小票信息
						var bgObj=document.createElement("div");
		                    bgObj.setAttribute('id','page1');
		                    bgObj.style.position="absolute";
		                    bgObj.style.width="300px";
		                    bgObj.style.height="300px";
		                document.body.appendChild(bgObj);
						$('#page1').html(printInfo);
						var myDoc = {
							settings:{topMargin:10,
					                  leftMargin:10,
					                  bottomMargin:10,
					                  rightMargin:10},
	         		        documents: document,
	         		        copyrights: '杰创软件拥有版权  www.jatools.com'
	                 	};
						jatoolsPrinter.print(myDoc ,false);
			           
						window.setTimeout("$('#page1').remove()", 1000);
					} else { window.alert(data.error); }
				}, "json");
		    }
			//返回按钮隐藏
	  		 $("#backDiv").css("z-index", "-1");
	  		  agentInfo="";
	  		  gn.returnindex();
	  		 //window.location.reload();
		};*/
		
			//办理页面
		function banLiYeMian(){
			$("#indexBL").css("display", "none");
			$("#index").css("display", "");
		}
		
		//返回上一页
		function returnInit(){
			 $("#tdiv").empty().html("<div class='tab'>" + mainType + "</div>").css("padding-left", "70px");
		     $("#index").css("display", "none");
	  		 $("#type").css("display", "");
	  		 showBackBtn();
	    }
		//显示返回按钮
		function showBackBtn(){
			var divBackIndex ="<table border='0' cellspacing='0' cellpadding='0' class='tab_nav'><tr>"+
                    "<td><a class='back' onclick='gn.returnindex()' onfocus='this.blur()'>返回</a></td></tr></table>"
            //返回按钮显示
//			$("#index").css("display", "none");
		    $("#backDiv").empty().html(divBackIndex);
		    $("#backDiv").css("z-index", "");
		}
		
		function rzdbjy(IDNumberB){
			alert("进入人证对比校验");
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'number/rzdbjy.action?IDNumber='+IDNumberB+"&"+parseInt(Math.random()*100000),
					success: function (req) {
					if(req.result != "1"){
						alert(req.msg);
						gn.returnindex();
						}
					}
				});
		}
		
		//返回首页
		function backIndex(){
			hidden();
			$("#type").css("display", "none");
			$("#index").css("display", "");
			$("#backDiv").css("z-index", "-1");
			$("#indexBL").css("display", "");
			$("#indexBL2").css("display", "none");
			$("#index").css("display", "none");
			$("#WYGetNumber").css("display", "none");
			gn.stopIdCardInterval(); //停止判断是否放入身份证
			$.ajax({
				type : 'GET',
				url : 'http://' + Url + '/sendCancle',
				data : {
					'ip' : ip
				},
				dataType : 'jsonp',
				async : true,
				success : function(data) {
					//alert(data.Msg)
				},
				error : function(data) {
					if (null != data && null != data.status) {
						//alert(data.status + ":请检查服务是否启动");
					} else {
						//alert('中断异常读卡故障！');
					}
				}
			});
			
		}
		
		//根据身份证获取本月的取号次数
		function numberCount(IDNumber,isAnget,typeId,agentId,count){
				$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'number/countSet.action?IDNumber='+IDNumber+"&isAnget="+isAnget+"&typeId="+typeId+"&agentId="+agentId+"&count="+count+"&"+parseInt(Math.random()*100000),
					success: function (req) {
					if(req.msg != "true"){
						alert(req.msg);
						gn.returnindex();
					}
					isCountSet=req.countcs;
					}
				});
		}
		
		 function encode64(input){
		    	// base64加密开始  
		        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"  
		                + "wxyz0123456789+/" + "=";  
		          
		        var output = "";  
		        var chr1, chr2, chr3 = "";  
		        var enc1, enc2, enc3, enc4 = "";  
		        var i = 0;  
		        do {  
		            chr1 = input.charCodeAt(i++);  
		            chr2 = input.charCodeAt(i++);  
		            chr3 = input.charCodeAt(i++);  
		            enc1 = chr1 >> 2;  
		            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);  
		            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);  
		            enc4 = chr3 & 63;  
		            if (isNaN(chr2)) {  
		                enc3 = enc4 = 64;  
		            } else if (isNaN(chr3)) {  
		                enc4 = 64;  
		            }  
		            output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)  
		                    + keyStr.charAt(enc3) + keyStr.charAt(enc4);  
		            chr1 = chr2 = chr3 = "";  
		            enc1 = enc2 = enc3 = enc4 = "";  
		        } while (i < input.length);  
		  
		        return output;  
		    }  
		//验证黑名单
		function yzhmd(IDNumber){
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'number/validateBlack.action?IDNumber='+IDNumber+"&"+parseInt(Math.random()*100000),
					success: function (req) {
						if(req.hmdflag == true){
							hmdbj = 1;
						}
						if(req.addhmd == true){
							addhmd(IDNumber);
						}
					}
				});	
		}
		
		//自动添加黑名单
		function addhmd(IDNumber){
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'hmd/addHmdZ.action?IDNumber='+IDNumber+"&"+parseInt(Math.random()*100000),
					success: function (req) {
						console.log("自动添加黑名单成功！");
					}
				});	
		}
		
		//验证缺纸
		function yzquezhi(){
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'zzj/YzZzj.action?'+parseInt(Math.random()*100000),
					success: function (req) {
						if(req.flag == '0'){
							qzbj = 0;
						}else if(req.flag == '1'){
							qzbj = 1;
						}else if(req.flag == '2'){
							qzbj = 2;
						}
						
						
						if(req.yzdybd == "1"){
							yzdybd = 1;
						}else{
							yzdybd = 0;
						}
					}
				});	
		}
		
		//验证重复取号
		function yzcfqh(IDnumber){
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'number/yzcfqh.action?IDNumber='+IDnumber+"&"+parseInt(Math.random()*100000),
					success: function (req) {
						if(req.cfqh == true){
							cfqhbj = 1;
						}
					}
				});
		}
		
		//是否开启通知提档或短信通知
		function zxcl(key,id,flag,isOpenZhiWen,isOpenTztd,ismessage,sfkqcyyw,dybd,bdywmc,namesCY){
			udybd = dybd;
			ubdywmc = bdywmc;
			 jdctypes = "";
			 jdcnum ="";
			var objDiv = $("#tdiv").empty();
			
			if(ismessage==0 && isOpenTztd==0){
				objDiv.html(tdtzxinxi+phonenumber2);
				this.tdtzbtnOk =function(){
					jdctypes = $("#tztdjdcsel").val();
				    jdcnum = $("#tztdjdcnum").val();
				    phoneNumber = $("#pnumber").val();
				    savaTztd(jdcnum,key,id,jdctypes,isOpenZhiWen,flag);
				}
				this.tdtzbtnCancel = function(){
					check(flag);
				}
			}else if(isOpenTztd==0){
				objDiv.html(tdtzxinxi);
				this.tdtzbtnOk =function(){
					jdctypes = $("#tztdjdcsel").val();
				    jdcnum = $("#tztdjdcnum").val();
				    savaTztd(jdcnum,key,id,jdctypes,isOpenZhiWen,flag);
				}
				this.tdtzbtnCancel = function(){
					check(flag);
				}
		    }else if(ismessage==0 && sfkqcyyw==0){
		    	objDiv.html(phonenumber);
		    	this.messagebtnOk =function(){
					phoneNumber = $("#pnumber").val();
					if(namesCY.indexOf("查验")>=0){
					objDiv.html(cartype);
					this.cartypebtnOk = function(){
						cartype2 = $("input[name='cartype']:checked").val();
						//carnumber =$("#carnumber").val();
						carnumber ='A001';
						if(isEmpty(carnumber)){
							window.alert("请填写车牌号");
							return;
						}
						gn.saveZhiWen(key,id,isOpenZhiWen,flag);
					}
					this.cartypebtnCancel = function(){
						check(flag);
					}
					}else{
						gn.saveZhiWen(key,id,isOpenZhiWen,flag);
					}
				}
		    	this.messagebtnCancel = function(){
					check(flag);
				}
		    }else if(sfkqcyyw==0 && namesCY.indexOf("查验")>=0){
				objDiv.html(cartype);
				this.cartypebtnOk = function(){
					cartype2 = $("input[name='cartype']:checked").val();
//					carnumber =$("#carnumber").val();
					carnumber='A001';
					if(isEmpty(carnumber)){
						window.alert("请填写车牌号");
						return;
					}
					gn.saveZhiWen(key,id,isOpenZhiWen,flag);
				}
				this.cartypebtnCancel = function(){
					check(flag);
				}
			}else if(ismessage==0){
				objDiv.html(phonenumber);
				this.messagebtnOk =function(){
					phoneNumber = $("#pnumber").val();
				    gn.saveZhiWen(key,id,isOpenZhiWen,flag);
				}
				this.messagebtnCancel = function(){
					check(flag);
				}
			}else{
//				savaTztd(jdcnum,key,id,jdctypes,isOpenZhiWen);
				gn.saveZhiWen(key,id,isOpenZhiWen,flag);
			}
		
		
		}
		
		//保存指纹信息
		function saveZwBase64(key,id,flag,zwbase64){
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'number/saveZWBase64.action?idNumber='+IDNumber+'&zwbase64='+encodeURIComponent(zwbase64)+"&"+parseInt(Math.random()*100000),
					success: function (req) {
						if(req.res == true){
							$("#zhiwenDiv").css("display","none");
							gn.createNumber(key,id);
							
						}else{
							alert("指静脉信息保存失败!!!");
							return;
						}
					}
					
				});	
		}
		
		function savaTztd(jdcnum,key,id,jdctypes,isOpenZhiWen,flag){
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
				url: 'number/saveTztd.action?IDNumber='+jdcnum+"&flag="+jdctypes+"&"+parseInt(Math.random()*100000),
					success: function (req) {
						if("true"==req.msg){
							gn.saveZhiWen(key,id,isOpenZhiWen,flag);
						}else{
							alert("失败");
					}
				}
			});	
		}
		
		function savaPnum(key,id,isOpenZhiWen,flag,num){
			$.ajax({type: "POST", cache: false,dataType: "json",async: false,
				url: 'number/saveTztd.action?IDNumber='+num+"&"+parseInt(Math.random()*100000),
					success: function (req) {
						if("true"==req.msg){
							gn.saveZhiWen(key,id,isOpenZhiWen,flag);
						}else{
							alert("失败");
					}
				}
			});	
		}
		//验证当前取号时间是否在取号时间段内
		function yzqhsj(){
			var yzqhsj;
			$.ajax({
				type:"post",
				cache:false,
				async:false,
				dataType:"text",
				url:"number/yzqhsj.action",
				success:function(req){
					if(req=="true"){
						yzqhsj = true;
					}else{
						yzqhsj = false;
					}
				}
			})
			if(yzqhsj == true){
				return true;
			}else{
				return false;	
			}
		}
		//判断取号量是否超过设置的最大取号量
		function pdqhl(id){
			var result;
			$.ajax({
				type:"post",
				cache:false,
				dataType:"json",
				data:{ywid:id},
				async:false,
				url:"number/pdqhl.action",
				success:function(data){
					if(data==true){
						result = [true];
					}else if(data==false){
						result = [false,data.msg];
					}
				}
			});
			return result;
		}
		
		
		this.banLi = function(){
			banLiYeMian();
		};
		
		this.clears = function(obj) {
//			obj.value = obj.value.replace(/[^A-Za-z0-9_]/g, "");
		};
				
		this.showChildType = function(key) {
			showTwoType(key);
		};
		this.checkOk = function(flag){
			check(flag);
		};
		this.returns =function(){
			returnInit();
		}
		this.returnindex =function(){
			backIndex();
		}
		this.checkCl =function(key,id,flag,isOpenZhiWen,isOpenTztd,ismessage,sfkqcyyw,dybd,bdywmc,namesCY){
			var result = pdqhl(id);
			if(result[0]){
				zxcl(key,id,flag,isOpenZhiWen,isOpenTztd,ismessage,sfkqcyyw,dybd,bdywmc,namesCY);
			}else{
				//alert(result[1]);
				alert("由于取号人数超过该业务每日受理数量,暂停取号");
				backIndex();
			}
		}
		this.numberCount=function(IDNumber,isAnget,typeId,agentId,count){
			numberCount(IDNumber,isAnget,typeId,agentId,count);
		}
		this.saveZhiWen=function(key,id,isOpenZhiWen,flag){
			//判断是否录入指纹
			if(0 == isOpenZhiWen){
				//判断是否需要保存指纹
				$.ajax({type: "POST", cache: false,dataType: "json",async: false,
					url: 'number/getZWBase64ByIdNumber.action?idNumber='+IDNumber,
						success: function (req) {
						$("#zwbase64").val("");
							if(req.res == true){//true需要录入 false不需要
									$("#zhiwenDiv").css("display","");
										zwctl.CloseZwDev();//关闭指纹仪
										///初始化采集指纹
										if( true == zwctl.InitZwDev()){
											zwctl.StartCaptureZw("1");//开始采集指纹
											alert("指静脉信息采集中，请将食指放在指静脉仪上，采集完成后点击确定！");
											//isLrZHiWen(key,id,flag);
										}else{
											alert("初始化设备失败");
											return;
										}
									
							}else if(req.res == false){alert("再次");
								if(confirm("指静脉信息已经存在，是否再次采集！！！")){
									$("#zhiwenDiv").css("display","");
									zwctl.CloseZwDev();//关闭指纹仪
									//初始化采集指纹
										if( true == zwctl.InitZwDev()){
											zwctl.StartCaptureZw("1");//开始采集指纹
										alert("指静脉信息采集中，请将食指放在指纹采集器上，采集完成后点击确定！");
										//isLrZHiWen(key,id,flag);
										}else{
											alert("初始化设备失败");
											return;
										}
								}else{
									//gn.createNumber(key,id);
								}
							}
					}
				});	
			}else{
				gn.createNumber(key,id);
			}
		}
		
		this.validateBlack = function (IDNumber){
			yzhmd(IDNumber);
		}
		this.yzquez = function(){
			yzquezhi();
		}
		this.checkcfqh = function(IDNumber){
			yzcfqh(IDNumber);
		}
		this.bcTztd = function (jdcnum,key,id,jdctypes,isOpenZhiWen){
			savaTztd(IDNumber,key,id,flag,isOpenZhiWen);
		}
		this.validateZw = function (key,id,flag,baseVal){
			saveZwBase64(key,id,flag,baseVal);
		}
		this.wybl = function(flag){
			SFZnumber="";SFZNameA="";SFZSex="";SFZNation="";SFZBorn="";SFZAddress="";SFZPolice="";SFZUserLifeB="";SFZUserLifeE="";SFZBase64="";agentfalg="";
			if(flag=='0'){ //网约身份证取号
				agentfalg=1;
				if (0 >= readSysCard.FindReader()) {
			  		window.alert("没有找到读卡器");
			  		return;
			  	}
			  	var nRet = readSysCard.ReadCardMsg();
			  	var tempCapt ="";
			  	if (0 == nRet) {
			  		
			  		SFZnumber = readSysCard.CardNo;
				    SFZNameA = readSysCard.NameA;
				    SFZSex = readSysCard.Sex;//性别
				    SFZNation = readSysCard.Nation;//民族
				    SFZBorn = readSysCard.Born;//出生日期
				    SFZAddress = readSysCard.Address;//地址
				    SFZPolice = readSysCard.Police;//发证机关
				    SFZUserLifeB = readSysCard.UserLifeB;//有效期开始
				    SFZUserLifeE = readSysCard.UserLifeE;//有效期结束
				    SFZBase64 = readSysCard.Base64Photo;//身份证流
			  		
			  		if (banliTypes == 1) {
			  			IDNumberB = readSysCard.CardNo;
			  			nameB = readSysCard.NameA;
			  			banliTypesVo = 1;
			  			banliTypes = 0;
			  			document.getElementById("msg2").innerHTML ="请放入车主身份证！";
			  			return;
			  		} else {
			  			IDNumber = readSysCard.CardNo;
			  			NameA = readSysCard.NameA;
			  		}
			  		
			  		IDType = 'A';
					//NameA = readSysCard.NameA;
					SynCardOcx1.SetPhotoType(2);//用base64
					//upIcardPic(readSysCard.Base64Photo,number);//获取身份证照片显示
					 //验证黑名单
					gn.validateBlack(IDNumber);
					if(hmdbj == 1){
						alert("此人已在黑名单中存在，无法取号！！！");
						hmdbj = 0;
						return;
					}
					
					//验证重复取号
					gn.checkcfqh(IDNumber);
					if(cfqhbj==1){
						alert("您今天取的号尚未办结，请勿重复取号!");
				   	 	cfqhbj=0;
						gn.returnindex();
						return;
					}
				}else{
					alert("未放置身份证或身份证无效");
					return 
				}
			}else if(flag=='1'){ //网约非身份证取号
				agentfalg=0;
				
				if (banliTypes == 1) {
		  			IDNumberB = $("#tIDNumber2").val();
		  			nameB = "";
		  			banliTypesVo = 1;
		  			banliTypes = 0;
		  			$("#inner_fsfz2").css("display", "none");
		  			document.getElementById("msg2").innerHTML ="请放入车主身份证！";
		  			return;
		  		} else {
		  			IDNumber = $("#tIDNumber2").val();
		  			NameA = "";
		  		}
				IDType = $("#tIDType2").val();
				
				//验证黑名单
				gn.validateBlack(IDNumber);
				if(hmdbj == 1){
					alert("此人已在黑名单中存在，无法取号！！！");
					hmdbj = 0;
					return;
				}
				
				//验证重复取号
				gn.checkcfqh(IDNumber);
				if(cfqhbj==1){
					alert("您今天取的号尚未办结，请勿重复取号!");
			   	 	cfqhbj=0;
					gn.returnindex();
					return;
				}
			}
			//预约取号提前 延后时间设置
			var yybefore = '15'; //提前时间
			var yyafter = '0'; //延后时间
		  	//查询预约信息并返回 打印信息
		  	$.ajax({type: "POST", cache: false,dataType: "json",async: false,data:{"yybefore":yybefore,"yyafter":yyafter,"sfzmhm":IDNumber,"IDType":IDType},
				url: '/queue/yyjk/queryYYXXzh.action',
				success: function (data) {
					if(data.code!='0'){
						udybd= data.business.dybd;
						ubdywmc = data.business.bdywmc;
						bsnsTypes = data;
						timeQuantum = data.timeQuantum;
						businessDefName = data.businessDefName;
						reservationUserName = data.reservationUserName; //预约人姓名
						idNo = data.idNo; //预约人证件号
						orderForOther = data.orderForOther; //是否为他人预约  是/否
						otherIdNo = data.otherIdNo; //他人证件号
						otherName = data.otherName; //他人姓名
						
						if(data.modelNumber!="6"){
						qhao('business');
						}else{
							var objDiv = $("#tdiv").empty();
							$("#WYGetNumber").css("display","none");
							$("#type").css("display","");
							objDiv.html(Wxphonenumber);
							
						}
						
					}else{
						$("#inner_fsfz2").css("display", "none");
						$("#WYGetNumber").css("display", "none");
						$("#indexBL").css("display", "");
						$("#backDiv").css("z-index", "-1");
						alert(data.msg);
						
					}
				}
		  		
		  	})
		 }
		//显示网约的办理人页面  选择本人或代理人
		this.showBL2 = function(){
			
			$("#indexBL2").css("display", "");
			$("#indexBL").css("display", "none");
		}
		
		//判断是否放入身份证  放入后自动调用身份证取号方法
		this.isPutIdCard = function(){
			//是否自动读卡
			if(isAutoReadCard){
				//判断是否能够读取到身份证信息
				if (0 < readSysCard.FindReader()&& 0 == readSysCard.ReadCardMsg()) {
					//判断身份证和代理人身份是否相同
					var tempIdNumber = readSysCard.CardNo;
					if(tempIdNumber!=IDNumberB){
						//读取到身份证信息后进行 身份证取号操作
						gn.card();
					}
			  	}else if(0 >= readSysCard.FindReader()){
			  		console.log("没有找到身份证读卡器");
			  	}else if(0 != readSysCard.ReadCardMsg()){
			  		console.log("没读到身份证信息");
			  	}
			}else{
		  		console.log("不进行自动读卡");
		  	}
		}
		
		//启动间隔读取身份证信息
		this.startIdCardInterval = function(){
			//设置读取间隔 1秒 调用判断是否放入身份证  放入后自动调用身份证取号方法
			//if(isEmpty(idCardInterval)) idCardInterval = setInterval("gn.isPutIdCard()",1000);
		}
		
		
		//关闭间隔读取身份证信息
		this.stopIdCardInterval = function(){
			//清除间隔读取身份证信息
			//window.clearInterval(idCardInterval);
			if(IDNumberB==""){
				isAutoReadCard=false;
			}else if(IDNumberB!=""&&autoReadCount==0){
				autoReadCount=autoReadCount+1;
			}else if(IDNumberB!=""&&autoReadCount==1){
				isAutoReadCard=false;
			}
		}
		
		this.showNoCard = function(){
			$("#tIDNumber2").val("");
			$("#inner_fsfz2").css("display", "");
		}
		
		this.wyCancel = function(){
			$("#inner_fsfz2").css("display", "none");
		}
		
		this.showWYGetNumber = function(data){
			timeQuantum = "";
         	businessDefName = "";
         	IDNumber="";IDNumberB="",NameA="";nameB="";
         	reservationUserName = ""; //预约人姓名
            idNo = ""; //预约人证件号
            orderForOther = ""; //是否为他人预约  是/否
            otherIdNo = ""; //他人证件号
            otherName = ""; //他人姓名
			if (data == '0') {
				//网约本人办理
				banliTypes = 0;
				document.getElementById("msg2").innerHTML ="请放入车主身份证！";
			} else {
				//网约代理人办理
				banliTypes = 1;
				document.getElementById("msg2").innerHTML ="请放入代办人身份证！";
			}
			showBackBtn();
			$("#WYGetNumber").css("display", "");
			$("#indexBL").css("display", "none");
			$("#indexBL2").css("display", "none");
			$("#inner_fsfz2").css("display", "none");
			
		}
		//js线程休眠 单位秒
		this.sleep = function(numberMillis){
			var now = new Date();
			var exitTime = now.getTime()+numberMillis;
			while(true){
				now = new Date();
	            if (now.getTime() > exitTime)
		        return;
			}
		}

		this.WxmessagebtnOk =function(){
			phoneNumber = $("#pnumber").val();
			qhao('business');
		}
		this.WxmessagebtnCancel = function(){
			check(flag);
		}
		
		//载入打印申请表内容
		this.loadPrintInfo = function(){
			$("#jdcbgdjbasqb").load("jdcbgdjbasqb.html #jdcbgdjbasqb",function(){
				jdcbgdjbasqb = $("#jdcbgdjbasqb").html();
				$("#jdcbgdjbasqb").html("");
			});
			$("#jdcjszsqb").load("jdcjszsqb.html #jdcjszsqb",function(){
				jdcjszsqb = $("#jdcjszsqb").html();
				$("#jdcjszsqb").html("");
			});
			$("#jdcjsrstqksbb").load("jdcjsrstqksbb.html #jdcjsrstqksbb",function(){
				jdcjsrstqksbb = $("#jdcjsrstqksbb").html();
				$("#jdcjsrstqksbb").html("");
			});
			$("#jdcjsrstqksbbbm").load("jdcjsrstqksbbbm.html #jdcjsrstqksbbbm",function(){
				jdcjsrstqksbbbm = $("#jdcjsrstqksbbbm").html();
				$("#jdcjsrstqksbbbm").html("");
			});
			$("#jdcjsrsttjzm").load("jdcjsrsttjzm.html #jdcjsrsttjzm",function(){
				jdcjsrsttjzm = $("#jdcjsrsttjzm").html();
				$("#jdcjsrsttjzm").html("");
			});
			$("#xcjszgsqb").load("xcjszgsqb.html #xcjszgsqb",function(){
				xcjszgsqb = $("#xcjszgsqb").html();
				$("#xcjszgsqb").html("");
			});
			$("#jdcdydjzybasqb").load("jdcdydjzybasqb.html #jdcdydjzybasqb",function(){
				jdcdydjzybasqb = $("#jdcdydjzybasqb").html();
				$("#jdcdydjzybasqb").html("");
			});
			$("#jdcjybzsqb").load("jdcjybzsqb.html #jdcjybzsqb",function(){
				jdcjybzsqb = $("#jdcjybzsqb").html();
				$("#jdcjybzsqb").html("");
			});
			$("#jdcpzsqb").load("jdcpzsqb.html #jdcpzsqb",function(){
				jdcpzsqb = $("#jdcpzsqb").html();
				$("#jdcpzsqb").html("");
			});
			$("#jdczczyzxdjzrsqb").load("jdczczyzxdjzrsqb.html #jdczczyzxdjzrsqb",function(){
				jdczczyzxdjzrsqb = $("#jdczczyzxdjzrsqb").html();
				$("#jdczczyzxdjzrsqb").html("");
			});
			
		}
	}


		
    return {
        getInstance: function() {
			if (!instance) {
				instance = new constructor();
			}
			return instance;
		}
    };
})();

var gn = GenerateNumber.getInstance();

function upIcardPic(base64,cardNumber){
	var params = "base64picPhoto=" + encodeURIComponent(base64) + "&cardNumber="+cardNumber;
	$.ajax({type:"POST", cache:false, data:params,
			url:"number/uploadPic.action",
			success:function (picres) {
			if(0 == picres){
				var features = "status=no,resizable=no,top=0,left=0,scrollbars=no,width=300,height=400"
				+ "titlebar=no,menubar=no,location=no,toolbar=no,z-look=yes" 
				//var newWin = window.open("/queue/showPhotoPic.jsp?cardNumber="+cardNumber, "_blank", features);
			}
		}
	});      
}
function showRs(){
	$.ajax({type: "POST", cache: false,dataType: "json",async: false,
						url: "number/showWaitRs.action?"+parseInt(Math.random()*100000),
						success: function (req) {
							if(req.isSuccess == true){
							$.each(req.datas, function(i, obj) {
								contAllRs = "大厅等待人数:"+obj.countAll+"人</br>";
							     });
							    //显示等待人数
								//$('#showWaitRs').html("<font size='5'>"+contAllRs+"</font>");
							}else{
								//$('#showWaitRs').html("大厅等待人数:0人</br>");
							}
						}
	});  
}
	
function isLrZHiWen(key,id,flag){
	waitZhiWen(key,id,flag)
}
function waitZhiWen(key,id,flag){
	
	var baseVal = document.getElementById("zwbase64").value;
	if("" != baseVal && "undefined" != baseVal){
		gn.validateZw(key,id,flag,baseVal);//保存指纹信息
	}else{
		if(confirm("是否重新录入！！！")){
			isLrZHiWen(key,id,flag);
		}else{
			gn.validateZw(key,id,flag,baseVal);//保存指纹信息
		}
	}
}
window.onload = function(){
		window.setInterval("showRs()",300000);
		//载入打印表单的信息
		gn.loadPrintInfo();
}

/*获取业务类型及帮助信息*/
this.helpShow =function(){
	var helpReply;//信息包装类
	var isSuccess = true;
	 if (isEmpty(helpReply)) {
	        $.ajax({
	        	type: "POST", 
	        	cache: false, 
	        	async: false, 
	        	dataType: "json",
		        url: "number/bsnsType.action?"+parseInt(Math.random()*100000),
		        success: function(request) {
		            if (request.isSuccess) {
		            	helpReply = request.datas;
					} else {
						isSuccess = request.isSuccess;
						window.alert(request.error);
					}
		        },
		        error: function() {
		            isSuccess = false;
		            window.alert("连接服务器失败，请稍候再试");
		        }
		    });
	    }
	 if (!isSuccess) { return; }
		//设计提示框表头
		var oHelp = $("#tID1").empty();
		var divTb="<thead align='center'><tr><th>业务类型</th><th>业务说明</th> </tr></thead>";
		oHelp.append(divTb);
		//遍历提示框信息
	$.each(helpReply, function(i, obj) { 
		var o=isNull(obj.helps);
		if (obj.helps !== undefined  && !o) { 
		var helps=enterString(obj.helps);
		var helps=spaceString(helps);
		var divCon ="<tr><td width='30%' height='15%'>"+obj.names+"</td><td width='70%' height='15%'>"+helps+"</td></tr>";
		oHelp.append(divCon);
		}
	});
	 		$("#hdiv").css("display", "");
	 		$("#hb1").css("display", "none");
	 }
		
	 $("#hdiv").mouseleave(function(){
		 $(this).css("display","none");
		 $("#hb1").css("display", "");
	 });
	
		//替换所有的回车换行  
	 function enterString(content){  
	     var string = content;  
	     try{  
	        string=string.replace(/\r\n/g,"<br>");  
	        string=string.replace(/\n/g,"<br>");  
	     }catch(e) {  
	         e.message;  
	     }  
	     return string;  
	 }  
	 
		//替换所有的空格字符串  
	 function spaceString(content){  
		     var string = content;  
		     try{  
		   string=string.replace(/(^s*)|(s*$)/g,"&nbsp;");
		     }catch(e) {  
		         e.message;  
		     }  
		     return string;  
		 }  
	 	//判断是否全是空格，是则true，否则false
	 function isNull( str ){
		 if ( str == "" ){return true;}
		 var regu = "^[ ]+$";
		 var re = new RegExp(regu);
		 return re.test(str);
		 }
	 //打印提示内容显示
	 function printMsgShow(msg){
		 $("#printInfo").html(msg);
		 $("#zhezhao").css("display","");
		 $("#printMsg").css("display","");
	 }
	 //打印提示内容隐藏
	 function printMsgHidden(){
		 $("#zhezhao").css("display","none");
		 $("#printMsg").css("display","none");
	 }
	 //键盘点击获取对应的值到输入框
	 function keyboardClick(a){
		 var idString = $("#tIDNumber").val();
		 $("#tIDNumber").val(idString+a);
	 }
	 //清除输入框内容
	 function deleteClick(){
		 var idString = $("#tIDNumber").val();
		 if(!isEmpty(idString)){
			 $("#tIDNumber").val("");
		 }
	 }
	 //删除输入框最后一个文字
	 function backClick(){
		 var idString = $("#tIDNumber").val();
		 var len = idString.length-1;
		 if(!isEmpty(idString)){
			 idString=idString.substring(0,len);
			 $("#tIDNumber").val(idString);
		 }
	 }
	 function dache(id){
		 $("#dc").attr("checked",true)
	}
	function xiaoche(id){
		 $("#xc").attr("checked",true)
	}
