package com.example.raishtec.Hikaru.Suga.controller;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.raishtec.Hikaru.Suga.domein.StudentDetail;
import com.example.raishtec.Hikaru.Suga.service.StudentService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.Test; // JUnit 5のテストアノテーション
import org.mockito.Mockito; // Mockito関連の機能を使うため
import static org.mockito.Mockito.*; // Mockitoの静的メソッドをインポート
import org.springframework.beans.factory.annotation.Autowired; // 自動注入のため
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // MockMvcの設定
import org.springframework.boot.test.context.SpringBootTest; // Springブートアプリのテスト設定
import org.springframework.http.MediaType; // メディアタイプ定義用
import org.springframework.test.web.servlet.MockMvc; // MockMvcによるAPIテスト
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // MockMvcリクエストビルダー
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // MockMvcステータス検証
import org.springframework.boot.test.mock.mockito.MockBean; // MockBeanのアノテーション
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // MockMvcのレスポンス検証用



@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が出来て空のリストが返ってくる事() throws Exception {
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).getStudentList();
  }

  @Test
  void 受講生詳細のID検索で生徒が見つからない場合エラーが発生すること() throws Exception {
    // モックデータのセットアップ
    String id = "99";
    when(service.searchStudent(id)).thenReturn(null); // サービスがnullを返す場合をシミュレート

    // API呼び出しと検証
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isBadRequest()) // ステータスコード400を検証
        .andExpect(content().string("指定の生徒が見つかりませんでした")); // エラーメッセージを検証

    // サービスが正しく呼び出されたかを検証
    verify(service, times(1)).searchStudent(id);
  }




//  @Test
//  void 受講生詳細のID検索で数字以外を用いた時に入力チェックに掛かる事() throws Exception{
//    Student student = new Student();
//    student.setId("テスト");
//    student.setFullname("菅ひかる");
//    student.setFurigana("すがひかる");
//    student.setEmail("test@example.com");
//    student.setCity("福岡県");
//    student.setAge(32);
//    student.setGender("man");
//
//    Set<ConstraintViolation<Student>> violations = validator.validate(student);
//
//    assertThat(violations.size()).isEqualTo(1);
//    assertThat(violations).extracting("message").containsOnly("数字のみを入力してください。");
//  }
//
//  @Test
//  void 受講生詳細の受講生で適切な値を入力した時に入力チェックが異常に発生しない事() throws Exception{
//    Student student = new Student();
//    student.setId("1");
//    student.setFullname("菅ひかる");
//    student.setFurigana("すがひかる");
//    student.setEmail("test@example.com");
//    student.setCity("福岡県");
//    student.setAge(32);
//    student.setGender("man");
//
//    Set<ConstraintViolation<Student>> violations = validator.validate(student);
//
//    Assertions.assertEquals(0,violations.size());
//  }
//
  @Test
  void 受講生詳細の登録が実装できて空で返ってくること() throws Exception {
    //リクエストデータは最初に構築して入力チェックの検証も重ねている。
    //本来であれば返りは登録されたデータが入るが、モック化すると意味がないため、レスポンスは作らない。
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON).content(
                """
    {
    "student":{
        "fullname":"小島　三朗",
        "furigana":"こじまちょう",
        "email":"sh0905553424@outlook.jp",
        "city":"宮崎",
        "age":"39",
        "gender":"man",
        "remark":""
    },
    "studentCourseDetailList":[
        {
          "studentCourses":  {
             "courseName":"AWSコース"
            },
            "courseApplicationStatus":{
             "status":"仮申込"
            }
        }
    ]
  }
                    """))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudentDetail(any(StudentDetail.class));
  }}
//  @Test
//  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
//    mockMvc.perform(put("/updateStudent")
//        .contentType(MediaType.APPLICATION_JSON).content(
//            """
//                {
//                    "student":{
//                        "id" :"6",
//                        "fullname":"菅ひかる",
//                        "furigana":"すがひかる",
//                        "email":"sh09054713265@outlook.jp",
//                        "city":"福岡",
//                        "age":"32",
//                        "gender":"women",
//                        "remark":"",
//                        "isDeleted":"true"
//                    },
//                    "studentCourses":[
//                    ]
//                }
//                """)).andExpect(status().isOk());
//
//    verify(service, times(1)).updateStudent(any());
//  }
//
//  @Test
//  void 受講生詳細の例外APIが実行できてステータスが400で帰ってくること() throws Exception {
//    mockMvc.perform(get("/testException"))
//        .andExpect(status().is4xxClientError())
//        .andExpect(MockMvcResultMatchers.content().string("sorry"));
//  }
//}
