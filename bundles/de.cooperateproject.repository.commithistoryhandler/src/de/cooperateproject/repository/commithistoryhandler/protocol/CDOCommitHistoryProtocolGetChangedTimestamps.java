package de.cooperateproject.repository.commithistoryhandler.protocol;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.Range;
import org.apache.log4j.Logger;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.net4j.util.io.ExtendedDataInputStream;
import org.eclipse.net4j.util.io.ExtendedDataOutputStream;

public class CDOCommitHistoryProtocolGetChangedTimestamps extends CDOCommitHistoryProtocolIndicationWithResponse {

	private static final Logger LOGGER = Logger.getLogger(CDOCommitHistoryProtocolGetChangedTimestamps.class);
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
		LOGGER.info(String.format("Processing request: %s", createRequestString()));
		Collection<Long> result = getHistoryManager().getChangedTimestamps(requestedResources, crossReferencedResources,
				relevantTime);
		out.writeInt(result.size());
		for (Long value : result) {
			out.writeLong(value);
		}
	}
	
	private String createRequestString() {
		String result = String.format("Requested resources %s, relevant referenced resources %s", toString(requestedResources), toString(crossReferencedResources));
		if (relevantTime != null) {
			return result + String.format(", time [%d;%d]", relevantTime.getMinimum(), relevantTime.getMaximum());
		}
		return result;
	}

	private static String toString(Iterable<CDOID> ids) {
		StringBuilder sb = new StringBuilder();
		CDOIDUtil.write(sb, ids);
		return sb.toString();
	}
}
