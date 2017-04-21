package chrisjluc.onesearch.ui.gameplay;

import android.content.Context;
import android.transition.CircularPropagation;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.adapters.WordSearchGridAdapter;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.utils.CommonUtil;
import chrisjluc.onesearch.wordSearchGenerator.generators.StringUtils;
import chrisjluc.onesearch.wordSearchGenerator.generators.WordSearchGenerator;
import chrisjluc.onesearch.wordSearchGenerator.models.Node;
import chrisjluc.onesearch.wordSearchGenerator.models.Point;

public class WordSearchGridView extends GridView {
    /*
     * TODO: Implement this using a viewgroup to draw out the wordsearch more efficiently
     * Gridview wasn't built to draw out so many elements at the same time, especially when drawing out hundreds of elements in advanced
     * Really slow on older devices
     * Use ViewGroup
     *
     */

    public boolean mIsWordFound = false;
    private int mXLength, mYLength;
    private int mColumnWidth;
    private int mHorizontalMargin, mVerticalMargin;
    private float mDensity;
    private Point mStartDrag, mEndDrag;
    private Node[] mWordSearchNodes;
    private List<Node> mWordSearchHighlightedNodes;
    private String mWord, mWordReverse;
    private Point mWordStart, mWordEnd;
    private WordFoundListener mWordFoundListener;
    private WordSearchGridAdapter mAdapter;

    public WordSearchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WordSearchManager manager = WordSearchManager.getInstance();
        WordSearchGenerator wordSearch = manager.getWordSearch(WordSearchActivity.currentItem++);
        while (wordSearch == null) {
            wordSearch = manager.getWordSearch(WordSearchActivity.currentItem++);
        }
        mXLength = wordSearch.getnCol();
        mYLength = wordSearch.getnRow();
        mWord = wordSearch.getWord();
        mWordReverse = StringUtils.reverse(mWord);
        List<Point> points = wordSearch.getStartAndEndPointOfWord();

        // Convert cartesian from matrix coordinates
        mWordStart = new Point(points.get(0).y, points.get(0).x);
        mWordEnd = new Point(points.get(1).y, points.get(1).x);
        mWordSearchNodes = wordSearch.getWordSearchNodes();
        // Init to max size of wordsearch
        mWordSearchHighlightedNodes = new ArrayList<Node>(mXLength);
        mHorizontalMargin = (int) getResources().getDimension(R.dimen.grid_horizontal_margin);
        mVerticalMargin = (int) getResources().getDimension(R.dimen.grid_horizontal_margin);

