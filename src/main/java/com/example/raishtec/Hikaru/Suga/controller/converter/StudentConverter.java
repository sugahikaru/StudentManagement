package com.example.raishtec.Hikaru.Suga.controller.converter;
import java.util.*;
import java.util.stream.Collectors;
import com.example.raishtec.Hikaru.Suga.data.CourseApplicationStatus;
import com.example.raishtec.Hikaru.Suga.data.Student;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import com.example.raishtec.Hikaru.Suga.domein.StudentCourseDetail;
import com.example.raishtec.Hikaru.Suga.domein.StudentDetail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
/**
 * 受講生とコース情報を組み合わせて受講生詳細に変換するConverter
 * 受講生情報、コース情報、申し込み状況を結び付けて、受講生詳細を生成する
 */
@Component
public class StudentConverter {



  public List<StudentDetail> convertStudentDetailList(
      List<Student> students,
      List<StudentCourses> studentCoursesList,
      List<CourseApplicationStatus> courseApplicationStatusList) {

    // CourseApplicationStatusをIDでマッピング
    Map<Integer, CourseApplicationStatus> courseStatusMap = courseApplicationStatusList.stream()
        .collect(Collectors.toMap(CourseApplicationStatus::getId, status -> status));

    // StudentCoursesをstudentIdでグループ化
    Map<String, List<StudentCourses>> studentCoursesMap = studentCoursesList.stream()
        .collect(Collectors.groupingBy(StudentCourses::getStudentId));

    // Studentを中心にStudentDetailを作成
    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      List<StudentCourses> courses = studentCoursesMap.getOrDefault(student.getId(), new ArrayList<>());

      // StudentCourseDetailを作成
      List<StudentCourseDetail> studentCourseDetails = courses.stream()
          .map(course -> {
            // StudentCourses の ID を用いて CourseApplicationStatus を取得
            CourseApplicationStatus status = courseStatusMap.get(course.getId());
            return new StudentCourseDetail(course, status);
          })
          .collect(Collectors.toList());

      // StudentDetailを作成
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      studentDetail.setStudentCourseDetailList(studentCourseDetails);

      studentDetails.add(studentDetail);
    }

    return studentDetails;
  }
}

