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

import java.util.List;

import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * analyzes an 'unpackaged' artifact, i.e. pom, and build directory, solutions
 */
public interface AnalyzeUnPackagedArtifact extends BasicZedRequest {
	
	EntityType<AnalyzeUnPackagedArtifact> T = EntityTypes.T(AnalyzeUnPackagedArtifact.class);

	// pom -> for resolution (CompiledArtifact)
	String getPom();
	void setPom(String value);
	
	// directory -> for scanning (path to build directory)
	String getClassesDirectory();
	void setClassesDirectory(String value);

	// solutions (AnalyisArtifact)
	List<AnalysisArtifact> getSolutions();
	void setSolutions(List<AnalysisArtifact> solutions);


}
