package pl.org.seva.locator.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.mapper.TagDataToDomainMapper
import pl.org.seva.locator.data.repository.TagLiveRepository
import pl.org.seva.locator.domain.repository.TagRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideTagDataToDomainMapper() = TagDataToDomainMapper()

    @Provides
    @Singleton
    fun provideTagRepository(
        tagDataToDomainMapper: TagDataToDomainMapper,
        tagDataSource: TagDataSource
    ): TagRepository =
        TagLiveRepository(
            tagDataToDomainMapper = tagDataToDomainMapper,
            tagDataSource = tagDataSource,
        )

}
