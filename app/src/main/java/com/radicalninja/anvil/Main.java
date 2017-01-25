package com.radicalninja.anvil;

import com.beust.jcommander.JCommander;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final Anvil anvil = new Anvil();
        new JCommander(anvil, args);
        try {
            anvil.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
