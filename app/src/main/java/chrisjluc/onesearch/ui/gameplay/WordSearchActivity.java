package chrisjluc.onesearch.ui.gameplay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.adapters.WordSearchPagerAdapter;
import chrisjluc.onesearch.animation.BounceTouch;
import chrisjluc.onesearch.base.BaseActivity;
import chrisjluc.onesearch.framework.WordSearchManager;
import chrisjluc.onesearch.models.GameState;
import chrisjluc.onesearch.sound.AudioPlayer;
import chrisjluc.onesearch.sound.util.AudioManagerUtils;
import chrisjluc.onesearch.ui.ResultsActivity;
import chrisjluc.onesearch.utils.DeviceUtils;

public class WordSearchActivity extends BaseActivity implements WordSearchGridView.WordFoundListener,
        PauseDialogFragment.PauseDialogListener, View.OnClickListener, TextToSpeech.OnInitListener {

    private final static boolean ON_SKIP_HIGHLIGHT_WORD = true;
    private final static long ON_SKIP_HIGHLIGHT_WORD_DELAY_IN_MS = 500;

    private final static int TIMER_GRANULARITY_IN_MS = 50;

    /**
     * Current number of grid views that have been instantiated
     * Actual fragment position is currentItem - 2
     */
    public static int currentItem;
    private final PauseDialogFragment mPauseDialogFragment = new PauseDialogFragment();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView mTimerTextView;
    private TextView mScoreTextView;
    private CountDownTimer mCountDownTimer;
    private String mGameState;
    private long mTimeRemaining;
    private long mRoundTime;
    private int mScore;
    private int mSkipped;
    private Random rand = new Random();
    private ImageButton soundBtn;
    private WordSearchPagerAdapter mWordSearchPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = R.string.ga_gameplay_screen;
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.wordsearch_activity);
        mGameState = GameState.START;
        findViewById(R.id.bSkip).setOnClickListener(this);
        findViewById(R.id.bPause).setOnClickListener(this);
        soundBtn = (ImageButton) findViewById(R.id.sound);
        soundBtn.setOnTouchListener(new BounceTouch(this));
        mTimerTextView = (TextView) findViewById(R.id.tvTimer);
        mScoreTextView = (TextView) findViewById(R.id.tvScore);
        mScoreTextView.setText("0");
        sTTobj = new TextToSpeech(this,this);
        sTTobj.setSpeechRate(0.5f);
        currentItem = 0;
        mScore = 0;
        mSkipped = 0;
        AudioManagerUtils.getInstance().setSound(this, soundBtn, R.raw.game_background_music, true, 90);
        // Vibrate for 500 milliseconds


        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        /*
          The {@link android.support.v4.view.PagerAdapter} that will provide
          fragments for each of the sections. We use a
          {@link FragmentPagerAdapter} derivative, which will keep every
          loaded fragment in memory. If this becomes too memory intensive, it
          may be best to switch to a
          {@link android.support.v13.app.FragmentStatePagerAdapter}.
         */
        mWordSearchPagerAdapter = new WordSearchPagerAdapter(getFragmentManager(), getApplicationContext());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (WordSearchViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mWordSearchPagerAdapter);

        mRoundTime = WordSearchManager.getInstance().getGameMode().getTime();
        mTimeRemaining = mRoundTime;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
    }

    private void pauseGameplay() {
        if (mGameState.equals(GameState.PAUSE))
            return;
        mGameState = GameState.PAUSE;
        stopCountDownTimer();
        mPauseDialogFragment.show(getFragmentManager(), "dialog");
        AudioManagerUtils.getInstance().pauseBackgroundMusic();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bSkip:
                //analyticsTrackEvent(R.string.ga_click_skip);
                if (ON_SKIP_HIGHLIGHT_WORD) {
                    ((WordSearchFragment) mWordSearchPagerAdapter.getFragmentFromCurrentItem(currentItem)).highlightWord();
                    (new CountDownTimer(ON_SKIP_HIGHLIGHT_WORD_DELAY_IN_MS, TIMER_GRANULARITY_IN_MS) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            mViewPager.setCurrentItem(currentItem);
                            mSkipped++;
                        }
                    }).start();
                } else {
                    mViewPager.setCurrentItem(currentItem);
                    mSkipped++;
                }


                break;
            case R.id.sound:
                AudioManagerUtils.getInstance().soundToggle(this, soundBtn);
                AudioManagerUtils.getInstance().setSound(this, soundBtn, R.raw.game_background_music, true, 90);
                break;
            case R.id.bPause:
               // analyticsTrackEvent(R.string.ga_click_pause);
                pauseGameplay();
                break;
        }
    }

    private Handler mHandler = new Handler();

    @Override
    public void notifyWordFound() {
        boolean sound = DeviceUtils.getSound(this);
        if (sound) {
            AudioPlayer.getInstance().play(this, winningSound());
        }
        mScore = mScore + 10;
        mScoreTextView.setText(Integer.toString(mScore));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mViewPager != null){
                    mViewPager.setCurrentItem(currentItem);
                }
            }
        }, 200);

    }

    @Override
    public void notifyWordNotFound() {
        boolean sound = DeviceUtils.getSound(this);
        if (sound) {
            AudioPlayer.getInstance().play(this, loosingSound());
        }

    }


    private int randomNumber() {
        int randomNum = rand.nextInt((100 - 0) + 1) + 0;
        return randomNum;
    }

    private int winningSound() {
        int rand = randomNumber();
        if (rand % 2 == 0) {
            return R.raw.hi_win;
        } else if (rand % 3 == 0) {
            return R.raw.hi_win;
        } else {
            return R.raw.low_win;
        }
    }

    private int loosingSound() {
        int rand = randomNumber();
        if (rand % 2 == 0) {
            return R.raw.wrong_answer_base;
        } else if (rand % 3 == 0) {
            return R.raw.wrong_answer_base;
        } else if (rand % 5 == 0) {
            return R.raw.wrong_answer_base;
        } else {
            return R.raw.wrong_light_bass;
        }
    }

    @Override
    public void onDialogQuit() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onDialogResume() {
        mGameState = GameState.PLAY;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
        setFullscreen();
        AudioManagerUtils.getInstance().resumeBackgroundMusic();


    }

    @Override
    public void onDialogRestart() {
        mGameState = GameState.PLAY;
        restart();
        AudioManagerUtils.getInstance().setSound(this, soundBtn, R.raw.game_background_music, true, 90);
    }

    private void restart() {
        mScore = 0;
        mSkipped = 0;
        mTimeRemaining = mRoundTime;
        setupCountDownTimer(mTimeRemaining);
        startCountDownTimer();
        setFullscreen();
        mScoreTextView.setText("0");
        mViewPager.setCurrentItem(currentItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //analyticsTrackScreen(getString(categoryId));
        if (mGameState.equals(GameState.START) || mGameState.equals(GameState.FINISHED))
            mGameState = GameState.PLAY;
        else
            pauseGameplay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioManagerUtils.getInstance().pauseBackgroundMusic();
        stopCountDownTimer();
    }
    private  TextToSpeech sTTobj;

    public TextToSpeech getTTObject(){
        return sTTobj;
    }

    @Override
    public void onBackPressed() {
        pauseGameplay();
    }

    private void setupCountDownTimer(final long timeinMS) {
        mCountDownTimer = new CountDownTimer(timeinMS, TIMER_GRANULARITY_IN_MS) {


            public void onTick(long millisUntilFinished) {
                mTimeRemaining = millisUntilFinished;
                long secondsRemaining = mTimeRemaining / 1000 + 1;
                mTimerTextView.setText(Long.toString(secondsRemaining));
                if (secondsRemaining <= 10) {
                    mTimerTextView.setTextColor(getResources().getColor(R.color.red));
                } else {
                    mTimerTextView.setTextColor(Color.BLACK);

                }
            }

            public void onFinish() {
                mGameState = GameState.FINISHED;
                mTimerTextView.setText("0");
                ((WordSearchFragment) mWordSearchPagerAdapter.getFragmentFromCurrentItem(currentItem)).highlightWord();
                (new CountDownTimer(ON_SKIP_HIGHLIGHT_WORD_DELAY_IN_MS, TIMER_GRANULARITY_IN_MS) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent i = new Intent(getApplicationContext(), ResultsActivity.class);
                        i.putExtra("score", mScore);
                        i.putExtra("skipped", mSkipped);
                        startActivity(i);
                        finish();
                    }
                }).start();
            }
        };
    }

    private void startCountDownTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.start();
    }

    private void stopCountDownTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
    }

    public long getTimeRemaining() {
        return mTimeRemaining;
    }

    public int getScore() {
        return mScore;
    }

    @Override
    public void onInit(int i) {
        if(i != TextToSpeech.ERROR) {
            sTTobj.setLanguage(Locale.US);
        }
        if (i == TextToSpeech.SUCCESS) {
            ((WordSearchFragment) mWordSearchPagerAdapter.getFragmentFromCurrentItem(currentItem)).speakOut();
        }
    }
}
