package com.camera.simplemjpeg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView.BufferType;

public class SettingsActivity extends Activity {

    private static final String TAG = "MJPEG";

    Button settings_done;

    Spinner resolution_spinner;
    EditText width_input;
    EditText height_input;

    //-- Left eye UI
    EditText left_eye_address1_input;
    EditText left_eye_address2_input;
    EditText left_eye_address3_input;
    EditText left_eye_address4_input;
    EditText left_eye_port_input;
    EditText left_eye_command_input;

    Button left_eye_address1_increment;
    Button left_eye_address2_increment;
    Button left_eye_address3_increment;
    Button left_eye_address4_increment;

    Button left_eye_address1_decrement;
    Button left_eye_address2_decrement;
    Button left_eye_address3_decrement;
    Button left_eye_address4_decrement;

    RadioGroup left_eye_port_group;
    RadioGroup left_eye_command_group;

    //-- Right eye UI
    EditText right_eye_address1_input;
    EditText right_eye_address2_input;
    EditText right_eye_address3_input;
    EditText right_eye_address4_input;
    EditText right_eye_port_input;
    EditText right_eye_command_input;

    Button right_eye_address1_increment;
    Button right_eye_address2_increment;
    Button right_eye_address3_increment;
    Button right_eye_address4_increment;

    Button right_eye_address1_decrement;
    Button right_eye_address2_decrement;
    Button right_eye_address3_decrement;
    Button right_eye_address4_decrement;

    RadioGroup right_eye_port_group;
    RadioGroup right_eye_command_group;

    int width = 640;
    int height = 480;

    int left_eye_ip_ad1 = 192;
    int left_eye_ip_ad2 = 168;
    int left_eye_ip_ad3 = 2;
    int left_eye_ip_ad4 = 1;
    int left_eye_ip_port = 80;
    String left_eye_ip_command = "?action=stream";

    int right_eye_ip_ad1 = 192;
    int right_eye_ip_ad2 = 168;
    int right_eye_ip_ad3 = 2;
    int right_eye_ip_ad4 = 1;
    int right_eye_ip_port = 80;
    String right_eye_ip_command = "?action=stream";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Bundle extras = getIntent().getExtras();

        //-- UI Setup
        //------------------------------------------------------------------------------
        //-- Common elements
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.resolution_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        resolution_spinner = (Spinner) findViewById(R.id.resolution_spinner);
        resolution_spinner.setAdapter(adapter);

        width_input = (EditText) findViewById(R.id.width_input);
        height_input = (EditText) findViewById(R.id.height_input);

        //-- Left eye stream elements
        left_eye_address1_input = (EditText) findViewById(R.id.address1_input);
        left_eye_address2_input = (EditText) findViewById(R.id.address2_input);
        left_eye_address3_input = (EditText) findViewById(R.id.address3_input);
        left_eye_address4_input = (EditText) findViewById(R.id.address4_input);
        left_eye_port_input = (EditText) findViewById(R.id.port_input);
        left_eye_command_input = (EditText) findViewById(R.id.command_input);

        left_eye_port_group = (RadioGroup) findViewById(R.id.port_radiogroup);
        left_eye_command_group = (RadioGroup) findViewById(R.id.command_radiogroup);

        //-- Right eye stream elements
        right_eye_address1_input = (EditText) findViewById(R.id.right_eye_address1_input);
        right_eye_address2_input = (EditText) findViewById(R.id.right_eye_address2_input);
        right_eye_address3_input = (EditText) findViewById(R.id.right_eye_address3_input);
        right_eye_address4_input = (EditText) findViewById(R.id.right_eye_address4_input);
        right_eye_port_input = (EditText) findViewById(R.id.right_eye_port_input);
        right_eye_command_input = (EditText) findViewById(R.id.right_eye_command_input);

        right_eye_port_group = (RadioGroup) findViewById(R.id.right_eye_port_radiogroup);
        right_eye_command_group = (RadioGroup) findViewById(R.id.right_eye_command_radiogroup);

        //-- Set up tabs
        final TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        if (tabHost != null) {
            tabHost.setup();

            TabHost.TabSpec spec1 = tabHost.newTabSpec("Left Eye");
            spec1.setContent(R.id.tab1);
            spec1.setIndicator("Left Eye", null);

            TabHost.TabSpec spec2 = tabHost.newTabSpec("Right Eye");
            spec2.setContent(R.id.tab2);
            spec2.setIndicator("Right Eye", null);

            tabHost.addTab(spec1);
            tabHost.addTab(spec2);
        } else {
            Log.e(TAG, "TabHost not loaded");
        }

