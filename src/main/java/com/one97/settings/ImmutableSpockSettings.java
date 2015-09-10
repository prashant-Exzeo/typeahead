package com.one97.settings;

import com.one97.SpockException;
import com.one97.common.util.BooleanUtils;
import com.one97.common.util.ClassUtils;
import com.one97.common.util.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

public class ImmutableSpockSettings implements SpockSettings {

    private static String[] EMPTY_ARRAY = new String[0];
    private final ImmutableMap<String, String> conf;
    private final transient ClassLoader loader;

    public ImmutableSpockSettings(ImmutableMap<String, String> conf, ClassLoader loader) {
        this.conf = conf;
        this.loader = loader;
    }

    @Override
    public ImmutableMap<String, String> getAsMap() {
        return this.conf;
    }

    @Override
    public Map<String, Object> getAsStructuredMap() {
        throw new NotImplementedException();
    }

    @Override
    public String get(String setting) {
        return conf.get(setting);
    }

    @Override
    public String get(String[] settings) {

        for (String setting : settings) {
            String retVal = this.conf.get(setting);
            if (retVal != null) {
                return retVal;
            }
            retVal = this.conf.get(StringUtils.camelCase(setting));
            if (retVal != null) {
                return retVal;
            }
        }
        return null;

    }

    @Override
    public ClassLoader getClassLoader() {
        return this.loader == null ? ClassUtils.getDefaultClassLoader() : loader;
    }

    @Override
    public String get(String setting, String defaultValue) {
        String retVal = get(setting);
        return retVal == null ? defaultValue : retVal;
    }

    @Override
    public String get(String[] settings, String defaultValue) {
        String retVal = get(settings);
        return retVal == null ? defaultValue : retVal;
    }

    @Override
    public Float getAsFloat(String setting, Float defaultValue) {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse float setting [" + setting + "] with value [" + sValue + "]", e);
        }

    }

    @Override
    public Float getAsFloat(String[] settings, Float defaultValue) {
        String sValue = get(settings);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse float setting [" + Arrays.toString(settings) + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Double getAsDouble(String setting, Double defaultValue) {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse double setting [" + setting + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Double getAsDouble(String[] settings, Double defaultValue) {
        String sValue = get(settings);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse double setting [" + Arrays.toString(settings) + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Integer getAsInt(String setting, Integer defaultValue) throws SpockException {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse int setting [" + setting + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Integer getAsInt(String[] settings, Integer defaultValue) throws SpockException {
        String sValue = get(settings);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse int setting [" + Arrays.toString(settings) + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Long getAsLong(String setting, Long defaultValue) {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse long setting [" + setting + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Long getAsLong(String[] settings, Long defaultValue) throws SpockException {
        String sValue = get(settings);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(sValue);
        } catch (NumberFormatException e) {
            throw new SpockException("Failed to parse long setting [" + Arrays.toString(settings) + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Boolean getAsBoolean(String setting, Boolean defaultValue) {
        return BooleanUtils.parseBoolean(get(setting), defaultValue);
    }

    @Override
    public Boolean getAsBoolean(String[] settings, Boolean defaultValue) throws SpockException {
        return BooleanUtils.parseBoolean(get(settings), defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz) throws SpockException {

        String sValue = get(setting);
        if (sValue == null) {
            return defaultClazz;
        }
        try {
            return (Class<? extends T>) getClassLoader().loadClass(sValue);
        } catch (ClassNotFoundException e) {
            throw new SpockException("Failed to load class setting [" + setting + "] with value [" + sValue + "]", e);
        }


    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz, String prefixPackage, String suffixClassName) throws SpockException {

        String sValue = get(setting);
        if (sValue == null) {
            return defaultClazz;
        }
        String fullClassName = sValue;
        try {
            return (Class<? extends T>) getClassLoader().loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            String prefixValue = prefixPackage;
            int packageSeparator = sValue.lastIndexOf('.');
            if (packageSeparator > 0) {
                prefixValue = sValue.substring(0, packageSeparator + 1);
                sValue = sValue.substring(packageSeparator + 1);
            }
            fullClassName = prefixValue + StringUtils.capitalize(StringUtils.camelCase(sValue)) + suffixClassName;
            try {
                return (Class<? extends T>) getClassLoader().loadClass(fullClassName);
            } catch (ClassNotFoundException e1) {
                return loadClass(prefixValue, sValue, suffixClassName, setting);
            } catch (NoClassDefFoundError e1) {
                return loadClass(prefixValue, sValue, suffixClassName, setting);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> loadClass(String prefixValue, String sValue, String suffixClassName, String setting) {
        String fullClassName = prefixValue + StringUtils.camelCase(sValue).toLowerCase(Locale.ROOT) + "." + StringUtils.capitalize(StringUtils.camelCase(sValue)) + suffixClassName;
        try {
            return (Class<? extends T>) getClassLoader().loadClass(fullClassName);
        } catch (ClassNotFoundException e2) {
            throw new SpockException("Failed to load class setting [" + setting + "] with value [" + get(setting) + "]", e2);
        }
    }

    @Override
    public String[] getAsArray(String settingPrefix, String[] defaultArray, Boolean commaDelimited) throws SpockException {

        List<String> result = Lists.newArrayList();

        if (get(settingPrefix) != null) {
            if (commaDelimited) {
                String[] strings = StringUtils.splitStringByCommaToArray(get(settingPrefix));
                if (strings.length > 0) {
                    for (String string : strings) {
                        result.add(string.trim());
                    }
                }
            } else {
                result.add(get(settingPrefix).trim());
            }
        }

        int counter = 0;
        while (true) {
            String value = get(settingPrefix + '.' + (counter++));
            if (value == null) {
                break;
            }
            result.add(value.trim());
        }
        if (result.isEmpty()) {
            return defaultArray;
        }
        return result.toArray(new String[result.size()]);

    }

    @Override
    public String[] getAsArray(String settingPrefix, String[] defaultArray) throws SpockException {
        return getAsArray(settingPrefix, defaultArray, true);
    }

    @Override
    public String[] getAsArray(String settingPrefix) throws SpockException {
        return getAsArray(settingPrefix, EMPTY_ARRAY, true);
    }

    @Override
    public void printSettings(Logger logger) {

        for (String key : conf.keySet()) {
            String value = conf.get(key);
            logger.info("   Using '{}' for key '{}'", value, key);
        }

    }
}
