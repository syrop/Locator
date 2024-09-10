package pl.org.seva.locator.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.org.seva.locator.datasource.model.TagDataSourceModel

@Dao
interface TagDao {

    @Query("SELECT * FROM TagDataSourceModel")
    fun getAll(): List<TagDataSourceModel>

    @Insert
    fun insert(tag: TagDataSourceModel)

    @Delete
    fun delete(tag: TagDataSourceModel)

}
