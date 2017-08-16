package com.tc.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdses.tool.UtilTc;
import com.tc.application.R;
import com.tc.view.widget.OnWheelChangedListener;
import com.tc.view.widget.OnWheelScrollListener;
import com.tc.view.widget.WheelView;
import com.tc.view.widget.adapters.AbstractWheelTextAdapter;

import java.util.ArrayList;
import java.util.Calendar;


public class DateWheelDialogN extends Dialog implements View.OnClickListener {
    //控件
    private WheelView mYearWheelView;
    private WheelView mDateWheelView;
    private WheelView mHourWheelView;
    private WheelView mMinuteWheelView;
    private CalendarTextAdapter mDateAdapter;
    private CalendarTextAdapter mHourAdapter;
    private CalendarTextAdapter mMinuteAdapter;
    private CalendarTextAdapter mYearAdapter;
    private TextView mTitleTextView;
    private Button mSureButton;
    private Dialog mDialog;
    private Button mCloseDialog;
    private LinearLayout mLongTermLayout;
    private TextView mLongTermTextView;

    //变量
    private ArrayList<String> arry_date = new ArrayList<String>();
    private ArrayList<String> arry_hour = new ArrayList<String>();
    private ArrayList<String> arry_minute = new ArrayList<String>();
    private ArrayList<String> arry_year = new ArrayList<String>();

    private int nowDateId = 0;
    private int nowHourId = 0;
    private int nowMinuteId = 0;
    private int nowYearId = 0;
    private String mYearStr;
    private String mDateStr;
    private String mHourStr;
    private String mMinuteStr;
    private boolean mBlnBeLongTerm = false;
    private boolean mBlnTimePickerGone = false;


    private final int MAX_TEXT_SIZE = 18;
    private final int MIN_TEXT_SIZE = 16;

    private Context mContext;
    private DateChooseInterface dateChooseInterface;

    public DateWheelDialogN(Context context, DateChooseInterface dateChooseInterface) {
        super(context);
        this.mContext = context;
        this.dateChooseInterface = dateChooseInterface;
        mDialog = new Dialog(context, R.style.dialog);
        initView();
        initData();
    }


    private void initData() {
        initYear();
        initDate();
         initHour();
          initMinute();
        initListener();
    }

    private void initListener() {
        mYearWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mYearAdapter);
                mYearStr = arry_year.get(wheel.getCurrentItem()) + "";
            }
        });

        mYearWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mYearAdapter);
            }
        });

        //日期
        mDateWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mDateAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mDateAdapter);
//                mDateCalendarTextView.setText(" " + arry_date.get(wheel.getCurrentItem()));
                mDateStr = arry_date.get(wheel.getCurrentItem());
            }
        });

        mDateWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mDateAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mDateAdapter);
            }
        });

        //小时***********************************
       /* mHourWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mHourAdapter);
                mHourStr = arry_hour.get(wheel.getCurrentItem()) + "";
            }
        });

        mHourWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mHourAdapter);
            }
        });*/

        //分钟
