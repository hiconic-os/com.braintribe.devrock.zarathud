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
package com.braintribe.devrock.zed.core.comparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.braintribe.devrock.zed.api.comparison.ComparisonContext;
import com.braintribe.devrock.zed.api.comparison.ZedComparison;
import com.braintribe.devrock.zed.forensics.fingerprint.FingerPrintExpert;
import com.braintribe.zarathud.model.data.AnnotationEntity;
import com.braintribe.zarathud.model.data.Artifact;
import com.braintribe.zarathud.model.data.ClassOrInterfaceEntity;
import com.braintribe.zarathud.model.data.ZedEntity;
import com.braintribe.zarathud.model.data.natures.HasAccessModifierNature;
import com.braintribe.zarathud.model.forensics.FingerPrint;
import com.braintribe.zarathud.model.forensics.findings.ComparisonIssueType;
import com.braintribe.zarathud.model.forensics.findings.ComparisonProcessFocus;

/**
 * basic implementation 
 * 
 * @author pit
 */
public class BasicComparator implements ZedComparison {
	
	private Map<String, ZedEntity> baseEntities = new HashMap<>();
	private Map<String, ZedEntity> otherEntities = new HashMap<>();
	
	
	ComparisonContext context = new BasicComparisonContext();

	
	@Override
	public ComparisonContext getComparisonContext() {
		return context;
	}

	@Override
	public boolean compare(Artifact baseArtifact, Artifact otherArtifact) {
		// setup comparison context 		
		String baseArtifactName = baseArtifact.getGroupId() + ":" + baseArtifact.getArtifactId() + "#" + baseArtifact.getVersion();
		String otherArtifactName = otherArtifact.getGroupId() + ":" + otherArtifact.getArtifactId() + "#" + otherArtifact.getVersion();
		
		List<ZedEntity> bases = baseArtifact.getEntries().stream()
													.filter( e -> testForRelevance(e))													
													.collect(  Collectors.toList());
		bases.stream().forEach( z -> baseEntities.put( z.getName(), z));
		
		
		List<ZedEntity> others = otherArtifact.getEntries().stream()
													.filter( e -> testForRelevance(e))
													.collect(  Collectors.toList());		
		others.stream().forEach( z -> otherEntities.put( z.getName(), z));
		
		List<String> matched = new ArrayList<>();
		
		context.pushCurrentProcessFocus( ComparisonProcessFocus.artifact);
		for (ZedEntity base : bases) {
			context.pushCurrentEntity(base);
			
			String name = base.getName();
			ZedEntity other = otherEntities.get(name);
			if (other == null) {
				FingerPrint fp = FingerPrintExpert.build(base, ComparisonIssueType.noContentToCompare);
				fp.setEntitySource(base);
				context.addFingerPrint(fp);
			}
			else {
				context.pushCurrentOther(other);
				CommonStatelessComparators.compareEntities(context, base, other);
				context.popCurrentOther();
				matched.add( other.getName());
			}			
			context.popCurrentEntity();
		}
		
		List<String> newOthers = new ArrayList<>( otherEntities.keySet());
		newOthers.removeAll(matched);
		
		if (newOthers.size() > 0) {
			// add the additions ..
			for (String newOther : newOthers) {
				ZedEntity additional = otherEntities.get(newOther);
				FingerPrint fp = FingerPrintExpert.build(additional, ComparisonIssueType.additionalContent);
				fp.setEntityComparisonTarget(additional);
				context.addFingerPrint(fp);				
			}
		}
		
		
		context.popCurrentProcessFocus();
		boolean empty = context.getFingerPrints().isEmpty();
		if (empty) {
			System.out.println("Comparison of [" + baseArtifactName + "] vs [" + otherArtifactName + "] has shown no discrepancies in contract");
		}
		else {
			System.out.println("Comparison of [" + baseArtifactName + "] vs [" + otherArtifactName + "] has shown discrepancies in contract");
			// show finger prints?
		}
		return empty; // return true if no fingerprints were added, false if there are any  
	}
	

	/**
	 * filter the entities to compare
	 * must be defined in the terminal
	 * must be either interface,class,enum or annotation (well that's probably all that exist on the entity level)
	 * must be public / protected
	 * @param e
	 * @return
	 */
	private boolean testForRelevance( ZedEntity e) {
		if (e.getDefinedInTerminal() == false) {
			return false;
		}
		
		if (e instanceof ClassOrInterfaceEntity == false &&  e instanceof AnnotationEntity) {
			return false;
		}
		
		if (e instanceof HasAccessModifierNature) {
			HasAccessModifierNature hamn = (HasAccessModifierNature) e;
			switch (hamn.getAccessModifier()) {
				case PROTECTED:
				case PUBLIC:
					return true;
				default:
				case PACKAGE_PRIVATE:
				case PRIVATE:
					return false;								
			}
		}
		return  true;
	}
	
	
}
