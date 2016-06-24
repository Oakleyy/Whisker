package ninja.oakley.whisker.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ninja.oakley.whisker.Whisker;

public class USBMountingService {

    private static final Logger logger = LogManager.getLogger(Whisker.class);

    private final Whisker whisker;

    private final Map<String, Drive> mounted;
    private final Service service;

    public USBMountingService(Whisker whisker){
        this.whisker = whisker;
        this.service = new Service();
        this.mounted = new HashMap<>();
    }

    public List<Drive> getMountedDrives(){
        return new ArrayList<>(mounted.values());
    }

    public Drive getDrive(String uuid){
        return mounted.get(uuid);
    }

    public void startService(){
        if(service.isAlive()) throw new RuntimeException("USBMountingService is already running.");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(service);
    }

    public void killService(){
        if(!service.isAlive()) throw new RuntimeException("USBMountingService isn't running.");
        service.kill();
    }

    private synchronized void mount(Drive drive) throws IOException {
        StringBuilder sb = new StringBuilder("mount");
        sb.append(" -o uid=").append(whisker.getConfiguration().getSystemUser());
        sb.append(" -o gid=").append(whisker.getConfiguration().getSystemGroup());
        sb.append(" -t exfat ");
        sb.append(drive.getPartition());
        sb.append(" /mnt/").append(drive.getUniqueId());
        
        Path p = Paths.get("/mnt/" + drive.getUniqueId());
        if(!Files.isDirectory(p)){
            Files.createDirectory(p);
        }

        ProcessBuilder builder = new ProcessBuilder(sb.toString());
        builder.redirectErrorStream(true);

        Process process = builder.start();
        List<String> rt = readInputStream(process.getInputStream(), true); 

        if(!rt.isEmpty()){
            throw new RuntimeException("Error with mounting drive: " + Arrays.toString(rt.toArray()));
        }

        mounted.put(drive.getUniqueId(), drive);
    }

    private synchronized void umount(Drive drive) throws IOException {
        StringBuilder sb = new StringBuilder("umount");
        sb.append(" /mnt/").append(drive.getUniqueId());

        ProcessBuilder builder = new ProcessBuilder(sb.toString());
        builder.redirectErrorStream(true);

        Process process = builder.start();
        List<String> rt = readInputStream(process.getInputStream(), true); 

        if(!rt.isEmpty()){
            throw new RuntimeException("Error with unmounting drive: " + Arrays.toString(rt.toArray()));
        }

        mounted.remove(drive.getUniqueId());
    }

    private List<String> requestData() throws IOException{
        ProcessBuilder builder = new ProcessBuilder("blkid");
        builder.redirectErrorStream(true);

        Process process = builder.start();

        List<String> rt = readInputStream(process.getInputStream(), true); 
        process.destroy();

        return rt;
    }

    private List<String> readInputStream(InputStream stream, boolean close) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> rt = new ArrayList<>();

        String line = reader.readLine();
        while (line != null) {
            rt.add(line);
            line = reader.readLine();
        }

        if(close){
            reader.close();
        }
        return rt;
    }

    public class Drive {

        private final String partition;
        private final Map<String, String> data;

        private Drive(String data){
            String[] m = data.split(":");
            this.partition = m[0].trim();
            this.data = parseData(m[1].trim());
        }

        public String getPartition(){
            return this.partition;
        }

        public Path getPath(){
            return Paths.get("/mnt/" + getUniqueId());
        }

        public String getUniqueId(){
            String id = data.get("UUID");
            if(id == null || id.isEmpty()){
                throw new RuntimeException("No id found for drive mounted at " + partition);
            }

            return id;
        }

        public String getEntry(String entry){
            return data.get(entry.toUpperCase().trim());
        }

        private Map<String, String> parseData(String st){
            String[] data = st.split(" ");

            Map<String, String> rt = new HashMap<>();
            for(String s : data){
                String[] d = s.split("=");
                rt.put(d[0].toUpperCase().trim(), d[1].substring(1, d[1].length() - 1));
            }
            return rt;
        }
    }

    private class Service implements Runnable {

        private boolean alive = false;

        @Override
        public void run() {
            this.alive = true;

            while(alive)
                try {
                    List<String> data = requestData();
                    Map<String, Drive> drives = new HashMap<>();

                    for(String st : data){
                        Drive d = new Drive(st);
                        if(d.getEntry("TYPE") != null && d.getEntry("TYPE").equalsIgnoreCase("exfat")){
                            drives.put(d.getUniqueId(), d);
                        }
                    }

                    for(String st : mounted.keySet()){
                        if(!mounted.containsKey(st)){
                            umount(mounted.get(st));
                        }
                    }

                    for(String st : drives.keySet()){
                        if(!mounted.containsKey(st)){
                            mount(drives.get(st));
                        }
                    }

                    Thread.sleep(30000);
                } catch (IOException e) {
                    kill();
                    logger.error("USBMountingService has stopped due to an error: " + e.getMessage());
                } catch (InterruptedException e) {
                    logger.info("USBMountingService has stopped");
                }
        }

        public boolean isAlive(){
            return alive;
        }

        public void kill(){
            this.alive = false;
        }
    }
}