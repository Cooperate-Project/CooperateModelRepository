package de.cooperateproject.repository.commithistoryhandler.protocol;

import org.eclipse.spi.net4j.ServerProtocolFactory;

public class CDOCommitHistoryProtocolFactory extends ServerProtocolFactory {

	public CDOCommitHistoryProtocolFactory() {
		super(CDOCommitHistoryProtocol.PROTOCOL_ID);
	}

	@Override
	public Object create(String description) {
		return new CDOCommitHistoryProtocol();
	}

}
