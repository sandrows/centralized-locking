import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    private static final int PORT_NUMBER = 1234;
    private static final int WAIT_TIME = 10; // Seconds

    private static Lock addLock = new ReentrantLock(); // Addition
    private static Lock subLock = new ReentrantLock(); // Subtraction

    public static void main(String[] args) throws IOException {

        assert (PORT_NUMBER > 0 && PORT_NUMBER < 65536) : "Invalid port!";

        try (ServerSocket conn = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Running on port: " + PORT_NUMBER);

            while (true) {
                // Each connection has its own thread
                new Handler(conn.accept()).start();
                //TODO user input for exiting
            }
        }
    }

    private static class Handler extends Thread {

        private Scanner srv_in;
        private PrintStream srv_out;

        public Handler(Socket srv_soc) throws IOException {
            this.srv_in = new Scanner(srv_soc.getInputStream());
            this.srv_out = new PrintStream(srv_soc.getOutputStream());
        }

        @Override
        public void run() {
            while (true) {
                srv_out.println("Which resource you want to acquire? (A/B)");
                String resource = srv_in.nextLine();

                switch (resource) {
                    case "A":
                    case "a":
                        try {
                            if (acquireLock(addLock)) {
                                try {
                                    addition(getNumbers("addition"));
                                } finally {
                                    addLock.unlock();
                                }
                            }
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case "B":
                    case "b":
                        try {
                            if (acquireLock(subLock)) {
                                try {
                                    subtraction(getNumbers("subtraction"));
                                } finally {
                                    subLock.unlock();
                                }
                            }
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        break;
                }
            }
        }

        // Resource A
        private void addition(int[] numbers) {
            srv_out.println("Sum is: " + (numbers[0] + numbers[1]));
        }

        // Resource B
        private void subtraction(int[] numbers) {
            srv_out.println("Difference is: " + (numbers[0] - numbers[1]));
        }

        private int[] getNumbers(String operation) {
            srv_out.println("Enter two space separated numbers for " + operation + ":");
            String[] numbers = srv_in.nextLine().split(" ");
            int num1 = Integer.parseInt(numbers[0]);
            int num2 = Integer.parseInt(numbers[1]);
            return new int[]{num1, num2};
        }

        private boolean acquireLock(Lock lock) throws InterruptedException {
            srv_out.println("Acquiring...");

            if (!lock.tryLock(WAIT_TIME, TimeUnit.SECONDS)) {
                srv_out.println("ERROR Maximum waiting time exceeded!");
                return false;
            }

            return true;
        }

    }

}
