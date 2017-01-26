package com.radicalninja.anvil;

import com.beust.jcommander.JCommander;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final Anvil.Arguments anvilArgs = new Anvil.Arguments();
        new JCommander(anvilArgs, args);
        final Anvil anvil = new Anvil(anvilArgs);
        try {
            anvil.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
