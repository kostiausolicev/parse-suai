package ru.kosti.vector_inform;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Applicant {
    private String snils;
    private boolean bvi;
    private int additionalPoints;
    private int priority;
    private int examsPoints; // Сумарное клоичество баллов за экзамены
    private int allPoints; // Экзамены + допы
    private List<Integer> points;
    private List<String> namesProfile;
    private boolean originalDocuments;
    private boolean consent;
}