        if (extras != null) {
            //-- Configure common
            width = extras.getInt("width", width);
            height = extras.getInt("height", height);

            width_input.setText(String.valueOf(width));
            height_input.setText(String.valueOf(height));
            resolution_spinner.setSelection(adapter.getCount() - 1);

            //-- Configure left eye
            left_eye_ip_ad1 = extras.getInt("left_eye_ip_ad1", left_eye_ip_ad1);
            left_eye_ip_ad2 = extras.getInt("left_eye_ip_ad2", left_eye_ip_ad2);
            left_eye_ip_ad3 = extras.getInt("left_eye_ip_ad3", left_eye_ip_ad3);
            left_eye_ip_ad4 = extras.getInt("left_eye_ip_ad4", left_eye_ip_ad4);
            left_eye_ip_port = extras.getInt("left_eye_ip_port", left_eye_ip_port);
            left_eye_ip_command = extras.getString("left_eye_ip_command");

            left_eye_address1_input.setText(String.valueOf(left_eye_ip_ad1));
            left_eye_address2_input.setText(String.valueOf(left_eye_ip_ad2));
            left_eye_address3_input.setText(String.valueOf(left_eye_ip_ad3));
            left_eye_address4_input.setText(String.valueOf(left_eye_ip_ad4));
            left_eye_port_input.setText(String.valueOf(left_eye_ip_port));
            left_eye_command_input.setText(left_eye_ip_command);

            //-- Configure right eye
            right_eye_ip_ad1 = extras.getInt("right_eye_ip_ad1", right_eye_ip_ad1);
            right_eye_ip_ad2 = extras.getInt("right_eye_ip_ad2", right_eye_ip_ad2);
            right_eye_ip_ad3 = extras.getInt("right_eye_ip_ad3", right_eye_ip_ad3);
            right_eye_ip_ad4 = extras.getInt("right_eye_ip_ad4", right_eye_ip_ad4);
            right_eye_ip_port = extras.getInt("right_eye_ip_port", right_eye_ip_port);
            right_eye_ip_command = extras.getString("right_eye_ip_command");

            right_eye_address1_input.setText(String.valueOf(right_eye_ip_ad1));
            right_eye_address2_input.setText(String.valueOf(right_eye_ip_ad2));
            right_eye_address3_input.setText(String.valueOf(right_eye_ip_ad3));
            right_eye_address4_input.setText(String.valueOf(right_eye_ip_ad4));
            right_eye_port_input.setText(String.valueOf(right_eye_ip_port));
            right_eye_command_input.setText(right_eye_ip_command);
        }

