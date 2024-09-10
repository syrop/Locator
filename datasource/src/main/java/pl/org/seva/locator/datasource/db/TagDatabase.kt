package pl.org.seva.locator.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.org.seva.locator.datasource.model.TagDataSourceModel

@Database(entities = [TagDataSourceModel::class], version = 1)
abstract class TagDatabase : RoomDatabase() {

    abstract fun tagDao(): TagDao

}
