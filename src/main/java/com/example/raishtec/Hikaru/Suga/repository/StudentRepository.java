package com.example.raishtec.Hikaru.Suga.repository;

import com.example.raishtec.Hikaru.Suga.data.CourseApplicationStatus;
import com.example.raishtec.Hikaru.Suga.data.Student;
import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
/**
 *受講生を扱うリポジトリ
 * 全件検索や単一条件検索、コース情報が行えるクラスです
 */
@Mapper
public interface StudentRepository {

  /**
   * 全件検索します
   *
   * @return 全件検索した受講生情報の一覧
   */
  List<Student> find();

  /**
   * 受講生の検索を行います。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  Student searchStudent(String id);

  /**
   * コース申し込み状況の全件検索
   *
   * @return 申し込み状況リスト
   */
  List<CourseApplicationStatus> searchCourseApplicationStatus();


  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報
   */
  @Select("SELECT * FROM student_courses")
  List<StudentCourses> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM student_courses WHERE student_id = #{studentId}")
  List<StudentCourses> searchStudentCourses(String studentId);

  /**
   * コースIDで指定したコースの申し込み状況
   *
   * @param courseId コースID
   * @return コースIDで指定したコースの申し込み状況
   */
  CourseApplicationStatus findCourseApplicationStatusByCourseId(int courseId);

  /**
   * 受講生の新規登録。IDは自動生成を行う
   *
   * @param student 受講生
   */
  void insertStudent(Student student);

  /**
   * 受講生コース情報の新規登録。IDは自動生成を行う
   *
   * @param studentCourses 受講生コース情報
   */
  void registerStudentCourses(StudentCourses studentCourses);

  /**
   * 新規申し込み状況の登録 IDは自動採番 初期値はデフォルトで'仮申込'
   *
   * @param courseApplicationStatus 申し込み状況
   */
  void insertCourseApplicationStatus(CourseApplicationStatus courseApplicationStatus);


  /**
   * 受講生を更新します
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します
   *
   * @param studentCourses 受講生コース名
   */
  void updateStudentsCourses(StudentCourses studentCourses);


  /**
   * 申し込み状況の更新
   *
   * @param courseApplicationStatus 申し込み状況
   */
  void updateCourseApplicationStatus(CourseApplicationStatus courseApplicationStatus);

  //@Update("UPDATE student SET age = #{age} WHERE name = #{name}")
  //void updateStudent(String name,int age);

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);

  @Select("SELECT * FROM student")
  List<Student> search();

  @Insert("INSERT student values(#{name},#{age})")
  void registerStudent(String name, int age);

  @Update("UPDATE students SET isDeleted = true WHERE id = #{id}")
  void updateIsDeletedTrue(String id);
}



