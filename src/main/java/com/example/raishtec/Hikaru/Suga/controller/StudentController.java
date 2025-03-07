package com.example.raishtec.Hikaru.Suga.controller;

import com.example.raishtec.Hikaru.Suga.data.StudentCourses;
import com.example.raishtec.Hikaru.Suga.domein.StudentDetail;
import com.example.raishtec.Hikaru.Suga.exception.TestException;
import com.example.raishtec.Hikaru.Suga.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 受講生の検索や登録、更新などを行う REST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;


  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索。 全件検索のみ。
   *
   * @return 受講生詳細全件一覧
   */
  @Operation(
      summary = "一覧検索",
      description = "受講生の一覧検索します。全件取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "検索成功",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class))),
          @ApiResponse(responseCode = "500", description = "サーバーエラー",
              content = @Content)})
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.getStudentList();
  }

  /**
   * 受講生検索。
   * IDに紐づく任意の受講生情報を取得します。
   * @param id
   * @return 受講生詳細
   */
   @Operation(
     summary = "受講生詳細検索",
     description = "指定したIDに基づいて、特定の受講生情報を取得します。",
    responses = {
     @ApiResponse(responseCode = "200", description = "検索成功",
       content = @Content(mediaType = "application/json",
           schema = @Schema(implementation = StudentDetail.class))),
   @ApiResponse(responseCode = "404", description = "受講生が見つかりませんでした",
      content = @Content),
     @ApiResponse(responseCode = "400", description = "無効なIDフォーマット",
       content = @Content)})
   @GetMapping("/student/{id}")
   public StudentDetail getStudent(@PathVariable String id) throws TestException {
     StudentDetail student = service.searchStudent(id);
     if (student == null) {
       throw new TestException("指定の生徒が見つかりませんでした");
     }
     return student;
   }

  /**
   * 受講生の登録
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(
      summary = "受講生登録",
      description = "受講生を登録します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "登録成功",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = StudentDetail.class))),
          @ApiResponse(responseCode = "400", description = "入力エラー",
              content = @Content),
          @ApiResponse(responseCode = "500", description = "サーバーエラー",
              content = @Content)})
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudentDetail(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生の更新を行います。キャンセルの更新もここで行います。論理削除
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
   @Operation(
     summary = "受講生更新",
    description = "既存の受講生詳細を更新します。", responses = {
        @ApiResponse(responseCode = "200", description = "更新成功",
           content = @Content(mediaType = "application/json")),
       @ApiResponse(responseCode = "400", description = "入力エラー",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "受講生が見つかりませんでした",
          content = @Content),
      @ApiResponse(responseCode = "500", description = "サーバーエラー",
         content = @Content)})
   @PutMapping("/updateStudent")
   public ResponseEntity<String> updateStudent(@RequestBody@Valid StudentDetail studentDetail) {
   service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新完了");
  }
  //@Operation(
  //    summary = "例外発生テスト",
  //   description = "このAPIは例外を強制的にスローします。現在は利用できません。",
  //  responses = {
  //      @ApiResponse(responseCode = "404", description = "指定されたリソースが見つかりません",
  //         content = @Content),
  //     @ApiResponse(responseCode = "500", description = "サーバーエラー",
  //         content = @Content)})
  // @GetMapping("/testException")
  //public void testException() throws TestException {
  //  throw new TestException("sorry");
  // }
  // @GetMapping("/studentInfo")
  // public List<Student> getStudentInfo(){
  //return repository.search();
  //}

  //@PostMapping("/studentInfo")
  // public void registerStudentInfo(String name,int age) {
  //repository.registerStudent(name,age);
  //}

  //@PatchMapping("/studentInfo")
  //public void updateStudentName(String name,int age){
  //  repository.updateStudent(name, age);
  //}

  //@DeleteMapping("/studentInfo")
  //public void deleteStudent(String name){
  //repository.deleteStudent(name);
  //}
}
