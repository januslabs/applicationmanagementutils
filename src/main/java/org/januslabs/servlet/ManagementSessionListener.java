package org.januslabs.servlet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class ManagementSessionListener implements HttpSessionListener,ServletContextListener,ServletRequestListener {

	private static final String ATTRIBUTE_NAME = "com.assurant.inc.servlet.ManagementSessionListener";
	final private AtomicInteger count=new AtomicInteger(0);
	private Map<HttpSession,String> sessionMap=new ConcurrentHashMap<HttpSession, String>();
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent)
	{		
		count.incrementAndGet();
		sessionEvent.getSession().setAttribute("count", count.get());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) 
	{		
		count.decrementAndGet();
		sessionEvent.getSession().setAttribute("count", count.get());
		sessionMap.remove(sessionEvent.getSession());
	}

	public Integer getCount()
	{
		return count.get();
	}
	
	public String getCountByRegionName()
	{
		Set<String> regionSet=new HashSet<String>(sessionMap.values());
		StringBuilder responseBuilder=new StringBuilder();
		responseBuilder.append(String.format("%20s%20s", "RegionName", "Active Users"));
		responseBuilder.append("\r\n");
		responseBuilder.append("-----------------------------------------");
		responseBuilder.append("\r\n");
		for (String region : regionSet) 
		{
			Integer count=Collections.frequency(sessionMap.values(), region);
			responseBuilder.append(String.format("%20s%20s", region, count));
			responseBuilder.append("\r\n");
		}
		responseBuilder.trimToSize();
		return responseBuilder.toString();
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event)
	{
		//nothing		
	}

	@Override
	public void requestInitialized(ServletRequestEvent event) 
	{
		HttpServletRequest servletRequest=(HttpServletRequest)event.getServletRequest();
		HttpSession session=servletRequest.getSession();
		if(session.isNew())
		{
			String remoteAddress=servletRequest.getRemoteAddr();
			sessionMap.put(session, remoteAddress);
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		//nothing		
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) 
	{
		servletContextEvent.getServletContext().setAttribute(ATTRIBUTE_NAME, this);		
	}
	
	public static ManagementSessionListener getInstance(ServletContext contextServlet)
	{
		return (ManagementSessionListener)contextServlet.getAttribute(ATTRIBUTE_NAME);
	}
	
	
	/*private String getRegionNameByIP(String remoteAddress) 
	{
		try
		{
		HttpURLConnection httpConnection=(HttpURLConnection)new URL("http://msp0lnans001.etdbw.com/galaga/geoip/"+ remoteAddress.trim()).openConnection();
		httpConnection.addRequestProperty("Accept", "application/json");
		httpConnection.setRequestMethod("GET");
		httpConnection.setDoInput(true);
		httpConnection.setDoInput(true);
		httpConnection.setReadTimeout(500);
		httpConnection.setConnectTimeout(100);
		BufferedReader reader=new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		String data=null;
		StringBuilder sb=new StringBuilder();
		sb.trimToSize();
		while((data=reader.readLine())!=null)
		{
			sb.append(data);
		}
		JSONObject jsonData=new JSONObject(sb.toString());
		return jsonData.optString("region_name");
		
		}catch(Exception ex)
		{
			return remoteAddress;
		}
		
	}*/
}
