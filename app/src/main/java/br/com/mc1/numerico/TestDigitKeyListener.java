package br.com.mc1.numerico;

import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author Rafael Pereira Ramos on 2019-10-13 <rafael.ramos@mc1.com.br>
 */
public class TestDigitKeyListener extends DigitsKeyListener {

    private boolean signal = false;
    private boolean decimal = false;

    public TestDigitKeyListener() {
    }

    @Override
    protected char[] getAcceptedChars() {
        if (signal && decimal) {
            return new char[]{'1','2','3','4', '5', '6', '7', '8', '9', '0',
                    FormatDecimalUtils.getInstance().SYSTEM_DECIMAL_SEPARATOR,'-'};
        } else if (signal) {
            return new char[]{'1','2','3','4', '5', '6', '7', '8', '9', '0', '-'};
        } else if (decimal) {
            return new char[]{'1','2','3','4', '5', '6', '7', '8', '9', '0',
                    FormatDecimalUtils.getInstance().SYSTEM_DECIMAL_SEPARATOR};
        } else {
            return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        }

        /*return new char[]{'1','2','3','4', '5', '6', '7', '8', '9', '0',
                FormatDecimalUtils.getInstance().SYSTEM_DECIMAL_SEPARATOR,'-'};*/
    }

    public void setSignal(boolean signal) {
        this.signal = signal;
    }

    public void setDecimal(boolean decimal) {
        this.decimal = decimal;
    }

    public TestDigitKeyListener(boolean sign, boolean decimal) {
        super(sign, decimal);
        this.signal = sign;
        this.decimal = decimal;
    }

    @Override
    public int getInputType() {
        return super.getInputType();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return super.filter(source, start, end, dest, dstart, dend);
    }

    @Override
    protected int lookup(KeyEvent event, Spannable content) {
        return super.lookup(event, content);
    }

    @Override
    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        return super.onKeyDown(view, content, keyCode, event);
    }

    @Override
    public boolean backspace(View view, Editable content, int keyCode, KeyEvent event) {
        return super.backspace(view, content, keyCode, event);
    }

    @Override
    public boolean forwardDelete(View view, Editable content, int keyCode, KeyEvent event) {
        return super.forwardDelete(view, content, keyCode, event);
    }

    @Override
    public boolean onKeyOther(View view, Editable content, KeyEvent event) {
        return super.onKeyOther(view, content, event);
    }

    @Override
    public boolean onKeyUp(View view, Editable content, int keyCode, KeyEvent event) {
        return super.onKeyUp(view, content, keyCode, event);
    }

    @Override
    public void clearMetaKeyState(View view, Editable content, int states) {
        super.clearMetaKeyState(view, content, states);
    }

    @Override
    public long clearMetaKeyState(long state, int which) {
        return super.clearMetaKeyState(state, which);
    }
}
