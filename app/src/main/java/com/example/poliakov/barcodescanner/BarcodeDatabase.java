package com.example.poliakov.barcodescanner;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
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

}