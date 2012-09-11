package com.omegapoint.core.util;


public class Base91 {

        public static final byte[] ENCODING_TABLE;
        private static final byte[] DECODING_TABLE;
        private static final int BASE;

        static {
            String ts = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&()*+,./:;<=>?@[]^_`{|}~\" ";
            ENCODING_TABLE = ts.getBytes();
            BASE = ENCODING_TABLE.length;

            DECODING_TABLE = new byte[256];
            for (int i = 0; i < 256; ++i)
                DECODING_TABLE[i] = -1;

            for (int i = 0; i < BASE; ++i)
                DECODING_TABLE[ENCODING_TABLE[i]] = (byte) i;
        }

        public static byte[] encodeToBytes(int[] data) {
            byte[] output = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                output[i] = ENCODING_TABLE[data[i]];
            }

            return output;
        }

        public static int[] decodeFromBytes(byte[] data) {
            int[] output = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                output[i] = DECODING_TABLE[data[i]];
            }
            return output;
        }
}
