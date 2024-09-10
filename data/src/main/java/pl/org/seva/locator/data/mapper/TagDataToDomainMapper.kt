package pl.org.seva.locator.data.mapper

import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.domain.model.TagDomainModel

class TagDataToDomainMapper {

    fun toDomain(input: TagDataModel) = TagDomainModel(input.name)

}
