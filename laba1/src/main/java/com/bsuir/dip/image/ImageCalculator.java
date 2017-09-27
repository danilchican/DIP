package com.bsuir.dip.image;

import com.bsuir.dip.type.Channel;
import org.opencv.core.Mat;

class ImageCalculator {

    private static final int HIST_WIDTH = 256;
    private static final int MAX_PIX_VALUE = HIST_WIDTH - 1;
    private static final int MIN_PIX_VALUE = 0;

    /**
     * Calculate histogram data.
     *
     * @param pixels
     * @return histogram data yAxis
     */
    public static int[] calcChannelHist(int[] pixels) {
        int[] hist = new int[HIST_WIDTH];

        for (int pixel : pixels) {
            hist[pixel] += 1;
        }

        return hist;
    }

    /**
     * Binary preparing.
     *
     * @param pixels
     * @param threshold
     * @return pixels
     */
    public static int[] calcBinaryPreparing(final int[] pixels, final int threshold) {
        int[] output = new int[pixels.length];

        for (int i = 0; i < pixels.length; i++) {
            output[i] = (pixels[i] >= threshold) ? MAX_PIX_VALUE : MIN_PIX_VALUE;
        }

        return output;
    }

    /**
     * Preparing.
     *
     * @param pixels
     * @param left
     * @param right
     * @return pixels
     */
    public static int[] calcPreparing(final int[] pixels, final int left, final int right) {
        int[] output = new int[pixels.length];

        for (int i = 0; i < pixels.length; i++) {
            output[i] = (pixels[i] >= left && pixels[i] <= right) ? MAX_PIX_VALUE : MIN_PIX_VALUE;
        }

        return output;
    }

    /**
     * Sobel operator filter.
     *
     * @param image
     * @return computed pixels
     */
    public static int[] calcSobel(Image image) {
        Mat oldImg = image.getImg();
        Mat img = new Mat();
        oldImg.copyTo(img);

        for (int i = 1; i < image.getHeight() - 1; i++) {
            for (int j = 1; j < image.getWidth() - 1; j++) {
                int blueChannel = calcSobelChannel(i, j, oldImg, Channel.BLUE);

                if (image.isGrayScale()) {
                    img.put(i, j, blueChannel);
                    continue;
                }

                int greenChannel = calcSobelChannel(i, j, oldImg, Channel.GREEN);
                int redChannel = calcSobelChannel(i, j, oldImg, Channel.RED);

                double[] pix = new double[]{redChannel, greenChannel, blueChannel};

                img.put(i, j, pix);
            }
        }

        return ImageConverter.convertToPixels(new Image(img));
    }

    private static int calcSobelChannel(final int j, final int i, final Mat image, Channel channel) {
        final int[] core1 = {
                1, 0, -1,
                2, 0, -2,
                1, 0, -1
        };

        final int[] core2 = {
                -1, -2, -1,
                0, 0, 0,
                1, 2, 1
        };

        final int index = channel.getIndex();

        int h1 = (int) (core1[0] * image.get(j - 1, i - 1)[index]
                + core1[1] * image.get(j, i - 1)[index]
                + core1[2] * image.get(j + 1, i - 1)[index]
                + core1[3] * image.get(j - 1, i)[index]
                + core1[4] * image.get(j, i)[index]
                + core1[5] * image.get(j + 1, i)[index]
                + core1[6] * image.get(j - 1, i + 1)[index]
                + core1[7] * image.get(j, i + 1)[index]
                + core1[8] * image.get(j + 1, i + 1)[index]);

        int h2 = (int) (core2[0] * image.get(j - 1, i - 1)[index]
                + core2[1] * image.get(j, i - 1)[index]
                + core2[2] * image.get(j + 1, i - 1)[index]
                + core2[3] * image.get(j - 1, i)[index]
                + core2[4] * image.get(j, i)[index]
                + core2[5] * image.get(j + 1, i)[index]
                + core2[6] * image.get(j - 1, i + 1)[index]
                + core2[7] * image.get(j, i + 1)[index]
                + core2[8] * image.get(j + 1, i + 1)[index]);

        int channelValue = (int) Math.sqrt(Math.pow(h1, 2) + Math.pow(h2, 2));

        channelValue = channelValue > MAX_PIX_VALUE ? MAX_PIX_VALUE : channelValue;
        channelValue = channelValue < MIN_PIX_VALUE ? MIN_PIX_VALUE : channelValue;

        return channelValue;
    }
}
