// ============================================================================
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
package com.braintribe.devrock.zarathud.model.response;

import com.braintribe.devrock.zarathud.model.request.BasicZedRequest;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.Resource;

/**
 * zed's response to an {@link BasicZedRequest} request
 * @author pit
 *
 */
public interface AnalyzedArtifact extends ZedResponse {
	
	
	final EntityType<AnalyzedArtifact> T = EntityTypes.T(AnalyzedArtifact.class);

	/**
	 * @return - a resource containing the fingerprints (expressive marshaller)
	 */
	Resource getFingerPrints();
	void setFingerPrints(Resource fingerPrints);
	
	/**
	 * @return - a resource containing the extraction (yaml)
	 */
	Resource getExtraction();
	void setExtraction(Resource extration);
	
	/**
	 * @return - a resource containing the dependency forensics result (yaml)
	 */
	Resource getDependencyForensics();
	void setDependencyForensics(Resource dependencyForensics);
	
	/**
	 * @return - a resource containing the classpath forensics (yaml)
 	 */
	Resource getClasspathForensics();
	void setClasspathForensics(Resource classpathForensics);
	
	/**
	 * @return - a resource containing the model forensics (yaml)
	 */
	Resource getModelForensics();
	void setModelForensics(Resource modelForensics);
}
