package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;

@Data
public class IRreportDetailList {
    private ArrayList<IRreportDetail> iRreportDetailList;

    public IRreportDetailList() {
        iRreportDetailList = new ArrayList<>();
    }

    public void addIRreportDetail(IRreportDetail iRreportDetail) {
        iRreportDetailList.add(iRreportDetail);
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

    public IRreportDetail findTSById(String id) {
        for (IRreportDetail iRreportDetail : iRreportDetailList) {
            if (iRreportDetail.isId(id) ) {
                return iRreportDetail;
            }
        }
        return null;
    }

    public void clearItems() {
        iRreportDetailList.clear();
    }
}
