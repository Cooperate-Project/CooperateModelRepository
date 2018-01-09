package de.cooperateproject.repository.focustransfer.protocol;

import java.util.Optional;

import org.eclipse.net4j.util.factory.ProductCreationException;
import org.eclipse.spi.net4j.ServerProtocolFactory;

/**
 * Factory for the {@link FocusTransferProtocol} to be used by Net4j.
 */
public class FocusTransferProtocolFactory extends ServerProtocolFactory {

	private static FocusTransferSharedInfrastructure sharedInfrastructure;

	/**
	 * Constructs the factory.
	 */
	public FocusTransferProtocolFactory() {
		super(FocusTransferProtocol.PROTOCOL_ID);
	}

	@Override
	public Object create(String description) {
		if (getSharedInfrastructure() == null) {
			throw new ProductCreationException("Shared infrastructure is not available.");
		}
		return new FocusTransferProtocol(getSharedInfrastructure());
	}

	/**
	 * Sets the shared infrastructure to be used by this factory during the creation
	 * of protocol instances. The infrastructure will be activated during the set
	 * process and an existing old instance will be deactivated.
	 * 
	 * @param newSharedInfrastructure
	 *            The shared infrastructure to be used.
	 */
	public static void setAndActivateSharedInfrastructure(FocusTransferSharedInfrastructure newSharedInfrastructure) {
		FocusTransferSharedInfrastructure oldInfrastructure = FocusTransferProtocolFactory.sharedInfrastructure;
		if (oldInfrastructure != newSharedInfrastructure) {
			Optional.ofNullable(oldInfrastructure).ifPresent(FocusTransferSharedInfrastructure::stop);
		}
		FocusTransferProtocolFactory.sharedInfrastructure = newSharedInfrastructure;
		Optional.ofNullable(newSharedInfrastructure).ifPresent(FocusTransferSharedInfrastructure::start);
	}

	protected static FocusTransferSharedInfrastructure getSharedInfrastructure() {
		return FocusTransferProtocolFactory.sharedInfrastructure;
	}

}
