package de.cooperateproject.repository.focustransfer.protocol;

/**
 * Handler for focus requests with an additional status indicator.
 */
public interface IFocusRequestHandlerWithStatus extends IFocusRequestHandler {

	/**
	 * Indicates the status of the handler.
	 * 
	 * @return True, if the handler is operational and can process requests. False,
	 *         otherwise.
	 */
	boolean isValid();

}
