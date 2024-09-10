package pl.org.seva.locator.datasource.mapper

import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.datasource.model.TagDataSourceModel

class TagDataSourceToDataMapperMapper {

    fun toData(input: TagDataSourceModel) = TagDataModel(
        input.name,
        input.address,
        input.x,
        input.y
    )

}
