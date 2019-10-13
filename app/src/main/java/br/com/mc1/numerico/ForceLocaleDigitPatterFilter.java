package br.com.mc1.numerico;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import java.text.DecimalFormatSymbols;

/**
 * @author Rafael Pereira Ramos on 2019-10-10 <rafael.ramos@mc1.com.br>
 */
public class ForceLocaleDigitPatterFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Character decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        Object returnVal = null;

        String input = source.toString();
        if (decimalSeparator.equals('.')) {
            if (input.contains(decimalSeparator.toString())) {
                returnVal = input;
            } else  if (input.contains(",")) {
                if (input.equals(",")) {
                    returnVal =  decimalSeparator.toString();
                } else {
                    returnVal =  decimalSeparator.toString().replace(",", decimalSeparator.toString());
                }
            }
        } else if (decimalSeparator.equals(',')) {
            if (input.contains(decimalSeparator.toString())) {
                returnVal =  input;
            } else  if (input.contains(".")) {
                if (input.equals(".")) {
                    returnVal =  decimalSeparator.toString();
                } else {
                    returnVal = decimalSeparator.toString().replace(".", decimalSeparator.toString());
                }
            }
        }

        if (source instanceof Spanned) {
            returnVal = returnVal != null ? returnVal.toString() : source;
            SpannableString sp = new SpannableString(returnVal.toString());
            TextUtils.copySpansFrom((Spanned) source, start, returnVal.toString().length(), null, sp, 0);
            return sp;
        }

        return input;
    }
}
