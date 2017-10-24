package de.cooperateproject.repository.commithistoryhandler.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.cdo.common.id.CDOID;

public class Commit implements Serializable {

	private static final long serialVersionUID = 8851913557239635808L;
	private long timestamp;
	
	private Collection<CrossReference> crossReferences = new ArrayList<>();
	
	private Collection<CDOID> changedResources = new ArrayList<>();
	
	private Collection<CDOID> changedObjects = new ArrayList<>();
	
	private Collection<CDOID> deletedObjects = new ArrayList<>();

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Collection<CrossReference> getCrossReferences() {
		return crossReferences;
	}

	public Collection<CDOID> getChangedResources() {
		return changedResources;
	}

	public Collection<CDOID> getChangedObjects() {
		return changedObjects;
	}
	
	public Collection<CDOID> getDeletedObjects() {
		return deletedObjects;
	}
}
