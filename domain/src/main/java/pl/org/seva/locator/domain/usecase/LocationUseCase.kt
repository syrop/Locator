package pl.org.seva.locator.domain.usecase

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver
import com.lemmingapex.trilateration.TrilaterationFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.repository.TagRepository
import kotlin.math.min

class LocationUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<List<Pair<String, Double>>, Pair<Double, Double>>() {

    override suspend fun executeInBackground(request: List<Pair<String, Double>>): Pair<Double, Double> {

        return withContext(Dispatchers.Default) {

            val positions = mutableListOf<DoubleArray>()
            val distances = mutableListOf<Double>()
            request.forEach { item ->
                val (address, distance) = item
                val tag = tagRepository[address]
                positions.add(doubleArrayOf(tag.x.toDouble(), tag.y.toDouble()))
                distances.add(distance)
            }
            val solver = NonLinearLeastSquaresSolver(
                TrilaterationFunction(positions.toTypedArray(), distances.toDoubleArray()),
                LevenbergMarquardtOptimizer(),
            )
            val optimum = solver.solve()
            val centroid = optimum.point.toArray()
            return@withContext centroid[0] to centroid[1]
        }
    }

}
