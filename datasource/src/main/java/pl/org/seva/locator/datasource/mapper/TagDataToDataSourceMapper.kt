package pl.org.seva.locator.datasource.mapper

import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.datasource.model.TagDataSourceModel

class TagDataToDataSourceMapper {

    fun toDataSource(input: TagDataModel) = TagDataSourceModel(
        input.address,
        input.name,
        input.x,
        input.y
    )

}
