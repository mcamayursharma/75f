package com.assignment.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.assignment.canvas.models.Position;

/**
 * Created by mayursharma on 3/27/17.
 */

public class DrawView extends View {

    private static final int FILL_ALPHA = 255 / 2;
    private Position [] positions;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {

        if(positions == null) return;
        for(Position position : positions) {
            
            Paint paint1 = new Paint();
            paint1.setColor(Color.YELLOW);
            paint1.setAlpha(FILL_ALPHA);
            paint1.setStyle(Paint.Style.FILL);
            canvas.drawRect(position.getX(), position.getY(), position.getX()+position.getWidth(), position.getY()+position.getHeight(), paint1);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
            canvas.drawRect(position.getX(), position.getY(), position.getX()+position.getWidth(), position.getY()+position.getHeight(), paint);

            Paint paint2 = new Paint();
            paint2.setColor(Color.BLACK);
            paint2.setTextSize(20);  //set text size
            canvas.drawText(position.getName(), position.getX()+5, position.getY()+20, paint2);
            paint2.setColor(Color.RED);
            paint2.setFakeBoldText(true);
            canvas.drawText("X", position.getX()+position.getWidth()-20, position.getY()+20, paint2);
        }

    }

    public void setPositions(Position [] positions)
    {
        this.positions = positions;
        invalidate();
    }






}
