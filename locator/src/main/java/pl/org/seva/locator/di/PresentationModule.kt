package pl.org.seva.locator.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.org.seva.locator.data.datasource.TagDataSource
import pl.org.seva.locator.data.mapper.TagDataToDomainMapper
import pl.org.seva.locator.data.repository.TagLiveRepository
import pl.org.seva.locator.domain.cleanarchitecture.usecase.UseCaseExecutor
import pl.org.seva.locator.domain.repository.TagRepository
import pl.org.seva.locator.domain.usecase.ScanUseCase
import pl.org.seva.locator.domain.usecase.StopScanUseCase
import pl.org.seva.locator.presentation.ScannerPresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideTagDomainToPresentationMapper() = TagDomainToPresentationMapper()

    @Provides
    fun providesUseCaseExecutorProvider(): UseCaseExecutorProvider = ::UseCaseExecutor

    @Provides
    fun providesScanUseCase(tagRepository: TagRepository) = ScanUseCase(tagRepository)

    @Provides
    fun providesStopScanUseCase(tagRepository: TagRepository) = StopScanUseCase(tagRepository)

    @Provides
    @Singleton
    fun provideTagRepository(
        tagDataToDomainMapper: TagDataToDomainMapper,
        tagDataSource: TagDataSource
    ) = TagLiveRepository(
        tagDataToDomainMapper = tagDataToDomainMapper,
        tagDataSource = tagDataSource,
    )

    @Provides
    @Singleton
    fun provideScannerPresentation(
        tagDomainToPresentationMapper: TagDomainToPresentationMapper,
        scanUseCase: ScanUseCase,
        stopScanUseCase: StopScanUseCase,
        useCaseExecutorProvider: UseCaseExecutorProvider,
    ) = ScannerPresentation(
        tagDomainToPresentationMapper,
        scanUseCase,
        stopScanUseCase,
        useCaseExecutorProvider,
    )

}
