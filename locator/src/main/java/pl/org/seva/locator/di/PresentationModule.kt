package pl.org.seva.locator.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.mapper.ScanResultDataToDomainMapper
import pl.org.seva.locator.data.mapper.TagDataToDomainMapper
import pl.org.seva.locator.data.mapper.TagDomainToDataMapper
import pl.org.seva.locator.data.repository.TagLiveRepository
import pl.org.seva.locator.domain.cleanarchitecture.usecase.UseCaseExecutor
import pl.org.seva.locator.domain.repository.TagRepository
import pl.org.seva.locator.domain.usecase.GetAllTagsUseCase
import pl.org.seva.locator.domain.usecase.AddTagUseCase
import pl.org.seva.locator.domain.usecase.ScanUseCase
import pl.org.seva.locator.domain.usecase.StopScanUseCase
import pl.org.seva.locator.domain.usecase.UpdateTagUseCase
import pl.org.seva.locator.presentation.CoordinatesPresentation
import pl.org.seva.locator.presentation.ScannerPresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.ScanResultDomainToPresentationMapper
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import pl.org.seva.locator.presentation.mapper.TagPresentationToDomainMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideTagDomainToPresentationMapper() = TagDomainToPresentationMapper()

    @Provides
    fun provideTagPresentationToDomainMapper() = TagPresentationToDomainMapper()

    @Provides
    fun providesUseCaseExecutorProvider(): UseCaseExecutorProvider = ::UseCaseExecutor

    @Provides
    fun providesScanUseCase(tagRepository: TagRepository) = ScanUseCase(tagRepository)

    @Provides
    fun providesStopScanUseCase(tagRepository: TagRepository) = StopScanUseCase(tagRepository)

    @Provides
    fun providesAddTagUseCase(tagRepository: TagRepository) = AddTagUseCase(tagRepository)

    @Provides
    fun providesUpdateUpdateTagUseCase(tagRepository: TagRepository) = UpdateTagUseCase(tagRepository)

    @Provides
    fun provideGetAllUseCase(tagRepository: TagRepository) = GetAllTagsUseCase(tagRepository)

    @Provides
    fun providesScanResultDataToDomainMapper() = ScanResultDataToDomainMapper()

    @Provides
    fun providesScanResultDomainToPresentationMapper() = ScanResultDomainToPresentationMapper()

    @Provides
    @Singleton
    fun provideTagRepository(
        tagDataToDomainMapper: TagDataToDomainMapper,
        tagDomainToDataMapper: TagDomainToDataMapper,
        scanResultDataToDomainMapper: ScanResultDataToDomainMapper,
        tagDataSource: TagDataSource
    ): TagRepository = TagLiveRepository(
        tagDataToDomainMapper = tagDataToDomainMapper,
        tagDomainToDataMapper = tagDomainToDataMapper,
        scanResultDataToDomainMapper = scanResultDataToDomainMapper,
        tagDataSource = tagDataSource,
    )

    @Provides
    @Singleton
    fun provideScannerPresentation(
        tagDomainToPresentationMapper: TagDomainToPresentationMapper,
        tagPresentationToDomainMapper: TagPresentationToDomainMapper,
        scanResultDataToPresentationMapper: ScanResultDomainToPresentationMapper,
        scanUseCase: ScanUseCase,
        stopScanUseCase: StopScanUseCase,
        addTagUseCase: AddTagUseCase,
        useCaseExecutorProvider: UseCaseExecutorProvider,
    ) = ScannerPresentation(
        tagDomainToPresentationMapper,
        scanResultDataToPresentationMapper,
        tagPresentationToDomainMapper,
        scanUseCase,
        stopScanUseCase,
        addTagUseCase,
        useCaseExecutorProvider,
    )

    @Provides
    @Singleton
    fun provideCoordinatesPresentation(
        tagDomainToPresentationMapper: TagDomainToPresentationMapper,
        tagPresentationToDomainMapper: TagPresentationToDomainMapper,
        getAllTagsUseCase: GetAllTagsUseCase,
        updateTagUseCase: UpdateTagUseCase,
        useCaseExecutorProvider: UseCaseExecutorProvider,
    ) = CoordinatesPresentation(
        tagDomainToPresentationMapper,
        tagPresentationToDomainMapper,
        getAllTagsUseCase,
        updateTagUseCase,
        useCaseExecutorProvider,
    )

}
