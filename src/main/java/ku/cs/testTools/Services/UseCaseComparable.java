package ku.cs.testTools.Services;

import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Models.TestToolModels.UseCase;

import java.util.Comparator;

public class UseCaseComparable implements Comparator<UseCase> {
    @Override
    public int compare(UseCase o1, UseCase o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
