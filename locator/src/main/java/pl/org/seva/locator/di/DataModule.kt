package pl.org.seva.locator.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.org.seva.locator.data.mapper.TagDataToDomainMapper
import pl.org.seva.locator.data.mapper.TagDomainToDataMapper

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideTagDataToDomainMapper() = TagDataToDomainMapper()

    @Provides
    fun provideTagDomainToDataMapper() = TagDomainToDataMapper()

}
