package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
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
            if (iRreport.getTrIR().equals(idTR) ) {
                return iRreport;
            }
        }
        return null;
    }
    public IRreport findIRById(String idIR) {
        for (IRreport iRreport : iRreportList) {
            if (iRreport.getIdIR().equals(idIR)) {
                return iRreport;
            }
        }
        return null;
    }

    public void clearIR(String idIR) {
        iRreportList.removeIf(iRreport -> iRreport.getTrIR().equals(idIR));
    }

    public void addIR(IRreport iRreport) {
        iRreportList.add(iRreport);
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

    public boolean isIdTRExist(String idTR) {
        for (IRreport iRreport : iRreportList) {
            if (iRreport.getTrIR().equals(idTR)) {
                return true;
            }
        }
        return false;
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
    public void findAllByIRreportId(String projectName, String tester) {
        IRreportList matchedIRreport = new IRreportList(); // เพื่อเก็บผลลัพธ์ที่ตรงกับเงื่อนไข

        // วนลูปผ่าน iRreportList เพื่อหาผลลัพธ์ที่ตรงกับเงื่อนไข
        for (IRreport IRreport : iRreportList) {
            // ตรวจสอบให้แน่ใจว่าไม่เป็น null ก่อนที่จะทำการเปรียบเทียบ
            boolean projectMatches = IRreport.getProjectName() != null && IRreport.getProjectName().trim().equalsIgnoreCase(projectName.trim());
            boolean testerMatches = IRreport.getTester() != null && IRreport.getTester().trim().equalsIgnoreCase(tester.trim());

            // ถ้าทุกเงื่อนไขตรงกันก็ให้เพิ่ม IRreport ไปยัง matchedIRreport
            if (projectMatches && testerMatches) {
                matchedIRreport.addIRreport(IRreport);
            }
        }

        // อัปเดต iRreportList ด้วยผลลัพธ์ที่ตรงกับเงื่อนไข
        iRreportList = matchedIRreport.getIRreportList();  // ถ้าต้องการแทนที่ iRreportList ด้วยผลลัพธ์ที่กรองแล้ว
        // หรือถ้าคุณไม่ต้องการแทนที่ iRreportList ก็สามารถจัดการแยกต่างหากได้
    }

}
