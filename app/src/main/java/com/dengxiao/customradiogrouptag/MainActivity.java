package com.dengxiao.customradiogrouptag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.dengxiao.customradiogrouptag.R.id.customRadioGroup;

public class MainActivity extends AppCompatActivity {

    private String[] str1 = {"亮黑色", "玫瑰金色", "黑色", "金色", "银色"};
    private String[] str2 = {"32G"};
    private String[] str3 = {"001sdfasfa", "003", "00dd2", "0dsfsdf04", "005Jjskdjf"};
    private String text1 = "";
    private String text2 = "";
    private String text3 = "";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);


        CustomRadioGroup customRadioGroup1 = (CustomRadioGroup) findViewById(R.id.customRadioGroup);
        setSpacing(customRadioGroup1,12,8);
        customRadioGroup1.setListener(new CustomRadioGroup.OnclickListener() {
            @Override
            public void OnText(String text) {
                text1 = text;
                mTextView.setText(text1 + text2 + text3);

            }
        });

        CustomRadioGroup customRadioGroup2 = (CustomRadioGroup) findViewById(R.id.customRadioGroup1);
        setSpacing(customRadioGroup2,12,8);
        customRadioGroup2.setListener(new CustomRadioGroup.OnclickListener() {
            @Override
            public void OnText(String text) {
                text2 = text;
                mTextView.setText(text1 + text2 + text3);
            }
        });

        CustomRadioGroup customRadioGroup3 = (CustomRadioGroup) findViewById(R.id.customRadioGroup2);
        setSpacing(customRadioGroup3,12,8);
        customRadioGroup3.setListener(new CustomRadioGroup.OnclickListener() {
            @Override
            public void OnText(String text) {
                text3 = text;
                mTextView.setText(text1 + text2 + text3);
            }
        });

        for (int i = 0; i < str1.length; i++) {
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.radiobutton_addcart, null);
            radioButton.setText(str1[i]);
            customRadioGroup1.addView(radioButton);
        }
        for (int i = 0; i < str2.length; i++) {
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.radiobutton_addcart, null);
            radioButton.setText(str2[i]);
            customRadioGroup2.addView(radioButton);
        }
        for (int i = 0; i < str3.length; i++) {
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.radiobutton_addcart, null);
            radioButton.setText(str3[i]);
            customRadioGroup3.addView(radioButton);
        }
    }
    private void setSpacing(CustomRadioGroup cg,int widthdp,int heightdp){
        cg.setHorizontalSpacing(widthdp);
        cg.setVerticalSpacing(heightdp);

    }


}
