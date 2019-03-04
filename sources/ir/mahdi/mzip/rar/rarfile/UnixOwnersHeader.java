package ir.mahdi.mzip.rar.rarfile;

import ir.mahdi.mzip.rar.io.Raw;

public class UnixOwnersHeader extends SubBlockHeader {
    private String group;
    private int groupNameSize;
    private String owner;
    private int ownerNameSize;

    public UnixOwnersHeader(SubBlockHeader sb, byte[] uoHeader) {
        super(sb);
        this.ownerNameSize = Raw.readShortLittleEndian(uoHeader, 0) & 65535;
        int pos = 0 + 2;
        this.groupNameSize = Raw.readShortLittleEndian(uoHeader, pos) & 65535;
        pos += 2;
        if (this.ownerNameSize + 4 < uoHeader.length) {
            byte[] ownerBuffer = new byte[this.ownerNameSize];
            System.arraycopy(uoHeader, pos, ownerBuffer, 0, this.ownerNameSize);
            this.owner = new String(ownerBuffer);
        }
        pos = this.ownerNameSize + 4;
        if (this.groupNameSize + pos < uoHeader.length) {
            byte[] groupBuffer = new byte[this.groupNameSize];
            System.arraycopy(uoHeader, pos, groupBuffer, 0, this.groupNameSize);
            this.group = new String(groupBuffer);
        }
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getGroupNameSize() {
        return this.groupNameSize;
    }

    public void setGroupNameSize(int groupNameSize) {
        this.groupNameSize = groupNameSize;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getOwnerNameSize() {
        return this.ownerNameSize;
    }

    public void setOwnerNameSize(int ownerNameSize) {
        this.ownerNameSize = ownerNameSize;
    }

    public void print() {
        super.print();
    }
}
