package com.suntendy.queue.util.cache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.suntendy.queue.queue.util.cache.PlayVoiceThread;

/*******************************************************************************
 * 描述: 缓存数据初始化及停止TOMCAT停止时未停止的JAVA进程 <br>
 * //////////////////////////////////////////////////////// <br>
 * 创建者: 刘东东 <br>
 * 创建日期: 2013-11-22 14:24:30 <br>
 * 修改者: <br>
 * 修改日期: <br>
 * 修改说明: <br>
 ******************************************************************************/
public class InitCacheListener implements ServletContextListener {
	private PlayVoiceThread playVoiceThread;
	private InitCacheManager initCacheManager;
	public static final List<String> MANUAL_DESTROY_THREAD_IDENTIFIERS = Arrays.asList("Thread");
	
	public void contextDestroyed(ServletContextEvent sce) {
		if (null != playVoiceThread) {
			playVoiceThread.interrupt();
		}
		
		if (null != initCacheManager) {
			initCacheManager.clearAll();
		}
		
		destroyJDBCDrivers();
		destroySpecifyThreads();
	}

	public void contextInitialized(ServletContextEvent sce) {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("spring/jdbc-oracle.properties");   
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		CacheManager cacheManager = CacheManager.getInstance();
		cacheManager.addOfDeptCache("deptCode", p.getProperty("dept.code"));
		cacheManager.addOfDeptCache("deptHall", p.getProperty("dept.hall"));
		cacheManager.addOfDeptCache("masterkeys", p.getProperty("dept.masterKeys"));
		cacheManager.addOfDeptCache("masterkeyEnd", p.getProperty("dept.masterKeyEnd"));
		cacheManager.addOfDeptCache("yfIp", p.getProperty("dept.yfIP"));
		cacheManager.addOfDeptCache("XTBS", p.getProperty("dept.XTBS"));
		cacheManager.addOfDeptCache("yfaqm", p.getProperty("dept.yfaqm"));
		cacheManager.addOfDeptCache("yfPort", p.getProperty("dept.yfPORT"));
		
		initCacheManager = new InitCacheManager(sce.getServletContext());
		initCacheManager.initSystem();
		initCacheManager.initBusinessType();
		initCacheManager.initQueueCache();
		initCacheManager.initNumber();
		initCacheManager.initLED();
		initCacheManager.initLED_tv();
		initCacheManager.initWindowCache(true);
		initCacheManager.initBAXX();
		
		String filePath = sce.getServletContext().getRealPath("/plugs");
		//启动播放声音线程
		playVoiceThread = new PlayVoiceThread(filePath);
		playVoiceThread.start();
	}
	
	/**
	 * 中断未停止的线程
	 */
	@SuppressWarnings("deprecation")
	private void destroySpecifyThreads() {
		try {
			final Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
			threadLocalsField.setAccessible(true);
			final Field inheritableThreadLocalsField = Thread.class.getDeclaredField("inheritableThreadLocals");
			inheritableThreadLocalsField.setAccessible(true);
			
			final Set<Thread> threads = Thread.getAllStackTraces().keySet();
			for (final Thread thread : threads) {
				if (needManualDestroy(thread)) {
					synchronized (this) {
						try {
							thread.stop();
//							thread.interrupt();
						} catch (Exception e) {}
					}
					
					continue;
				}
				clear(threadLocalsField.get(thread));
				clear(inheritableThreadLocalsField.get(thread));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean needManualDestroy(Thread thread) {
		final String threadName = thread.getName();
		for (String manualDestroyThreadIdentifier : MANUAL_DESTROY_THREAD_IDENTIFIERS) {
			if (threadName.contains(manualDestroyThreadIdentifier)) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void clear(final Object threadLocalMap) throws Exception {
        if (null == threadLocalMap) return;
        
        final Field tableField = threadLocalMap.getClass().getDeclaredField("table");
        tableField.setAccessible(true);
        final Object table = tableField.get(threadLocalMap);
        for (int i = 0, length = Array.getLength(table); i < length; i++) {
            final Object entry = Array.get(table, i);
            if (entry != null) {
                final Object threadLocal = ((WeakReference)entry).get();
                if (threadLocal != null) {
                    Array.set(table, i, null);
                }
            }
        }
    }

	/**
	 * 撤销注册的JDBC驱动
	 */
	private void destroyJDBCDrivers() {
		final Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}