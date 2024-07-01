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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.braintribe.codec.marshaller.yaml.YamlMarshaller;
import com.braintribe.common.lcd.Pair;
import com.braintribe.devrock.mc.api.commons.PartIdentifications;
import com.braintribe.devrock.zarathud.model.ClassesProcessingRunnerContext;
import com.braintribe.devrock.zarathud.model.context.ConsoleOutputVerbosity;
import com.braintribe.devrock.zarathud.model.request.AnalyzeArtifactRequest;
import com.braintribe.devrock.zarathud.model.request.BasicZedRequest;
import com.braintribe.devrock.zarathud.model.request.ZedRequest;
import com.braintribe.devrock.zarathud.model.response.AnalyzedArtifact;
import com.braintribe.devrock.zarathud.runner.api.ZedWireRunner;
import com.braintribe.devrock.zarathud.runner.wire.ZedRunnerWireTerminalModule;
import com.braintribe.devrock.zarathud.runner.wire.contract.ZedRunnerContract;
import com.braintribe.devrock.zed.forensics.fingerprint.persistence.FingerPrintMarshaller;
import com.braintribe.devrock.zed.forensics.fingerprint.persistence.FingerprintOverrideContainer;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.logging.Logger;
import com.braintribe.model.artifact.analysis.AnalysisArtifact;
import com.braintribe.model.artifact.analysis.AnalysisDependency;
import com.braintribe.model.artifact.consumable.Part;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.impl.AbstractDispatchingServiceProcessor;
import com.braintribe.model.processing.service.impl.DispatchConfiguration;
import com.braintribe.model.resource.FileResource;
import com.braintribe.model.resource.Resource;
import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.zarathud.model.data.Artifact;
import com.braintribe.zarathud.model.forensics.ClasspathForensicsResult;
import com.braintribe.zarathud.model.forensics.DependencyForensicsResult;
import com.braintribe.zarathud.model.forensics.FingerPrint;
import com.braintribe.zarathud.model.forensics.ForensicsRating;
import com.braintribe.zarathud.model.forensics.ModelForensicsResult;
import com.braintribe.zarathud.model.forensics.ModuleForensicsResult;

import jsinterop.utils.Collections;

/**
 * jinni wrapper for zed
 * @author pit
 *
 */
public class ZedServiceProcessor  extends AbstractDispatchingServiceProcessor<ZedRequest, Object> {
	private static Logger log = Logger.getLogger(ZedServiceProcessor.class);
	private FingerPrintMarshaller fingerPrintMarshaller = new FingerPrintMarshaller();
	private YamlMarshaller marshaller;
	{
		marshaller = new YamlMarshaller();
		marshaller.setWritePooled(true);
	}
	
	
	@Override
	protected void configureDispatching(DispatchConfiguration<ZedRequest, Object> dispatching) {
		dispatching.register( AnalyzeArtifactRequest.T, this::analyze);
	}
	
	private String extractTerminalCodeLocation(AnalysisArtifact aa) {
		Part part = aa.getParts().get( PartIdentifications.jar.asString());
		Resource resource = part.getResource();
		if (resource instanceof FileResource) {
			FileResource fr = (FileResource) resource;
			File f = new File(fr.getPath());
			if (f.isDirectory()) {
				return f.getAbsolutePath();				 
			}
		}		
		return null;
	}
	
