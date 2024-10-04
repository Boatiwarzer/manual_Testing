package ku.cs.testTools.Models.TestToolModels;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
@Data
public class UseCaseList {
    private ArrayList<UseCase> useCaseList;

    public UseCaseList() {
        useCaseList = new ArrayList<UseCase>();
    }

    public void addUseCase(UseCase useCase) {
        useCaseList.add(useCase);
    }

    public void removeUseCase(UseCase useCase) {
        useCaseList.remove(useCase);
    }

    public int getSize() {
        return useCaseList.size();
    }


    public UseCase findByUseCaseId(String useCaseId) {
        for (UseCase useCase : useCaseList) {
            if (useCase.getUseCaseID().equals(useCaseId)) {
                return useCase;
            }
        }
        return null;
    }
    public boolean isUseCaseNameExist(String useCaseName) {
        for (UseCase useCase : useCaseList) {
            if (useCase.getUseCaseName().equals(useCaseName)) {
                return true;
            }
        }
        return false;
    }
}