/*        mMinuteWheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMinuteAdapter);
                mMinuteStr = arry_minute.get(wheel.getCurrentItem()) + "";
            }
        });

        mMinuteWheelView.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMinuteAdapter);
            }
        });*/
    }

    private void initMinute() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowMinite = nowCalendar.get(Calendar.MINUTE);
        arry_minute.clear();
        for (int i = 0; i <= 59; i++) {
            arry_minute.add(i + "");
            if (nowMinite == i) {
                nowMinuteId = arry_minute.size() - 1;
            }
        }
        mMinuteAdapter = new CalendarTextAdapter(mContext, arry_minute, nowMinuteId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mMinuteWheelView.setVisibleItems(5);
        mMinuteWheelView.setViewAdapter(mMinuteAdapter);
        mMinuteWheelView.setCurrentItem(nowMinuteId);
        mMinuteStr = arry_minute.get(nowMinuteId) + "";
        setTextViewStyle(mMinuteStr, mMinuteAdapter);

    }

    private void initHour() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        arry_hour.clear();
        for (int i = 0; i <= 23; i++) {
            arry_hour.add(i + "");
            if (nowHour == i) {
                nowHourId = arry_hour.size() - 1;
            }
        }

        mHourAdapter = new CalendarTextAdapter(mContext, arry_hour, nowHourId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mHourWheelView.setVisibleItems(5);
        mHourWheelView.setViewAdapter(mHourAdapter);
        mHourWheelView.setCurrentItem(nowHourId);
        mHourStr = arry_hour.get(nowHourId) + "";
        setTextViewStyle(mHourStr, mHourAdapter);
    }

    private void initYear() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowYear = nowCalendar.get(Calendar.YEAR);
        arry_year.clear();
        for (int i = 0; i <= 99+120; i++) {
            int year = nowYear - 150 + i;
            arry_year.add(year + "年");
            if (nowYear == year) {
                nowYearId = arry_year.size() - 1;
            }
        }
        mYearAdapter = new CalendarTextAdapter(mContext, arry_year, nowYearId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mYearWheelView.setVisibleItems(5);
        mYearWheelView.setViewAdapter(mYearAdapter);
        mYearWheelView.setCurrentItem(nowYearId);
        mYearStr = arry_year.get(nowYearId);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_choose, null);
        mDialog.setContentView(view);
        mYearWheelView = (WheelView) view.findViewById(R.id.year_wv);
        mDateWheelView = (WheelView) view.findViewById(R.id.date_wv);
        mHourWheelView = (WheelView) view.findViewById(R.id.hour_wv);
        mMinuteWheelView = (WheelView) view.findViewById(R.id.minute_wv);
        mTitleTextView = (TextView) view.findViewById(R.id.title_tv);
        mSureButton = (Button) view.findViewById(R.id.sure_btn);
        mCloseDialog = (Button) view.findViewById(R.id.date_choose_close_btn);
        mLongTermLayout = (LinearLayout) view.findViewById(R.id.long_term_layout);
        mLongTermTextView = (TextView) view.findViewById(R.id.long_term_tv);

        mSureButton.setOnClickListener(this);
        mCloseDialog.setOnClickListener(this);
        mLongTermTextView.setOnClickListener(this);
    }

    private void initDate() {
        Calendar nowCalendar = Calendar.getInstance();
        int nowYear = nowCalendar.get(Calendar.YEAR);
        arry_date.clear();
        setDate(nowYear);
        mDateAdapter = new CalendarTextAdapter(mContext, arry_date, nowDateId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        mDateWheelView.setVisibleItems(5);
        mDateWheelView.setViewAdapter(mDateAdapter);
        mDateWheelView.setCurrentItem(nowDateId);

        mDateStr = arry_date.get(nowDateId);
        setTextViewStyle(mDateStr, mDateAdapter);
    }

    public void setDateDialogTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setTimePickerGone(boolean isGone) {
        mBlnTimePickerGone = isGone;
        if (isGone) {
            LinearLayout.LayoutParams yearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            yearParams.rightMargin = 22;

            LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mYearWheelView.setLayoutParams(yearParams);
            mDateWheelView.setLayoutParams(dateParams);

            mHourWheelView.setVisibility(View.GONE);
            mMinuteWheelView.setVisibility(View.GONE);
        } else {
            mHourWheelView.setVisibility(View.VISIBLE);
            mMinuteWheelView.setVisibility(View.VISIBLE);
        }

    }

    public void showLongTerm(boolean show) {
        if (show) {
            mLongTermLayout.setVisibility(View.VISIBLE);
        } else {
            mLongTermLayout.setVisibility(View.GONE);
        }

    }


    private void setDate(int year) {
        boolean isRun = isRunNian(year);
        Calendar nowCalendar = Calendar.getInstance();
        int nowMonth = nowCalendar.get(Calendar.MONTH) + 1;
        int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
        for (int month = 1; month <= 12; month++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    for (int day = 1; day <= 31; day++) {
                        arry_date.add(month + "月" + day + "日");

                        if (month == nowMonth && day == nowDay) {
                            nowDateId = arry_date.size() - 1;
                        }
                    }
                    break;
                case 2:
                    if (isRun) {
                        for (int day = 1; day <= 29; day++) {
                            arry_date.add(month + "月" + day + "日");
                            if (month == nowMonth && day == nowDay) {
                                nowDateId = arry_date.size() - 1;
                            }
                        }
                    } else {
                        for (int day = 1; day <= 28; day++) {
                            arry_date.add(month + "月" + day + "日");
                            if (month == nowMonth && day == nowDay) {
                                nowDateId = arry_date.size() - 1;
                            }
                        }
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    for (int day = 1; day <= 30; day++) {
                        arry_date.add(month + "月" + day + "日");
                        if (month == nowMonth && day == nowDay) {
                            nowDateId = arry_date.size() - 1;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isRunNian(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setTextViewStyle(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(MAX_TEXT_SIZE);
                textvew.setTextColor(mContext.getResources().getColor(R.color.text_10));
            } else {
                textvew.setTextSize(MIN_TEXT_SIZE);
                textvew.setTextColor(mContext.getResources().getColor(R.color.text_11));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn:
                if (mBlnTimePickerGone) {
                    dateChooseInterface.getDateTime(strTimeToDateFormat(mYearStr, mDateStr), mBlnBeLongTerm);

                } else {
                    dateChooseInterface.getDateTime(strTimeToDateFormat(mYearStr, mDateStr , mHourStr , mMinuteStr), mBlnBeLongTerm);
                   // dateChooseInterface.getDateTime(strTimeToDateFormat(mYearStr, mDateStr), mBlnBeLongTerm);
                }
                dismissDialog();
                break;
            case R.id.date_choose_close_btn:
                dismissDialog();
                break;
            case R.id.long_term_tv:
                if (!mBlnBeLongTerm) {
                    mLongTermTextView.setBackgroundResource(R.drawable.gouxuanok);
                    mBlnBeLongTerm = true;
                } else {
                    mLongTermTextView.setBackgroundResource(R.drawable.gouxuanno);
                    mBlnBeLongTerm = false;
                }
            default:
                break;
        }
    }


    private void dismissDialog() {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            return;
        }

        if (null == mDialog || !mDialog.isShowing() || null == mContext
                || ((Activity) mContext).isFinishing()) {

            return;
        }

        mDialog.dismiss();
        this.dismiss();
    }


    public void showDateChooseDialog() {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            return;
        }

        if (null == mContext || ((Activity) mContext).isFinishing()) {


            return;
        }

        if (null != mDialog) {

            mDialog.show();
            return;
        }

        if (null == mDialog) {

            return;
        }

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    private String strTimeToDateFormat(String yearStr, String dateStr, String hourStr, String minuteStr) {

        return yearStr.replace("年", "-") + dateStr.replace("月", "-").replace("日", " ")
                + hourStr + ":" + minuteStr;
    }

    private String strTimeToDateFormat(String yearStr, String dateStr) {
        int month = dateStr.indexOf("月");
        int day = dateStr.indexOf("日");
        String monthR="",dayR="";
        if (month == 1) {
            monthR = "0" + dateStr.substring(0, 1);
            if (day == 3) {
                dayR = "0" + dateStr.substring(2, 3);
            }else{
                dayR=dateStr.substring(2, 4);
            }
        }else if(month==2){
            monthR =dateStr.substring(0, 2);
            if(day==4){
                dayR="0"+dateStr.substring(3,4);
            }else{
                dayR=dateStr.substring(3,5);
            }
        }
        dateStr=monthR+"月"+dayR+"日";

        UtilTc.showLog("month day" + month + ":" + day+"="+dateStr);

        return yearStr.replace("年", "-") + dateStr.replace("月", "-").replace("日", "");
    }


    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, R.id.tempValue, currentItem, maxsize, minsize);
            this.list = list;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            String str = list.get(index) + "";
            return str;
        }
    }

    public interface DateChooseInterface {
        void getDateTime(String time, boolean longTimeChecked);
    }

}
