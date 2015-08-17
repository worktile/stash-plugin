package com.pragbits.stash;

public enum ColorCode {

    BLUE("#2267c4"),
    PALE_BLUE("#439fe0"),
    GREEN("#2dc422"),
    PURPLE("#9055fc"),
    GRAY("#aabbcc"),
    RED("#ff0024");

    private String code;

    ColorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
