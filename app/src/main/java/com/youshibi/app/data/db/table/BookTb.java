package com.youshibi.app.data.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Chu on 2017/5/29.
 */

@Entity(
        active = true
)
public class BookTb implements Parcelable {

    @Id
    private String id;

    private String name;

    private String coverUrl;

    private String describe;

    private String author;

    private boolean isFinished;

    private int readNumber; //阅读次数

    private long latestReadTimestamp;//最后一次的阅读时间

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1075074410)
    private transient BookTbDao myDao;

    @Generated(hash = 847588366)
    public BookTb(String id, String name, String coverUrl, String describe, String author,
            boolean isFinished, int readNumber, long latestReadTimestamp) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
        this.describe = describe;
        this.author = author;
        this.isFinished = isFinished;
        this.readNumber = readNumber;
        this.latestReadTimestamp = latestReadTimestamp;
    }

    @Generated(hash = 1469509304)
    public BookTb() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean getIsFinished() {
        return this.isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.coverUrl);
        dest.writeString(this.describe);
        dest.writeString(this.author);
        dest.writeByte(this.isFinished ? (byte) 1 : (byte) 0);
        dest.writeInt(this.readNumber);
        dest.writeLong(this.latestReadTimestamp);
    }

    public int getReadNumber() {
        return this.readNumber;
    }

    public void setReadNumber(int readNumber) {
        this.readNumber = readNumber;
    }

    public long getLatestReadTimestamp() {
        return this.latestReadTimestamp;
    }

    public void setLatestReadTimestamp(long latestReadTimestamp) {
        this.latestReadTimestamp = latestReadTimestamp;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1406023060)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBookTbDao() : null;
    }

    protected BookTb(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.coverUrl = in.readString();
        this.describe = in.readString();
        this.author = in.readString();
        this.isFinished = in.readByte() != 0;
        this.readNumber = in.readInt();
        this.latestReadTimestamp = in.readLong();
    }

    public static final Creator<BookTb> CREATOR = new Creator<BookTb>() {
        @Override
        public BookTb createFromParcel(Parcel source) {
            return new BookTb(source);
        }

        @Override
        public BookTb[] newArray(int size) {
            return new BookTb[size];
        }
    };
}
