package com.one97.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.google.common.base.Strings.isNullOrEmpty;

/*
 * ---------------------------------------------------------------------------------------------
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * ---------------------------------------------------------------------------------------------
 */

public class StringUtils {

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static String[] splitStringByCommaToArray(final String s) {
        return splitStringToArray(s, ',');
    }

    public static String[] splitStringToArray(final CharSequence s, final char c) {
        if (s == null || s.length() == 0) {
            return EMPTY_STRING_ARRAY;
        }
        int count = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        final String[] result = new String[count];
        final StringBuilder builder = new StringBuilder();
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                if (builder.length() > 0) {
                    result[res++] = builder.toString();
                    builder.setLength(0);
                }

            } else {
                builder.append(s.charAt(i));
            }
        }
        if (builder.length() > 0) {
            result[res++] = builder.toString();
        }
        if (res != count) {
            // we have empty strings, copy over to a new array
            String[] result1 = new String[res];
            System.arraycopy(result, 0, result1, 0, res);
            return result1;
        }
        return result;
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        if (capitalize) {
            sb.append(Character.toUpperCase(str.charAt(0)));
        } else {
            sb.append(Character.toLowerCase(str.charAt(0)));
        }
        sb.append(str.substring(1));
        return sb.toString();
    }

    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    public static String camelCase(String value, StringBuilder sb) {
        boolean changed = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '_') {
                if (!changed) {
                    if (sb != null) {
                        sb.setLength(0);
                    } else {
                        sb = new StringBuilder();
                    }
                    // copy it over here
                    for (int j = 0; j < i; j++) {
                        sb.append(value.charAt(j));
                    }
                    changed = true;
                }
                if (i < value.length() - 1) {
                    sb.append(Character.toUpperCase(value.charAt(++i)));
                }
            } else {
                if (changed) {
                    sb.append(c);
                }
            }
        }
        if (!changed) {
            return value;
        }
        return sb.toString();
    }

    public static String camelCase(String value) {
        return camelCase(value, null);
    }

    public static String concatenateNameParts (String firstName, String middleName, String lastName) {
    	StringBuilder nameBuilder = new StringBuilder();

    	if(!isNullOrEmpty(firstName) ) {
    		nameBuilder.append(firstName.trim());
    	}

    	if(!isNullOrEmpty(middleName)) {
    		if(!isNullOrEmpty(nameBuilder.toString())){
    			nameBuilder.append(" ");
    		}
    		nameBuilder.append(middleName.trim());
    	}

    	if(!isNullOrEmpty(lastName)) {
    		if(!isNullOrEmpty(nameBuilder.toString())){
    			nameBuilder.append(" ");
    		}
    		nameBuilder.append(lastName.trim());
    	}

    	return nameBuilder.toString();
    }
    
    public static String removeTypePrefix(String stringWithPrefix){
    	if(isNullOrEmpty(stringWithPrefix))
    		return stringWithPrefix;
    	
    	return stringWithPrefix.trim().substring(1, stringWithPrefix.length());
    }

    public static String hashGenerator(String str) {

        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        md5.reset();
        md5.update(str.getBytes());
        byte[] digest = md5.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }

        return hashtext.substring(0, 5);
    }
}
