package ru.sberbank.learning.rates.networking;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Record")
public class CurrencyHistory {
    @Attribute(name = "Id")
    private String mId;

    @Attribute(name = "Date")
    private String mDate;

    @Element(name = "Nominal")
    private Double mNominal;

    @Element(name = "Value")
    private Double mValue;

    public String getId() {
        return mId;
    }

    public String getmDate() {
        return mDate;
    }

    public Double getNominal() {
        return mNominal;
    }

    public Double getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return "CurrencyHistory{" +
                "mId='" + mId + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mNominal=" + mNominal +
                ", mValue=" + mValue +
                '}';
    }
}