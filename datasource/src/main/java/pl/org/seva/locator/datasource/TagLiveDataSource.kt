package pl.org.seva.locator.datasource

import android.content.Context
import androidx.room.Room
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.PeripheralType
import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.model.ScanResultDataModel
import pl.org.seva.locator.data.model.TagDataModel
import pl.org.seva.locator.datasource.db.TagDatabase
import pl.org.seva.locator.datasource.mapper.PeripheralToDataMapper
import pl.org.seva.locator.datasource.mapper.ScanResultToDataMapper
import pl.org.seva.locator.datasource.mapper.TagDataSourceToDataMapperMapper
import pl.org.seva.locator.datasource.mapper.TagDataToDataSourceMapper

class TagLiveDataSource(
    ctx: Context,
    private val peripheralToDataMapper: PeripheralToDataMapper,
    private val scanResultToDataMapper: ScanResultToDataMapper,
    private val tagDataToDataSourceMapper: TagDataToDataSourceMapper,
    private val tagDataSourceToDataMapperMapper: TagDataSourceToDataMapperMapper,
) : TagDataSource {

    private val central = BluetoothCentralManager(ctx)
    private val tagDb = Room.databaseBuilder(
        ctx,
        TagDatabase::class.java,
        "tags",
    ).build()
    private val tagDao = tagDb.tagDao()

    override fun scan(onFound: (TagDataModel, ScanResultDataModel) -> Unit) {
        central.scanForPeripherals(
            resultCallback = { bluetoothPeripheral, scanResult ->
                if (bluetoothPeripheral.type == PeripheralType.LE) {
                    onFound(
                        peripheralToDataMapper.toData(bluetoothPeripheral),
                        scanResultToDataMapper.toData(scanResult),
                    )
                }
            },
            scanError = {},
        )
    }

    override fun stopScan() {
        central.stopScan()
    }

    override fun add(tag: TagDataModel) {
        tagDao.insert(tagDataToDataSourceMapper.toDataSource(tag))
    }

    override fun update(tag: TagDataModel) {
        tagDao.update(tagDataToDataSourceMapper.toDataSource(tag))
    }

    override fun getAll() = tagDao.getAll().map { tagDataSourceToDataMapperMapper.toData(it) }

    override fun delete(address: String) {
        tagDao.delete(address)
    }

}
