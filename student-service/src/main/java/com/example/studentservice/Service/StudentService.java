package com.example.studentservice.Service;

import com.example.studentservice.Dto.StudentDTO;
import com.example.studentservice.Model.Student;
import com.example.studentservice.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(this::convertToDTO).orElse(null);
    }

    public StudentDTO addStudent(String name, String contact, MultipartFile picture) throws Exception {
        if (name == null || contact == null || picture == null || picture.isEmpty()) {
            throw new IllegalArgumentException("Missing fields or invalid image");
        }
        if (studentRepository.existsByContact(contact)) {
            throw new IllegalStateException("Contact already exists");
        }

        Student student = new Student();
        student.setName(name);
        student.setContact(contact);
        student.setPicture(picture.getBytes());

        return convertToDTO(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found");
        }
        studentRepository.deleteById(id);
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setStudentId(student.getStudentId());
        dto.setName(student.getName());
        dto.setContact(student.getContact());
        dto.setPicture(Base64.getEncoder().encodeToString(student.getPicture()));
        return dto;
    }
}