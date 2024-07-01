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
package com.braintribe.devrock.zarathud;

import java.io.File;

import org.junit.Assert;

import com.braintribe.common.lcd.Pair;
import com.braintribe.devrock.zarathud.model.context.ConsoleOutputVerbosity;
import com.braintribe.devrock.zarathud.model.request.AnalyzeArtifactRequest;
import com.braintribe.devrock.zarathud.model.response.AnalyzedArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.analysis.AnalysisTerminal;

public abstract class AbstractZedServiceProcessorTest implements HasCommonFilesystemNode {

	protected File input;
	protected File output;
	
	{	
		Pair<File,File> pair = filesystemRoots("standard");
		input = pair.first;
		output = pair.second;		
	}
	
	protected void test(AnalysisArtifactResolution resolution) {
		
		AnalysisTerminal analysisTerminal = resolution.getTerminals().get(0);
		
		AnalysisArtifact terminal = null;
		if (analysisTerminal instanceof AnalysisArtifact) {
			terminal = (AnalysisArtifact) analysisTerminal;
		}
		else if (analysisTerminal instanceof AnalysisDependency) { 
			AnalysisDependency ad = (AnalysisDependency) analysisTerminal;
			terminal = ad.getSolution();
		}
		else {
			Assert.fail("teminal can only be an AnalysisArtifact or an AnalysisDependency");
			return;
		}
		
		AnalyzeArtifactRequest request = AnalyzeArtifactRequest.T.create();
		request.setResolution(resolution);
		request.setTerminal(terminal);
		request.setOutputDir( output.getAbsolutePath());
		request.setWriteAnalysisData(true);
		request.setVerbosity(ConsoleOutputVerbosity.taciturn);
				
		ZedServiceProcessor processor = new ZedServiceProcessor();
		AnalyzedArtifact processed = processor.analyze(null, request);
	}
}
