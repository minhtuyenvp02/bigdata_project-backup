package org.example;

import java.sql.Timestamp;

public class Stock {
    private Long id;
    private String sym;
    private String mc;
    private double c;
    private double f;
    private double r;
    private double lastPrice;
    private double lastVolume;
    private double lot;
    private String ot;
    private String changePc;
    private String avePrice;
    private String highPrice;
    private String lowPrice;
    private String fBVol;
    private String fBValue;
    private String fSVolume;
    private String fSValue;
    private String fRoom;
    private String g1;
    private String g2;
    private String g3;
    private String g4;
    private String g5;
    private String g6;
    private String g7;
    private String mp;
    private String CWUnderlying;
    private String CWIssuerName;
    private String CWType;
    private String CWMaturityDate;
    private String CWLastTradingDate;
    private String CWExcersisePrice;
    private String CWExerciseRatio;
    private String CWListedShare;

    public Stock(Long id, String sym, String mc, double c, double f, double r, double lastPrice, double lastVolume, double lot, String ot, String changePc, String avePrice, String highPrice, String lowPrice, String fBVol, String fBValue, String fSVolume, String fSValue, String fRoom, String g1, String g2, String g3, String g4, String g5, String g6, String g7, String mp, String CWUnderlying, String CWIssuerName, String CWType, String CWMaturityDate, String CWLastTradingDate, String CWExcersisePrice, String CWExerciseRatio, String CWListedShare, String sType, String sBenefit, String industry, Timestamp crawledTime) {
        this.id = id;
        this.sym = sym;
        this.mc = mc;
        this.c = c;
        this.f = f;
        this.r = r;
        this.lastPrice = lastPrice;
        this.lastVolume = lastVolume;
        this.lot = lot;
        this.ot = ot;
        this.changePc = changePc;
        this.avePrice = avePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.fBVol = fBVol;
        this.fBValue = fBValue;
        this.fSVolume = fSVolume;
        this.fSValue = fSValue;
        this.fRoom = fRoom;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
        this.g4 = g4;
        this.g5 = g5;
        this.g6 = g6;
        this.g7 = g7;
        this.mp = mp;
        this.CWUnderlying = CWUnderlying;
        this.CWIssuerName = CWIssuerName;
        this.CWType = CWType;
        this.CWMaturityDate = CWMaturityDate;
        this.CWLastTradingDate = CWLastTradingDate;
        this.CWExcersisePrice = CWExcersisePrice;
        this.CWExerciseRatio = CWExerciseRatio;
        this.CWListedShare = CWListedShare;
        this.sType = sType;
        this.sBenefit = sBenefit;
        this.industry = industry;
        this.crawledTime = crawledTime;
    }

    private String sType;
    private String sBenefit;
    private String industry;
    private Timestamp crawledTime;

}
