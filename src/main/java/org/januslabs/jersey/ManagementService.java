package org.januslabs.jersey;

import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.januslabs.servlet.ManagementSessionListener;
import org.januslabs.util.GitRepositoryState;
import org.januslabs.util.VersionRetriever;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;


@Path("/admin")
public class ManagementService {

	private static final org.slf4j.Logger  LOG= LoggerFactory.getLogger(ManagementService.class);
	@Context private ServletContext context;
	@Context private HttpServletRequest request;
	@Context private HttpHeaders headers;
	
	@GET
	@Path(value = "/version")
	@Produces({"text/plain"})
	public String version() throws Exception
	{
		LOG.debug("version called");
		VersionRetriever versionRet=new VersionRetriever();
		return versionRet.retrieveVersion();
	}
	
	@GET
	@Path(value = "/logger/{logLevel}/{contextName}")
	@Produces({"text/plain"})
	public Response changeLogLevel(@PathParam("logLevel")String logLevel,@PathParam("contextName")String contextName)
	{
		
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
		return Response.ok(logger.getLevel().toString()).build();
		
	}
	
	@GET
	@Path(value = "/session")
	@Produces({"text/plain"})
	public Response getActiveSessions() throws Exception
	{
		LOG.debug("getActiveSessions called");
		ManagementSessionListener sessionListener=ManagementSessionListener.getInstance(context);
		return Response.ok().entity("Concurrent user count is  " + sessionListener.getCount()).build();
		
	}
	
	@GET
	@Path(value = "/sessionbyregion")
	@Produces({"text/plain"})
	public Response getActiveSessionsByRegion() throws Exception
	{
		LOG.debug("getActiveSessions called");
		ManagementSessionListener sessionListener=ManagementSessionListener.getInstance(context);
		return Response.ok().entity( sessionListener.getCountByRegionName()).build();
		
	}
	
	@Path("/headers")
	@GET
	public String getHeaders() {
        StringBuilder buf = new StringBuilder();
        for (String header : headers.getRequestHeaders().keySet()) {
            buf.append("<li>");
            buf.append(header);
            buf.append(" = ");
            buf.append(headers.getRequestHeader(header));
            buf.append("</li>\n");
        }
        return buf.toString();
    }
	
	@GET
	@Path("/git")
	@Produces(MediaType.APPLICATION_JSON)
	public GitRepositoryState git() throws Exception{
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));

		GitRepositoryState gitRepositoryState = new GitRepositoryState(properties);
		return gitRepositoryState;
		

	}
}
