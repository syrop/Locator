package pl.org.seva.locator.presentation.model

data class LocatorViewState(
    val isLoading: Boolean,
    val tags: List<TagPresentationModel>,
    val rssiMap: Map<String, Int>,
    val location: Pair<Double, Double>?,
) {

    fun withTags(tags: List<TagPresentationModel>) = copy(
        isLoading = false,
        tags = tags,
    )

    fun withMap(rssiMap: Map<String, Int>) = copy(
        rssiMap = rssiMap,
    )

    fun withLocation(location: Pair<Double, Double>) = copy(location = location)

}
