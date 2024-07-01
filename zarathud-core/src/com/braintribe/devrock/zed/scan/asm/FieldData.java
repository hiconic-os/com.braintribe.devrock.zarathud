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
package com.braintribe.devrock.zed.scan.asm;

import java.util.List;

import com.braintribe.zarathud.model.data.ScopeModifier;

public class FieldData {

	private String name;
	private String signature;
	private String desc;
	private Object intializer;
	private AccessModifier accessModifier;
	private ScopeModifier scopeModifier;
	private List<AnnotationTuple> annotationTuples;
	private boolean staticNature;
	private boolean finalNature;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Object getIntializer() {
		return intializer;
	}
	public void setIntializer(Object intializer) {
		this.intializer = intializer;
	}
	public AccessModifier getAccessModifier() {
		return accessModifier;
	}
	public void setAccessModifier(AccessModifier accessModifier) {
		this.accessModifier = accessModifier;
	}
	 
	public ScopeModifier getScopeModifier() {
		return scopeModifier;
	}
	public void setScopeModifier(ScopeModifier scopeModifier) {
		this.scopeModifier = scopeModifier;
	}
	public List<AnnotationTuple> getAnnotationTuples() {
		return annotationTuples;
	}
	public void setAnnotationTuples(List<AnnotationTuple> annotationTuples) {
		this.annotationTuples = annotationTuples;
	}
	public boolean getIsStatic() {
		return staticNature;
	}
	public void setIsStatic(boolean staticNature) {
		this.staticNature = staticNature;
	}
	public boolean getisFinal() {
		return finalNature;
	}
	public void setIsFinal(boolean finalNature) {
		this.finalNature = finalNature;
	}
	
	
	
	
	
	
	
}
