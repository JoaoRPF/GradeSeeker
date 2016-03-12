package com.sifanrpf.gradeseeker;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMaxMinFloat implements InputFilter {

    private float min, max;

    public InputFilterMaxMinFloat(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMaxMinFloat(String min, String max) {
        this.min = Float.parseFloat(min);
        this.max = Float.parseFloat(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
        try {
            float input = Float.parseFloat(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }     
        return "";
    }

    private boolean isInRange(float a, float b, float c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

}
