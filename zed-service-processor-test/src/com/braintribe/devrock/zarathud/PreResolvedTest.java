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
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.devrock.zarathud.model.context.ConsoleOutputVerbosity;
import com.braintribe.devrock.zarathud.model.request.AnalyzeArtifactRequest;
import com.braintribe.devrock.zarathud.model.response.AnalyzedArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.analysis.AnalysisTerminal;
import com.braintribe.testing.category.KnownIssue;

/**
 * can only run on the machine where the files were made - NOT IN THE CI
 */
@Category(KnownIssue.class)
public class PreResolvedTest extends AbstractZedServiceProcessorTest {
	
	private static final String resolutionFile_mc_core_jars = "com.braintribe.devrock.mc-core-2.0.81-rc.yaml";
	private static final String resolutionFile_mc_core_folders = "com.braintribe.devrock.mc-core-2.0.81-rc.folders.yaml";
	
	private static final String resolutionFile_swiss_re_jars = "swissre.claims.swissre-claims-api-model-2.3.83-rc.yaml";
	private static final String resolutionFile_swiss_re_folders = "swissre.claims.swissre-claims-api-model-2.3.83-rc.folders.yaml";
	
	private AnalysisArtifactResolution resolution;
	private AnalysisArtifact terminal;		
	private YamlMarshaller marshaller = new YamlMarshaller();

	@Test
	public void test()  {
	
		try (InputStream in = new FileInputStream( new File( input, resolutionFile_swiss_re_jars))) {
			resolution = (AnalysisArtifactResolution) marshaller.unmarshall(in);
		}
		catch (Exception e) {
			Assert.fail("cannot read stored resolution as " + e.getMessage());
		}
		
		AnalysisTerminal analysisTerminal = resolution.getTerminals().get(0);
		
		if (analysisTerminal instanceof AnalysisArtifact) {
			terminal = (AnalysisArtifact) analysisTerminal;
		}
		else if (analysisTerminal instanceof AnalysisDependency) { 
			AnalysisDependency ad = (AnalysisDependency) analysisTerminal;
			terminal = ad.getSolution();
		}
		
		AnalyzeArtifactRequest request = AnalyzeArtifactRequest.T.create();
		request.setResolution(resolution);
		request.setTerminal(terminal);
		request.setOutputDir( output.getAbsolutePath());
		request.setWriteAnalysisData(true);
		request.setVerbosity(ConsoleOutputVerbosity.taciturn);
				
		ZedServiceProcessor processor = new ZedServiceProcessor();
		AnalyzedArtifact process = processor.analyze(null, request);
	}
	
}
