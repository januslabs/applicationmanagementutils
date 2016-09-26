package org.januslabs.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.ConcurrentTestRunner;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;

@RunWith(ConcurrentTestRunner.class)
public class GeoIpLoadTest {

	@Rule public ConcurrentRule rule = new ConcurrentRule();
	@Rule public RepeatingRule repeatedly = new RepeatingRule();
	private AtomicInteger count=new AtomicInteger(0);
	
	@Test
	@Concurrent(count = 1)
	@Repeating(repetition=1)
	public void getRegionNameByIP() 
	{
		System.out.println(count.incrementAndGet());
		String remoteAddress="167.79.196.6";
		try
		{
		HttpURLConnection httpConnection=(HttpURLConnection)new URL("http://msp0lnans001.etdbw.com/galaga/geoip/"+ remoteAddress.trim()).openConnection();
		httpConnection.addRequestProperty("Accept", "application/json");
		httpConnection.setRequestMethod("GET");
		httpConnection.setDoInput(true);
		httpConnection.setDoInput(true);
		httpConnection.setReadTimeout(1000);
		httpConnection.setConnectTimeout(2000);
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
	
}
