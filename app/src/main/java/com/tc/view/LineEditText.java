package com.tc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by 123 on 2017/8/25.
 */
@SuppressLint("AppCompatCustomView")
public class LineEditText extends EditText {

    private Paint paint;

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        //���û��ʵ�����
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //�����Զ��廭�ʵ���ɫ�����������óɺ�ɫ
        paint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**canvas��ֱ�ߣ������½ǵ����½ǣ�this.getHeight()-2�ǻ�ø�edittext�ĸ߶ȣ����Ǳ���Ҫ-2�������ܱ�֤
         * ���ĺ�����edittext���棬�����ſ��ü��������-2���������һ�Կ�һ���Ƿ��ܿ��ü���
         */
        canvas.drawLine(0, this.getHeight()-2, this.getWidth()-2, this.getHeight()-2, paint);
    }

}
