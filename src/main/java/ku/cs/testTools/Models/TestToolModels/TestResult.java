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
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {
    @Id
    private String id;
    private String name;
    private String testSC;
    private LocalDateTime date;
    private String description;
    private String testStep;
    private String expected;
    private String actual;
    private String status;
    private String tester;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult testResult = (TestResult) o;
        return getId() != null && Objects.equals(getId(), testResult.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
