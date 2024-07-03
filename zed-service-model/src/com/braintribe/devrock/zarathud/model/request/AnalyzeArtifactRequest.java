// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
