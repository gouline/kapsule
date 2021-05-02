/*
 * Copyright 2017 Mike Gouline
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.gouline.kapsule.demo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import kotlin.jvm.functions.Function1;
import net.gouline.kapsule.Delegate;
import net.gouline.kapsule.Kapsule;
import net.gouline.kapsule.demo.App;
import net.gouline.kapsule.demo.R;
import net.gouline.kapsule.demo.data.Dao;
import net.gouline.kapsule.demo.di.Module;

/**
 * Counter activity showcasing that Kapsule still works with Java, even if it's not pretty.
 */
public class CounterActivity extends AppCompatActivity {

    private final Kapsule<Module> mKap = new Kapsule<>();
    private final Delegate<Module, Dao> mDao = mKap.required(new Function1<Module, Dao>() {
        @Override
        public Dao invoke(Module module) {
            return module.getDao();
        }
    });

    private TextView mCounterTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        mKap.inject(App.Companion.module(this));

        mCounterTextView = (TextView) findViewById(R.id.txt_counter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateCount();
    }

    /**
     * Fetches count from the DAO and applies it to the text view.
     */
    private void updateCount() {
        if (mDao.getValue() != null) {
            String text = Integer.toString(mDao.getValue().fetchCount());
            mCounterTextView.setText(text);
        }
    }
}
