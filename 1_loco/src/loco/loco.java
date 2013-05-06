package loco;

import java.util.ArrayList;

import processing.core.*;
import com.nand.locomotion.*;

public class loco extends PApplet {
	MotionRecord record;
	float radius, minRadius, maxRadius;
	float minGyro=0;
	float maxGyro=0;
	float minTime=0;
	float maxTime=0;
	
	float wavePeakAngle=PI;
	ArrayList<PVector> listSphere;
	
	void computeLocMap()
	{
	  ArrayList<MotionFrame> frames = record.frames;
	  for( MotionFrame frame : frames)
	  {
	    float time = (float)frame.time;
	    float gyro = frame.gyroscope.mag();
	  }
	}
	
	void computeMinMax(ArrayList<MotionFrame> frames)
	{
		int s = frames.size();
		float[] tabGyro = new float[s];
		float[] tabTime = new float[s];
		
		for(int i = 0; i < s; i++)
		{
			float magGyro = frames.get(i).gyroscope.mag();
			tabGyro[i] = magGyro;
			tabTime[i] = (float)frames.get(i).time;
		}
		minGyro = min(tabGyro);
		maxGyro = max(tabGyro);
		minTime = min(tabTime);
		maxTime = max(tabTime);
	}
	
	void computeGyro(float gyro, float time)
	{
		float mappedRadius = map(gyro, minGyro, maxGyro, 0, minRadius);
		float rad = minRadius + mappedRadius;
		float mappedTime = map(time, minTime, maxTime, 0, TWO_PI);
	}
	
	float wavePeakPos(float theta)
	{
		float mag = ((theta - wavePeakAngle));
		return mag*mag;
	}
	void computeSphere(float radius, int phiDef, int thetaDef)
	{
		listSphere.clear();
		float deltaTheta = TWO_PI/thetaDef;
		float deltaPhi = PI/phiDef;
		
		for(int i = 0; i < thetaDef; i++)
		{
			for(int j = 0; j < phiDef; j++)
			{
				float theta = i * deltaTheta;
				float phi = j * deltaPhi;
				
				float x = radius * cos(theta)*sin(phi)*wavePeakPos(theta);
				float y = radius * sin(theta)*sin(phi);
				float z = radius * cos(phi);
				listSphere.add(new PVector(x,y,z));
			}
		}
	}
	
	void drawPoints(ArrayList<PVector> pointList)
	{
		for(PVector pos : pointList)
		{
			point(pos.x, pos.y, pos.z);
		}
	}
	void drawLocMap()
	{
	  
	}

	public void setup()
	{
	  size(800, 600, P3D);
	  record = new MotionRecord(this, "GeBeo.csv");
	  listSphere = new ArrayList<PVector>();
	  minRadius = width/8;
	  radius = minRadius;
	  maxRadius = min(height, width)/2;
	  
	}

	public void draw()
	{
		background(0);
		smooth();
		translate(width/2, height/2);
		stroke(255);
		rotateY(-mouseX*0.01f);
		rotateX(-mouseY*0.01f);
		
		//update
		//radius = (radius*0.9f > minRadius) ? radius*0.9f : minRadius;
		wavePeakAngle = (wavePeakAngle > 0) ? wavePeakAngle-0.3f : wavePeakAngle;
	    computeSphere(radius, 100, 500);
		drawPoints(listSphere);
		

	}
	
	public void mousePressed()
	{
		//radius = maxRadius;
		wavePeakAngle = TWO_PI;
	}
}
