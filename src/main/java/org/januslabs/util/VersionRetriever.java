package org.januslabs.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jcabi.manifests.Manifests;

public class VersionRetriever {

	@SuppressWarnings("resource")
	public String retrieveVersion()
	{
		Properties properties = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("version.properties");
		try {
			
			if(Manifests.exists("Build-Time"))
			{
				StringBuilder versionTextBuilder=new StringBuilder();
				if(Manifests.exists("Build-Time")) versionTextBuilder.append( String.format("Build-Time is  %s %n",Manifests.read("Build-Time")));
				if(Manifests.exists("Build-Java")) versionTextBuilder.append( String.format("Build-Java is  %s %n",Manifests.read("Build-Java")));
				if(Manifests.exists("Build-By")) versionTextBuilder.append( String.format("Built-By   %s %n",Manifests.read("Built-By")));
				if(Manifests.exists("Project-Version")) versionTextBuilder.append( String.format("Project-Version is  %s %n",Manifests.read("Project-Version")));
				if(Manifests.exists("Build-User")) versionTextBuilder.append( String.format("Build-User is  %s %n",Manifests.read("Build-User")));
				if(Manifests.exists("Class-Path")) versionTextBuilder.append( String.format("Class-Path is  %s %n",Manifests.read("Class-Path")));
				if(Manifests.exists("ReleaseNotes")) versionTextBuilder.append( String.format("ReleaseNotes is  %s %n",Manifests.read("ReleaseNotes")));
				if(Manifests.exists("BuildURL")) versionTextBuilder.append( String.format("Jenkins-BuildUrl is  %s %n",Manifests.read("BuildURL")));
				if(Manifests.exists("BuildTag")) versionTextBuilder.append( String.format("Jenkins-BuildTag is  %s %n",Manifests.read("BuildTag")));
				
			
				if(Manifests.exists("ReleaseNotes"))
				{
					versionTextBuilder.append( String.format("ReleaseNotes is  %s %n",Manifests.read("ReleaseNotes")));
				}

				if(Manifests.exists("Git-Commit")) versionTextBuilder.append( String.format("Git-Commit is  %s %n", Manifests.read("Git-Commit")));

				return versionTextBuilder.toString();
			}
			
			else if(inputStream!=null)
			{

				properties.load(inputStream);
	
				StringBuilder version = new StringBuilder();
				version.append("\n" + properties.getProperty("service.name") + "\n");
				version.append("Version: " + properties.getProperty("service.version") + "\n");
				version.append("Description: " + properties.getProperty("service.description") + "\n\n");
	
				version.append("Jersey Version: " + properties.getProperty("jersey.version") + "\n\n");
				/*
				 * Note this one will not work properly on your local machine if you
				 * are running from within eclipse. Trust me it works in the real
				 * environment though.
				 */
				version.append("Build Time: " + properties.getProperty("build.time") + "\n");
			
				return version.toString();
			}
			else
			{
				return "No version logic has been implemented";
			}
		} catch (FileNotFoundException e) {
			return "FileNotFoundException";
		} catch (IOException e) {
			return "IOException";
		}
	}
}
