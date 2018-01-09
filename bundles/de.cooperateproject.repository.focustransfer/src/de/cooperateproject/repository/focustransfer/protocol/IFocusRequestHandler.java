package de.cooperateproject.repository.focustransfer.protocol;

import org.eclipse.emf.cdo.common.id.CDOID;

/**
 * Handler for a focus request.
 */
public interface IFocusRequestHandler {

	/**
	 * Handles the request to focus a given element in a given diagram.
	 * 
	 * @param diagramIdentifier
	 *            The identifier of the affected diagram.
	 * @param elementID
	 *            The identifier of the affected element.
	 * @throws Exception
	 *             Thrown in case of an error during processing the request.
	 */
	void handleFocusRequest(String diagramIdentifier, CDOID elementID) throws Exception;

}
