package com.example.raishtec.Hikaru.Suga.domein;

import com.example.raishtec.Hikaru.Suga.data.CourseApplicationStatus;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentCourseDetail {

  @Valid
  private StudentCourses studentCourses;

  @Valid
  private CourseApplicationStatus courseApplicationStatus;

  // コンストラクタを追加
  public StudentCourseDetail(StudentCourses studentCourses, CourseApplicationStatus courseApplicationStatus) {
    this.studentCourses = studentCourses;
    this.courseApplicationStatus = courseApplicationStatus;
  }
}

