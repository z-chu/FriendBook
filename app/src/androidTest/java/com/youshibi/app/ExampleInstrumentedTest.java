package com.youshibi.app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.youshibi.app.data.bean.BookRt;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.data.net.RequestClient;
import com.youshibi.app.data.net.RequestSubscriber;
import com.zchu.log.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.youshibi.app", appContext.getPackageName());
    }


    @Test
    public void getBookList() {
        RequestClient.getServerAPI().getBookList(1, 20)
                .subscribe(new RequestSubscriber<DataList<BookRt>>() {
                    @Override
                    public void onSuccess(DataList<BookRt> data) {
                        Logger.e(data);
                        Logger.e(data.DataList);
                    }

                    @Override
                    public void onResultError(int code, String msg) {

                    }
                });
    }
}
