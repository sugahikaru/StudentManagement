# 📚 StudentManagement 📚

## 🏁 はじめに

本アプリケーションは、
**JavaとSpring BootによるWebアプリケーション開発を学習する目的で作成したポートフォリオ**になります。

## 🧾 サービス概要

このプロジェクトは、**ITスクール運営者向けの内部管理システム**です。
受講生のプロフィール、コースの受講状況、申し込み内容などを一元管理でき、オンラインスクール運営における業務の効率化と情報の見える化を支援します。

## 💻 開発環境

**使用技術**  

バックエンド

  ![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
  ![Spring Boot](https://img.shields.io/badge/SpringBoot-3.3.4-6DB33F?logo=spring&logoColor=white)

インフラ

![AWS EC2](https://img.shields.io/badge/AWS-EC2-FF9900?logo=amazonaws&logoColor=white)
![AWS RDS](https://img.shields.io/badge/AWS-RDS-527FFF?logo=amazonaws&logoColor=white)
![AWS ALB](https://img.shields.io/badge/AWS-ALB-FF9900?logo=amazonaws&logoColor=white)
![badge](https://img.shields.io/badge/MySQL-%234479A1?logo=mysql&logoColor=white)

使用ツール

![MyBatis](https://img.shields.io/badge/MyBatis-3.0.3-DC382D?logo=mybatis&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-Tool-FF6C37?logo=postman&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-Code-181717?logo=github&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-IDE-000000?logo=intellijidea&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI/CD-2088FF?logo=githubactions&logoColor=white)

## 📘 機能一覧

| 機能         | 詳細                                        | メソッド | URL                    |
|--------------|---------------------------------------------|----------|------------------------|
| 受講生登録     | 新しい受講生の詳細を登録する                          | POST     | `/registerStudent`     |
| 受講生更新     | 既存の受講生情報を更新する                            | PUT      | `/updateStudent`       |
| 受講生一覧     | 全ての受講生情報（論理削除されていないもの）を取得する | GET      | `/studentList`         |
| 受講生取得(ID) | 指定したIDの受講生詳細を取得する                      | GET      | `/student/{id}`        |
| 論理削除       | 指定した受講生を論理的に削除する                      | DELETE   | `/deleteStudent/{id}`  |

---

# 🎥 API動作デモ（画像＆動画）

---

## 🆕 受講生登録

![受講生登録](./images/RegisterStundent.png)

> 新しい受講生を登録するAPIを実行した様子です。

---

## ✏️ 受講生更新

![受講生更新](./images/UpdateStundet.png)

> 既存の受講生情報を更新するAPIを実行した様子です。

---

## 📄 受講生一覧取得

以下のサムネイル画像をクリックすると、Google DriveでAPIの動作動画を再生できます。

[![受講生一覧デモ](./images/StudentList.png)](https://drive.google.com/file/d/1Kp0oJjgHrgFnozpZgHiHo-0LWUzobd8T/view?usp=drive_link)
> 登録済みの受講生全件を取得するAPIを実行した様子です。

---

## 🔍 受講生ID検索

![受講生ID検索](./images/StudentID.png)

> 特定のIDを指定して受講生詳細を取得するAPIを実行した様子です。

---

## 🗑️ 論理削除

![論理削除](./images/DeleteStudent1.png)

> 特定の受講生を論理削除するAPIを実行した様子です。

![受講生ID検索該当なし](./images/DeleteStudent2.png)

> 論理削除後、再度ID指定でAPIを実行した様子です。

---

## 🗂 データベース設計（ER図）

本アプリケーションで使用している主なテーブル構成は以下のとおりです。

```mermaid
erDiagram
     
     students {
         int id PK
         varchar(100) fullname
         varchar(100) furigana
         varchar(100) email
         int age
         varchar(10) gender
         text remark
         boolean isDeleted
     }

    student_courses {
        int id PK
        int student_id FK
        varchar(100) course_name
        timestamp start_date
        timestamp end_date
    }

    course_application_status {
        int id PK
        int courseId FK
        varchar(255) status
    }

    students ||--o{ student_courses : enrolls
    student_courses ||--o{ course_application_status : has

```

### テーブル概要

| テーブル名                  | 説明                          |
|---------------------------|-----------------------------|
| students                  | 受講生情報を管理             |
| student_courses           | 受講生ごとのコース申込情報   |
| course_application_status | コースの申し込み状況履歴     |

---

## 📊 シーケンス図

---

#### 受講生詳細の登録フロー

```mermaid
 sequenceDiagram
actor User
participant API
participant DB

    User ->>+ API: POST /registerStudent (受講生詳細)
    API ->> API: 入力データをバリデーション（@Valid）
    alt 入力データが有効
        API ->> DB: INSERT students
        API ->> DB: INSERT student_courses
        API ->> DB: INSERT course_application_status
        API -->> User: 200 OK（登録した受講生詳細）
    else 入力データが無効
        API -->> User: 400 Bad Request（エラーメッセージ）
    end
```

---

#### 受講生詳細の更新フロー

```mermaid
sequenceDiagram
actor User
participant API as Spring Boot API
participant DB as Database

User ->>+ API: PUT /updateStudent (受講生詳細)
API ->> API: 入力データをバリデーション（@Valid）
alt 入力データが有効
    API ->> DB: UPDATE students, student_courses, course_application_status
    API -->> User: 200 OK（更新完了）
else 入力データが無効
    API -->> User: 400 Bad Request（エラーメッセージ）
end
```

---

#### 受講生詳細の全件検索フロー

```mermaid
sequenceDiagram
actor User
participant API as Spring Boot API
participant Service as Service Layer
participant DB as Database

User ->>+ API: GET /studentList
API ->> Service: getStudentList()
Service ->> DB: SELECT * FROM students
Service ->> DB: SELECT * FROM student_courses
Service ->> DB: SELECT * FROM course_application_status
DB -->> Service: 各テーブルのデータ
Service ->> Service: コンバーターでDTOに変換
Service -->> API: 結合した一覧DTO
API -->> User: 200 OK（学生一覧をJSONで返却）
```

---

#### 受講生のID検索フロー

```mermaid
sequenceDiagram
actor User
participant API as Spring Boot API
participant Service as Service Layer
participant DB as Database

User ->>+ API: GET /student/{id}
API ->> API: IDフォーマットをバリデーション
alt IDフォーマットが有効
    API ->> Service: searchStudent(id)
    Service ->> DB: SELECT * FROM students WHERE id = ?
    Service ->> DB: SELECT * FROM student_courses WHERE student_id = ?
    Service ->> DB: SELECT * FROM course_application_status WHERE student_id = ?
    DB -->> Service: 各テーブルのデータ
    Service -->> API: 結合した StudentDetail データ
    API -->> User: 200 OK（受講生詳細を返却）
else IDフォーマットが無効
    API -->> User: 400 Bad Request（無効なIDフォーマット）
end
```
---

##### 論理削除フロー

```mermaid
sequenceDiagram
actor User
participant API as Spring Boot API
participant Service as Service Layer
participant DB as Database

User ->>+ API: DELETE /students/{id}
API ->> API: IDフォーマットをバリデーション
alt IDフォーマットが有効
API ->> Service: deleteStudent(id)
Service ->> DB: SELECT * FROM students WHERE id = ? AND (isDeleted IS NULL OR isDeleted = false)
alt 対象の受講生が存在
Service ->> DB: UPDATE students SET isDeleted = true WHERE id = ?
DB -->> Service: 更新結果を返却
Service -->> API: 処理成功
API -->> User: 204 No Content（削除完了）
else 対象の受講生が存在しない
Service -->> API: 受講生が見つからない例外をスロー
API -->> User: 404 Not Found（既に削除済み または 存在しない）
end
else IDフォーマットが無効
API -->> User: 400 Bad Request（無効なIDフォーマット）
end
```

---

## 🧪 テスト（JUnit）

本プロジェクトではJUnit 5を用いて単体テストを実装し
プルリクエスト作成時やmainブランチへのpush時にGitHub Actionsで自動実行し、品質を担保しています。

### Controller層
- 正常系：期待通りのレスポンスを返すか
- 異常系：入力値不正やデータ未存在時に適切な HTTP ステータス・エラーメッセージを返すか

  ⇒[StudentController.java](./src/main/java/com/example/raishtec/Hikaru/Suga/controller/StudentController.java)

### Service層
- ビジネスロジックの正当性をテスト

  ⇒[StudentService.java](./src/main/java/com/example/raishtec/Hikaru/Suga/service/StudentService.java)

### Repository層
- DB関連処理とクエリのテスト

  ⇒[StudentRepository.java](./src/main/java/com/example/raishtec/Hikaru/Suga/repository/StudentRepository.java)

### Converter 層
- 複数のドメインモデル（Student, StudentCourses, CourseApplicationStatus）を結合し、StudentDetailを生成する変換ロジックのテスト
- 正常系：関連付けが正しく行われることを確認
- 異常系：学生IDとコース情報のIDが一致しない場合、コース情報が紐づかないことを確認

  ⇒[StudentConverter.java](./src/main/java/com/example/raishtec/Hikaru/Suga/controller/converter/StudentConverter.java)

![説明文](./images/Test Succsess.png)


## 🔄 CI/CDパイプライン

- プルリクエスト作成時や main ブランチへの push 時に以下を自動化：
  - JUnit テスト実行で品質チェック（CI）
  - 成功時はEC2サーバーにビルド済みJarをデプロイ & systemctlで再起動（CD）

  ⇒[JavaTest.yml](./StudentManagement/.github/workflows/JavaTest.yml)

---

## ✅ 力を入れたところ

1. **論理削除の採用**  
   退会者データをUPDATEで`isDeleted=true`にして保持し、将来の分析やマーケティングに活用。
2. **バリデーションと例外処理**  
   正規表現や`@NotNull`で入力の正確性を担保し、分かりやすいエラーメッセージでユーザー体験を向上。
3. **ドキュメント整備**  
   JavadocやOpenAPIアノテーションでクラス・メソッドの役割を明示し、理解しやすくした。
4. **命名の工夫**  
   誰が読んでも分かりやすい名前を付けて、コードの可読性を高めた。
5. **自動テスト**  
   GitHub Actionsで結合テストを自動化し、品質管理を継続的に実施。
6. **拡張・保守性の向上**  
   小規模でも将来の拡張や保守がしやすい構造を意識して設計。

---

## 🚀 今後の課題・展望

- DTO導入  
  入力と出力の役割を分け、サービスの安全性を高める。
- 多様な検索機能  
  ID以外にも名前などで検索できるようにする。
- マスターテーブル管理  
  コースやステータスをマスタテーブルに分けて管理を効率化。
- 認証・認可機能  
  システムのセキュリティを強化する。
- ステータス履歴管理  
  ステータス変更をINSERTで記録し、詳細な分析を可能にする。
- ドメインモデル関連のテスト  
  `equals`/`hashCode`/`toString` やコンストラクタの単体テストを追加して品質を向上。
- フォーム・レスポンスクラスのテスト  
  バリデーションやドメイン変換メソッド（`toDomain` / `fromDomain`）のテストを拡充し、エラー防止と保守性を高める。


