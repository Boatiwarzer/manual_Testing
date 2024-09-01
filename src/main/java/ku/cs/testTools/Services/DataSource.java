package ku.cs.testTools.Services;

public interface DataSource <T> {
    T readData();
    void writeData(T t);
}
