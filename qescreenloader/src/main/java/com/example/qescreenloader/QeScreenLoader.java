package com.example.qescreenloader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qegame.qeutil.androids.QeAndroid;
import com.qegame.qeutil.androids.views.QeViews;
import com.qegame.qeutil.doing.Do;

import java.util.ArrayList;
import java.util.List;

public class QeScreenLoader extends Fragment {

    // TODO: 01.03.2020 Отображать прогресс
    // TODO: 01.03.2020 Расширить возможности интерфейса Element
    // TODO: 01.03.2020 Позиционирование елементов. По умолчанию он все линере
    // TODO: 01.03.2020 Больше различных View

    enum Type {
        WAVE_TOP() {
            @Override
            public ElementAnimator getAnimator() {
                return new ElementAnimator.WaveTop();
            }
        },
        WAVE_BOTTOM {
            @Override
            public ElementAnimator getAnimator() {
                return new ElementAnimator.WaveBottom();
            }
        },
        SEQUENTIALLY_TOP {
            @Override
            public ElementAnimator getAnimator() {
                return new ElementAnimator.SequentiallyTop();
            }
        },
        SEQUENTIALLY_BOTTOM {
            @Override
            public ElementAnimator getAnimator() {
                return new ElementAnimator.SequentiallyBottom();
            }
        };

        Type() {

        }

        public abstract ElementAnimator getAnimator();
    }

    /** Views */
    private List<View> elements;

    /** Количество элементов */
    private int elementCount = 5;
    @ColorInt
    private int elementColor;

    /** Контейнер объектов */
    private ViewGroup elementContainer;
    private ViewGroup background;

    private ElementAnimator elementAnimator;

    @ColorInt
    private int colorBackgroundDef;
    @ColorInt
    private int colorBackground;

    public QeScreenLoader() {
        setType(Type.WAVE_TOP);
    }

    public void setColorBackground(@ColorInt int color) {
        this.colorBackground = color;
        if (background != null) background.setBackgroundColor(color);
    }
    public void setColorElements(@ColorInt int color) {
        this.elementColor = color;
        if (elements == null) return;
        for (View view : elements) {
            if (view instanceof Element)
                ((Element) view).setColor(color);
        }
    }
    public void setType(Type type) {
        if (type == null) elementAnimator = null;
        else this.elementAnimator = type.getAnimator();
    }
    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }


    protected View initElement(int position) {
        return new Ball(getContext());
    }


    private void runAnimation() {
        elementAnimator.run(elements);
    }
    private void initElements() {
        elementContainer.removeAllViews();
        for (int i = 0; i < elementCount; i++) {
            View view = initElement(i);
            if (view instanceof Element)
                ((Element) view).setColor(elementColor);
            elements.add(view);
            elementContainer.addView(view);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loader, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getView() != null;
        this.colorBackgroundDef = QeAndroid.getThemeColor(getContext(), QeAndroid.ThemeColor.PRIMARY);
        this.elementContainer = getView().findViewById(R.id.elementContainer);
        this.background = (ViewGroup) this.elementContainer.getParent();
        this.background.setBackgroundColor(colorBackground == 0? colorBackgroundDef : colorBackground);
        this.elements = new ArrayList<>();
        initElements();
    }
    @Override
    public void onResume() {
        super.onResume();

        QeViews.doOnMeasureView(getView(), new Do.With<View>() {
            @Override
            public void work(View with) {
                runAnimation();
            }
        });

    }

}
