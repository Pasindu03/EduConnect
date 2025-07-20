package com.example.enrollementservice.Repository;

import com.example.enrollementservice.Model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourseId(String courseId);
    List<Enrollment> findByDateBetween(LocalDate from, LocalDate to);
    boolean existsByStudentIdAndCourseId(String studentId, String courseId);
}
