import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SoccerKickSimulation extends JPanel {
    // Screen dimensions
    private static final int SCREEN_WIDTH = 512;
    private static final int SCREEN_HEIGHT = 256;
    
    // Field dimensions in feet
    private static final int FIELD_LENGTH = 90;
    private static final int GOAL_WIDTH = 24;
    private static final int GOAL_HEIGHT = 8;
    
    // Conversion factors (using 16-bit integers)
    private static final int PIXELS_PER_FOOT = 5;
    private static final int TIME_STEP = 16;
    private static final int GRAVITY = 32;

    // Add viewport padding constants
    private static final int BALL_PADDING = 20;  // Minimum pixels from edge
    private static final int MAX_BALL_SIZE = 20; // Maximum ball diameter in pixels
    
    // Ball properties
    private int ballX;
    private int ballY;
    private int ballZ;
    private int velocityX;
    private int velocityY;
    private int velocityZ;
    private int horizontalSpinEffect;  // Side spin (clockwise/counterclockwise)
    private int verticalSpinEffect;    // Top/under spin
    private boolean isKicking;
    
    // Ball physics constants
    private static final int INITIAL_VELOCITY = 4000;
    private static final int DRAG_COEFFICIENT = 97;
    private static final int MAGNUS_COEFFICIENT = 50;
    
    // Ball strike position
    private Point strikePosition;
    
    public SoccerKickSimulation() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setupInitialPosition();
        setupMouseListener();
        startAnimation();
    }
    
    private void setupInitialPosition() {
        ballX = SCREEN_WIDTH * 50;
        ballY = SCREEN_HEIGHT * 90;
        ballZ = 0;
        isKicking = false;
        strikePosition = null;
    }
    
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isKicking) {
                    strikePosition = e.getPoint();
                    initiateKick(e.getX(), e.getY());
                }
            }
        });
    }
    
    private void initiateKick(int mouseX, int mouseY) {
        // Calculate horizontal spin based on horizontal strike position
        int horizontalOffset = mouseX - (SCREEN_WIDTH / 2);
        horizontalSpinEffect = -(horizontalOffset * 100) / SCREEN_WIDTH;
        
        // Calculate vertical spin based on vertical strike position
        // Center is at y = SCREEN_HEIGHT - 20 (ball's initial position)
        int verticalOffset = mouseY - (SCREEN_HEIGHT - 20);
        verticalSpinEffect = (verticalOffset * 100) / 40; // 40 pixels total height range
        
        // Adjust initial velocities based on strike position
        velocityX = (horizontalSpinEffect * INITIAL_VELOCITY) / 100;
        velocityY = -INITIAL_VELOCITY;
        
        // Adjust initial vertical velocity based on strike position
        velocityZ = INITIAL_VELOCITY / 2;
        if (verticalSpinEffect > 0) {
            // Top spin reduces initial vertical velocity
            velocityZ = (velocityZ * (100 - verticalSpinEffect)) / 100;
        } else {
            // Under spin increases initial vertical velocity
            velocityZ = (velocityZ * (100 - verticalSpinEffect)) / 100;
        }
        
        isKicking = true;
    }
    
    private void updateBallPosition() {
        if (!isKicking) return;
        
        // Update position
        ballX += (velocityX * TIME_STEP) / 1000;
        ballY += (velocityY * TIME_STEP) / 1000;
        ballZ += (velocityZ * TIME_STEP) / 1000;
        
        // Apply horizontal Magnus effect (side spin)
        int horizontalMagnusForce = (horizontalSpinEffect * velocityY * MAGNUS_COEFFICIENT) / 10000;
        velocityX += (horizontalMagnusForce * TIME_STEP) / 1000;
        
        // Apply vertical Magnus effect (top/under spin)
        int verticalMagnusForce = (verticalSpinEffect * velocityY * MAGNUS_COEFFICIENT) / 10000;
        velocityZ -= (verticalMagnusForce * TIME_STEP) / 1000;
        
        // Apply gravity
        velocityZ -= (GRAVITY * TIME_STEP) / 1000;
        
        // Apply drag with spin effects
        velocityX = (velocityX * DRAG_COEFFICIENT) / 100;
        velocityY = (velocityY * DRAG_COEFFICIENT) / 100;
        velocityZ = (velocityZ * DRAG_COEFFICIENT) / 100;
        
        // Check if ball hits ground
        if (ballZ < 0) {
            resetBall();
        }
    }
    
    private void resetBall() {
        setupInitialPosition();
        velocityX = 0;
        velocityY = 0;
        velocityZ = 0;
        horizontalSpinEffect = 0;
        verticalSpinEffect = 0;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        drawGoalWithPerspective(g2d);
        
        if (isKicking) {
            drawBallWithPerspective(g2d);
        } else {
            drawInitialBallWithTargeting(g2d);
        }
    }
    
    private void drawInitialBallWithTargeting(Graphics2D g2d) {
        // Draw the ball
        g2d.setColor(Color.WHITE);
        int ballDisplayX = SCREEN_WIDTH/2 - 10;
        int ballDisplayY = SCREEN_HEIGHT - 30;
        g2d.fillOval(ballDisplayX, ballDisplayY, 20, 20);
        
        // Draw target crosshair if mouse is over ball
        if (strikePosition != null) {
            g2d.setColor(Color.RED);
            g2d.drawLine(strikePosition.x - 5, strikePosition.y, strikePosition.x + 5, strikePosition.y);
            g2d.drawLine(strikePosition.x, strikePosition.y - 5, strikePosition.x, strikePosition.y + 5);
        }
    }
    
    private void drawGoalWithPerspective(Graphics2D g2d) {
        int goalDistance = FIELD_LENGTH * PIXELS_PER_FOOT;
        int perspectiveScale = 1000;
        
        int goalWidth = (GOAL_WIDTH * PIXELS_PER_FOOT * perspectiveScale) / goalDistance;
        int goalHeight = (GOAL_HEIGHT * PIXELS_PER_FOOT * perspectiveScale) / goalDistance;
        
        g2d.setColor(Color.WHITE);
        int goalX = (SCREEN_WIDTH - goalWidth) / 2;
        int goalY = SCREEN_HEIGHT / 3;
        g2d.fillRect(goalX, goalY, 2, goalHeight);
        g2d.fillRect(goalX + goalWidth, goalY, 2, goalHeight);
        g2d.fillRect(goalX, goalY, goalWidth, 2);
    }
    
    private void drawBallWithPerspective(Graphics2D g2d) {
        // Calculate raw position first
        int displayX = SCREEN_WIDTH / 2 + (ballX / 100 - SCREEN_WIDTH / 2) * 
                      (FIELD_LENGTH - ballY/100) / FIELD_LENGTH;
        int displayY = SCREEN_HEIGHT - (ballY / 100) - (ballZ / 100);
        
        // Calculate ball size with perspective
        int ballSize = 10 * (FIELD_LENGTH - ballY/100) / FIELD_LENGTH;
        if (ballSize < 2) ballSize = 2;
        if (ballSize > MAX_BALL_SIZE) ballSize = MAX_BALL_SIZE;
        
        // Adjust position to keep ball fully in view
        displayX = Math.max(BALL_PADDING + ballSize/2, 
                 Math.min(SCREEN_WIDTH - BALL_PADDING - ballSize/2, displayX));
        displayY = Math.max(BALL_PADDING + ballSize/2, 
                 Math.min(SCREEN_HEIGHT - BALL_PADDING - ballSize/2, displayY));
        
        // Draw ball shadow - fades with height
        int shadowAlpha = Math.max(0, 255 - (ballZ / 100));
        g2d.setColor(new Color(0, 0, 0, shadowAlpha));
        int shadowY = SCREEN_HEIGHT - (ballY / 100);
        int shadowSize = Math.max(2, ballSize - (ballZ / 300));
        g2d.fillOval(displayX - shadowSize/2, shadowY - shadowSize/2, shadowSize, shadowSize);
        
        // Draw ball
        g2d.setColor(Color.WHITE);
        g2d.fillOval(displayX - ballSize/2, displayY - ballSize/2, ballSize, ballSize);
        
        // Draw spin indicator lines if ball is large enough
        if (ballSize >= 6) {
            g2d.setColor(Color.BLACK);
            // Horizontal spin indicator
            if (Math.abs(horizontalSpinEffect) > 10) {
                int spinLineLength = ballSize / 3;
                int spinDirection = horizontalSpinEffect > 0 ? 1 : -1;
                g2d.drawLine(displayX - spinLineLength, displayY, 
                           displayX + spinLineLength * spinDirection, displayY);
            }
            // Vertical spin indicator
            if (Math.abs(verticalSpinEffect) > 10) {
                int spinLineLength = ballSize / 3;
                int spinDirection = verticalSpinEffect > 0 ? 1 : -1;
                g2d.drawLine(displayX, displayY - spinLineLength,
                           displayX, displayY + spinLineLength * spinDirection);
            }
        }
    }
    
    private void startAnimation() {
        Timer timer = new Timer(TIME_STEP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBallPosition();
                repaint();
            }
        });
        timer.start();
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Soccer Kick Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SoccerKickSimulation());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}