package com.example.courseservice.Controller;

import com.example.courseservice.Model.Course;
import com.example.courseservice.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable String id) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return ResponseEntity.status(404).body("Course not found");
        }
        return ResponseEntity.ok(course);
    }

    @PostMapping
    public ResponseEntity<?> saveCourse(@RequestBody Course course) {
        try {
            return ResponseEntity.ok(courseService.saveCourse(course));
        } catch (IllegalStateException e) {
            if (e.getMessage().equals("Course already exists")) {
                return ResponseEntity.status(409).body("Duplicate Course ID");
            }
            return ResponseEntity.status(400).body("Invalid input");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body("Course not found");
        }
    }
}
