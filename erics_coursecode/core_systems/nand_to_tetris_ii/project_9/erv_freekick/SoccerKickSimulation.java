import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class SoccerKickSimulation extends JPanel implements ActionListener, MouseListener {
    // Previous constants remain the same
    private static final int SCREEN_WIDTH = 512;
    private static final int SCREEN_HEIGHT = 256;

    // Real world dimensions (in feet * 1000 for fixed-point precision)
    private static final int FIELD_LENGTH = 90_000;  // 90 feet
    private static final int GOAL_HEIGHT = 6_000;    // 6 feet
    private static final int GOAL_WIDTH = 24_000;    // 24 feet
    private static final int BALL_DIAMETER = 730;    // 0.73 feet
    private static final int CAMERA_HEIGHT = 5_500;  // 5.5 feet
    private static final int CAMERA_DISTANCE = -6_000; // -6 feet
    
    // Physics constants (scaled)
    private static final int GRAVITY = -32174;        // -32.174 ft/s² * 1000
    private static final int INITIAL_VELOCITY = 82000; // 82 ft/s * 1000
    private static final int MAGNUS_SCALE = 350;      // 0.35 * 1000
    
    // Ball minimum size in pixels
    private static final int BASE_BALL_SIZE = 8;
    
    // Ball physics properties (all scaled by 1000)
    private int ballX = 0;
    private int ballY = 0;
    private int ballZ = 0;
    private int velocityX = 0;
    private int velocityY = 0;
    private int velocityZ = 0;
    private int spinX = 0; // Rotation around X axis (top/bottom spin)
    private int spinY = 0; // Rotation around Y axis (side spin)
    private int spinZ = 0; // Rotation around Z axis (curl)
    
    // Strike position on ball (-1000 to 1000 for normalized coordinates)
    private double strikeX = 0.0;
    private double strikeY = 0.0;
    
    // UI State
    private boolean isAiming = true;
    private boolean kickStarted = false;
    private Point aimPoint = null;
    private Timer timer;
    private int timeMs = 0;  // Time in milliseconds
    
    // Magnus effect constant - increased for more noticeable effect
    private static final double MAGNUS_COEFFICIENT = 0.35;
    private static final double INITIAL_VELOCITY = 25.0;  // m/s
    
    private Timer timer;
    private double time = 0.0;
    
    public SoccerKickSimulation() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        timer = new Timer(16, this);
        addMouseListener(this);
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    
    private int getBallScreenSize(int distance) {
        // Scale = (90000/distance) * 8
        return Math.max((FIELD_LENGTH / distance) * BASE_BALL_SIZE, BASE_BALL_SIZE);
    }
    
    private Point projectToScreen(int x, int y, int z) {
        int relativeX = x - CAMERA_DISTANCE;
        int relativeY = y;
        int relativeZ = z - CAMERA_HEIGHT;
        
        if (relativeX <= 0) return null;
        
        // Scale factor uses the relationship between screen height and field size
        int screenX = SCREEN_WIDTH / 2 + (relativeY * SCREEN_HEIGHT) / (relativeX * 2);
        int screenZ = SCREEN_HEIGHT / 2 - (relativeZ * SCREEN_HEIGHT) / (relativeX * 2);
        
        return new Point(screenX, screenZ);
    }
    
    private void calculateSpinFromStrike() {
        // Convert strike position to spin (scaled by 1000)
        spinX = -(strikeY * 10);
        spinY = 0;
        spinZ = strikeX * 10;
        
        velocityX = INITIAL_VELOCITY;
        velocityY = (strikeX * INITIAL_VELOCITY) / 5;  // Divide by 5 instead of * 0.2
        velocityZ = Math.max(16400, -(strikeY * INITIAL_VELOCITY) / 3);
    }
    
    private void applyMagnusEffect() {
        // Calculate speed using integer square root approximation
        int speedSq = (velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ) / 1000;
        int speed = intSqrt(speedSq);
        if (speed < 100) return;  // Minimum speed threshold
        
        // Magnus force calculation using integer math
        int magnusY = -((MAGNUS_SCALE * (spinZ * velocityX - spinX * velocityZ)) / speed) / 1000;
        int magnusZ = ((MAGNUS_SCALE * (spinX * velocityY - spinY * velocityX)) / speed) / 1000;
        
        velocityY += magnusY;
        velocityZ += magnusZ;
    }
    
    // Integer square root approximation
    private int intSqrt(int n) {
        if (n <= 0) return 0;
        
        int x = n;
        int y = (x + 1) / 2;
        
        while (y < x) {
            x = y;
            y = (x + n / x) / 2;
        }
        
        return x;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isAiming && kickStarted) {
            timeMs += 16;
            
            applyMagnusEffect();
            
            // Update position (scale time by 1000 for fixed-point math)
            ballX += (velocityX * 16) / 1000;
            ballY += (velocityY * 16) / 1000;
            ballZ += (velocityZ * 16) / 1000;
            
            // Apply gravity
            velocityZ += (GRAVITY * 16) / 1000;
            
            if (ballZ < 0) {
                timer.stop();
                kickStarted = false;
            }
        }
        repaint();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (isAiming) {
            Point ballScreen = projectToScreen(ballX, ballY, ballZ);
            if (ballScreen != null) {
                int ballSize = getBallScreenSize(ballX - CAMERA_DISTANCE);
                Rectangle bounds = new Rectangle(
                    ballScreen.x - ballSize/2,
                    ballScreen.y - ballSize/2,
                    ballSize,
                    ballSize
                );
                
                if (bounds.contains(e.getPoint())) {
                    aimPoint = e.getPoint();
                    // Convert to -1000 to 1000 range for fixed-point
                    strikeX = ((e.getX() - ballScreen.x) * 2000) / ballSize;
                    strikeY = ((e.getY() - ballScreen.y) * 2000) / ballSize;
                    strikeX = Math.max(-1000, Math.min(1000, strikeX));
                    strikeY = Math.max(-1000, Math.min(1000, strikeY));
                    
                    Timer kickTimer = new Timer(500, evt -> {
                        isAiming = false;
                        kickStarted = true;
                        calculateSpinFromStrike();
                        timer.start();
                        ((Timer)evt.getSource()).stop();
                    });
                    kickTimer.setRepeats(false);
                    kickTimer.start();
                }
            }
        }
    }
    
    // Drawing methods remain similar but use integer math
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw goal posts and field grid...
        drawGoal(g2d);
        drawGrid(g2d);
        
        Point ballScreen = projectToScreen(ballX, ballY, ballZ);
        if (ballScreen != null) {
            int ballSize = getBallScreenSize(ballX - CAMERA_DISTANCE);
            
            g2d.setColor(Color.WHITE);
            g2d.fillOval(
                ballScreen.x - ballSize/2,
                ballScreen.y - ballSize/2,
                ballSize,
                ballSize
            );
            
            if (isAiming) {
                g2d.setColor(Color.BLACK);
                g2d.drawLine(ballScreen.x - ballSize/2, ballScreen.y,
                            ballScreen.x + ballSize/2, ballScreen.y);
                g2d.drawLine(ballScreen.x, ballScreen.y - ballSize/2,
                            ballScreen.x, ballScreen.y + ballSize/2);
                
                if (aimPoint != null) {
                    g2d.setColor(Color.RED);
                    g2d.fillOval(aimPoint.x - 2, aimPoint.y - 2, 4, 4);
                }
            }
        }
    }
    
    private void drawGoal(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        Point goalLeft = projectToScreen(FIELD_LENGTH, -GOAL_WIDTH/2, 0);
        Point goalRight = projectToScreen(FIELD_LENGTH, GOAL_WIDTH/2, 0);
        Point goalTopLeft = projectToScreen(FIELD_LENGTH, -GOAL_WIDTH/2, GOAL_HEIGHT);
        Point goalTopRight = projectToScreen(FIELD_LENGTH, GOAL_WIDTH/2, GOAL_HEIGHT);
        
        if (goalLeft != null && goalRight != null && goalTopLeft != null && goalTopRight != null) {
            g2d.drawLine(goalLeft.x, goalLeft.y, goalTopLeft.x, goalTopLeft.y);
            g2d.drawLine(goalRight.x, goalRight.y, goalTopRight.x, goalTopRight.y);
            g2d.drawLine(goalTopLeft.x, goalTopLeft.y, goalTopRight.x, goalTopRight.y);
        }
    }
    
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(0, 100, 0));
        for (int i = 0; i <= FIELD_LENGTH; i += 10000) {  // Every 10 feet
            for (int j = -12000; j <= 12000; j += 2000) {  // Every 2 feet
                Point p1 = projectToScreen(i, j, 0);
                Point p2 = projectToScreen(i, j + 2000, 0);
                if (p1 != null && p2 != null) {
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Soccer Free Kick Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SoccerKickSimulation());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}