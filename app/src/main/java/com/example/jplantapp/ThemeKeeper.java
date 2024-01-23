package com.example.jplantapp;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
class ThemeKeeper {

    private static int current_id = -1;
    public static int getCurrentTheme(BasicActivity a){
        if (current_id == -1) {
        current_id = a.getSharedPreferences("Theme", MODE_PRIVATE).getInt("id", 0);
        }
        current_id = current_id % themes.length; // just in case
        return themes[current_id];
    }
    private static final int[] themes = {R.style.Theme_LightGreen, R.style.Theme_LightBlue, R.style.Theme_LightOrange};

    public static void rollover(BasicActivity a) {
        current_id = (current_id + 1) % themes.length;
        SharedPreferences.Editor e = a.getSharedPreferences("Theme", MODE_PRIVATE).edit();
        e.putInt("id", current_id);
        e.apply();
    }
}
