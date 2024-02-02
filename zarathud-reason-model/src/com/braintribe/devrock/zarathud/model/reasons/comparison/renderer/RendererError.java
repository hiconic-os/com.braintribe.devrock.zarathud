package com.braintribe.devrock.zarathud.model.reasons.comparison.renderer;

import com.braintribe.gm.model.reason.Reason;
import com.braintribe.model.generic.annotation.SelectiveInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@SelectiveInformation("cannot render report due to failure in render process : ${renderFailure}")
public interface RendererError extends Reason {
	
	EntityType<RendererError> T = EntityTypes.T(RendererError.class);

	String getRenderFailure();
	void setRenderFailure(String value);
	
}
