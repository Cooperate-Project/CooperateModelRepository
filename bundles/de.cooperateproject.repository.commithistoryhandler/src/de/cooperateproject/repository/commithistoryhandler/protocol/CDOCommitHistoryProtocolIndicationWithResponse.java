package de.cooperateproject.repository.commithistoryhandler.protocol;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.emf.cdo.spi.common.protocol.CDODataInputImpl;
import org.eclipse.emf.cdo.spi.common.protocol.CDODataInputImpl.Default;
import org.eclipse.emf.cdo.spi.common.protocol.CDODataOutputImpl;
import org.eclipse.net4j.signal.IndicationWithResponse;
import org.eclipse.net4j.util.io.ExtendedDataInputStream;
import org.eclipse.net4j.util.io.ExtendedDataOutputStream;

import de.cooperateproject.repository.commithistoryhandler.manager.CDOHistoryManagerAggregator;
import de.cooperateproject.repository.commithistoryhandler.manager.ICDOHistoryManager;

public abstract class CDOCommitHistoryProtocolIndicationWithResponse extends IndicationWithResponse
		implements CDOCommitHistoryProtocolSignal {

	private static final Logger LOGGER = Logger.getLogger(CDOCommitHistoryProtocolIndicationWithResponse.class);
	private ICDOHistoryManager historyManager;

	public CDOCommitHistoryProtocolIndicationWithResponse(CDOCommitHistoryProtocol protocol, short signalID) {
		super(protocol, signalID);
	}

	@Override
	protected void indicating(ExtendedDataInputStream in) throws Exception {
		String repositoryID = in.readString();
		LOGGER.debug(String.format("Received request for %s with repositoryID %s.", this.getClass().getSimpleName(), repositoryID));
		historyManager = CDOHistoryManagerAggregator.INSTANCE.getHistoryManager(repositoryID)
				.orElseThrow(IllegalArgumentException::new);
		indicatingData(in);
	}

	protected abstract void indicatingData(ExtendedDataInputStream in) throws Exception;

	protected ICDOHistoryManager getHistoryManager() {
		return historyManager;
	}
	
	protected static CDOID readCDOID(ExtendedDataInputStream in) throws IOException {
		Default cdoIn = new CDODataInputImpl.Default(in);
		return CDOIDUtil.read(cdoIn);
	}
	
	protected static void writeCDOID(ExtendedDataOutputStream out, CDOID cdoid) throws IOException {
        CDODataOutputImpl cdoOut = new CDODataOutputImpl(out);
        CDOIDUtil.write(cdoOut, cdoid);
	}
}
