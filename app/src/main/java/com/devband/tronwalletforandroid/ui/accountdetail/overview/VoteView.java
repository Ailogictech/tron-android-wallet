package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface VoteView extends IView {

    void finishLoading(long totalVotes, long total);
    void showLoadingDialog();
    void showServerError();
}
