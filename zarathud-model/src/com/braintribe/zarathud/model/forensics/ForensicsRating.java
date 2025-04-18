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

import java.util.Collection;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

public enum ForensicsRating implements EnumBase {
	IGNORE,OK,INFO,WARN,ERROR,FATAL;
	
	public static boolean isEqualOrAbove( ForensicsRating first, ForensicsRating second) {
		return first.ordinal() >= second.ordinal();
	}
	
	public static ForensicsRating setIfAbove( ForensicsRating currentRating, ForensicsRating newRating) {
		if (currentRating.ordinal() >= newRating.ordinal())
			return currentRating;
		return newRating;							
	}
	
	public static ForensicsRating getWorstRating( Collection<ForensicsRating> ratings) {
		ForensicsRating initial = ForensicsRating.INFO;
		for (ForensicsRating rating : ratings) {
			if (ForensicsRating.isEqualOrAbove(rating, initial)) {
				initial = rating;
			}
		}
		return initial;
	}
	

	public static EnumType T = EnumTypes.T(ForensicsRating.class);

	@Override
	public EnumType type() {
		return T;
	}
	
	
}
