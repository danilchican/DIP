package com.bsuir.danilchican;

import com.bsuir.danilchican.network.Network;

public class App {

    public static void main(String[] args) {
        Network network = new Network();
        network.loadImages();
        network.teach();
    }
}
