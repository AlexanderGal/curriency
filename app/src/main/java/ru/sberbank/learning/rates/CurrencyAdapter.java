package ru.sberbank.learning.rates;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.List;

import ru.sberbank.learning.rates.networking.Currency;
import ru.sberbank.learning.rates.storage.CurrenciesStorage;

public class CurrencyAdapter extends BaseAdapter {
    private static int viewCount = 0;
    List<Currency> list;

    public CurrencyAdapter(CurrenciesStorage storage) {
        this.list = storage.getLoadedList().getCurrencies();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Currency getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            Log.e("-------VIEW COUNT"," Views : " + ++viewCount);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.rate_adapter_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(
                    (TextView) view.findViewById(R.id.currency_name),
                    (TextView) view.findViewById(R.id.currency_num_code),
                    (TextView) view.findViewById(R.id.currency_char_code),
                    (TextView) view.findViewById(R.id.currency_nominal),
                    (TextView) view.findViewById(R.id.currency_value)
            );
            view.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Currency currentCurrency = getItem(position);
        holder.setData(
                currentCurrency.getName(),
                currentCurrency.getNumCode(),
                currentCurrency.getCharCode(),
                currentCurrency.getNominal(),
                currentCurrency.getValue());

        return view;
    }

    private class ViewHolder {
        TextView mName;
        TextView mNumbCode;
        TextView mCharCode;
        TextView mNominal;
        TextView mValue;

        public ViewHolder(TextView mName, TextView mNumbCode, TextView mCharCode, TextView mNominal, TextView mValue) {
            this.mName = mName;
            this.mNumbCode = mNumbCode;
            this.mCharCode = mCharCode;
            this.mNominal = mNominal;
            this.mValue = mValue;
        }

        public void setData(String name, int numbCode, String charCode, Double nominal, Double value) {
            mName.setText(name);
            mNumbCode.setText("(" + numbCode + ")");
            mCharCode.setText(charCode);
            mNominal.setText(String.format("%.0f", nominal));
            mValue.setText(String.format("%.2f", value));
        }
    }
}
