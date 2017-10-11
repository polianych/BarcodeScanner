package com.example.poliakov.barcodescanner;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

/**
 * Entity mapped to table "BARCODE".
 */

@Entity(indexes = {
        @Index(value = "code, barcodeDatabaseId DESC", unique = true)
})
public class Barcode {
    @Id
    private Long id;

    private long barcodeDatabaseId;

    @ToOne(joinProperty = "barcodeDatabaseId")
    private BarcodeDatabase barcodeDatabase;

    @NotNull
    private String code;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 727224059)
    private transient BarcodeDao myDao;

    @Generated(hash = 1537840084)
    private transient Long barcodeDatabase__resolvedKey;

    @Generated(hash = 437857879)
    public Barcode() {
    }

    public Barcode(Long id) {
        this.id = id;
    }

    @Generated(hash = 564483443)
    public Barcode(Long id, long barcodeDatabaseId, @NotNull String code) {
        this.id = id;
        this.barcodeDatabaseId = barcodeDatabaseId;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getBarcodeDatabaseId() {
        return this.barcodeDatabaseId;
    }

    public void setBarcodeDatabaseId(long barcodeDatabaseId) {
        this.barcodeDatabaseId = barcodeDatabaseId;
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
    @Generated(hash = 965427278)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBarcodeDao() : null;
    }


}