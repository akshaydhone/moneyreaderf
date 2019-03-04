package ir.mahdi.mzip.rar.rarfile;

public enum HostSystem {
    msdos((byte) 0),
    os2((byte) 1),
    win32((byte) 2),
    unix((byte) 3),
    macos((byte) 4),
    beos((byte) 5);
    
    private byte hostByte;

    private HostSystem(byte hostByte) {
        this.hostByte = hostByte;
    }

    public static HostSystem findHostSystem(byte hostByte) {
        if (msdos.equals(hostByte)) {
            return msdos;
        }
        if (os2.equals(hostByte)) {
            return os2;
        }
        if (win32.equals(hostByte)) {
            return win32;
        }
        if (unix.equals(hostByte)) {
            return unix;
        }
        if (macos.equals(hostByte)) {
            return macos;
        }
        if (beos.equals(hostByte)) {
            return beos;
        }
        return null;
    }

    public boolean equals(byte hostByte) {
        return this.hostByte == hostByte;
    }

    public byte getHostByte() {
        return this.hostByte;
    }
}
