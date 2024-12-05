package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Data
@AllArgsConstructor
public class IRreportList {
    private ArrayList<IRreport> iRreportList;

    public IRreportList() {
        iRreportList = new ArrayList<IRreport>();
    }
    public void addIRreport(IRreport iRreport) {
        iRreportList.add(iRreport);
    }
    public void clear() {
        iRreportList.clear();
    }

    public IRreport findTRById(String idTR) {
        for (IRreport iRreport : iRreportList) {
            if (iRreport.isId(idTR) ) {
                return iRreport;
            }
        }
        return null;
    }
    public void addOrUpdateIRreport(IRreport iRreport) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID or name
        for (int i = 0; i < iRreportList.size(); i++) {
            IRreport existing = iRreportList.get(i);

            if (existing.isId(iRreport.getIdIR())) {
                // Update existing item
                iRreportList.set(i, iRreport);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            iRreportList.add(iRreport);
        }
    }

    public void deleteIRreport(IRreport iRreport) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < iRreportList.size(); i++) {
            IRreport existing = iRreportList.get(i);
            if (existing.isId(iRreport.getIdIR())) {
                // Remove the item from the list
                iRreportList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    public void sort(Comparator<IRreport> cmp) {
        Collections.sort(iRreportList, cmp);
    }
}
