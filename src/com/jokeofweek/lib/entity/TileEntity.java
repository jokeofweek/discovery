package com.jokeofweek.lib.entity;

import com.jokeofweek.data.map.resource.Resource;
import com.jokeofweek.lib.CSIColor;

public class TileEntity extends Renderable {

	Resource type;
	
	public TileEntity(Resource type, char character, CSIColor color) {
		this(type, character, color, CSIColor.TRANSPARENT);
	}

	public TileEntity(Resource type, char character, CSIColor color, CSIColor backgroundColor){
		super(character, color, backgroundColor);
		this.type = type;
	}
	
	public Resource getType() {
		return type;
	}

	public void setType(Resource type) {
		this.type = type;
	}

}