	private Map<String, AnalysisArtifact> extractCodeLocation(List<AnalysisArtifact> aas) {
		Map<String, AnalysisArtifact> result = new HashMap<>();
		for (AnalysisArtifact a : aas) {
			String folder = extractTerminalCodeLocation(a);
			if (folder != null) {
				result.put(folder, a);
			}
		}
		return result;
	}
	
	
	/**
	 * run the analysis 
	 * @param context - the {@link ServiceRequestContext}
	 * @param request - the {@link BasicZedRequest} request
	 * @return - the resulting {@link AnalyzedArtifact}
	 */
	public AnalyzedArtifact analyze( ServiceRequestContext context, AnalyzeArtifactRequest request) {
		WireContext<ZedRunnerContract> wireContext = Wire.context( ZedRunnerWireTerminalModule.INSTANCE);
		String terminalName = request.getTerminal().asString();
		
		 
		
		ClassesProcessingRunnerContext cprC = ClassesProcessingRunnerContext.T.create();
		cprC.setTerminal( terminalName);

		AnalysisArtifact terminal = request.getTerminal();
		List<AnalysisArtifact> solutions = request.getResolution().getSolutions();
		
		// check the terminal, whether it's 'jar-based' or 'directory-based'
		String terminalClassfolder = extractTerminalCodeLocation( terminal);
		if (terminalClassfolder != null) {
			cprC.setTerminalClassesDirectoryNames( Collections.list( terminalClassfolder));
			// remove it from solutions?
			solutions.remove( request.getTerminal());
		}
		
		// check all solutions whether they are 'jar-based' or 'directory-based'
		Map<String, AnalysisArtifact> solutionClassfolders = extractCodeLocation( solutions);
		if (!solutionClassfolders.isEmpty()) {
			cprC.setNonpackedSolutionsOfClasspath( solutionClassfolders);
			// remove from solutions?
			solutionClassfolders.values().stream().forEach( aa -> solutions.remove(aa));							
		}
		
		cprC.setClasspath( solutions);
		cprC.setCompiledSolutionsOfClasspath( solutions);
		
		List<AnalysisDependency> terminalDependencies = terminal.getOrigin().getDependencies().stream().map( cd -> AnalysisDependency.from(cd)).collect(Collectors.toList());
		cprC.setDependencies(terminalDependencies);
		
		ConsoleOutputVerbosity consoleOutputVerbosity = request.getVerbosity();
		if (consoleOutputVerbosity == null) {
			consoleOutputVerbosity = ConsoleOutputVerbosity.verbose;
		}
		cprC.setConsoleOutputVerbosity( consoleOutputVerbosity);
		
		// injected ratings override 
		if (request.getCustomRatingsResource() != null) {		
			cprC.setCustomRatingsResource( request.getCustomRatingsResource());
		}
		// part of the terminal artifact, toplevel override
		if (request.getPullRequestResource() != null) {
			cprC.setPullRequestRatingsResource( request.getPullRequestResource());
		}
		
		ZedWireRunner zedWireRunner = wireContext.contract().classesRunner( cprC);
		
		Maybe<Pair<ForensicsRating,Map<FingerPrint,ForensicsRating>>> resultsMaybe = zedWireRunner.run();
		
		Pair<ForensicsRating,Map<FingerPrint,ForensicsRating>> results;
		if (resultsMaybe.hasValue()) {
			Reason whyUnsatisfied = resultsMaybe.whyUnsatisfied();
			results = resultsMaybe.value();
			log.warn( whyUnsatisfied.stringify());
		}
		else {
			results = resultsMaybe.get();
		}
				 		
		AnalyzedArtifact response = AnalyzedArtifact.T.create();
		if (results.first == null) {
			log.warn("no data available for [" + request.getTerminal() + "]");
			return response;
		}
		
		String outputDirectory = request.getOutputDir();
		if (outputDirectory == null) {
			outputDirectory = ".";
		}
		
		File output = new File( outputDirectory);
		output.mkdirs();
		
		File fingerprintsFile = writeFingerPrints( output, terminalName, results.second);
		response.setFingerPrints( toResource(fingerprintsFile));
		
		if (request.getWriteAnalysisData()) {
			// write extraction & forensics & add at 
			Artifact analyzedArtifact = zedWireRunner.analyzedArtifact();
			File exFile = writeYaml(output, terminalName, "extraction", analyzedArtifact);
			response.setDependencyForensics( toResource( exFile));
						
			DependencyForensicsResult dependencyForensicsResult = zedWireRunner.dependencyForensicsResult();
			File depFile = writeYaml(output, terminalName, "dependency", dependencyForensicsResult);
			response.setDependencyForensics( toResource(depFile));
			
			ClasspathForensicsResult classpathForensicsResult = zedWireRunner.classpathForensicsResult();
			File cpFile = writeYaml(output, terminalName, "classpath", classpathForensicsResult);
			response.setClasspathForensics( toResource(cpFile));
			
			ModelForensicsResult modelForensicsResult = zedWireRunner.modelForensicsResult();
			File mdlFile = writeYaml(output, terminalName, "model", modelForensicsResult);
			response.setModelForensics( toResource(mdlFile));
			
			ModuleForensicsResult moduleForensicsResult = zedWireRunner.moduleForensicsResult();
			File moduleFile = writeYaml(output, terminalName, "module", moduleForensicsResult);
			response.setModelForensics( toResource(moduleFile));
			
		}
		
		return response;
	}
	
	/**
	 * build a resource from the file 
	 * @param file - {@link File} to wrap
	 * @return - resulting {@link Resource}
	 */
	private Resource toResource( File file) {
		Resource resource = Resource.createTransient(() -> new FileInputStream( file));
		return resource;
	}
	
	/**
	 * dumps fingerprints 
	 * @param output - {@link File} pointing to the directory
	 * @param terminalName - the name of the terminal 
	 * @param fingerPrints - the fingerprint data to store 
	 * @return - the {@link File} written
	 */
	private File writeFingerPrints( File output, String terminalName, Map<FingerPrint,ForensicsRating> fingerPrints) {
		FingerprintOverrideContainer fpovrc = new FingerprintOverrideContainer();
		fpovrc.setFingerprintOverrideRatings(fingerPrints);		
		terminalName = terminalName.replace( ':', '.');
		String name = terminalName + ".fpr.txt";
		File target = new File( output, name);
		try (OutputStream out = new FileOutputStream( target)) {
			fingerPrintMarshaller.marshall(out, fpovrc);
			return target;
		}
		catch (Exception e) {
			throw new IllegalStateException("cannot write ", e);
		}
	}
	
	/**
	 * dump data 
	 * @param output - {@link File} pointing to the directory 
	 * @param terminalName - the name of the terminal
	 * @param code - the code (actually a prefix to the suffix of the file)
	 * @param payload - the payload as {@link GenericEntity}
	 * @return - the {@link File} written 
	 */
	private File writeYaml( File output, String terminalName, String code, GenericEntity payload) {
		terminalName = terminalName.replace( ':', '.');
		String name = terminalName + "." + code + ".yaml";
		File target = new File( output, name);
		try (OutputStream out = new FileOutputStream( target)) {
			marshaller.marshall(out, payload);
			return target;
		}
		catch (Exception e) {
			throw new IllegalStateException("boink", e);
		}
		
	}
}
