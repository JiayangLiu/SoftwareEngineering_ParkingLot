package com.nku.cs.parkinglot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nku.cs.parkinglot.widget.datechoosewheelviewdialog.DateChooseWheelViewDialog;

public class BookLongtermParkActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_BookLongtermStartTime;
    private Button btn_BookLongtermEndTime;
    private Button btn_BookLongtermParkingLot;
    private Button btn_BookLongtermNextStep;
    private TextView txv_BookLongtermStartTime;
    private TextView txv_BookLongtermEndTime;
    private TextView txv_BookLongtermParkingLot;
    private TextView txv_BookLongtermUnoccupiedFeedback;
    private EditText edt_BookLongtermDayNumber;

    private Bundle bundlePark;

    private boolean startTimeSelected = false;
    private boolean endTimeSelected = false;
    private boolean dayNumberFilled = false;
    private boolean parkingLotSelected = false;
    private boolean available = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_longterm_park);

        btn_BookLongtermStartTime = (Button) this.findViewById(R.id.btn_BookLongtermStartTime);
        btn_BookLongtermEndTime = (Button) this.findViewById(R.id.btn_BookLongtermEndTime);
        btn_BookLongtermParkingLot = (Button) this.findViewById(R.id.btn_BookLongtermParkingLot);
        btn_BookLongtermNextStep = (Button) this.findViewById(R.id.btn_BookLongtermNextStep);
        txv_BookLongtermStartTime = (TextView) this.findViewById(R.id.txv_BookLongtermStartTime);
        txv_BookLongtermEndTime = (TextView) this.findViewById(R.id.txv_BookLongtermEndTime);
        txv_BookLongtermParkingLot = (TextView) this.findViewById(R.id.txv_BookLongtermParkingLot);
        txv_BookLongtermUnoccupiedFeedback = (TextView) this.findViewById(R.id.txv_BookLongtermUnoccupiedFeedback);
        edt_BookLongtermDayNumber = (EditText) this.findViewById(R.id.edt_BookLongtermDayNumber);

        btn_BookLongtermStartTime.setOnClickListener(this);
        btn_BookLongtermEndTime.setOnClickListener(this);
        btn_BookLongtermParkingLot.setOnClickListener(this);
        btn_BookLongtermNextStep.setOnClickListener(this);

        bundlePark = new Bundle();

    }
    @Override
    public void onClick(View v) {
        if (! edt_BookLongtermDayNumber.getText().toString().equals("")) {
            dayNumberFilled = true;
        } else {
            dayNumberFilled = false;
        }
        switch (v.getId()) {
            case R.id.btn_BookLongtermStartTime:    // 开始时间
                DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(BookLongtermParkActivity.this, new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        bundlePark.putString("startTime", time);
                        txv_BookLongtermStartTime.setText(time);
                    }
                });
                startDateChooseDialog.setYearMonthDayPickerGone(true);
                startDateChooseDialog.setDateDialogTitle("开始时间");
                startDateChooseDialog.showDateChooseDialog();
                startTimeSelected = true;

                if (startTimeSelected && endTimeSelected && dayNumberFilled && parkingLotSelected)
                    checkUnoccpupied();
                break;
            case R.id.btn_BookLongtermEndTime:  // 结束时间
                DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(BookLongtermParkActivity.this, new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        bundlePark.putString("endTime", time);
                        txv_BookLongtermEndTime.setText(time);
                    }
                });
                endDateChooseDialog.setYearMonthDayPickerGone(true);
                endDateChooseDialog.setDateDialogTitle("结束时间");
                endDateChooseDialog.showDateChooseDialog();
                endTimeSelected = true;
                if (startTimeSelected && endTimeSelected && dayNumberFilled && parkingLotSelected)
                    checkUnoccpupied();
                break;
            case R.id.btn_BookLongtermParkingLot:  // 停车场
                showParkingLotDialog();
                parkingLotSelected = true;
                if (startTimeSelected && endTimeSelected && parkingLotSelected)
                    checkUnoccpupied();
                break;
            case R.id.btn_BookLongtermNextStep:  // 下一步
                if (available) {
                    bundlePark.putString("DayNumber", edt_BookLongtermDayNumber.getText().toString());
                    Intent intent = new Intent(this, BookLongtermUserActivity.class);
                    intent.putExtras(bundlePark);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "请选择合适时段和车场", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout toastView = (LinearLayout) toast.getView();
                    ImageView imageCodeProject = new ImageView(getApplicationContext());
                    imageCodeProject.setImageResource(R.drawable.circle_close);
                    toastView.addView(imageCodeProject, 0);
                    toast.show();
                }
                break;
            default:
                break;
        }
    }

    // "选择停车场"对话框
    private void showParkingLotDialog() {
        android.support.v7.app.AlertDialog.Builder builderSingle = new android.support.v7.app.AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("选择停车场");

        final android.widget.ArrayAdapter<String> arrayAdapter = new android.widget.ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("刘嘉洋停车场");
        arrayAdapter.add("付曦燕停车场");
        arrayAdapter.add("王静远停车场");

        builderSingle.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                bundlePark.putString("startTime", arrayAdapter.getItem(which));
                txv_BookLongtermParkingLot.setText(arrayAdapter.getItem(which));
            }
        });
        builderSingle.show();
    }

    // 查看车场时段的空余情况
    private void checkUnoccpupied() {
        // 查看用户当前选择下的空余情况
        // ... = getUnoccupiedInfo(...);
        txv_BookLongtermUnoccupiedFeedback.setText("结果反馈");
        available = true;
    }
}
