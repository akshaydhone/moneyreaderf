package ir.mahdi.mzip.rar.unpack.ppm;

public class SEE2Context {
    public static final int size = 4;
    private int count;
    private int shift;
    private int summ;

    public void init(int initVal) {
        this.shift = 3;
        this.summ = (initVal << this.shift) & 65535;
        this.count = 4;
    }

    public int getMean() {
        int retVal = this.summ >>> this.shift;
        this.summ -= retVal;
        return (retVal == 0 ? 1 : 0) + retVal;
    }

    public void update() {
        if (this.shift < 7) {
            int i = this.count - 1;
            this.count = i;
            if (i == 0) {
                this.summ += this.summ;
                int i2 = this.shift;
                this.shift = i2 + 1;
                this.count = 3 << i2;
            }
        }
        this.summ &= 65535;
        this.count &= 255;
        this.shift &= 255;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count & 255;
    }

    public int getShift() {
        return this.shift;
    }

    public void setShift(int shift) {
        this.shift = shift & 255;
    }

    public int getSumm() {
        return this.summ;
    }

    public void setSumm(int summ) {
        this.summ = 65535 & summ;
    }

    public void incSumm(int dSumm) {
        setSumm(getSumm() + dSumm);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SEE2Context[");
        buffer.append("\n  size=");
        buffer.append(4);
        buffer.append("\n  summ=");
        buffer.append(this.summ);
        buffer.append("\n  shift=");
        buffer.append(this.shift);
        buffer.append("\n  count=");
        buffer.append(this.count);
        buffer.append("\n]");
        return buffer.toString();
    }
}
