package ir.mahdi.mzip.rar.unpack.ppm;

import android.support.v4.media.session.PlaybackStateCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.IOException;

public class RangeCoder {
    public static final int BOT = 32768;
    public static final int TOP = 16777216;
    private static final long uintMask = 4294967295L;
    private long code;
    private long low;
    private long range;
    private final SubRange subRange = new SubRange();
    private Unpack unpackRead;

    public static class SubRange {
        private long highCount;
        private long lowCount;
        private long scale;

        public long getHighCount() {
            return this.highCount;
        }

        public void setHighCount(long highCount) {
            this.highCount = 4294967295L & highCount;
        }

        public long getLowCount() {
            return this.lowCount & 4294967295L;
        }

        public void setLowCount(long lowCount) {
            this.lowCount = 4294967295L & lowCount;
        }

        public long getScale() {
            return this.scale;
        }

        public void setScale(long scale) {
            this.scale = 4294967295L & scale;
        }

        public void incScale(int dScale) {
            setScale(getScale() + ((long) dScale));
        }

        public String toString() {
            StringBuilder buffer = new StringBuilder();
            buffer.append("SubRange[");
            buffer.append("\n  lowCount=");
            buffer.append(this.lowCount);
            buffer.append("\n  highCount=");
            buffer.append(this.highCount);
            buffer.append("\n  scale=");
            buffer.append(this.scale);
            buffer.append("]");
            return buffer.toString();
        }
    }

    public SubRange getSubRange() {
        return this.subRange;
    }

    public void initDecoder(Unpack unpackRead) throws IOException, RarException {
        this.unpackRead = unpackRead;
        this.code = 0;
        this.low = 0;
        this.range = 4294967295L;
        for (int i = 0; i < 4; i++) {
            this.code = ((this.code << 8) | ((long) getChar())) & 4294967295L;
        }
    }

    public int getCurrentCount() {
        this.range = (this.range / this.subRange.getScale()) & 4294967295L;
        return (int) ((this.code - this.low) / this.range);
    }

    public long getCurrentShiftCount(int SHIFT) {
        this.range >>>= SHIFT;
        return ((this.code - this.low) / this.range) & 4294967295L;
    }

    public void decode() {
        this.low = (this.low + (this.range * this.subRange.getLowCount())) & 4294967295L;
        this.range = (this.range * (this.subRange.getHighCount() - this.subRange.getLowCount())) & 4294967295L;
    }

    private int getChar() throws IOException, RarException {
        return this.unpackRead.getChar();
    }

    public void ariDecNormalize() throws IOException, RarException {
        boolean c2 = false;
        while (true) {
            if ((this.low ^ (this.low + this.range)) >= 16777216) {
                c2 = this.range < PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID;
                if (!c2) {
                    return;
                }
            }
            if (c2) {
                this.range = ((-this.low) & 32767) & 4294967295L;
                c2 = false;
            }
            this.code = ((this.code << 8) | ((long) getChar())) & 4294967295L;
            this.range = (this.range << 8) & 4294967295L;
            this.low = (this.low << 8) & 4294967295L;
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("RangeCoder[");
        buffer.append("\n  low=");
        buffer.append(this.low);
        buffer.append("\n  code=");
        buffer.append(this.code);
        buffer.append("\n  range=");
        buffer.append(this.range);
        buffer.append("\n  subrange=");
        buffer.append(this.subRange);
        buffer.append("]");
        return buffer.toString();
    }
}
