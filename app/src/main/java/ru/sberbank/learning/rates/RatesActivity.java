package ru.sberbank.learning.rates;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import ru.sberbank.learning.rates.adapters.CurrencyAdapter;
import ru.sberbank.learning.rates.networking.CurrenciHistoryList;
import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.networking.CurrencyHistory;
import ru.sberbank.learning.rates.storage.CurrenciesStorage;

public class RatesActivity extends Activity {
    private static final String CBR_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final String LOADING = "Loading...";
    public static final String CHARCODE = "CHARCODE";
    public static final String NOMINAL = "NOMINAL";
    public static final String VALUE = "VALUE";

    private ListView mRateListView;
    private CurrencyAdapter mCurrencyAdapter;
    private CurrenciesStorage mCurrenciesStorage;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        mRateListView = (ListView) findViewById(R.id.currency_list);
        mCurrenciesStorage = ((CurrencyApplication) getApplication()).getmStorage();

        if (!mCurrenciesStorage.isReady()) {
            showLoadingStub();
            CurrencyLoaderTask currentLoaderTask = new CurrencyLoaderTask(this, mCurrenciesStorage);
            currentLoaderTask.execute();
        } else {
            showCurrencyList();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void showLoadingStub() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(LOADING);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void showCurrencyList() {
        mCurrencyAdapter = new CurrencyAdapter(mCurrenciesStorage);
        mRateListView.setAdapter(mCurrencyAdapter);
        addOnItemClickListner(mRateListView);

        if (mProgressDialog != null) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

    private void addOnItemClickListner(ListView mRateListView) {
        mRateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RatesActivity.this, CurrencyCalculateActivity.class);
                CharSequence selectedCharCode = ((TextView) view.findViewById(R.id.currency_char_code)).getText();
                Double value = Double.parseDouble(((TextView) view.findViewById(R.id.currency_value)).getText().toString());
                Integer nominal = Integer.parseInt(((TextView) view.findViewById(R.id.currency_nominal)).getText().toString());
                intent.putExtra(CHARCODE, selectedCharCode.toString());
                intent.putExtra(VALUE, value);
                intent.putExtra(NOMINAL, nominal);
                startActivity(intent);

            }
        });
    }

    private static class CurrencyLoaderTask extends AsyncTask<Void, Void, CurrenciesList> {
        private WeakReference<CurrenciesStorage> weakReferenceStorage;
        private WeakReference<Context> weakReferenceContext;

        CurrencyLoaderTask(Context con, CurrenciesStorage storage) {
            weakReferenceStorage = new WeakReference<>(storage);
            weakReferenceContext = new WeakReference<>(con);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected CurrenciesList doInBackground(Void... params) {
            if (weakReferenceStorage.get().isReady()) {
                return null;
            }
            try (InputStream is = new URL(CBR_URL).openStream();
                    InputStream da = new URL("https://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=02/03/2017&date_req2=07/03/2017&VAL_NM_RQ=R01235").openStream();) {
                Log.e("ch",CurrenciHistoryList.readFromStream(da).getCurrencies().toString());
                return CurrenciesList.readFromStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CurrenciesList currenciesList) {
            CurrenciesStorage cStorage = weakReferenceStorage.get();
            if (cStorage == null || weakReferenceContext == null || currenciesList == null) return;
            cStorage.setLoadedList(currenciesList);
            ((RatesActivity) weakReferenceContext.get()).showCurrencyList();
        }
    }
}
