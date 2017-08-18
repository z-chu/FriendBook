package com.zchu.reader;

/**
 * Created by Chu on 2017/8/12.
 */

public abstract class BufferedReaderAdatper implements PageLoaderAdapter {


    @Override
    public abstract int getSectionCount();

    @Override
    public abstract String getSectionName(int section);
}
