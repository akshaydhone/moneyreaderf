package ir.mahdi.mzip.rar.rarfile;

public enum UnrarHeadertype {
    MainHeader((byte) 115),
    MarkHeader((byte) 114),
    FileHeader((byte) 116),
    CommHeader((byte) 117),
    AvHeader((byte) 118),
    SubHeader((byte) 119),
    ProtectHeader((byte) 120),
    SignHeader((byte) 121),
    NewSubHeader((byte) 122),
    EndArcHeader((byte) 123);
    
    private byte headerByte;

    private UnrarHeadertype(byte headerByte) {
        this.headerByte = headerByte;
    }

    public static UnrarHeadertype findType(byte headerType) {
        if (MarkHeader.equals(headerType)) {
            return MarkHeader;
        }
        if (MainHeader.equals(headerType)) {
            return MainHeader;
        }
        if (FileHeader.equals(headerType)) {
            return FileHeader;
        }
        if (EndArcHeader.equals(headerType)) {
            return EndArcHeader;
        }
        if (NewSubHeader.equals(headerType)) {
            return NewSubHeader;
        }
        if (SubHeader.equals(headerType)) {
            return SubHeader;
        }
        if (SignHeader.equals(headerType)) {
            return SignHeader;
        }
        if (ProtectHeader.equals(headerType)) {
            return ProtectHeader;
        }
        if (MarkHeader.equals(headerType)) {
            return MarkHeader;
        }
        if (MainHeader.equals(headerType)) {
            return MainHeader;
        }
        if (FileHeader.equals(headerType)) {
            return FileHeader;
        }
        if (EndArcHeader.equals(headerType)) {
            return EndArcHeader;
        }
        if (CommHeader.equals(headerType)) {
            return CommHeader;
        }
        if (AvHeader.equals(headerType)) {
            return AvHeader;
        }
        return null;
    }

    public boolean equals(byte header) {
        return this.headerByte == header;
    }

    public byte getHeaderByte() {
        return this.headerByte;
    }
}
