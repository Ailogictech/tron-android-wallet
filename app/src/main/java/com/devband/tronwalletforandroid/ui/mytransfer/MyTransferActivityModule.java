package com.devband.tronwalletforandroid.ui.mytransfer;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class MyTransferActivityModule {

    @Binds
    public abstract TransferView view(TransferActivity transferActivity);

    @Provides
    static TransferPresenter provideTransferPresenter(TransferView transferView, Tron tron,
            TronNetwork tronNetwork) {
        return new TransferPresenter(transferView, tron, tronNetwork, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
