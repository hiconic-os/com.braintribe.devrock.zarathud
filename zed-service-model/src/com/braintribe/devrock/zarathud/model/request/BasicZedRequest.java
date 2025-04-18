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

import com.braintribe.devrock.zarathud.model.context.ConsoleOutputVerbosity;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Alias;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * the request to analyze an artifact (based on ZedRunner's resolution feature)
 * @author pit
 *
 */
@Abstract
public interface BasicZedRequest extends ZedRequest {
	
	final EntityType<BasicZedRequest> T = EntityTypes.T(BasicZedRequest.class);

	@Description("the qualified name of the artifact to analyze")
	@Alias( "t")
	@Mandatory
	String getTerminalName();
	void setTerminalName(String terminal);
	
	@Description("the output directory where to write the result to")
	@Alias( "o")
	String getOutputDir();
	void setOutputDir(String  output);

	@Description("whether to additionally write all collected data. Default : false")
	@Alias( "w")
	boolean getWriteAnalysisData();
	void setWriteAnalysisData(boolean writeAnalysisData);
	
	@Description("the verbosity : taciturn, terse, verbose, garrulous. Default : verbose")
	@Alias( "v")
	ConsoleOutputVerbosity getVerbosity();
	void setVerbosity(ConsoleOutputVerbosity  verbosity);
}
