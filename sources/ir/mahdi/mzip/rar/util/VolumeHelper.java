package ir.mahdi.mzip.rar.util;

public class VolumeHelper {
    private VolumeHelper() {
    }

    public static String nextVolumeName(String arcName, boolean oldNumbering) {
        int len;
        StringBuilder buffer;
        char c;
        if (oldNumbering) {
            len = arcName.length();
            if (len <= 4 || arcName.charAt(len - 4) != '.') {
                return null;
            }
            buffer = new StringBuilder();
            int off = len - 3;
            buffer.append(arcName, 0, off);
            if (isDigit(arcName.charAt(off + 1)) && isDigit(arcName.charAt(off + 2))) {
                char[] ext = new char[3];
                arcName.getChars(off, len, ext, 0);
                int i = ext.length - 1;
                while (true) {
                    c = (char) (ext[i] + 1);
                    ext[i] = c;
                    if (c != ':') {
                        break;
                    }
                    ext[i] = '0';
                    i--;
                }
                buffer.append(ext);
            } else {
                buffer.append("r00");
            }
            return buffer.toString();
        }
        len = arcName.length();
        int indexR = len - 1;
        while (indexR >= 0 && !isDigit(arcName.charAt(indexR))) {
            indexR--;
        }
        int index = indexR + 1;
        int indexL = indexR - 1;
        while (indexL >= 0 && isDigit(arcName.charAt(indexL))) {
            indexL--;
        }
        if (indexL < 0) {
            return null;
        }
        indexL++;
        buffer = new StringBuilder(len);
        buffer.append(arcName, 0, indexL);
        char[] digits = new char[((indexR - indexL) + 1)];
        arcName.getChars(indexL, indexR + 1, digits, 0);
        indexR = digits.length - 1;
        while (indexR >= 0) {
            c = (char) (digits[indexR] + 1);
            digits[indexR] = c;
            if (c != ':') {
                break;
            }
            digits[indexR] = '0';
            indexR--;
        }
        if (indexR < 0) {
            buffer.append('1');
        }
        buffer.append(digits);
        buffer.append(arcName, index, len);
        return buffer.toString();
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
