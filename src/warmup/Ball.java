package warmup;

public class Ball {
    
    private physics.Vect velocity;
    private physics.Circle circle;
    
    public Ball(double xVelocity, double yVelocity, double xCoor, double yCoor, double radius) {
        velocity = new physics.Vect(xVelocity, yVelocity);
        circle = new physics.Circle(xCoor, yCoor, radius);
    }
    
    // duh
    public physics.Circle getCircle() {
        return circle;
    }
    
    // duh
    public physics.Vect getCenter() {
        return circle.getCenter();
    }
    
    // duh
    public void setCenter(physics.Vect v) {
        circle = new physics.Circle(v, circle.getRadius());
    }
    
    // duh
    public physics.Vect getVelocity() {
        return velocity;
    }
    
    // duh
    public void setVelocity(physics.Vect v) {
        velocity = v;
    }
    
    // duh
    public double getRadius() {
        return circle.getRadius();
    }
    
    //duh
    public void move(double timestep) {
        circle = new physics.Circle(velocity.times(timestep).plus(circle.getCenter()), circle.getRadius());
    }

}