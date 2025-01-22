package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class IRreportDetailList {
    private ArrayList<IRreportDetail> iRreportDetailList;

    public IRreportDetailList() {
        iRreportDetailList = new ArrayList<>();
    }

    public void addIRreportDetail(IRreportDetail iRreportDetail) {
        iRreportDetailList.add(iRreportDetail);
    }

    public void clearIRDetail(String irdID) {
        iRreportDetailList.removeIf(iRreportDetail -> iRreportDetail.getIdIRD().equals(irdID));
    }

    public void addOrUpdateIRreportDetail(IRreportDetail iRreportDetail) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < iRreportDetailList.size(); i++) {
            IRreportDetail existingDetail = iRreportDetailList.get(i);

            if (existingDetail.isId(iRreportDetail.getIdIRD())) {
                // Update existing item
                iRreportDetailList.set(i, iRreportDetail);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            iRreportDetailList.add(iRreportDetail);
        }
    }
    public void deleteIRreportDetail(IRreportDetail iRreportDetail) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < iRreportDetailList.size(); i++) {
            IRreportDetail existingDetail = iRreportDetailList.get(i);
            if (existingDetail.isId(iRreportDetail.getIdIRD())) {
                // Remove the item from the list
                iRreportDetailList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }

    public boolean isIdTRDExist(String idTRD) {
        for (IRreportDetail iRreportDetail : iRreportDetailList) {
            if (iRreportDetail.getIdTRD().equals(idTRD)) {
                return true;
            }
        }
        return false;
    }



    public IRreportDetail findIRDByTRD(String id) {
        for (IRreportDetail iRreportDetail : iRreportDetailList) {
            if (iRreportDetail.isTrd(id) ) {
                return iRreportDetail;
            }
        }
        return null;
    }

    public List<IRreportDetail> findAllIRDinIRById (String idIR) {
        List<IRreportDetail> result = new ArrayList<>();
        for (IRreportDetail ird : iRreportDetailList) {
            if (ird.getIdIR().equals(idIR)) {
                result.add(ird);
            }
        }
        return result;
    }

    public void clearItems() {
        iRreportDetailList.clear();
    }
}
