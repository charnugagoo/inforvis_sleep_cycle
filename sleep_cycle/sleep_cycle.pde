String[] data;
int[] sleep;
int[] awake;
int count;
int xshift;

int[] fullmoon;
int[] newmoon;

// output coordinaries
int currx1, currx2, curry1, curry2;

void setup() {
// reading data
  // data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle/data.txt");
  data = loadStrings("/Users/charnugagoo/Dropbox/Study/Data Visulization/SleepingCircle/infovis_sleep_cycle/sleep_cycle/data.txt");
  count = data.length;
  sleep = new int[count];
  awake = new int[count];
  for(int i=0; i<count; i++) {
    String[] times = split(data[i], ' ');
    sleep[i] = Integer.parseInt(times[0]);
    awake[i] = Integer.parseInt(times[1]);
  }
// moon data
  data = loadStrings("/Users/charnugagoo/Dropbox/Study/Data Visulization/SleepingCircle/infovis_sleep_cycle/sleep_cycle/FullMoon.txt");
  // data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle/FullMoon.txt");
  fullmoon = new int[data.length];
  for(int i=0; i<data.length; i++) {
    fullmoon[i] = Integer.parseInt(data[i]);
  }
  // data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle/NewMoon.txt");
  data = loadStrings("/Users/charnugagoo/Dropbox/Study/Data Visulization/SleepingCircle/infovis_sleep_cycle/sleep_cycle/NewMoon.txt");
  newmoon = new int[data.length];
  for(int i=0; i<data.length; i++) {
    newmoon[i] = Integer.parseInt(data[i]);
  }
    
// setup sketch field
  size(1000, 400);
  currx1 = 0;
  currx2 = currx1 + width;
  curry1 = min(min(sleep), min(awake));
  curry2 = max(max(sleep), max(awake));
} 

void draw() {
  background(240);
  strokeWeight(5);
  for(int i = 0; i < count; i++) {
    float x = map(i * 6, 0, width, xshift, width + xshift);
    float y1 = map(sleep[i], curry1, curry2, 0, height);
    float y2 = map(awake[i], curry1, curry2, 0, height);
    stroke(120);
    line(x, y1, x, y2);
    if(i % 30 == 0) {
      strokeWeight(1);
      line(x, 0, x, height);
      strokeWeight(5);
    };
    stroke(0, 0, 120);
    point(x, y1);
    stroke(0, 0, 120);
    point(x, y2);
  }
  for(int i = 0; i < fullmoon.length; i++) {
    float x = map(fullmoon[i] * 6, 0, width, xshift, width + xshift);
    stroke(0);
    fill(255);
    strokeWeight(1);
    ellipse(x, height - 10, 10, 10);
  }
  for(int i = 0; i < newmoon.length; i++) {
    float x = map(newmoon[i] * 6, 0, width, xshift, width + xshift);
    stroke(0);
    fill(0);
    strokeWeight(1);
    ellipse(x, height - 10, 10, 10);
  }
  smooth();
}

void keyPressed() {
  if (key == ',') {
    xshift += 20;
  } else if (key == '.') {
    xshift -= 20;
  } else if (key == '<') {
    xshift += 100;
  } else if (key == '>') {
    xshift -= 100;
  }
}

