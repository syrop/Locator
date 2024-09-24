package pl.org.seva.locator.data.repository

import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.mapper.ScanResultDataToDomainMapper
import pl.org.seva.locator.data.mapper.TagDataToDomainMapper
import pl.org.seva.locator.data.mapper.TagDomainToDataMapper
import pl.org.seva.locator.data.model.ScanResultDataModel
import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class TagLiveRepository(
    private val tagDataToDomainMapper: TagDataToDomainMapper,
    private val scanResultDataToDomainMapper: ScanResultDataToDomainMapper,
    private val tagDomainToDataMapper: TagDomainToDataMapper,
    private val tagDataSource: TagDataSource,
) : TagRepository {

    private val list = mutableListOf<Pair<TagDataModel, ScanResultDataModel?>>()

    override val tags get() = list.map { tagDataToDomainMapper.toDomain(it.first) to scanResultDataToDomainMapper.toDomain(it.second) }

    override fun load() {
        list.clear()
        list.addAll(tagDataSource.getAll().map { it to null })
    }

    override fun scan(onFound: () -> Unit) {
        list.clear()
        onFound()
        tagDataSource.scan { tag, scanResult ->
            if (list.find { it.first.address == tag.address } == null) {
                list.add(tag to scanResult)
                onFound()
            }
        }
    }

    override fun continuousScan(onFound: (Pair<TagDomainModel, ScanResultDomainModel>) -> Unit) {
        tagDataSource.scan { tag, scanResult ->
            onFound(
                tagDataToDomainMapper.toDomain(tag) to
                    scanResultDataToDomainMapper.toDomain(scanResult)
            )
        }
    }

    override fun stopScan() = tagDataSource.stopScan()

    override fun add(tag: TagDomainModel) {
        val all = tagDataSource.getAll()
        if (all.find { it.address == tag.address } != null) {
            return
        }
        val newY = (tagDataSource.getAll().maxOfOrNull { it.y } ?: -1) + 1
        tagDataSource.add(tagDomainToDataMapper.toData(tag.copy(y = newY)))
    }

    override fun update(tag: TagDomainModel) {
        tagDataSource.update(tagDomainToDataMapper.toData(tag))
    }

    override fun delete(address: String) {
        tagDataSource.delete(address)
    }

    override operator fun get(address: String): TagDomainModel {
        val pair = requireNotNull(list.find { it.first.address == address }) { "Wrong address" }
        return tagDataToDomainMapper.toDomain(pair.first)
    }

}
