package ir.mahdi.mzip.rar.unsigned;

public class UnsignedByte {
    public static byte longToByte(long unsignedByte1) {
        return (byte) ((int) (255 & unsignedByte1));
    }

    public static byte intToByte(int unsignedByte1) {
        return (byte) (unsignedByte1 & 255);
    }

    public static byte shortToByte(short unsignedByte1) {
        return (byte) (unsignedByte1 & 255);
    }

    public static short add(byte unsignedByte1, byte unsignedByte2) {
        return (short) (unsignedByte1 + unsignedByte2);
    }

    public static short sub(byte unsignedByte1, byte unsignedByte2) {
        return (short) (unsignedByte1 - unsignedByte2);
    }

    public static void main(String[] args) {
        System.out.println(add((byte) -2, (byte) 1));
        System.out.println(add((byte) -1, (byte) 1));
        System.out.println(add(Byte.MAX_VALUE, (byte) 1));
        System.out.println(add((byte) -1, (byte) -1));
        System.out.println(sub((byte) -2, (byte) 1));
        System.out.println(sub((byte) 0, (byte) 1));
        System.out.println(sub(Byte.MIN_VALUE, (byte) 1));
        System.out.println(1);
    }
}