        // Calculate column dimensions
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mDensity = displayMetrics.density;
        int width = displayMetrics.widthPixels;
        mColumnWidth = (int) (width - 2 * mHorizontalMargin * mDensity) / (mXLength);
        setColumnWidth(mColumnWidth);
        mAdapter = new WordSearchGridAdapter(context, mWordSearchNodes, mColumnWidth, mXLength);
        setAdapter(mAdapter);
    }

    public void setWordFoundListener(WordFoundListener listener) {
        this.mWordFoundListener = listener;
    }

    private void isWordFound() {
        String selectedWord = getSelectedString();
        if (selectedWord != null && mWord.equals(selectedWord)
                /*|| mWordReverse.equals(selectedWord)*/) {
            mIsWordFound = true;
            mWordFoundListener.notifyWordFound();
            wordFoundHighLight();
        }else{
            mWordFoundListener.notifyWordNotFound();
        }
    }

    private String getSelectedString() {
        // No need for validation because updateCurrentHighlightedNodes validates
        if (mEndDrag == null || mStartDrag == null) return null;
        int dX = mEndDrag.x - mStartDrag.x;
        int dY = mEndDrag.y - mStartDrag.y;

        int length = 0;

        if (dX != 0)
            length = Math.abs(dX);

        if (dY != 0)
            length = Math.abs(dY);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length + 1; i++) {
            Point point = new Point(mStartDrag.x, mStartDrag.y);
            if (dX != 0)
                point.x += dX > 0 ? i : -i;
            if (dY != 0)
                point.y += dY > 0 ? i : -i;

            int index = point.y * mXLength + point.x;
            if (index < 0 || index >= mWordSearchNodes.length)
                continue;
            sb.append(mWordSearchNodes[index].getLetter());
        }
        return sb.toString();
    }

    public void highlightWord() {
        mStartDrag = mWordStart;
        updateCurrentHighlightedNodes(mWordEnd, true);
    }

    private void updateCurrentHighlightedNodes(Point p, boolean isFound) {
        if (p.y < 0 || p.y >= mYLength || p.x < 0 || p.x >= mXLength) return;

        if (p.equals(mEndDrag)){
            return;
        }

        mEndDrag = p;

        boolean isValid = false;
        // diagonal
        if (Math.abs(mEndDrag.x - mStartDrag.x) == Math.abs(mEndDrag.y - mStartDrag.y)) {
            isValid = true;
            // horizontal
        } else if (mEndDrag.x - mStartDrag.x == 0) {
            isValid = true;
            // vertical
        } else if (mEndDrag.y - mStartDrag.y == 0) {
            isValid = true;
        }

        if (!isValid) return;

        clearHighlightedNodes();
        int dX = mEndDrag.x - mStartDrag.x;
        int dY = mEndDrag.y - mStartDrag.y;

        int length = 0;

        if (dX != 0)
            length = Math.abs(dX);

        if (dY != 0)
            length = Math.abs(dY);

        for (int i = 0; i < length + 1; i++) {
            Point point = new Point(mStartDrag.x, mStartDrag.y);
            if (dX != 0)
                point.x += dX > 0 ? i : -i;
            if (dY != 0)
                point.y += dY > 0 ? i : -i;

            int index = point.y * mXLength + point.x;
            if (index < 0 || index >= mWordSearchNodes.length)
                continue;
            Node n = mWordSearchNodes[index];
            if(isFound){
                n.setHighlighted(true);
                n.setHightLightStatus(CommonUtil.WORD_FOUND_HIGHLIGHT);
            } else{
                n.setHighlighted(false);
                n.setHightLightStatus(CommonUtil.WORD_NOT_FOUND_HIGHLIGHT);
            }

            mWordSearchHighlightedNodes.add(n);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void clearHighlightedNodes() {
        for (Node n : mWordSearchHighlightedNodes){
            n.setHighlighted(false);
            n.setHightLightStatus(CommonUtil.WORD_NORMAL);
        }

        mWordSearchHighlightedNodes.clear();
    }

    private void wordFoundHighLight(){
        if(mWordSearchHighlightedNodes != null && mWordSearchHighlightedNodes.size() != 0){
            for (Node n : mWordSearchHighlightedNodes){
                n.setHighlighted(true);
                n.setHightLightStatus(CommonUtil.WORD_FOUND_HIGHLIGHT);
            }
            mWordSearchHighlightedNodes.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void clearHighlighedNodesAndNotifyAdapter() {
       // wrongWordHighlight();
        for (Node n : mWordSearchHighlightedNodes){
            n.setHighlighted(false);
            n.setHightLightStatus(CommonUtil.WORD_NORMAL);
        }
        mWordSearchHighlightedNodes.clear();
        mAdapter.notifyDataSetChanged();
    }

   /* private void wrongWordHighlight() throws InterruptedException {
        for (Node n : mWordSearchHighlightedNodes){
            n.setHighlighted(false);
            n.setHightLightStatus(CommonUtil.WORD_NOT_FOUND_HIGHLIGHT);
        }

      //  mWordSearchHighlightedNodes.clear();
        mAdapter.notifyDataSetChanged();
      //  Thread.sleep(200);
    }
*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x1, x2, y1, y2;
        boolean result = false;
        if (mIsWordFound) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = (int) event.getX();
                y1 = (int) event.getY();
                mStartDrag = new Point(calcRelativeX(x1), calcRelativeY(y1));
                result = true;
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = (int) event.getX();
                y2 = (int) event.getY();
                result = true;
                updateCurrentHighlightedNodes(new Point(calcRelativeX(x2), calcRelativeY(y2)), false);
                break;
            case MotionEvent.ACTION_UP:
                isWordFound();
                if (!mIsWordFound)
                        clearHighlighedNodesAndNotifyAdapter();
                result = true;
                break;
        }

        if (result) invalidate();
        return result;
    }

    private int calcRelativeX(int d) {
        return (int) (d - mHorizontalMargin * mDensity) / mColumnWidth;
    }

    private int calcRelativeY(int d) {
        return (int) (d - mVerticalMargin * mDensity) / mColumnWidth;
    }

    public String getWord() {
        return mWord;
    }

    public interface WordFoundListener {
        public void notifyWordFound();
        public void notifyWordNotFound();
    }
}
