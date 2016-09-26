package org.januslabs.util;

import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.ALWAYS)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GitRepositoryState {

	  String tags;                    // =${git.tags} // comma separated tag names
	  String branch;                  // =${git.branch}
	  String dirty;                   // =${git.dirty}
	  String remoteOriginUrl;         // =${git.remote.origin.url}

	  String commitId;                // =${git.commit.id.full} OR ${git.commit.id}
	  String commitIdAbbrev;          // =${git.commit.id.abbrev}
	  String describe;                // =${git.commit.id.describe}
	  String describeShort;           // =${git.commit.id.describe-short}
	  String commitUserName;          // =${git.commit.user.name}
	  String commitUserEmail;         // =${git.commit.user.email}
	  String commitMessageFull;       // =${git.commit.message.full}
	  String commitMessageShort;      // =${git.commit.message.short}
	  String commitTime;              // =${git.commit.time}
	  String closestTagName;          // =${git.closest.tag.name}
	  String closestTagCommitCount;   // =${git.closest.tag.commit.count}

	  String buildUserName;           // =${git.build.user.name}
	  String buildUserEmail;          // =${git.build.user.email}
	  String buildTime;               // =${git.build.time}
	  String buildHost;               // =${git.build.host}
	  String buildVersion;             // =${git.build.version}

	  public GitRepositoryState() {
	  }
	  
	  public GitRepositoryState(Properties properties)
	  {
	    this.tags = properties.get("git.tags").toString();
	    this.branch = properties.get("git.branch").toString();
	    this.dirty = properties.get("git.dirty").toString();
	    this.remoteOriginUrl = properties.get("git.remote.origin.url").toString();

	    this.commitId = properties.get("git.commit.id").toString(); // OR properties.get("git.commit.id") depending on your configuration
	    if(!this.commitId.isEmpty())
	    {
	    	this.commitId="http://msp0lnans001.etdbw.com:7990/projects/AEBC/repos/cherwell-api/commits/"+this.commitId;
	    }
	    this.commitIdAbbrev = properties.get("git.commit.id.abbrev").toString();
	    this.describe = properties.get("git.commit.id.describe").toString();
	    this.describeShort = properties.get("git.commit.id.describe-short").toString();
	    this.commitUserName = properties.get("git.commit.user.name").toString();
	    this.commitUserEmail = properties.get("git.commit.user.email").toString();
	    this.commitMessageFull = properties.get("git.commit.message.full").toString();
	    this.commitMessageShort = properties.get("git.commit.message.short").toString();
	    this.commitTime = properties.get("git.commit.time").toString();
	    this.closestTagName = properties.get("git.closest.tag.name").toString();
	    this.closestTagCommitCount = properties.get("git.closest.tag.commit.count").toString();

	    this.buildUserName = properties.get("git.build.user.name").toString();
	    this.buildUserEmail = properties.get("git.build.user.email").toString();
	    this.buildTime = properties.get("git.build.time").toString();
	    this.buildHost = properties.get("git.build.host").toString();
	    this.buildVersion = properties.get("git.build.version").toString();
	  }
}
