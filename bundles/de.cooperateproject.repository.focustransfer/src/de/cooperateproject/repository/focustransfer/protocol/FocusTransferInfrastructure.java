package de.cooperateproject.repository.focustransfer.protocol;

import java.util.Optional;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.protocol.IProtocol;
import org.eclipse.net4j.util.lifecycle.Lifecycle;

import de.cooperateproject.repository.focustransfer.protocol.request.FocusTransferElementSelectionRequest;

/**
 * Infrastructure used by the focus transfer protocol.
 * 
 * By design, infrastructures are simple objects that are attached to a
 * protocol. For our use case, we use it as communication backend after
 * receiving messages.
 * 
 * The infrastructure allows registering, and unregistering handlers for focus
 * requests. It delegates focus requests to a shared infrastructure that
 * distributes them to other infrastructures.
 */
public class FocusTransferInfrastructure implements IFocusRequestHandlerWithStatus {

	private final FocusTransferSharedInfrastructure sharedInfrastructure;
	private FocusTransferProtocol protocol;

	/**
	 * Constructs the infrastructure.
	 * 
	 * @param sharedInfrastructure
	 *            The shared infrastructure that connects to other infrastructures.
	 */
	public FocusTransferInfrastructure(FocusTransferSharedInfrastructure sharedInfrastructure) {
		this.sharedInfrastructure = sharedInfrastructure;
	}

	/**
	 * Registers this infrastructure for notifications about a specific diagram.
	 * 
	 * @param diagramIdentifier
	 *            The identifier of the requested diagram.
	 */
	public void register(String diagramIdentifier) {
		getSharedInfrastructure().registerRequestHandler(this, diagramIdentifier);
	}

	/**
	 * Unregisters this infrastructure from notifications about a specific diagram.
	 * 
	 * @param diagramIdentifier
	 *            The identifier of the requested diagram.
	 */
	public void unregister(String diagramIdentifier) {
		getSharedInfrastructure().unregisterRequestHandler(this, diagramIdentifier);
	}

	/**
	 * Handles the focus request for a given element that originates from this
	 * infrastructure.
	 * 
	 * @param diagramIdentifier
	 *            The identifier of the requested diagram.
	 * @param selectedElementID
	 *            The ID of the selected element.
	 */
	public void processOwnElementSelection(String diagramIdentifier, CDOID selectedElementID) {
		getSharedInfrastructure().handleFocusRequest(this, diagramIdentifier, selectedElementID);
	}

	@Override
	public void handleFocusRequest(String diagramIdentifier, CDOID elementID) throws Exception {
		FocusTransferElementSelectionRequest request = new FocusTransferElementSelectionRequest(getProtocol(),
				diagramIdentifier, elementID);
		request.sendAsync();
	}

	/**
	 * Associates a protocol with this infrastructure.
	 * 
	 * @param protocol
	 *            The protocol to be used by this infrastructure.
	 */
	public void registerProtocol(FocusTransferProtocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * Dissociates a protocol with this infrastructure.
	 */
	public void unregisterProtocol() {
		this.protocol = null;
	}

	protected FocusTransferProtocol getProtocol() {
		return this.protocol;
	}

	protected FocusTransferSharedInfrastructure getSharedInfrastructure() {
		return this.sharedInfrastructure;
	}

	@Override
	public boolean isValid() {
		boolean protocolIsActive = Optional.ofNullable(protocol).map(Lifecycle::isActive).orElse(false);
		boolean channelIsActive = !Optional.ofNullable(protocol).map(IProtocol::getChannel).map(IChannel::isClosed)
				.orElse(true);
		return protocolIsActive && channelIsActive;
	}

}
