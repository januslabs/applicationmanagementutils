package org.januslabs.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class ManagementServiceTest {

	@Test
	public void parseURL() throws Exception
	{
		URL url=new URL("http://localhost:8080/ContactUs/admin/logger/DEBUG/ContactUs");
		String URI=url.getPath();
		String logLevel=URI.substring(0, URI.lastIndexOf("/") );
		logLevel=logLevel.substring(logLevel.lastIndexOf("/")+1);
	    String contextName=URI.substring( URI.lastIndexOf("/")+1 );
	    Assert.assertEquals("ContactUs", contextName);
	    Assert.assertEquals("DEBUG", logLevel);
	}
	
	@Test
	public void parseURL_NonServletMapped() throws Exception
	{
		URL url=new URL("http://localhost:8080/testservices/admin/logger/ARS/debug");
		String URI=url.getPath();
		String contextName=URI.substring(0, URI.lastIndexOf("/") );
		contextName=contextName.substring(contextName.lastIndexOf("/")+1);
	    String logLevel=URI.substring( URI.lastIndexOf("/")+1 );
	    Assert.assertEquals("debug", logLevel);
	}
	
	@Test
	public void regex() throws Exception
	{
		String name="SEVERE: The web application  [/assurantregistrationservice] " +
				"appears to have started a thread named [com.google.inject.internal.util.$Finalizer] " +
				"but has failed to stop it. This is very likely to create a memory leak.";
		  if(name.contains("com\\.google.inject.internal.*InjectorImpl") || name.contains("com.google.inject.internal.util.$Finalizer"))
		  {
			  Assert.assertTrue(true);
		  }
	}
	
	@Test
	public void getRegionNameByIPInternal() 
	{
		String remoteAddress="167.79.196.6";
		try
		{
		HttpURLConnection httpConnection=(HttpURLConnection)new URL("http://msp0lnans001.etdbw.com/galaga/geoip/"+ remoteAddress.trim()).openConnection();
		httpConnection.addRequestProperty("Accept", "application/json");
		httpConnection.setRequestMethod("GET");
		httpConnection.setDoInput(true);
		httpConnection.setDoInput(true);
		httpConnection.setReadTimeout(100);
		httpConnection.setConnectTimeout(200);
		BufferedReader reader=new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		String data=null;
		StringBuilder sb=new StringBuilder();
		sb.trimToSize();
		while((data=reader.readLine())!=null)
		{
			sb.append(data);
		}
		JSONObject jsonData=new JSONObject(sb.toString());
		System.out.println(jsonData);
		String response= jsonData.optString("region_name");
		System.out.println(response);
		
		}catch(Exception ex)
		{
			ex.printStackTrace(System.out);
		}
		
	}
	
	@Ignore
	public void getRegionNameByIP() throws Exception
	{
		HttpURLConnection httpConnection=(HttpURLConnection)new URL("http://freegeoip.net/json/167.79.196.6").openConnection();
		httpConnection.setRequestMethod("GET");
		httpConnection.setDoInput(true);
		httpConnection.setDoInput(true);
		BufferedReader reader=new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		String data=null;
		StringBuilder sb=new StringBuilder();
		sb.trimToSize();
		while((data=reader.readLine())!=null)
		{
			sb.append(data);
		}
		JSONObject jsonData=new JSONObject(sb.toString());
		System.out.println(jsonData.optString("region_name"));
		System.out.println(jsonData.optString("region_code"));
		System.out.println(jsonData.optString("zipcode"));
		System.out.println(jsonData.optString("ip"));
		System.out.println(sb.toString());
		Map<JSONObject,String> sessionMap=new ConcurrentHashMap<JSONObject, String>();
		sessionMap.put(new JSONObject() ,"MO");
		sessionMap.put(new JSONObject() ,"MO");
		sessionMap.put(new JSONObject() ,"KS");
		sessionMap.put(new JSONObject() ,"KS");
		sessionMap.put(new JSONObject() ,"KS");
		sessionMap.put(new JSONObject() ,"NE");
		Set<String> regionSet=new HashSet<String>(sessionMap.values());
		System.out.format("%20s%20s", "RegionName", "Active Users");
		
		for (String region : regionSet) 
		{
			Integer count=Collections.frequency(sessionMap.values(), region);
			//System.out.println("Region /count :\t" + region + " " + count);
			printTabular(region, count);
		}
			
		
	}
	
	
	
	private void printTabular(String region, Integer count)
	{
		System.out.println("\r\n");
		System.out.format("%20s%20s", region, count);
	}	
	 
}
