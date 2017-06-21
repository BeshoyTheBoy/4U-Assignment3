
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ranam3235
 */
public class Zombie_Playgrounds extends JComponent {

    // Height and Width of our game
    static final int WIDTH = 1000;
    static final int HEIGHT = 800;
    //Title of the window
    String title = "Zombie Playgrounds";
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    // YOUR GAME VARIABLES WOULD GO HERE
    // for bullet velocities
    int xVel;
    int yVel;
    int bullets = 0;
    int kills = 0;
    long current = 0;
    int spawnDelay = 10000;
    long spawnTime = 0;
    boolean on = false;
    // player health int
    int playerHealth = 150;
    // Bullet speeds
    int bulletSpeed = 10;
    int zombieHealth = 100;
    // Controls the delay in the shooting mechanism a.k.a "fire rate"
    int delay = 200;
    long nextTime = 0;
    //ADDED BY ME
    //we needa  delay between zombie's consecutive attacks
    int zombieHitDelay = 1000;
    long nextHit = 0;
    int regenDelay = 5000;
    long regenTime = 0;
    // booleans for w, a, s, d 
    boolean wPressed;
    boolean sPressed;
    boolean aPressed;
    boolean dPressed;
    // boolean for if zombie has attacked player
    boolean zombieHit = false;
    // boolean used to identify if the "shooting" button has been pressed
    boolean fire = false;
    // whether or not the bullet is travelling through the air
    boolean pending = false;
    // angle for determining where player is facing
    double angle = 0;
    Robot r;
    // rectangle to act as player
    Rectangle player = new Rectangle(300, 300, 70, 70);
    // array of all map elements
    Rectangle[] blocks = new Rectangle[5];
    // array list for enemies
    ArrayList<Rectangle> enemyArray = new ArrayList();
    // iterator for enemies
    Iterator<Rectangle> eit = enemyArray.iterator();
    // array list for bullets
    ArrayList<Rectangle> bullet = new ArrayList();
    // iterator for rectangles
    Iterator<Rectangle> it = bullet.iterator();
    // zombie image loaded
    BufferedImage zombie = loadImage("zombie.png");
    // player image loaded
    BufferedImage megaman = loadImage("megaman.png");
    // crate image loaded
    BufferedImage crate = loadImage("crate.png");
    // fence image loaded
    BufferedImage fence = loadImage("fence.png");
    BufferedImage background = loadImage("background.png");

    // GAME VARIABLES END HERE
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public Zombie_Playgrounds() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game 
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();

        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        g.drawImage(background, 0, 0, 1280, 800, null);

        // GAME DRAWING GOES HERE
        g.setColor(Color.cyan);
        g2d.translate(player.x + player.width / 2, player.y + player.height / 2);
        g2d.rotate(angle);
        // creating player figure (currently a circle)
//        g.fillRect(-player.width / 2, -player.height / 2, player.width, player.height);
        g.drawImage(megaman, -player.width / 2, -player.height / 2, player.width, player.height, null);
        g.drawString("" + playerHealth, -player.width / 2 + 25, -player.height / 2);
        g.setColor(Color.RED);

        g.setColor(Color.MAGENTA);
        // creating a rectangle on top of the player figure in order to test mouse movement
//        g.fillRect(-player.width / 2, -player.height / 2, 100, 5);

        // reverting screen to before rotation
        g2d.rotate(-angle);
        g2d.translate(-player.x - player.width / 2, -player.y - player.height / 2);

        g.drawString("Kills: " + kills, 950, 780);

        g.setColor(Color.GREEN);

        // for loop to draw enemies
        for (Rectangle enemy : enemyArray) {
//            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
            g.drawImage(zombie, enemy.x, enemy.y, enemy.width, enemy.height, null);

        }

        g.setColor(Color.darkGray);

