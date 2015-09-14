package com.one97.common.path;

import com.one97.common.util.StringUtils;
import com.google.common.collect.ImmutableMap;

import java.util.Map;


/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */

public class PathSupport<T> {

    public interface Decoder {
        String decode(String value);
    }

    public static final Decoder NO_DECODER = new Decoder() {
        @Override
        public String decode(String value) {
            return value;
        }
    };

    private final Decoder decoder;
    private final TrieNode<T> root;
    private final char separator;
    private T rootValue;

    public PathSupport() {
        this('/', "*", NO_DECODER);
    }

    public PathSupport(Decoder decoder) {
        this('/', "*", decoder);
    }

    public PathSupport(char separator, String wildcard, Decoder decoder) {
        this.decoder = decoder;
        this.separator = separator;
        root = new TrieNode<>(new String(new char[]{separator}), null, null, wildcard);
    }

    public class TrieNode<T> {
        private transient String key;
        private transient T value;
        private boolean isWildcard;
        private final String wildcard;

        private transient String namedWildcard;

        private ImmutableMap<String, TrieNode<T>> children;

        private final TrieNode parent;

        public TrieNode(String key, T value, TrieNode parent, String wildcard) {
            this.key = key;
            this.wildcard = wildcard;
            this.isWildcard = (key.equals(wildcard));
            this.parent = parent;
            this.value = value;
            this.children = ImmutableMap.of();
            if (isNamedWildcard(key)) {
                namedWildcard = key.substring(key.indexOf('{') + 1, key.indexOf('}'));
            } else {
                namedWildcard = null;
            }
        }

        public void updateKeyWithNamedWildcard(String key) {
            this.key = key;
            namedWildcard = key.substring(key.indexOf('{') + 1, key.indexOf('}'));
        }

        public boolean isWildcard() {
            return isWildcard;
        }

        public synchronized void addChild(TrieNode<T> child) {

            ImmutableMap.Builder<String, TrieNode<T>> builder
                    = ImmutableMap.builder();

            children = builder
                    .putAll(children)
                    .put(child.key, child)
                    .build();
        }

        public TrieNode getChild(String key) {
            return children.get(key);
        }

        public synchronized void insert(String[] path, int index, T value) {
            if (index >= path.length)
                return;

            String token = path[index];
            String key = token;
            if (isNamedWildcard(token)) {
                key = wildcard;
            }
            TrieNode<T> node = children.get(key);
            if (node == null) {
                if (index == (path.length - 1)) {
                    node = new TrieNode<>(token, value, this, wildcard);
                } else {
                    node = new TrieNode<>(token, null, this, wildcard);
                }

                ImmutableMap.Builder<String, TrieNode<T>> builder
                        = ImmutableMap.builder();

                children = builder
                        .putAll(children)
                        .put(key, node)
                        .build();
            } else {
                if (isNamedWildcard(token)) {
                    node.updateKeyWithNamedWildcard(token);
                }

                // in case the target(last) node already exist but without a value
                // than the value should be updated.
                if (index == (path.length - 1)) {
                    assert (node.value == null || node.value == value);
                    if (node.value == null) {
                        node.value = value;
                    }
                }
            }

            node.insert(path, index + 1, value);
        }

        private boolean isNamedWildcard(String key) {
            return key.indexOf('{') != -1 && key.indexOf('}') != -1;
        }

        private String namedWildcard() {
            return namedWildcard;
        }

        private boolean isNamedWildcard() {
            return namedWildcard != null;
        }

        public T retrieve(String[] path, int index, Map<String, String> params) {
            if (index >= path.length)
                return null;

            String token = path[index];
            TrieNode<T> node = children.get(token);
            boolean usedWildcard;
            if (node == null) {
                node = children.get(wildcard);
                if (node == null) {
                    return null;
                }
                usedWildcard = true;
            } else {
                usedWildcard = token.equals(wildcard);
            }

            put(params, node, token);

            if (index == (path.length - 1)) {
                return node.value;
            }

            T res = node.retrieve(path, index + 1, params);
            if (res == null && !usedWildcard) {
                node = children.get(wildcard);
                if (node != null) {
                    put(params, node, token);
                    res = node.retrieve(path, index + 1, params);
                }
            }

            return res;
        }

        private void put(Map<String, String> params, TrieNode<T> node, String value) {
            if (params != null && node.isNamedWildcard()) {
                params.put(node.namedWildcard(), decoder.decode(value));
            }
        }
    }

    public void insert(String path, T value) {
        String[] strings = StringUtils.splitStringToArray(path, separator);
        if (strings.length == 0) {
            rootValue = value;
            return;
        }
        int index = 0;
        // supports initial delimiter.
        if (strings.length > 0 && strings[0].isEmpty()) {
            index = 1;
        }
        root.insert(strings, index, value);
    }

    public T retrieve(String path) {
        return retrieve(path, null);
    }

    public T retrieve(String path, Map<String, String> params) {
        if (path.length() == 0) {
            return rootValue;
        }
        String[] strings = StringUtils.splitStringToArray(path, separator);
        if (strings.length == 0) {
            return rootValue;
        }
        int index = 0;
        // supports initial delimiter.
        if (strings.length > 0 && strings[0].isEmpty()) {
            index = 1;
        }
        return root.retrieve(strings, index, params);
    }

}
