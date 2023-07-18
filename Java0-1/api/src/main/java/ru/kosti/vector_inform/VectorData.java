package ru.kosti.vector_inform;

import lombok.*;
import ru.kosti.applicant.Applicant;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VectorData {
    private String link;
    private int budgetPlaces;
    private int contractPlaces;
    private List<String> exams;
    private MinimalPoints minimalPointsBudget;
    private MinimalPoints minimalPointsContract;
    private List<Applicant> vector;
}
