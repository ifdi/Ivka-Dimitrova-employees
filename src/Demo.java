import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Demo {

    public static final String SEPARATOR = ",";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter file path: ");
        String csvFilePath = sc.next();

        List<String[]> parsedRows = parseFile(csvFilePath);

        HashMap<Integer, List<Record>> recordsByProject = new HashMap<>();

        for (String[] row : parsedRows) {
            Record record = mapRowToRecord(row);
            if (!recordsByProject.containsKey(record.getProjectId())) {
                recordsByProject.put(record.getProjectId(), new ArrayList<>());
            }
            recordsByProject.get(record.getProjectId()).add(record);
        }

        HashMap<String, Long> pairsTimeMap = new HashMap<>();
        for (Map.Entry<Integer, List<Record>> e : recordsByProject.entrySet()) {
            List<Record> values = e.getValue();
            values.sort(Comparator.comparing(Record::getEmployeeId));
            for (int i = 0; i < values.size(); i++) {
                Record recordI = values.get(i);
                for (int j = i + 1; j < values.size(); j++) {
                    Record recordJ = values.get(j);
                    long commonTime = recordI.commonDays(recordJ);
                    String pairsEmployeeId = recordI.getEmployeeId() + SEPARATOR + recordJ.getEmployeeId();
                    if (!pairsTimeMap.containsKey(pairsEmployeeId)) {
                        pairsTimeMap.put(pairsEmployeeId, 0L);
                    }
                    pairsTimeMap.put(pairsEmployeeId, pairsTimeMap.get(pairsEmployeeId) + commonTime);
                }
            }
        }

        long maxCommonTime = 0;
        int employeeId1 = -1;
        int employeeId2 = -1;
        for (Map.Entry<String, Long> e : pairsTimeMap.entrySet()) {
            if (e.getValue() > maxCommonTime) {
                maxCommonTime = e.getValue();
                String[] pairsEmployees = e.getKey().split(SEPARATOR);
                employeeId1 = Integer.parseInt(pairsEmployees[0]);
                employeeId2 = Integer.parseInt(pairsEmployees[1]);
            }
        }

        if (employeeId1 < 0 || employeeId2 < 0) {
            System.out.println("There are no pairs of employees who worked together on common projects and in the same time.");
        } else {
            System.out.println("Employee with ID " + employeeId1 + " and employee with ID " + employeeId2 +
                    " worked " + maxCommonTime + " days on common projects.");
        }
    }

    static List<String[]> parseFile(String csvFilePath) {
        String[] row;
        List<String[]> allRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                //Skip header row and any empty row
                if (line.startsWith("EmpID") || line.isBlank()) {
                    continue;
                }

                row = line.split(SEPARATOR);
                allRows.add(row);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(csvFilePath + " is an invalid file");
        }
        return allRows;
    }

    static Record mapRowToRecord(String[] row) {
        return new Record(row[0].trim(), row[1].trim(), row[2].trim(), row[3].trim());
    }
}