// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.devrock.zarathud.model.request;

import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.Resource;

/**
 * 
 */
public interface AnalyzeArtifactRequest extends BasicZedRequest {
	
	EntityType<AnalyzeArtifactRequest> T = EntityTypes.T(AnalyzeArtifactRequest.class);
	
	String resolution = "resolution";
	String terminal = "terminal";
	String customRatingsResource = "customRatingsResource";
	String pullRequetsResource = "pullRequetsResource";
	
	AnalysisArtifactResolution getResolution();
	void setResolution(AnalysisArtifactResolution value);

	AnalysisArtifact getTerminal();
	void setTerminal(AnalysisArtifact value);
	
	/**
	 * overrides internal (hardwired) level of ratings 
	 * @return - in most cases an external file that is injected 
	 */
	Resource getCustomRatingsResource();
	void setCustomRatingsResource(Resource value);

	/**
	 * overrides the combined levels of ratings (hardwired and injected)
	 * @return - in most cases, the file that is referenced as part of the terminal
	 */
	Resource getPullRequestResource();
	void setPullRequestResource(Resource value);


}
