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
	
	float wavePeakAngle=PI/2;
	ArrayList<PVector> listSphere;
	ArrayList<PVector> listPoints;
	ArrayList<PVector> pointColors;
	
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
	
	
	void computeGyroPoints(MotionRecord record)
	{
		for(MotionFrame frame : record.frames)
		{
			float gyro = frame.gyroscope.mag();
			float time = (float) frame.time;
			computeGyro(time, gyro);
		}
	}
	void computeGyro(float time, float gyro)
	{
		float mappedRadius = map(gyro, minGyro, maxGyro, 0, width/8);
		float rad = minRadius + mappedRadius;
		float mappedTime = map(time, minTime, maxTime, 0, TWO_PI);
		float theta = mappedTime;
		int phiDef = 100;
		float deltaPhi = PI/phiDef;

		for(int i = 0; i < phiDef; i++)
		{
				float phi = i * deltaPhi;
				float x = rad * cos(theta)*sin(phi);
				float y = rad * sin(theta)*sin(phi);
				float z = rad * cos(phi);
				listPoints.add(new PVector(x,y,z));
		}	
	}
	
	float wavePeakMag(float theta)
	{
		
		float mag = 1/abs(cos((theta - wavePeakAngle)));
		return mag;
	}
	void computeSpherePoints(float radius, int phiDef, int thetaDef)
	{
		listPoints.clear();
		pointColors.clear();
		float deltaTheta = TWO_PI/thetaDef;
		float deltaPhi = PI/phiDef;
		for(int i = 0; i < thetaDef; i++)
		{
			for(int j = 0; j < phiDef; j++)
			{
				float theta = i * deltaTheta;
				float phi = j * deltaPhi;
				float rad = radius + wavePeakMag(phi);
				float x = rad * cos(theta)*sin(phi);
				float y = rad * sin(theta)*sin(phi);
				float z = rad * cos(phi);
				listPoints.add(new PVector(x,y,z));
				float red = abs(theta - PI);
				red = map(phi, 0, PI, 0, 255);
				pointColors.add(new PVector(red, red, 255));
			}
		}
	}
	
	void drawPoints(ArrayList<PVector> pointList)
	{
		for(int i = 0; i < pointList.size(); i++)
		{
			stroke(pointColors.get(i).x, pointColors.get(i).y, pointColors.get(i).z);
			point(pointList.get(i).x, pointList.get(i).y, pointList.get(i).z);
		}
	}

	public void setup()
	{
	  size(800, 600, P3D);
	  noCursor();
	  record = new MotionRecord(this, "GeBeo.csv");

	  listPoints = new ArrayList<PVector>();
	  pointColors = new ArrayList<PVector>();
	  
	  //sphere stuff
	  minRadius = width/8;
	  radius = minRadius;
	  maxRadius = min(height, width)/2;
	  
	  //gyro stuff
	  computeMinMax(record.frames);
	  computeGyroPoints(record);
	}

	public void draw()
	{
		background(0);
		smooth();
		line(0,0, -radius/2, radius/2);
		translate(width/2, height/2);
		stroke(255);
		
		rotateY(-mouseX*0.01f);
		rotateX(-mouseY*0.01f);
		//update
		//radius = (radius*0.9f > minRadius) ? radius*0.9f : minRadius;
		wavePeakAngle -= 0.05f;//(wavePeakAngle > 0) ? wavePeakAngle-0.1f : wavePeakAngle;
	    computeSpherePoints(radius, 100, 200);
		drawPoints(listPoints);
		

	}
	
	public void mousePressed()
	{
		//radius = maxRadius;
		wavePeakAngle = TWO_PI;
	}
}
