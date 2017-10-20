package com.youshibi.app.data.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

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

    private Integer latestReadSection;//最后一次阅读的章节

    private String latestReadSectionId;//最后一次阅读的章节的id

    private int latestReadPage;//最后一次阅读章节的页码

    private boolean hasUpdate;//是否有新的更新

    private Integer sectionCount;

    private int sort; //保存自定义排序的顺序

    private long createTimestamp;

    private long updateTimestamp;

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


    @Generated(hash = 710856636)
    public BookTb(String id, String name, String coverUrl, String describe, String author,
            boolean isFinished, int readNumber, long latestReadTimestamp,
            Integer latestReadSection, String latestReadSectionId, int latestReadPage,
            boolean hasUpdate, Integer sectionCount, int sort, long createTimestamp,
            long updateTimestamp) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
        this.describe = describe;
        this.author = author;
        this.isFinished = isFinished;
        this.readNumber = readNumber;
        this.latestReadTimestamp = latestReadTimestamp;
        this.latestReadSection = latestReadSection;
        this.latestReadSectionId = latestReadSectionId;
        this.latestReadPage = latestReadPage;
        this.hasUpdate = hasUpdate;
        this.sectionCount = sectionCount;
        this.sort = sort;
        this.createTimestamp = createTimestamp;
        this.updateTimestamp = updateTimestamp;
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

    public Integer getLatestReadSection() {
        return this.latestReadSection;
    }

    public void setLatestReadSection(Integer latestReadSection) {
        this.latestReadSection = latestReadSection;
    }

    public String getLatestReadSectionId() {
        return this.latestReadSectionId;
    }

    public void setLatestReadSectionId(String latestReadSectionId) {
        this.latestReadSectionId = latestReadSectionId;
    }

    public int getLatestReadPage() {
        return this.latestReadPage;
    }

    public void setLatestReadPage(int latestReadPage) {
        this.latestReadPage = latestReadPage;
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

    public boolean getHasUpdate() {
        return this.hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
        dest.writeValue(this.latestReadSection);
        dest.writeString(this.latestReadSectionId);
        dest.writeInt(this.latestReadPage);
        dest.writeByte(this.hasUpdate ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sort);
    }

    public Integer getSectionCount() {
        return this.sectionCount;
    }

    public void setSectionCount(Integer sectionCount) {
        this.sectionCount = sectionCount;
    }

    public long getCreateTimestamp() {
        return this.createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public long getUpdateTimestamp() {
        return this.updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
        this.latestReadSection = (Integer) in.readValue(Integer.class.getClassLoader());
        this.latestReadSectionId = in.readString();
        this.latestReadPage = in.readInt();
        this.hasUpdate = in.readByte() != 0;
        this.sort = in.readInt();
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
