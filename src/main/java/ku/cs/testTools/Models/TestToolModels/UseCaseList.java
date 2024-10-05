package ku.cs.testTools.Models.TestToolModels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UseCaseList {
    private ArrayList<UseCase> useCaseList;

    public UseCaseList() {
        useCaseList = new ArrayList<UseCase>();
    }

    public void addUseCase(UseCase useCase) {
        useCaseList.add(useCase);
    }

    public ArrayList<UseCase> getUseCaseList() {
        return useCaseList;
    }

    public void setUseCaseList(ArrayList<UseCase> useCaseList) {
        this.useCaseList = useCaseList;
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
    public boolean isUseCaseIDExist(String useCaseID) {
        for (UseCase useCase : useCaseList) {
            if (useCase.getUseCaseID().equals(useCaseID)) {
                return true;
            }
        }
        return false;
    }
    public boolean isUseCaseNameExist(String useCaseName) {
        for (UseCase useCase : useCaseList) {
            if (useCase.getUseCaseName().equals(useCaseName)) {
                return true;
            }
        }
        return false;
    }
    public void sort(Comparator<UseCase> cmp) {
        Collections.sort(useCaseList, cmp);
    }
}