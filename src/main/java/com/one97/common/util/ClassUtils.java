package com.one97.common.util;

import com.one97.SpockException;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

import static org.elasticsearch.common.Strings.toCamelCase;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class ClassUtils {

    /**
     * The package separator character '.'
     */
    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p/>
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you absolutely need a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
     * reference as well).
     *
     * @return the default ClassLoader (never <code>null</code>)
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * Determine the name of the package of the given class:
     * e.g. "java.lang" for the <code>java.lang.String</code> class.
     *
     * @param clazz the class
     * @return the package name, or the empty String if the class
     *         is defined in the default package
     */
    public static String getPackageName(Class<?> clazz) {
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
    }

    public static String getPackageNameNoDomain(Class<?> clazz) {
        String fullPackage = getPackageName(clazz);
        if (fullPackage.startsWith("org.") || fullPackage.startsWith("com.") || fullPackage.startsWith("net.")) {
            return fullPackage.substring(4);
        }
        return fullPackage;
    }

    public static boolean isInnerClass(Class<?> clazz) {
        return !Modifier.isStatic(clazz.getModifiers())
                && clazz.getEnclosingClass() != null;
    }

    public static boolean isConcrete(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        return !clazz.isInterface() && !Modifier.isAbstract(modifiers);
    }

    public static <T> Class<? extends T> loadClass(ClassLoader classLoader, String className, String prefixPackage, String suffixClassName) {
        return loadClass(classLoader, className, prefixPackage, suffixClassName, null);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Class<? extends T> loadClass(ClassLoader classLoader, String className, String prefixPackage, String suffixClassName, String errorPrefix) {
        Throwable t = null;
        String[] classNames = classNames(className, prefixPackage, suffixClassName);
        for (String fullClassName : classNames) {
            try {
                return (Class<? extends T>) classLoader.loadClass(fullClassName);
            } catch (ClassNotFoundException ex) {
                t = ex;
            } catch (NoClassDefFoundError er) {
                t = er;
            }
        }
        if (errorPrefix == null) {
            errorPrefix = "failed to load class";
        }
        throw new SpockException(errorPrefix + " with value [" + className + "]; tried " + Arrays.toString(classNames), t);
    }

    private static String[] classNames(String className, String prefixPackage, String suffixClassName) {
        String prefixValue = prefixPackage;
        int packageSeparator = className.lastIndexOf('.');
        String classNameValue = className;
        // If class name contains package use it as package prefix instead of specified default one
        if (packageSeparator > 0) {
            prefixValue = className.substring(0, packageSeparator + 1);
            classNameValue = className.substring(packageSeparator + 1);
        }
        return new String[]{
                className,
                prefixValue + StringUtils.capitalize(toCamelCase(classNameValue)) + suffixClassName,
                prefixValue + toCamelCase(classNameValue) + "." + StringUtils.capitalize(toCamelCase(classNameValue)) + suffixClassName,
                prefixValue + toCamelCase(classNameValue).toLowerCase(Locale.ROOT) + "." + StringUtils.capitalize(toCamelCase(classNameValue)) + suffixClassName,
        };
    }


    private ClassUtils() {

    }
}