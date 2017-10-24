package com.bsuir.danilchican.network;

public class Network {

    /**
     * Matrix size.
     */
    public static final int N = 10;

    private int[][] teachMatrix = new int[N][N];

    public Network() {
        init();
    }

    private void init() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                teachMatrix[i][j] = (i != j) ? 1 : 0;
            }
        }
    }
}
