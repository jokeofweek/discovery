package com.jokeofweek.data.map;

import com.jokeofweek.data.GameData;
import com.jokeofweek.lib.entity.Entity;
import com.jokeofweek.lib.util.Position;

/*
 * RECURSIVE SHADOWCASTING
 * Original Author: Bj�rn Bergstr�m
 * e-mail: dungeondweller@swipnet.se
 *
 * Translated to Java: Santiago Zapata
 * e-mail: java.koder@gmail.com
 *
 * fov.cpp part of rscfovdemo
 *
 * implementation of recursive shadowcasting
 *
 * 060322: Santiago Zapata - translated to Java
 *
 * 020125: Bj�rn Bergstr�m - changed from float to double to remove compiler
 *         warnings
 * 020125: Bj�rn Bergstr�m - included a check to avoid orthogonal edges to be
 *         scanned more than once
 * 020125: Greg McIntyre - declared the nwL, neL etc in FOV::start outside the
 *         for loops
 *
 */

public class Fov {
	
    private Map map;
    private GameData gameData;
    private Entity playerEntity;

	double slope(double x1, double y1, double x2, double y2){
		double xDiff=x1-x2;
		double yDiff=y1-y2;
		if(yDiff != 0){
			return xDiff/yDiff;
		}else{
			return 0;
		}
	}

	double invSlope(double x1, double y1, double x2, double y2){
		double slope=this.slope(x1,y1,x2,y2);
		if(slope != 0){
			return 1/slope;
		}else{
			return 0;
		}
	}


