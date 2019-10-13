package br.com.mc1.numerico;

import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.widget.EditText;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;



/**
 * @author Rafael Pereira Ramos on 2019-08-19 <rafael.ramos@mc1.com.br>
 */
public class NumericBoxOnTextChangedHandler {

    private String maskType = "";
    private int maskValue = -1;
    private boolean isFloat = false;
    private TestDigitKeyListener keyListener;

    public NumericBoxOnTextChangedHandler() {
    }

    public void setMaskType(String maskType) {
        this.maskType = maskType;
    }

    public void setMaskValue(int maskValue) {
        this.maskValue = maskValue;
    }

    public void setFloat(boolean aFloat) {
        isFloat = aFloat;
    }

    public void formatAndSet(CharSequence s, EditText input, TestDigitKeyListener keyListener) {
        String value = s.toString();

        this.keyListener = keyListener;

        value = handleSignal(value);
        if (isFloat) {
            value = handleDecimalSeparator(value);
        }

        switch (maskType) {
            case "Decimal":
            case "Percentage":
                formatAndSetDecimal(value, input);
                break;
            case "Currency":
                formatAndSetCurrency(value, input);
                break;
                default:
                    formatAndSetInteger(value, input);
        }
    }

    private void formatAndSetCurrency(String value, EditText input) {
        if (value != null && !value.isEmpty()) {
            Number numberValue;

            if (isFloat) {

                DecimalFormat formatter = FormatDecimalUtils.getInstance().getDecimalFormat(maskValue);

                numberValue = Double.valueOf(FormatDecimalUtils.getInstance().clearDecimalCharacters(value));

                BigDecimal parsed = new BigDecimal(numberValue.doubleValue())
                        .setScale(maskValue, BigDecimal.ROUND_FLOOR)
                        .divide(new BigDecimal(Math.pow(10, maskValue)), BigDecimal.ROUND_FLOOR);

                value = formatter.format(parsed);

                input.setText(value);
            } else {
                formatAndSetInteger(value, input);
            }

            input.setSelection(value.length());
        }
    }

    private void formatAndSetInteger(String value, EditText input) {
        if (value != null && !value.isEmpty()) {
            Number numberValue;
            NumberFormat formatter = NumberFormat.getIntegerInstance();

            if (value.equals("-")) {
                moveCursor(value, input);
                return;
            }

            try {
                String intVal = value.replace(FormatDecimalUtils.getInstance().SYSTEM_DECIMAL_SEPARATOR.toString(),
                        "");
                intVal = FormatDecimalUtils.getInstance().removeGroupSeparators(intVal);
                numberValue = Integer.valueOf(intVal);
                value = formatter.format(numberValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                value = "0";
            }

            moveCursor(value, input);
        }
    }

    private void moveCursor(String value, EditText input) {
        input.setText(value);
        input.setSelection(value.length());
    }

    private void formatAndSetDecimal(String value, EditText input) {
        if (value != null && !value.isEmpty()) {

            if (value.equals("-")) {
                moveCursor(value, input);
                return;
            }

            if (isFloat) {
                value = FormatDecimalUtils.getInstance().removeGroupSeparators(value);
                value = FormatDecimalUtils.getInstance().wrap(value, maskValue);

                try {
                    value = FormatDecimalUtils.getInstance().dynamicMask(value);
                } catch (ParseException | NumberFormatException e) {

                    value = "0.0";
                }

                moveCursor(value, input);
            } else {
                formatAndSetInteger(value, input);
            }
        }
    }

    private String handleSignal(String value) {
        if (value.contains("-")) {
            if (value.startsWith("-"))
                keyListener.setSignal(false);
            else
                return FormatDecimalUtils.getInstance().removeExtraSignal(value);
        } else {
            keyListener.setSignal(value.isEmpty());
        }

        return value;
    }

    private String handleDecimalSeparator(String value) {
        String decimalSeparator = FormatDecimalUtils.getInstance().SYSTEM_DECIMAL_SEPARATOR.toString();

        if (value.contains(decimalSeparator)) {
            int separatorAmount = value.split(
                    new StringBuilder()
                            .append("\\")
                            .append(decimalSeparator)
                            .toString()
            ).length-1;

            if (separatorAmount > 1) {
                return FormatDecimalUtils.getInstance().removeExtraDecimalSeparator(value);
            } else {
                keyListener.setDecimal(false);
            }
        } else {
            keyListener.setDecimal(true);
        }

        return value;
    }

    private void formatAndSetNoMask(String value, EditText input) {
        try {
            if (isFloat) {
                input.setText(Double.toString(Double.parseDouble(value)));
            } else {
                input.setText(Integer.toString(Integer.parseInt(value)));
            }
        } catch (Exception e) {

            if (isFloat) {
                input.setText("0.0");
            } else {
                input.setText("0");
            }
        }
    }
}
