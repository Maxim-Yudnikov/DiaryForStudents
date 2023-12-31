package com.maxim.diaryforstudents.editDiary.createLesson.data

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface CreateLessonRepository {
    suspend fun create(
        startTime: String,
        endTime: String,
        theme: String,
        homework: String,
        name: String,
        classId: String
    ): CreateResult

    class Base(private val database: DatabaseReference) : CreateLessonRepository {
        override suspend fun create(
            startTime: String,
            endTime: String,
            theme: String,
            homework: String,
            name: String,
            classId: String
        ): CreateResult {
            val ref = database.child("lessons").push()
            val date = (System.currentTimeMillis() / 86400000L).toInt()
            val lessonInDatabase =
                handleQuery(database.child("lessons").orderByChild("date").equalTo(date.toDouble()))
            if (lessonInDatabase.filter { it.date == date && it.name == name }
                    .isNotEmpty()) return CreateResult.Failure("You have already created a lesson today")

            val lesson = Lesson(
                name, classId, date, startTime, endTime, theme, homework, (date - 4) / 7
            )
            handleTask(ref.setValue(lesson))
            return CreateResult.Success
        }

        private suspend fun handleTask(task: Task<Void>): Unit = suspendCoroutine { cont ->
            task.addOnSuccessListener {
                cont.resume(Unit)
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
        }

        private suspend fun handleQuery(query: Query): List<Lesson> = suspendCoroutine { cont ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull {
                        it.getValue(Lesson::class.java)!!
                    }
                    cont.resume(data)
                }

                override fun onCancelled(error: DatabaseError) =
                    cont.resumeWithException(error.toException())
            })
        }
    }
}

private data class Lesson(
    val name: String = "",
    val classId: String = "",
    val date: Int = 0,
    val startTime: String = "",
    val endTime: String = "",
    val theme: String = "",
    val homework: String = "",
    val week: Int = 0
)