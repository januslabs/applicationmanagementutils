package org.januslabs.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.manifests.ClasspathMfs;
import com.jcabi.manifests.Manifests;
import com.jcabi.manifests.ServletMfs;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;

@WebListener
public class ManagementServletContextListener implements ServletContextListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(ManagementServletContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
		LOG.debug("About to detach context named {} ", selector	.getDefaultLoggerContext().getName());
		LoggerContext context = selector.detachLoggerContext(selector.getDefaultLoggerContext().getName());
		if (context != null)
		{
			LOG.warn("Stopping logger context {} ", selector.getDefaultLoggerContext().getName());
			// when the web-app is destroyed, its logger context should be
			// stopped
			context.stop();
		} else 
		{
			LOG.warn("No context named  {}   was found.", selector.getDefaultLoggerContext().getName());
		}

		

	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		try 
		{
			LOG.debug("contextInitialized called {} ",event);
			Manifests.DEFAULT.append((new ClasspathMfs()));
			ServletContext sc = event.getServletContext();
			Manifests.DEFAULT.append((new ServletMfs(sc)));		
			LOG.debug("adding servlet  {} ",sc);
	        ServletRegistration sr = sc.addServlet("ManagementServlet","org.januslabs.servlet.ManagementServiceServlet");	       
	        sr.addMapping("/admin/*");	       
		} catch (Exception e) {
			// ignore
			LOG.error("error in contextInitialized {}" , e);
			e.printStackTrace();
		}

	}

}
