package com.example.demo;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.nut.Frame;
import net.bramp.ffmpeg.nut.RawHandler;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class DemoApplication {
  private static final String inputPath = "/Users/visualcamp/Desktop/mediaData/stream.webm";
//  private static final String inputPath = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
  private static final String outputPath = "/Users/visualcamp/Desktop/vaano/";

  public static void main(String[] args) {
    bufferedImage2();
  }

  private void videoTransfer () {

    try {
      FFmpeg ffmpeg = new FFmpeg("/usr/local/bin/ffmpeg");
      FFprobe ffprobe = new FFprobe("/usr/local/bin/ffprobe");
      for (int i = 0; i < 3; i++) {
        FFmpegBuilder builder = new FFmpegBuilder()
            .overrideOutputFiles(true)
            .addInput(inputPath)
            .addExtraArgs("-ss", String.valueOf(i))
            .addExtraArgs("-t", "3")
            .addOutput(outputPath + "pitching" + i + ".mp4")
            .addExtraArgs("-an") //영상 소리 제거
            .done();
        FFmpegExecutor excutor = new FFmpegExecutor(ffmpeg, ffprobe);
        excutor.createJob(builder).run();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void bufferedImage () {
    FFmpegFrameGrabber g = new FFmpegFrameGrabber(inputPath);
    try {
      g.start();
      Java2DFrameConverter c = new Java2DFrameConverter();
      BufferedImage bImage = c.convert(g.grabImage());
      for (int i = 0 ; i < 500 ; i++) {
        ImageIO.write(bImage, "jpg", new File(outputPath + "video-frame-" + System.currentTimeMillis() + ".jpg"));
      }

      g.stop();
    } catch (IOException | FrameGrabber.Exception e) {
      e.printStackTrace();
    }
  }

  private static void bufferedImage2 () {
    FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputPath);
    try {
      frameGrabber.start();
      Java2DFrameConverter c = new Java2DFrameConverter();
      BufferedImage bi = c.convert(frameGrabber.grabImage());
      ImageIO.write(bi,"png", new File(outputPath + "image.png"));
      frameGrabber.stop();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
