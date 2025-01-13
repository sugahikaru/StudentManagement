package com.example.raishtec.Hikaru.Suga.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.raishtec.Hikaru.Suga.controller.StudentController;
import com.example.raishtec.Hikaru.Suga.controller.converter.StudentConverter;
import com.example.raishtec.Hikaru.Suga.data.CourseApplicationStatus;
import com.example.raishtec.Hikaru.Suga.data.Student;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import com.example.raishtec.Hikaru.Suga.domein.StudentCourseDetail;
import com.example.raishtec.Hikaru.Suga.domein.StudentDetail;
import com.example.raishtec.Hikaru.Suga.exception.TestException;
import com.example.raishtec.Hikaru.Suga.repository.StudentRepository;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Duration;



@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void 事前準備() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_repositoryとconverterの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourses> studentCoursesList = new ArrayList<>();
    List<CourseApplicationStatus> courseApplicationStatusList = new ArrayList<>();

    when(repository.find()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCoursesList);
    when(repository.searchCourseApplicationStatus()).thenReturn(courseApplicationStatusList);

    sut.getStudentList();

    verify(repository, times(1)).find();
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchCourseApplicationStatus();
    verify(converter, times(1)).convertStudentDetailList(studentList, studentCoursesList,
        courseApplicationStatusList);
  }

  @Test
  void 受講生登録で受講生とコースが登録できていること() {
    // 事前準備
    StudentDetail studentDetail = new StudentDetail();

    // 学生情報を設定
    Student student = new Student();
    student.setId("1");
    student.setFullname("山田太郎");
    student.setEmail("taro@example.com");
    studentDetail.setStudent(student);

    // コース情報を設定
    List<StudentCourseDetail> studentCourseDetails = new ArrayList<>();
    StudentCourses course1 = new StudentCourses();
    course1.setId(1);
    course1.setCourseName("JAVAコース");

    StudentCourses course2 = new StudentCourses();
    course2.setId(2);
    course2.setCourseName("AWSコース");

    // 各コースに対応する申し込み状況を設定
    CourseApplicationStatus status1 = new CourseApplicationStatus(1, 1, "仮申込");
    CourseApplicationStatus status2 = new CourseApplicationStatus(2, 2, "受講中");

    // StudentCourseDetail を作成
    StudentCourseDetail detail1 = new StudentCourseDetail(course1, status1);
    StudentCourseDetail detail2 = new StudentCourseDetail(course2, status2);

    studentCourseDetails.add(detail1);
    studentCourseDetails.add(detail2);

    studentDetail.setStudentCourseDetailList(studentCourseDetails);

    // モックの動作設定
    doNothing().when(repository).insertStudent(any(Student.class));
    doNothing().when(repository).registerStudentCourses(any(StudentCourses.class));
    doNothing().when(repository).insertCourseApplicationStatus(any(CourseApplicationStatus.class));

    // 実行
    StudentDetail result = sut.registerStudentDetail(studentDetail);

    // 検証
    verify(repository, times(1)).insertStudent(student);
    verify(repository, times(2)).registerStudentCourses(
        any(StudentCourses.class)); // 2つのコースが登録されることを検証
    verify(repository, times(2)).insertCourseApplicationStatus(
        any(CourseApplicationStatus.class)); // 2つの申込状況が登録されることを検証
    assertEquals(studentDetail, result); // 返り値が同じであることを確認
  }

  @Test
  void 受講生コースの初期化_受講生IDと日付が正しく設定されること() {
    String studentId = "1";
    Student student = new Student();
    student.setId(studentId);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    StudentCourses studentCourse = new StudentCourses();
    studentDetail.setStudentCourseDetailList(List.of(new StudentCourseDetail(studentCourse, null)));

    LocalDateTime now = LocalDateTime.now();
    doNothing().when(repository).insertStudent(any(Student.class));
    doNothing().when(repository).registerStudentCourses(any(StudentCourses.class));

    sut.registerStudentDetail(studentDetail);

    assertEquals(studentId, studentCourse.getStudentId());
    assertNotNull(studentCourse.getStartDate());
    assertNotNull(studentCourse.getEndDate());

    // 許容範囲を比較
    LocalDateTime expectedEndDate = now.plusMonths(1);
    long diffMillis = Duration.between(expectedEndDate, studentCourse.getEndDate()).toMillis();
    assertFalse(Math.abs(diffMillis) < 1000,
        "終了日が許容範囲外です: 差分 = " + diffMillis + "ミリ秒");
  }

  @Test
  void 受講生詳細の検索_リポジトリの処理を適切に呼び出して受講生IDに紐づく受講生情報と受講生コース情報が返ってくること()
      throws TestException {
    // 事前準備
    String id = "1";
    Student student = new Student();
    student.setId(id);

    // モックの StudentCourses オブジェクトのリストを準備
    List<StudentCourses> studentCoursesList = new ArrayList<>();
    StudentCourses studentCourse1 = new StudentCourses();
    studentCourse1.setId(101);
    studentCourse1.setStudentId(student.getId());
    studentCoursesList.add(studentCourse1);

    StudentCourses studentCourse2 = new StudentCourses();
    studentCourse2.setId(102);
    studentCourse2.setStudentId(student.getId());
    studentCoursesList.add(studentCourse2);

    // モックの CourseApplicationStatus を準備
    CourseApplicationStatus status1 = new CourseApplicationStatus(101, 1, "仮申込");
    CourseApplicationStatus status2 = new CourseApplicationStatus(102, 2, "受講中");

    // リポジトリのモック設定
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourses(student.getId())).thenReturn(studentCoursesList);
    when(repository.findCourseApplicationStatusByCourseId(101)).thenReturn(status1);
    when(repository.findCourseApplicationStatusByCourseId(102)).thenReturn(status2);

    // 実行
    StudentDetail result = sut.searchStudent(id);

    // 検証: StudentDetail の内容
    assertNotNull(result);
    assertEquals(id, result.getStudent().getId());

    // 検証: StudentCourseDetail のリスト
    List<StudentCourseDetail> courseDetails = result.getStudentCourseDetailList();
    assertEquals(2, courseDetails.size());

    // 検証: 各 StudentCourseDetail の内容
    StudentCourseDetail detail1 = courseDetails.get(0);
    assertEquals(studentCourse1.getId(), detail1.getStudentCourses().getId());
    assertEquals(status1.getStatus(), detail1.getCourseApplicationStatus().getStatus());

    StudentCourseDetail detail2 = courseDetails.get(1);
    assertEquals(studentCourse2.getId(), detail2.getStudentCourses().getId());
    assertEquals(status2.getStatus(), detail2.getCourseApplicationStatus().getStatus());

    // モックのメソッドが適切な回数呼び出されたことを検証
    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourses(student.getId());
    verify(repository, times(1)).findCourseApplicationStatusByCourseId(101);
    verify(repository, times(1)).findCourseApplicationStatusByCourseId(102);
  }

}
//
//  @Test
//  void 受講生詳細の更新_受講生とコースが正しく更新されること() {
//    // 事前準備
//    StudentDetail studentDetail = new StudentDetail();
//    Student student = new Student();
//    studentDetail.setStudent(student);
//    List<StudentCourses> studentCourseList = new ArrayList<>();
//    StudentCourses course1 = new StudentCourses();
//    StudentCourses course2 = new StudentCourses();
//    studentCourseList.add(course1);
//    studentCourseList.add(course2);
//    studentDetail.setStudentCourses(studentCourseList);
//
//    // モックの動作設定
//    doNothing().when(repository).updateStudent(student);
//    doNothing().when(repository).updateStudentsCourses(any(StudentCourses.class));
//
//    // 実行
//    sut.updateStudent(studentDetail);
//
//    // 検証
//    verify(repository, times(1)).updateStudent(student);
//    verify(repository, times(2)).updateStudentsCourses(any(StudentCourses.class)); // 2つのコースが更新されることを検証
//
//  }
//}
