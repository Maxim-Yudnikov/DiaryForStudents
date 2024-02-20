package com.maxim.diaryforstudents.login.data

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val EMAIL: String,
    val SCHOOLS: List<LoginSchools>
)

data class LoginSchools(
    val PARTICIPANT: LoginParticipant
)

data class LoginParticipant(
    val SYS_GUID: String,
    val SURNAME: String,
    val NAME: String,
    val SECONDNAME: String,
    val GRADE: LoginGrade
)

data class LoginGrade(
    val NAME: String,
    val SCHOOL: LoginSchool,
    val GRADE_HEAD: LoginGradeHead
)

data class LoginSchool(
    val SHORT_NAME: String
)

data class LoginGradeHead(
    val SURNAME: String,
    val NAME: String,
    val SECONDNAME: String,
)