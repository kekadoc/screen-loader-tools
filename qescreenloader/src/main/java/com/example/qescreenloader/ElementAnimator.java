package com.example.qescreenloader;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qegame.qeanim.AnimCollector;
import com.qegame.qeanim.view.TranslationY;
import com.qegame.qeutil.listening.subscriber.Subscriber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Аниматор */
public abstract class ElementAnimator {

    private AnimCollector<Animator, View> animCollector;
    private List<View> views;

    public ElementAnimator() {
        this.animCollector = new AnimCollector.NonNullCollector<Animator, View>() {
            @NonNull
            @Override
            protected Animator initAnimation(@NonNull View target, int type) {
                return makeAnimation(target, type);
            }
        };
    }

    /** Создать анимацию для View
     *
     * @param view View
     * @param type Особый тип аниации */
    protected abstract Animator makeAnimation(@NonNull View view, int type);

    protected abstract void startAnimation();
    protected abstract void stopAnimation();

    /** Запустить анимацию.
     * @param views Коллекция View */
    public final void run(@NonNull List<View> views) {
        this.views = views;
        startAnimation();
    }

    /** Остановить анимацию */
    public final void stop() {
        stopAnimation();
    }

    /** Список всех View */
    public final List<View> getViews() {
        return views;
    }

    /** Запрос на аниматор. */
    protected final Animator getAnimation(View view, int type) {
        return animCollector.getAnimation(view, type);
    }
    /** Запрос на аниматор. */
    protected final Animator getAnimation(View view) {
        return getAnimation(view, 0);
    }




    /** Подразумивается что элементы будут анимированны по порядку. */
    public static abstract class InSeries extends ElementAnimator {

        /** Продолжительность анимации одного элемента */
        private long duration;

        public InSeries() {
            this.duration = 300L;
        }

        /** Запрос на следуюший View, к которому бедет применина анимация. */
        protected abstract View getNextView(View view);

        public long getDuration() {
            return duration;
        }

    }

    private static abstract class NativeSeries extends InSeries {
        private static final String TAG = "NativeSeries-TAG";

        private static final int REVERSE = 10;
        private static final int RESTART = 20;

        private boolean reverse;

        private int orderType = REVERSE;

        /** Анимировать следующий объект. */
        protected void animateNext(@Nullable Animator previewAnim, @NonNull View view) {
            getAnimation(view).start();
        }

        @Override
        protected View getNextView(@Nullable View view) {
            if (view == null) return getViews().get(0);

            int index = getViews().indexOf(view);
            int size = getViews().size();

            if (index == (size - 1)) {
                switch (orderType) {
                    case RESTART: return getViews().get(0);
                    case REVERSE:
                        reverse = true;
                        return getViews().get(size - 2);
                }
            }
            if (index == 0 && reverse) {
                reverse = false;
                return getViews().get(1);
            }

            if (reverse) return getViews().get(index - 1);
            else return getViews().get(index + 1);

        }
        @Override
        protected void startAnimation() {
            animateNext(null, getNextView(null));
        }
        @Override
        protected void stopAnimation() {

        }
    }

    /** Волна Вверх */
    public static final class WaveTop extends NativeSeries {
        private static final String TAG = "WaveTop-TAG";

        @Override
        protected Animator makeAnimation(@NonNull View view, int type) {
            return TranslationY.builder(view)
                    .from(0f)
                    .to((float) -view.getHeight() / 2)
                    .duration(getDuration())
                    .onEnd(new Subscriber.Single<ObjectAnimator>() {
                        @Override
                        public void onCall(ObjectAnimator param) {
                            animateNext(getAnimation(view), getNextView(view));
                        }
                    })
                    .repeatCount(1)
                    .build();
        }

    }
    /** Волна Вниз */
    public static final class WaveBottom extends NativeSeries {

        @Override
        protected Animator makeAnimation(@NonNull View view, int type) {
            return TranslationY.builder(view)
                    .to((float) view.getHeight() / 2)
                    .duration(getDuration())
                    .onEnd(new Subscriber.Single<ObjectAnimator>() {
                        @Override
                        public void onCall(ObjectAnimator param) {

                        }
                    })
                    .build();
        }
    }
    /** Последовательно Вверх */
    public static final class SequentiallyTop extends NativeSeries {

        @Override
        protected Animator makeAnimation(@NonNull View view, int type) {
            return TranslationY.builder(view)
                    .to((float) -view.getHeight() / 2)
                    .duration(getDuration())
                    .onEnd(new Subscriber.Single<ObjectAnimator>() {
                        @Override
                        public void onCall(ObjectAnimator param) {
                            animateNext(getAnimation(view), getNextView(view));
                        }
                    })
                    .repeatCount(1)
                    .build();
        }
    }
    /** Последовательно Вниз */
    public static final class SequentiallyBottom extends NativeSeries {

        @Override
        protected Animator makeAnimation(@NonNull View view, int type) {
            return TranslationY.builder(view)
                    .to((float) view.getHeight() / 2)
                    .duration(getDuration())
                    .onEnd(new Subscriber.Single<ObjectAnimator>() {
                        @Override
                        public void onCall(ObjectAnimator param) {
                            animateNext(getAnimation(view), getNextView(view));
                        }
                    })
                    .repeatCount(1)
                    .build();
        }
    }

}
