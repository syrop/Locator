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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    fun providesPeripheralToDataMapper() = PeripheralToDataMapper()

    @Provides
    @Singleton
    fun provideTagDataSource(
        @ApplicationContext ctx: Context,
        peripheralToDataMapper: PeripheralToDataMapper
    ): TagDataSource = TagLiveDataSource(ctx, peripheralToDataMapper)
}
