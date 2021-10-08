package com.youngmanster.collection_kotlin.base.set;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Can save multiple the value of the map.</p>
 *
 * @author yangyan;
 */
public interface MultiValueMap<K, V> {

    /**
     * 添加Key-Value。
     *
     * @param key   key.
     * @param value value.
     */
    void add(K key, V value);

    /**
     * 添加Key-List<Value>。
     *
     * @param key    key.
     * @param values values.
     */
    void add(K key, List<V> values);

    /**
     * 设置一个Key-Value，如果这个Key存在就被替换，不存在则被添加。
     *
     * @param key   key.
     * @param value values.
     */
    void set(K key, V value);

    /**
     * 设置Key-List<Value>，如果这个Key存在就被替换，不存在则被添加。
     * @param key    key.
     * @param values values.
     * @see #set(Object, Object)
     */
    void set(K key, List<V> values);

    /**
     * 替换所有的Key-List<Value>。
     *
     * @param values values.
     */
    void set(Map<K, List<V>> values);

    /**
     * 移除某一个Key，对应的所有值也将被移除。
     *
     * @param key key.
     * @return value.
     */
    List<V> remove(K key);

    void remove(K key, int index);

    /**
     * 移除所有的值。
     * Remove all key-value.
     */
    void clear();

    /**
     * 拿到Key的集合。
     * @return Set.
     */
    Set<K> keySet();

    /**
     * 拿到所有的值的集合。
     *
     * @return List.
     */
    List<V> values();

    /**
     * 拿到某一个Key下的某一个值。
     *
     * @param key   key.
     * @param index index value.
     * @return The value.
     */
    V getValue(K key, int index);

    /**
     * 拿到某一个Key的所有值。
     *
     * @param key key.
     * @return values.
     */
    List<V> getValues(K key);

    /**
     * 拿到MultiValueMap的大小.
     *
     * @return size.
     */
    int size();

    /**
     * 判断MultiValueMap是否为null.
     *
     * @return True: empty, false: not empty.
     */
    boolean isEmpty();

    /**
     * 判断MultiValueMap是否包含某个Key.
     *
     * @param key key.
     * @return True: contain, false: none.
     */
    boolean containsKey(K key);

}