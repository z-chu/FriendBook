package com.zchu.reader;

import java.util.List;

/**
 * Created by newbiechen on 17-7-1.
 */

public class TxtPage {

    public TxtPage() {
    }

    public TxtPage(int position, List<String> lines) {
        this.position = position;
        this.lines = lines;
    }

    int position;
     List<String> lines;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
