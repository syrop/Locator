package pl.org.seva.locator.datasource.mapper

import com.welie.blessed.BluetoothPeripheral
import pl.org.seva.locator.data.model.TagDataModel

class PeripheralToDataMapper {

    fun toData(input: BluetoothPeripheral): TagDataModel = TagDataModel(
        input.name,
        input.address,
        0,
        0,
    )

}
