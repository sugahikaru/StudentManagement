package com.example.raishtec.Hikaru.Suga.domein;

import com.example.raishtec.Hikaru.Suga.data.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {
  @Valid
  private Student student;
  @Valid
  private List<StudentCourseDetail> studentCourseDetailList;
}
