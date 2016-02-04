/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A utility class to help process XML {@link Document}s.
 * 
 * @author Andras Belicza
 */
public class XmlProcessor {
	
	/** Document element of the document. */
	public final Element docElement;
	
	/**
	 * Creates a new {@link XmlProcessor}.
	 * 
	 * @param doc {@link Document} to be processed
	 */
	public XmlProcessor( final Document doc ) {
		docElement = doc.getDocumentElement();
	}
	
	/**
	 * Returns the first element specified by its tag name, in the scope of the document element.
	 * 
	 * @param tagName tag name of the element to be returned
	 * @return the first element specified by its tag name or <code>null</code> if no such element exists in the document elemept scope
	 * 
	 * @see #getElementByTagName(Element, String)
	 */
	public Element getElementByTagName( final String tagName ) {
		return getElementByTagName( docElement, tagName );
	}
	
	/**
	 * Returns the first element specified by its tag name, in the scope of the specified element.
	 * 
	 * <p>
	 * The scope means the element to look for and return must be a descendant of the scope element.
	 * </p>
	 * 
	 * @param scopeElement scope element
	 * @param tagName tag name of the element to be returned
	 * @return the first element specified by its tag name or <code>null</code> if no such element exists in the specified scope
	 */
	public static Element getElementByTagName( final Element scopeElement, final String tagName ) {
		final NodeList nodeList = scopeElement.getElementsByTagName( tagName );
		
		return nodeList.getLength() > 0 ? (Element) nodeList.item( 0 ) : null;
	}
	
}
