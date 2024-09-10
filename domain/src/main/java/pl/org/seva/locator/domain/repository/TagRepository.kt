package pl.org.seva.locator.domain.repository

import pl.org.seva.locator.domain.model.TagDomainModel

interface TagRepository {

    val tags: List<TagDomainModel>

    fun load()

    fun scan(onFound: () -> Unit = {})

    fun stopScan()

    fun add(tag: TagDomainModel)

    fun update(tag: TagDomainModel)

}
