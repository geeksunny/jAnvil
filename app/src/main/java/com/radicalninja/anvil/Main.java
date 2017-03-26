package com.radicalninja.anvil;

import com.beust.jcommander.JCommander;

public class Main {

    public static void main(String[] args) {
        final Anvil.Arguments anvilArgs = new Anvil.Arguments();
        new JCommander(anvilArgs, args);
        final Anvil anvil = new Anvil(anvilArgs);
        try {
            System.out.println("Starting job.");
            anvil.run();
            System.out.println("Job's finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
