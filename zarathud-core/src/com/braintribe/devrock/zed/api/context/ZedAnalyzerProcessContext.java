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
package com.braintribe.devrock.zed.api.context;

import java.net.URL;
import java.util.List;
import java.util.Map;

import com.braintribe.devrock.zed.api.core.ZedSignatureAnalyzer;
import com.braintribe.devrock.zed.scan.asm.AnnotationTuple;
import com.braintribe.devrock.zed.scan.asm.ClassData;
import com.braintribe.devrock.zed.scan.asm.InheritanceData;
import com.braintribe.devrock.zed.scan.asm.MethodData;
import com.braintribe.zarathud.model.data.TypeReferenceEntity;
import com.braintribe.zarathud.model.data.ZedEntity;


/**
 * the context used during analyzing the classes 
 * @author pit
 *
 */
public interface ZedAnalyzerProcessContext {
	ZedAnalyzerContext context();
	ZedEntity entity();
	ClassData classData();
	InheritanceData inheritanceData();	
	URL resourceURL();
	List<MethodData> methods();	
	ZedSignatureAnalyzer signatures();
	List<AnnotationTuple> annotations();
	
	/**
	 * @return - the template parameters of the current entity
	 */
	Map<String,TypeReferenceEntity> classTemplateTypes();
	
	/**	
	 * @return - the currently relevant template types  of the current method (see push/pop)
	 */
	Map<String, TypeReferenceEntity> templateTypes();
	/**
	 * @param references - push a new set of template types
	 */
	void pushTypeReferences( Map<String,TypeReferenceEntity> references);
	/**
	 * pop'm 
	 */
	void popTypeReferences();
	
	String selfReferenceTemplateKey();
	void pushSelfReferenceTemplateKey( String key);
	void popSelfReferenceTemplateKey();
}
