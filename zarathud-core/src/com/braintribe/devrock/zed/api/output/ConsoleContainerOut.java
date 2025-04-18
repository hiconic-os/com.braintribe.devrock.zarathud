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
package com.braintribe.devrock.zed.api.output;

import java.util.List;

import com.braintribe.console.output.ConfigurableConsoleOutputContainer;
import com.braintribe.console.output.ConsoleOutputContainer;
import com.braintribe.devrock.zarathud.model.reasons.UrlNotFound;
import com.braintribe.devrock.zed.api.context.ConsoleOutputContext;
import com.braintribe.devrock.zed.context.BasicConsoleOutputContext;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.zarathud.model.data.Artifact;
import com.braintribe.zarathud.model.forensics.ArtifactForensicsResult;
import com.braintribe.zarathud.model.forensics.ClasspathForensicsResult;
import com.braintribe.zarathud.model.forensics.DependencyForensicsResult;
import com.braintribe.zarathud.model.forensics.ModelDeclarationForensicsResult;
import com.braintribe.zarathud.model.forensics.ModelForensicsResult;
import com.braintribe.zarathud.model.forensics.ModuleForensicsResult;

/**
 * Zed's output features 
 * @author pit
 *
 */
public interface ConsoleContainerOut {
	/**
	 * create output for the terminal {@link Artifact}
	 * @param context - the {@link ConsoleOutputContext}
	 * @param artifact - the analyzed terminal {@link Artifact} 
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processTerminal( ConsoleOutputContext context, Artifact artifact);	
	
	/**
	 * create output for the {@link DependencyForensicsResult}
	 * @param context - the {@link ConsoleOutputContext}
	 * @param dependencyForensicsResult - the {@link DependencyForensicsResult} to process
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processDependencyForensics( ConsoleOutputContext context, DependencyForensicsResult dependencyForensicsResult);
	/**
	 * create output for a {@link ArtifactForensicsResult}
	 * @param context - the {@link ConsoleOutputContext}
	 * @param artifactForensicsResult - the {@link ArtifactForensicsResult} to process
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processArtifactForensicsResult( ConsoleOutputContext context, ArtifactForensicsResult artifactForensicsResult);
	
	/**
	 * create output for the {@link ClasspathForensicsResult}
	 * @param context - the {@link ConsoleOutputContext}
	 * @param classpathForensicsResult
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processClasspathForensicsResult( ConsoleOutputContext context, ClasspathForensicsResult classpathForensicsResult);
	
	/**
	 * create output for the {@link ModelForensicsResult}
	 * @param context - the {@link ConsoleOutputContext}
	 * @param modelForensicsResult - {@link ModelForensicsResult}
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processModelForensicsResult( ConsoleOutputContext context, ModelForensicsResult modelForensicsResult);
	
	/**
	 * create output for the {@link ModelDeclarationForensicsResult}
	 * @param context - the {@link ConsoleOutputContext}
	 * @param modelDeclarationResult - {@link ModelDeclarationForensicsResult}
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processModelDeclarationResult( ConsoleOutputContext context, ModelDeclarationForensicsResult modelDeclarationResult);

	/**
	 * create output for the {@link ModuleForensicsResult}
	 * @param consoleOutputContext - the {@link BasicConsoleOutputContext}
	 * @param moduleForensicsResult - the {@link ModuleForensicsResult} to output
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processModuleForensicsResult(ConsoleOutputContext consoleOutputContext, ModuleForensicsResult moduleForensicsResult);
	
	/**
	 * processes the collected scan errors - simply {@link UrlNotFound} reasons
	 * @param reasons - {@link List} of {@link UrlNotFound} reasons
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processScanErrorReasons( ConsoleOutputContext context, List<Reason> reasons);
	
	/**
	 * processes reasons (other than the scan reasons)
	 * @param reason - a {@link List} of other reasons
	 * @return - a {@link ConfigurableConsoleOutputContainer}
	 */
	ConsoleOutputContainer processReasons( ConsoleOutputContext context, List<Reason> reason);
	
	
	
}
