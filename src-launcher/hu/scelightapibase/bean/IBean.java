/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean;

/**
 * Ancestor interface for beans.
 * 
 * @author Andras Belicza
 * 
 * @see IIdedBean
 */
public interface IBean {
	
	/**
	 * Returns the bean instance version.
	 * 
	 * @return the bean instance version
	 */
	int getBeanVer();
	
	/**
	 * Clones this bean.
	 * 
	 * @param <T> dynamic type of the bean to return
	 * @return a clone of this bean
	 */
	< T extends IBean > T cloneBean();
	
	/**
	 * Builds a string representation of the bean using the public <code>getXXX()</code> methods.
	 * <p>
	 * The string representation will contain the short class name, and the string representation of the properties returned by the public <code>getXXX()</code>
	 * methods.<br>
	 * Only <code>getXXX()</code> methods that take 0 arguments and have a non-void return type will be included. {@link Object#getClass()} will be excluded.
	 * Moreover <code>getXXX()</code> methods annotated with the {@link HiddenProperty} annotation are also excluded.<br>
	 * If a <code>getXXX()</code> method returns an instance of {@link IBean}, then its {@link #buildDevString(StringBuilder)} method will be used to get its
	 * string representation.
	 * </p>
	 * 
	 * <p>
	 * <b>Note:</b> This is an effective way to build string representation of {@link IBean}s because the same string builder is shared and passed on to the
	 * child beans to build.
	 * </p>
	 * 
	 * <p>
	 * <b>Note #2:</b> If a <code>getXXX()</code> method returns <code>this</code>, then the string <code>"this"</code> is appended. Other than this case,
	 * circular references are not detected and will result in a {@link StackOverflowError} (e.g. if <code>a</code> and <code>a2</code> are equals in the
	 * following case: <code>ABean a; BBean b=a.getB(); ABean a2=b.getA();</code>).
	 * </p>
	 * 
	 * @param b string builder to append the string representation of the bean to
	 */
	void buildDevString( StringBuilder b );
	
}
