package com.example.raishtec.Hikaru.Suga;

import com.example.raishtec.Hikaru.Suga.data.Student;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import com.example.raishtec.Hikaru.Suga.repository.StudentRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@OpenAPIDefinition(info=@Info(title = "受講生管理システム"))
@SpringBootApplication
public class HikaruSugaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HikaruSugaApplication.class, args);
	}
  }
