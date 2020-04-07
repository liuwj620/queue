package com.suntendy.queue.util.scriptsession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContextFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.ServletContextAware;

import com.suntendy.queue.employee.domain.Employee;
import com.suntendy.queue.util.cache.CacheManager;
import com.suntendy.queue.util.scriptsession.event.CYTangKuang;
import com.suntendy.queue.util.scriptsession.event.ChuangKouPing;
import com.suntendy.queue.util.scriptsession.event.DualScreenEvent;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventIMG;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventFail;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventWP;
import com.suntendy.queue.util.scriptsession.event.DualScreenEventYZ;
import com.suntendy.queue.util.scriptsession.event.EvaluateCompleteEvent;
import com.suntendy.queue.util.scriptsession.event.EvaluateEvent;
import com.suntendy.queue.util.scriptsession.event.GuaQiTuiSong;
import com.suntendy.queue.util.scriptsession.event.GuoHaoTuiSong;
import com.suntendy.queue.util.scriptsession.event.JspRegisterEvent;
import com.suntendy.queue.util.scriptsession.event.ModifyWindowCountEvent;
import com.suntendy.queue.util.scriptsession.event.PassEvent;
import com.suntendy.queue.util.scriptsession.event.PauseOrRecoverEvent;
import com.suntendy.queue.util.scriptsession.event.PauseOrRecoverEventZS;
import com.suntendy.queue.util.scriptsession.event.ReadDataEvent;
import com.suntendy.queue.util.scriptsession.event.RzdbTs;
import com.suntendy.queue.util.scriptsession.event.SendPJXXEvent;
import com.suntendy.queue.util.scriptsession.event.SendStatusCY;
import com.suntendy.queue.util.scriptsession.event.ShowCallInfoEvent;
import com.suntendy.queue.util.scriptsession.event.ShowCallNumEvent;
import com.suntendy.queue.util.scriptsession.event.ShowLzxxDataEvent;
import com.suntendy.queue.util.scriptsession.event.ShowQuHaoSumEvent;
import com.suntendy.queue.util.scriptsession.event.ShowWXCallInfoEvent;
import com.suntendy.queue.util.scriptsession.event.ShowYWDDRSEvent;
import com.suntendy.queue.util.scriptsession.event.ShowZhpLzxx;
import com.suntendy.queue.util.scriptsession.event.ShowZhxxDataEvent;
import com.suntendy.queue.util.scriptsession.event.UserLoginEvent;
import com.suntendy.queue.util.scriptsession.event.WarningMessageEvent;
import com.suntendy.queue.util.scriptsession.event.passLzxx;
import com.suntendy.queue.util.scriptsession.event.tsLzxx;
import com.suntendy.queue.window.domain.Screen;

