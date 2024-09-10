package pl.org.seva.locator.data.repository

import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.mapper.TagDataToDomainMapper
import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.domain.repository.TagRepository

class TagLiveRepository(
    private val tagDataToDomainMapper: TagDataToDomainMapper,
    private val tagDataSource: TagDataSource,
) : TagRepository {

    private val list = mutableListOf<TagDataModel>()

    override val tags get() = list.map { tagDataToDomainMapper.toDomain(it) }

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

}
