package com.example.raishtec.Hikaru.Suga.data;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseApplicationStatus {

  private int id;

  private int courseId;

  @NotBlank
  private String status;

}
