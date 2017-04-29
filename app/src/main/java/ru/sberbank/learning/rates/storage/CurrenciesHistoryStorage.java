package ru.sberbank.learning.rates.storage;

import android.support.annotation.Nullable;

import ru.sberbank.learning.rates.networking.CurrenciHistoryList;
import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.networking.Currency;
import ru.sberbank.learning.rates.networking.CurrencyHistory;

public final class CurrenciesHistoryStorage {

    private CurrenciHistoryList mLoadedList;

    public synchronized boolean isReady() {
        return mLoadedList != null;
    }

    public synchronized CurrenciHistoryList getLoadedList() {
        return mLoadedList;
    }

    public synchronized void setLoadedList(CurrenciHistoryList loadedList) {
        mLoadedList = loadedList;
    }

    @Nullable
    public synchronized CurrencyHistory findById(@Nullable String id) {
        if (mLoadedList != null && id != null) {
            for (CurrencyHistory currency : mLoadedList.getCurrencies()) {
                if (currency.getId().equals(id)) {
                    return currency;
                }
            }
        }

        return null;
    }
}
