package com.example.poliakov.barcodescanner;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import java.util.List;
import org.greenrobot.greendao.DaoException;

/**
 * Entity mapped to table "BARCODE_DATABASE".
 */

@Entity(indexes = {
        @Index(value = "name, date DESC", unique = true)
})
public class BarcodeDatabase {

    @Id
    private Long id;

    @NotNull
    private String name;
    private java.util.Date date;

    @ToMany(referencedJoinProperty = "barcodeDatabaseId")
    private List<Barcode> barcodes;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 243314343)
    private transient BarcodeDatabaseDao myDao;

    @Generated(hash = 1070960228)
    public BarcodeDatabase() {
    }

    public BarcodeDatabase(Long id) {
        this.id = id;
    }

    @Generated(hash = 1194682647)
    public BarcodeDatabase(Long id, @NotNull String name, java.util.Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the BarcodeDatabase. */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Long getBarcodesCount() {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        BarcodeDao targetDao = daoSession.getBarcodeDao();
        return targetDao.queryBuilder().where(BarcodeDao.Properties.BarcodeDatabaseId.eq(id)).count();
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1724403928)
    public List<Barcode> getBarcodes() {
        if (barcodes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BarcodeDao targetDao = daoSession.getBarcodeDao();
            List<Barcode> barcodesNew = targetDao._queryBarcodeDatabase_Barcodes(id);
            synchronized (this) {
                if (barcodes == null) {
                    barcodes = barcodesNew;
                }
            }
        }
        return barcodes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1385026949)
    public synchronized void resetBarcodes() {
        barcodes = null;
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
    @Generated(hash = 2032722844)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBarcodeDatabaseDao() : null;
    }

}