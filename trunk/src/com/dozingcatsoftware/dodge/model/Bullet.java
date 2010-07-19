package com.dozingcatsoftware.dodge.model;

/** Represents a bullet on the field that the player attempts to avoid. The base Bullet object moves in a 
 * straight line to its target position, and is then removed from the field.
 */

public class Bullet extends Sprite {

	/** Creates a Bullet with the specified speed. Its current and target positions should be set using
	 * the Sprite methods setPosition and setTargetPosition.
	 */
	public static Bullet create(double speed) {
		Bullet self = new Bullet();
		self.setSpeed(speed);
		return self;
	}
	

	/** Returns true if the bullet should be removed from the field. This implementation returns true if the
	 * bullet is sufficiently close to its target position. Subclasses can override if their logic is different;
	 * see SineWaveBullet.
	 */
	public boolean shouldRemoveFromField(Field field) {
		return this.squaredDistanceToTarget()<0.0001;
	}

}
