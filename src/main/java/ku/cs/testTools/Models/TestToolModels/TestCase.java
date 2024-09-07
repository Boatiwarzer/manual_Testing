package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class TestCase {
    @Id
    private String idTC;
    private LocalDateTime dateTC;
    private String useCase;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCase testCase = (TestCase) o;
        return getIdTC() != null && Objects.equals(getIdTC(), testCase.getIdTC());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
