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
package com.braintribe.devrock.zarathud.model;

import java.util.List;

import com.braintribe.devrock.zarathud.model.context.ConfigurationAspect;
import com.braintribe.devrock.zarathud.model.context.CoreAnalysisAspect;
import com.braintribe.devrock.zarathud.model.context.CoreProcessingAspect;
import com.braintribe.devrock.zarathud.model.context.RatingAspect;
import com.braintribe.model.artifact.compiled.CompiledArtifact;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


/**
 * a context as used by the ant integration..
 * @author pit
 *
 */
public interface JarProcessingRunnerContext extends CoreAnalysisAspect, CoreProcessingAspect, RatingAspect, ConfigurationAspect {
	
	EntityType<JarProcessingRunnerContext> T = EntityTypes.T(JarProcessingRunnerContext.class);
	String terminalJarName = "terminalJarName";
	String compiledSolutionsOfClasspath = "compiledSolutionsOfClasspath";

	String getTerminalJarName();
	void setTerminalJarName( String name);
	
	/**
	 * @return - a list of solutions, ie. as returned by the pom reader
	 */
	List<CompiledArtifact> getCompiledSolutionsOfClasspath();
	void setCompiledSolutionsOfClasspath( List<CompiledArtifact> solution);
	
	
}
