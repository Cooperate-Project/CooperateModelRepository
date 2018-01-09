package de.cooperateproject.repository.focustransfer.protocol;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.emf.cdo.common.id.CDOID;

/**
 * Shared infrastructure to be used as communication platform between
 * infrastructure instances.
 * 
 * This infrastructure dispatches focus requests to matching
 * {@link IFocusRequestHandler} instances. In order to do this, the request
 * handlers can register and unregister themselves for multiple diagram
 * identifiers.
 */
public class FocusTransferSharedInfrastructure {

	private static final Logger LOGGER = Logger.getLogger(FocusTransferSharedInfrastructure.class);
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private final Map<IFocusRequestHandlerWithStatus, Collection<String>> requestHandlers = Collections
			.synchronizedMap(new HashMap<>());

	/**
	 * Start the shared infrastructure.
	 * 
	 * This method should be called before using the infrastructure in order to
	 * allow proper operation.
	 */
	public void start() {
		LOGGER.info("Starting shared infrastructure.");
		resetRequestHandlers();
		executor.scheduleWithFixedDelay(this::removeInactiveHandlers, 0, 1, TimeUnit.MINUTES);
	}

	/**
	 * Stop the shared infrastructure.
	 * 
	 * This clears all request handlers and stops internal processes.
	 */
	public void stop() {
		LOGGER.info("Stopping shared infrastructure.");
		executor.shutdownNow();
		resetRequestHandlers();
	}

	/**
	 * Register a {@link IFocusRequestHandlerWithStatus} with this infrastructure
	 * for events regarding a specific diagram.
	 * 
	 * @param handler
	 *            The handler to be registered.
	 * @param diagramIdentifier
	 *            The diagram identifier of interest.
	 */
	public void registerRequestHandler(IFocusRequestHandlerWithStatus handler, String diagramIdentifier) {
		LOGGER.info(String.format("Register new handler for diagram \"%s\"", diagramIdentifier));
		requestHandlers.putIfAbsent(handler, new HashSet<>());
		requestHandlers.get(handler).add(diagramIdentifier);
	}

	/**
	 * Unregister a {@link IFocusRequestHandlerWithStatus} with this infrastructure
	 * for events regarding a specific diagram.
	 * 
	 * @param handler
	 *            The handler to be unregistered.
	 * @param diagramIdentifier
	 *            The diagram identifier of interest.
	 */
	public void unregisterRequestHandler(IFocusRequestHandler handler, String diagramIdentifier) {
		LOGGER.info(String.format("Unregister handler for diagram \"%s\"", diagramIdentifier));
		Collection<String> diagrams = requestHandlers.getOrDefault(handler, Collections.emptySet());
		diagrams.remove(diagramIdentifier);
		if (diagrams.isEmpty()) {
			unregisterRequestHandler(handler);
		}
	}

	/**
	 * Unregister a {@link IFocusRequestHandlerWithStatus} with this infrastructure
	 * for all events.
	 * 
	 * @param handler
	 *            The handler to be unregistered.
	 */
	public void unregisterRequestHandler(IFocusRequestHandler handler) {
		LOGGER.info("Removing handler completely.");
		requestHandlers.remove(handler);
	}

	/**
	 * Process a focus request sent by a handler.
	 * 
	 * This dispatches the event to all other handlers that have been registered for
	 * the affected diagram via
	 * #{link{@link #registerRequestHandler(IFocusRequestHandlerWithStatus, String)}}.
	 * The sender will not be notified.
	 * 
	 * @param sender
	 *            The sender of the focus request.
	 * @param diagramIdentifier
	 *            The identifier of the affected diagram.
	 * @param selectedElementID
	 *            The ID of the selected element.
	 */
	public void handleFocusRequest(IFocusRequestHandler sender, String diagramIdentifier, CDOID selectedElementID) {
		Collection<IFocusRequestHandler> relevantRequestHandlers = requestHandlers.entrySet().stream()
				.filter(entry -> entry.getValue().contains(diagramIdentifier)).map(Entry::getKey)
				.filter(handler -> handler != sender).collect(Collectors.toSet());
		LOGGER.info(String.format("Dispatching focus request to \"%s\" (%s) to %d handlers.", diagramIdentifier,
				selectedElementID.toString(), relevantRequestHandlers.size()));
		for (IFocusRequestHandler requestHandler : relevantRequestHandlers) {
			try {
				requestHandler.handleFocusRequest(diagramIdentifier, selectedElementID);
			} catch (Exception e) {
				LOGGER.warn("Could not dispatch focus request to handler.", e);
			}
		}
	}

	protected void resetRequestHandlers() {
		requestHandlers.clear();
	}

	protected void removeInactiveHandlers() {
		if (requestHandlers.entrySet().removeIf(e -> !e.getKey().isValid())) {
			LOGGER.info("Removed inactive handler(s).");
		}
	}

}
