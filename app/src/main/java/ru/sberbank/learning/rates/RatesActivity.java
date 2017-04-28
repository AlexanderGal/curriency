package ru.sberbank.learning.rates;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.storage.CurrenciesStorage;

public class RatesActivity extends Activity {
    private static final String CBR_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final String LOADING = "Loading...";
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
            Log.e("ERROR","START LOADING");
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
        if (mProgressDialog != null) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

    private static class CurrencyLoaderTask extends AsyncTask<Void, Void, CurrenciesList> {
        private WeakReference<CurrenciesStorage> weakReferenceStorage;
        private WeakReference<Context> weakReferenceContext;

        public CurrencyLoaderTask(Context con, CurrenciesStorage storage) {
            weakReferenceStorage = new WeakReference<>(storage);
            weakReferenceContext = new WeakReference<>(con);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected CurrenciesList doInBackground(Void... params) {
            if (weakReferenceStorage.get().isReady()) {
                return null;
            }
            try (InputStream is = new URL(CBR_URL).openStream()) {
                CurrenciesList list = CurrenciesList.readFromStream(is);
                return list;
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
