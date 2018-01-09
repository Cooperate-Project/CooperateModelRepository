package de.cooperateproject.repository.focustransfer.protocol.request;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.net4j.signal.Request;
import org.eclipse.net4j.util.io.ExtendedDataOutputStream;

import de.cooperateproject.repository.focustransfer.protocol.FocusTransferProtocol;
import de.cooperateproject.repository.protocolutils.CDOIDWriter;

/**
 * Request that sends a focus request for a specific diagram and element.
 */
public class FocusTransferElementSelectionRequest extends Request implements CDOIDWriter {

	/**
	 * The ID of the signal to be used in the {@link FocusTransferProtocol}.
	 */
	public static final short SIGNAL_ID = 0x11;
	private final String diagramIdentifier;
	private final CDOID elementID;

	/**
	 * Constructs the request.
	 * 
	 * @param protocol
	 *            The protocol instance this indication belongs to.
	 * @param diagramIdentifier
	 *            The identifier of the affected diagram.
	 * @param elementID
	 *            The identifier of the affected element.
	 */
	public FocusTransferElementSelectionRequest(FocusTransferProtocol protocol, String diagramIdentifier,
			CDOID elementID) {
		super(protocol, SIGNAL_ID);
		this.diagramIdentifier = diagramIdentifier;
		this.elementID = elementID;
	}

	@Override
	protected void requesting(ExtendedDataOutputStream out) throws Exception {
		out.writeString(diagramIdentifier);
		writeCDOID(out, elementID);
	}

}
