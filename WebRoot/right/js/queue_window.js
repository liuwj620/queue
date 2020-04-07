dwr.engine.setActiveReverseAjax(true);
dwr.engine.setNotifyServerOnPageUnload(true);
var serip;
var _div;//声明号码查询变量
function isEmptyOfQueue(val) {
    return "undefined" == typeof(val) || "" == val || null == val;
}
var callNumberTime,timeout;
//强制叫号
function forcedToNumber(_serverIP,jhbj){//叫号标记，0可以叫号 1不能叫号(主要用与挂起恢复)
		$.ajax({type: "POST", cache: false, async: false, dataType: "json",
			        url: 'http://'+_serverIP+'/queue/right/forcedToCallNumber_status.action',
			        success: function(data) {
			        	callNumberTime = data.forcedToNumberTime*1000;
						window.clearTimeout(timeout);
			            if ("0"==data.isForcedToNumber) {//判断是否开启强制叫号
				            if ("0"==jhbj) {//挂起恢复
					            if("1"==data.numberCountFlag){//判断号码池是否有号 0代表号码池有可呼叫号，1 号码池没有可呼叫的号
					            	setTimeout("forcedToNumber('"+_serverIP+"')", 5000);
					        	}else if("2"==data.numberCountFlag){//2 已经叫号，不再进行操作
									
								}else{
									sw.call();
								}
							}
						}
			        },
			        error: function() {
			            window.alert("连接服务器失败，请稍候再试");
			        }
		});
}
SWindow = (function() {
	var instance, timeoutValue, _serverIP, isok, _iscameraCallNum;
	
	function setMsg(obj) {
        var times = 5000;
        $("#mpt").remove();
        window.clearTimeout(timeoutValue);
        
        if ("string" === typeof(obj)) {
            var _div = $("<div>", {id: "mpt"});
            //系统提示：
       		_div.html("\u7cfb\u7edf\u63d0\u793a\uff1a").append($("<span>").html(obj));
       		obj = _div;
        } else if ("object" === typeof(obj)) {
            times = 10000;
            $(obj).attr("id", "mpt");
        }
        
   		$("#noticecon").append(obj);
   		timeoutValue = window.setTimeout("$('#mpt').remove()", times);
    }
    
    function ajax(action, params, callback) {
    	
        $.ajax({type: "GET", cache: false, dataType: "jsonp",
        	jsonp: "callback", data: params, async: false,
	        url: "http://" + _serverIP + "/queue/" + action + "?callback=?",
	        success: function(data) {
	            callback(data);
	        },
	        error: function(xhr) {
	        	alert(action);
	            //请求出错(请检查相关度网络状况)
	            setMsg("\u8bf7\u6c42\u51fa\u9519(\u8bf7\u68c0\u67e5\u76f8\u5173\u5ea6\u7f51\u7edc\u72b6\u51b5)");
	        }
//            error:function(XmlHttpRequest,textStatus,errorThrown){
//                  alert(XmlHttpRequest.responseText);
//           }
	    });
	}
	
	function pauseOrRecover(reason, oImg, params) {
	    params.reason = reason;
	    ajax("right/pauseOrRecover.action", params, function(data) {
	        if (data.isSuccess && "pause" == oImg.id) {
		    	$("#mpt").remove();
		    	oImg.id = "recover";
				oImg.src = oImg.src.replace("btn_zt2.gif", "btn_hf2.gif");
			} else if (data.isSuccess && "recover" == oImg.id) {
		    	oImg.id = "pause";
		        oImg.src = oImg.src.replace("btn_hf2.gif", "btn_zt2.gif");
		    }
	    	setMsg(decodeURIComponent(data.msg));
	    });
	}
	function constructor() {
		var _params;
	    
	    this.evalUSBSetTimeouResult;
	    
	    this.login = function(operNum, loginIP, serverIP, serverDwrPath) {
		    _serverIP = serverIP;
		    serip=serverIP;
		    _iscameraCallNum = operNum ;
		    JspRegister._path = serverDwrPath;
			JspRegister.register(loginIP, "M");
			_params = {operNum: operNum, loginIP: loginIP};
			sw.getTotal(1);
			timeout = setTimeout("forcedToNumber('"+serverIP+"','0')", 10000);//调用强制叫号
	    };
		this.minOrMax = function(oLabel) {
		    if ("tomin" == oLabel.id) {
		        $("div[id=noticecon]","div[id=newnotice]").slideUp();
		        oLabel.id = "tomax";
		        oLabel.title="\u8fd8\u539f";//还原
		    } else if ("tomax" == oLabel.id) {
		        $("div[id=noticecon]","div[id=newnotice]").slideDown();
		        oLabel.id = "tomin";
		        oLabel.title="\u6700\u5c0f\u5316";//最小化
		    }
		};
		
		this.getTotal = function (flag) {
		    ajax("right/current.action", _params, function(data) {
		        if (1 == flag) {
		        	$("#queMsg").html(data.msg);
		        }
			    $("#spanTotal").html(decodeURIComponent(data.count));
			});
		};
		
		var s=0; var m=0; var se;//定义计时变量
		this.call = function() {
		    ajax("right/call.action", _params, function(data) {
		        var msgArr = decodeURIComponent(data.msg).split("@");
				setMsg(msgArr[0]);
				if (1 < msgArr.length) {
				    $("#queMsg").html(msgArr[1]);
				    sw.getTotal(0);
				    timeout = setTimeout("forcedToNumber('"+_serverIP+"','0')", callNumberTime);//调用强制叫号
				}
		    });
		    clearInterval(se);//点击叫号开始计时
		    start();//点击叫号开始计时
		};
		
		//----计时方式--------
		function start(){
			se=setInterval(function(){jhsecond()},1000);
			}
		
		function jhsecond(){
			if(s>0 && (s%60)==0){
					m+=1;
					s=0;
				}  
			var t="叫号计时："+m+"分"+s+"秒";
//			document.getElementById("jhse").html=t;
			$("#jhse").text(t);
			s+=1;
		};
		
		function end(){
			m=0;
			s=0;
//			document.getElementById("jhse").html="";
			$("#jhse").text();
			clearInterval(se);
		}
		//--计时方式结束---
		
		this.recall = function() {
			end();
		    ajax("right/recall.action", _params, function(data) {
		        setMsg(decodeURIComponent(data.msg));
		    });
		    //clearInterval(se);点击叫号开始计时
		    start();//点击叫号开始计时
		};
		
		this.pass =function(){
			end();//过号结束计时
			ajax("right/judgePassReason.action",_params,function(data){
				if(data.iscall=="false"){
					setMsg("\u672a\u53eb\u53f7\u002c\u4e0d\u80fd\u8fc7\u53f7"); //未叫号,不能过号
				}else{
					if(data.hasPassReason=="false"){
						sw.judgePassReason("NoReason",_params);
					}else{
						var oSelect = $("<select>").change(function(){
							sw.judgePassReason($(this).val(),_params);
						});
						oSelect.append($("<option>", {value: ""}).html("-\u8bf7\u9009\u62e9-"));//请选择
						oSelect.append(decodeURIComponent(data.msg)); //选择内容
						var _div = $("<div>", {style: "text-align: center; margin-bottom: 5px;"});
					        _div.append("\u8bf7\u9009\u62e9\u8fc7\u53f7\u539f\u56e0").append(oSelect);//请选择过号原因：
						setMsg(_div);
					};
				}
			});
		};
		
		this.judgePassReason = function(reason,_params) {
			_params.reason = reason;
		    ajax("right/pass.action", _params, function(data) {
		        var msgArr = decodeURIComponent(data.msg).split("@");
		        if(msgArr[0]==3){
		        	setMsg(msgArr[2]);
		        }else{
		        	setMsg(msgArr[0]);
		        	}
				
				if (1 < msgArr.length) {
				    $("#queMsg").html("");
				    timeout = setTimeout("forcedToNumber('"+_serverIP+"','0')", callNumberTime);//调用强制叫号
				}
		    });
		};
		
		this.evalu = function() {
			end();//评价结束计时
		/*	iscamera(_iscameraCallNum);
			if(0==isok){
				jsrzP.nW = 300;
				jsrzP.nH = 320;
				var openres = jsrzP.OnContorlInit();//判断摄像头是否开启成功
				if(false == openres){
					var openres1 = jsrzP.OnContorlInit();//第二次判断摄像头是否开启成功
					 if(false == openres1){
					 	isok = 1;
					 
					 }
				}else{
					if("" != $("#base64Code").val()){
						subCameraPic(_iscameraCallNum);//提交照片
						if(false == jsrzP.OnCloseDev()){//关闭摄像头
							jsrzP.OnCloseDev();
						}
					}else{
						return;
					}
				}
			}*/
		    ajax("right/evalu.action", _params, function(data) {
		        var msgs = decodeURIComponent(data.msg).split("@");
				setMsg(msgs[0]);
				if (0 == msgs[1]) {
					if (0 == msgs[2]) {//判断评价器类型 0新评价器 1旧评价器
						 var wrong = XJPJQ.reset();//初始化
						  if(true == wrong){
						  		//XJPJQ.start();
						  		var result = XJPJQ.getKey(10);
						  		sw.submitEval({flag: 1,evaluResult:result,typeFlag: 0});
						 /* var evalUSBAddress = 1;
							var evalUSBCom = PJQ.GetPJCom();
							if (evalUSBCom > 0) {
								if (!PJQ.OpenPort(evalUSBCom, 9600)) {
									//打开评价器串口失败！
									window.alert("\u6253\u5f00\u8bc4\u4ef7\u5668\u4e32\u53e3\u5931\u8d25\uff01");
								} else {
									if (!PJQ.SendPingjiaMsg(evalUSBAddress)) {
										//向评价器串口发送命令失败！
										window.alert("\u5411\u8bc4\u4ef7\u5668\u4e32\u53e3\u53d1\u9001\u547d\u4ee4\u5931\u8d25\uff01");
									} else {
										evalUSBSetTimeouResult = setTimeout(function () {
											PJQ.ClosePort();
											sw.submitEval({flag: 2});
										}, 10000);
									}
								}*/
							} else {
								//获取评价器串口失败！
								window.alert("\u83b7\u53d6\u8bc4\u4ef7\u5668\u4e32\u53e3\u5931\u8d25\uff01");
							}
					}else{
						    var evalUSBAddress = 1;
							var evalUSBCom = PJQ.GetPJCom();
							if (evalUSBCom > 0) {
								if (!PJQ.OpenPort(evalUSBCom, 9600)) {
									//打开评价器串口失败！
									window.alert("\u6253\u5f00\u8bc4\u4ef7\u5668\u4e32\u53e3\u5931\u8d25\uff01");
								} else {
									if (!PJQ.SendPingjiaMsg(evalUSBAddress)) {
										//向评价器串口发送命令失败！
										window.alert("\u5411\u8bc4\u4ef7\u5668\u4e32\u53e3\u53d1\u9001\u547d\u4ee4\u5931\u8d25\uff01");
									} else {
										evalUSBSetTimeouResult = setTimeout(function () {
											PJQ.ClosePort();
											sw.submitEval({flag: 2});
										}, msgs[3]);
									}
								}
							} else {
								//获取评价器串口失败！
								window.alert("\u83b7\u53d6\u8bc4\u4ef7\u5668\u4e32\u53e3\u5931\u8d25\uff01");
							}
					}
				}
		    });
		};
		this.submitEval = function(params) {
			if(1 == params.typeFlag){
				switch (params.evaluResult) {
					case 1: //满意
						params.evaluResult = 2;
						break;
					case 2: //一般
						params.evaluResult = 3;
						break;
					case 3: //不满意
						params.evaluResult = 4;
						break;
					case 4: //非常满意
						params.evaluResult = 1;
						break;
				}
			}else{
				switch (params.evaluResult) {
					case 0: //非常满意
						params.flag = 2;
						params.evaluResult = 0;
						break;
					case 1: //非常满意
						params.evaluResult = 1;
						break;
					case 2: //满意
						params.evaluResult = 2;
						break;
					case 3: //一般
						params.evaluResult = 3;
						break;
					case 4: //不满意
						params.evaluResult = 4;
						break;
					case 5: //不满意
						params.evaluResult = 4;
						break;
				}
			}
			ajax("number/saveEvalu.action", $.extend({}, _params, params));
		};
		
		this.complete = function(data) {
		    if (data.isComplete) {
		    	setMsg(decodeURIComponent(data.msg));//完成评价
		    	$("#queMsg").html("");
		    	 timeout = setTimeout("forcedToNumber('"+_serverIP+"','0')", callNumberTime);//调用强制叫号
		    } else {
		    	$("#queMsg").html(data.num);
		    }
		};
		
		this.hangup = function() {
			end();//挂起结束计时
			ajax("right/hangup.action", _params, function(data) {
		        var msgArr = decodeURIComponent(data.msg).split("@");
				setMsg(msgArr[0]);
				if (1 < msgArr.length) {
				    $("#queMsg").html("");
				   timeout = setTimeout("forcedToNumber('"+_serverIP+"','0')", callNumberTime);//调用强制叫号
				}
		    });
		};
		
		this.hangupRecover = function() {
			end();//挂起结束计时
			ajax("right/hangupNumber.action", _params, function(data) {
		        if (data.isSuccess) {
		            var oSelect = $("<select>").change(function() {
			            var params = $.extend({}, _params, {id: $(this).val()});
			            ajax("right/hangupRecover.action", params, function(data) {
			            	setMsg(decodeURIComponent(data.msg));
			            	timeout = setTimeout("forcedToNumber('"+_serverIP+"','1')", callNumberTime);//调用强制叫号
			            });
			        });
			        
			        oSelect.append($("<option>", {value: ""}).html("-\u8bf7\u9009\u62e9-"));//请选择
			        $.each(eval("(" + decodeURIComponent(data.msg) + ")"), function(key, val) {
			            var oOpt = $("<option>", {value: key}).html(val);
			            oSelect.append(oOpt);
			        });
			        var _div = $("<div>", {style: "text-align: center; margin-bottom: 5px;"});
			        _div.append("\u5df2\u6302\u8d77\u53f7\u7801\uff1a").append(oSelect);
			        setMsg(_div);
		        } else {
		            setMsg(decodeURIComponent(data.msg));
		        }
		    });
			start();
		};
		
		this.excChange = function() {
			    ajax("right/excChangeWin.action", _params, function(data) {
			        if (data.isSuccess) {
			            var oSelect = $("<select>").change(function() {
				           	var params = $.extend({}, _params, {id: $(this).val()});
				            ajax("right/transferenceNumber.action", params, function(data) {
				            //流转成功
				            	var msgArr = decodeURIComponent(data.msg).split("@");
								setMsg(msgArr[0]);
								if (1 < msgArr.length) {
								    $("#queMsg").html("");
								    sw.getTotal(0);//刷新类型显示数量
								    timeout = setTimeout("forcedToNumber('"+_serverIP+"','0')", callNumberTime);//调用强制叫号
								}
				            });
				        });
				        
				        oSelect.append($("<option>", {value: ""}).html("-\u8bf7\u9009\u62e9-"));//请选择
				        $.each(eval("(" + decodeURIComponent(data.msg) + ")"), function(key, val) {
				            var oOpt = $("<option>", {value: key}).html(val);
				            oSelect.append(oOpt);
				        });
				        var _div = $("<div>", {style: "text-align: center; margin-bottom: 5px;"});
				        _div.append("\u6d41\u8f6c\u7a97\u53e3").append(oSelect);//流转窗口：
				        setMsg(_div);
			        } else {
			            setMsg(decodeURIComponent(data.msg));
			        }
			    });
			    
			    
		};
		
		this.pause = function(oImg) {
			end();//暂停结束计时
		    if ("pause" == oImg.id) {
			    ajax("right/pauseReason.action", _params, function(data) {
			        if (data.isSuccess) {
			            var oSelect = $("<select>").change(function() {
				            pauseOrRecover($(this).val(), oImg, _params);
				        });
				        
				        oSelect.append($("<option>", {value: ""}).html("-\u8bf7\u9009\u62e9-"));//请选择
				        $.each(eval("(" + decodeURIComponent(data.msg) + ")"), function(key, val) {
				            var oOpt = $("<option>", {value: key}).html(val);
				            oSelect.append(oOpt);
				        });
				        var _div = $("<div>", {style: "text-align: center; margin-bottom: 5px;"});
				        _div.append("\u6682\u505c\u539f\u56e0\uff1a").append(oSelect);//暂停原因：
				        setMsg(_div);
			        } else {
			            setMsg(decodeURIComponent(data.msg));
			        }
			    });
		    } else if ("recover" == oImg.id) {
		        pauseOrRecover("", oImg, _params);
		    }
		};
		
		//----添加顺序号查询----
		this.sxhcx = function() {
			$("#callCon").css("display","");
			$("#newnotice").css("display","none");
			/*
			oInput = "<input style='color:#ff0000;width:50px;' id='snum' type='text'>";
				but="<input type='button' value='查询' onClick='saNum()'>";
			_div = $("<div>", {style: "text-align: center; margin-bottom: 5px;"});
			_div.append("\u987a\u5e8f\u53f7\uff1a").append(oInput).append(but);//顺序号：
//			$("#noticecon").append(_div);
			setMsg(_div);
		*/};
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

var sw = SWindow.getInstance();
//原方法。dom加载完后执行显示小窗口
/*$(function() {
    $(window).load(function() {
    	showBtn();
    	//setInterval("sw.getTotal(0)",2000);
        $("div[id=newnotice]").css({"right":"2px", "bottom":"2px"});
        $("div[id=newnotice]").slideDown("slow");
    }).scroll(function() {
        $("div[id=newnotice]").css({"bottom":"0px"});
        $("div[id=newnotice]").css({"right":"2px", "bottom":"2px"});
    }).resize(function() {
        $("div[id=newnotice]").css({"bottom":""});
        $("div[id=newnotice]").css({"right":"2px", "bottom":"2px"});
    });
    
    $("div[id=newnotice]").bind("mousedown", function (event) {
		var offset_x = $(this)[0].offsetLeft;
		var offset_y = $(this)[0].offsetTop;
		var mouse_x = event.pageX;
		var mouse_y = event.pageY;
		$(document).bind("mousemove", function (ev) {
			var _x = ev.pageX - mouse_x;
			var _y = ev.pageY - mouse_y;
			var now_x = (offset_x + _x) + "px";
			var now_y = (offset_y + _y) + "px";
			$("div[id=newnotice]").css({top:now_y, left:now_x});
		});
	});
	
	$(document).bind("mouseup", function () {
		$(this).unbind("mousemove");
	});
});*/

function displayWin() {
    	showBtn();
    	//setInterval("sw.getTotal(0)",2000);
        $("div[id=newnotice]").css({"right":"2px", "bottom":"2px"});
        $("div[id=newnotice]").slideDown("slow");
     $(window).scroll(function() {
        $("div[id=newnotice]").css({"bottom":"0px"});
        $("div[id=newnotice]").css({"right":"2px", "bottom":"2px"});
    }).resize(function() {
        $("div[id=newnotice]").css({"bottom":""});
        $("div[id=newnotice]").css({"right":"2px", "bottom":"2px"});
    });
    
    $("div[id=newnotice]").bind("mousedown", function (event) {
		var offset_x = $(this)[0].offsetLeft;
		var offset_y = $(this)[0].offsetTop;
		var mouse_x = event.pageX;
		var mouse_y = event.pageY;
		$(document).bind("mousemove", function (ev) {
			var _x = ev.pageX - mouse_x;
			var _y = ev.pageY - mouse_y;
			var now_x = (offset_x + _x) + "px";
			var now_y = (offset_y + _y) + "px";
			$("div[id=newnotice]").css({top:now_y, left:now_x});
		});
	});
	
	$(document).bind("mouseup", function () {
		$(this).unbind("mousemove");
	});
}

//	function showBtn() {
//		var windowWidth = 415;
//		var content="<img src='images/btn_jh2.gif' style='cursor: pointer;margin-right:10px;' onclick='sw.call()' />"+
//					"<img src='images/btn_ch2.gif' style='cursor: pointer;margin-right:10px;' onclick='sw.recall()' />"+
//					"<img src='images/btn_gh2.gif' style='cursor: pointer;margin-right:10px;' onclick='sw.pass()' />"+
//					"<img src='images/btn_qpj2.gif' style='cursor: pointer;margin-right:10px;' onclick='sw.evalu()'/>"+
//					"<img src='images/btn_gq2.gif' style='cursor: pointer;margin-right:10px;' onclick='sw.hangup()'/>"+
//					"<img src='images/btn_gqhf2.gif' style='cursor: pointer;margin-right:10px;' onclick='sw.hangupRecover()'/>"+
//					"<img id='pause' src='images/btn_zt2.gif' style='cursor: pointer;margin-right:10px;' onclick='sw.pause(this)' />";
//		$.ajax({type: "POST", cache: false, async: false, dataType: "json",
//			        url: 'http://<%=serverIP%>/queue//number/shwoBtn_status.action',
//			        success: function(request) {
//			            if (request.isSuccess) {
//			                isChange = true;
//			                delete request.isSuccess;
//							$.each(request.datas, function(key, obj) {
//								if("0"==obj.status){
//									content +="<img src='images/"+obj.names+"' style='cursor: pointer;margin-right:10px;' onclick='"+obj.event+"'/>";
//									windowWidth+=60;
//								}
//		     				});
//		     				$("#changebtn").html(content);
//		     				$("#newnotice").css("width",windowWidth);
//						} else {
//							 isSuccess = request.isSuccess;
//							 $("#changebtn").html(content);
//						}
//			        },
//			        error: function() {
//			            isSuccess = false;
//			            window.alert("连接服务器失败，请稍候再试");
//			        }
//		});
//	}

	function showBtn() {
		var windowWidth = 415;
		
		var content="<img src='images/btn_jh2.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.call()' />"+
					"<img src='images/btn_ch2.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.recall()' />"+
					"<img src='images/btn_gh2.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.pass()' />"+
					"<img src='images/btn_qpj2.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.evalu()'/>"+
					"<img src='images/btn_gq2.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.hangup()'/>"+
					"<img src='images/btn_gqhf2.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.hangupRecover()'/>"+
					"<img id='pause' src='images/btn_zt2.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.pause(this)' />"+
					"<img id='pause' src='http://"+serip+"/queue/right/images/btn_hmcx.jpg' style='cursor: pointer;margin-right:10px;' onclick='sw.sxhcx()' />";
		
		$("#changebtn").html(content);
 		$("#newnotice").css("width",windowWidth+=60);
	}

	//顺序号查询结果显示
	function saNum(){
		var num = $("#snum").val();
	//	var num=document.getElementById("snum");
		if("undefined" != typeof(num) && "" != num && null != num){
			$.ajax({type: "POST", cache: false, async: false, dataType: "json",
		        url: "http://"+serip+"/queue/right/getBarIdNum.action?cxhm="+num+"&"+"tjms=1",
		        success: function(req) {
					if("undefined" != req.winId && "" != req.winId && null != req.winId){
						_div.append("叫号窗口:"+req.winId);
					}else{
						_div.append("未查询到相关顺序号信息！")
					}			
				},
	    error: function() {
			isSuccess = false;
	        window.alert("网络超时，请稍候再试");
				}
			});
		}else{
			alert("顺序号不能为空！");
		}
	}
	
	
	function showFail(){
		alert("外屏页面与内屏断开通讯，请手动刷新外屏页面!");
	}
