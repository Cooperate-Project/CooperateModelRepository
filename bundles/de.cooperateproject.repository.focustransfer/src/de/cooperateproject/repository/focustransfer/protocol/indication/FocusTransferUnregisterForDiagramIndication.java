package de.cooperateproject.repository.focustransfer.protocol.indication;

import org.eclipse.net4j.signal.Indication;
import org.eclipse.net4j.signal.SignalProtocol;
import org.eclipse.net4j.util.io.ExtendedDataInputStream;

import de.cooperateproject.repository.focustransfer.protocol.FocusTransferProtocol;
import de.cooperateproject.repository.protocolutils.CDOIDReader;

/**
 * Indication that receives an unregister request for a specific diagram.
 */
public class FocusTransferUnregisterForDiagramIndication extends Indication implements CDOIDReader {

	/**
	 * The ID of the signal to be used in the {@link FocusTransferProtocol}.
	 */
	public static final short SIGNAL_ID = 0x02;

	/**
	 * Constructs the indication.
	 * 
	 * @param protocol
	 *            The protocol instance this indication belongs to.
	 */
	public FocusTransferUnregisterForDiagramIndication(SignalProtocol<?> protocol) {
		super(protocol, SIGNAL_ID);
	}

	@Override
	protected void indicating(ExtendedDataInputStream in) throws Exception {
		String diagramIdentifier = in.readString();
		((FocusTransferProtocol) getProtocol()).getInfraStructure().unregister(diagramIdentifier);
	}

}
