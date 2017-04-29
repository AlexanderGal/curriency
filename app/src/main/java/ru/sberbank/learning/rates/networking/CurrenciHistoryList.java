package ru.sberbank.learning.rates.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import ru.sberbank.learning.rates.utils.DateFormatUtils;


@Root(name = "ValCurs")
public class CurrenciHistoryList implements Serializable {

    @Attribute(name = "ID")
    private String mID;

    @Attribute(name = "name")
    private String mName;

    @ElementList(entry = "Record", inline = true)
    private List<CurrencyHistory> currencies;

    public String getName() {
        return mName;
    }
    public String getId(){return mID;}

    public List<CurrencyHistory> getCurrencies() {
        return currencies;
    }

    /**
     * Читает список валют из потока.
     *
     * @param stream Входной поток
     * @return Список валют
     * @throws IOException При ошибке чтения
     */
    public static CurrenciHistoryList readFromStream(@NonNull InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, Charset.forName("windows-1251")));

        RegistryMatcher m = new RegistryMatcher();
        m.bind(Double.class, new DoubleTransformer());
        Serializer serializer = new Persister(m);

        try {
            return serializer.read(CurrenciHistoryList.class, reader);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}