        resolution_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View viw, int arg2, long arg3) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                if (item.equals("640x480")) {
                    width = 640;
                    height = 480;
                } else if (item.equals("480x640")) {
                    width = 480;
                    height = 640;
                } else if (item.equals("320x240")) {
                    width = 320;
                    height = 240;
                } else if (item.equals("240x320")) {
                    width = 240;
                    height = 320;
                } else if (item.equals("176x144")) {
                    width = 176;
                    height = 144;
                } else if (item.equals("144x176")) {
                    width = 144;
                    height = 176;
                }
                width_input.setText(String.valueOf(width));
                height_input.setText(String.valueOf(height));
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //-- Left eye GUI configuration
        left_eye_address1_increment = (Button) findViewById(R.id.address1_increment);
        left_eye_address1_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address1_input.getText().toString();
                        int val = left_eye_ip_ad1;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        left_eye_ip_ad1 = val;
                        left_eye_address1_input.setText(String.valueOf(left_eye_ip_ad1), BufferType.NORMAL);

                    }
                }
        );
        left_eye_address2_increment = (Button) findViewById(R.id.address2_increment);
        left_eye_address2_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address2_input.getText().toString();
                        int val = left_eye_ip_ad2;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        left_eye_ip_ad2 = val;
                        left_eye_address2_input.setText(String.valueOf(left_eye_ip_ad2), BufferType.NORMAL);

                    }
                }
        );
        left_eye_address3_increment = (Button) findViewById(R.id.address3_increment);
        left_eye_address3_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address3_input.getText().toString();
                        int val = left_eye_ip_ad3;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        left_eye_ip_ad3 = val;
                        left_eye_address3_input.setText(String.valueOf(left_eye_ip_ad3), BufferType.NORMAL);

                    }
                }
        );
        left_eye_address4_increment = (Button) findViewById(R.id.address4_increment);
        left_eye_address4_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address4_input.getText().toString();
                        int val = left_eye_ip_ad4;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        left_eye_ip_ad4 = val;
                        left_eye_address4_input.setText(String.valueOf(left_eye_ip_ad4), BufferType.NORMAL);

                    }
                }
        );

        left_eye_address1_decrement = (Button) findViewById(R.id.address1_decrement);
        left_eye_address1_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address1_input.getText().toString();
                        int val = left_eye_ip_ad1;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        left_eye_ip_ad1 = val;
                        left_eye_address1_input.setText(String.valueOf(left_eye_ip_ad1), BufferType.NORMAL);

                    }
                }
        );

        left_eye_address2_decrement = (Button) findViewById(R.id.address2_decrement);
        left_eye_address2_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address2_input.getText().toString();
                        int val = left_eye_ip_ad2;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        left_eye_ip_ad2 = val;
                        left_eye_address2_input.setText(String.valueOf(left_eye_ip_ad2), BufferType.NORMAL);

                    }
                }
        );
        left_eye_address3_decrement = (Button) findViewById(R.id.address3_decrement);
        left_eye_address3_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address3_input.getText().toString();
                        int val = left_eye_ip_ad3;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        left_eye_ip_ad3 = val;
                        left_eye_address3_input.setText(String.valueOf(left_eye_ip_ad3), BufferType.NORMAL);

                    }
                }
        );
        left_eye_address4_decrement = (Button) findViewById(R.id.address4_decrement);
        left_eye_address4_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = left_eye_address4_input.getText().toString();
                        int val = left_eye_ip_ad4;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        left_eye_ip_ad4 = val;
                        left_eye_address4_input.setText(String.valueOf(left_eye_ip_ad4), BufferType.NORMAL);

                    }
                }
        );

        left_eye_port_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.port_80) {
                    left_eye_port_input.setText(getString(R.string.port_80));
                } else if (checkedId == R.id.port_8080) {
                    left_eye_port_input.setText(getString(R.string.port_8080));
                }
            }
        });

        left_eye_command_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.command_streaming) {
                    left_eye_command_input.setText(getString(R.string.command_streaming));
                } else if (checkedId == R.id.command_videofeed) {
                    left_eye_command_input.setText(getString(R.string.command_videofeed));
                }
            }
        });

        //-- Ip address for right eye (GUI configuration)
        right_eye_address1_increment = (Button) findViewById(R.id.right_eye_address1_increment);
        right_eye_address1_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address1_input.getText().toString();
                        int val = right_eye_ip_ad1;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        right_eye_ip_ad1 = val;
                        right_eye_address1_input.setText(String.valueOf(right_eye_ip_ad1), BufferType.NORMAL);

                    }
                }
        );
        right_eye_address2_increment = (Button) findViewById(R.id.right_eye_address2_increment);
        right_eye_address2_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address2_input.getText().toString();
                        int val = right_eye_ip_ad2;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        right_eye_ip_ad2 = val;
                        right_eye_address2_input.setText(String.valueOf(right_eye_ip_ad2), BufferType.NORMAL);

                    }
                }
        );
        right_eye_address3_increment = (Button) findViewById(R.id.right_eye_address3_increment);
        right_eye_address3_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address3_input.getText().toString();
                        int val = right_eye_ip_ad3;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        right_eye_ip_ad3 = val;
                        right_eye_address3_input.setText(String.valueOf(right_eye_ip_ad3), BufferType.NORMAL);

                    }
                }
        );
        right_eye_address4_increment = (Button) findViewById(R.id.right_eye_address4_increment);
        right_eye_address4_increment.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address4_input.getText().toString();
                        int val = right_eye_ip_ad4;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val >= 0 && val < 255) {
                            val += 1;
                        } else if (val < 0) {
                            val = 0;
                        } else if (val >= 255) {
                            val = 255;
                        }

                        right_eye_ip_ad4 = val;
                        right_eye_address4_input.setText(String.valueOf(right_eye_ip_ad4), BufferType.NORMAL);

                    }
                }
        );

        right_eye_address1_decrement = (Button) findViewById(R.id.right_eye_address1_decrement);
        right_eye_address1_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address1_input.getText().toString();
                        int val = right_eye_ip_ad1;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        right_eye_ip_ad1 = val;
                        right_eye_address1_input.setText(String.valueOf(right_eye_ip_ad1), BufferType.NORMAL);

                    }
                }
        );

        right_eye_address2_decrement = (Button) findViewById(R.id.right_eye_address2_decrement);
        right_eye_address2_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address2_input.getText().toString();
                        int val = right_eye_ip_ad2;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        right_eye_ip_ad2 = val;
                        right_eye_address2_input.setText(String.valueOf(right_eye_ip_ad2), BufferType.NORMAL);

                    }
                }
        );
        right_eye_address3_decrement = (Button) findViewById(R.id.right_eye_address3_decrement);
        right_eye_address3_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address3_input.getText().toString();
                        int val = right_eye_ip_ad3;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        right_eye_ip_ad3 = val;
                        right_eye_address3_input.setText(String.valueOf(right_eye_ip_ad3), BufferType.NORMAL);

                    }
                }
        );
        right_eye_address4_decrement = (Button) findViewById(R.id.right_eye_address4_decrement);
        right_eye_address4_decrement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String s = right_eye_address4_input.getText().toString();
                        int val = right_eye_ip_ad4;
                        if (!"".equals(s)) {
                            val = Integer.parseInt(s);
                        }
                        if (val > 0 && val <= 255) {
                            val -= 1;
                        } else if (val <= 0) {
                            val = 0;
                        } else if (val > 255) {
                            val = 255;
                        }

                        right_eye_ip_ad4 = val;
                        right_eye_address4_input.setText(String.valueOf(right_eye_ip_ad4), BufferType.NORMAL);

                    }
                }
        );

        right_eye_port_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.right_eye_port_80) {
                    right_eye_port_input.setText(getString(R.string.port_80));
                } else if (checkedId == R.id.right_eye_port_8080) {
                    right_eye_port_input.setText(getString(R.string.port_8080));
                }
            }
        });

        right_eye_command_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.right_eye_command_streaming) {
                    right_eye_command_input.setText(getString(R.string.command_streaming));
                } else if (checkedId == R.id.right_eye_command_videofeed) {
                    right_eye_command_input.setText(getString(R.string.command_videofeed));
                }
            }
        });

        //-- Settings done button configuration
        settings_done = (Button) findViewById(R.id.settings_done);
        settings_done.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        String s;

                        //-- Common
                        s = width_input.getText().toString();
                        if (!"".equals(s)) {
                            width = Integer.parseInt(s);
                        }
                        s = height_input.getText().toString();
                        if (!"".equals(s)) {
                            height = Integer.parseInt(s);
                        }

                        //-- Left eye
                        s = left_eye_address1_input.getText().toString();
                        if (!"".equals(s)) {
                            left_eye_ip_ad1 = Integer.parseInt(s);
                        }
                        s = left_eye_address2_input.getText().toString();
                        if (!"".equals(s)) {
                            left_eye_ip_ad2 = Integer.parseInt(s);
                        }
                        s = left_eye_address3_input.getText().toString();
                        if (!"".equals(s)) {
                            left_eye_ip_ad3 = Integer.parseInt(s);
                        }
                        s = left_eye_address4_input.getText().toString();
                        if (!"".equals(s)) {
                            left_eye_ip_ad4 = Integer.parseInt(s);
                        }

                        s = left_eye_port_input.getText().toString();
                        if (!"".equals(s)) {
                            left_eye_ip_port = Integer.parseInt(s);
                        }

                        s = left_eye_command_input.getText().toString();
                        left_eye_ip_command = s;

                        //-- Right eye
                        s = right_eye_address1_input.getText().toString();
                        if (!"".equals(s)) {
                            right_eye_ip_ad1 = Integer.parseInt(s);
                        }
                        s = right_eye_address2_input.getText().toString();
                        if (!"".equals(s)) {
                            right_eye_ip_ad2 = Integer.parseInt(s);
                        }
                        s = right_eye_address3_input.getText().toString();
                        if (!"".equals(s)) {
                            right_eye_ip_ad3 = Integer.parseInt(s);
                        }
                        s = right_eye_address4_input.getText().toString();
                        if (!"".equals(s)) {
                            right_eye_ip_ad4 = Integer.parseInt(s);
                        }

                        s = right_eye_port_input.getText().toString();
                        if (!"".equals(s)) {
                            right_eye_ip_port = Integer.parseInt(s);
                        }

                        s = right_eye_command_input.getText().toString();
                        right_eye_ip_command = s;

                        Intent intent = new Intent();
                        //-- Common
                        intent.putExtra("width", width);
                        intent.putExtra("height", height);
                        //-- Left eye
                        intent.putExtra("left_eye_ip_ad1", left_eye_ip_ad1);
                        intent.putExtra("left_eye_ip_ad2", left_eye_ip_ad2);
                        intent.putExtra("left_eye_ip_ad3", left_eye_ip_ad3);
                        intent.putExtra("left_eye_ip_ad4", left_eye_ip_ad4);
                        intent.putExtra("left_eye_ip_port", left_eye_ip_port);
                        intent.putExtra("left_eye_ip_command", left_eye_ip_command);
                        //-- Right eye
                        intent.putExtra("right_eye_ip_ad1", right_eye_ip_ad1);
                        intent.putExtra("right_eye_ip_ad2", right_eye_ip_ad2);
                        intent.putExtra("right_eye_ip_ad3", right_eye_ip_ad3);
                        intent.putExtra("right_eye_ip_ad4", right_eye_ip_ad4);
                        intent.putExtra("right_eye_ip_port", right_eye_ip_port);
                        intent.putExtra("right_eye_ip_command", right_eye_ip_command);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );
    }
}
