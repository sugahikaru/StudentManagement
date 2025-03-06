package com.example.raishtec.Hikaru.Suga.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.raishtec.Hikaru.Suga.data.CourseApplicationStatus;
import com.example.raishtec.Hikaru.Suga.data.Student;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.find();
    assertThat(actual.size()).isEqualTo(3);
  }

  @Test
  void 受講生申し込み情報の全件検索が行えること() {
    List<CourseApplicationStatus> actual = sut.searchCourseApplicationStatus();
    assertThat(actual.size()).isEqualTo(3);
  }

  @Test
  void 受講生のコース情報全件検索が行えること() {
    List<StudentCourses> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(3); // 初期データの件数に合わせて変更
  }

  @Test
  void 受講生の検索が行えること() {
    Student actual = sut.searchStudent("1"); // 1はテスト用の既存IDに変更
    assertThat(actual).isNotNull();
    assertThat(actual.getFullname()).isEqualTo("Taro Yamada"); // 既存の名前に合わせて変更
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    List<StudentCourses> actual = sut.searchStudentCourses("1"); // 1はテスト用の既存受講生IDに変更
    assertThat(actual).isNotEmpty();
    assertThat(actual.get(0).getCourseName()).isEqualTo("Java"); // 既存のコース名に合わせて変更
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setFullname("菅ひかる");
    student.setFurigana("すがひかる");
    student.setEmail("test@example.com");
    student.setCity("福岡県");
    student.setAge(32);
    student.setGender("man");

    sut.insertStudent(student);

    List<Student> actual = sut.find();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourses studentCourse = new StudentCourses();
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("新しいコース");
    studentCourse.setStartDate(LocalDateTime.parse("2024-10-16T10:34:23.4255004"));
    studentCourse.setEndDate(LocalDateTime.parse("2025-10-16T10:34:23.4255004"));

    sut.registerStudentCourses(studentCourse);

    List<StudentCourses> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(4); // 追加後の件数に合わせて変更
  }

  @Test
  void 受講生の受講生詳細を更新が行えること() {
    Student student = sut.searchStudent("1");
    student.setCity("Fukuoka");

    sut.updateStudent(student);

    assertThat(student.getCity()).isEqualTo("Fukuoka");
  }

  @Test
  void 受講生のコース情報の更新が行えること() {
    List<StudentCourses> courses = sut.searchStudentCourses("1"); // 1はテスト用の既存受講生IDに変更
    StudentCourses course = courses.get(0);
    course.setCourseName("更新されたコース名");

    sut.updateStudentsCourses(course);

    List<StudentCourses> updatedCourses = sut.searchStudentCourses("1");
    assertThat(updatedCourses.get(0).getCourseName()).isEqualTo("更新されたコース名");
  }
}
