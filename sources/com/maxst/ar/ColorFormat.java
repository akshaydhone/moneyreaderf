package com.maxst.ar;

public enum ColorFormat {
    RGB888(1),
    YUV420sp(2),
    YUV420(3),
    YUV420_888(4),
    GRAY8(5);
    
    private int value;

    private ColorFormat(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ColorFormat fromValue(int id) {
        for (ColorFormat type : values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        return null;
    }
}
