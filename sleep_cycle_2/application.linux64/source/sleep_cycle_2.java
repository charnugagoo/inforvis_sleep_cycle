import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sleep_cycle_2 extends PApplet {

class LogEntry {
  float begin;
  float duration;
  int year, month, day, hour, minute;
  public void set(String entry) {
    String[] s = split(entry, ',');
    begin    = PApplet.parseFloat(s[0]);
    duration = PApplet.parseFloat(s[1]);
    year     = PApplet.parseInt(s[2]);
    month    = PApplet.parseInt(s[3]);
    day      = PApplet.parseInt(s[4]);
    hour     = PApplet.parseInt(s[5]);
    minute   = PApplet.parseInt(s[6]);
  }
};

class TempEntry {
  float time;
  float avg;
  float high;
  float low;
  public void set(String entry) {
    String[] s = split(entry, ',');
    time = PApplet.parseFloat(s[0]);
    avg  = PApplet.parseFloat(s[1]);
    high = PApplet.parseFloat(s[2]);
    low  = PApplet.parseFloat(s[3]);
  }
}

LogEntry[] log;
LogEntry[] firstq;
LogEntry[] lastq;
LogEntry[] fullmoon;
LogEntry[] newmoon;

TempEntry[] temp;

// ArrayList[] moon;


String[] data;
int count;
float first;
float timespan = 12;
float offset = 0;
float cycle = 24;
float gridstep = 0.5f;

boolean showgrid = true;
boolean showtemp = true;
boolean yearundrawn = true;

public void setup() {
  // loading data
  data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle_2/sleeplog.csv");
  count = data.length;
  log = new LogEntry[count];
  for (int i = 0; i < count; i++) {
    log[i] = new LogEntry(); // why do we need this line?
    log[i].set(data[i]);
  }
  first = PApplet.parseInt(log[0].begin);
  
  data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle_2/MoonPhrase/New Moon.csv");
  newmoon = new LogEntry[data.length];
  for (int i = 0; i < data.length; i++) {
    newmoon[i] = new LogEntry();
    newmoon[i].set(data[i]);
  }

  data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle_2/MoonPhrase/Full Moon.csv");
  fullmoon = new LogEntry[data.length];
  for (int i = 0; i < data.length; i++) {
    fullmoon[i] = new LogEntry();
    fullmoon[i].set(data[i]);
  }

  data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle_2/MoonPhrase/First Quarter.csv");
  firstq = new LogEntry[data.length];
  for (int i = 0; i < data.length; i++) {
    firstq[i] = new LogEntry();
    firstq[i].set(data[i]);
  }

  data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle_2/MoonPhrase/Last Quarter.csv");
  lastq = new LogEntry[data.length];
  for (int i = 0; i < data.length; i++) {
    lastq[i] = new LogEntry();
    lastq[i].set(data[i]);
  }

  // setup sketch field
  size(1440, 600);
}

public void drawaxis() {
  for(float t = first + offset; t <= first + timespan + offset; t += gridstep / 24.0f) {
    drawtimepoint(t, 1);
  }
}

public void drawdate(LogEntry l) {
  float x = map(PApplet.parseInt(l.begin), first + offset, first + timespan + offset, 0, width);
  pushStyle();
  fill(0);
  textAlign(CENTER);
  if(timespan >= 32) {
    if(l.day == 1) {
      text(l.month + "/" + l.year, x, height * 7 / 8 - 10);
    }
  } else {
    text(l.month + "/" + l.day, x, height * 7 / 8 - 10);
    if(yearundrawn) {
      text(l.year, width / 2, height * 7 / 8 + 10);
      yearundrawn = false;
    }
  }
  popStyle();
}

public void drawtimepoint(float time, int mode) {
  float x = map(time, first + offset, first + timespan + offset, 0, width);
  float y = map(cos(time * TWO_PI * 24 / cycle), -1, 1, height / 4, height * 3 / 4);
  noStroke();
  // stripe color: red in 0:00 - 12:00, gray in 12:00 - 23:59 
//  ellipse(x, y, max(500 / timespan, 1), 5);
  float w = max(800 / timespan, 8) / 2; // width of rect
  switch(mode) {
    case 1:
    fill(0);
    rect(x - w / 2, y - 0.25f, w, 0.5f);
    break;
    default:
    fill(map(sin(time * TWO_PI), -1, 1, 64, 255), 64, 64);
    rect(x - w / 2, y - 4, w, 8);
  }
}

public void drawtimeperiod(float time, float dur) {
  for (float t = 0; t < dur; t = t + 5.0f / 1440) {
    drawtimepoint(time + t, 0);
  }
}

public void drawmoon(LogEntry[] moon, int mode) {
  pushStyle();
  float r = 20; // radius of moon icon
  float y = height * 3 / 16; // y-axis of moon icon
  for(int i = 0; i < moon.length; i++) {
    float x = map(moon[i].begin, first + offset, first + timespan + offset, 0, width);
    switch(mode) {
      case 0: { // new moon
        stroke(0);
        fill(0);
        ellipse(x, y, r, r);
        break;
      }
      case 1: { // first quarter
        stroke(0);
        fill(0);
        arc(x, y, r, r, PI / 3, PI * 4 / 3, PIE);
        fill(255);
        arc(x, y, r, r, -PI * 2 / 3, PI / 3, OPEN);
        break;
      }
      case 2: { // full moon
        stroke(0);
        fill(255);
        ellipse(x, y, r, r);
        break;
      }
      case 3: { // first quarter
        stroke(0);
        fill(0);
        arc(x, y, r, r, -PI * 2 / 3, PI / 3, PIE);
        fill(255);
        arc(x, y, r, r, PI / 3, PI * 4 / 3, OPEN);
        break;
      }
    }
  }
  popStyle();
}

public void drawtemp() {
  String[] data = loadStrings("/Users/tom/Documents/Codes/infovis_sleep_cycle/sleep_cycle_2/AverageMaxMinTemp2009.csv"); 
  temp = new TempEntry[data.length];
  for(int i = 0; i < data.length; i++) {
    temp[i] = new TempEntry();
    temp[i].set(data[i]);
  }
  pushStyle();
  for(int i = 0; i < temp.length; i++) {
    float x = map(temp[i].time, first + offset, first + timespan + offset, 0, width);
    float yl = map(temp[i].low,  -10, 30, height * 2 / 8, 0);
    float ya = map(temp[i].avg,  -10, 30, height * 2 / 8, 0);
    float yh = map(temp[i].high, -10, 30, height * 2 / 8, 0);
    fill(120, 120, 120, 80);
    rect(x - 5, yl, 10, yh - yl);
    fill(0, 0, 120);
    ellipse(x, yl, 10, 10);
    fill(0, 90, 0);
    ellipse(x, ya, 10, 10);
    fill(180, 90, 00);
    ellipse(x, yh, 10, 10);
  }
  popStyle();
}

public void draw() {
  noLoop();
  background(240);
  fill(0);
  text("Cycle: " + cycle, 10, height / 16);
  yearundrawn = true;
  for (int i = 0; i < count; i++) {
    if(log[i].begin + log[i].duration >= first + offset &&
       log[i].begin <= first + timespan + offset) {
      drawtimeperiod(log[i].begin, log[i].duration);
      drawdate(log[i]);
    }
  }
  drawmoon(newmoon, 0);
  drawmoon(firstq, 1);
  drawmoon(fullmoon, 2);
  drawmoon(lastq, 3);
  if(showgrid) drawaxis();
  if(showtemp) drawtemp();
}  

public void keyTyped() {
  switch(key) {
  case '_': case '-': timespan *= 2; break;
  case '+': case '=': timespan /= 2; break;
  case ',': offset -= 1; break;
  case '.': offset += 1; break;
  case '<': offset -= 7; break;
  case '>': offset += 7; break;
  case ']': cycle += 0.1f; break;
  case '[': cycle -= 0.1f; break;
  case '}': cycle += 0.01f; break;
  case '{': cycle -= 0.01f; break;
  case 'R': case 'r': cycle = 24; offset = 0; break;
  case 'G': case 'g': showgrid = !showgrid; break;
  case 'T': case 't': showtemp = !showtemp; break;
  case '1': gridstep *= 2; break;
  case '2': gridstep /= 2; break;  
  }
  redraw();
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sleep_cycle_2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
