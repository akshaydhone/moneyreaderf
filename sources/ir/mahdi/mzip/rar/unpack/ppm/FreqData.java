package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.io.Raw;

public class FreqData extends Pointer {
    public static final int size = 6;

    public FreqData(byte[] mem) {
        super(mem);
    }

    public FreqData init(byte[] mem) {
        this.mem = mem;
        this.pos = 0;
        return this;
    }

    public int getSummFreq() {
        return Raw.readShortLittleEndian(this.mem, this.pos) & 65535;
    }

    public void setSummFreq(int summFreq) {
        Raw.writeShortLittleEndian(this.mem, this.pos, (short) summFreq);
    }

    public void incSummFreq(int dSummFreq) {
        Raw.incShortLittleEndian(this.mem, this.pos, dSummFreq);
    }

    public int getStats() {
        return Raw.readIntLittleEndian(this.mem, this.pos + 2);
    }

    public void setStats(int state) {
        Raw.writeIntLittleEndian(this.mem, this.pos + 2, state);
    }

    public void setStats(State state) {
        setStats(state.getAddress());
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("FreqData[");
        buffer.append("\n  pos=");
        buffer.append(this.pos);
        buffer.append("\n  size=");
        buffer.append(6);
        buffer.append("\n  summFreq=");
        buffer.append(getSummFreq());
        buffer.append("\n  stats=");
        buffer.append(getStats());
        buffer.append("\n]");
        return buffer.toString();
    }
}
