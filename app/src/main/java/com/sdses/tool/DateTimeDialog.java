package com.sdses.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.tc.application.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期 选择 �?
 * Created by Administrator on 2016/8/19.
 */
public class DateTimeDialog extends AlertDialog implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, TimePicker.OnTimeChangedListener, DatePicker.OnDateChangedListener, View.OnClickListener {


    private Button cancleButton, okButton;
    private RadioGroup radioGroup;
    private RadioButton dateButton, timeButton;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private ViewPager viewPager;
    private View datePickerView;
    private View timePickerView;
    private Date date;
    private Calendar calendar;
    // 自定�? 监听�?
    private MyOnDateSetListener myOnDateSetListener;
    // 格式�? 工具
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy��MM��dd��");
    private SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HHʱmm��");

    /**
     * @param context
     * @param myOnDateSetListener 监听�?
     */
    public DateTimeDialog(Context context, MyOnDateSetListener myOnDateSetListener) {
        this(context, null, myOnDateSetListener);
    }

    /**
     * @param context
     * @param date                默认 显示 �? 时间
     * @param myOnDateSetListener 监听�?
     */
    public DateTimeDialog(Context context, Date date, MyOnDateSetListener myOnDateSetListener) {
        super(context);
        this.date = date;
        this.myOnDateSetListener = myOnDateSetListener;
        init();
    }

    private void init() {

        // 用于 控制 timePicker�? 显示 样式
        Context contextThemeWrapper = new ContextThemeWrapper(
                getContext(), android.R.style.Theme_Holo_Light);
        LayoutInflater localInflater = LayoutInflater.from(getContext()).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.view_date_time_picker_dialog, null);


        setView(view);

        timePicker = new TimePicker(getContext());
        timePicker.setIs24HourView(true);

        viewPager = (ViewPager) view.findViewById(R.id.contentViewPager);
        okButton = (Button) view.findViewById(R.id.okButton);
        cancleButton = (Button) view.findViewById(R.id.cancelButton);
        radioGroup = (RadioGroup) view.findViewById(R.id.titleGroup);
        dateButton = (RadioButton) view.findViewById(R.id.dateButton);
        timeButton = (RadioButton) view.findViewById(R.id.timeButton);

        datePickerView = localInflater.inflate(R.layout.view_date_picker_layout, null);
        datePicker = (DatePicker) datePickerView.findViewById(R.id.datePicker);

        timePickerView = localInflater.inflate(R.layout.view_time_picker_layout, null);
        timePicker = (TimePicker) timePickerView.findViewById(R.id.timePicker);


        // 初始�? 状�??
        if (date == null) {
            calendar = Calendar.getInstance();
            date = calendar.getTime();
        } else {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }


        dateButton.setText(simpleDateFormat.format(date));
        timeButton.setText(simpleTimeFormat.format(date));
        timePicker.setIs24HourView(true);


        // 设置 显示 宽高
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        timePicker.measure(width, height);
        datePicker.measure(width, height);
        int viewPagerHeight;
        if (datePicker.getMeasuredHeight() > timePicker.getMeasuredHeight()) {
            viewPagerHeight = datePicker.getMeasuredHeight();
        } else {
            viewPagerHeight = timePicker.getMeasuredHeight();
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, viewPagerHeight);
        params.addRule(RelativeLayout.BELOW, radioGroup.getId());
        viewPager.setLayoutParams(params);

        // 设置 viewPager 显示 内容
        ViewPagerAdapter testPage = new ViewPagerAdapter();
        viewPager.setAdapter(testPage);


        /**
         * 设置 监听�?
         */
        radioGroup.setOnCheckedChangeListener(this);
        viewPager.addOnPageChangeListener(this);
        timePicker.setOnTimeChangedListener(this);
        cancleButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        //初始�? 显示 时间
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));


    }

    /**
     * 隐藏 �? 显示 弹框
     */
    public void hideOrShow() {
        if (this == null) {
            return;
        }
        if (!this.isShowing()) {
            this.show();
        } else {
            this.dismiss();
        }
    }

    public void setMyOnDateSetListener(MyOnDateSetListener myOnDateSetListener) {
        this.myOnDateSetListener = myOnDateSetListener;
    }


    /**
     * 标题 切换 监听
     *
     * @param radioGroup
     * @param i
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == dateButton.getId()) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(1);
        }
    }

    /**
     * ViewPager  滚动 监听
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            radioGroup.check(dateButton.getId());
        } else {
            radioGroup.check(timeButton.getId());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 小时 选择�? 改变 监听
     *
     * @param timePicker
     * @param i
     * @param i1
     */
    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, i1);
        timeButton.setText(simpleTimeFormat.format(calendar.getTime()));
    }

    /**
     * 日期 选择 �? 改变 监听
     *
     * @param datePicker
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        dateButton.setText(simpleDateFormat.format(calendar.getTime()));
//        Log.i("testss", simpleDateFormat.format(calendar.getTime()));
    }

    /**
     * 确认 取消 按钮 点击 监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelButton:
                this.hideOrShow();
                break;
            case R.id.okButton:
                this.hideOrShow();
                if (myOnDateSetListener != null) {
//                    SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
//                    Log.i("testss", "----------" + mFormatter.format(calendar.getTime()));
                    myOnDateSetListener.onDateSet(calendar.getTime());
                }
                break;

        }

    }

    /**
     * �? 确认 按钮 点击�? 回调 �? 日期 监听�?
     */
    public interface MyOnDateSetListener {
        void onDateSet(Date date);
    }

    /**
     * viewPager 适配�?
     */
    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position == 0) {
                view = datePickerView;
            } else {
                view = timePickerView;
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }


}
