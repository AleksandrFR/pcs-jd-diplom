
import com.cedarsoftware.util.io.JsonWriter;
import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        String host = "localhost";
        int port = 8989;
        boolean b = true;
        while (b) {
            try (Socket clientSocket = new Socket(host, port);

                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                System.out.println("\n" + in.readLine());

                String inString;
                while (!clientSocket.isOutputShutdown()) {
                    if (in.ready()) {

                        System.out.println(in.readLine());
                        System.out.println(in.readLine());
                        String word = scanner.nextLine();
                        if (word.equals("quit")) {
                            System.out.println("выйти?(y/n)");
                            if (scanner.nextLine().equals("y")) {
                                b = false;
                                break;
                            }
                        }
                        out.println(word);

                        while (true) {
                            inString = in.readLine();
                            if (inString.startsWith("Ф")) {
                                System.out.println(inString);
                                break;
                            }
                            if (!StringUtil.isBlank(inString)) {
                                String niceFormattedJson = JsonWriter.formatJson(inString);
                                System.out.println(niceFormattedJson);
                                break;
                            }
                        }
                        clientSocket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
