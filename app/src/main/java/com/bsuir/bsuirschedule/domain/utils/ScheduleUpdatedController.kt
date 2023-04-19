package com.bsuir.bsuirschedule.domain.utils

import com.bsuir.bsuirschedule.domain.models.ScheduleDay
import com.bsuir.bsuirschedule.domain.models.ScheduleSubject
import com.bsuir.bsuirschedule.domain.models.ScheduleUpdatedAction
import com.bsuir.bsuirschedule.domain.models.ScheduleUpdatedActionsList

class ScheduleUpdatedController(
    prevOriginalSchedule: ArrayList<ScheduleDay>,
    currOriginalSchedule: ArrayList<ScheduleDay>
) {

    private val previousSubjects = ArrayList<ScheduleSubject>()
    private val currentSubjects = ArrayList<ScheduleSubject>()
    private var deletedLessons = ArrayList<ScheduleSubject>()
    private var addedLessons = ArrayList<ScheduleSubject>()

    init {

        prevOriginalSchedule.map {
            previousSubjects.addAll(it.schedule)
        }

        currOriginalSchedule.map {
            currentSubjects.addAll(it.schedule)
        }

        val deleted = previousSubjects.minus(currentSubjects.toHashSet())
        val added = currentSubjects.minus(previousSubjects.toHashSet())

        if (deleted.isNotEmpty()) {
            deletedLessons = deleted as ArrayList<ScheduleSubject>
        }

        if (added.isNotEmpty()) {
            addedLessons = added as ArrayList<ScheduleSubject>
        }

    }

    fun getChangedActions(): ArrayList<ScheduleUpdatedAction> {

        return arrayListOf(
            ScheduleUpdatedAction(
                id = 0,
                action = ScheduleUpdatedActionsList.DELETED_SUBJECT,
                items = getDeletedLessons()
            ),
            ScheduleUpdatedAction(
                id = 1,
                action = ScheduleUpdatedActionsList.ADDED_SUBJECT,
                items = getAddedLessons()
            ),
            ScheduleUpdatedAction(
                id = 2,
                action = ScheduleUpdatedActionsList.CHANGED_NOTE,
                items = getNoteChangedLessons()
            ),
            ScheduleUpdatedAction(
                id = 3,
                action = ScheduleUpdatedActionsList.CHANGED_WEEKS,
                items = getWeeksChangedLessons()
            ),
            ScheduleUpdatedAction(
                id = 4,
                action = ScheduleUpdatedActionsList.CHANGED_LESSON_TYPE,
                items = getLessonTypeChangedLessons()
            ),
            ScheduleUpdatedAction(
                id = 5,
                action = ScheduleUpdatedActionsList.CHANGED_SUBGROUP,
                items = getSubgroupChangedLessons()
            ),
            ScheduleUpdatedAction(
                id = 6,
                action = ScheduleUpdatedActionsList.CHANGED_EMPLOYEE,
                items = getEmployeeChangedLessons()
            ),
            ScheduleUpdatedAction(
                id = 7,
                action = ScheduleUpdatedActionsList.CHANGED_GROUP,
                items = getGroupChangedLessons()
            ),
            ScheduleUpdatedAction(
                id = 8,
                action = ScheduleUpdatedActionsList.CHANGED_AUDIENCE,
                items = getAudienceChangedLessons()
            )
        )
    }

    private fun getDeletedLessons(): List<ScheduleSubject> {
        return deletedLessons
    }

    private fun getAddedLessons(): List<ScheduleSubject> {
        return addedLessons
    }

    private fun getAudienceChangedLessons(): List<ScheduleSubject> {
        val newAudienceLessons = ArrayList<ScheduleSubject>()
        deletedLessons.map { deleted ->
            addedLessons.map { added ->
                if (deleted.getMainHashCode() == added.getMainHashCode() &&
                        deleted.audience != added.audience) {
                    newAudienceLessons.add(added)
                }
            }
        }

        return newAudienceLessons
    }

    private fun getNoteChangedLessons(): List<ScheduleSubject> {
        val noteChangedLessons = ArrayList<ScheduleSubject>()
        deletedLessons.map { deleted ->
            addedLessons.map { added ->
                if (deleted.getMainHashCode() == added.getMainHashCode() &&
                    deleted.note != added.note) {
                    noteChangedLessons.add(added)
                }
            }
        }

        return noteChangedLessons
    }

    private fun getWeeksChangedLessons(): List<ScheduleSubject> {
        val weeksChangedLessons = ArrayList<ScheduleSubject>()
        deletedLessons.map { deleted ->
            addedLessons.map { added ->
                if (deleted.getMainHashCode() == added.getMainHashCode() &&
                    deleted.weekNumber != added.weekNumber) {
                    weeksChangedLessons.add(added)
                }
            }
        }

        return weeksChangedLessons
    }

    private fun getLessonTypeChangedLessons(): List<ScheduleSubject> {
        val lessonTypeChangedLessons = ArrayList<ScheduleSubject>()
        deletedLessons.map { deleted ->
            addedLessons.map { added ->
                if (deleted.getMainHashCode() == added.getMainHashCode() &&
                    deleted.lessonTypeAbbrev != added.lessonTypeAbbrev) {
                    lessonTypeChangedLessons.add(added)
                }
            }
        }

        return lessonTypeChangedLessons
    }

    private fun getSubgroupChangedLessons(): List<ScheduleSubject> {
        val audienceChangedLessons = ArrayList<ScheduleSubject>()
        deletedLessons.map { deleted ->
            addedLessons.map { added ->
                if (deleted.getMainHashCode() == added.getMainHashCode() &&
                    deleted.numSubgroup != added.numSubgroup) {
                    audienceChangedLessons.add(added)
                }
            }
        }

        return audienceChangedLessons
    }

    private fun getEmployeeChangedLessons(): List<ScheduleSubject> {
        val employeeChangedLessons = ArrayList<ScheduleSubject>()
        deletedLessons.map { deleted ->
            addedLessons.map { added ->
                if (deleted.getMainHashCode() == added.getMainHashCode() &&
                    deleted.employees != added.employees) {
                    employeeChangedLessons.add(added)
                }
            }
        }

        return employeeChangedLessons
    }

    private fun getGroupChangedLessons(): List<ScheduleSubject> {
        val groupChangedLessons = ArrayList<ScheduleSubject>()
        deletedLessons.map { deleted ->
            addedLessons.map { added ->
                if (deleted.getMainHashCode() == added.getMainHashCode() &&
                    deleted.employees != added.employees) {
                    groupChangedLessons.add(added)
                }
            }
        }

        return groupChangedLessons
    }

}


