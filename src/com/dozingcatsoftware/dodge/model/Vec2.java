package com.dozingcatsoftware.dodge.model;

/** Class representing a 2d vector. Ideally would be immutable but updating the fields directly
 * can greatly improve performance, so the x and y fields are exposed directly.
 */

public class Vec2 {

	public double x;
	public double y;
	
	public final static Vec2 ZERO = new Vec2(0, 0);
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2 add(Vec2 that) {
		return new Vec2(this.x+that.x, this.y+that.y);
	}
	
	public Vec2 subtract(Vec2 that) {
		return new Vec2(this.x-that.x, this.y-that.y);
	}
	
	public Vec2 multiply(double val) {
		return new Vec2(val*x, val*y);
	}
	
	public Vec2 negate() {
		return new Vec2(-x, -y);
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y);
	}
	
	public double distanceTo(Vec2 that) {
		double xdiff = this.x - that.x;
		double ydiff = this.y - that.y;
		return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	}
	
	public double squaredDistanceTo(Vec2 that) {
		double xdiff = this.x - that.x;
		double ydiff = this.y - that.y;
		return xdiff*xdiff + ydiff*ydiff;
	}
	
	public Vec2 normalize() {
		double mag = this.magnitude();
		if (mag==0.0) return new Vec2(0,0);
		return new Vec2(x/mag, y/mag);
	}
}
