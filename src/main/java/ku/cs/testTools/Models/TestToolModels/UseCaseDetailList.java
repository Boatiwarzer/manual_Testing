package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.ArrayList;
@Data
public class UseCaseDetailList {
    private ArrayList<UseCaseDetail> useCaseDetailList;

    public UseCaseDetailList() {
        useCaseDetailList = new ArrayList<UseCaseDetail>();
    }

    public void addUseCaseDetail(UseCaseDetail useCaseDetail) {
        useCaseDetailList.add(useCaseDetail);
    }

    public void removeUseCaseDetail(UseCaseDetail useCaseDetail) {
        useCaseDetailList.remove(useCaseDetail);
    }

    public ArrayList<UseCaseDetail> getUseCaseDetailList() {
        return useCaseDetailList;
    }

    public void setUseCaseDetailList(ArrayList<UseCaseDetail> useCaseDetailList) {
        this.useCaseDetailList = useCaseDetailList;
    }

    public UseCaseDetail getUseCaseDetail(String useCaseID) {
        for (UseCaseDetail useCaseDetail : useCaseDetailList) {
            if (useCaseDetail.getUseCaseID().equals(useCaseID)) {
                return useCaseDetail;
            }
        }
        return null;
    }

    public UseCaseDetail findByUseCaseId(String  useCaseId) {
        for (UseCaseDetail useCaseDetail : useCaseDetailList) {
            if (useCaseDetail.getUseCaseID().equals(useCaseId)) {
                return useCaseDetail;
            }
        }
        return null;
    }

    public void clear() {
        useCaseDetailList.clear();
    }

    // clear use case detail that contains use Case ID
    public void clearUseCaseDetail(String useCaseID) {
        useCaseDetailList.removeIf(useCaseDetail -> useCaseDetail.getUseCaseID().equals(useCaseID));
    }
}
