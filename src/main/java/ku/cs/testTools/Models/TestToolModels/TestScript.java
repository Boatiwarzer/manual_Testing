package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class TestScript {
    @Id
    private String idTS;
    private String nameTS;
    private LocalDateTime dateTS;
    private String useCase;
    private String descriptionTS;
    private String testCase;
    private String preCon;
    private String freeText;

    public TestScript(String idTS, String nameTS, LocalDateTime dateTS, String descriptionTS, String preCon, String trim5, String trim6, String freeText) {
        this.idTS = idTS;
        this.nameTS = nameTS;
        this.dateTS = dateTS;
        this.useCase = useCase;
        this.descriptionTS = descriptionTS;
        this.testCase = testCase;
        this.preCon = preCon;
        this.freeText = freeText;
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestScript that = (TestScript) o;
        return getIdTS() != null && Objects.equals(getIdTS(), that.getIdTS());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
