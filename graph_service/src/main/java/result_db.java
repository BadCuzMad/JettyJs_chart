import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//загоняем в базу значения из определенного файла, получаем отработанные значения, загоняем в другую базу, анализируем
//итого две базы данных prepared statement и все такое
//подключиться к бд, заполнить первую БД, взять данные из БД, сделать по ним график.
public class result_db {
//f.replaceAll("[^0-9|,-:]","");

    public static void fillInputDb() throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("graph_service/src/main/resources/db/output.txt"));

        String crutch = ", ";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/signal_db", "postgres", "qwerty")) {
            System.out.println("Conn ok");
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS signal(\n" +
                        "model int,\n" +
                        "rd int,\n" +
                        "dopb int,\n" +
                        "ssh int,\n" +
                        "rep int,\n" +
                        "step int,\n" +
                        "param int,\n" +
                        "decibel int,\n" +
                        "skorost real,\n" +
                        "koef_shuma real,\n" +
                        "shum real);");
                stmt.executeUpdate("TRUNCATE TABLE signal");
                String query = "insert into signal(model, rd, dopb, ssh, rep, step, param, decibel, skorost, koef_shuma, shum)" +
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                try (Stream<String> lines = Files.lines(Paths.get("graph_service/src/main/resources/db/table_2020_08_27_06-43-15.txt"))) {
                    List<String> data = lines.collect(Collectors.toList());
                    for (String datum : data) {
                        String[] temp_arr;
                        String s = datum.replaceAll("[^0-9|,-:\\s]", "").
                                replaceAll(crutch, ":").
                                replaceAll(",", ".").
                                replaceAll(" ","");
                        temp_arr = s.split(":");
                        writer.write(s +'\n');
                        preparedStatement.setInt(1, Integer.parseInt(temp_arr[0]));
                        preparedStatement.setInt(2, Integer.parseInt(temp_arr[1]));
                        preparedStatement.setInt(3, Integer.parseInt(temp_arr[2]));
                        preparedStatement.setInt(4, Integer.parseInt(temp_arr[3]));
                        preparedStatement.setInt(5, Integer.parseInt(temp_arr[4]));
                        preparedStatement.setInt(6,Integer.parseInt(temp_arr[5]));
                        preparedStatement.setInt(7,Integer.parseInt(temp_arr[6]));
                        preparedStatement.setInt(8,Integer.parseInt(temp_arr[7]));
                        preparedStatement.setDouble(9, Double.parseDouble(temp_arr[8]));
                        preparedStatement.setDouble(10, Double.parseDouble(temp_arr[9]));
                        preparedStatement.setDouble(11, Double.parseDouble(temp_arr[10]));
                        preparedStatement.execute();
                    }

                }

            }
            writer.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void fillOutputDb() {

    }
}
