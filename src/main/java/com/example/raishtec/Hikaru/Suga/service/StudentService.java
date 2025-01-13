package com.example.raishtec.Hikaru.Suga.service;

import com.example.raishtec.Hikaru.Suga.controller.converter.StudentConverter;
import com.example.raishtec.Hikaru.Suga.data.CourseApplicationStatus;
import com.example.raishtec.Hikaru.Suga.data.Student;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import com.example.raishtec.Hikaru.Suga.domein.StudentCourseDetail;
import com.example.raishtec.Hikaru.Suga.domein.StudentDetail;
import com.example.raishtec.Hikaru.Suga.exception.TestException;
import com.example.raishtec.Hikaru.Suga.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索。 全件検索のみ。
   *
   * @return 受講生全件一覧
   */
  public List<StudentDetail> getStudentList() {
    List<Student> studentList = repository.find();
    List<StudentCourses> studentCoursesList = repository.searchStudentCourseList();
    List<CourseApplicationStatus> courseApplicationStatusList = repository.searchCourseApplicationStatus();
    return converter.convertStudentDetailList(studentList, studentCoursesList,
        courseApplicationStatusList);
  }

  //public List<StudentCourses>getstudentCoursesList(){
  //  return repository.searchStudentCourseList();
  // }

  /**
   * 受講生詳細の登録を行います。受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日と終了日を登録
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudentDetail(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.insertStudent(student);
    // コース情報を登録
    for (StudentCourseDetail studentCourseDetail : studentDetail.getStudentCourseDetailList()) {
      StudentCourses studentCourse = studentCourseDetail.getStudentCourses();
      initStudentCourse(studentCourse, student);
      repository.registerStudentCourses(studentCourse);

      // 申し込み状況を登録
      CourseApplicationStatus courseApplicationStatus = studentCourseDetail.getCourseApplicationStatus();
      if (courseApplicationStatus == null) {
        courseApplicationStatus = new CourseApplicationStatus();
        studentCourseDetail.setCourseApplicationStatus(courseApplicationStatus);
      }
      courseApplicationStatus.setCourseId(studentCourse.getId());
      if (courseApplicationStatus.getStatus() == null) {
        courseApplicationStatus.setStatus("仮申込");
      }
      repository.insertCourseApplicationStatus(courseApplicationStatus);
    }
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する
   *
   * @param studentCourses 受講生コース情報
   * @param student        受講生
   */
  private void initStudentCourse(StudentCourses studentCourses, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourses.setStudentId(student.getId());
    studentCourses.setStartDate(now);
    studentCourses.setEndDate(now.plusYears(1));
  }

  /**
   * 受講生検索。 IDに紐づく任意の受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudent(String id) throws TestException {
    Student student = repository.searchStudent(id);

    // 学生が見つからない場合は例外をスロー
    if (student == null) {
      throw new TestException("指定されたIDの生徒が見つかりません: " + id);
    }
    List<StudentCourses> studentCourses1 = repository.searchStudentCourses(student.getId());
    List<StudentCourseDetail> studentCourseDetailList = new ArrayList<>();

    // 各コース情報に対して申し込み状況を取得し、コース詳細を作成
    for (StudentCourses studentCourse : studentCourses1) {
      CourseApplicationStatus courseApplicationStatus = repository.findCourseApplicationStatusByCourseId(
          studentCourse.getId());
      StudentCourseDetail studentCourseDetail = new StudentCourseDetail();
      studentCourseDetail.setStudentCourses(studentCourse);
      studentCourseDetail.setCourseApplicationStatus(courseApplicationStatus);
      studentCourseDetailList.add(studentCourseDetail);
    }
    return new StudentDetail(student, studentCourseDetailList);

  }


  /**
   * 受講生詳細の更新を行います。受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  public void updateStudent(StudentDetail studentDetail) {
    // 学生情報をデータベースに保存
    repository.updateStudent(studentDetail.getStudent());

    // 各StudentCourseDetailを処理
    List<StudentCourseDetail> studentCourseDetailList = studentDetail.getStudentCourseDetailList();
    if (studentCourseDetailList != null) {
      for (StudentCourseDetail studentCourseDetail : studentCourseDetailList) {
        // StudentCoursesオブジェクトを取得して更新
        StudentCourses studentCourses = studentCourseDetail.getStudentCourses();
        if (studentCourses != null) {
          repository.updateStudentsCourses(studentCourses);
        }

        // CourseApplicationStatusオブジェクトを取得して更新
        CourseApplicationStatus courseApplicationStatus = studentCourseDetail.getCourseApplicationStatus();
        if (courseApplicationStatus != null) {
          repository.updateCourseApplicationStatus(courseApplicationStatus);
        }
      }
    }
  }
}
