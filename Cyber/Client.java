import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("www.google.com", 80)); //find the actual ip address
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client Started--- ");
        String ip=socket.getLocalAddress().getHostAddress();
        System.out.println(ip);
        NetworkConnection nc = new NetworkConnection("192.168.0.117", 12345); //Connection to the server ip and port

        System.out.println("Connected to The Server");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a User Name:");
        String uname = sc.nextLine();

        nc.write(uname);

        // Create and start threads
        Thread writer = new Thread(new writerThread(nc, uname));
        Thread reader = new Thread(new readerThread(nc));

        writer.start();
        reader.start();

        try {
            writer.join(); // Wait for writer thread to complete
            reader.join(); // Wait for reader thread to complete
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
        }

        System.out.println("Client shutting down...");
    }
}

class writerThread implements Runnable {
    private final NetworkConnection nc;
    private final String uname;

    writerThread(NetworkConnection nc, String uname) {
        this.uname = uname;
        this.nc = nc;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1. List");
            System.out.println("2. Message");
            System.out.println("Choose an option:");
            int option;
            try {
                option = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (option) {
                case 1:
                    String listRequest = uname + "#" + uname + "#list";


                    nc.write(listRequest);
                    break;
                case 2:
                    System.out.println("Enter User Name:");
                    String name = sc.nextLine();
                    System.out.println("Enter Message:");
                    String msg = sc.nextLine();
                    Cipher cipher=new Cipher();
                    String playen= cipher.playfairEncrypt(msg);
                    String transEn=cipher.transposeEncrypt(playen);
                    String sendMessage = uname + "#" + name + "#send#" + transEn;

                    nc.write(sendMessage);
                    System.out.println("Message sent...");
                    break;
                default:
                    System.out.println("Invalid option, please choose 1 or 2.");
            }
        }
    }
}

class readerThread implements Runnable {
    private final NetworkConnection nc;

    readerThread(NetworkConnection nc) {
        this.nc = nc;
    }

    @Override
    public void run() {
        Cipher cipher=new Cipher();
        while (true) {
            String enmsg = (String) nc.read();
            if (enmsg.equals(enmsg.toLowerCase())){
                System.out.println(enmsg);
            }
            else {
                String transDec=cipher.transposeDecrypt(enmsg);
                String playdecr=cipher.playfairDecrypt(transDec,"Cyber");
                if (playdecr != null && !playdecr.isEmpty()) {
                    System.out.println("Message Received: " + playdecr);
                } else {
                    System.out.println("No message received or connection lost.");
                }
            }

        }
    }
}
