//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011-2012, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.syncdb;

import tripleplay.util.Base90;

/**
 * Handles encoding/decoding properties to/from strings.
 */
public interface Codec<T>
{
    /** A codec for enums which encodes to/from {@link Enum#name}. */
    class EnumC<E extends Enum<E>> implements Codec<E> {
        public static <E extends Enum<E>> EnumC<E> create (Class<E> eclass) {
            return new EnumC<E>(eclass);
        }

        public EnumC (Class<E> eclass) {
            _eclass = eclass;
        }
        @Override public String encode (E value) {
            return value.name();
        }
        @Override public E decode (String data) {
            return Enum.valueOf(_eclass, data);
        }
        protected final Class<E> _eclass;
    }

    /** A codec for strings. The identity codec. */
    Codec<String> STRING = new Codec<String>() {
        public String encode (String value) {
            return value;
        }
        public String decode (String data) {
            return data;
        }
    };

    /** A codec for ints. */
    Codec<Integer> INT = new Codec<Integer>() {
        public String encode (Integer value) {
            return Base90.encodeInt(value);
        }
        public Integer decode (String data) {
            return Base90.decodeInt(data);
        }
    };

    /** A codec for int arrays. */
    Codec<int[]> INTS = new Codec<int[]>() {
        public String encode (int[] values) {
            StringBuilder buf = new StringBuilder();
            for (int value : values) {
                if (buf.length() > 0) buf.append("\t");
                buf.append(Base90.encodeInt(value));
            }
            return buf.toString();
        }
        public int[] decode (String data) {
            if (data.length() == 0) return new int[0];
            String[] encs = data.split("\t");
            int[] values = new int[encs.length];
            for (int ii = 0; ii < encs.length; ii++) values[ii] = Base90.decodeInt(encs[ii]);
            return values;
        }
    };

    /** A codec for longs. */
    Codec<Long> LONG = new Codec<Long>() {
        public String encode (Long value) {
            return Base90.encodeLong(value);
        }
        public Long decode (String data) {
            return Base90.decodeLong(data);
        }
    };

    /** A codec for booleans. Encodes to the string {@code t} or {@code f}. */
    Codec<Boolean> BOOLEAN = new Codec<Boolean>() {
        public String encode (Boolean value) {
            return value ? "t" : "f";
        }
        public Boolean decode (String data) {
            return "t".equals(data);
        }
    };

    /** Encodes the supplied value to a string. */
    String encode (T value);

    /** Decodes the supplied string into a value. */
    T decode (String data);
}
