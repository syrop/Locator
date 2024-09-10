package pl.org.seva.locator.data.repository

import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.mapper.TagDataToDomainMapper
import pl.org.seva.locator.data.mapper.TagDomainToDataMapper
import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class TagLiveRepository(
    private val tagDataToDomainMapper: TagDataToDomainMapper,
    private val tagDomainToDataMapper: TagDomainToDataMapper,
    private val tagDataSource: TagDataSource,
) : TagRepository {

    private val list = mutableListOf<TagDataModel>()

    override val tags get() = list.map { tagDataToDomainMapper.toDomain(it) }

    override fun load() {
        list.clear()
        list.addAll(tagDataSource.getAll())
    }

    override fun scan(onFound: () -> Unit) {
        list.clear()
        onFound()
        tagDataSource.scan { tag ->
            if (list.find { it.address == tag.address } == null) {
                list.add(tag)
                onFound()
            }
        }
    }

    override fun stopScan() = tagDataSource.stopScan()

    override fun save(tag: TagDomainModel) {
        val all = tagDataSource.getAll()
        if (all.find { it.address == tag.address } != null) {
            return
        }
        val newY = (tagDataSource.getAll().maxOfOrNull { it.y } ?: -1) + 1
        tagDataSource.add(tagDomainToDataMapper.toData(tag.copy(y = newY)))
    }

}