	/* scanNW2N
		scans the octant covering the area from north west to north from left to
		right
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanNW2N(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope){
		if(distance > maxRadius){
			return;
		}

		// calculate start and end cell of the scan
		int xStart=(int)((double)xCenter + 0.5 - (startSlope * distance));
		int xEnd=(int)((double)xCenter + 0.5 - (endSlope * distance));
		int yCheck=yCenter - distance;

		// is the starting cell the leftmost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(xStart != xCenter-(1*distance)){
			this.applyCell(xStart,yCheck);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked = this.scanCell(xStart,yCheck);

		// scan from the cell after the starting cell (xStart+1) to end cell of
		// scan (xCheck<=xEnd)
		for(int xCheck=xStart+1; xCheck<=xEnd; xCheck++){
			// is the current cell the rightmost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(xCheck != xCenter){
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by to the left of the blocking cell
			//
			// +---+a####+---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   |#####|   |  a = old [xCheck,yCheck]
			// |   |#####|   |  b = new [xCheck-0.00001,yCheck+0.99999]
			// |   |#####|   |
			// +---b#####+---+
			// +---++---++---+
			// |   ||   ||   |
			// |   ||   || @ |
			// |   ||   ||   |
			// +---++---++---+
			//
			if(this.scanCell(xCheck,yCheck)){
				if(!prevBlocked){
					this.scanNW2N(xCenter,yCenter,distance+1,maxRadius,startSlope,this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck-0.000001,(double)yCheck+0.999999));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by to the right
			// of the blocking cells
			//
			// #####a---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// #####|   ||   |  a = new and old [xCheck,yCheck]
			// #####|   ||   |
			// #####|   ||   |
			// #####+---++---+
			// +---++---++---+
			// |   ||   ||   |
			// |   ||   || @ |
			// |   ||   ||   |
			// +---++---++---+
			//
			else{
				if(prevBlocked){
					startSlope=this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck,(double)yCheck);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked){
			this.scanNW2N(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}


	/* scanNE2N
		scans the octant covering the area from north east to north from right to
		left
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanNE2N(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope){
		if(distance > maxRadius)
		{
			return;
		}

		// calculate start and end cell of the scan
		int xStart=(int)((double)xCenter + 0.5 - (startSlope * distance));
		int xEnd=(int)((double)xCenter + 0.5 - (endSlope * distance));
		int yCheck=yCenter - distance;

		// is starting cell the rightmost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(xStart != xCenter-(-1*distance))
		{
			this.applyCell(xStart,yCheck);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked=this.scanCell(xStart,yCheck);

		// scan from the cell after the starting cell (xStart-1) to end cell of
		// scan (xCheck>=xEnd)
		for(int xCheck=xStart-1; xCheck>=xEnd; xCheck--)
		{
			// is the current cell the leftmost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(xCheck != xCenter)
			{
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by to the right of the blocking cell
			//
			// +---+a####+---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   |#####|   |  a = old [xCheck,yCheck]
			// |   |#####|   |  b = new [xCheck+0.9999,yCheck-0.00001]
			// |   |#####|   |
			// +---+#####b---+
			// +---++---++---+
			// |   ||   ||   |
			// | @ ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			//
			if(this.scanCell(xCheck,yCheck))
			{
				if(!prevBlocked)
				{
					this.scanNE2N(xCenter,yCenter,distance+1,maxRadius,startSlope,this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+1,(double)yCheck+0.99999));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by to the left
			// of the blocking cells
			//
			// +---+a---b#####  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   |#####  a = old [xCheck,yCheck]
			// |   ||   |#####  b = new [xCheck+0.99999,yCheck]
			// |   ||   |#####
			// +---++---+#####
			// +---++---++---+
			// |   ||   ||   |
			// | @ ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			//
			else
			{
				if(prevBlocked)
				{
					startSlope=this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+0.9999999,(double)yCheck);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked)
		{
			this.scanNE2N(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}


	/* scanNW2W
		scans the octant covering the area from north west to west from top to
		bottom
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanNW2W(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope)
	{
		if(distance > maxRadius)
		{
			return;
		}

		// calculate start and end cell of the scan
		int yStart=(int)((double)yCenter + 0.5 - (startSlope * distance));
		int yEnd=(int)((double)yCenter + 0.5 - (endSlope * distance));
		int xCheck=xCenter - distance;

		// is starting cell the topmost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(yStart != yCenter-(1*distance))
		{
			this.applyCell(xCheck,yStart);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked=this.scanCell(xCheck,yStart);

		// scan from the cell after the starting cell (yStart+1) to end cell of
		// scan (yCheck<=yEnd)
		for(int yCheck=yStart+1; yCheck<=yEnd; yCheck++)
		{
			// is the current cell the bottommost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(yCheck != yCenter)
			{
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by the top of the blocking cell (see fig.)
			//
			// +---++---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   ||   |  a = old [xCheck,yCheck]
			// |   ||   ||   |  b = new [xCheck+0.99999,yCheck-0.00001]
			// |   ||   ||   |
			// +---b+---++---+
			// a####+---++---+
			// #####|   ||   |
			// #####|   ||   |
			// #####|   ||   |
			// #####+---++---+
			// +---++---++---+
			// |   ||   ||   |
			// |   ||   || @ |
			// |   ||   ||   |
			// +---++---++---+
			//
			if(this.scanCell(xCheck,yCheck))
			{
				if(!prevBlocked)
				{
					this.scanNW2W(xCenter,yCenter,distance+1,maxRadius,startSlope,this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+0.99999,(double)yCheck-0.00001));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by the bottom
			// of the blocking cells
			//
			// #####+---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// #####|   ||   |  a = old and new [xCheck,yCheck]
			// #####|   ||   |
			// #####|   ||   |
			// #####+---++---+
			// a---++---++---+
			// |   ||   ||   |
			// |   ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			// +---++---++---+
			// |   ||   ||   |
			// |   ||   || @ |
			// |   ||   ||   |
			// +---++---++---+
			//
			else
			{
				if(prevBlocked)
				{
					startSlope=this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck,(double)yCheck);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked)
		{
			this.scanNW2W(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}

	/* scanSW2W
		scans the octant covering the area from southe west to west from bottom to
		top
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanSW2W(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope)
	{
		if(distance > maxRadius)
		{
			return;
		}

		// calculate start and end cell of the scan
		int yStart=(int)((double)yCenter + 0.5 - (startSlope * distance));
		int yEnd=(int)((double)yCenter + 0.5 - (endSlope * distance));
		int xCheck=xCenter - distance;

		// is starting cell the bottommost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(yStart != yCenter-(-1*distance))
		{
			this.applyCell(xCheck,yStart);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked=this.scanCell(xCheck,yStart);

		// scan from the cell after the starting cell (yStart-1) to end cell of
		// scan (yCheck>=yEnd)
		for(int yCheck=yStart-1; yCheck>=yEnd; yCheck--)
		{
			// is the current cell the topmost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(yCheck != yCenter)
			{
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by the bottom of the blocking cell
			//
			// +---++---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   ||   |  a = old [xCheck,yCheck]
			// |   ||   || @ |  b = new [xCheck+0.99999,yCheck+1]
			// |   ||   ||   |
			// +---++---++---+
			// a####+---++---+
			// #####|   ||   |
			// #####|   ||   |
			// #####|   ||   |
			// #####+---++---+
			// +---b+---++---+
			// |   ||   ||   |
			// |   ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			//
			if(this.scanCell(xCheck,yCheck))
			{
				if(!prevBlocked)
				{
					this.scanSW2W(xCenter,yCenter,distance+1,maxRadius,startSlope,this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+0.99999,(double)yCheck+1));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by the top of
			// the blocking cells
			//
			// +---++---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   ||   |  a = old [xCheck,yCheck]
			// |   ||   || @ |  b = new [xCheck,yCheck+0.99999]
			// |   ||   ||   |
			// +---++---++---+
			// a---++---++---+
			// |   ||   ||   |
			// |   ||   ||   |
			// |   ||   ||   |
			// b---++---++---+
			// #####+---++---+
			// #####|   ||   |
			// #####|   ||   |
			// #####|   ||   |
			// #####+---++---+
			//
			else
			{
				if(prevBlocked)
				{
					startSlope=this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck,(double)yCheck+0.99999);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked)
		{
			this.scanSW2W(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}


	/* scanSW2S
		scans the octant covering the area from south west to south from left to
		right
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanSW2S(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope)
	{
		if(distance > maxRadius)
		{
			return;
		}

		// calculate start and end cell of the scan
		int xStart=(int)((double)xCenter + 0.5 + (startSlope * distance));
		int xEnd=(int)((double)xCenter + 0.5 + (endSlope * distance));
		int yCheck=yCenter + distance;

		// is the starting cell the leftmost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(xStart != xCenter+(-1*distance))
		{
			this.applyCell(xStart,yCheck);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked=this.scanCell(xStart,yCheck);

		// scan from the cell after the starting cell (xStart+1) to end cell of
		// scan (xCheck<=xEnd)
		for(int xCheck=xStart+1; xCheck<=xEnd; xCheck++)
		{
			// is the current cell the rightmost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(xCheck != xCenter)
			{
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by to the left of the blocking cell
			//
			// +---++---++---+
			// |   ||   ||   |
			// |   ||   || @ |
			// |   ||   ||   |
			// +---++---++---+
			// +---ba####+---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   |#####|   |  a = old [xCheck,yCheck]
			// |   |#####|   |  b = new [xCheck-0.00001,yCheck]
			// |   |#####|   |
			// +---+#####+---+
			//
			if(this.scanCell(xCheck,yCheck))
			{
				if(!prevBlocked)
				{
					this.scanSW2S(xCenter,yCenter,distance+1,maxRadius,startSlope,this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck-0.00001,(double)yCheck));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by to the right
			// of the blocking cells
			//
			// +---++---++---+
			// |   ||   ||   |
			// |   ||   || @ |
			// |   ||   ||   |
			// +---++---++---+
			// #####a---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// #####|   ||   |  a = old [xCheck,yCheck]
			// #####|   ||   |  b = new [xCheck,yCheck+0.99999]
			// #####|   ||   |
			// #####b---++---+
			//
			else
			{
				if(prevBlocked)
				{
					startSlope=this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck,(double)yCheck+0.99999);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked)
		{
			this.scanSW2S(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}


	/* scanSE2S
		scans the octant covering the area from south east to south from right to
		left
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanSE2S(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope)
	{
		if(distance > maxRadius)
		{
			return;
		}

		// calculate start and end cell of the scan
		int xStart=(int)((double)xCenter + 0.5 + (startSlope * distance));
		int xEnd=(int)((double)xCenter + 0.5 + (endSlope * distance));
		int yCheck=yCenter + distance;

		// is starting cell the rightmost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(xStart != xCenter+(1*distance))
		{
			this.applyCell(xStart,yCheck);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked=this.scanCell(xStart,yCheck);

		// scan from the cell after the starting cell (xStart-1) to end cell of
		// scan (xCheck>=xEnd)
		for(int xCheck=xStart-1; xCheck>=xEnd; xCheck--)
		{
			// is the current cell the leftmost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(xCheck != xCenter)
			{
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by to the right of the blocking cell
			//
			// +---++---++---+
			// |   ||   ||   |
			// | @ ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			// +---+a####b---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   |#####|   |  a = old [xCheck,yCheck]
			// |   |#####|   |  b = new [xCheck+1,yCheck]
			// |   |#####|   |
			// +---+#####+---+
			//
			if(this.scanCell(xCheck,yCheck))
			{
				if(!prevBlocked)
				{
					this.scanSE2S(xCenter,yCenter,distance+1,maxRadius,startSlope,this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+1,(double)yCheck));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by to the left
			// of the blocking cells
			//
			// +---++---++---+
			// |   ||   ||   |
			// | @ ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			// +---+a---+#####  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   |#####  a = old [xCheck,yCheck]
			// |   ||   |#####  b = new [xCheck+0.99999,yCheck+0.99999]
			// |   ||   |#####
			// +---++---b#####
			//
			else
			{
				if(prevBlocked)
				{
					startSlope=this.slope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+0.99999,(double)yCheck+0.99999);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked)
		{
			this.scanSE2S(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}


	/* scanNE2E
		scans the octant covering the area from north east to east from top to
		bottom
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanNE2E(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope)
	{
		if(distance > maxRadius)
		{
			return;
		}

		// calculate start and end cell of the scan
		int yStart=(int)((double)yCenter + 0.5 + (startSlope * distance));
		int yEnd=(int)((double)yCenter + 0.5 + (endSlope * distance));
		int xCheck=xCenter + distance;

		// is starting cell the topmost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(yStart != yCenter+(-1*distance))
		{
			this.applyCell(xCheck,yStart);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked=this.scanCell(xCheck,yStart);

		// scan from the cell after the starting cell (yStart+1) to end cell of
		// scan (yCheck<=yEnd)
		for(int yCheck=yStart+1; yCheck<=yEnd; yCheck++)
		{
			// is the current cell the bottommost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(yCheck != yCenter)
			{
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by the top of the blocking cell (see fig.)
			//
			// +---++---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   ||   |  a = old [xCheck,yCheck]
			// |   ||   ||   |  b = new [xCheck,yCheck-0.00001]
			// |   ||   ||   |
			// +---++---+b---+
			// +---++---+a####
			// |   ||   |#####
			// |   ||   |#####
			// |   ||   |#####
			// +---++---+#####
			// +---++---++---+
			// |   ||   ||   |
			// | @ ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			//
			if(this.scanCell(xCheck,yCheck))
			{
				if(!prevBlocked)
				{
					this.scanNE2E(xCenter,yCenter,distance+1,maxRadius,startSlope,this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck,(double)yCheck-0.00001));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by the bottom
			// of the blocking cells
			//
			// +---++---+#####  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   |#####  a = old [xCheck,yCheck]
			// |   ||   |#####  b = new [xCheck+0.99999,yCheck]
			// |   ||   |#####
			// +---++---+#####
			// +---++---+a---b
			// |   ||   ||   |
			// |   ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			// +---++---++---+
			// |   ||   ||   |
			// | @ ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			//
			else
			{
				if(prevBlocked)
				{
					startSlope=this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+0.99999,(double)yCheck);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked)
		{
			this.scanNE2E(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}

	/* scanSE2E
		scans the octant covering the area from south east to east from bottom to
		top
		the method ignores the octants starting and ending cells since they have
		been applied in FOV::start
	*/
	void scanSE2E(int xCenter, int yCenter, int distance, int maxRadius, double startSlope, double endSlope)
	{
		if(distance > maxRadius)
		{
			return;
		}

		// calculate start and end cell of the scan
		int yStart=(int)((double)yCenter + 0.5 + (startSlope * distance));
		int yEnd=(int)((double)yCenter + 0.5 + (endSlope * distance));
		int xCheck=xCenter + distance;

		// is starting cell the bottommost cell in the octant?
		// NO: call applyCell() to starting cell
		// YES: it has already been applied in FOV::start()
		if(yStart != yCenter+(1*distance))
		{
			this.applyCell(xCheck,yStart);
		}

		// find out if starting cell blocks LOS
		boolean prevBlocked=this.scanCell(xCheck,yStart);

		// scan from the cell after the starting cell (yStart-1) to end cell of
		// scan (yCheck>=yEnd)
		for(int yCheck=yStart-1; yCheck>=yEnd; yCheck--)
		{
			// is the current cell the topmost cell in the octant?
			// NO: call applyCell() to current cell
			// YES: it has already been applied in FOV::start()
			if(yCheck != yCenter)
			{
				// apply cell
				this.applyCell(xCheck,yCheck);
			}

			// cell blocks LOS
			// if previous cell didn't block LOS (prevBlocked==false) we have
			// hit a 'new' section of walls. a new scan will be started with an
			// endSlope that 'brushes' by the bottom of the blocking cell
			//
			// +---++---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   ||   |  a = old [xCheck,yCheck]
			// | @ ||   ||   |  b = new [xCheck,yCheck+1]
			// |   ||   ||   |
			// +---++---++---+
			// +---++---+a####
			// |   ||   |#####
			// |   ||   |#####
			// |   ||   |#####
			// +---++---+#####
			// +---++---+b---+
			// |   ||   ||   |
			// |   ||   ||   |
			// |   ||   ||   |
			// +---++---++---+
			//
			if(this.scanCell(xCheck,yCheck))
			{
				if(!prevBlocked)
				{
					this.scanSE2E(xCenter,yCenter,distance+1,maxRadius,startSlope,this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck,(double)yCheck+1));
				}
				prevBlocked=true;
			}

			// cell doesn't block LOS
			// if the cell is the first non-blocking cell after a section of walls
			// we need to calculate a new startSlope that 'brushes' by the top of
			// the blocking cells
			//
			// +---++---++---+  @ = [xCenter+0.5,yCenter+0.5]
			// |   ||   ||   |  a = old [xCheck,yCheck]
			// | @ ||   ||   |  b = new [xCheck+0.99999,yCheck+0.99999]
			// |   ||   ||   |
			// +---++---++---+
			// +---++---+a---+
			// |   ||   ||   |
			// |   ||   ||   |
			// |   ||   ||   |
			// +---++---++---b
			// +---++---+#####
			// |   ||   |#####
			// |   ||   |#####
			// |   ||   |#####
			// +---++---+#####
			//
			else
			{
				if(prevBlocked)
				{
					startSlope=this.invSlope((double)xCenter+0.5,(double)yCenter+0.5,(double)xCheck+0.99999,(double)yCheck+0.99999);
				}
				prevBlocked=false;
			}
		}

