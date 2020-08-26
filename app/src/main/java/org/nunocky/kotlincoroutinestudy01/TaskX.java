package org.nunocky.kotlincoroutinestudy01;

import androidx.annotation.NonNull;

class TaskX {

    interface Callback {
        void onStart();

        void onCompleted(@NonNull String[] strs);
    }

    private static Callback zeroCallback = new Callback() {
        @Override
        public void onStart() {
        }

        @Override
        public void onCompleted(@NonNull String[] strs) {
        }
    };

    private Callback mCallback = zeroCallback;

    public void setCallback(Callback cb) {
        if (cb == null) {
            cb = zeroCallback;
        }

        mCallback = cb;
    }

    public void execute() {
        mCallback.onStart();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }


        final String[] strs = {"AAA", "BBB", "CCC"};
        mCallback.onCompleted(strs);
    }
}
