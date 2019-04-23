package examples;

import java.io.File;
import java.util.Scanner;

import examples.components.RoutedMessenger;
import examples.media.pipes.*;
import midas.Midas;

public class PipesExample {
    public static void main(String[] args) {
        File pipesDir = new File("pipes");

        try {
            pipesDir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RoutedMessenger.Dispatcher dispatcher = new RoutedMessenger.Dispatcher(
            args[0],
            msg -> System.out.println(String.join(" -> ", msg.routes) + ": " + msg.message)
        );
        Midas midas = new Midas("PipesExample")
            .addDispatcher(dispatcher);

        for (int i = 1; i < args.length; i++) {
            midas.addMedium(new Writer("pipes/" + args[0] + "-" + args[i]));
            midas.addMedium(new Reader("pipes/" + args[i] + "-" + args[0]));
        }

        System.out.println("Ready");

        Scanner in = new Scanner(System.in);

        while (true) {
            String cmd = in.nextLine();

            if (cmd.trim().isEmpty()) {
                break;
            }

            int index = cmd.indexOf(" ");

            dispatcher.send(cmd.substring(0, index), cmd.substring(index + 1));
        }

        try {
            if (pipesDir.exists()) {
                for (String pipePath: pipesDir.list()) {
                    File pipe = new File(pipesDir.getPath(), pipePath);
                    pipe.delete();
                }
                pipesDir.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        in.close();
        System.exit(0);
    }
}