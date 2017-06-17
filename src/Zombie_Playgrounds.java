
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    
    // For determining coordiantes
    int mouseX;
    int mouseY;
    
    
    // booleans
    boolean wPressed;
    boolean sPressed;
    boolean aPressed;
    boolean dPressed;
    
    
    // Bullet speed and direction variables
    int bulletSpeed = 10;
    // +1 - Right
    // -1 - Left
    int bulletDirection = 1;
    
    // Boolean used to identify if the "shooting" button has been pressed
    boolean fire = false;
    
    // whether or not the bullet for player 1 is travelling through the air
    boolean p1Pending = false;
    double angle = 0;
    Robot r;
    // dimensions for player
    Rectangle player = new Rectangle(300, 300, 50, 50);
    // array of blocks
    Rectangle[] blocks = new Rectangle[5];
    // array list for zombies
    ArrayList<Rectangle> enemyArray = new ArrayList();
    // for player 1
    ArrayList<Rectangle> bullet = new ArrayList();
    Iterator<Rectangle> it = bullet.iterator();
    // Controls the delay in the shooting mechanism a.k.a "fire rate"
    int delay = 800;
    long nextTime = 0;

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

        // GAME DRAWING GOES HERE
        g.setColor(Color.cyan);
        g2d.translate(player.x + player.width / 2, player.y + player.height / 2);
        g2d.rotate(angle);
        // creating player figure (currently a circle)
        g.fillOval(-player.width / 2, -player.height / 2, player.width, player.height);
        //g.fillOval(player.x, player.y, player.width, player.height);
        g.setColor(Color.MAGENTA);
        // creating a rectangle on top of the player figure in order to test mouse movement
        g.fillRect(-player.width / 2, -player.height / 2, 100, 5);

        // reverting screen to before rotation
        g2d.rotate(-angle);
        g2d.translate(-player.x - player.width / 2, -player.y - player.height / 2);


        g.setColor(Color.GREEN);

        // for loop to draw enemies
        for (Rectangle enemy : enemyArray) {
            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        }

        g.setColor(Color.darkGray);



        // use a for loop to go through the array of blocks
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != null) {
                g.fillRect(blocks[i].x, blocks[i].y, blocks[i].width, blocks[i].height);
            }
        }




        g.setColor(Color.RED);
        // creates bullets for player 1
        for (Rectangle bullets : bullet) {
            g2d.translate(bullets.x + bullets.width / 2, bullets.y + bullets.height / 2);
            g2d.rotate(angle);
            g.fillRect(-bullets.width / 2, -bullets.height / 2, bullets.width, bullets.height);
            
            g2d.rotate(-angle);
            g2d.translate(-bullets.x - bullets.width / 2, -bullets.y - bullets.height / 2);
        }
        
        

        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void preSetup() {

        // create all the different blocks to use in the level
        // They are each in the array
        blocks[0] = new Rectangle(100, 400, 150, 150); // crate 1 (left)
        blocks[1] = new Rectangle(425, 400, 150, 150); // crate 2 (middle)
        blocks[2] = new Rectangle(750, 400, 150, 150); // crate 3 (right side)
        blocks[3] = new Rectangle(0, 150, 400, 10); // left side of wall
        blocks[4] = new Rectangle(600, 150, 700, 10); // right side of wall




        // for loop for zombies
        for (int i = 0; i < 10; i++) {
            enemyArray.add(new Rectangle(400 + (i * 50), 400, 30, 30));

        }



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

            collisions();
            zombieZombieCollisions();
            shooting();

            // delay timer used to control fire rate for both players
            if (startTime > nextTime) {
                nextTime = startTime + delay;
                p1Pending = false;

            }


            // if w is pressed move up at the speed of 5
            if (wPressed) {
                player.y = player.y - 5;
            }
            // if a is pressed move left at the speed of 5
            if (aPressed) {
                player.x = player.x - 5;
            }
            // if s is pressed move right at the speed of 5
            if (sPressed) {
                player.y = player.y + 5;
            }
            // if d is pressed move right at the speed of 5
            if (dPressed) {
                player.x = player.x + 5;
            }



            for (Rectangle enemy : enemyArray) {
                // GEN 1 Zombie chase AI
                if (player.x <= enemy.x) {
                    enemy.x -= 3;
                }
                if (player.x >= enemy.x) {
                    enemy.x += 3;
                }

                if (player.y <= enemy.y) {
                    enemy.y -= 3;
                }
                if (player.y >= enemy.y) {
                    enemy.y += 3;
                }

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
            // for mouse coordinates
            mouseX = e.getX();
            mouseY = e.getY();

            if (e.getButton() == MouseEvent.BUTTON1) {
                fire = true;
            }







            //System.out.println("( " + mouseX + " , " + mouseY + " )");
            // mouse cooordinate code ends



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
            int mx = e.getX();
            int my = e.getY();


            int x = mx - player.x;
            int y = my - player.y;

            angle = Math.atan2(y, x);
            if (angle < 0) {
                angle = angle + Math.toRadians(360);
            }

            //System.out.println("angle: " + Math.toDegrees(angle));          

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
                    // set my overlap as a number - -1 means not set
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
                    // move the enemys x i so the no longer hit the block
                    // we also fix the dx so that we are no longer changing that
                    if (overlapX < overlapY) {
                        // which side am I on?
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


                            // I'm on the block so not in the air!

                        } else {
                            // im under the block, just fix the overlap
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
        if (player.x >= 950) {
            player.x = 950;
        }
        // do not allow player to go off to the top
        if (player.y <= 0) {
            player.y = 0;
        }
        // do not allow player to go off to the bottom
        if (player.y >= 750) {
            player.y = 750;
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

        // FOR PLAYER 1

        if (fire) {
            if (p1Pending == false) {
                bullet.add(new Rectangle(player.x, player.y, 10, 10));
                // passed = false;
                p1Pending = true;
            }


        }

        for (Rectangle bullets : bullet) {

            bullets.x += bulletSpeed;



        }

    }
}
