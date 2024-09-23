package pl.org.seva.locator.datasource.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pl.org.seva.locator.datasource.model.TagDataSourceModel

@Dao
interface TagDao {

    @Query("SELECT * FROM TagDataSourceModel")
    fun getAll(): List<TagDataSourceModel>

    @Insert
    fun insert(tag: TagDataSourceModel)

    @Delete
    fun delete(tag: TagDataSourceModel)

    @Update
    fun update(tag: TagDataSourceModel)

    @Query("DELETE FROM TagDataSourceModel where address = :address")
    fun delete(address: String)

}
