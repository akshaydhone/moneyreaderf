package ir.mahdi.mzip.rar.rarfile;

public enum SubBlockHeaderType {
    EA_HEAD((short) 256),
    UO_HEAD((short) 257),
    MAC_HEAD((short) 258),
    BEEA_HEAD((short) 259),
    NTACL_HEAD((short) 260),
    STREAM_HEAD((short) 261);
    
    private short subblocktype;

    private SubBlockHeaderType(short subblocktype) {
        this.subblocktype = subblocktype;
    }

    public static SubBlockHeaderType findSubblockHeaderType(short subType) {
        if (EA_HEAD.equals(subType)) {
            return EA_HEAD;
        }
        if (UO_HEAD.equals(subType)) {
            return UO_HEAD;
        }
        if (MAC_HEAD.equals(subType)) {
            return MAC_HEAD;
        }
        if (BEEA_HEAD.equals(subType)) {
            return BEEA_HEAD;
        }
        if (NTACL_HEAD.equals(subType)) {
            return NTACL_HEAD;
        }
        if (STREAM_HEAD.equals(subType)) {
            return STREAM_HEAD;
        }
        return null;
    }

    public boolean equals(short subblocktype) {
        return this.subblocktype == subblocktype;
    }

    public short getSubblocktype() {
        return this.subblocktype;
    }
}
