package com.zeiris.cherryweather.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)

    @Delete
    fun delete(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<T>)
}