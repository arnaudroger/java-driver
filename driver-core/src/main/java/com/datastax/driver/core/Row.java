/*
 *      Copyright (C) 2012-2015 DataStax Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.datastax.driver.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.*;

import com.google.common.reflect.TypeToken;

import com.datastax.driver.core.exceptions.InvalidTypeException;

/**
 * A CQL Row returned in a {@link ResultSet}.
 * <p>
 * The values of a CQL Row can be retrieve by either index (index starts at 0)
 * or name. When setting them by name, names follow the case insensitivity
 * rules explained in {@link ColumnDefinitions}.
 */
public interface Row extends GettableData {
    // Note that we re-include all the methods of GettableData just for the sake of better javadoc

    /**
     * Returns the columns contained in this Row.
     *
     * @return the columns contained in this Row.
     */
    public ColumnDefinitions getColumnDefinitions();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNull(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNull(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBool(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBool(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimestamp(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimestamp(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getDate(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getDate(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTime(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public long getTime(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getBytesUnsafe(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getBytesUnsafe(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getBytes(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer getBytes(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getVarint(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger getVarint(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getDecimal(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getDecimal(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getUUID(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getUUID(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public InetAddress getInet(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public InetAddress getInet(String name);

    /**
     * Returns the {@code i}th value of this row as a {@link Token}.
     * <p>
     * {@link #getPartitionKeyToken()} should generally be preferred to this method (unless the
     * token column is aliased).
     *
     * @param i the index ({@code 0 <= i < size()}) of the column to retrieve.
     * @return the value of the {@code i}th column in this row as an Token.
     *
     * @throws IndexOutOfBoundsException if {@code i < 0 || i >= this.columns().size()}.
     * @throws InvalidTypeException if column {@code i} is not of the type of token values
     * for this cluster (this depends on the configured partitioner).
     */
    public Token getToken(int i);

    /**
     * Returns the value of column {@code name} as a {@link Token}.
     * <p>
     * {@link #getPartitionKeyToken()} should generally be preferred to this method (unless the
     * token column is aliased).
     *
     * @param name the name of the column to retrieve.
     * @return the value of column {@code name} as a Token.
     *
     * @throws IllegalArgumentException if {@code name} is not part of the
     * ResultSet this row is part of, i.e. if {@code !this.columns().names().contains(name)}.
     * @throws InvalidTypeException if column {@code name} is not of the type of token values
     * for this cluster (this depends on the configured partitioner).
     */
    public Token getToken(String name);

    /**
     * Returns the value of the first column containing a {@link Token}.
     * <p>
     * This method is a shorthand for queries returning a single token in an unaliased
     * column. It will look for the first name matching {@code token(...)}:
     * <pre>
     * {@code
     * ResultSet rs = session.execute("SELECT token(k) FROM my_table WHERE k = 1");
     * Token token = rs.one().getPartitionKeyToken(); // retrieves token(k)
     * }
     * </pre>
     * If that doesn't work for you (for example, if you're using an alias), use
     * {@link #getToken(int)} or {@link #getToken(String)}.
     *
     * @return the value of column {@code name} as a Token.
     *
     * @throws IllegalStateException if no column named {@code token(...)} exists in this
     * ResultSet.
     * @throws InvalidTypeException if the first column named {@code token(...)} is not of
     * the type of token values for this cluster (this depends on the configured partitioner).
     */
    public Token getPartitionKeyToken();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getList(int i, Class<T> elementsClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getList(int i, TypeToken<T> elementsType);


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getList(String name, Class<T> elementsClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getList(String name, TypeToken<T> elementsType);


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Set<T> getSet(int i, Class<T> elementsClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Set<T> getSet(int i, TypeToken<T> elementsType);


    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Set<T> getSet(String name, Class<T> elementsClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Set<T> getSet(String name, TypeToken<T> elementsType);

    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> getMap(int i, Class<K> keysClass, Class<V> valuesClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> getMap(int i, TypeToken<K> keysType, TypeToken<V> valuesType);

    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> getMap(String name, Class<K> keysClass, Class<V> valuesClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> getMap(String name, TypeToken<K> keysType, TypeToken<V> valuesType);

    /**
     * {@inheritDoc}
     */
    @Override
    public UDTValue getUDTValue(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public UDTValue getUDTValue(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public TupleValue getTupleValue(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public TupleValue getTupleValue(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(String name);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(int i, Class<T> targetClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(int i, TypeToken<T> targetType);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(int i, TypeCodec<T> codec);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(String name, Class<T> targetClass);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(String name, TypeToken<T> targetType);

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(String name, TypeCodec<T> codec);
}
