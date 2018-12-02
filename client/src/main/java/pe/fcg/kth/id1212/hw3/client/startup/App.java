package pe.fcg.kth.id1212.hw3.client.startup;

import pe.fcg.kth.id1212.hw3.client.view.CommandLineInterpreter;

public class App {
    public static void main(String[] args) {
        if(args.length == 1) {
            try {
                String host = args[0];
                new CommandLineInterpreter(host).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Host argument missing.");
            System.exit(1);
        }
    }
}
