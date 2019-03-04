package ir.mahdi.mzip.rar.rarfile;

public class FileNameDecoder {
    public static int getChar(byte[] name, int pos) {
        return name[pos] & 255;
    }

    public static String decode(byte[] name, int encPos) {
        int decPos = 0;
        int flags = 0;
        int flagBits = 0;
        int encPos2 = encPos + 1;
        int highByte = getChar(name, encPos);
        StringBuffer buf = new StringBuffer();
        encPos = encPos2;
        while (encPos < name.length) {
            if (flagBits == 0) {
                encPos2 = encPos + 1;
                flags = getChar(name, encPos);
                flagBits = 8;
            } else {
                encPos2 = encPos;
            }
            switch (flags >> 6) {
                case 0:
                    encPos = encPos2 + 1;
                    buf.append((char) getChar(name, encPos2));
                    decPos++;
                    break;
                case 1:
                    encPos = encPos2 + 1;
                    buf.append((char) (getChar(name, encPos2) + (highByte << 8)));
                    decPos++;
                    break;
                case 2:
                    buf.append((char) ((getChar(name, encPos2 + 1) << 8) + getChar(name, encPos2)));
                    decPos++;
                    encPos = encPos2 + 2;
                    break;
                case 3:
                    encPos = encPos2 + 1;
                    int length = getChar(name, encPos2);
                    if ((length & 128) != 0) {
                        encPos2 = encPos + 1;
                        int correction = getChar(name, encPos);
                        length = (length & 127) + 2;
                        while (length > 0 && decPos < name.length) {
                            buf.append((char) ((highByte << 8) + ((getChar(name, decPos) + correction) & 255)));
                            length--;
                            decPos++;
                        }
                        encPos = encPos2;
                        break;
                    }
                    length += 2;
                    while (length > 0 && decPos < name.length) {
                        buf.append((char) getChar(name, decPos));
                        length--;
                        decPos++;
                    }
                    break;
                default:
                    encPos = encPos2;
                    break;
            }
            flags = (flags << 2) & 255;
            flagBits -= 2;
        }
        return buf.toString();
    }
}
