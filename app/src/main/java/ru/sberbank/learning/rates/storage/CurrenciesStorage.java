package ru.sberbank.learning.rates.storage;

import android.support.annotation.Nullable;

import java.util.HashMap;

import ru.sberbank.learning.rates.networking.CurrenciHistoryList;
import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.networking.Currency;


/**
 * Хранилище загруженного списка валют.
 *
 * @author Дмитрий Соколов <me@dimasokol.ru>
 */
public final class CurrenciesStorage {

    private CurrenciesList mLoadedList;
    private HashMap<String, CurrenciHistoryList> mCurrenciHistoryList;

    public synchronized boolean isReady() {
        return mLoadedList != null;
    }

    public synchronized boolean isReady(String id) {
        return mCurrenciHistoryList.get(id) != null;
    }

    public synchronized CurrenciesList getLoadedList() {
        return mLoadedList;
    }

    public synchronized HashMap<String, CurrenciHistoryList> getLoadedHistoryList() {
        return mCurrenciHistoryList;
    }

    public synchronized void setLoadedList(CurrenciesList loadedList) {
        mLoadedList = loadedList;
    }

    public synchronized void putLoadedList(CurrenciHistoryList loadedList) {
        mCurrenciHistoryList.put(loadedList.getId(), loadedList);
    }


    @Nullable
    public synchronized CurrenciHistoryList findById(@Nullable String id) {
        if (mCurrenciHistoryList != null && id != null)
            return mCurrenciHistoryList.get(id);
        return null;
    }

    @Nullable
    public synchronized Currency findByCode(@Nullable String code) {
        if (mLoadedList != null && code != null) {
            for (Currency currency : mLoadedList.getCurrencies()) {
                if (currency.getCharCode().equals(code)) {
                    return currency;
                }
            }
        }

        return null;
    }
}
