package org.januslabs.stereotype;

import org.glassfish.jersey.message.internal.TracingLogger.Level;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.TracingConfig;

import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;

/*
 * Base class that registers  swagger endpoints and version endpoints.
 * Also enables on demand tracing, please pass the following header to
 * get tracing going.
 * Header - X-Jersey-Tracing-Threshold , possible values are  "SUMMARY", "TRACE" & "VERBOSE"
 * X-Jersey-Tracing-Accept, possible values ON_DEMAND , ALL and OFF 
 */
public class AEBJerseyConfig extends ResourceConfig {

	public AEBJerseyConfig() 
	{	
		packages("com.wordnik.swagger.jaxrs.json","com.assurant.inc.jersey");
		register(ApiListingResourceJSON.class);
		register(JerseyApiDeclarationProvider.class);
		register(JerseyResourceListingProvider.class);
		property(ServerProperties.WADL_FEATURE_DISABLE, true);
		property(ServerProperties.TRACING, TracingConfig.ON_DEMAND.toString());
		property(ServerProperties.TRACING_THRESHOLD,Level.SUMMARY.toString() );
	}
	
	/*public void addPackages(String ... packages)
	{
		List<String> packageList=Arrays.asList(Tokenizer.tokenize(packages, Tokenizer.COMMON_DELIMITERS));
		packageList.add("com.wordnik.swagger.jaxrs.json");
		packageList.add("com.assurant.inc.jersey");
		packages(packageList.toArray(new String[packageList.size()]));
	}*/

}