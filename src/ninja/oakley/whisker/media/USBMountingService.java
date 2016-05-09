package ninja.oakley.whisker.media;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class USBMountingService {

    
    private final Map<String, Drive> mounted;
    private final Service service;

    public USBMountingService(){
        this.service = new Service();
        this.mounted = new HashMap<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(service);
    }

    private void mount(Drive drive){
        
    }
    
    private List<String> requestData() throws IOException{
        ProcessBuilder builder = new ProcessBuilder("blkid");
        builder.redirectErrorStream(true);
        
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        List<String> rt = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            rt.add(line);
            line = reader.readLine();
        }
        
        process.destroy();
        reader.close();

        return rt;
    }

    private class Drive {

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
                rt.put(d[0].toUpperCase().trim(), d[1].substring(1, d[1].length()));

            }

            return rt;
        }


    }

    private class Service implements Runnable {

        private boolean alive = true;
        
        @Override
        public void run() {
            while(alive)
            try {
                List<String> data = requestData();
                
                for(String st : data){
                    Drive d = new Drive(st);
                    if(!mounted.containsKey(d.getUniqueId())){
                        
                    }
                }
                
            } catch (IOException e) {
                
                //TODO logging
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
