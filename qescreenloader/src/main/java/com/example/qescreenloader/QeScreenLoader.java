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

public class QeScreenLoader extends Fragment {

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
    private ArrayList<View> elements;

    /** Количество элементов */
    private int elementCount = 5;
    /** Маргины */
    private int[] elementMargin = new int[] {10, 10, 10, 10};
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

    public void setType(Type type) {
        if (type == null) elementAnimator = null;
        else this.elementAnimator = type.getAnimator();
    }

    public void setElementAnimator(ElementAnimator elementAnimator) {
        this.elementAnimator = elementAnimator;
    }

    public void setElementCount(int elementCount) {
        this.elementCount = elementCount;
    }

    public void setElementMargin(int[] elementMargin) {
        this.elementMargin = elementMargin;
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
            elements.add(view);
            elementContainer.addView(view);
            QeViews.setMargins(view, elementMargin[0], elementMargin[1], elementMargin[2], elementMargin[3]);
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
