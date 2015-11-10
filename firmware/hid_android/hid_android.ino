/*
 * HID Android
 * -----------------
 * Fw used to unlock the phone with a Teensy (or any other HID-enabled
 * board) though a button. Useful when working with the phone inserted
 * on a Google Cardboard-like device
 *
 * Author: DEF
 */

void setup() 
{
  //-- Keyboard, mouse, and other HID init
  Keyboard.begin();
  Mouse.begin();
  Mouse.move(-1000,-1000,0);
  pinMode(13, OUTPUT);
  digitalWrite(13, HIGH);
  delay(1000); 
  digitalWrite(13, LOW);
}

void loop() 
{
  //-- Move pointer off-screen (to the origin of coordinates)
  for( int i = 0; i < 100; i++)
  {
    Mouse.move(-20,-20,0);
    delay(20);      
  }
  
  //-- Move pointer to unlock point
  for (int i = 0; i < 100; i++)
  {
    Mouse.move(4,13,0);
    delay(20); 
  }   
  Mouse.press();
  
  //-- Move unlock gizmo to unlock phone
  for (int i = 0; i < 100; i++)
  {
    Mouse.move(6,0,0);
    delay(20); 
  }    
  
  //-- Wait forever
  Mouse.release();
  digitalWrite(13, HIGH);
  while(1) { delay(1000);}
}
