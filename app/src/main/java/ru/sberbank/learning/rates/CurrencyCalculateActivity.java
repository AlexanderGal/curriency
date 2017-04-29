package ru.sberbank.learning.rates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

public class CurrencyCalculateActivity extends Activity {
    private TextView mCharCode;
    private TextView mValue;
    private EditText mNominal;
    private Double ammount;
    private static final String RU_CHARCODE = " RUR";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_activity_layout);
        Intent intent = getIntent();

        mCharCode = (TextView) findViewById(R.id.currency_char_code);
        mValue = (TextView) findViewById(R.id.sum_total);
        mNominal = (EditText) findViewById(R.id.ammount);
        mCharCode.setText(intent.getStringExtra(RatesActivity.CHARCODE));

        ammount = intent.getDoubleExtra(RatesActivity.VALUE, 0);

        mValue.setText(Double.toString(ammount) + RU_CHARCODE);
        mNominal.setText(Integer.toString(intent.getIntExtra(RatesActivity.NOMINAL, 0)));

        mNominal.setSelection(mNominal.getText().length());

        mNominal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mNominal.getText().length() > 0) {
                    BigDecimal nominal = new BigDecimal(mNominal.getText().toString());
                    mValue.setText((nominal.multiply(new BigDecimal(ammount))).setScale(2, BigDecimal.ROUND_CEILING).toString() + RU_CHARCODE);
                }
                return false;
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
