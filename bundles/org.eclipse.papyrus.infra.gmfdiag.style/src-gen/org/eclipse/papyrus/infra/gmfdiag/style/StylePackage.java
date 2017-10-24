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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.papyrus.infra.gmfdiag.style.StyleFactory
 * @model kind="package"
 * @generated
 */
public interface StylePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "style";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/papyrus/infra/gmfdiag/style";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "style";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	StylePackage eINSTANCE = org.eclipse.papyrus.infra.gmfdiag.style.impl.StylePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.papyrus.infra.gmfdiag.style.impl.PapyrusDiagramStyleImpl <em>Papyrus Diagram Style</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.papyrus.infra.gmfdiag.style.impl.PapyrusDiagramStyleImpl
	 * @see org.eclipse.papyrus.infra.gmfdiag.style.impl.StylePackageImpl#getPapyrusDiagramStyle()
	 * @generated
	 */
	int PAPYRUS_DIAGRAM_STYLE = 0;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPYRUS_DIAGRAM_STYLE__OWNER = NotationPackage.STYLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Diagram Kind Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPYRUS_DIAGRAM_STYLE__DIAGRAM_KIND_ID = NotationPackage.STYLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Papyrus Diagram Style</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAPYRUS_DIAGRAM_STYLE_FEATURE_COUNT = NotationPackage.STYLE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Papyrus Diagram Style</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 * @ordered
	 */
	int PAPYRUS_DIAGRAM_STYLE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle <em>Papyrus Diagram Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Papyrus Diagram Style</em>'.
	 * @see org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle
	 * @generated
	 */
	EClass getPapyrusDiagramStyle();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Owner</em>'.
	 * @see org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getOwner()
	 * @see #getPapyrusDiagramStyle()
	 * @generated
	 */
	EReference getPapyrusDiagramStyle_Owner();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getDiagramKindId <em>Diagram Kind Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Diagram Kind Id</em>'.
	 * @see org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle#getDiagramKindId()
	 * @see #getPapyrusDiagramStyle()
	 * @generated
	 */
	EAttribute getPapyrusDiagramStyle_DiagramKindId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	StyleFactory getStyleFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.papyrus.infra.gmfdiag.style.impl.PapyrusDiagramStyleImpl <em>Papyrus Diagram Style</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.papyrus.infra.gmfdiag.style.impl.PapyrusDiagramStyleImpl
		 * @see org.eclipse.papyrus.infra.gmfdiag.style.impl.StylePackageImpl#getPapyrusDiagramStyle()
		 * @generated
		 */
		EClass PAPYRUS_DIAGRAM_STYLE = eINSTANCE.getPapyrusDiagramStyle();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PAPYRUS_DIAGRAM_STYLE__OWNER = eINSTANCE.getPapyrusDiagramStyle_Owner();

		/**
		 * The meta object literal for the '<em><b>Diagram Kind Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAPYRUS_DIAGRAM_STYLE__DIAGRAM_KIND_ID = eINSTANCE.getPapyrusDiagramStyle_DiagramKindId();

	}

} //StylePackage
