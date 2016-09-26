package org.januslabs.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.januslabs.util.VersionRetriever;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;

@WebServlet(urlPatterns={"/admin/*"})
public class ManagementServiceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1239720717206778702L;
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ManagementServiceServlet.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse) URL for logger is
	 * /admin/logger/{logLevel}/{contextName} URL for version is /admin/version
	 */
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String httpMethod = request.getMethod();
		LOG.info("Received http request {}", request);
		//String URI = request.getPathInfo();
		
		String URI = request.getPathInfo() == null ? (request.getServletPath() == null ? request.getRequestURI() : request.getServletPath()) : request.getPathInfo();
		LOG.info("Received http request , method  {}", httpMethod);
		LOG.info("Received http request , URI  {}", URI);
		if ("GET".equalsIgnoreCase(httpMethod)) 
		{
			if (URI.contains("version")) 
			{
				LOG.info("Received http request , version  {}", URI);
				VersionRetriever versionRet=new VersionRetriever();
				String text=versionRet.retrieveVersion();
				response.setContentType("text/plain");
		        response.setContentLength(text.length());
		        PrintWriter out = response.getWriter();
		        out.println(text);
		        out.close();
		        out.flush();
		        response.setStatus( HttpServletResponse.SC_OK );		        
			}

			else if (URI.contains("logger"))
			{
				LOG.info("Received http request , logger  {}", URI);
				
			
				String logLevel=URI.substring(0, URI.lastIndexOf("/") );
				logLevel=logLevel.substring(logLevel.lastIndexOf("/")+1);
			    String contextName=URI.substring( URI.lastIndexOf("/")+1 );
			    LOG.info("Received http request , loglevel {} , context Name  {}", URI);
							    
			    ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
				LoggerContext context =selector.getLoggerContext(contextName);
				List<Logger> loggers=context.getLoggerList();
				for (Logger logger : loggers)
				{			
					logger.setLevel(Level.toLevel(logLevel));
					LOG.debug("Changed log level to {} for the logger  {} " ,logger.getLevel(), logger.getName());
				}
				Logger logger = context.getLogger(Logger.ROOT_LOGGER_NAME);
				logger.setLevel(Level.toLevel(logLevel));				
				response.setContentType("text/plain");
		        response.setContentLength(logger.getLevel().toString().length());
		        PrintWriter out = response.getWriter();
		        out.println(logger.getLevel().toString());
		        out.close();
		        out.flush();
		        response.setStatus( HttpServletResponse.SC_OK );	
			}
			else if (URI.contains("headers"))
			{
				LOG.info("Received http request , header  {}", URI);
				PrintWriter out = response.getWriter();
				response.setContentType("text/plain");
				Enumeration<String> headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements()) {
					String headerName = headerNames.nextElement();
					out.write(headerName);
					out.write("\r\n");
					Enumeration<String> headers = request.getHeaders(headerName);
					while (headers.hasMoreElements())
					{
						String headerValue = headers.nextElement();
						out.write("\t" + headerValue);
						out.write("\r\n");
					}
				}
				out.close();
				out.flush();
			    response.setStatus( HttpServletResponse.SC_OK );
			}
			else
			{
				throw new IOException("yet to implemented");
			}

		}
	}
}
