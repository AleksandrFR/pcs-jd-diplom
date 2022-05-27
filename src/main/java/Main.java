import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static String listToJson(List<PageEntry> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<PageEntry>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void main(String[] args) throws Exception {
        var file = new File("pdfs");
        BooleanSearchEngine engine = new BooleanSearchEngine(file);
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    out.println("Привет пользователь!");
                    out.println("это поиск по словам в файлах pdf папки pdfs!");
                    out.println("введи слово для поиска(или quit для выхода):\n");   // обработка одного подключения

                    String word = in.readLine();

                    if (engine.search(word) == null) {
                        out.println("ФАЙЛЫ В ПАПКЕ НЕ СОДЕРЖАТ СЛОВО: " + word);
                    }
                    List<PageEntry> list = engine.search(word);
                    String answer = listToJson(list);
                    out.println(answer);

                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}