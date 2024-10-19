package com.rozoomcool.chguburoapi.util.dataLoder

import com.rozoomcool.chguburoapi.entity.*
import com.rozoomcool.chguburoapi.service.UserService
import jakarta.transaction.Transactional
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        addAdmin()
    }

    @Transactional
    fun addAdmin() {
        var student = userService.create(
            User(
                username = "student",
                password = passwordEncoder.encode("student"),
                role = Role.STUDENT,
//                department = Department.STUDENT,
                profile = Profile(
                    firstname = "Sigma",
                    lastname = "Gigachad"
                )
            )
        )
        var eduDepartment = userService.create(
            User(
                username = "education",
                password = passwordEncoder.encode("education"),
                role = Role.EMPLOYEE,
//                department = Department.EDUCATION_DEPARTMENT
            )
        )
        var administration = userService.create(
            User(
                username = "admin",
                password = passwordEncoder.encode("admin"),
                role = Role.ADMIN,
//                department = Department.ADMINISTRATION
            )
        )

//        val subject = subjectService.add(Subject(name = "math"))
//        var course = courseRepository.save(Course(title = "fjskldjf", description = "fgfd", complexity = Complexity.BEGINNER, subject = subject))
//        val chapter = chapterRepository.save(Chapter(title = "gfkgdf", description = "fdgl", course = course, ordinal = 0))
//        course.chapters = course.chapters.apply {
//            add(Chapter(title = "fklgdfjg", description = "klgd", ordinal = 0))
//        }
//
//        course = courseRepository.save(course)
////        chapterRepository.deleteById(1)
////        courseRepository.deleteById(course.id!!)
    }
}