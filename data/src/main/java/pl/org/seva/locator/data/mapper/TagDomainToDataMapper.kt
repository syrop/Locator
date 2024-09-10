package pl.org.seva.locator.data.mapper

import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.domain.model.TagDomainModel

class TagDomainToDataMapper {

    fun toData(input: TagDomainModel) = TagDataModel(
        input.name,
        input.address,
        input.x,
        input.y,
    )

}
