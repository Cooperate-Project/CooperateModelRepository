/**
 * Copyright (c) 2013 CEA LIST.
 * 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Laurent Wouters laurent.wouters@cea.fr - Initial API and implementation
 *  
 * 
 */
package org.eclipse.papyrus.infra.gmfdiag.style;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.notation.Style;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Papyrus Diagram Style</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getDiagramKindId <em>Diagram Kind Id</em>}</li>
 * </ul>
 *
 * @see org.eclipse.papyrus.infra.gmfdiag.style.StylePackage#getPapyrusDiagramStyle()
 * @model
 * @generated
 */
public interface PapyrusDiagramStyle extends Style {
	/**
	 * Returns the value of the '<em><b>Owner</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owner</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner</em>' reference.
	 * @see #setOwner(EObject)
	 * @see org.eclipse.papyrus.infra.gmfdiag.style.StylePackage#getPapyrusDiagramStyle_Owner()
	 * @model required="true"
	 * @generated
	 */
	EObject getOwner();

	/**
	 * Sets the value of the '{@link org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getOwner <em>Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(EObject value);

	/**
	 * Returns the value of the '<em><b>Diagram Kind Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagram Kind Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagram Kind Id</em>' attribute.
	 * @see #setDiagramKindId(String)
	 * @see org.eclipse.papyrus.infra.gmfdiag.style.StylePackage#getPapyrusDiagramStyle_DiagramKindId()
	 * @model
	 * @generated
	 */
	String getDiagramKindId();

	/**
	 * Sets the value of the '{@link org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getDiagramKindId <em>Diagram Kind Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram Kind Id</em>' attribute.
	 * @see #getDiagramKindId()
	 * @generated
	 */
	void setDiagramKindId(String value);

} // PapyrusDiagramStyle
