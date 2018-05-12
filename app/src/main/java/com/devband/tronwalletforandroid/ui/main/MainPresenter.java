package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronlib.Hosts;
import com.devband.tronlib.ServiceBuilder;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronwalletforandroid.database.model.WalletModel;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.main.adapter.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.main.to.Asset;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.protos.Protocol;

import java.net.ConnectException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> {

    private CoinMarketCapService mCoinMarketCapService;

    private AdapterDataModel<Asset> mAdapterDataModel;

    public MainPresenter(MainView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<Asset> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        mCoinMarketCapService = ServiceBuilder.createService(CoinMarketCapService.class, Hosts.COINMARKETCAP_API);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public boolean isLogin() {
        return Tron.getInstance(mContext).isLogin();
    }

    public void getMyAccountInfo() {
        Tron.getInstance(mContext).queryAccount(Tron.getInstance(mContext).getAddress())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Protocol.Account>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Protocol.Account account) {
                mAdapterDataModel.clear();
                mView.displayAccountInfo(account);

                for (String key : account.getAssetMap().keySet()) {
                    mAdapterDataModel.add(Asset.builder()
                            .name(key)
                            .balance(account.getAssetMap().get(key))
                            .build());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                // todo - error msg
                if (e instanceof ConnectException) {
                    // internet error
                }
            }
        });
    }

    public void getTronMarketInfo() {
        mCoinMarketCapService.getPrice("tronix")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CoinMarketCap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CoinMarketCap> coinMarketCaps) {
                        if (coinMarketCaps.size() > 0) {
                            mView.setTronMarketInfo(coinMarketCaps.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public boolean logout() {
        Tron.getInstance(mContext).logout();

        return true;
    }

    @Nullable
    public WalletModel getLoginWallet() {
        return Tron.getInstance(mContext).getLoginWallet();
    }

    public boolean renameWallet(@NonNull String walletName) {
        return Tron.getInstance(mContext).renameWallet(walletName);
    }

    public void createWallet(@NonNull String nickname) {
        Tron.getInstance(mContext).createWallet(nickname);
        mView.successCreateWallet();
    }

    public List<WalletModel> getWalletList() {
        return Tron.getInstance(mContext).getWalletList();
    }

    public void changeLoginWallet(@NonNull WalletModel walletModel) {
        Tron.getInstance(mContext).changeLoginWallet(walletModel);
    }

    public void importWallet(@NonNull String nickname, @NonNull String privateKey) {
        int result = Tron.getInstance(mContext).importWallet(nickname, privateKey);

        if (result == Tron.SUCCESS) {
            mView.successImportWallet();
        } else if (result == Tron.ERROR_EXIST_WALLET) {
            mView.duplicatedWallet();
        } else if (result == Tron.ERROR_PRIVATE_KEY) {
            mView.failCreateWallet();
        }
    }
}
