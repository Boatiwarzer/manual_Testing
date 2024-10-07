package ku.cs.testTools.Models.TestToolModels;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

    public UseCaseDetail getUseCaseDetail(String useCaseID) {
        for (UseCaseDetail useCaseDetail : useCaseDetailList) {
            if (useCaseDetail.getUseCaseID().equals(useCaseID)) {
                return useCaseDetail;
            }
        }
        return null;
    }

    public UseCaseDetail findByUseCaseId(String useCaseId) {
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

//    private List<UseCaseDetail> useCaseDetailList;
    public List<UseCaseDetail> getUseCaseDetailList() {
        return useCaseDetailList;
    }
    public boolean isDetailExist(String useCaseID, String action, int number, String detail) {
        for (UseCaseDetail useCaseDetail : useCaseDetailList) {
            if (useCaseDetail.getUseCaseID().equals(useCaseID) &&
                    useCaseDetail.getAction().equals(action) &&
                    useCaseDetail.getNumber() == number &&
                    useCaseDetail.getDetail().equals(detail)) {
                return true; // ถ้าเจอ UseCaseDetail ที่มี detail ซ้ำ ให้ return true
            }
        }
        return false; // ถ้าไม่มี detail ซ้ำ ให้ return false
    }
}
