# StudentManagement

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
| 論理削除       | 指定した受講生を論理的に削除する                      | DELETE   | `/deleteStudent/{id}`  |
| 受講生取得(ID) | 指定したIDの受講生詳細を取得する                      | GET      | `/student/{id}`        |
| 受講生一覧     | 全ての受講生情報（論理削除されていないもの）を取得する | GET      | `/studentList`         |

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


### シーケンス図

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
