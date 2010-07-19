package com.dozingcatsoftware.dodge.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** This class contains the logic for determining the number of types of bullets for each level.
 */

public class LevelManager {
	
	int BASE_BULLETS = 10;
	int NEW_BULLETS_PER_LEVEL = 3;
	Class DEFAULT_BULLET_CLASS = Bullet.class;
	
	// This structure defines the bullet frequencies as they change with levels
	List<LevelConfig> LEVEL_INFO = Arrays.asList(
		new LevelConfig(5, Arrays.asList(new LevelBulletConfig(90, Bullet.class),
				                         new LevelBulletConfig(10, StopAndGoBullet.class))),
				                         
		new LevelConfig(10, Arrays.asList(new LevelBulletConfig(90, Bullet.class),
						                  new LevelBulletConfig( 5, StopAndGoBullet.class),
						                  new LevelBulletConfig( 5, SineWaveBullet.class))),
				                         
		new LevelConfig(15, Arrays.asList(new LevelBulletConfig(85, Bullet.class),
								          new LevelBulletConfig(10, StopAndGoBullet.class),
								          new LevelBulletConfig( 5, SineWaveBullet.class))),
								          
		new LevelConfig(20, Arrays.asList(new LevelBulletConfig(80, Bullet.class),
										  new LevelBulletConfig(10, StopAndGoBullet.class),
										  new LevelBulletConfig(10, SineWaveBullet.class)))
								          
	);
	
	int currentLevel;
	LevelConfig currentLevelConfig;
	
	// Describes the bullet frequencies for a level. Contains a list of LevelBulletConfig objects which 
	// have a bullet class and frequency weight.
	static class LevelConfig {
		public int level;
		public List<LevelBulletConfig> bulletConfigs;
		int totalFreq = 0;
		static Random RAND = new Random();
		
		public LevelConfig(int level, List bulletConfigs) {
			this.level = level;
			this.bulletConfigs = bulletConfigs;
			for(LevelBulletConfig bc : this.bulletConfigs) {
				totalFreq += bc.frequency;
			}
		}
		
		public Class<Bullet> bulletClassForValue(int value) {
			// keep subtracting frequencies from value until it hits 0
			// e.g. if frequencies are (10, 20, 70) 0-9 will pick the first, 10-29 second, rest third
			for(LevelBulletConfig bc : this.bulletConfigs) {
				value -= bc.frequency;
				if (value<0) return bc.bulletClass;
			}
			// shouldn't be here, take last
			return this.bulletConfigs.get(this.bulletConfigs.size()-1).bulletClass;
		}
		
		public Class selectBulletClass() {
			return bulletClassForValue(RAND.nextInt(this.totalFreq));
		}
	}
	
	static class LevelBulletConfig {
		public int frequency;
		public Class bulletClass;
		
		public LevelBulletConfig(int f, Class b) {
			this.frequency = f;
			this.bulletClass = b;
		}
	}
	
	public LevelManager() {
		setCurrentLevel(1);
	}

	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public void setCurrentLevel(int level) {
		currentLevel = level;
		
		LevelConfig newConfig = null;
		for(LevelConfig lc : LEVEL_INFO) {
			if (level>=lc.level) {
				newConfig = lc;
			}
			else break;
		}
		currentLevelConfig = newConfig;
	}
	
	public int numberOfBulletsForLevel(int level) {
		return BASE_BULLETS + (level-1) * NEW_BULLETS_PER_LEVEL;
	}
	
	public int numberOfBulletsForCurrentLevel() {
		return numberOfBulletsForLevel(currentLevel);
	}
	
	public Class selectBulletClassForLevel(int level) {
		if (currentLevelConfig==null) return DEFAULT_BULLET_CLASS;
		return currentLevelConfig.selectBulletClass();
	}
	
	public Class selectBulletClassForCurrentLevel() {
		return selectBulletClassForLevel(currentLevel);
	}
}
