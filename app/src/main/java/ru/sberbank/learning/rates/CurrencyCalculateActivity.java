package ru.sberbank.learning.rates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

public class CurrencyCalculateActivity extends Activity {
    private static final String RU_CHARCODE = " RUR";
    private static final String CURRIENCY_HISTORY = "https://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=02/03/2017&date_req2=07/03/2017&VAL_NM_RQ=R01235";
    private Double ammount;

    private TextView mCharCode;
    private TextView mValue;
    private EditText mNominal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_activity_layout);
        init();
        addListner(mNominal);
    }

    private void init() {
        Intent intent = getIntent();
        ammount = intent.getDoubleExtra(RatesActivity.VALUE, 0);

        mCharCode = (TextView) findViewById(R.id.currency_char_code);
        mCharCode.setText(intent.getStringExtra(RatesActivity.CHARCODE));
        mValue = (TextView) findViewById(R.id.sum_total);
        String valueText = Double.toString(ammount) + RU_CHARCODE;
        mValue.setText(valueText);

        mNominal = (EditText) findViewById(R.id.ammount);
        String nominalText = Integer.toString(intent.getIntExtra(RatesActivity.NOMINAL, 0));
        mNominal.setText(nominalText);
        mNominal.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        mNominal.setSelection(mNominal.getText().length());
    }

    private void addListner(TextView view) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String result;
                if (mNominal.getText().length() > 0) {
                    BigDecimal nominal = new BigDecimal(mNominal.getText().toString());
                    result = (nominal.multiply(new BigDecimal(ammount))).setScale(2, BigDecimal.ROUND_CEILING).toString() + RU_CHARCODE;
                    mValue.setText(result);
                } else {
                    result = "0.00" + RU_CHARCODE;
                    mValue.setText(result);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCharCode = null;
        mNominal = null;
        mValue = null;
        ammount = null;
    }
}
