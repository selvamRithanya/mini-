package com.ems.util;

import java.math.BigDecimal;

public final class HtmlUtil {

    private HtmlUtil() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public static String money(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return String.format("%.2f", amount);
    }

    public static String attr(String value) {
        return escape(value);
    }
}
