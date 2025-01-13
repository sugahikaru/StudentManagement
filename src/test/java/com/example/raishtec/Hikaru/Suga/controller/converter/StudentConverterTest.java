package com.example.raishtec.Hikaru.Suga.controller.converter;

import static org.junit.jupiter.api.Assertions.*;

import com.example.raishtec.Hikaru.Suga.data.CourseApplicationStatus;
import com.example.raishtec.Hikaru.Suga.data.Student;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import com.example.raishtec.Hikaru.Suga.domein.StudentCourseDetail;
import com.example.raishtec.Hikaru.Suga.domein.StudentDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class StudentConverterTest {

  @Test
  void testConvertStudentDetails() {
    // Arrange: モックデータの準備

    // 学生データを準備
    Student student1 = new Student();
    student1.setId("1");
    student1.setFullname("山田太郎");
    student1.setFurigana("やまだたろう");
    student1.setEmail("taro@example.com");
    student1.setCity("東京");
    student1.setGender("man");
    student1.setRemark("備考１");

    Student student2 = new Student();
    student2.setId("2");
    student2.setFullname("鈴木花子");
    student2.setFurigana("すずきはなこ");
    student2.setEmail("hanako@example.com");
    student2.setCity("大阪");
    student2.setGender("woman");
    student2.setRemark("備考２");

    List<Student> students = Arrays.asList(student1, student2);

    // コースデータを準備
    LocalDateTime now = LocalDateTime.now();

    StudentCourses course1 = new StudentCourses();
    course1.setId(1);
    course1.setStudentId("1");
    course1.setCourseName("JAVAコース");
    course1.setStartDate(now);
    course1.setEndDate(now.plusMonths(1));

    StudentCourses course2 = new StudentCourses();
    course2.setId(2);
    course2.setStudentId("1");
    course2.setCourseName("AWSコース");
    course2.setStartDate(now.plusMonths(2));
    course2.setEndDate(now.plusMonths(3));

    StudentCourses course3 = new StudentCourses();
    course3.setId(3);
    course3.setStudentId("2");
    course3.setCourseName("Web制作コース");
    course3.setStartDate(now.plusMonths(4));
    course3.setEndDate(now.plusMonths(5));

    List<StudentCourses> studentCourses = Arrays.asList(course1, course2, course3);

    // 受講申込ステータスデータを準備
    CourseApplicationStatus status1 = new CourseApplicationStatus(1, 1, "受講中");
    CourseApplicationStatus status2 = new CourseApplicationStatus(2, 2, "仮申込み");
    CourseApplicationStatus status3 = new CourseApplicationStatus(3, 3, "受講終了");

    List<CourseApplicationStatus> courseApplicationStatusList = Arrays.asList(status1, status2, status3);

    // テスト対象のクラス
    StudentConverter converter = new StudentConverter();

    // Act: メソッドを実行
    List<StudentDetail> result = converter.convertStudentDetailList(students, studentCourses, courseApplicationStatusList);

    // Assert: 結果の検証
    assertNotNull(result);
    assertEquals(2, result.size()); // 学生2名分の詳細が返る

    // 1人目の詳細情報を確認
    StudentDetail detail1 = result.get(0);
    assertEquals("1", detail1.getStudent().getId());
    assertEquals(2, detail1.getStudentCourseDetailList().size());
    assertEquals("JAVAコース", detail1.getStudentCourseDetailList().get(0).getStudentCourses().getCourseName());
    assertEquals("AWSコース", detail1.getStudentCourseDetailList().get(1).getStudentCourses().getCourseName());
    assertEquals("受講中", detail1.getStudentCourseDetailList().get(0).getCourseApplicationStatus().getStatus());
    assertEquals("仮申込み", detail1.getStudentCourseDetailList().get(1).getCourseApplicationStatus().getStatus());

    // 2人目の詳細情報を確認
    StudentDetail detail2 = result.get(1);
    assertEquals("2", detail2.getStudent().getId());
    assertEquals(1, detail2.getStudentCourseDetailList().size());
    assertEquals("Web制作コース", detail2.getStudentCourseDetailList().get(0).getStudentCourses().getCourseName());
    assertEquals("受講終了", detail2.getStudentCourseDetailList().get(0).getCourseApplicationStatus().getStatus());
  }

  @Test
  void testConvertStudentDetailsWithMismatchedStudentIds() {
    // Arrange: モックデータの準備

    // 学生データを準備
    Student student1 = new Student();
    student1.setId("1");
    student1.setFullname("山田太郎");
    student1.setFurigana("やまだたろう");
    student1.setEmail("taro@example.com");
    student1.setCity("東京");
    student1.setGender("man");
    student1.setRemark("備考１");

    Student student2 = new Student();
    student2.setId("2");
    student2.setFullname("鈴木花子");
    student2.setFurigana("すずきはなこ");
    student2.setEmail("hanako@example.com");
    student2.setCity("大阪");
    student2.setGender("woman");
    student2.setRemark("備考２");

    List<Student> students = Arrays.asList(student1, student2);

    // コースデータを準備 (不一致のID)
    LocalDateTime now = LocalDateTime.now();

    StudentCourses course1 = new StudentCourses();
    course1.setId(1);
    course1.setStudentId("3"); // 不一致のID
    course1.setCourseName("JAVAコース");
    course1.setStartDate(now);
    course1.setEndDate(now.plusMonths(1));

    StudentCourses course2 = new StudentCourses();
    course2.setId(2);
    course2.setStudentId("4"); // 不一致のID
    course2.setCourseName("AWSコース");
    course2.setStartDate(now.plusMonths(2));
    course2.setEndDate(now.plusMonths(3));

    List<StudentCourses> studentCourses = Arrays.asList(course1, course2);

    // 受講申込ステータスデータを準備
    CourseApplicationStatus status1 = new CourseApplicationStatus(1, 1, "受講中");
    CourseApplicationStatus status2 = new CourseApplicationStatus(2, 2, "仮申込み");
    CourseApplicationStatus status3 = new CourseApplicationStatus(3, 3, "受講終了");

    List<CourseApplicationStatus> courseApplicationStatusList = Arrays.asList(status1, status2, status3);

    // テスト対象のクラス
    StudentConverter converter = new StudentConverter();

    // Act: メソッドを実行
    List<StudentDetail> result = converter.convertStudentDetailList(students, studentCourses, courseApplicationStatusList);

    // Assert: 結果の検証
    assertNotNull(result);
    assertEquals(2, result.size()); // 学生2名分の詳細が返る

    // 1人目の詳細情報を確認 (コース情報は空)
    StudentDetail detail1 = result.get(0);
    assertEquals("1", detail1.getStudent().getId());
    assertTrue(detail1.getStudentCourseDetailList().isEmpty());

    // 2人目の詳細情報を確認 (コース情報は空)
    StudentDetail detail2 = result.get(1);
    assertEquals("2", detail2.getStudent().getId());
    assertTrue(detail2.getStudentCourseDetailList().isEmpty());
  }
}
