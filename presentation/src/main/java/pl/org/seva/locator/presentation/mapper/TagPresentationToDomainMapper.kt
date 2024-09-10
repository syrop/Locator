package pl.org.seva.locator.presentation.mapper

import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.presentation.model.TagPresentationModel

class TagPresentationToDomainMapper {

    fun toDomain(input: TagPresentationModel) = TagDomainModel(
        input.name,
        input.address,
        input.x,
        input.y,
    )

}
