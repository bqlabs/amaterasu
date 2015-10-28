/*
 * HID Android
 * -----------------
 * Fw used to unlock the phone with a Teensy (or any other HID-enabled
 * board) though a button. Useful when working with the phone inserted
 * on a Google Cardboard-like device
 *
 * Author: DEF
 */
 
 // #define DEBUG_MODE
 
 //-- Includes
 #include <Bounce.h>
 
 //- -Define screen things
 #define MOUSE_MAX_INC_X 60
 #define MOUSE_MAX_INC_Y 60
 
 #define MOUSE_MIN_INC_X 4
 #define MOUSE_MIN_INC_Y 4
 
 //-- Define hardware pins
 #define X_AXIS_PIN A0
 #define Y_AXIS_PIN A1
 #define USER_BUTTON_PIN A2
 #define LED_PIN 13
 

void setup() 
{
  //- Setup hardware pins
  pinMode(LED_PIN, OUTPUT);
  pinMode(USER_BUTTON_PIN, INPUT_PULLUP);
  
  //-- Keyboard, mouse, and other HID init
  Keyboard.begin();
  Mouse.begin();
  #ifdef DEBUG_MODE
    Serial.begin(9600);
  #endif
  
  //-- Blink LED to indicate ON
  digitalWrite(13, HIGH);
  delay(200); 
  digitalWrite(13, LOW);
  delay(200); 
  digitalWrite(13, HIGH);
  delay(200);
  digitalWrite(13, LOW);
}

void loop() 
{
  //-- Static variables
  static Bounce user_button = Bounce(USER_BUTTON_PIN, 10);
  
  //-- Deal with clicks
  user_button.update();
  if (user_button.fallingEdge())
  {
    Mouse.press();
    digitalWrite(13, HIGH);
  }
  else if (user_button.risingEdge())
  {
    Mouse.release();
    digitalWrite(13, LOW);
  }
  
  //-- Mouse movement
  int8_t amount_to_move_x = 0;
  int8_t amount_to_move_y = 0;
  
  int analog_joystick_x = analogRead(X_AXIS_PIN);
  int analog_joystick_y = analogRead(Y_AXIS_PIN);
  
  amount_to_move_x = map(analog_joystick_x, 0, 1023, -MOUSE_MAX_INC_X, MOUSE_MAX_INC_X);
  amount_to_move_y = map(analog_joystick_y, 0, 1023, -MOUSE_MAX_INC_Y, MOUSE_MAX_INC_Y);
  
  //-- Avoid drifting due to potentiometer
  amount_to_move_x = abs(amount_to_move_x) > MOUSE_MIN_INC_X ? amount_to_move_x : 0;
  amount_to_move_y = abs(amount_to_move_y) > MOUSE_MIN_INC_Y ? amount_to_move_y : 0;

  #ifdef DEBUG_MODE
    Serial.print("X:");
    Serial.print(amount_to_move_x);
    Serial.print(" Y:");
    Serial.println(amount_to_move_y);
  #endif
  
  Mouse.move(amount_to_move_x, -amount_to_move_y, 0);
  delay(25);
  
  //-- Move unlock gizmo to unlock phone
//  for (int i = 0; i < 100; i++)
//  {
//    Mouse.move(6,0,0);
//    delay(20); 
//  }    
  
}
