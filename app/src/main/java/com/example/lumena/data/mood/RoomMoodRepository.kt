package com.example.lumena.data.mood

class RoomMoodRepository(private val dao: MoodDao) : MoodRepository {

    override suspend fun getEntryForDate(dateIso: String): MoodEntry? =
        dao.getEntryForDate(dateIso)

    override suspend fun saveOrUpdate(entry: MoodEntry) {
        val existing = dao.getEntryForDate(entry.dateIso)
        if (existing != null) {
            val updated = existing.copy(
                mood = entry.mood,
                energy = entry.energy,
                note = entry.note
            )
            dao.update(updated)
        } else {
            dao.insert(entry)
        }
    }

    override fun getEntriesBetween(startIso: String, endIso: String) =
        dao.getEntriesBetween(startIso, endIso)

    override fun getLatest(limit: Int) =
        dao.getLatest(limit)
}
