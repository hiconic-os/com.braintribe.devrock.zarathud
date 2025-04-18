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
package com.braintribe.zarathud.model.forensics;

import java.util.List;
import java.util.Map;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.zarathud.model.forensics.findings.ComparisonIssueType;

/**
 * a finger print as generated by the forensic experts for each issue
 * @author pit
 *
 */
/**
 * 
 * @author pit
 */
public interface FingerPrint extends GenericEntity {
	
	EntityType<FingerPrint> T = EntityTypes.T(FingerPrint.class);

	String slots = "slots";
	String issueData = "issueData";
	String comparisonIssueData = "comparisonIssueData";
	String comparisonIssue = "comparisonIssue";
	String entitySource = "entitySource";
	String entityComparisonTarget = "entityComparisonTarget";
	String origin = "origin";

	/**
	 * @return - a map of the slot keys and values
	 */
	Map<String,String> getSlots();
	void setSlots( Map<String,String> map);
	
	/**
	 * @return - additional string data attached to an issue 
	 */
	List<String> getIssueData();
	void setIssueData( List<String> data);
	
	
	/**
	 * @return - additional data added by the comparator (mainly ZedEntities, MethodEntity, FieldEntity et al)
	 */
	List<GenericEntity> getComparisonIssueData();
	void setComparisonIssueData(List<GenericEntity> value);
	
	/**
	 * @return - the actual issue the comparator found
	 */
	ComparisonIssueType getComparisonIssueType();
	void setComparisonIssueType(ComparisonIssueType value);


	/**
	 * @return - the actually entity that was the source/center of the issue
	 */
	GenericEntity getEntitySource();
	void setEntitySource( GenericEntity entity);
	
	/**
	 * @return - an optional entity that was involved (most comparison)
	 */
	GenericEntity getEntityComparisonTarget();
	void setEntityComparisonTarget( GenericEntity entity);
	

	/**
	 * @return - the {@link FingerPrintOrigin}, i.e. where the {@link FingerPrint} has been created (extracted or injected)
	 */
	FingerPrintOrigin getOrigin();
	void setOrigin(FingerPrintOrigin value);

	
}
