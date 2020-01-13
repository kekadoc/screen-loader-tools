package com.example.qescreenloader;

import android.animation.Animator;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qegame.animsimple.path.Path;
import com.qegame.animsimple.path.TranslationY;
import com.qegame.qeutil.listening.subscriber.Subscriber;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ElementAnimator {
    private static final String TAG = "ElementAnimator-TAG";

    private ArrayList<View> views;

    public final void run(ArrayList<View> views) {
        this.views = views;
        startAnimation();
    }

    public final void stop() {
        stopAnimation();
    }

    public final ArrayList<View> getViews() {
        return views;
    }

    protected abstract void startAnimation();

    protected abstract void stopAnimation();

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

    private static abstract class NativeSeries<A extends Path<View, ?>> extends InSeries {

        private ArrayList<A> animations;

        private boolean reverse;
        private boolean startOverOnLast;

        NativeSeries() {
            this.animations = new ArrayList<>();
        }

        ArrayList<A> getAnimations() {
            return animations;
        }

        /** Создание анимации для View */
        protected abstract A makeAnim(@NonNull View view);

        /** Анимировать следующий объект. */
        protected void animateNext(@Nullable A previewAnim, @NonNull View view) {
            A nextAnimation = makeAnim(view);
            getAnimations().add(getViews().indexOf(view), nextAnimation);
            nextAnimation.start();
        }

        @Override
        protected View getNextView(@Nullable View view) {
            if (view == null) return getViews().get(0);

            int index = getViews().indexOf(view);
            int size = getViews().size();

            if (index == (size - 1)) {
                if (startOverOnLast) return getViews().get(0);
                else {
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
            animations.clear();
        }
    }

    /** Волна Вверх */
    public static final class WaveTop extends NativeSeries<TranslationY<View>> {

        @Override
        protected TranslationY<View> makeAnim(@NonNull View view) {
            return TranslationY.builder(view)
                    .to((float) -view.getHeight() / 2)
                    .duration(getDuration())
                    .onEnd(new Subscriber.Twins<Path<View, Float>, Animator>() {
                        @Override
                        public void onCall(Path<View, Float> first, Animator second) {
                            Log.e(TAG, "onCall: " + getViews().size() + "  " + view);
                            animateNext(getAnimations().get(getViews().indexOf(view)), getNextView(view));
                        }
                    })
                    .build();
        }
        @Override
        protected void animateNext(@Nullable TranslationY<View> previewAnim, @NonNull View view) {
            if (previewAnim != null) previewAnim.reverse();
            super.animateNext(previewAnim, view);
        }

    }
    /** Волна Вниз */
    public static final class WaveBottom extends NativeSeries<TranslationY<View>> {

        @Override
        protected TranslationY<View> makeAnim(@NonNull View view) {
            return TranslationY.builder(view)
                    .to((float) view.getHeight() / 2)
                    .duration(getDuration())
                    .onEnd(new Subscriber.Twins<Path<View, Float>, Animator>() {
                        @Override
                        public void onCall(Path<View, Float> first, Animator second) {
                            animateNext(getAnimations().get(getViews().indexOf(view)), getNextView(view));
                        }
                    })
                    .build();
        }
        @Override
        protected void animateNext(@Nullable TranslationY<View> previewAnim, @NonNull View view) {
            if (previewAnim != null) previewAnim.reverse();
            super.animateNext(previewAnim, view);
        }

    }
    /** Последовательно Вверх */
    public static final class SequentiallyTop extends NativeSeries<TranslationY<View>> {

        @Override
        protected TranslationY<View> makeAnim(@NonNull View view) {
            return TranslationY.builder(view)
                    .to((float) -view.getHeight() / 2)
                    .duration(getDuration())
                    .reverse(true)
                    .onEnd(new Subscriber.Twins<Path<View, Float>, Animator>() {
                        @Override
                        public void onCall(Path<View, Float> first, Animator second) {
                            animateNext(getAnimations().get(getViews().indexOf(view)), getNextView(view));
                        }
                    })
                    .build();
        }

    }
    /** Последовательно Вниз */
    public static final class SequentiallyBottom extends NativeSeries<TranslationY<View>> {

        @Override
        protected TranslationY<View> makeAnim(@NonNull View view) {
            return TranslationY.builder(view)
                    .to((float) view.getHeight() / 2)
                    .duration(getDuration())
                    .reverse(true)
                    .onEnd(new Subscriber.Twins<Path<View, Float>, Animator>() {
                        @Override
                        public void onCall(Path<View, Float> first, Animator second) {
                            animateNext(getAnimations().get(getViews().indexOf(view)), getNextView(view));
                        }
                    })
                    .build();
        }

    }
}
