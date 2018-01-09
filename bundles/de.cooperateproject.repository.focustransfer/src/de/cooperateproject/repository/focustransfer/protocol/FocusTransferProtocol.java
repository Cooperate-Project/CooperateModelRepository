package de.cooperateproject.repository.focustransfer.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.net4j.signal.SignalProtocol;
import org.eclipse.net4j.signal.SignalReactor;

import de.cooperateproject.repository.focustransfer.protocol.indication.FocusTransferElementSelectionIndication;
import de.cooperateproject.repository.focustransfer.protocol.indication.FocusTransferRegisterForDiagramIndication;
import de.cooperateproject.repository.focustransfer.protocol.indication.FocusTransferUnregisterForDiagramIndication;

/**
 * Implementation of a Net4j protocol that can handle focus requests.
 */
public class FocusTransferProtocol extends SignalProtocol<FocusTransferInfrastructure> {

	/**
	 * The ID of the protocol.
	 */
	public static final String PROTOCOL_ID = "COOPERATE_FOCUS_TRANSFER";
	private final Map<Short, Supplier<SignalReactor>> signalReactorFactories;

	/**
	 * Constructs the protocol.
	 * 
	 * @param focusTransferSharedInfrastructure
	 *            The shared infrastructure that connects various protocol
	 *            instances.
	 */
	public FocusTransferProtocol(FocusTransferSharedInfrastructure focusTransferSharedInfrastructure) {
		super(PROTOCOL_ID);
		signalReactorFactories = createFactories();
		registerInfrastructure(focusTransferSharedInfrastructure);
	}

	@Override
	protected SignalReactor createSignalReactor(short signalID) {
		return Optional.ofNullable(signalReactorFactories.get(signalID)).map(Supplier::get)
				.orElseGet(() -> super.createSignalReactor(signalID));
	}

	@Override
	public void close() {
		unregisterInfrastructure();
		super.close();
	}

	protected Map<Short, Supplier<SignalReactor>> createFactories() {
		Map<Short, Supplier<SignalReactor>> result = new HashMap<>();

		result.put(FocusTransferRegisterForDiagramIndication.SIGNAL_ID,
				() -> new FocusTransferRegisterForDiagramIndication(this));
		result.put(FocusTransferUnregisterForDiagramIndication.SIGNAL_ID,
				() -> new FocusTransferUnregisterForDiagramIndication(this));
		result.put(FocusTransferElementSelectionIndication.SIGNAL_ID,
				() -> new FocusTransferElementSelectionIndication(this));

		return result;
	}

	protected void registerInfrastructure(FocusTransferSharedInfrastructure sharedInfrastructure) {
		setInfraStructure(new FocusTransferInfrastructure(sharedInfrastructure));
		getInfraStructure().registerProtocol(this);
	}

	protected void unregisterInfrastructure() {
		getInfraStructure().unregisterProtocol();
		setInfraStructure(null);
	}

}
