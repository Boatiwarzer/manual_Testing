package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private String dateTS;
    private String useCase;
    private String descriptionTS;
    private String testCase;
    private String preCon;
    private String freeText;
    @OneToMany(mappedBy = "testScript")
    private List<TestScriptDetail> testScriptDetailList;

    public TestScript(String idTS, String nameTS, String dateTS, String useCase, String descriptionTS, String testCase, String preCon, String freeText) {
        this.idTS = idTS;
        this.nameTS = nameTS;
        this.dateTS = dateTS;
        this.useCase = useCase;
        this.descriptionTS = descriptionTS;
        this.testCase = testCase;
        this.preCon = preCon;
        this.freeText = freeText;
    }

    public TestScript(String idTS, String nameTS, String dateTS, String descriptionTS, String preCon, String useCase, String testCase, String freeText, List<TestScriptDetail> testScriptDetailList) {
        this.idTS = idTS;
        this.nameTS = nameTS;
        this.dateTS = dateTS;
        this.useCase = useCase;
        this.descriptionTS = descriptionTS;
        this.testCase = testCase;
        this.preCon = preCon;
        this.freeText = freeText;
        this.testScriptDetailList = testScriptDetailList;
    }
    public void setTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.dateTS = now.format(dtf);
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

    public boolean isId(String id) {
        return this.idTS.equals(id);
    }
}
