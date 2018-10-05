import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int PORT_NUMBER = 1234;
    private static final String SERVER = "localhost";

    public static void main(String[] args) throws IOException {

        assert (PORT_NUMBER > 0 && PORT_NUMBER < 65536) : "Invalid port!";

        //TODO If server is not running
        Socket cli_soc = new Socket(SERVER, PORT_NUMBER);

        Scanner cli_in = new Scanner(cli_soc.getInputStream());
        PrintStream cli_out = new PrintStream(cli_soc.getOutputStream());
        Scanner user_input = new Scanner(System.in);

        while (true) {
            // Which resource?
            System.out.println(cli_in.nextLine());

            if(user_input.nextLine().matches("[Yy]")) cli_out.println("Y");
            else{
                cli_out.println("N");
                return;
            }

            System.out.println(cli_in.nextLine());

            // Operation
            String status = cli_in.nextLine();
            if (status.startsWith("ERROR")) {
                System.out.println(status.substring(6));
                System.out.println();
                continue;
            } else {
                System.out.println(status);
                cli_out.println(user_input.nextLine());
            }

            // Response
            String response = cli_in.nextLine();
            System.out.println(response + "\n");

            //TODO user input for exiting
        }
    }

}
