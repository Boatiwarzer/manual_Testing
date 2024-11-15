package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.TestFlowPosition;
import ku.cs.testTools.Models.TestToolModels.TestFlowPositionList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TestFlowPositionListFileDataSource implements DataSource<TestFlowPositionList>, ManageDataSource<TestFlowPosition>{
    private String directory;
    private String fileName;

    public TestFlowPositionListFileDataSource(String directory, String fileName) {
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
    public TestFlowPositionList readData() {
        TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null)
            {
                String[] data = line.split(",");
                if (data[0].trim().equals("position")) {
                    TestFlowPosition testFlowPosition = new TestFlowPosition(
                            Integer.parseInt(data[1].trim()), // positionID
                            Double.parseDouble(data[2].trim()), // xPosition
                            Double.parseDouble(data[3].trim()), // yPosition
                            Double.parseDouble(data[4].trim()), // fitWidth
                            Double.parseDouble(data[5].trim()), // fitHeight
                            Double.parseDouble(data[6].trim()) // rotation
                            //Integer.parseInt(data[7].trim()) // subsystemID
                    );
                    testFlowPositionList.addPosition(testFlowPosition);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
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
        return testFlowPositionList;
    }

    @Override
    public void writeData(TestFlowPositionList testFlowPositionList) {
//        // Import actorList from CSV
//        ActorListFileDataSource actorListFileDataSource = new ActorListFileDataSource(directory, fileName);
//        ActorList actorList = actorListFileDataSource.readData();
//        // Import componentPreferenceList from CSV
//        ComponentPreferenceListFileDataSource componentPreferenceListFileDataSource = new ComponentPreferenceListFileDataSource(directory, fileName);
//        ComponentPreferenceList componentPreferenceList = componentPreferenceListFileDataSource.readData();
//        // Import connectionList from CSV
//        ConnectionListFileDataSource connectionListFileDataSource = new ConnectionListFileDataSource(directory, fileName);
//        ConnectionList connectionList = connectionListFileDataSource.readData();
//        // Import noteList from CSV
//        NoteListFileDataSource noteListFileDataSource = new NoteListFileDataSource(directory, fileName);
//        NoteList noteList = noteListFileDataSource.readData();
//        // Import preferenceList from CSV
//        PreferenceListFileDataSource preferenceListFileDataSource = new PreferenceListFileDataSource(directory, fileName);
//        PreferenceList preferenceList = preferenceListFileDataSource.readData();
//        // Import subsystemList from CSV
//        SubSystemListFileDataSource subsystemListFileDataSource = new SubSystemListFileDataSource(directory, fileName);
//        SubSystemList subsystemList = subsystemListFileDataSource.readData();
//        // Import useCaseDetailList from CSV
//        ku.cs.testTools.Services.UsecaseServices.UseCaseDetailListFileDataSource useCaseDetailListFileDataSource = new UseCaseDetailListFileDataSource(directory, fileName);
//        UseCaseDetailList useCaseDetailList = useCaseDetailListFileDataSource.readData();
//        // Import useCaseList from CSV
//        ku.cs.testTools.Services.UsecaseServices.UseCaseListFileDataSource useCaseListFileDataSource = new UseCaseListFileDataSource(directory, fileName);
//        UseCaseList useCaseList = useCaseListFileDataSource.readData();
//        // Import useCaseSystemList from CSV
//        UseCaseSystemListFileDataSource useCaseSystemListFileDataSource = new UseCaseSystemListFileDataSource(directory, fileName);
//        UseCaseSystemList useCaseSystemList = useCaseSystemListFileDataSource.readData();
//
//        //File writer
//        String filePath = directory + File.separator + fileName;
//        File file = new File(filePath);
//        FileWriter writer = null;
//        BufferedWriter buffer = null;
//        try {
//            writer = new FileWriter(file, StandardCharsets.UTF_8);
//            buffer = new BufferedWriter(writer);
//
//            // Write ActorList to CSV
//            for (Actor actor : actorList.getActorList()) {
//                String line = actorListFileDataSource.createLine(actor);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write ComponentPreferenceList to CSV
//            for (ComponentPreference componentPreference : componentPreferenceList.getComponentPreferenceList()) {
//                String line = componentPreferenceListFileDataSource.createLine(componentPreference);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write ConnectionList to CSV
//            for (Connection connection : connectionList.getConnectionList()) {
//                String line = connectionListFileDataSource.createLine(connection);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write NoteList to CSV
//            for (Note note : noteList.getNoteList()) {
//                String line = noteListFileDataSource.createLine(note);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write PositionList to CSV
//            for (Position position : positionList.getPositionList()) {
//                String line = createLine(position);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write PreferenceList to CSV
//            for (Preference preference : preferenceList.getPreferenceList()) {
//                String line = preferenceListFileDataSource.createLine(preference);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write subSystemList to CSV
//            for (SubSystem subsystem : subsystemList.getSubSystemList()) {
//                String line = subsystemListFileDataSource.createLine(subsystem);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write UseCaseDetailList to CSV
//            for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
//                String line = useCaseDetailListFileDataSource.createLine(useCaseDetail);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write UseCaseList to CSV
//            for (UseCase useCase : useCaseList.getUseCaseList()) {
//                String line = useCaseListFileDataSource.createLine(useCase);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            // Write UseCaseSystemList to CSV
//            for (UseCaseSystem useCaseSystem :useCaseSystemList.getSystemList()){
//                String line = useCaseSystemListFileDataSource.createLine(useCaseSystem);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            buffer.close();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }


    @Override
    public String createLine(TestFlowPosition testFlowPosition) {
        return null;
    }
}
