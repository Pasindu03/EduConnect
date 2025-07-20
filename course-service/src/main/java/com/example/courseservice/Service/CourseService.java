package com.example.courseservice.Service;

import com.example.courseservice.Model.Course;
import com.example.courseservice.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course saveCourse(Course course) {
        if (courseRepository.existsById(course.getId())) {
            throw new IllegalStateException("Course already exists");
        }
        return courseRepository.save(course);
    }

    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalStateException("Course not found");
        }
        courseRepository.deleteById(id);
    }
}
