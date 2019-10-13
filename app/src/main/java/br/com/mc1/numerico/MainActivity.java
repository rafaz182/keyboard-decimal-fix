package br.com.mc1.numerico;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    Switch isFloat;
    TextView value;
    EditText numeric;
    EditText digits;
    Button ptBR;
    Button enUS;

    NumericBoxOnTextChangedHandler textChangedHandler = new NumericBoxOnTextChangedHandler();
    TestDigitKeyListener digitKeyListener = new TestDigitKeyListener(true, true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isFloat = findViewById(R.id.isfloat);
        value = findViewById(R.id.value);
        numeric = findViewById(R.id.numeric);
        digits = findViewById(R.id.digits);
        ptBR = findViewById(R.id.locale1);
        enUS = findViewById(R.id.locale2);

        isFloat.setChecked(false);
        isFloat.setOnCheckedChangeListener(this);

        digits.setInputType(InputType.TYPE_CLASS_NUMBER);

        numeric.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        numeric.setFilters(new InputFilter[]{new ForceLocaleDigitPatterFilter()});
        numeric.setKeyListener(digitKeyListener); // should be set after setInputType or will not work
        numeric.addTextChangedListener(this);

        ptBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFields();
                Locale.setDefault(new Locale("pt", "BR"));
            }
        });

        enUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFields();
                Locale.setDefault(new Locale("en", "US"));
            }
        });

        textChangedHandler.setFloat(false);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
        setTextWithoutTriggerTextChangedListener(new Runnable() {
            @Override
            public void run() {
                textChangedHandler.formatAndSet(charSequence, numeric, digitKeyListener);
            }
        });
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String valueStr = numeric.getText().toString();

        if (valueStr == null || valueStr.isEmpty() || valueStr.equals("-")) {
            return;
        }

        if (isFloat.isChecked()) {
            String valueWrapped = FormatDecimalUtils.getInstance().wrap(valueStr, Integer.parseInt(digits.getText().toString()));
            try {
                Double value = FormatDecimalUtils.getInstance().parseDouble(valueWrapped);
                this.value.setText(Double.toString(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Integer value = Integer.parseInt(FormatDecimalUtils.getInstance().removeGroupSeparators(valueStr));
            this.value.setText(Integer.toString(value));
        }
    }

    private void setTextWithoutTriggerTextChangedListener(Runnable task) {
        numeric.removeTextChangedListener(this);
        task.run();
        numeric.addTextChangedListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        resetFields();
        digitKeyListener.setDecimal(isChecked);
        textChangedHandler.setFloat(isChecked);

        if (isChecked) {
            //numeric.setInputType(InputType.TYPE_NULL | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            textChangedHandler.setMaskType("Decimal");
            textChangedHandler.setMaskValue(2);
        } else {
            //numeric.setInputType(InputType.TYPE_NULL | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
    }

    private void resetFields() {
        numeric.setText("");
        value.setText("");
    }
}
