package pl.org.seva.locator.domain.repository

import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel

interface TagRepository {

    val tags: List<Pair<TagDomainModel, ScanResultDomainModel>>

    fun load()

    fun scan(onFound: () -> Unit = {})

    fun stopScan()

    fun add(tag: TagDomainModel)

    fun update(tag: TagDomainModel)

}
