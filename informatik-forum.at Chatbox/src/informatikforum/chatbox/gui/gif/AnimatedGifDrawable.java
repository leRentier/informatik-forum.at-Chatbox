package informatikforum.chatbox.gui.gif;

import informatikforum.chatbox.business.CommonData;

import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AnimatedGifDrawable extends AnimationDrawable {

    private int mCurrentIndex = 0;

    // TODO Change to something with weakreferences..
    private ArrayList<UpdateListener> mListeners;
    private CommonData cd;
	private boolean started;

    public AnimatedGifDrawable(InputStream source) {

    	mListeners = new ArrayList<UpdateListener>();

        GifDecoder decoder = new GifDecoder();
        decoder.read(source);
        this.cd = CommonData.getInstance();

        // Iterate through the gif frames, add each as animation frame
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            Bitmap bitmap = decoder.getFrame(i);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            // Explicitly set the bounds in order for the frames to display
            drawable.setBounds(0, 0, Math.round(bitmap.getWidth()*cd.getSmileyScale()), Math.round(bitmap.getHeight()*cd.getSmileyScale()));
            addFrame(drawable, decoder.getDelay(i));
            if (i == 0) {
                // Also set the bounds for this container drawable
                setBounds(0, 0, Math.round(bitmap.getWidth()*cd.getSmileyScale()), Math.round(bitmap.getHeight()*cd.getSmileyScale()));
            }
        }
    }

    /**
     * Naive method to proceed to next frame. Also notifies listener.
     */
    public void nextFrame() {
        mCurrentIndex = (mCurrentIndex + 1) % getNumberOfFrames();
for(int i=0; i<this.mListeners.size(); i++){
	mListeners.get(i).update();
}
//        if (mListener != null) mListener.update();
    }

    /**
     * Return display duration for current frame
     */
    public int getFrameDuration() {
    	// If frame duration is 0, we have a not animated GIF. --> Make it as long as possible.
        return getDuration(mCurrentIndex) == 0 ? Integer.MAX_VALUE : getDuration(mCurrentIndex);
    }
    
    @Override
    public int getDuration(int i){
    	int dur = super.getDuration(i);
    	
    	return dur == 0 ? Integer.MAX_VALUE : dur;
    }

    /**
     * Return drawable for current frame
     */
    public Drawable getDrawable() {
        return getFrame(mCurrentIndex);
    }

    /**
     * Interface to notify listener to update/redraw 
     * Can't figure out how to invalidate the drawable (or span in which it sits) itself to force redraw
     */
    public interface UpdateListener {
        void update();
    }

public void addUpdateListener(UpdateListener ul) {
	if(ul != null){
		this.mListeners.add(ul);
	}
}

public boolean isStarted() {
	return this.started;
}

public void started(){
	this.started = true;
}

}