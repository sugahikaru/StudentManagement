package com.example.raishtec.Hikaru.Suga.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
@Schema(description = "受講生詳細")
@Getter
@Setter
public class Student {

  @Pattern(regexp = "^\\d+$", message = "数字のみを入力してください。")
  private String id;
  @NotNull
  private String fullname;
  @NotBlank
  @Pattern(regexp = "^[ぁ-ん]+$", message = "ひらがなのみを入力してください。")
  private String furigana;
  @Email(message = "有効なメールアドレスを入力してください。")
  private String email;
  @NotEmpty
  private String city;
  @Positive
  private int age;
  @Pattern(regexp = "^(man|women)$", message = "値は 'man' または 'women' のいずれかである必要があります。")
  private String gender;
  private String remark;
  private boolean isDeleted;

  }

