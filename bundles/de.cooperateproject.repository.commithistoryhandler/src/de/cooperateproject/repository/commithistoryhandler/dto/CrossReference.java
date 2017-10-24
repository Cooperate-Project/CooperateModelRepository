package de.cooperateproject.repository.commithistoryhandler.dto;

import java.io.Serializable;
import java.util.Optional;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.CDOObjectReference;
import org.eclipse.emf.cdo.common.id.CDOID;

public class CrossReference implements Serializable {

	private static final long serialVersionUID = -2154961001607083547L;
	private Commit commit;
	private CDOID fromObject;
	private CDOID fromResource;
	private CDOID toObject;
	private CDOID toResource;

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public CDOID getFromObject() {
		return fromObject;
	}

	public void setFromObject(CDOID fromObject) {
		this.fromObject = fromObject;
	}

	public CDOID getFromResource() {
		return fromResource;
	}

	public void setFromResource(CDOID fromResource) {
		this.fromResource = fromResource;
	}

	public CDOID getToObject() {
		return toObject;
	}

	public void setToObject(CDOID toObject) {
		this.toObject = toObject;
	}

	public CDOID getToResource() {
		return toResource;
	}

	public void setToResource(CDOID toResource) {
		this.toResource = toResource;
	}

	public static class Builder {
		private CrossReference instance = new CrossReference();

		public static Builder create() {
			return new Builder();
		}

		public Builder setCommit(Commit commit) {
			instance.setCommit(commit);
			return this;
		}

		public Builder setFromObject(CDOID id) {
			instance.setFromObject(id);
			return this;
		}

		public Builder setFromResource(CDOID id) {
			instance.setFromResource(id);
			return this;
		}

		public Builder setToObject(CDOID id) {
			instance.setToObject(id);
			return this;
		}

		public Builder setToResource(CDOID id) {
			instance.setToResource(id);
			return this;
		}

		public CrossReference build() {
			return instance;
		}
	}

	public static CrossReference create(CDOObjectReference ref) {
		CDOID sourceId = ref.getSourceID();
		CDOObject sourceObject = ref.getSourceObject();
		Optional<CDOID> sourceResourceId = Optional.ofNullable(sourceObject).map(CDOObject::cdoResource).map(CDOObject::cdoID);		
		CDOID targetId = ref.getTargetID();
		CDOObject targetObject = ref.getTargetObject();
		Optional<CDOID> targetResourceId = Optional.ofNullable(targetObject).map(CDOObject::cdoResource).map(CDOObject::cdoID);

		Builder builder = CrossReference.Builder.create().setFromObject(sourceId).setToObject(targetId);
		sourceResourceId.ifPresent(builder::setFromResource);
		targetResourceId.ifPresent(builder::setToResource);
		return builder.build();
	}

}
