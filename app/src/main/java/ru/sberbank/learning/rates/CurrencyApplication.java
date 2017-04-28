package ru.sberbank.learning.rates;

import android.app.Application;

import ru.sberbank.learning.rates.storage.CurrenciesStorage;

public class CurrencyApplication extends Application {
    private CurrenciesStorage mStorage = new CurrenciesStorage();

    public CurrenciesStorage getmStorage() {
        return mStorage;
    }
}
