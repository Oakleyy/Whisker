package ninja.oakley.whisker.media;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import ninja.oakley.whisker.Whisker;
import ninja.oakley.whisker.hardware.USBMountingService.Drive;

public class MediaSearchingService {

    private final Whisker whisker;
    
    private final Service service = new Service();
    private final int deep;

    public MediaSearchingService(Whisker whisker, int deep){
        this.whisker = whisker;
        this.deep = deep;
    }

    public MediaSearchingService(Whisker whisker){
        this.whisker = whisker;
        this.deep = 0;
    }

    private class Service implements Runnable{

        @Override
        public void run() {
            try {
                for(Drive drive : whisker.getUSBMountingService().getMountedDrives()){
                    goDeeper(drive.getPath(), deep);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        
        private void goDeeper(Path root, int deepLeft) throws IOException {
            --deepLeft;
            DirectoryStream<Path> ds = Files.newDirectoryStream(root);
            for(Path path : ds){
                if(Files.isDirectory(path)){
                    goDeeper(path, deepLeft);
                } else if(Files.isRegularFile(path)) {
                    processFile(path);
                } else {
                    //Nothing.
                }
            }
        }
        
        private void processFile(Path path){
            if(!getFileExtension(path).equalsIgnoreCase("mp4")){
                return;
            }
            
            
            
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
