package ru.kosti;

import lombok.*;
import ru.kosti.applicant.ApplicantData;
import ru.kosti.vector_inform.VectorData;
import ru.kosti.vector_list.VectorList;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseType {
    private String uuid;
    private long tgId;
    private String vectorName;
    private ApplicantData applicantData;
    private VectorData vectorData;
    private List<String> vectorList;
    private byte[] file;

    public boolean hasVectorList() {
        return vectorList != null;
    }

    public boolean hasApplicantData() {
        return applicantData != null;
    }

    public boolean hasVectorInformation() {
        return vectorData != null;
    }

    public boolean hasFile() {
        return file != null;
    }
}
