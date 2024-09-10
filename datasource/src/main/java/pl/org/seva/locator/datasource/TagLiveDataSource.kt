package pl.org.seva.locator.datasource

import android.content.Context
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.PeripheralType
import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.datasource.mapper.PeripheralToDataMapper

class TagLiveDataSource(
    ctx: Context,
    private val peripheralToDataMapper: PeripheralToDataMapper
) : TagDataSource {

    private val central = BluetoothCentralManager(ctx)

    override fun scan(onFound: (TagDataModel) -> Unit) {
        central.scanForPeripherals(
            resultCallback = { bluetoothPeripheral, _ ->
                if (bluetoothPeripheral.type == PeripheralType.LE) {
                    onFound(peripheralToDataMapper.toData(bluetoothPeripheral))
                }
            },
            scanError = {}
        )
    }

    override fun stopScan() {
        central.stopScan()
    }

}
