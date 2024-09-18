package pl.org.seva.locator.datasource.mapper

import android.bluetooth.le.ScanResult
import pl.org.seva.locator.data.model.ScanResultDataModel

class ScanResultToDataMapper {

    fun toData(input: ScanResult) = ScanResultDataModel(
        input.txPower,
        input.rssi,
    )
}
