package maas.com.mx.maas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

/**
 * Created by damserver on 21/04/2015.
 */

    public class splash extends Activity {
        private Thread mSplashThread;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.splash);
            final splash sPlashScreen = this;

            mSplashThread =  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){

                            wait(5000);
                        }
                    }
                    catch(InterruptedException ex){
                    }

                    finish();

                    Intent intent = new Intent();
                    intent.setClass(sPlashScreen,main.class);
                    startActivity(intent);

                }
            };

            mSplashThread.start();
        }


        @Override

        public boolean onTouchEvent(MotionEvent evt)
        {
            if(evt.getAction() == MotionEvent.ACTION_DOWN)
            {
                synchronized(mSplashThread){
                    mSplashThread.notifyAll();
                }
            }
            return true;
        }
    }
