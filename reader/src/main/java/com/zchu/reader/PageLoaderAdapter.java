package com.zchu.reader;

import java.util.List;

/**
 * Created by Chu on 2017/8/12.
 */

public interface PageLoaderAdapter {

    int getPageCount(int section, PageProperty property);

    List<String> getPageLines(int section, int page, PageProperty property);

    int getSectionCount();

    String getSectionName(int section);

    boolean hasNextSection(int currentSection);

    boolean hasPreviousSection(int currentSection);


}
