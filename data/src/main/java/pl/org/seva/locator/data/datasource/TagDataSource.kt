package pl.org.seva.locator.data.datasource

import pl.org.seva.locator.data.model.ScanResultDataModel
import pl.org.seva.locator.data.model.TagDataModel

interface TagDataSource {

    fun scan(onFound: (TagDataModel, ScanResultDataModel) -> Unit)

    fun stopScan()

    fun add(tag: TagDataModel)

    fun update(tag: TagDataModel)

    fun getAll(): List<TagDataModel>
}
