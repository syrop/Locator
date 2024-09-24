package pl.org.seva.locator.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.repository.TagRepository

class LocationUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<List<Pair<String, Double>>, Pair<Double, Double>>() {

    override suspend fun executeInBackground(request: List<Pair<String, Double>>): Pair<Double, Double> {

        fun location(list: List<Triple<Double, Double, Double>>): Pair<Double, Double> {
            if (list.size != 3) throw IllegalArgumentException("Exactly three items are allowed")
            val (x1, y1, r1) = list[0]
            val (x2, y2, r2) = list[1]
            val (x3, y3, r3) = list[2]
            val a = -2 * x1 + 2 * x2
            val b = -2 * y1 + 2 * y2
            val c = r1 * r1 - r2 * r2 - x1 * x1 + x2 * x2 - y1 * y1 + y2 * y2
            val d = -2 * x2 + 2 * x3
            val e = -2 * y2 + 2 * y3
            val f = r2 * r2 - r3 * r3 - x2 * x2 + x3 * x3 - y2 * y2 + y3 * y3
            val x = (c * e - f * b) / (e * a - b * d)
            val y = (c * d - a * f) / (b * d - a * e)
            return x to y
        }

        return withContext(Dispatchers.Default) {
            val solutions = mutableListOf<Pair<Double, Double>>()
            for (i1 in 0..request.size - 3) {
                val tag1 = tagRepository[request[i1].first]
                val x1 = tag1.x.toDouble()
                val y1 = tag1.y.toDouble()
                val r1 = request[i1].second
                for (i2 in i1 + 1..request.size - 2) {
                    val tag2 = tagRepository[request[i2].first]
                    val x2 = tag2.x.toDouble()
                    val y2 = tag2.y.toDouble()
                    val r2 = request[i2].second
                    for (i3 in i2 + 1..request.size - 1) {
                        val tag3 = tagRepository[request[i3].first]
                        val x3 = tag3.x.toDouble()
                        val y3 = tag3.y.toDouble()
                        val r3 = request[i3].second
                        solutions.add(location(listOf(
                            Triple(x1, y1, r1),
                            Triple(x2, y2, r2),
                            Triple(x3, y3, r3),
                        )))
                    }
                }
            }
            val x = solutions.map { it.first }.average()
            val y = solutions.map { it.second }.average()
            return@withContext x to y
        }
    }

}
