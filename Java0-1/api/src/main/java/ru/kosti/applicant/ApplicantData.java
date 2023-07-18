package ru.kosti.applicant;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantData {
    private String actuality_date;
    private int position;
}
