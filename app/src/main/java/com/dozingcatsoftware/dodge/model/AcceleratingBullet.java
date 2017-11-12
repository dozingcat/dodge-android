package com.dozingcatsoftware.dodge.model;

/** Bullet that increases speed over time. Not currently used.
 */

public class AcceleratingBullet extends Bullet {
	
	double acceleration;
	
	public static AcceleratingBullet create(double speed, double accel) {
		AcceleratingBullet self = new AcceleratingBullet();
		self.setSpeed(speed);
		self.setAcceleration(accel);
		return self;
	}
	
	public void tick(double dt) {
		speed += acceleration * dt;
		super.tick(dt);
	}
	
	////////////////////////////////////////////////////

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

}
