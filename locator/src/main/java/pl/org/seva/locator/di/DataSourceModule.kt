package pl.org.seva.locator.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.datasource.TagLiveDataSource
import pl.org.seva.locator.datasource.mapper.PeripheralToDataMapper
import pl.org.seva.locator.datasource.mapper.ScanResultToDataMapper
import pl.org.seva.locator.datasource.mapper.TagDataSourceToDataMapperMapper
import pl.org.seva.locator.datasource.mapper.TagDataToDataSourceMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class   DataSourceModule {

    @Provides
    fun providesPeripheralToDataMapper() = PeripheralToDataMapper()

    @Provides
    fun provideTagDataToDataSourceMapper() = TagDataToDataSourceMapper()

    @Provides
    fun provideTagDataSourceToDataMapper() = TagDataSourceToDataMapperMapper()

    @Provides
    fun provideScanResultToDataMapper() = ScanResultToDataMapper()

    @Provides
    @Singleton
    fun provideTagDataSource(
        @ApplicationContext ctx: Context,
        peripheralToDataMapper: PeripheralToDataMapper,
        scanResultToDataMapper: ScanResultToDataMapper,
        tagDataToDataSourceMapper: TagDataToDataSourceMapper,
        tagDataSourceToDataMapperMapper: TagDataSourceToDataMapperMapper,
    ): TagDataSource = TagLiveDataSource(
        ctx,
        peripheralToDataMapper,
        scanResultToDataMapper,
        tagDataToDataSourceMapper,
        tagDataSourceToDataMapperMapper,
    )

}
