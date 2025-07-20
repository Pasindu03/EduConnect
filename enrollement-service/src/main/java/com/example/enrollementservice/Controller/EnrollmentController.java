package com.example.enrollementservice.Controller;

import com.example.enrollementservice.DTO.EnrollmentDTO;
import com.example.enrollementservice.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService service;

    @GetMapping
    public ResponseEntity<?> listEnrollments(
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (courseId != null) {
            return ResponseEntity.ok(service.filterByCourse(courseId));
        } else if (from != null && to != null) {
            return ResponseEntity.ok(service.filterByDate(from, to));
        } else {
            return ResponseEntity.ok(service.getAll());
        }
    }

    @PostMapping
    public ResponseEntity<?> enroll(@RequestBody EnrollmentDTO dto) {
        try {
            return ResponseEntity.ok(service.enroll(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid input");
        }
    }
}
