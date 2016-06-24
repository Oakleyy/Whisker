package ninja.oakley.whisker.media;

import java.awt.AWTException;
import java.io.IOException;

public final class WhiskerPlayer {

    private final Media media;
    private final AudioOutput audioOutput;

    private Process process;
    private Status status = Status.UNKNOWN;
    
    /**
     * construct a new Builder from a media object.
     * 
     * @param media
     * @return
     */
    public static WhiskerPlayer.Builder newBuilder(Media media){
        return new WhiskerPlayer.Builder(media);
    }

    private WhiskerPlayer(WhiskerPlayer.Builder builder) throws AWTException{
        this.media = builder.media;
        this.audioOutput = builder.audioOutput;
        status = Status.READY;
    }

    /**
     * Play the movie.
     * 
     * @throws IOException
     */
    public void play() throws IOException {
        switch(status){
            case LOADING:
            case PLAYING:
                break;
            case PAUSED:
                executeAction(PlayerAction.PLAY);
                break;
            case READY:
                prepareMovie();
                break;
            case DISPOSED:
                throw new RuntimeException("Movie disposed."); 
            case UNKNOWN:
            default:
                throw new RuntimeException("Unknown Movie State.");  
        }
    }

    /**
     * Pause the movie.
     * 
     * @throws IOException
     */
    public void pause() throws IOException{
        switch(status){
            case LOADING:
            case READY:
                break;
            case PAUSED:
            case PLAYING:
                executeAction(PlayerAction.PAUSE);
                break;
            case DISPOSED:
                throw new RuntimeException("Movie disposed."); 
            case UNKNOWN:
            default:
                throw new RuntimeException("Unknown Movie State.");  
        }
    }

    /**
     * Destroy the instance. Becomes unusable.
     * 
     */
    public void dispose(){
        process.destroy();
        this.status = Status.DISPOSED;
    }

    /**
     * Get the status of the movie.
     * 
     * @return status
     */
    public Status getStatus(){
        return this.status;
    }

    private void prepareMovie(){
        status = Status.LOADING;
        process = executeCommand(buildMovie());
        status = Status.PLAYING;
    }

    private void executeAction(PlayerAction action) throws IOException {
        switch(action){
            case PAUSE:
                writeKey('p');
            case PLAY:
                writeKey('p');
            default:
                break;
        }
    }

    private String buildMovie(){
        StringBuilder st = new StringBuilder("omxplayer");
        st.append(" -p -o ");
        st.append(audioOutput.getKeyword());
        st.append(" ");
        st.append(media.getPath().toAbsolutePath().toString());
        System.out.println(st.toString());
        return st.toString();
    }

    private Process executeCommand(String command) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;
    }

    private void writeKey(char c) throws IOException {
        process.getOutputStream().write(c);
        process.getOutputStream().flush();
    }

    public static class Builder{

        private final Media media;
        private AudioOutput audioOutput = AudioOutput.HDMI;

        private Builder(Media media){
            this.media = media;
        }

        public WhiskerPlayer.Builder setAudioOutput(AudioOutput audioOutput){
            if(audioOutput == null){
                throw new NullPointerException("audioOutput == null");
            }

            this.audioOutput = audioOutput;
            return this;
        }

        public WhiskerPlayer build() throws AWTException{
            return new WhiskerPlayer(this);
        }



    }

    public enum AudioOutput {
        HDMI("hdmi"),
        LOCAL("local"),
        BOTH("both");

        private final String keyword;

        private AudioOutput(String keyword){
            this.keyword = keyword;
        }

        public String getKeyword(){
            return this.keyword;
        }
    }

    public enum Status {
        LOADING,
        PLAYING,
        PAUSED,
        READY,
        UNKNOWN,
        DISPOSED;
    }

}
