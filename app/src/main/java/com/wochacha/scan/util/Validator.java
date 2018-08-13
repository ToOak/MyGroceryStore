package com.wochacha.scan.util;


public final class Validator {


    /**
     * check whether be empty/null or not
     *
     * @param string
     * @return
     */
    public static boolean isEffective(String string) {
        if ((string == null) || ("".equals(string)) || (" ".equals(string))
                || ("null".equals(string)) || ("\n".equals(string)))
            return false;
        else
            return true;
    }


    public static boolean isUtf8Data(byte[] b, int index, int type) {
        int lLen = b.length, lCharCount = 0;
        for (int i = index; i < lLen && lCharCount < type; ++lCharCount) {
            byte lByte = b[i++];
            if (lByte >= 0)
                continue;
            if (lByte < (byte) 0xc0 || lByte > (byte) 0xfd)
                return false;
            int lCount = lByte > (byte) 0xfc ? 5 : lByte > (byte) 0xf8 ? 4 : lByte > (byte) 0xf0 ? 3 : lByte > (byte) 0xe0 ? 2 : 1;
            if (i + lCount > lLen)
                return false;
            for (int j = 0; j < lCount; ++j, ++i)
                if (b[i] >= (byte) 0xc0)
                    return false;
        }
        return true;

    }
}