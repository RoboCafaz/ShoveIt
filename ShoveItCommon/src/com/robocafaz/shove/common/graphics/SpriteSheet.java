package com.robocafaz.shove.common.graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.robocafaz.shove.common.objects.GameObject;

public class SpriteSheet extends GameObject {
  protected final BufferedImage sheet;
  protected final int frameWidth;
  protected final int frameHeight;
  protected final int frameRate;

  protected final int frames;
  protected final int animations;

  protected int frameCounter;
  protected int currentFrame;
  protected int currentAnimation;

  protected BufferedImage currentImage;

  public SpriteSheet(String name, BufferedImage sheet, int frameWidth, int frameHeight, int frameRate) {
    super(name);
    this.sheet = sheet;
    this.frameWidth = frameWidth;
    this.frameHeight = frameHeight;
    this.frameRate = frameRate;
    this.frames = (sheet.getWidth() / frameWidth);
    this.animations = (sheet.getHeight() / frameHeight);
    this.currentFrame = 0;
    this.currentAnimation = 0;

    changeImage();
  }

  public BufferedImage getImage() {
    return this.currentImage;
  }

  public void changeAnimation(int animation) {
    if (animation >= this.animations) {
      animation = (this.animations - 1);
    } else if (animation < 0) {
      animation = 0;
    }
    if (this.currentAnimation != animation) {
      this.currentAnimation = animation;
      this.currentFrame = 0;
      changeImage();
    }
  }

  private void changeImage() {
    this.currentImage = this.sheet.getSubimage(this.frameWidth * this.currentFrame, this.frameHeight * this.currentAnimation,
        this.frameWidth, this.frameHeight);
  }

  @Override
  public void update(long currentTime) {
    boolean imageChanged = false;
    this.frameCounter++;
    if (this.frameCounter >= this.frameRate) {
      this.frameCounter = 0;
      this.currentFrame++;
      if (this.currentFrame >= this.frames) {
        this.currentFrame = 0;
      }
      imageChanged = true;
    }

    if (imageChanged) {
      changeImage();
    }
  }

  public static void main(String... args) {
    BufferedImage image;
    try {
      image = ImageIO.read(new File("C:\\Users\\bcafazzo\\Pictures\\gfx\\testsheet.png"));
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    final SpriteSheet sheet = new SpriteSheet("TestSheet", image, 80, 64, 5);

    final JLabel name = new JLabel();
    final JLabel fWidth = new JLabel();
    final JLabel fHeight = new JLabel();
    final JLabel fRate = new JLabel();
    final JLabel frames = new JLabel();
    final JLabel anim = new JLabel();
    final JLabel fCount = new JLabel();
    final JLabel cFrame = new JLabel();
    final JLabel cAnim = new JLabel();
    final JLabel imageLabel = new JLabel();
    final JButton upButton = new JButton("^");
    upButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sheet.changeAnimation(sheet.currentAnimation + 1);
      }
    });
    final JButton downButton = new JButton("v");
    downButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sheet.changeAnimation(sheet.currentAnimation - 1);
      }
    });

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(12, 2));

        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(name);
        infoPanel.add(new JLabel("Frame Width:"));
        infoPanel.add(fWidth);
        infoPanel.add(new JLabel("Frame Height:"));
        infoPanel.add(fHeight);
        infoPanel.add(new JLabel("Frame Rate:"));
        infoPanel.add(fRate);
        infoPanel.add(new JLabel("Frames:"));
        infoPanel.add(frames);
        infoPanel.add(new JLabel("Animations:"));
        infoPanel.add(anim);
        infoPanel.add(new JLabel("Frame Counter:"));
        infoPanel.add(fCount);
        infoPanel.add(new JLabel("Current Frame:"));
        infoPanel.add(cFrame);
        infoPanel.add(new JLabel("Current Animation:"));
        infoPanel.add(cAnim);
        infoPanel.add(upButton);
        infoPanel.add(downButton);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
      }
    });
    Timer timer = new Timer("Repaint Timer");
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        sheet.update(System.currentTimeMillis());
        name.setText(sheet.name);
        fWidth.setText("" + sheet.frameWidth);
        fHeight.setText("" + sheet.frameHeight);
        fRate.setText("" + sheet.frameRate);
        frames.setText("" + sheet.frames);
        anim.setText("" + sheet.animations);
        fCount.setText((sheet.frameCounter + 1) + "/" + sheet.frameRate);
        cFrame.setText((sheet.currentFrame + 1) + "/" + sheet.frames);
        cAnim.setText((sheet.currentAnimation + 1) + "/" + sheet.animations);
        imageLabel.setIcon(new ImageIcon(sheet.getImage()));
      }
    }, new Date(), 50);
  }
}