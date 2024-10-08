package pl.org.seva.locator.domain.repository

import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel

interface TagRepository {

    val tags: List<Pair<TagDomainModel, ScanResultDomainModel>>

    fun load()

    fun scan(onFound: () -> Unit = {})

    fun continuousScan(onFound: (Pair<TagDomainModel, ScanResultDomainModel>) -> Unit = {})

    fun stopScan()

    fun add(tag: TagDomainModel)

    fun update(tag: TagDomainModel)

    fun delete(address: String)

    operator fun get(address: String): TagDomainModel

}