public class PublishEventListener implements ApplicationListener,
		ServletContextAware {

	private ServletContext ctx;
	private static Map<String, String> photos = new HashMap<String, String>();

	public void setServletContext(ServletContext ctx) {
		this.ctx = ctx;
	}

	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof JspRegisterEvent) {// jsp注册事件
			String clientIp = (String) event.getSource();
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			ScriptSession scriptSession = WebContextFactory.get()
					.getScriptSession();
			ssm.setValue(clientIp, scriptSession);
		}else if (event instanceof ChuangKouPing) {//双屏信息事件验证
			   System.out.println("进入电视窗口屏推送页面");
			   final String[] content = ((String) event.getSource()).split("@");
			   final String ip = content[0];
			   final String ckid =content[1];
			   final String ckmc = content[2];
			   final String sxh = content[3];
				ScriptSessionManager ssm = ScriptSessionManager.getInstance();
				final ScriptSession session = ssm.getValue("S@" + ip);
				if (null == session) return;
				
				Browser.withSession(session.getId(), new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer
						.appendScript("waiPingXinXi({ckidh:'")
						.appendScript(ckid)
						.appendScript("', ckmch: '")
						.appendScript(ckmc)
						.appendScript("', sxhh: '")
						.appendScript(sxh)
						.appendScript("'})");
						session.addScript(buffer);
					}
				});
		   
		   }
		else if (event instanceof ShowCallNumEvent) {
			final String[] datas = ((String) event.getSource()).split("@");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("M@" + datas[0]);
			if (null == session) {
				return;
			}

			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("sw.complete({isComplete:false, num:'")
							.appendScript(datas[1]).appendScript("'})");
					session.addScript(buffer);
				}
			});
		} else if (event instanceof ModifyWindowCountEvent) {// 取号后修改右下角窗口未办理数
			final String[] content = ((String) event.getSource()).split("@");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			Collection<Screen> windowCache = CacheManager.getInstance()
					.getWindowCache().values();

			for (Screen screen : windowCache) {
				String businessType = screen.getBusinessid();
				if (StringUtils.isNotEmpty(businessType)
						&& -1 < businessType.indexOf(content[0])) {
					final ScriptSession session = ssm.getValue("M@"
							+ screen.getBarip());
					if (null != session) {
						Browser.withSession(session.getId(), new Runnable() {
							public void run() {
								ScriptBuffer buffer = new ScriptBuffer();
								buffer.appendScript("sw.getTotal(0)");
								session.addScript(buffer);
							}
						});
					}
				}
			}
		} else if (event instanceof ShowQuHaoSumEvent) {// 取号外屏等待人数
			final String[] content = ((String) event.getSource()).split("@");
			String pagePath = ctx.getContextPath()
					+ "/advertise/showQuhaoSum.jsp";

			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({nameAndShul: '")
							.appendScript(content[0]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof WarningMessageEvent) {// 超时警告信息
			final String[] content = ((String) event.getSource()).split("@");
			String pagePath = ctx.getContextPath() + "/right/win.jsp";
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({msg: '")
							.appendScript(content[0]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof UserLoginEvent) {// 登录事件
			final Employee employee = (Employee) event.getSource();
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@"
					+ employee.getWSIp());
			System.out.println("登录外屏IP:"+employee.getWSIp());
			if (null == session)
				return;

			if (null != employee.getPhoto()
					&& null == photos.get(employee.getCode())) {
				String photoDir = "photoTmp";
				String code = employee.getCode();
				String realPath = ctx.getRealPath("/") + photoDir;
				File fileDir = new File(realPath);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}

				try {
					String photoName = code + ".jpg";
					realPath = realPath + "/" + photoName;
					FileOutputStream fout = new FileOutputStream(realPath);
					fout.write(employee.getPhoto());
					fout.flush();
					fout.close();

					photos.put(code, ctx.getContextPath() + "/" + photoDir
							+ "/" + photoName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			final String photo = photos.get(employee.getCode());
			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("login({id: '")
							.appendScript(employee.getId())
							.appendScript("', code: '")
							.appendScript(employee.getCode())
							.appendScript("', name: '")
							.appendScript(employee.getName())
							.appendScript("', loginIP: '")
							.appendScript(employee.getLoginIp())
							.appendScript("', comments: '")
							.appendScript(employee.getComments())
							.appendScript("', photo: '")
							.appendScript(StringUtils.trimToEmpty(photo))
							.appendScript("'})");
					session.addScript(buffer);
				}
			});
		} else if (event instanceof ShowCallInfoEvent) {// 显示叫号信息事件
			final String[] content = ((String) event.getSource()).split("@");
			String pagePath = ctx.getContextPath()
					+ "/window/tv_window_lin_an.jsp";
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath1 = ctx.getContextPath()
					+ "/window/comprehensiveScreen.jsp";
			Browser.withPage(pagePath1, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath2 = ctx.getContextPath() + "/window/tvScreen.jsp";
			Browser.withPage(pagePath2, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath3 = ctx.getContextPath()
					+ "/window/tv_window_wu_xi.jsp";
			Browser.withPage(pagePath3, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath4 = ctx.getContextPath()
					+ "/window/tv_queue_led_1.jsp";
			Browser.withPage(pagePath4, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath5 = ctx.getContextPath()
					+ "/window/tv_queue_led_2.jsp";
			Browser.withPage(pagePath5, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath6 = ctx.getContextPath()
					+ "/window/tv_queue_led_jn.jsp";
			Browser.withPage(pagePath6, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath7 = ctx.getContextPath()
					+ "/window/tv_queue_led_jn1.jsp";
			Browser.withPage(pagePath7, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath8 = ctx.getContextPath()
					+ "/window/tv_window_lin_yi.jsp";
			Browser.withPage(pagePath8, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath9 = ctx.getContextPath()
					+ "/window/tv_queue_led_guiyang.jsp";
			Browser.withPage(pagePath9, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath10 = ctx.getContextPath()
					+ "/window/tv_window_yin_chuan.jsp";
			Browser.withPage(pagePath10, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath11 = ctx.getContextPath()
					+ "/window/tv_queue_led_weifang.jsp";
			Browser.withPage(pagePath11, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath12 = ctx.getContextPath()
					+ "/window/tv_queue_led_jiangmen.jsp";
			Browser.withPage(pagePath12, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath13 = ctx.getContextPath()
					+ "/window/tv_queue_led_haikou.jsp";
			Browser.withPage(pagePath13, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath14 = ctx.getContextPath()
					+ "/window/tv_queue_led_gy_jinyang.jsp"; // 贵阳金阳综合屏
			Browser.withPage(pagePath14, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath15 = ctx.getContextPath()
					+ "/window/tv_queue_led_chengdu.jsp"; // 成都综合屏
			Browser.withPage(pagePath15, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath16 = ctx.getContextPath()
					+ "/window/tv_queue_led_xinhui.jsp"; // 新会综合屏
			Browser.withPage(pagePath16, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath17 = ctx.getContextPath()
					+ "/window/tv_window_xin_hui.jsp";
			Browser.withPage(pagePath17, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath18 = ctx.getContextPath()
					+ "/window/tv_queue_led_old_xinhui.jsp";
			Browser.withPage(pagePath18, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath19 = ctx.getContextPath()
					+ "/window/tv_queue_led_ningbo.jsp";
			Browser.withPage(pagePath19, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath20 = ctx.getContextPath()
					+ "/window/tv_window_liuzhou.jsp";
			Browser.withPage(pagePath20, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath21 = ctx.getContextPath()
					+ "/window/tv_queue_led_haikou_qn.jsp";
			Browser.withPage(pagePath21, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath22 = ctx.getContextPath()
					+ "/window/tv_queue_led_wulanchabu.jsp";
			Browser.withPage(pagePath22, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath23 = ctx.getContextPath()
					+ "/window/tv_queue_led_chengduDL.jsp";
			Browser.withPage(pagePath23, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath24 = ctx.getContextPath()
					+ "/window/tv_queue_led_chengdu_sisuo.jsp";
			Browser.withPage(pagePath24, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath25 = ctx.getContextPath()
					+ "/window/tv_queue_led_chengdu_yisuo.jsp";
			Browser.withPage(pagePath25, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath26 = ctx.getContextPath()
					+ "/window/tv_queue_led_chengdu_xinjin.jsp";
			Browser.withPage(pagePath26, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath27 = ctx.getContextPath()
					+ "/window/tv_queue_led_fangchenggang.jsp";
			Browser.withPage(pagePath27, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
					System.out.println(buffer);
				}
			});
			String pagePath28 = ctx.getContextPath()
					+ "/window/tv_queue_led_anyang.jsp";
			Browser.withPage(pagePath28, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
					System.out.println(buffer + "===11111111111111111");
				}
			});
			String pagePath29 = ctx.getContextPath()
					+ "/window/tv_queue_led_fangchenggangSP.jsp";
			Browser.withPage(pagePath29, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
					System.out.println(buffer);
				}
			});
			String pagePath30 = ctx.getContextPath()
					+ "/window/tv_queue_led_guigang.jsp";
			Browser.withPage(pagePath30, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath31 = ctx.getContextPath()
					+ "/window/tv_queue_led_nanning_pj.jsp";
			Browser.withPage(pagePath31, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath32 = ctx.getContextPath()
					+ "/window/tv_queue_led_zaozhuang.jsp";
			Browser.withPage(pagePath32, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath33 = ctx.getContextPath()
					+ "/window/tv_queue_led_guigang_2.jsp";
			Browser.withPage(pagePath33, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath34 = ctx.getContextPath()
					+ "/window/tv_queue_led_zhuhai_zongsuo.jsp";
			Browser.withPage(pagePath34, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', ywlx: '")
							.appendScript(content[2]).appendScript("', ywl: '")
							.appendScript(content[3]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath35 = ctx.getContextPath()
					+ "/window/tv_queue_led_langfang.jsp";
			Browser.withPage(pagePath35, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath36 = ctx.getContextPath()
					+ "/window/tv_queue_led_zhongshan.jsp";
			Browser.withPage(pagePath36, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath37 = ctx.getContextPath()
					+ "/window/tv_queue_led_zhongshan_ZHP.jsp";
			Browser.withPage(pagePath37, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', ywlx: '")
							.appendScript(content[2]).appendScript("', ywl: '")
							.appendScript(content[3]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath38 = ctx.getContextPath()
					+ "/window/tv_window_zhong_shan.jsp";
			Browser.withPage(pagePath38, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			// 梧州综合屏
			String pagePath39 = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou.jsp";
			Browser.withPage(pagePath39, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
					.appendScript(content[0])
					.appendScript("', winNum: '")
					.appendScript(content[1])
					.appendScript("', ywlx: '")
					.appendScript(content[2]).appendScript("', ywl: '")
					.appendScript(content[3]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath40 = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou2.jsp";// 梧州电视机屏
			Browser.withPage(pagePath40, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			
			String pagePath41 = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou_jsr.jsp";
			Browser.withPage(pagePath41, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', ywlx: '")
							.appendScript(content[2]).appendScript("', ywl: '")
							.appendScript(content[3]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath42 = ctx.getContextPath()
			+ "/window/tv_window_ji_lin_1.jsp";
			Browser.withPage(pagePath42, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath43 = ctx.getContextPath()
			+ "/window/tv_window_ji_lin_2.jsp";
			Browser.withPage(pagePath43, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath44 = ctx.getContextPath()//电视队列屏
				+ "/waiping/duilieping_1080_1920.jsp";
			Browser.withPage(pagePath44, new Runnable() {
			public void run() {
				ScriptBuffer buffer = new ScriptBuffer();
				System.out.println("serialNum="+content[0]+"----winNum="+content[1]+"----code="+content[2]);
				buffer.appendScript("showMessage({serialNum: '")
						.appendScript(content[0])
						.appendScript("', winNum: '")
						.appendScript(content[1])
						.appendScript("', code: '")
						.appendScript(content[2]).appendScript("'})");
				ScriptSessions.addScript(buffer);
			}
		});
		} else if (event instanceof GuoHaoTuiSong) {// 梧州过号推送已过号字样
			final String[] content = ((String) event.getSource()).split("@");

			String pagePath1 = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou.jsp";
			Browser.withPage(pagePath1, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("GuoHaoTuiSong({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath2 = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou2.jsp";// 梧州电视机屏
			Browser.withPage(pagePath2, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("GuoHaoTuiSong({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof GuaQiTuiSong) {
			final String[] content = ((String) event.getSource()).split("@");

			String pagePath1 = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou.jsp";
			Browser.withPage(pagePath1, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("GuaQiTuiSong({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath2 = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou2.jsp";// 梧州电视机屏
			Browser.withPage(pagePath2, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("GuaQiTuiSong({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof ShowWXCallInfoEvent) {// 显示无锡叫号信息事件

			final String[] content = ((String) event.getSource()).split("@");

			String pagePath = ctx.getContextPath()
					+ "/window/tv_window_wu_xi_2.jsp";
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showMessage({serialNum: '")
							.appendScript(content[0])
							.appendScript("', winNum: '")
							.appendScript(content[1])
							.appendScript("', code: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof ReadDataEvent) {// 读取数据事件
			final String[] content = ((String) event.getSource()).split("&");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@" + content[0]);
			if (null == session)
				return;

			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showData({title: '")
							.appendScript(content[1])
							.appendScript("', content: ")
							.appendScript(content[2])
							// .appendScript(",").appendScript(content[3])
							.appendScript("})");
					session.addScript(buffer);
				}
			});
		} else if (event instanceof ShowLzxxDataEvent) {// 读取数据事件
			final String[] content = ((String) event.getSource()).split("&");
			/*
			 * ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			 * final ScriptSession session = ssm.getValue("S@" + content[0]); if
			 * (null == session) return;
			 * 
			 * Browser.withSession(session.getId(), new Runnable() { public void
			 * run() { ScriptBuffer buffer = new ScriptBuffer();
			 * buffer.appendScript
			 * ("showLzxxData({jsz: '").appendScript(content[1])
			 * .appendScript("', xsz: '").appendScript(content[2])
			 * .appendScript("', djzs: '").appendScript(content[3])
			 * .appendScript("'})"); session.addScript(buffer); } });
			 */
			String pagePath = ctx.getContextPath()
					+ "/window/tv_queue_led_1.jsp";
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showLzxxData({jsz: '")
							.appendScript(content[0]).appendScript("', xsz: '")
							.appendScript(content[1])
							.appendScript("', djzs: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath1 = ctx.getContextPath()
					+ "/window/tv_queue_led_2.jsp";
			Browser.withPage(pagePath1, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showLzxxData({jsz: '")
							.appendScript(content[0]).appendScript("', xsz: '")
							.appendScript(content[1])
							.appendScript("', djzs: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			
		} else if (event instanceof ShowZhpLzxx) {// 银川临时使用，不需要后可删除
			final String[] content = ((String) event.getSource()).split("&");
			String pagePath2 = ctx.getContextPath()
					+ "/window/tv_queue_led_yinchuan.jsp";
			Browser.withPage(pagePath2, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showLzxxData({hphm: '")
							.appendScript(content[0]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof ShowZhxxDataEvent) {// 济宁综合屏数据
			final String[] content = ((String) event.getSource()).split("&");
			if ("0".equals(content[0])) {
				String pagePath = ctx.getContextPath()
						+ "/window/tv_queue_led_jn.jsp";
				Browser.withPage(pagePath, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showZhxxData({flag: '")
								.appendScript(content[0]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});
			} else {
				String pagePath = ctx.getContextPath()
						+ "/window/tv_queue_led_jn1.jsp";
				Browser.withPage(pagePath, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showZhxxData({flag: '")
								.appendScript(content[0]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});
			}
		} else if (event instanceof EvaluateEvent) {// 请评价事件
			final String[] content = ((String) event.getSource()).split("@");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@" + content[0]);
			if (null == session) {
				return;
			}

			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("toEvaluation({reason:'")
							.appendScript(content[1])
							.appendScript("', isCamera:'")
							.appendScript(content[2])
							.appendScript("', isSignature:'")
							.appendScript(content[3])
							.appendScript("', isOpenForceEnvalue:'")
							.appendScript(content[4])
							.appendScript("', pt:'")
							.appendScript(content[5]).appendScript("'})");
					session.addScript(buffer);
				}
			});
		} else if (event instanceof EvaluateCompleteEvent) {// 评价完成事件
			final String[] datas = ((String) event.getSource()).split("@");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("M@" + datas[0]);
			if (null == session) {
				return;
			}

			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					try {
						buffer.appendScript(
								"sw.complete({isComplete:true, msg:'")
								.appendScript(
										URLEncoder.encode(datas[1], "UTF-8"))
								.appendScript("'})");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					session.addScript(buffer);
				}
			});
		} else if (event instanceof PassEvent) {// 过号显示滚动信息事件
			final String[] content = ((String) event.getSource()).split("@");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@" + content[0]);
			if (null == session)
				return;

			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("passshowdata('" + content[1] + "')");
					session.addScript(buffer);

				}
			});
		} else if (event instanceof PauseOrRecoverEvent) {// 暂停或恢复事件
			final String[] content = ((String) event.getSource()).split("@");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@" + content[1]);
			//final ScriptSession session = ssm.getValue("S@" + "192.168.1.70");
			if (null == session)
				return;

			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("pause(").appendScript(content[0])
							.appendScript(")");
					session.addScript(buffer);
				}
			});
		} else if (event instanceof DualScreenEvent) {// 双屏信息事件
			final String[] content = ((String) event.getSource()).split("@");
			final String[] num = content[1].split(":");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@" + content[0]);
			if (null == session)
				return;
			
			
			if(num[0].equals("1")){
				Browser.withSession(session.getId(), new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showJDCMessage({hpzl:'")
								.appendScript(num[1]).appendScript("',")
								.appendScript("hpzm:'")
								.appendScript(num[2]).appendScript("',")
								.appendScript("clpp1:'")
								.appendScript(num[3]).appendScript("',")
								.appendScript("cllx:'")
								.appendScript(num[4]).appendScript("',")
								.appendScript("djrq:'")
								.appendScript(num[5]).appendScript("',")
								.appendScript("yxqz:'")
								.appendScript(num[6]).appendScript("',")
								.appendScript("zt:'")
								.appendScript(num[7])
								.appendScript("'})");
						session.addScript(buffer);
					}
				});
				
			}else if(num[0].equals("2")){
				Browser.withSession(session.getId(), new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showJSRMessage({sfzmhm:'")
								.appendScript(num[1]).appendScript("',")
								.appendScript("lxzsyzbm:'")
								.appendScript(num[2]).appendScript("',")
								.appendScript("lxzsxxdz:'")
								.appendScript(num[3]).appendScript("',")
								.appendScript("cllx:'")
								.appendScript(num[4]).appendScript("',")
								.appendScript("xm:'")
								.appendScript(num[5]).appendScript("',")
								.appendScript("lxdh:'")
								.appendScript(num[6]).appendScript("',")
								.appendScript("sjhm:'")
								.appendScript(num[7]).appendScript("',")
								.appendScript("binss:'")
								.appendScript(num[8])
								.appendScript("'})");
						session.addScript(buffer);
					}
				});
				
			}else{
				if(num[3]!=null){
					Browser.withSession(session.getId(), new Runnable() {
						public void run() {
							ScriptBuffer buffer = new ScriptBuffer();
							buffer.appendScript("showDualScreenInfo({number:'")
									.appendScript(num[0]).appendScript("',")
									.appendScript("isOpenIndexCamera:'")
									.appendScript(num[2]).appendScript("',")
									.appendScript("id:'")
									.appendScript(num[1]).appendScript("',")
									.appendScript("numphoto:'")
									.appendScript(num[3])
									.appendScript("'})");
							session.addScript(buffer);
						}
					});
				}else {
					Browser.withSession(session.getId(), new Runnable() {
						public void run() {
							ScriptBuffer buffer = new ScriptBuffer();
							buffer.appendScript("showDualScreenInfo({number:'")
									.appendScript(num[0]).appendScript("',")
									.appendScript("isOpenIndexCamera:'")
									.appendScript(num[2]).appendScript("',")
									.appendScript("id:'").appendScript(num[1])
									.appendScript("'})");
							session.addScript(buffer);
						}
					});
				}
			}
		} else if (event instanceof DualScreenEventYZ) {// 双屏信息事件验证
			System.out.println("进入双屏页面状态验证");
			final String[] content = ((String) event.getSource()).split("@");
			final String num = content[1];
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@" + content[0]);
			if (null == session)
				return;

			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					if ("0".equals(content[2])) {
						buffer.appendScript("showDualScreenInfoYZ({yzxx:'")
								.appendScript(num).appendScript("'})");
					} else if ("1".equals(content[2])) {
						buffer.appendScript("showXhym({yzxx:'")
								.appendScript(num).appendScript("'})");
					}
					session.addScript(buffer);
				}
			});

		} else if (event instanceof DualScreenEventWP) {// 定时监听外屏通讯

			final String[] content = ((String) event.getSource()).split("@");
			final String num = content[1];

			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("S@" + content[0]);
			// final Employee employee = (Employee) event.getSource();
			// System.out.println(employee.getLoginIp());
			// ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			// final ScriptSession session = ssm.getValue("S@"
			// + employee.getLoginIp());

			if (null == session)
				return;
			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					System.out.println("进入双屏通讯验证");
					buffer.appendScript("showDualScreenInfoYZ({yzxx:'")
							.appendScript(num).appendScript("'})");
					session.addScript(buffer);
				}
			});

		} else if (event instanceof DualScreenEventFail) {// 小窗口外屏通讯失败推送
			final String content = (String) event.getSource();
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			final ScriptSession session = ssm.getValue("M@" + content);
			// final Employee employee = (Employee) event.getSource();
			// ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			// final ScriptSession session = ssm.getValue("S@"
			// + employee.getLoginIp());

			if (null == session)
				return;
			Browser.withSession(session.getId(), new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					System.out.println("双屏通讯验证结果: " + content + "外屏通讯失败");
					buffer.appendScript("showFail()");
					session.addScript(buffer);
				}
			});
		}else if (event instanceof DualScreenEventIMG) {//双屏信息事件验证
			   System.out.println("进入双屏推送IMG页面");
			   final String[] content = ((String) event.getSource()).split("@");
			   final String img =content[1];
			   final String message = content[2];
			   final String bj = content[3];
				ScriptSessionManager ssm = ScriptSessionManager.getInstance();
				final ScriptSession session = ssm.getValue("S@" + content[0]);
				if (null == session) return;
				
				Browser.withSession(session.getId(), new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer
						.appendScript("showDualScreenInfoIMG({image:'")
						.appendScript(img)
						.appendScript("', message: '")
						.appendScript(message)
						.appendScript("', bj: '")
						.appendScript(bj)
						.appendScript("'})");
						session.addScript(buffer);
					}
				});
		   
		   }else if (event instanceof passLzxx) {// 推送领证信息
			System.out.println("进入领证推送");
			final String[] content = ((String) event.getSource()).split("@");

			if ("0".equals(content[2])) {
				// String pagePath = ctx.getContextPath() +
				// "/window/tv_queue_led_yinchuanDS.jsp";
				String pagePath = "/queue/window/tv_queue_led_yinchuanDS.jsp";
				// System.out.println("pagePath="+pagePath);
				Browser.withPage(pagePath, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});

				// String pagePath1 = ctx.getContextPath() +
				// "/window/tv_queue_led_weifang.jsp";
				String pagePath1 = "/queue/window/tv_queue_led_weifang.jsp";
				Browser.withPage(pagePath1, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});
				// String pagePath2 = ctx.getContextPath() +
				// "/window/tv_queue_led_jiangmen.jsp";
				String pagePath2 = "/queue/window/tv_queue_led_jiangmen.jsp";
				Browser.withPage(pagePath2, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});
				// String pagePath3 = ctx.getContextPath() +
				// "/window/tv_queue_led_haikou.jsp";
				String pagePath3 = "/queue/window/tv_queue_led_haikou.jsp";
				Browser.withPage(pagePath3, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});
				String pagePath4 = "/queue/window/tv_queue_led_guigang.jsp";
				Browser.withPage(pagePath4, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});
				String pagePath5 = "/queue/window/tv_queue_led_nanningLZ.jsp";
				Browser.withPage(pagePath5, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
					}
				});
				String pagePath6 = "/queue/window/tv_queue_led_guigang_2.jsp";
				Browser.withPage(pagePath6, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
						System.out.println(buffer);
					}
				});
				
				String pagePath8 = "/queue/window/tv_queue_led_wuzhou_jsr.jsp";
				Browser.withPage(pagePath8, new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("showLzxx({xm: '")
								.appendScript(content[0])
								.appendScript("', barid: '")
								.appendScript(content[1]).appendScript("'})");
						ScriptSessions.addScript(buffer);
						System.out.println(buffer+"梧州");
					}
				});

			}
			// String pagePath3 = ctx.getContextPath() +
			// "/window/tv_queue_led_haikou.jsp";
			String pagePath4 = "/queue/window/tv_queue_led_liuzhouZHP.jsp";
			Browser.withPage(pagePath4, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showLzxx({xm: '")
							.appendScript(content[0])
							.appendScript("', barid: '")
							.appendScript(content[1]).appendScript("', bj: '")
							.appendScript(content[2]).appendScript("', lsh: '")
							.appendScript(content[3]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			// String pagePath5 = ctx.getContextPath()
			// +"/window/tv_queue_led_fangchenggang.jsp";
			String pagePath5 = "/queue/window/tv_queue_led_fangchenggang.jsp";
			Browser.withPage(pagePath5, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showLzxx({xm: '")
							.appendScript(content[0])
							.appendScript("', barid: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
					System.out.println(buffer);
				}
			});
			// String pagePath6 = ctx.getContextPath() +
			// "/window/tv_queue_led_haikou.jsp";
			String pagePath6 = "/queue/window/tv_queue_led_anyang.jsp";
			Browser.withPage(pagePath6, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showLzxx({xm: '")
							.appendScript(content[0])
							.appendScript("', barid: '")
							.appendScript(content[1]).appendScript("', bj: '")
							.appendScript(content[2]).appendScript("', lsh: '")
							.appendScript(content[3]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			String pagePath7 = "/queue/window/tv_queue_led_fangchenggangSP.jsp";
			Browser.withPage(pagePath7, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showLzxx({xm: '")
							.appendScript(content[0])
							.appendScript("', barid: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
					System.out.println(buffer);
				}
			});
			String pagePath8 = "/queue/waiping/lingzhengping_1080_1920.jsp";
			Browser.withPage(pagePath8, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					System.out.println("xm="+content[0]+"----barid="+content[1]+"bj="+content[2]+"----lsh"+content[3]);
					buffer.appendScript("showLzxx({xm: '")
							.appendScript(content[0])
							.appendScript("', barid: '")
							.appendScript(content[1]).appendScript("', bj: '")
							.appendScript(content[2]).appendScript("', lsh: '")
							.appendScript(content[3]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			

		} else if (event instanceof tsLzxx) {// 成都接口领证信息推送
			System.out.println("内容=" + (String) event.getSource());
			final String[] content = ((String) event.getSource()).split("@");
			String pagePath = ctx.getContextPath()
					+ "/window/tv_queue_led_chengdu.jsp";
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("tsLzxx({bj: '")
							.appendScript(content[0])
							.appendScript("', xxbh: '")
							.appendScript(content[1]).appendScript("', nr: '")
							.appendScript(content[2]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof SendPJXXEvent) {
			String pagePath = "/queue/window/tv_queue_led_liuzhouPJXXP.jsp";
			final String content = ((StringBuffer) event.getSource())
					.toString();
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					// buffer.appendScript("showPJXX({bj: '").appendScript(content[0])
					// .appendScript("', xxbh: '").appendScript(content[1])
					// .appendScript("', nr: '").appendScript(content[2])
					// .appendScript("'})");
					// ScriptSessions.addScript(buffer);
					buffer.appendScript("showPJXX({content:'")
							.appendScript(content).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath2 = "/queue/window/tv_queue_led_fangchenggang.jsp";
			System.out.println(pagePath2);
			final String content2 = ((StringBuffer) event.getSource())
					.toString();
			Browser.withPage(pagePath2, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showPJXX({content:'")
							.appendScript(content2).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath3 = "/queue/window/tv_queue_led_nanning_pj.jsp";
			System.out.println(pagePath3);
			final String content3 = ((StringBuffer) event.getSource())
					.toString();
			Browser.withPage(pagePath3, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showPJXX({content:'")
							.appendScript(content3).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath4 = "/queue/window/tv_queue_led_guigang_2.jsp";
			System.out.println(pagePath4);
			final String content4 = ((StringBuffer) event.getSource())
					.toString();
			Browser.withPage(pagePath4, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showPJXX({content:'")
							.appendScript(content4).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof ShowYWDDRSEvent) {
			String pagePath1 = "/queue/window/tv_queue_led_zhuhai_zongsuo.jsp";
			final String[] content = ((String) event.getSource()).split("@");
			Browser.withPage(pagePath1, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					// buffer.appendScript("showPJXX({bj: '").appendScript(content[0])
					// .appendScript("', xxbh: '").appendScript(content[1])
					// .appendScript("', nr: '").appendScript(content[2])
					// .appendScript("'})");
					// ScriptSessions.addScript(buffer);
					buffer.appendScript("showYWL({ywlx: '")
							.appendScript(content[0])
							.appendScript("', ddrs: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

			String pagePath2 = "/queue/window/tv_queue_led_zhongshan_ZHP.jsp";
			final String[] content2 = ((String) event.getSource()).split("@");
			Browser.withPage(pagePath2, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					// buffer.appendScript("showPJXX({bj: '").appendScript(content[0])
					// .appendScript("', xxbh: '").appendScript(content[1])
					// .appendScript("', nr: '").appendScript(content[2])
					// .appendScript("'})");
					// ScriptSessions.addScript(buffer);
					buffer.appendScript("showYWL({ywlx: '")
							.appendScript(content[0])
							.appendScript("', ddrs: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			
			String pagePath3 = "/queue/window/tv_queue_led_wuzhou.jsp";
			final String[] content3 = ((String) event.getSource()).split("@");
			Browser.withPage(pagePath3, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					// buffer.appendScript("showPJXX({bj: '").appendScript(content[0])
					// .appendScript("', xxbh: '").appendScript(content[1])
					// .appendScript("', nr: '").appendScript(content[2])
					// .appendScript("'})");
					// ScriptSessions.addScript(buffer);
					buffer.appendScript("showYWL({ywlx: '")
							.appendScript(content[0])
							.appendScript("', ddrs: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			
			String pagePath4 = "/queue/window/tv_queue_led_wuzhou_jsr.jsp";
			final String[] content4 = ((String) event.getSource()).split("@");
			Browser.withPage(pagePath4, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					// buffer.appendScript("showPJXX({bj: '").appendScript(content[0])
					// .appendScript("', xxbh: '").appendScript(content[1])
					// .appendScript("', nr: '").appendScript(content[2])
					// .appendScript("'})");
					// ScriptSessions.addScript(buffer);
					buffer.appendScript("showYWL({ywlx: '")
							.appendScript(content[0])
							.appendScript("', ddrs: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
			
			String pagePath5 = "/queue/window/tv_queue_led_beihai.jsp";
			final String[] content5 = ((String) event.getSource()).split("@");
			Browser.withPage(pagePath5, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					// buffer.appendScript("showPJXX({bj: '").appendScript(content[0])
					// .appendScript("', xxbh: '").appendScript(content[1])
					// .appendScript("', nr: '").appendScript(content[2])
					// .appendScript("'})");
					// ScriptSessions.addScript(buffer);
					buffer.appendScript("showYWL({ywlx: '")
							.appendScript(content[0])
							.appendScript("', ddrs: '")
							.appendScript(content[1]).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof PauseOrRecoverEventZS) {// 中山暂停恢复业务
			String pagePath = "/queue/window/tv_queue_led_zhongshan.jsp";
			final String content = (String) event.getSource();
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("PauseOrRecover({winNum: '")
							.appendScript(content).appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});
		} else if (event instanceof SendStatusCY) {
			final String[] content = ((String) event.getSource()).split("@");
			// 梧州查验屏
			String pagePath = ctx.getContextPath()
					+ "/window/tv_queue_led_wuzhou_CY.jsp";
			Browser.withPage(pagePath, new Runnable() {
				public void run() {
					ScriptBuffer buffer = new ScriptBuffer();
					buffer.appendScript("showCYMessage({carType: '")
							.appendScript(content[0])
							.appendScript("', carNumber: '")
							.appendScript(content[1])
							.appendScript("', sendstatus0: '")
							.appendScript(content[2])
							.appendScript("', sendstatus1: '")
							.appendScript(content[3])
							.appendScript("', SerialNum: '")
							.appendScript(content[4])
							.appendScript("'})");
					ScriptSessions.addScript(buffer);
				}
			});

		} else if (event instanceof CYTangKuang) {

		}else if (event instanceof RzdbTs) {
			final String[] content = ((String) event.getSource()).split("@");
			ScriptSessionManager ssm = ScriptSessionManager.getInstance();
			String ip;
 			if("0".equals(content[0])){
				ip="S@"+content[1];//双屏推送
			}else{
				ip="M@"+content[2];//小窗口推送
			}
			final ScriptSession session = ssm.getValue(ip);
			
			if (null == session)
				return;
			if("0".equals(content[0])){
				Browser.withSession(session.getId(), new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("sveinDb({loginIp: '")
						.appendScript(content[2]).appendScript("'})");
						session.addScript(buffer);
					}
				});
			}else {
				Browser.withSession(session.getId(), new Runnable() {
					public void run() {
						ScriptBuffer buffer = new ScriptBuffer();
						buffer.appendScript("sveinRet({retCode: '")
						.appendScript(content[1]).appendScript("'})");
						session.addScript(buffer);
					}
				});
			}
				
		}
	}
}