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
package com.braintribe.devrock.zed.forensics.fingerprint.register;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.braintribe.common.lcd.Pair;
import com.braintribe.devrock.zed.forensics.fingerprint.FingerPrintCommons;
import com.braintribe.devrock.zed.forensics.fingerprint.FingerPrintExpert;
import com.braintribe.devrock.zed.forensics.fingerprint.HasFingerPrintTokens;
import com.braintribe.devrock.zed.forensics.fingerprint.filter.FingerPrintFilter;
import com.braintribe.zarathud.model.forensics.FingerPrint;
import com.braintribe.zarathud.model.forensics.FingerPrintOrigin;
import com.braintribe.zarathud.model.forensics.ForensicsRating;
import com.braintribe.zarathud.model.forensics.findings.ClasspathForensicIssueType;
import com.braintribe.zarathud.model.forensics.findings.DependencyForensicIssueTypes;
import com.braintribe.zarathud.model.forensics.findings.IssueType;
import com.braintribe.zarathud.model.forensics.findings.ModelDeclarationForensicIssueType;
import com.braintribe.zarathud.model.forensics.findings.ModelForensicIssueType;

/**
 * the configurable rating 
 * 
 * @author pit
 *
 */
public class RatingRegistry implements FingerPrintCommons, HasFingerPrintTokens {
	
	private static ForensicsRating minimalRating = ForensicsRating.IGNORE;
	
	private Map<String, IssueType> parserMap = new HashMap<>();	
	
	private List<Class<? extends IssueType>> issueCategories = new ArrayList<>();
	 	
	private Map<FingerPrint, ForensicsRating> ratingsMap = new LinkedHashMap<>();
	
	private Map<String, String> tipMap = new HashMap<>();
	
