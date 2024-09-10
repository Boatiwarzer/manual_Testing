package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Models.UsecaseModels.ActorList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TestScriptServices implements DataSource<TestScriptList>, ManageDataSource<TestScript> {
    private String directory;
    private String fileName;

    public ActorListFileDataSource(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directory + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TestScriptList readData() {
        TestScriptList testScriptList = new TestScriptList();
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals("actor")) {
                    Actor actor = new Actor(
                            Integer.parseInt(data[1].trim()), // actorID
                            data[2].trim(), // actorName
                            data[3].trim(), // alias
                            data[4].trim(), // description
                            data[5].trim(), // note
                            Integer.parseInt(data[6].trim()) // positionID
                    );
                    actorList.addActor(actor);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return actorList;
    }