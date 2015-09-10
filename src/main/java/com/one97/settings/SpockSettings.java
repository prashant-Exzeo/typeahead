package com.one97.settings;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.common.settings.NoClassSettingsException;
import org.elasticsearch.common.settings.SettingsException;
import org.slf4j.Logger;

import java.util.Map;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public interface SpockSettings {

    /**
     * The settings as a flat {@link Map}.
     */
    ImmutableMap<String, String> getAsMap();

    /**
     * The settings as a structured {@link Map}.
     */
    Map<String, Object> getAsStructuredMap();

    /**
     * Returns the setting value associated with the setting key.
     *
     * @param setting The setting key
     * @return The setting value, <tt>null</tt> if it does not exists.
     */
    String get(String setting);

    /**
     * Returns the setting value associated with the first setting key.
     */
    String get(String[] settings);

    ClassLoader getClassLoader();

    /**
     * Returns the setting value associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    String get(String setting, String defaultValue);

    /**
     * Returns the setting value associated with the first setting key, if none exists,
     * returns the default value provided.
     */
    String get(String[] settings, String defaultValue);

    /**
     * Returns the setting value (as float) associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    Float getAsFloat(String setting, Float defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as float) associated with teh first setting key, if none
     * exists, returns the default value provided.
     */
    Float getAsFloat(String[] settings, Float defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as double) associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    Double getAsDouble(String setting, Double defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as double) associated with teh first setting key, if none
     * exists, returns the default value provided.
     */
    Double getAsDouble(String[] settings, Double defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as int) associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    Integer getAsInt(String setting, Integer defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as int) associated with the first setting key. If it does not exists,
     * returns the default value provided.
     */
    Integer getAsInt(String[] settings, Integer defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as long) associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    Long getAsLong(String setting, Long defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as long) associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    Long getAsLong(String[] settings, Long defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as boolean) associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    Boolean getAsBoolean(String setting, Boolean defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as boolean) associated with the setting key. If it does not exists,
     * returns the default value provided.
     */
    Boolean getAsBoolean(String[] settings, Boolean defaultValue) throws SettingsException;

    /**
     * Returns the setting value (as a class) associated with the setting key. If it does not exists,
     * returns the default class provided.
     *
     * @param setting      The setting key
     * @param defaultClazz The class to return if no value is associated with the setting
     * @param <T>          The type of the class
     * @return The class setting value, or the default class provided is no value exists
     * @throws NoClassSettingsException Failure to load a class
     */
    <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz) throws NoClassSettingsException;

    /**
     * Returns the setting value (as a class) associated with the setting key. If the value itself fails to
     * represent a loadable class, the value will be appended to the <tt>prefixPackage</tt> and suffixed with the
     * <tt>suffixClassName</tt> and it will try to be loaded with it.
     *
     * @param setting         The setting key
     * @param defaultClazz    The class to return if no value is associated with the setting
     * @param prefixPackage   The prefix package to prefix the value with if failing to load the class as is
     * @param suffixClassName The suffix class name to prefix the value with if failing to load the class as is
     * @param <T>             The type of the class
     * @return The class represented by the setting value, or the default class provided if no value exists
     * @throws NoClassSettingsException Failure to load the class
     */
    <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz, String prefixPackage, String suffixClassName) throws NoClassSettingsException;

    /**
     * The values associated with a setting prefix as an array. The settings array is in the format of:
     * <tt>settingPrefix.[index]</tt>.
     * <p/>
     * <p>It will also automatically load a comma separated list under the settingPrefix and merge with
     * the numbered format.
     *
     * @param settingPrefix  The setting prefix to load the array by
     * @param defaultArray   The default array to use if no value is specified
     * @param commaDelimited Whether to try to parse a string as a comma-delimited value
     * @return The setting array values
     * @throws SettingsException
     */
    String[] getAsArray(String settingPrefix, String[] defaultArray, Boolean commaDelimited) throws SettingsException;

    /**
     * The values associated with a setting prefix as an array. The settings array is in the format of:
     * <tt>settingPrefix.[index]</tt>.
     * <p/>
     * <p>If commaDelimited is true, it will automatically load a comma separated list under the settingPrefix and merge with
     * the numbered format.
     *
     * @param settingPrefix The setting prefix to load the array by
     * @return The setting array values
     * @throws SettingsException
     */
    String[] getAsArray(String settingPrefix, String[] defaultArray) throws SettingsException;

    /**
     * The values associated with a setting prefix as an array. The settings array is in the format of:
     * <tt>settingPrefix.[index]</tt>.
     * <p/>
     * <p>It will also automatically load a comma separated list under the settingPrefix and merge with
     * the numbered format.
     *
     * @param settingPrefix The setting prefix to load the array by
     * @return The setting array values
     * @throws SettingsException
     */
    String[] getAsArray(String settingPrefix) throws SettingsException;


    void printSettings(Logger logger);
}