        // use a for loop to go through the array of blocks and draw them
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != null) {
//                g.fillRect(blocks[i].x, blocks[i].y, blocks[i].width, blocks[i].height);
                g.drawImage(crate, blocks[0].x, blocks[0].y, blocks[0].width, blocks[0].height, null);
                g.drawImage(crate, blocks[1].x, blocks[1].y, blocks[1].width, blocks[1].height, null);
                g.drawImage(crate, blocks[2].x, blocks[2].y, blocks[2].width, blocks[2].height, null);

                g.drawImage(fence, blocks[3].x, blocks[3].y, blocks[3].width, blocks[3].height, null);
                g.drawImage(fence, blocks[4].x, blocks[4].y, blocks[4].width, blocks[4].height, null);
            }
        }

        g.setColor(Color.ORANGE);
        // for loop to draw bullets when fired
        for (Rectangle bullets : bullet) {
            g.fillRect(bullets.x, bullets.y, bullets.width, bullets.height);

        }

        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void preSetup() {

        System.out.println("Details of the game, along with persisting issues are located in commit message, enjoy");

        // create all the different blocks to use in the level
        // They are each in the array
        blocks[0] = new Rectangle(100, 400, 150, 150); // crate 1 (left)
        blocks[1] = new Rectangle(425, 400, 150, 150); // crate 2 (middle)
        blocks[2] = new Rectangle(750, 400, 150, 150); // crate 3 (right side)
        blocks[3] = new Rectangle(0, 150, 400, 10); // left side of wall
        blocks[4] = new Rectangle(600, 150, 700, 10); // right side of wall

//        // for loop to spawn enemies
//        for (int i = 0; i < 10; i++) {
//            enemyArray.add(new Rectangle(400 + (i * 50), 10, 36, 60));
//
//        }
        // MOUSE ROTATION
        try {
            r = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Zombie_Playgrounds.class.getName()).log(Level.SEVERE, null, ex);
        }

        r.mouseMove(WIDTH / 2, HEIGHT / 2);

    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        preSetup();

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE
            // call all methods
            collisions();
            zombieZombieCollisions();
            shooting();
            bulletCollisions();
            playerHealth();
            enemySpawns();
            enemyPlayerCollisions();

            // delay timer used to control fire rate for both players
            if (startTime > nextTime) {
                nextTime = startTime + delay;
                pending = false;

            }

            // if w is pressed move up at the speed of 6
            if (wPressed) {
                player.y = player.y - 6;
            }
            // if a is pressed move left at the speed of 6
            if (aPressed) {
                player.x = player.x - 6;
            }
            // if s is pressed move right at the speed of 6
            if (sPressed) {
                player.y = player.y + 6;
            }
            // if d is pressed move right at the speed of 6
            if (dPressed) {
                player.x = player.x + 6;
            }

            for (Rectangle enemy : enemyArray) {
                // GEN 1 Zombie chase AI
                if (player.x <= enemy.x) {
                    enemy.x -= 2;
                }
                if (player.x >= enemy.x) {
                    enemy.x += 2;
                }

                if (player.y <= enemy.y) {
                    enemy.y -= 2;
                }
                if (player.y >= enemy.y) {
                    enemy.y += 2;
                }

            }
            if (startTime > nextHit) {
                zombieHit = false;
                nextHit = startTime + zombieHitDelay;
            }
            if (startTime > regenTime) {
                zombieHit = false;
                regenTime = startTime + regenDelay;
                playerHealth = 150;

            }

            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(1);
                } else {
                    // sleep to make up the extra time
                    Thread.sleep(desiredTime - deltaTime);
                }
            } catch (Exception e) {
            };
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {
        // if a mouse button has been pressed down

        @Override
        public void mousePressed(MouseEvent e) {

            if (e.getButton() == MouseEvent.BUTTON1) {
                fire = true;

                int xVel2[] = new int[bullets];
                int yVel2[] = new int[bullets];

                xVel = (int) (Math.cos((angle)) * bulletSpeed);

                yVel = (int) (Math.sin((angle)) * bulletSpeed);

            }

        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                fire = false;
            }
        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }

        // if the mouse has moved is
        @Override
        public void mouseMoved(MouseEvent e) {

            // int to store mouse x and y positions
            int mx = e.getX();
            int my = e.getY();

            // int x = mouse x - players x
            int x = mx - player.x;
            // int y = mouse y - players y
            int y = my - player.y;

            // these calculations above are used to do some simple SOHCAHTOA calculations
            // angle is based upon mouse location, and origin (in this case the player)
            angle = Math.atan2(y, x);
            if (angle < 0) {
                angle = angle + Math.toRadians(360);
            }

            int num = (int) (5 * Math.cos(angle));

        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {
        // if a key has been pressed down

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = true;

            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                fire = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                fire = false;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        Zombie_Playgrounds game = new Zombie_Playgrounds();

        // starts the game loop
        game.run();
    }

    public void collisions() {

        // PLAYER COLLISIONS START HERE
        for (int i = 0; i < blocks.length; i++) {
            // if the player is hitting a block at i i
            if (player.intersects(blocks[i])) {
                // handle the collision with the block at i i
                int overlapX = -1;
                // player is on the left
                if (player.x <= blocks[i].x) {
                    // right corner of player subtract left corner of block
                    overlapX = player.x + player.width - blocks[i].x;

                } else {
                    // right corner of block subtract left corner of player
                    overlapX = blocks[i].x + blocks[i].width - player.x;
                }

                // do the same but for the y values
                // set my overlap as a number - -1 means not set
                int overlapY = -1;
                // player is above the block
                if (player.y <= blocks[i].y) {
                    // bottom of player subtract top of block
                    overlapY = player.y + player.height - blocks[i].y;
                } else {
                    // bottom of block subtract top of player
                    overlapY = blocks[i].y + blocks[i].height - player.y;
                }

                // now check which overlap is smaller
                // we will correct that one because it will be less obvious!
                // fix the x overlapping
                // move the players x i so the no longer hit the block
                // we also fix the dx so that we are no longer changing that
                if (overlapX < overlapY) {
                    // which side am I on?
                    // on the right side
                    if (player.x <= blocks[i].x) {
                        player.x = blocks[i].x - player.width;

                    } else {
                        player.x = blocks[i].x + blocks[i].width;

                    }

                } else {
                    // fixing the y overlap in the same way
                    // the difference this time is we have to deal with the dy and not dx

                    // above the block
                    if (player.y <= blocks[i].y) {
                        // no more y collision
                        player.y = blocks[i].y - player.height;

                        // I'm on the block so not in the air!
                    } else {
                        // im under the block, just fix the overlap
                        player.y = blocks[i].y + blocks[i].height;

                    }

                }

            }

        }
        // PLAYER COLLISIONS ENDS HERE

        // ENEMY COLLISION STARTS HERE
        for (Rectangle enemy : enemyArray) {
            for (int i = 0; i < blocks.length; i++) {
                // if the enemy is hitting a block at i i
                if (enemy.intersects(blocks[i])) {
                    // handle the collision with the block at i i
                    int overlapX = -1;
                    // enemy is on the left
                    if (enemy.x <= blocks[i].x) {
                        // right corner of enemy subtract left corner of block
                        overlapX = enemy.x + enemy.width - blocks[i].x;

                    } else {
                        // right corner of block subtract left corner of enemy
                        overlapX = blocks[i].x + blocks[i].width - enemy.x;
                    }

                    // do the same but for the y values
                    // set enemy overlap as a number - -1 means not set
                    int overlapY = -1;
                    // enemy is above the block
                    if (enemy.y <= blocks[i].y) {
                        // bottom of enemy subtract top of block
                        overlapY = enemy.y + enemy.height - blocks[i].y;
                    } else {
                        // bottom of block subtract top of enemy
                        overlapY = blocks[i].y + blocks[i].height - enemy.y;
                    }

                    // now check which overlap is smaller
                    // we will correct that one because it will be less obvious!
                    // fix the x overlapping
                    // move the enemy's x i so the no longer hit the block
                    // we also fix the dx so that we are no longer changing that
                    if (overlapX < overlapY) {
                        // which side is the enemy on?
                        // on the right side
                        if (enemy.x <= blocks[i].x) {
                            enemy.x = blocks[i].x - enemy.width;

                        } else {
                            enemy.x = blocks[i].x + blocks[i].width;

                        }

                    } else {
                        // fixing the y overlap in the same way
                        // the difference this time is we have to deal with the dy and not dx

                        // above the block
                        if (enemy.y <= blocks[i].y) {
                            // no more y collision
                            enemy.y = blocks[i].y - enemy.height;

                            // enemy is on the block so not in the air!
                        } else {
                            // enemy is under the block, just fix the overlap
                            enemy.y = blocks[i].y + blocks[i].height;

                            // take position of enemy, take size of object, compare them ( size of obj - position of enemy) if its less than half the obj - move left, if not move right
                        }

                    }

                }

            }

        }

        // ENEMY COLLISION ENDS HERE
        // BOUNDRIES START HERE
        // do not allow player to leave the screen
        // do not allow player to go off to the left
        if (player.x <= 0) {
            player.x = 0;
        }
        // do not allow player to go off to the right
        if (player.x >= 1000 - player.width) {
            player.x = 1000 - player.width;
        }
        // do not allow player to go off to the top
        if (player.y <= 0) {
            player.y = 0;
        }
        // do not allow player to go off to the bottom
        if (player.y >= 800 - player.width) {
            player.y = 800 - player.width;
        }

        for (Rectangle enemy : enemyArray) {

            // do not allow enemy to go off to the left
            if (enemy.x <= 0) {
                enemy.x = 0;
            }
            // do not allow enemy to go off to the right
            if (enemy.x >= 1000 - enemy.width) {
                enemy.x = 1000 - enemy.width;
            }
            // do not allow enemy to go off to the top
            if (enemy.y <= 0) {
                enemy.y = 0;
            }
            // do not allow enemy to go off to the bottom
            if (enemy.y >= 800 - enemy.height) {
                enemy.y = (800 - enemy.height);
            }
        }
        // BOUNDRIES END HERE

    }

    public void zombieZombieCollisions() {

        for (int i = 0; i < enemyArray.size() - 1; i++) {

            for (int a = (i + 1); a < enemyArray.size(); a++) {

                // if the enemy is hitting a block at i i
                if (enemyArray.get(i).intersects(enemyArray.get(a))) {
                    // handle the collision with the block at i i
                    int overlapX = -1;
                    // enemy is on the left
                    if (enemyArray.get(i).x <= enemyArray.get(a).x) {
                        // right corner of enemy subtract left corner of block
                        overlapX = enemyArray.get(i).x + enemyArray.get(i).width - enemyArray.get(a).x;

                    } else {
                        // right corner of block subtract left corner of enemy
                        overlapX = enemyArray.get(a).x + enemyArray.get(a).width - enemyArray.get(i).x;
                    }

                    // do the same but for the y values
                    // set my overlap as a number - -1 means not set
                    int overlapY = -1;
                    // enemy is above the block
                    if (enemyArray.get(i).y <= enemyArray.get(a).y) {
                        // bottom of enemy subtract top of block
                        overlapY = enemyArray.get(i).y + enemyArray.get(i).height - enemyArray.get(a).y;
                    } else {
                        // bottom of block subtract top of enemy
                        overlapY = enemyArray.get(a).y + enemyArray.get(a).height - enemyArray.get(i).y;
                    }

                    // now check which overlap is smaller
                    // we will correct that one because it will be less obvious!
                    // fix the x overlapping
                    // move the enemys x i so the no longer hit the block
                    // we also fix the dx so that we are no longer changing that
                    if (overlapX < overlapY) {
                        // which side am I on?
                        // on the right side
                        if (enemyArray.get(i).x <= enemyArray.get(a).x) {
                            enemyArray.get(i).x = enemyArray.get(a).x - enemyArray.get(i).width;

                        } else {
                            enemyArray.get(i).x = enemyArray.get(a).x + enemyArray.get(a).width;

                        }

                    } else {
                        // fixing the y overlap in the same way
                        // the difference this time is we have to deal with the dy and not dx

                        // above the block
                        if (enemyArray.get(i).y <= enemyArray.get(a).y) {
                            // no more y collision
                            enemyArray.get(i).y = enemyArray.get(a).y - enemyArray.get(i).height;

                            // I'm on the block so not in the air!
                        } else {
                            // im under the block, just fix the overlap
                            enemyArray.get(i).y = enemyArray.get(a).y + enemyArray.get(i).height;

                            // take position of enemy, take size of object, compare them ( size of obj - position of enemy) if its less than half the obj - move left, if not move right
                        }

                    }

                }

            }
        }
    }

    public void shooting() {

        // shooting
        if (fire) {
            if (pending == false) {
                bullet.add(new Rectangle(player.x + 50, player.y + 28, 10, 10));


                pending = true;
            }
            bullets = bullets + 1;

        }

        // change the velocity of every bullet
        for (int i = 0; i < bullet.size(); i++) {

            bullet.get(i).x += xVel;
            bullet.get(i).y += yVel;

        }
    }

    public void bulletCollisions() {

        // run through enemy and bullet arrays and whenever a single bullet collides with an enemy, both are removed
        for (int j = 0; j < enemyArray.size(); j++) {
            for (int i = 0; i < bullet.size(); i++) {

                // if enemy is shot
                if (enemyArray.get(j).intersects(bullet.get(i))) {

                    kills = kills + 1;

                    // remove the bullet
                    bullet.remove(i);

                    // and the enemy
                    enemyArray.remove(j);

                }

            }
        }

        // whenever a bullet goes off screen, remove it
        for (int i = 0; i < bullet.size(); i++) {
            if (bullet.get(i).x + bullet.get(i).width >= WIDTH || bullet.get(i).x <= 0
                    || bullet.get(i).y <= 0 || bullet.get(i).y + bullet.get(i).height >= HEIGHT) {
                bullet.remove(i);
            }

        }
        // when a bullet impacts a wall it is removed
        for (int i = 0; i < blocks.length; i++) {
            for (int a = 0; a < bullet.size(); a++) {

                if (blocks[i].intersects(bullet.get(a))) {

                    bullet.remove(a);
                }

            }

        }
    }

    // method for loading images
    public static BufferedImage loadImage(String name) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(name));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return img;
    }

    public void playerHealth() {

        // for all enemies
        for (int j = 0; j < enemyArray.size(); j++) {

            // if enemy intersects with player
            if (enemyArray.get(j).intersects(player) && zombieHit == false) {

                // take 50 hp off player's health (max player hp is 150)
                playerHealth = playerHealth - 50;
                // change boolean to true, as in zombie did hit player
                zombieHit = true;
//                System.out.println("HIT");

            }

            // if player health = 0, the player is dead
            if (playerHealth == 0) {
                System.out.println("You died with " + kills + " kills");

                System.exit(0);

            }

        }

    }

    public void enemySpawns() {
        Random spawn = new Random();
        int zombieWave = spawn.nextInt(15);






        current = System.currentTimeMillis();

        if (current > spawnTime) {
            on = false;
            spawnTime = current + spawnDelay;
            System.out.println("test");
        }

        // for loop to spawn enemies
        if (on == false) {
            for (int i = 0; i < 15; i++) {
                enemyArray.add(new Rectangle(400 + (i * 50), 10, 36, 60));

            }
            on = true;

        }

    }

    public void enemyPlayerCollisions() {

        for (int i = 0; i < enemyArray.size(); i++) {

            // if the enemy is hitting a block at i i
            if (player.intersects(enemyArray.get(i))) {
                // handle the collision with the block at i i
                int overlapX = -1;
                // enemy is on the left
                if (player.x <= enemyArray.get(i).x) {
                    // right corner of enemy subtract left corner of blockwwwwwwwww
                    overlapX = player.x + player.width - enemyArray.get(i).x;

                } else {
                    // right corner of block subtract left corner of enemy
                    overlapX = enemyArray.get(i).x + enemyArray.get(i).width - player.x;
                }

                // do the same but for the y values
                // set my overlap as a number - -1 means not set
                int overlapY = -1;
                // enemy is above the block
                if (player.y <= enemyArray.get(i).y) {
                    // bottom of enemy subtract top of block
                    overlapY = player.y + player.height - enemyArray.get(i).y;
                } else {
                    // bottom of block subtract top of enemy
                    overlapY = enemyArray.get(i).y + enemyArray.get(i).height - player.y;
                }

                // now check which overlap is smaller
                // we will correct that one because it will be less obvious!
                // fix the x overlapping
                // move the enemys x i so the no longer hit the block
                // we also fix the dx so that we are no longer changing that
                if (overlapX < overlapY) {
                    // which side am I on?
                    // on the right side
                    if (player.x <= enemyArray.get(i).x) {
                        player.x = enemyArray.get(i).x - player.width;

                    } else {
                        player.x = enemyArray.get(i).x + enemyArray.get(i).width;

                    }

                } else {
                    // fixing the y overlap in the same way
                    // the difference this time is we have to deal with the dy and not dx

                    // above the block
                    if (player.y <= enemyArray.get(i).y) {
                        // no more y collision
                        player.y = enemyArray.get(i).y - player.height;

                        // I'm on the block so not in the air!
                    } else {
                        // im under the block, just fix the overlap
                        player.y = enemyArray.get(i).y + player.height;

                        // take position of enemy, take size of object, compare them ( size of obj - position of enemy) if its less than half the obj - move left, if not move right
                    }

                }

            }

        }
    }
}
