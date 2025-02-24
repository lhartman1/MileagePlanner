package net.lhartman.mileageplanner.data

import net.lhartman.mileageplanner.utils.getMonday
import net.lhartman.mileageplanner.utils.getSunday
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class OfflineMileageRepository(private val dayMileageDao: DayMileageDao) : MileageRepository {
    override fun getAllStream(): Flow<List<DayMileage>> = dayMileageDao.getAllDays()

    override fun getDayMileage(date: LocalDate): Flow<DayMileage?> = dayMileageDao.getDayMileage(date)

    override fun getWeekMileage(date: LocalDate): Flow<List<DayMileage>> {
        return dayMileageDao.getWeekMileage(
            date.getMonday(),
            date.getSunday(),
        )
    }

    override suspend fun insertDayMileage(vararg dayMileage: DayMileage) = dayMileageDao.insert(*dayMileage)

    override suspend fun updateDayMileage(dayMileage: DayMileage) = dayMileageDao.update(dayMileage)

    override suspend fun deleteDayMileage(dayMileage: DayMileage) = dayMileageDao.delete(dayMileage)
}
