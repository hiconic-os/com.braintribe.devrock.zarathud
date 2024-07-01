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
package com.braintribe.devrock.zarathud.test.extraction.comparison;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.braintribe.common.lcd.Pair;
import com.braintribe.devrock.zarathud.test.common.HasCommonFilesystemNode;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.zarathud.model.data.Artifact;

public class ComparisonDataProducer extends AbstractComparisonTest implements HasCommonFilesystemNode {
	private File input;
	private File output;

	{	
		Pair<File,File> pair = filesystemRoots("extraction");
		input = pair.first;
		output = pair.second;				
	}
	
	 
	private void extractIdentical(String terminal, String prefix) { 
		Maybe<Artifact> maybe = extract(terminal);
		if (maybe.isEmpty()) {
			Assert.fail( maybe.whyUnsatisfied().stringify());
		}

		Artifact artifact = maybe.value();
		
		save(artifact, new File( input, prefix + "-1.yaml"));
		save(artifact, new File( input, prefix + "-2.yaml"));
	}
	
	private void extract(String terminal, String prefix) { 
		Maybe<Artifact> maybe = extract(terminal);
		if (maybe.isEmpty()) {
			Assert.fail( maybe.whyUnsatisfied().stringify());
		}

		Artifact artifact = maybe.value();
		
		save(artifact, new File( input, prefix + ".yaml"));		
	}

	
	
	@Test
	public void produceIdenticalExtractionForAnalysisArtifactModel() {
		extractIdentical("com.braintribe.devrock:analysis-artifact-model#2.0.22", "analysis-artifact-model");
	}
	
	@Test
	public void produceExtractionForAnalysisArtifactModel_2_0_22() {
		extract("com.braintribe.devrock:analysis-artifact-model#2.0.22", "analysis-artifact-model-2.0.22");
	}
	
	@Test
	public void produceExtractionForAnalysisArtifactModel_2_0_18() {
		extract("com.braintribe.devrock:analysis-artifact-model#2.0.18", "analysis-artifact-model-2.0.18");
	}
	
	@Test
	public void produceExtractionForMcCore_2_0_55() {
		extract("com.braintribe.devrock:mc-core#2.0.55", "mc-core-2.0.55");
	}
	
	@Test
	public void produceExtractionForMcCore_2_0_54() {
		extract("com.braintribe.devrock:mc-core#2.0.54", "mc-core-2.0.54");
	}
	
	@Test
	public void produceExtractionForLibraryBase() {
		extract("com.braintribe.devrock.zarathud.test:library-base#1.0.1-pc", "library-base");
	}
	
	@Test
	public void produceExtractionForLibraryOther() {
		extract("com.braintribe.devrock.zarathud.test:library-other#1.0.1-pc", "library-other");
	}
	
}
