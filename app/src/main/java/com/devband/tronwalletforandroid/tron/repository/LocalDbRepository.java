package com.devband.tronwalletforandroid.tron.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.WalletDao;
import com.devband.tronwalletforandroid.database.model.WalletModel;

import java.util.Calendar;
import java.util.Date;

public class LocalDbRepository implements WalletRepository {

    private WalletDao mWalletDao;

    public LocalDbRepository(@NonNull Context context) {
        mWalletDao = AppDatabase.getDatabase(context).walletDao();
    }

    @Override
    public boolean storeAddress(@NonNull WalletModel walletModel) {
        Date now = Calendar.getInstance().getTime();
        walletModel.setCreated(now);

        mWalletDao.insert(walletModel);

        return walletModel.getId() > 0;
    }

    @Nullable
    @Override
    public WalletModel loadAddress() {
        return null;
    }
}
