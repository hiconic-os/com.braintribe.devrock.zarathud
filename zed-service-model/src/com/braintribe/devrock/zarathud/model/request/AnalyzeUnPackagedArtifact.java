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
