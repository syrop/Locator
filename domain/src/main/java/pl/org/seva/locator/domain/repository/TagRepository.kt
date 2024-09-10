package pl.org.seva.locator.domain.repository

import pl.org.seva.locator.domain.model.TagDomainModel

interface TagRepository {

    val tags: List<TagDomainModel>

    fun scan(onFound: () -> Unit = {})

    fun stopScan()

}
