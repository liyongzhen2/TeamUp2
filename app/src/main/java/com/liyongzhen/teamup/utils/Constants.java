package com.liyongzhen.teamup.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 8/21/2017.
 */

public class Constants {

    public static String[][] coinsArr = {
//            {"ETH_GNT", "ETH", "Ethereum", "Golem"},
//            {"BTC_BELA", "BTC", "Bitcoin", "Belacoin"},
//            {"BTC_GAME", "BTC", "Bitcoin", "GameCredits"},
            {"USDT_ETC", "USDT", "USDT", "Ethereum Classic"},
//            {"BTC_GRC", "BTC", "Bitcoin", "Gridcoin Research"},
//            {"BTC_SBD", "BTC", "Bitcoin", "Steem Dollars"},
//            {"XMR_ZEC", "XMR", "Monero", "Zcash"},
//            {"XMR_BLK", "XMR", "Monero", "BlackCoin"},
//            {"BTC_REP","BTC", "Bitcoin", "Augur"},
            {"USDT_ZEC", "USDT", "USDT", "Zcash"},
//            {"BTC_LBC", "BTC", "Bitcoin", "LBRY Credits"},
//            {"BTC_BCY", "BTC", "Bitcoin", "BitCrystaks"},
//            {"BTC_VTC", "BTC", "Bitcoin", "Vertcoin"},
//            {"BTC_RIC", "BTC", "Bitcoin", "Riecoin"},
//            {"BTC_FCT", "BTC", "Bitcoin", "Factom"},
//            {"BTC_POT", "BTC", "Bitcoin", "PotCoin"},
//            {"XMR_BCN", "USDT", "USDT", "Bytecoin"},
//            {"BTC_PPC", "BTC", "Bitcoin", "Peercoin"},
            {"USDT_NXT", "USDT", "USDT", "NXT"},
            {"USDT_BCH", "USDT", "USDT", "Bitcoin Cash"},
//            {"BTC_FLDC", "BTC", "Bitcoin", "FoldingCoin"},
//            {"BTC_GNO", "BTC", "Bitcoin", "Gnosis"},
//            {"ETH_STEEM", "ETH", "Ethereum", "STEEM"},
//            {"BTC_DASH", "BTC", "Bitcoin", "Dash"},
//            {"BTC_RADS", "BTC", "Bitcoin", "Radium"},
//            {"BTC_BCN", "BTC", "Bitcoin", "Bytecoin"},
//            {"BTC_MAID", "BTC", "Bitcoin", "MaidSafeCoin"},
//            {"BTC_VRC", "BTC", "Bitcoin", "VeriCoin"},
//            {"BTC_DOGE", "BTC", "Bitcoin", "Dogecoin"},
//            {"BTC_CLAM", "BTC", "Bitcoin", "CLAMS"},
//            {"BTC_DGB", "BTC", "Bitcoin", "DigiByte"},
//            {"BTC_XVC", "BTC", "Bitcoin", "Vcash"},
//            {"XMR_DASH", "XMR", "Monero", "Dash"},
//            {"BTC_BTS", "BTC", "Bitcoin", "BitShares"},
//            {"BTC_ETH", "BTC", "Bitcoin", "Ethereum"},
//            {"BTC_NAV", "BTC", "Bitcoin", "NAVCoin"},
//            {"BTC_SYS", "BTC", "Bitcoin", "Syscoin"},
//            {"BTC_VIA", "BTC", "Bitcoin", "Viacoin"},
//            {"XMR_LTC", "XMR", "Monero", "Litecoin"},
//            {"BTC_SC", "BTC", "Bitcoin", "Siacoin"},
//            {"BTC_NOTE", "BTC", "Bitcoin", "DNotes"},
//            {"ETH_ETC", "ETH", "Ethereum", "Ethereum Classic"},
//            {"BTC_SJCX", "BTC", "Bitcoin", "Storjcoin X"},
//            {"BTC_BURST", "BTC", "Bitcoin", "Burst"},
//            {"BTC_NXC", "BTC", "Bitcoin", "Nexium"},
//            {"BTC_GNT", "BTC", "Bitcoin", "Golem"},
            {"USDT_XMR", "USDT", "USDT", "Monero"},
//            {"BTC_NAUT", "BTC", "Bitcoin", "Nautiluscoin"},
//            {"BTC_ETC", "BTC", "Bitcoin", "Ethereum Classic"},
//            {"BTC_EXP", "BTC", "Bitcoin", "Expanse"},
//            {"BTC_OMNI", "BTC", "Bitcoin", "Omni"},
//            {"BTC_XMR", "BTC", "Bitcoin", "Monero"},
//            {"BTC_ZEC", "BTC", "Bitcoin", "Zcash"},
//            {"BTC_XCP", "BTC", "Bitcoin", "Counterparty"},
            {"USDT_ETH", "USDT", "USDT", "Ethereum"},
            {"USDT_REP", "USDT", "USDT", "Augur"},
//            {"BTC_XPM", "BTC", "Bitcoin", "Primecoin"},
//            {"BTC_XEM", "BTC", "Bitcoin", "NEM"},
//            {"BTC_BTM", "BTC", "Bitcoin", "Bitmark"},
//            {"ETH_ZEC", "ETH", "Ethereum", "Zcash"},
//            {"BTC_STEEM", "BTC", "Bitcoin", "STEEM"},
//            {"BTC_XBC", "BTC", "Bitcoin", "BitcoinPlus"},
            {"USDT_STR", "USDT", "USDT", "Stellar"},
//            {"BTC_BTCD", "BTC", "Bitcoin", "BitcoinDark"},
//            {"BTC_LTC", "BTC", "Bitcoin", "Litecoin"},
//            {"BTC_DCR", "BTC", "Bitcoin", "Decred"},
//            {"BTC_BLK", "BTC", "Bitcoin", "BlackCoin"},
//            {"BTC_PINK", "BTC", "Bitcoin", "Pinkcoin"},
//            {"XMR_NXT", "XMR", "Monero", "NXT"},
//            {"BTC_NMC", "BTC", "Bitcoin", "Namecoin"},
            {"USDT_XRP", "USDT", "USDT", "Ripple"},
//            {"BTC_FLO", "BTC", "Bitcoin", "Florincoin"},
//            {"BTC_EMC2", "BTC", "Bitcoin", "Einsteinium"},
//            {"ETH_REP", "ETH", "Ethereum", "Augur"},
//            {"XMR_MAID", "XMR", "Monero", "MaidSafeCoin"},
//            {"BTC_XRP", "BTC", "Bitcoin", "Ripple"},
//            {"BTC_NEOS", "BTC", "Bitcoin", "Neoscoin"},
//            {"XMR_BTCD", "XMR", "Monero", "BitcoinDark"},
//            {"BTC_STR", "BTC", "Bitcoin", "Stellar"},
            {"USDT_DASH", "USDT", "USDT", "Dash"},
//            {"BTC_ARDR", "BTC", "Bitcoin", "Ardor"},
//            {"ETH_BCH", "ETH", "Ethereum", "Bitcoin Cash"},
//            {"BTC_LSK", "BTC", "Bitcoin", "Lisk"},
            {"USDT_BTC", "USDT", "USDT", "Bitcoin"},
//            {"ETH_GNO", "ETH", "Ethereum", "Gnosis"},
//            {"BTC_NXT", "BTC", "Bitcoin", "NXT"},
//            {"BTC_STRAT", "BTC", "Bitcoin", "Stratis"},
//            {"ETH_LSK", "ETH", "Ethereum", "Lisk"},
//            {"BTC_AMP", "BTC", "Bitcoin", "Synereo AMP"},
//            {"BTC_BCH", "BTC", "Bitcoin", "Bitcoin Cash"},
//            {"BTC_HUC", "BTC", "Bitcoin", "Huntercoin"},
            {"USDT_LTC", "USDT", "USDT", "Litecoin"},
//            {"BTC_PASC", "BTC", "Bitcoin", "PascalCoin"}
    };

    public static String EXTRA_COINPAIR = "coinpair";
    public static String EXTRA_COININFO = "coininfo";
    public static String EXTRA_POSTTYPE = "coinpair";
    public static String EXTRA_USERID = "userid";
    public static String EXTRA_ALARMTYPE = "alarmtype";
    public static String EXTRA_ALARMID = "alarmid";


    public static String TICKER_BROADCAST = "ticker_broadcast";

    public static String PREF_LOGINSTATE = "login_state";
    public static String PREF_ALRAMLIST = "alarmlist";

    public static final int IMAGE_MAX_SIZE = 800;

    public static final String DIR_LOCAL_IMAGE = Environment.getExternalStorageDirectory() + File.separator + "CryptoTweets";
    public static final String DIR_LOCAL_IMAGE_TEMP = DIR_LOCAL_IMAGE + File.separator + "temp";
}
