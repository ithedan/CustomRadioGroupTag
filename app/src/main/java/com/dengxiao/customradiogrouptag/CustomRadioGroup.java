package com.dengxiao.customradiogrouptag;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dengxiao on 2016/12/2.
 */

public class CustomRadioGroup extends RadioGroup {

    private List<RowView> rowViews;//存放每行radioBotton的集合
    private int horizontalSpacing =20;//默认水平间距
    private int verticalSpacing = 10;//默认垂直间距
    private Context mContext;
    private OnclickListener listener;

    public void setListener(OnclickListener listener) {
        this.listener = listener;
    }

    public CustomRadioGroup(Context context) {
        this(context, null);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        rowViews = new ArrayList<>();
    }

    //设置水平间距（单位dp）
    public void setHorizontalSpacing(int horizontalSpacing_dp) {
        this.horizontalSpacing = dip2px(mContext, horizontalSpacing_dp);
    }

    //设置垂直间距（单位dp）
    public void setVerticalSpacing(int verticalSpacing_dp) {
        this.verticalSpacing = dip2px(mContext, verticalSpacing_dp);
        ;
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rowViews.clear();//清空集合
        //获取屏幕总宽度(包含默认paddingleft和paddingright)
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //去除默认宽带与实际宽带进行比较
        int nopaddingWidth = width - getPaddingLeft() - getPaddingRight();

        RowView rowView = null;
        //遍历所有的view进行分行
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);//获取对应的view
            childView.measure(0, 0);//通知父view进行测量
            if (rowView == null) {
                rowView = new RowView();
            }
            if (rowView.getRowViews().size() == 0) {//如果当前行一个view都没有，，直接添加，不用比较
                rowView.addChidView(childView);
            } else if (rowView.getRowWidth() + horizontalSpacing + childView.getMeasuredWidth() > nopaddingWidth) {
                //如果当前的行宽度+水平间距+当前view的宽带大于nopaddingWidth，则需要换行
                rowViews.add(rowView);//将之前的行保存起来
                rowView = new RowView();//重新创建一行，，将当前的view保存起来
                rowView.addChidView(childView);
            } else {
                rowView.addChidView(childView);//当前childView添加后没有超出nopaddingWidth，可以将childView添加到当前行
            }
            // 如果当前childView是最后一个子View，会造成最后一行line丢失
            if (i == getChildCount() - 1) {
                rowViews.add(rowView);
            }
        }
        //计算layout所有行需要的高度
        int heght = getPaddingTop() + getPaddingBottom();//加上padding值
        for (int i = 0; i < rowViews.size(); i++) {
            heght += rowViews.get(i).getRowHeight();//添加每行高度
        }
        heght += (rowViews.size() - 1) * verticalSpacing;//添加垂直间距高度
        setMeasuredDimension(width, heght);//向父view申请宽带和高度
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        }
    }

    //将每个view放到对应的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        for (int i = 0; i < rowViews.size(); i++) {
            RowView rowView = rowViews.get(i);//获取到当前的line对象
            //后面每一行的top值要相应的增加,当前行的top是上一行的top值+height+垂直间距
            if (i > 0) {
                paddingTop += rowViews.get(i - 1).getRowHeight() + verticalSpacing;
            }
            List<View> viewList = rowView.getRowViews();//获取line的子View集合

            for (int j = 0; j < viewList.size(); j++) {
                View childView = viewList.get(j);//获取当前的子View
                if (j == 0) {
                    //每行的第一个子View,需要靠左边摆放
                    childView.layout(paddingLeft, paddingTop, paddingLeft + childView.getMeasuredWidth(),
                            paddingTop + childView.getMeasuredHeight());
                } else {
                    //摆放后面的子View，需要参考前一个子View的right
                    View preView = viewList.get(j - 1);//获取前一个子View
                    int left = preView.getRight() + horizontalSpacing;//前一个VIew的right+水平间距
                    childView.layout(left, preView.getTop(), left + childView.getMeasuredWidth(), preView.getBottom());
                }
            }
        }
    }

    public interface OnclickListener{
        void OnText(String text);
    }

    class RowView {
        private List<View> lineViews;//用于存放每行的view
        private int rowWidth;//行宽
        private int rowHeight;//行高

        public RowView() {
            lineViews = new ArrayList<>();

        }

        public List<View> getRowViews() {
            return lineViews;
        }

        public int getRowWidth() {
            return rowWidth;
        }

        public int getRowHeight() {
            return rowHeight;
        }

        //存放view到rowViews
        public void addChidView(View view) {
            if (!lineViews.contains(view)) {
                ((RadioButton)view).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null){
                            listener.OnText(((RadioButton)v).getText().toString().trim());
                        }
                    }
                });
                //更新高度和宽度
                if (lineViews.size() == 0) {
                    //第一次添加view,不用添加水平间距
                    rowWidth = view.getMeasuredWidth();
                } else {
                    //不是第一次添加，需要添加水平间距
                    rowWidth += view.getMeasuredWidth() + horizontalSpacing;
                }
                //height应该是所有子view中高度最大的那个
                rowHeight = Math.max(view.getMeasuredHeight(), rowHeight);
                lineViews.add(view);
            }

        }

    }


}
