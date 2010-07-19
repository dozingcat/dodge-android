package com.dozingcatsoftware.dodge.model;

import java.util.Random;

/** Bullet that travels in a straight line, with a horizontal or vertical displacement given by the sine function.
 */

public class SineWaveBullet extends Bullet {
	
	double amplitude;
	double period;
	double lifetime;
	double age = 0;
	double angle;
	
	Vec2 startPosition;
	boolean movingHorizontally;
	Vec2 motionDirection;
	
	final static double PI2 = 2*Math.PI;
	static Random RAND = new Random();
	
	/** Creates and initializes a SineWaveBullet with the given parameters.
	 * 
	 * @param speed speed of the bullet as it moves along the straight-line path
	 * @param amplitude maximum displacement from the straight-line path
	 * @param period time in seconds for a full cycle
	 * @return
	 */
	public static SineWaveBullet create(double speed, double amplitude, double period) {
		SineWaveBullet self = new SineWaveBullet();
		self.setSpeed(speed);
		self.amplitude = amplitude;
		self.period = period;
		return self;
	}
	
	/** Overrides Bullet implementation to apply a displacement from the straight-line path.
	 */
	@Override
	public void tick(double dt) {
		if (motionDirection==null && this.targetPosition!=null && this.position!=null) {
			Vec2 toTarget = this.targetPosition.subtract(this.position);
			this.lifetime = toTarget.magnitude() / this.speed;
			
			this.startPosition = new Vec2(this.position.x, this.position.y);
			this.motionDirection = toTarget.normalize();
			// start sine wave at a random phase
			this.angle = PI2 * RAND.nextDouble();
			// if moving between the sides of the field, x will start at exactly 0 or 1
			movingHorizontally = (this.position.x==0.0 || this.position.x==1.0);
		}
		if (motionDirection!=null) {
			this.age += dt;
			this.angle += PI2*dt / this.period;
			if (this.angle>PI2) this.angle -= PI2;
			// get position for straight line travel and add sine wave displacement
			double distance = this.age * this.speed;
			this.position.x = this.startPosition.x + (distance*this.motionDirection.x);
			this.position.y = this.startPosition.y + (distance*this.motionDirection.y);

			double displacement = this.amplitude * Math.sin(angle);
			if (movingHorizontally) {
				this.position.y += displacement;
			}
			else {
				this.position.x += displacement;
			}
		}
	}
	
	/** Overrides bullet implementation to remove when a straight-line bullet would be at the target position.
	 * (This bullet won't be exactly at the target position because of the displacement).
	 */
	@Override
	public boolean shouldRemoveFromField(Field field) {
		return this.age >= this.lifetime;
	}


}
