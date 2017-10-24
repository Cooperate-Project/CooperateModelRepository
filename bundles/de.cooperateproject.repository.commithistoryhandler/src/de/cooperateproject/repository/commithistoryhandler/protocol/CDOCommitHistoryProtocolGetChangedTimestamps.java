package de.cooperateproject.repository.commithistoryhandler.protocol;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.Range;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.net4j.util.io.ExtendedDataInputStream;
import org.eclipse.net4j.util.io.ExtendedDataOutputStream;

public class CDOCommitHistoryProtocolGetChangedTimestamps extends CDOCommitHistoryProtocolIndicationWithResponse {

	public static final short SIGNAL_ID = 1;
	private Collection<CDOID> requestedResources = new ArrayList<>();
	private Collection<CDOID> crossReferencedResources = new ArrayList<>();
	private Range<Long> relevantTime;

	public CDOCommitHistoryProtocolGetChangedTimestamps(CDOCommitHistoryProtocol protocol) {
		super(protocol, SIGNAL_ID);
	}
	
	@Override
	protected void indicatingData(ExtendedDataInputStream in) throws Exception {
		int numberOfResources = in.readInt();
		for (int i = 0; i < numberOfResources; ++i) {
			requestedResources.add(readCDOID(in));			
		}
		
		int numberOfCrossReferencedResources = in.readInt();
		for (int i = 0; i < numberOfCrossReferencedResources; ++i) {
			crossReferencedResources.add(readCDOID(in));
		}
		
		boolean hasTimeRange = in.readBoolean();
		if (hasTimeRange) {
			long fromTime = in.readLong();
			long toTime = in.readLong();			
			relevantTime = Range.between(fromTime, toTime);
		}
	}

	@Override
	protected void responding(ExtendedDataOutputStream out) throws Exception {
		Collection<Long> result = getHistoryManager().getChangedTimestamps(requestedResources, crossReferencedResources,
				relevantTime);
		out.writeInt(result.size());
		for (Long value : result) {
			out.writeLong(value);
		}
	}

}
