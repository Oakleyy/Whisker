package ninja.oakley.whisker.data;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ninja.oakley.whisker.Whisker;
import ninja.oakley.whisker.data.USBMountingService.Drive;

/**
 * Searches mounted drives for media matching the .mp4 filetag. When found it sends the data to the 
 * library manager for processing.
 * 
 * @author oakley
 *
 */
public class MediaSearchingService {

    private final Whisker whisker;

    private final Service service;
    private final int deep;
    private final int searchTime;
    private final TimeUnit timeUnit;

    /**
     * Constructor for a media searching class.
     * 
     * @param whisker instance of main class
     * @param deep how many folders deep it should go
     * @param searchTime interval of seaching
     * @param timeUnit unit for searchTime
     */
    public MediaSearchingService(Whisker whisker, int deep, int searchTime, TimeUnit timeUnit){
        this.whisker = whisker;
        this.service = new Service();
        this.deep = deep;
        this.searchTime = searchTime;
        this.timeUnit = timeUnit;
    }

    /**
     * Constructor for a media searching class.
     * Assumes 0 deep, seachTime of 5 min.
     * 
     * @param whisker instance of main class
     */
    public MediaSearchingService(Whisker whisker){
        this.whisker = whisker;
        this.service = new Service();
        this.deep = 0;
        this.searchTime = 5;
        this.timeUnit = TimeUnit.MINUTES;
    }

    /**
     * Starts the service, in a new thread, if not started already.
     * 
     * exception Runtime Exception if already running
     */
    public void startService(){
        if(service.isAlive()) throw new RuntimeException("MediaSearchingService is already running.");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(service);
    }

    /**
     * Kills the service and stops searching.
     * 
     * exception Runtime Exception if it isn't running
     */
    public void killService(){
        if(!service.isAlive()) throw new RuntimeException("MediaSearchingService isn't running.");
        service.kill();
    }

    private class Service implements Runnable {

        private boolean alive = false;

        @Override
        public void run() {
            this.alive = true;

            while(alive){
                try {     
                    for(Drive drive : whisker.getUSBMountingService().getMountedDrives()){
                        List<Path> paths = goDeeper(drive.getPath(), new ArrayList<>(), deep);
                        whisker.getLibraryManager().processRawPaths(paths);
                    }

                    Thread.sleep(timeUnit.toMillis(searchTime));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {

                }
            }
        }

        public boolean isAlive(){
            return this.alive;
        }

        public void kill(){
            this.alive = false;
        }

        private List<Path> goDeeper(Path root, List<Path> list, int deepLeft) throws IOException {
            --deepLeft;
            DirectoryStream<Path> ds = Files.newDirectoryStream(root);
            for(Path path : ds){
                if(Files.isDirectory(path)){
                    goDeeper(path, list, deepLeft);
                } else if(Files.isRegularFile(path)) {
                    if(processFile(path)){
                        list.add(path);
                    }
                } else {
                    //Nothing.
                }
            }
            return list;
        }

        private boolean processFile(Path path){
            if(!getFileExtension(path).equalsIgnoreCase("mp4")){
                return false;
            }

            return true;
        }

        private String getFileExtension(Path path){
            String rt = "";

            String fileName = path.getFileName().toString();

            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                rt = fileName.substring(i+1);
            }

            return rt;
        }


    }
}
