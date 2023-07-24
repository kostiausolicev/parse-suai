package ru.kosti.vector_inform;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VectorData {
    private String name;
    private String linkBudget;
    private String linkContract;
    private String linkContractAbroad;
    private String linkSeparate;
    private String linkSpecial;
    private int budgetPlaces;
    private int contractPlaces;
    private int separatePlaces;
    private int contractAbroadPlaces;
    private int specialPlaces;
    private List<String> exams;
    private MinimalPoints minimalPointsBudget;
    private List<Applicant> vector;
}
