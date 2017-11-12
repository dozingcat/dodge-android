package com.dozingcatsoftware.dodge.model;

/** Bullet that periodically stops and then continues moving.
 */
public class StopAndGoBullet extends Bullet {
	
	double goInterval;
	double stopInterval;
	
	double intervalTimer;
	boolean moving;
	
	public static StopAndGoBullet create(double speed, double goInterval, double stopInterval) {
		StopAndGoBullet self = new StopAndGoBullet();
		self.setSpeed(speed);
		self.goInterval = goInterval;
		self.stopInterval = stopInterval;
		self.intervalTimer = goInterval;
		self.moving = true;
		return self;
	}
	
	public void tick(double dt) {
		// move or not depending on boolean flag, then update timer and flip flag/countdown if needed
		if (moving) {
			super.tick(dt);
		}
		
		intervalTimer -= dt;
		if (intervalTimer<=0) {
			moving = !moving;
			intervalTimer = (moving) ? goInterval : stopInterval;
		}
	}

}
