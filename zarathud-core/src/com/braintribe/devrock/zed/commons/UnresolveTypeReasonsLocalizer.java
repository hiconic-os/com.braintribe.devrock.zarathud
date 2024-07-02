package com.braintribe.devrock.zed.commons;

import java.util.List;

import com.braintribe.devrock.zarathud.model.reasons.UrlNotFound;
import com.braintribe.gm.model.reason.Reason;

public class UnresolveTypeReasonsLocalizer {

	private static final String CLASS = ".class";

	public static void conceptualizeUnresolvableTypeReasons( List<Reason> reasons) {
		conceptualizeUnresolvableTypeReasons(reasons, null);
	}
	
	public static void conceptualizeUnresolvableTypeReasons( List<Reason> reasons, List<String> directories) {
		
		for (Reason reason : reasons) {
			if (reason instanceof UrlNotFound) {
				UrlNotFound unfReason = (UrlNotFound) reason;
				String scannedType = unfReason.getScannedType();
				if (directories == null) {
					scannedType = processClass(scannedType);
					unfReason.setScannedType(scannedType);
				}
				else {
					for (String directory : directories) {
						if (scannedType.startsWith( directory)) {
							scannedType = scannedType.substring( directory.length());
							break;
						}						
					}
					scannedType = processClass(scannedType);							
					unfReason.setScannedType(scannedType);
				}
			}
		}
					
		
	}
	
	private static String processClass(String typeName) {
		if (typeName.endsWith( CLASS)) {
			typeName = typeName.substring(0, typeName.length() - CLASS.length());
		}		
		if (typeName.startsWith( "/")) {
			typeName = typeName.substring(1);
		}
		typeName = typeName.replace('/', '.');
		return typeName;
		
		
	}
}
