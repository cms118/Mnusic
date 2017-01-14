package in.mnnit.pprasunn.mnusic;



import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    Uri u;
    static seekUpdater su;
    SeekBar sb;
    FloatingActionButton btPlay,btFF,btNxt,btPv,btFB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        btPlay=(FloatingActionButton)findViewById(R.id.btPlay);
        btFF=(FloatingActionButton)findViewById(R.id.btFF);
        btNxt=(FloatingActionButton)findViewById(R.id.btNxt);
        btPv=(FloatingActionButton)findViewById(R.id.btPv);
        btFB=(FloatingActionButton)findViewById(R.id.btFB);


        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);
        btFB.setOnClickListener(this);

        sb=(SeekBar)findViewById(R.id.seekBar);

        if(mp!=null){
            if(su!=null){
                su.endthread();
                su.interrupt();
            }
            mp.stop();
            mp.release();
        }

        Intent i=getIntent();
        Bundle b=i.getExtras();
        mySongs =(ArrayList) b.getParcelableArrayList("songlist");
        position=b.getInt("pos",0);

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        mp.setLooping(true);
       // mp.attachAuxEffect(1);
        btPlay.setImageResource(R.drawable.pause1);
        sb.setMax(mp.getDuration());
        su=new seekUpdater(true);
        su.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });



    }


    class seekUpdater extends Thread{

        private boolean running;

        seekUpdater(boolean status){
            running=status;
        }

        public void endthread(){
            running=false;
        }

        @Override
        public void run() {
            try {
                while (running==true) {
                    int dur = mp.getDuration();
                    int cur = mp.getCurrentPosition();
                    while (cur < dur) {
                        sleep(500);
                        cur = mp.getCurrentPosition();
                        sb.setProgress(cur);
                    }
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
                running=false;
            }
        }
    }


    @Override
    public void onClick(View v) {
        //int a=R.drawable.pause;
        int id=v.getId();
        switch(id){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    btPlay.setImageResource(R.drawable.play1);
                    mp.pause();
                }
                else {
                    btPlay.setImageResource(R.drawable.pause1);
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                if(su!=null)
                {
                    su.endthread();
                    su.interrupt();
                    su=null;
                }
                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                mp.setLooping(true);
                btPlay.setImageResource(R.drawable.pause1);
                sb.setMax(mp.getDuration());
                sb.setProgress(mp.getCurrentPosition());
                su=new seekUpdater(true);
                su.start();
                break;
            case R.id.btPv:
                if(su!=null)
                {
                    su.endthread();
                    su.interrupt();
                    su=null;
                }
                mp.stop();
                mp.release();
                position=(position-1<0)?mySongs.size()-1:position-1;
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                mp.setLooping(true);
                btPlay.setImageResource(R.drawable.pause1);
                sb.setMax(mp.getDuration());
                sb.setProgress(mp.getCurrentPosition());
                su=new seekUpdater(true);
                break;
        }
    }
}