		// if the last cell of the scan didn't block LOS a new scan should be
		// started
		if(!prevBlocked)
		{
			this.scanSE2E(xCenter,yCenter,distance+1,maxRadius,startSlope,endSlope);
		}
	}

	private int maxRadiusX;
	private int startX, startY;
	private boolean circle;

	public void startCircle(int x, int y, int maxRadius){
		circle = true;
		this.maxRadiusX = maxRadius;
		startX = x;
		startY = y;
		start( x, y, maxRadius);
	}

	public void start(int x, int y, int maxRadius)
	{

                this.maxRadiusX = maxRadius;
                
		// apply starting cell
		this.applyCell(x,y);

		if(maxRadius > 0)
		{
			// scan and apply north
			// until a blocking cell is hit or
			// until maxRadius is reached
			int nL;
			for(nL=1; nL<=maxRadius; nL++)
			{
				this.applyCell(x,y-nL);
				if(this.scanCell(x,y-nL))
				{
					break;
				}
			}

			// scan and apply north east
			// until a blocking cell is hit or
			// until maxRadius is reached
			int neL;
			for(neL=1; neL<=maxRadius; neL++)
			{
				this.applyCell(x+neL,y-neL);
				if(this.scanCell(x+neL,y-neL))
				{
					break;
				}
			}

			// scan and apply east
			// until a blocking cell is hit or
			// until maxRadius is reached
			int eL;
			for(eL=1; eL<=maxRadius; eL++)
			{
				this.applyCell(x+eL,y);
				if(this.scanCell(x+eL,y))
				{
					break;
				}
			}

			// scan and apply south east
			// until a blocking cell is hit or
			// until maxRadius is reached
			int seL;
			for(seL=1; seL<=maxRadius; seL++)
			{
				this.applyCell(x+seL,y+seL);
				if(this.scanCell(x+seL,y+seL))
				{
					break;
				}
			}

			// scan and apply south
			// until a blocking cell is hit or
			// until maxRadius is reached
			int sL;
			for(sL=1; sL<=maxRadius; sL++)
			{
				this.applyCell(x,y+sL);
				if(this.scanCell(x,y+sL))
				{
					break;
				}
			}

			// scan and apply south west
			// until a blocking cell is hit or
			// until maxRadius is reached
			int swL;
			for(swL=1; swL<=maxRadius; swL++)
			{
				this.applyCell(x-swL,y+swL);
				if(this.scanCell(x-swL,y+swL))
				{
					break;
				}
			}

			// scan and apply west
			// until a blocking cell is hit or
			// until maxRadius is reached
			int wL;
			for(wL=1; wL<=maxRadius; wL++)
			{
				this.applyCell(x-wL,y);
				if(this.scanCell(x-wL,y))
				{
					break;
				}
			}

			// scan and apply north west
			// until a blocking cell is hit or
			// until maxRadius is reached
			int nwL;
			for(nwL=1; nwL<=maxRadius; nwL++)
			{
				this.applyCell(x-nwL,y-nwL);
				if(this.scanCell(x-nwL,y-nwL))
				{
					break;
				}
			}


			// scan the octant covering the area from north west to north
			// if it isn't blocked
			if(nL!=1 || nwL!=1)
			{
				this.scanNW2N(x,y,1,maxRadius,1,0);
			}

			// scan the octant covering the area from north east to north
			// if it isn't blocked
			if(nL!=1 || neL!=1)
			{
				this.scanNE2N(x,y,1,maxRadius,-1,0);
			}

			// scan the octant covering the area from north west to west
			// if it isn't blocked
			if(nwL!=1 || wL!=1)
			{
				this.scanNW2W(x,y,1,maxRadius,1,0);
			}

			// scan the octant covering the area from south west to west
			// if it isn't blocked
			if(swL!=1 || wL!=1)
			{
				this.scanSW2W(x,y,1,maxRadius,-1,0);
			}

			// scan the octant covering the area from south west to south
			// if it isn't blocked
			if(swL!=1 || sL!=1)
			{
				this.scanSW2S(x,y,1,maxRadius,-1,0);
			}

			// scan the octant covering the area from south east to south
			// if it isn't blocked
			if(seL!=1 || sL!=1)
			{
				this.scanSE2S(x,y,1,maxRadius,1,0);
			}

			// scan the octant covering the area from north east to east
			// if it isn't blocked
			if(neL!=1 || eL!=1)
			{
				this.scanNE2E(x,y,1,maxRadius,-1,0);
			}

			// scan the octant covering the area from south east to east
			// if it isn't blocked
			if(seL!=1 || eL!=1)
			{
				this.scanSE2E(x,y,1,maxRadius,1,0);
			}

		}
	}

    boolean scanCell(int x, int y)
	{
            if (x < 0 || y < 0 || x >= Map.MAP_SIZE || y >= Map.MAP_SIZE)
                return false;

            if (!circle)
                return map.getTiles()[x][y].checkFlag(Tile.ATTRIBUTE_BLOCKSIGHT);
            else {
                if (Position.distance(x,y, startX, startY) >= maxRadiusX)
                    return true;
				else
					return map.getTiles()[x][y].checkFlag(Tile.ATTRIBUTE_BLOCKSIGHT);
			}
	}

	void applyCell(int x, int y)
	{
            if (x < 0 || y < 0 || x >= Map.MAP_SIZE || y >= Map.MAP_SIZE)
                return;

           /* if (map.getTiles()[x][y].getAttribute(Tile.TileAttribute.DARK))
            {
                if (Position.distance(new Position(x,y), Game.player.getPosition())<= 1){
                    map.tile[x][y].setAttribute(Tile.TileAttribute.EXPLORED,false);
                    map.tile[x][y].setAttribute(Tile.TileAttribute.INSIGHT,true);
                }
            
            } else {*/
            if (Position.distance(new Position(x,y), playerEntity.getPosition())<= this.maxRadiusX){
                    map.getTiles()[x][y].setFlag(Tile.ATTRIBUTE_INSIGHT,true);
            }
            //}
	}

	public Fov(GameData gameData)
	{
		this.gameData = gameData;
		this.map = gameData.getMap();
		this.playerEntity = gameData.getPlayer().getEntity();
	}

}
