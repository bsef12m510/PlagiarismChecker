package com.prepostseo.plagchecker.checker.fragment;

import org.docx4j.model.images.AbstractConversionImageHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPart;

/** The DefaultConversionImageHandler is a pure File-based ImageHandler. 
 * 
 */
public class AndroidDataUriImageHandler extends AbstractConversionImageHandler {
	

	/** Creates as HTMLConversionImageHandler
	 */
	public AndroidDataUriImageHandler() {
		super( "", false);
		// imageDirPath.equals("") forces createEncodedImage
		// for internal ones.  TODO: review external handling
	}

	@Override
	protected String createStoredImage(BinaryPart arg0, byte[] arg1)
			throws Docx4JException {
		// TODO implement
		return null;
	}
	
}
    