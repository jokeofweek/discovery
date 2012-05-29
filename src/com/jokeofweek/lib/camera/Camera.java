package com.jokeofweek.lib.camera;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;

public abstract class Camera {

	public abstract Position getPosition();
	
	public abstract void update(GameContainer container, WSwingConsoleInterface csi);
	
}
