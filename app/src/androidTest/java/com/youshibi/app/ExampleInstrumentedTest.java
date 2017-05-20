package com.youshibi.app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.DataList;
import com.zchu.log.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscriber;

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
        DataManager
                .getInstance()
                .getBookList(1, 20)
                .subscribe(new Subscriber<DataList<Book>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e);
                        throw new RuntimeException(e);
                    }

                    @Override
                    public void onNext(DataList<Book> bookDataList) {
                        Logger.e(bookDataList);
                    }
                });
    }
}
