package com.example.studentservice.Controller;

import com.example.studentservice.Dto.StudentDTO;
import com.example.studentservice.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.status(404).body("Student not found");
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> addStudent(
            @RequestPart("name") String name,
            @RequestPart("contact") String contact,
            @RequestPart("picture") MultipartFile picture) {

        try {
            return ResponseEntity.ok(studentService.addStudent(name, contact, picture));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving student");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Student not found");
        }
    }
}
