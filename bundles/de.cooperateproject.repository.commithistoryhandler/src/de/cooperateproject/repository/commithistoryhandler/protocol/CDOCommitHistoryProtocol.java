package de.cooperateproject.repository.commithistoryhandler.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.net4j.signal.SignalProtocol;
import org.eclipse.net4j.signal.SignalReactor;

public class CDOCommitHistoryProtocol extends SignalProtocol<Object> {

	public static final String PROTOCOL_ID = "CDO_COMMIT_HISTORY";
	private final Map<Short, Supplier<SignalReactor>> reactorMap;

	public CDOCommitHistoryProtocol() {
		super(PROTOCOL_ID);
		reactorMap = createReactorMap(this);
	}

	@Override
	protected SignalReactor createSignalReactor(short signalID) {
		return reactorMap.getOrDefault(signalID, () -> super.createSignalReactor(signalID)).get();
	}

	private static Map<Short, Supplier<SignalReactor>> createReactorMap(CDOCommitHistoryProtocol protocol) {
		Map<Short, Supplier<SignalReactor>> reactors = new HashMap<>();

		reactors.put(CDOCommitHistoryProtocolGetChangedTimestamps.SIGNAL_ID,
				() -> new CDOCommitHistoryProtocolGetChangedTimestamps(protocol));

		return reactors;
	}

}
