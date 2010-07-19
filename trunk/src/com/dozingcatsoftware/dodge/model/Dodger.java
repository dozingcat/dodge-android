package com.dozingcatsoftware.dodge.model;

/** Represents the sprite controlled by the player which is attempting to avoid bullets. A Dodger's motion
 * can be specified by the targetPosition ivar defined in the Sprite superclass, or by the velocityDirection
 * ivar which is set when the player uses a trackball or directional pad to steer.
 */

public class Dodger extends Sprite {

	// velocity can be specified instead of targetPosition, velocityDirection is normalized
	Vec2 velocityDirection;
	
	@Override
	public void setTargetPosition(Vec2 targetPosition) {
		this.targetPosition = targetPosition;
		velocityDirection = null;
	}
	
	public Vec2 getVelocityDirection() {
		return velocityDirection;
	}
	public void setVelocityDirection(Vec2 value) {
		this.velocityDirection = value.normalize();
		this.targetPosition = null;
	}

	// if set, sprite will be constrained between min and max x/y values
	double xmin=0, xmax=0, ymin=0, ymax=0;
	
	/** Sets a bounding box for the dodger's position. Used to make sure that the player doesn't move off the 
	 * screen or past the midpoint of the goal areas.
	 */
	public void setLimits(double xmin, double ymin, double xmax, double ymax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}
	
	/** Overrides Sprite implementation to use velocityDirection if set, and to enforce bounds set in setLimits.
	 */
	@Override
	public void tick(double dt) {
		if (velocityDirection==null) {
			super.tick(dt);
		}
		else {
			double dist = speed * dt;
			position.x += dist * velocityDirection.x;
			position.y += dist * velocityDirection.y;
		}
		if (xmax>xmin && ymax>ymin) {
			this.enforceBounds(xmin, ymin, xmax, ymax);
		}
	}
}
