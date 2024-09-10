package pl.org.seva.locator.datasource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagDataSourceModel(
    @PrimaryKey val address: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "x") val x: Int,
    @ColumnInfo(name = "y") val y: Int,
)
