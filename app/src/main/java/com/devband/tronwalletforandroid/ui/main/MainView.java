package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.ui.mvp.IView;

import org.tron.protos.Protocol;

public interface MainView extends IView {

    void displayAccountInfo(@NonNull Protocol.Account account);
}
