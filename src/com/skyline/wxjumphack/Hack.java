package com.skyline.wxjumphack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

/**
 * Created by chenliang on 2018/1/1.
 */
public class Hack {
    /**
     * 生成随机数，避免固定间隔检测
     */
    public static int getRandom(int min,int max){
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }
    static final String ADB_PATH = "/home/liubailin/Android/Sdk/platform-tools/adb";

    /**
     * 弹跳系数，现在已经会自动适应各种屏幕，请不要修改。
     */
    static final double JUMP_RATIO = 1.38f;
 
    public static void main(String... strings) {

        String root = Hack.class.getResource("/").getPath();
        System.out.println("root: " + root);
        File srcDir = new File(root, "imgs/input");
        srcDir.mkdirs();
        System.out.println("srcDir: " + srcDir.getAbsolutePath());
        MyPosFinder myPosFinder = new MyPosFinder();
        NextCenterFinder nextCenterFinder = new NextCenterFinder();
        WhitePointFinder whitePointFinder = new WhitePointFinder();
        int total = 0;
        int centerHit = 0;
        double jumpRatio = 0;
        for (int i = 0; i < 5000; i++) {
            try {
                total++;
                File file = new File(srcDir, i + ".png");
                if (file.exists()) {
                    file.deleteOnExit();
                }
                Runtime.getRuntime().exec(ADB_PATH + " shell /system/bin/screencap -p /sdcard/screenshot.png");
                Thread.sleep(1_000);
                Runtime.getRuntime().exec(ADB_PATH + " pull /sdcard/screenshot.png " + file.getAbsolutePath());
                Thread.sleep(1_000);

                System.out.println("screenshot, file: " + file.getAbsolutePath());
                BufferedImage image = ImgLoader.load(file.getAbsolutePath());
                if (jumpRatio == 0) {
                    jumpRatio = JUMP_RATIO * 1080 / image.getWidth();
                }
                int[] myPos = myPosFinder.find(image);
                if (myPos != null) {
                    System.out.println("find myPos, succ, (" + myPos[0] + ", " + myPos[1] + ")");
                    int[] nextCenter = nextCenterFinder.find(image, myPos);
                    if (nextCenter == null || nextCenter[0] == 0) {
                        System.err.println("find nextCenter, fail");
                        break;
                    } else {
                        int centerX, centerY;
                        int[] whitePoint = whitePointFinder.find(image, nextCenter[0] - 120, nextCenter[1], nextCenter[0] + 120, nextCenter[1] + 180);
                        if (whitePoint != null) {
                            centerX = whitePoint[0];
                            centerY = whitePoint[1];
                            centerHit++;
                            System.out.println("find whitePoint, succ, (" + centerX + ", " + centerY + "), centerHit: " + centerHit+ ", total: " + total);
                        } else {
                            if (nextCenter[2] != Integer.MAX_VALUE && nextCenter[4] != Integer.MIN_VALUE) {
                                centerX = (nextCenter[2] + nextCenter[4]) / 2;
                                centerY = (nextCenter[3] + nextCenter[5]) / 2;
                            } else {
                                centerX = nextCenter[0];
                                centerY = nextCenter[1] + 48;
                            }
                        }
                        System.out.println("find nextCenter, succ, (" + centerX + ", " + centerY + ")");
                        int distance = (int) (Math.sqrt((centerX - myPos[0]) * (centerX - myPos[0]) + (centerY - myPos[1]) * (centerY - myPos[1])) * jumpRatio);
                        System.out.println("distance: " + distance);
                        
                        Random random = new Random();
                        
                        String cmd = " shell input swipe "+ (290 +random.nextInt(100))+" " + (290+random.nextInt(100))+" "+ (290 + random.nextInt(100))+ " " + (290 + random.nextInt(100))+ " " ;
                               System.out.println(ADB_PATH + cmd + distance); 
                               Runtime.getRuntime().exec(ADB_PATH + cmd + distance);
                    }
                    
                } else {
                    System.err.println("find myPos, fail");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            try {
                 Thread.sleep(getRandom(4000,9000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("centerHit: " + centerHit + ", total: " + total);
    }

}
