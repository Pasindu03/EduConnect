package com.example.enrollementservice.Service;


import com.example.enrollementservice.DTO.EnrollmentDTO;
import com.example.enrollementservice.Model.Enrollment;
import com.example.enrollementservice.Repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final String STUDENT_SERVICE_URL = "http://localhost:8082/students/";
    private final String COURSE_SERVICE_URL = "http://localhost:8081/courses/";

    public List<EnrollmentDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EnrollmentDTO> filterByCourse(String courseId) {
        return repository.findByCourseId(courseId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<EnrollmentDTO> filterByDate(LocalDate from, LocalDate to) {
        return repository.findByDateBetween(from, to).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EnrollmentDTO enroll(EnrollmentDTO dto) {
        validateStudent(dto.getStudentId());
        validateCourse(dto.getCourseId());

        if (repository.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new IllegalStateException("Duplicate enrollment");
        }

        Enrollment e = new Enrollment();
        e.setStudentId(dto.getStudentId());
        e.setCourseId(dto.getCourseId());
        e.setDate(LocalDate.parse(dto.getDate()));

        return toDTO(repository.save(e));
    }

    private EnrollmentDTO toDTO(Enrollment e) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(e.getStudentId());
        dto.setCourseId(e.getCourseId());
        dto.setDate(e.getDate().toString());
        return dto;
    }

    private void validateStudent(String studentId) {
        try {
            restTemplate.getForObject(STUDENT_SERVICE_URL + studentId, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Student not found");
        }
    }

    private void validateCourse(String courseId) {
        try {
            restTemplate.getForObject(COURSE_SERVICE_URL + courseId, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Course not found");
        }
    }
}