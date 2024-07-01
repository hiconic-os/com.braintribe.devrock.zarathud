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
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisArtifactResolution;
import com.braintribe.testing.category.KnownIssue;

/**
 * can only run on the machine where the files were made - NOT IN THE CI
 */
@Category(KnownIssue.class)
public class PreResolvedTest extends AbstractZedServiceProcessorTest {
	

	private static final String resolutionFile_mc_core_folders = "com.braintribe.devrock.mc-core-2.0.74.folders.yaml";
	
	private AnalysisArtifactResolution resolution;
	private AnalysisArtifact terminal;		
	private YamlMarshaller marshaller = new YamlMarshaller();

	@Test
	public void test()  {
	
		try (InputStream in = new FileInputStream( new File( input, resolutionFile_mc_core_folders))) {
			resolution = (AnalysisArtifactResolution) marshaller.unmarshall(in);
		}
		catch (Exception e) {
			Assert.fail("cannot read stored resolution as " + e.getMessage());
		}
		
		test( resolution);		
	}
	
}