	{
		// currently known categories 
		issueCategories.add( DependencyForensicIssueTypes.class);
		issueCategories.add( ClasspathForensicIssueType.class);
		issueCategories.add( ModelForensicIssueType.class);
		
		// parser map creation.. 
		issueCategories.stream().flatMap( c -> Stream.of( c.getEnumConstants())).forEach( e -> parserMap.put( e.name(), (IssueType) e));

		//
		//
		// initial Rating for the ForensicFindingsCode
		//
		//
		
		//
		// dependencies
		//
		ratingsMap.put( FingerPrintExpert.build(DependencyForensicIssueTypes.MissingDependencyDeclarations), ForensicsRating.WARN);
		tipMap.put( DependencyForensicIssueTypes.MissingDependencyDeclarations.name(), "An artifact is referenced by the terminal but hasn't been declared as direct dependency");
		
		ratingsMap.put( FingerPrintExpert.build(DependencyForensicIssueTypes.ExcessDependencyDeclarations), ForensicsRating.INFO);
		tipMap.put( DependencyForensicIssueTypes.ExcessDependencyDeclarations.name(), "An artifact has been declared as direct dependency but is never referenced by the terminal");
		
		ratingsMap.put( FingerPrintExpert.build(DependencyForensicIssueTypes.ForwardDeclarations), ForensicsRating.OK);
		tipMap.put( DependencyForensicIssueTypes.ForwardDeclarations.name(), "A type the terminal references is semantically assigned to a different dependency than the artifact that declares it");
		
		ratingsMap.put( FingerPrintExpert.build(DependencyForensicIssueTypes.MissingForwardDependencyDeclarations), ForensicsRating.WARN);
		tipMap.put( DependencyForensicIssueTypes.MissingForwardDependencyDeclarations.name(), "A type the terminal references is semantically assigned to a different dependency than the artifact that declares it, but this dependency doesn't exist");
			
		//
		// classpath
		//
		ratingsMap.put( FingerPrintExpert.build( ClasspathForensicIssueType.ShadowingClassesInClasspath), ForensicsRating.WARN);
		tipMap.put( ClasspathForensicIssueType.ShadowingClassesInClasspath.name(), "There are several different classes with the same package- and class-name within the classpath");
		
		//
		// model definition (aka java declarations)
		//
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.MissingGetter), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.MissingGetter.name(), "The model property has no declared getter function");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.MissingSetter), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.MissingSetter.name(), "The model property has no declared setter function");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.TypeMismatch), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.TypeMismatch.name(), "Getter and setter functions have mismatching types");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.InvalidTypes), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.InvalidTypes.name(), "An invalid type is used as a property");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.CollectionInCollection), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.CollectionInCollection.name(), "A collection-type was used within a collection-type of a property");

		// overall
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.NonConformMethods), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.NonConformMethods.name(), "An invalid method declaration was used, either shadowing a getter/setter pair or otherwise invalid");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.ConformMethods), ForensicsRating.INFO);
		tipMap.put( ModelForensicIssueType.ConformMethods.name(), "While not a getter/setter function, the function itself is valid, but may not be available in some forms of the model");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.NonCanonic), ForensicsRating.INFO);
		tipMap.put( ModelForensicIssueType.NonCanonic.name(), "The model itself is valid, but its full content isnt' isomorph, i.e. some content will be lost on model-projections");

		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.UnexpectedField), ForensicsRating.INFO);
		tipMap.put( ModelForensicIssueType.UnexpectedField.name(), "The model itself is valid, but contains a field that is neither required nor accounted for by the model structure");
		
		// property literal
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.PropertyNameLiteralMissing), ForensicsRating.INFO);
		tipMap.put( ModelForensicIssueType.PropertyNameLiteralMissing.name(), "The property has no name field declared");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.PropertyNameLiteralTypeMismatch), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.PropertyNameLiteralTypeMismatch.name(), "The name field declared for the property is not the correct type (string)");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.PropertyNameLiteralMismatch), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.PropertyNameLiteralMismatch.name(), "The value of the name field declared for the property doesn't correspond with the name of the property");
	
		//
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.ContainsNoGenericEntities), ForensicsRating.INFO);
		tipMap.put( ModelForensicIssueType.ContainsNoGenericEntities.name(), "The artifact is a model yet it contains no generic entities");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.InvalidEntityTypeDeclaration), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.InvalidEntityTypeDeclaration.name(), "The entity's T field is not valid (wrong types, wrong call, wrong parameter)");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.MissingEntityTypeDeclaration), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.MissingEntityTypeDeclaration.name(), "The entity doesn't contain a T field");
		
		// enums in model
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.EnumTypeNoEnumbaseDerivation), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.EnumTypeNoEnumbaseDerivation.name(), "The enum entity doesn't derive from com.braintribe.model.generic.base.EnumBase");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.EnumTypeNoTField), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.EnumTypeNoTField.name(), "The enum entity doesn't contain a T field");
		
		ratingsMap.put( FingerPrintExpert.build( ModelForensicIssueType.EnumTypeNoTypeFunction), ForensicsRating.ERROR);
		tipMap.put( ModelForensicIssueType.EnumTypeNoTypeFunction.name(), "The enum entity doesn't declare the type() function");
		
		// model declaration (aka model-declaration.xml)
		ratingsMap.put( FingerPrintExpert.build( ModelDeclarationForensicIssueType.MissingDeclarationFile), ForensicsRating.ERROR);
		tipMap.put( ModelDeclarationForensicIssueType.MissingDeclarationFile.name(), "The model doesn't contain the required embedded 'model-declaration.xml' file");
		
		ratingsMap.put( FingerPrintExpert.build( ModelDeclarationForensicIssueType.MissingTypeDeclarations), ForensicsRating.INFO);
		tipMap.put( ModelDeclarationForensicIssueType.MissingTypeDeclarations.name(), "The embedded 'model-declaration.xml' file doesn't list all entities of the model");
		
		ratingsMap.put( FingerPrintExpert.build( ModelDeclarationForensicIssueType.ExcessTypeDeclarations), ForensicsRating.ERROR);
		tipMap.put( ModelDeclarationForensicIssueType.ExcessTypeDeclarations.name(), "The embedded 'model-declaration.xml' file list entities that do not exist within the model");
		
		ratingsMap.put( FingerPrintExpert.build( ModelDeclarationForensicIssueType.DeclarationFileInvalid), ForensicsRating.ERROR);
		tipMap.put( ModelDeclarationForensicIssueType.DeclarationFileInvalid.name(), "The embedded 'model-declaration.xml' file cannot be read");
		
		ratingsMap.put( FingerPrintExpert.build( ModelDeclarationForensicIssueType.MissingDeclaredDependencyDeclarations), ForensicsRating.ERROR);
		tipMap.put( ModelDeclarationForensicIssueType.MissingDeclaredDependencyDeclarations.name(), "The embedded 'model-declaration.xml' file doesn't list all depedencies required");
		
		ratingsMap.put( FingerPrintExpert.build( ModelDeclarationForensicIssueType.ExcessDeclaredDependencyDeclarations), ForensicsRating.WARN);
		tipMap.put( ModelDeclarationForensicIssueType.ExcessDeclaredDependencyDeclarations.name(), "The embedded 'model-declaration.xml' file list more than the required depedencies");
	}
	
	/**
	 * @param ratings - attaches further finger print (key role) and ratings
	 */
	public void addRatingOverrides( Map<FingerPrint, ForensicsRating> ratings, FingerPrintOrigin origin) {
		if (ratings == null)
			return;
		ratings.keySet().stream().forEach( f -> f.setOrigin( origin));
		ratingsMap.putAll( ratings);
	}
	
	/**
	 * @return - the currently active ratings 
	 */
	public Map<FingerPrint, ForensicsRating> getCurrentRatings() {
		return ratingsMap;
	}
	

	/**
	 * returns the assigned (tool) tip of a FingerPrint's issue 
	 * @param fp - the {@link FingerPrint}
	 * @return - the text assigned
	 */
	public String getTip(FingerPrint fp) {
		String issue = fp.getSlots().get(ISSUE);
		return tipMap.get(issue);
	}
	
	/**
	 * returns the assigned (tool) tip of a FingerPrint's issue 
	 * @param fp - the {@link FingerPrint}
	 * @return - the text assigned
	 */
	public String getTip(String issue) {		
		return tipMap.get(issue);
	}
	
	
	
	
	/**
	 * find a matching rating : it's the last entry of the matching finger print with the most number of slots 
	 * @param filter - a {@link Predicate}, mostly {@link FingerPrintFilter}
	 * @return - {@link Pair} of the found {@link FingerPrint} and its associated {@link ForensicsRating}
	 */
	public static Pair<FingerPrint,ForensicsRating> getActiveRating( FingerPrint filter, Map<FingerPrint,ForensicsRating> map) {
		// find all matching
		List<Pair<FingerPrint, Double>> bestMatches = new ArrayList<>();
		
		
		int maxMatches = 0;
		int numSlots = filter.getSlots().size();
		
		for (FingerPrint fp : map.keySet()) {
		
			int numMatches = FingerPrintExpert.numMatches(fp, filter);
			if (numMatches <= 0) {
				continue;
			}
			double ratio = (double) numSlots / (double) fp.getSlots().size();
					
			if (numMatches > maxMatches) {
				maxMatches = numMatches;
				bestMatches.clear();
				bestMatches.add( Pair.of( fp, ratio));
			}
			else if (numMatches == maxMatches) {
				bestMatches.add( Pair.of( fp, ratio));
			}
		};
		
		if (bestMatches.size() == 0) 
			return null;
		
		bestMatches.sort( new Comparator<Pair<FingerPrint, Double>>() {
			@Override
			public int compare(Pair<FingerPrint, Double> o1, Pair<FingerPrint, Double> o2) {			
				return o1.second.compareTo(o2.second);						
			}			
		});
		
		FingerPrint fingerPrint = bestMatches.get( bestMatches.size() -1).first;
		return Pair.of( fingerPrint, map.get(fingerPrint));							
	}
	 
	
	/**
	 * find the active rating in the current setup 
	 * @param filter
	 * @return
	 */
	public Pair<FingerPrint,ForensicsRating> getActiveRating( FingerPrint filter) {
		return getActiveRating(filter, ratingsMap);
	}
	
	
	/**
	 * @param map - the map to extract the entry adhering to a category 
	 * @param issueTypeClass - the category of issue
	 * @return - a Map that only contains the ratings for issues of the given category 
	 */
	public Map<FingerPrint, ForensicsRating> extractRatingsOfCategory( Map<FingerPrint, ForensicsRating> map, Class<? extends IssueType> issueTypeClass) {
		Map<FingerPrint, ForensicsRating> result = new HashMap<>();
		for (Entry<FingerPrint, ForensicsRating> entry : map.entrySet()) {
			String issue = entry.getKey().getSlots().get( ISSUE);
			IssueType issueType = parserMap.get(issue);
			
			boolean isOfCategory = Stream.of( issueTypeClass.getEnumConstants()).filter( c -> {return c == issueType;}).findFirst().isPresent();
			
			if (isOfCategory) {
				result.put( entry.getKey(), entry.getValue());
			}			
		}
		return result;
	}
	
	
	
	/**
	 * @param prints
	 * @return
	 */
	public Collection<IssueType> getIssuesOfFingerPrints( Collection<FingerPrint> prints){
		Set<IssueType> result = new HashSet<>();
		prints.stream().forEach( fp -> {
			
			String issue = fp.getSlots().get( ISSUE);
			IssueType issueType = parserMap.get(issue);
			result.add( issueType);
		});
		return result;
	}
		
	/**
	 * @param prints
	 * @param issueTypeClass
	 * @return
	 */
	public Collection<FingerPrint> getFingerPrintsOfCategory(Collection<FingerPrint> prints, Class<? extends IssueType> issueTypeClass) {
		Set<FingerPrint> result = new HashSet<>();
		prints.stream().forEach( fp -> {
			
			String issue = fp.getSlots().get( ISSUE);
			IssueType issueType = parserMap.get(issue);
			
			boolean isOfCategory = Stream.of( issueTypeClass.getEnumConstants()).filter( c -> {return c == issueType;}).findFirst().isPresent();
			
			if (isOfCategory) {
				result.add( fp);
			}
		});
		return result;
	}
	
	/**
	 * get the worst rating amongst any categories 
	 * @param prints - a {@link Collection} of {@link FingerPrint} to rate
	 * @return - the worst rating 
	 */
	public ForensicsRating getWorstRatingOfFingerPrints(Collection<FingerPrint> prints) {
		return getWorstRatingOfFingerPrints(prints, ratingsMap);
	}
	
	public static ForensicsRating getWorstRatingOfFingerPrints(Collection<FingerPrint> prints, Map<FingerPrint,ForensicsRating> map) {
		ForensicsRating worstRating = minimalRating;
		for (FingerPrint fp : prints) {
			Pair<FingerPrint, ForensicsRating> fr = getActiveRating(fp, map);
			if (fr == null) {
				continue;
			}
			ForensicsRating rating = fr.second;
			if (rating.ordinal() > worstRating.ordinal()) {
				worstRating = rating;
			}
		}
		return worstRating;
	}
	
	public Map<ForensicsRating, List<FingerPrint>> rateFingerPrints( Collection<FingerPrint> prints) {
		return rateFingerPrints(prints, ratingsMap);
	}
	
	public static Map<ForensicsRating, List<FingerPrint>> rateFingerPrints( Collection<FingerPrint> prints, Map<FingerPrint,ForensicsRating> map) {
		Map<ForensicsRating, List<FingerPrint>> result = new HashMap<>();
		
		for (FingerPrint fp : prints) {
			Pair<FingerPrint, ForensicsRating> fr = getActiveRating(fp, map);
			if (fr == null) {
				continue;
			}
			ForensicsRating rating = fr.second;
			List<FingerPrint> printsOfRating = result.computeIfAbsent(rating, r -> new ArrayList<>());
			printsOfRating.add(fp);			
		} 		
		return result;
	}
	
	public ForensicsRating getWorstRatingOfFingerPrints(Map<ForensicsRating, List<FingerPrint>> ratedPrints) {
		ForensicsRating worstRating = minimalRating;
		for (ForensicsRating rating : ratedPrints.keySet()) {			
			if (rating.ordinal() > worstRating.ordinal()) {
				worstRating = rating;
			}
		}
		return worstRating;
	}
	
	public static List<FingerPrint> coalesce( List<FingerPrint> prints) {
		Set<String> issues = new HashSet<>();
		List<FingerPrint> coalesced = new ArrayList<>();
		for (FingerPrint fingerPrint : prints) {
			if (issues.add( fingerPrint.getSlots().get( ISSUE))) {
				coalesced.add(fingerPrint);
			}
		}
		return coalesced;
	}
	

	
	
	/**
	 * get the active rating for each print and put'em into a map
	 * @param fingerPrints - a {@link Collection} of {@link FingerPrint}
	 * @return - a {@link Map} correlating the {@link FingerPrint} to its current {@link RatingRegistry}
	 */
	public Map<FingerPrint, ForensicsRating> rateCurrentFingerPrints(Collection<FingerPrint> fingerPrints) {
		Map<FingerPrint, ForensicsRating> result = new HashMap<>();		
		for (FingerPrint lock : fingerPrints) {			
			Pair<FingerPrint, ForensicsRating> pair = getActiveRating(lock);
			if (pair == null)
				continue;
			ForensicsRating forensicsRating = pair.second;
			result.put( lock, forensicsRating);
		}
		return result;
	}
	

}
