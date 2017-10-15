package com.example.poliakov.barcodescanner;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import java.util.Date;

/**
 * Created by poliakov on 10/15/17.
 * Entity mapped to table "BARCODE_SCAN".
 */

@Entity(indexes = {
        @Index(value = "code, type, barcodeDatabaseId DESC"),
        @Index(value = "barcodeDatabaseId DESC")
})
public class BarcodeScan {

    static final int NOT_FOUND_TYPE = 0;
    static final int VALID_TYPE = 1;
    static final int PRESENT_TYPE = 2;

    @Id
    private Long id;

    @NotNull
    private java.util.Date date;

    @NotNull
    private long barcodeDatabaseId;

    @NotNull
    private String code;

    @NotNull
    private Integer type;

    @ToOne(joinProperty = "barcodeDatabaseId")
    private BarcodeDatabase barcodeDatabase;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 690448998)
    private transient BarcodeScanDao myDao;
    @Generated(hash = 1537840084)
    private transient Long barcodeDatabase__resolvedKey;

    @Generated(hash = 436828657)
    public BarcodeScan() {
    }

    public BarcodeScan(Long id) {
        this.id = id;
    }

    @Generated(hash = 1414022806)
    public BarcodeScan(Long id, @NotNull java.util.Date date,
            long barcodeDatabaseId, @NotNull String code, @NotNull Integer type) {
        this.id = id;
        this.date = date;
        this.barcodeDatabaseId = barcodeDatabaseId;
        this.code = code;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    public java.util.Date getDate() {
        return this.date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public long getBarcodeDatabaseId() {
        return this.barcodeDatabaseId;
    }

    public void setBarcodeDatabaseId(long barcodeDatabaseId) {
        this.barcodeDatabaseId = barcodeDatabaseId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1337717047)
    public BarcodeDatabase getBarcodeDatabase() {
        long __key = this.barcodeDatabaseId;
        if (barcodeDatabase__resolvedKey == null
                || !barcodeDatabase__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BarcodeDatabaseDao targetDao = daoSession.getBarcodeDatabaseDao();
            BarcodeDatabase barcodeDatabaseNew = targetDao.load(__key);
            synchronized (this) {
                barcodeDatabase = barcodeDatabaseNew;
                barcodeDatabase__resolvedKey = __key;
            }
        }
        return barcodeDatabase;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 658242331)
    public void setBarcodeDatabase(@NotNull BarcodeDatabase barcodeDatabase) {
        if (barcodeDatabase == null) {
            throw new DaoException(
                    "To-one property 'barcodeDatabaseId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.barcodeDatabase = barcodeDatabase;
            barcodeDatabaseId = barcodeDatabase.getId();
            barcodeDatabase__resolvedKey = barcodeDatabaseId;
        }
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 208371106)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBarcodeScanDao() : null;
    }


}