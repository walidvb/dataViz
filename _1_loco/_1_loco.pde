import com.nand.locomotion.*;

MotionRecord record;

void computeLocMap()
{
  ArrayList<MotionFrame> frames = record.frames;
  int n = frames.size();
  for( MotionFrame frame : frames)
  {
    float time = (double)frame.time;
    float gyro = frame.gyroscope.mag
  }
}

void drawLocMap()
{
  
}

void setup()
{
  size(800, 600, P3D);
  record = new MotionRecord(this, "GeBeo.csv");
}

void draw()
{
background(255);


}
