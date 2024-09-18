package pl.org.seva.locator.data.mapper

import pl.org.seva.locator.data.model.ScanResultDataModel
import pl.org.seva.locator.domain.model.ScanResultDomainModel

class ScanResultDataToDomainMapper {

    fun toDomain(input: ScanResultDataModel?) = ScanResultDomainModel(
        input?.txPower ?: 0,
        input?.rssi ?: 0,
    )

}
