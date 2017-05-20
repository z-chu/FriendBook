package com.youshibi.app.mvp;

import android.content.Context;

/**
 * Created by zchu on 16-11-17.
 * The root view interface for every mvp view
 */

public interface MvpView {

    void showToast(String message);

    Context provideContext();

}
