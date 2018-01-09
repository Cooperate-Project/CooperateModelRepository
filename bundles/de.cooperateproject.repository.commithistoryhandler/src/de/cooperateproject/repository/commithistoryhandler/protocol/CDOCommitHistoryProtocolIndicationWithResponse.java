package de.cooperateproject.repository.commithistoryhandler.protocol;

import org.apache.log4j.Logger;
import org.eclipse.net4j.signal.IndicationWithResponse;
import org.eclipse.net4j.util.io.ExtendedDataInputStream;

import de.cooperateproject.repository.commithistoryhandler.manager.CDOHistoryManagerAggregator;
import de.cooperateproject.repository.commithistoryhandler.manager.ICDOHistoryManager;
import de.cooperateproject.repository.protocolutils.CDOIDReaderWriter;

public abstract class CDOCommitHistoryProtocolIndicationWithResponse extends IndicationWithResponse
		implements CDOCommitHistoryProtocolSignal, CDOIDReaderWriter {

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

}
