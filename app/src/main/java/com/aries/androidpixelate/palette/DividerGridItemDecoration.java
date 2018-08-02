package com.aries.androidpixelate.palette;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 适用于GridView，支持在item之间设置任何类型的间距，支持控制是否显示上下左右间隔及是否绘制上下左右背景
 * https://www.cnblogs.com/baiqiantao/p/6923751.html
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int spanCount;
    private int spaceSize;
    private boolean includeLREdge;
    private boolean includeTBEdge;
    private boolean drawLREdge;
    private boolean drawTBEdge;

    private DividerGridItemDecoration(Builder builder) {
        mDivider = builder.mDivider;
        spanCount = builder.spanCount;
        spaceSize = builder.spaceSize;
        includeLREdge = builder.includeLREdge;
        includeTBEdge = builder.includeTBEdge;
        drawLREdge = builder.drawLREdge;
        drawTBEdge = builder.drawTBEdge;
    }

    @Override
    public String toString() {
        return "GridItemDecoration{" +
                "spanCount=" + spanCount +
                ", spaceSize=" + spaceSize +
                ", includeLREdge=" + includeLREdge +
                ", includeTBEdge=" + includeTBEdge +
                '}';
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        if (includeLREdge) {
            outRect.left = spaceSize - column * spaceSize / spanCount;
            outRect.right = (column + 1) * spaceSize / spanCount;
        } else {
            outRect.left = column * spaceSize / spanCount;
            outRect.right = spaceSize - (column + 1) * spaceSize / spanCount;
        }
        if (includeTBEdge) {
            if (position < spanCount) outRect.top = spaceSize; // top edge
            outRect.bottom = spaceSize; // item bottom
        } else {
            if (position >= spanCount) outRect.top = spaceSize; // item top
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider != null) {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
            if (includeLREdge && drawLREdge) drawLR(c, parent);
            if (includeTBEdge && drawTBEdge) drawTB(c, parent);
        }
    }

    private void drawLR(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            //最左边那条线
            if (i % spanCount == 0) {
                int left = child.getLeft() - spaceSize;
                int right = left + spaceSize;
                int bottom = child.getBottom();
                int top = child.getTop() - spaceSize;
                if (i == 0) top = child.getTop(); //【左上方】那一块交给drawTB绘制
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            //最右边那条线
            if ((i + 1) % spanCount == 0) {
                int left = child.getRight();
                int right = left + spaceSize;
                int bottom = child.getBottom();
                int top = child.getTop() - spaceSize;
                if (i == spanCount - 1) top = child.getTop(); //【右上方】那一块交给drawTB绘制
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private void drawTB(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            //最上边那条线
            if (i < spanCount) {
                int top = child.getTop() - spaceSize;
                int bottom = top + spaceSize;
                int left = child.getLeft();
                int right = child.getRight() + spaceSize;
                if ((i + 1) % spanCount == 0 ||
                        (childCount < spanCount && i == childCount - 1))
                    right = child.getRight();  //上边最右边那条线已经绘制了
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            //最下边那条线
            if (childCount % spanCount == 0 && i >= spanCount * (childCount / spanCount - 1)) {
                int top = child.getBottom();
                int bottom = top + spaceSize;
                int left = child.getLeft() - spaceSize;
                int right = child.getRight();
                if ((i + 1) % spanCount == 0) right = child.getRight() + spaceSize;    //最右边那条线
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            } else if (i >= spanCount * (childCount / spanCount)) {
                int top = child.getBottom();
                int bottom = top + spaceSize;
                int right = child.getRight();
                int left = child.getLeft() - spaceSize;
                if (!drawLREdge && i % spanCount == 0) left = child.getLeft(); //最左边那条线
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i >= spanCount) {
                View child = parent.getChildAt(i);
                int left = child.getLeft() - spaceSize;
                if (i % spanCount == 0) left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop() - spaceSize;
                int bottom = top + spaceSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i % spanCount != 0) {
                View child = parent.getChildAt(i);
                int top = child.getTop();
                int bottom = child.getBottom();
                int left = child.getLeft() - spaceSize;
                int right = left + spaceSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public static final class Builder {
        private Drawable mDivider = null;
        private int spanCount;
        private int spaceSize;
        private boolean includeLREdge = false;
        private boolean includeTBEdge = false;
        private boolean drawLREdge = true;
        private boolean drawTBEdge = true;

        public Builder() {
        }

        /**
         * 若不指定，则使用空白代替
         */
        public Builder mDivider(Drawable val) {
            mDivider = val;
            return this;
        }

        /**
         * 行数或列数
         */
        public Builder spanCount(int val) {
            spanCount = val;
            return this;
        }

        /**
         * 行列间距大小
         */
        public Builder spaceSize(int val) {
            spaceSize = val;
            return this;
        }

        /**
         * 是否包含左右边界
         */
        public Builder includeLREdge(boolean val) {
            includeLREdge = val;
            return this;
        }

        /**
         * 是否包含上下边界
         */
        public Builder includeTBEdge(boolean val) {
            includeTBEdge = val;
            return this;
        }

        /**
         * 是否绘制左右边界
         */
        public Builder drawLREdge(boolean val) {
            drawLREdge = val;
            return this;
        }

        /**
         * 是否绘制上下边界
         */
        public Builder drawTBEdge(boolean val) {
            drawTBEdge = val;
            return this;
        }

        public DividerGridItemDecoration build() {
            return new DividerGridItemDecoration(this);
        }
    }
}