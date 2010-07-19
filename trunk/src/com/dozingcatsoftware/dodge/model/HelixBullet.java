package com.dozingcatsoftware.dodge.model;

import java.util.Random;

/** Bullet that moves in an orbit around its straight-line path to the target position. Not currently used.
 */

public class HelixBullet extends Bullet {
	
	static final double PI2 = 2*Math.PI;
	static Random RAND = new Random();
	
	double angle;
	double angularSpeed;
	double radius;
	
	Vec2 motionDirection;
	Vec2 startPosition;
	double lifetime;
	double age;

	public static HelixBullet create(double speed, double helixRadius, double helixPeriod) {
		HelixBullet self = new HelixBullet();
		self.setSpeed(speed);
		self.angle = PI2 * RAND.nextDouble();
		self.angularSpeed = PI2 / helixPeriod;
		self.radius = helixRadius;
		return self;
	}

	public void tick(double dt) {
		if (motionDirection==null && this.targetPosition!=null && this.position!=null) {
			Vec2 toTarget = this.targetPosition.subtract(this.position);
			this.lifetime = toTarget.magnitude() / this.speed;
			
			this.startPosition = new Vec2(this.position.x, this.position.y);
			this.motionDirection = toTarget.normalize();
		}
		if (motionDirection!=null) {
			this.age += dt;
			double distance = this.age * this.speed;
			this.angle += dt * this.angularSpeed;
			if (this.angle > PI2) this.angle -= PI2;
			this.position.x = this.startPosition.x + (distance*this.motionDirection.x) + (this.radius * Math.cos(this.angle));
			this.position.y = this.startPosition.y + (distance*this.motionDirection.y) + (this.radius * Math.sin(this.angle));
		}
	}
	
	public boolean shouldRemoveFromField(Field field) {
		return this.age >= this.lifetime;
	}

}
