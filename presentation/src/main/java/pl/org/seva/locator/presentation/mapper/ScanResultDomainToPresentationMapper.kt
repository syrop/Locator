package pl.org.seva.locator.presentation.mapper

import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.presentation.model.ScanResultPresentationModel

class ScanResultDomainToPresentationMapper {

    fun toPresentation(input: ScanResultDomainModel) = ScanResultPresentationModel(input.txPower, input.rssi)

}
