package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class TestCaseDetail {
    @Id
    private String idTCD;
    private String nameTCD;
    private String variableTCD;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCaseDetail that = (TestCaseDetail) o;
        return getIdTCD() != null && Objects.equals(getIdTCD(), that.getIdTCD());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
