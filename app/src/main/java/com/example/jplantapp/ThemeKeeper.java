package com.example.jplantapp;

class ThemeKeeper {
    private static int current_id = 0;

    public static int getCurrentTheme(){
        return themes[current_id];
    }
    private static final int[] themes = {R.style.Theme_LightGreen, R.style.Theme_LightBlue, R.style.Theme_LightOrange};

    public static void rollover() {
        current_id = (current_id + 1) % themes.length;
    }
}
